package cn.sbx.deeper.moblie.fargments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.sunboxsoft.monitor.utils.PublicWay;
import com.sunboxsoft.monitor.utils.Res;

import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sbx.deeper.moblie.activity.SeePicture_PageActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.PhotoAudio;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.CompressorImageUtil;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

public class AnJianDetailAddPictureFragment extends BaseFragment implements
        OnClickListener, IRefreshButtonAndText {
    private static final String TAG = "AnJianDetailAddPictureFragment";
    public static Bitmap bimap;
    private GridAdapter adapter;
    BitmapUtils bitmapUtils;
    // List<PhotoAudio> imgList = new ArrayList<PhotoAudio>();
    private String sdPath = android.os.Environment
            .getExternalStorageDirectory().toString();
    private String taskid;
    private CustInfo_AnJian custInfo;
    static final int CAMERA_RESULT = 0x01;
    static final int CHOOSE_IMAGE = 0x99;
    static final int CHOOSE_VIDEO = 5;
    private GridView noScrollgridview;
    private String imagePath;
    DatabaseHelper conne = null;
    private Uri imageURI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bitmapUtils = new BitmapUtils(mActivity);
        TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
        btn_next = (Button) getActivity().findViewById(R.id.btn_next);
        btn_next.setVisibility(View.GONE);
        btn_next.setText("确定");
        btn_next.setOnClickListener(this);
        tv_title.setText("图片");
        Res.init(getActivity());
        bimap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_addpic_unfocused);
        PublicWay.activityList.add(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {
            custInfo = (CustInfo_AnJian) bundle.getSerializable("custinfo");
            taskid = bundle.getString("id");
        }
        // 查询该用户图片信息,先清空集合
        Constants.imgList.clear();
        try {
            selectPicture(taskid, custInfo.getCmSchedId());
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(mActivity, "图片加载失败", Toast.LENGTH_SHORT).show();
        }
        System.out.println("=====1====");
    }

    private File imageFile;
    private String time;
    Date date;
    private Button btn_next;
    private String time_now;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = View.inflate(getActivity().getApplicationContext(),
                R.layout.layout_anjian_picture, null);
        noScrollgridview = (GridView) view.findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(getActivity().getApplicationContext());
        // adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new OnItemClickListener() {


            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                time_now = format.format(new Date());
                if (arg2 == Constants.imgList.size()) { // 点击的是加号,开启相机
                    Log.i("ddddddd", "----------");
                    SimpleDateFormat format1 = new SimpleDateFormat(
                            "yyyyMMddHHmmss");

                    time = format1.format(new Date());
                    imagePath = sdPath + "/" + Constants.sd_cache + "/"
                            + time_now + "/" + Constants.loginName + "/"
                            + custInfo.getServicePointId() + "_" + time
                            + ".jpg";

                    try {
                        date = format1.parse(time);
                        // date = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss")
                        // .parse(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    imageFile = new File(imagePath);
                    if (!imageFile.getParentFile().exists()) {
                        imageFile.getParentFile().mkdirs();
                    }
                    imageURI = Uri.fromFile(imageFile);
                    Intent i = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(MediaStore.EXTRA_OUTPUT,
                            imageURI);
                    startActivityForResult(i, CAMERA_RESULT);

                } else { // 点击预览图片
                    Intent intent = new Intent(mActivity,
                            SeePicture_PageActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    intent.putExtra("taskId", taskid);
                    intent.putExtra("SchedID", custInfo.getCmSchedId());
                    startActivity(intent);

                }
            }
        });

        System.out.println("=====2====");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isDeletePicture) {//当删除图片后在进行刷新适配器,防止每拍一张就查询数据库
            // 查询该用户图片信息,先清空集合
            Constants.imgList.clear();
            try {
                selectPicture(taskid, custInfo.getCmSchedId());
                adapter.notifyDataSetChanged();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(mActivity, "图片加载失败", Toast.LENGTH_SHORT).show();
            }
            Constants.isDeletePicture = false;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        // 界面加载完后清空 imgList 集合
        // imgList.clear();
        System.out.println("=====3====");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Bitmap bm = null;
        final PhotoAudio photoAudio = new PhotoAudio();
        if (resultCode == Activity.RESULT_OK) {
            Uri uri;
            switch (requestCode) {
                case CHOOSE_IMAGE:
                    // imageURI = data.getData();
                    // imagePath = getImageRealPath(imageURI);
                    // photoAudio = new PhotoAudio();
                    // photoAudio.setUrl(imagePath);
                    // imgList.add(photoAudio);
                    // initAdapterIMG();
                    // String outPath = sdPath + FileUtil.getUUid() + ".jpg";
                    // File tfile = new File(outPath);
                    // if (!tfile.getParentFile().exists())
                    // tfile.getParentFile().mkdirs();
                    // System.out.println(imagePath);
                    // ImageUtils.shrinkFromFile(imagePath, outPath, 480, 800);
                    break;
                case CAMERA_RESULT: // 从照相机获取，显示缩略图，并添加路径到列表中

                    Bitmap bitmap = CompressorImageUtil.getScaledBitmap(mActivity, imageURI);
                    storeImageToSDCARD(bitmap, imagePath);// 将压缩后的图片保存,覆盖之前图片
                    // photoAudio = new PhotoAudio();
                    String path = imageFile.getPath();// 绝对路径
                    String[] split = path.split("sc/");
                    photoAudio.setCmScFileRoute("/sc/" + split[1]);// 相对路径
                    photoAudio.setUrl("/sc/" + split[1]);// 设置图片路径,相对路径

                    String name = imageFile.getName();
                    long length = imageFile.length();// 图片大小
                    int height = bitmap.getHeight();// 图片 高度 :像素
                    int width = bitmap.getWidth();
                    photoAudio.setCmScFileName(name);
                    photoAudio.setCmScFileForm("jpg");
                    photoAudio.setCmScBusiType("PIC");// 业务类型 来自数据字典,

                    photoAudio.setCmScFileSize(String.valueOf(length));// 字节数
                    photoAudio.setCmScFileDttm(new SimpleDateFormat(
                            "yyyy-MM-dd-HH.mm.ss").format(date));// 时间
                    photoAudio.setCmScFileVar1(height + "");
                    photoAudio.setCmScFileVar2(width + "");
                    Constants.imgList.add(photoAudio);
                    // 刷新适配器
                    adapter.notifyDataSetChanged();
                    // initAdapterIMG();
                    // btn_next.setVisibility(View.VISIBLE);
                    // System.out.println(imagePath);
                    // File tfile1 = new File(outPath1);
                    // if (!tfile1.getParentFile().exists())
                    // tfile1.getParentFile().mkdirs();
                    // ImageUtils.shrinkFromFile(imagePath, imagePath, 400, 800);
                    try {
                        conne = new DatabaseHelper(getActivity());
//                                conne.setAutoCommit(false);
                        String insert_into="insert into perFile_aj (cmSchedId,accountId,cmScFileName,cmScFileForm,cmScBusiType,cmScFileRoute,cmScFileSize,cmScFileDttm,cmScFileVar1,cmScFileVar2,cmScdate)"
                                + " values (?,?,?,?,?,?,?,?,?,?,?)";

                        SQLiteDatabase writableDatabase = conne.getWritableDatabase();
                        writableDatabase.execSQL(insert_into,new Object[]{"" + custInfo.getCmSchedId() + "","" + taskid + "",photoAudio.getCmScFileName(),photoAudio.getCmScFileForm(),
                                photoAudio.getCmScBusiType(), photoAudio.getCmScFileRoute(), photoAudio.getCmScFileSize(),photoAudio.getCmScFileDttm(),photoAudio.getCmScFileVar1(),
                                photoAudio.getCmScFileVar2(), time_now});


                        writableDatabase.close();
//                                conne.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
//							Toast.makeText(mActivity, "图片获取失败", 1).show();
                    }

                    // 将图片信息保存至数据库
//                    ThreadUtils.getThreadPool_Instance().submit(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                conne = new DatabaseHelper(getActivity());
////                                conne.setAutoCommit(false);
//                                String insert_into="insert into perFile_aj (cmSchedId,accountId,cmScFileName,cmScFileForm,cmScBusiType,cmScFileRoute,cmScFileSize,cmScFileDttm,cmScFileVar1,cmScFileVar2,cmScdate)"
//                                        + " values (?,?,?,?,?,?,?,?,?,?,?)";
//
//                                SQLiteDatabase writableDatabase = conne.getWritableDatabase();
//                                writableDatabase.execSQL(insert_into,new Object[]{"" + custInfo.getCmSchedId() + "","" + taskid + "",photoAudio.getCmScFileName(),photoAudio.getCmScFileForm(),
//                                        photoAudio.getCmScBusiType(), photoAudio.getCmScFileRoute(), photoAudio.getCmScFileSize(),photoAudio.getCmScFileDttm(),photoAudio.getCmScFileVar1(),
//                                        photoAudio.getCmScFileVar2(), time_now});
//
//
//                                writableDatabase.close();
////                                conne.commit();
//                            } catch (SQLException e) {
//                                e.printStackTrace();
////							Toast.makeText(mActivity, "图片获取失败", 1).show();
//                            }
//
//                        }
//                    });

                    break;

            }

        }
    }

    protected String getImageRealPath(Uri imageURI) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(imageURI, proj, null, null,
                null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;
        private Context mContext;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            // inflater = LayoutInflater.from(context);
            this.mContext = context;
        }

        // public void update() {
        // loading();
        // }

        public int getCount() {
            // if (Bimp.tempSelectBitmap.size() == 9) {
            // return 9;
            // }
            // return (Bimp.tempSelectBitmap.size() + 1);
            return Constants.imgList.size() + 1;
        }

        public Object getItem(int arg0) {
            return Constants.imgList.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.item_published_grida, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Constants.imgList.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 9) {// 最多添加9张图片
                    holder.image.setVisibility(View.GONE);

                }
            } else {

//				BitmapFactory.Options options = new BitmapFactory.Options();
//	               options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一

//                btp = BitmapFactory.decodeFile(sdPath
//                      + Constants.imgList.get(position).getUrl());
//                holder.image.setImageBitmap(btp);
//                DisplayImageOptions options;
//                options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher).cacheInMemory(true)
//                        .cacheOnDisc(true).imageScaleType(ImageScaleType.NONE).build();
//
//                ImageLoader.getInstance().displayImage("file:///"+sdPath+Constants.imgList.get(position).getUrl(), holder.image, options);

//                btp = ImageUtils.getBitmapImage(sdPath
//                        + Constants.imgList.get(position).getUrl());
//                holder.image.setImageBitmap(btp);
                bitmapUtils.display( holder.image,"file:///"+sdPath+Constants.imgList.get(position).getUrl());


//
//                String url = "file:///" + sdPath + Constants.imgList.get(position).getUrl();
//                LogUtil.i(TAG, "url" + url);
//                ImageLoader.getInstance().displayImage(url, holder.image, ImageLoaderOptions.pager_options);


//				 ImageLoader.getInstance().displayImage(imgList.get(position).getUrl(),
//				 holder.image, ImageLoaderOptions.list_options);
//				 String Url =
//				 sdPath+"20160323/1400017/6485700014_20160323225350.jpg";
//				 ImageLoader.getInstance().displayImage(Url, holder.image,
//				 ImageLoaderOptions.list_options);
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        private Bitmap btp;

        // public void loading() {
        // new Thread(new Runnable() {
        // public void run() {
        // while (true) {
        // if (Bimp.max == Bimp.tempSelectBitmap.size()) {
        // Message message = new Message();
        // message.what = 1;
        // handler.sendMessage(message);
        // break;
        // } else {
        // Bimp.max += 1;
        // Message message = new Message();
        // message.what = 1;
        // handler.sendMessage(message);
        // }
        // }
        // }
        // }).start();
        // }
    }

    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = comp(BitmapFactory.decodeFile(srcPath, newOpts));
        // return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        System.out.println("baos.toByteArray().length:"
                + baos.toByteArray().length);
        while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            System.out.println("11baos.toByteArray().length:"
                    + baos.toByteArray().length);
            baos.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
    }

    private Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            System.out.println("baos.toByteArray().length:"
                    + baos.toByteArray().length);
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public void storeImageToSDCARD(Bitmap colorImage, String path) {
        File imagefile = new File(path);
        // if (!imagefile.exists()) {
        try {
            imagefile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imagefile);
            colorImage.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // } else {
        // }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_next:
                final ProgressHUD overlayProgress = AlertUtils.showDialog(
                        getActivity(), null, null, false); // 时间太长了，把加载进度条取消掉。。。。
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() { // TODO Auto-generated
                        if (Constants.imgList.size() > 0) {
                            // 将图片信息保存至数据库
                            DatabaseHelper conne = null;
                            String sql_insert="insert into perFile_aj (cmSchedId,accountId,cmScFileName,cmScFileForm,cmScBusiType,cmScFileRoute,cmScFileSize,cmScFileDttm,cmScFileVar1,cmScFileVar2)"
                                    + " values (?,?,?,?,?,?,?,?,?,?)";
                            try {
                                conne = new DatabaseHelper(getActivity());
                                SQLiteDatabase writableDatabase = conne.getWritableDatabase();



                                for (PhotoAudio photoAudio : Constants.imgList) {
                                    writableDatabase.execSQL(sql_insert,new Object[]{"" + custInfo.getCmSchedId()
                                            + "", "" + taskid + "",photoAudio.getCmScFileName(),photoAudio.getCmScFileForm(),
                                            photoAudio.getCmScBusiType(),photoAudio.getCmScFileRoute(),photoAudio.getCmScFileSize(),
                                            photoAudio.getCmScFileDttm(),photoAudio.getCmScFileVar1(),photoAudio.getCmScFileVar2()});

                                }

                                writableDatabase.close();

                                Toast.makeText(mActivity, "保存成功", Toast.LENGTH_LONG).show();
                            } catch (SQLException e) {
                                e.printStackTrace();
                                Toast.makeText(mActivity, "保存失败", Toast.LENGTH_LONG).show();
                            }
                        }

                        if (overlayProgress != null) {
                            overlayProgress.dismiss();
                        }
                    }
                }, 1000);
                break;
            case R.id.bt_left:
                // 单击返回按钮时,清空图片临时存储集合
                Constants.imgList.clear();
                break;
            default:
                break;
        }

    }

    private void selectPicture(String taskid2, String cmSchedId)
            throws SQLException {
        DatabaseHelper conne = new DatabaseHelper(getActivity());
        String qal_select="select cmScFileRoute from perFile_aj where cmSchedId = '"
                + cmSchedId + "' and accountId = '" + taskid2 + "'";
        SQLiteDatabase writableDatabase = conne.getWritableDatabase();
        net.sqlcipher.Cursor set = writableDatabase.rawQuery(qal_select,null);
        if(set.moveToFirst()){
            PhotoAudio photoAudio1 = new PhotoAudio();
            photoAudio1.setUrl(set.getString(set.getColumnIndex("cmScFileRoute")));
            Constants.imgList.add(photoAudio1);
            while (set.moveToNext()) {
                PhotoAudio photoAudio = new PhotoAudio();
                photoAudio.setUrl(set.getString(set.getColumnIndex("cmScFileRoute")));
                Constants.imgList.add(photoAudio);
            }
        }



        set.close();
        writableDatabase.close();
        conne.close();
    }

    @Override
    public void refreshButtonAndText() {
        super.refreshButtonAndText();
        btn_next = (Button) getActivity().findViewById(R.id.btn_next);
        btn_next.setVisibility(View.GONE);
        // // 刷新缩略图适配器'
        // // 刷新适配器
        // 查询该用户图片信息,先清空集合
        Constants.imgList.clear();
        try {
            selectPicture(taskid, custInfo.getCmSchedId());
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(mActivity, "图片加载异常", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
//        noScrollgridview.setAdapter(adapter);
        System.out.println("=========返回处理=========");
    }

}
