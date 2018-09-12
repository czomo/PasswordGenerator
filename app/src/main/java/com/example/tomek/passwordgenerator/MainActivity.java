package com.example.tomek.passwordgenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textView;
    private Button generatePass;
    private Button copy;
    private Button save;
    private Switch uppperCase;
    private Switch numbers;
    private Switch numbersOnly;
    private EditText password;
    private EditText specCharEdit;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        textView = (TextView) findViewById(R.id.seektext);
        generatePass = (Button) findViewById(R.id.passwordbtn);
        copy = (Button) findViewById(R.id.passwordcopy);
        save = (Button) findViewById(R.id.passwordsave);
        uppperCase = (Switch) findViewById(R.id.uppercase);
        numbers = (Switch) findViewById(R.id.numbers);
        numbersOnly = (Switch) findViewById(R.id.numbersOnly);
        password = (EditText) findViewById(R.id.password);
        specCharEdit = (EditText) findViewById(R.id.specchar);

        seekBar.setMax(25);
        textView.setText("Password lengt: " + seekBar.getProgress() + " / " + seekBar.getMax());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Covered: " + progress + "/" + seekBar.getMax());
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();

            }
        });
        generatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() < 4) {
                    Toast.makeText(getApplicationContext(), "For your safety please choose longer password", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                    password.setText(genereatePass(seekBar.getProgress()));
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Bank.class);
                myIntent.putExtra("key", password.getText().toString()); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                // setContentView(R.layout.bank);

            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText() != null) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("password", password.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        numbersOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean switchStateNumbersOnly = numbersOnly.isChecked();
                if (switchStateNumbersOnly == true) {
                    uppperCase.setChecked(false);
                    numbers.setChecked(false);
                }
            }
        });
        uppperCase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean switchStateNumbers = uppperCase.isChecked();
                if (switchStateNumbers == true) {
                    numbersOnly.setChecked(false);
                }
            }
        });
        numbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean switchStateUpperCase = numbers.isChecked();
                if (switchStateUpperCase == true) {
                    numbersOnly.setChecked(false);
                }
            }
        });
    }

    private String genereatePass(int lenght) {
        Boolean switchStateUpperCase = uppperCase.isChecked();
        Boolean switchStateNumbers = numbers.isChecked();
        Boolean switchStateNumbersOnly = numbersOnly.isChecked();
        char[] chars;
        String addchars;
        String addchars2;
        if (switchStateNumbersOnly == true) {
            addchars = "1234567890";
        } else if (switchStateUpperCase == true && switchStateNumbers == true) {
            addchars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        } else if (switchStateUpperCase == true) {
            addchars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        } else if (switchStateNumbers == true) {
            addchars = "qwertyuiopasdfghjklzxcvbnm1234567890";
        } else {
            addchars = "qwertyuiopasdfghjklzxcvbnm";
        }

        if (specCharEdit.getText() != null) {
            addchars2 = specCharEdit.getText().toString();
            addchars = addchars + addchars2;
        }
        chars = addchars.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < lenght; i++) {
            char pp = chars[random.nextInt(chars.length)];
            stringBuilder.append(pp);
        }
        return stringBuilder.toString();
    }

}
