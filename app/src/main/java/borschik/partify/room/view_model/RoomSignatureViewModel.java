package borschik.partify.room.view_model;

import android.app.Application;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import borschik.partify.room.Room;
import borschik.partify.room.model.client_side.RoomServiceDiscovery;
import borschik.partify.room.view_model.utils.ErrorListener;

public class RoomSignatureViewModel extends AndroidViewModel {
    private static final String TAG = "DnsService";

    //Binding between service discovery and UI
    private RoomServiceDiscovery roomServiceDiscovery;

    private MutableLiveData<NsdServiceInfo> discoveredServicesLiveData = new MutableLiveData<>();

    public RoomSignatureViewModel(@NonNull Application application) {
        super(application);
        roomServiceDiscovery = RoomServiceDiscovery.getInstance(application.getApplicationContext());
    }

    public void startDiscover() {
        //This methods starts the discovering of nearby partify servers
        //Information about services discovered should be passed to ui
        roomServiceDiscovery.startDiscover(discoveryListener);

    }

    public void stopDiscover() {
        //This method stops the discovery process
        roomServiceDiscovery.stopDiscover(discoveryListener);
    }

    public void resolveService(NsdServiceInfo nsdServiceInfo, ErrorListener errorListener) {
        roomServiceDiscovery.resolve(nsdServiceInfo, new RoomServiceDiscovery.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "onResolveFailed: " + errorCode);
                errorListener.onError(errorCode);
            }

            @Override
            public void onServiceResolved(Room.Signature roomSignature) {
                Log.d(TAG, "onServiceResolved: " + roomSignature.getHost() + " " + roomSignature.getPort());
            }
        });
    }


    public LiveData<NsdServiceInfo> getDiscoveredServices() {
        return discoveredServicesLiveData;
    }

    private NsdManager.DiscoveryListener discoveryListener = new NsdManager.DiscoveryListener() {
        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.d(TAG, "onStartDiscoveryFailed: ");
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.d(TAG, "onStopDiscoveryFailed: ");
        }

        @Override
        public void onDiscoveryStarted(String serviceType) {
            Log.d(TAG, "onDiscoveryStarted: ");
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.d(TAG, "onDiscoveryStopped: ");
        }

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceFound: ");
            // discovering ourselves is unhandled
            if (!serviceInfo.getServiceType().equals(Room.SERVICE_TYPE)) {
                Log.d(TAG, "onServiceFound: unknownServiceType: " + serviceInfo.getServiceType());
            } else {
                Log.d(TAG, "onServiceFound: service added to the list, name: " + serviceInfo.getServiceName());
                discoveredServicesLiveData.postValue(serviceInfo);
            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceLost: ");
        }
    };
}
