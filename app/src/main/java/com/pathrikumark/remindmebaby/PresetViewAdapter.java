package com.pathrikumark.remindmebaby;

import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by keert on 14-11-2017.
 */

public class PresetViewAdapter extends RecyclerView.Adapter<PresetViewAdapter.ReminderViewHolder>{
//    private static MyClickListener myClickListener;
    private final List<ReminderViewHolder> viewHolders;
    private List<Reminder> reminderList;

    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
//            Log.i("RemindMeBaby", "Inside Runnable");
            synchronized (viewHolders) {
//                Log.i("RemindMeBaby", "Above For loop");
                for (ReminderViewHolder holder : viewHolders) {
//                    Log.i("RemindMeBaby", "Inside For Loop");
                    holder.updateTimeRemaining();
                }
            }
        }
    };

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                Log.i("RemindMeBaby", "Before Post");
                mHandler.post(updateRemainingTimeRunnable);
//                Log.i("RemindMeBaby", "After post");
            }
        }, 1000, 1000);
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView reminderView;
        TextView reminderName, reminderDuration, reminderStatus;
        ImageButton reminderStart, reminderEnd ;
        ProgressBar reminderProgress;
        Reminder reminder;

        ReminderViewHolder(View itemView){
            super(itemView);
            reminderView = (CardView)itemView.findViewById(R.id.reminderView);
            reminderName = (TextView)itemView.findViewById(R.id.reminderName);
            reminderStart = (ImageButton)itemView.findViewById(R.id.reminderStart);
            reminderEnd = (ImageButton)itemView.findViewById(R.id.reminderEnd);
            reminderProgress = (ProgressBar)itemView.findViewById(R.id.reminderProgress);
            reminderDuration = (TextView)itemView.findViewById(R.id.reminderDuration);
            reminderStatus = (TextView)itemView.findViewById(R.id.reminderStatus);

            reminderStart.setOnClickListener(this);
            reminderEnd.setOnClickListener(this);
//            Log.i("RemindMe", " Clicked on start " + reminderStart.getTag().toString());
        }

        @Override
        public void onClick(View v) {
//            Log.i("RemindMe", " Clicked on Item " + "onclick");
//            myClickListener.onItemClick(getAdapterPosition(), v, this);
            String viewTag = v.getTag().toString();
            switch (viewTag){
                case "ReminderStart":
                    toggleStart();
                    break;
                case "ReminderEnd":
                    toggleWait();
                    break;
            }
        }

        public void setData(Reminder reminder){
            this.reminder = reminder;
        }

        public void toggleStart(){
            clearView();
            reminder.toggleStart();
            reminderProgress.setMax(reminder.getReminderDuration());
            updateView();
        }

        public void toggleWait(){
            clearView();
            reminder.toggleWait();
            reminderProgress.setMax(reminder.getReminderDuration());
            updateView();
        }

        public void bindView(){
            reminderName.setText(reminder.name);
            reminderProgress.setMax(reminder.getReminderDuration());
            if(reminder.needsUpdate())
                updateView();
            else
                clearView();
        }

        public void updateTimeRemaining(){
            if(reminder.needsUpdate()) {
                reminder.updateElapsedDuration();
                if (!reminder.isSnoozed())
                    checkCompletion();
                updateView();
            } else {
                clearView();
            }
        }

        public void checkCompletion(){
            int progress = reminder.getElapsedDuration();
            int max = reminder.getReminderDuration();
            boolean hasCompleted = (progress >= max);
            if (hasCompleted)
                reminder.toggleSnooze();
        }

        public void updateView() {
//            if(!reminder.needsUpdate()) {
//
//                return;
//            }
            int progress = reminder.getElapsedDuration();
            if(reminder.isSnoozed()){
                reminderDuration.setText(reminder.formattedElapsed + "/" + reminder.formattedDuration);
                reminderStatus.setText(reminder.getStatusText());
                reminderProgress.setVisibility(View.GONE);
                reminderDuration.setVisibility(View.VISIBLE);
                return;
            }
//            int max = reminder.getReminderDuration();
//            boolean hasCompleted = (progress >= max);
//            if(hasCompleted) {
////                toggleStop();
//                toggleSnooze();
////                reminderStatus.setText(reminder.getStatusText());
//                return;
//            }
            reminderDuration.setText(reminder.formattedElapsed + "/" + reminder.formattedDuration);
            reminderProgress.setProgress(progress);
            reminderDuration.setVisibility(View.VISIBLE);
            reminderProgress.setVisibility(View.VISIBLE);
            reminderStatus.setText(reminder.getStatusText());
        }

        public void clearView(){
            reminderProgress.setVisibility(View.GONE);
            reminderDuration.setVisibility(View.GONE);
            reminderStatus.setText(reminder.getStatusText());
        }
    }

//    public void setOnItemClickListener(MyClickListener myClickListener) {
//        this.myClickListener = myClickListener;
//    }

    public PresetViewAdapter(List<Reminder> reminderList) {
        this.viewHolders = new ArrayList<>();
        this.reminderList = reminderList;
        startUpdateTimer();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reminder_item, viewGroup, false);
        ReminderViewHolder reminderViewHolder = new ReminderViewHolder(v);
        synchronized (viewHolders) {
            viewHolders.add(reminderViewHolder);
        }
        return reminderViewHolder;
    }

    @Override
    public void onBindViewHolder(final ReminderViewHolder reminderViewHolder, int i) {
        reminderViewHolder.setData(reminderList.get(i));
        reminderViewHolder.bindView();
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

//    public interface MyClickListener {
//        void onItemClick(int position, View v, ReminderViewHolder reminderViewHolder);
//    }
}
