package kr.palmapps.palmpay_dev_ver4;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import kr.palmapps.palmpay_dev_ver4.lib.DevToast;

/**
 * 앱 시작시 필요한 권한을 처리
 */
public class PermissionActivity extends AppCompatActivity {

    private static final int PERMISSION_MULTI_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        if (Build.VERSION.SDK_INT > 23) {
            if (checkAndRequestPermissions()) {
                goIndexActivity();
            }
        }
    }

    private boolean checkAndRequestPermissions() {
        String [] permissions = new String[] {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH,
        };

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String permission:permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }

        if(!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_MULTI_CODE);

            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults.length == 0) return;

        switch (requestCode) {
            case PERMISSION_MULTI_CODE:
                checkPermissionResult(permissions, grantResults);

                break;
        }
    }

    private void checkPermissionResult(String[] permissions, int[] grantResults) {
        boolean isAllGranted = true;

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
            }
        }

        if (isAllGranted) {
            goIndexActivity();
        }

        else {
            showPermissionDialog();
        }
    }

    /**
     * dialog를 보여줌
     */
    private void showPermissionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.permission_setting_title)
                .setMessage(R.string.permission_setting_message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        DevToast.s(PermissionActivity.this, R.string.permission_setting_restart);

                        PermissionActivity.this.finish();

                        goAppSettingActivity();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                        PermissionActivity.this.finish();
                    }
                })
                .show();
    }

    /**
     * IndexActivity로 이동
     */
    public void goIndexActivity() {
        Intent intent = new Intent(this, IndexActivity.class);
        startActivity(intent);

        finish();
    }

    /**
     * App Setting Activity로 이동
     */
    private void goAppSettingActivity() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

}
