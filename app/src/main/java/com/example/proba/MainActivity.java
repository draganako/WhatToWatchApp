package com.example.proba;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.proba.adapters.TitleAdapter;
import com.example.proba.datamodels.Title;
import com.example.proba.datamodels.TitleData;
import com.example.proba.datamodels.User;
import com.example.proba.datamodels.UserData;
import com.example.proba.ui.home.HomeFragment;
import com.example.proba.ui.home.PopularTitlesFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private boolean isChecked = false;
    private Button sortButton;
    private String username;
    private SharedPreferences sharedPref;
    private FirebaseAuth mfirebaseAuth;
    private SearchView searchView;
    private TitleAdapter ura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        fillDatabase();//////////////////////////////////////////////////////

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ExtendedFloatingActionButton fab = findViewById(R.id.extended_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        ImageView pp = (ImageView) header.findViewById(R.id.imageViewProfilePic);
        TextView usnTv = (TextView) header.findViewById(R.id.textViewProfileName);

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangeProfileActivity.class);
                startActivity(intent);
            }
        });

        sharedPref = getApplicationContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
        User user = UserData.getInstance().getUserByUsername(username);
        usnTv.setText(username);
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
        }); //closing the setOnClickListener method

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void changeMode(View view){

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void fillDatabase()
    {
       Title newTitle = new Title();
       newTitle.name = "NOVIII";
       newTitle.synopsis = "objectType";
       newTitle.image = "";
       newTitle.year=1997;

       TitleData.getInstance().AddTitle(newTitle);

       newTitle=new Title("");
       TitleData.getInstance().AddTitle(newTitle);

    }
}