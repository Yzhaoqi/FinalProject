package com.group.android.finalproject.player.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;

import com.group.android.finalproject.player.services.MusicService;

/**
 * Created by YZQ on 2016/12/21.
 */

public class MusicPresenter {
    private static MusicPresenter mPresenter;
    private MusicService musicService;
    private View mainView, playView;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder)(iBinder)).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };

    private MusicPresenter(Context context) {
        Intent intent = new Intent(context, MusicService.class);
        context.bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }

    public static MusicPresenter getInstance(Context context) {
        if (mPresenter == null) {
            mPresenter = new MusicPresenter(context);
        }
        return mPresenter;
    }

    public void loadMusic(String filepath) {
        musicService.load(filepath);
    }

    public void musicPlay() {
        musicService.play();
    }

    public int getCurrentPosition() {
        return musicService.getCurrentPosition();
    }

    public int getDuration() {
        return musicService.getDuration();
    }
}
