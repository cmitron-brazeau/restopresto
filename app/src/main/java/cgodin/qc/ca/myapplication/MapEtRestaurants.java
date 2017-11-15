package cgodin.qc.ca.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import android.preference.PreferenceManager;

import cgodin.qc.ca.myapplication.user.User;

public class MapEtRestaurants extends AppCompatActivity {

    public static final String USER_ID = "userId";
    public static final String PREF_CONNECTED_USERID = "prefConnectedUserId";
    SQLite sql;
    TextView tvTitle;
    int userId = 0;
    User connectedUser;
    Button btnSign_out;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_et_restaurants);

        sql = new SQLite(this);
        intent = getIntent();

        userId = intent.getIntExtra(USER_ID, 0);
        connectedUser = sql.getUser(userId);

        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(connectedUser.getUserName());

        btnSign_out = (Button)findViewById(R.id.btnSign_out);
        btnSign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                userId = -1;
                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(MapEtRestaurants.this).edit();
                prefs.putInt(PREF_CONNECTED_USERID, userId);
                prefs.apply();
                prefs.commit();
                Intent myIntent = new Intent(MapEtRestaurants.this, MainActivity.class);
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
}
