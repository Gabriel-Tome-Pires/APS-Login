package com.example.atividadeaps;

import android.content.Context;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterItem extends BaseAdapter {

    Context ctx;
    List<ItemPerdido> itemPerdidos;
    LayoutInflater inflater;

    public AdapterItem(Context ctx, List<ItemPerdido> itemPerdidos){
        this.ctx = ctx;
        this.itemPerdidos = itemPerdidos;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return itemPerdidos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_nome_item);
        //ImageView imageView = (ImageView) convertView.findViewById(R.id.imageItem);

        textView.setText(itemPerdidos.get(position).name);

        return convertView;
    }
}
