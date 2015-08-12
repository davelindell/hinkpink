package com.lindell.app.hinkpink;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lindell.app.hinkpink.communication.ClientCommunicator;
import com.lindell.app.hinkpink.shared.ClientException;
import com.lindell.app.hinkpink.shared.communication.AddConnectionParams;
import com.lindell.app.hinkpink.shared.communication.AddConnectionResult;
import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;
import com.lindell.app.hinkpink.shared.models.HinkPinkUser;

import java.util.List;


public class AddFriendActivity extends ActionBarActivity {
    HinkPinkUser user;
    ClientCommunicator cc;
    List<HinkPinkConnection> connectionList;
    EditText friendEmail;
    EditText friendDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        this.cc = new ClientCommunicator();
        user = new HinkPinkUser();
        user.setEmail(getIntent().getStringExtra("email"));
        user.setPassword(getIntent().getStringExtra("password"));

        friendEmail = (EditText) findViewById(R.id.friendEmail);
        friendDisplayName = (EditText) findViewById(R.id.friendDisplayName);

        Button addFriendButton = (Button) findViewById(R.id.button);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddConnectionParams params = new AddConnectionParams();
                params.setEmail(user.getEmail());
                params.setPassword(user.getPassword());
                params.setFriendEmail(friendEmail.getText().toString());
                params.setFriendDisplayName(friendDisplayName.getText().toString());
                AddFriendTask addFriendTask = new AddFriendTask(params);
                addFriendTask.execute((Void) null);
            }
        });
    }

    public class AddFriendTask extends AsyncTask<Void, Void, Boolean> {
        AddConnectionParams params;
        int err;
        AddFriendTask(AddConnectionParams params) {
            this.err = 0;
            this.params = params;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            AddConnectionResult result = null;
            try {
                result = cc.addFriend(this.params);
            } catch (ClientException e) {
                return false;
            }
            if (result.isValid())
                return true;
            else {
                if (result.isAlreadyExists()) {
                    err = 1;
                } else {
                    err = 0;
                }
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                finish();
            } else {
                if (err == 0) {
                    friendEmail.setError("Can't find a user with this email!");
                    friendEmail.requestFocus();
                } else if (err == 1) {
                    friendEmail.setError("You've already added this friend!");
                    friendEmail.requestFocus();
                } else {
                    friendEmail.setError("Error loading data!");
                    friendEmail.requestFocus();
                }
            }
        }

        @Override
        protected void onCancelled() {
            return;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
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
}
