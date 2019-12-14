package com.example.contact.ui.favourites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.contact.Contact;
import com.example.contact.CustomAdapter;
import com.example.contact.MyDatabase;
import com.example.contact.R;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment {

    MyDatabase db;
    ListView lvFavourites;
    CustomAdapter adapter;
    ArrayList<Contact> favouriteList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourites, container, false);
        db = new MyDatabase(getActivity());
        lvFavourites = root.findViewById(R.id.lv_favourites);
        favouriteList = db.getAllFavourites();
        adapter = new CustomAdapter(getActivity(),R.layout.row_listview,favouriteList);
        lvFavourites.setAdapter(adapter);

        return root;
    }
}