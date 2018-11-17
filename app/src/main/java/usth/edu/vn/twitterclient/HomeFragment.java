package usth.edu.vn.twitterclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {
    private RecyclerView postList;
    private FloatingActionButton fab;
    private DatabaseReference postsRef;
    private FirebaseAuth mAuth;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postsRef=FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fab =  view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToPostActivity();
            }
        });
        postList = view.findViewById(R.id.all_users_post_list1);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        displayAllUserPost();
        postList.setLayoutManager(linearLayoutManager);
        return view;

    }

    private void displayAllUserPost() {

        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(postsRef ,Posts.class)
                .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Posts,PostsViewHolder>( options) {
            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.all_posts_layout,viewGroup, false);
                return new PostsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Posts model) {
                    holder.setFullname(model.getFullname());
                    holder.setTime(model.getTime());
                    holder.setDate(model.getDate());
                    holder.setDescription(model.getDescription());
                    holder.setProfileImage(model.getProfileImage());
                    holder.setTweetImage(model.getTweetImage());

            }
        };
        adapter.startListening();
        postList.setAdapter(adapter);
//        adapter.stopListening();


    }

    @Override
    public void onStart() {
        super.onStart();
    }

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
            Picasso.get().load(profileImage).into(image);
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
            TextView postDecryption =mView.findViewById(R.id.post_description);
            postDecryption.setText(description);
        }
        public void setTweetImage(String tweetImage) {
            ImageView postImage  =mView.findViewById(R.id.post_image);
            Picasso.get().load(tweetImage).into(postImage);
        }
    }


    private void sendUserToPostActivity() {
        Intent intent = new Intent(getActivity(), PostActivity.class);
        startActivity(intent);
    }
}
