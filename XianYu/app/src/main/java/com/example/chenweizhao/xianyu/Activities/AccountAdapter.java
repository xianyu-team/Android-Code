package com.example.chenweizhao.xianyu.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenweizhao.xianyu.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountAdapter extends ArrayAdapter<Account> {
    private int resourceId;
    private Context context;

    public AccountAdapter (Context context, int textViewResourceId, List<Account> accounts) {
        super(context, textViewResourceId, accounts);
        this.context = context;
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Account account = getItem(position);
        View view;
        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
            viewHolder.icon = view.findViewById(R.id.account_profile);
            viewHolder.userName =  view.findViewById(R.id.account_nickname);
            viewHolder.schoolName = view.findViewById(R.id.account_university);
            viewHolder.right = view.findViewById(R.id.account_right);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (account.getIcon() != null) {
            viewHolder.icon.setImageBitmap(stringToBitmap(account.getIcon()));
        }
        else {
            viewHolder.icon.setImageResource(R.drawable.user);
        }
        viewHolder.userName.setText(account.getUserName());
        viewHolder.schoolName.setText(account.getSchoolName());
        viewHolder.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("userId", account.getUserId());
                Intent intent = new Intent(context, FollowDetailActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }


    class ViewHolder {
        CircleImageView icon;
        TextView userName;
        TextView schoolName;
        ImageView right;
    }

    private Bitmap stringToBitmap (String src){
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(src, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
