package gameLogic;

import java.io.Serializable;

public class Player implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String name;
    protected int numGamesPlayed;
    protected int numGamesWon;
    public HangmanGame game;
    public int numErrors;
    public int numCorrect;
    protected String errors;

    public Player() {
        name = "Unknown";
        numGamesPlayed = 0;
        numGamesWon = 0;
    } // Player()

    public Player(String pName) {
        name = pName;
    } // Player(String)

    protected void setName(String n) {
        name = n;
    } // setName(String)

    protected String getName() {
        return name;
    } // getName()

    protected void setNumGP(int numGP) {
        numGamesPlayed = numGP;
    } // setNumGP(int)

    public int getNumGP() {
        return numGamesPlayed;
    } // getNumGP()

    protected void setNumGW(int numGW) {
        numGamesWon = numGW;
    } // setNumGW(int)

    public int getNumGW() {
        return numGamesWon;
    } // getNumGW()

    public void setGame(HangmanGame currGame) {
        game = currGame;
    } // setGame(HangmanGame)

    public HangmanGame getGame() {
        return game;
    } // getGame()

    public void setNumErr(int err) {
        numErrors = err;
    } // setNumErr(int)

    public int getNumErr() {
        return numErrors;
    } // getNumErr()

    public void setNumCorr(int corr) {
        numCorrect = corr;
    } // setNumCorr(int)

    public int getNumCorr() {
        return numCorrect;
    } // getNumCorr

    public void setErrors(String err) {
        errors = err;
    } // setErrors(String)

    public String getErrors() {
        return errors;
    } // getErrors();
} // Player

