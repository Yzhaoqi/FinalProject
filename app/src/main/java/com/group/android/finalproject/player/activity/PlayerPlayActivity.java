package com.group.android.finalproject.player.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.group.android.finalproject.R;
import com.group.android.finalproject.player.presenter.MusicPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerPlayActivity extends AppCompatActivity {

    private TextView date, place, feel, remark;
    private TextView current, duration;
    private SeekBar seekBar;
    private MusicPresenter musicPresenter;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private MHandler mHandler;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    mHandler.sendMessage(new Message());
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    class MHandler extends Handler {
        public void handleMessage(Message msg) {
            Date durationTime = new Date(musicPresenter.getDuration());
            Date currentTime = new Date(musicPresenter.getCurrentPosition());
            seekBar.setMax(musicPresenter.getDuration());
            seekBar.setProgress(musicPresenter.getCurrentPosition());
            current.setText(time.format(currentTime));
            duration.setText(time.format(durationTime));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_play_activity_main);

        Bundle extras = getIntent().getExtras();

        Toolbar toolbar = (Toolbar)findViewById(R.id.player_play_toolbar);
        toolbar.setTitle((String)extras.get("title"));
        setSupportActionBar(toolbar);

        date = (TextView)findViewById(R.id.player_play_date);
        place = (TextView)findViewById(R.id.player_play_place);
        feel = (TextView)findViewById(R.id.player_play_feel);
        remark = (TextView)findViewById(R.id.player_play_remark);
        current = (TextView)findViewById(R.id.player_play_current);
        duration = (TextView)findViewById(R.id.player_play_duration);
        seekBar = (SeekBar)findViewById(R.id.player_play_seek);

        date.setText((String)extras.get("date"));
        place.setText((String)extras.get("place"));
        feel.setText((String)extras.get("feel"));
        remark.setText((String)extras.get("remark"));

        musicPresenter = MusicPresenter.getInstance(this);
        mHandler = new MHandler();
        new Thread(mRunnable).start();
    }
}
