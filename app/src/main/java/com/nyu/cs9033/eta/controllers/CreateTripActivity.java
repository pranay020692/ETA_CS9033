package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CreateTripActivity extends Activity {

    private static final String TAG = "CreateTripActivity";
    Button saveButton;
    Button cancelButton;
    Button addContactsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // Access the Database
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases/MyDB";
            File f = new File(destPath);
            if (!f.exists()) {
                CopyDB(getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button button = (Button) findViewById(R.id.buttonLocation);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // read the search value for the location and call HW3 Foursquare APIs
                EditText valueSearch = (EditText) findViewById(R.id.search1);
                EditText locationSearch = (EditText) findViewById(R.id.search2);
                EditText locationTypeSearch = (EditText) findViewById(R.id.search3);

                String value = valueSearch.getText().toString();
                String location = locationSearch.getText().toString();
                String locationType = locationTypeSearch.getText().toString();

                Uri loc = Uri.parse("location://com.example.nyu.hw3api");
                Intent callIntent = new Intent(Intent.ACTION_VIEW, loc);
                String search = value + ", " + location + "::" + locationType;
                callIntent.putExtra("searchVal", search);

                if (value.matches("") || location.matches("") || locationType.matches("")) {
                    Toast.makeText(CreateTripActivity.this, "Enter the Search Fields!", Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(callIntent, 2);
                }
            }
        });

        addContactsButton = (Button) findViewById(R.id.buttonAdd);
        addContactsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //implicit Intent call to pick contacts
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            }

        });

        saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText zName = (EditText) findViewById(R.id.nameEdit);
                EditText zOrigin = (EditText) findViewById(R.id.destEdit);
                //EditText zDestination = (EditText) findViewById(R.id.origEdit);

                String aName = zName.getText().toString();
                String aOrigin = zOrigin.getText().toString();
                //String aDestination = zDestination.getText().toString();


                if (aName.matches("") || aOrigin.matches("")) {// check if fields are not blank
                    Toast.makeText(CreateTripActivity.this, "Name, Origin and Destination is Required!", Toast.LENGTH_SHORT).show();
                } else {
                    Trip mTrip = createTrip();
                    saveTrip(mTrip);
                    Toast.makeText(CreateTripActivity.this, "Trip Created!", Toast.LENGTH_SHORT).show();
                }

            }

        });

        cancelButton = (Button) findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                cancelTrip();
            }
        });
        // TODO - fill in here
    }

    //uses the trip object to create a trip
    public Trip createTrip() {
        EditText zName = (EditText) findViewById(R.id.nameEdit);
        //EditText zOrigin = (EditText) findViewById(R.id.origEdit);
        EditText zDestination = (EditText) findViewById(R.id.destEdit);
        String aName = zName.getText().toString();
        //String aOrigin = zOrigin.getText().toString();
        String aDestination = zDestination.getText().toString();
        Trip mTrip = new Trip(aName, null, aDestination, 50);
        return mTrip;
    }

    //saves the trip in the database
    public boolean saveTrip(Trip trip) {

        String aName = trip.getName();
        //String aOrigin = trip.getName();
        String aDestination = trip.getDestination();

        // TODO - fill in here
        Pair peopleLocationPair = new Pair<>(aName, aDestination);
        CreateTripAPIAdapter CreateTripAPI = new CreateTripAPIAdapter();
        CreateTripAPI.callCreateTrip(peopleLocationPair, this);
        return true;
    }


    //cancels the trip and returns to the main activity
    public void cancelTrip() {
        Intent cIntent = new Intent(this, MainActivity.class);
        setResult(RESULT_CANCELED, cIntent);
        finish();
        // TODO - fill in here
    }


    public void CopyDB(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        ;
        EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        //Receive the Names and Phonenumber from Contacts
        if (reqCode == 1) {
            try {
                Cursor cursor = managedQuery(data.getData(), null, null, null, null);
                while (cursor.moveToNext()) {
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    String a = "";
                    String phoneNumber;
                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1"))
                        hasPhone = "true";
                    else
                        hasPhone = "false";

                    if (Boolean.parseBoolean(hasPhone)) {
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (phones.moveToNext()) {
                            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            a += phoneNumber;
                        }
                        phones.close();
                    }
                    Person p = new Person(name, a, null);
                    if (name != null) {
                        if (nameEdit.getText().toString().trim().length() > 0)
                            nameEdit.append(", " + p.getName());
                        else
                            nameEdit.setText(p.getName());
                    }

                }

            } catch (NullPointerException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        //Receive the Address of the Destination.
        else if (reqCode == 2) {
            ArrayList<String> list = data.getStringArrayListExtra("retVal");
            EditText zDestination = (EditText) findViewById(R.id.destEdit);
            for (String details : list) {
                zDestination.setText(list.get(1));
            }
        }
    }
}

