package co.italiantailor.gallery;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import co.italiantailor.gallery.StaggeredAdapter.ViewHolder;

import com.squareup.picasso.Picasso;

public class StaggeredAdapter2 extends ArrayAdapter<String> {

	Context context;

	public StaggeredAdapter2(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.row_staggered_demo,
					null);
			holder = new ViewHolder();
			//holder.image = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.imageView = (ScaleImageView) convertView .findViewById(R.id.imageView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder = (ViewHolder) convertView.getTag();
		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Picasso.with(context).load(new File(getItem(position))).error(R.drawable.ic_launcher).resize(width/2,100).centerCrop().into(holder.imageView);

		return convertView;
	}

	static class ViewHolder {
		ImageView image;
		ScaleImageView imageView;
	}
}
