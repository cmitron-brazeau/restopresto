package cgodin.qc.ca.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cgodin.qc.ca.myapplication.restaurant.Restaurant;
import cgodin.qc.ca.myapplication.user.User;

public class FragmentGoogleMap extends Fragment {

    public static final String USER_ID = "userId";
    public static final String PREF_CONNECTED_USERID = "prefConnectedUserId";
    SQLite sql;
    int userId = 0;
    User connectedUser;
    TextView tvTitre;

    public static final String ARG_USER_ID = "userId";

    private OnFragmentInteractionListener mListener;

    public FragmentGoogleMap() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sql = new SQLite(getContext());
        userId = getArguments().getInt(ARG_USER_ID);
        connectedUser = sql.getUser(userId);

        View view = inflater.inflate(R.layout.fragment_fragment_google_map, container, false);

        ArrayList<Restaurant> favPlaces = sql.getAllFavoritePlaces();

        tvTitre = (TextView)view.findViewById(R.id.tvTitre);

        for (int i = 0; i < favPlaces.size(); i++) {
            tvTitre.setText(tvTitre.getText() + "\n" + favPlaces.get(i).getName());
        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
