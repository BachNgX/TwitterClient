package usth.edu.vn.twitterclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import usth.edu.vn.twitterclient.Profile.Likes;
import usth.edu.vn.twitterclient.Profile.Media;
import usth.edu.vn.twitterclient.Profile.TweetsOfProfile;
import usth.edu.vn.twitterclient.Profile.Tweets_replies;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;



    private TextView username, fullname;
    private CircleImageView userProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        mAuth =FirebaseAuth.getInstance();
        currentUserId =mAuth.getCurrentUser().getUid();
        profileUserRef =FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        mToolbar=findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });


        fullname = findViewById(R.id.fullname_profile);
        username = findViewById(R.id.username_profile);
        userProfileImage = findViewById(R.id.i_profile);

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String myFullname = dataSnapshot.child("fullname").getValue().toString();
                String myUsername = dataSnapshot.child("username").getValue().toString();
                String myProfileImage = dataSnapshot.child("profileImage").getValue().toString();

                Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);
                fullname.setText(myFullname);
                username.setText(myUsername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =  item.getItemId();
        if(id == android.R.id.home) {
            sendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }
    private void sendUserToMainActivity() {
        Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(intent);

    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TweetsOfProfile(), "Tweets");
        adapter.addFrag(new Tweets_replies(),"Tweets&  replies");
        adapter.addFrag(new Media(),"Media");
        adapter.addFrag(new Likes(),"Likes");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int postion){
            return mFragmentList.get(postion);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
