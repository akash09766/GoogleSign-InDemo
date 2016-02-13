package gendevs.com.googlesign_indemo.db;

import android.content.Context;

/**
 * Created by Akash on 09/02/16.
 */
public class DbController {

    private static final String TAG = DbController.class.getSimpleName();

    protected DbStoreHelper dbStoreHelper;
    protected DbService dbService;

    protected Context mContext;
    private static final String DATABASE_NAME = "events.db40";

    public DbController(Context context) {
        mContext = context;
        dbStoreHelper = DbStoreHelper.getInstance(getDefaultDbPath());
        dbService = dbStoreHelper.getDbService();
        dbService.setActivationDepth(5);

    }

    public String getDefaultDbPath() {
        return String
                .format("%s/%s", mContext.getDir("data", 0), DATABASE_NAME);
    }
}
