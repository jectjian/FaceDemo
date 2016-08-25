package com.example.facedemo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

/**
 * @author :lvjain
 * @describe ：获取Image工具类
 * @date ：2016-4-15 下午4:46:38
 */
public class ImageUtils {
	public static String getImageDegreeZero(String path, String dir) {

		int degree = readPictureDegree(path);

		if (degree != 0) {

			Bitmap cameraBitmap = rotaingImageView(degree,
					BitmapFactory.decodeFile(path));

			String name = path;

			try {
				saveBitmapFile(dir, path, cameraBitmap);
				path = dir + name;
			} catch (Exception e) {

			}

		}

		return path;

	}

	public static void saveBitmapFile(String dir, String name, Bitmap bitmap)
			throws Exception {

		File file = new File(dir);

		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(file, name);

		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file));
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		bos.flush();
		bos.close();
	}

	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}
