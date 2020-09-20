package borschik.partify.room.model.server_side;

import androidx.lifecycle.LiveData;

import borschik.partify.room.Room;

class GrpcServer {
    //This class is used to serve users via GRPC
    //  for further reading see https://developer.android.com/guide/topics/connectivity/grpc
    //Instances should listen on give host and port (see signature) and subscribe on roomLiveData
    GrpcServer(Room.Signature signature, LiveData<Room> roomLiveData) {
        //TODO develop
    }
}
