package cgodin.qc.ca.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cgodin.qc.ca.myapplication.user.User;

public class Favoris extends AppCompatActivity implements FragmentFavoris.OnFragmentInteractionListener {

    public static final String USER_ID = "userId";
    public static final String PREF_CONNECTED_USERID = "prefConnectedUserId";
    Intent intent;
    SQLite sql;
    int userId = 0;
    User connectedUser;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        intent = getIntent();
        sql = new SQLite(this);

        userId = intent.getIntExtra(USER_ID, 0);
        connectedUser = sql.getUser(userId);

        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(connectedUser.getUserName());
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
    public void onFragmentInteraction(Uri uri) {

    }
}
