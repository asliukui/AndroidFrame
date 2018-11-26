package com.acmenxd.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/6 17:35
 * @detail
 */
public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Frame框架");
        setContentView(R.layout.activity_main);
    }
}
