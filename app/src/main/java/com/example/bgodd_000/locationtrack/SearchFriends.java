package com.example.bgodd_000.locationtrack;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class SearchFriends extends Fragment {


    private static final String TAG = "SearchFriends";

    View myFragment;


    //This arraylist will have data as pulled from database
    ArrayList<String> databaseResults = new ArrayList<>();


    private Socket client;
    private PrintWriter printwriter;
    BufferedReader din = null;

    /*
    OnNameSetListener onNameSetListener;

    public interface OnNameSetListener{
        public void setName(String name);
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            onNameSetListener = (OnNameSetListener) activity;
        } catch(ClassCastException e){

        }
    }*/

    public static final String PREFS2 = "usernamePrefs";
    String username;
    String friendname;
    //String username2 = "Christian";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.fragment_search,container,false);

        ImageView refresh = (ImageView) myFragment.findViewById(R.id.refreshButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshActivityClick(v);
            }
        });

        //retrieve username
        SharedPreferences usernamePrefs = getActivity().getSharedPreferences(PREFS2, 0);
        username = usernamePrefs.getString("usernameKey", "");




        return myFragment;
    }



    private void refreshActivityClick(View v) {
        //Log.d(TAG, "Settings Activity Click");
        //setContentView(R.layout.settings_menu);
        if(databaseResults != null){
            databaseResults.clear();
        }
        RetrieveUsernames retrieveUsernamesTask = new RetrieveUsernames();
        retrieveUsernamesTask.execute();


    }

    //Retrieve UserNames from Server LILIANA BALBOA
    public class RetrieveUsernames extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("Loading Users...");
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.show();
        }



        @Override
        protected Void doInBackground(Void... params) {
            try {

                //client = new Socket("10.202.110.243", 4444); // connect to the server
                client = new Socket("192.168.0.103", 4444);
                //client = new Socket("192.168.1.131", 4444);
                din = new BufferedReader(new InputStreamReader(client.getInputStream()));
                printwriter = new PrintWriter(client.getOutputStream(), true);
                printwriter.println("");
                printwriter.println("");
                printwriter.println("retrieveUsernames"); // write the message to output stream
                printwriter.println("quit");
                printwriter.flush();

                //while (din.readLine()!= null){
                String line;
                while((line = din.readLine())!= null){
                    //String userNames = din.readLine();
                    //databaseResults.add(userNames);
                    //databaseResults.add(din.readLine());
                    databaseResults.add(line);
                    //databaseResults.append(line);
                    Log.d(TAG, line);

                }


                printwriter.close();
                din.close();
                //String userNames = din.readLine();

                client.close(); // closing the connection
                //}

            } catch (UnknownHostException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            super.onPostExecute(result);
            databaseResults.remove(username);
            MyCustomAdapter adapter = new MyCustomAdapter(databaseResults, getActivity());
            //databaseResults.remove(username);
            //int arrayLength = databaseResults.size();

            //for (int i=0;i<arrayLength;i++) {
              //  if (databaseResults.equals(databaseResults[i],username)) {
                    //index = i;
                    //break;
                //}
            //}
            //if(Arrays.asList(databaseResults).contains(username)) {
            //}


            //handle listview and assign adapter
            ListView lView = (ListView) myFragment.findViewById(R.id.usersListView);
            lView.setAdapter(adapter);
            pd.dismiss();

        }

    }


    public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<String> databaseResults = new ArrayList<>();
        private Context context;



        public MyCustomAdapter(ArrayList<String> databaseResults, Context context) {
            this.databaseResults = databaseResults;
            this.context = context;
        }

        @Override
        public int getCount() {
            return databaseResults.size();
        }

        @Override
        public Object getItem(int pos) {
            return databaseResults.get(pos);
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
                view = inflater.inflate(R.layout.list_row_user, parent, false);
            }

            //Handle TextView and display string from your list
            final TextView listItemText = (TextView)view.findViewById(R.id.userName);
            listItemText.setText(databaseResults.get(position));

            //Handle buttons and add onClickListeners

            Button addBtn = (Button)view.findViewById(R.id.addUserButton);
            addBtn.setTag(position);

            addBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick (View view) {
                    //do something

                    int position = (int) view.getTag();
                    String selectedFromList = databaseResults.get(position);
                    Log.d(TAG, selectedFromList);
                    EventBus bus = EventBus.getDefault();
                    bus.post(new TextChangedEvent(selectedFromList));
                    Toast.makeText(getContext(), "User "+ selectedFromList + " has been added to your Friends list", Toast.LENGTH_SHORT).show();
                    friendname = selectedFromList;
                    sendFriendUsername sendFriendTask = new sendFriendUsername();
                    sendFriendTask.execute();
                    //onNameSetListener.setName(selectedFromList);

                }
            });



            return view;
        }
    }



    public class sendFriendUsername extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {

                //client = new Socket("10.202.110.243", 4444); // connect to the server
                client = new Socket("192.168.0.103", 4444);
                //client = new Socket("192.168.1.131", 4444);
                //din = new BufferedReader(new InputStreamReader(client.getInputStream()));
                printwriter = new PrintWriter(client.getOutputStream(), true);
                printwriter.println(username);
                printwriter.println(friendname);
                printwriter.println("storeFriendUsername"); // write the message to output stream
                printwriter.println("quit");
                printwriter.flush();

                printwriter.close();
                //din.close();
                //String userNames = din.readLine();

                client.close(); // closing the connection
                //}

            } catch (UnknownHostException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }

    }




}
