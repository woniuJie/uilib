package com.culiu.baseuilib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chuchujie.core.widget.radiobutton.BadgeRadioButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BadgeRadioButton radioButton1 = (BadgeRadioButton) findViewById(R.id.radio_button_1);
        radioButton1.getBadge().setBadgeNumber(1);

        BadgeRadioButton radioButton2 = (BadgeRadioButton) findViewById(R.id.radio_button_2);
        radioButton2.getBadge().setBadgeNumber(10);

        BadgeRadioButton radioButton3 = (BadgeRadioButton) findViewById(R.id.radio_button_3);
        radioButton3.getBadge().setBadgeNumber(1000);

        BadgeRadioButton radioButton4 = (BadgeRadioButton) findViewById(R.id.radio_button_4);
        radioButton4.getBadge().setBadgeText("你好");



        findViewById(R.id.content_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BottomBarActivity.class));
            }
        });

    }
}
