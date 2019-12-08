package com.example.contact;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contact.ui.contacts.ContactsFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactActivity extends AppCompatActivity {
    Uri uri ;
    EditText edtName;
    EditText edtPhone;
    Button btnChooseImage;
    CircleImageView profileImage;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        edtName = (EditText) findViewById(R.id.edt_name);
        edtPhone = (EditText) findViewById(R.id.edt_num_phone);
        btnChooseImage = (Button) findViewById(R.id.btn_choose_avatar);
        profileImage = (CircleImageView) findViewById(R.id.avatar);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        //getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        TextView name = view.findViewById(R.id.tv_header);
        ImageView close = view.findViewById(R.id.iv_close);
        ImageView done = view.findViewById(R.id.iv_done);
        name.setText("Add new contact");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact();
                contact.setIsClick(0);
                contact.name = edtName.getText().toString();
                contact.phone = edtPhone.getText().toString();
                if(uri == null){
                    contact.uriAvatar = "";
                }
                else contact.uriAvatar = uri + "";
                if((contact.name.isEmpty() != true)&& (contact.phone.isEmpty() != true)){
                    Intent intent = new Intent(AddContactActivity.this,ContactsFragment.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contact", contact);
                    intent.putExtra("package",bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    checkInfor(contact);
                }
            }
        });
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }else{
                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();
                }
            }
        });

    }
    public void checkInfor(Contact contact) {
        if(contact.name.isEmpty()){
            Toast.makeText(this,"Vui lòng nhập tên!!!",Toast.LENGTH_SHORT).show();
        }else if(contact.phone.isEmpty()){
            Toast.makeText(this,"Vui lòng nhập số điện thoại!!!",Toast.LENGTH_SHORT).show();
        }
    }
    private void pickImageFromGallery() {
        String action;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            action = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            action = Intent.ACTION_PICK;
        }
        Intent intent = new Intent(action, uri);
        //Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select File"), IMAGE_PICK_CODE);
        }
        //startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_DENIED){
                    pickImageFromGallery();
                }else{
                    Toast.makeText(this,"Permission denied!!!!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            profileImage.setImageURI(data.getData());
            uri = data.getData();
        }
    }
}
