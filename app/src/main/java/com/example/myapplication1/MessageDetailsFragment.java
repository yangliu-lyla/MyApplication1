package com.example.myapplication1;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication1.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {

    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m){

        selected=m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding=DetailsLayoutBinding.inflate(inflater);

        binding.MessageText.setText(selected.message);
        binding.timeText.setText(selected.timeSent);
        binding.databaseText.setText("Id= "+selected.id);

        return binding.getRoot();


    }

    @Override
    public void onPause() {
        super.onPause();
        // 隐藏 Fragment
        getView().setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 显示 Fragment
        getView().setVisibility(View.VISIBLE);
    }

}
