package borschik.partify.room.model.server_side;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

import borschik.partify.room.Room;
import borschik.partify.room.view_model.utils.ErrorListener;

class DnsService {
    private static final String TAG = "DnsService";
    //This class makes our Room discoverable as a service via DNS-SD
    //  for further reading see https://developer.android.com/training/connect-devices-wirelessly
    private NsdManager nsdManager;
    private String serviceName;

    DnsService(Context context) {
        this.nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    public void register(String name, ErrorListener errorListener, LocalRoomProvider.RegistrationSignatureListener registrationListener) {
        // This method registers the partify service and makes it discoverable by partify clients
        //  it receives a room name via arguments and return Room.Signature via callback
        serviceName = name;

        NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
        nsdServiceInfo.setServiceName(name);
        nsdServiceInfo.setServiceType(Room.SERVICE_TYPE);

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert serverSocket != null : "ServerSocket build failure";
        int availablePort = serverSocket.getLocalPort();
        nsdServiceInfo.setPort(availablePort);

        nsdManager.registerService(
                nsdServiceInfo,
                NsdManager.PROTOCOL_DNS_SD,
                new NsdManager.RegistrationListener() {
                    @Override
                    public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                        Log.d(TAG, "NsdManager onRegistrationFailed() listener " + errorCode);
                        errorListener.onError(errorCode);
                    }

                    @Override
                    public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                        Log.d(TAG, "NsdManager onUnregistrationFailed() listener");
                        errorListener.onError(errorCode);
                    }

                    @Override
                    public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                        Log.d(TAG, "NsdManager onServiceRegistered() listener");
                        // in case user's name is occupied and android changed it
                        serviceName = serviceInfo.getServiceName();

                        if (serviceInfo.getServiceName() == null) Log.d(TAG, "serviceInfo.getServiceName() is null ");
                        if (serviceInfo.getHost() == null) Log.d(TAG, "serviceInfo.getHost() is null ");

                        Room.Signature signature = new Room.Signature(
                                serviceName,
                                serviceInfo.getHost(),
                                serviceInfo.getPort()
                        );
                        registrationListener.onServiceRegistered(signature);
                    }

                    @Override
                    public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                        Log.d(TAG, "NsdManager onServiceUnregistered() listener");
                    }
                }
        );
        Log.d(TAG, "DnsService register() ");
    }

    public void unregister() {
        //This method unregisters the partify service
        //TODO develop
    }
}
