package com.sunboxsoft.deeper.moblie.handwriting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.domian.CmMrComm_Dic;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.PhotoAudio;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

public class AddImageActivity extends Activity {
    static final int BACKGROUND_COLOR = Color.WHITE;
    static final int BRUSH_COLOR = Color.BLACK;
    protected static final int SUCCESS_SAVE = 10;
    protected static final int UN_SUCCESS_SAVE = 20;
    protected static final int CHECK_SERVICE = 30;

    private Paint mPaint;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private List<Bitmap> mBitmaps = new ArrayList<Bitmap>();
    private String fuWu_code = "";
    private ArrayList<PhotoAudio> imgListForRec = new ArrayList<PhotoAudio>();
    //    private SharedPreferences sharedPreferencesForUrl;
//    SharedPreferences.Editor editor;
//    private SharedPreferences.Editor booleanEdit;
    private boolean showurlbool = false;

    private String sdPath = android.os.Environment
            .getExternalStorageDirectory().toString();
    private String urLforPic = "";
    private LinearLayout ll_contener;
    private Button bt_back,btn_save;
    private boolean alreadySavePic=false;
    private FrameLayout frameLayout;
    private ProgressHUD progressHUD;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS_SAVE:
                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                    alreadySavePic=false;
                    break;
                case UN_SUCCESS_SAVE:
                    Toast.makeText(getApplicationContext(), "图片保存失败", Toast.LENGTH_SHORT).show();
                    break;
                case CHECK_SERVICE:
                    Toast.makeText(getApplicationContext(), "请选择服务评价", Toast.LENGTH_SHORT).show();
                    break;
                case 100:
                    for (int i = 0; i < imgListForRec.size(); i++) {
                        if (imgListForRec.get(i).getUrl().contains(custInfo.accountId)) {
                            showurlbool = true;
                            urLforPic = imgListForRec.get(i).getUrl();
                            break;

                        }

                    }
                    if (showurlbool) {
                        ll_contener.setVisibility(View.GONE);
                        btn_save.setVisibility(View.GONE);
                        alreadySavePic=false;

//            Bitmap bitmapImage = ImageUtils.getBitmapImage();


//            int bitmapImageWidth = bitmapImage.getWidth();
//            int bitmapImageHeight = bitmapImage.getHeight();

                        ImageView imageView = new ImageView(AddImageActivity.this);
                        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//            params.setMargins(
//                    0,
//                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(this, 5),
//                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(this, 5),
//                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(this, 5));
//            params.height = cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(this, LayoutParams.MATCH_PARENT);
//            params.width = cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(this, LayoutParams.MATCH_PARENT);
//            params.topMargin = cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(this, 3);
//            params.rightMargin = cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(this, 5);

                        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        imageView.setScaleType(ImageView.ScaleType.CENTER);


                        frameLayout.setBackgroundColor(getResources().getColor(R.color.white));
//            imageView.setLayoutParams(mLayoutParams);
//            imageView.setImageBitmap(bitmapImage);
//            frameLayout.addView(imageView);
//            ImageLoader.getInstance().displayImage(sdPath
//                    + urLforPic,imageView);
//            Bitmap bitmap = BitmapFactory.decodeFile(sdPath + urLforPic);
//            imageView.setImageBitmap(bitmap);
                        DisplayImageOptions options;
                        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher).cacheInMemory(true)
                                .cacheOnDisc(true).imageScaleType(ImageScaleType.NONE).build();

                        ImageLoader.getInstance().displayImage("file:///"+sdPath+urLforPic,imageView, options);
                        frameLayout.addView(imageView,mLayoutParams);


                    } else {
                        alreadySavePic=true;
                        ll_contener.setVisibility(View.VISIBLE);
                        btn_save.setVisibility(View.VISIBLE);
                        mView = new PaintView(AddImageActivity.this);
//                        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);
                        frameLayout.addView(mView);
                        mView.requestFocus();
                        // 添加一个删除按钮
                        ImageView imageView = new ImageView(AddImageActivity.this);
                        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                -1, -2);
                        params.setMargins(
                                0,
                                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 5),
                                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 5),
                                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 5));
                        params.height = cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 40);
                        params.width = cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 40);
                        params.topMargin = cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 3);
                        params.rightMargin = cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 5);
                        imageView.setLayoutParams(params);
                        imageView.setBackgroundResource(R.drawable.chahao);
                        frameLayout.addView(imageView);
                        // 获取custinfo


                        // 获取服务评价数据字典
                        cmMrComm_list = getCommInfodata();



                        btn_save.setOnClickListener(listener_saveinage);
                        imageView.setOnClickListener(listener_deleteimage);


                        final RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radiogroup);

                        // 创建服务评价
                        final LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(
                                -1, -2);
                        ll_params.setMargins(
                                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 5),
                                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 5),
                                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 5),
                                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(AddImageActivity.this, 5));
                        // 动态创建服务评价单选框
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 获取服务评价数据字典
                                cmMrComm_list = getCommInfodata();

                                for (int a = 0; a < cmMrComm_list.size(); a++) {
                                    RadioButton radioButton = new RadioButton(
                                            AddImageActivity.this);
                                    // LinearLayout.LayoutParams linearParams =
                                    // (LinearLayout.LayoutParams)
                                    // radioButton.getLayoutParams();
                                    // // 取radiobutton控件radiobutton当前的布局参数
                                    // linearParams.height = 365; // 当控件的高强制设成365象素
                                    // aaa.setLayoutParams(linearParams); //
                                    // 使设置好的布局参数应用到控件radiobutton
                                    radioButton.setLayoutParams(ll_params);
                                    radioButton.setTextAppearance(getApplicationContext(),
                                            R.style.anjian_style_qianming);
                                    radioButton.setText(cmMrComm_list.get(a).cmMrCommDescr);
                                    radioButton.setTextSize(16);
                                    radioButton.setPadding(cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getApplicationContext(), 40), 0,
                                            cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                                    getApplicationContext(), 30), 0);
                                    radiogroup.addView(radioButton,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                }
                                radiogroup
                                        .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                            @Override
                                            public void onCheckedChanged(RadioGroup group,
                                                                         int checkedId) {
                                                RadioButton childAt = (RadioButton) radiogroup
                                                        .findViewById(checkedId);
                                                // 获取到所选选项的编码
                                                for (int i = 0; i < cmMrComm_list.size(); i++) {
                                                    if (cmMrComm_list.get(i).getCmMrCommDescr()
                                                            .equals(childAt.getText())) {
                                                        fuWu_code = cmMrComm_list.get(i)
                                                                .getCmMrCommCd();
                                                    }
                                                }
                                            }
                                        });
                            }
                        });
                    }

                    try{
                        if(progressHUD!=null){
                            progressHUD.dismiss();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    break;


                default:
                    break;
            }

        }

        ;
    };


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_anjian_qianming);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
//        sharedPreferencesForUrl = getSharedPreferences("", MODE_PRIVATE);
        ll_contener=(LinearLayout)findViewById(R.id.ll_contener);

        bt_back = (Button) findViewById(R.id.bt_back);
        btn_save = (Button) findViewById(R.id.btn_save);
        bt_back.setOnClickListener(listener_back);

        Intent intent = getIntent();
        if (intent != null) {
            taskId = intent.getStringExtra("taskId");
            custInfo = (CustInfo_AnJian) intent
                    .getSerializableExtra("custInfo");
        }
        progressHUD = AlertUtils.showDialog1(AddImageActivity.this, null, null, true);
        progressHUD.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    selectPicture(taskId, custInfo.getCmSchedId());
                } catch (SQLException e) {
                    e.printStackTrace();
//            Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();

//        String urLforPic = sharedPreferencesForUrl.getString("URLforPic", "");
//        for (int i = 0; i < imgListForRec.size(); i++) {
//            if (imgListForRec.get(i).getUrl().equals(urLforPic) && urLforPic != "") {
//
//            }
//
//        }

        frameLayout = (FrameLayout) findViewById(R.id.tablet_view);
        frameLayout.setBackgroundColor(getResources().getColor(R.color.white));

//        showurlbool = sharedPreferencesForUrl.getBoolean("SHOWURLBOOL", false);




        // p = getWindow().getAttributes();//设置窗体属性 如 对话框 ,activity 窗体等
        // p.height = width;// (int) (d.getHeight() * 0.4); //高度设置为屏幕的0.4
        // p.width = height;// (int) (d.getWidth() * 0.6); //宽度设置为屏幕的0.6
        // getWindow().setAttributes(p); // 设置生效

    }

    //查询数据库中有没有图片

    private void selectPicture(String taskid2, String cmSchedId)
            throws SQLException {
        String sql_for_pic="select cmScFileRoute from perFile_aj where cmSchedId = '"
                + cmSchedId + "' and accountId = '" + taskid2 + "'";
        DatabaseHelper conne=new DatabaseHelper(AddImageActivity.this);
//        Connection conne = new DatabaseHelper(AddImageActivity.this);
        SQLiteDatabase ps1 = conne.getWritableDatabase();
        Cursor cursor = ps1.rawQuery(sql_for_pic, null);
        if(cursor.moveToFirst()){
            PhotoAudio photoAudio1 = new PhotoAudio();
            photoAudio1.setUrl(cursor.getString(0));
            imgListForRec.add(photoAudio1);
            while (cursor.moveToNext()) {
                PhotoAudio photoAudio = new PhotoAudio();
                photoAudio.setUrl(cursor.getString(0));
                imgListForRec.add(photoAudio);
            }
        }

        Message msg = Message.obtain();
        msg.what=100;
        mHandler.sendMessage(msg);

        cursor.close();
        ps1.close();
    }

    //获取本地是否有图片的boolean值
//    public void setSharPreBool(Boolean logic){
//        booleanEdit = sharedPreferencesForUrl.edit();
//        booleanEdit.putBoolean("SHOWURLBOOL",logic);
//        booleanEdit.commit();
//    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    // 返回
    private OnClickListener listener_back = new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    // 删除图片
    private OnClickListener listener_deleteimage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mView.clear();
        }
    };

    // 将图片bmp 保存为图片
    private OnClickListener listener_saveinage = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if(alreadySavePic){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 获取到bmp
                        Bitmap mSignBitmap = mView.getCachebBitmap();

                        try {
                            PhotoAudio photoAudio = createFile(mSignBitmap);
                            // 将服务评价  图片 信息 保存到数据库,
                            if (!TextUtils.isEmpty(fuWu_code)) {
                                try {
                                    saveData(taskId, photoAudio);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
//							Toast.makeText(getApplicationContext(), "保存成功", 0).show();
                                mHandler.sendEmptyMessage(SUCCESS_SAVE);
                            } else {
//							Toast.makeText(getApplicationContext(), "请选择服务评价", 0).show();
                                mHandler.sendEmptyMessage(CHECK_SERVICE);
                            }
                        } catch (FileNotFoundException e) {
//						Toast.makeText(getApplicationContext(), "图片保存失败", 1).show();
                            mHandler.sendEmptyMessage(UN_SUCCESS_SAVE);
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }else {
                Toast.makeText(AddImageActivity.this,"签名已经保存",Toast.LENGTH_SHORT).show();
            }


        }
    };
    private LayoutParams p;
    private PaintView mView;
    private int width;
    private int height;

    /**
     * This view implements the drawing canvas.
     * <p>
     * It handles all of the input events and drawing functions.
     */
    private Bitmap cachebBitmap;
    private String servercePoint;
    private ArrayList<CmMrComm_Dic> cmMrComm_list;
    private CustInfo_AnJian custInfo;
    private String taskId;
    private String time_now;


    class PaintView extends View {
        private Paint paint;
        private Canvas cacheCanvas;
        //
        private Path path;

        public Bitmap getCachebBitmap() {
            return cachebBitmap;
        }

        public PaintView(Context context) {
            super(context);
            init();
        }

        private void init() {
            paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(6);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            path = new Path();
            /*
             * cachebBitmap = Bitmap.createBitmap(width,
			 * height,Config.ARGB_8888); // cachebBitmap =
			 * Bitmap.createBitmap(p.width, (int) (p.height *
			 * 0.8),Config.ARGB_8888); cacheCanvas = new Canvas(cachebBitmap);
			 * cacheCanvas.drawColor(Color.WHITE);
			 */
        }

        public void clear() {
            if (cacheCanvas != null) {
                paint.setColor(BACKGROUND_COLOR);
                cacheCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));//防锯齿
                cacheCanvas.drawPaint(paint);
                paint.setColor(Color.BLACK);
                cacheCanvas.drawColor(Color.WHITE);
                invalidate();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // canvas.drawColor(BRUSH_COLOR);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));//防锯齿
            canvas.drawBitmap(cachebBitmap, 0, 0, null);
            canvas.drawPath(path, paint);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {

            int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
            int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
            if (curW >= w && curH >= h) {
                return;
            }

            if (curW < w)
                curW = w;
            if (curH < h)
                curH = h;

            Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
                    Bitmap.Config.ARGB_8888);
            Canvas newCanvas = new Canvas();
            newCanvas.setBitmap(newBitmap);
            if (cachebBitmap != null) {
                newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
            }
            cachebBitmap = newBitmap;
            cacheCanvas = newCanvas;
            cacheCanvas.drawColor(Color.WHITE);
            cacheCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));//防锯齿
        }

        private float cur_x, cur_y;

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    cur_x = x;
                    cur_y = y;
                    path.moveTo(cur_x, cur_y);
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    path.quadTo(cur_x, cur_y, x, y);
                    cur_x = x;
                    cur_y = y;
                    break;
                }

                case MotionEvent.ACTION_UP: {
                    cacheCanvas.drawPath(path, paint);
                    path.reset();
                    break;
                }
            }
            invalidate();

            return true;
        }
    }

    /**
     * 创建手写签名文件
     *
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    private PhotoAudio createFile(Bitmap mSignBitmap) throws FileNotFoundException,
            IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        time_now = format.format(new Date());
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format1.format(new Date());
        ByteArrayOutputStream baos = null;
        String _path = null;
        // try {
        String sign_dir = Environment.getExternalStorageDirectory()
                + File.separator;
        _path = sign_dir + Constants.sd_cache + "/" + time_now + "/"
                + Constants.loginName + "/" + custInfo.getServicePointId() + "_" + time
                + custInfo.accountId + ".jpg";// + custInfo.cmMrAddress + custInfo.cmMrBuilding

//        editor = sharedPreferencesForUrl.edit();
//        editor.putString("URLforPic", _path);
//        editor.commit();
//        setSharPreBool(true);


        File imageFile = new File(_path);
        if (!imageFile.getParentFile().exists())
            imageFile.getParentFile().mkdirs();

        PhotoAudio photoAudio = new PhotoAudio();
        String path = imageFile.getPath();// 绝对路径
        String[] split = path.split("sc/");
        photoAudio.setCmScFileRoute("/sc/" + split[1]);// 相对路径
        photoAudio.setUrl("/sc/" + split[1]);// 设置图片路径,相对路径
        String name = imageFile.getName();
        long length = imageFile.length();// 图片大小
        int height = mSignBitmap.getHeight();// 图片 高度 :像素
        int width = mSignBitmap.getWidth();
        photoAudio.setCmScFileName(name);
        photoAudio.setCmScFileForm("jpg");
        photoAudio.setCmScBusiType("SIG");// 业务类型 来自数据字典,
        photoAudio.setCmScFileSize(String.valueOf(length));// 字节数

        Date date = null;
        try {
            date = format1.parse(time);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        photoAudio.setCmScFileDttm(new SimpleDateFormat(
                "yyyy-MM-dd-HH.mm.ss").format(date));// 时间
        photoAudio.setCmScFileVar1(height + "");
        photoAudio.setCmScFileVar2(width + "");


        baos = new ByteArrayOutputStream();
        mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] photoBytes = baos.toByteArray();
        if (photoBytes != null) {
            new FileOutputStream(imageFile).write(photoBytes);
        }
        // } catch (IOException e) {
        // e.printStackTrace();
        // } finally {
        try {
            if (baos != null)
                baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // }
        return photoAudio;
    }

    // 服务评价字典
    public ArrayList<CmMrComm_Dic> getCommInfodata() {
        String selectDic = "select * from dictionaries where parentID = 'cmMrComm' ";
        Cursor DicSet = null;
        CmMrComm_Dic cmMrComm = null;
        ArrayList<CmMrComm_Dic> cmMrComm_list = new ArrayList<CmMrComm_Dic>();
        DatabaseHelper conne = null;
        SQLiteDatabase state=null;
        try {
            conne=new DatabaseHelper(AddImageActivity.this);
            state = conne.getWritableDatabase();

            DicSet = state.rawQuery(selectDic,null);
            if (DicSet.moveToFirst()){

//                for (int i = 0; i < DicSet.getCount(); i++) {
//                    DicSet.move(i);
//                    cmMrComm = new CmMrComm_Dic();
//                    cmMrComm.cmMrCommCd = DicSet.getString(DicSet.getColumnIndex("dictionaryCode"));
//                    cmMrComm.cmMrCommDescr = DicSet.getString(DicSet.getColumnIndex("dictionaryDescr"));
//                    cmMrComm_list.add(cmMrComm);
//                }
                cmMrComm = new CmMrComm_Dic();
                cmMrComm.cmMrCommCd = DicSet.getString(DicSet.getColumnIndex("dictionaryCode"));
                cmMrComm.cmMrCommDescr = DicSet.getString(DicSet.getColumnIndex("dictionaryDescr"));
                cmMrComm_list.add(cmMrComm);
                while (DicSet.moveToNext()) {
                    cmMrComm = new CmMrComm_Dic();
                    cmMrComm.cmMrCommCd = DicSet.getString(DicSet.getColumnIndex("dictionaryCode"));
                    cmMrComm.cmMrCommDescr = DicSet.getString(DicSet.getColumnIndex("dictionaryDescr"));
                    cmMrComm_list.add(cmMrComm);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (DicSet != null) {
                try {
                    DicSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            if (state != null) {
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            if (conne != null) {
                try {
                    conne.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        return cmMrComm_list;

    }

    ;

    // 保存数据
    public void saveData(String userid, PhotoAudio photoAudio) throws SQLException {

        // 将数据插入到安检上传表中,首先判断该用户在上传表中是否存在,进行插入或更新
        String sql_isSave = "select * from uploadcustInfo_aj where  accountId='"
                + taskId
                + "' and cmSchedId = '"
                + custInfo.getCmSchedId()
                + "'";
        DatabaseHelper conne;
        SQLiteDatabase state;
        conne=new DatabaseHelper(AddImageActivity.this);

        state = conne.getWritableDatabase();

        Cursor executeQuery = state.rawQuery(sql_isSave, null);
        executeQuery.moveToLast();
        int a = executeQuery.getCount();
        if (a == 0) {
            // 表中不存在该用户,插入

            String sql = "insert into uploadcustInfo_aj (cmSchedId,accountId,cmMrCommCd) "
                    + "values ("
                    + "'"
                    + custInfo.getCmSchedId()
                    + "',"
                    + "'"
                    + taskId + "'," + "'" + fuWu_code + "')";

            state.execSQL(sql);
        } else {
            // 表中存在该用户,更新
            String sql = "update  uploadcustInfo_aj set cmMrCommCd = '"
                    + fuWu_code + "' where  accountId= '" + taskId
                    + "' and cmSchedId = '" + custInfo.getCmSchedId() + "'";
            state.execSQL(sql);
        }

//		将照片信息保存至数据库
        String insert_sth="insert into perFile_aj (cmSchedId,accountId,cmScFileName,cmScFileForm,cmScBusiType,cmScFileRoute,cmScFileSize,cmScFileDttm,cmScFileVar1,cmScFileVar2,cmScdate)"
                + " values (?,?,?,?,?,?,?,?,?,?,?)";

        state.execSQL(insert_sth,new Object[]{custInfo.getCmSchedId(),taskId,photoAudio.getCmScFileName(),photoAudio.getCmScFileForm(),
                photoAudio.getCmScBusiType(),photoAudio.getCmScFileRoute(),photoAudio.getCmScFileSize(),photoAudio.getCmScFileDttm(),
                photoAudio.getCmScFileVar1(),photoAudio.getCmScFileVar2(),time_now});



        if (executeQuery != null) {
            try {
                executeQuery.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (state != null) {
            try {
                state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conne != null) {
            try {
                conne.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
