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
import com.example.playschool_support.QueryAdapter;
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
import java.util.List;

import Model.Message;
import Model.Users;

//distinguish between unresolved and resolved
public class SupportFragment extends Fragment {

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;

    //to store id of unresolved queries
    private List<String> userList1;
    //to store ids of resolved queries
    private  List<String> userList2;

    //to store unres and res user details
    private List<Users> unresList;
    private List<Users> resList;

    //unres,resolved recyclerview
    private RecyclerView unResrecyclerView;
    private RecyclerView resRecyclerView;

    //adapter for both recyclerviews
    private QueryAdapter queryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        //get unresrecycler view and linear layout manager
        unResrecyclerView = view.findViewById(R.id.unresolved_recyclerview);
        unResrecyclerView.setHasFixedSize(true);
        unResrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //get resrecycler view and linear layout manager
        resRecyclerView = view.findViewById(R.id.resolved_recyclerview);
        resRecyclerView.setHasFixedSize(true);
        resRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //get current user,firestore and database references to root "chats"
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        firebaseFirestore=FirebaseFirestore.getInstance();

        //initialize both lists
        userList1=new ArrayList<>();
        userList2=new ArrayList<>();

        //put unresolved userids in userlist1 and resolved userids in userlist2
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList1.clear();
                userList2.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Message message = dataSnapshot1.getValue(Message.class);
                    //if the query is not resolved add the userid to userlist1
                    if("false".equals(message.getIsResolved()))
                    {
                        if(firebaseUser.getUid().equals(message.getSenderId()))
                        {
                            userList1.add(message.getReceiverId());
                        }
                        if(firebaseUser.getUid().equals(message.getReceiverId()))
                        {
                            userList1.add(message.getSenderId());
                        }
                    }
                    //if query is resolved add the userid to userlist2
                    if("true".equals(message.getIsResolved())) {
                        if (firebaseUser.getUid().equals(message.getSenderId())) {
                            userList2.add(message.getReceiverId());
                        }
                        if (firebaseUser.getUid().equals(message.getReceiverId())) {
                            userList2.add(message.getSenderId());
                        }
                    }
                }
                //call getUserLists to get lists of res and unres users
                getUserLists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }

    private void getUserLists()
    {

        unresList=new ArrayList<>();
        resList=new ArrayList<>();

        //add user details into unreslist and reslist according to resolved status
        firebaseFirestore.collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        unresList.clear();
                        resList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Users user = doc.toObject(Users.class);
                            //add users from userlist1 to unreslist
                            for (String id : userList1) {
                                if (id.equals(user.getUserId())) {
                                    if (unresList.size() != 0) {
                                        int z = 0;
                                        for (int i = 0; i < unresList.size(); i++) {
                                            Users user1 = unresList.get(i);
                                            if (user.getUserId().equals(user1.getUserId())) {
                                                z = 1;
                                            }
                                        }
                                        if (z == 0) {
                                            unresList.add(user);
                                        }
                                    } else {
                                        unresList.add(user);
                                    }
                                }
                            }
                            //add users from userlist2 to reslist
                            for (String id : userList2) {
                                if (id.equals(user.getUserId())) {
                                    if (resList.size() != 0) {
                                        int z = 0;
                                        for (int i = 0; i < resList.size(); i++) {
                                            Users user1 = resList.get(i);
                                            if (user.getUserId().equals(user1.getUserId())) {
                                                z = 1;
                                            }
                                        }
                                        if (z == 0) {
                                            resList.add(user);
                                        }
                                    } else {
                                        resList.add(user);
                                    }
                                }
                            }
                        }
                        //create adapters for recyclerViews
                        unResrecyclerView.setAdapter(new QueryAdapter(getContext(),unresList));
                        resRecyclerView.setAdapter(new QueryAdapter(getContext(),resList));
                        }
                    });
                }
    }

