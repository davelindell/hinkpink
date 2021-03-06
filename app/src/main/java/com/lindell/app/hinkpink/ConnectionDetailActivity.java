package com.lindell.app.hinkpink;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.lindell.app.hinkpink.communication.ClientCommunicator;
import com.lindell.app.hinkpink.shared.ClientException;
import com.lindell.app.hinkpink.shared.communication.GetConnectionGamesParams;
import com.lindell.app.hinkpink.shared.communication.GetConnectionGamesResult;
import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;
import com.lindell.app.hinkpink.shared.models.HinkPinkGame;

import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ConnectionDetailActivity extends ActionBarActivity {

    private ClientCommunicator cc;
    private HinkPinkConnection connection;
    private String email;
    private String password;

    private List<String> myHints;
    private List<String> friendHints;
    private ArrayAdapter myHintsAdapter;
    private ArrayAdapter friendHintsAdapter;

    private List<HinkPinkGame> gameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_detail);
        myHints = new ArrayList<String>();
        friendHints = new ArrayList<String>();


        cc = new ClientCommunicator();

        this.gameList = new ArrayList<HinkPinkGame>();
        this.connection = (HinkPinkConnection) getIntent().getSerializableExtra("connection");
        this.email = getIntent().getStringExtra("email");
        this.password = getIntent().getStringExtra("password");

        TextView friendHinkPinkLabel = (TextView) findViewById(R.id.friendHinkPinkLabel);
        friendHinkPinkLabel.setText("From " + connection.getDisplayName());

        myHintsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textview_incoming_connection, myHints);
        friendHintsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textview_incoming_connection, friendHints);

        // Here, you set the data in your ListView
        ListView myHintsList = (ListView) findViewById(R.id.listView1);
        ListView friendHintsList = (ListView) findViewById(R.id.listView2);

        myHintsList.setAdapter(myHintsAdapter);
        friendHintsList.setAdapter(friendHintsAdapter);

        myHintsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                beginGameDetailActivity(gameList.get(position));
            }
        });

        friendHintsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                beginGameDetailActivity(gameList.get(position));
            }
        });

        Button newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginNewGameActivity(connection);
            }
        });

        LoadGamesTask loadGamesTask = new LoadGamesTask();
        loadGamesTask.execute((Void) null);

        try {
            loadGamesTask.get(1000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        populateView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadGamesTask loadGamesTask = new LoadGamesTask();
        loadGamesTask.execute((Void) null);

        try {
            loadGamesTask.get(1000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        populateView();
    }

    void beginNewGameActivity(HinkPinkConnection connection) {
        Intent intent = new Intent(this, NewGameActivity.class);
        intent.putExtra("connection",connection);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        startActivity(intent);
    }

    void beginGameDetailActivity(HinkPinkGame game) {
        Intent intent = new Intent(this, GameDetailActivity.class);
        intent.putExtra("game",game);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        intent.putExtra("connection",connection);
        startActivity(intent);
    }

    void populateView() {
        friendHints.clear();
        myHints.clear();

        if (!gameList.isEmpty()) {
            for (HinkPinkGame g : gameList) {
                // check if game hint was given by connection or not
                if (g.getHintPlayerID().equals(connection.getPlayerID())) {
                    friendHints.add(g.getHint());
                } else {
                    myHints.add(g.getHint());
                }
            }
        }
        friendHintsAdapter.notifyDataSetChanged();
        myHintsAdapter.notifyDataSetChanged();
    }

    void loadGames() {
        GetConnectionGamesParams connectionGamesParams = new GetConnectionGamesParams();
        connectionGamesParams.setEmail(email);
        connectionGamesParams.setPassword(password);
        connectionGamesParams.setConnectionID(connection.getId());
        try {
            GetConnectionGamesResult result = cc.getConnectionGames(connectionGamesParams);
            this.gameList = result.getGameList();
        } catch (ClientException e) {
            System.err.println("ClientException: " + e.getMessage());
        }
        return;
    }

    public class LoadGamesTask extends AsyncTask<Void, Void, Boolean> {
        LoadGamesTask() {
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            loadGames();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection_detail, menu);
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
