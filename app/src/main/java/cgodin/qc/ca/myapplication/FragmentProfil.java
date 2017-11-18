package cgodin.qc.ca.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import cgodin.qc.ca.myapplication.user.User;

public class FragmentProfil extends Fragment {

    ConnectedNavigation activity;

    public static final String PREF_CONNECTED_USERID = "prefConnectedUserId";

    Button btnSign_out, btnDeleteUser;
    TextView tvUserName, tvEmail;

    private OnFragmentInteractionListener mListener;

    public FragmentProfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = (ConnectedNavigation)getActivity();

        View view = (View)inflater.inflate(R.layout.fragment_fragment_profil, container, false);

        tvUserName = (TextView)view.findViewById(R.id.tvUsername);
        tvEmail = (TextView)view.findViewById(R.id.tvEmail);
        tvUserName.setText(activity.connectedUser.getUserName());
        tvEmail.setText(activity.connectedUser.getEmail());

        btnDeleteUser = (Button)view.findViewById(R.id.btnDeleteUser);
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                activity.sql.deleteUser(activity.userId);
                activity.sql.deleteAllFavoritePlaces(activity.userId);
                activity.userId = -1;
                mListener.onFragmentSignOut();
            }
        });

        btnSign_out = (Button)view.findViewById(R.id.btnSign_out);
        btnSign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                activity.userId = -1;
                mListener.onFragmentSignOut();
            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentSignOut();
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
