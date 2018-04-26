package vip.danielday.permissionmanager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import java.util.List;

import vip.danielday.permissionrequest.PermissionRequestManager;
import vip.danielday.permissionrequest.annotation.PermissionCanceled;
import vip.danielday.permissionrequest.annotation.PermissionDenied;
import vip.danielday.permissionrequest.annotation.PermissionGranted;
import vip.danielday.permissionrequest.util.SettingUtil;


public class PermissionFragment extends Fragment {

    private Button btn_permission;

    public PermissionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        btn_permission = (Button) view.findViewById(R.id.btn_permission);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone();
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
    public void PermissionGranted(int requestCode) {
        switch (requestCode){
            case 10:
                Toast.makeText(getActivity(), "电话、相机权限申请通过", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                Toast.makeText(getActivity(), "定位权限申请通过", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @PermissionDenied
    public void PermissionDenied(int requestCode, List<String> denyList) {
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
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage(builder)
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SettingUtil.go2Setting(getActivity());
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
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("定位权限被禁止，需要手动打开")
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SettingUtil.go2Setting(getActivity());
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
    public void PermissionCanceled(int requestCode) {
        Toast.makeText(getActivity(), "requestCode:" + requestCode, Toast.LENGTH_SHORT).show();
    }
}
