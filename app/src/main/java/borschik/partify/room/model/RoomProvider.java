package borschik.partify.room.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import borschik.partify.room.Room;

public interface RoomProvider {
    //Interface of a class, which know room's state
    @NonNull
    LiveData<Room> getRoom();
}
