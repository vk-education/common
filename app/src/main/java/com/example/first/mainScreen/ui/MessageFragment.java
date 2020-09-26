package com.example.first.mainScreen.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.first.R;

public class MessageFragment extends Fragment {
    private static final String ARG_INF_DOG = "code";
    static final int BAD_INTERNET = 1;
    static final int PROFILE_END = 2;

    private int code;

    public MessageFragment() {
    }

    static MessageFragment newInstance(int code) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_INF_DOG, code);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getInt(ARG_INF_DOG);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);

        ImageView imgInfo = v.findViewById(R.id.message);

        if (code == PROFILE_END)
            imgInfo.setImageResource(R.drawable.profile_end);
        else if (code == BAD_INTERNET)
            imgInfo.setImageResource(R.drawable.bed_internet);

        return v;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout.LayoutParams linearLay = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLay.topMargin = view.getTop() + 100;

        view.setLayoutParams(linearLay);
    }
}
