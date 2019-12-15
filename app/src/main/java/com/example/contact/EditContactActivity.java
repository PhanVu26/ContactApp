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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditContactActivity extends AppCompatActivity {
    CheckBox cbFavourite;
    EditText edtName;
    EditText edtPhone;
    ImageView ivAvatar;
    ImageView btnChange;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    Uri uri;
    Contact contact;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // SetCustomActionBar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        //getSupportActionBar().setElevation(0);

        View view = getSupportActionBar().getCustomView();
        TextView name = view.findViewById(R.id.tv_header);
        ImageView close = view.findViewById(R.id.iv_close);
        ImageView done = view.findViewById(R.id.iv_done);
        name.setText("Edit contact");

        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        btnChange = (ImageView) findViewById(R.id.btn_change);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtPhone = (EditText) findViewById(R.id.edt_num_phone);
        cbFavourite = (CheckBox) findViewById(R.id.cb_favourite);

        // Nhận intent từ InforActivity
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("package");
        contact = (Contact) bundle.getSerializable("contact");

        edtName.setText(contact.getName());
        edtPhone.setText(contact.getPhone());
        uri = Uri.parse(contact.getUriAvatar());
        if(contact.uriAvatar.equals("")){
            ivAvatar.setImageResource(R.drawable.ic_person_black_24dp);
        }else ivAvatar.setImageURI(Uri.parse(contact.uriAvatar));

        if (contact.getIsClick() == 1) {
            cbFavourite.setChecked(true);
        } else {
            cbFavourite.setChecked(false);
        }

        // Kiểm tra người dùng có ấn chọn/bỏ chọn
        cbFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    contact.setIsClick(1);
                }else{
                    contact.setIsClick(0);
                }
            }
        });

        // Close Activity
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Finished Edit
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact c = new Contact();
                c.name = edtName.getText().toString();
                c.phone = edtPhone.getText().toString();
                c.setIsClick(contact.getIsClick());
                if(uri == null){
                    c.uriAvatar = "";
                }
                else c.uriAvatar = uri + "";
                c.setId(contact.getId());
                if((c.name.isEmpty() != true)&& (c.phone.isEmpty() != true)){
                    Intent intent = new Intent(EditContactActivity.this,InforContactActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contact", c);
                    intent.putExtra("package",bundle);
                    intent.putExtra("delete","no");// Thông báo không xóa contact này
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    checkInfor(c);
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

    public void ChangeAvatar(View view) {
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

    private void pickImageFromGallery() {
        String action;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            action = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            action = Intent.ACTION_PICK;
        }
        Intent intent = new Intent(action, uri);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select File"), IMAGE_PICK_CODE);
        }
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
            ivAvatar.setImageURI(data.getData());
            uri = data.getData();
        }
    }
}
