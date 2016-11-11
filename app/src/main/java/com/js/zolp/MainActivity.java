package com.js.zolp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    private Context context;
    private TextView textView;
    private Button imageButton, openCVButton;

    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        initView();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageActivity.class);
                startActivity(intent);
            }
        });
        openCVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpencvActivity.class);
                startActivity(intent);
            }
        });

    }

    static {
        //System.loadLibrary();
    }

    public void initView() {
        textView = (TextView) findViewById(R.id.textView);
        imageButton = (Button) findViewById(R.id.btn_image);
        openCVButton = (Button) findViewById(R.id.btn_opencv);
    }
}
