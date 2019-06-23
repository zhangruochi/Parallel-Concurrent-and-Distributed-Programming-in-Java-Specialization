## Project Goals and Outcomes

This week we learned about communicating between Java Virtual Machines (JVMs) using sockets, and how high-level Java APIs make this process much simpler for programmers by offering stream-based communication APIs.

In this mini-project, you will gain experience working with Java Sockets by implementing a simple, stripped down file server that responds to HTTP requests by loading the contents of files and transmitting them to file server clients.

HTTP (or the Hypertext Transfer Protocol) was originally developed to support the communication of linked documents from early web servers to early web clients. It establishes a uniform protocol (or language) that all computers must be able to speak over the network in order to request HTTP documents. That protocol is based on a few fundamental commands, one of which is the GET command. Semantically, a GET request issued by an HTTP client to an HTTP server indicates that the client would like to retrieve a piece of read-only information from the server. For example, one common usage of GET is to request files from a file server by passing the path to the desired file. If a client were requesting a file called /path/to/file, the HTTP request would be a string starting with:

```bash
GET /path/to/file HTTP/1.1
```

This string indicates the command (GET), the resource requested (/path/to/file), and the version of the HTTP protocol being used (1.1). In general, many other lines would follow this line to form a complete HTTP request with other metadata such as information on the client performing the request.

An HTTP response to the above GET might look something like the following:

```bash
HTTP/1.0 200 OK\r\n
Server: FileServer\r\n
\r\n
FILE CONTENTS HERE\r\n
```

The first line of this response indicates that the file was found successfully. The second line identifies the type of server being spoken to. The third line serves as a separator between the HTTP header and the body of the request on the fourth line, the contents of the file itself. Note that the \r and \n are special-purpose new line characters required by the HTTP protocol.

f course, like any robust software system, HTTP must also integrate an error model that allows a server to indicate to clients when something has gone wrong. One of the most common HTTP error codes (and likely one that you have personally run into) is 404 Not Found. 404 Not Found indicates that the resource requested by a GET was not found on the server it was requested from. In the case of a file server, this would indicate that the client requested the contents of a file that did not exist on the server. An example 404 response is shown below:

```bash
HTTP/1.0 404 Not Found\r\n
Server: FileServer\r\n
\r\n
```

In this mini-project, you will implement a simple file server that responds to HTTP GET commands with the contents of the requested file. In the case of a client requesting a file that does not exist, you will return a 404 Not Found error instead.

## Project Setup

Please refer to Mini-Project 0 for a description of the build and testing process used in this course.

Once you have downloaded and unzipped the project files using the gray button labeled miniproject_10.zip at the top of this description, you should see the project source code file at

miniproject_10/src/main/java/edu/coursera/distributed/FileServer.java

and the project tests in

miniproject_10/src/test/java/edu/coursera/distributed/FileServerTest.java

It is recommended that you review the demo video for this week before starting this assignment, in which Professor Sarkar works through a similar example.

## Project Instructions

Your modifications should be made entirely inside of FileServer.java. You should not change the signatures of any public or protected methods inside of FileServer.java, but you can edit the method bodies and add any new methods that you choose. We will use our copy of FileServerTest.java in the final grading process, so do not change that file or any other file except FileServer .java.

Your main goals for this assignment are as follows:

Inside FileServer.java, implement the run method to use the provided ServerSocket object and root directory to implement the basic HTTP file server protocol described above. There are four helpful TODOs in FileServer.java to help guide your implementation.
You are provided with a proxy filesystem class called PCDPFilesystem which you will use to read files requested by file server clients. The only PCDPFilesystem API you need to be aware of is called readFile, which accepts a PCDPPath object representing the path to a file you would like the contents of. readFile returns the contents of the specified file if it exists. Otherwise, it returns null.

The PCDPPath object has a single constructor which accepts the absolute path to a file as a String. Hence, you can construct a PCDPPath object after parsing the requested file from an HTTP GET request.

## Project Evaluation

Your assignment submission should consist of only the FileServer.java file that you modified to implement this mini-project. As before, you can upload this file through the assignment page for this mini-project. After that, the Coursera autograder will take over and assess your submission, which includes building your code and running it on one or more tests. Please give it a few minutes to complete the grading. Note that for all assignments in this course you are free to resubmit as many times as you like. Once it has completed, you should see a score appear in the “Score” column of the “My submission” tab based on the following rubric:

- 10% - correctness of testTermination
- 10% - correctness of testFileGet
- 10% - correctness of testFileGets
- 10% - correctness of testNestedFileGet
- 20% - correctness of testDoublyNestedFileGet
- 10% - correctness of testLargeFileGet
- 10% - correctness of testMissingFileGet
- 20% - correctness of testMissingNestedFileGet