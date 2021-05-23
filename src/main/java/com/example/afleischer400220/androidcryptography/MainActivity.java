package com.example.afleischer400220.androidcryptography;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RadioGroup radioCipherGroup;
    RadioButton selectedCipher;
    Button applyCipher;
    EditText scytaleRows;
    EditText caesarShift;
    EditText vigenereKeyword;
    TextView currentCipherTV;
    Button encrypt;
    Button decrypt;
    EditText plaintextET;
    EditText ciphertextET;

    boolean isBruteForce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking all the radiogroups, buttons, text views, and edit texts
        radioCipherGroup = (RadioGroup)this.findViewById(R.id.radioCiphers);
        applyCipher = (Button)this.findViewById(R.id.btnDisplay);
        scytaleRows = (EditText)this.findViewById(R.id.rows);
        caesarShift = (EditText)this.findViewById(R.id.shift);
        vigenereKeyword = (EditText)this.findViewById(R.id.keyword);
        currentCipherTV = (TextView)this.findViewById(R.id.selectedCipher);
        encrypt = (Button)this.findViewById(R.id.encryptBtn);
        decrypt = (Button)this.findViewById(R.id.decryptBtn);
        plaintextET = (EditText)this.findViewById(R.id.plaintext);
        ciphertextET = (EditText)this.findViewById(R.id.ciphertext);

        //Set onClick listeners
        applyCipher.setOnClickListener(this);
        encrypt.setOnClickListener(this);
        decrypt.setOnClickListener(this);
        //Start encode/decode buttons as inactive until the cipher is selected
        encrypt.setEnabled(false);
        decrypt.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        //APPLYCIPHER - checks what is in the radio buttons and extra info, then sets up the proper cipher
        if (v.equals(applyCipher)) {
            //Get which cipher it is and set up the text for which cipher
            int selectedId = radioCipherGroup.getCheckedRadioButtonId();
            selectedCipher = findViewById(selectedId);
            String cipherAndKeyString = selectedCipher.getText().toString() + " selected";
            //If it is scytale, add the rows
            if (selectedCipher.getText().toString().equals("Scytale")) {
                cipherAndKeyString += ", Rows: " + scytaleRows.getText().toString();
                if (scytaleRows.getText().toString().equals("")) {
                    cipherAndKeyString += "N/A";
                }
            }
            //If it is caesar, add the shift
            else if (selectedCipher.getText().toString().equals("Caesar")) {
                cipherAndKeyString += ", Shift: " + caesarShift.getText().toString();
                if (caesarShift.getText().toString().equals("")) {
                    cipherAndKeyString += "BRUTE FORCE";
                    isBruteForce = true;
                }
                else {
                    isBruteForce = false;
                }
            }
            //If it is vigenere, add the keyword
            else if (selectedCipher.getText().toString().equals("Vigenere")) {
                cipherAndKeyString += ", Keyword: " + vigenereKeyword.getText().toString().toUpperCase();
                if (vigenereKeyword.getText().toString().equals("")) {
                    cipherAndKeyString += "N/A";
                }
            }
            //Set the text and enable the encode/decode buttons
            currentCipherTV.setText(cipherAndKeyString);
            encrypt.setEnabled(true);
            decrypt.setEnabled(true);
        }
        //ENCRYPT
        else if (v.equals(encrypt)) {
            //Set the plaintext to all uppercase, and letters only
            String OGplaintext = plaintextET.getText().toString().toUpperCase();
            String plaintext = "";
            for (int i = 0; i < OGplaintext.length(); i++) {
                if (Character.isLetter(OGplaintext.charAt(i))) {
                    plaintext += OGplaintext.charAt(i);
                }
            }
            plaintextET.setText(plaintext);

            //Depending on the cipher selected, encrypt the plaintext
            if (selectedCipher.getText().toString().equals("Scytale")  && !scytaleRows.getText().toString().equals("")) {
                ciphertextET.setText(scytaleCipher(plaintext, true));
            }
            else if (selectedCipher.getText().toString().equals("Caesar")  && !caesarShift.getText().toString().equals("")) {
                ciphertextET.setText(caesarCipher(plaintext, true, false));
            }
            else if (selectedCipher.getText().toString().equals("Vigenere")  && !vigenereKeyword.getText().toString().equals("")) {
                ciphertextET.setText(vigenereCipher(plaintext, true));
            }
        }
        //DECRYPT
        else if (v.equals(decrypt)) {
            //Set the ciphertext to all uppercase, and letters only
            String OGciphertext = ciphertextET.getText().toString().toUpperCase();
            String ciphertext = "";
            for (int i = 0; i < OGciphertext.length(); i++) {
                if (Character.isLetter(OGciphertext.charAt(i))) {
                    ciphertext += OGciphertext.charAt(i);
                }
            }
            ciphertextET.setText(ciphertext);
            //Depending on the cipher selected, decrypt the plaintext
            if (selectedCipher.getText().toString().equals("Scytale") && !scytaleRows.getText().toString().equals("")) {
                plaintextET.setText(scytaleCipher(ciphertext, false));
            }
            else if (selectedCipher.getText().toString().equals("Caesar")) {
                plaintextET.setText(caesarCipher(ciphertext, false, isBruteForce));
            }
            else if (selectedCipher.getText().toString().equals("Vigenere") && !vigenereKeyword.getText().toString().equals("")) {
                plaintextET.setText(vigenereCipher(ciphertext, false));
            }
        }
    }

    //caesarCipher - this method encrypts or decrypts text using the caesar cipher
    public String caesarCipher(String text, boolean isEncrypt, boolean isBruteForce) {
        char[] chars = new char[text.length()];
        int newLetter;

        //if brute force, try every shift
        if (isBruteForce) {
            String bigDaddyReturn = "";
            for (int tempShift = 1; tempShift < 26; tempShift++) {
                for (int i = 0; i < text.length(); i++) {
                    newLetter = (int) text.charAt(i) - tempShift;

                    if (newLetter < 65) {
                        newLetter += 26;
                    }
                    if (newLetter > 90) {
                        newLetter -= 26;
                    }
                    chars[i] = (char)newLetter;
                }
                bigDaddyReturn += tempShift + ": " + new String(chars) + "\n";
            }
            return bigDaddyReturn;
        }
        else {
            //get the desired shift and convert it to 0-26
            int shift = Integer.parseInt(caesarShift.getText().toString());
            if (shift > 26) {
                shift %= 26;
            }
            if (shift < 0) {
                shift = (shift % 26) + 26;
            }
            //for every letter in the text, either add or subtract the shift depending on what mode this is
            for (int i = 0; i < text.length(); i++) {
                if (isEncrypt) {
                    newLetter = (int)text.charAt(i) + shift;
                }
                else {
                    newLetter = (int)text.charAt(i) - shift;
                }
                //if the ascii value went past z or a, loop it to the other end of the alphabet
                if (newLetter < 65) {
                    newLetter += 26;
                }
                if (newLetter > 90) {
                    newLetter -= 26;
                }
                chars[i] = (char)newLetter;
            }
        }
        return new String(chars);
    }

    //scytaleCipher - this method encrypts or decrypts text using the scytale cipher
    public String scytaleCipher(String plaintext, boolean isEncrypt) {
        int rows = Integer.parseInt(scytaleRows.getText().toString());
        int cols = (int)Math.ceil(plaintext.length()/(double)rows); //find the proper number of columns
        char[][] letterArray = new char[rows][cols];
        String result = "";
        int counter = 0; //counter keeps track of which letter we are on in the text
        int remainder = plaintext.length() % rows; //remainder is how many '@' are needed
        if (isEncrypt) {
            //if encrypting, first add the text into the array row by row, adding @s if needed
            for (int i = 0; i < letterArray.length; i++) {
                for (int j = 0; j < letterArray[0].length; j++) {
                    if (j == cols - 1 && remainder == 0 && plaintext.length() != (rows * cols)) {
                        letterArray[i][j] = '@';
                    }
                    else {
                        letterArray[i][j] = plaintext.charAt(counter);
                        if (counter < plaintext.length() - 1) {
                            counter++;
                        }
                        if (j == cols - 1) {
                            remainder--;
                        }
                    }
                }
            }
            //second, read back the array column by column
            for (int i = 0; i < letterArray[0].length; i++) {
                for (int j = 0; j < letterArray.length; j++) {
                    result += letterArray[j][i];
                }
            }
        }
        else {
            //if decrypting, first add the text into the array column by column
            for (int i = 0; i < letterArray[0].length; i++) {
                for (int j = 0; j < letterArray.length; j++) {
                    if (counter < plaintext.length()) {
                        letterArray[j][i] = plaintext.charAt(counter);
                    }
                    counter++;

                }
            }
            //second, read back the array row by row
            for (int i = 0; i < letterArray.length; i++) {
                for (int j = 0; j < letterArray[0].length; j++) {
                    if (letterArray[i][j] != '@') {
                        result += letterArray[i][j];
                    }
                }
            }
        }
        return result;
    }

    //vigenereCipher - this method encrypts or decrypts text using the vigenere cipher
    public String vigenereCipher(String plaintext, boolean isEncrypt) {
        char[] chars = new char[plaintext.length()];
        int i = 0;
        int j = 0;
        int newLetter;
        int shift;
        while(i < plaintext.length()) {
            int keywordIndex = j % vigenereKeyword.length(); //keywordIndex keeps track of where we are in the key
            shift = (int)(vigenereKeyword.getText().toString().toUpperCase().charAt(keywordIndex)) - 65;
            //add or subtract the shift depending on encrypting or decrypting
            if (isEncrypt) {
                newLetter = (int)plaintext.charAt(i) + shift;
            }
            else {
                newLetter = (int)plaintext.charAt(i) - shift;

            }
            //if the ascii value went past z or a, loop it to the other end of the alphabet
            if (newLetter < 65) {
                newLetter += 26;
            }
            if (newLetter > 90) {
                newLetter -= 26;
            }
            chars[i] = (char)newLetter;
            i++;
            j++;
        }
        return new String(chars);
    }
}
