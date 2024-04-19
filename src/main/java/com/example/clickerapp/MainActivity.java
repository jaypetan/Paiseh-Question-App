package com.example.clickerapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    int currentQn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
            // Create an Intent to start the second activity called "WebViewActivity"
        });
        Intent intent1 = getIntent();
        currentQn = intent1.getIntExtra("currentQn", 0);

        final TextView myTextView = findViewById(R.id.qnText);
        myTextView.setText("Question " + currentQn);
    }

    /** Callback when the user click the "GO" button */
    public void btnGoHandler1(View view) {
        // Start the intended activity
        Intent intent = new Intent(this, LoadingPageActivity.class);
        intent.putExtra("currentQn",currentQn);
        intent.putExtra("url", "http://10.0.2.2:9999/clicker/select?choice=A");
        startActivity(intent);
    }

    public void btnGoHandler2(View view) {
        // Start the intended activity
        Intent intent = new Intent(this, LoadingPageActivity.class);
        intent.putExtra("currentQn",currentQn);
        intent.putExtra("url", "http://10.0.2.2:9999/clicker/select?choice=B");
        startActivity(intent);
    }
    public void btnGoHandler3(View view) {
        // Start the intended activity
        Intent intent = new Intent(this, LoadingPageActivity.class);
        intent.putExtra("currentQn",currentQn);
        intent.putExtra("url", "http://10.0.2.2:9999/clicker/select?choice=C");
        startActivity(intent);
    }
    public void btnGoHandler4(View view) {
        // Start the intended activity
        Intent intent = new Intent(this, LoadingPageActivity.class);
        intent.putExtra("currentQn",currentQn);
        intent.putExtra("url", "http://10.0.2.2:9999/clicker/select?choice=D");
        startActivity(intent);
    }

    public void btnExitHandler(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}