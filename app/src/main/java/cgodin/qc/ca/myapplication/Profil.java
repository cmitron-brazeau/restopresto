package cgodin.qc.ca.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.regex.Pattern;

import cgodin.qc.ca.myapplication.user.User;

public class Profil extends AppCompatActivity {

    public static final String USER_ID = "userId";
    public static final String PREF_CONNECTED_USERID = "prefConnectedUserId";
    Intent intent;
    SQLite sql;
    int userId = 0;
    User connectedUser;
    Button btnSign_out, btnSaveChanges;
    EditText etUserName, etEmail, etNewPassword, etConfirmNewPassword;
    TextView tvNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        intent = getIntent();
        sql = new SQLite(this);

        userId = intent.getIntExtra(USER_ID, 0);
        connectedUser = sql.getUser(userId);

        etUserName = (EditText)findViewById(R.id.etUserName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etNewPassword = (EditText)findViewById(R.id.etNewPassword);
        etConfirmNewPassword = (EditText)findViewById(R.id.etConfirmNewPassword);
        tvNewPassword = (TextView)findViewById(R.id.tvNewPassword);

        etUserName.setText(connectedUser.getUserName());
        etEmail.setText(connectedUser.getEmail());
        if (!connectedUser.getEmail().equals("")){
            etEmail.setEnabled(false);
        }
        if (connectedUser.getPassword().equals("")){
            tvNewPassword.setText(getString(R.string.tvAddNewPassword));
        }

        btnSaveChanges = (Button)findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCourriel = "";
                String strMotPasse = "";
                String strUsername = "";
                if(!(etEmail.getText().toString().trim().equals(""))){
                    boolean patterMatch = Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", etEmail.getText().toString());
                    if(patterMatch){
                        boolean emailExists = false;
                        ArrayList<User> users =  sql.getAllUsers();
                        for (int i = 0; i < users.size(); i++){
                            if(etEmail.getText().toString().toLowerCase().equals(users.get(i).getEmail().toLowerCase())){
                                emailExists = true;
                            }
                        }
                        if (!emailExists) {
                            if ((etNewPassword.getText().toString().equals(etConfirmNewPassword.getText().toString())) && !(etNewPassword.getText().toString().equals("") || etConfirmNewPassword.getText().toString().equals(""))) {
                                if (!(etUserName.getText().toString().trim().equals(""))) {
                                    strCourriel = etEmail.getText().toString();
                                    strMotPasse = etNewPassword.getText().toString();
                                    strUsername = etUserName.getText().toString();
                                    User user = new User(strUsername, strMotPasse, strCourriel, connectedUser.getFacebookID());
                                    tvNewPassword.setText(user.getUserName() + " | " + user.getPassword() + " | " + user.getEmail() + " | " + user.getFacebookID());

                                } else {
                                    etUserName.setError(getString(R.string.etUsernameVide));
                                    etUserName.requestFocus();
                                }
                            } else {
                                if (etNewPassword.getText().toString().equals("")) {
                                    etNewPassword.setError(getString(R.string.etPasswordVide));
                                    etNewPassword.requestFocus();
                                } else if (etConfirmNewPassword.getText().toString().equals("")) {
                                    etConfirmNewPassword.setError(getString(R.string.etConfirmVide));
                                    etConfirmNewPassword.requestFocus();
                                } else if (!(etNewPassword.getText().toString().equals(etConfirmNewPassword.getText().toString()))) {
                                    etNewPassword.setError(getString(R.string.etPasswordInvalide));
                                    etNewPassword.setText("");
                                    etConfirmNewPassword.setText("");
                                    etNewPassword.requestFocus();
                                }
                            }
                        }
                        else {
                            etEmail.setError(getString(R.string.etCourrielExiste));
                            etEmail.requestFocus();
                        }
                    }
                    else{
                        etEmail.setError(getString(R.string.etCourrielInvalide));
                        etEmail.requestFocus();
                    }
                }
                else{
                    etEmail.setError(getString(R.string.etCourrielVide));
                    etEmail.requestFocus();
                }
            }
        });

        btnSign_out = (Button)findViewById(R.id.btnSign_out);
        btnSign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                userId = -1;
                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(Profil.this).edit();
                prefs.putInt(PREF_CONNECTED_USERID, userId);
                prefs.apply();
                prefs.commit();
                Intent myIntent = new Intent(Profil.this, MainActivity.class);
                Profil.this.startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(this).edit();
        prefs.putInt(PREF_CONNECTED_USERID, userId);
        prefs.apply();
        prefs.commit();
    }
}
