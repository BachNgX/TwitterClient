package usth.edu.vn.twitterclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity  {

    private EditText userName, fullName, countryName;
    private Button saveInformationButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private ProgressDialog loadingBar;
    private StorageReference userProfileImageRef;


    private CircleImageView profileImage;
    final static  int GALLERY_PICK=1;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth= FirebaseAuth.getInstance();
        currentUserId= mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        userProfileImageRef= FirebaseStorage.getInstance().getReference().child("Profile Images");



        userName = findViewById(R.id.setup_username);
        fullName = findViewById(R.id.setup_fullname);
        countryName = findViewById(R.id.setup_country_name);
        saveInformationButton = findViewById(R.id.setup_information_button);
        profileImage = findViewById(R.id.setup_profile_image);
        loadingBar= new ProgressDialog(this);


        saveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountSetupInformation();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String image=  dataSnapshot.child("profileImage").getValue().toString();

                    ///use Picasso library to display the image
//                    Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImage);
                    Glide.with(SetupActivity.this).load(image).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PICK && resultCode== RESULT_OK  && data!=null) {
            Uri imageUri = data.getData();
            //add a cropping function ,use the arthur hub library
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK) {
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait,  While we are updating your profile image..");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                //save the crop image inside fireStorage
                Uri resultUri = result.getUri();
                StorageReference filePath = userProfileImageRef.child(currentUserId+".jpg");
//                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        //Bitmap hochladen
////                        final String  downloadUrl =uri.toString();
//                    }
//                });
                final String downloadUrl = filePath.getDownloadUrl().toString();

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Intent setupIntent = new Intent(SetupActivity.this,SetupActivity.class);
                            startActivity(setupIntent);

                            Toast.makeText(SetupActivity.this,"Profile Image  stored successfully...",Toast.LENGTH_SHORT).show();

//                             final String downloadUrl = filePath.getDownloadUrl().toString();
//                             final String downloadUrl = StorageReference.getDownloadUr().;



                            userRef.child("profileImage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(SetupActivity.this,"Profile Image stored to database successfully",Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(SetupActivity.this,"Error occured.."+message,Toast.LENGTH_SHORT);
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void saveAccountSetupInformation() {
        String username = userName.getText().toString();
        String fullname = fullName.getText().toString();
        String country = countryName.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"please write your username...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this,"please write your full name...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(country)){
            Toast.makeText(this,"please write your country...",Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait,  While we are creating your new account..");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("username",username);
            userMap.put("fullname",fullname);
            userMap.put("country",country);
            userMap.put("status","hi.............");
            userMap.put("gender","none");
            userMap.put("dob","");
            userMap.put("relationship","none");
            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()) {
                        sendUserToMainActivity();
                        Toast.makeText(SetupActivity.this," your account is created successfully..",Toast.LENGTH_LONG).show();
//                        loadingBar.dismiss();
                    }
                    else {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this,"Error occured"+message,Toast.LENGTH_LONG).show();
//                        loadingBar.dismiss();
                    }
                }
            });

        }
    }

    private void sendUserToMainActivity() {
        Intent mainIntent= new Intent(SetupActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
