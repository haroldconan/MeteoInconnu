package com.tp.meteoinconnu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tp.meteoinconnu.objects.Users;

import java.util.List;

public class AdaptateurListe extends ArrayAdapter<Users> {
    public AdaptateurListe(Context context, List<Users> users) {
        super(context,0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vue_item_user, parent, false);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.txtnom = convertView.findViewById(R.id.txtnomitem);
            viewHolder.txtprenom = convertView.findViewById(R.id.txtprenomitem);
            viewHolder.img = convertView.findViewById(R.id.imguseritem);
            convertView.setTag(viewHolder);
        }
        viewHolder.txtprenom.setText(getItem(position).getPrenom());
        viewHolder.txtnom.setText(getItem(position).getNom());
        String imageURL = getItem(position).getImgURL();
        if(!imageURL.equals(""))
        Picasso.get().load(imageURL).into(viewHolder.img );

        return convertView;
    }

    private class ViewHolder {
        TextView txtprenom, txtnom;
        ImageView img;
    }
}
