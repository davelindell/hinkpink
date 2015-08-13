package com.lindell.app.hinkpink;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lindell.app.hinkpink.communication.ClientCommunicator;
import com.lindell.app.hinkpink.shared.ClientException;
import com.lindell.app.hinkpink.shared.communication.GetConnectionGamesParams;
import com.lindell.app.hinkpink.shared.communication.GetGameParams;
import com.lindell.app.hinkpink.shared.communication.GetGameResult;
import com.lindell.app.hinkpink.shared.communication.SubmitGameInfoParams;
import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;
import com.lindell.app.hinkpink.shared.models.HinkPinkGame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class GameDetailActivity extends ActionBarActivity {
    private ClientCommunicator cc;
    private HinkPinkGame game;
    private HinkPinkConnection connection;
    private String email;
    private String password;
    private ArrayAdapter hintsAdapter;
    private ArrayAdapter guessesAdapter;
    private List<String> extraHints;
    private List<String> guesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        this.game = (HinkPinkGame) getIntent().getSerializableExtra("game");
        this.email = (String) getIntent().getStringExtra("email");
        this.password = (String) getIntent().getStringExtra("password");
        this.connection = (HinkPinkConnection) getIntent().getSerializableExtra("connection");

        this.extraHints = new ArrayList<String>();
        this.guesses = new ArrayList<String>();

        this.cc = new ClientCommunicator();

        hintsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textview_incoming_connection, extraHints);
        guessesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textview_incoming_connection, guesses);

        // Here, you set the data in your ListView
        ListView listViewHints = (ListView) findViewById(R.id.listViewHints);
        ListView listViewGuesses = (ListView) findViewById(R.id.listViewGuesses);

        listViewHints.setAdapter(hintsAdapter);
        listViewGuesses.setAdapter(guessesAdapter);

        TextView hintText = (TextView) findViewById(R.id.textViewHint);
        hintText.setText(game.getHint());

        Long connectionID = connection.getPlayerID();
        if (connectionID.equals(game.getGuessPlayerID())) { // current player provided the hint
            EditText extraHintText = (EditText) findViewById(R.id.editText);
            extraHintText.setHint("Extra Hint");

            Button submitButton = (Button) findViewById(R.id.submitButton);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubmitGameInfoParams params = new SubmitGameInfoParams();
                    EditText extraHintText = (EditText) findViewById(R.id.editText);

                    params.setEmail(email);
                    params.setPassword(password);
                    params.setInformation(extraHintText.getText().toString());
                    params.setIsGuess(false);
                    params.setGameID(game.getId());

                    SubmitInfoTask submitHintTask = new SubmitInfoTask(params);
                    submitHintTask.execute((Void) null);

                    try {
                        submitHintTask.get(1000, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException e) {
                        System.out.println(e.getMessage());
                    } catch (ExecutionException e) {
                        System.out.println(e.getMessage());
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }

                    extraHintText.setText("");

                    updateView();

                }
            });

        } else { // opposing player provided the hint
            EditText guessText = (EditText) findViewById(R.id.editText);
            guessText.setHint("Guess");

            Button submitButton = (Button) findViewById(R.id.submitButton);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubmitGameInfoParams params = new SubmitGameInfoParams();
                    EditText guessText = (EditText) findViewById(R.id.editText);

                    params.setEmail(email);
                    params.setPassword(password);
                    params.setGameID(game.getId());
                    params.setInformation(guessText.getText().toString());
                    params.setIsGuess(true);

                    SubmitInfoTask submitGuessTask = new SubmitInfoTask(params);
                    submitGuessTask.execute((Void) null);

                    try {
                        submitGuessTask.get(1000, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException e) {
                        System.out.println(e.getMessage());
                    } catch (ExecutionException e) {
                        System.out.println(e.getMessage());
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }

                    guessText.setText("");

                    updateView();
                }
            });
        }

        Button refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetGameParams params = new GetGameParams();
                params.setEmail(email);
                params.setPassword(password);
                params.setGameID(game.getId());
                GetGameTask getGameTask = new GetGameTask(params);
                getGameTask.execute((Void) null);

                try {
                    getGameTask.get(1000, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    System.out.println(e.getMessage());
                } catch (ExecutionException e) {
                    System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }

                updateView();
            }

        });

        updateView();
    }

    public class GetGameTask extends AsyncTask<Void, Void, Boolean> {
        GetGameParams params;

        GetGameTask(GetGameParams params) {
            this.params = params;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            GetGameResult result = new GetGameResult();
            try {
                result = cc.getGame(this.params);
            } catch (ClientException e) {
                System.out.println(e.getMessage());
            }
            game = result.getGame();
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

    public class SubmitInfoTask extends AsyncTask<Void, Void, Boolean> {
        SubmitGameInfoParams params;

        SubmitInfoTask(SubmitGameInfoParams params) {
            this.params = params;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            GetGameResult result = new GetGameResult();
            GetGameParams gameParams = new GetGameParams();
            gameParams.setGameID(game.getId());
            gameParams.setEmail(email);
            gameParams.setPassword(password);

            try {
                cc.submitGameInfo(this.params);
            } catch (ClientException e) {
                System.out.println(e.getMessage());
            }
            // get updated game list
            try {
                result = cc.getGame(gameParams);
            } catch (ClientException e) {
                System.out.println(e.getMessage());
            }
            game = result.getGame();

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

    void updateView() {
        extraHints.clear();
        guesses.clear();
        for (String s : game.getExtraHints()){
            extraHints.add(s);
        }
        for (String s : game.getGuesses()) {
            guesses.add(s);
        }

        guessesAdapter.notifyDataSetChanged();
        hintsAdapter.notifyDataSetChanged();
    }
}
