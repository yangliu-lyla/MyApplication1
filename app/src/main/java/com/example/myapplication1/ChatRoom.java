package com.example.myapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
    String currentDateandTime = sdf.format(new Date());

    ChatMessage chatMessage;


private  RecyclerView.Adapter myAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        ChatMessageDAO mDAO = db.cmDAO();

        Executor thread = Executors.newSingleThreadExecutor();
        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());


            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages=chatModel.messages.getValue();

        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<>());
        }


        binding.sendButton.setOnClickListener(click ->{
           // messages.add(binding.textInput.getText().toString());
            chatMessage=new ChatMessage(binding.textInput.getText().toString(),currentDateandTime,true);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);
            });

        });

        binding.button2.setOnClickListener(click ->{
            // messages.add(binding.textInput.getText().toString());
            chatMessage=new ChatMessage(binding.textInput.getText().toString(),currentDateandTime,false);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);
            });

        });

        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;

            public MyRowHolder(@NonNull View itemView) {
            super(itemView);


           itemView.setOnClickListener((clk->{

               int position = getAdapterPosition();
               ChatMessage removedMessage = messages.get(position);

               AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage(("Do you want to delete the message: "+ messageText.getText()))
                        .setTitle("Question: ")
                        .setNegativeButton("No",(dialog,cl)->{})
                        .setPositiveButton("Yes",(dialog,cl)->{

                            Executor thread1=Executors.newSingleThreadExecutor();

                            thread1.execute(() -> {
                                mDAO.deleteMessage(removedMessage);
                                messages.remove(position);

                                runOnUiThread(()->{
                                    //on UI thread
                                    myAdapter.notifyDataSetChanged();
                                });


                            });



                            Snackbar.make(messageText, "You deleted message #"+position,Snackbar.LENGTH_LONG)
                                    .setAction("Undo",clk1->{
                                        messages.add(position,removedMessage);
                                        myAdapter.notifyItemInserted(position);

                                        // 向数据库重新插入消息
                                        Executor undoThread = Executors.newSingleThreadExecutor();
                                        undoThread.execute(() -> {
                                            mDAO.insertMessage(removedMessage);
                                        });

                                    })
                                    .show();

                }

                ).create().show();




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