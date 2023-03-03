package com.example.appviolent.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.appviolent.FolderViolent;
import com.example.appviolent.FolderViolentAdapter;
import com.example.appviolent.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListVideoFragment extends Fragment {

    ListView listView ;
    ImageButton btnFilter;
    ArrayList<FolderViolent> arrayList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListVideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListVideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListVideoFragment newInstance(String param1, String param2) {
        ListVideoFragment fragment = new ListVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_video, container, false);
        listView = view.findViewById(R.id.list_video);
        btnFilter= view.findViewById(R.id.btnFilter);

        FolderViolent folderViolent = new FolderViolent("bc" , "Bóp Cổ","0",R.drawable.camera);
        FolderViolent folderViolent1 = new FolderViolent("cq" , "Cởi Quần Áo","0",R.drawable.camera);
        FolderViolent folderViolent2 = new FolderViolent("da" , "Đá, Đạp","0",R.drawable.camera);
        FolderViolent folderViolent3 = new FolderViolent("dn" , "Đánh, Tát","0",R.drawable.camera);
        FolderViolent folderViolent4 = new FolderViolent("kc" , "Kẹp Cổ","0",R.drawable.camera);
        FolderViolent folderViolent5 = new FolderViolent("lg" , "Lên Gối","0",R.drawable.camera);
        FolderViolent folderViolent6 = new FolderViolent("lk" , "Lôi Kéo","0",R.drawable.camera);
        FolderViolent folderViolent7 = new FolderViolent("na" , "Nằm Xuống Sàn","0",R.drawable.camera);
        FolderViolent folderViolent8 = new FolderViolent("nc" , "Nắm Cổ","0",R.drawable.camera);
        FolderViolent folderViolent9 = new FolderViolent("ne" , "Ném Đồ Vật","0",R.drawable.camera);
        FolderViolent folderViolent10 = new FolderViolent("nt" , "Nắm Tóc","0",R.drawable.camera);
        FolderViolent folderViolent11 = new FolderViolent("om" , "Ôm, Vật Lôn","0",R.drawable.camera);
        FolderViolent folderViolent12 = new FolderViolent("tc" , "Thủ Thế Võ","0",R.drawable.camera);
        FolderViolent folderViolent13 = new FolderViolent("vk" , "Vật, Vũ Khí","0",R.drawable.camera);
        FolderViolent folderViolent14 = new FolderViolent("xd" , "Xô Đẩy","0",R.drawable.camera);
        FolderViolent folderViolent15 = new FolderViolent("xt" , "Xỉ Tay","0",R.drawable.camera);
        FolderViolent folderViolent16 = new FolderViolent("mau" , "Máu","0",R.drawable.camera);
        FolderViolent folderViolent17 = new FolderViolent("no" , "Không","0",R.drawable.camera);

        arrayList.add(folderViolent);
        arrayList.add(folderViolent1);
        arrayList.add(folderViolent2);
        arrayList.add(folderViolent3);
        arrayList.add(folderViolent4);
        arrayList.add(folderViolent5);
        arrayList.add(folderViolent6);
        arrayList.add(folderViolent7);
        arrayList.add(folderViolent8);
        arrayList.add(folderViolent9);
        arrayList.add(folderViolent10);
        arrayList.add(folderViolent11);
        arrayList.add(folderViolent12);
        arrayList.add(folderViolent13);
        arrayList.add(folderViolent14);
        arrayList.add(folderViolent15);
        arrayList.add(folderViolent16);
        arrayList.add(folderViolent17);
        FolderViolentAdapter arrayAdapter = new FolderViolentAdapter(getContext(), arrayList,new ListVideoExampleFragment());
        listView.setAdapter(arrayAdapter);
        for (int i = 0 ;i<  arrayAdapter.getListItem().size(); i++ ){
            FolderViolent a = arrayAdapter.getListItem().get(i);
        }
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0 ;i<  arrayList.size(); i++ ){

                    System.out.println(arrayList.get(i).getNumber());
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}