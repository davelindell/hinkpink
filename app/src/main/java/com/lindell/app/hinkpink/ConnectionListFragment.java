package com.lindell.app.hinkpink;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lindell.app.hinkpink.communication.ClientCommunicator;
import com.lindell.app.hinkpink.shared.ClientException;
import com.lindell.app.hinkpink.shared.communication.ValidateUserParams;
import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;
import com.lindell.app.hinkpink.shared.models.HinkPinkUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConnectionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectionListFragment extends Fragment {
    private HinkPinkUser user;
    private ClientCommunicator cc;
    ArrayAdapter connectionListAdapter;
    private List<HinkPinkConnection> connectionList;
    private List<String> displayNameList;
    private ConnectionListTask connectionListTask;
    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ConnectionListFragment newInstance(String email, String password) {
        ConnectionListFragment fragment = new ConnectionListFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL_PARAM, email);
        args.putString(PASSWORD_PARAM, password);
        fragment.setArguments(args);
        return fragment;
    }

    public ConnectionListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.cc = new ClientCommunicator();
        this.connectionList = new ArrayList<HinkPinkConnection>();
        this.displayNameList = new ArrayList<String>();
        this.connectionListAdapter = null;
        user = new HinkPinkUser();

        if (getArguments() != null) {
            user.setEmail(getArguments().getString(EMAIL_PARAM));
            user.setPassword(getArguments().getString(PASSWORD_PARAM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connection_list, container, false);
        Button addFriendButton = (Button) view.findViewById(R.id.add_friend);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginAddFriendActivity(user.getEmail(), user.getPassword());
            }
        });

        connectionListAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.textview_connection_list, displayNameList);

        // Here, you set the data in your ListView
        ListView connectionListView = (ListView) view.findViewById(R.id.connectionListView);

        connectionListView.setAdapter(connectionListAdapter);
        connectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                beginConnectionDetailActivity(connectionList.get(position));
            }
        });

        Button refreshButton = (Button) view.findViewById(R.id.conListRefreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionListTask connectionListTask = new ConnectionListTask();
                connectionListTask.execute((Void) null);
                try {
                    connectionListTask.get(1000, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    System.out.println(e.getMessage());
                } catch (ExecutionException e) {
                    System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                connectionListAdapter.notifyDataSetChanged();
            }
        });

        connectionListTask = new ConnectionListTask();
        connectionListTask.execute((Void) null);

        try {
            connectionListTask.get(1000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        connectionListAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void beginAddFriendActivity(String email, String password) {
        Intent intent = new Intent(getActivity(), AddFriendActivity.class);
        // http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents/2141166#2141166
        intent.putExtra("email",email);
        intent.putExtra("password", password);
        getActivity().startActivity(intent);
    }

    public class ConnectionListTask extends AsyncTask<Void, Void, Boolean> {
        ConnectionListTask() {
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            setConnectionList();
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
        LinearLayout connectionLayout = (LinearLayout) getView().findViewById(R.id.connection_form);

        for (HinkPinkConnection c : connectionList) {
            final HinkPinkConnection cur_connection = c;
            Button cur_button = new Button(getActivity());
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
        Intent intent = new Intent(getActivity(), ConnectionDetailActivity.class);
        // http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents/2141166#2141166
        intent.putExtra("connection",connection);
        intent.putExtra("email",user.getEmail());
        intent.putExtra("password",user.getPassword());
        startActivity(intent);
    }


    void setConnectionList() {
        ValidateUserParams params = new ValidateUserParams();
        params.setEmail(user.getEmail());
        params.setPassword(user.getPassword());
        try {
            this.connectionList = cc.getUserConnections(params).getConnectionList();
            this.displayNameList.clear();
            for (HinkPinkConnection c : this.connectionList) {
                this.displayNameList.add(c.getDisplayName());
            }

        } catch (ClientException e) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Network Connection Error")
                    .setMessage("Unable to connect to the network. Please check your connection.")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            setConnectionList();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
