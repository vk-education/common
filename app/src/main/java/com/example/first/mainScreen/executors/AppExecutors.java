package com.example.first.mainScreen.executors;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {
    private final Executor mDiskIO;

    private final Executor mMainThread;

    private final ScheduledExecutorService mScheduledExecutorService;

    private static AppExecutors sAppExecutors = new AppExecutors();

    public static AppExecutors getInstance() {
        return sAppExecutors;
    }

    private AppExecutors(Executor diskIo, Executor mainThread, ScheduledExecutorService mScheduledExecutorService) {
        this.mDiskIO = diskIo;
        this.mMainThread = mainThread;
        this.mScheduledExecutorService = mScheduledExecutorService;
    }

    private  AppExecutors() {
        this(Executors.newSingleThreadExecutor(),
                new MainThreadExecutor(), Executors.newScheduledThreadPool(1));
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
