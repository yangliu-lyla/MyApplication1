package com.example.myapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.myapplication1.databinding.ActivityChatRoomBinding;
import com.example.myapplication1.databinding.ReceiveMessageBinding;
import com.example.myapplication1.databinding.SentMessageBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ChatRoom extends AppCompatActivity {
ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel ;
    ArrayList<ChatMessage> messages=new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
  //  String currentDateandTime = sdf.format(new Date());

    ChatMessage chatMessage;


private  RecyclerView.Adapter myAdapter;

    Executor thread = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        ChatMessageDAO mDAO = db.cmDAO();
        FrameLayout fragmentLocation = findViewById(R.id.fragmentLocation);
        boolean IamTablet = fragmentLocation != null;

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        chatModel.selectedMessage.observe(this, newMessageValue -> {

            if (newMessageValue!=null) {
//                MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
//                FragmentManager fMgr = getSupportFragmentManager();
//                FragmentTransaction tx = fMgr.beginTransaction();
//
//                 tx.add(R.id.fragmentLocation, chatFragment);
//                tx.commit();

                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();

                // 移除之前添加的旧 Fragment
                Fragment oldFragment = fMgr.findFragmentById(R.id.fragmentLocation);
                if (oldFragment != null) {
                    tx.remove(oldFragment);
                }


                // 创建并添加新的 Fragment
                MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
                tx.add(R.id.fragmentLocation, chatFragment);
                tx.addToBackStack("");
                tx.commit();


            }

        });



        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        messages=chatModel.messages.getValue();

        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<>());
        }


        binding.sendButton.setOnClickListener(click ->{
           // messages.add(binding.textInput.getText().toString());
            String currentDateandTime = sdf.format(new Date());
            chatMessage=new ChatMessage(binding.textInput.getText().toString(),currentDateandTime,true);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
            thread.execute(() -> {
               long id =  mDAO.insertMessage(chatMessage);
               chatMessage.id=(int)id;
            });

        });

        binding.button2.setOnClickListener(click ->{
            // messages.add(binding.textInput.getText().toString());
            String currentDateandTime = sdf.format(new Date());
            chatMessage=new ChatMessage(binding.textInput.getText().toString(),currentDateandTime,false);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);
                long id =  mDAO.insertMessage(chatMessage);
                chatMessage.id=(int)id;
            });

        });

        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;

            public MyRowHolder(@NonNull View itemView) {
            super(itemView);

           itemView.setOnClickListener((clk->{

               int position = getAdapterPosition();
               ChatMessage selected = messages.get(position);

               chatModel.selectedMessage.postValue(selected);
               // 显示详情布局
               binding.fragmentLocation.setVisibility(View.VISIBLE);

                /*
               AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage(("Do you want to delete the message: "+ messageText.getText()))
                        .setTitle("Question: ")
                        .setNegativeButton("No",(dialog,cl)->{})
                        .setPositiveButton("Yes",(dialog,cl)->{
                            // ChatMessage m =messages.get(position);
                          //    mDAO.deleteMessage(m);
                            ChatMessage removedMessage = messages.remove(position);

                            myAdapter.notifyItemRemoved(position);

                            Snackbar.make(messageText, "You deleted message #"+position,Snackbar.LENGTH_LONG)
                                    .setAction("Undo",clk1->{
                                        messages.add(position,removedMessage);
                                        myAdapter.notifyItemInserted(position);

                                    })
                                    .show();


                }).create().show(); */




           }));

            messageText=itemView.findViewById(R.id.message);
            timeText=itemView.findViewById(R.id.time);
            }
        }


        binding.recycleView.setAdapter(myAdapter=new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               if (viewType==0){
                SentMessageBinding binding=
                        SentMessageBinding.inflate(getLayoutInflater(),parent,false);
                return new MyRowHolder(binding.getRoot());}
                else {
                   ReceiveMessageBinding binding =
                           ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
               }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                //holder.messageText.setText("");
                //holder.timeText.setText("");
                ChatMessage obj=messages.get(position);

                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage chatMessage=messages.get(position);
                if (chatMessage.isSentButton()){
                return 0;}

                else return 1;
            }



        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));


    }
}