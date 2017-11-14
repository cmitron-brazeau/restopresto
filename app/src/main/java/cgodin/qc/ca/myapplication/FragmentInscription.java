package cgodin.qc.ca.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by o.aspirot on 2017-11-08.
 */

public class FragmentInscription extends Fragment {

    SQLite sql;
    private testInterface unInterface;

    public FragmentInscription() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sql = new SQLite(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragmentinscription,container,false);
        view.setBackgroundColor(Color.WHITE);


        final Button buttonSignIn = view.findViewById(R.id.btnSauverUtil);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText nomUtili = (EditText)view.findViewById(R.id.etNomUtil);
                EditText password1 = (EditText)view.findViewById(R.id.etPassword1);
                EditText password2 = (EditText)view.findViewById(R.id.etPassword2);
                EditText email = (EditText)view.findViewById(R.id.etEmail);

                boolean blnPret = false;
                String strNom = "";
                String strMotPasse = "";
                String strEmail = "";

                if(!(nomUtili.getText().toString().trim().equals(""))){
                    if((password1.getText().toString().equals(password2.getText().toString())) && !(password1.getText().toString().equals("") || password2.getText().toString().equals(""))){
                        if(!(email.getText().toString().trim().equals(""))){
                            boolean b = Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", email.getText().toString());
                            if(b){
                                blnPret = true;
                                strNom = nomUtili.getText().toString();
                                strMotPasse = password1.getText().toString();
                                strEmail = email.getText().toString();
                            }
                            else{
                                email.setError("entrer un adresse courriel");
                                email.requestFocus();
                            }
                        }
                        else{
                            email.setError("entrer un email");
                            email.requestFocus();
                        }
                    }
                    else{
                        if(password1.getText().toString().equals("")) {
                            password1.setError("le mot de passe est vide");
                            password1.requestFocus();
                        }
                        else if(password2.getText().toString().equals("")) {
                            password2.setError("le mot de passe est vide");
                            password2.requestFocus();
                        }
                        else if(!(password1.getText().toString().equals(password2.getText().toString()))) {
                            password2.setError("le mot de passe n'est pas valide");
                            password2.requestFocus();
                        }
                    }
                }
                else{
                    nomUtili.setError("le nom est vide");
                    nomUtili.requestFocus();
                }

                if(blnPret){
                    sql.insertPersonne(strNom, strMotPasse, strEmail);
                    Toast.makeText(getContext(), "utilisateur enregistrer avec succes", Toast.LENGTH_SHORT).show();
                    unInterface.changerFragment(new FragmentAcceuil(), false);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        unInterface = (testInterface)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unInterface = null;
    }
}