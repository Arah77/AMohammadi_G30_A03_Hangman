package gameLogic;

import android.content.Context;
import android.os.Environment;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Scanner;

public class Dictionary implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected Scanner listReader;
    private String filename = "dictionary.txt";
    Context con;

    public Dictionary(Context c) {
        con = c;
    } // Dictionary(Context)

    public Scanner readFile() {
        try {
            listReader = new Scanner(con.getAssets().open(String.format(filename)));
        } catch (IOException e) {
            e.getStackTrace();
        } // try/catch
        return listReader;
    } // readFile
} // Dictionary
