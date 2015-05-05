package com.example.ben.videos_personal_3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends Activity {

    static DBAdapter mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDB();
        populateListView();
        listViewItemClick();
    }

    private void openDB() {
        mDb = new DBAdapter(this);
        mDb.open();
    }

    public void onClick_ModifyList(View v) {
        startActivity(new Intent("com.example.ben.videos_personal_3.ModifyListActivity"));
    }

    public void populateListView() {
        Cursor cursor = mDb.getAllRows();

        // get String array of field names of database
        String[] fromFields = new String[]{DBAdapter.COL_KEY_NAME_ID,
                DBAdapter.COL_NAME_VIDEO};

        // Get int array corresponding to each item in a row of the ListView.
        // The items are from the   item_layout.xml   file
        int[] toViewIDs = new int[] {R.id.textViewID, R.id.textViewVideoName};

        // Set up cursor adapter
        SimpleCursorAdapter mCursorAdapter;
        mCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout,
                cursor, fromFields, toViewIDs, 0);

        // R.id.listView refers to "ListView" element in  activity_main.xml
        // with id == listView
        ListView mList = (ListView)findViewById(R.id.listView);
        mList.setAdapter(mCursorAdapter);
    }

    // returns the name of the video selected in listViewItemClick() method
    private String getVideo(long id) {
        String video = "";
        Cursor c = mDb.getRow(id);
        if(c.moveToFirst()) {
            video = c.getString(DBAdapter.COL_VIDEO_PATH);
        }
        c.close();
        return video;
    }

    private  void listViewItemClick() {
        // R.id.listView refers to "ListView" element in  activity_main.xml
        // with id == listView
        ListView mListView = (ListView)findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // "long id" refers to the value of the key field  "_id"
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String video1 = getVideo(id);
                Intent i = new Intent("com.example.ben.videos_personal_3.VideoPlayerActivity");
                i.putExtra("str1", video1);
                startActivity(i);
            }
        });
    }
}
