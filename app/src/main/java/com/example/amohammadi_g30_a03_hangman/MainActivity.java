package com.example.amohammadi_g30_a03_hangman;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import gameLogic.Scoreboard;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, Serializable {

    Button newPBtn;
    Button returnPBtn;
    Button subBtn;
    Spinner returningPNames;
    EditText newPName;
    String playerName = "";
    boolean returningPlayer = false;
    Scoreboard score;
    boolean newPickedButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newPBtn = (Button) findViewById(R.id.btnNewPlayer);
        returnPBtn = (Button) findViewById(R.id.btnReturningPlayer);
        subBtn = (Button) findViewById(R.id.btnSub);
        returningPNames = (Spinner) findViewById(R.id.namesSpn);
        newPName = (EditText) findViewById(R.id.txtPlayerName);

        ActionBar actionBar;
        actionBar = getSupportActionBar();


        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#147897"));

        actionBar.setBackgroundDrawable(colorDrawable);

        newPBtn.setOnClickListener(this);
        returnPBtn.setOnClickListener(this);
        subBtn.setOnClickListener(this);
        returningPNames.setOnItemSelectedListener(this);


    } // onCreate(Bundle)

    public void enterName() {
        if (!newPName.getText().toString().equals("")) {
            playerName = newPName.getText().toString();
            if (playerName.length() == 1)
                playerName = playerName.toUpperCase();
            else
                playerName = Character.toUpperCase(playerName.charAt(0)) + playerName.substring(1).toLowerCase();
            
            newPName.setText("");

            Intent i = new Intent(this, GameActivity.class);
            i.putExtra("name", playerName);
            startActivity(i);
        } // if
        if (playerName.equals("")) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        } // if
    } // enterName()

    public boolean pickName() throws IOException, ClassNotFoundException {
        boolean hasPlayers = true;
        score = new Scoreboard();
        score.deserialize(getFilesDir().getCanonicalFile());

        String names[] = new String[score.playersList.getLength()];
        for (int i = 0; i < names.length; ++i) {
            names[i] = score.getNextPlayer(i).name;
        } // for

        if (score.playersList.getLength() == 0) {
            Toast.makeText(this, "There are no current players registered", Toast.LENGTH_SHORT).show();
            hasPlayers = false;
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, names);
            returningPNames.setAdapter(adapter);
        } // if/else
        return hasPlayers;
    } // pickName

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewPlayer:
                newPickedButton = true;
                returningPNames.setVisibility(View.GONE);
                newPName.setVisibility(View.VISIBLE);
                break;
            case R.id.btnReturningPlayer:
                newPickedButton = false;
                newPName.setVisibility(View.GONE);
                returningPNames.setVisibility(View.VISIBLE);
                try {
                    pickName();
                } catch (IOException e) {
                    Toast.makeText(this, "Cannot open file", Toast.LENGTH_SHORT).show();
                } catch (ClassNotFoundException e) {
                    Toast.makeText(this, "Trouble opening file", Toast.LENGTH_SHORT).show();
                } // try/catch
                break;
            case R.id.btnSub:
                if (newPickedButton) {
                    enterName();
                } else {
                    if (playerName.equals("")) {
                        Toast.makeText(this, "Please create a new player", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent rInt = new Intent(this, ReturningGameActivity.class);
                        rInt.putExtra("name", playerName);
                        startActivity(rInt);
                    } // else
                } // if/else
        } // switch
    } // onClick()

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        playerName = parent.getSelectedItem().toString();
        returningPlayer = true;
    } // onItemSelected()

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    } // onNothingSelected()
} // MainActivity