package borschik.partify.room.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import borschik.partify.room.Room;
import borschik.partify.room.model.RoomProvider;
import borschik.partify.room.model.client_side.RemoteRoomProvider;
import borschik.partify.room.model.server_side.LocalRoomProvider;
import borschik.partify.room.view_model.utils.ErrorListener;

public class RoomViewModel extends AndroidViewModel {
    // separated on two view models (local and remote), use them instead

    //Binding between room and UI
    private RoomProvider roomProvider;
    private ErrorListener errorListener;

    public RoomViewModel(@NonNull Application application, ErrorListener errorListener,
                         Room.Signature roomSignature) {
        //Constructor for creating RoomViewModel with RemoteRoomProvider
        super(application);
        this.errorListener = errorListener;
        roomProvider = new RemoteRoomProvider(roomSignature);
    }

    public RoomViewModel(@NonNull Application application, ErrorListener errorListener,
                         String roomName) {
        //Constructor for creating RoomViewModel with LocalRoomProvider
        super(application);
        this.errorListener = errorListener;
        roomProvider = new LocalRoomProvider(roomName, errorListener);
    }

    public LiveData<Room> getRoom() {
        return roomProvider.getRoom();
    }
}
