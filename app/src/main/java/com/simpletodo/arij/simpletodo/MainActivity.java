package com.simpletodo.arij.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //declaring stateful objects here; will be null prior to onCreate
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    public static final int EDIT_REQUEST_CODE=20;
    public static final String ITEM_TEXT= "itemText";
    public static final String ITEM_POSITION= "itemPosition";


    //called by android when activity first opens
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //superclass logic executes first
        super.onCreate(savedInstanceState);
        //inflating the layout file from res/layout/activity_main.xml
        setContentView(R.layout.activity_main);

        readItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemsAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v){
        //obtain a reference to EditText created by the layout
        EditText etNewItem= (EditText) findViewById(R.id.etNewItem);
        //get EditText's content as a string
        String itemText = etNewItem.getText().toString();
        //add item to list via adapter
        itemsAdapter.add(itemText);
        etNewItem.setText("");

       //write items to file
        writeItems();
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener(){

        //listen for a long click on an item
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
               //remove the item at the position and notify adapter of change
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();

               //write items to file
                writeItems();
                return true;
            }
        });

        //listen for a short click on an item
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               //set up intent to send information to edit screen page
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });



    }

    //get the data file
    private File getDataFile(){
        return new File (getFilesDir(), "todo.txt");
    }

    //read items in the file for the list of items
    private void readItems(){
        try {
            //store items in arraylist
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e) {
            //create an empty arraylist
            e.printStackTrace();
            items = new ArrayList<>();
        }
    }

    //write items list to the file
    private void writeItems(){
        try{
            FileUtils.writeLines(getDataFile(), items);
        }
        catch (IOException e){
            Log.e("MainActivity", "Error writing file",e);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if (resultCode==RESULT_OK && requestCode== EDIT_REQUEST_CODE){
            //extract the updated item value from results extras and get the position
            String updatedItem=data.getExtras().getString(ITEM_TEXT);
            int position = data.getExtras().getInt(ITEM_POSITION, 0);

            //set the items list to the new information and write to files
            items.set(position, updatedItem);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
            Toast.makeText(this, "Item Updated", Toast.LENGTH_SHORT).show();

        }

    }






}
