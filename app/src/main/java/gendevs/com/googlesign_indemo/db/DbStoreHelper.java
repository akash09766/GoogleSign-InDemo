package gendevs.com.googlesign_indemo.db;

/**
 * Created by Akash on 09/02/16.
 */
public class DbStoreHelper {

    private static DbStoreHelper dbStoreHelper;
    private static DbService dbService;

    public void closeDb() {
        if (dbService != null)
            dbService.closeDb();
    }

    public DbService getDbService() {
        return dbService;
    }

    public static DbStoreHelper getInstance(String path) {
        if (dbStoreHelper == null) {
            dbStoreHelper = new DbStoreHelper();
        }
        if (dbService == null)
            dbService = new DbService();
        dbService.setPath(path);
        return dbStoreHelper;
    }
}
