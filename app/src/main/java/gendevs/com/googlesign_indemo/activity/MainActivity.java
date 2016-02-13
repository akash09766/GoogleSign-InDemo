package gendevs.com.googlesign_indemo.activity;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;
import gendevs.com.googlesign_indemo.R;
import gendevs.com.googlesign_indemo.contstant.Constants;
import gendevs.com.googlesign_indemo.db.UserDbController;
import gendevs.com.googlesign_indemo.model.User;

public class MainActivity extends BaseActivity {

    private TextView mId, mName, mEmailId;
    private CircleImageView mUserLogo;
    private int mThemeChoise = 0;
    private User user;
    private String id;
    private UserDbController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setThemeForApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getResources().getString(R.string.title_activity_main));
        mUserLogo = (CircleImageView) findViewById(R.id.user_iv_id);
        mId = (TextView) findViewById(R.id.id_tv_value_id);
        mName = (TextView) findViewById(R.id.displayname_tv_value_id);
        mEmailId = (TextView) findViewById(R.id.email_tv_value_id);
        manageViews();
        setDataTo();

        saveBooleanInPreference(Constants.LOGGED_IN_STATUS, true, this);
    }

    private void manageViews() {

        id = getPreferencesString(Constants.LOGGED_IN_USER_ID, "", this);
        controller = new UserDbController(this);

        user = controller.getUser(id);

        if (user != null) {
            if (user.getId() != null) {
                findViewById(R.id.id_view).setVisibility(View.VISIBLE);
            }
            if (user.getmEmail() != null) {
                findViewById(R.id.email_view).setVisibility(View.VISIBLE);
            }
            if (user.getmUserName() != null) {
                findViewById(R.id.displayname_view).setVisibility(View.VISIBLE);
            }
        }

    }

    private void setDataTo() {

        if (user != null) {
            if (user.getId() != null) {
                mId.setText(user.getId());
            }
            if (user.getmEmail() != null) {
                mName.setText(user.getmUserName());
            }
            if (user.getmUserName() != null) {
                mEmailId.setText(user.getmEmail());
            }
            if (user.getmUserPic() != null) {
//                mUri.setText(user.getmUserPic().toString());
                ImageLoader.getInstance().displayImage(user.getmUserPic(),mUserLogo);
            }
        }
    }

    private void setThemeForApplication() {

        mThemeChoise = getIntInPreference(Constants.THEMECODE,0,getApplicationContext());
        switch (mThemeChoise) {
            case 0:
                setTheme(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.AppTheme_01);
                break;
            case 2:
                setTheme(R.style.AppTheme_02);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout_menu_id:

                saveBooleanInPreference(Constants.LOGGED_IN_STATUS, false, this);

                Intent intent = new Intent(this, LoginActivity.class);
                navigateToActivity(intent);

                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    public void navigateToActivity(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } catch (Exception e) {
                e.printStackTrace();
                startActivity(intent);
            }

        } else {
            startActivity(intent);
        }
    }
}
