package com.example.contact;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resource;
    private ArrayList<Contact> arrContact;
    private ArrayList<Contact> temp1 ;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Contact> arrContact) {
        super(context, resource, arrContact);
        this.context = context;
        this.resource = resource;
        this.arrContact = arrContact;
        this.temp1 = new ArrayList<Contact>();
        this.temp1.addAll(arrContact);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_listview,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.btnCall = (ImageButton) convertView.findViewById(R.id.btn_call);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Contact contact = arrContact.get(position);
        viewHolder.tvName.setText(contact.getName());
        viewHolder.btnCall.setImageResource(R.drawable.ic_phone);
        if(contact.uriAvatar.equals("")){
            viewHolder.avatar.setImageResource(R.drawable.ic_person_black_24dp);
        }
        else  viewHolder.avatar.setImageURI(Uri.parse(contact.getUriAvatar()));
        viewHolder.btnCall.setFocusable(false);
        // Call button
        viewHolder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",contact.getPhone(),null));
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    public class ViewHolder{
        CircleImageView avatar;
        TextView tvName;
        ImageButton btnCall;
    }
    // function Search
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrContact.clear();
        if (charText.length() == 0) {
            arrContact.addAll(temp1);
        } else {
            for (Contact wp : temp1) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrContact.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
