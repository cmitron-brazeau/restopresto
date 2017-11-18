package cgodin.qc.ca.myapplication.restaurant;

import android.graphics.Bitmap;

public class Restaurant {
    private String placeId;
    private String name;
    private String address;
    private Float rating;
    private Bitmap photo;
    private Location location;
    private String phoneNumber;
    private OpenHours openHours;
    private String website;
    private Boolean favorite;

    public Restaurant(Bitmap photo, String placeId, Location location, String name, String address, String phoneNumber, OpenHours openHours, Float rating, String website){
        this.photo = photo;
        this.placeId = placeId;
        this.location = location;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openHours = openHours;
        this.rating = rating;
        this.website = website;
        this.favorite = false;
    }

    public Restaurant(String placeId, Location location, String name, String address, String phoneNumber, OpenHours openHours, Float rating, String website){
        this.placeId = placeId;
        this.location = location;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openHours = openHours;
        this.rating = rating;
        this.website = website;
        this.favorite = false;
    }

    // GETTERS AND SETTERS
    public String getPlaceId() {
        return placeId;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public OpenHours getOpenHours() {
        return openHours;
    }

    public Float getRating() {
        return rating;
    }

    public String getWebsite() {
        return website;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
