package com.evt.evt.dmp.Analysis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.evt.evt.dmp.MainActivity;
import com.evt.evt.dmp.R;
import com.evt.evt.dmp.protocal.DmpWebService;
import com.evt.evt.dmp.protocal.PlanItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by everitime5 on 2018-01-24.
 */

public class AnalysisDay extends MainActivity{
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
        analysisText = findViewById(R.id.analysisDay);
        textView = findViewById(R.id.textView);
    }

    public void getApiPlan(){
        Call<ArrayList<PlanItem>> comment = dmpWebService.getAllPlan();
        comment.enqueue(new Callback<ArrayList<PlanItem>>() {
            @Override
            public void onResponse(Call<ArrayList<PlanItem>> call, Response<ArrayList<PlanItem>> response) {
                ArrayList<PlanItem> planItems = response.body();
                for(int i=0; i<planItems.size(); i++){
                    /*Log.d("test","[planItems-time]"+planItems.get(i).getTime());
                    Log.d("test","[planItems-plan]"+planItems.get(i).getPlan());
                    Log.d("test","[planItems-complete]"+planItems.get(i).getComplete());*/

                    if(planItems.get(i).getComplete()==2||planItems.get(i).getComplete()==1){
                        cnt++;
                        if(planItems.get(i).getComplete()==2){
                            resultScore++;
                        }
                    }
                }
                /*Log.v("Sanch",cnt+""+resultScore+"");*/
                float i=resultScore/cnt*100;
                int average;
                average = (int)i;
                analysisText.setText( average +"%");
                textView.setText("현재 계획수 :"+(int)cnt+"개 중에 실행된 계획"+ (int)resultScore +"개 입니다.");
            }

            @Override
            public void onFailure(Call<ArrayList<PlanItem>> call, Throwable t) {
                Log.e("[Error]",""+t.getMessage());
            }
        });
    }

}
