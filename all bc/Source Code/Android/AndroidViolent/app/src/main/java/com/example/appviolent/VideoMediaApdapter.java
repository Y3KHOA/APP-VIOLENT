package com.example.appviolent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideoMediaApdapter extends ArrayAdapter<ItemVideoMedia> {
    Context context;
    List<ItemVideoMedia> list ;
    int index = 0 ;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public VideoMediaApdapter(@NonNull Context context, List<ItemVideoMedia> list) {
        super(context,R.layout.list_video_media_item,list);
        this.context = context;
        this.list = list;

    }   public VideoMediaApdapter(@NonNull Context context, List<ItemVideoMedia> list, int index) {
        super(context,R.layout.list_video_media_item,list);
        this.context = context;
        this.list = list;
        this.index = index;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemVideoMedia v = getItem(position);
        if(convertView == null ){
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_video_media_item,parent,false);

        }
        ImageView imageView = convertView.findViewById(R.id.imageava);
        TextView textnameVideo = convertView.findViewById(R.id.txt_content);
        TextView texttimeVideo = convertView.findViewById(R.id.txt_time_notification);
        TextView textActionVideo = convertView.findViewById(R.id.txt_action);
        CardView cardView = convertView.findViewById(R.id.item_media_video);

        Picasso.with(context).load(v.getUrlImage()).into(imageView);

        String[] name = v.getName().split("_");
        String label = name[0];
        System.out.println("Nhãn" + label);
        textnameVideo.setText("Video thứ " + ((10)*index+ (position+1)));

        texttimeVideo.setText(v.getTime());
        textActionVideo.setText("Hành động bạo lực " +changeLabel(label));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println(getContext()+ "dasdsa");
//                System.out.println(MainActivity.class+ "asd");
//                System.out.println(() + "asd");
                if(getContext() instanceof MainActivityHomePage2){
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("key_video" , v);
                    intent.putExtra("key_indexHome" ,"ListVideo");
                getContext().startActivity(intent);
                }
                else{
                    Intent intent = new Intent(context, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("key_video", v);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            }
        });

        return convertView;
    }
    private String changeLabel(String content) {
        String label = "";
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
                label = label + "lôi kéo";
                break;
            case "ma":
                label = label + "máu";
                break;
            case "na":
                label = label + "năm xuống sàn";
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

            case "no":
                label = label + "Không bạo lực";
                break;

        }
        return label;
    }
}
