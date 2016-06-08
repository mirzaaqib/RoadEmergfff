 package com.mirzaaqibbeg.roademergencies;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mirzaaqibbeg.roademergencies.DataStructure.ContactLocal;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SmsEditor extends AppCompatActivity {

    static final int PICK_CONTACT = 1;
    SharedPreferences preferences;
    private TextView textViewContacts;

//    private final static int RQS_PICK_CONTACT=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_editor);
        preferences = getSharedPreferences(Fields.SETTINGS, MODE_PRIVATE);
        textViewContacts = (TextView) findViewById(R.id.contactText);
        textViewContacts.setText(preferences.getString(Fields.CONTACTS,""));


    }

    @Override
    protected void onResume() {
        super.onResume();
        textViewContacts = (TextView) findViewById(R.id.contactText);
        textViewContacts.setText(preferences.getString(Fields.NUMBER, ""));

    }

    public void pickContact(View view) {

        Intent intent = new Intent(this, ContactList.class);
        startActivity(intent);
//        getContactsToSMS();


    }


    public void sendMessage(View view) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SmsEditor.this);
//        builderSingle.setIcon(R.dr);
        builderSingle.setTitle("Select One to send:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                SmsEditor.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Stuck in Traffic");
        arrayAdapter.add("Out of fuel");
        arrayAdapter.add("Call me asap");
        arrayAdapter.add("I'm driving");
        arrayAdapter.add("On my way");
        arrayAdapter.add("I'm in a meeting");
        arrayAdapter.add("sorry,I'm busy.Call back later");

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = arrayAdapter.getItem(which);
                        String numbers=preferences.getString(Fields.NUMBER,"");
                        if (!numbers.equals("")){
                            // TODO: 09-03-2016 send message
                            numbers=numbers.replace("\n", ";");
                            numbers=numbers.substring(0, numbers.length() - 1);
                            Uri sendSmsTo=Uri.parse("smsto:"+numbers);
                            Intent intent= new Intent(Intent.ACTION_SENDTO,sendSmsTo);
                            intent.putExtra("sms_body",message);
                            startActivity(intent);


                        }else {
                            Toast.makeText(SmsEditor.this,"No number saved",Toast.LENGTH_LONG).show();
                        }

//                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
//                                SmsEditor.this);
//                        builderInner.setMessage(strName);
//                        builderInner.setTitle("Your Selected Item is");
//                        builderInner.setPositiveButton(
//                                "Ok",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(
//                                            DialogInterface dialog,
//                                            int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        builderInner.show();
                    }
                });
        builderSingle.show();
    }

    public void clearContacts(View view) {
        preferences.edit().putString(Fields.NUMBER,"").apply();
        recreate();
    }
}
