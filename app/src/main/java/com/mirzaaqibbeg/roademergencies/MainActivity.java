package com.mirzaaqibbeg.roademergencies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
    }

    public void onButtonClick(View view) {
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
}
