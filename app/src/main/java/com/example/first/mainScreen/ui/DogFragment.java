package com.example.first.mainScreen.ui;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.first.R;

public class DogFragment extends Fragment {
    private static final String ARG_ID_PHOTO = "idPhoto";
    private static final String ARG_INF_DOG = "infDog";

    private Bitmap bmpImage = null;
    private String messenger = null;
    private Listener mListener;

    public DogFragment() {
    }

    interface Listener{
        void swipeLeft();
        void swipeRight();
    }

    void setOnSwipeListener(@NonNull Listener listener) {
        mListener = listener;
    }

    static DogFragment newInstance(String infUser, Bitmap bmpImage) {
        DogFragment fragment = new DogFragment();
        Bundle args = new Bundle();

        if (infUser != null) {
            args.putParcelable(ARG_ID_PHOTO, bmpImage);
            args.putString(ARG_INF_DOG, infUser);
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bmpImage = getArguments().getParcelable(ARG_ID_PHOTO);
            messenger = getArguments().getString(ARG_INF_DOG);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dog, container, false);

        ImageView imgDogMain = v.findViewById(R.id.imgDogMain);
        TextView textMessenger = v.findViewById(R.id.textInf);

        if (bmpImage != null)
            imgDogMain.setImageBitmap(bmpImage);
        else {
            imgDogMain.setImageResource(R.drawable.dog_example2);
        }

        textMessenger.setText(messenger);

        if (mListener != null) {
            v.setOnTouchListener(new OnSwipeListener(getContext(), v, new OnSwipeListener.Listener() {
                @Override
                public void swipeLeft() {
                    mListener.swipeLeft();
                }

                @Override
                public void swipeRight() {
                    mListener.swipeRight();
                }
            }));
        }
        return v;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Animation sunRiseAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_screen);
        // Подключаем анимацию к нужному View
        view.startAnimation(sunRiseAnimation);
    }
}

