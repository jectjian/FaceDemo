package com.example.facedemo;

import java.io.ByteArrayOutputStream;

import org.json.JSONObject;

import android.graphics.Bitmap;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

public class DetectUtils {
	
	public interface CallBack{
		void success(JSONObject result);
		
		void error(FaceppParseException exception);
	}
	
	public static void detect(final Bitmap bm,final CallBack callBack){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					HttpRequests request = new HttpRequests(MyContanst.APIKEY,MyContanst.APISECRET,true,true);
					
					Bitmap bitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
					
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					
					byte array[] = stream.toByteArray();
					
					PostParameters parameters = new PostParameters();
					parameters.setImg(array);
					
					JSONObject object = request.detectionDetect(parameters);
					if(callBack != null){
						callBack.success(object);
					}
				} catch (FaceppParseException e) {
					e.printStackTrace();
					if(callBack != null){
						callBack.error(e);
					}
				}
			}
		}).start();
	}
}
