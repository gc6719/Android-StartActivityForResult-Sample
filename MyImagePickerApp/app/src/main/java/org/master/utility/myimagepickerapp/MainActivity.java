package org.master.utility.myimagepickerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class MainActivity extends AppCompatActivity {
    //Request code for image picker
    private int GALLERY_IMAGE_REQ_CODE = 102 ;

    //Request code for editor screen
    private int DS_PHOTO_EDITOR_REQ_CODE = 200 ;

//    Request code for share screen
    private int SHARE_SCREEN_REQ_CODE = 300 ;


    //Request code for permission
    private int STORAGE_PERMISSION_CODE = 1;

    //User picker Image Uri
    public static Uri imguri;

    AppCompatImageView imageView ;
    AppCompatButton pickImageBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        pickImageBtn = findViewById(R.id.pickImageBtn);
        pickImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMyClick();
            }
        });
        requestStoragePermission();
    }

    void onMyClick(){
        ImagePicker.with(MainActivity.this)
                .start(GALLERY_IMAGE_REQ_CODE);
    }


    // method to start editor
    void startEditor(Uri inputImageUri){
        Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
        dsPhotoEditorIntent.setData(inputImageUri);

        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "YOUR_OUTPUT_IMAGE_FOLDER");

        // Optional customization: hide some tools you don't need as below
        int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
        startActivityForResult(dsPhotoEditorIntent, DS_PHOTO_EDITOR_REQ_CODE);
    }

    // method to start sharescreen / final activity
    void startShareActivity(Uri inputImageUri){
        Intent dsPhotoEditorIntent = new Intent(this, FinalActivity.class);
        dsPhotoEditorIntent.setData(inputImageUri);
        startActivityForResult(dsPhotoEditorIntent, SHARE_SCREEN_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{

            if (resultCode == Activity.RESULT_OK) {

                if(requestCode == GALLERY_IMAGE_REQ_CODE  ){
                    //callback from image picker
                    imguri=data.getData();
                    startEditor(imguri);
                }if(requestCode == DS_PHOTO_EDITOR_REQ_CODE){
                    // callback from editor
                    imguri=data.getData();
                    startShareActivity(imguri);

                }else if(requestCode == SHARE_SCREEN_REQ_CODE){
                    // callback from share activity
                    imguri=data.getData();
                    if(imguri != null){
                        //if image uri is set then re-start editor
                        startEditor(imguri);
                    }else{
                        //if image uri is not set then , do nothing
                        Toast.makeText(this, "Nothing to do, Back pressed", Toast.LENGTH_SHORT).show() ;
                    }
                }
                else{
                    Toast.makeText(this, "Nothing to do", Toast.LENGTH_SHORT).show() ;
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}