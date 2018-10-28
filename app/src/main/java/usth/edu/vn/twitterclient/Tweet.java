package comm.example.twitterapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Tweet extends AppCompatActivity {
    EditText tweetContent;
    Button tweetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet);

        tweetContent = (EditText)findViewById(R.id.tweetContent);
        tweetBtn = (Button)findViewById(R.id.btnTweet);

        tweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = tweetContent.getText().toString();
                Toast.makeText(Tweet.this, content, Toast.LENGTH_LONG).show();
            }
        });

    }
}
