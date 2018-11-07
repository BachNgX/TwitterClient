package usth.edu.vn.twitterclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText userEmail,userPassword;
    private TextView needNewAccountLink;
    private ProgressDialog loadingBar2;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        needNewAccountLink= findViewById(R.id.register_account_link);
        userEmail= findViewById(R.id.login_email);
        userPassword=  findViewById(R.id.login_password);
        loginButton =  findViewById(R.id.login_button);

        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegisterActivity();
            }
        });

        //allow user login their account
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowingUserToLogin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser= mAuth.getCurrentUser();
       //the user is already login
        if(currentUser!=null){
            sendUserToMainActivity();
        }
    }

    private void allowingUserToLogin() {
        String email= userEmail.getText().toString();
        String password= userPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please write your email...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write your password...",Toast.LENGTH_SHORT).show();
        }
        else{

//            loadingBar2.setTitle("Login");
//            loadingBar2.setMessage("Please wait,  While we are allowing you login into your account..");
//            loadingBar2.show();
//            loadingBar2.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendUserToMainActivity();
                                Toast.makeText(LoginActivity.this,"you  are logined successfully...",Toast.LENGTH_SHORT).show();
                                //loadingBar2.dismiss();
                            }
                            else{
                                String message =task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"Error Occured"+message,Toast.LENGTH_SHORT).show();
                                //loadingBar2.dismiss();
                            }
                        }
                    });

        }
    }

    private void sendUserToMainActivity() {
        Intent mainIntent= new Intent(LoginActivity.this,MainActivity.class);
         //add the validation that is  by pressing  the back button, not allow to come back login activity unless logout
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this,ResgisterActivity.class);
        startActivity(registerIntent);
        //finish();
    }


}
