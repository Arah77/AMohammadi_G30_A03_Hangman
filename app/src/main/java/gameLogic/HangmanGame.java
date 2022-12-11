package gameLogic;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import linked_data_structures.DoublyLinkedList;
import linked_data_structures.SinglyLinkedList;

public class HangmanGame implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected SinglyLinkedList<String> wordList = new SinglyLinkedList();
    protected SinglyLinkedList<Character> lettersList = new SinglyLinkedList();
    public SinglyLinkedList<Character> guessedLetters = new SinglyLinkedList();
    public ArrayList<Integer> guessedIndices = new ArrayList<Integer>();
    private String word;
    private Player player;
    private Context c;

    public HangmanGame() {
    } // HangmanGame()

    // populates word SinglyLinkedList
    public boolean populateWordList(Scanner listReader) {
        boolean isValid = true;
        try {
            while (listReader.hasNext()) {
                StringTokenizer line = new StringTokenizer(listReader.nextLine(), "\n");
                wordList.add(line.nextToken());
            }
            listReader.close();
        } catch (NullPointerException e) {
            isValid = false;
        }
        return isValid;
    } // populateList()

    protected SinglyLinkedList<String> getWordList() {
        return wordList;
    } // getWordList()

    public SinglyLinkedList<Character> getLettersList() {
        return lettersList;
    } // getLetters()

    public void clearLists() {
        int j = guessedLetters.getLength() - 1;

        for (int i = lettersList.getLength() - 1; i >= 0; i--) {
            lettersList.remove(i);
        }

        while (j > 0) {
            guessedLetters.remove(j);
            --j;
        }
    } // clearLists()

    // populates letter SinglyLinkedList
    public void populateLettersList() {
        String word = pickWord();
        char letter;
        for (int i = word.length() - 1; i >= 0; --i) {
            letter = word.charAt(i);
            lettersList.add(letter);
        } // for
    } // populateLettersList

    // picks a random word in list
    protected String pickWord() {
        int max = wordList.getLength();
        int ranNum = (int) Math.floor(Math.random() * (max - 0) + 0);

        String selectedWord = wordList.getElementAt(ranNum);
        setWord(selectedWord);
        return selectedWord;
    } // pickWord()

    // selects a random index in list
    public int selectRanIndex() {
        int l = lettersList.getLength();
        Random ran = new Random();
        int ranIndex = ran.nextInt(l);
        return ranIndex;
    } // selectRanIndex()

    // checks if letter is in list
    public boolean checkLetter(char guess) {
        boolean isRight = false;
        for (int i = 0; i < lettersList.getLength(); ++i) {
            if (guess == lettersList.getElementAt(i)) {
                isRight = true;
            } // if
        } // for
        return isRight;
    } // checkLetter(char)

    // finds list index for specific letter
    public ArrayList<Integer> findLetIndex(char guess) {
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < lettersList.getLength(); ++i) {
            if (guess == lettersList.getElementAt(i)) {
                indices.add(i);
            } // if
        } // for
        return indices;
    } // findLetIndex(char)

    // checks if input is a number
    public boolean checkNum(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    } // checkNum(String)

    protected void setWord(String w) {
        word = w;
    } // setWord(String)

    public String getWord() {
        return word;
    } // getWord()

    public void setPlayer(String name) {
        player = new Player(name);
    } // setPlayer(String)

    public Player getPlayer() {
        return player;
    } // getPlayer()

    public boolean winLoss(int numErr, int numCorr) {
        boolean hasWon = false;
        if (numCorr == getLettersList().getLength()) {
            hasWon = true;
        }
        return hasWon;
    } // winLoss(int, int)

    public void removeWord() {
        for (int i = 0; i < wordList.getLength(); ++i) {
            if (wordList.getElementAt(i).equals(getWord())) {
                wordList.remove(i);
            } // if
        } // for

    } // removeWord()

    public DoublyLinkedList<Player> sort(DoublyLinkedList<Player> list) {
        Player[] tempArr = new Player[list.getLength()];
        Player temp;
        boolean sorted = false;
        // temporarily adds to an array to be sorted
        for (int i = 0; i < list.getLength(); ++i) {
            tempArr[i] = list.getElementAt(i);
        }

        // clears list
        for (int i = list.getLength() - 1; i >= 0; i--) {
            list.remove(i);
        }

        // sorts array
        while (!sorted) {
            sorted = true;

            for (int i = 0; i < tempArr.length - 1; i++) {
                for (int j = i + 1; j < tempArr.length; j++) {
                    if (tempArr[i].name.compareToIgnoreCase(tempArr[j].name) > 0) {
                        temp = tempArr[i];
                        tempArr[i] = tempArr[j];
                        tempArr[j] = temp;
                        sorted = false;
                    } // if
                } // inner for

            } // outer for
        } // while

        // repopulates list
        for (int i = tempArr.length - 1; i >= 0; i--) {
            list.add(tempArr[i]);
        }

        return list;
    } // sort
} // HangmanGame
