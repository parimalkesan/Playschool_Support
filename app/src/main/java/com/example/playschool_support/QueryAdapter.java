package com.example.playschool_support;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import Model.Users;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.ViewHolder> {
    private Context context;
    private List<Users> userList;

    public QueryAdapter(Context context, List<Users> users)
    {
        this.context=context;
        this.userList=users;
    }

    //to provide layout for each view in recyclerview
    public QueryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_data_list,parent,false);
        return new QueryAdapter.ViewHolder(view);
    }

    @Override
    //to set userName_tv and user_prof_image for each view in recyclerview
    public void onBindViewHolder(@NonNull QueryAdapter.ViewHolder holder, int position) {
        //set username
        final Users user = userList.get(position);
        holder.userName_tv.setText(user.getUserName());

        //set user profile image
        if ("default".equals(user.getImageUrl())) {
            holder.user_prof_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(user.getImageUrl()).into(holder.user_prof_image);
        }
        holder.lastMsgTv.setVisibility(View.GONE);
        holder.user_timestamp.setVisibility(View.GONE);

        //set onClickListener on each view in recyclerview to go to ConversationActivity class
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ConversationActivity.class);
                intent.putExtra("userid", user.getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //Viewholder to provide a holder for each view in recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName_tv;
        public ImageView user_prof_image;
        private TextView lastMsgTv;
        private  TextView user_timestamp;
        public ViewHolder(View view)
        {
            super(view);
            userName_tv=view.findViewById(R.id.userName_tv);
            user_prof_image=view.findViewById(R.id.user_prof_image);
            lastMsgTv=view.findViewById(R.id.last_msg_tv);
            user_timestamp=view.findViewById(R.id.user_timestamp);
        }
    }
}
