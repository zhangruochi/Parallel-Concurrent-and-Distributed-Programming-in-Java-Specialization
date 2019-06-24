package edu.coursera.distributed;

/**
 * Class for storing the absolute path to a file as separate path
 * components.
 */
public final class PCDPPath {
    /**
     * PCDPPath components.
     */
    private final String[] components;

    /**
     * Construct a path object from an absolute path that uses the '/'
     * separator.
     *
     * @param path String representation of the path to parse into a PCDPPath
     *             object.
     */
    public PCDPPath(final String path) {
        if (!path.startsWith("/")) {
            throw new RuntimeException("Only absolute paths supported, " +
                    "received path \"" + path + "\"");
        }

        components = path.substring(1).split("/");
    }

    /**
     * Return the length of this path in path components.
     *
     * @return Number of path components
     */
    public int getNComponents() {
        return components.length;
    }

    /**
     * Get a single component out of the whole path.
     *
     * @param index The index of the component to retrieve.
     * @return A component of this path
     */
    public String getComponent(final int index) {
        return components[index];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String component : components) {
            sb.append("/");
            sb.append(component);
        }
        return sb.toString();
    }
}
