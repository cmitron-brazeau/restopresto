package cgodin.qc.ca.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.SignInButton;

import java.util.List;

import cgodin.qc.ca.myapplication.personne.Personne;

public class FragmentAcceuil extends Fragment{

    SQLite sql;
    private testInterface unInterface;

    public FragmentAcceuil() {
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
        FacebookSdk.sdkInitialize(getContext());
        final View view = inflater.inflate(R.layout.fragmentacceuil, container, false);
        view.setBackgroundColor(Color.WHITE);

        SignInButton signInButton = view.findViewById(R.id.sign_in_google);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setPadding(0, 0, 5, 0);
                break;
            }
        }
        //TODO google sign-in onClickListener


        final Button buttonSignIn = view.findViewById(R.id.btnSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText nom = (EditText)view.findViewById(R.id.etUtilisateur);
                String strNom = nom.getText().toString();
                EditText password = (EditText)view.findViewById(R.id.etPassword);
                String strPassword = password.getText().toString();

                if(strNom.trim().equals("")){
                    nom.setError("le nom doit être écrit");
                    nom.requestFocus();
                }
                else{
                    if(strPassword.trim().equals("")){
                        password.setError("le mot de passe doit être écrit");
                        password.requestFocus();
                    }
                    else{
                        List<Personne> listPersonne = sql.getAllPersonnes();
                        for(int i = 0; i < listPersonne.size(); i++){
                            if(strNom.equals(listPersonne.get(i).firstName)){
                                Toast.makeText(getContext(), "utilisateur entrer avec succes", Toast.LENGTH_SHORT).show();
                                unInterface.changerFragment(new FragmentPrincipal(), true);
                            }
                            else{
                                Toast.makeText(getContext(), "utilisateur non existant", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        final Button buttonSignUp = view.findViewById(R.id.btnSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unInterface.changerFragment(new FragmentInscription(), true);
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
