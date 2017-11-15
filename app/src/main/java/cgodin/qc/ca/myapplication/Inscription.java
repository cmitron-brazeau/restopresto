package cgodin.qc.ca.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

import cgodin.qc.ca.myapplication.user.User;

public class Inscription extends AppCompatActivity {

    SQLite sql;
    Button buttonSignIn;
    EditText etCourriel;
    EditText etPassword;
    EditText etConfirm;
    EditText etUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        sql = new SQLite(this);


        buttonSignIn = (Button)findViewById(R.id.btnEnregistrer);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etCourriel = (EditText)findViewById(R.id.etCourriel);
                etPassword = (EditText)findViewById(R.id.etPassword);
                etConfirm = (EditText)findViewById(R.id.etConfirmPassword);
                etUsername = (EditText)findViewById(R.id.etUserName);

                String strCourriel = "";
                String strMotPasse = "";
                String strUsername = "";

                if(!(etCourriel.getText().toString().trim().equals(""))){
                    boolean patterMatch = Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", etCourriel.getText().toString());
                    if(patterMatch){
                        boolean emailExists = false;
                        ArrayList<User> users =  sql.getAllUsers();
                        for (int i = 0; i < users.size(); i++){
                            if(etCourriel.getText().toString().toLowerCase().equals(users.get(i).getEmail().toLowerCase())){
                                emailExists = true;
                            }
                        }
                        if (!emailExists) {
                            if ((etPassword.getText().toString().equals(etConfirm.getText().toString())) && !(etPassword.getText().toString().equals("") || etConfirm.getText().toString().equals(""))) {
                                if (!(etUsername.getText().toString().trim().equals(""))) {
                                    strCourriel = etCourriel.getText().toString();
                                    strMotPasse = etPassword.getText().toString();
                                    strUsername = etUsername.getText().toString();
                                    User newUser = new User(strUsername, strMotPasse, strCourriel, "0");
                                    sql.insertUser(newUser);
                                    Toast.makeText(Inscription.this, newUser.getUserName() + " " + getString(R.string.toastAddUser), Toast.LENGTH_SHORT).show();
                                    Intent myIntent = new Intent(Inscription.this, MainActivity.class);
                                    Inscription.this.startActivity(myIntent);
                                } else {
                                    etUsername.setError(getString(R.string.etUsernameVide));
                                    etUsername.requestFocus();
                                }
                            } else {
                                if (etPassword.getText().toString().equals("")) {
                                    etPassword.setError(getString(R.string.etPasswordVide));
                                    etPassword.requestFocus();
                                } else if (etConfirm.getText().toString().equals("")) {
                                    etConfirm.setError(getString(R.string.etConfirmVide));
                                    etConfirm.requestFocus();
                                } else if (!(etPassword.getText().toString().equals(etConfirm.getText().toString()))) {
                                    etPassword.setError(getString(R.string.etPasswordInvalide));
                                    etPassword.setText("");
                                    etConfirm.setText("");
                                    etPassword.requestFocus();
                                }
                            }
                        }
                        else {
                            etCourriel.setError(getString(R.string.etCourrielExiste));
                            etCourriel.requestFocus();
                        }
                    }
                    else{
                        etCourriel.setError(getString(R.string.etCourrielInvalide));
                        etCourriel.requestFocus();
                    }
                }
                else{
                    etCourriel.setError(getString(R.string.etConfirmVide));
                    etCourriel.requestFocus();
                }
            }
        });
    }
}
