package borschik.partify.room.model.server_side;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import borschik.partify.PartifyApplication;
import borschik.partify.room.Room;
import borschik.partify.room.model.RoomProvider;
import borschik.partify.room.view_model.utils.ErrorListener;

public class LocalRoomProvider implements RoomProvider {
    private static final String TAG = "DnsService";
    //LocalRoomProvider is responsible for hosting the room:
    //  - room must be discoverable via DNS-SD
    //  - room must serve GRPC requests via GrpcServer
    //  - room must update it's state via SpotifyRemoteClient
    private MutableLiveData<Room> roomMutableLiveData = new MutableLiveData<>();
    private DnsService dnsService;
    private GrpcServer grpcServer;
    private SpotifyRemoteClient spotifyRemoteClient;

    public interface RegistrationSignatureListener {
        public void onServiceRegistered(Room.Signature signature);
    }

    public  LocalRoomProvider(String roomName, ErrorListener errorListener) {
        //TODO connect to Spotify via Spotify Remote using spotifyRemoteClient
        //TODO start grpc server
        //make room discoverable via dnsService
        // TODO solve context problem (currently getting from static property of PartifyApplication) !
        Log.d(TAG, "LocalRoomProvider() constructor");
        dnsService = new DnsService(PartifyApplication.getAppContext());
        dnsService.register(roomName, errorListener,
                signature -> {
                    // get Room.Signature from dnsService, build Room.State, build Room and post value to roomMutableLiveData
                    Log.d(TAG, "onServiceRegistered() callback ");
                    roomMutableLiveData.postValue(new Room(
                            signature,
                            new Room.State()
                    ));
                });
    }

    @Override
    @NonNull
    public LiveData<Room> getRoom() {
        return roomMutableLiveData;
    }
}
