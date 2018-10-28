package usth.edu.vn.twitterclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText userEmail,userPassword;
    private TextView needNewAcountLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        needNewAcountLink= findViewById(R.id.register_account_link);
        userEmail= findViewById(R.id.login_email);
        userPassword=  findViewById(R.id.login_password);
        loginButton =  findViewById(R.id.login_button);

        needNewAcountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToRegisterActivity();
            }
        });
    }

    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this,ResgisterActivity.class);
        startActivity(registerIntent);
        finish();
    }


}
