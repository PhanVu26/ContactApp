package com.example.contact;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapterRecents extends ArrayAdapter<ContactRecents> {

    private Context context;
    private int resource;
    private ArrayList<ContactRecents> arrContact;

    public CustomAdapterRecents(@NonNull Context context, int resource, @NonNull ArrayList<ContactRecents> arrContact) {
        super(context, resource, arrContact);
        this.context = context;
        this.resource = resource;
        this.arrContact = arrContact;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CustomAdapterRecents.ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_listview_recents,parent,false);
            viewHolder = new CustomAdapterRecents.ViewHolder();
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.ivCallType = (ImageView) convertView.findViewById(R.id.iv_call_type);
            viewHolder.civAvatar = (CircleImageView) convertView.findViewById(R.id.civ_avatar);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (CustomAdapterRecents.ViewHolder) convertView.getTag();
        }
        final ContactRecents contactRecents = arrContact.get(position);
        viewHolder.tvPhone.setText(contactRecents.getPhone());
        viewHolder.civAvatar.setImageResource(R.drawable.ic_person_black_24dp);
        if(contactRecents.getCallType().equals("OUTGOING")){
            viewHolder.ivCallType.setImageResource(R.drawable.ic_call_made_black_24dp);
        }else if(contactRecents.getCallType().equals("INCOMING")){
            viewHolder.ivCallType.setImageResource(R.drawable.ic_call_received_black_24dp);
        }else
            viewHolder.ivCallType.setImageResource(R.drawable.ic_call_missed_black_24dp);
        DateFormat sdf= new SimpleDateFormat("EEE, MMM dd KK:mm:ss a",new Locale("en"));
        viewHolder.tvDate.setText(sdf.format(contactRecents.getDate()));
        return convertView;
    }
    public class ViewHolder{
        TextView tvPhone;
        ImageView ivCallType;
        CircleImageView civAvatar;
        TextView tvDate;
    }
}
