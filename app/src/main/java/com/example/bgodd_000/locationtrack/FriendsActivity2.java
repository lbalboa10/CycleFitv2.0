package com.example.bgodd_000.locationtrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class FriendsActivity2 extends Activity {

        EventBus bus = EventBus.getDefault();
        ArrayList<String> friends2 = new ArrayList<>();

        //ListView lView2;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_friends2);
            addListenerOnButton();
            //bus.register(FriendsActivity2.this);
            bus.register(this);
            //lView2 = (ListView) findViewById(R.id.friendsListView);
        }



        public void addListenerOnButton() {

            final Context context = this;

            ImageView searchButton = (ImageView) findViewById(R.id.searchButton);



            searchButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Intent intent = new Intent(context, SearchFriends2.class);
                    startActivity(intent);

                }

            });

        }

    @Override
    public void onStart() {
        super.onStart();
        //bus.register(this);
    }

    @Override
    protected void onResume() {

        super.onResume();

    }


    @Override
    protected void onPause() {

        super.onPause();

    }

    public void onEvent(TextChangedEvent event) {
        friends2.add(event.newText);
        ListView lView2 = (ListView) findViewById(R.id.friendsListView);
        MyCustomAdapter adapter2 = new MyCustomAdapter(friends2, FriendsActivity2.this);
        lView2.setAdapter(adapter2);
        lView2.setVisibility(View.VISIBLE);
        //for(int i=0 ; i<adapter2.getCount() ; i++){
        //    dataFromUsers.add(adapter2.getItem(i));

        //}
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

            return view;
        }

    }

    @Override
    public void onStop() {
        //bus.unregister(this);
        super.onStop();
    }
}
