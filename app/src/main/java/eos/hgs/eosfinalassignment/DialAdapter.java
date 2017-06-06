package eos.hgs.eosfinalassignment;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DialAdapter extends BaseAdapter {

    private final int N = 12;
    private Context context;
    private String[] dials, captions;

    public DialAdapter(Context context) {
        this.context = context;
        dials = context.getResources().getStringArray(R.array.dials);
        captions = context.getResources().getStringArray(R.array.dial_captions);
    }

    @Override
    public int getCount() {
        return N;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.key_layout, parent, false);
            TextView vNumber = (TextView) view.findViewById(R.id.dial_number);
            TextView vChar = (TextView) view.findViewById(R.id.dial_char);
            vNumber.setText(dials[position]);
            if (captions[position].equals("")) {
                vChar.setVisibility(TextView.GONE);
                vNumber.setGravity(Gravity.CENTER);
            } else vChar.setText(captions[position]);
            return view;
        } else {
            return convertView;
        }
    }
}
