package edu.coursera.distributed;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.ConnectException;
import java.nio.channels.ClosedByInterruptException;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import java.util.concurrent.ThreadLocalRandom;

import junit.framework.TestCase;

public class FileServerTest extends TestCase {
    private int port;
    private static final String rootDirName = "static";
    private static final File rootDir = new File(rootDirName);
    private static final Random rand = new Random();

    private static final Map<String, String> files = new HashMap<String, String>();

    private static String getRandomFileContents(final int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(Integer.toString(rand.nextInt(10)));
        }
        return sb.toString();
    }

    private static void deleteRecursively(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                deleteRecursively(c);
            }
        }

        if (f.exists()) {
            if (!f.delete()) {
                throw new FileNotFoundException("Failed to delete file: " + f);
            }
        }
    }

    static {
        files.put("/static/A.txt", getRandomFileContents(5));
        files.put("/static/B.txt", getRandomFileContents(10));
        files.put("/static/dir1/C.txt", getRandomFileContents(10));
        files.put("/static/dir3/dir4/E.txt", getRandomFileContents(10));
        files.put("/static/ABC.txt", getRandomFileContents(2048));
    }

    private PCDPFilesystem getFilesystem() {
        PCDPFilesystem fs = new PCDPFilesystem();

        for (Map.Entry<String, String> entry : files.entrySet()) {
            PCDPPath path = new PCDPPath(entry.getKey());
            fs.addFile(path, entry.getValue());
        }

        return fs;
    }

    private HttpServer launchServer() throws IOException {
        System.err.println("\nLaunching server for " +
                Thread.currentThread().getStackTrace()[2].getMethodName());
        port = ThreadLocalRandom.current().nextInt(3000, 9000);

        final ServerSocket socket = new ServerSocket(port);
        socket.setReuseAddress(true);
        final PCDPFilesystem fs = getFilesystem();
        
        Runnable runner = new Runnable () {
            @Override
            public void run() {
                try {
                    FileServer server = new FileServer();
                    server.run(socket, fs);
                } catch (SocketException s) {
                    // Do nothing, assume killed by main thread
                } catch (ClosedByInterruptException s) {
                    // Do nothing, assume killed by main thread
                } catch (IOException io) {
                    throw new RuntimeException(io);
                }
            }
        };

        Thread thread = new Thread(runner);

        thread.start();

        return new HttpServer(thread, socket);
    }

    private HttpResponse sendHttpRequest(final String path, final boolean print)
            throws IOException {
        assert !path.startsWith("/");

        if (print) {
            System.err.print("Requesting " + path + "... ");
        }

        URL obj = new URL("http://localhost:" + port + "/" + path);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000); // 5 seconds
        con.setReadTimeout(5000); // 5 seconds

        final int responseCode = con.getResponseCode();

        final String responseStr;
        if (responseCode != 404) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            responseStr = response.toString();
        } else {
            responseStr = "";
        }

        if (print) {
            System.err.println("reponse code is " + responseCode +
                    ", with content length " + responseStr.length());
        }

        return new HttpResponse(responseCode, responseStr);
    }

    public void testTermination() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/A.txt", true);
        assertEquals(200, response.code);
        assertEquals(files.get("/static/A.txt"), response.body);

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testFileGets() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/A.txt", true);
        assertEquals(200, response.code);
        assertEquals(files.get("/static/A.txt"), response.body);

        response = sendHttpRequest(rootDirName + "/B.txt", true);
        assertEquals(200, response.code);
        assertEquals(files.get("/static/B.txt"), response.body);

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testNestedFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/dir1/C.txt", true);
        assertEquals(200, response.code);
        assertEquals(files.get("/static/dir1/C.txt"), response.body);

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testDoublyNestedFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/dir3/dir4/E.txt", true);
        assertEquals(200, response.code);
        assertEquals(files.get("/static/dir3/dir4/E.txt"), response.body);

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testLargeFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/ABC.txt", true);
        assertEquals(200, response.code);
        assertEquals(files.get("/static/ABC.txt"), response.body);

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testMissingFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/missing.txt", true);
        assertEquals(404, response.code);

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    public void testMissingNestedFileGet() throws IOException, InterruptedException {
        final HttpServer server = launchServer();

        HttpResponse response = sendHttpRequest(rootDirName + "/missingdir/missing.txt", true);
        assertEquals(404, response.code);

        // Termination
        server.socket.close();
        server.thread.interrupt();
        server.thread.join();
    }

    class HttpResponse {
        public final int code;
        public final String body;

        public HttpResponse(final int setCode, final String setBody) {
            code = setCode;
            body = setBody;
        }
    }

    class HttpServer {
        public final Thread thread;
        public final ServerSocket socket;

        HttpServer(final Thread setThread, final ServerSocket setSocket) {
            thread = setThread;
            socket = setSocket;
        }
    }
}
