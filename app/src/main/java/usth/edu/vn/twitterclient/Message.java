package comm.example.twitterapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Message extends AppCompatActivity {
    EditText messContent;
    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);

        messContent = (EditText)findViewById(R.id.editMessage);
        sendBtn = (Button)findViewById(R.id.btnSend);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = messContent.getText().toString();
                Toast.makeText(Message.this, content, Toast.LENGTH_LONG).show();
            }
        });
    }
}
