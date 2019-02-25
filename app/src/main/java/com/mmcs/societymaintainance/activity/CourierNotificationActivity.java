package com.mmcs.societymaintainance.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;
import com.mmcs.societymaintainance.R;

public class CourierNotificationActivity extends AppCompatActivity {
EditText edt_message;
Button btnOk;
    public static MediaPlayer r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_notification_activity);
        edt_message=findViewById(R.id.edt_message);
        RemoteMessage remoteMessage = (RemoteMessage) getIntent().getExtras().get("NOTIFICATION_VALUE");
        String body = remoteMessage.getNotification().getBody();
        edt_message.setText(body);
        btnOk=findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setTitle();
        if (r != null) {
            r.stop();
        }
        back();
    }
    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.broadcast_msg));
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
}
