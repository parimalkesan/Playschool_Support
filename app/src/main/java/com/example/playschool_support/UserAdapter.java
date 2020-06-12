package com.example.playschool_support;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Model.Message;
import Model.Users;
//adapter class to recyclerview in ChatFragment
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>
{
    private String lastMessage;
    public static String tstamp;
    private Context context;
    private List<Users> userList;

    public UserAdapter(Context context,List<Users> users)
    {
        this.context=context;
        this.userList=users;
    }

    //to provide layout for each view in recyclerview
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_data_list,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    //to set userName_tv and user_prof_image for each view in recyclerview
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //set username
        final Users user = userList.get(position);
        holder.userName_tv.setText(user.getUserName());

        //set user profile image
        if ("default".equals(user.getImageUrl())) {
            holder.user_prof_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(user.getImageUrl()).into(holder.user_prof_image);
        }

        //display last message sent or received for each user
        last_Message(user.getUserId(), holder.lastMsgTv, holder.user_timestamp);

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
    //check for last message sent or received for a user and its timestamp
    private void last_Message(final String userId, final TextView lastMsg,final TextView user_timestamp)
    {
        lastMessage="default";
        tstamp="";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    //check for msgs between current user and other user
                    Message message=dataSnapshot1.getValue(Message.class);
                    if(firebaseUser!=null&&firebaseUser.getUid().equals(message.getSenderId()) && userId.equals(message.getReceiverId())
                            || firebaseUser!=null&&userId.equals(message.getSenderId()) && firebaseUser.getUid().equals(message.getReceiverId()))
                    {
                        //set last message
                        lastMessage=message.getMessage();

                        //get timestamp of last message
                        String timestamp=message.getTimestamp();
                        Calendar cal= Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(Long.parseLong(timestamp));
                        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        if(currentDate.equals(DateFormat.format("dd/MM/yyyy",cal).toString()))
                        {
                            tstamp= DateFormat.format("hh:mm aa",cal).toString();
                        }
                        else
                        {
                            tstamp=DateFormat.format("dd/MM/yyyy",cal).toString();
                        }
                    }
                }
                //set lst message for each user
                switch (lastMessage)
                {
                    case "default":lastMsg.setText("No Message");
                        break;
                    default:lastMsg.setText(lastMessage);
                        break;
                }
                //set the timestamp for each user
                switch (tstamp)
                {
                    case "":user_timestamp.setText("");
                        break;
                    default:user_timestamp.setText(tstamp);
                        break;
                }
                lastMessage="default";
                tstamp="";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
