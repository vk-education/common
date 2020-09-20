package borschik.partify.room.model.client_side;

import borschik.partify.room.Room;

class GrpcClient{
    //This class represents client for remote access to room
    //  for further reading see https://developer.android.com/guide/topics/connectivity/grpc
    private Room.Signature roomSignature;

    GrpcClient(Room.Signature roomSignature) {
        this.roomSignature = roomSignature;
    }

    private void connect() {
        //TODO connect to GRPC server with host and port provided by roomSignature
    }
}
