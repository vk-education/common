package borschik.partify.room.model.client_side;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import androidx.annotation.NonNull;

import borschik.partify.PartifyApplication;
import borschik.partify.room.Room;

public class RoomServiceDiscovery {
    //RoomInfoDiscovery uses a DNS-SD to discover and resolve information about nearby partify rooms
    //  for further reading see https://developer.android.com/training/connect-devices-wirelessly
    public static final int FAILURE_INVALID_NSD_SERVICE_INFO = -1;

    private NsdManager nsdManager;

    public RoomServiceDiscovery(Context context) {
        this.nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    @NonNull
    public static RoomServiceDiscovery getInstance(Context context) {
        return PartifyApplication.from(context).getRoomServiceDiscovery();
    }

    public void startDiscover(NsdManager.DiscoveryListener discoveryListener) {
        nsdManager.discoverServices(
                Room.SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    public void stopDiscover(NsdManager.DiscoveryListener discoveryListener) {
        nsdManager.stopServiceDiscovery(discoveryListener);
    }

    public interface ResolveListener {
        void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode);
        void onServiceResolved(Room.Signature roomSignature);
    }

    public void resolve(NsdServiceInfo serviceInfo, ResolveListener resolveListener) {
        nsdManager.resolveService(serviceInfo, new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                resolveListener.onResolveFailed(serviceInfo, errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                if (!serviceInfo.getServiceType().equals(borschik.partify.room.Room.SERVICE_TYPE)) {
                    resolveListener.onResolveFailed(serviceInfo, FAILURE_INVALID_NSD_SERVICE_INFO);
                    return;
                }

                Room.Signature roomSignature = new Room.Signature(serviceInfo.getServiceName(),
                        serviceInfo.getHost(), serviceInfo.getPort());

                resolveListener.onServiceResolved(roomSignature);
            }
        });
    }
}
