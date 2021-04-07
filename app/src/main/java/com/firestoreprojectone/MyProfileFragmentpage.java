package com.firestoreprojectone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class MyProfileFragmentpage extends Fragment {
    EditText etEmail, etpassword, etName, etDob, etCity;
    ProgressDialog loadingBar;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    String userId;
    RadioGroup radioGroup;
    RadioButton radioMale, radioFemale;


    public MyProfileFragmentpage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, viewGroup, false);

        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etpassword = (EditText) view.findViewById(R.id.etpassword);
        etName = (EditText) view.findViewById(R.id.etName);
        etDob = (EditText) view.findViewById(R.id.etDob);
        etCity = (EditText) view.findViewById(R.id.etCity);


        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioMale = (RadioButton)view. findViewById(R.id.radioMale);
        radioFemale = (RadioButton)view. findViewById(R.id.radioFemale);
        getUserData();
        return view;
    }

    public void getUserData() {
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("User_Details").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    etEmail.setText(documentSnapshot.getString("firestore_password"));
                    etName.setText(documentSnapshot.getString("firestore_name"));
                    etpassword.setText(documentSnapshot.getString("firestore_email"));
                    etCity.setText(documentSnapshot.getString("firestore_city"));
                    etDob.setText(documentSnapshot.getString("firestore_birthdate"));
                    if(documentSnapshot.getString("firestore_gender").equals("Male")){
                        radioMale.setChecked(true);
                    }
                    else {
                        radioFemale.setChecked(true);
                    }


                } else {
                    Toast.makeText(getContext(), "Somethig went wrong please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}