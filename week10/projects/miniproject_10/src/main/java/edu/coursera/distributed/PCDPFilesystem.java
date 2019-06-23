package edu.coursera.distributed;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A class to represent a virtual, in-memory filesystem. PCDPFilesystem exposes
 * two user-visible APIs: one for adding a file to a given virtual filesystem
 * (addFile) and one for reading the contents of a file that already exists
 * (readFile).
 */
public class PCDPFilesystem {
    /**
     * The root folder for the virtual filesystem.
     */
    private final PCDPFolder root = new PCDPFolder("static");

    /**
     * Default constructor, creating an empty filesystem.
     */
    public PCDPFilesystem() {
    }

    /**
     * Add a file at the provided path with the provided contents.
     *
     * @param path PCDPPath to the new file to create
     * @param contents Contents of the new file
     */
    public void addFile(PCDPPath path, String contents) {
        assert path.getNComponents() > 0;

        assert path.getComponent(0).equals(root.getName());

        int componentIndex = 1;
        PCDPFolder curr = root;
        while (componentIndex < path.getNComponents()) {
            String component = path.getComponent(componentIndex++);
            PCDPFilesystemComponent next = curr.getChild(component);

            if (componentIndex < path.getNComponents()) {
                // Haven't reached bottom of path yet so must be folder
                if (next == null) {
                    PCDPFolder newFolder = new PCDPFolder(component);
                    curr.addChild(newFolder);
                    curr = newFolder;
                } else {
                    assert next instanceof PCDPFolder;
                    curr = (PCDPFolder)next;
                }
            } else {
                // Reached base filename
                assert next == null;
                PCDPFile newFile = new PCDPFile(component, contents);
                curr.addChild(newFile);
            }
        }
    }

    /**
     * Read the file specified by path.
     *
     * @param path The absolute path to the file to read.
     * @return The contents of the specified file, or null if the file does not
     *         seem to exist.
     */
    public String readFile(PCDPPath path) {
        if (path.getNComponents() == 0) {
            return null;
        }

        if (!path.getComponent(0).equals(root.getName())) {
            return null;
        }

        int componentIndex = 1;
        PCDPFilesystemComponent curr = root;
        while (componentIndex < path.getNComponents()) {
            final String nextComponent = path.getComponent(componentIndex++);

            if (curr == null || !(curr instanceof PCDPFolder)) {
                return null;
            }

            PCDPFilesystemComponent next = ((PCDPFolder)curr).getChild(
                    nextComponent);

            curr = next;
        }

        if (curr == null || !(curr instanceof PCDPFile)) {
            return null;
        } else {
            return ((PCDPFile)curr).read();
        }
    }
}
