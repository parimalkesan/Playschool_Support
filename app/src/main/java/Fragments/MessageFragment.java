package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.playschool_support.R;
import com.example.playschool_support.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Model.Message;
import Model.Users;

//to display all the users who messaged us
public class MessageFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> userList;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;

    //to store id of users we had chat
    private List<String> userList1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        //get recycler view and linear layout manager
        recyclerView = view.findViewById(R.id.recycler_view_chathistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //get current user,firestore and database references to root "chats"
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        firebaseFirestore=FirebaseFirestore.getInstance();

        userList1 = new ArrayList<>();

        //put userids in userlist1 with whom we had chat from root ""chats
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList1.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Message message = dataSnapshot1.getValue(Message.class);
                    if(firebaseUser.getUid().equals(message.getSenderId()))
                    {
                        userList1.add(message.getReceiverId());
                    }
                    if(firebaseUser.getUid().equals(message.getReceiverId()))
                    {
                        userList1.add(message.getSenderId());
                    }
                }
                //call chatUsers
                chatUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return view;
    }
    //get list of users who had chats with current user and add users other than current user to userList
    private void chatUsers()
    {
        //initialize userlist to store details of users
        userList=new ArrayList<>();

        //get list of all users we had chat with
        firebaseFirestore.collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        userList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Users user = doc.toObject(Users.class);
                            //match both the lists to find the user with which we had chat
                            for (String id : userList1) {
                                if (id.equals(user.getUserId())) {
                                    if (userList.size() != 0) {
                                        int z = 0;
                                        for (int i = 0; i < userList.size(); i++) {
                                            Users user1 = userList.get(i);
                                            if (user.getUserId().equals(user1.getUserId())) {
                                                z = 1;
                                            }
                                        }
                                        if (z == 0) {
                                            userList.add(user);
                                        }
                                    } else {
                                        userList.add(user);
                                    }
                                }
                            }
                        }
                        //create useradapter for recyclerView
                        Collections.reverse(userList);
                        userAdapter=new UserAdapter(getContext(),userList);
                        recyclerView.setAdapter(userAdapter);
                    }
                });
    }
}
