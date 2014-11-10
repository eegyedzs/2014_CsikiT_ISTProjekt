package com.example.gallery;

import java.io.File;

import com.example.gallery.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class ImageDialog extends DialogFragment implements
		LoaderCallbacks<Cursor> {

	LayoutInflater mLayoutInflater;
	ImageView mDisplay;
	MyImageView mImgOriginal;
	Bitmap mFaceBitmap;
	Bitmap e;
	private int mFaceWidth = 200;
	private int mFaceHeight = 200;
	Canvas canvas;
	private static final int MAX_FACES = 1;
	int[] fpx;
	int[] fpy;
	int count;

	protected static final int GUIUPDATE_SETFACE = 999;
	protected Handler mHandler = new Handler() {
		// @Override
		public void handleMessage(Message msg) {
			mImgOriginal.invalidate();
			super.handleMessage(msg);
		}
	};
	String mId = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mImgOriginal = new MyImageView(ImageDialog.this.getActivity()
				.getApplicationContext());
		getLoaderManager().initLoader(0, null, this);
	}

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		mLayoutInflater = getActivity().getLayoutInflater();
		View v = mLayoutInflater.inflate(R.layout.image_dialog_layout, null);
		mDisplay = (ImageView) v.findViewById(R.id.imgOriginal);
		builder.setView(v);
		builder.setTitle("Image");
		return builder.create();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Bundle b = getArguments();
		mId = b.getString("image_id");
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		CursorLoader cLoader = new CursorLoader(getActivity(), uri, null,
				"_id=" + mId, null, null);
		return cLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		if (arg1.getCount() == 0)
			return;
		String path = "";

		if (arg1.moveToFirst()) {
			path = arg1.getString(arg1.getColumnIndex("_data"));
			File imgFile = new File(path);
			if (imgFile.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				e = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
				mFaceBitmap = myBitmap.copy(Bitmap.Config.RGB_565, true);
				mFaceWidth = e.getWidth();
				mFaceHeight = e.getHeight();
				canvas = new Canvas(e);
				mDisplay.setImageBitmap(e);
				doLengthyCalc();
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}

	public void setFace() {
		FaceDetector fd;
		FaceDetector.Face[] faces = new FaceDetector.Face[1];
		PointF midpoint = new PointF();
		fpx = null;
		fpy = null;
		count = 0;
		try {
			fd = new FaceDetector(mFaceWidth, mFaceHeight, MAX_FACES);
			count = fd.findFaces(mFaceBitmap, faces);
		} catch (Exception e) {
			return;
		}

		if (count > 0) {
			fpx = new int[count];
			fpy = new int[count];

			for (int i = 0; i < count; i++) {
				try {
					faces[i].getMidPoint(midpoint);
					fpx[i] = (int) midpoint.x;
					fpy[i] = (int) midpoint.y;
				} catch (Exception e) {
				}
			}
		}

		mImgOriginal.setDisplayPoints(fpx, fpy, count, 0);
	}

	private void doLengthyCalc() {
		Thread t = new Thread() {
			Message m = new Message();

			public void run() {
				try {
					setFace();
					m.what = ImageDialog.GUIUPDATE_SETFACE;
					ImageDialog.this.mHandler.sendMessage(m);
					ImageDialog.this.getActivity().runOnUiThread(
							new Runnable() {

								@Override
								public void run() {
									for (int i = 0; i < count; i++) {
										try {
											// Toast.makeText(ImageDialog.this.getActivity(),
											// "alma " + i,
											// Toast.LENGTH_SHORT).show();
											Paint paint = new Paint();
											paint.setColor(Color.GREEN);
											paint.setStyle(Paint.Style.FILL_AND_STROKE);
											paint.setStrokeWidth(10);
											canvas.drawRect(fpx[i] - 40,
													fpy[i] - 40, fpx[i] + 40,
													fpy[i] + 40, paint);
											mDisplay.invalidate();
										} catch (Exception e) {
										}
									}
								}
							});
				} catch (Exception e) {
				}
			}
		};

		t.start();
	}

}