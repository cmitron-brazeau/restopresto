package cgodin.qc.ca.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import cgodin.qc.ca.myapplication.user.User;

public class ConnectedNavigation extends AppCompatActivity implements FragmentRestaurants.OnFragmentInteractionListener,
    FragmentGoogleMap.OnFragmentInteractionListener, FragmentProfil.OnFragmentInteractionListener{

    public static final String USER_ID = "userId";
    public static final String PREF_CONNECTED_USERID = "prefConnectedUserId";
    SQLite sql;
    int userId = 0;
    User connectedUser;
    Button btnRestaurants, btnFavoris, btnProfil;
    FrameLayout frameContainer;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_navigation);

        intent = getIntent();
        userId = intent.getIntExtra(USER_ID, 0);

        frameContainer = (FrameLayout)findViewById(R.id.frameContainer);

        // initialement
        Bundle arguments = new Bundle();
        arguments.putInt(FragmentRestaurants.ARG_USER_ID, userId);
        FragmentRestaurants fragmentRestaurants = new FragmentRestaurants();
        fragmentRestaurants.setArguments(arguments);
        changerFragment(fragmentRestaurants);

        btnRestaurants =(Button)findViewById(R.id.btnRestaurants);
        btnRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle arguments = new Bundle();
                arguments.putInt(FragmentRestaurants.ARG_USER_ID, userId);
                FragmentRestaurants fragmentRestaurants = new FragmentRestaurants();
                fragmentRestaurants.setArguments(arguments);
                changerFragment(fragmentRestaurants);
            }
        });

        btnFavoris = (Button)findViewById(R.id.btnFavoris);
        btnFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle arguments = new Bundle();
                arguments.putInt(FragmentGoogleMap.ARG_USER_ID, userId);
                FragmentGoogleMap fragmentGoogleMap = new FragmentGoogleMap();
                fragmentGoogleMap.setArguments(arguments);
                changerFragment(fragmentGoogleMap);
            }
        });

        btnProfil = (Button)findViewById(R.id.btnProfil);
        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle arguments = new Bundle();
                arguments.putInt(FragmentProfil.ARG_USER_ID, userId);
                FragmentProfil fragmentProfil = new FragmentProfil();
                fragmentProfil.setArguments(arguments);
                changerFragment(fragmentProfil);
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
        Log.i("SAVING_USERID","#" + userId);
        Log.i("SAVING_USERID","#" + userId);
        Log.i("SAVING_USERID","#" + userId);
        Log.i("SAVING_USERID","#" + userId);
        Log.i("SAVING_USERID","#" + userId);
        Log.i("SAVING_USERID","#" + userId);
        Log.i("SAVING_USERID","#" + userId);
        Log.i("SAVING_USERID","#" + userId);
        Log.i("SAVING_USERID","#" + userId);
        Log.i("SAVING_USERID","#" + userId);
    }

    @Override
    public void onBackPressed() {
        // disabled
    }

    public void changerFragment(Fragment frag) {
        frameContainer.removeAllViews();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, frag).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentSignOut(int userId) {
        this.userId = userId;
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }
}
