package com.lindell.app.hinkpink;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.lindell.app.hinkpink.communication.ClientCommunicator;
import com.lindell.app.hinkpink.shared.ClientException;
import com.lindell.app.hinkpink.shared.communication.NewGameParams;
import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.lindell.app.hinkpink.R.string.error_field_required;


public class NewGameActivity extends ActionBarActivity {
    ClientCommunicator cc;
    HinkPinkConnection connection;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        this.connection = (HinkPinkConnection) getIntent().getSerializableExtra("connection");
        this.email = (String)getIntent().getStringExtra("email");
        this.password = (String)getIntent().getStringExtra("password");

        cc = new ClientCommunicator();

        final RadioButton buttonOne = (RadioButton) findViewById(R.id.radioButtonOne);
        final RadioButton buttonTwo = (RadioButton) findViewById(R.id.radioButtonTwo);
        final RadioButton buttonThree = (RadioButton) findViewById(R.id.radioButtonThree);

        Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean checkedButton =  buttonOne.isChecked() || buttonTwo.isChecked() || buttonThree.isChecked();
                EditText editTextHint = (EditText) findViewById(R.id.editTextHint);
                boolean hintEmpty = editTextHint.getText().toString().isEmpty();
                if (!checkedButton || hintEmpty) {
                    editTextHint.setError(getString(error_field_required));
                    editTextHint.requestFocus();
                } else {
                    NewGameParams params = new NewGameParams();
                    params.setEmail(email);
                    params.setPassword(password);
                    params.setHint(editTextHint.getText().toString());

                    int numSyllables = 0;
                    if (buttonOne.isChecked()) {
                        numSyllables = 1;
                    } else if (buttonTwo.isChecked()) {
                        numSyllables = 2;
                    } else if (buttonThree.isChecked()) {
                        numSyllables = 3;
                    }

                    params.setNumSyllables(numSyllables);
                    params.setPlayerConnectionID(connection.getPlayerConnectionID());
                    params.setConnectionID(connection.getId());

                    SubmitNewGameTask submitNewGameTask = new SubmitNewGameTask(params);
                    submitNewGameTask.execute((Void) null);

                    try {
                        submitNewGameTask.get(1000, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException e) {
                        System.out.println(e.getMessage());
                    } catch (ExecutionException e) {
                        System.out.println(e.getMessage());
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }

                    finish();

                }
            }
        });

        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOne.setChecked(true);
                buttonTwo.setChecked(false);
                buttonThree.setChecked(false);
            }
        });

        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOne.setChecked(false);
                buttonTwo.setChecked(true);
                buttonThree.setChecked(false);
            }
        });

        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOne.setChecked(false);
                buttonTwo.setChecked(false);
                buttonThree.setChecked(true);
            }
        });
    }

    public class SubmitNewGameTask extends AsyncTask<Void, Void, Boolean> {
        NewGameParams params;

        SubmitNewGameTask(NewGameParams params) {
            this.params = params;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                cc.submitNewGame(this.params);
            } catch (ClientException e) {
                System.out.println(e.getMessage());
                return false;
            }
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
        getMenuInflater().inflate(R.menu.menu_new_game, menu);
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
