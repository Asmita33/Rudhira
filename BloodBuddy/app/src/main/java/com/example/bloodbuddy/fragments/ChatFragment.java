package com.example.bloodbuddy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bloodbuddy.R;
import com.example.bloodbuddy.Users;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // create  vars
    RecyclerView recyclerView;
    ArrayList<Users> usersList;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //HERE: Get and Inflate the created layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // HERE: instantiate the variables
        usersList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.chatRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //HERE: create/fetch the data here
        usersList.add(new Users(R.drawable.chat1, "Naam rakhna hai"));
        usersList.add(new Users(R.drawable.chat2, "Naam me kya rakha hai"));
        usersList.add(new Users(R.drawable.chat3, "Naam socha ni ja raha"));



        //HERE: set RV adapter
        recyclerView.setAdapter(new chatAdapter(this.getContext(), usersList));

        return view;
    }
}