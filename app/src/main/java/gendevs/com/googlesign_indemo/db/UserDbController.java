package gendevs.com.googlesign_indemo.db;

import android.content.Context;

import java.util.List;

import gendevs.com.googlesign_indemo.model.User;

/**
 * Created by Akash on 09/02/16.
 */
public class UserDbController extends DbController {

    private static final String TAG = UserDbController.class.getSimpleName();

    public UserDbController(Context context) {
        super(context);
        dbService.setActivationDepth(100);
    }

    public User getUser(String id) {
        return dbService.getResultById(User.class, "id", id);
    }

    public synchronized void saveUser(User user) {
        dbService.save(user);
    }

    public List<User> getAllUsers() {
        return dbService.getResults(User.class);
    }
}
