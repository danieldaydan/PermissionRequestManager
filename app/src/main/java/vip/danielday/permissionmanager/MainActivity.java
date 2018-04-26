package vip.danielday.permissionmanager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import vip.danielday.permissionrequest.PermissionRequestManager;
import vip.danielday.permissionrequest.annotation.PermissionCanceled;
import vip.danielday.permissionrequest.annotation.PermissionDenied;
import vip.danielday.permissionrequest.annotation.PermissionGranted;
import vip.danielday.permissionrequest.util.SettingUtil;


public class MainActivity extends AppCompatActivity  {

    private Button btn_click;
    private Button btn_click1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_container, new PermissionFragment());
        transaction.commitAllowingStateLoss();
    }

    private void initViews() {
        btn_click = (Button) findViewById(R.id.btn_click);
        btn_click1 = (Button) findViewById(R.id.btn_click1);
    }


    private void initEvents() {
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone();
            }
        });
        btn_click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMap();
            }
        });
    }

    private void callPhone(){
        PermissionRequestManager.PermissionRequest(this, new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA},10);
    }

    private void callMap(){
        PermissionRequestManager.PermissionRequest(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
    }

    @PermissionGranted
    public void onPermissionGranted(int requestCode) {
        switch (requestCode){
            case 10:
                Toast.makeText(this, "电话、相机权限申请通过", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                Toast.makeText(this, "定位权限申请通过", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @PermissionDenied
    public void onPermissionDenied(int requestCode, List<String> denyList) {
        switch (requestCode) {
            case 10:
                //多个权限申请返回结果
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < denyList.size(); i++) {
                    if (Manifest.permission.CALL_PHONE.equals(denyList.get(i))) {
                        builder.append("电话");
                    } else if (Manifest.permission.CAMERA.equals(denyList.get(i))) {
                        builder.append("相机");
                    }
                }
                builder.append("权限被禁止，需要手动打开");
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage(builder)
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SettingUtil.go2Setting(MainActivity.this);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();

                break;
            case 0:
                //单个权限申请返回结果
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("定位权限被禁止，需要手动打开")
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SettingUtil.go2Setting(MainActivity.this);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                break;
            default:
                break;
        }
    }

    @PermissionCanceled
    public void onPermissionCanceled(int requestCode) {
        Toast.makeText(this, "requestCode:" + requestCode, Toast.LENGTH_SHORT).show();
    }


    /**
     * 在Service中申请权限
     *
     * @param view
     */
    public void startPermissionService(View view) {
        Intent intent = new Intent(this, PermissionService.class);
        startService(intent);
    }
}
