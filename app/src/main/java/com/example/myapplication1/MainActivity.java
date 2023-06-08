package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This page has the function to check the format of password.
 *
 * @author Yang Liu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the centre of the screen. */
    TextView tv = null;
    /** This holds the edittext at the centre of the screen. */
    EditText et = null;
    /** This holds the button on the bottom of the screen. */
    Button btn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);


        btn.setOnClickListener(clk->{
           String password = et.getText().toString();
           if(checkPasswordComplexity(password))
                tv.setText("Your password meets the requirements.");
            else tv.setText("You shall not pass!");



        });
    }

    /**
     * This function is used to check if the password is correct.
     *
     * @param pw The String object that we are checking
     * @return Return true if the password is complex enough, and false if it is not complex enough.
     */
    boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;


        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isDigit(c)) {
                foundNumber=true;
            } else if (Character.isUpperCase(c)) {
                foundUpperCase=true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (isSpecialCharacter(c)){
                foundSpecial = true;
            }
        }

        if(!foundUpperCase)
        {
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this /* MyActivity */, "You are missing an upper case letter", duration);
            toast.show();

           return false;

        }  else if( ! foundLowerCase)
        {
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this /* MyActivity */, "You are missing an lower case letter", duration);
            toast.show();

            return false;

        }  else if( ! foundNumber) {
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this /* MyActivity */, "You are missing a number", duration);
            toast.show();
            return false;
        }  else if(! foundSpecial) {
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this /* MyActivity */, "You are missing an special character", duration);
            toast.show();
            return false;
        }

        else    return true; //only get here if they're all true



     }

    private boolean isSpecialCharacter(char c) {
        switch(c)
        {
            case '#':
            case '?':
            case '*':
                return true;
            default:
                return false;
        }
    }


}