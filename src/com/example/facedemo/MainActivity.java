package com.example.facedemo;

import java.io.File;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.FaceBean;
import com.example.bean.ResultBean;
import com.example.facedemo.DetectUtils.CallBack;
import com.facepp.error.FaceppParseException;
import com.google.gson.Gson;

public class MainActivity extends Activity implements OnClickListener {

	private Button mCheck, mSelect_pic, mSelect_camera;
	private ImageView mBaclImg;
	private ProgressBar mProgress;

	private final int SELECT_PIC_CODE = 0x0001;
	private final int SELECT_CAMERA_CODE = 0x0002;
	private File cameraFile;
	private Bitmap bitmap = null;

	private Paint mPaint;
	private boolean isQuest = false;//标记是否已请求成功

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int sign = msg.what;
			if (sign == 1) {
				JSONObject object = (JSONObject) msg.obj;
				ResultBean bean = new Gson().fromJson(object.toString(), ResultBean.class);
				Log.e("----", bean.toString());
				if (bean.getFace().size() == 0) {// 识别图片中无人脸信息
					Toast.makeText(MainActivity.this, "当前图片未识别出人脸信息", Toast.LENGTH_LONG).show();
				} else {// 识别图片中有人脸信息
					drawFace(bean.getFace());
					mBaclImg.setImageBitmap(bitmap);
					isQuest = true;//标记已识别成功
				}
			} else if (sign == 2) {
				Toast.makeText(MainActivity.this, "识别失败", Toast.LENGTH_LONG).show();
			}
			mProgress.setVisibility(View.GONE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPaint = new Paint();

		mBaclImg = (ImageView) findViewById(R.id.main_back_image);
		mCheck = (Button) findViewById(R.id.main_check);
		mSelect_pic = (Button) findViewById(R.id.main_select_pic);
		mSelect_camera = (Button) findViewById(R.id.main_select_camera);
		mProgress = (ProgressBar) findViewById(R.id.main_progress);

		mCheck.setOnClickListener(this);
		mSelect_pic.setOnClickListener(this);
		mSelect_camera.setOnClickListener(this);
	}

	// 根据识别出的数据画人脸
	private void drawFace(List<FaceBean> list) {
		if (list != null && list.size() > 0) {
			Bitmap bm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
			Canvas canvas = new Canvas(bm);
			canvas.drawBitmap(bitmap, 0, 0, null);

			for (int i = 0; i < list.size(); i++) {

				/****************************************识别标识出脸部信息***************************************************/
				// 1.画脸部布局
				float x = list.get(i).getPosition().getCenter().getX() / 100 * bm.getWidth();
				float y = list.get(i).getPosition().getCenter().getY() / 100 * bm.getHeight();
				float w = list.get(i).getPosition().getWidth() / 100 * bm.getWidth();
				float h = list.get(i).getPosition().getHeight() / 100 * bm.getHeight();
				mPaint.setColor(0xffffffff);
				mPaint.setStrokeWidth(3);
				canvas.drawLine(x - w / 2, y - h / 2, x - w / 2, y + h / 2, mPaint);
				canvas.drawLine(x - w / 2, y - h / 2, x + w / 2, y - h / 2, mPaint);
				canvas.drawLine(x + w / 2, y - h / 2, x + w / 2, y + h / 2, mPaint);
				canvas.drawLine(x - w / 2, y + h / 2, x + w / 2, y + h / 2, mPaint);
				
				//2.画眼睛
				float eye_left_x = list.get(i).getPosition().getEye_left().getX() / 100 * bm.getWidth(); 
				float eye_left_y = list.get(i).getPosition().getEye_left().getY() / 100 * bm.getHeight();
				float eye_right_x = list.get(i).getPosition().getEye_right().getX() / 100 * bm.getWidth(); 
				float eye_right_y = list.get(i).getPosition().getEye_right().getY() / 100 * bm.getHeight();
				mPaint.setColor(0xff0000ff);
				mPaint.setStrokeWidth(1);
				canvas.drawLine(eye_left_x - w / 6, eye_left_y - h / 6, eye_left_x - w / 6, eye_left_y + h / 6, mPaint);
				canvas.drawLine(eye_left_x - w / 6, eye_left_y - h / 6, eye_left_x + w / 6, eye_left_y - h / 6, mPaint);
				canvas.drawLine(eye_left_x + w / 6, eye_left_y - h / 6, eye_left_x + w / 6, eye_left_y + h / 6, mPaint);
				canvas.drawLine(eye_left_x - w / 6, eye_left_y + h / 6, eye_left_x + w / 6, eye_left_y + h / 6, mPaint);
				canvas.drawLine(eye_right_x - w / 6, eye_right_y - h / 6, eye_right_x - w / 6, eye_right_y + h / 6, mPaint);
				canvas.drawLine(eye_right_x - w / 6, eye_right_y - h / 6, eye_right_x + w / 6, eye_right_y - h / 6, mPaint);
				canvas.drawLine(eye_right_x + w / 6, eye_right_y - h / 6, eye_right_x + w / 6, eye_right_y + h / 6, mPaint);
				canvas.drawLine(eye_right_x - w / 6, eye_right_y + h / 6, eye_right_x + w / 6, eye_right_y + h / 6, mPaint);
				
				//3.画鼻子
				float nose_x = list.get(i).getPosition().getNose().getX() / 100 * bm.getWidth(); 
				float nose_y = list.get(i).getPosition().getNose().getY() / 100 * bm.getHeight();
				mPaint.setColor(0xffaaaaaa);
				mPaint.setStrokeWidth(1);
				canvas.drawLine(nose_x - w / 6, nose_y - h / 6, nose_x - w / 6, nose_y + h / 6, mPaint);
				canvas.drawLine(nose_x - w / 6, nose_y - h / 6, nose_x + w / 6, nose_y - h / 6, mPaint);
				canvas.drawLine(nose_x + w / 6, nose_y - h / 6, nose_x + w / 6, nose_y + h / 6, mPaint);
				canvas.drawLine(nose_x - w / 6, nose_y + h / 6, nose_x + w / 6, nose_y + h / 6, mPaint);
				
				//4.画嘴巴
//				float mouth_left_x = list.get(i).getPosition().getMouth_left().getX() / 100 * bm.getWidth(); 
//				float mouth_left_y = list.get(i).getPosition().getMouth_left().getY() / 100 * bm.getHeight();
//				float mouth_right_x = list.get(i).getPosition().getMouth_right().getX() / 100 * bm.getWidth(); 
//				float mouth_right_y = list.get(i).getPosition().getMouth_right().getY() / 100 * bm.getHeight();
//				float mouth_x = mouth_right_x - mouth_left_x;
//				float mouth_y = mouth_right_y - mouth_left_y;
//				mPaint.setColor(0xffff3333);
//				mPaint.setStrokeWidth(1);
//				canvas.drawLine(mouth_left_x - w / 6, mouth_left_y - h / 6, mouth_left_x - w / 6, mouth_left_y + h / 6 + mouth_y, mPaint);
//				canvas.drawLine(mouth_left_x - w / 3, mouth_left_y - h / 6, mouth_left_x + w / 3 + mouth_x, mouth_left_y - h / 6, mPaint);
//				canvas.drawLine(mouth_left_x + w / 6, mouth_left_y - h / 6, mouth_left_x + w / 6, mouth_left_y + h / 6 + mouth_y, mPaint);
//				canvas.drawLine(mouth_left_x - w / 3, mouth_left_y + h / 6, mouth_left_x + w / 3 + mouth_x, mouth_left_y + h / 6, mPaint);
				
				/****************************************识别出年龄信息***************************************************/
				Bitmap ageBitmap = showAgeView(list.get(i).getAttribute().getAge().getValue(), "Male".equals(list.get(i).getAttribute().getGender().getValue()));
				int ageWidth = ageBitmap.getWidth();
				int ageHeight = ageBitmap.getHeight();
				if (bm.getWidth() < mBaclImg.getWidth() && bm.getHeight() < mBaclImg.getHeight()) {
					float ratio = (float) Math.max(bm.getWidth() * 1.0 / mBaclImg.getWidth(), bm.getHeight() * 1.0 / mBaclImg.getHeight());
					ageBitmap = Bitmap.createScaledBitmap(ageBitmap, (int) (ageWidth * ratio), (int) (ageHeight * ratio), false);
				}
				canvas.drawBitmap(ageBitmap, x - ageBitmap.getWidth() / 2, y - h / 2 - ageBitmap.getHeight(), null);

				bitmap = bm;
			}
		}
	};

	// 展示显示年龄
	private Bitmap showAgeView(int value, boolean isMale) {
		TextView mAge = (TextView) findViewById(R.id.main_age);
		mAge.setText(value + "");
		if (isMale) {// 男
			mAge.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male), null, null, null);
		} else {// 女
			mAge.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female), null, null, null);
		}
		mAge.setDrawingCacheEnabled(true);// 开启Cache提高绘图速度
		Bitmap ageBitmap = Bitmap.createBitmap(mAge.getDrawingCache());
		mAge.setDrawingCacheEnabled(false);// 关闭Cache

		return ageBitmap;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_check:
			if (bitmap != null && !isQuest) {
				mProgress.setVisibility(View.VISIBLE);
				DetectUtils.detect(bitmap, new CallBack() {

					@Override
					public void success(JSONObject result) {
						Message message = new Message();
						message.what = 1;
						message.obj = result;
						mHandler.sendMessage(message);
					}

					@Override
					public void error(FaceppParseException exception) {
						Message message = new Message();
						message.what = 2;
						mHandler.sendMessage(message);
					}
				});
			} else if(isQuest){
				Toast.makeText(MainActivity.this, "当前图片已识别完成，请重新选择图片继续", Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(MainActivity.this, "请先选择照片", Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.main_select_pic:
			isQuest = false;//重新初始化标记
			selectPicForImage();
			break;

		case R.id.main_select_camera:
			isQuest = false;//重新初始化标记
			selectPicForCamera();
			break;

		default:
			break;
		}
	}

	private void selectPicForCamera() {
		cameraFile = new File(Environment.getExternalStorageDirectory() + "/image/", System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), SELECT_CAMERA_CODE);
	}

	private void selectPicForImage() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, SELECT_PIC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == SELECT_PIC_CODE) {// 从相册选取图片
			if (intent != null) {
				Uri uri = intent.getData();
				if (uri != null) {
					Cursor cursor = getContentResolver().query(uri, null, null, null, null);
					if (cursor != null) {
						cursor.moveToFirst();
						int cursorColumIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
						String picPath = cursor.getString(cursorColumIndex);
						cursor.close();

						bitmap = CutBitmap(picPath);
						mBaclImg.setImageBitmap(bitmap);
					}
				}
			}
		} else if (requestCode == SELECT_CAMERA_CODE) {
			String filePath = ImageUtils.getImageDegreeZero(cameraFile.getAbsolutePath(), Environment.getExternalStorageDirectory() + "/image/");

			bitmap = CutBitmap(filePath);
			mBaclImg.setImageBitmap(bitmap);
		}
	}

	// 裁剪BitMap
	private Bitmap CutBitmap(String picPath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();// 设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picPath, opts);
		// 计算图片缩放比例
		double maxSideLength = Math.max(opts.outWidth * 1.0 / 1024, opts.outHeight * 1.0 / 1024);
		opts.inSampleSize = (int) Math.ceil(maxSideLength);
		// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inJustDecodeBounds = false;
		Bitmap resultBitmap = BitmapFactory.decodeFile(picPath, opts);
		return resultBitmap;
	}
}
