package borschik.partify;

import android.net.nsd.NsdServiceInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import borschik.partify.room.view_model.utils.ErrorListener;

public class DiscoveredServicesAdapter extends RecyclerView.Adapter<DiscoveredServicesAdapter.ViewHolder> {
    public static interface ItemClickListener {
        public void onItemClicked(NsdServiceInfo nsdServiceInfo);
    }

    private List<NsdServiceInfo> discoveredServices;
    private ItemClickListener itemClickListener;

    public DiscoveredServicesAdapter(List<NsdServiceInfo> discoveredServices, ItemClickListener itemClickListener) {
        this.discoveredServices = discoveredServices;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discovered_services_recycler_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.serviceName.setText(
                discoveredServices.get(position).getServiceName()
        );

        holder.serviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(discoveredServices.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return discoveredServices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView serviceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
        }
    }
}
