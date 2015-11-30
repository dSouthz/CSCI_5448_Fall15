package com.csci5448.hiketracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewUserActivity extends AppCompatActivity {

    private static final String TAG = "NewUserActivity";

    Button mSubmitButton;
    EditText mNameField;
    UserDataSource userDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);
        userDataSource = new UserDataSource(this);
        Button submitButton = (Button)findViewById(R.id.submitButton);
        mSubmitButton = submitButton;
        mSubmitButton.setAlpha(0.0f);
        addSubmitListener();
        EditText nameField = (EditText)findViewById(R.id.nameField);
        mNameField = nameField;
        addTextWatcher();
    }

    private void addSubmitListener() {
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNameField.getText().length() > 0) {
                    createUser();
                }
            }
        });
    }

    private void createUser() { new CreateNewUser().execute(mNameField.getText().toString()); }

    private void addTextWatcher() {
        mNameField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                toggleButtonVisibility();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void toggleButtonVisibility() {
                if (mNameField.getText().length() > 0) {
                    mSubmitButton.animate().alpha(1.0f);
                    mSubmitButton.setVisibility(View.VISIBLE);
                } else {
                    mSubmitButton.animate().alpha(0.0f);
                }
            }
        });
    }

    public class CreateNewUser extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... name) {
            User newUser = new User();
            newUser.setUserName(name[0]);
            newUser.setAverageLength(0);
            newUser.setMostRecent(getString(R.string.nothingYetLabel));
            newUser.setSummitCount(0);
            newUser.setTotalCount(0);
            userDataSource.save(newUser);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Intent mainActivity = new Intent(NewUserActivity.this, MainActivity.class);
            startActivity(mainActivity);
        }
    }
}
