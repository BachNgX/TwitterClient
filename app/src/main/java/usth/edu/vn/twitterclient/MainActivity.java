package usth.edu.vn.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private BottomNavigationView navigationView2;

    private DrawerLayout drawerLayout;
    private RecyclerView postList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth=FirebaseAuth.getInstance();

        navigationView2= findViewById(R.id.nav_bottom) ;

        mToolbar = findViewById(R.id.main_page_Toobar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");


        drawerLayout = findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView = findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                UserMenuSelector(item);
                return false;
            }
        });

    }

//basically implement a chord to check the user authentication
    @Override
    protected void onStart() {
        FirebaseUser currentUser= mAuth.getCurrentUser();
        //the user is not authenticated
        if(currentUser==null){
            SendUserToLoginActivity();
        }
        super.onStart();
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent =new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }



    //the bar left clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return  super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_profile:
                Toast.makeText(this, "Profile", LENGTH_SHORT).show();
                break;

            case R.id.nav_lists:
                Toast.makeText(this, "List", LENGTH_SHORT).show();
                break;

            case R.id.nav_bookmarks:
                Toast.makeText(this, "Bookmarks", LENGTH_SHORT).show();
                break;

            case R.id.nav_moments:
                Toast.makeText(this, "Moment", LENGTH_SHORT).show();
                break;

            case R.id.nav_setting:
                Toast.makeText(this, "Setting and Privacy", LENGTH_SHORT).show();
                break;

            case R.id.nav_help:
                Toast.makeText(this, "Help Center", LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
        }
    }
}
