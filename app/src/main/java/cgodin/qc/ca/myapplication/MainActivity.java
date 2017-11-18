package cgodin.qc.ca.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import android.preference.PreferenceManager;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import cgodin.qc.ca.myapplication.user.User;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    SQLite sql;
    GoogleApiClientHelper googleHelper;
    SignInButton googleSignInButton;
    LoginButton facebookSignInButton;
    CallbackManager callbackManager;
    EditText etCourriel;
    EditText etPassword;
    GoogleApiClient googleApiClient;
    public static final int REQ_CODE = 111;
    int connectedUserID = -1;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        Log.i("CONNECTEDUSER", String.valueOf(connectedUserID));
        if (connectedUserID > 0){
            Intent myintent = new Intent(MainActivity.this, ConnectedNavigation.class);
            myintent.putExtra(ConnectedNavigation.USER_ID, connectedUserID);
            MainActivity.this.startActivity(myintent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this).enableAutoManage(MainActivity.this, MainActivity.this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        setContentView(R.layout.activity_main);
        restaurer();

        sql = new SQLite(this);
        googleHelper = new GoogleApiClientHelper(googleApiClient);

        googleSignInButton = (SignInButton) findViewById(R.id.sign_in_google);
        googleSignInButton.setSize(SignInButton.SIZE_WIDE);
        for (int i = 0; i < googleSignInButton.getChildCount(); i++) {
            View v = googleSignInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setPadding(0, 0, 5, 0);
                break;
            }
        }
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(myIntent, REQ_CODE);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        facebookSignInButton = (LoginButton) findViewById(R.id.sign_in_facebook);
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        facebookSignInButton.setReadPermissions(permissions);
        facebookSignInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();



                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Get facebook data from login
                        int userID = -1;
                        String userEmail = "";
                        String userFirstName = "";
                        String userFacebookId = "";
                        try {
                            userEmail = object.getString("email");
                        } catch (Exception ex) {
                            userEmail = "null";
                        }

                        try {
                            userFacebookId = object.getString("id");
                        } catch (Exception ex) {
                            userFacebookId = "N/A";
                        }

                        try {
                            userFirstName = object.getString("first_name");
                        } catch (Exception ex) {
                            userFirstName = "N/A";
                        }

                        if (userFacebookId != "N/A") {
                            ArrayList<User> users = sql.getAllUsers();
                            for (int i = 0; i < users.size(); i++) {
                                if (users.get(i).getFacebookID().equals(userFacebookId)) {
                                    userID = users.get(i).getId();
                                }
                            }

                            if (userID > 0) {
                                User user = sql.getUser(userID);
                                Intent myIntent = new Intent(MainActivity.this, ConnectedNavigation.class);
                                myIntent.putExtra(ConnectedNavigation.USER_ID, user.getId());
                                MainActivity.this.startActivity(myIntent);
                            } else {
                                sql.insertUser(new User(userFirstName, "", userEmail, userFacebookId, "0"));
                                ArrayList<User> arrayUsers = sql.getAllUsers();
                                for (int i = 0; i < arrayUsers.size(); i++) {
                                    if (arrayUsers.get(i).getFacebookID().equals(userFacebookId)) {
                                        userID = arrayUsers.get(i).getId();
                                    }
                                }
                                Intent myIntent = new Intent(MainActivity.this, ConnectedNavigation.class);
                                myIntent.putExtra(ConnectedNavigation.USER_ID, userID);
                                MainActivity.this.startActivity(myIntent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.toastFacebookLoginError), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Bundle parameters = new Bundle();

                parameters.putString("fields", "id, first_name, email"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, getString(R.string.toastFacebookLoginError), Toast.LENGTH_SHORT).show();
            }
        });

        final Button buttonSignIn = (Button) findViewById(R.id.btnSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etCourriel = (EditText) findViewById(R.id.etCourriel);
                String strCourriel = etCourriel.getText().toString();
                etPassword = (EditText) findViewById(R.id.etPassword);
                String strPassword = etPassword.getText().toString();

                if (strCourriel.trim().equals("")) {
                    etCourriel.setError(getString(R.string.etCourrielVide));
                    etCourriel.requestFocus();
                } else {
                    if (strPassword.trim().equals("")) {
                        etPassword.setError(getString(R.string.etPasswordVide));
                        etPassword.requestFocus();
                    } else {
                        List<User> listUser = sql.getAllUsers();
                        int userID = -1;
                        for (int i = 0; i < listUser.size(); i++) {
                            if (strCourriel.equals(listUser.get(i).getEmail()) && strPassword.equals(listUser.get(i).getPassword())) {
                                userID = listUser.get(i).getId();
                            }
                        }
                        if (userID > 0) {
                            User user = sql.getUser(userID);
                            Intent myIntent = new Intent(MainActivity.this, ConnectedNavigation.class);
                            myIntent.putExtra(ConnectedNavigation.USER_ID, user.getId());
                            MainActivity.this.startActivity(myIntent);
                        } else {
                            etPassword.setError(getString(R.string.LoginInvalide));
                            etPassword.setText("");
                            etPassword.requestFocus();
                        }
                    }
                }
            }
        });

        final Button btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Inscription.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                String givenName = account.getGivenName();
                String email = account.getEmail();
                String googleId = account.getId();

                int userID = -1;

                ArrayList<User> users = sql.getAllUsers();
                for (int i = 0; i < users.size(); i++){
                    if (users.get(i).getGoogleID().equals(googleId)){
                        userID = users.get(i).getId();
                    }
                }

                Auth.GoogleSignInApi.signOut(googleApiClient);

                if (userID > 0){
                    User user = sql.getUser(userID);
                    Intent myIntent = new Intent(MainActivity.this, ConnectedNavigation.class);
                    myIntent.putExtra(ConnectedNavigation.USER_ID, user.getId());
                    MainActivity.this.startActivity(myIntent);
                }
                else {
                    sql.insertUser(new User(givenName, "", email, "0", googleId));
                    ArrayList<User> arrayUsers = sql.getAllUsers();
                    for (int i = 0; i < arrayUsers.size(); i++){
                        if (arrayUsers.get(i).getGoogleID().equals(googleId)){
                            userID = arrayUsers.get(i).getId();
                        }
                    }
                    Intent myIntent = new Intent(MainActivity.this, ConnectedNavigation.class);
                    myIntent.putExtra(ConnectedNavigation.USER_ID, userID);
                    MainActivity.this.startActivity(myIntent);
                }
            }
        }
    }

    public void restaurer(){
        // lecture du fichier de préférences.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        connectedUserID = prefs.getInt(ConnectedNavigation.PREF_CONNECTED_USERID, 0);
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
        Log.i("RESTORE", String.valueOf(connectedUserID));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
