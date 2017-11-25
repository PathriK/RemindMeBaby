package com.pathrikumark.remindmebaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PresetActivity extends AppCompatActivity {

    private List<Reminder> reminderList;
    private RecyclerView presetView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset);

        presetView = (RecyclerView)findViewById(R.id.preset);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        presetView.setLayoutManager(llm);
        presetView.setHasFixedSize(true);
//        presetView.setItemViewCacheSize(1);

        initializeData();
        initializeAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ((PresetViewAdapter) mAdapter).setOnItemClickListener(new PresetViewAdapter.MyClickListener() {
//            @Override
//            public void onItemClick(int position, View v, PresetViewAdapter.ReminderViewHolder reminderViewHolder) {
//                Toast.makeText(v.getContext(), "Position" + position, Toast.LENGTH_SHORT).show();
////                reminderList.get(position).name = "Changed";
////                boolean hasObserver = mAdapter.hasObservers();
////                Log.i("RemindMe", "Had Observer" + hasObserver);
////                mAdapter.notifyItemChanged(position);
////                reminderViewHolder.timer.start();
//                Log.i("RemindMe", " Clicked on Item " + position + "-" + v.getTag().toString());
//                Log.i("RemindMe", "First Position" + ((PresetViewAdapter.ReminderViewHolder)presetView.findViewHolderForAdapterPosition(0)).psttt);
//            }
//        });
    }

    private void initializeData(){
        reminderList = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            reminderList.add(new Reminder(i + "", i*60*1000, i*120*1000));
        }
    }

    private void initializeAdapter(){
        mAdapter = new PresetViewAdapter(reminderList);
        presetView.setAdapter(mAdapter);
    }
}
