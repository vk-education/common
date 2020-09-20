package borschik.partify.room.model.client_side;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import borschik.partify.room.Room;
import borschik.partify.room.model.RoomProvider;

public class RemoteRoomProvider implements RoomProvider {
    //RemoteRoomProvider is responsible for updating the room's state via GrpcClient
    private MutableLiveData<Room> roomMutableLiveData = new MutableLiveData<>();
    private GrpcClient grpcClient;

    public RemoteRoomProvider(Room.Signature roomSignature) {
        roomMutableLiveData.postValue(new Room(roomSignature, null));
        grpcClient = new GrpcClient(roomSignature);
    }

    @Override
    @NonNull
    public LiveData<Room> getRoom() {
        return roomMutableLiveData;
    }
}
