package com.station.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class EditAntennaList extends ListActivity {	
    public void onCreate(Bundle savedInstanceState) {
//    	requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_antenna_list);
        /*
        setTitle("天线列表");
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.rootblock_default_bg);
        this.getWindow().setBackgroundDrawable(drawable);
        */
        
        
        final String session_id = getIntent().getStringExtra("session_id");
        SQLiteDatabase db = DatabaseHelper.getInstance();
        Cursor cursor = db.query("antenna_info", new String[]{"rowid as _id", "antenna_name", "model","frequency","azimuth"}, "session_id=?", new String[]{session_id}, null, null, null);
        startManagingCursor(cursor);
        setListAdapter(new SimpleCursorAdapter(this, R.layout.antenna_list_item, cursor, new String[]{"antenna_name", "model","frequency","azimuth"}, new int[]{R.id.antenna_name, R.id.extended_info,R.id.frequency,R.id.azimuth}));
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	try{
    	Cursor cursor = (Cursor) getListAdapter().getItem(position);
    	int rowid = cursor.getInt(0);
    	Intent intent = new Intent(this, EditAntenna.class);
    	intent.putExtra("rowid", Integer.toString(rowid));
    	startActivity(intent);
    	}
    	catch (Exception e) {

    	    e.printStackTrace();
    	    System.out.println(e);
    	}
    }
}