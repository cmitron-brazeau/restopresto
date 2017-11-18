package cgodin.qc.ca.myapplication;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cgodin.qc.ca.myapplication.restaurant.Restaurant;

public class RecyclerAdapterRestaurant extends RecyclerView.Adapter<RecyclerAdapterRestaurant.RestaurantHolder> {
    private ArrayList<Restaurant> mRestaurants;

    public static class RestaurantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgPhoto;
        private TextView tvName;
        private TextView tvRating;
        private RatingBar ratingBar;
        private TextView tvAddress;
        private Restaurant mRestaurant;

        public RestaurantHolder(View v) {
            super(v);
            imgPhoto = (ImageView) v.findViewById(R.id.imgPhoto);
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvRating = (TextView) v.findViewById(R.id.tvRating);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
            tvAddress = (TextView) v.findViewById(R.id.tvAddress);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            //Load fragment from recyclerView in fragment
            ConnectedNavigation connnectedActivity = (ConnectedNavigation) view.getContext();
            Bundle arguments = new Bundle();
            arguments.putInt(FragmentDetailResto.ARG_USER_ID, connnectedActivity.userId);
            arguments.putString(FragmentDetailResto.ARG_PLACE_ID, mRestaurant.getPlaceId());
            arguments.putBoolean(FragmentDetailResto.ARG_TWO_PANE, connnectedActivity.blnTwoPane);
            FragmentDetailResto frag = new FragmentDetailResto();
            frag.setArguments(arguments);
            if (connnectedActivity.blnTwoPane){
                connnectedActivity.changerFragment(connnectedActivity.frameDetail, frag, false);
            }
            else {
                connnectedActivity.changerFragment(connnectedActivity.frameContainer, frag, true);
            }
        }

        public void bindRestaurant(Restaurant Restaurant) {
            mRestaurant = Restaurant;
            if (Restaurant.getPhoto() != null)
                imgPhoto.setImageBitmap(Restaurant.getPhoto());
            tvName.setText(Restaurant.getName());
            tvRating.setText(String.valueOf(Restaurant.getRating()));
            ratingBar.setRating(Restaurant.getRating());
            tvAddress.setText(Restaurant.getAddress());
        }
    }

    public RecyclerAdapterRestaurant(ArrayList<Restaurant> Restaurants) {
        mRestaurants = Restaurants;
    }


    @Override
    public RecyclerAdapterRestaurant.RestaurantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_restaurant, parent, false);
        return new RestaurantHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RestaurantHolder holder, int position) {
        Restaurant itemRestaurant = mRestaurants.get(position);
        Log.i("Restau", itemRestaurant.getName() + " | " + itemRestaurant.getAddress() + " | " + String.valueOf(itemRestaurant.getRating()));
        Log.i("Restau", itemRestaurant.getName() + " | " + itemRestaurant.getAddress());
        Log.i("Restau", itemRestaurant.getName() + " | " + itemRestaurant.getAddress());
        if (!itemRestaurant.getName().equals("") && !itemRestaurant.getAddress().equals("") && itemRestaurant.getRating() > 0.0) {
            holder.bindRestaurant(itemRestaurant);
        }
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }
}
