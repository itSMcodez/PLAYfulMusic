package com.itsmcodez.playful;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.itsmcodez.playful.databinding.ActivityPermissionsBinding;

public class PermissionsActivity extends AppCompatActivity {
    private ActivityPermissionsBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    
    @Override
    protected void onStart() {
        super.onStart();
        
        if(isPermissionChecked(STORAGE_PERMISSION)){
            this.startActivity(new Intent(this, MainActivity.class));
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to views
        binding = ActivityPermissionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        binding.storagePermissionCard.setOnClickListener(view -> {
            requestPermission(STORAGE_PERMISSION);
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PERMISSION_REQUEST_CODE == requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(PermissionsActivity.this, MainActivity.class));
        }
    }
    
    private boolean isPermissionChecked(String permission) {
        
        if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
    	else return false;
    }
    
    private void requestPermission(String permission){
        ActivityCompat.requestPermissions(this, new String[]{STORAGE_PERMISSION}, PERMISSION_REQUEST_CODE);
    }
}
