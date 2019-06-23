package edu.coursera.distributed;

/**
 * Representation of a file node in the virtual filesystem.
 */
public final class PCDPFile extends PCDPFilesystemComponent {
    /**
     * Contents of the represented file.
     */
    private final String contents;

    /**
     * Construct a file with the given name and contents.
     *
     * @param setName Name of the file
     * @param setContents Contents of the file
     */
    public PCDPFile(final String setName, final String setContents) {
        super(setName);
        contents = setContents;
    }

    /**
     * Fetch the file contents.
     *
     * @return The contents of the represented file
     */
    public String read() {
        return contents;
    }
}
