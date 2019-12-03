package com.example.contact;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contact.ui.contacts.ContactsFragment;

import static android.widget.Toast.LENGTH_SHORT;

public class InforContactActivity extends AppCompatActivity {
    ImageView ivEdit;
    TextView tvName;
    TextView tvPhone;
    ImageView ivAvatar;
    MyDatabase db;
    Contact contact;
    String uri;
    static final int EDT_CODE = 456;
    CheckBox cbFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_contact);
        ivEdit = (ImageView) findViewById(R.id.ib_edit);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        cbFavourite = (CheckBox) findViewById(R.id.cb_favourite);
        db = new MyDatabase(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("package");
        contact = (Contact) bundle.getSerializable("contact");
        uri = contact.uriAvatar;
        tvName.setText(contact.name);
        tvPhone.setText(contact.phone);
        if(contact.uriAvatar.equals("")){
            ivAvatar.setImageResource(R.drawable.ic_person_black_24dp);
        }else ivAvatar.setImageURI(Uri.parse(contact.uriAvatar));


        if (contact.getIsClick() == 1) {
            cbFavourite.setChecked(true);
        } else {
            cbFavourite.setChecked(false);
        }
        cbFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    db.addFavourites(contact);
                    contact.setIsClick(1);
                }else{
                    db.deleteFavourites(contact);
                    contact.setIsClick(0);
                }
            }
        });

    }

    public void EditContact(View view) {
        Intent intent = new Intent(InforContactActivity.this,EditContactActivity.class);
        Contact contact = new Contact();
        contact.name = tvName.getText().toString();
        contact.phone = tvPhone.getText().toString();
        contact.uriAvatar = uri;
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact",contact);
        intent.putExtra("package",bundle);
        startActivityForResult(intent,EDT_CODE);
        //db.deleteContact(contact);
        //db.close();
    }


    public void Call(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",contact.getPhone(),null));
        startActivity(intent);
    }
    // Mở Hộp thoại nhắn tin
    public void SendSMS(View view) {
        // The number on which you want to send SMS
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact.phone, null)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle bundle = intent.getBundleExtra("package");
        Contact contact = (Contact) bundle.getSerializable("contact");
        tvName.setText(contact.name);
        tvPhone.setText(contact.phone);

        Intent intent2 = new Intent(InforContactActivity.this,ContactsFragment.class);
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("contact",contact);
        intent2.putExtra("package",bundle2);
        setResult(RESULT_OK,intent2);
        finish();
    }
}
