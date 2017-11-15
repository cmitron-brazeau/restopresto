package cgodin.qc.ca.myapplication;

import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleApiClientHelper
{
    private GoogleApiClient googleApiClient;

    public GoogleApiClientHelper(GoogleApiClient googleApiClient){
        this.googleApiClient = googleApiClient;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }
}
