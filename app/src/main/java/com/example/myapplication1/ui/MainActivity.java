package com.example.myapplication1.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication1.R;
import com.example.myapplication1.data.MainViewModel;
import com.example.myapplication1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    ImageView imgView;
    Switch sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView=findViewById(R.id.imageView);
        sw=findViewById(R.id.spin_switch);

        sw.setOnCheckedChangeListener((btn, isChecked)->{
            if(isChecked)
            {
                RotateAnimation rotate=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotate.setDuration(5000);
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setInterpolator(new LinearInterpolator());

                imgView.startAnimation(rotate);
            }
            else {
                imgView.clearAnimation();
            }
        });

//        model =new ViewModelProvider(this).get(MainViewModel.class);
//
//        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(variableBinding.getRoot());
//
//        variableBinding.mybutton.setOnClickListener((click)->{
//            model.editString.postValue(variableBinding.myedittext.getText().toString());
//        });
//
//        model.editString.observe(this,s->{
//            variableBinding.mytextview.setText("Your edit text has: "+s);
//        });
//
//        model.isSelected.observe(this, selected->{
//            variableBinding.checkBox.setChecked(selected);
//            variableBinding.radioButton.setChecked(selected);
//            variableBinding.switch1.setChecked(selected);
//
//            Context context = getApplicationContext();
//            CharSequence text = "The value is now: "+selected;
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//
//        });
//
//        variableBinding.checkBox.setOnCheckedChangeListener((checkBox,isChecked)->{
//            model.isSelected.postValue(isChecked);
//        });
//
//        variableBinding.radioButton.setOnCheckedChangeListener((checkBox,isChecked)->{
//            model.isSelected.postValue(isChecked);
//        });
//
//        variableBinding.switch1.setOnCheckedChangeListener((checkBox,isChecked)->{
//            model.isSelected.postValue(isChecked);
//        });
//
//        variableBinding.myimagebutton.setOnClickListener((click)->{
//
//            Context context = getApplicationContext();
//            CharSequence text = "The width = "+variableBinding.myimagebutton.getWidth()+
//                    " and height = "+variableBinding.myimagebutton.getHeight();
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//
//            //model.editString.postValue(variableBinding.myedittext.getText().toString());
//        });



        /*
        variableBinding.mytextview.setText(model.editString);
        variableBinding.mybutton.setOnClickListener((click)->{
        model.editString=variableBinding.myedittext.getText().toString();
        variableBinding.mytextview.setText("Your edit text has: "+model.editString);
        });
        */


        /*use viewbinding
        variableBinding.mybutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String editString = variableBinding.myedittext.getText().toString();
                variableBinding.mytextview.setText("Your edit text has: " + editString);
            }
        } );
        */


       /*
        setContentView(R.layout.activity_main);

        EditText myedit = findViewById(R.id.myedittext);
        TextView mytext = findViewById(R.id.mytextview);
        Button mybutton = findViewById(R.id.mybutton);

        //anonymous class
        mybutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
               String editString = myedit.getText().toString();
               mytext.setText("Your edit text has: "+editString);
            }
        });
       */


    }
}