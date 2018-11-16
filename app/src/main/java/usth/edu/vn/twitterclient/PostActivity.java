package usth.edu.vn.twitterclient;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button updateButton;
    private EditText postDescription;
    private ImageButton selectPostImage;
    private final static  int GALLERY_PICK=1;
    private Uri imageUri;
    private String description;
    private StorageReference postImageRef;
    private String saveCurrentDate,saveCurrentTime,postRadomName, downloadUrl,currentUserId;
    private DatabaseReference userRef, postsRef;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mToolbar=findViewById(R.id.post_tweet_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.exit);
//        mToolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendUserToMainActivity();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Tweet");
        updateButton= findViewById(R.id.tweet_button);
        postDescription = findViewById(R.id.post_description);
        selectPostImage= findViewById(R.id.select_post_image);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postImageRef = FirebaseStorage.getInstance().getReference();


        selectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationPost();
            }
        });



    }

    private void openGallery() {
        Intent  galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==GALLERY_PICK && resultCode==RESULT_OK && data!=null) {
            imageUri = data.getData();
            selectPostImage.setImageURI(imageUri);
        }
    }

    private void validationPost() {
         description = postDescription.getText().toString();

        if(TextUtils.isEmpty(description)) {
            Toast.makeText(PostActivity.this,"hm... you have to write sth...",Toast.LENGTH_SHORT).show();
        } else {
            storingTweet();
        }
    }

    private void storingTweet() {
        Calendar calForDate= Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime= Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        postRadomName= saveCurrentDate+saveCurrentTime;

        StorageReference filePath = postImageRef.child("Post Images").child(imageUri.getLastPathSegment()+ postRadomName + ".jpg");

        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PostActivity.this, "Post Image  stored successfully......", Toast.LENGTH_SHORT).show();
                    Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String downloadUrl = uri.toString();
                        }
                    });
                    Toast.makeText(PostActivity.this,"tweeted..",Toast.LENGTH_SHORT).show();
                    savingTweetInformation();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(PostActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void savingTweetInformation() {
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String userFullName = dataSnapshot.child("fullname").getValue().toString();
                    String userProfileImage = dataSnapshot.child("profileImage").getValue().toString();

                    HashMap postMap = new HashMap();
                    postMap.put("uid", currentUserId);
                    postMap.put("date", saveCurrentDate);
                    postMap.put("time", saveCurrentTime);
                    postMap.put("description", description);
                    postMap.put("tweetImage", downloadUrl);
                    postMap.put("profileImage", userProfileImage);
                    postMap.put("fullname", userFullName);

                    postsRef.child(currentUserId+postRadomName).updateChildren(postMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()) {
                                        sendUserToMainActivity();
                                        Toast.makeText(PostActivity.this,"  new Tweet is updated...", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PostActivity.this,"  Error...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
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
        Intent intent = new Intent(PostActivity.this,MainActivity.class);
        startActivity(intent);

    }
}
