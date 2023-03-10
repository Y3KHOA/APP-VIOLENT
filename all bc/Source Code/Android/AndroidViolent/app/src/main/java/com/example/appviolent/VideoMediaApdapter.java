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
        System.out.println("Nh??n" + label);
        textnameVideo.setText("Video th??? " + ((10)*index+ (position+1)));

        texttimeVideo.setText(v.getTime());
        textActionVideo.setText("H??nh ?????ng b???o l???c " +changeLabel(label));

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
                label = label + "l??i k??o";
                break;
            case "ma":
                label = label + "m??u";
                break;
            case "na":
                label = label + "n??m xu???ng s??n";
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

            case "no":
                label = label + "Kh??ng b???o l???c";
                break;

        }
        return label;
    }
}
