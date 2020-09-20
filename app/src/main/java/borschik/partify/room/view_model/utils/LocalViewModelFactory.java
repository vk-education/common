package borschik.partify.room.view_model.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import borschik.partify.room.view_model.LocalRoomViewModel;

public class LocalViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private ErrorListener errorListener;
    private String roomName;

    public LocalViewModelFactory(@NonNull Application application, ErrorListener errorListener,
                                 String roomName) {
        this.application = application;
        this.errorListener = errorListener;
        this.roomName = roomName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LocalRoomViewModel(application, errorListener, roomName);
    }
}
