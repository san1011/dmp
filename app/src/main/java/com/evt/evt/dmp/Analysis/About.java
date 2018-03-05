package com.evt.evt.dmp.Analysis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.evt.evt.dmp.R;

/**
 * Created by Limchaesan on 2018. 3. 5..
 */

public class About extends AppCompatActivity{

    private TextView textView;
    private TextView textView2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
