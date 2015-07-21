package com.lindell.app.hinkpink;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;
import com.lindell.app.hinkpink.shared.models.HinkPinkGame;


public class GameDetailActivity extends ActionBarActivity {

    HinkPinkGame game;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        this.game = (HinkPinkGame) getIntent().getSerializableExtra("game");
        this.email = (String) getIntent().getStringExtra("email");

        ArrayAdapter hintsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_game_detail, game.getExtraHints());
        ArrayAdapter guessesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_game_detail, game.getGuesses());

        // Here, you set the data in your ListView
        ListView listViewHints = (ListView) findViewById(R.id.listViewHints);
        ListView listViewGuesses = (ListView) findViewById(R.id.listViewGuesses);

        listViewHints.setAdapter(hintsAdapter);
        listViewGuesses.setAdapter(guessesAdapter);

        //TODO: check if current user is guesser or hinter
        //TODO: add buttons to guess or provide additional hints
        // TODO: add servlets to handle guess/extraHint inputs

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_detail, menu);
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
