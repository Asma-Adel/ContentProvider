package com.example.contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
     Button loadContacts;
     TextView listContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listContacts = findViewById(R.id.listContacts);
        loadContacts = findViewById(R.id.loadContacts);

        loadContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadContacts();
            }
        });
    }
    private void loadContacts(){
        StringBuilder builder = new StringBuilder();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null,
                null, null);
       
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNum > 0) {
                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                            new String[]{id}, null);

                    while (cursor2.moveToNext()) {
                        String phoneNum = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        builder.append("Contact : ").append(name).append(", Phone Number : ").append(phoneNum).append("\n\n");
                    }

                    cursor2.close();
                }
            }
        }
        cursor.close();

        listContacts.setText(builder.toString());
    }
}
