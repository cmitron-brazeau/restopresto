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

import java.util.ArrayList;

import cgodin.qc.ca.myapplication.restaurant.Restaurant;
import cgodin.qc.ca.myapplication.user.User;

public class ConnectedNavigation extends AppCompatActivity implements FragmentRestaurants.OnFragmentInteractionListener,
        FragmentGoogleMap.OnFragmentInteractionListener, FragmentProfil.OnFragmentInteractionListener,
        FragmentDetailResto.OnFragmentInteractionListener, FragmentVide.OnFragmentInteractionListener{

    public static final String USER_ID = "userId";
    public static final String PREF_CONNECTED_USERID = "prefConnectedUserId";
    // Requête API
    public static final String API_KEY = "key=AIzaSyCB8_OeW81M0sm5G17c-bjNR8qZ6ulB3aE"; //always last param
    public static final String LNG_EN = "language=en&";
    public static final String LNG_FR = "language=fr&";
    // Nearby
    public static final String URL_PLACE_NEARBY = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?{parameters}";
    public static final String TYPE = "type=restaurant&";
    public static final String OPENNOW = "opennow&";
    public static final String LOCATION = "location=-33.8670,151.1957&";
    public static final String RADIUS = "radius=500&";
    public static final String KEYWORD = "keyword={search}&";
    // Detail
    public static final String URL_PLACE_DETAIL = "https://maps.googleapis.com/maps/api/place/details/json?{parameters}";
    public static final String PLACE_ID = "placeid={placeId}&";
    // Photo
    public static final String URL_PLACE_PHOTO = "https://maps.googleapis.com/maps/api/place/photo?{parameters}";
    public static final String MAXHEIGHT = "maxheight={maxheight}&";
    public static final String PHOTOREFERENCE = "photoreference={photoreference}&";

    Fragment fragment = new Fragment();
    FragmentRestaurants fragmentRestaurants;
    ArrayList<Restaurant> mRestaurants;

    SQLite sql;
    int userId = 0;
    User connectedUser;
    Button btnRestaurants, btnFavoris, btnProfil;
    FrameLayout frameContainer, frameDetail;
    Intent intent;
    boolean blnTwoPane, blnEnableTwoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_navigation);

        sql = new SQLite(ConnectedNavigation.this);
        intent = getIntent();
        userId = intent.getIntExtra(USER_ID, 0);
        connectedUser = sql.getUser(userId);

        frameContainer = (FrameLayout)findViewById(R.id.frameContainer);

        if (findViewById(R.id.frameDetail) != null) {
            blnTwoPane = true;
            frameDetail = (FrameLayout) findViewById(R.id.frameDetail);
        }
        else {
            blnTwoPane = false;
        }

        if (savedInstanceState == null){
            // initialement
            fragmentRestaurants = new FragmentRestaurants();
            changerFragment(frameContainer, fragmentRestaurants, false);
        }


        btnRestaurants =(Button)findViewById(R.id.btnRestaurants);
        btnRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentRestaurants = new FragmentRestaurants();
                changerFragment(frameContainer, fragmentRestaurants, false);
                if (blnTwoPane){
                    FragmentVide fragmentVide = new FragmentVide();
                    changerFragment(frameDetail, fragmentVide, false);
                }
            }
        });

        btnFavoris = (Button)findViewById(R.id.btnFavoris);
        btnFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment= new FragmentGoogleMap();
                changerFragment(frameContainer, fragment, false);
                if (blnTwoPane){
                    FragmentVide fragmentVide = new FragmentVide();
                    changerFragment(frameDetail, fragmentVide, false);
                }
            }
        });

        btnProfil = (Button)findViewById(R.id.btnProfil);
        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new FragmentProfil();
                changerFragment(frameContainer, fragment, false);
                if (blnTwoPane){
                    FragmentVide fragmentVide = new FragmentVide();
                    changerFragment(frameDetail, fragmentVide, false);
                }
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

        Log.i("SAVE", String.valueOf(userId));
        Log.i("SAVE", String.valueOf(userId));
        Log.i("SAVE", String.valueOf(userId));
        Log.i("SAVE", String.valueOf(userId));
        Log.i("SAVE", String.valueOf(userId));
        Log.i("SAVE", String.valueOf(userId));
        Log.i("SAVE", String.valueOf(userId));
        Log.i("SAVE", String.valueOf(userId));
        Log.i("SAVE", String.valueOf(userId));
        Log.i("SAVE", String.valueOf(userId));
    }

    public void changerFragment(FrameLayout container, Fragment frag, Boolean back) {
        if (back){
            getSupportFragmentManager().beginTransaction().replace(container.getId(), frag).addToBackStack(null).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(container.getId(), frag).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentSignOut() {
        Intent myintent = new Intent(ConnectedNavigation.this, MainActivity.class);
        ConnectedNavigation.this.startActivity(myintent);
    }
}
