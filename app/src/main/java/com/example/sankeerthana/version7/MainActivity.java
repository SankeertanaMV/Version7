package com.example.sankeerthana.version7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends AppCompatActivity {

    private UsersDBAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new UsersDBAdapter(this);
        dbHelper.open();

        //Clean all data
        dbHelper.deleteAllUsers();
        //Add some data
        dbHelper.insertSomeUsers();

        //Generate ListView from SQLite Database
        displayListView();

    }

    private void displayListView() {


        Cursor cursor = dbHelper.fetchAllUsers();

        // The desired columns to be bound
        String[] columns = new String[] {
                UsersDBAdapter.KEY_NAME,
                UsersDBAdapter.KEY_FLATNUMBER,
                UsersDBAdapter.KEY_BILL_EP1,
                UsersDBAdapter.KEY_BILL_EP2,
                UsersDBAdapter.KEY_BILL_EP3,
                UsersDBAdapter.KEY_BILL_FLAT,
                UsersDBAdapter.KEY_USAGE_EP1,
                UsersDBAdapter.KEY_USAGE_EP2,
                UsersDBAdapter.KEY_USAGE_EP3,
                UsersDBAdapter.KEY_USAGE_FLAT
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.name,
                R.id.flatnumber,
                R.id.bill_ep1,
                R.id.bill_ep2,
                R.id.bill_ep3,
                R.id.bill_flat,
                R.id.usage_ep1,
                R.id.usage_ep2,
                R.id.usage_ep3,
                R.id.usage_flat,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.user_info,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String userName =
                        cursor.getString(cursor.getColumnIndexOrThrow("name"));
                Toast.makeText(getApplicationContext(),
                        userName, Toast.LENGTH_SHORT).show();
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchUsersByName(constraint.toString());
            }
        });

    }
}

