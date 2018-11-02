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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ResgisterActivity extends AppCompatActivity {
    private EditText userEmail, userPassword, userConfirmPassword;
    private Button createAccountButton;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister);

        mAuth=FirebaseAuth.getInstance();

        userEmail= findViewById(R.id.register_email);
        userPassword=findViewById(R.id.login_password);
        userConfirmPassword=findViewById(R.id.register_confirm_password);
        createAccountButton= findViewById(R.id.register_create_account);
        loadingBar= new ProgressDialog(this);

        //authenticate the user
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });

    }

    private void CreateNewAccount() {
        String email= userEmail.getText().toString();
        String password= userPassword.getText().toString();
        String confirmPassword = userConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please write your email...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write your password...",Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this,"Please confirm your password...",Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(password)){
            Toast.makeText(this,"your password is not match... ",Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Creating new Account");
            loadingBar.setMessage("Please wait,  While we are creating your new account..");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //sendUserToSetupActivity();
                                Toast.makeText(ResgisterActivity.this
                                        ," you are authenticated successfull...",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else {
                                String message =  task.getException().getMessage();
                                Toast.makeText(ResgisterActivity.this,"Error Occured:"+message,Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }
                    });


        }
    }


    private void sendUserToSetupActivity() {
        //send user setup activity
        Intent setupIntent = new Intent(ResgisterActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        //finish();
    }
}
