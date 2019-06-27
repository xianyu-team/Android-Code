package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenweizhao.xianyu.R;

import java.util.List;


public class AssignmentAdapter extends ArrayAdapter<AssignmentCommon> {
    private int resourceId;
    private Context context;

    public AssignmentAdapter (Context context, int textViewResourceId, List<AssignmentCommon> assignmentCommons) {
        super(context, textViewResourceId, assignmentCommons);
        this.context = context;
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AssignmentCommon assignmentCommon = getItem(position);
        View view;
        AssignmentAdapter.ViewHolder viewHolder;
        viewHolder = new AssignmentAdapter.ViewHolder();
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
            viewHolder.description = view.findViewById(R.id.item_description);
            viewHolder.time =  view.findViewById(R.id.item_time);
            viewHolder.money = view.findViewById(R.id.item_money);
            viewHolder.right = view.findViewById(R.id.item_detail);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (AssignmentAdapter.ViewHolder) view.getTag();
        }

        viewHolder.description.setText(assignmentCommon.getTask_sketch());
        viewHolder.time.setText(assignmentCommon.getTask_publishDate());
        viewHolder.money.setText(String.valueOf(assignmentCommon.getTask_bonus()));
        viewHolder.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SendTaskActivity.class);
                intent.putExtra("user_id", assignmentCommon.getUser_id());
                intent.putExtra("task_id", assignmentCommon.getTask_id());
                intent.putExtra("sketch", assignmentCommon.getTask_sketch());
                context.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        TextView description;
        TextView time;
        TextView money;
        ImageView right;
    }
}
