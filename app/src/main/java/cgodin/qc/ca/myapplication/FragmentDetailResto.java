package cgodin.qc.ca.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cgodin.qc.ca.myapplication.restaurant.Restaurant;
import cgodin.qc.ca.myapplication.user.User;

public class FragmentDetailResto extends Fragment {

    public static final String ARG_USER_ID = "userId";
    public static final String ARG_PLACE_ID = "placeId";
    public static final String ARG_TWO_PANE = "twoPane";

    SQLite sql;
    int userId;
    String placeId;
    User connectedUser;
    Boolean blnTwoPane;

    TextView tvName, tvRating, tvMondayHours, tvTuesdayHours, tvWednesdayHours, tvThursdayHours, tvFridayHours, tvSaturdayHours,
            tvSundayHours, tvPhoneCall, tvPhoneNumber, tvTexto, tvWebsite, tvAddress;
    ImageView imgPhoto;
    RatingBar ratingBar;
    Button btnAddFavorite;

    private OnFragmentInteractionListener mListener;

    public FragmentDetailResto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sql = new SQLite(getContext());
        userId = getArguments().getInt(ARG_USER_ID);
        placeId = getArguments().getString(ARG_PLACE_ID);
        blnTwoPane = getArguments().getBoolean(ARG_TWO_PANE);
        connectedUser = sql.getUser(userId);

        View view = inflater.inflate(R.layout.fragment_fragment_detail_resto, container, false);

        if (blnTwoPane) {
            // change fragment width to 50% of the screen
            Point size = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(size);
            int width = size.x;
            view.getLayoutParams().width = width / 2;
        }

        final Restaurant mRestaurant =  mListener.onFragmentShowsRestaurantDetail(placeId);
        ArrayList<Restaurant> favPlaces = sql.getAllFavoritePlaces();
        for (int i = 0; i < favPlaces.size(); i++){
            if (mRestaurant.getPlaceId().equals(favPlaces.get(i).getPlaceId())){
                mRestaurant.setFavorite(true);
            }
        }

        tvName = (TextView)view.findViewById(R.id.tvName);
        tvName.setText(mRestaurant.getName());

        imgPhoto = (ImageView)view.findViewById(R.id.imgPhoto);
        imgPhoto.setImageBitmap(Bitmap.createScaledBitmap(mRestaurant.getPhoto(), 400, 400, false));

        ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        ratingBar.setRating(mRestaurant.getRating());

        tvRating = (TextView)view.findViewById(R.id.tvRating);
        tvRating.setText(String.valueOf(mRestaurant.getRating()));

        tvMondayHours = (TextView)view.findViewById(R.id.tvMondayHours);
        tvTuesdayHours = (TextView)view.findViewById(R.id.tvTuesdayHours);
        tvWednesdayHours = (TextView)view.findViewById(R.id.tvWednesdayHours);
        tvThursdayHours = (TextView)view.findViewById(R.id.tvThursdayHours);
        tvFridayHours = (TextView)view.findViewById(R.id.tvFridayHours);
        tvSaturdayHours = (TextView)view.findViewById(R.id.tvSaturdayHours);
        tvSundayHours = (TextView)view.findViewById(R.id.tvSundayHours);
        tvMondayHours.setText(mRestaurant.getOpenHours().getMonday());
        tvTuesdayHours.setText(mRestaurant.getOpenHours().getTuesday());
        tvWednesdayHours.setText(mRestaurant.getOpenHours().getWednesday());
        tvThursdayHours.setText(mRestaurant.getOpenHours().getThursday());
        tvFridayHours.setText(mRestaurant.getOpenHours().getFriday());
        tvSaturdayHours.setText(mRestaurant.getOpenHours().getSaturday());
        tvSundayHours.setText(mRestaurant.getOpenHours().getSunday());

        tvPhoneCall = (TextView)view.findViewById(R.id.tvPhoneCall);
        tvPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = mRestaurant.getPhoneNumber().replace("(", "").replace(")", "").replace(" ", "");
                Uri call = Uri.parse("tel:" + number);
                Intent surf = new Intent(Intent.ACTION_DIAL, call);
                startActivity(surf);
            }
        });

        tvPhoneNumber = (TextView)view.findViewById(R.id.tvPhoneNumber);
        tvPhoneNumber.setPaintFlags(tvPhoneNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvPhoneNumber.setText(mRestaurant.getPhoneNumber());
        tvPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = mRestaurant.getPhoneNumber().replace("(", "").replace(")", "").replace(" ", "");
                Uri call = Uri.parse("tel:" + number);
                Intent surf = new Intent(Intent.ACTION_DIAL, call);
                startActivity(surf);
            }
        });

        tvTexto = (TextView)view.findViewById(R.id.tvTexto);
        tvTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = mRestaurant.getPhoneNumber().replace("(", "").replace(")", "").replace(" ", "");
                Intent surf = new Intent(Intent.ACTION_VIEW);
                surf.setData(Uri.parse("sms:" + number));
                startActivity(surf);
            }
        });

        tvWebsite = (TextView)view.findViewById(R.id.tvWebsite);
        tvWebsite.setText(mRestaurant.getWebsite());

        tvAddress = (TextView)view.findViewById(R.id.tvAddress);
        tvAddress.setText(mRestaurant.getAddress());

        btnAddFavorite = (Button)view.findViewById(R.id.btnAddFavorite);
        if (mRestaurant.getFavorite()){
            btnAddFavorite.setText(getString(R.string.btnRemoveFavorite));
        }
        btnAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRestaurant.getFavorite()){
                    sql.deleteFavoritePlace(mRestaurant.getPlaceId());
                }
                else {
                    sql.insertFavoritePlace(userId, mRestaurant);
                }
                mRestaurant.setFavorite(!mRestaurant.getFavorite());
                btnAddFavorite.setText((mRestaurant.getFavorite() ? getString(R.string.btnRemoveFavorite) :
                        getString(R.string.btnAddFavorite)));
            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
        Restaurant onFragmentShowsRestaurantDetail(String placeId);
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
