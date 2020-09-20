package borschik.partify;

import android.app.Application;
import android.content.Context;

import borschik.partify.room.model.client_side.RoomServiceDiscovery;
import borschik.partify.track.model.SpotifyApiClient;

public class PartifyApplication extends Application {
    private RoomServiceDiscovery roomServiceDiscovery;
    private SpotifyApiClient spotifyApiClient;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        roomServiceDiscovery = new RoomServiceDiscovery(getApplicationContext());
        spotifyApiClient = new SpotifyApiClient(getApplicationContext());
        this.context = getApplicationContext();
    }

    public RoomServiceDiscovery getRoomServiceDiscovery() {
        return roomServiceDiscovery;
    }

    public SpotifyApiClient getSpotifyApiClient() {
        return spotifyApiClient;
    }

    public static Context getAppContext() {
        return PartifyApplication.context;
    }

    public static PartifyApplication from(Context context) {
        return (PartifyApplication) context.getApplicationContext();
    }
}
