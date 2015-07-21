package com.lindell.app.hinkpink;

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;


public class ConnectionDetailActivity extends ActionBarActivity {

    private ClientCommunicator cc;
    private HinkPinkConnection connection;
    private String email;
    private String password;

    private List<String> myHints;
    private List<String> friendHints;

    private List<HinkPinkGame> gameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_detail);
        myHints = new ArrayList<String>();
        friendHints = new ArrayList<String>();

        this.connection = (HinkPinkConnection) getIntent().getSerializableExtra("connection");
        this.email = getIntent().getStringExtra("email");
        this.password = getIntent().getStringExtra("password");

        TextView friendHinkPinkLabel = (TextView) findViewById(R.id.friendHinkPinkLabel);
        friendHinkPinkLabel.setText(connection.getDisplayName());

        loadGames();

        populateView();

        ArrayAdapter myHintsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_connection_detail, myHints);
        ArrayAdapter friendHintsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_connection_detail, friendHints);

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
        startActivity(intent);
    }

    void populateView() {
        for (HinkPinkGame g : gameList) {
            // check if game hint was given by connection or not
            if (g.getHintPlayerID().equals(connection.getPlayerID())) {
                friendHints.add(g.getHint());
            } else {
                myHints.add(g.getHint());
            }
        }
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
