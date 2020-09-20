package com.example.first.matches;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.first.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements RecyclerClickListener {

    private ArrayList<UserModel> matches;
    private Context context;
    MyAdapter(ArrayList<UserModel> matches, Context context) {
        this.matches = matches;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_matches_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setTag(matches.get(position));
        final UserModel user = matches.get(position);
        holder.nameView.setText(user.name);
        if (user.seen.equals("false")) holder.newMatch.setVisibility(View.VISIBLE);

        setPhoto(holder,position);

        holder.cardView.setOnClickListener(new View.OnClickListener() { // click on element
            @Override
            public void onClick(View v) {
                itemClick(user.id);
                user.seen = "true";
            }
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public interface RecyclerClickListener2{
       void itemClick2(String id);
    }

    @Override
    public void itemClick(String id) {
        ((RecyclerClickListener2)context).itemClick2(id);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        CardView cardView;
        ImageView photoView;
        LinearLayout newMatch;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name);
            cardView = itemView.findViewById(R.id.card_view);
            photoView = itemView.findViewById(R.id.photo);
            newMatch = itemView.findViewById(R.id.newMatch); }
        }

        private void setPhoto(final ViewHolder holder, int position) {
            UserModel user = matches.get(position);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference myRef = storageRef.child("Profiles").child(user.id).child("AvatarImage");
            long BATCH_SIZE = 1024 * 1024;
            myRef.getBytes(BATCH_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    holder.photoView.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }

    }

