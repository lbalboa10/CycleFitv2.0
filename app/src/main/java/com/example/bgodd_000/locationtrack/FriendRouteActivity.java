package com.example.bgodd_000.locationtrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class FriendRouteActivity extends AppCompatActivity {

    String friendName;
    private Socket client;
    private PrintWriter printwriter;
    BufferedReader din = null;
    private static final String TAG = "FriendRouteActivity";
    //double distance2;
    double distance;
    double seconds;
    double avgSpeed;
    double avgIncline;
    double avgRPM;
    double avgHR;
    double cal;
    String ts;
    String message;
    String des;

    public static final String PREFS2 = "usernamePrefs";
    String username;

    int hours;
    double hr_rem;
    int min;
    double sec;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_route);

        Intent i = getIntent();
        friendName = i.getStringExtra("Name");

        TextView friend = (TextView) findViewById(R.id.friendName);

        friend.setText(friendName);

        RetrieveRoute retrieveRouteTask = new RetrieveRoute();
        retrieveRouteTask.execute();

        //retrieve username
        SharedPreferences usernamePrefs = getSharedPreferences(PREFS2, 0);
        username = usernamePrefs.getString("usernameKey", "");

    }

    //Retrieve UserNames from Server LILIANA BALBOA
    public class RetrieveRoute extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(FriendRouteActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Loading Route Summary...");
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
                printwriter.println(friendName);
                printwriter.println(username);
                printwriter.println("retrieveRoute"); // write the message to output stream
                printwriter.println("quit");
                printwriter.flush();

                //while (din.readLine()!= null){
                //String line;
                //while ((line = din.readLine()) != null) {
                distance = Double.valueOf(din.readLine());
                seconds = Double.valueOf(din.readLine());
                avgSpeed = Double.valueOf(din.readLine());
                avgIncline = Double.valueOf(din.readLine());
                avgRPM = Double.valueOf(din.readLine());
                avgHR = Double.valueOf(din.readLine());
                cal = Double.valueOf(din.readLine());
                ts = din.readLine();
                message = din.readLine();



                hours = (int) (seconds / 3600);
                hr_rem = seconds % 3600;
                min = (int) hr_rem / 60;
                sec = hr_rem % 60;


                    //databaseResults.add(line);
                //distance = din.readLine();
                //time = din.readLine();
                //avgSpeed = din.readLine();
                //avgIncline = din.readLine();
                //avgRPM = din.readLine();
                //avgHR = din.readLine();
                //cal = din.readLine();



                    //Log.d(TAG, distance);

                //}


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
        //sumText.setText(String.format("Route Summary:\nTotal Distance Traveled: %.2f m\nTime: %d hr %d min %.2f sec\n Average Speed: %.2f m/s\nAverage Incline: %.2f deg\nAverage Pedal RPM: %.2f rpm\nAverage Heart Rate: %.2f bpm\nCalories Burned: %.1f cal", rt.totalDistance, hours, min, sec, rt.avgSpeed, rt.avgIncline, rt.avgRPM, rt.avgHR, rt.calorieBurn));
        //String routeSummary = "Route Summary\n" + "Total Distance Traveled: " + distance + "\nTime: " + time + "\nAverage Speed: " +;
        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            TextView route = (TextView) findViewById(R.id.friendRoute);
            if (message == null) {
                pd.dismiss();
                TextView description = (TextView) findViewById(R.id.des);
                des = "Time: " + ts;
                description.setText(des);
                route.setText(String.format("Route Summary:\nTotal Distance Traveled: %.2f m\nTime: %d hr %d min %.2f sec\n Average Speed: %.2f m/s\nAverage Incline: %.2f deg\nAverage Pedal RPM: %.2f rpm\nAverage Heart Rate: %.2f bpm\nCalories Burned: %.1f cal", distance, hours, min, sec, avgSpeed, avgIncline, avgRPM, avgHR, cal));
            }else{
                pd.dismiss();
                route.setText(message);
            }
            //route.setText(String.format("Total Distance Traveled: %.2f m\n", distance2));
            //route.setText(distance); //Update TextView with Route

        }

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


    @Override
    public void onStop() {
        //bus.unregister(this);
        super.onStop();
    }
}
