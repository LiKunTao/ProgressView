package com.lkt.pview.progerssveiw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lkt.pview.progerssveiw.R;
import com.lkt.pview.progerssveiw.bean.PointBean;
import com.lkt.pview.progerssveiw.view.ProgressView;
import com.lkt.pview.progerssveiw.viewinterface.IOnPointClick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    List<PointBean> datas = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private int random = 12;
    private ProgressView progressView;
    private Random myRandom = new Random();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = findViewById(R.id.timeLineView);
        textView = findViewById(R.id.textView);
        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = myRandom.nextInt(20);
                init(i);
            }
        });
        init(random);
        progressView.setiOnPointClick(new IOnPointClick() {
            @Override
            public void onPointClick(int position) {
                PointBean pointBean = datas.get(position);
                Toast.makeText(MainActivity.this, pointBean.getAction() + pointBean.getUser(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(int val) {
        datas.clear();
        for (int i = 0; i < val; i++) {
            PointBean pointBean = new PointBean();
            pointBean.setAction("refuse");
            pointBean.setDate(sdf.format(new Date(System.currentTimeMillis())));
            pointBean.setUser("用户" + i);
            datas.add(pointBean);
        }
        progressView.setData(datas);
        textView.setText("数量" + datas.size());
    }
}
