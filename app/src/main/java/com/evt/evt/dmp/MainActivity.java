package com.evt.evt.dmp;

import android.content.ClipData;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.evt.evt.dmp.protocal.DmpWebService;
import com.evt.evt.dmp.protocal.PlanItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MainDayChangeFragment.getDatas{
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private View.OnLongClickListener onLongClickListener;
    private MainDayChangeFragment mainDayChangeFragment;
    private Retrofit retrofit;
    private DmpWebService dmpWebService;
    private ImageButton dating, eating, reading, sleeping, working, work_out;

    public static String dbSetDate; //viewPage 데이트 세팅값
    private String day; //초반 날짜세팅
    private DateFormat dateFormet = new SimpleDateFormat("yyyy-MM-dd");
    private Date date = new Date();
    private Button button;
    private ArrayList<PlanItem> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(DmpWebService.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        dmpWebService = retrofit.create(DmpWebService.class);

        initUi(savedInstanceState);
        initViewPager(savedInstanceState);
    }

    public void initUi(Bundle savedInstanceState){
        onLongClickListener = new OnLongClickListener();

        dating = (ImageButton) findViewById(R.id.image_date);
        eating = (ImageButton) findViewById(R.id.image_eating);
        reading = (ImageButton) findViewById(R.id.image_reading);
        sleeping = (ImageButton) findViewById(R.id.image_sleeping);
        working = (ImageButton) findViewById(R.id.image_working);
        work_out = (ImageButton) findViewById(R.id.image_work_out);

        dating.setOnLongClickListener(onLongClickListener);
        eating.setOnLongClickListener(onLongClickListener);
        reading.setOnLongClickListener(onLongClickListener);
        sleeping.setOnLongClickListener(onLongClickListener);
        working.setOnLongClickListener(onLongClickListener);
        work_out.setOnLongClickListener(onLongClickListener);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setApiPlan();
            }
        });
    }

    public void initViewPager(Bundle savedInstanceState){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, position);
                dbSetDate = dateFormet.format(cal.getTime());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(viewPager.getCurrentItem()==0){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            dbSetDate = dateFormet.format(cal.getTime());
        }
    }

    @Override
    public void getDatasSet(ArrayList<PlanItem> planItems) {
        datas=planItems;
    }

    private class PagerAdapter extends FragmentStatePagerAdapter  {
        private ArrayList<String> planString;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, position);
            day = dateFormet.format(cal.getTime());
            return new MainDayChangeFragment(day);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        public void setData(ArrayList<String> planString) {
            this.planString = planString;
        }

    }

    //db에 자료넘겨줄 완료 버튼 메소드
    private void setApiPlan() {
        ArrayList<PlanItem> resultDatas =new ArrayList<>();
        PlanItem resultData;

        for (int i = 0; i < datas.size(); i++) {
            if(datas.get(i).getPlan()!=""){ //만일 Date값이 있을때만 (Date는 롱클릭 clipData로 받아오기때문에 longClick없으면 date값이 없음!!!)
                resultData = new PlanItem();
                resultData.setTime(datas.get(i).getTime());
                resultData.setPlan(datas.get(i).getPlan());
                resultData.setComplete(datas.get(i).getComplete());
                resultData.setDate(dbSetDate);
                resultData.setId("san1011@naver.com");
                resultDatas.add(resultData);
            }
        }

        Call<ArrayList<PlanItem>> comment = dmpWebService.setPlan(resultDatas);  //웹서비스 연결
        comment.enqueue(new Callback<ArrayList<PlanItem>>() {
            @Override
            public void onResponse(Call<ArrayList<PlanItem>> call, Response<ArrayList<PlanItem>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(),"저장 되었습니다",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(),"저장 실패",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PlanItem>> call, Throwable t) {
                Toast.makeText(getApplication(),"저장에 실패",Toast.LENGTH_LONG).show();
                Log.e("setApiPlan", "error:" + t.getMessage());
            }
        });
    }

    //MainAddPlanAdapter에 값넘겨주는 메소드
    public class OnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            ClipData clip = null;
            switch (v.getId()) {
                case R.id.image_date:
                    clip = ClipData.newPlainText("date", day);
                    break;
                case R.id.image_eating:
                    clip = ClipData.newPlainText("eating", day);
                    break;
                case R.id.image_reading:
                    clip = ClipData.newPlainText("reading", day);
                    break;
                case R.id.image_sleeping:
                    clip = ClipData.newPlainText("sleeping", day);
                    break;
                case R.id.image_working:
                    clip = ClipData.newPlainText("working", day);
                    break;
                case R.id.image_work_out:
                    clip = ClipData.newPlainText("fitness", day);
                    break;
            }
            v.startDragAndDrop(clip, new View.DragShadowBuilder(v), null, 0);
            return false;
        }
    }
}
