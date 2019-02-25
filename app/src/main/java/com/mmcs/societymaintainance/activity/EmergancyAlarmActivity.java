package com.mmcs.societymaintainance.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;
import com.mmcs.societymaintainance.R;

public class EmergancyAlarmActivity extends AppCompatActivity {
    ImageView image;
    EditText edt_message;
    Button btnOk;
    //public static Ringtone r;
    public static MediaPlayer r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergancy_alarm);
        image = findViewById(R.id.image);
        edt_message = findViewById(R.id.edt_message);
        btnOk = findViewById(R.id.btnOk);
        RemoteMessage remoteMessage = (RemoteMessage) getIntent().getExtras().get("NOTIFICATION_VALUE");
        String body = remoteMessage.getNotification().getBody();
        edt_message.setText(body);
        Animation shake = AnimationUtils.loadAnimation(EmergancyAlarmActivity.this, R.anim.shake);
        image.startAnimation(shake);
        setTitle();
        back();
        if (r == null) {
            //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            //r = RingtoneManager.getRingtone(this, notification);
            r = MediaPlayer.create(this, R.raw.emergency_alarm);
            r.setLooping(true);
        } else
            r.pause();
        r.start();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r.stop();
                finish();
            }
        });
    }

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.emergency_alarm));
    }

    private void back() {
        RelativeLayout drawerIcon = (RelativeLayout) findViewById(R.id.drawerIcon);
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (r.isPlaying())
            r.stop();
    }
}
