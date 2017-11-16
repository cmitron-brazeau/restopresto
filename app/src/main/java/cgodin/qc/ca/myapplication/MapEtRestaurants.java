package cgodin.qc.ca.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import android.preference.PreferenceManager;

import cgodin.qc.ca.myapplication.user.User;

public class MapEtRestaurants extends AppCompatActivity implements FragmentGoogleMap.OnFragmentInteractionListener{

    public static final String USER_ID = "userId";
    public static final String PREF_CONNECTED_USERID = "prefConnectedUserId";
    SQLite sql;
    TextView tvTitle;
    int userId = 0;
    User connectedUser;
    Button btnProfil, btnFavoris;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_et_restaurants);

        intent = getIntent();
        sql = new SQLite(this);

        userId = intent.getIntExtra(USER_ID, 0);
        connectedUser = sql.getUser(userId);

        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(connectedUser.getUserName());

        btnProfil = (Button)findViewById(R.id.btnProfil);
        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MapEtRestaurants.this, Profil.class);
                myIntent.putExtra(Profil.USER_ID, userId);
                MapEtRestaurants.this.startActivity(myIntent);
            }
        });

        btnFavoris = (Button)findViewById(R.id.btnFavoris);
        btnFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MapEtRestaurants.this, Favoris.class);
                myIntent.putExtra(Favoris.USER_ID, userId);
                MapEtRestaurants.this.startActivity(myIntent);
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

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
