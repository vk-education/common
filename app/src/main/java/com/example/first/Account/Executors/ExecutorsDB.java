package com.example.first.Account.Executors;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorsDB {
    private final Executor mDiskIO;

    private final Executor mMainThread;

    private final ScheduledExecutorService mScheduledExecutorService;

    private static ExecutorsDB sAppExecutors = new ExecutorsDB();

    public static ExecutorsDB getInstance() {
        return sAppExecutors;
    }

    private ExecutorsDB(Executor diskIO, Executor mainThread, ScheduledExecutorService scheduledExecutorService) {
        this.mDiskIO = diskIO;
        this.mMainThread = mainThread;
        mScheduledExecutorService = scheduledExecutorService;
    }

    private ExecutorsDB() {
        this(Executors.newSingleThreadExecutor(),
                new MainThreadExecutor(), Executors.newScheduledThreadPool(1));
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    public ScheduledExecutorService scheduled(){
        return mScheduledExecutorService;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
