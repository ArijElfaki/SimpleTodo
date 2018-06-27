package com.simpletodo.arij.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.simpletodo.arij.simpletodo.MainActivity.ITEM_TEXT;
import static com.simpletodo.arij.simpletodo.MainActivity.ITEM_POSITION;

public class EditItemActivity extends AppCompatActivity {
    EditText etItemText;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etItemText= (EditText) findViewById(R.id.etItemText);
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        position= getIntent().getIntExtra(ITEM_POSITION,0);
        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSaveItem(View v){
        Intent data = new Intent();

        //pass the updated text back
        data.putExtra(ITEM_TEXT, etItemText.getText().toString());
        data.putExtra(ITEM_POSITION, position);
        setResult(RESULT_OK, data);

        //close the edit activity and go back to main
        finish();
    }



}
