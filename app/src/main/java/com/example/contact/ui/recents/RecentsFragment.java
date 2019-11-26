package com.example.contact.ui.recents;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.contact.ContactRecents;
import com.example.contact.CustomAdapterRecents;
import com.example.contact.R;

import java.util.ArrayList;
import java.util.Date;

public class RecentsFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    ArrayList<ContactRecents> list;
    CustomAdapterRecents adapter;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recents, container, false);
        list = new ArrayList<ContactRecents>();
        listView = (ListView) root.findViewById(R.id.lv_contact_recents);
        getCallDetails();
        return root;
    }

    private void getCallDetails() {
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CALL_LOG},1);
        }
        Cursor cursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            ContactRecents contactRecents = new ContactRecents();
            contactRecents.setPhone(phNumber);
            contactRecents.setCallType(dir);
            contactRecents.setDate(callDayTime);
            list.add(contactRecents);

        }
        cursor.close();
        adapter = new CustomAdapterRecents(getContext(),R.layout.row_listview_recents,list);
        listView.setAdapter(adapter);
    }
}