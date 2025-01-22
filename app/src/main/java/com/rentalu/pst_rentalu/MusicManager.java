package com.rentalu.pst_rentalu;

import android.content.Context;
import android.media.MediaPlayer;
public class MusicManager {
    private static MediaPlayer mediaPlayer;
    private static int currentSoundtrackIndex = 0;

    private static final int[] SOUNDTRACKS = {
            R.raw.mazaphonk_by_alexi_action,
            R.raw.app_toothless,
            R.raw.fur_elise_house,
            // Add more soundtrack resources here
    };

    private static final String[] SOUNDTRACK_NAMES = {
            "Mazaphonk By Alexi Action",
            "Toothless Meme Song",
            "Fur Elise House",

            // Add more soundtrack names here
    };

    // Method to get the name of the current soundtrack
    public static String getSoundtrackName(int index) {
        if (index >= 0 && index < SOUNDTRACK_NAMES.length) {
            return SOUNDTRACK_NAMES[index];
        } else {
            return "Unknown Soundtrack";
        }
    }

    public static MediaPlayer getMediaPlayer(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, SOUNDTRACKS[currentSoundtrackIndex]);
            mediaPlayer.setLooping(true);
        }
        return mediaPlayer;
    }

    public static void startMusic(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, SOUNDTRACKS[currentSoundtrackIndex]);
            mediaPlayer.setLooping(true);
        }
        mediaPlayer.start();
    }

    public static void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void changeToNextSoundtrack(Context context) {
        currentSoundtrackIndex = (currentSoundtrackIndex + 1) % SOUNDTRACKS.length;
        releaseMediaPlayer();
        startMusic(context);
    }

    public static void changeToPreviousSoundtrack(Context context) {
        currentSoundtrackIndex = (currentSoundtrackIndex - 1 + SOUNDTRACKS.length) % SOUNDTRACKS.length;
        releaseMediaPlayer();
        startMusic(context);
    }

    public static boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public static void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public static int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public static void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public static int getCurrentSoundtrackIndex() {
        return currentSoundtrackIndex;
    }
}

