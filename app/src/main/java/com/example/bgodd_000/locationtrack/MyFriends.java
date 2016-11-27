package com.example.bgodd_000.locationtrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class MyFriends extends Fragment {


    View myFragment2;
    EventBus bus = EventBus.getDefault();
    ArrayList<String> friends2 = new ArrayList<>();
    MyCustomAdapter adapter2 = null;
    ListView lView2;

    public static final String PREFS3 = "arrayPrefs";


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onViewCreated(View myFragment2, Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bus.register(this);


        myFragment2 = inflater.inflate(R.layout.fragment_myfriends, container, false);

        SharedPreferences arrayPrefs = getActivity().getSharedPreferences(PREFS3, 0);
        //username = usernamePrefs.getString("usernameKey", "");
        String test = arrayPrefs.getString("array_" + 0, null);
        //Log.d(TAG, test);
        if( test != null) {
            int size = arrayPrefs.getInt("array_size", 0);
            friends2 = new ArrayList<>(size);
            //friends2 = new String[size];
            for (int i = 0; i < size; i++) {
                friends2.add(arrayPrefs.getString("array_" + i, null));
            }

            ListView lView2 = (ListView) myFragment2.findViewById(R.id.friendsListView);
            adapter2 = new MyCustomAdapter(friends2, getActivity());
            lView2.setAdapter(adapter2);
            lView2.setVisibility(View.VISIBLE);
            lView2.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3)
                {
                    String value = (String)adapter.getItemAtPosition(position);
                    // assuming string and if you want to get the value on click of list item
                    // do what you intend to do on click of listview row
                    Intent i = new Intent(getActivity(), FriendRouteActivity.class);
                    i.putExtra("Name", value);
                    startActivity(i);
                }
            });
        }



        return myFragment2;



    }

    @Override
    public void onPause() {
        super.onPause();

    }

    //MyCustomAdapter adapter2 = null;

    public void onEvent(TextChangedEvent event) {

        if(friends2.size() == 0) {
            if (!(friends2.contains(event.newText))) {
                friends2.add(event.newText);
            }

            adapter2 = new MyCustomAdapter(friends2, getActivity());
            lView2 = (ListView) myFragment2.findViewById(R.id.friendsListView);
            lView2.setAdapter(adapter2);
            lView2.setVisibility(View.VISIBLE);

            lView2.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3)
                {
                    String value = (String)adapter.getItemAtPosition(position);
                    // assuming string and if you want to get the value on click of list item
                    // do what you intend to do on click of listview row
                    Intent i = new Intent(getActivity(), FriendRouteActivity.class);
                    i.putExtra("Name", value);
                    startActivity(i);
                }
            });


        }else{
            if (!(friends2.contains(event.newText))) {
                friends2.add(event.newText);
            }

            adapter2.notifyDataSetChanged();


        }
        /*
        lView2.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                String value = (String)adapter.getItemAtPosition(position);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
                Intent i = new Intent(getActivity(), FriendRouteActivity.class);
                i.putExtra("Name", value);
                startActivity(i);
            }
        });*/

        SharedPreferences arrayPrefs = getActivity().getSharedPreferences(PREFS3,0);
        SharedPreferences.Editor friendsFile = arrayPrefs.edit();
        //friendsFile.putString("usernameKey", user.name);
        //friendsFile.commit();
        //store array in shared prefs
        //Editor edit = prefs.edit();
        friendsFile.putInt("array_size", friends2.size());
        for(int i=0;i<friends2.size(); i++) {
            friendsFile.putString("array_" + i, friends2.get(i));
            friendsFile.commit();
        }

    }





    public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<String> friends2 = new ArrayList<>();
        private Context context;


        public MyCustomAdapter(ArrayList<String> friends2, Context context) {
            this.friends2 = friends2;
            this.context = context;
        }

        @Override
        public int getCount() {
            return friends2.size();
        }

        @Override
        public String getItem(int pos) {
            return friends2.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return 0;
            //just return 0 if your list items do not have an Id variable.
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_row_friend, parent, false);
            }

            //Handle TextView and display string from your list
            TextView listItemText = (TextView) view.findViewById(R.id.userName2);
            listItemText.setText(friends2.get(position));

             /*
        //Handle buttons and add onClickListeners
        //Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        Button addBtn = (Button)view.findViewById(R.id.addUserButton);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                friends.add(databaseResults.get(position));
                //lView2.notifyDataSetChange();

                notifyDataSetChanged();
            }
        });*/


        /*lView2.setOnItemClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onItemClick() {


                notifyDataSetChanged();
            }
        });*/

            return view;
        }

    }

    @Override
    public void onStop() {
        super.onStop();

    }


}

