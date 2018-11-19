package usth.edu.vn.twitterclient.findpeople;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import usth.edu.vn.twitterclient.HomeFragment;
import usth.edu.vn.twitterclient.MainActivity;
import usth.edu.vn.twitterclient.Posts;
import usth.edu.vn.twitterclient.R;

public class FindFriendsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageButton searchButton;
    private EditText searchInputText;
    private DatabaseReference allUserDatabaseRef;

    private RecyclerView searchResultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        mToolbar=findViewById(R.id.find_frends_toolbar);
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New message");

        allUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        searchResultList = findViewById(R.id.search_result_list);
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));

        searchButton = findViewById(R.id.search_people);
        searchInputText = findViewById(R.id.search_box_input);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBoxInput =searchInputText.getText().toString();
                searchPeopleAndFriends(searchBoxInput);
            }
        });
    }

    private void searchPeopleAndFriends(String searchBoxInput) {
        Toast.makeText(this,"Searching...",Toast.LENGTH_LONG).show();

        Query searchPeopleandFriendsQuery = allUserDatabaseRef.orderByChild("fullname")
                .startAt(searchBoxInput).endAt(searchBoxInput+ "\uf8ff");

        FirebaseRecyclerOptions<FindFrends> options =
                new FirebaseRecyclerOptions.Builder<FindFrends>()
                        .setQuery(searchPeopleandFriendsQuery ,FindFrends.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<FindFrends,FindFriendsViewHolder>( options) {

            @NonNull
            @Override
            public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.all_users_display_layout,viewGroup, false);
                return new FindFriendsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull FindFrends model) {
                holder.setFullname(model.getFullname());
                holder.setUsername(model.getUsername());
                holder.setProfileImage( model.getProfileImage());
            }
        };
        adapter.startListening();
        searchResultList.setAdapter(adapter);
    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = mView;
        }
        public void setProfileImage(String profileImage) {
            CircleImageView myImage =mView.findViewById(R.id.all_users_profile_image);
            Picasso.get().load(profileImage).into(myImage);

        }
        public void setFullname(String fullname) {
            TextView myName =mView.findViewById(R.id.all_users_profile_fullname);
            myName.setText(fullname);
        }
        public void setUsername(String username) {
            TextView myUserName =mView.findViewById(R.id.all_users_username);
            myUserName.setText(username);
        }

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
        Intent intent = new Intent(FindFriendsActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
