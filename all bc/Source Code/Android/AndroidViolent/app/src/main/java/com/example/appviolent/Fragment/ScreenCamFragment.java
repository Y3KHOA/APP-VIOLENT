package com.example.appviolent.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.appviolent.CameraDetailsActivity;
import com.example.appviolent.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScreenCamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScreenCamFragment extends Fragment {
    CameraActivity mCamAc ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View mView;

    CameraBridgeViewBase mOpencvCamView;
//    BaseLoaderCallback mLoaderCallBack= new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            super.onManagerConnected(status);
//        }
//    }

    public ScreenCamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScreenCamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScreenCamFragment newInstance(String param1, String param2) {
        ScreenCamFragment fragment = new ScreenCamFragment();
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
        mCamAc = new CameraActivity();
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#0DA7B1");
        window.setStatusBarColor(colorCodeDark);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView= inflater.inflate(R.layout.fragment_screen_cam, container, false);
        Switch switchcam1 = mView.findViewById(R.id.switch_cam1);
        switchcam1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                 System.out.println("Status " + b);
                if(b) {
                    Intent intent = new Intent(getContext(), CameraDetailsActivity.class);
                    startActivity(intent);
                }
            }
        });
        return mView;
    }

}