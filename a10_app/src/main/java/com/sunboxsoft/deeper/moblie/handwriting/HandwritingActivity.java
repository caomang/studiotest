package com.sunboxsoft.deeper.moblie.handwriting;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sbx.deeper.moblie.contrants.Constants;
import petrochina.ghzy.a10fieldwork.R;

public class HandwritingActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Bitmap mSignBitmap;
	private String signPath;
	private ImageView ivSign;
	private TextView tvSign;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_handwriting_image_main);
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
  		  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
  		 }
//        ivSign =(ImageView)findViewById(R.id.iv_sign);
//        tvSign = (TextView)findViewById(R.id.tv_sign);      
        
//        ivSign.setOnClickListener(signListener);
//        tvSign.setOnClickListener(signListener);
        
        Intent intent = getIntent();
        if(intent!=null){
//        	当前客户的服务点id
        	servercePoint = intent.getStringExtra("servercePoint");
        }
        WritePadDialog writeTabletDialog = new WritePadDialog(
				HandwritingActivity.this, new DialogListener() {
					@Override
					public void refreshActivity(Object object) {							
						
						mSignBitmap = (Bitmap) object;
//						signPath = createFile();//
						signPath =storeImageToSDCARD(mSignBitmap);
						/*BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 15;
						options.inTempStorage = new byte[5 * 1024];
						Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);*/														
//						ivSign.setImageBitmap(mSignBitmap);
//						tvSign.setVisibility(View.GONE);
//						发送广播,显示签名
						 Intent mIntent = new Intent(Constants.GET_KEHU_QIAN_MING);
			                mIntent.putExtra("signBitmap", signPath);
			                //发送广播  
			                sendBroadcast(mIntent);  
						
					}
				});
		writeTabletDialog.show();
        
        
    }
    
	@Override
	protected void onResume() {
		/**
		  * 设置为横屏
		  */
		 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
		  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 }
		 
//		 
		 
		 
		super.onResume();
	}
	private OnClickListener signListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			WritePadDialog writeTabletDialog = new WritePadDialog(
					HandwritingActivity.this, new DialogListener() {
						@Override
						public void refreshActivity(Object object) {							
							
							mSignBitmap = (Bitmap) object;
							signPath = createFile();
							
							/*BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 15;
							options.inTempStorage = new byte[5 * 1024];
							Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);*/														
							ivSign.setImageBitmap(mSignBitmap);
							tvSign.setVisibility(View.GONE);
//							发送广播,显示签名
							 Intent mIntent = new Intent(Constants.GET_KEHU_QIAN_MING);  
				                mIntent.putExtra("signBitmap", signPath);
				                //发送广播  
				                sendBroadcast(mIntent);  
							
						}
					});
			writeTabletDialog.show();
		}
	};
	private String servercePoint;
	
	/**
	 * 创建手写签名文件
	 * 
	 * @return
	 */
	private String sdPath = Environment
			.getExternalStorageDirectory().toString();
	
	
	public String storeImageToSDCARD(Bitmap colorImage) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String time_now = format.format(new Date());
			SimpleDateFormat format1 = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			String time = format1.format(new Date());
			 String imagePath = sdPath + "/" + Constants.sd_cache + "/"
					+ time_now + "/" + Constants.loginName + "/"
					+ servercePoint + "_";
//			 String imagePath = sdPath + "/" + Constants.sd_cache + "/"
//					 + time_now + "/" + Constants.loginName + "/"
//					 + servercePoint + "_" + time+ ".jpg";
			 File mfile = new File(imagePath);
			 
			 if(!mfile.exists()){
				 mfile.mkdirs();
			 }
			 String nameString = time+".jpg";
			 File imagefile = new File(mfile.getAbsolutePath().toString(),nameString);
			 
//		File imagefile = new File(imagePath);
		// if (!imagefile.exists()) {
		try {
			imagefile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imagefile);
			colorImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// } else {
		// }
		return imagefile.getAbsolutePath().toString();
	}
	
	
	private String createFile() {
		ByteArrayOutputStream baos = null;
		String imagePath = null;
		try {
			
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String time_now = format.format(new Date());
				SimpleDateFormat format1 = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				String time = format1.format(new Date());
				 imagePath = sdPath + "/" + Constants.sd_cache + "/"
						+ time_now + "/" + Constants.loginName + "/"
						+ servercePoint + "_" + time+ ".jpg";
			
//			String sign_dir = Environment.getExternalStorageDirectory() + File.separator;			
//			_path = sign_dir + System.currentTimeMillis() + ".jpg";
			baos = new ByteArrayOutputStream();
			mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] photoBytes = baos.toByteArray();
			if (photoBytes != null) {
				new FileOutputStream(new File(imagePath)).write(photoBytes);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imagePath;
	}
}
