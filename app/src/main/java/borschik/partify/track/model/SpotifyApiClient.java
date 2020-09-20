package borschik.partify.track.model;

import android.content.Context;

import androidx.annotation.NonNull;

import borschik.partify.PartifyApplication;

public class SpotifyApiClient {
    //Class representing spotify web service
    //  here we should hold an HTTP connection to Spotify servers and call some api endpoints

    public SpotifyApiClient(Context context) {
        //TODO open connection?
    }

    @NonNull
    public static SpotifyApiClient getInstance(Context context) {
        return PartifyApplication.from(context).getSpotifyApiClient();
    }

    private void authorize() {
        //Here we should authorize on Spotify using App Authorization
        //  for further reading see https://developer.spotify.com/documentation/general/guides/authorization-guide/
        //TODO develop
    }

    public void search() {
        //Method for searching tracks on spotify
        //  we should pass here some listener and use it for querying some Spotify tracks
        //  for further reading see https://developer.spotify.com/documentation/web-api/reference/search/search/
        //TODO develop
    }
}
