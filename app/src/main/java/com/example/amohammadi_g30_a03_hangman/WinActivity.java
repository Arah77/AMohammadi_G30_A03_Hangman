package com.example.amohammadi_g30_a03_hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

public class WinActivity extends AppCompatActivity implements Serializable, View.OnClickListener {

    Button leaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        leaveBtn = (Button) findViewById(R.id.btnLeave);
        leaveBtn.setOnClickListener(this);
    } // onCreate(Bundle)

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLeave) {
            finish();
        } // if
    } // onClick(View)
} // WinActivity