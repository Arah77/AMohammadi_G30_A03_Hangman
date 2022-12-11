package gameLogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import linked_data_structures.DoublyLinkedList;

public class Scoreboard implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public DoublyLinkedList<Player> playersList = new DoublyLinkedList<Player>();
    protected String filename = "hangman.ser";
    private int numPlayers;

    public Scoreboard() {
    } // Scoreboard

    public Scoreboard(File filesDir) {
        try {
            deserialize(filesDir);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("Hello");
        }
        numPlayers = 0;
    } // Scoreboard(File)

    public void addPlayer(Player player) throws IOException {
        playersList.add(player);
    } // addPlayer(Player)

    public void gamePlayed(Player player, boolean wOl) {
        player.setNumGP(player.getNumGP() + 1);
        if (wOl) {
            player.setNumGW(player.getNumGW() + 1);
        } // if
    } // gamePlayed(Player, boolean)

    public void sortPlayers() {
        Player[] playerArr = new Player[playersList.getLength()];
        Player tempPlayer = new Player();

        // populates array
        for (int i = playerArr.length - 1; i >= 0; i--) {
            playerArr[i] = playersList.getElementAt(i);
        } // for

        // clears player list
        for (int j = playersList.getLength() - 1; j >= 0; j--) {
            playersList.remove(j);
        }

        // sort array
        for (int i = 0; i < playerArr.length - 1; i++) {
            for (int j = 0; j < playerArr.length - 1 - i; j++) {
                if (playerArr[j + 1].name.compareTo(playerArr[j].name) > 0) {
                    tempPlayer = playerArr[j];
                    playerArr[j] = playerArr[j + 1];
                    playerArr[j + 1] = tempPlayer;
                } // if
            } // inner for
        } // outer for

        // repopulate player list
        for (int i = 0; i < playerArr.length; ++i) {
            playersList.add(playerArr[i]);
        } // for
    } // sortPlayers

    // removes player from list
    public void removePlayer(String pName) {
        for (int i = 0; i < playersList.getLength(); ++i) {
            if (playersList.getElementAt(i).name.equals(pName)) {
                playersList.remove(i);
            } // if
        } // for
    } // removePlayer(Player)

    public void serialize(File filesDir) throws IOException {
        try {
            File file = new File(filesDir, filename);

            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(playersList); // a doublyLinkedList of players

            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.getStackTrace();
        } // try/catch
    } // Serialize()

    public void deserialize(File filesDir) throws IOException, ClassNotFoundException {
        try {
            File file = new File(filesDir, filename);
            FileInputStream inpStream = new FileInputStream(file);
            if (inpStream.available() > 0) {
                ObjectInputStream objInp = new ObjectInputStream(inpStream);

                playersList = (DoublyLinkedList<Player>) objInp.readObject();
                objInp.close();
                inpStream.close();
            }
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        } // try/catch
    } // Deserialize()

    public Player getNextPlayer(int index) {
        try {
            return playersList.getElementAt(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        } // try/catch
    } // getNextPlayer(int)
} // ScoreBoard
