package com.example.myapplication1;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatMessageDAO {
    @Insert
    public long insertMessage(ChatMessage m);

    @Update
    public  int anyUpdate(ChatMessage updateMessage);

    @Query("Select * from ChatMessage")
    public List<ChatMessage> getAllMessages();


    @Delete
    @Transaction
    public void deleteMessage(ChatMessage m);

}
