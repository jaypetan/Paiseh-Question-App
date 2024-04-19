package com.example.clickerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    int currentQn;
    public void checkCurrentQuestion() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:9999/clicker/checkQn");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    currentQn = Integer.parseInt(in.readLine());
                    in.close();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (currentQn < 1) {
                                showErrorDialog();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle("Error");
        builder.setMessage("The Quiz has not started!");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void btnGoHandler(View view) {
        checkCurrentQuestion();
        if(currentQn >= 1){
            // Create an Intent to start the second activity called "WebViewActivity"
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("currentQn",currentQn);
            startActivity(intent);
        }
    }
}