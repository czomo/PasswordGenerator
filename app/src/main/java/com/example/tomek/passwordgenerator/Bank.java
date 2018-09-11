package com.example.tomek.passwordgenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomek.passwordgenerator.Helper.DBHelper;



public class Bank extends AppCompatActivity {
    static String mySum = "";
    Button btnAdd, btnUpdate, btnDelete, back;
    EditText website;
    ListView passlist;
    String saveWebsite = "";

    public static String addition1() {
        return mySum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank);
        net.sqlcipher.database.SQLiteDatabase.loadLibs(this);


        passlist = (ListView) findViewById(R.id.passlist);
        passlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) passlist.getItemAtPosition(position);
                website.setText(item);
                saveWebsite = item;

            }
        });
        website = (EditText) findViewById(R.id.website);
        btnAdd = (Button) findViewById(R.id.btnadd);
        btnUpdate = (Button) findViewById(R.id.btnupdate);
        btnDelete = (Button) findViewById(R.id.btndelete);
        back = (Button) findViewById(R.id.back);
        passlist = (ListView) findViewById(R.id.passlist);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                mySum = intent.getStringExtra("key");
                DBHelper.getInstance(Bank.this).insertWebsite(website.getText().toString());
                reload();
                Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_SHORT).show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                mySum = intent.getStringExtra("key");
                DBHelper.getInstance(Bank.this).updateWebsite(saveWebsite, website.getText().toString(), mySum);
                reload();
                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.getInstance(Bank.this).deleteWebsite(website.getText().toString());
                reload();
                Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();

            }
        });
        passlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                Log.v("long clicked", "pos: " + pos);
                Toast.makeText(getApplicationContext(), "Copied to clippboard!", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", DBHelper.toCopy(((TextView) arg1).getText().toString()));
                clipboard.setPrimaryClip(clip);
                return true;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //setContentView(R.layout.activity_main);
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
