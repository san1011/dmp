package com.evt.dmp.Analysis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.evt.dmp.MainActivity;
import com.evt.dmp.R;
import com.evt.dmp.protocal.DmpWebService;
import com.evt.dmp.protocal.PlanItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by everitime5 on 2018-01-24.
 */

public class AnalysisDay extends AppCompatActivity{
    private TextView analysisText,textView,textView2;
    private Retrofit retrofit;
    private DmpWebService dmpWebService;
    private float cnt=0,resultScore=0;
    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_day);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(DmpWebService.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        dmpWebService = retrofit.create(DmpWebService.class);

        initUi(savedInstanceState);
        getApiPlan();
    }

    public void initUi(Bundle savedInstanceState){
        textView = findViewById(R.id.textView);
    }

    public void getApiPlan(){
        Call<ArrayList<PlanItem>> comment = dmpWebService.getAllPlan();

        comment.enqueue(new Callback<ArrayList<PlanItem>>() {
            @Override
            public void onResponse(Call<ArrayList<PlanItem>> call, Response<ArrayList<PlanItem>> response) {
                ArrayList<PlanItem> planItems = response.body();
                for(int i=0; i<planItems.size(); i++){
                    if(planItems.get(i).getComplete()==2||planItems.get(i).getComplete()==1){
                        cnt++;
                        if(planItems.get(i).getComplete()==2){
                            resultScore++;
                        }
                    }
                }
                initGraph();
            }

            @Override
            public void onFailure(Call<ArrayList<PlanItem>> call, Throwable t) {
                Log.e("[Error]",""+t.getMessage());
            }
        });
    }

    public void initGraph() {
        float i=resultScore/cnt*100;
        int average;
        average = (int)i;
        textView.setText("계획 수 : "+(int)cnt+"개 "+"\n"+"실행 수 : "+(int)resultScore +"개 ");

        PieView pieView = (PieView) findViewById(R.id.pieView);
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        pieView.setInnerText(average+"%");
        pieView.setPercentage(average);

        PieAngleAnimation animation = new PieAngleAnimation(pieView);
        animation.setDuration(1000); //This is the duration of the animation in millis
        pieView.startAnimation(animation);
    }

}