package com.example.tomek.passwordgenerator;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tomek.passwordgenerator.Helper.DBHelper;

/**
 * Created by Tomek on 11.07.2018.
 */

public class Bank extends AppCompatActivity {
    Button btnAdd,btnUpdate,btnDelete;
    EditText website;
    ListView passlist;
    String saveWebsite="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank);
        Intent intent = getIntent();
        final String  value = intent.getStringExtra("key");
        //net.sqlcipher.database.SQLiteDatabase.loadLibs(this);
        net.sqlcipher.database.SQLiteDatabase.loadLibs(this);


        passlist= (ListView)findViewById(R.id.passlist);
        passlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)passlist.getItemAtPosition(position);
                website.setText(item);
                saveWebsite=item;


            }
        });
        website=(EditText)findViewById(R.id.website);
        btnAdd=(Button)findViewById(R.id.btnadd);
        btnUpdate=(Button)findViewById(R.id.btnupdate);
        btnDelete=(Button)findViewById(R.id.btndelete);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.getInstance(Bank.this).insertWebsite(website.getText().toString());
                reload();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.getInstance(Bank.this).updateWebsite(saveWebsite,website.getText().toString());
                reload();
                Toast.makeText(getApplicationContext(),"Updated!" , Toast.LENGTH_SHORT).show();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.getInstance(Bank.this).deleteWebsite(website.getText().toString());
                reload();
                //Toast.makeText(getApplicationContext(),"Deleted!" , Toast.LENGTH_SHORT).show();

            }
        });
        reload();
    }

    private void reload() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                DBHelper.getInstance(this).getAll());
        passlist.setAdapter(adapter);
    }
}
