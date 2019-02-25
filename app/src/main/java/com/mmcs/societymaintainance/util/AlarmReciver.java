package com.mmcs.societymaintainance.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();


      /*  MediaPlayer pl=new MediaPlayer();
        try {
            pl.setDataSource(context,notification);
            pl.setLooping(true);
            pl.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
