package cn.sbx.deeper.moblie.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.photo.zoom.PhotoView;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.util.ImageUtils;
import cn.sbx.deeper.moblie.view.ViewPagerFixed;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 这个是用于进行图片浏览时的界面
 *
 * @author king
 * @version 2014年10月18日  下午11:47:53
 * @QQ:595163260
 */
public class SeePicture_PageActivity extends Activity {
    private String sdPath = android.os.Environment
            .getExternalStorageDirectory().toString();
    //数据库的链接
    private Connection conne;

    private Intent intent;
    // 返回按钮
    private Button back_bt;
    //删除按钮
    private Button del_bt;
    //顶部显示预览图片位置的textview
    private TextView positionTextView;
    //获取前一个activity传过来的position
    private int position;
    //当前的位置
    private int location = 0;

    private ArrayList<View> listViews = null;
    private ViewPagerFixed pager;
    private MyPageAdapter adapter;

    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    public List<String> drr = new ArrayList<String>();
    public List<String> del = new ArrayList<String>();

    private Context mContext;


    RelativeLayout photo_relativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.isDeletePicture = false;
        setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
        mContext = this;
        back_bt = (Button) findViewById(R.id.gallery_back);
        del_bt = (Button) findViewById(R.id.gallery_del);

        back_bt.setOnClickListener(new BackListener());
        del_bt.setOnClickListener(new DelListener());
        intent = getIntent();
        taskId = intent.getStringExtra("taskId");
        schedId = intent.getStringExtra("SchedID");
        Bundle bundle = intent.getExtras();
        position = Integer.parseInt(intent.getStringExtra("position"));

//		isShowOkBt();
        // 为发送按钮设置文字
        pager = (ViewPagerFixed) findViewById(R.id.viewPager);
        pager.setOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < Constants.imgList.size(); i++) {
            initListViews(Constants.imgList.get(i).getUrl());
        }

        adapter = new MyPageAdapter(listViews);
        pager.setAdapter(adapter);
        pager.setPageMargin((int) getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
        int id = intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Constants.isDeletePicture = false;
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private String taskId;
    private String schedId;

    private void initListViews(String url) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        PhotoView img = new PhotoView(this);
        img.setBackgroundColor(0xff000000);
        Bitmap btp = ImageUtils.getBitmapImage(sdPath + url);
        img.setImageBitmap(btp);
//		img.setImageBitmap(BitmapFactory.decodeFile(sdPath+url));
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    // 返回按钮添加的监听器
    private class BackListener implements OnClickListener {

        public void onClick(View v) {
            finish();
        }
    }

    // 删除按钮添加的监听器
    private class DelListener implements OnClickListener {

        public void onClick(View v) {
            if (listViews.size() == 1) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SeePicture_PageActivity.this)
                        .setMessage("确认删除吗？").setTitle("提示")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//				删除数据库中的记录
                                try {
                                    deletePhoto(Constants.imgList.get(location).getUrl(), taskId, schedId);
                                    boolean b = deleteUriForPhoto(Constants.imgList.get(location).getUrl());
                                    if (b) {
                                        Constants.imgList.clear();
                                        Constants.isDeletePicture = true;
                                    } else {
                                        Toast.makeText(SeePicture_PageActivity.this, "图片删除失败", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                dialog.dismiss();
                                SeePicture_PageActivity.this.finish();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();


//				Bimp.max = 0;
//				send_bt.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
//				Intent intent = new Intent("data.broadcast.action");
//                sendBroadcast(intent);

            } else {


                AlertDialog.Builder builder = new AlertDialog.Builder(SeePicture_PageActivity.this)
                        .setMessage("确认删除吗？").setTitle("提示")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//				删除数据库中的记录
                                try {
                                    deletePhoto(Constants.imgList.get(location).getUrl(), taskId, schedId);
                                    boolean b = deleteUriForPhoto(Constants.imgList.get(location).getUrl());
                                    if (b) {
                                        Constants.imgList.remove(location);
                                        pager.removeAllViews();
                                        listViews.remove(location);
                                        adapter.setListViews(listViews);
                                        adapter.notifyDataSetChanged();
                                        Constants.isDeletePicture = true;
                                    } else {
                                        Toast.makeText(SeePicture_PageActivity.this, "图片删除失败", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext, "图片未能删除", Toast.LENGTH_SHORT).show();
                                }

                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        }

    }

    private void deletePhoto(String url, String taskId, String schedId) throws SQLException {

        Connection conne = SQLiteData.openOrCreateDatabase();
        Statement state = conne.createStatement();

        String str = "delete from perFile_aj where cmSchedId = '" + schedId + "' and accountId = '" + taskId + "' and cmScFileRoute = '" + url + "'";
        state.executeUpdate(str);
        state.close();
        conne.close();
    }

    // 完成按钮的监听
    private class GallerySendListener implements OnClickListener {
        public void onClick(View v) {
            finish();
            intent.setClass(mContext, MainActivity.class);
            startActivity(intent);
        }

    }

    private boolean deleteUriForPhoto(String url) {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+url);
        boolean delete = file.delete();
//        if (!TextUtils.isEmpty(url)) {
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            File file = new File(url);
//            Uri uri = Uri.fromFile(file);
//            intent.setData(uri);
//            SeePicture_PageActivity.this.sendBroadcast(intent);
//            boolean delete = file.delete();
//            return delete;
//        }

        return delete;
    }


/*	public void isShowOkBt() {
        if (Bimp.tempSelectBitmap.size() > 0) {
			send_bt.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
			send_bt.setPressed(true);
			send_bt.setClickable(true);
			send_bt.setTextColor(Color.WHITE);
		} else {
			send_bt.setPressed(false);
			send_bt.setClickable(false);
			send_bt.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}


*/

    /**
     * 监听返回按钮
     */
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(position==1){
				this.finish();
				intent.setClass(SeePicture_PageActivity.this, AlbumActivity.class);
				startActivity(intent);
			}else if(position==2){
				this.finish();
				intent.setClass(SeePicture_PageActivity.this, ShowAllPhoto.class);
				startActivity(intent);
			}
		}
		return true;
	}*/


    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}
