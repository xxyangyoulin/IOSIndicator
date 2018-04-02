package cn.xxyangyoulin.iosindicator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cn.xxyangyoulin.library.IOSIndicator;

public class MainActivity extends AppCompatActivity {

    private IOSIndicator indicator;
    private TextView tv;
    private int currentPage;

    List<String> titles = Arrays.asList("首页", "社区", "我");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indicator = (IOSIndicator) findViewById(R.id.indicator);
        indicator.setItemTitles(titles);

        //主动切换 切换监听
        indicator.setSwitchListener(new IOSIndicator.ClickListener() {
            @Override
            public void onClick(int currentIndex, boolean isRepeat) {
                if (!isRepeat) {
                    System.out.println("点击了--" + currentIndex);
                }
            }
        });
    }

    public void click(View v) {
        if (currentPage >= titles.size()) {
            currentPage = 0;
        }

        //被动切换
        indicator.setCurrentPage(currentPage++);

    }
}
