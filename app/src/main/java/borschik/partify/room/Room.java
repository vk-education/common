package borschik.partify.room;

import androidx.annotation.NonNull;

import java.net.InetAddress;

public class Room {
    //Room is responsible for storing information about playback, currently playing track, etc.
    //Room may be created or discovered by user
    public static final String SERVICE_TYPE = "_spartify._tcp.";

    private Signature signature;
    private State state;

    public Room(@NonNull Signature signature, State state) {
        this.signature = signature;
        this.state = state;
    }

    @NonNull
    public Signature getSignature() {
        return signature;
    }

    public State getState() {
        return state;
    }

    public static class Signature {
        //Info required to discover and connect to room
        private String name;
        private InetAddress host;
        private int port;

        public Signature(String name, InetAddress host, int port) {
            this.name = name;
            this.host = host;
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public InetAddress getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }

    public static class State {
        //Info about playback, currently playing track, etc.
    }
}
