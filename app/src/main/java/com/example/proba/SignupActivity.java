package com.example.proba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proba.datamodels.User;
import com.example.proba.datamodels.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity
{

    Button Signin;
    TextInputLayout txtUsername;
    TextInputLayout txtEmail;
    TextInputLayout txtPassword;
    TextView orLogIn;

    UserData userData;

    private ProgressDialog progress;
    private FirebaseAuth firebaseAuth;
    //private FirebaseStorage.getInstance().get.. --za slike
    private boolean successfull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("");
        userData.getInstance().getUsers();
        firebaseAuth = FirebaseAuth.getInstance();

        progress = new ProgressDialog(this);

        txtPassword = (TextInputLayout)   findViewById(R.id.passSignup);
        txtEmail = (TextInputLayout)   findViewById(R.id.emailSignup);
        txtUsername=(TextInputLayout) findViewById(R.id.usernameSignup);

        Signin = (Button) findViewById(R.id.doneSignupButton);

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                boolean proceedWithRegistration=true;

                if(userData.getInstance().getUserByUsername(txtUsername.getEditText().getText().toString())!=null)
                {
                    proceedWithRegistration = false;
                    Toast.makeText(getApplicationContext(), "Entered username is already taken", Toast.LENGTH_LONG).show();
                }
                if(proceedWithRegistration)
                    registerUser();
            }
        });

        orLogIn = (TextView) findViewById(R.id.orLoginText);
        orLogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void registerUser()
    {
        String email = txtEmail.getEditText().getText().toString();
        String pass = txtPassword.getEditText().getText().toString();

        final String name=txtUsername.getEditText().getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(), "Bad email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(getApplicationContext(), "Bad password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.setMessage("Registering...");
        progress.show();

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    User u = new User();
                    u.username =name;
                    u.email = email;
                    u.picture = "";

                    userData.getInstance().AddUser(u);
                    Toast.makeText(getApplicationContext(), "Successfully registered ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Could not register", Toast.LENGTH_SHORT).show();
                }

            }
        });

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    ArrayList<User> probepos = new ArrayList<>();
                    int indexx = -1;

                    probepos = userData.getInstance().getUsers();
                    for (int i = 0; i < probepos.size(); i++) {
                        String a = probepos.get(i).email;
                        if (a.compareTo(email) == 0) {
                            indexx = i;
                        }
                    }
                    User uu;
                    if (indexx != -1)
                    {
                        uu = probepos.get(indexx);

                        Context context = getApplicationContext();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                "Userdata", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.loggedUser_email), uu.email);
                        editor.putString(getString(R.string.loggedUser_username), uu.username);
                        editor.putString(getString(R.string.loggedUser_image), uu.picture);

                        editor.putInt(getString(R.string.loggedUser_index), indexx);
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(), "Could not login! ", Toast.LENGTH_SHORT).show();
                }
                progress.dismiss();
            }
        });

    }
}