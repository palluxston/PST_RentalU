package com.rentalu.pst_rentalu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class setting_layout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    TextView username, musicTrackNumText, soundtrackText,soundtrackName;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SeekBar soundtrackSlider;
    ImageButton preSoundtrackButton, playSoundtrackButton, nextSoundtrackButton;
    private Handler seekBarHandler;
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);

        drawerLayout = findViewById(R.id.drawer_layout);

        // UI Components for Music Player
        musicTrackNumText = findViewById(R.id.txt_music_track_no);
        soundtrackSlider = findViewById(R.id.soundtrack_slider);
        preSoundtrackButton = findViewById(R.id.prev_soundtrack_button);
        playSoundtrackButton = findViewById(R.id.play_soundtrack_button);
        nextSoundtrackButton = findViewById(R.id.next_soundtrack_button);
        soundtrackText = findViewById(R.id.progressTextMin);
        soundtrackName = findViewById(R.id.soundtrack_name);

        int currentSoundtrackDuration = MusicManager.getDuration();
        soundtrackSlider.setMax(currentSoundtrackDuration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent i = getIntent();
        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.userName);
        username.setText(i.getStringExtra("Username"));

        Switch musicToggle = findViewById(R.id.music_toggle);

        musicToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                MusicManager.startMusic(getApplicationContext());
                Toast.makeText(setting_layout.this, "Music has been On!", Toast.LENGTH_SHORT).show();
            } else {
                MusicManager.stopMusic();
                Toast.makeText(setting_layout.this, "Music has been OFF!", Toast.LENGTH_SHORT).show();
            }
        });

        updateSoundtrackControls();

        soundtrackSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int newPosition = (int) (progress / 100f * MusicManager.getDuration());
                    MusicManager.seekTo(newPosition);
                    updateSoundtrackSeekBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Called when user starts touching the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Called when user stops touching the SeekBar
            }
        });

        seekBarHandler = new Handler();
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                updateSoundtrackSeekBar();
                seekBarHandler.postDelayed(this, 1000); // Update every second
            }
        };
        seekBarHandler.postDelayed(updateSeekBar, 0);

        preSoundtrackButton.setOnClickListener(v -> {
            MusicManager.changeToPreviousSoundtrack(getApplicationContext());
            updateSoundtrackControls();
        });

        playSoundtrackButton.setOnClickListener(v -> {
            if (MusicManager.isPlaying()) {
                MusicManager.pauseMusic();
                playSoundtrackButton.setImageResource(R.drawable.ic_pause);
            } else {
                MusicManager.startMusic(getApplicationContext());
                playSoundtrackButton.setImageResource(R.drawable.ic_play);
            }
            updateSoundtrackControls();
        });

        nextSoundtrackButton.setOnClickListener(v -> {
            MusicManager.changeToNextSoundtrack(getApplicationContext());
            updateSoundtrackControls();
        });
    }

    private void updateSoundtrackSeekBar() {
        if (MusicManager.isPlaying() && soundtrackSlider != null) {
            int currentPosition = MusicManager.getCurrentPosition();
            int duration = MusicManager.getDuration();
            if (duration > 0) {
                int progress = (int) (((float) currentPosition / duration) * 100);
                soundtrackSlider.setProgress(progress);

                // Display progress in minutes and seconds
                int minutes = currentPosition / 60000;
                int seconds = (currentPosition % 60000) / 1000;
                String progressText = String.format("%02d:%02d", minutes, seconds);
                soundtrackText.setText("Soundtrack: " + progressText);
            }
        }
    }

    private void updateSoundtrackControls() {
        int currentSoundtrackIndex = MusicManager.getCurrentSoundtrackIndex();
        musicTrackNumText.setText("Soundtrack: " + (currentSoundtrackIndex + 1));

        // Set the name of the current soundtrack in progressTextMin
        String currentSoundtrackName = MusicManager.getSoundtrackName(currentSoundtrackIndex);
        soundtrackText.setText("Now Playing: " + currentSoundtrackName);
        soundtrackName.setText("Soundtrack Name: "+currentSoundtrackName);

        soundtrackSlider.setMax(MusicManager.getDuration());
        soundtrackSlider.setProgress(MusicManager.getCurrentPosition());

        if (MusicManager.isPlaying()) {
            playSoundtrackButton.setImageResource(R.drawable.ic_pause);
        } else {
            playSoundtrackButton.setImageResource(R.drawable.ic_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (seekBarHandler != null) {
            seekBarHandler.removeCallbacks(updateSeekBar);
        }
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_profile) {
            Intent i = new Intent(setting_layout.this, profile_layout.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        } else if (item.getItemId() == R.id.nav_home) {

                Intent i = new Intent(setting_layout.this, view_property.class);
                View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
                username = headerView.findViewById(R.id.userName);
                i.putExtra("Username", username.getText().toString());
                startActivity(i);
                finish();

        } else if(item.getItemId() == R.id.nav_newsfeed){
            Intent i = new Intent(setting_layout.this, newsfeed_page.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        } else if (item.getItemId() == R.id.nav_setting) {
            //
        } else if (item.getItemId() == R.id.nav_about) {
            Intent i = new Intent(setting_layout.this, about_us.class);
            View headerView = navigationView.getHeaderView(0); //0 is index of the nav_drawer_header layout
            username = headerView.findViewById(R.id.userName);
            i.putExtra("Username", username.getText().toString());
            startActivity(i);
            finish();
        }  else if (item.getItemId() == R.id.nav_logout) {
            //logout confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(setting_layout.this);
            builder.setMessage("Do you really want to logout?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(setting_layout.this, LoadingActivity.class);
                    startActivity(i);
                    finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //
                }
            });
            AlertDialog mDialog = builder.create();
            mDialog.show();
        }
        return true;
    }
}