package borschik.partify.room.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import borschik.partify.room.Room;
import borschik.partify.room.model.RoomProvider;
import borschik.partify.room.model.client_side.RemoteRoomProvider;
import borschik.partify.room.view_model.utils.ErrorListener;


public class RemoteRoomViewModel extends AndroidViewModel {
    // Binding between room and UI
    private RoomProvider roomProvider;
    private ErrorListener errorListener;

    public RemoteRoomViewModel(@NonNull Application application, ErrorListener errorListener,
                         Room.Signature roomSignature) {
        // Constructor for creating RoomViewModel with RemoteRoomProvider
        super(application);
        this.errorListener = errorListener;
        roomProvider = new RemoteRoomProvider(roomSignature);
    }

    public LiveData<Room> getRoom() {
        return roomProvider.getRoom();
    }
}
