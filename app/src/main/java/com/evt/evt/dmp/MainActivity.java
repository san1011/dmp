package com.evt.evt.dmp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Scroller;
import android.widget.Toast;

import com.evt.evt.dmp.Analysis.About;
import com.evt.evt.dmp.Analysis.AnalysisDay;
import com.evt.evt.dmp.protocal.DmpWebService;
import com.evt.evt.dmp.protocal.PlanItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MainDayChangeFragment.getDatas{

    private View.OnLongClickListener onLongClickListener;
    private MainDayChangeFragment mainDayChangeFragment;
    private Retrofit retrofit;
    private DmpWebService dmpWebService;
    private ImageButton dating, eating, reading, sleeping, working, work_out;

    private Button button;
    private ArrayList<PlanItem> datas;
    private android.app.Fragment fragment;
    private HorizontalCalendar horizontalCalendar;


    public static String dbSetDate; //viewPage 데이트 세팅값
    private String day; //초반 날짜세팅
    private DateFormat dateFormet = new SimpleDateFormat("yyyy-MM-dd");
    private Date date = new Date();

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // start 2 months ago from now
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        // end after 2 months from now
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        // Default Date se읽ㄴt to Today.
        final Calendar defaultSelectedDate = Calendar.getInstance();

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.LTGRAY, Color.WHITE)
                .colorTextMiddle(Color.LTGRAY, Color.parseColor("#ffd54f"))
                .end()
                .defaultSelectedDate(defaultSelectedDate)
                .addEvents(new CalendarEventsPredicate() {

                    Random rnd = new Random();
                    @Override
                    public List<CalendarEvent> events(Calendar date) {
                        List<CalendarEvent> events = new ArrayList<>();
                        int count = rnd.nextInt(6);

                        for (int i = 0; i <= count; i++){
                            events.add(new CalendarEvent(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)), "event"));
                        }

                        return events;
                    }
                })
                .build();

        Log.i("Default Date", android.text.format.DateFormat.format("yyyy-MM-dd", defaultSelectedDate).toString());

        day=android.text.format.DateFormat.format("yyyy-MM-dd",defaultSelectedDate).toString();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                day = android.text.format.DateFormat.format("yyyy-MM-dd", date).toString();
                Toast.makeText(MainActivity.this, day + " selected!", Toast.LENGTH_SHORT).show();
                Log.i("onDateSelected", day + " - Position = " + position);
                //viewPager.setCurrentItem(position);
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {
                super.onCalendarScroll(calendarView, dx, dy);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalCalendar.goToday(false);
            }
        });

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
        viewPager.setCurrentItem(30);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, position-30);
                dbSetDate = dateFormet.format(cal.getTime());
                horizontalCalendar.centerCalendarToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, position-30);
            day = dateFormet.format(cal.getTime());
            Log.d("sanch","position : "+position);
            return new MainDayChangeFragment(day);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return 61;
        }

    }


    @Override
    public void getDatasSet(ArrayList<PlanItem> planItems) {
    }

    //db에 자료넘겨줄 완료 버튼 메소드
    private void setApiPlan() {
        //Log.v("sanch!!",MainAddPlanAdapter.stackDatas+""); //todo 전역변수 사용바꾸기
        ArrayList<PlanItem> resultDatas =new ArrayList<>();
        PlanItem resultData;

        for (int i = 0; i < MainAddPlanAdapter.stackDatas.size(); i++) {
            if(MainAddPlanAdapter.stackDatas.get(i).getPlan()!="" || MainAddPlanAdapter.stackDatas.get(i).getDate()!=""){ //만일 Date값이 있을때만 (Date는 롱클릭 clipData로 받아오기때문에 longClick없으면 date값이 없음!!!)
                resultData = new PlanItem();
                resultData.setTime(MainAddPlanAdapter.stackDatas.get(i).getTime());
                resultData.setPlan(MainAddPlanAdapter.stackDatas.get(i).getPlan());
                resultData.setComplete(MainAddPlanAdapter.stackDatas.get(i).getComplete());
                resultData.setDate(dbSetDate);
                resultData.setId("san1011@naver.com");
                resultDatas.add(resultData);
            }
        }
        Log.v("sanch",resultDatas+"");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent =new Intent(getApplicationContext(), AnalysisDay.class);
                startActivity(intent);
                break;
            case R.id.action_about:
                Intent intent1 = new Intent(getApplicationContext(), About.class);
                startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);

    }
}