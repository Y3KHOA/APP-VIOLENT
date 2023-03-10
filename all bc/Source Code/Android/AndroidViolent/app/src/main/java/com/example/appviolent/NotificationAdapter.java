package com.example.appviolent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<NotificationClass> {
    public NotificationAdapter(@NonNull Context context, ArrayList<NotificationClass> arrayList) {
        super(context,R.layout.item_notification,arrayList);
    }
    boolean flag = true;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NotificationClass  v = getItem(position);
        if(convertView== null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notification,parent,false);

        }
        ImageView btnMore = convertView.findViewById(R.id.btnMore);
        TextView txt_label = convertView.findViewById(R.id.txt_content);
        TextView txt_tilte = convertView.findViewById(R.id.txt_title);
        TextView txt_time = convertView.findViewById(R.id.txt_time_notification);
        RelativeLayout layout = convertView.findViewById(R.id.layout_notification);
        String[] split = v.content.split("_");
        txt_label.setText(changeLabel(split[0]));

        txt_time.setText(v.time_notification);
        CardView itemview = convertView.findViewById(R.id.item_notification);
        int k = itemview.getLayoutParams().height;
        System.out.println(v.content+ " dsadsadsadsa");

        itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnMore.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                itemview.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                layout.setPadding(0,20,0,20);
                Intent notifyIntent = new Intent(getContext(), ShowOneVideo.class);
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                notifyIntent.putExtra("key_video" ,v );

                getContext().startActivity(notifyIntent);
            }
        });

        return convertView;
    }

    private String changeLabel(String content) {
        String label = "C?? hi???n t?????ng ";
        switch (content) {
            case "bc":
                label = label + "b??p c???";
                break;
            case "cq":
                label = label + "c???i qu???n ??o";
                break;
            case "da":
                label = label + "????, ?????p";
                break;
            case "dn":
                label = label + "????nh, t??t";
                break;
            case "kc":
                label = label + "k???p c???";
                break;
            case "lg":
                label = label + "l??n g???i";
                break;
            case "lk":
                label = label + " l??i k??o";
                break;
            case "ma":
                label = label + "x?? x??t x???y m??u";
                break;
            case "na":
                label = label + "n???m xu???ng s??n";
                break;
            case "nc":
                label = label + "n???m c???";
                break;
            case "ne":
                label = label + "n??m ????? v???t";
                break;
            case "nt":
                label = label + "n???m t??c";
                break;
            case "om":
                label = label + "??m, v???t l???n";
                break;
            case "tc":
                label = label + "th??? th??? v??";
                break;
            case "vk":
                label = label + "v???t, v?? kh??";
                break;
            case "xd":
                label = label + "x?? ?????y";
                break;
            case "xt":
                label = label + "x??? tay";
                break;



        }
        return label + " ???? x???y ra";
    }
}
