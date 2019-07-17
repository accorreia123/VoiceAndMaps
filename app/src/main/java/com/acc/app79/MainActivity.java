package com.acc.app79;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SPEAK_REQUEST=10;
TextView  textViewValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewValue=(TextView) findViewById(R.id.textViewValue);


        PackageManager packageManager=this.getPackageManager();
        List<ResolveInfo> listOfInformation = packageManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),
                0
        );
        if(listOfInformation.size()>0){

            Toast.makeText(MainActivity.this, "Device does support Speech Recognition",
                    Toast.LENGTH_SHORT).show();
            listenToTheUsersVice();
        }else {
            Toast.makeText(MainActivity.this, "Device does not support Speech Recognition",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void listenToTheUsersVice(){
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        Intent intent = voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk to me!");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
        startActivityForResult(voiceIntent,SPEAK_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SPEAK_REQUEST && resultCode==RESULT_OK){
            ArrayList<String> voiceWords=data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            float[] confidLevels=data.getFloatArrayExtra(
                    RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
            int index=0;
            String st="";
            for( String s : voiceWords){

                if (confidLevels!=null && index < confidLevels.length){
                    st+=s + " - "+confidLevels[index]+"\n";
                    textViewValue.setText(st);
                }
            }

        }
    }
}
