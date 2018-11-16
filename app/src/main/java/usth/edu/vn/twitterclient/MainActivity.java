package usth.edu.vn.twitterclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private ActionBar toolbar;
    private BottomNavigationView bottomNavigationView;


    private DrawerLayout drawerLayout;
   // private RecyclerView postList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, postsRef;

    private CircleImageView navProfileImage;
    private TextView navProfileUserFullName;
    private TextView navProfileUserName;
    private FloatingActionButton fab;
    private RecyclerView postList;
    private HomeFragment mHome;

    String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            currentUserId = mAuth.getCurrentUser().getUid();

        }
        //check real-time database by using the user reference
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        postsRef=FirebaseDatabase.getInstance().getReference().child("Posts");

        BottomNavigationView navigation = findViewById(R.id.nav_bottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        mToolbar = findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        fab= findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              sendUserToPostActivity();
            }
        });

        drawerLayout = findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
//        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
//        mToolbar.setNavigationIcon(R.drawable.profile);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bottomNavigationView=findViewById(R.id.nav_bottom);



        navigationView = findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        navProfileImage = navView.findViewById(R.id.nav_profile_image);
        navProfileUserFullName =navView.findViewById(R.id.nav_user_fullname);
        navProfileUserName =navView.findViewById(R.id.nav_user_username);

//        postList= findViewById(R.id.all_users_post_list);
//        postList.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//        postList.setLayoutManager(linearLayoutManager);
////
//        displayAllUserPost();

        //currentUser who is online
        if(mAuth.getCurrentUser() != null) {

            userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && mAuth.getCurrentUser()!=null){
                    if(dataSnapshot.hasChild("username")) {
                        String username =dataSnapshot.child("username").getValue().toString();
                        navProfileUserName.setText(username);
                    }

                    if(dataSnapshot.hasChild("fullname")) {
//                        String image =dataSnapshot.child("profileImage").getValue().toString();
//                        Toast.makeText(MainActivity.this,"......",Toast.LENGTH_SHORT).show();

                        String fullname =dataSnapshot.child("fullname").getValue().toString();
                        navProfileUserFullName.setText(fullname);
                    }
                    if(dataSnapshot.hasChild("profileImage")) {
                        String image =dataSnapshot.child("profileImage").getValue().toString();
////                        Toast.makeText(MainActivity.this,"......",Toast.LENGTH_SHORT).show();
//
//                        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(navProfileImage);
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(navProfileImage);
//                    Glide.with(MainActivity.this).load(image).into(navProfileImage);
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Profile Name do not exist..",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });}

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });
    }
//
//    private void displayAllUserPost() {
//
//        FirebaseRecyclerOptions<Posts> options =
//                new FirebaseRecyclerOptions.Builder<Posts>()
//                .setQuery(postsRef ,Posts.class)
//                .build();
//        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Posts,PostsViewHolder>( options) {
//            @NonNull
//            @Override
//            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view = LayoutInflater.from(viewGroup.getContext())
//                        .inflate(R.layout.all_posts_layout,viewGroup, false);
//                return new PostsViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Posts model) {
//                    holder.setFullname(model.getFullname());
//                    holder.setTime(model.getTime());
//                    holder.setDate(model.getDate());
//                    holder.setDescription(model.getDescription());
//                    holder.setProfileImage(model.getProfileImage());
//                    holder.setTweetImage(model.getTweetImage());
//
//            }
//        };
//
//        postList.setAdapter(adapter);
////
////        FirebaseRecyclerAdapter<Posts,PostsViewHolder> firebaseRecyclerAdapter
////                = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(
////                        Posts.class, R.layout.all_posts_layout, PostsViewHolder.class, ) {
////            @Override
////            protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Posts model) {
////
////            }
////
////            @NonNull
////            @Override
////            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
////                return null;
////            }
////        }
//
//    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView= itemView;
        }
        public void setFullname(String fullname) {
            TextView userName =mView.findViewById(R.id.post_user_name);
            userName.setText(fullname);
        }
        public void setProfileImage ( String profileImage) {
            CircleImageView image  =mView.findViewById(R.id.post_profile_image);
            Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(image);
        }
        public void setTime(String time) {
            TextView postTime =mView.findViewById(R.id.post_time);
            postTime.setText("  "+time);
        }
        public void setDate(String date) {
            TextView postDate =mView.findViewById(R.id.post_date);
            postDate.setText(date);
        }
        public void setDescription(String description) {
            TextView postDecription =mView.findViewById(R.id.post_description);
            postDecription.setText(description);
        }
        public void setTweetImage(String tweetImage) {
            ImageView postImage  =mView.findViewById(R.id.post_image);
            Picasso.get().load(tweetImage).placeholder(R.drawable.profile).into(postImage);
        }
    }


    private void sendUserToPostActivity() {
        Intent intent= new Intent(getApplicationContext(),PostActivity.class);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home:
                   mToolbar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.search:
                    mToolbar.setTitle("Search");
                    fragment= new SearchFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.notification:
                    mToolbar.setTitle("Notification");
                    fragment= new NotificationFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.message:
                    mToolbar.setTitle("Message");
                    fragment= new MessageFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//basically implement a chord to check the user authentication
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser= mAuth.getCurrentUser();
        //the user is not authenticated
        if(currentUser==null){
            SendUserToLoginActivity();
        }
        else {
            checkUserExistence();
        }
    }

    private void checkUserExistence() {
        final String  currentUserId = mAuth.getCurrentUser().getUid();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(currentUserId)){
                    sendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToSetupActivity() {
        Intent setupIntent =new Intent(MainActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent =new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
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
