package com.example.contact.ui.contacts;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class ContactsFragment extends Fragment implements SearchView.OnQueryTextListener{
    static final int ADD_CODE = 123;
    static final int EDT_CODE = 456;
    private static final int REQUEST_CODE = 1234;
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
    ImageView searchByVoice;
    int index;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);

        // Khởi tạo giá trị cho các biến
        floatingNewContactBtn = (FloatingActionButton) root.findViewById(R.id.floatinngNewContactBtn);
        lvContact = (ListView) root.findViewById(R.id.lv_contact);
        searchView = root.findViewById(R.id.sv_contact);
        searchView.setOnQueryTextListener(this);
        searchByVoice = root.findViewById(R.id.im_search_voice);
        listContacts = new ArrayList<Contact>();
        temp = new ArrayList<Contact>();
        db = new MyDatabase(getActivity());

        // Lấy toàn bộ contact từ database để hiển thị
        contacts = db.getAllContact();
        listContacts.addAll(contacts);
        customAdapter = new CustomAdapter(getActivity(),R.layout.row_listview,listContacts);
        lvContact.setAdapter(customAdapter);

        // Tìm kiếm bằng giọng nói
        searchByVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                try {
                    startActivityForResult(intent, REQUEST_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Ấn vào từng contact trong listView
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
                startActivityForResult(intent,EDT_CODE);
            }
        });

        // Thêm contact mới
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
            listContacts.add(contact);
            customAdapter.notifyDataSetChanged();
        }
        if(requestCode == EDT_CODE && resultCode == RESULT_OK){ // EditActivity
            Bundle bundle = intent.getBundleExtra("package");
            Contact contact = (Contact) bundle.getSerializable("contact");
            listContacts.remove(index);
            db.deleteContact(editContact);
            String s = intent.getStringExtra("delete");
            if(s.equals("no")){
                listContacts.add(index,contact);
                db.addContact(contact);
            }
            customAdapter.notifyDataSetChanged();
        }
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            final ArrayList < String > result= intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String searchResult = result.get(0);
            customAdapter.filter(searchResult);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String text = s;
        customAdapter.filter(s);
        return false;
    }
}