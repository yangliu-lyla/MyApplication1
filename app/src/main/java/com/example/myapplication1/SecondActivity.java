package com.example.myapplication1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplication1.databinding.ActivitySecondBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SecondActivity extends AppCompatActivity {

    private String imageName = "picture.png";
    private  static String TAG = "SecondActivity";
    ActivitySecondBinding binding;
    ActivityResultLauncher<Intent> mCameraResult;

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( TAG, "The application is now visible on screen.");
    }

    protected void onPause() {
        super.onPause();
        Log.w( TAG, "The application no longer responds to user input.");


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = prefs.edit();

        String phoneNumber = binding.editTextPhone.getText().toString();
        edit.putString("PhoneNumber",  phoneNumber );


        edit.apply();


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("emailedit");

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedPhoneNumber =  prefs.getString("PhoneNumber", "");
        binding.editTextPhone.setText(savedPhoneNumber);

        binding.textView.setText("Welcome back "+emailAddress);

        String picPath = getFilesDir().getAbsolutePath() + File.separator + imageName;
        File profilePic = new File(picPath);
        if (profilePic.exists()){
            Bitmap thumbnail = BitmapFactory.decodeFile(picPath);
            binding.imageView.setImageBitmap(thumbnail);
        }

        binding.button.setOnClickListener(cl->{

            String phoneNum = binding.editTextPhone.getText().toString();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNum));
            startActivity(call);

        });


        mCameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            binding.imageView.setImageBitmap(thumbnail);

                            FileOutputStream fOut = null;
                            try{fOut = openFileOutput(imageName, Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG,100, fOut);
                                fOut.flush();
                                fOut.close();
                            }catch (FileNotFoundException e){
                                e.printStackTrace();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
        );

        binding.button2.setOnClickListener((v) -> {
            //Log.e(TAG, "clicked CHANGE PICTURE.");
            Intent cameraIntent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            mCameraResult.launch(cameraIntent);

            //startActivity(cameraIntent);
        });


//        binding.button2.setOnClickListener(cl-> {
//
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//            ActivityResultLauncher<Intent> cameraResult = null;
//            cameraResult.launch(cameraIntent);
//
//            cameraResult = registerForActivityResult(
//                    new ActivityResultContracts.StartActivityForResult(),
//                    new ActivityResultCallback<ActivityResult>() {
//                        @Override
//                        public void onActivityResult(ActivityResult result) {
//                            if (result.getResultCode() == Activity.RESULT_OK) {
//                                Intent data = result.getData();
//                                Bitmap thumbnail = data.getParcelableExtra("data");
//                                binding.imageView.setImageBitmap(thumbnail);
//                            }
//                        }
//
//                    }
//            );
//
//
//        });


    }
}