package org.master.utility.myimagepickerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FinalActivity extends AppCompatActivity {

    AppCompatImageView finalImage;
    AppCompatButton reEditImageBtn;
    AppCompatButton reStartBtn;
    Uri imgUriReceived ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        finalImage = findViewById(R.id.finalImage);
        reEditImageBtn = findViewById(R.id.reEditImageBtn);
        reStartBtn = findViewById(R.id.reStartBtn);
        setImageFromPreviousScreen();
        buttonClickHandler();
    }

    void setImageFromPreviousScreen(){
        imgUriReceived = getIntent().getData();
        if(imgUriReceived != null){
            finalImage.setImageURI(imgUriReceived);
        }else{
            Toast.makeText(this, "No Image found", Toast.LENGTH_SHORT).show() ;
        }
    }


    void buttonClickHandler(){
        reEditImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgUriReceived != null ){
                    // set Image Uri for restarting editor
                    Intent editorRequestIntent = new Intent();
                    editorRequestIntent.setData(imgUriReceived);
                    setResult(Activity.RESULT_OK, editorRequestIntent);
                    finish();
                }else{
                    finish();
                }
            }
        });

        reStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do not set Image Uri
                finish();
            }
        });



    }

   /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case 200:
                    outputUri = data.getData();
                    bindidng.resultImage.setImageURI(outputUri);
                    break;

            }

        }

    }*/

}