package cgodin.qc.ca.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.FloatProperty;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cgodin.qc.ca.myapplication.restaurant.Location;
import cgodin.qc.ca.myapplication.restaurant.OpenHours;
import cgodin.qc.ca.myapplication.restaurant.Restaurant;
import cgodin.qc.ca.myapplication.user.User;

public class FragmentRestaurants extends Fragment {
    ConnectedNavigation activity;

    // Requête API
    public static final String API_KEY = "key=AIzaSyCB8_OeW81M0sm5G17c-bjNR8qZ6ulB3aE"; //always last param
    public static final String LNG_EN = "language=en&";
    public static final String LNG_FR = "language=fr&";
    // Nearby
    public static final String URL_PLACE_NEARBY = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?{parameters}";
    public static final String TYPE = "type=restaurant&";
    public static final String LOCATION = "location=-33.8670,151.1957&";
    public static final String RADIUS = "radius={radius}&";
    public static final String KEYWORD = "keyword={keyword}&";
    // Detail
    public static final String URL_PLACE_DETAIL = "https://maps.googleapis.com/maps/api/place/details/json?{parameters}";
    public static final String PLACE_ID = "placeid={placeId}&";
    // Photo
    public static final String URL_PLACE_PHOTO = "https://maps.googleapis.com/maps/api/place/photo?{parameters}";
    public static final String MAXHEIGHT = "maxheight={maxheight}&";
    public static final String PHOTOREFERENCE = "photoreference={photoreference}&";

    private OnFragmentInteractionListener mListener;

    public FragmentRestaurants() {
        // Required empty public constructor
    }

    ProgressBar loadingProgress;
    Button btnSearch;
    EditText etKeyword, etRadius;
    ArrayList<Bitmap> mBitmaps;
    ArrayList<String> mPlacesPhotoUrl;
    TextView tvDonnees;
    ArrayList<Restaurant> backupRestaurants;


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerAdapterRestaurant mAdapter;

    private AsyncTask mTaskNearby, mTaskDetail, mTaskPhoto;

    @Override
    public void onCreate(Bundle save)
    {
        super.onCreate(save);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = (ConnectedNavigation)getActivity();

        View view = (View) inflater.inflate(R.layout.fragment_fragment_restaurants, container, false);

        loadingProgress = (ProgressBar) view.findViewById(R.id.loadingProgress);
        etKeyword = (EditText)view.findViewById(R.id.etKeyword);
        etRadius = (EditText)view.findViewById(R.id.etRadius);
        tvDonnees = (TextView)view.findViewById(R.id.tvDonnees);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.restaurantRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        btnSearch = (Button)view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRadius.getText().equals("")){
                    etRadius.setError(getString(R.string.etRadiusVide));
                    etRadius.requestFocus();
                }
                else if (Integer.valueOf(etRadius.getText().toString()) > 50000){
                    etRadius.setError(getString(R.string.etRadiusInvalide));
                    etRadius.requestFocus();
                }
                else {
                    String urlRequest = URL_PLACE_NEARBY.replace("{parameters}", TYPE + LOCATION +
                            RADIUS.replace("{radius}", etRadius.getText()) + KEYWORD.replace("{keyword}", etKeyword.getText()) +
                            (Locale.getDefault().getLanguage().equals("fr") ? LNG_FR : LNG_EN) + API_KEY);
                    mTaskNearby = new getWebFluxJSONNearbyPlaces().execute(urlRequest);
                }
            }
        });
        if (backupRestaurants != null){
            setRestaurantRecyclerAdapter(backupRestaurants);
        }
        else if (activity.mRestaurants != null) {
            setRestaurantRecyclerAdapter(activity.mRestaurants);
        }
        else {
            String urlRequest = URL_PLACE_NEARBY.replace("{parameters}", TYPE + LOCATION +
                    RADIUS.replace("{radius}", etRadius.getText()) +
                    (Locale.getDefault().getLanguage().equals("fr") ? LNG_FR : LNG_EN) + API_KEY);
            mTaskNearby = new getWebFluxJSONNearbyPlaces().execute(urlRequest);
        }

        return view;
    }

    private class getWebFluxJSONNearbyPlaces extends AsyncTask<String, Integer, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            btnSearch.setEnabled(false);
            etKeyword.setEnabled(false);
            etRadius.setEnabled(false);
            loadingProgress.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
            loadingProgress.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                int totalBytes = connection.getContentLength();
                int currentBytes = 0;
                while ((line = reader.readLine()) != null) {
                    currentBytes = line.getBytes("UTF-8").length;
                    buffer.append(line + "\n");
                    publishProgress(currentBytes * 100 / totalBytes);
                }
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            loadingProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            btnSearch.setEnabled(true);
            etKeyword.setEnabled(true);
            etRadius.setEnabled(true);
            loadingProgress.setVisibility(View.GONE);

            JSONObject webFluxJSON = new JSONObject();
            try {
                webFluxJSON = new JSONObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            lireJSONNearbyPlaces(webFluxJSON);
        }
    }

    public void lireJSONNearbyPlaces(JSONObject webFluxJSON) {
        ArrayList<String> mPlacesId = new ArrayList<>();
        mPlacesPhotoUrl = new ArrayList<>();

        JSONArray resultsJSONArray = new JSONArray();
        JSONObject resultJSON;
        String status = "";
        try {
            resultsJSONArray = webFluxJSON.getJSONArray("results");
            status = webFluxJSON.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status.equals("OK")){
            for (int i = 0; i < resultsJSONArray.length(); i++){
                try {
                    resultJSON = resultsJSONArray.getJSONObject(i);
                    mPlacesId.add(resultJSON.getString("place_id"));
                    mPlacesPhotoUrl.add(resultJSON.getJSONArray("photos").getJSONObject(0).getString("photo_reference"));
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
            mTaskDetail = new getWebFluxJSONPlacesDetail().execute(mPlacesId);
        }
        else {
            tvDonnees.setText(getString(R.string.tvDonneesNoData) + etKeyword.getText() + getString(R.string.tvDonnees2) + " " +
                    etRadius.getText() + " " + getString(R.string.tvDonnees3));
            activity.mRestaurants = new ArrayList<>();
            setRestaurantRecyclerAdapter(activity.mRestaurants);
        }
    }

    private class getWebFluxJSONPlacesDetail extends AsyncTask<ArrayList<String>, Integer, ArrayList<String>> {

        protected void onPreExecute() {
            super.onPreExecute();
            btnSearch.setEnabled(false);
            etKeyword.setEnabled(false);
            etRadius.setEnabled(false);
            loadingProgress.setProgressTintList(ColorStateList.valueOf(Color.RED));
            loadingProgress.setVisibility(View.VISIBLE);
        }

        protected ArrayList<String> doInBackground(ArrayList<String>... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            ArrayList<String> arrayWebFluxJSONDetailPlaces = new ArrayList<>();
            int nbPlaces = params[0].size();

            for (int i = 0; i < nbPlaces; i++) {

                try {

                    String urlRequest = URL_PLACE_DETAIL.replace("{parameters}", PLACE_ID.replace("{placeId}", params[0].get(i)) +
                            (Locale.getDefault().getLanguage().equals("fr") ? LNG_FR : LNG_EN) + API_KEY);
                    URL url = new URL(urlRequest);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream stream = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    arrayWebFluxJSONDetailPlaces.add(buffer.toString());
                    publishProgress(i * 100 / nbPlaces);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return arrayWebFluxJSONDetailPlaces;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            loadingProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            btnSearch.setEnabled(true);
            etKeyword.setEnabled(true);
            etRadius.setEnabled(true);
            loadingProgress.setVisibility(View.GONE);

            lireJSONDetailPlaces(result);
        }
    }

    public void lireJSONDetailPlaces(ArrayList<String> arrayWebFluxJSON) {
        activity.mRestaurants = new ArrayList<>();

        for (int i = 0; i < arrayWebFluxJSON.size(); i++) {

            JSONObject webFluxJSON;
            JSONObject resultJSON;
            String address = "";
            String phoneNumber = "";
            JSONArray arrayJSONWeekdayText;
            OpenHours openHours = new OpenHours("0", "1", "2", "3", "4", "5", "6");
            String placeId = "";
            Float rating = Float.valueOf(0);
            String website = "";
            JSONObject locationJSON;
            Location location = new Location(0.0, 0.0);
            String name = "";
            try {
                webFluxJSON = new JSONObject(arrayWebFluxJSON.get(i));
                resultJSON = webFluxJSON.getJSONObject("result");
                address = resultJSON.getString("formatted_address");
                phoneNumber = resultJSON.getString("formatted_phone_number");
                arrayJSONWeekdayText = resultJSON.getJSONObject("opening_hours").getJSONArray("weekday_text");
                openHours = new OpenHours(arrayJSONWeekdayText.getString(0), arrayJSONWeekdayText.getString(1), arrayJSONWeekdayText.getString(2),
                        arrayJSONWeekdayText.getString(3), arrayJSONWeekdayText.getString(4), arrayJSONWeekdayText.getString(5), arrayJSONWeekdayText.getString(6));
                placeId = resultJSON.getString("place_id");
                rating = Float.valueOf(String.valueOf(resultJSON.getDouble("rating")));
                website = resultJSON.getString("website");
                locationJSON = resultJSON.getJSONObject("geometry").getJSONObject("location");
                location = new Location(locationJSON.getDouble("lat"), locationJSON.getDouble("lng"));
                name = resultJSON.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            activity.mRestaurants.add(new Restaurant(placeId, location, name, address, phoneNumber, openHours, rating, website));
        }
        mTaskPhoto = new getWebPhotoPlaces().execute(mPlacesPhotoUrl);
    }

    private class getWebPhotoPlaces extends AsyncTask<ArrayList<String>, Integer, ArrayList<Bitmap>> {

        protected void onPreExecute() {
            super.onPreExecute();
            btnSearch.setEnabled(false);
            etKeyword.setEnabled(false);
            etRadius.setEnabled(false);

            loadingProgress.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            loadingProgress.setVisibility(View.VISIBLE);
        }

        protected ArrayList<Bitmap> doInBackground(ArrayList<String>... params) {

            Bitmap bitmap;
            Integer nbImages = params[0].size();
            mBitmaps = new ArrayList<>();

            for (int i = 0; i < nbImages; i++) {
                try {
                    String urlRequest = URL_PLACE_PHOTO.replace("{parameters}", MAXHEIGHT.replace("{maxheight}", "150") +
                            PHOTOREFERENCE.replace("{photoreference}", params[0].get(i)) +
                            (Locale.getDefault().getLanguage().equals("fr") ? LNG_FR : LNG_EN) + API_KEY);
                    bitmap = Picasso.with(getContext()).load(urlRequest).resize(150, 150).centerCrop().get();
                    mBitmaps.add(bitmap);
                    publishProgress(i * 100 / nbImages);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return mBitmaps;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            loadingProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> mBitmaps) {
            super.onPostExecute(mBitmaps);
            btnSearch.setEnabled(true);
            etKeyword.setEnabled(true);
            etRadius.setEnabled(true);
            loadingProgress.setVisibility(View.GONE);

            tvDonnees.setText(getString(R.string.tvDonnees) + etKeyword.getText() + getString(R.string.tvDonnees2) + " " +
                etRadius.getText() + " " + getString(R.string.tvDonnees3));
            setRestaurantBitmaps();
        }
    }

    public void setRestaurantBitmaps(){
        for (int i = 0; i < mBitmaps.size(); i++) {
            activity.mRestaurants.get(i).setPhoto(mBitmaps.get(i));
        }
        setRestaurantRecyclerAdapter(activity.mRestaurants);
    }

    private void setRestaurantRecyclerAdapter(ArrayList<Restaurant> restaurants){
        backupRestaurants = restaurants;
        activity.mRestaurants = restaurants;
        mAdapter = new RecyclerAdapterRestaurant(restaurants);
        mRecyclerView.setAdapter(mAdapter);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if(mTaskNearby != null && mTaskNearby.getStatus() == AsyncTask.Status.RUNNING)
            mTaskNearby.cancel(true);
        if(mTaskDetail != null && mTaskDetail.getStatus() == AsyncTask.Status.RUNNING)
            mTaskDetail.cancel(true);
        if(mTaskPhoto != null && mTaskPhoto.getStatus() == AsyncTask.Status.RUNNING)
            mTaskPhoto.cancel(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
