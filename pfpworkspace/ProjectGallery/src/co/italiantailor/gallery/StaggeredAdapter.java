package co.italiantailor.gallery;

import java.io.File;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class StaggeredAdapter extends SimpleCursorAdapter {

	Cursor cursor;
	Context context;

	public StaggeredAdapter(Context context, int layout, Cursor cursor, String[] from,
			int[] to, int flags) {
		super(context, layout, cursor, from, to, flags);

		this.cursor = cursor;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(context);
			convertView = layoutInflator.inflate(R.layout.row_staggered_demo,
					parent);
			holder = new ViewHolder();
			holder.image = (ScaleImageView) convertView
					.findViewById(R.id.imageView1);
			/*holder.imageView = (ScaleImageView) convertView
					.findViewById(R.id.imageView1);
			convertView.setTag(holder);*/
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//cursor.moveToPosition(position);
		//String path = cursor.getString(cursor
		//		.getColumnIndex(MediaStore.Images.Media.DATA));
		// Toast.makeText(context, position + " " + path,
		// Toast.LENGTH_LONG).show();
		//holder.imageView.setImageBitmap(BitmapFactory.decodeFile(path));
		Picasso.with(context).load(new File(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)))).error(R.drawable.ic_launcher).into(holder.image);

		return convertView;
	}

	static class ViewHolder {
		ImageView image;
		ScaleImageView imageView;
	}

}