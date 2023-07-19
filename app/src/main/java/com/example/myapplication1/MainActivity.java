package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication1.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    protected String cityName;

    protected RequestQueue queue = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        ActivityMainBinding binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.button.setOnClickListener(clk-> {
            cityName = binding.editText.getText().toString();
            String url = null;
            try {
                url = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName,"UTF-8")
                        + "&appid=10397cd3538c42e96e19cb5e411691d6&units=metric";

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
            (response) -> {
                //JSONObject coord=response.getJSONArray("coord");
                try {
                    JSONArray weatherArray = response.getJSONArray("weather");
                    JSONObject position0 = weatherArray.getJSONObject(0);
                    JSONObject mainObject = response.getJSONObject( "main" );

                    String description = position0.getString("description");
                    String iconName = position0.getString("icon");
                    double current = mainObject.getDouble("temp");
                    double min = mainObject.getDouble("temp_min");
                    double max = mainObject.getDouble("temp_max");
                    int humidity = mainObject.getInt("humidity");
                    String picurl="https://openweathermap.org/img/w/" + iconName + ".png";

                    runOnUiThread( (  )  -> {

                        binding.temp.setText("The current temperature is " + current);
                        binding.temp.setVisibility(View.VISIBLE);

                        binding.minTemp.setText("The min temperature is " + min);
                        binding.minTemp.setVisibility(View.VISIBLE);

                        binding.maxTemp.setText("The max temperature is " + max);
                        binding.maxTemp.setVisibility(View.VISIBLE);

                        binding.humidity.setText("The humitidy is " + humidity+"%");
                        binding.humidity.setVisibility(View.VISIBLE);

                        binding.description.setText("The description is " + description);
                        binding.description.setVisibility(View.VISIBLE);


                    });


                    ImageRequest imgReq = new ImageRequest(picurl, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            // Do something with loaded bitmap...
                            FileOutputStream fOut = null;
                            try {
                                fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);

                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

                                String filePath = getFilesDir().getAbsolutePath() + "/" + iconName + ".png";
                                Bitmap savedBitmap = BitmapFactory.decodeFile(filePath);

                                binding.icon.setImageBitmap( savedBitmap );
                                binding.icon.setVisibility(View.VISIBLE);
                                fOut.flush();
                                fOut.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                            (error ) -> {

                    });

                    queue.add(imgReq);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            },
                    (error) -> {
                    });
            queue.add(request);

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