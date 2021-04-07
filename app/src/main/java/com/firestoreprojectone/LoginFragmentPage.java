package com.firestoreprojectone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragmentPage extends Fragment {
    EditText et_USERNAME, et_PWD;
    Button btnLogin;
    TextView tvRegisterhere;
    ProgressDialog loadingBar;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String fg;
    View view;

    public LoginFragmentPage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login, container, false);

        et_USERNAME = (EditText)view. findViewById(R.id.et_USERNAME);
        et_PWD = (EditText)view. findViewById(R.id.et_PWD);

        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         fg=sharedPreferences.getString("logedin","no");


        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();


        loadingBar = new ProgressDialog(getContext());


        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();


            }
        });
        return view;
    }

    private void LoginUser() {
        String username = et_USERNAME.getText().toString().trim();
        String password = et_PWD.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Please enter your Username...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter your password...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password Must be >= 6 Characters", Toast.LENGTH_SHORT).show();
            return;
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(username, password);

        }
    }

    String str1;
    private void AllowAccessToAccount(final String username, final String password) {

        fAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loadingBar.dismiss();
                    Toast.makeText(getContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    String[] str=username.split("@");
                    for(int i=0; i< str.length; i++)
                    {
                         str1=str[0];
                    }
                    Intent intent=new Intent(getContext(),DashboardActivity.class);
                    intent.putExtra("message","  Welocome to dashboard "+str1);
                    startActivity(intent);
                    resetFields();

                } else {
                    Toast.makeText(getContext(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }
        });
    }

    private void resetFields(){
        et_USERNAME.getText().clear();
        et_PWD.getText().clear();
    }
}