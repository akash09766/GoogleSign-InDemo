package gendevs.com.googlesign_indemo.model;

import android.net.Uri;

import gendevs.com.googlesign_indemo.db.DbEntity;

/**
 * Created by Akash on 13/01/16.
 */
public class User implements DbEntity<Object> {

    public String id;
    public String mUserName;
    public String mPassword;
    public String mEmail;
    public String mUserPic;

    public User() {

    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmUserPic() {
        return mUserPic;
    }

    public void setmUserPic(String mUserPic) {
        this.mUserPic = mUserPic;
    }

}
