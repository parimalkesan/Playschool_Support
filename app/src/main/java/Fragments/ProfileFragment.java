package Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.playschool_support.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import Model.Users;
import de.hdodenhof.circleimageview.CircleImageView;
//to display profile of current user
public class ProfileFragment extends Fragment {

    CircleImageView prof_image_view;
    TextView prof_email;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //get reference to prof_image_view and prof_email_tv
        prof_image_view = view.findViewById(R.id.prof_image_view);
        prof_email = view.findViewById(R.id.prof_email_tv);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        //set email and image for current user
        prof_email.setText(firebaseUser.getEmail());
        prof_image_view.setImageResource(R.mipmap.ic_launcher);

        return view;
    }
}
