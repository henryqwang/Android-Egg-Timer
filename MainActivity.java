package com.example.android.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar timerSeekBar;
    TextView timerTextView;
    Button timerButton;
    CountDownTimer countDownTimer;
    MediaPlayer mplayer;

    boolean counterIsActive = false;


    static int timeLeft; //This is a static class variable to remember the time left once timer is paused

    public void controlTimer(View view){

        if(counterIsActive == false) { //If not in the process of counting down: perform countdown, change button text, and disable seek bar

            counterIsActive = true;
            timerSeekBar.setEnabled(false);
            timerButton.setText("Stop");


            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) { //The +100 ms is to counter the effect of round down during the first tick of miliSecondsLeft/1000
                @Override
                public void onTick(long miliSecondsLeft) {
                    updateTimerDisplay((int) miliSecondsLeft / 1000);
                    timeLeft = ((int) miliSecondsLeft / 1000);
                }

                @Override
                public void onFinish() {
                    timerTextView.setText("0:00");
                    mplayer.start();

                }
            }.start();
        }else{ //When flag counterIsActive is true, we're in the middle of counting down: pressing the button means to stop counting
            updateTimerDisplay(timeLeft);
            timerSeekBar.setProgress(timeLeft);
            timerButton.setText("Go");
            countDownTimer.cancel();
            timerSeekBar.setEnabled(true);
            counterIsActive = false;
        }
    }

    public void updateTimerDisplay(int secondsLeft){
        //Variables that render the time display
        int minutes = (int)(secondsLeft/60);
        int seconds = (int)(secondsLeft - minutes * 60);

        //For aesthetic purposes
        String formattedSecond = Integer.toString(seconds);
        formattedSecond = (seconds < 10 ? "0"+formattedSecond : formattedSecond);

        timerTextView.setText(Integer.toString(minutes)+":"+formattedSecond);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView instantiation
        timerTextView = (TextView)findViewById(R.id.timerTextView);

        //BottonView instantiation
        timerButton = (Button)findViewById(R.id.timerButton);

        //SeekBar instantiation
        timerSeekBar = (SeekBar)findViewById(R.id.timerSeekBar);
        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(30);

        //Media player creation
        mplayer = MediaPlayer.create(getApplicationContext(), R.raw.siren);


        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //Renders the numerical display based on the progress of the seek bar
                updateTimerDisplay(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
