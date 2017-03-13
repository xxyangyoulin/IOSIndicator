package com.mnnyang.iosindicator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private IOSIndicator indicator;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indicator = (IOSIndicator) findViewById(R.id.indicator);
        indicator.setItemTitles(Arrays.asList("首页", "社区", "我", "设置"));

        //主动切换 点击监听
        indicator.setClickListener(new IOSIndicator.ClickListener() {
            @Override
            public void onClick(int currentIndex) {
                System.out.println("点击了--" + currentIndex);
            }
        });
    }

    public void click(View v) {
        Random random = new Random();
        int ran = random.nextInt(4);

        //被动切换
        indicator.setCurrentPage(ran);

    }
}
