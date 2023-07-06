package com.example.myapplication1;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData< >();
    public MutableLiveData<ChatMessage> selectedMessage = new MutableLiveData< >();
}