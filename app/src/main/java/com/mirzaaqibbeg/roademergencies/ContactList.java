package com.mirzaaqibbeg.roademergencies;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mirzaaqibbeg.roademergencies.DataStructure.ContactLocal;

import java.util.ArrayList;

public class ContactList extends AppCompatActivity {

    private ArrayList<ContactLocal> alContacts;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        ListView listView= (ListView) findViewById(R.id.listContact);

        preferences = getSharedPreferences(Fields.SETTINGS, MODE_PRIVATE);



        ContentResolver cr = this.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if(cursor.moveToFirst())
        {
            alContacts = new ArrayList<ContactLocal>();
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                ContactLocal local=new ContactLocal();
                local.setId(id);
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                local.setName(name);
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);

                    while (pCur.moveToNext())
                    {

                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        local.setNumber(contactNumber);
                        Log.e(Fields.MECHANIC,contactNumber);
                        break;
                    }
                    pCur.close();
                }
                alContacts.add(local);

            } while (cursor.moveToNext()) ;
        }
        CustomAdapter customAdapter=new CustomAdapter(alContacts);




//        ArrayAdapter<ContactLocal> arrayAdapter=new ArrayAdapter<ContactLocal>(this,android.R.layout.activity_list_item,alContacts);
        listView.setAdapter(customAdapter);
        final SharedPreferences.Editor editor=preferences.edit();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create intent to deliver some kind of result data
                ContactLocal contactLocal=alContacts.get(position);
                String number=contactLocal.getNumber();
                String name=contactLocal.getName();

                String numbers=preferences.getString(Fields.NUMBER, "");
                editor.putString(Fields.NUMBER,numbers+""+number+"\n");
                editor.apply();

                Toast.makeText(ContactList.this, name+" added to your quick contack list.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public  class CustomAdapter extends BaseAdapter{

        private final ArrayList<ContactLocal> contactList;

        public CustomAdapter(ArrayList<ContactLocal> arrayList){
           this.contactList=arrayList;
        }

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public Object getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getApplicationContext());
                v = vi.inflate(R.layout.contact_list_item, null);
            }
            TextView textView= (TextView) v.findViewById(R.id.title);
            TextView textNumber= (TextView) v.findViewById(R.id.phoneNumber);
            ContactLocal contactLocal=contactList.get(position);
            textView.setText(contactLocal.getName());
            textNumber.setText(contactLocal.getNumber());
            return v;
        }
    }

}
