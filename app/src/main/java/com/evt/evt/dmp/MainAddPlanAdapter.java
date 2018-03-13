package com.evt.evt.dmp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evt.evt.dmp.protocal.PlanItem;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by everitime5 on 2018-02-01.
 */

public class MainAddPlanAdapter extends RecyclerView.Adapter<MainAddPlanAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PlanItem> datas = new ArrayList<>();
    public static ArrayList<PlanItem> stackDatas = new ArrayList<>();
    private String currentDay;
    private String dates;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_add_plan_recycler, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(datas.get(position).getTime());

        final AlertDialog.Builder alBuilder = new AlertDialog.Builder(context); //알림창 띄우기

        //첫데이터 plan 이미지 그려주기
        if(datas.get(position).getPlan()!=""){
            holder.textView1.setVisibility(View.INVISIBLE); //todo visible로 만들기
            Log.d("sanch",position+"");
            switch (datas.get(position).getPlan()){
                case "date":
                    holder.imageView.setImageResource(R.drawable.plan_add_date_square);
                    break;
                case "eating":
                    holder.imageView.setImageResource(R.drawable.plan_add_eating_square);
                    break;
                case "reading":
                    holder.imageView.setImageResource(R.drawable.plan_add_reading_square);
                    break;
                case "sleeping":
                    holder.imageView.setImageResource(R.drawable.plan_add_sleep_square);
                    break;
                case "working":
                    holder.imageView.setImageResource(R.drawable.plan_add_work_square);
                    break;
                case "fitness":
                    holder.imageView.setImageResource(R.drawable.plan_add_workout_square);
                    break;
            }
        }else{
            holder.imageView = (ImageView) holder.imageView;
            holder.imageView.setImageResource(0);
        }

        //complete 그려주기
        if(datas.get(position).getComplete() == 2){
            holder.imageView1.setVisibility(View.VISIBLE);
        }else{
            holder.imageView1.setVisibility(View.INVISIBLE);
        }

        //이미지 드래그앤 드랍 시작
        holder.imageView.setOnDragListener(new View.OnDragListener() {
            @SuppressLint("ResourceType")
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (v instanceof ImageView) {
                    holder.imageView = (ImageView) v;
                } else {
                    return false;
                }
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        PlanItem plan = datas.get(position);
                        plan.setPlan(event.getClipDescription().getLabel()+"");
                        datas.set(position, plan);
                        datas.get(position).setComplete(1);
                        stackDatas=datas;
                        notifyItemChanged(position);
                }
                return true;
            }
        });

        //이미지가 배치된후 롱클릭 리스너
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() { //할일 편집창 띄우기
            @Override
            public boolean onLongClick(final View v) {
                if (datas.get(position).getPlan() != "") {
                    alBuilder.setMessage("Edit Plan")
                            .setCancelable(false)
                            .setPositiveButton("수행완료", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    datas.get(position).setDate(currentDay);
                                    datas.get(position).setComplete(2);
                                    stackDatas=datas;
                                    holder.imageView1.setVisibility(View.VISIBLE);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("할일취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    datas.get(position).setPlan(""); //포지션에 있는 이미지만 빈공간으로 바꿔야함
                                    datas.get(position).setComplete(-1);
                                    datas.get(position).setDate(currentDay);
                                    stackDatas=datas;
                                    holder.imageView.setImageResource(0);
                                    holder.imageView1.setVisibility(View.INVISIBLE);
                                    notifyDataSetChanged();
                                }
                            });
                    alBuilder.show();
                }
                return false;
            }
        });
    }

    //데이터 불러와 세팅메소드
    public void addDatas(ArrayList<PlanItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public ArrayList<PlanItem> getItems() {
        return this.datas;
    }

    public void clearDatas() {
        this.datas.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private TextView textView1;
        private ImageView imageView;
        private ImageView imageView1;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView1 = (TextView) itemView.findViewById(R.id.textView1);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView1 = (ImageView) itemView.findViewById(R.id.imageView1);
        }
    }
}
