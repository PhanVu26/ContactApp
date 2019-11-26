package com.example.contact.ui.contacts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.contact.AddContactActivity;
import com.example.contact.Contact;
import com.example.contact.CustomAdapter;
import com.example.contact.InforContactActivity;
import com.example.contact.MainActivity;
import com.example.contact.MyDatabase;
import com.example.contact.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ContactsFragment extends Fragment {
    static final int ADD_CODE = 123;
    static final int EDT_CODE = 456;
    private MyDatabase db;
    Contact editContact;
    ArrayList<Contact> contacts;
    ArrayList<Contact> temp;
    FloatingActionButton floatingNewContactBtn;
    ArrayList<Contact> listContacts;
    CustomAdapter customAdapter;
    ListView lvContact;
    SearchView searchView;
    CustomAdapter adapter;
    int index;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
        floatingNewContactBtn = (FloatingActionButton) root.findViewById(R.id.floatinngNewContactBtn);
        listContacts = new ArrayList<Contact>();
        temp = new ArrayList<Contact>();
        lvContact = (ListView) root.findViewById(R.id.lv_contact);
        db = new MyDatabase(getActivity());
        contacts = db.getAllContact();
        listContacts.addAll(contacts);
        customAdapter = new CustomAdapter(getActivity(),R.layout.row_listview,listContacts);
        lvContact.setAdapter(customAdapter);

        adapter = new CustomAdapter(getActivity(),R.layout.row_listview,listContacts);
        //Getting the instance of AutoCompleteTextView
        MultiAutoCompleteTextView actv= (MultiAutoCompleteTextView)root.findViewById(R.id.sv_search);
        //actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = (Contact) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), InforContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("contact",contact);
                intent.putExtra("package",bundle);
                index = listContacts.indexOf(contact);
                editContact = contact;
                //db.deleteContact(contact);
                //startActivity(intent);
                startActivityForResult(intent,EDT_CODE);
            }
        });
        floatingNewContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                startActivityForResult(intent,ADD_CODE);
            }
        });
        return root;
    }

    // for AddContactActivity
    @Override
    public  void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if(requestCode == ADD_CODE && resultCode == RESULT_OK){ // AddActivity
            Bundle bundle = intent.getBundleExtra("package");
            Contact contact = (Contact) bundle.getSerializable("contact");
            db.addContact(contact);
            //contacts = db.getAllContact();
            listContacts.add(contact);
            //listContacts.addAll(contacts);
            customAdapter.notifyDataSetChanged();

        }
        else if(requestCode == EDT_CODE && resultCode == RESULT_OK){ // EditActivity
            Bundle bundle = intent.getBundleExtra("package");
            Contact contact = (Contact) bundle.getSerializable("contact");
            listContacts.remove(index);
            customAdapter.notifyDataSetChanged();
            listContacts.add(index,contact);
            db.addContact(contact);
            db.deleteContact(editContact);
            customAdapter.notifyDataSetChanged();
        }

    }
}