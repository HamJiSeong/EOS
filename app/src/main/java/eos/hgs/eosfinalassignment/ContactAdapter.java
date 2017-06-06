package eos.hgs.eosfinalassignment;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{
    private ArrayList<ContactListFragment.ContactInfo> contacts;
    private Fragment fg;

    public ContactAdapter(Fragment fg, ArrayList<ContactListFragment.ContactInfo> contacts) {
        this.contacts = contacts;
        this.fg = fg;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactListFragment.ContactInfo item = contacts.get(position);
        holder.tv_name.setText(item.name);
        holder.tv_phone.setText(item.phone);
        final String phone = item.phone;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permission.askPermission(fg, Manifest.permission.READ_CONTACTS, new Permission.FuncCall() {
                    @Override
                    public void call() {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:".concat(phone)));
                        fg.startActivity(intent);
                    }
                });
            }
        });
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_phone;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_contact_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_contact_phone_numbers);
        }

    }
}
