package com.evt.dmp.advertise;

/**
 * Created by everitime5 on 2018-02-12.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.evt.dmp.R;

import java.util.ArrayList;

public class TestAppActivity extends Activity implements
        OnItemClickListener {

    private ArrayList<ServiceActivity> serviceList = new ArrayList<TestAppActivity.ServiceActivity>();

    public void setService() {
        serviceList.add(new ServiceActivity("Banner", BannerActivity.class));
        serviceList.add(new ServiceActivity("SDK 정보", AboutActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setService();

        LinearLayout container = new LinearLayout(this);

        ListView serviceListView = new ListView(this);
        serviceListView.setAdapter(new ServiceAdapter(serviceList));
        serviceListView.setOnItemClickListener(this);

        container.addView(serviceListView, LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);

        setContentView(container);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        this.serviceList.get(arg2).onclick();
    }

    private class ServiceActivity {
        protected String name;
        protected Class<?> cls;

        public ServiceActivity(String name, Class<?> cls) {
            this.name = name;
            this.cls = cls;
        }

        public String getName() {
            // return (this.cls != null) ? this.name + cls.getSimpleName() :
            // this.name;
            return this.name;
        }

        public void onclick() {
            if ( cls == null ) {
                return;
            }

            Intent intent = new Intent(TestAppActivity.this, cls);
            startActivity(intent);
        }
    }

    private class ServiceAdapter extends BaseAdapter {

        private ArrayList<ServiceActivity> mList;

        public ServiceAdapter(ArrayList<ServiceActivity> list) {
            mList = list;
        }

        public int getCount() {
            return mList.size();
        }

        public Object getItem(int position) {
            return mList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if ( v == null ) {
                v = View.inflate(TestAppActivity.this,
                        R.layout.service_item, null);
            }
            ServiceActivity sa = mList.get(position);
            if ( sa != null ) {
                TextView tv = (TextView) v.findViewById(R.id.rowService);
                tv.setText(sa.getName());
            }
            return v;
        }

    }

}