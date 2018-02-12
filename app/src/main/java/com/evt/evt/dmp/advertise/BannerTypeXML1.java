package com.evt.evt.dmp.advertise;

/**
 * Created by everitime5 on 2018-02-12.
 */


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.evt.evt.dmp.R;
import com.kakao.adfit.ads.AdListener;
import com.kakao.adfit.ads.ba.BannerAdView;

public class BannerTypeXML1 extends Activity {
    private static final String LOGTAG = "BannerTypeXML1";

    private BannerAdView adView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_type_1);

        initAdFit();

    }

    @Override
    public void onResume(){
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (adView != null) {
            adView.destroy();
            adView = null;
        }
    }

    private void initAdFit() {
        // AdFit sdk 초기화 시작
        adView = (BannerAdView) findViewById(R.id.adview);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(LOGTAG, "onAdLoaded");
            }

            @Override
            public void onAdFailed(int code) {
                Log.d(LOGTAG, "onAdFailed : " + code);
            }

            @Override
            public void onAdClicked() {
                Log.d(LOGTAG, "onAdClicked");
            }
        });

        // 할당 받은 clientId 설정
        adView.setClientId("DAN-s164c5nwco54");

        // 광고 갱신 시간 : 기본 60초
        // 0 으로 설정할 경우, 갱신하지 않음.
        adView.setRequestInterval(30);

        // 광고 사이즈 설정
        adView.setAdUnitSize("320x50");

        adView.loadAd();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
