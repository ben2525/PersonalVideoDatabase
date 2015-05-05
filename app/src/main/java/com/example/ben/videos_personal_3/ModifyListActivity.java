package com.example.ben.videos_personal_3;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ModifyListActivity extends Activity{
    EditText idEntry;
    Button buttonModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_list);

        idEntry = (EditText) findViewById(R.id.ListIDEntry);
        buttonModify = (Button) findViewById(R.id.buttonModifyInfo);

        populateModifyListView();
        initialVisibility();
        onClick_Modify();
    }

    // See populateListView() in MainActivity.java for description
    // May look into modifying this method to reduce duplication
    private void populateModifyListView() {
        Cursor cursor = MainActivity.mDb.getAllRows();

        String[] fromFieldNames = new String[] {DBAdapter.COL_KEY_NAME_ID,
                DBAdapter.COL_NAME_VIDEO};
        int[] toViewIDs = new int[] {R.id.textViewID, R.id.textViewVideoName};

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(),
                R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);

        ListView myList = (ListView)findViewById(R.id.modifyListView);
        myList.setAdapter(myCursorAdapter);
    }

    private void initialVisibility() {
        idEntry.setVisibility(View.GONE);
    }

    public void onClick_buttonAdd(View view) {
        idEntry.setVisibility(View.GONE);
        buttonModify.setVisibility(View.GONE);
        startActivity(new Intent("com.example.ben.videos_personal_3.AddingVideos"));
    }

    public void onClick_buttonDelete(View view) {
        idEntry.setVisibility(View.VISIBLE);
        idEntry.setText("");
        idEntry.setHint(" id#  ");
        buttonModify.setText("Delete Item");
        buttonModify.setVisibility(View.VISIBLE);
    }

    // Delete a selection.
    public void onClick_Modify(){
        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(idEntry.getText().toString())) {
                    MainActivity.mDb.deleteRow(Integer.valueOf(idEntry.getText().toString()));
                }
                // Update the viewed list,  reset the text field   "idEntry"
                populateModifyListView();
                idEntry.setText("");
                idEntry.requestFocus();
                idEntry.setHint(" id#  ");
            }
        });
    }
}
