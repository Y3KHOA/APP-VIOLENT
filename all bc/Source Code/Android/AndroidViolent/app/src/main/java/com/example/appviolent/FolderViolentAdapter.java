package com.example.appviolent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appviolent.Fragment.ListVideoExampleFragment;
import com.example.appviolent.Fragment.VideoFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FolderViolentAdapter extends ArrayAdapter<FolderViolent> {
    ArrayList<FolderViolent> arr = new ArrayList<>();
    ListVideoExampleFragment fragment = new ListVideoExampleFragment();
    public FolderViolentAdapter(@NonNull Context context, ArrayList<FolderViolent> arr , ListVideoExampleFragment fragment) {
        super(context, R.layout.list_folder_video,arr);
        this.arr= arr;
        this.fragment = fragment;
    }
    public ArrayList<FolderViolent> getListItem (){
        return this.arr;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Detection");
        FolderViolent v = getItem(position);
        if(convertView == null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_folder_video,parent,false);

        }
        CardView cardView = convertView.findViewById(R.id.item_folder);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView txt_label = convertView.findViewById(R.id.txt_content);
        TextView txt_number = convertView.findViewById(R.id.txt_time_notification);

        imageView.setImageResource(v.idImg);
        txt_label.setText(v.name);
//        txt_number.setText(v.number);
        myRef.child(v.label).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int k = 0 ;
                for (DataSnapshot i : snapshot.getChildren()){
                    k++;

                }
                txt_number.setText(String.valueOf(k));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getContext(), MainActivity.class);
               intent.putExtra("key_label" , v.getLabel());
               getContext().startActivity(intent);
            }
        });


        return convertView;
    }
}
