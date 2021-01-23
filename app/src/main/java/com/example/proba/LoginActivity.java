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

import com.example.proba.datamodels.Title;
import com.example.proba.datamodels.TitleData;
import com.example.proba.datamodels.User;
import com.example.proba.datamodels.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private UserData userData;
    private TextInputLayout txtPassword;
    private TextInputLayout txtEmail;
    private TextView txtOrSignup;
    private Button loginB;
    private ProgressDialog progress;
    private SharedPreferences sharedPref;
    private TitleData titleData;//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("");

        firebaseAuth = FirebaseAuth.getInstance();
        userData.getInstance().getUsers();

        progress = new ProgressDialog(this);
        txtEmail = (TextInputLayout) findViewById(R.id.emailLogin);
        txtPassword = (TextInputLayout) findViewById(R.id.passLogin);
        txtOrSignup = (TextView) findViewById(R.id.orSignupText);

        loginB = (Button) findViewById(R.id.doneLoginButton);
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();

            }
        });

        txtOrSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

       // userData.getInstance().getUsers();
    }

        private void loginUser()
        {
            String email = txtEmail.getEditText().getText().toString();
            String pass = txtPassword.getEditText().getText().toString();

            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(getApplicationContext(), "Bad email", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(pass))
            {
                Toast.makeText(getApplicationContext(), "Bad password", Toast.LENGTH_SHORT).show();
                return;
            }

            progress.setMessage("Logging in...");
            progress.show();

            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        ArrayList<User> probepos = new ArrayList<>();
                        int indexx = -1;

                        probepos = userData.getInstance().getUsers();
                        for(int i =0; i<probepos.size(); i++)
                        {
                            String a = probepos.get(i).email;
                            if( a.compareTo(email) == 0)
                            {
                                indexx = i;
                            }
                        }
                        User uu = new User();
                        if(indexx != -1)
                        {
                            uu = probepos.get(indexx);

                            Context context = getApplicationContext();
                            sharedPref = context.getSharedPreferences(
                                    "Userdata", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.loggedUser_email), uu.email);
                            editor.putString(getString(R.string.loggedUser_username), uu.username);
                            editor.putString(getString(R.string.loggedUser_image), uu.picture);


                            editor.putInt(getString(R.string.loggedUser_index), indexx);
                            editor.commit();

                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Could not login! ", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }
            });
        }
    }
