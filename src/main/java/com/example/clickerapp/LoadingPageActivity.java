package com.example.clickerapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.DialogInterface;

public class LoadingPageActivity extends AppCompatActivity {
    int currentQn;
    int newCurrentQn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        WebView webView = (WebView) findViewById(R.id.webView);
        //webView.getSettings().setJavaScriptEnabled(true);
        // URL hardcoded
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);

        Intent intent1 = getIntent();
        currentQn = intent1.getIntExtra("currentQn", 0);
    }

    public void checkCurrentQuestion() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:9999/clicker/checkQn");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    newCurrentQn = Integer.parseInt(in.readLine());
                    in.close();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (currentQn == 5) {
                                showEndDialog();
                            } else if (newCurrentQn == currentQn) {
                                showErrorDialog();
                            } else {
                                navigateToMainActivity();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoadingPageActivity.this);
        builder.setTitle("Error");
        builder.setMessage("The Question hasn't changed!");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEndDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoadingPageActivity.this);
        builder.setTitle("Thank you!");
        builder.setMessage("End of Questionnaire!");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code to execute when OK button is clicked
                navigateToStartActivity();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("currentQn", newCurrentQn);
        startActivity(intent);
    }

    private void navigateToStartActivity() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void btnNextHandler(View view) {
        checkCurrentQuestion();
    }

    public void btnExitHandler2(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}

