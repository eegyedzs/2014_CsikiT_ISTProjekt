package co.italiantailor.gallery;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;

import com.origamilabs.library.views.StaggeredGridView;
import com.squareup.picasso.Picasso;

/**
 * 
 * This will not work so great since the heights of the imageViews 
 * are calculated on the iamgeLoader callback ruining the offsets. To fix this try to get 
 * the (intrinsic) image width and height and set the views height manually. I will
 * look into a fix once I find extra time.
 * 
 * @author Maurycy Wojtowicz
 *
 */
public class MainActivity extends Activity {

	final Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StaggeredGridView gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);
		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		gridView.setItemMargin(margin);
		gridView.setPadding(margin, 0, margin, 0);
		//ListView list = (ListView) this.findViewById(R.id.list);

        //String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = this.getContentResolver().query(imageUri, null,
				null, null, MediaStore.Audio.Media.TITLE);
		//StaggeredAdapter adapter = new StaggeredAdapter(MainActivity.this, R.id.imageView1, cursor);
		String[] from = { MediaStore.MediaColumns.DATA };
		int[] to = { R.id.imageView1 };
		/*StaggeredAdapter adapter = new StaggeredAdapter(this,
				R.layout.row_staggered_demo, cursor, from, to,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.row_staggered_demo, cursor, from, to,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);*/
		ArrayList<String> pathList = new ArrayList<String>();
		cursor.moveToFirst();
		//for(int i=0;i<10;++i){
			while(!cursor.isAfterLast()) {
		     pathList.add(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))); //add the item
		     cursor.moveToNext();
		}
		String[] path = new String[pathList.size()];
		path = pathList.toArray(path);
		Log.e("FACEBOOK", path.toString());
		StaggeredAdapter2 adapter = new StaggeredAdapter2(MainActivity.this, R.id.imageView1, path);
		gridView.setAdapter(adapter);
		//list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		/*if (cursor.moveToNext()) {
			ImageView image = (ImageView) this.findViewById(R.id.image);
			Log.e("FACEBOOK", "" + cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
			Picasso.with(this).load(new File(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)))).error(R.drawable.ic_launcher).into(image);
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
