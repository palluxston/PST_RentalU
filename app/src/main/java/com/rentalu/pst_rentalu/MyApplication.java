package com.rentalu.pst_rentalu;
import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize and start the music
        MusicManager.startMusic(this);
    }

    @Override
    public void onTerminate() {
        // Release the media player when the application is terminated
        MusicManager.releaseMediaPlayer();
        super.onTerminate();
    }
}
