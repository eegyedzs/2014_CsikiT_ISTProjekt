package com.example.gallery;

import com.example.gallery.R;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends FragmentActivity  implements LoaderCallbacks<Cursor>{
	
	SimpleCursorAdapter mAdapter;
	
	@Override
	protected void onStart() {		
		super.onStart();
        getSupportLoaderManager().initLoader(0, null, this);
	}
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        mAdapter = new SimpleCursorAdapter(
        								getBaseContext(), 
        								R.layout.gridview, 
        								null, 
        								new String[] { "_data","_id"} , 
        								new int[] { R.id.img},
        								0
        );
        gridView.setAdapter(mAdapter);     
        getSupportLoaderManager().initLoader(0, null, this);
        OnItemClickListener itemClickListener = new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        		Cursor c1 = (Cursor ) arg0.getItemAtPosition(position);
        		String id = c1.getString(c1.getColumnIndex("image_id"));
        		Bundle b = new Bundle();
        		b.putString("image_id", id);
        		ImageDialog img = new ImageDialog();
        		img.setArguments(b);
        		img.show(getSupportFragmentManager(), "IMAGEDIALOG");
        		return ;
        		
        	}
        	
		};
		gridView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;		
		
		return new CursorLoader(this, uri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {		
		mAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub		
	}
}