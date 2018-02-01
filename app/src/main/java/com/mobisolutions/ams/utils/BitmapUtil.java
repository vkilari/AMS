package com.mobisolutions.ams.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.View;
import android.view.View.MeasureSpec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapUtil {

	public static Bitmap decodeFileWithSampleSize(String filePath, int maxWidth, int maxHeight) {
		final Options options = new Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);

	    options.inSampleSize = calculateInSampleSize(options, 1600, 1600);
	    options.inPurgeable = true;

	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(filePath, options);

	}

	public static int readBitmapOrientation(String fileName) {
		try {
			ExifInterface inter = new ExifInterface(fileName);
			String orientationAttr = inter.getAttribute(ExifInterface.TAG_ORIENTATION);
			int orientation = Integer.parseInt(orientationAttr);
			return orientation;
		} catch (Exception e) {
			return 0;
		}
	}

	private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {

		int inSampleSize = 2;
		int targetWidth = options.outWidth / inSampleSize;
		while (targetWidth > reqWidth) {
			targetWidth = options.outWidth / inSampleSize;
			inSampleSize = inSampleSize * 2;
		}

	    return inSampleSize;
	}
	/**
	 * Crop the source image if it exceeds one of the max dimensions.
	 *
	 * @param src
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap cropBitmapIfExceeds(Bitmap src, int maxWidth, int maxHeight) {
		if (src.getWidth() > maxWidth || src.getHeight() > maxHeight) {
			int croppedWidth = Math.min(maxWidth, src.getWidth());
			int croppedHeight = Math.min(maxHeight, src.getHeight());

			int x = (src.getWidth() - croppedWidth) / 2;
			int y = (src.getHeight() - croppedHeight) / 2;

			Bitmap bitmap = Bitmap.createBitmap(src, x, y, croppedWidth, croppedHeight);
			return bitmap;
		}
		return src;
	}

	/**
	 * Rescales image to touch from inside.
	 *
	 * @param src
	 *
	 * @param maxWidth
	 * @param maxHeight
	 * @param filter
	 * @param shrinkOnly
	 * @return
	 */
	public static Bitmap scaleBitmapToTouchFromInside(Bitmap src, int maxWidth, int maxHeight, boolean filter, boolean shrinkOnly) {
		// proportionally scale image to at least be contained in one dimension
		if (!shrinkOnly || (maxWidth < src.getWidth() && maxHeight < src.getHeight())) {
			float sx = maxWidth / (float) src.getWidth();
			float sy = maxHeight / (float) src.getHeight();
			float scale = Math.max(sx, sy);
			int width = Math.round(scale * src.getWidth());
			int height = Math.round(scale * src.getHeight());

			Bitmap bitmap = Bitmap.createScaledBitmap(src, width, height, filter);
			return bitmap;
		}
		return src;
	}

	public static Bitmap scaleBitmapToCropFromInside(Bitmap src, int maxWidth, int maxHeight, boolean filter, boolean shrinkOnly) {

		Bitmap scaled = scaleBitmapToTouchFromInside(src, maxWidth, maxHeight, filter, shrinkOnly);

		//Bitmap crpopped = cropBitmapIfExceeds(scaled, maxWidth, maxHeight);

		return scaled;
	}

	public static Bitmap cropBitmapToSameAspect(Bitmap src, int width, int height) {
		float aspect = width / (float) height;
		float srcAspect = src.getWidth() / (float) src.getHeight();

		if (aspect != srcAspect) {
			int maxWidth = 0;
			int maxHeight = 0;

			if (aspect > srcAspect) {
				maxWidth = src.getWidth();
				maxHeight = (int) Math.max(src.getWidth() / aspect, height);
			} else {
				maxWidth = (int) Math.max(src.getHeight() * aspect, width);
				maxHeight = src.getHeight();
			}

			Bitmap bitmap = cropBitmapIfExceeds(src, maxWidth, maxHeight);
			return bitmap;
		}
		return src;
	}

	public static Bitmap decodeWithLargestSampleToTouchFromInside(String pathName, int maxWidth, int maxHeight) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);

		options.inSampleSize = 1;
		if (options.outWidth > maxWidth || options.outHeight > maxHeight) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round(options.outHeight / (float) maxHeight);
			final int widthRatio = Math.round(options.outWidth / (float) maxWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			options.inSampleSize = Math.min(heightRatio, widthRatio);
		}

		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
		return bitmap;
	}

	public static Bitmap rotateToMatchOrientation(Bitmap bitmap, int orientation) {
		if (orientation == 0) {
			return bitmap;
		}

		Matrix matrix = new Matrix();
		matrix.setRotate(orientation);

		Bitmap possiblyNewBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return possiblyNewBitmap;
	}

	public static Bitmap decodeBitmapToFitScreen(String filePath, int screenWidth, int screenHeight, int orientation) {

		int width = screenWidth;
		int height = screenHeight;
		if ((orientation / 90) % 2 != 0) {
			width = screenHeight;
			height = screenWidth;
		}

		Bitmap bitmap = BitmapUtil.decodeWithLargestSampleToTouchFromInside(filePath, width, height);

		Bitmap src = bitmap;
		bitmap = BitmapUtil.cropBitmapToSameAspect(src, width, height);
		if (bitmap != src) {
			src.recycle();
			src = null;
			System.gc();
		}

		src = bitmap;
		bitmap = BitmapUtil.scaleBitmapToTouchFromInside(src, width, height, true, true);
		if (bitmap != src) {
			src.recycle();
			src = null;
			System.gc();
		}

		src = bitmap;
		bitmap = BitmapUtil.cropBitmapIfExceeds(src, width, height);
		if (bitmap != src) {
			src.recycle();
			src = null;
			System.gc();
		}

		if (orientation != 0) {
			src = bitmap;
			bitmap = BitmapUtil.rotateToMatchOrientation(src, orientation);
			if (bitmap != src) {
				src.recycle();
				src = null;
				System.gc();
			}
		}

		return bitmap;
	}

	public static byte[] save(Bitmap bitmap, CompressFormat format, int quality) throws IOException {
		if(bitmap == null) {
			return null;
		}

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			bitmap.compress(format, quality, outStream);
		} finally {
			outStream.close();
		}
		return outStream.toByteArray();
	}

	public static Bitmap loadBitmapFromView(View v, int width, int height) {

		int specWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		int specHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

		v.measure(specWidth, specHeight);
		//v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
	    Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
	    Canvas c = new Canvas(b);

	    v.draw(c);
	    return b;
	}



	public static Bitmap getBitmap(String image_link)
	{
		URL website;
		int req_width =600;
		int req_height=750;

		try {
			website = new URL(image_link);
			HttpURLConnection connection = (HttpURLConnection) website.openConnection();
			InputStream is = connection.getInputStream();

			if(req_width == 0)
			{
				return BitmapFactory.decodeStream(is);
			}


			final Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, options);
			is.close();


			connection = (HttpURLConnection) website.openConnection();
			is = connection.getInputStream();
			options.inSampleSize = calculateInSampleSize(options, req_width, req_height);
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(is, null , options);

		} catch (Exception  e) {
			return null;
		}
	}

	/*public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}*/

}
