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
        String label = "Có hiện tượng ";
        switch (content) {
            case "bc":
                label = label + "bóp cổ";
                break;
            case "cq":
                label = label + "cởi quần áo";
                break;
            case "da":
                label = label + "đá, đạp";
                break;
            case "dn":
                label = label + "đánh, tát";
                break;
            case "kc":
                label = label + "kẹp cổ";
                break;
            case "lg":
                label = label + "lên gối";
                break;
            case "lk":
                label = label + " lôi kéo";
                break;
            case "ma":
                label = label + "xô xát xảy máu";
                break;
            case "na":
                label = label + "nằm xuống sàn";
                break;
            case "nc":
                label = label + "nắm cổ";
                break;
            case "ne":
                label = label + "ném đồ vật";
                break;
            case "nt":
                label = label + "nắm tóc";
                break;
            case "om":
                label = label + "ôm, vật lộn";
                break;
            case "tc":
                label = label + "thủ thế võ";
                break;
            case "vk":
                label = label + "vật, vũ khí";
                break;
            case "xd":
                label = label + "xô đẩy";
                break;
            case "xt":
                label = label + "xỉ tay";
                break;



        }
        return label + " đã xảy ra";
    }
}
