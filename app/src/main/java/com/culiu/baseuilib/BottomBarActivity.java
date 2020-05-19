package com.culiu.baseuilib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chuchujie.core.widget.bottombar.BottomBarInfo;
import com.chuchujie.core.widget.bottombar.BottomBarItemView;
import com.chuchujie.core.widget.bottombar.BottomBarView;

import java.util.ArrayList;
import java.util.List;

public class BottomBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        BottomBarView bottomBarView = (BottomBarView) findViewById(R.id.bottom_bar_view);

        List<BottomBarInfo> mBottomBarInfos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BottomBarInfo bottomBarInfo = new BottomBarInfo();
            bottomBarInfo.setIconNormal(getResources().getDrawable(R.drawable.icon_home_normal));
            bottomBarInfo.setIconSelected(getResources().getDrawable(R.drawable.icon_home_checked));
            bottomBarInfo.setTextColorNormal(getResources().getColor(R.color.colorPrimaryDark));
            bottomBarInfo.setTextColorSelected(getResources().getColor(R.color.colorAccent));
            bottomBarInfo.setTextSize(14);
            bottomBarInfo.setImageTextSpace(2);
            bottomBarInfo.setText("tab" + i);
            mBottomBarInfos.add(bottomBarInfo);
        }

        bottomBarView.setOnCheckedChangeListener(new BottomBarView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(BottomBarView bottomBarView, int checkedId, int position) {
                Log.d("BottomBarActivity", "bottomBarView:" + bottomBarView + ", checkedId:" + checkedId + ", position:" + position);
            }
        });

        bottomBarView.setUpBottomView(mBottomBarInfos);

        for (int i = 0; i < bottomBarView.getChildCount(); i++) {
            BottomBarItemView bottomBarItemView = (BottomBarItemView) bottomBarView.getChildAt(i);
            bottomBarItemView.getBadge().setBadgeNumber(i + 1);
            bottomBarItemView.getBadge().stroke(Color.GREEN, 1, true);
            bottomBarItemView.invalidate();
        }


    }


}
