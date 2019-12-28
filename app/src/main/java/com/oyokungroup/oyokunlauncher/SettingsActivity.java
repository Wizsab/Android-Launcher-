package com.oyokungroup.oyokunlauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    ImageView mHomeScreenImage;
    LinearLayout mDesktopGrid;
    TextView mGridLabel;

    int mNumRow, mNumColumn, checkedItem = 0;

    int REQUEST_CODE_IMAGE = 1;
    String PREFS_NAME = "OyokunPrefs";

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button mHomeScreenButton = findViewById(R.id.homeScreenButton);
        mDesktopGrid = findViewById(R.id.desktopGrid);

        mDesktopGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        mHomeScreenImage = findViewById(R.id.homeScreenImage);

        mHomeScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        getData();
    }

    private void getData(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String imageUriString = sharedPreferences.getString("imageUri", null);
        int numRow = sharedPreferences.getInt("numRow", 7);
        int numColumn = sharedPreferences.getInt("numColumn", 5);

        if(imageUriString != null){
            imageUri = Uri.parse(imageUriString);
            mHomeScreenImage.setImageURI(imageUri);
        }

        mGridLabel = findViewById(R.id.gridLabel);
        String mGrid = numRow +"x" + numColumn;
        mGridLabel.setText(mGrid);
    }

    private void saveData(){
        SharedPreferences.Editor sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();

        if(imageUri != null)
            sharedPreferences.putString("imageUri", imageUri.toString());

        sharedPreferences.putInt("numRow", mNumRow);
        sharedPreferences.putInt("numColumn", mNumColumn);
        sharedPreferences.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK){
            imageUri = data.getData();
            mHomeScreenImage.setImageURI(imageUri);
            saveData();
        }
    }
    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
        alertDialog.setTitle("Desktop Grid");
        String[] items = {"4x4","5x4","4x5","5x5"};

        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mNumRow = 4;
                        mNumColumn = 4;
                        break;
                    case 1:
                        mNumRow = 5;
                        mNumColumn = 4;
                        break;
                    case 2:
                        mNumRow = 4;
                        mNumColumn = 5;
                        break;
                    case 3:
                        mNumRow = 5;
                        mNumColumn = 5;
                        break;
                }
            }
        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData();
                getData();
            }
        }).
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getData();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}