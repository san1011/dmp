package com.evt.dmp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evt.dmp.protocal.DmpWebService;
import com.evt.dmp.protocal.dto.PlanItem;
import com.evt.dmp.protocal.dto.PlanItemRoot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by everitime5 on 2018-02-01.
 */


@SuppressLint("ValidFragment")
public class MainDayChangeFragment extends Fragment {
    private MainAddPlanAdapter mainAddPlanAdapter;
    private RecyclerView recyclerView;
    private getDatas getDatasListener;
    private Retrofit retrofit;
    private DmpWebService dmpWebService;
    private TextView textView;
    private String day;
    private PlanItemRoot planItemRoot;
    private ArrayList<PlanItem> planItems = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public MainDayChangeFragment(String day) {
        this.day = day;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof getDatas) {
            getDatasListener = (getDatas) context;
        } else {
            throw new RuntimeException(context.toString() + "");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getDatasListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(DmpWebService.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        dmpWebService = retrofit.create(DmpWebService.class);

        return inflater.inflate(R.layout.fragment_daychange_viewpager, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(day);
        initRecycler(view,savedInstanceState);

    }

    public void initRecycler(View view, Bundle savedInstanceState) {
        mainAddPlanAdapter = new MainAddPlanAdapter();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mainAddPlanAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getApiPlan();
        getDatas();
    }

    //데이터 베이스 저장된 값 불러오는 메소드
    public void getApiPlan() { //currentDay는 0번째부를때는(오늘) MainDayChangeAdapter에서 불러오고 2번째부터는 MainFragment(changePage...메소드에서 불러옴)

        Call<PlanItemRoot> comment = dmpWebService.getPlan("san1011@naver.com",day); //todo id, date 변수로 담아야함 변수에 빈값이면 nullpointExeception
        comment.enqueue(new Callback<PlanItemRoot>() {
            @Override
            public void onResponse(Call<PlanItemRoot> call, Response<PlanItemRoot> response) {
                planItemRoot = response.body();
                Log.d("sanch",day);

                for(PlanItem planItem : planItemRoot.getResponse()){
                    planItems.add(planItem); //todo 객체 새로만듬
                }

                ArrayList<PlanItem> timeDatas = new ArrayList<>();
                ArrayList<String> planString = new ArrayList<>(); //plan 배열에 저장
                ArrayList<Integer> complete = new ArrayList<>(); //complete 배열에 저장
                PlanItem timeData;
                //Log.d("sanch",planItems+"");

                for(int i=6; i<=24; i++){ //기초 데이터 파싱
                    boolean flag=false;
                    for(int j=0; j<planItems.size(); j++) {
                        if (planItems.get(j).getTime().equals(i + ":00")){ //불러오는 데이터와 시간이 일치하면
                            planString.add(planItems.get(j).getPlan()); //plan을 불러와 세팅 planString에 세팅
                            complete.add(planItems.get(j).getComplete()); //complete를 불러와 complete에 세팅
                            flag=true;
                        }
                    }
                    if(flag==false) { //시간이 일치하지않으면
                        planString.add(""); //아니라면 빈값세팅
                        complete.add(0); //아니라면 0세팅
                    }
                }

                //불러온 데이터로 그려주기
                int j=0;
                for (int i = 6; i <= 24; i++) {
                    timeData = new PlanItem();
                    timeData.setTime(i + ":00");
                    timeData.setPlan(planString.get(j));
                    timeData.setComplete(complete.get(j));
                    timeData.setDate("");
                    timeData.setId("san1011@naver.com");
                    timeDatas.add(timeData);
                    j++;
                }
                //값 recyclerView에 전달

                mainAddPlanAdapter.clearDatas();
                mainAddPlanAdapter.addDatas(timeDatas);
            }

            @Override
            public void onFailure(Call<PlanItemRoot> call, Throwable t) {
                Log.e("setApiPlan", "error:" + t.getMessage());
            }
        });
    }

    public interface getDatas {
        void getDatasSet(ArrayList<PlanItem> planItems);
    }

    public void getDatas() {
        getDatasListener.getDatasSet((ArrayList<PlanItem>) mainAddPlanAdapter.getItems());
    }
}
