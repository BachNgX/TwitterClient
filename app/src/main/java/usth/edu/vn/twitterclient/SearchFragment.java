package usth.edu.vn.twitterclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<ListItem> listItems;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_search, container, false);
        fab=(FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToPostActivity();
            }
        });

        recyclerView = view.findViewById(R.id.all_list_search);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new TrendAdapter(tDataset);
        listItems = new ArrayList<>();
        for(int i=0; i<10; i++) {
            ListItem listItem = new ListItem(
                    "Trend"+ i+1,
                        "asd"
            );
            listItems.add(listItem);
        }
        adapter= new TrendAdapter(listItems, getActivity());
        recyclerView.setAdapter(adapter);
        return  view;
    }
    private void sendUserToPostActivity() {
        Intent intent= new Intent(getActivity(),PostActivity.class);
        startActivity(intent);
    }

}
