package com.example.quizapp.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Tiện ích quản lý luồng nền (Thread Pool) và giao tiếp Main Thread (UI).
 * Dùng cho các tác vụ truy vấn DB SQLite tránh giật lag giao diện (ANR).
 * 
 * Phụ trách chung cho toàn bộ dự án.
 */
public class AppExecutors {

    private static volatile AppExecutors sInstance;
    private final ExecutorService diskIO;
    private final Executor mainThread;

    private AppExecutors(ExecutorService diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new AppExecutors(
                            Executors.newSingleThreadExecutor(),
                            new MainThreadExecutor()
                    );
                }
            }
        }
        return sInstance;
    }

    public ExecutorService diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}

