package com.example.amohammadi_g30_a03_hangman;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import gameLogic.HangmanGame;
import gameLogic.Player;
import gameLogic.Scoreboard;
import linked_data_structures.DoublyLinkedList;

public class ReturningGameActivity extends AppCompatActivity implements View.OnClickListener {

    protected Scoreboard score = new Scoreboard();
    protected String playerName;
    protected Intent i;
    protected TextView txtWelPlayer;
    protected TextView txtWord;
    protected HangmanGame game;
    protected View viewPopupWindow;
    protected Player player;
    protected char[] letters;
    protected Button btnGuess;
    protected EditText pInput;
    protected TextView txtGuessedLetters;
    protected int numErr;
    protected ImageView hangmanImg;
    protected int[] hangmanImages = {R.drawable.hangman1, R.drawable.hangman2, R.drawable.hangman3, R.drawable.hangman4,
            R.drawable.hangman5, R.drawable.hangman6, R.drawable.hangman7};
    protected MenuItem hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returning_game);

        try {
            score = new Scoreboard(getFilesDir().getCanonicalFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        txtWelPlayer = (TextView) findViewById(R.id.txtWelPlayer);
        txtWord = (TextView) findViewById(R.id.txtWord);
        hangmanImg = (ImageView) findViewById(R.id.hangmanImgView);
        btnGuess = (Button) findViewById(R.id.guessBtn);
        pInput = (EditText) findViewById(R.id.fldLetter);
        txtGuessedLetters = (TextView) findViewById(R.id.txtGuessedLetters);

        ActionBar actionBar;
        actionBar = getSupportActionBar();


        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#147897"));

        actionBar.setBackgroundDrawable(colorDrawable);

        btnGuess.setOnClickListener(this);
        i = getIntent();
        playerName = i.getStringExtra("name");
        txtWelPlayer.setText("Welcome back " + playerName);
        txtWord.setText("");
        returningPlayer(playerName);
        showReturningBoxComponents();
    } // onCreate(Bundle)

    protected void showReturningBoxComponents() {
        int numLet = game.getLettersList().getLength();
        letters = new char[numLet];

        for (int i = 0; i < game.getLettersList().getLength(); ++i) {
            if (Character.toString(game.getLettersList().getElementAt(i)).equals(" ")) {
                ++game.getPlayer().numCorrect;
                letters[i] = ' ';
            } else {
                letters[i] = '_';
            } // if/else

            for (int j = 0; j < game.guessedLetters.getLength(); ++j) {
                if (game.getLettersList().getElementAt(i).equals(game.guessedLetters.getElementAt(j))) {
                    letters[i] = game.getLettersList().getElementAt(i);
                    continue;
                } // if
            } // for
        } // for

        displayWordToScreen();
    } // showReturningBoxComponents

    protected void returningPlayer(String pName) {
        for (int i = 0; i < score.playersList.getLength(); ++i) {
            if (score.playersList.getElementAt(i).name.equals(pName)) {
                player = score.playersList.getElementAt(i);
            } // if
        } // for
        if (player != null) {
            game = player.getGame();
        } // if
        numErr = player.getNumErr();
        if (numErr == 0) {
            hangmanImg.setImageResource(hangmanImages[0]);
        } else
            hangmanImg.setImageResource(hangmanImages[game.getPlayer().numErrors]);
        txtGuessedLetters.setText(player.getErrors());
    } // returningPlayer(String)

    protected void showNewWordBoxComponents() {
        int numLet = game.getLettersList().getLength();
        letters = new char[numLet];

        for (int i = 0; i < game.getLettersList().getLength(); ++i) {
            if (Character.toString(game.getLettersList().getElementAt(i)).equals(" ")) {
                ++game.getPlayer().numCorrect;
                letters[i] = ' ';
            } else {
                letters[i] = '_';
            } // if/else
        } // for

        displayWordToScreen();
    } // showNewWordBoxComponents()

    protected void displayWordToScreen() {
        String displayedWord = "";
        for (char c : letters) {
            displayedWord += c + " ";
        }
        txtWord.setText(displayedWord);
    } // displayWordToScreen()

    protected void playerGuess() {
        boolean beenGuessed = false;
        String playerGuess = pInput.getText().toString();
        // confirms input format
        if (playerGuess.length() != 0 && game.checkNum(playerGuess) == false && !playerGuess.equals(" ") && playerGuess.matches("[A-Za-z-]")) {

            if (playerGuess.length() > 0 && playerGuess.length() < 2) {
                String tempGuess = playerGuess.toLowerCase(); // sets guess to lower case
                char guessedLetter = tempGuess.charAt(0); // takes letter from guess
                game.guessedLetters.add(guessedLetter); // adds letter to SinglyLinkedList of all letters guessed

                if (game.checkLetter(guessedLetter)) { // if the guessed letter is correct
                    game.guessedIndices = game.findLetIndex(guessedLetter); // find where it is correct

                    for (int i = 0; i < game.guessedIndices.size(); ++i) {
                        if (!(letters[game.guessedIndices.get(i)] == '_')) {
                            Toast.makeText(this, "You have already guessed that letter", Toast.LENGTH_SHORT).show();
                        } else ++game.getPlayer().numCorrect;
                        letters[game.guessedIndices.get(i)] = guessedLetter; // show letter
                        // in
                        // according index
                    } // for
                    pInput.setText("");
                } else {
                    // updates hangman image
                    if (txtGuessedLetters.getText().toString().isEmpty()) {
                        ++game.getPlayer().numErrors;
                        hangmanImg.setImageResource(hangmanImages[game.getPlayer().numErrors]);
                        txtGuessedLetters.append(playerGuess + " ");
                        pInput.setText("");
                    } else {
                        // verifies letter has not already been guessed
                        for (int i = game.guessedLetters.getLength() - 1; i > 0; --i) {
                            if (game.guessedLetters.getElementAt(i) == guessedLetter) {
                                Toast.makeText(this, "You have already guessed that letter", Toast.LENGTH_SHORT).show();
                                pInput.setText("");
                                beenGuessed = true;
                            } // if
                        } // for
                        if (!beenGuessed) {
                            ++game.getPlayer().numErrors;
                            hangmanImg.setImageResource(hangmanImages[game.getPlayer().numErrors]);
                            txtGuessedLetters.append(playerGuess + " ");
                            pInput.setText("");
                        } // if
                    } // else
                } // else
            } // if guess is one letter
            else {
                Toast.makeText(this, "Please enter a single letter", Toast.LENGTH_SHORT).show();
            } // else
        } // if text is entered
        else {
            Toast.makeText(this, "Please enter a letter", Toast.LENGTH_SHORT).show();
        } // else

        displayWordToScreen();
        checkWinLoss();
    } // playerGuess()

    public void showWinLoss() {
        boolean hasWon = game.winLoss(game.getPlayer().numErrors, game.getPlayer().numCorrect);
        if (hasWon) {
            score.gamePlayed(game.getPlayer(), true);
            Toast.makeText(this, "Nice! You have guessed the correct word", Toast.LENGTH_SHORT).show();
        } else {
            score.gamePlayed(game.getPlayer(), false);
            Toast.makeText(this, "You've lost:(. The Word was " + game.getWord() + ". Try Again!", Toast.LENGTH_SHORT).show();
        }
    } // showWinLoss()

    protected void checkWinLoss() {
        try {
            if (game.getPlayer().getNumErr() == 6 || game.getPlayer().getNumCorr() == game.getLettersList().getLength()) {
                showWinLoss();
                resetGame();
            } // if
        } catch (IndexOutOfBoundsException q) {
            Intent winInt = new Intent(this, WinActivity.class);
            startActivity(winInt);
            finish();
        } // try/catch
    } // checkWinLoss()

    public void delWord() {
        game.removeWord();
    } // delWord()

    protected void createNextWordBox() {
        game.populateLettersList();
        showNewWordBoxComponents();
    } // createNextWordBox()

    private void resetIndices() {
        for (int i = 0; i < game.guessedIndices.size(); i++) {
            game.guessedIndices.remove(i);
        }
    } // resetIndices

    // resets game for next round
    private void resetGame() {
        delWord();
        game.clearLists();
        txtWord.setText("");
        resetIndices();
        if (hint != null) {
            hint.setEnabled(true);
        }
        game.getPlayer().setNumErr(0);
        game.getPlayer().setNumCorr(0);
        txtGuessedLetters.setText("");
        createNextWordBox();
        hangmanImg.setImageResource(hangmanImages[0]);
    } // resetGame()

    protected void giveHint() {
        boolean guessed;
        char hintLet;
        int index;
        // checks if index to give hint in word is appropriate according to if it has
        // already been guessed or if it's a space
        do {
            guessed = false;
            index = game.selectRanIndex();
            hintLet = game.getLettersList().getElementAt(index);
            for (int l = 0; l < game.guessedLetters.getLength(); ++l) {
                if ((game.guessedLetters.getElementAt(l) == hintLet) || game.guessedLetters.getElementAt(l) == ' ') {
                    guessed = true;
                    break;
                } // if
            } // for
        } while (guessed);

        // display hint at index
        for (int i = 0; i < game.getLettersList().getLength(); ++i) {
            if (game.getLettersList().getElementAt(i) == hintLet) {
                letters[i] = hintLet;
                game.guessedLetters.add(hintLet);
                Toast.makeText(this, "Hint: " + hintLet, Toast.LENGTH_SHORT).show();
                ++game.getPlayer().numCorrect;
            } // if
        } // for
        displayWordToScreen();
        checkWinLoss();
    } // giveHint()

    protected void restartGame() throws IOException {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("name", player.name);
        score.removePlayer(player.name);
        score.serialize(getFilesDir());
        finish();
        startActivity(intent);
    } // restartGame()

    protected void showScoreboard() {
        View v = findViewById(android.R.id.content).getRootView();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        viewPopupWindow = layoutInflater.inflate(R.layout.score_popup, null);

        PopupWindow popupWindow = new PopupWindow(viewPopupWindow, v.getWidth(), v.getHeight(), true);

        Button closeBtn = (Button) viewPopupWindow.findViewById(R.id.closeBtn);

        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        score.sortPlayers();
        populateBoard();
    } // showScoreboard()

    protected void populateBoard() {
        DoublyLinkedList<Player> players = score.playersList;
        TableLayout tb = (TableLayout) viewPopupWindow.findViewById(R.id.scoreTable);
        TextView[] txtArr = new TextView[players.getLength()];
        TableRow[] trHead = new TableRow[players.getLength()];
        TextView titleTxt = new TextView(this);
        TableRow titleRow = new TableRow(this);
        String title = String.format("%-22s%-22s%-22s", "Name", "Games Played", "Games Won");
        titleRow.setBackgroundColor(Color.LTGRAY);
        titleRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        titleTxt.setPadding(10, 10, 10, 10);
        titleTxt.setText(title);
        titleRow.addView(titleTxt);
        tb.addView(titleRow, new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < players.getLength(); ++i) {
            String displayTxt = "";
            trHead[i] = new TableRow(this);
            if (i % 2 == 0) {
                trHead[i].setBackgroundColor(Color.parseColor("#147897"));
            } else
                trHead[i].setBackgroundColor(Color.parseColor("#4E96AC"));
            trHead[i].setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            txtArr[i] = new TextView(this);
            displayTxt = String.format("%-27s %-15d %22d", players.getElementAt(i).name, players.getElementAt(i).getNumGP(), players.getElementAt(i).getNumGW());
            txtArr[i].setPadding(10, 10, 10, 10);
            txtArr[i].setText(displayTxt);
            trHead[i].addView(txtArr[i]);
            tb.addView(trHead[i], new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        } // for
    } // populateBoard()

    protected void saveGame(File filesDir) throws IOException {
        game.getPlayer().setErrors(txtGuessedLetters.getText().toString());
        score.serialize(getFilesDir());
    } // saveGame()

    protected void showInstructions() {
        Intent tutoInt = new Intent(this, TutorialActivity.class);
        startActivity(tutoInt);
    } // showInstructions

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guessBtn:
                playerGuess();
                hideKeyboard(v);
                break;
        } // switch
    } // onClick

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.game_menu, menu);
        return true;
    } // onCreateOptionsMenu(Menu)

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scoreItm:
                showScoreboard();
                return true;
            case R.id.hintItm:
                giveHint();
                item.setEnabled(false);
                hint = item;
                return true;
            case R.id.tutoItm:
                showInstructions();
                return true;
            case R.id.newGameItm:
                try {
                    restartGame();
                } catch (IOException e) {
                    Toast.makeText(this, "Unable to start a new game", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.quitItm:
                try {
                    saveGame(this.getFilesDir());
                } catch (IOException e) {
                    Toast.makeText(this, "Unable to save game", Toast.LENGTH_SHORT).show();
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } // switch
    } // onOptionsItemSelected(MenuItem)

    // Removes keyboard from display to save space.
    // Got idea from https://www.tutorialspoint.com/how-to-close-or-hide-the-virtual-keyboard-on-android#:~:text=To%20hide%20keyboard%2C%20use%20the,getApplicationWindowToken()%2C0)%3B
    protected void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    } // hideKeyboard
} // ReturningGameActivity