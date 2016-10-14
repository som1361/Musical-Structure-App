package com.example.somayyeh.musicplayer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Cursor cursor;
    int flag = 0;
    String[] projection;
    ListView v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void sortMusic(View v) {
        switch (v.getId()) {
            case R.id.Tracks:
                projection = new String[]{MediaStore.Audio.Media.DISPLAY_NAME};
                flag = 1;
                //  t.setText("tracks");
                break;
            case R.id.Artists:
                //  playSearchArtist("Blackbird Blackbird");
                //   t.setText(getRealPathFromURI());
                projection = new String[]{MediaStore.Audio.Media.ARTIST};
                flag = 0;
                break;
            case R.id.Albums:
                //    t.setText("Albums");
                projection = new String[]{MediaStore.Audio.Media.ALBUM};
                flag = 2;
                break;
            default:
                throw new RuntimeException("Unknow button ID");
        }

        getRealPathFromURI(projection);
    }

    private void getRealPathFromURI(String projection[]) {
        //Some audio may be explicitly marked as not being music
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        List<String> songs = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String item = cursor.getString(0);
            if (!songs.contains(item)) {

                songs.add(item);
            }
        }
        v = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songs);
        v.setAdapter(adapter1);

        v.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        String val = (String) parent.getItemAtPosition(position);

        if (flag == 0) {
            intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS,
                    MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE);
            intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, val);
            intent.putExtra(SearchManager.QUERY, val);
        } else if (flag == 1) {

            intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS,
                    MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE);
            intent.putExtra(MediaStore.EXTRA_MEDIA_ALBUM, val);
            intent.putExtra(SearchManager.QUERY, val);
        } else {
           intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, "vnd.android.cursor.item/audio");
            intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, val);
            intent.putExtra(SearchManager.QUERY, val);

        }
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);

        }

    }
}