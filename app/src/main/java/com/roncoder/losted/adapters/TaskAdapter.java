package com.roncoder.losted.adapters;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roncoder.losted.R;
import com.roncoder.losted.models.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private final ArrayList<Task> tasks;
    private OnItemTaskClickListener listener;

    public TaskAdapter(ArrayList<Task> tasks){
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_task_item,
                parent,
                false
        ), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = tasks.get(position);
        holder.task.setText(task.getMessage());
        holder.task.setChecked(task.isFinish());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public interface OnItemTaskClickListener{
        void onClick(int position, View v);
    }

    public void setOnItemTaskClickListener(OnItemTaskClickListener listener){
        this.listener = listener;
    }

    public static class TaskHolder extends RecyclerView.ViewHolder{

        CheckBox task;

        public TaskHolder(@NonNull View itemView, OnItemTaskClickListener listener) {
            super(itemView);

            task = itemView.findViewById(R.id.task_id);

            itemView.setOnClickListener(v-> {
                int position = getAdapterPosition();
                if(position != NO_POSITION){
                    listener.onClick(position, v);
                }
            });
        }
    }
}
