package gendevs.com.googlesign_indemo.db;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Akash on 09/02/16.
 */
public class DbAdapter {
    public static final String dbFileName = "database.db";
    private static String dbDirectoryPath = "database";
    private static String fullDbPath;

    public static String createDbFile(String dbPath) {
        fullDbPath = dbPath;
        String fqFilename = null;
        try {
            File localDirectoryPath = getLocalDirectoryPath(fullDbPath);
            createDbDirectory(getFullQualifiedDirectoryName(localDirectoryPath));
            fqFilename = getFullyQualifiedDbFilename(localDirectoryPath);
            if (new File(fqFilename).exists())
                return fqFilename;
            createTheDbFile(fqFilename);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return fqFilename;
    }

    private static String getFullyQualifiedDbFilename(File localDirectoryPath) {
        return String.format("%s/%s/%s", localDirectoryPath, dbDirectoryPath,
                dbFileName);
    }

    private static String getFullQualifiedDirectoryName(File localDirectoryPath) {
        return String.format("%s/%s", localDirectoryPath, dbDirectoryPath);
    }

    /**
     * This method should only be used by test classes.
     */
    public static void deleteDbFile() {
        try {
            File file = new File(
                    getFullyQualifiedDbFilename(getLocalDirectoryPath(fullDbPath)));
            boolean result = file.delete();
            System.out.println(String.format("Was file *%s* deleted: %s",
                    file == null ? "null" : file.toString(),
                    String.valueOf(result)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createDbDirectory(String fqDirectoryName) {
        File dbDirectory = new File(fqDirectoryName);
        boolean result = false;
        if (!dbDirectory.exists())
            result = dbDirectory.mkdirs();
        System.out.println(String.format("Directory named *%s* created: %s",
                fqDirectoryName, String.valueOf(result)));
        return;
    }

    private static void createTheDbFile(String fqFilename)
            throws FileNotFoundException {
            new File(fqFilename);
    }

    private static File getLocalDirectoryPath(String dbPath) throws Exception {
        // System.getProperty("user.dir");
        return new File(dbPath);
    }
}

