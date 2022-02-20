package com.portofos.coundowntimerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer player;
    private MediaPlayer mMediaPlayer;

    private static final long START_TIME_IN_MILLIS = 600000;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
        // We want to set our context from 00:00 to our actual time that is left.
        updateCountDownText();
    }

    private void startTimer() {
        // Whenever startTimer is invoked, we will create a CountDownTimer object
        // and then immediately start the CountDownTimer by start()
        mMediaPlayer = new MediaPlayer();
        // first arg = length in milliseconds. second arg = how many milliseconds shall pass when
        // our onTick method should be called. In our case every 1000 millisecond =
        // 1 millisecond = 0.001 seconds. Therefore the onTick Method will be invoked every 1 Second.
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // the parameter is amount of millis until this new CountDownTimer is finished.
                // we save this in our member variable so we if it get cancelled we can continue
                // on where we have stopped.
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                // play sound each tic
                mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.clap_reverb);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.start();
            }

            @Override
            public void onFinish() {
            }
        }.start();
        // After we have start our timer we need a reference to refer if we have started or not.

        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void play(View v) {
        if (player == null) {
            player = MediaPlayer.create(MainActivity.this, R.raw.clap_reverb);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlayer();
                }
            });
        }
        player.start();
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(MainActivity.this, "MediaPlayer released", Toast.LENGTH_SHORT);
        }
    }

    private void pause(View v) {
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}