package eos.hgs.eosfinalassignment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class KeypadFragment extends Fragment implements MainActivity.BackKeyListener {

    private GridView keypad;
    private TextView numPanel;
    private StringBuffer buf;
    private MainActivity.BackKeyListener listener;
    public KeypadFragment() {
        buf = new StringBuffer();
    }

    @Override
    public boolean onBackKeyPressed() {
        return deleteNumber();
    }

    private void addNumber(int dial) {
        if (dial <= 9) buf.append(String.valueOf(dial));
        else buf.append(dial == 10 ? "*" : "#");
        int len = buf.length();
        if (len <= 14) numPanel.setText(buf.toString());
        else numPanel.setText(buf.substring(len - 14));
    }

    private boolean deleteNumber() {
        int len = buf.length();
        if (len == 0) return false;
        buf.deleteCharAt(--len);
        if (len <= 14) numPanel.setText(buf.toString());
        else numPanel.setText(buf.substring(len - 14));
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.keypad_layout, container, false);
        keypad = (GridView) v.findViewById(R.id.gv_keypad);
        numPanel = (TextView) v.findViewById(R.id.numberPanel);
        keypad.setAdapter(new DialAdapter(getContext()));
        keypad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int dial;
                if (position < 9) dial = position + 1;
                else if (position == 10) dial = 0;
                else if (position == 9) dial = 10;
                else dial = 11;
                addNumber(dial);
            }
        });
        Button call = (Button) v.findViewById(R.id.btn_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionResult = getContext().checkSelfPermission(Manifest.permission.CALL_PHONE);
                    if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setTitle("권한이 필요합니다.").setMessage("이 기능을 사용하기 위해서는 단말기의 \"전화걸기\" 권한이 필요합니다. 계속 하시겠습니까?")
                                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                            }
                                        }
                                    })
                                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getActivity(), "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
                                        }
                                    }).create().show();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:".concat(buf.toString())));
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:".concat(buf.toString())));
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:".concat(buf.toString())));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_DENIED) {
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getActivity(), "권한 요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
