package gendevs.com.googlesign_indemo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Locale;

import gendevs.com.googlesign_indemo.R;
import gendevs.com.googlesign_indemo.contstant.Constants;
import gendevs.com.googlesign_indemo.db.UserDbController;
import gendevs.com.googlesign_indemo.fragment.ChooseLangFragment;
import gendevs.com.googlesign_indemo.fragment.ChooseThemeFragment;
import gendevs.com.googlesign_indemo.model.User;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ChooseThemeFragment.ChooseListener
        , ChooseLangFragment.ChooseLangListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private String TAG = LoginActivity.class.getSimpleName();

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String DUMMY_USERANEM = "akash@gmail.com";
    private static final String DUMMY_PASSWORD = "123123";
    private static final String[] mThemeNames = {"Theme 1", "Theme 2", "Theme 3"};
    private static final String[] mLangNames = {"English", "Hindi"};
    private static final int RC_SIGN_IN = 200;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private int mThemeChoise = 0;
    private SignInButton mGoogleSignInbtn;
    private Button mEmailSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions signInOptions;
    private UserDbController userDbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setThemeForApplication();

        super.onCreate(savedInstanceState);

        setLangForApplication();
        setTitle(getString(R.string.mainactivityname));
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mGoogleSignInbtn = (SignInButton) findViewById(R.id.google_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);
        mGoogleSignInbtn.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        userDbController = new UserDbController(getApplicationContext());
        customizeGoogleSignInButton();

        removePrevAttachedDailogs();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getBooleanInPreference(Constants.LOGGED_IN_STATUS, false, this)) {
            Intent intent = new Intent(this, MainActivity.class);
            navigateToActivity(intent);
        }
    }


    private void customizeGoogleSignInButton() {

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
        mGoogleSignInbtn.setScopes(signInOptions.getScopeArray());

        for (int i = 0; i < mGoogleSignInbtn.getChildCount(); i++) {
            View v = mGoogleSignInbtn.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(42, 0, 0, 0);
                tv.setTextSize(17);
                return;
            }
        }
    }

    private void removePrevAttachedDailogs() {

        DialogFragment dialogThemeFragment = (DialogFragment) getFragmentManager().findFragmentByTag(ChooseThemeFragment.class.getSimpleName());
        if (dialogThemeFragment != null) {

            dialogThemeFragment.dismiss();
            dialogThemeFragment.dismissAllowingStateLoss();
        }
        DialogFragment dialogLangFragment = (DialogFragment) getFragmentManager().findFragmentByTag(ChooseLangFragment.class.getSimpleName());
        if (dialogLangFragment != null) {

            dialogLangFragment.dismiss();
            dialogLangFragment.dismissAllowingStateLoss();
        }
    }

    private void changeLangauge(String lang_code) {

        Locale locale = new Locale(lang_code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void setThemeForApplication() {

        mThemeChoise = getIntInPreference(Constants.THEMECODE, 0, getApplicationContext());
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

    private void setLangForApplication() {

        int langChoise = getIntInPreference(Constants.LANGCODE, 0, getApplicationContext());
        switch (langChoise) {
            case 0:
                changeLangauge("en");
                break;
            case 1:
                changeLangauge("hi");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_info:

                showLangOption();
                return true;

            case R.id.action_theme:

                int code = getIntInPreference(Constants.THEMECODE, 0, getApplicationContext());
                showThemeOption(mThemeNames, code);

                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    private void showLangOption() {

        int langcode = getIntInPreference(Constants.LANGCODE, 0, getApplicationContext());

        FragmentManager manager = getFragmentManager();

        ChooseLangFragment alert = new ChooseLangFragment();

        Bundle b = new Bundle();

        b.putInt(Constants.POSITION, langcode);
        b.putStringArray(Constants.CODE, mLangNames);

        alert.setArguments(b);

        alert.show(manager, ChooseLangFragment.class.getSimpleName());
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel_email = false;
        boolean cancel_password = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel_password = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel_email = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel_email = true;
        }

        if (cancel_email || cancel_password) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, this);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.equalsIgnoreCase(DUMMY_USERANEM);
    }

    private boolean isPasswordValid(String password) {

        if (password.length() > 4) {
            return password.equalsIgnoreCase(DUMMY_PASSWORD);
        } else {
            return false;
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.email_sign_in_button:
                attemptLogin();
                break;
            case R.id.google_sign_in_button:
                attemptLoginViaGoogle();
                break;
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private Context context;
        private User user;

        UserLoginTask(String email, String password, Context context) {
            mEmail = email;
            mPassword = password;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(2000);
                user = new User();
                user.setId("12312312003123"); // dummy id
                user.setmUserName(DUMMY_USERANEM);
                user.setmEmail(DUMMY_USERANEM);
                user.setmPassword(DUMMY_PASSWORD);
                saveStringInPreferences(Constants.LOGGED_IN_USER_ID, user.getId(), context);
                userDbController.saveUser(user);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                moveToMainActivity(user.getId());
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void moveToMainActivity(String id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.LOGGED_IN_USER_ID, id);
        navigateToActivity(intent);
    }

    private void showThemeOption(String[] data, int currentSeleted) {

        FragmentManager manager = getFragmentManager();

        ChooseThemeFragment alert = new ChooseThemeFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.POSITION, currentSeleted);
        bundle.putStringArray(Constants.CODE, data);

        alert.setArguments(bundle);
        alert.show(manager, ChooseThemeFragment.class.getSimpleName());
    }

    @Override
    public void onPositiveClick(int position, String code) {  //click handler for Choosing one of the bizcode
        showProgress(true);
        int currentSelected = getIntInPreference(Constants.LANGCODE, 0, getApplicationContext());
        if (currentSelected == position) {
            showProgress(false);
            return;
        }

        switch (position) {
            case 0:
                saveIntInPreference(Constants.THEMECODE, position, getApplicationContext());
                recreate();
                break;
            case 1:
                saveIntInPreference(Constants.THEMECODE, position, getApplicationContext());

                recreate();
                break;
            case 2:
                saveIntInPreference(Constants.THEMECODE, position, getApplicationContext());
                recreate();
                break;
        }

        showProgress(false);
    }

    @Override
    public void onSetClick(int position, String code) {
        showProgress(true);
        int currentSelected = getIntInPreference(Constants.LANGCODE, 0, getApplicationContext());
        if (currentSelected == position) {
            showProgress(false);
            return;
        }
        switch (position) {
            case 0:
                saveIntInPreference(Constants.LANGCODE, position, getApplicationContext());
                recreate();
                break;
            case 1:
                saveIntInPreference(Constants.LANGCODE, position, getApplicationContext());
                recreate();
                break;
        }
    }

    @Override
    public void onCancelClick() {
        showProgress(false);
    }


    @Override
    public void onNegativeClick() {
        showProgress(false);
    }

    public void navigateToActivity(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                startActivity(intent);
                finish();
            }
        } else {
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showLongSnackBar(Constants.ERROR_CONNECTING);
    }

    private void attemptLoginViaGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            showShortSnackBar(Constants.LOGIN_SUCCESS);
            User user = new User();
            user.setId(acct.getId());
            user.setmUserName(acct.getDisplayName());
            user.setmEmail(acct.getEmail());
            Uri uri = acct.getPhotoUrl();
            if (uri != null)
                user.setmUserPic(uri.toString());

            userDbController.saveUser(user);
            saveStringInPreferences(Constants.LOGGED_IN_USER_ID, user.getId(), this);
            moveToMainActivity(user.getId());

        } else {
            Log.d(TAG, "handleSignInResult:" + result.isSuccess() + result.getStatus());
            showLongSnackBar(Constants.TRY_AGIN);
        }
    }


}

