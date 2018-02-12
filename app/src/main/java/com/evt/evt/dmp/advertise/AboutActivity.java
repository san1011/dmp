package com.evt.evt.dmp.advertise;

/**
 * Created by everitime5 on 2018-02-12.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.evt.evt.dmp.R;
import com.kakao.adfit.AdfitSdk;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView version = (TextView) findViewById(R.id.version);
        version.setText("Publisher SDK v" + AdfitSdk.SDK_VERSION);

    }
}