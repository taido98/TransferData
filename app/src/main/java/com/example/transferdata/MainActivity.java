package com.example.transferdata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView mImgSendData, mImgReceiveData;
    protected OnBackPressedListener onBackPressedListener;
    private SharedPreferences sharedPreferences;
    private Button mBtnChooseDirectory;
    private String chosenDir = "";
    private boolean newFolderEnabled = true;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initAction();
    }
    private void initView() {
        mImgSendData = findViewById(R.id.img_send_data);
        mImgReceiveData = findViewById(R.id.img_receive_data);
        mBtnChooseDirectory = findViewById(R.id.btn_choose_directory);
    }
    private void initAction() {
        mImgSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(intent);
            }
        });
        mImgReceiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HostActivity.class);
                startActivity(intent);
            }
        });
        mBtnChooseDirectory.setOnClickListener(l -> {

            // Create DirectoryChooserDialog and register a callback
            DirectoryChooserDialog directoryChooserDialog =
                    new DirectoryChooserDialog(MainActivity.this, chosenDir -> {
                        this.chosenDir = chosenDir;
                        Toast.makeText(MainActivity.this, "Chosen directory: " + chosenDir, Toast.LENGTH_LONG).show();

                        sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString(Variables.APP_TYPE, chosenDir);
                        sharedPreferencesEditor.commit();
                    });
            // Toggle new folder button enabling
            directoryChooserDialog.setNewFolderEnabled(this.newFolderEnabled);
            // Load directory chooser dialog for initial 'm_chosenDir' directory.
            // The registered callback will be called upon final directory selection.
            directoryChooserDialog.chooseDirectory(this.chosenDir);
            this.newFolderEnabled = !this.newFolderEnabled;
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.onBackPressedListener = (() -> {
            Toast.makeText(MainActivity.this, "Please press again to exit", Toast.LENGTH_SHORT).show();
            MainActivity.this.onBackPressedListener = null;
        });
    }
    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }
}
