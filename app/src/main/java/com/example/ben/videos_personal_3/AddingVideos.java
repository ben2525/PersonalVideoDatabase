package com.example.ben.videos_personal_3;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class AddingVideos extends Activity{

    Cursor mcursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_videos);

        populateAdditionalVideosListView();
        contentListItemClicked();
    }

    //Get a cursor to a listing of all videos on internal storage using a content provider
    //Call this function in     populateAdditionalVideosListView()
    private Cursor getVideos() {
        // Set query parameters
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[] {MediaStore.MediaColumns._ID,
                                            MediaStore.MediaColumns.TITLE};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        // Run and return query
        return getContentResolver().query(uri, projection, selection, selectionArgs,
                sortOrder);
    }

    public void populateAdditionalVideosListView() {
        mcursor = getVideos();

        // get String array of field names of content provider
        String[] fromFields = new String[]{MediaStore.MediaColumns._ID,
                                           MediaStore.MediaColumns.TITLE};

        // Get int array corresponding to each item in a row of the ListView.
        // The items are from the file      item_layout_adding_videos.xml
        int[] toViewIDs = new int[] {R.id.textViewContentID,
                                     R.id.textViewContentVideoName};

        // Set up cursor adapter
        SimpleCursorAdapter mCursorAdapter;
        mCursorAdapter = new SimpleCursorAdapter(getBaseContext(),
                R.layout.item_layout_adding_videos,
                mcursor, fromFields, toViewIDs, 0);

        // R.id.listVideosView refers to "ListView" element in  activity_adding_videos.xml
        // with id == listVideosView
        ListView mList = (ListView)findViewById(R.id.listVideosView);
        mList.setAdapter(mCursorAdapter);
    }


    private void contentListItemClicked() {
        // R.id.listView refers to "ListView" element in  activity_main.xml
        // with id == listVideosView
        ListView mListView = (ListView)findViewById(R.id.listVideosView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // "long id" refers to the value of the key field  "_id"
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Long vidID = id;
                String vidPath = "";
                String vidName = "";

                // Get one row of info (from item clicked in list) based on argument   "vidID"
                Uri singleUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, vidID);
                String[] pathProjection = new String[] {
                        MediaStore.MediaColumns._ID,
                        MediaStore.MediaColumns.TITLE,
                        MediaStore.MediaColumns.DATA};
                Cursor pathCursor = getContentResolver().query(singleUri, pathProjection, null, null, null);
                if(pathCursor.moveToFirst()){
                    vidPath = pathCursor.getString(pathCursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                    vidName = pathCursor.getString(pathCursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
                }

                // Insert chosen item into database for use in    MainActivity
                MainActivity.mDb.insertRow(vidName, vidPath);

                Toast.makeText(getApplicationContext(),
                        vidName + "\n added to view list",
                        Toast.LENGTH_LONG).show();

                pathCursor.close();
            }
        });
    }
}
