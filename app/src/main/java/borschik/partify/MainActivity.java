package borschik.partify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import borschik.partify.room.Room;
import borschik.partify.room.view_model.LocalRoomViewModel;
import borschik.partify.room.view_model.RoomSignatureViewModel;
import borschik.partify.room.view_model.utils.ErrorListener;
import borschik.partify.room.view_model.utils.LocalViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DnsService";

    private LocalRoomViewModel localRoomViewModel;
    private RoomSignatureViewModel roomSignatureViewModel;

    // UI
    RecyclerView discoveredServices;
    DiscoveredServicesAdapter adapter;
    List<NsdServiceInfo> discoveredServicesList = new ArrayList<>();

    private EditText enterRoomName;
    private TextView roomName;
    private Button register;
    private Button discover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UISetup();

        register.setOnClickListener(v -> makeAppDiscoverable());
        discover.setOnClickListener(v -> discoverServices());
    }



    private void discoverServices() {
        ErrorListener errorListener = errorCode -> {
            // TODO inform views about errors, which may occur asynchronously
        };

        roomSignatureViewModel = new ViewModelProvider(this).get(RoomSignatureViewModel.class);
        roomSignatureViewModel.getDiscoveredServices().observe(this, nsdServiceInfo -> {
            Log.d(TAG, "discoverServices: nsdServiceInfo onChanged()");
            discoveredServicesList.add(nsdServiceInfo);

            String msg = Integer.toString(discoveredServicesList.size()) + " " + discoveredServicesList.get(discoveredServicesList.size()-1) + "";
            Log.d(TAG, msg);

            adapter.notifyItemChanged(discoveredServicesList.size()-1);
        });
        roomSignatureViewModel.startDiscover();
        discoveredServices.setVisibility(View.VISIBLE);
    }

    private void makeAppDiscoverable() {
        ErrorListener errorListener = errorCode -> {
            // TODO inform views about errors, which may occur asynchronously
        };

        String nameOfRoom = enterRoomName.getText().toString();

        localRoomViewModel = new ViewModelProvider(this,
                new LocalViewModelFactory(this.getApplication(), errorListener, nameOfRoom))
                .get(LocalRoomViewModel.class);
        localRoomViewModel.getRoom().observe(this, new Observer<Room>() {
            @Override
            public void onChanged(Room room) {
                Log.d(TAG, "Room onChanged()");
                roomName.setText(room.getSignature().getName());
            }
        });
    }

    private void UISetup() {
        enterRoomName = findViewById(R.id.editText);
        roomName = findViewById(R.id.textView);
        register = findViewById(R.id.register_btn);
        discover = findViewById(R.id.discover_btn);

        recyclerSetup();
    }

    private void recyclerSetup() {
        ErrorListener errorListener = errorCode -> {
            // inform views about errors, which may occur asynchronously
            discover.setBackgroundResource(R.color.colorError);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    discover.setBackgroundResource(R.color.colorDefaultBtn);
                }
            }, 1000);
        };

        discoveredServices = findViewById(R.id.discoveredServicesList);
        discoveredServices.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiscoveredServicesAdapter(discoveredServicesList, nsdServiceInfo -> {
            // if service was clicked - resolve it and get its signature
            roomSignatureViewModel.resolveService(nsdServiceInfo, errorListener);
        });
        discoveredServices.setAdapter(adapter);
    }

}