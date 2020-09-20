package borschik.partify.room.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import borschik.partify.room.Room;
import borschik.partify.room.model.RoomProvider;
import borschik.partify.room.model.server_side.LocalRoomProvider;
import borschik.partify.room.view_model.utils.ErrorListener;

public class LocalRoomViewModel extends AndroidViewModel {
    private static final String TAG = "DnsService";

    // Binding between room and UI
    private RoomProvider roomProvider;
    private ErrorListener errorListener;

    public LocalRoomViewModel(@NonNull Application application, ErrorListener errorListener,
                         String roomName) {
        //Constructor for creating RoomViewModel with LocalRoomProvider
        super(application);
        Log.d(TAG, "LocalRoomViewModel() constructor");
        this.errorListener = errorListener;
        roomProvider = new LocalRoomProvider(roomName, errorListener);
    }

    public LiveData<Room> getRoom() {
        return roomProvider.getRoom();
    }
}
