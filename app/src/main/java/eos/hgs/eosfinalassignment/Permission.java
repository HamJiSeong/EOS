package eos.hgs.eosfinalassignment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import java.util.concurrent.Callable;


public class Permission {

    public interface FuncCall{
        public void call();
    }
    public static void askPermission(final Fragment fg, final String permission, FuncCall func){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResult = fg.getContext().checkSelfPermission(permission);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                if (fg.shouldShowRequestPermissionRationale(permission)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(fg.getContext());
                    dialog.setTitle("권한이 필요합니다.").setMessage("이 기능을 사용하기 위해서는 단말기의 \"연락처 읽기\" 권한이 필요합니다. 계속 하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        fg.requestPermissions(new String[]{permission}, 1000);
                                    }
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(fg.getContext(), "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
                                }
                            }).create().show();
                } else {
                    fg.requestPermissions(new String[]{permission}, 1000);
                }
            } else {
                func.call();
            }
        } else {
        }

    }
}
