package com.example.atividadeaps;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListItensFragment extends Fragment {


    ListView listView;
    AdapterItem adapterItem;
    List<ItemPerdido> itemPerdidos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_itens, container, false);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Objeto");

        listView = view.findViewById(R.id.itens_list);
        itemPerdidos = new ArrayList<ItemPerdido>();

        itemPerdidos = getItemPerdidos(databaseReference, new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<ItemPerdido> itemPerdidos) {
                adapterItem = new AdapterItem(getContext(), itemPerdidos);
                listView.setAdapter(adapterItem);
            }
        });





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    List<ItemPerdido> getItemPerdidos(DatabaseReference databaseReference, OnDataLoadedListener listener){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemPerdido itemPerdido = snapshot.getValue(ItemPerdido.class);
                    itemPerdidos.add(itemPerdido);
                }

                listener.onDataLoaded(itemPerdidos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return itemPerdidos;
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(List<ItemPerdido> itemPerdidos);
    }

}