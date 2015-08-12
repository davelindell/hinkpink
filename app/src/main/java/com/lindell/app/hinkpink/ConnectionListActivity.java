package com.lindell.app.hinkpink;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lindell.app.hinkpink.communication.ClientCommunicator;
import com.lindell.app.hinkpink.shared.ClientException;
import com.lindell.app.hinkpink.shared.communication.RegisterUserParams;
import com.lindell.app.hinkpink.shared.communication.ValidateUserParams;
import com.lindell.app.hinkpink.shared.communication.ValidateUserResult;
import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;
import com.lindell.app.hinkpink.shared.models.HinkPinkGame;
import com.lindell.app.hinkpink.shared.models.HinkPinkUser;

import java.util.ArrayList;
import java.util.List;


public class ConnectionListActivity extends ActionBarActivity {

    //TODO: Make room for incoming/pending lists
    private HinkPinkUser user;
    private ClientCommunicator cc;
    private List<HinkPinkConnection> connectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_list);
        this.cc = new ClientCommunicator();
        this.connectionList = new ArrayList<HinkPinkConnection>();
        user = new HinkPinkUser();
        user.setEmail(getIntent().getStringExtra("email"));
        user.setPassword(getIntent().getStringExtra("password"));

        Button addFriendButton = (Button) findViewById(R.id.add_friend);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginAddFriendActivity(user.getEmail(),user.getPassword());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // populate the connectionList variable
        ConnectionListTask connectionListTask = new ConnectionListTask();
        connectionListTask.execute((Void) null);
    }

    public class ConnectionListTask extends AsyncTask<Void, Void, Boolean> {

        ConnectionListTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            setConnectionList();
            loadConnections();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            return;
        }

        @Override
        protected void onCancelled() {
            return;
        }
    }

    void loadConnections() {
        LinearLayout connectionLayout = (LinearLayout) findViewById(R.id.connection_form);


        for (HinkPinkConnection c : connectionList) {
            final HinkPinkConnection cur_connection = c;
            Button cur_button = new Button(this);
            cur_button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            cur_button.setText(c.getDisplayName());
            cur_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    beginConnectionDetailActivity(cur_connection);
                }
            });
            //add button to the layout
            connectionLayout.addView(cur_button);
        }
    }

    public void beginConnectionDetailActivity(HinkPinkConnection connection) {
        Intent intent = new Intent(this, ConnectionDetailActivity.class);
        // http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents/2141166#2141166
        intent.putExtra("connection",connection);
        intent.putExtra("email",user.getEmail());
        intent.putExtra("password",user.getPassword());
        startActivity(intent);
    }

    public void beginAddFriendActivity(String email, String password) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        // http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents/2141166#2141166
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        startActivity(intent);
    }

    void setConnectionList() {
        ValidateUserParams params = new ValidateUserParams();
        params.setEmail(user.getEmail());
        params.setPassword(user.getPassword());
        try {
            this.connectionList = cc.getUserConnections(params).getConnectionList();
        } catch (ClientException e) {
            new AlertDialog.Builder(this)
                .setTitle("Network Connection Error")
                .setMessage("Unable to connect to the network. Please check your connection.")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setConnectionList();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
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
