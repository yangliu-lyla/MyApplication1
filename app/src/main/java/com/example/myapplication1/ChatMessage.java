package com.example.myapplication1;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey (autoGenerate=true)
    @ColumnInfo(name="id")
    public int id;
    @ColumnInfo(name="message")
    String message;
    @ColumnInfo(name="TimeSent")
    String  timeSent;
    @ColumnInfo(name="SendOrReceive")
    boolean isSentButton;


    ChatMessage (String m,String t, boolean sent){
        this.message =m;
        this.timeSent =t;
        this.isSentButton =sent;

    }

    ChatMessage(){

    }

    public String getMessage(){
        return message;
    }

    public String getTimeSent(){
        return timeSent;
    }

    public boolean isSentButton(){
        return isSentButton;
    }
}
