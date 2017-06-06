package eos.hgs.eosfinalassignment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactListFragment extends Fragment {

    ArrayList<ContactInfo> contacts;

    private void getContacts() {
        Cursor c = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (c.moveToNext()) {
            ContactInfo info = new ContactInfo();
            info.name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            info.phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(info);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_list_layout, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        contacts = new ArrayList<>();

        Permission.askPermission(this, Manifest.permission.READ_CONTACTS, new Permission.FuncCall() {
            @Override
            public void call() {
                getContacts();
            }
        });

        rv.setAdapter(new ContactAdapter(this, contacts));
        rv.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_DENIED) {
                    getContacts();
                }
            } else {
                Toast.makeText(getActivity(), "권한 요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ContactInfo {
        String phone, name;

        @Override
        public String toString() {
            return "ContactInfo{" +
                    "phone='" + phone + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
