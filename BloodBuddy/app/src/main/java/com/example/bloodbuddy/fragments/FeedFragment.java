package com.example.bloodbuddy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bloodbuddy.Adapers.FeedAdapter;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.modelClasses.Feed;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // create  vars
    RecyclerView recyclerView;
    ArrayList<Feed> feedList;
    private FirebaseFirestore db;
    private DocumentReference ref;


    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        // binding
        feedList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.feedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FeedAdapter feedAdapter = new FeedAdapter(getContext(), feedList);
        recyclerView.setAdapter(feedAdapter);


        db = FirebaseFirestore.getInstance();

        //HERE: create/fetch the data here
//        feedList.add(new Feed("This post describes a solution to a problem we ran into with communication between Fragments and Activities, which involved logic in our Activities that we wanted to extract. It happened specifically in the case of accessing an Activity’s ViewModel from a Fragment which was used in multiple Activities, each with their own ViewModel.",
//                R.drawable.chat3, ""));

        db.collection("Feed").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                // after getting the data we are calling on success method
                // and inside this method we are checking if the received
                // query snapshot is empty or not.
                if (!queryDocumentSnapshots.isEmpty()) {
                    // if the snapshot is not empty we are
                    // hiding our progress bar and adding
                    // our data in a list.
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        // after getting this list we are passing
                        // that list to our object class.
//                        Feed f = d.toObject(Feed.class);
                        String text = d.getString("text");
                        String link = d.getString("link");
                        String image = d.getString("image");
                        Feed f = new Feed(text, image, link);

                        // and we will pass this object class
                        // inside our arraylist which we have
                        // created for recycler view.
                        feedList.add(f);
                    }
                    // after adding the data to recycler view.
                    // we are calling recycler view notifuDataSetChanged
                    // method to notify that data has been changed in recycler view.
                    feedAdapter.notifyDataSetChanged();
                } else {
                    // if the snapshot is empty we are displaying a toast message.
                 //   Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                }

            }
        });





        return view;
    }
}