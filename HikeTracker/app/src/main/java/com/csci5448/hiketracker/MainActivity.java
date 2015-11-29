package com.csci5448.hiketracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    UserDataSource userDataSource;
    List<User> users;

    public static final String TAG = "MainActivity";
    public static final String HIKEDATA_PARCEL = "HikeData";
    public static final String USER_PARCEL = "User";
    public static final String SOURCE_STRING = "Source String Key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userDataSource = new UserDataSource(this);
        getUserData();
    }

    public void startLocator(View view){
        Intent myIntent = new Intent(MainActivity.this, LocatorActivity.class);
        myIntent.putExtra(USER_PARCEL, users.get(0));
        startActivity(myIntent);
    }

    public void viewHistory(View view) {
        Intent myIntent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(myIntent);
    }

    private void startNewUser() {
        Intent myIntent = new Intent(MainActivity.this, NewUserActivity.class);
        startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUserData() { new GetUserData().execute(); }

    //    private void
    public class GetUserData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "On doInBackground...");
            users = userDataSource.getUsers();      // Load previously stored user data

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if(users.size() > 0) {
                User user = users.get(0);
                TextView summitCount = (TextView)findViewById(R.id.summitCount);
                summitCount.setText(String.valueOf(user.getSummitCount()));

                TextView avgLength = (TextView)findViewById(R.id.avgLength);
                avgLength.setText(String.valueOf(user.getAverageLength()));

                TextView mostRecent = (TextView)findViewById(R.id.mostRecent);
                mostRecent.setText(String.valueOf(user.getMostRecent()));

                TextView userName = (TextView)findViewById(R.id.userName);
                userName.setText(String.valueOf(user.getUserName()));
            } else {
                startNewUser();
            }
        }
    }
}
