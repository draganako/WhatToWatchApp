package com.example.proba;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.proba.adapters.TitleAdapter;
import com.example.proba.datamodels.Title;
import com.example.proba.datamodels.TitleData;
import com.example.proba.datamodels.User;
import com.example.proba.datamodels.UserData;
import com.example.proba.ui.favoriti.FavoritiFragment;
import com.example.proba.ui.home.HomeFragment;
import com.example.proba.ui.home.PopularTitlesFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private boolean isChecked = false;
    private Button sortButton;
    private String username;
    private SharedPreferences sharedPref;
    private FirebaseAuth mfirebaseAuth;
    private SearchView searchView;
    private TitleAdapter ura;
    private TextView usnTv;
    private ImageView pp;

    /*FirebaseStorage storage;
    private StorageReference storageReference;
    String firestorageUri = null;*/

    // private List<Title> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);*/
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favoriti)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View header = navigationView.getHeaderView(0);
        usnTv = (TextView) header.findViewById(R.id.textViewProfileName);
        ImageView imageViewEdit = (ImageView)header.findViewById(R.id.imageViewEdit);
        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChangeProfileActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("picture", sharedPref.getString(getString(R.string.loggedUser_image), "EMPTY"));
                startActivityForResult(intent,1234);
            }
        });

        sharedPref = getApplicationContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        User user = UserData.getInstance().getUserByUsername(username);
        usnTv.setText(username);
        pp = (ImageView) header.findViewById(R.id.imageViewProfilePic);
        setProfilePic(sharedPref.getString(getString(R.string.loggedUser_image), "EMPTY"));

        mfirebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = mfirebaseAuth.getCurrentUser();
        
        navigationView.getMenu().getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(firebaseUser != null)
                {
                    sharedPref = getApplicationContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.remove(getString(R.string.loggedUser_username));
                    editor.remove(getString(R.string.loggedUser_email));
                    editor.remove("userImage");
                    editor.remove(getString(R.string.loggedUser_index));
                    editor.commit();

                    mfirebaseAuth.signOut();

                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Could not log out!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        //for dark theme
        navigationView.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().toString().compareTo("Dark mode")==0)
                    item.setTitle("Light mode");
                else
                    item.setTitle("Light mode");
                //changeMode();
                return true;
            }
        });

        sortButton=(Button) findViewById(R.id.extended_fab);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, sortButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_choose_filter, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.none:
                                ura = HomeFragment.popularTitlesFragment.adapter;
                                ura.filterTitles(searchView.getQuery(),0);
                                return true;
                            case R.id.movies:
                                ura = HomeFragment.popularTitlesFragment.adapter;
                                ura.filterTitles(searchView.getQuery(),1);
                                return true;
                            default://series
                                ura = HomeFragment.popularTitlesFragment.adapter;
                                ura.filterTitles(searchView.getQuery(),2);
                                return true;
                        }
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        searchView=findViewById(R.id.search_titles);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (newText!=null)
                {
                    ura = HomeFragment.popularTitlesFragment.adapter;
                    ura.filterTitles(newText,0);
                }
                return false;

            }


        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1234) : {
                if (resultCode == Activity.RESULT_OK) {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
                    setProfilePic(sharedPref.getString(getString(R.string.loggedUser_image), "EMPTY"));
                    username=sharedPref.getString(getString(R.string.loggedUser_username),"EMPTY");
                    usnTv.setText(username);
                    String email=sharedPref.getString(getString(R.string.loggedUser_email),"EMPTY");
                    UserData.getInstance().updateUserProfile(email,
                            username,sharedPref.getString(getString(R.string.loggedUser_image), "EMPTY"));
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuItem item=menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setProfilePic(String profilePhotoUri)
    {
        if (profilePhotoUri != null && !profilePhotoUri.equals("")) {
            Glide.with(this).load(profilePhotoUri).into(pp);
        } else {
            pp.setImageResource(R.drawable.ic_user_24);
        }
    }

}