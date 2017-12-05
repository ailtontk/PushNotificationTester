package com.example.ailton.pushnotificationtester;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    TextView tvToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        tvToken = findViewById(R.id.tvToken);
        Button btCopy = findViewById(R.id.btCopy);
        Button btGenerateAgain = findViewById(R.id.btGenerateAgain);

        //Try to generate
        generateToken();

        //Generate agin button
        btGenerateAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateToken();
            }
        });

        //Copy button
        btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Token", tvToken.getText().toString());
                clipboard.setPrimaryClip(clip);

                Log.d("Token Firebase", tvToken.getText().toString());
                Toast.makeText(MainActivity.this, "Copiado para a área de transferência e para o console do Android Studio.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token == null) {
            token = "Token ainda não gerado. Toque no botão abaixo para tentar novamente.";
        }

        tvToken.setText(token);
    }
}
