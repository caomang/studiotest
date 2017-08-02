package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sunboxsoft.deeper.moblie.handwriting.AddImageActivity;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.audio.SoundMeter;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.domian.ApproveViewTag;
import cn.sbx.deeper.moblie.domian.CmMrComm_Dic;
import cn.sbx.deeper.moblie.domian.CmScAjrh_Dic;
import cn.sbx.deeper.moblie.domian.CmScShType_Dic;
import cn.sbx.deeper.moblie.domian.CmScSpItem;
import cn.sbx.deeper.moblie.domian.CmScYhzg;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.DictionariesHiddenDanger;
import cn.sbx.deeper.moblie.domian.PhotoAudio;
import cn.sbx.deeper.moblie.domian.SecurityCheck;
import cn.sbx.deeper.moblie.domian.SecurityCheckHiddenDanger;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecForm;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTD;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTR;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTable;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UIDate;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UIInput;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UIOption;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UISelect;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.SinopecMenuPage;
import cn.sbx.deeper.moblie.domian.UploadcustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.readType;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.DatabaseHelperInstance;
import cn.sbx.deeper.moblie.util.FileCache;
import cn.sbx.deeper.moblie.util.FileUtil;
import cn.sbx.deeper.moblie.util.ImageUtils;
import cn.sbx.deeper.moblie.util.MobilePrint;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.FlowLayout;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 审批组件详细界面
 *
 * @author terry.C
 *         <p/>
 *         注释记录 new ApproveDetailTask().execute(taskId, isnext);
 */
@SuppressLint("ValidFragment")
public class AnJianDetailFragment extends BaseFragment implements
        OnClickListener, IRefreshButtonAndText {
    //    String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
//    String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/cb_db.db";
    // private String sdPath = android.os.Environment
    // .getExternalStorageDirectory()
    // + "/"
    // + Constants.home_cache
    // + "/polling/";
    // /storage/sdcard0/sunboxsoft/polling/
    private String sdPath = Environment
            .getExternalStorageDirectory() + "/ " + Constants.sd_cache + "/";
    private String sdPath1 = Environment
            .getExternalStorageDirectory().toString();
    // 业务类型/上传日期/手持终端设备编号/图片文件名称
    // /sc/20151209/110001/0306500055_20151208153628.JPG
    private CheckBox cb_left;
    // private Button bt_next;
    private Button btn_next;
    private ExpandableListView elv_details;
    public Map<String, Map<String, ApproveViewTag>> requiredDatas = new HashMap<String, Map<String, ApproveViewTag>>();
    public Map<String, Map<String, ApproveViewTag>> submitDatas = new HashMap<String, Map<String, ApproveViewTag>>();
    private SinopecApproveDetailEntry detailEntry;
    Context mContext;
    private SQLiteDatabase instance;
    private boolean showText = false;
    private SinopecMenuModule menuModule;
    private String taskId;
    private String tabType;
    private IApproveBackToList backToList;
    private int targetContainer;
    private String listPath;
    private String formPath;
    private String htmlPath;
    private String filePath;
    private String deviceDetailPath;
    public BaseFragment sinopecApproveFragment;
    private BaseActivity activity;
    private ProgressHUD progressHUDx;
    String cmSchedIDPage;
    boolean isBatch;
    private Cursor cursor;
    Button btn_save;
    private String commonAdviceList = "";
    LinearLayout ll_dialog;
    private LayoutInflater layoutInflater;
    public String currNote;
    public String displayDevice;
    public String deviceResult;
    EditText bz_edit;// 表字
    String bz_text = "";// 表字
    EditText beizhu_edit;// 备注
    TextView ls_bz_tv;// 上次表字
    TextView qiliang;// 气量
    TextView qifei;// 气费
    public String beizhu = "";// 备注
    public String jiage_fang = "";// 价格每立方
    public String spinner_data = "";// 读数类型
    public String aj_spinnerString = "";// 拒绝入内、到访不遇、正常入户。默认为不选。
    public String photo_url = "";
    public String audio_url = "";
    public String bq_cb_data = "";
    public Button next_user_btn, up_user_btn, anjian_btn;
    Button photoButton, audioButton;
    List<PhotoAudio> imgList = new ArrayList<PhotoAudio>();
    List<PhotoAudio> audioList = new ArrayList<PhotoAudio>();
    private boolean isShosrt = false;
    private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
            voice_rcd_hint_tooshort;
    private ImageView img1, sc_img1;
    private SoundMeter mSensor;
    private View rcChat_popup;
    private LinearLayout del_re;
    private ImageView chatting_mode_btn, volume;
    private boolean btn_vocie = false;
    private int flag = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 200:
                    if(spinner_serve_evaluate==null){
                        spinner_serve_evaluate = (TextView) view .findViewById(R.id.spinner_serve_evaluate);
                    }


                    if (showText) {
                        spinner_serve_evaluate.setText("已签字");
                    } else {
                        spinner_serve_evaluate.setText("请客户选择服务评价并签名");
                    }
                    break;
                case 111:
                    if(progressHUDx!=null){
                        progressHUDx.dismiss();
                    }
                    initPhoto();

                    break;
                default:
                    break;
            }

        }
    };
    private String voiceName;
    private long startVoiceT, endVoiceT;
    DatabaseHelperInstance databaseHelperInstance;
    // 标示是否显示删除
    private boolean isShowDelete_img = false;
    private boolean isShowDelete_audio = false;
    private boolean isShowDelete_video = false;
    private GridAdapter imgAdapter;
    private AudioGridAdapter audioAdapter;
    private static final int MAX_PICS = 5;
    private GridView imageGrid, videoGrid, audioGrid;
    static final int CAMERA_RESULT = 0x01;
    static final int CHOOSE_IMAGE = 0x99;
    static final int CHOOSE_VIDEO = 5;
    protected static final int LOADING = 10;
    protected static final int Over = 20;
    protected static final int Over1 = 100;
    String imagePath = null;
    Uri imageURI;
    private RelativeLayout btn_layout;
    private String separator = ";#";
    private Spinner aj_zg_spinner, aj_zjg_spinner, aj_fm_spinner,
            aj_rsq_spinner, aj_bj_spinner, aj_cnl_spinner, aj_zj_spinner,
            aj_qt_spinner, aj_bjq_spinner;
    private int isAQ = 0;// 是否安全隐患
    private EditText aj_tongzhidan, aj_biaozi, aj_beizhu, user_anjian_tzd;
    private String aj_spinnerCode;// 安检入户情况编码
    private String spinner_Jg;// 警告标志
    private String spinner_fuWu = "";
    private String spinner_dushu = "";
    private boolean isOpen = false;
    private boolean isOpen2 = false;
    private String code_yinHuanZG;
    private TextView spinner_serve_evaluate;
    String setSafeDetail="";

    //安检员的姓名
    private String loginNameForSign;
    private SharedPreferences loginNameForUser;
    // 打印类对象
    MobilePrint mp;
    private ProgressHUD progressHUD;
    // 存放入户安检数据字典
    ArrayList<CmScAjrh_Dic> cmScAjrh_list = null;

    Handler handler = new Handler() {
        private ProgressHUD progress;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    progress = AlertUtils.showDialog(mActivity, null, null, false);
                    break;
                case Over:
                    if (progress != null) {
                        progress.dismiss();

                    }
                    break;
                case Over1:

                    tv_morephoto = (TextView) view.findViewById(R.id.tv_morephoto);
                    // tv_qianming = (TextView) view.findViewById(R.id.tv_qianming);
                    user_address = (TextView) view.findViewById(R.id.user_address);
                    user_data = (TextView) view.findViewById(R.id.anjian_history_date);
                    // tv_olddata = (TextView) view.findViewById(R.id.tv_olddata);
                    tv_oldissafe_anjian = (TextView) view
                            .findViewById(R.id.tv_oldissafe_anjian);

                    // tv_isYH = (TextView) view.findViewById(R.id.tv_isYH);

                    jiliang_edit = (EditText) view.findViewById(R.id.jiliang_edit);
                    syqiliang_edit = (EditText) view.findViewById(R.id.syqiliang_edit);
                    et_beizhu_edit = (EditText) view.findViewById(R.id.et_beizhu_edit);

                    // iv_pic_qianming = (ImageView)
                    // view.findViewById(R.id.iv_pic_qianming);
                    iv_pic_qita = (ImageView) view.findViewById(R.id.iv_pic_qita);
                    iv_pic_jiben = (ImageView) view.findViewById(R.id.iv_pic_jiben);
                    iv_pic_anjian = (ImageView) view.findViewById(R.id.iv_pic_anjian);
                    iv_pic_zhaopian = (ImageView) view.findViewById(R.id.iv_pic_zhaopian);
                    image1 = (ImageView) view.findViewById(R.id.image1);
                    image2 = (ImageView) view.findViewById(R.id.image2);
                    iv_pic_shebeiziliao = (ImageView) view
                            .findViewById(R.id.iv_pic_shebeiziliao);

                    iv_pic_shebeiziliao.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewPropertyAnimator.animate(iv_pic_shebeiziliao).rotationBy(180)
                                    .setDuration(350).start();
                            if (isOpen2) {
                                ll_showshebeixinxi.setVisibility(View.GONE);
                                isOpen2 = !isOpen2;
                            } else {
                                ll_showshebeixinxi.setVisibility(View.VISIBLE);
                                isOpen2 = !isOpen2;
                            }
                        }
                    });
                    iv_pic_zhaopian.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewPropertyAnimator.animate(iv_pic_zhaopian).rotationBy(180)
                                    .setDuration(350).start();
                            if (isOpen) {
                                ll_showpacter.setVisibility(View.GONE);
                                isOpen = !isOpen;
                            } else {
                                ll_showpacter.setVisibility(View.VISIBLE);
                                // 加载图片显示区域
                                // 拍照
                                // LinearLayout ll_photocontainer = (LinearLayout) view
                                // .findViewById(R.id.ll_photocontainer_add);
                                LinearLayout ll_takepicture = (LinearLayout) view
                                        .findViewById(R.id.ll_takepicture);

                                ll_takepicture.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // 弹出添加图片界面,将当前用户传递过去

                                        Fragment f_pic = new AnJianDetailAddPictureFragment();
                                        Bundle bundle_picture = new Bundle();
                                        bundle_picture.putString("id", taskId);
                                        bundle_picture.putSerializable("custinfo", custInfo);
                                        f_pic.setArguments(bundle_picture);
                                        ((ActivityInTab) getActivity()).navigateTo(f_pic);
                                    }
                                });

                                // createPhoto_new(ll_photocontainer);

                                isOpen = !isOpen;
                            }
                        }
                    });
                    // tv_qianming.setOnClickListener(this);

                    ll_ruhuqingkuang = (LinearLayout) view
                            .findViewById(R.id.ll_ruhuqingkuang);
                    ll_qitaxinxi = (LinearLayout) view.findViewById(R.id.ll_qitaxinxi);
                    ll_shebeixinxi = (LinearLayout) view.findViewById(R.id.ll_shebeixinxi);
                    ll_showshebeixinxi = (LinearLayout) view
                            .findViewById(R.id.ll_showshebeixinxi);
                    ll_showpacter = (LinearLayout) view.findViewById(R.id.ll_showpacter);

                    // 静态设备信息
                    tv_biaoju = (TextView) view.findViewById(R.id.tv_biaoju);
                    tv_liguan = (TextView) view.findViewById(R.id.tv_liguan);
                    tv_zaoju = (TextView) view.findViewById(R.id.tv_zaoju);
                    tv_lianjieguan = (TextView) view.findViewById(R.id.tv_lianjieguan);
                    tv_cainuanlu = (TextView) view.findViewById(R.id.tv_cainuanlu);
                    tv_reshuiqi = (TextView) view.findViewById(R.id.tv_reshuiqi);
                    tv_baojingqi = (TextView) view.findViewById(R.id.tv_baojingqi);
                    // 标题
                    tv_title1 = (TextView) view.findViewById(R.id.tv_title1);
                    tv_title2 = (TextView) view.findViewById(R.id.tv_title2);
                    tv_title3 = (TextView) view.findViewById(R.id.tv_title3);
                    tv_title_shebeiziliao = (TextView) view
                            .findViewById(R.id.tv_title_shebeiziliao);
                    tv_title4 = (TextView) view.findViewById(R.id.tv_title4);

                    radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);

                    // 加粗
                    TextPaint tp = tv_title1.getPaint();
                    tp.setFakeBoldText(true);
                    TextPaint tp2 = tv_title2.getPaint();
                    tp2.setFakeBoldText(true);
                    TextPaint tp3 = tv_title3.getPaint();
                    tp3.setFakeBoldText(true);
                    TextPaint tp34 = tv_title4.getPaint();
                    tp34.setFakeBoldText(true);
                    TextPaint tp5 = tv_title_shebeiziliao.getPaint();
                    tp5.setFakeBoldText(true);
                    // 下划线
                    tv_biaoju.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    tv_liguan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    tv_zaoju.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    tv_lianjieguan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    tv_cainuanlu.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    tv_reshuiqi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    tv_baojingqi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

                    tv_biaoju.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", taskId);
                            bundle.putString("device_name", "表具");
                            Fragment f = new AnJianDetailFormFragment();
                            f.setArguments(bundle);
                            ((ActivityInTab) getActivity()).navigateTo(f);
                        }
                    });
                    tv_liguan.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle_liguan = new Bundle();
                            bundle_liguan.putString("id", taskId);
                            bundle_liguan.putSerializable("custinfo", custInfo);
                            bundle_liguan.putString("device_name", "立管阀门");
                            Fragment f1 = new AnJianDetailFormFragment_LiGuan();
                            f1.setArguments(bundle_liguan);
                            ((ActivityInTab) getActivity()).navigateTo(f1);
                        }
                    });
                    tv_zaoju.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle_zaoju = new Bundle();
                            bundle_zaoju.putString("id", taskId);
                            bundle_zaoju.putSerializable("custinfo", custInfo);
                            bundle_zaoju.putString("device_name", "灶具");
                            Fragment f_zaoju = new AnJianDetailFormFragment_ZaoJU();
                            f_zaoju.setArguments(bundle_zaoju);
                            ((ActivityInTab) getActivity()).navigateTo(f_zaoju);
                        }
                    });
                    tv_lianjieguan.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle_lianjieguan = new Bundle();
                            bundle_lianjieguan.putString("id", taskId);
                            bundle_lianjieguan.putSerializable("custinfo", custInfo);
                            bundle_lianjieguan.putString("device_name", "连接管");
                            Fragment f_lianjieguan = new AnJianDetailFormFragment_LianJieGuan();
                            f_lianjieguan.setArguments(bundle_lianjieguan);
                            ((ActivityInTab) getActivity()).navigateTo(f_lianjieguan);
                        }
                    });
                    tv_cainuanlu.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle_cainuanlu = new Bundle();
                            bundle_cainuanlu.putString("id", taskId);
                            bundle_cainuanlu.putSerializable("custinfo", custInfo);
                            bundle_cainuanlu.putString("device_name", "采暖炉");
                            Fragment f_cainuanlu = new AnJianDetailFormFragment_CaiNuanLu();
                            f_cainuanlu.setArguments(bundle_cainuanlu);
                            ((ActivityInTab) getActivity()).navigateTo(f_cainuanlu);
                        }
                    });
                    tv_reshuiqi.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle_reshuiqi = new Bundle();
                            bundle_reshuiqi.putString("id", taskId);
                            bundle_reshuiqi.putSerializable("custinfo", custInfo);
                            bundle_reshuiqi.putString("device_name", "热水器");
                            Fragment f_reshuiqi = new AnJianDetailFormFragment_ReShuiQi();
                            f_reshuiqi.setArguments(bundle_reshuiqi);
                            ((ActivityInTab) getActivity()).navigateTo(f_reshuiqi);
                        }
                    });
                    tv_baojingqi.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle_baojingqi = new Bundle();
                            bundle_baojingqi.putString("id", taskId);
                            bundle_baojingqi.putSerializable("custinfo", custInfo);
                            bundle_baojingqi.putString("device_name", "报警器");
                            Fragment f_baojingqi = new AnJianDetailFormFragment_BaoJngQi();
                            f_baojingqi.setArguments(bundle_baojingqi);
                            ((ActivityInTab) getActivity()).navigateTo(f_baojingqi);
                        }
                    });
                    // 保存

                    btn_next.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(aj_spinnerCode)) { // 入户情况唯一必填
                                final ProgressHUD overlayProgress = AlertUtils.showDialog(
                                        getActivity(), null, null, false); // 时间太长了，把加载进度条取消掉。。。。
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() { // TODO Auto-generated
                                        // method stub
                                        saveData(taskId); //
                                        if (overlayProgress != null) {
                                            overlayProgress.dismiss();
                                        }
                                        Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_LONG).show();
                                        // 修改客户信息表的状态
                                        changeCmState(taskId);
                                        // dialogUpdate(getActivity());

                                        // 当选择正常入户时 提示是否打印

                                        if (aj_spinnerCode.equals("ZCRH")) {

                                            // 提示是否打印
                                            new Builder(getActivity())
                                                    .setTitle("提示")
                                                    .setMessage("是否打印当前保存的数据？")
                                                    .setPositiveButton(
                                                            "确定",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(
                                                                        DialogInterface dialog,
                                                                        int which) {
                                                                    if (!mp.adapter.isEnabled()) {
                                                                        Toast.makeText(
                                                                                mActivity,
                                                                                "请开启蓝牙", Toast.LENGTH_SHORT)
                                                                                .show();
                                                                    }
                                                                    // 判断是否成功连接蓝牙
                                                                    if (mp.GetBluetoothData()) {

                                                                        // 调用打印方法
                                                                        if (mp.mPrinter != null) {

                                                                            SecurityCheck sc = SelectSecurityCheck();

                                                                            // 获取隐患集合数据
                                                                            List<SecurityCheckHiddenDanger> list = SelectHiddenDanger();
                                                                            // 获取该用户电子签名信息
                                                                            String imagePath_relative = getHandWriterInfo();

                                                                            // 判断是否存在安全隐患
                                                                            if (list.size() > 0) {
                                                                                // 获取字典表类型
                                                                                List<DictionariesHiddenDanger> dicList = SelectSecurityCheckType();
                                                                                // 存在安全隐患
                                                                                mp.SafetyChecklist_HiddenDanger(
                                                                                        getActivity()
                                                                                                .getResources(),
                                                                                        mp.mPrinter,
                                                                                        true,
                                                                                        sc,
                                                                                        dicList,
                                                                                        taskId,
                                                                                        sdPath1
                                                                                                + imagePath_relative,loginNameForSign,getActivity());

                                                                            } else {
                                                                                mp.SafetyChecklist(
                                                                                        getActivity()
                                                                                                .getResources(),
                                                                                        mp.mPrinter,
                                                                                        true,
                                                                                        sc,
                                                                                        sdPath1
                                                                                                + imagePath_relative,loginNameForSign);
                                                                            }
                                                                        }
                                                                    } else {
                                                                        new Builder(
                                                                                getActivity())
                                                                                .setTitle(
                                                                                        "打印失败")
                                                                                .setMessage(
                                                                                        "当前设备尚未连接蓝牙打印机")
                                                                                .show();
                                                                    }
                                                                }

                                                            }).setNegativeButton("取消", null)
                                                    .show();

                                        }

                                    }
                                }, 2000);

                            } else {

                                Toast.makeText(getActivity(), "请选择安检入户情况", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    spinner_jgbiao = (Spinner) view.findViewById(R.id.spinner_jgbiao);
                    rl_safe_explain = (RelativeLayout) view
                            .findViewById(R.id.rl_safe_explain);
                    device_layout_container = (LinearLayout) view
                            .findViewById(R.id.device_layout_contain);
                    safe_detail_textview=(TextView)view.findViewById(R.id.safe_detail_textview);


                    jiliang_edit = (EditText) view.findViewById(R.id.jiliang_edit);
                    syqiliang_edit = (EditText) view.findViewById(R.id.syqiliang_edit);
                    et_beizhu_edit = (EditText) view.findViewById(R.id.et_beizhu_edit);


                    if (state_up.equals("2")) {
                        btn_next.setVisibility(View.INVISIBLE);
                    }


                    if (custInfo.getCmMrUnit().equals("null")) {
                        user_address.setText(custInfo.getCmMrCommunity()
                                + custInfo.getCmMrBuilding() + "#" + custInfo.getCmMrRoomNum());
                    } else {
                        user_address.setText(custInfo.getCmMrCommunity()
                                + custInfo.getCmMrBuilding() + "#" + custInfo.getCmMrUnit()
                                + "-" + custInfo.getCmMrRoomNum());
                    }


                    user_data.setText(custInfo.getCmMrLastSecchkDt().equals("null")
                            || TextUtils.isEmpty(custInfo.getCmMrLastSecchkDt()) ? "无"
                            : custInfo.getCmMrLastSecchkDt());

                    if (uploadcustInfo != null && state_up.equals("2")) { // 已上传不可修改,保存后可修改
                        if (!TextUtils.isEmpty(uploadcustInfo.getCmMr())) {
                            jiliang_edit.setText(uploadcustInfo.getCmMr());
                            jiliang_edit.setEnabled(false);
                            jiliang_edit.setTextColor(Color.GRAY);
                        }
                        if (!TextUtils.isEmpty(uploadcustInfo.getCmScSyql())) {
                            syqiliang_edit.setText(uploadcustInfo.getCmScSyql());
                            syqiliang_edit.setEnabled(false);
                            syqiliang_edit.setTextColor(Color.GRAY);
                        }
                        if (!TextUtils.isEmpty(uploadcustInfo.getCmScRemark())) {
                            et_beizhu_edit.setText(uploadcustInfo.getCmScRemark());
                            et_beizhu_edit.setEnabled(false);
                            et_beizhu_edit.setTextColor(Color.GRAY);
                        }
                    } else if (uploadcustInfo != null) {
                        if (!TextUtils.isEmpty(uploadcustInfo.getCmMr())) {
                            jiliang_edit.setText(uploadcustInfo.getCmMr());
                        }
                        if (!TextUtils.isEmpty(uploadcustInfo.getCmScSyql())) {
                            syqiliang_edit.setText(uploadcustInfo.getCmScSyql());
                        }
                        if (!TextUtils.isEmpty(uploadcustInfo.getCmScRemark())) {
                            et_beizhu_edit.setText(uploadcustInfo.getCmScRemark());
                        }
                    }

                    initPhoto();

                    String select_isExplain = "select cmScSpItem,cmScSpCheck from perSp_aj where accountId = '"
                            + taskId + "'";
                    Cursor executeQuery = null;
                    try {
//                        conne = new DatabaseHelper(mContext);
//                        state = conne.getWritableDatabase();
                        executeQuery = instance.rawQuery(select_isExplain,new String[]{});
                        executeQuery.moveToLast();
                        // 已经讲解的选项
                        ArrayList<String> alreadyCheck = new ArrayList<String>();
                        executeQuery.moveToPrevious();
                        int a = 0;
                        while (executeQuery.moveToNext()) {
                            // alreadyCheck[a] = executeQuery.getString(1);
                            alreadyCheck.add(executeQuery.getString(1));
                            a++;
                        }
                        // 用来初始化多选框是否选中
                        final boolean[] flags = new boolean[cmScSpItem_list.size()];
                        // 多选框内容,来自数据字典
                        final String[] options_safe = new String[cmScSpItem_list.size()];
                        final String[] options_safe_code = new String[cmScSpItem_list
                                .size()];// 编码
                        for (int i = 0; i < cmScSpItem_list.size(); i++) {
                            options_safe[i] = cmScSpItem_list.get(i).getCmScSpItemDescr();
                            options_safe_code[i] = cmScSpItem_list.get(i).getCmScSpItem();
                            if (alreadyCheck.size() > 0) {
                                // 已讲解的内容编码与在数据字典中相同时,flags对应位置为true 否则为false
                                if (alreadyCheck.contains(cmScSpItem_list.get(i)
                                        .getCmScSpItem())) {
                                    flags[i] = true;
                                } else {
                                    flags[i] = false;
                                }
                            } else {
                                flags[i] = false;
                            }
                            final ArrayList<String> checkedList = new ArrayList<String>();

                            // 点击安全讲解
                            rl_safe_explain.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    // 创建选择框
                                    Builder builder = new Builder(
                                            getActivity());
                                    builder.setTitle("安全讲解选项");
                                    builder.setMultiChoiceItems(options_safe, flags,
                                            new OnMultiChoiceClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which, boolean isChecked) {
                                                    flags[which] = isChecked;
                                                    // // 将选择的选项存放在集合中
                                                    // for(int i=0;i<flags.length;i++){
                                                    // if(flags[i]){
                                                    // checkedList.add(options_safe_code[i]);
                                                    // }
                                                    // }
                                                }
                                            });

                                    builder.setPositiveButton(" 确 定 ",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    if (state_up.equals("2")) {
                                                        Toast.makeText(mContext,
                                                                "已上传的用户不可修改", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        // 将选择的选项存放在集合中,当点击保存时,插入数据库
                                                        // 先清空集合
                                                        checkedList.clear();
                                                        setSafeDetail="";
                                                        for (int i = 0; i < flags.length; i++) {
                                                            if (flags[i]) {
                                                                setSafeDetail+=options_safe[i]+" ";
                                                                checkedList
                                                                        .add(options_safe_code[i]);

                                                            }
                                                        }
                                                        if(setSafeDetail==""){
                                                            setSafeDetail="请选择客户安全讲解情况";
                                                        }

                                                        safe_detail_textview.setText(setSafeDetail);
                                                        // 单击确定按钮,所选项保存到数据库,先清空该用户已讲解选项
                                                        if (checkedList.size() > 0) {
                                                            DatabaseHelper conne1 = null;
                                                            SQLiteDatabase stat = null;
                                                            try {

                                                                conne1 = new DatabaseHelper(mContext);
                                                                stat = conne1
                                                                        .getWritableDatabase();


                                                                String string = "delete from perSp_aj where accountId = '"
                                                                        + taskId
                                                                        + "' and cmSchedId = '"
                                                                        + custInfo
                                                                        .getCmSchedId()
                                                                        + "'";

                                                                stat.execSQL(string);
                                                                for (int j = 0; j < checkedList
                                                                        .size(); j++) {

                                                                    // 插入数据

                                                                    String inster = "insert into  perSp_aj (cmSchedId,accountId,cmScSpItem,cmScSpCheck) values ('"
                                                                            + custInfo
                                                                            .getCmSchedId()
                                                                            + "','"
                                                                            + taskId
                                                                            + "','"
                                                                            + checkedList
                                                                            .get(j)
                                                                            + "','Y') ";
                                                                    stat.execSQL(inster);

                                                                }
                                                            } catch (SQLException e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(mContext,
                                                                        "提交失败", Toast.LENGTH_SHORT).show();
                                                            } finally {
                                                                if (stat != null) {
                                                                    try {
                                                                        stat.close();
                                                                    } catch (SQLException e) {
                                                                        // TODO
                                                                        // Auto-generated
                                                                        // catch block
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                                if (conne1 != null) {
                                                                    try {
                                                                        conne1.close();
                                                                    } catch (SQLException e) {
                                                                        // TODO
                                                                        // Auto-generated
                                                                        // catch block
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                            Toast.makeText(mContext, "已提交",
                                                                    Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            checkedList.clear();
                                                            // 没选项选中,点击确定,则清空存放选项的集合,保存时清除数据库记录
                                                            DatabaseHelper databaseHelper=new DatabaseHelper(mContext);
                                                            SQLiteDatabase stat=databaseHelper.getWritableDatabase();
                                                            String string = "delete from perSp_aj where accountId = '"
                                                                    + taskId
                                                                    + "' and cmSchedId = '"
                                                                    + custInfo
                                                                    .getCmSchedId()
                                                                    + "'";

                                                            stat.execSQL(string);
                                                            if (stat!=null){
                                                                stat.close();
                                                            }

                                                            if(databaseHelper!=null){
                                                                databaseHelper.close();

                                                            }



                                                        }
                                                        // 关闭对话框
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                    builder.setNegativeButton("取消",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    System.out
                                                            .println("取消=================");
                                                }
                                            });
                                    // 创建对话框对象
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (executeQuery != null) {
                            try {
                                executeQuery.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
//                        if (state != null) {
//                            try {
//                                state.close();
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        if (conne != null) {
//                            try {
//                                conne.close();
//                            } catch (SQLException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//
//                        }

                    }

                    aj_spinner_new = (Spinner) view.findViewById(R.id.aj_spinner);
                    device_layout_new = (LinearLayout) view
                            .findViewById(R.id.device_layout);
                    user_name1 = (TextView) view.findViewById(R.id.user_name1);
                    user_no = (TextView) view.findViewById(R.id.user_no);
                    user_view_anjian = (RelativeLayout) view
                            .findViewById(R.id.user_view_anjian);
                    final String[] options = new String[cmScAjrh_list.size() + 1];
                    options[0] = "请选择";
                    for (int i = 0; i < cmScAjrh_list.size(); i++) {
                        options[i + 1] = cmScAjrh_list.get(i).getCmScAjrhDescr();
                    }
                    aj_spinner_new
                            .setBackgroundResource(R.drawable.ic_approve_spinner_background);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                            R.layout.spinner_item, options);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    aj_spinner_new.setAdapter(adapter);
                    // 设置上次选择项
                    if (uploadcustInfo != null) {
                        String cmScAjrh = uploadcustInfo.getCmScAjrh();
                        // options2s.
                        for (int x = 0; x < cmScAjrh_list.size(); x++) {

                            if (cmScAjrh_list.get(x).getCmScAjrh().equals(cmScAjrh)) {
                                aj_spinner_new.setSelection(x + 1);
                            }
                        }
                    }
                    aj_spinner_new.setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {

                            if (arg2 == 0) {// 未选择入户情况
                                aj_spinnerCode = "";
                            } else {
                                aj_spinnerCode = cmScAjrh_list.get(arg2 - 1).getCmScAjrh();
                            }

                            aj_spinnerString = options[arg2];
                            if (aj_spinnerString.equals("正常入户")) {
                                // 先显示对应的界面
                                ll_ruhuqingkuang.setVisibility(View.VISIBLE);// 显示正常入户布局
                                ll_shebeixinxi.setVisibility(View.VISIBLE);
                                ll_qitaxinxi.setVisibility(View.VISIBLE);

                                device_layout_container.setVisibility(View.VISIBLE);
                                // 先清空该容器子view
                                int width = MeasureSpec.getSize(screenWidth);
                                device_layout_container.removeAllViews();
                                device_layout_container.addView(new AnJianYiCFragment(
                                        mActivity, mContext, width, cmScShType_list,
                                        custInfo).getView());
                                // 设置隐患整改情况选项
                                // cmScYhzgdata = getCmScYhzgdata();
                                Spinner aj_spinner_yhzg = (Spinner) view
                                        .findViewById(R.id.aj_spinner_yhzg);
                                String[] options = new String[cmScYhzgdata.size()];
                                int isCheck = 0;
                                for (int i = 0; i < cmScYhzgdata.size(); i++) {
                                    options[i] = cmScYhzgdata.get(i).getCmScYhzgDescr();
                                    if (uploadcustInfo != null
                                            && uploadcustInfo.getCmScYhzg() != null
                                            && uploadcustInfo.getCmScYhzg().equals(
                                            cmScYhzgdata.get(i).getCmScYhzg())) {
                                        isCheck = i;
                                    }
                                }

                                ArrayAdapter<String> adapter_isModification = new ArrayAdapter<String>(
                                        mContext, R.layout.spinner_item, options);

                                adapter_isModification
                                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                aj_spinner_yhzg
                                        .setBackgroundResource(R.drawable.ic_approve_spinner_background);

                                aj_spinner_yhzg.setAdapter(adapter_isModification);
                                aj_spinner_yhzg.setSelection(isCheck);
                                aj_spinner_yhzg
                                        .setOnItemSelectedListener(new OnItemSelectedListener() {

                                            @Override
                                            public void onItemSelected(AdapterView<?> arg0,
                                                                       View arg1, int arg2, long arg3) {

                                                code_yinHuanZG = cmScYhzgdata.get(arg2)
                                                        .getCmScYhzg();

                                            }

                                            @Override
                                            public void onNothingSelected(
                                                    AdapterView<?> arg0) {

                                            }
                                        });
                                user_anjian_tzd = (EditText) view
                                        .findViewById(R.id.user_anjian_tzd);
                                if (uploadcustInfo != null) {
                                    user_anjian_tzd.setText(uploadcustInfo.getCmScZgtzd());
                                }

                            } else {
                                ll_ruhuqingkuang.setVisibility(View.GONE);
                                device_layout_container.setVisibility(View.GONE);
                                ll_shebeixinxi.setVisibility(View.GONE);
                                ll_qitaxinxi.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });


//        user_name1.setText(custInfo.getEntityName());
//        user_no.setText(custInfo.getAccountId());
                    user_view_anjian.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 如果安检时间是 显示无 不可点击

                            Bundle bundle = new Bundle();
                            bundle.putString("id", taskId);
                            bundle.putString("state_up", state_up);
                            bundle.putSerializable("custinfo", custInfo);
                            // bundle.putSerializable("upcustInfo", uploadcustInfo);
                            Fragment f = new AnJianUserInfoFragment();
                            f.setArguments(bundle);
                            ((ActivityInTab) getActivity()).navigateTo(f);
                        }
                    });
                    RelativeLayout anjian_history_record = (RelativeLayout) view
                            .findViewById(R.id.user_view);
                    anjian_history_record.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String historyTime = (String) user_data.getText();
                            if (!historyTime.equals("无")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("id", taskId);
                                bundle.putSerializable("custinfo", custInfo);
                                Fragment f = new AnJianHistoryRecordFragment();
                                f.setArguments(bundle);
                                ((ActivityInTab) getActivity()).navigateTo(f);
                            }
                        }
                    });

                    // 张贴警告标示情况
                    final String[] options_jg = new String[]{"未张贴", "已张贴"};
                    spinner_jgbiao
                            .setBackgroundResource(R.drawable.ic_approve_spinner_background);
                    ArrayAdapter<String> adapter_JG = new ArrayAdapter<String>(mContext,
                            R.layout.spinner_item, options_jg);
                    adapter_JG
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_jgbiao.setAdapter(adapter_JG);

                    // 初始化
                    if (uploadcustInfo != null) {
                        String cmScZtjs = uploadcustInfo.getCmScZtjs();
                        // options2s.
                        if (cmScZtjs != null && cmScZtjs.equals("Y")) {
                            spinner_jgbiao.setSelection(1);
                        }
                    }

                    spinner_jgbiao.setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {
                            spinner_Jg = options_jg[arg2];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
                    // 安检入户情况

                    user_name1.setText(custInfo.getEntityName());
                    user_no.setText(custInfo.getAccountId());

                    // 服务评价,签名界面

                    spinner_serve_evaluate = (TextView) view
                            .findViewById(R.id.spinner_serve_evaluate);

                    spinner_serve_evaluate.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(mActivity, AddImageActivity.class);
                            intent.putExtra("taskId", taskId);
                            // intent.putExtra("servercePoint",
                            // custInfo.getServicePointId());
                            intent.putExtra("custInfo", custInfo);
                            startActivity(intent);
                        }
                    });

                    if(progressHUD!=null){
                        progressHUD.dismiss();
                    }


                    break;
                default:
                    break;
            }

        }

        ;
    };

    public AnJianDetailFragment(IApproveBackToList backToList,
                                int targetContainer) {
        this.backToList = backToList;
        this.targetContainer = targetContainer;
    }

    public AnJianDetailFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        if(custInfo!=null){
            FileCache.deleteOAFile();
            if (imageUrl_List != null && imageUrl_List.size() > 0) {
                imageUrl_List.clear();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        imageUrl_List = selectPicture(taskId, custInfo.getCmSchedId());
                    } catch (SQLiteException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "图片初始化失败", Toast.LENGTH_SHORT).show();
                    }
                    getPicUlrl();
                    Message msg = Message.obtain();
                    msg.what=200;
                    mHandler.sendMessage(msg);


                }
            }).start();



        }


    }

    private void getPicUlrl() {
        if (imageUrl_List == null || imageUrl_List.size() == 0) {
            showText = false;
        }

        for (int i = 0; i < imageUrl_List.size(); i++) {
            showText = false;
            if (imageUrl_List.get(i).contains(custInfo.accountId)) {

                showText = true;
                break;
            }

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
        TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
        // tv_title.setText(menuModule.caption);
        tv_title.setText("安检");
        btn_next = (Button) getActivity().findViewById(R.id.btn_next);
        btn_next.setVisibility(View.VISIBLE);
        btn_next.setText("保存");
        mContext = getActivity();
        //获取使用人员的姓名，然后打印的时候赋值给安检员签字

        loginNameForUser = mContext.getSharedPreferences("LoginNameForUser", mContext.MODE_PRIVATE);

        loginNameForSign = loginNameForUser.getString("LoginNameForSign", "");


        Bundle bundle = getArguments();
        if (bundle != null) {
            SinopecMenuModule item = (SinopecMenuModule) bundle
                    .getSerializable("entry");
            menuModule = item;
            taskId = bundle.getString("id");
            tabType = bundle.getString("tabType");
            isBatch = bundle.getBoolean("isBatch");
            currNote = bundle.getString("currNote");
            displayDevice = bundle.getString("displayDevice");
            deviceResult = bundle.getString("deviceResult");

            cmSchedIDPage = bundle.getString("cmSchedID");

            // if (!isBatch) {// 判断是否是批量审批
            // sinopecApproveFragment = (PollingApproveFragment)
            // getTargetFragment();
            // } else
            // sinopecApproveFragment = (SinopecApproveBatchFragment)
            // getTargetFragment();
        }
        for (SinopecMenuPage page : menuModule.menuPages) {
            if ("detail".equalsIgnoreCase(page.code)) {
                listPath = WebUtils.rootUrl + URLUtils.baseContentUrl + page.id;
            } else if ("childForm".equalsIgnoreCase(page.code)) {
                formPath = WebUtils.rootUrl + URLUtils.baseContentUrl + page.id;
            } else if ("childHTML".equalsIgnoreCase(page.code)) {
                htmlPath = WebUtils.rootUrl + URLUtils.baseContentUrl + page.id;
            } else if ("attachement".equalsIgnoreCase(page.code)) {
                filePath = WebUtils.rootUrl + URLUtils.baseContentUrl + page.id;
            } else if ("commonAdviceList".equalsIgnoreCase(page.code)) {// 审批意见列表
                commonAdviceList = WebUtils.rootUrl + URLUtils.baseContentUrl
                        + page.id;
            } else if ("deviceDetail".equalsIgnoreCase(page.code)) {// 审批意见列表
                deviceDetailPath = WebUtils.rootUrl + URLUtils.baseContentUrl
                        + page.id;
            }
        }
        layoutInflater = getActivity().getLayoutInflater();

        // new Thread() {
        // public void run() {
        // 获取字典
        progressHUD = AlertUtils.showDialog1(mActivity, null, null, true);
        progressHUD.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDate();
            }
        }).start();


        // }
        //
        // }.start();
        mp = new MobilePrint(mActivity);
        // 判断打印及是否连接
        if (!mp.isConnected) {
            // 判断是否已和打印设备配对
            if (!mp.GetBluetoothData()) {
                // 没有 ,连接蓝牙设备
                mp.initBluetooth();
            }
        }

        // 注册广播
        registerBoradcastReceiver();

    }

    private void getDate() {
         databaseHelperInstance=new DatabaseHelperInstance();

        instance = databaseHelperInstance.getInstance(mContext);


        // 获取该用户信息 根据taskid
        custInfo = getAccountInfos(taskId);
        // 获取字典信息,安检入户情况
        cmScAjrh_list = getDicdata();
        // // 获取字典信息,服务评价
        // cmMrComm_list = getCommInfodata();
        // 获取字典信息,全讲解情况字典
        cmScSpItem_list = getCmScSpItemdata();
        // 获取字典信息,隐患类型
        cmScShType_list = getYinHuanDate();
        // 查询上传表,初始化各选项
        uploadcustInfo = getUpLoadDate();
        // 读数类型

        typedata = getreadTypedata();
        // 获取当前用户上传状态
        state_up = getCustInfoState();
        // 当前用户照片
        try {
            imageUrl_List = selectPicture(taskId, custInfo.getCmSchedId());
        } catch (SQLiteException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "图片初始化失败", Toast.LENGTH_SHORT).show();
        }

        getPicUlrl();
        Message msg = Message.obtain();
        msg.what=200;
        mHandler.sendMessage(msg);
        
        // 设置隐患整改情况选项
        cmScYhzgdata = getCmScYhzgdata();


    }

    // 当前用户照片
    private ArrayList<String> selectPicture(String taskid2, String cmSchedId)
            throws SQLException {
        String select_pic_sql="select cmScFileRoute from perFile_aj where cmSchedId = '"
                + cmSchedId + "' and accountId = '" + taskid2 + "'";
//        DatabaseHelper conne=new DatabaseHelper(mContext);
//        Connection conne = SQLiteData.openOrCreateDatabase(getActivity());
//        PreparedStatement ps1 = conne
//                .prepareStatement();
//        SQLiteDatabase ps1 = conne.getWritableDatabase();

        Cursor set = instance.rawQuery(select_pic_sql,new String[]{});
        imageUrl_List = new ArrayList<String>();
        int a = 0;
        if (a < 2) {
            while (set.moveToNext()) {
                imageUrl_List.add(set.getString(0));
                a++;
            }
        }


        set.close();
//        instance.close();
//        conne.commit();
        return imageUrl_List;
    }

    // 隐患整改情况字典
    public ArrayList<CmScYhzg> getCmScYhzgdata() {
        String selectDic = "select dictionaryCode,dictionaryDescr from dictionaries where parentID = 'cmScYhzg' ";
        Cursor DicSet = null;
        CmScYhzg cmScYhzg = null;
        ArrayList<CmScYhzg> cmScSpItem_list = new ArrayList<CmScYhzg>();
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(selectDic,new String[]{});

            if (DicSet.moveToFirst()){

                cmScYhzg = new CmScYhzg();
                cmScYhzg.cmScYhzg = DicSet.getString(0);
                cmScYhzg.cmScYhzgDescr = DicSet.getString(1);
                cmScSpItem_list.add(cmScYhzg);
                while(DicSet.moveToNext()){
                    cmScYhzg = new CmScYhzg();
                    cmScYhzg.cmScYhzg = DicSet.getString(0);
                    cmScYhzg.cmScYhzgDescr = DicSet.getString(1);
                    cmScSpItem_list.add(cmScYhzg);
                }



            }

            Message msg = Message.obtain();
            msg.what = Over1;
            handler.sendMessage(msg);


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (DicSet != null) {
                try {
                    DicSet.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
        }
        return cmScSpItem_list;

    }

    ;

    // 获取当前用户上传状态
    public String getCustInfoState() {
        String sql1 = "select cmMrState from custInfo_ju_aj where accountId ='"
                + taskId + "' and cmSchedId = '" + custInfo.getCmSchedId()
                + "' ";
        Cursor DicSet = null;
        try {
//            conne=new DatabaseHelper(mContext);
////            conne = SQLiteData.openOrCreateDatabase(getActivity());
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(sql1,new String[]{});
            while (DicSet.moveToNext()) {
                stateString = DicSet.getString(0);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (DicSet != null) {
                try {
                    DicSet.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
        }
        return stateString;

    }

    ;

    // 查询上传表
    public UploadcustInfo_AnJian getUpLoadDate() {
        String sql1 = "select cmScAjrh,cmScZtjs,cmMr,cmScSyql,cmMrCommCd," +
                "cmScRemark,readType,cmScZgtzd,cmScResType,cmScUserType,cmScYhzg" +
                " from uploadcustInfo_aj where accountId ='"
                + taskId + "' and cmSchedId = '" + custInfo.getCmSchedId()
                + "' ";
        Cursor DicSet = null;
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(sql1,new String[]{});


            while (DicSet.moveToNext()) {
                uploadcustInfo = new UploadcustInfo_AnJian();
                uploadcustInfo.cmScAjrh = DicSet.getString(0);
                uploadcustInfo.cmScZtjs = DicSet.getString(1);
                uploadcustInfo.cmMr = DicSet.getString(2);
                uploadcustInfo.cmScSyql = DicSet.getString(3);
                uploadcustInfo.cmMrCommCd = DicSet.getString(4);
                uploadcustInfo.cmScRemark = DicSet.getString(5);
                uploadcustInfo.readType = DicSet.getString(6);
                uploadcustInfo.cmScZgtzd = DicSet.getString(7);
                uploadcustInfo.cmScResType = DicSet.getString(8);
                uploadcustInfo.cmScUserType = DicSet.getString(9);
                uploadcustInfo.cmScYhzg = DicSet.getString(10);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (DicSet != null) {
                try {
                    DicSet.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
        }
        return uploadcustInfo;

    }

    ;

    // 客户隐患类型,去重
    public ArrayList<CmScShType_Dic> getYinHuanDate() {
        String selectDic = "select distinct cmScShType,cmScShTypeDescr from dic_cmScShItem_aj ";
        Cursor DicSet = null;
        CmScShType_Dic cmScShType = null;
        cmScShType_list = new ArrayList<CmScShType_Dic>();
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(selectDic,new String[]{});
            while (DicSet.moveToNext()) {
                cmScShType = new CmScShType_Dic();
                cmScShType.cmScShType = DicSet.getString(0);
                cmScShType.cmScShTypeDescr = DicSet
                        .getString(1);
                cmScShType_list.add(cmScShType);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (DicSet != null) {
                try {
                    DicSet.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
        }
        return cmScShType_list;

    }

    ;

    // 读数类型
    public ArrayList<readType> getreadTypedata() {
        String selectDic = "select dictionaryCode,dictionaryDescr from dictionaries where parentID = 'readType' ";
        Cursor DicSet = null;
        readType readTy = null;
        readType_list = new ArrayList<readType>();
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(selectDic,new String[]{});
            while (DicSet.moveToNext()) {
                readTy = new readType();
                readTy.readType = DicSet.getString(0);
                readTy.readTypeDescr = DicSet.getString(1);
                readType_list.add(readTy);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (DicSet != null) {
                try {
                    DicSet.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
        }
        return readType_list;

    }

    ;

    // 客户安全讲解情况字典
    public ArrayList<CmScSpItem> getCmScSpItemdata() {
        String selectDic = "select  dictionaryCode,dictionaryDescr from dictionaries where parentID = 'cmScSpItem' ";
        Cursor DicSet = null;
        CmScSpItem cmScSpItem = null;
        cmScSpItem_list = new ArrayList<CmScSpItem>();
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(selectDic,new String[]{});
            while (DicSet.moveToNext()) {
                cmScSpItem = new CmScSpItem();
                cmScSpItem.cmScSpItem = DicSet.getString(0);
                cmScSpItem.cmScSpItemDescr = DicSet
                        .getString(1);
                cmScSpItem_list.add(cmScSpItem);
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
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
        }
        return cmScSpItem_list;

    }

    ;

    // 安检入户情况字典
    public ArrayList<CmScAjrh_Dic> getDicdata() {
        String selectDic = "select dictionaryCode,dictionaryDescr from dictionaries where parentID = 'cmScAjrh' ";
        Cursor DicSet = null;
        CmScAjrh_Dic cmScAjrh = null;
        cmScAjrh_list = new ArrayList<CmScAjrh_Dic>();
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(selectDic,new String[]{});
            while (DicSet.moveToNext()) {
                cmScAjrh = new CmScAjrh_Dic();
                cmScAjrh.cmScAjrh = DicSet.getString(0);
                cmScAjrh.cmScAjrhDescr = DicSet.getString(1);
                cmScAjrh_list.add(cmScAjrh);
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
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
        }
        return cmScAjrh_list;

    }

    ;

    // 服务评价字典
    public ArrayList<CmMrComm_Dic> getCommInfodata() {
        String selectDic = "select dictionaryCode,dictionaryDescr from dictionaries where parentID = 'cmMrComm' ";
        Cursor DicSet = null;
        CmMrComm_Dic cmMrComm = null;
        cmMrComm_list = new ArrayList<CmMrComm_Dic>();
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(selectDic,new String[]{});
            while (DicSet.moveToNext()) {
                cmMrComm = new CmMrComm_Dic();
                cmMrComm.cmMrCommCd = DicSet.getString(0);
                cmMrComm.cmMrCommDescr = DicSet.getString(1);
                cmMrComm_list.add(cmMrComm);
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
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
        }
        return cmMrComm_list;

    }

    ;

    public CustInfo_AnJian getAccountInfos(String taskId) {

        String selectAccount = "select entityName,cmMrAddress,cmMrCommunity,cmMrBuilding,cmMrUnit,cmMrRoomNum,meterType," +
                "manufacturer,model,serialNumber,cmMrMtrBarCode,cmMlr,cmScLgfmGj,cmScLgfmWz," +
                "cmScLgfmCz,cmScZjPp,cmScZjYs,cmScZjXhbh,cmScZjSyrq,cmScLjgCz,cmScCnlPp," +
                "cmScCnlPffs,cmScCnlSyrq,cmScRsqPp,cmScRsqPffs,cmScRsqSyrq," +
                "cmScBjqPp,cmScBjqSyrq,cmMrLastSecchkDt,cmScIntvl,cmScAqyh,cmScYhzg," +
                "cmSchedId,accountId,customerClass,cmCustClDescr,servicePointId from custInfo_ju_aj where accountId = '"
                + taskId + "'"+" and cmSchedId = '"+cmSchedIDPage+"'";//cmSchedIDPage
        Cursor accountSet = null;
        try {
            long c = System.currentTimeMillis();
            Log.e("开始插入数据----------》",String.valueOf(c));
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            long c1= System.currentTimeMillis();
            Log.e("开始插入数据----------》",String.valueOf(c1));
            Log.e("开始插入数据----------》",String.valueOf(c1-c));
            SimpleDateFormat sdfTwo = new SimpleDateFormat("mm:ss:SSS",
                    Locale.getDefault());
            String time11 = sdfTwo.format(c1-c);
            Log.e("开始插入数据----------》",String.valueOf(time11));
            long c2 = System.currentTimeMillis();
            accountSet = instance.rawQuery(selectAccount,new String[]{});
            custInfo = new CustInfo_AnJian();
            while (accountSet.moveToNext()) {
                custInfo.entityName = accountSet.getString(0);
                custInfo.cmMrAddress = accountSet.getString(1);
                custInfo.cmMrCommunity = accountSet.getString(2);
                custInfo.cmMrBuilding = accountSet.getString(3);
                custInfo.cmMrUnit = accountSet.getString(4);
                custInfo.cmMrRoomNum = accountSet.getString(5);
                custInfo.meterType = accountSet.getString(6);
                custInfo.manufacturer = accountSet.getString(7);
                custInfo.model = accountSet.getString(8);
                custInfo.serialNumber = accountSet.getString(9);
                custInfo.cmMrMtrBarCode = accountSet
                        .getString(10);
                custInfo.cmMlr = accountSet.getString(11);
                custInfo.cmScLgfmGj = accountSet.getString(12);
                custInfo.cmScLgfmWz = accountSet.getString(13);
                custInfo.cmScLgfmCz = accountSet.getString(14);
                custInfo.cmScZjPp = accountSet.getString(15);
                custInfo.cmScZjYs = accountSet.getString(16);
                custInfo.cmScZjXhbh = accountSet.getString(17);
                custInfo.cmScZjSyrq = accountSet.getString(18);
                custInfo.cmScLjgCz = accountSet.getString(19);
                custInfo.cmScCnlPp = accountSet.getString(20);
                custInfo.cmScCnlPffs = accountSet.getString(21);
                custInfo.cmScCnlSyrq = accountSet.getString(22);
                custInfo.cmScRsqPp = accountSet.getString(23);
                custInfo.cmScRsqPffs = accountSet.getString(24);
                custInfo.cmScRsqSyrq = accountSet.getString(25);
                custInfo.cmScBjqPp = accountSet.getString(26);
                custInfo.cmScBjqSyrq = accountSet.getString(27);
                custInfo.cmMrLastSecchkDt = accountSet
                        .getString(28);
                custInfo.cmScIntvl = accountSet.getString(29);
                custInfo.cmScAqyh = accountSet.getString(30);
                custInfo.cmScYhzg = accountSet.getString(31);

                custInfo.cmSchedId = accountSet.getString(32);
                custInfo.accountId = accountSet.getString(33);
                custInfo.customerClass = accountSet.getString(34);
                custInfo.cmCustClDescr = accountSet.getString(35);
                custInfo.servicePointId = accountSet
                        .getString(36);
            }
            long c3= System.currentTimeMillis();
            SimpleDateFormat sdfTwo1 = new SimpleDateFormat("mm:ss:SSS",
                    Locale.getDefault());
            String time111 = sdfTwo1.format(c3-c2);
            Log.e("开始插入数据----------》",String.valueOf(time11));

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (accountSet != null) {
                try {
                    accountSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return custInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(mContext, R.layout.anjian_approve_detail_child_new,
                null);



        return view;
    }

    private void initPhoto() {
        // 初始化图片
        if (imageUrl_List.size() > 0) {
            if (imageUrl_List.size() == 1) {
                // 一张图片
                image1.setImageBitmap(BitmapFactory.decodeFile(sdPath1
                        + imageUrl_List.get(0)));
                tv_morephoto.setVisibility(View.INVISIBLE);
                image2.setVisibility(View.GONE);
            } else {
                // 两张或更多
                image2.setVisibility(View.VISIBLE);
                image1.setImageBitmap(BitmapFactory.decodeFile(sdPath1
                        + imageUrl_List.get(0)));
                image2.setImageBitmap(BitmapFactory.decodeFile(sdPath1
                        + imageUrl_List.get(1)));
                tv_morephoto.setVisibility(View.VISIBLE);
                tv_morephoto.setTextColor(Color.BLACK);
            }

        } else {
            // 当前用户没有图片
            image1.setImageResource(R.drawable.zhaopian);
            image2.setVisibility(View.GONE);
            tv_morephoto.setVisibility(View.INVISIBLE);
        }
    }

    // 拍照
    private void createPhoto_new(LinearLayout layout) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        final String time_now = format.format(new Date()).replace("-", "");
        View userView = layoutInflater.inflate(R.layout.cb_photo_layout, null,
                false);
        imageGrid = (GridView) userView.findViewById(R.id.GridView01);

        photoButton = (Button) userView.findViewById(R.id.btn_take_photo);
        photoButton.setText("拍照");
        photoButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated
                // method stub
                if (imgAdapter != null) {

                    if (imgAdapter.getCount() == MAX_PICS) {
                        Toast.makeText(getActivity(),
                                "最多可拍" + MAX_PICS + "张照片", Toast.LENGTH_SHORT).show();
                    } else {
                        // imagePath = sdPath + FileUtil.getUUid() + ".jpg";
                        SimpleDateFormat format1 = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        String time = format1.format(new Date())
                                .replace("-", "").replace(":", "");
                        imagePath = sdPath + time_now + "/"
                                + Constants.loginName + "/"
                                + custInfo.getServicePointId() + "_" + time
                                + ".jpg";

                        File imageFile = new File(imagePath);
                        if (!imageFile.getParentFile().exists())
                            imageFile.getParentFile().mkdirs();
                        imageURI = Uri.fromFile(imageFile);
                        Intent i = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        i.putExtra(MediaStore.EXTRA_OUTPUT,
                                imageURI);
                        startActivityForResult(i, CAMERA_RESULT);
                    }
                }
            }
        });
        imageGrid.setPadding(
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10));
        // if (content.contains("(photo)2")) {
        // photoButton.setVisibility(View.GONE);
        // imgList.clear();
        // if (replacePhoto1(content) != "") {
        // if (replacePhoto1(content).contains(separator)) {
        // String[] strings = replacePhoto1(content).split(separator);
        // imgList.clear();
        // for (int j = 0; j < strings.length; j++) {
        // PhotoAudio photoAudio = new PhotoAudio();
        // photoAudio.setUrl(strings[j]);
        // imgList.add(photoAudio);
        // }
        // } else {
        // PhotoAudio photoAudio = new PhotoAudio();
        // photoAudio.setUrl(replacePhoto1(content));
        // imgList.add(photoAudio);
        // }
        //
        // }
        // }
        initViewPhoto();
        initAdapterIMG();
        layout.addView(userView);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        // new ApproveDetailTask().execute(taskId, ""); // 下载数据
    }

    // 获取详细数据
    private class ApproveDetailTask extends
            AsyncTask<String, Void, SinopecApproveDetailEntry> {
        private ProgressHUD overlayProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            overlayProgress = AlertUtils.showDialog(getActivity(), null, this,
                    false);
            overlayProgress.setCanceledOnTouchOutside(false);
            // ll_dialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(final SinopecApproveDetailEntry result) {
            super.onPostExecute(result);
            if (overlayProgress != null)
                overlayProgress.dismiss();
            // ll_dialog.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (result != null) {
                        if (result.form != null) {
                            elv_details.setAdapter(new MyAdapter(
                                    result.form.sinopecTables));
                            for (int i = 0; i < result.form.sinopecTables
                                    .size(); i++) {
                                SinopecTable table = result.form.sinopecTables
                                        .get(i);
                                if ("".equalsIgnoreCase(table.expand)
                                        || "true"
                                        .equalsIgnoreCase(table.expand)) {
                                    elv_details.expandGroup(i);
                                }
                            }
                            // if (result.operater != null
                            // && result.operater.sinopecTables.size() > 0) {
                            // // bt_next.setVisibility(View.VISIBLE);
                            // btn_next.setVisibility(View.GONE);
                            // } else {
                            // // bt_next.setVisibility(View.GONE);
                            // btn_next.setVisibility(View.GONE);
                            // }
                        } else {
                            // Toast.makeText(mContext, "请在PC端处理",
                            // Toast.LENGTH_SHORT).show();
                            Toast.makeText(mContext, "已经是第一户了",
                                    Toast.LENGTH_SHORT).show();
                            // btn_next.setVisibility(View.GONE);
                        }
                        /*if (tabType.equals("dy")) { // 如果类型是待阅的
                            // 刷新状态栏和刷新数据
                            // sinopecApproveOAFragment.refresh_DY_Data();
                            if (!isBatch) {// 判断是否是批量审批
                                ((SinopecApproveFragment) sinopecApproveFragment)
                                .searchData();
                            } else
                                ((SinopecApproveBatchFragment) sinopecApproveFragment)
                                        .searchData();
                        }*/
                    }
                    // 添加到必填集合里面
                    addRequiredList();
                }
            }, 200);
            // new Handler().postDelayed(new Runnable() {
            //
            // @Override
            // public void run() {
            // if (result != null) {
            // if (result.other != null) {
            // if (!result.other.currNode.equals("")) {
            // currNote = result.other.currNode;
            // }
            // if (!result.other.displayDevice.equals("")) {
            //
            // displayDevice = result.other.displayDevice;
            // }
            // if (!result.other.deviceResult.equals("")) {
            //
            // deviceResult = result.other.deviceResult;
            // }
            // // if (result.other.currNode.equals("1")) {
            // // btn_next.setText("登记");
            // // } else if (result.other.currNode.equals("2")) {
            // // btn_next.setText("工单执行");
            // // } else if (result.other.currNode.equals("3")) {
            // // btn_next.setText("销记");
            // // }
            // // Constants.Change_Num = result.other.currNode;
            // // btn_save.setVisibility(View.VISIBLE);
            // btn_next.setVisibility(View.VISIBLE);
            // } else {
            // btn_save.setVisibility(View.GONE);
            // btn_next.setVisibility(View.GONE);
            // }
            // }
            // }
            // }, 200);
        }

        @Override
        protected SinopecApproveDetailEntry doInBackground(String... params) {
            // TODO Auto-generated method stub
            InputStream inputStream = null;
            try {
                // inputStream =
                // getActivity().getAssets().open("7539000000.xml");
                String a = params[0];
                String b = params[1];
                selectList(a, b, tabType);
                inputStream = new ByteArrayInputStream(sb.toString().getBytes(
                        "UTF-8"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return detailEntry = DataCollectionUtils.receiveApproveDetailDom(
                    inputStream, listPath, tabType, taskId);
        }
    }

    // 查询待办数据
    public DatabaseHelper db;// 操作数据库的工具类
    StringBuilder sb;

    public void selectList(String user_id, String isnext, String type) {
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))

        db = new DatabaseHelper(mContext);
        String sqlstring = "select rowid from anjian_data where user_id=" + "'"
                + user_id + "'";
        // 查询数据返回游标对象
        SQLiteDatabase writableDatabase = db.getWritableDatabase();
        Cursor c1 = writableDatabase.rawQuery(sqlstring, new String[]{});
        int id = 0;
        while (c1.moveToNext()) {
            id = c1.getInt(0);
        }
        c1.close();
        String sql = "";
        if (isnext.equals("")) {
            sql = "select distinct * from anjian_data where user_id=" + "'"
                    + user_id + "'";
        } else {
            if (isnext.equalsIgnoreCase("next")) {
                sql = "select distinct * from anjian_data where rowid > " + "'"
                        + +id + "'" + " and state=" + "'" + type + "'"
                        + " limit 0,1";
            } else {
                sql = "select distinct * from anjian_data where rowid < " + "'"
                        + id + "'" + " and state=" + "'" + type + "'"
                        + " ORDER BY rowid DESC" + " limit 0,1";
            }

        }

        // 查询数据返回游标对象
        Cursor c = null;
        c = db.getWritableDatabase().rawQuery(sql, null);

        // 到了提交的时刻了
        sb = new StringBuilder();
        if (c.getCount() == 0) {
            if (isnext.equals("")) {
                sql = "select distinct * from chaobiao_data where user_id="
                        + "'" + user_id + "'";
                c = db.getWritableDatabase().rawQuery(sql, null);
            } else {
                return;
            }

        }
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root><form>");
        while (c.moveToNext()) {
            // 通过游标对象获取值
            taskId = c.getString(0);
            if (type.equals("1")) {
                // sb.append("<table id=" + "\"1\"" + " title=" + "\"基本信息\""
                // + " expand=" + "\"false\"" + " editfield=" + "\"true\""
                // + " columns=" + "\"2\"" + ">");
                // sb.append("<tr parent=\"\"" + "><td>" + "客户名称" + "</td><td>"
                // + c.getString(1) + "(user)" + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "地址" + "</td><td>"
                // + c.getString(4) + c.getString(5) + c.getString(6)
                // + c.getString(7) + "栋" + c.getString(8) + "单元"
                // + c.getString(9) + "室" + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "抄表时间" + "</td><td>"
                // + c.getString(19) + "(date)" + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "表字" + "</td><td>"
                // + c.getString(21) + "(biaozi)" + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "表字类型" + "</td><td>"
                // + c.getString(20) + "(bztype)" + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "气量" + "</td><td>"
                // + c.getString(22) + "|" + c.getString(18) + "(qiliang)"
                // + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "本期气费" + "</td><td>"
                // + c.getString(16) + "(qifei)" + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "历史欠费" + "</td><td>"
                // + c.getString(25) + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "拍照" + "</td><td>"
                // + c.getString(27) + "(photo)" + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "录音" + "</td><td>"
                // + c.getString(28) + "(audio)" + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "备注" + "</td><td>"
                // + c.getString(32) + "(beizhu)" + "</td></tr>");
                // sb.append("</table>");
                sb.append("<table id=" + "\"1\"" + " title=" + "\"安检信息\""
                        + " expand=" + "\"true\"" + " editfield=" + "\"true\""
                        + " columns=" + "\"1\"" + ">");
                sb.append("<tr parent=\"\"" + "><td>" + c.getString(10) + ";#"
                        + c.getString(1) + ";#" + c.getString(4)
                        + c.getString(5) + c.getString(6) + c.getString(7)
                        + "栋" + c.getString(8) + "单元" + c.getString(9) + "室"
                        + ";#" + "(anjian)" + "</td></tr>");
                sb.append("</table>");
            } else {
                sb.append("<table id=" + "\"1\"" + " title=" + "\"基本信息\""
                        + " expand=" + "\"true\"" + " editfield=" + "\"true\""
                        + " columns=" + "\"2\"" + ">");
                sb.append("<tr parent=\"\"" + "><td>" + "客户名称" + "</td><td>"
                        + c.getString(1) + "(user)" + "</td></tr>");
                sb.append("<tr parent=\"\"" + "><td>" + "地址" + "</td><td>"
                        + c.getString(4) + c.getString(5) + c.getString(6)
                        + c.getString(7) + "栋" + c.getString(8) + "单元"
                        + c.getString(9) + "号" + "</td></tr>");
                sb.append("</table>");
                sb.append("<table id=" + "\"1\"" + " title=" + "\"安检信息\""
                        + " expand=" + "\"true\"" + " editfield=" + "\"true\""
                        + " columns=" + "\"2\"" + ">");
                sb.append("<tr parent=\"\"" + "><td>" + "安检日期" + "</td><td>"
                        + c.getString(13) + "</td></tr>");
                sb.append("<tr parent=\"\"" + "><td>" + "完成时间" + "</td><td>"
                        + c.getString(15) + "</td></tr>");
                sb.append("<tr parent=\"\"" + "><td>" + "安检入户情况" + "</td><td>"
                        + c.getString(16) + "</td></tr>");
                sb.append("<tr parent=\"\"" + "><td>" + "计量表读数" + "</td><td>"
                        + c.getString(19) + "</td></tr>");
                sb.append("<tr parent=\"\"" + "><td>" + "是否存在安全隐患"
                        + "</td><td>" + c.getString(41) + "</td></tr>");
                sb.append("<tr parent=\"\"" + "><td>" + "安全隐患整改通知单编号"
                        + "</td><td>" + c.getString(42) + "</td></tr>");
                sb.append("<tr parent=\"\"" + "><td>" + "备注" + "</td><td>"
                        + c.getString(43) + "</td></tr>");
                sb.append("<tr parent=\"\"" + "><td>" + "拍照" + "</td><td>"
                        + c.getString(48) + "(photo)2" + "</td></tr>");
                sb.append("<tr parent=\"\"" + "><td>" + "录音" + "</td><td>"
                        + c.getString(49) + "(audio)2" + "</td></tr>");
                sb.append("</table>");
                // sb.append("<table id=" + "\"1\"" + " title=" + "\"安检信息\""
                // + " expand=" + "\"true\"" + " editfield=" + "\"true\""
                // + " columns=" + "\"1\"" + ">");
                // sb.append("<tr parent=\"\"" + "><td>" + c.getString(10)
                // + "(anjian)" + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "拍照" + "</td><td>"
                // + c.getString(48) + "(photo)2" + "</td></tr>");
                // sb.append("<tr parent=\"\"" + "><td>" + "录音" + "</td><td>"
                // + c.getString(49) + "(audio)2" + "</td></tr>");
                // sb.append("</table>");
            }

        }
        c.close();
        sb.append("</form></root>");
        System.out.println(sb.toString());
    }

    public class MyAdapter extends BaseExpandableListAdapter {

        List<SinopecTable> groupList = new ArrayList<SinopecTable>();
        List<List<SinopecTR>> childList = new ArrayList<List<SinopecTR>>();
        LayoutParams fwParams;

        public MyAdapter(List<SinopecTable> sinopecTables) {
            fwParams = new LayoutParams(-1, -2);
            groupList = sinopecTables;
            for (int i = 0; i < groupList.size(); i++) {
                SinopecTable sinopecTable = groupList.get(i);
                List<SinopecTR> subChildList = null;
                subChildList = new ArrayList<SinopecTR>();
                for (int j = 0; j < sinopecTable.trs.size(); j++) {
                    SinopecTR sinopecTR = sinopecTable.trs.get(j);
                    boolean bool = false;
                    for (int k = 0; k < sinopecTR.tds.size(); k++) {
                        SinopecTD sinopecTD = sinopecTR.tds.get(k);
                        if (sinopecTD.sinopecViews == null
                                || sinopecTD.sinopecViews.size() == 0) {

                        } else {
                            if (sinopecTD.sinopecViews.get(0) instanceof UIInput) {
                                UIInput uiInput = (UIInput) sinopecTD.sinopecViews
                                        .get(0);
                                if ("hidden".equalsIgnoreCase(uiInput.type)) {
                                    bool = true;
                                }
                            }
                        }
                    }
                    if (bool == false) {
                        subChildList.add(sinopecTR);
                    }
                }
                childList.add(subChildList);
            }
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            int columns = Integer
                    .parseInt(groupList.get(groupPosition).columns);
            // int columns = 2;//这里有bug 先这样吧
            if (columns > 2 && childList.get(groupPosition).size() >= 1) {
                return 1;
            } else {
                return childList.get(groupPosition).size();
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            View view = getActivity().getLayoutInflater().inflate(
                    R.layout.item_approve_detail_group, parent, false);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            ImageView iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
            tv_title.setText(groupList.get(groupPosition).title);

            // view.setBackgroundResource(R.drawable.approve_detail_group_bg);
            // if (isExpanded) {
            // iv_pic.setBackgroundResource(R.drawable.approve_detail_group_1);
            // } else {
            // iv_pic.setBackgroundResource(R.drawable.approve_detail_group_2);
            // }
            if (isExpanded) {
                iv_pic.setBackgroundResource(R.drawable.mail_noadd);
            } else {
                iv_pic.setBackgroundResource(R.drawable.mail_add);
            }
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = null;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.item_approve_detail_child2, parent, false);
                holder = new Holder();
                holder.ll_parent = (LinearLayout) convertView
                        .findViewById(R.id.ll_parent);
                holder.ll_left = (LinearLayout) convertView
                        .findViewById(R.id.ll_left);
                holder.ll_right = (LinearLayout) convertView
                        .findViewById(R.id.ll_right);
                holder.ll_scroll = (LinearLayout) convertView
                        .findViewById(R.id.ll_scroll);
                holder.ll_scrll_left = (LinearLayout) convertView
                        .findViewById(R.id.ll_scrll_left);
                holder.hsv = (HorizontalScrollView) convertView
                        .findViewById(R.id.hsv);
                holder.ll_content = (LinearLayout) convertView
                        .findViewById(R.id.ll_content);
                holder.vv_height = (View) convertView
                        .findViewById(R.id.vv_height);
                holder.two_line = (View) convertView
                        .findViewById(R.id.two_line);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            int columns = Integer
                    .parseInt(groupList.get(groupPosition).columns);
            // int columns = 2;//这里有bug
            SinopecTR sinopecTR = childList.get(groupPosition).get(
                    childPosition);
            // 获取列的集合
            List<SinopecTD> sinopecTDList = sinopecTR.tds;
            // 获取下该table是不是设备列表
            String deviceList = groupList.get(groupPosition).title;
            System.out
                    .println(deviceList + "=======================deviceList");
            if (deviceList.equals("设备列表")) {
                String content = "";
                // 如果是1列，表示内容为html格式
                holder.ll_parent.setVisibility(View.VISIBLE);
                holder.vv_height.setVisibility(View.GONE);
                holder.ll_left.setVisibility(View.VISIBLE);
                holder.ll_right.setVisibility(View.GONE); //
                holder.ll_scroll.setVisibility(View.GONE); // 隐藏滚动视图
                holder.ll_left.removeAllViews(); // 移除所有的视图
                holder.two_line.setVisibility(View.GONE);
                SinopecTD sinopecTD = sinopecTDList.get(0);
                content = sinopecTD.content.toString().trim();
                if (!"".equals(content)) {
                    final String[] strings = content.split("\\|");
                    System.out.println(content + "=====content====="
                            + strings[1]);
                    LinearLayout layout = (LinearLayout) layoutInflater
                            .inflate(R.layout.polling_device_item_layout, null,
                                    false);
                    // 设备名称
                    TextView device_name = (TextView) layout
                            .findViewById(R.id.device_name);
                    // 设备信息
                    TextView device_info = (TextView) layout
                            .findViewById(R.id.device_info);
                    // 导航
                    Button device_navigation = (Button) layout
                            .findViewById(R.id.device_navigation);
                    // 作业
                    Button device_homework = (Button) layout
                            .findViewById(R.id.device_homework);
                    // 设备状态
                    ImageView device_state = (ImageView) layout
                            .findViewById(R.id.device_state);

                    device_name.setText("名称:" + strings[1].toString().trim()
                            + "(" + strings[0].toString().trim() + ")");
                    device_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
                    device_name.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                          /*  SinopecApproveOADetailFormFragment formFragment = new SinopecApproveOADetailFormFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("formpath", deviceDetailPath);
                            bundle.putString("htmlPath", htmlPath);
                            bundle.putString("filePath", filePath);
                            bundle.putString("key", strings[0].toString()
                                    .trim());
                            formFragment.setArguments(bundle);
                            ((ActivityInTab) getActivity())
                                    .navigateTo(formFragment);*/
                        }
                    });
                    device_info.setText(strings[4].toString().trim());
                    if (strings[2].toString().trim().equals("0")) {
                        device_state.setBackgroundResource(R.drawable.daijian);
                    } else {
                        device_state.setBackgroundResource(R.drawable.daijian2);
                    }
                    // 导航
                    device_navigation.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Toast.makeText(getActivity(), "暂不支持此功能", Toast.LENGTH_LONG).show();
                        }
                    });
                    // 作业
                    device_homework.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            // btn_next.performClick();
                            nextOperation(strings[0].toString().trim());
                            // Toast.makeText(getActivity(), "暂不支持此功能",
                            // 1).show();
                        }
                    });
                    holder.ll_left.addView(layout);
                }
            }
            if (columns == 1 && !deviceList.equals("设备列表")) { // 如果是1列，表示内容为html格式
                holder.ll_parent.setVisibility(View.VISIBLE);
                holder.vv_height.setVisibility(View.GONE);
                holder.ll_left.setVisibility(View.VISIBLE);
                holder.ll_right.setVisibility(View.GONE); //
                holder.ll_scroll.setVisibility(View.GONE); // 隐藏滚动视图
                holder.two_line.setVisibility(View.GONE);
                holder.ll_left.removeAllViews(); // 移除所有的视图
                for (int i = 0; i < sinopecTDList.size(); i++) {
                    SinopecTD sinopecTD = sinopecTDList.get(i);
                    if (sinopecTD.content != null
                            && sinopecTD.content.contains("(html)")) {
                        createWebView(holder.ll_left, sinopecTD.content);
                    } else if (sinopecTD.content != null
                            && sinopecTD.content.contains("(anjian)")) {
                        // 安检
                        createAnJianView(holder.ll_left, sinopecTD.content);
                    } else {
                        // 创建视图
                        TextView textView = new TextView(getActivity());
                        textView.setLayoutParams(new LayoutParams(
                                -1, -1));
                        holder.ll_left.addView(textView);
                        if ((sinopecTD.sinopecViews == null || sinopecTD.sinopecViews
                                .size() == 0)) { // 表示没有input视图
                            textView.setTextColor(Color.parseColor("#000000"));
                            textView.setText((sinopecTD.content
                                    .contains("(html)") ? replaceHtml(sinopecTD.content)
                                    : sinopecTD.content));
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            textView.setPadding(
                                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                            getActivity(), 20),
                                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                            getActivity(), 10),
                                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                            getActivity(), 10),
                                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                            getActivity(), 10));
                            textView.setTag(null);
                        } else {
                            for (int j = 0; j < sinopecTD.sinopecViews.size(); j++) {
                                if (sinopecTD.sinopecViews.get(j) instanceof UIInput) {
                                    UIInput uiInput = (UIInput) sinopecTD.sinopecViews
                                            .get(j);
                                    if (!"text".equalsIgnoreCase(uiInput.type)) {
                                        addInputView(textView, uiInput);
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (columns == 2) { // 两列
                holder.ll_parent.setVisibility(View.VISIBLE);
                holder.vv_height.setVisibility(View.GONE);
                holder.ll_left.setVisibility(View.VISIBLE);
                holder.ll_right.setVisibility(View.VISIBLE); //
                holder.ll_scroll.setVisibility(View.GONE); // 隐藏滚动视图
                holder.ll_left.removeAllViews();
                holder.ll_right.removeAllViews();
                holder.two_line.setVisibility(View.VISIBLE);

                int count = 0;
                for (int i = 0; i < sinopecTDList.size(); i++) {
                    if (TextUtils.isEmpty(sinopecTDList.get(i).content)
                            && sinopecTDList.get(i).sinopecViews.size() == 0) {
                        count++;
                    }
                }
                if (count == 2) {
                    holder.ll_parent.setVisibility(View.GONE);
                    holder.vv_height.setVisibility(View.VISIBLE);
                    return convertView;
                }

                for (int i = 0; i < sinopecTDList.size(); i++) {
                    SinopecTD sinopecTD = sinopecTDList.get(i); // 获取一个td
                    if ((sinopecTD.sinopecViews == null || sinopecTD.sinopecViews
                            .size() == 0)) { // 表示没有input视图
                        if (i == 0) {
                            if (sinopecTD.content.equals("1")
                                    || sinopecTD.content.equals("0")) {
                                ImageView imageView = new ImageView(
                                        getActivity());
                                imageView
                                        .setLayoutParams(new LayoutParams(
                                                80, 80));
                                imageView.setPadding(
                                        cn.sbx.deeper.moblie.util.TextUtils
                                                .Dp2Px(getActivity(), 20),
                                        cn.sbx.deeper.moblie.util.TextUtils
                                                .Dp2Px(getActivity(), 10),
                                        cn.sbx.deeper.moblie.util.TextUtils
                                                .Dp2Px(getActivity(), 10),
                                        cn.sbx.deeper.moblie.util.TextUtils
                                                .Dp2Px(getActivity(), 10));
                                if (sinopecTD.content.equals("0")) {
                                    imageView
                                            .setBackgroundResource(R.drawable.ic_item_not_selected);
                                } else {
                                    imageView
                                            .setBackgroundResource(R.drawable.ic_item_selected);
                                }
                                holder.ll_left.addView(imageView);
                            } else {
                                // 创建视图
                                TextView textView = new TextView(getActivity());
                                textView.setLayoutParams(new LayoutParams(
                                        -1, -1));
                                holder.ll_left.addView(textView);

                                textView.setTextColor(Color
                                        .parseColor("#3773c0"));
                                textView.setText((sinopecTD.content
                                        .contains("(html)") ? replaceHtml(sinopecTD.content)
                                        : sinopecTD.content));
                                textView.setTextSize(
                                        TypedValue.COMPLEX_UNIT_SP, 16);
                                textView.setPadding(
                                        cn.sbx.deeper.moblie.util.TextUtils
                                                .Dp2Px(getActivity(), 20),
                                        cn.sbx.deeper.moblie.util.TextUtils
                                                .Dp2Px(getActivity(), 10),
                                        cn.sbx.deeper.moblie.util.TextUtils
                                                .Dp2Px(getActivity(), 10),
                                        cn.sbx.deeper.moblie.util.TextUtils
                                                .Dp2Px(getActivity(), 10));
                                textView.setTag(null);
                            }

                        } else {
                            if (sinopecTD.content.contains("(html)")) {
                                createWebView(holder.ll_right,
                                        sinopecTD.content);
                            } else if (sinopecTD.content.contains("(image)")) {
                                createImageWebView(holder.ll_right,
                                        sinopecTD.content);
                            } else {
                                // 创建视图 第二列
                                if (sinopecTD.content.contains("(user)")) {
                                    View userView = layoutInflater.inflate(
                                            R.layout.cb_user_layout, null,
                                            false);
                                    TextView user_name = (TextView) userView
                                            .findViewById(R.id.user_name1);
                                    user_name.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    user_name
                                            .setText(replaceUser(sinopecTD.content));
                                    userView.setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", taskId);
                                            Fragment f = new AnJianUserInfoFragment(
                                                    backToList, targetContainer);
                                            f.setArguments(bundle);
                                            ((ActivityInTab) getActivity())
                                                    .navigateTo(f);

                                        }
                                    });
                                    holder.ll_right.addView(userView);
                                } else if (sinopecTD.content.contains("(date)")) {

                                    View userView = layoutInflater.inflate(
                                            R.layout.cb_date_layout, null,
                                            false);
                                    TextView now_date = (TextView) userView
                                            .findViewById(R.id.now_date);
                                    TextView sc_date = (TextView) userView
                                            .findViewById(R.id.sc_date);
                                    now_date.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    sc_date.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    SimpleDateFormat sDateFormat = new SimpleDateFormat(
                                            "yyyy-MM-dd");
                                    String date = sDateFormat
                                            .format(new Date());
                                    bq_cb_data = date;
                                    now_date.setText(date);
                                    sc_date.setText(replaceDate(sinopecTD.content));
                                    holder.ll_right.addView(userView);

                                } else if (sinopecTD.content
                                        .contains("(bztype)")) {

                                    View userView = layoutInflater.inflate(
                                            R.layout.cb_bz_type_layout, null,
                                            false);
                                    Spinner spinner = (Spinner) userView
                                            .findViewById(R.id.spinner);
                                    // final String[] options =
                                    // selectSpinnerData();
                                    final String[] options = new String[3];
                                    options[0] = "入户";
                                    options[1] = "估读";
                                    options[2] = "自抄";
                                    spinner.setBackgroundResource(R.drawable.ic_approve_spinner_background);
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                            mContext, R.layout.spinner_item,
                                            options);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner.setAdapter(adapter);
                                    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                                        @Override
                                        public void onItemSelected(
                                                AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                                            spinner_data = options[arg2];
                                        }

                                        @Override
                                        public void onNothingSelected(
                                                AdapterView<?> arg0) {

                                        }
                                    });
                                    TextView sc_ds_type = (TextView) userView
                                            .findViewById(R.id.ls_bz_tv);
                                    spinner.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    sc_ds_type.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    sc_ds_type
                                            .setText(replaceBZType(sinopecTD.content));
                                    holder.ll_right.addView(userView);

                                } else if (sinopecTD.content
                                        .contains("(biaozi)")) {

                                    View userView = layoutInflater.inflate(
                                            R.layout.cb_biaozi_layout, null,
                                            false);
                                    bz_edit = (EditText) userView
                                            .findViewById(R.id.bz_edit);
                                    ls_bz_tv = (TextView) userView
                                            .findViewById(R.id.ls_bz_tv);
                                    if (!bz_text.equals("")) {
                                        bz_edit.setText(bz_text);
                                    }
                                    bz_edit.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    ls_bz_tv.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    ls_bz_tv.setText(replaceLSBZ(sinopecTD.content));

                                    bz_edit.setOnFocusChangeListener(new OnFocusChangeListener() {

                                        @Override
                                        public void onFocusChange(View v,
                                                                  boolean hasFocus) {
                                            if (!hasFocus) {
                                                bz_text = bz_edit.getText()
                                                        .toString().trim();
                                                if (!"".equals(bz_edit
                                                        .getText().toString()
                                                        .trim())) {
                                                    qiliang.setText((Float
                                                            .parseFloat(bz_edit
                                                                    .getText()
                                                                    .toString()
                                                                    .trim()) - Float
                                                            .parseFloat(ls_bz_tv
                                                                    .getText()
                                                                    .toString()
                                                                    .trim()))
                                                            + "");
                                                    if (!qiliang.getText()
                                                            .toString()
                                                            .equals("")
                                                            && !jiage_fang
                                                            .equals("")) {

                                                        qifei.setText((Float
                                                                .parseFloat(qiliang
                                                                        .getText()
                                                                        .toString()) * (Float
                                                                .parseFloat(jiage_fang)))
                                                                + "");
                                                    }

                                                }

                                            }

                                        }
                                    });
                                    holder.ll_right.addView(userView);

                                } else if (sinopecTD.content
                                        .contains("(qiliang)")) {

                                    View userView = layoutInflater.inflate(
                                            R.layout.cb_date_layout, null,
                                            false);
                                    qiliang = (TextView) userView
                                            .findViewById(R.id.now_date);
                                    TextView sc_date = (TextView) userView
                                            .findViewById(R.id.sc_date);
                                    qiliang.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    sc_date.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    String[] strings = replaceQL(
                                            sinopecTD.content).split("\\|");
                                    sc_date.setText(strings[0] + "   月均:"
                                            + strings[1]);
                                    holder.ll_right.addView(userView);

                                } else if (sinopecTD.content
                                        .contains("(qifei)")) {

                                    View userView = layoutInflater.inflate(
                                            R.layout.cb_date_layout, null,
                                            false);
                                    ImageView ivImageView = (ImageView) userView
                                            .findViewById(R.id.iv);
                                    ivImageView.setVisibility(View.GONE);
                                    qifei = (TextView) userView
                                            .findViewById(R.id.now_date);
                                    TextView jiage = (TextView) userView
                                            .findViewById(R.id.sc_date);
                                    qifei.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    jiage.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    jiage_fang = replaceQF(sinopecTD.content);
                                    jiage.setText("￥ "
                                            + replaceQF(sinopecTD.content)
                                            + "/立方");
                                    holder.ll_right.addView(userView);

                                } else if (sinopecTD.content
                                        .contains("(beizhu)")) {

                                    View userView = layoutInflater.inflate(
                                            R.layout.cb_beizhu_layout, null,
                                            false);
                                    beizhu_edit = (EditText) userView
                                            .findViewById(R.id.bz_edit);
                                    if (!beizhu.equals("")) {
                                        beizhu_edit.setText(beizhu);
                                    }
                                    beizhu_edit.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    beizhu_edit
                                            .setOnFocusChangeListener(new OnFocusChangeListener() {

                                                @Override
                                                public void onFocusChange(
                                                        View v, boolean hasFocus) {
                                                    if (!hasFocus) {
                                                        beizhu = beizhu_edit
                                                                .getText()
                                                                .toString()
                                                                .trim();
                                                    }

                                                }
                                            });
                                    holder.ll_right.addView(userView);

                                } else if (sinopecTD.content
                                        .contains("(photo)")) {
                                    // 创建拍照
                                    createPhoto(holder.ll_right,
                                            sinopecTD.content);
                                } else if (sinopecTD.content
                                        .contains("(audio)")) {
                                    // 创建录音
                                    createAudio(holder.ll_right,
                                            sinopecTD.content);
                                } else {
                                    TextView textView = new TextView(
                                            getActivity());
                                    textView.setLayoutParams(new LayoutParams(
                                            -1, -1));
                                    holder.ll_right.addView(textView);
                                    textView.setText((sinopecTD.content
                                            .contains("(html)") ? replaceHtml(sinopecTD.content)
                                            : sinopecTD.content));
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(
                                            TypedValue.COMPLEX_UNIT_SP, 16);
                                    textView.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(getActivity(), 10));
                                    textView.setTag(null);
                                }

                            }
                        }
                    } else {
                        for (int j = 0; j < sinopecTD.sinopecViews.size(); j++) {
                            if (sinopecTD.sinopecViews.get(j) instanceof UIInput) {
                                UIInput uiInput = (UIInput) sinopecTD.sinopecViews
                                        .get(j);
                                if (i == 0) {
                                    if (!"text".equalsIgnoreCase(uiInput.type)) {
                                        // 创建视图
                                        TextView textView = new TextView(
                                                getActivity());
                                        textView.setLayoutParams(new LayoutParams(
                                                -1, -2));
                                        holder.ll_left.addView(textView);
                                        addInputView(textView, uiInput);
                                    }
                                } else {
                                    if ("text".equalsIgnoreCase(uiInput.type)) {
                                        EditText editText = new EditText(
                                                getActivity());
                                        editText.setLayoutParams(new LayoutParams(
                                                -1, -1));
                                        holder.ll_right.addView(editText);
                                        addInputEditView(editText, uiInput);
                                    } else {
                                        TextView textView = new TextView(
                                                getActivity());
                                        textView.setLayoutParams(new LayoutParams(
                                                -1, -2));
                                        holder.ll_right.addView(textView);
                                        addInputView(textView, uiInput);
                                    }
                                }
                            }
                            if (sinopecTD.sinopecViews.get(j) instanceof UIDate) {
                                UIDate date = (UIDate) sinopecTD.sinopecViews
                                        .get(j);
                                Button button = new Button(getActivity());
                                LayoutParams layoutParams = new LayoutParams(
                                        -1, -1);
                                layoutParams.setMargins(3, 3, 3, 3);
                                button.setLayoutParams(layoutParams);
                                holder.ll_right.addView(button);
                                button.setBackgroundResource(R.drawable.approve_list_edittext2);
                                addDataView(button, date);
                            }
                            if (sinopecTD.sinopecViews.get(j) instanceof UISelect) {
                                UISelect select = (UISelect) sinopecTD.sinopecViews
                                        .get(j);
                                // Spinner button = new Spinner(getActivity());
                                // LinearLayout.LayoutParams layoutParams=new
                                // LinearLayout.LayoutParams(-1, -1);
                                // layoutParams.setMargins(3, 3, 3, 3);
                                // Spinner.setLayoutParams(layoutParams);
                                // holder.ll_right.addView(button);
                                // Spinner.setBackgroundResource(R.drawable.approve_list_edittext2);

                                // holder.ll_right.addView(spinner);
                                addSelectView(holder.ll_right, select);

                            }
                        }
                    }
                }
            } else if (columns > 2) { // 三列或者三列以上的数据
                holder.ll_parent.setVisibility(View.VISIBLE);
                holder.vv_height.setVisibility(View.GONE);
                holder.ll_left.setVisibility(View.GONE);
                holder.ll_right.setVisibility(View.GONE); //
                holder.ll_scroll.setVisibility(View.VISIBLE); // 隐藏滚动视图
                holder.two_line.setVisibility(View.VISIBLE);
                creatViewThreadMoreView(holder.ll_scrll_left,
                        holder.ll_content, childList.get(groupPosition));
            }
            return convertView;
        }

        private boolean spinnerflag = true;
        boolean spFlag = false;
        Map<String, String> sppString = new HashMap<String, String>();

        private void addSelectView(LinearLayout linearLayout,
                                   final UISelect select) {
            // TODO Auto-generated method stub
            spFlag = false;
            String[] options = new String[select.options.size()];
            int i = 0;
            int j = 0;
            int checked = 0;
            for (UIOption option : select.options) {
                options[i] = option.name;

                if (spinnerflag) {
                    if (option.checked != null && option.checked.equals("true")) {
                        checked = i;
                        spinnerflag = false;
                    }
                } else {
                    if (sppString.get(select.name) != null) {
                        if (sppString.get(select.name).equalsIgnoreCase(
                                option.name)) {
                            checked = i;
                        }
                    }
                }

                i++;
            }

            if (spinnerflag) {
                for (UIOption option : select.options) {
                    options[j] = option.name;
                    if (sppString.get(select.name) != null) {
                        if (sppString.get(select.name).equalsIgnoreCase(
                                option.name)) {
                            checked = j;
                        }
                    }
                    j++;
                }
            }
            RelativeLayout view = (RelativeLayout) getActivity()
                    .getLayoutInflater().inflate(R.layout.layout_spinner, null);
            Spinner spinner = (Spinner) view.findViewById(R.id.spinnerBase1);
            spinner.setBackgroundResource(R.drawable.approve_list_edittext2);
            LayoutParams params = new LayoutParams(-1, -1);
            params.setMargins(3, 3, 3, 3);
            params.gravity = Gravity.CENTER;
            // params.setMargins(cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext,
            // 15), cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 5),
            // cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 15),
            // cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 5));
            spinner.setLayoutParams(params);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item, options);
            adapter.setDropDownViewResource(R.layout.spinner_text_layout);
            // spinner.setDropDownWidth(mContext.getResources().getDimensionPixelOffset(R.dimen.inbox_mail_width));
            // spinner.setDropDownVerticalOffset(5);
            // spinner.setDropDownHorizontalOffset(cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext,
            // 10));
            spinner.setAdapter(adapter);
            spinner.setSelection(checked);
            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    if (!spFlag) {
                        spFlag = true;
                        select.value = select.options.get(arg2).value;
                        // conditionDateParams.put(select.name,
                        // select.options.get(arg2).value);
                        return;
                    }
                    // conditionDateParams.put(select.name,
                    // select.options.get(arg2).value);
                    sppString.put(select.name, select.options.get(arg2).name);
                    select.value = select.options.get(arg2).value;
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
            view.removeView(spinner);
            linearLayout.addView(spinner);
        }

        // 创建出一个webview视图
        public void createWebView(LinearLayout layout, String content) {
            WebView webView = new WebView(getActivity());
            // webView.setLayoutParams(new LinearLayout.LayoutParams(-1,
            // com.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 100)));
            webView.setLayoutParams(new LayoutParams(-1,
                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(),
                            150)));
            webView.setHorizontalScrollBarEnabled(false);
            webView.setVerticalScrollBarEnabled(false);
            webView.setPadding(
                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 5),// 10
                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 5),
                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 5),
                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 5));
            webView.setBackgroundColor(Color.parseColor("#f5f5f5"));
            webView.loadDataWithBaseURL(null, replaceHtmlWebView(content),
                    "text/html", "UTF-8", null);
            webView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);// NORMAL
            layout.addView(webView);
            webView.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        }

        // 创建出一个imageWebview视图
        public void createImageWebView(LinearLayout layout, String content) {
            WebView webView = new WebView(getActivity());
            webView.setLayoutParams(new LayoutParams(-1,
                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(),
                            100)));
            webView.setHorizontalScrollBarEnabled(false);
            webView.setVerticalScrollBarEnabled(false);
            webView.setPadding(cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                    getActivity(), 10), cn.sbx.deeper.moblie.util.TextUtils
                            .Dp2Px(getActivity(), 10),
                    cn.sbx.deeper.moblie.util.TextUtils
                            .Dp2Px(getActivity(), 10),
                    cn.sbx.deeper.moblie.util.TextUtils
                            .Dp2Px(getActivity(), 10));
            webView.setBackgroundColor(Color.parseColor("#f5f5f5"));
            webView.loadDataWithBaseURL(null, replaceImageWebView(content),
                    "text/html", "UTF-8", null);
            webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
            layout.addView(webView);
            webView.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        }

        // 创建三列或者三列以上数据的滚动视图
        public void creatViewThreadMoreView(LinearLayout ll_left,
                                            final LinearLayout ll_content, List<SinopecTR> sinopecTRs) {
            int len = 0;
            // 创建之前先把控件清空下
            ll_left.removeAllViews();
            ll_content.removeAllViews();

            // 获取左右两边的数据之后，查看数据的个数，如果超过指定个数，需要设置高度
            for (int i = 0; i < sinopecTRs.size(); i++) {
                SinopecTR tr = sinopecTRs.get(i);
                for (int j = 0; j < tr.tds.size(); j++) {
                    SinopecTD sinopecTD = tr.tds.get(j);
                    if (TextUtils.isEmpty(sinopecTD.content)) {
                        if (sinopecTD.sinopecViews.size() == 0) {
                            continue;
                        }
                        if (sinopecTD.sinopecViews.get(0) instanceof UIInput) {
                            if (((UIInput) sinopecTD.sinopecViews.get(0)).value
                                    .length() / 7 > 0) {
                                if (len < ((UIInput) sinopecTD.sinopecViews
                                        .get(0)).value.length() / 7) {
                                    len = ((UIInput) sinopecTD.sinopecViews
                                            .get(0)).value.length() / 7;
                                }
                            }
                        }
                    } else {
                        if (sinopecTD.content.length() / 7 > 0) {
                            if (len < sinopecTD.content.length() / 7)
                                len = sinopecTD.content.length() / 7;
                        }
                    }
                }
            }
            final int len0 = len;
            // 先把左边的数据给获取到
            List<SinopecTD> sinopectdList = new ArrayList<SinopecTD>();
            for (int i = 0; i < sinopecTRs.size(); i++) {
                SinopecTR sinopecTR = sinopecTRs.get(i);
                if (sinopecTR.tds.size() > 0) {
                    sinopectdList.add(sinopecTR.tds.get(0));
                }
            }

            // 获取右边的数据
            final List<List<SinopecTD>> rightList = new ArrayList<List<SinopecTD>>();
            for (int i = 0; i < sinopecTRs.size(); i++) {
                SinopecTR sinopecTR = sinopecTRs.get(i);
                List<SinopecTD> list = new ArrayList<SinopecTD>();
                if (sinopecTR.tds.size() > 1) {
                    for (int j = 1; j < sinopecTR.tds.size(); j++) {
                        list.add(sinopecTR.tds.get(j));
                    }
                    rightList.add(list);
                }
            }

            // 创建左边的视图
            for (int i = 0; i < sinopectdList.size(); i++) {
                final int index = i;
                final SinopecTD sinopecTD = sinopectdList.get(i);
                final TextView textView = new TextView(getActivity());
                textView.setLayoutParams(new LayoutParams(-1,
                        cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                getActivity(), 20 + 16 * (len0 + 1))));
                textView.setPadding(cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                        getActivity(), 10), cn.sbx.deeper.moblie.util.TextUtils
                                .Dp2Px(getActivity(), 10),
                        cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                getActivity(), 10),
                        cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                getActivity(), 10));
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                ll_left.addView(textView);
                if (sinopecTD.sinopecViews.size() == 0) { // 如果是普通文本
                    textView.setText((sinopecTD.content.contains("(html)") ? replaceHtml(sinopecTD.content)
                            : sinopecTD.content));
                    textView.setBackgroundColor(Color.parseColor("#ebf1ff"));
                    textView.setTextColor(Color.parseColor("#3773c0"));
                    textView.setTag(null);
                } else {
                    if (sinopecTD.sinopecViews.get(0) instanceof UIInput) { // 如果是非text的文本
                        UIInput uiInput = (UIInput) sinopecTD.sinopecViews
                                .get(0);
                        if (!"text".equalsIgnoreCase(uiInput.type)) {
                            addInputView(textView, uiInput);
                        }
                    }

                }
                // 先在这儿获取下宽高，然后赋值给scrollview的子元素
                textView.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (textView.getWidth() != 0) { // 添加视图
                            addScrollView(ll_content, rightList.get(index),
                                    textView.getMeasuredWidth(),
                                    textView.getMeasuredHeight(), index);
                        }
                    }
                });

                // 横线
                View lineView = new View(getActivity());
                lineView.setLayoutParams(new LayoutParams(-1, 1));
                lineView.setBackgroundResource(R.drawable.shape_matters_router_child_divider);
                ll_left.addView(lineView);
            }
        }

        public void addScrollView(LinearLayout layout, List<SinopecTD> list,
                                  int width, int height, int index) {
            // 添加第一个视图
            LinearLayout ll_first = new LinearLayout(getActivity());
            ll_first.setLayoutParams(new LayoutParams(-2, height));
            ll_first.setOrientation(LinearLayout.HORIZONTAL);
            ll_first.setGravity(Gravity.CENTER);
            layout.addView(ll_first);
            for (int i = 0; i < list.size(); i++) {
                final SinopecTD sinopecTD = list.get(i);
                if (sinopecTD.sinopecViews.size() == 0) { // 如果是普通文本的话
                    final TextView textView = new TextView(getActivity());
                    textView.setLayoutParams(new LayoutParams(
                            width, -2));
                    textView.setPadding(cn.sbx.deeper.moblie.util.TextUtils
                                    .Dp2Px(getActivity(), 10), 0,
                            cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                    getActivity(), 10), 0);
                    textView.setText((sinopecTD.content.contains("(html)") ? replaceHtml(sinopecTD.content)
                            : sinopecTD.content));
                    textView.setTextColor(Color.BLACK);
                    textView.setTag(null);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    ll_first.addView(textView);
                    textView.setMovementMethod(new ScrollingMovementMethod());
                    // if (index != 0) {
                    // textView.setMovementMethod(new
                    // ScrollingMovementMethod());
                    // textView.setOnTouchListener(new OnTouchListener() {
                    //
                    // @Override
                    // public boolean onTouch(View v, MotionEvent event) {
                    // // TODO Auto-generated method stub
                    // textView.getParent().requestDisallowInterceptTouchEvent(true);
                    // return false;
                    // }
                    // });
                    // }

                } else {
                    if (sinopecTD.sinopecViews.get(0) instanceof UIInput) {
                        UIInput uiInput = (UIInput) sinopecTD.sinopecViews
                                .get(0);
                        if (!"text".equalsIgnoreCase(uiInput.type)) { // 如果不是文本的话
                            final TextView textView = new TextView(
                                    getActivity());
                            textView.setLayoutParams(new LayoutParams(
                                    width, -1));
                            textView.setPadding(
                                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                            getActivity(), 10),
                                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                            getActivity(), 10),
                                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                            getActivity(), 10),
                                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                            getActivity(), 10));
                            textView.setGravity(Gravity.CENTER_VERTICAL);
                            textView.setText((sinopecTD.content
                                    .contains("(html)") ? replaceHtml(sinopecTD.content)
                                    : sinopecTD.content));
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            textView.setTextColor(Color.BLACK);
                            ll_first.addView(textView);
                            addInputView(textView, uiInput);
                            textView.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    if (v.getTag() == null)
                                        return;
                                    startFragmentView((UIInput) v.getTag());
                                }
                            });
                        } else {
                            // 如果是文本的话，
                            EditText editText = new EditText(getActivity());
                            editText.setLayoutParams(new LayoutParams(
                                    width, height));
                            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            editText.setText(uiInput.value);
                            if (uiInput.alt != null && !uiInput.alt.equals("")) {
                                editText.setHint(uiInput.alt);
                            } else {
                                editText.setHint("请输入");
                            }
                            editText.setTextColor(Color.BLACK);
                            editText.setBackgroundColor(Color
                                    .rgb(255, 247, 234));
                            if (btn_next.isShown()) {
                                editText.setEnabled(true);
                            } else {
                                editText.setEnabled(false);
                            }
                            ll_first.addView(editText);
                            editText.addTextChangedListener(new MyTextWatcher(
                                    uiInput));
                        }
                    }
                    if (sinopecTD.sinopecViews.get(0) instanceof UIDate) {
                        UIDate date = (UIDate) sinopecTD.sinopecViews.get(0);
                        Button button = new Button(getActivity());
                        LayoutParams layoutParams = new LayoutParams(
                                -1, -1);
                        layoutParams.setMargins(3, 3, 3, 3);
                        button.setLayoutParams(layoutParams);
                        ll_first.addView(button);
                        button.setBackgroundResource(R.drawable.approve_list_edittext2);
                        addDataView(button, date);
                    }
                }

                // 竖线
                View verLine = new View(getActivity());
                verLine.setLayoutParams(new LayoutParams(1, height));
                verLine.setBackgroundResource(R.drawable.shape_matters_router_child_divider);
                ll_first.addView(verLine);
            }

            // 横线
            View horLine = new View(getActivity());
            horLine.setLayoutParams(new LayoutParams(-1, 1));
            horLine.setBackgroundResource(R.drawable.shape_matters_router_child_divider);
            layout.addView(horLine);
        }

        private void addDataView(Button button, final UIDate date) {
            // TODO Auto-generated method stub
            if (date.type.equals("date")) {
                if (button instanceof Button) {
                    final Button button2 = button;
                    String[] strarray = date.value.split("-");
                    final int mYear = Integer.parseInt(strarray[0]);
                    final int mMonth = Integer.parseInt(strarray[1]);
                    final int mDay = Integer.parseInt(strarray[2]);
                    button2.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            DatePickerDialog dialog = new DatePickerDialog(
                                    getActivity(), new OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view,
                                                      int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    Calendar c = Calendar.getInstance();
                                    c.set(year, monthOfYear, dayOfMonth);
                                    button2.setText(DateFormat.format(
                                            "yyy-MM-dd", c));
                                    date.value = button2.getText()
                                            .toString();
                                }
                            }, mYear, mMonth - 1, mDay);
                            dialog.show();
                        }
                    });
                    button2.setText(new StringBuilder().append(mYear)
                            .append("-").append(mMonth).append("-")
                            .append((mDay < 10) ? "0" + mDay : mDay));
                }
            }
            if (date.type.equals("time")) {
                if (button instanceof Button) {
                    final Button button2 = button;
                    String[] strarray = date.value.split(":");
                    final int mHour = Integer.parseInt(strarray[0]);
                    final int mMinute = Integer.parseInt(strarray[1]);
                    button2.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            TimePickerDialog dialog = new TimePickerDialog(
                                    getActivity(), new OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view,
                                                      int hourOfDay, int minute) {
                                    // TODO Auto-generated method stub
                                    Calendar c = Calendar.getInstance();
                                    c.set(0, 0, 0, hourOfDay, minute);
                                    button2.setText(c
                                            .get(Calendar.HOUR_OF_DAY)
                                            + ":"
                                            + c.get(Calendar.MINUTE));
                                    date.value = button2.getText()
                                            .toString();
                                }
                            }, mHour, mMinute, true);
                            dialog.show();
                        }
                    });
                    button2.setText(new StringBuilder().append(mHour)
                            .append(":")
                            .append((mMinute < 10) ? "0" + mMinute : mMinute));
                }
            }
        }

        /**
         * 如果类型是input的话，需要创建出视图,需要给控件赋上tag值，为了给点击各自的点击事件使用,里面会赋值，赋大小、颜色
         *
         * @param view
         * @param uiInput
         */
        public void addInputView(TextView view, UIInput uiInput) {
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setPadding(cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                        getActivity(), 10), cn.sbx.deeper.moblie.util.TextUtils
                                .Dp2Px(getActivity(), 10),
                        cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                getActivity(), 10),
                        cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(
                                getActivity(), 10));
                textView.setText(uiInput.value.trim());
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView.setTag(null);
                if ("form".equalsIgnoreCase(uiInput.type)
                        || "html".equalsIgnoreCase(uiInput.type)
                        || "url".equalsIgnoreCase(uiInput.type)) {
                    textView.setTextColor(Color.BLUE);
                    if (uiInput.title == null || uiInput.title.equals("")) {
                        textView.setText(uiInput.value);
                    } else {
                        textView.setText(uiInput.title.trim());
                    }

                    textView.setTag(uiInput);
                } else if ("label".equalsIgnoreCase(uiInput.type)) {
                    textView.setTextColor(Color.parseColor("#3773c0"));
                    textView.setText(uiInput.title.trim());
                } else if ("file".equalsIgnoreCase(uiInput.type)) {
                    textView.setTextColor(Color.BLUE);
                    if (uiInput.title == null || uiInput.title.equals("")) {
                        String[] sourceStrArray = uiInput.value.split("/");
                        textView.setText(sourceStrArray[sourceStrArray.length - 1]);
                    } else {
                        textView.setText(uiInput.title.trim());
                    }

                    textView.setTag(uiInput);
                }
                textView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (v.getTag() == null)
                            return;
                        startFragmentView((UIInput) v.getTag());
                    }
                });
            }
        }

        // 添加edittext控件
        public void addInputEditView(View view, UIInput uiInput) {
            EditText editText = (EditText) view;
            editText.setTextColor(Color.BLACK);
            editText.setBackgroundColor(Color.rgb(255, 247, 234));
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            editText.setText(uiInput.value.trim());
            if (uiInput.alt != null && !uiInput.alt.equals("")) {
                editText.setHint(uiInput.alt);
            } else {
                editText.setHint("请输入");
            }
            if (btn_next.isShown()) {
                editText.setEnabled(true);
            } else {
                editText.setEnabled(false);
            }
            editText.setTag(null);
            editText.addTextChangedListener(new MyTextWatcher(uiInput));
        }

        // 根据不同的类型跳转到不同的跳转界面上
        public void startFragmentView(final UIInput uiInput) {
           /* if ("form".equalsIgnoreCase(uiInput.type)) {
                SinopecApproveOADetailFormFragment formFragment = new SinopecApproveOADetailFormFragment();
                Bundle bundle = new Bundle();
                bundle.putString("formpath", formPath);
                bundle.putString("htmlPath", htmlPath);
                bundle.putString("filePath", filePath);
                bundle.putString("key", uiInput.value);
                formFragment.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(formFragment);
            } else if ("html".equalsIgnoreCase(uiInput.type)) {
                SinopecApproveOADetailHtmlFragment formFragment = new SinopecApproveOADetailHtmlFragment();
                Bundle bundle = new Bundle();
                bundle.putString("htmlPath", htmlPath);
                bundle.putString("key1", uiInput.name);
                bundle.putString("key2", uiInput.value);
                formFragment.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(formFragment);
            } else if ("url".equalsIgnoreCase(uiInput.type)) {
                SinopecApproveOADetailUrlFragment formFragment = new SinopecApproveOADetailUrlFragment();
                Bundle bundle = new Bundle();
                bundle.putString("urlPath", uiInput.value);
                formFragment.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(formFragment);
            } else if ("file".equalsIgnoreCase(uiInput.type)) {
                // DownloadUtils.downLoadFile(getActivity(), uiInput.value);
                DownloadUtils.downloadIntranetFile(getActivity(),
                        uiInput.value, filePath);
            } else {

            }*/
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return false;
        }
    }

    public class Holder {
        public LinearLayout ll_parent;
        public LinearLayout ll_left;
        public LinearLayout ll_right;
        // 下面四个是滚动视图用的
        public LinearLayout ll_scroll;
        public LinearLayout ll_scrll_left;
        public HorizontalScrollView hsv;
        public LinearLayout ll_content;
        public View vv_height;
        public View two_line;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.tv_biaoju:
                Bundle bundle = new Bundle();
                bundle.putString("id", taskId);
                bundle.putString("device_name", "表具");
                Fragment f = new AnJianDetailFormFragment();
                f.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(f);
                break;
            case R.id.tv_liguan:
                Bundle bundle_liguan = new Bundle();
                bundle_liguan.putString("id", taskId);
                bundle_liguan.putSerializable("custinfo", custInfo);
                bundle_liguan.putString("device_name", "立管阀门");
                Fragment f1 = new AnJianDetailFormFragment_LiGuan();
                f1.setArguments(bundle_liguan);
                ((ActivityInTab) getActivity()).navigateTo(f1);
                break;
            case R.id.tv_zaoju:
                Bundle bundle_zaoju = new Bundle();
                bundle_zaoju.putString("id", taskId);
                bundle_zaoju.putSerializable("custinfo", custInfo);
                bundle_zaoju.putString("device_name", "灶具");
                Fragment f_zaoju = new AnJianDetailFormFragment_ZaoJU();
                f_zaoju.setArguments(bundle_zaoju);
                ((ActivityInTab) getActivity()).navigateTo(f_zaoju);
                break;
            case R.id.tv_lianjieguan:
                Bundle bundle_lianjieguan = new Bundle();
                bundle_lianjieguan.putString("id", taskId);
                bundle_lianjieguan.putSerializable("custinfo", custInfo);
                bundle_lianjieguan.putString("device_name", "连接管");
                Fragment f_lianjieguan = new AnJianDetailFormFragment_LianJieGuan();
                f_lianjieguan.setArguments(bundle_lianjieguan);
                ((ActivityInTab) getActivity()).navigateTo(f_lianjieguan);
                break;
            case R.id.tv_cainuanlu:
                Bundle bundle_cainuanlu = new Bundle();
                bundle_cainuanlu.putString("id", taskId);
                bundle_cainuanlu.putSerializable("custinfo", custInfo);
                bundle_cainuanlu.putString("device_name", "采暖炉");
                Fragment f_cainuanlu = new AnJianDetailFormFragment_CaiNuanLu();
                f_cainuanlu.setArguments(bundle_cainuanlu);
                ((ActivityInTab) getActivity()).navigateTo(f_cainuanlu);
                break;
            case R.id.tv_reshuiqi:
                Bundle bundle_reshuiqi = new Bundle();
                bundle_reshuiqi.putString("id", taskId);
                bundle_reshuiqi.putSerializable("custinfo", custInfo);
                bundle_reshuiqi.putString("device_name", "热水器");
                Fragment f_reshuiqi = new AnJianDetailFormFragment_ReShuiQi();
                f_reshuiqi.setArguments(bundle_reshuiqi);
                ((ActivityInTab) getActivity()).navigateTo(f_reshuiqi);
                break;
            case R.id.tv_baojingqi:
                Bundle bundle_baojingqi = new Bundle();
                bundle_baojingqi.putString("id", taskId);
                bundle_baojingqi.putSerializable("custinfo", custInfo);
                bundle_baojingqi.putString("device_name", "报警器");
                Fragment f_baojingqi = new AnJianDetailFormFragment_BaoJngQi();
                f_baojingqi.setArguments(bundle_baojingqi);
                ((ActivityInTab) getActivity()).navigateTo(f_baojingqi);
                break;
            case R.id.iv_pic_zhaopian:
                ViewPropertyAnimator.animate(iv_pic_zhaopian).rotationBy(180)
                        .setDuration(350).start();
                if (isOpen) {
                    ll_showpacter.setVisibility(View.GONE);
                    isOpen = !isOpen;
                } else {
                    ll_showpacter.setVisibility(View.VISIBLE);
                    // 加载图片显示区域
                    // 拍照
                    // LinearLayout ll_photocontainer = (LinearLayout) view
                    // .findViewById(R.id.ll_photocontainer_add);
                    LinearLayout ll_takepicture = (LinearLayout) view
                            .findViewById(R.id.ll_takepicture);

                    ll_takepicture.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // 弹出添加图片界面,将当前用户传递过去

                            Fragment f_pic = new AnJianDetailAddPictureFragment();
                            Bundle bundle_picture = new Bundle();
                            bundle_picture.putString("id", taskId);
                            bundle_picture.putSerializable("custinfo", custInfo);
                            f_pic.setArguments(bundle_picture);
                            ((ActivityInTab) getActivity()).navigateTo(f_pic);
                        }
                    });

                    // createPhoto_new(ll_photocontainer);

                    isOpen = !isOpen;
                }

                break;
            case R.id.iv_pic_shebeiziliao:

                ViewPropertyAnimator.animate(iv_pic_shebeiziliao).rotationBy(180)
                        .setDuration(350).start();
                if (isOpen2) {
                    ll_showshebeixinxi.setVisibility(View.GONE);
                    isOpen2 = !isOpen2;
                } else {
                    ll_showshebeixinxi.setVisibility(View.VISIBLE);
                    isOpen2 = !isOpen2;
                }
                break;
            // case R.id.tv_qianming:
            // // 弹出签名界面
            // if (!TextUtils.isEmpty(spinner_fuWu)) {
            //
            // Intent intent = new Intent(mActivity, HandwritingActivity.class);
            // intent.putExtra("taskId", taskId);
            // intent.putExtra("servercePoint", custInfo.getServicePointId());
            // startActivity(intent);
            // } else {
            // Toast.makeText(mContext, "请先选择服务评价", Toast.LENGTH_LONG).show();
            // }
            //
            // break;
            // 保存数据
            case R.id.btn_next:

                if (!TextUtils.isEmpty(aj_spinnerCode)) { // 入户情况唯一必填
                    final ProgressHUD overlayProgress = AlertUtils.showDialog(
                            getActivity(), null, null, false); // 时间太长了，把加载进度条取消掉。。。。
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() { // TODO Auto-generated
                            // method stub
                            saveData(taskId); //
                            if (overlayProgress != null) {
                                overlayProgress.dismiss();
                            }
                            Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_LONG).show();
                            // 修改客户信息表的状态
                            changeCmState(taskId);
                            // dialogUpdate(getActivity());

                            // 当选择正常入户时 提示是否打印

                            if (aj_spinnerCode.equals("ZCRH")) {

                                // 提示是否打印
                                new Builder(getActivity())
                                        .setTitle("提示")
                                        .setMessage("是否打印当前保存的数据？")
                                        .setPositiveButton(
                                                "确定",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        if (!mp.adapter.isEnabled()) {
                                                            Toast.makeText(
                                                                    mActivity,
                                                                    "请开启蓝牙", Toast.LENGTH_SHORT)
                                                                    .show();
                                                        }
                                                        // 判断是否成功连接蓝牙
                                                        if (mp.GetBluetoothData()) {

                                                            // 调用打印方法
                                                            if (mp.mPrinter != null) {

                                                                SecurityCheck sc = SelectSecurityCheck();

                                                                // 获取隐患集合数据
                                                                List<SecurityCheckHiddenDanger> list = SelectHiddenDanger();
                                                                // 获取该用户电子签名信息
                                                                String imagePath_relative = getHandWriterInfo();
                                                                if(imagePath_relative==null){
                                                                    imagePath_relative="";
                                                                }

                                                                // 判断是否存在安全隐患
                                                                if (list.size() > 0) {
                                                                    // 获取字典表类型
                                                                    List<DictionariesHiddenDanger> dicList = SelectSecurityCheckType();
                                                                    // 存在安全隐患
                                                                    mp.SafetyChecklist_HiddenDanger(
                                                                            getActivity()
                                                                                    .getResources(),
                                                                            mp.mPrinter,
                                                                            true,
                                                                            sc,
                                                                            dicList,
                                                                            taskId,
                                                                            sdPath1
                                                                                    + imagePath_relative,loginNameForSign,getActivity());

                                                                } else {
                                                                    mp.SafetyChecklist(
                                                                            getActivity()
                                                                                    .getResources(),
                                                                            mp.mPrinter,
                                                                            true,
                                                                            sc,
                                                                            sdPath1
                                                                                    + imagePath_relative,loginNameForSign);
                                                                }
                                                            }
                                                        } else {
                                                            new Builder(
                                                                    getActivity())
                                                                    .setTitle(
                                                                            "打印失败")
                                                                    .setMessage(
                                                                            "当前设备尚未连接蓝牙打印机")
                                                                    .show();
                                                        }
                                                    }

                                                }).setNegativeButton("取消", null)
                                        .show();

                            }

                        }
                    }, 2000);

                } else {

                    Toast.makeText(getActivity(), "请选择安检入户情况", Toast.LENGTH_LONG).show();
                }

                break;

            default:
                break;
        }

		/*
		 * 下一户 if (cb_left.getId() == v.getId()) {
		 * btn_next.setVisibility(View.GONE); backPrecious(); } else if
		 * (btn_next.getId() == v.getId()) { // Toast.makeText(getActivity(),
		 * "暂不支持", 1).show(); if (tabType.equals("1")) { if
		 * (!aj_tongzhidan.getText().toString().trim().equals("")) { if
		 * (!aj_biaozi.getText().toString().trim().equals("")) { final
		 * ProgressHUD overlayProgress = AlertUtils .showDialog(getActivity(),
		 * null, null, false); // 时间太长了，把加载进度条取消掉。。。。 Handler handler = new
		 * Handler(); handler.postDelayed(new Runnable() {
		 *
		 * @Override public void run() { // TODO Auto-generated method stub if
		 * (overlayProgress != null) { overlayProgress.dismiss(); }
		 * submitData("3", taskId); // Toast.makeText(getActivity(), "提交完成", //
		 * 1).show(); dialogUpdate(getActivity()); } }, 2000); } else {
		 * Toast.makeText(getActivity(), "请输入表字", 1).show(); }
		 *
		 * } else { Toast.makeText(getActivity(), "请输入通知单号", 1).show(); } } else
		 * if (tabType.equals("3")) {
		 *
		 * final ProgressHUD overlayProgress = AlertUtils.showDialog(
		 * getActivity(), null, null, false); // 时间太长了，把加载进度条取消掉。。。。 Handler
		 * handler = new Handler(); handler.postDelayed(new Runnable() {
		 *
		 * @Override public void run() { // TODO Auto-generated method stub if
		 * (overlayProgress != null) { overlayProgress.dismiss(); }
		 * submitYBC(taskId); approveRouteSuccess1(targetContainer, backToList);
		 * Toast.makeText(getActivity(), "提交完成", 1).show(); } }, 2000);
		 *
		 * }
		 *
		 * } else if (next_user_btn.getId() == v.getId()) { // 下一户 if
		 * (!aj_tongzhidan.getText().toString().trim().equals("")) { if
		 * (!aj_biaozi.getText().toString().trim().equals("")) { submitData("3",
		 * taskId); // new ApproveDetailTask().execute(taskId, "next");
		 * Toast.makeText(mContext, "已保存", 1).show(); } else {
		 * dialogUpdateNext(mContext, "是否放弃安检？", "next"); } } else {
		 * dialogUpdateNext(mContext, "是否放弃安检？", "next"); } //
		 * Toast.makeText(getActivity(), "暂不支持", 1).show(); } else if
		 * (up_user_btn.getId() == v.getId()) { // 上一户 if
		 * (!aj_tongzhidan.getText().toString().trim().equals("")) { if
		 * (!aj_biaozi.getText().toString().trim().equals("")) { submitData("3",
		 * taskId); new ApproveDetailTask().execute(taskId, "up");
		 * Toast.makeText(mContext, "已保存", 1).show(); } else {
		 * dialogUpdateNext(mContext, "是否放弃安检？", "up"); } } else {
		 * dialogUpdateNext(mContext, "是否放弃安检？", "up"); } //
		 * Toast.makeText(getActivity(), "暂不支持", 1).show(); } else if
		 * (anjian_btn.getId() == v.getId()) { // 安检 //
		 * Toast.makeText(getActivity(), "暂不支持", 1).show(); if
		 * (!aj_tongzhidan.getText().toString().trim().equals("")) { if
		 * (!aj_biaozi.getText().toString().trim().equals("")) { submitData("3",
		 * taskId); Fragment f = new PollingDetailFragment(); Bundle bundle =
		 * new Bundle(); bundle.putSerializable("entry", menuModule);
		 * bundle.putString("id", taskId); bundle.putString("tabType", tabType);
		 * bundle.putBoolean("isBatch", false);// 普通审批
		 * bundle.putString("currNote", currNote); f.setArguments(bundle);
		 * f.setTargetFragment(AnJianDetailFragment.this, 0); ((ActivityInTab)
		 * mActivity).navigateTo(f); Toast.makeText(mContext, "已保存", 1).show();
		 * } else { dialogUpdateNext(mContext, "是否放弃安检？", "chaobiao"); } } else
		 * { dialogUpdateNext(mContext, "是否放弃安检？", "chaobiao"); } }
		 */
    }

    private String getHandWriterInfo() {
        String relativeFileRoute = null;
        String select = "select cmScFileRoute from perFile_aj where accountId = '"
                + taskId + "' and cmScBusiType = 'SIG'";

        try {
//            conne = new DatabaseHelper(mContext);
//            SQLiteDatabase ps = conne.getWritableDatabase();

            if(instance==null){
                databaseHelperInstance=new DatabaseHelperInstance();

                instance = databaseHelperInstance.getInstance(getActivity());
            }
            Cursor resultSet = instance.rawQuery(select,new String[]{});
            if(resultSet.moveToFirst()){
                relativeFileRoute = resultSet.getString(0);
//                while (resultSet.moveToNext()) {
//                    relativeFileRoute = resultSet.getString(0);
//                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return relativeFileRoute;

    }

    // 获取安检异常字典类型和描述
    public List<DictionariesHiddenDanger> SelectSecurityCheckType() {
        String sql = "select distinct cmScShType,cmScShTypeDescr from dic_cmScShItem_aj";
        // 实体对象
        List<DictionariesHiddenDanger> dicList = new ArrayList<DictionariesHiddenDanger>();

        try {
//            conne = new DatabaseHelper(mContext);
//
//            state = conne.getWritableDatabase();
            if(instance==null){
                databaseHelperInstance=new DatabaseHelperInstance();

                instance = databaseHelperInstance.getInstance(getActivity());
            }

            Cursor DicSet = instance.rawQuery(sql,new String[]{});

            if(DicSet.moveToFirst()){
                DictionariesHiddenDanger dicc = new DictionariesHiddenDanger();
                dicc.cmScShType = DicSet.getString(0);
                dicc.cmScShTypeDescr = DicSet.getString(1);
                dicList.add(dicc);
                while (DicSet.moveToNext()) {
                    DictionariesHiddenDanger dic = new DictionariesHiddenDanger();
                    dic.cmScShType = DicSet.getString(0);
                    dic.cmScShTypeDescr = DicSet.getString(1);
                    dicList.add(dic);
                }
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dicList;
    }

    // 获取安检打印表单数据
    public SecurityCheck SelectSecurityCheck() {
        String sql = "select c.cmMrAddress,u.cmScDttm,u.cmMrCommCd from custInfo_ju_aj as c left join uploadcustInfo_aj as u where c.accountId = '"
                + taskId + "' and u.accountId='" + taskId + "'";
        // 实体对象
        SecurityCheck sc = new SecurityCheck();
        Cursor DicSet = null;
        try {
//            conne = new DatabaseHelper(mContext);
//
//            state =  conne.getWritableDatabase();
            if(instance==null){
                databaseHelperInstance=new DatabaseHelperInstance();

                instance = databaseHelperInstance.getInstance(getActivity());
            }

            DicSet = instance.rawQuery(sql,new String[]{});
            if(DicSet.moveToFirst()){
                sc.userNo = taskId;
                sc.path = user_address.getText().toString().trim();
                sc.checkDate = DicSet.getString(1);
                sc.cmMrCommCd = SelectServiceDescriptionk(DicSet
                        .getString(2));
                while (DicSet.moveToNext()) {
                    sc.userNo = taskId;
                    sc.path = user_address.getText().toString().trim();
                    sc.checkDate = DicSet.getString(1);
                    sc.cmMrCommCd = SelectServiceDescriptionk(DicSet
                            .getString(2));
                }
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (DicSet != null) {
                try {
                    DicSet.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
        }

        return sc;
    }

    // 获取安全隐患类型
    public List<SecurityCheckHiddenDanger> SelectHiddenDanger() {
        String sqls = "select cmScShType,cmScShTypeDescr,cmScShItem,cmScShItemDescr from perSh_aj where accountId = '"
                + taskId + "' and cmScShCheck ='Y'";

        // 实体集合
        List<SecurityCheckHiddenDanger> sc = new ArrayList<SecurityCheckHiddenDanger>();
        Cursor IsDicSet = null;
        try {
//            conne = new DatabaseHelper(mContext);
//
//            state = conne.getWritableDatabase();

            // 执行查询
            if(instance==null){
                databaseHelperInstance=new DatabaseHelperInstance();

                instance = databaseHelperInstance.getInstance(getActivity());
            }
            IsDicSet = instance.rawQuery(sqls,new String[]{});
            if(IsDicSet.moveToFirst()){
                while (IsDicSet.moveToNext()) {
                    SecurityCheckHiddenDanger s1 = new SecurityCheckHiddenDanger();
                    s1.cmScShType = IsDicSet.getString(0);
                    s1.cmScShItemDescr = IsDicSet.getString(1);
                    sc.add(s1);
                }
                while (IsDicSet.moveToNext()) {
                    SecurityCheckHiddenDanger s = new SecurityCheckHiddenDanger();
                    s.cmScShType = IsDicSet.getString(0);
                    s.cmScShItemDescr = IsDicSet.getString(1);
                    sc.add(s);
                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (IsDicSet != null) {
                try {
                    IsDicSet.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
        }
        return sc;
    }

    // 获取制定类型的安全隐患
    public List<SecurityCheckHiddenDanger> SelectHiddenDanger(String ID,
                                                              String type,Context cont) {
        String sqls = "select cmScShType,cmScShTypeDescr,cmScShItem,cmScShItemDescr from perSh_aj where accountId = '"
                + ID + "' and cmScShCheck ='Y' and cmScShType = '" + type + "'";

        // 实体集合
        List<SecurityCheckHiddenDanger> sc = new ArrayList<SecurityCheckHiddenDanger>();
        try {
//            conne = new DatabaseHelper(mContext);
//
//            state = conne.getWritableDatabase();

            // 执行查询
            if(instance==null){
                databaseHelperInstance=new DatabaseHelperInstance();

                instance = databaseHelperInstance.getInstance(cont);
            }
            Cursor IsDicSet = instance.rawQuery(sqls,new String[]{});
            if(IsDicSet.moveToFirst()){
                SecurityCheckHiddenDanger sxx = new SecurityCheckHiddenDanger();
                sxx.cmScShType = IsDicSet.getString(0);
                sxx.cmScShItemDescr = IsDicSet.getString(3);
                sc.add(sxx);
                while (IsDicSet.moveToNext()) {
                    SecurityCheckHiddenDanger s = new SecurityCheckHiddenDanger();
                    s.cmScShType = IsDicSet.getString(0);
                    s.cmScShItemDescr = IsDicSet.getString(3);
                    sc.add(s);
                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sc;
    }

    // 根据服务编码获取服务描述
    public String SelectServiceDescriptionk(String code) {
        String sql = "select dictionaryDescr from dictionaries where dictionaryCode= '"
                + code + "' and parentID='cmMrComm'";

        String serviceDis = null;
        try {
//            conne =  new DatabaseHelper(mContext);
//
//            state =conne.getWritableDatabase();
            if(instance==null){
                databaseHelperInstance=new DatabaseHelperInstance();

                instance = databaseHelperInstance.getInstance(getActivity());
            }

            Cursor DicSet = instance.rawQuery(sql,new String[]{});
            if(DicSet.moveToFirst()){
                while (DicSet.moveToNext()) {
                    serviceDis = DicSet.getString(0);
                }
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return serviceDis;
    }

    // 文本监听
    public class MyTextWatcher implements TextWatcher {

        private Object object;

        public MyTextWatcher(Object object0) {
            object = object0;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            // if (object instanceof UIInput) {
            // UIInput uiInput = (UIInput) object;
            //
            // }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (object instanceof UIInput) {
                UIInput uiInput = (UIInput) object;
                if (uiInput.regex.endsWith("false")) {
                    if (s.length() > 0) {
                        uiInput.value = s.toString();
                        uiInput.tag.value = s.toString();
                    }
                } else {
                    if (s.length() > 0) {
                        if (checkString(s.toString(), uiInput)) {
                            uiInput.value = s.toString();
                            uiInput.tag.value = s.toString();
                        } else {
                            new Builder(getActivity())
                                    .setTitle("提示")
                                    .setMessage(uiInput.regexmessage)
                                    .setPositiveButton("确定", null).show();
                            s.delete(0, s.length());
                        }
                    } else {
                        uiInput.value = s.toString();
                        uiInput.tag.value = s.toString();
                    }
                }
            }
        }
    }

    private boolean checkString(String s, UIInput input) {
        return s.matches(input.regex);
    }

    // 添加必填集合
    public void addRequiredList() {
        if (detailEntry.form == null)
            return;
        SinopecForm form = detailEntry.form;
        for (int i = 0; i < form.sinopecTables.size(); i++) {
            // 如果为false表示不需要提交
            if ("false".equalsIgnoreCase(form.sinopecTables.get(i).EditField))
                continue;
            for (int j = 0; j < form.sinopecTables.get(i).trs.size(); j++) {

                SinopecTR sinopecTR = form.sinopecTables.get(i).trs.get(j);
                for (int k = 0; k < sinopecTR.tds.size(); k++) {
                    SinopecTD sinopecTD = sinopecTR.tds.get(k);
                    for (int l = 0; l < sinopecTD.sinopecViews.size(); l++) {
                        Object object = sinopecTD.sinopecViews.get(l);
                        if (object instanceof UIInput) {
                            UIInput uiInput = (UIInput) object;

                            // uiInput.tag = new ApproveViewTag(uiInput.parent,
                            // uiInput.submitflag, uiInput.required,
                            // uiInput.type, uiInput.name, uiInput.value,
                            // uiInput.message, uiInput.checked);

                            uiInput.tag = new ApproveViewTag(uiInput.parent,
                                    uiInput.submitflag, uiInput.required,
                                    uiInput.type, uiInput.name, uiInput.value,
                                    uiInput.message, uiInput.checked, "input");

                            // 提交集合
                            if (!TextUtils.isEmpty(uiInput.value)
                                    || TextUtils.isEmpty(uiInput.value)) {
                                Map<String, ApproveViewTag> map = null;
                                if (submitDatas.get(uiInput.submitflag) == null) {
                                    map = new HashMap<String, ApproveViewTag>();
                                } else {
                                    map = submitDatas.get(uiInput.submitflag);
                                }
                                map.put(uiInput.name, uiInput.tag);
                                submitDatas.put(uiInput.submitflag, map);
                            }

                            if (TextUtils.isEmpty(uiInput.submitflag))
                                continue;

                            // 必填集合
                            if (requiredDatas.get(uiInput.submitflag) == null) {
                                Map<String, ApproveViewTag> map = new HashMap<String, ApproveViewTag>();
                                map.put(uiInput.name, uiInput.tag);
                                requiredDatas.put(uiInput.submitflag, map);
                            } else {
                                Map<String, ApproveViewTag> map = requiredDatas
                                        .get(uiInput.submitflag);
                                if ("true".equalsIgnoreCase(uiInput.required)) { // 如果是必填的，
                                    map.put(uiInput.name, uiInput.tag);
                                    for (String tag : map.keySet()) {
                                        ApproveViewTag approveViewTag = map
                                                .get(tag);
                                        if (!"true"
                                                .equalsIgnoreCase(approveViewTag.required_tag)) {
                                            map.remove(tag);
                                        }
                                    }
                                }
                            }
                        }
                        if (object instanceof UIDate) {
                            UIDate uidDate = (UIDate) object;
                            uidDate.tag = new ApproveViewTag("",
                                    uidDate.submitflag, uidDate.required,
                                    uidDate.type, uidDate.name, uidDate.value,
                                    uidDate.Message, "", "date");

                            // 提交集合
                            if (!TextUtils.isEmpty(uidDate.value)
                                    || TextUtils.isEmpty(uidDate.value)) {
                                Map<String, ApproveViewTag> map = null;
                                if (submitDatas.get(uidDate.submitflag) == null) {
                                    map = new HashMap<String, ApproveViewTag>();
                                } else {
                                    map = submitDatas.get(uidDate.submitflag);
                                }
                                map.put(uidDate.name, uidDate.tag);
                                submitDatas.put(uidDate.submitflag, map);
                            }

                            if (TextUtils.isEmpty(uidDate.submitflag))
                                continue;

                            // 必填集合
                            if (requiredDatas.get(uidDate.submitflag) == null) {
                                Map<String, ApproveViewTag> map = new HashMap<String, ApproveViewTag>();
                                map.put(uidDate.name, uidDate.tag);
                                requiredDatas.put(uidDate.submitflag, map);
                            } else {
                                Map<String, ApproveViewTag> map = requiredDatas
                                        .get(uidDate.submitflag);
                                if ("true".equalsIgnoreCase(uidDate.required)) { // 如果是必填的，
                                    map.put(uidDate.name, uidDate.tag);
                                    for (String tag : map.keySet()) {
                                        ApproveViewTag approveViewTag = map
                                                .get(tag);
                                        if (!"true"
                                                .equalsIgnoreCase(approveViewTag.required_tag)) {
                                            map.remove(tag);
                                        }
                                    }
                                }
                            }
                        }
                        if (object instanceof UISelect) {
                            UISelect uidSelect = (UISelect) object;
                            uidSelect.tag = new ApproveViewTag("",
                                    uidSelect.submitflag, uidSelect.required,
                                    uidSelect.type, uidSelect.name,
                                    uidSelect.value, uidSelect.Message, "",
                                    "date");

                            // 提交集合
                            if (!TextUtils.isEmpty(uidSelect.value)
                                    || TextUtils.isEmpty(uidSelect.value)) {
                                Map<String, ApproveViewTag> map = null;
                                if (submitDatas.get(uidSelect.submitflag) == null) {
                                    map = new HashMap<String, ApproveViewTag>();
                                } else {
                                    map = submitDatas.get(uidSelect.submitflag);
                                }
                                map.put(uidSelect.name, uidSelect.tag);
                                submitDatas.put(uidSelect.submitflag, map);
                            }

                            if (TextUtils.isEmpty(uidSelect.submitflag))
                                continue;

                            // 必填集合
                            if (requiredDatas.get(uidSelect.submitflag) == null) {
                                Map<String, ApproveViewTag> map = new HashMap<String, ApproveViewTag>();
                                map.put(uidSelect.name, uidSelect.tag);
                                requiredDatas.put(uidSelect.submitflag, map);
                            } else {
                                Map<String, ApproveViewTag> map = requiredDatas
                                        .get(uidSelect.submitflag);
                                if ("true".equalsIgnoreCase(uidSelect.required)) { // 如果是必填的，
                                    map.put(uidSelect.name, uidSelect.tag);
                                    for (String tag : map.keySet()) {
                                        ApproveViewTag approveViewTag = map
                                                .get(tag);
                                        if (!"true"
                                                .equalsIgnoreCase(approveViewTag.required_tag)) {
                                            map.remove(tag);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 添加下一步
     */

    // 处理返回函数 现在不是重写
    @Override
    public void refreshButtonAndText() {
        // TODO Auto-generated method stub
        // btn_save.setVisibility(View.GONE);
        TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
        // tv_title.setText(menuModule.caption);
        tv_title.setText("安检");
        btn_next = (Button) getActivity().findViewById(R.id.btn_next);
        btn_next.setText("保存");
        if (state_up.equals("2")) {
            btn_next.setVisibility(View.INVISIBLE);
        } else {
            btn_next.setVisibility(View.VISIBLE);
        }
        // new ApproveDetailTask().execute(); // 下载数据
        // new ApproveDetailTask().execute(taskId, "");
        // 是否存在安全隐患
        // if (custInfo.getCmScAqyh() != null
        // && custInfo.getCmScAqyh().equals("Y")
        // || Constants.isYiChang.equals("是")) {
        // tv_isYH.setText("是");
        // } else {
        // tv_isYH.setText("否");
        // }
        btn_next.setOnClickListener(this);
		/*
		 * btn_next.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 *
		 * if (tabType.equals("1")) { if
		 * (!aj_tongzhidan.getText().toString().trim().equals("")) { if
		 * (!aj_biaozi.getText().toString().trim().equals("")) { final
		 * ProgressHUD overlayProgress = AlertUtils .showDialog(getActivity(),
		 * null, null, false); // 时间太长了，把加载进度条取消掉。。。。 Handler handler = new
		 * Handler(); handler.postDelayed(new Runnable() {
		 *
		 * @Override public void run() { // TODO Auto-generated // method stub
		 * if (overlayProgress != null) { overlayProgress.dismiss(); }
		 * submitData("3", taskId); // Toast.makeText(getActivity(), "提交完成", 1)
		 * .show(); dialogUpdate(getActivity()); } }, 2000); } else {
		 * Toast.makeText(getActivity(), "请输入表字", 1).show(); }
		 *
		 * } else { Toast.makeText(getActivity(), "请输入通知单号", 1).show(); } } else
		 * if (tabType.equals("3")) {
		 *
		 * final ProgressHUD overlayProgress = AlertUtils.showDialog(
		 * getActivity(), null, null, false); // 时间太长了，把加载进度条取消掉。。。。 Handler
		 * handler = new Handler(); handler.postDelayed(new Runnable() {
		 *
		 * @Override public void run() { // TODO Auto-generated method stub if
		 * (overlayProgress != null) { overlayProgress.dismiss(); }
		 * submitYBC(taskId); approveRouteSuccess1(targetContainer, backToList);
		 * Toast.makeText(getActivity(), "提交完成", 1).show(); } }, 2000);
		 *
		 * }
		 *
		 * } });
		 */

        progressHUDx = AlertUtils.showDialog(getActivity(), null, null,
                false);
        progressHUDx.show();
        // 重新动态加载隐患信息,先清空该容器子view


        new Thread(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (device_layout_container != null) {
                            int width = MeasureSpec.getSize(screenWidth);
                            device_layout_container.removeAllViews();
                            device_layout_container.addView(new AnJianYiCFragment(mActivity,
                                    mContext, width, cmScShType_list, custInfo).getView());
                        }
                    }
                });

              // 当前用户照片
                try {
                    imageUrl_List = selectPicture(taskId, custInfo.getCmSchedId());
                    // 初始化用户图片

                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "图片初始化失败", Toast.LENGTH_SHORT).show();
                }
                Message msg = Message.obtain();
                msg.what=111;
                mHandler.sendMessage(msg);

            }
        }).start();



    }

    // 下一步操作
    public void nextOperation(String device_no) {

        // 验证
        for (String required : requiredDatas.keySet()) {
            boolean bool = false;
            for (String submit : submitDatas.keySet()) {
                if (required.equalsIgnoreCase(submit)) {
                    bool = true;
                    // 判断必填项是否填写
                    for (String required2 : requiredDatas.get(required)
                            .keySet()) {
                        // 如果是必填项
                        if (requiredDatas.get(required).get(required2).required_tag
                                .equals("true")) {
                            // 如果提交集合里面，对应的没有值，或者不等于true
                            if ((submitDatas.get(submit).get(required2).value == null || submitDatas
                                    .get(submit).get(required2).value
                                    .equals(""))
                                    && submitDatas.get(submit).get(required2).required_tag
                                    .equalsIgnoreCase("true")) {
                                Toast.makeText(
                                        mContext,
                                        requiredDatas.get(required).get(
                                                required2).message,
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    break;
                }
            }
            if (bool == false) {
                for (String key : requiredDatas.get(required).keySet()) {
                    Toast.makeText(mContext,
                            requiredDatas.get(required).get(key).message,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root><form>");
        if (submitDatas != null) {
            for (String key : submitDatas.keySet()) {
                for (String key2 : submitDatas.get(key).keySet()) {
                    sb.append("<item name=\""
                            + submitDatas.get(key).get(key2).name + "\">"
                            + submitDatas.get(key).get(key2).value + "</item>");
                }
            }
        }
        System.out.println(sb.toString());

        new Thread() {
            public void run() {
                synchronized (Constants.lock) {
                    FileCache.setSerializableCacheFile(getActivity(),
                            detailEntry);
                }
            }
        }.start();
       /* Bundle bundle = new Bundle();
        bundle.putSerializable("entry", menuModule);
        bundle.putString("tabType", tabType);
        bundle.putString("taskId", taskId);
        bundle.putString("currNote", currNote);
        bundle.putString("displayDevice", displayDevice);
        bundle.putString("deviceResult", deviceResult);
        bundle.putString("device_no", device_no);
        bundle.putString("mark_tz", "2");// 跳转标示
        bundle.putSerializable("submit", (Serializable) submitDatas);
        // Fragment f = new SinopecApproveOARouteFragment2(backToList,
        // targetContainer);
        Fragment f = new PollingOperationFragment(backToList, targetContainer);
        f.setArguments(bundle);
        ((ActivityInTab) getActivity()).navigateTo(f);*/

    }

    // 查询
    public String[] selectSpinnerData() {
        String sql = "select distinct sc_ds_type from anjian_data";
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        db=new DatabaseHelper(mContext);
//        db = SQLiteDatabase.openOrCreateDatabase(path, null);
        // 查询数据返回游标对象
        Cursor c = db.getWritableDatabase().rawQuery(sql, null);
        String[] options = new String[c.getCount()];
        // 到了提交的时刻了
        int i = 0;
        while (c.moveToNext()) {
            options[i] = c.getString(0);
            i++;
        }
        c.close();
        return options;
    }

    // 定义按钮的响应,覆写系统的默认处理
    class ButtonHandler extends Handler {

        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<DialogInterface>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(
                            mDialog.get(), msg.what);
                    break;
            }
        }
    }

    class MHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    softUpdateDialog.show();// 显示对话框
                    break;
            }
            super.handleMessage(msg);
        }

    }

    AlertDialog softUpdateDialog;

    // 下一户
    private void dialogUpdate(final Context mContext) {
        // 如果本地已经下载完了包
        LayoutInflater factoryHis = LayoutInflater.from(getActivity());// 提示框
        View viewDialog = null;
        viewDialog = factoryHis.inflate(R.layout.operation_dialog_layout, null);
        softUpdateDialog = new Builder(mContext)
                .setView(viewDialog).setCancelable(false).create();
        softUpdateDialog.setView(viewDialog, 0, 0, 0, 0);
        Button btn_dialog_ok = (Button) viewDialog
                .findViewById(R.id.btn_dialog_ok);
        TextView textView = (TextView) viewDialog.findViewById(R.id.content);
        textView.setText("保存完成，是否继续安检下一户？");
        btn_dialog_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // new ApproveDetailTask().execute(taskId, "next");
                softUpdateDialog.dismiss();
                // new ApproveDetailTask().execute(taskId, ""); // 下载数据
            }
        });
        Button btn_dialog_cancel = (Button) viewDialog
                .findViewById(R.id.btn_dialog_cancel);
        btn_dialog_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                softUpdateDialog.dismiss();
                approveRouteSuccess1(targetContainer, backToList);
            }
        });
        ButtonHandler buttonHandler = new ButtonHandler(softUpdateDialog);
        // 设定对话框的处理Handler
        try {
            Field field = softUpdateDialog.getClass()
                    .getDeclaredField("mAlert");
            field.setAccessible(true);
            // 获得mAlert变量的值
            Object obj = field.get(softUpdateDialog);
            field = obj.getClass().getDeclaredField("mHandler");
            field.setAccessible(true);
            // 修改mHandler变量的值，使用新的ButtonHandler类
            field.set(obj, buttonHandler);
        } catch (Exception e) {
        }
        // 显示对话框
        Message msg = new Message();
        msg.what = 0;
        msg.setTarget(new MHandler());
        msg.sendToTarget();
    }

    // 保存数据
    public void saveData(String userid) {
        // 当前时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new Date());

        // 将数据插入到安检上传表中,首先判断该用户在上传表中是否存在,进行插入或更新

        String sql_isSave = "select * from uploadcustInfo_aj where  accountId='"
                + taskId
                + "' and cmSchedId = '"
                + custInfo.getCmSchedId()
                + "'";
        Cursor executeQuery = null;
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();

            executeQuery = instance.rawQuery(sql_isSave,new String[]{});
            if (spinner_Jg != null) {
                jgString = spinner_Jg.equals("已张贴") ? "Y" : "N";
            }
            String beiJu = et_beizhu_edit.getText().toString().trim();
            if (user_anjian_tzd != null) {
                tzd = user_anjian_tzd.getText().toString().trim();
            }
            if (TextUtils.isEmpty(beiJu)) {
                beiJu = "";
            }

            if (jiliang_edit != null) {
                jiLiang = jiliang_edit.getText().toString().trim();
            }

            if (syqiliang_edit != null) {
                syqiliang = syqiliang_edit.getText().toString().trim();
            }

            executeQuery.moveToLast();
            int a = executeQuery.getCount();
            if (a == 0) {
                // 表中不存在该用户,插入

                String sql = "insert into uploadcustInfo_aj (cmSchedId,accountId,cmScDttm,cmScAjrh,cmScYhzg,cmScZgtzd,cmScZtjs,cmMr,"
                        + "cmScSyql,cmScRemark,cmMrState,readType) "
                        + "values (" + "'"
                        + custInfo.getCmSchedId()
                        + "',"
                        + "'"
                        + taskId
                        + "',"
                        + "'"
                        + date
                        + "',"
                        + "'"
                        + aj_spinnerCode
                        + "',"
                        + "'"
                        + code_yinHuanZG
                        + "',"
                        + "'"
                        + tzd
                        + "',"
                        + "'"
                        + jgString
                        + "',"
                        + "'"
                        + jiLiang
                        + "',"
                        + "'"
                        + syqiliang
                        + "',"
                        + "'"
                        + beiJu
                        + "',"
                        + "'"
                        + 3
                        + "' , '" + spinner_dushu + "')";

                instance.execSQL(sql);
            } else {
                // 表中存在该用户,更新

                String sql = "update  uploadcustInfo_aj set cmScDttm = '"
                        + date + "', " + "cmScAjrh = '" + aj_spinnerCode
                        + "' ," + "cmScZtjs = '" + jgString + "' ,"
                        + "cmMr = '" + jiLiang + "' ," + "cmScSyql = '"
                        + syqiliang + "' ," + "cmScRemark = '" + beiJu + "' ,"
                        + "cmMrState = '" + 3 + "',readType ='" + spinner_dushu
                        + "' ,cmScYhzg = '" + code_yinHuanZG
                        + "' ,cmScZgtzd = '" + tzd + "'  where  accountId= '"
                        + taskId + "' and cmSchedId = '"
                        + custInfo.getCmSchedId() + "'";
                instance.execSQL(sql);

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (executeQuery != null) {
                try {
                    executeQuery.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if (conne != null) {
//                try {
//                    conne.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
        }
    }

    // 保存后修改客户表状态

    protected void changeCmState(String taskId) {

        String sql_changeState = "update custInfo_ju_aj set cmMrState = " + "'"
                + 3 + "' where  accountId=" + "'" + taskId
                + "' and cmSchedId = '" + custInfo.getCmSchedId() + "'";
        try {
//            conne = new DatabaseHelper(mContext);
//
//            state = conne.getWritableDatabase();

            instance.execSQL(sql_changeState);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 提交
    public void submitData(String state, String userid) {
        final StringBuilder photoBuilder = new StringBuilder();
        final StringBuilder audioBuilder = new StringBuilder();
        for (int i = 0; i < imgList.size(); i++) {
            photoBuilder.append(imgList.get(i).getUrl() + separator);
        }
        for (int i = 0; i < audioList.size(); i++) {
            audioBuilder.append(audioList.get(i).getUrl() + separator);
        }
        if (!photoBuilder.toString().equals("")) {
            if (photoBuilder.toString().lastIndexOf(separator) > 0) {
                photo_url = photoBuilder.toString().substring(0,
                        photoBuilder.toString().lastIndexOf(separator));
            }
        }
        if (!audioBuilder.toString().equals("")) {
            if (audioBuilder.toString().lastIndexOf(separator) > 0) {
                audio_url = audioBuilder.toString().substring(0,
                        audioBuilder.toString().lastIndexOf(separator));
            }
        }
        // if (beizhu_edit != null) {
        // beizhu = beizhu_edit.getText().toString().trim();
        // }
        String isAQString = "否";
        if (isAQ > 0) {
            isAQString = "是";
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss");
        String date = sDateFormat.format(new Date());
        String sql = "update anjian_data set state=" + "'" + state + "'"
                + ",beizhu=" + "'" + aj_beizhu.getText().toString().trim()
                + "'" + ",wancheng_time=" + "'" + date + "'" + ",jlb_dushu="
                + "'" + aj_biaozi.getText().toString() + "'" + ",aj_ruhu_qk="
                + "'" + aj_biaozi.getText().toString() + "'" + ",aj_date="
                + "'" + date + "'" + ",yhqk_tongzhi_no=" + "'"
                + aj_tongzhidan.getText().toString() + "'" + ",yhqk_is=" + "'"
                + isAQString + "'" + ",audio_url=" + "'" + audio_url + "'"
                + ",photo_url=" + "'" + photo_url + "'" + " where user_id="
                + "'" + userid + "'" + "";
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        db = new DatabaseHelper(mContext);
        // 查询数据返回游标对象
        db.getWritableDatabase().execSQL(sql);
        db.close();

    }

    // 提交已保存的
    public void submitYBC(String userid) {
        String sql = "update anjian_data set state='2' where user_id=" + "'"
                + userid + "'" + "";
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        db = new DatabaseHelper(mContext);
        // 查询数据返回游标对象
        db.getWritableDatabase().execSQL(sql);
        db.close();

    }

    private ViewHolder holder;

    static class ViewHolder {
        ImageView imageView;
        ImageView delete_iv;
        ImageView audio_play;
        TextView time_len;
    }

    class AudioGridAdapter extends BaseAdapter {

        private Context mContext;
        private int maxPic;
        private boolean reachMax = false;
        private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示

        public AudioGridAdapter(Context mContext) {
            this.mContext = mContext;

        }

        public void setIsShowDelete(boolean isShowDelete) {
            this.isShowDelete = isShowDelete;
            notifyDataSetChanged();
        }

        public AudioGridAdapter(Context mContext, int maxPic) {
            this(mContext);
            this.maxPic = maxPic;
        }

        public boolean isReachMax() {
            return reachMax;
        }

        public int getMaxPic() {
            return maxPic;
        }

        public void setMaxPic(int maxPic) {
            this.maxPic = maxPic;
        }

        @Override
        public int getCount() {
            return audioList.size();
        }

        @Override
        public Object getItem(int idx) {
            return audioList.get(idx);
        }

        @Override
        public long getItemId(int idx) {
            return idx;
        }

        @Override
        public View getView(final int idx, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = ((LayoutInflater) (getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                        .inflate(R.layout.polling_photo_item, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView
                        .findViewById(R.id.polling_photo_iv);
                holder.audio_play = (ImageView) convertView
                        .findViewById(R.id.polling_audio_paly);
                holder.time_len = (TextView) convertView
                        .findViewById(R.id.time_len);
                holder.delete_iv = (ImageView) convertView
                        .findViewById(R.id.delete_markView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.delete_iv.setVisibility(isShowDelete ? View.VISIBLE
                    : View.GONE);// 设置删除按钮是否显示
            try {
                Resources res = getResources();
                Bitmap bmp = BitmapFactory.decodeResource(res,
                        R.drawable.voice_rcd_btn_disable);
                holder.audio_play.setVisibility(View.VISIBLE);
                holder.audio_play.setBackgroundResource(R.drawable.sound);
                holder.time_len.setVisibility(View.VISIBLE);
                holder.time_len.setText(audioList.get(idx).getTimeLength()
                        + "\"");
                holder.imageView.setBackgroundColor(getResources().getColor(
                        R.color.list_top));

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return convertView;
        }

    }

    private static final int POLL_INTERVAL = 300;
    private static final String ActivityInTab = null;

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            stop();
        }
    };
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };

    private void start(String name) {
        mSensor.start(name);
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        volume.setImageResource(R.drawable.amp1);
    }

    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
            case 1:
                volume.setImageResource(R.drawable.amp1);
                break;
            case 2:
            case 3:
                volume.setImageResource(R.drawable.amp2);

                break;
            case 4:
            case 5:
                volume.setImageResource(R.drawable.amp3);
                break;
            case 6:
            case 7:
                volume.setImageResource(R.drawable.amp4);
                break;
            case 8:
            case 9:
                volume.setImageResource(R.drawable.amp5);
                break;
            case 10:
            case 11:
                volume.setImageResource(R.drawable.amp6);
                break;
            default:
                volume.setImageResource(R.drawable.amp7);
                break;
        }
    }

    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private Spinner aj_spinner_new;
    private LinearLayout device_layout_new;
    private TextView user_name1, user_no;
//    private DatabaseHelper conne;
//    private SQLiteDatabase state;
    private CustInfo_AnJian custInfo;
    private ArrayList<CmMrComm_Dic> cmMrComm_list;
    private TextView user_address, user_data, tv_biaoju, tv_liguan, tv_zaoju,
            tv_lianjieguan, tv_cainuanlu, tv_reshuiqi, tv_baojingqi, tv_title1,
            tv_title2, tv_title3, tv_title4, tv_title_shebeiziliao,
            tv_oldissafe_anjian, tv_morephoto,safe_detail_textview;
    private ArrayList<CmScSpItem> cmScSpItem_list;
    private RelativeLayout rl_safe_explain;
    private Spinner spinner_jgbiao;
    private LinearLayout device_layout_container;
    private ArrayList<CmScShType_Dic> cmScShType_list;
    private FlowLayout flowLayout;
    private EditText jiliang_edit, syqiliang_edit, et_beizhu_edit;
    private UploadcustInfo_AnJian uploadcustInfo = null;
    private ArrayList<readType> readType_list;
    private ArrayList<readType> typedata;
    private String state_up;
    private String stateString;
    private LinearLayout ll_ruhuqingkuang, ll_qitaxinxi, ll_shebeixinxi,
            ll_showshebeixinxi, ll_showpacter;
    private ImageView iv_pic_qita, iv_pic_jiben, iv_pic_anjian,
            iv_pic_zhaopian, iv_pic_shebeiziliao, iv_pic_qianming, image2,
            image1;
    private View view;
    private RelativeLayout user_view_anjian;
    private ArrayList<CmScYhzg> cmScYhzgdata;
    private String tzd = "";
    private String jgString = "";
    private String jiLiang = "";
    private String syqiliang = "";
    private RadioGroup radiogroup;
    private MyBroadCast myBroadCast;
    private ArrayList<String> imageUrl_List;

    /**
     * @param name
     * @Description
     */
    private void playMusic(String name) {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(name);
            mMediaPlayer.prepare();
            // File file = new File(name);
            // FileInputStream fis = new FileInputStream(file);
            // mMediaPlayer.setDataSource(fis.getFD());
            // mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 恢复adapter
    public void initAdapterAudio() {

        isShowDelete_audio = false;
        audioAdapter.setIsShowDelete(isShowDelete_audio);
    }

    // 恢复adapter
    public void initAdapterIMG() {
        isShowDelete_img = false;
        imgAdapter.setIsShowDelete(isShowDelete_img);
    }

    private void initViewAudio() {
        audioAdapter = new AudioGridAdapter(getActivity(), MAX_PICS);
        audioGrid.setAdapter(audioAdapter);
        audioGrid.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                if (isShowDelete_audio) {
                    isShowDelete_audio = false;
                } else {
                    isShowDelete_audio = true;
                }
                audioAdapter.setIsShowDelete(isShowDelete_audio);
                return true;
            }
        });
        audioGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (isShowDelete_audio) {
                    audioList.remove(position);
                    audioAdapter.notifyDataSetChanged();
                } else {
                    if (audioList.get(position).getUrl().contains(".mp3")) {
                        playMusic(audioList.get(position).getUrl().toString());
                    }
                }

            }
        });
    }

    private void initViewPhoto() {
        imgAdapter = new GridAdapter(getActivity(), MAX_PICS);
        imageGrid.setAdapter(imgAdapter);
        imageGrid.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                if (isShowDelete_img) {
                    isShowDelete_img = false;
                } else {
                    isShowDelete_img = true;
                }
                imgAdapter.setIsShowDelete(isShowDelete_img);
                return true;
            }
        });
        imageGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (isShowDelete_img) {
                    imgList.remove(position);
                    imgAdapter.notifyDataSetChanged();
                } else {
                    System.out.println("====当前处于非删除状态");
                }
            }
        });

    }

    class GridAdapter extends BaseAdapter {

        private Context mContext;
        private int maxPic;
        private boolean reachMax = false;
        private String type;
        private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示

        public GridAdapter(Context mContext) {
            this.mContext = mContext;

        }

        public GridAdapter(Context mContext, int maxPic) {
            this(mContext);
            this.maxPic = maxPic;
        }

        public void setIsShowDelete(boolean isShowDelete) {
            this.isShowDelete = isShowDelete;
            notifyDataSetChanged();
        }

        public boolean isReachMax() {
            return reachMax;
        }

        public int getMaxPic() {
            return maxPic;
        }

        public void setMaxPic(int maxPic) {
            this.maxPic = maxPic;
        }

        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public Object getItem(int idx) {
            return imgList.get(idx);
        }

        @Override
        public long getItemId(int idx) {
            return idx;
        }

        @Override
        public View getView(final int idx, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = ((LayoutInflater) (getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                        .inflate(R.layout.polling_photo_item, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView
                        .findViewById(R.id.polling_photo_iv);
                holder.audio_play = (ImageView) convertView
                        .findViewById(R.id.polling_audio_paly);
                holder.time_len = (TextView) convertView
                        .findViewById(R.id.time_len);
                holder.delete_iv = (ImageView) convertView
                        .findViewById(R.id.delete_markView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.delete_iv.setVisibility(isShowDelete ? View.VISIBLE
                    : View.GONE);// 设置删除按钮是否显示
            Bitmap btp = null;
            try {
                // is = new FileInputStream(imgList.get(idx));
                btp = ImageUtils.getBitmapImage(imgList.get(idx).getUrl());
                holder.audio_play.setVisibility(View.GONE);
                holder.time_len.setVisibility(View.GONE);
                holder.imageView.setImageBitmap(btp);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return convertView;
        }

    }

//    protected String getImageRealPath(Uri imageURI) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        cursor = getActivity().managedQuery(imageURI, proj, null, null, null);
//        int column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }

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
        if (!imagefile.exists()) {
            try {
                imagefile.createNewFile();
                FileOutputStream fos = new FileOutputStream(imagefile);
                colorImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {// 图片所在SD卡的路径
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 800);// 自定义一个宽和高
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);// 压缩好比例大小后再进行质量压缩
    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;// 获取图片的高
        final int width = options.outWidth;// 获取图片的框
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;// 求出缩放值
    }

    // @SuppressLint("NewApi")
    // @Override
    // public void onActivityResult(int requestCode, int resultCode, Intent
    // data) {
    // super.onActivityResult(requestCode, resultCode, data);
    // // Bitmap bm = null;
    // PhotoAudio photoAudio = null;
    // if (resultCode == Activity.RESULT_OK) {
    // switch (requestCode) {
    // case CHOOSE_IMAGE:
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
    // break;
    // case CAMERA_RESULT: // 从照相机获取，显示缩略图，并添加路径到列表中
    // // String outPath1 = sdPath + FileUtil.getUUid() + ".jpg";
    //
    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    // final String time_now = format.format(new Date()).replace("-",
    // "");
    // SimpleDateFormat format1 = new SimpleDateFormat(
    // "yyyy-MM-ddHH:mm:ss");
    // String time = format1.format(new Date()).replace("-", "")
    // .replace(":", "");
    //
    // String outPath1 = sdPath + time_now + "/" + Constants.loginName
    // + "/" + custInfo.getServicePointId() + "_" + time
    // + ".jpg";
    // Bitmap bitmap = getimage(imagePath);
    // System.out.println(bitmap.getByteCount());
    // storeImageToSDCARD(bitmap, outPath1);
    // photoAudio = new PhotoAudio();
    // photoAudio.setUrl(outPath1);
    // imgList.add(photoAudio);
    // initAdapterIMG();
    // btn_next.setVisibility(View.VISIBLE);
    // // System.out.println(imagePath);
    // // File tfile1 = new File(outPath1);
    // // if (!tfile1.getParentFile().exists())
    // // tfile1.getParentFile().mkdirs();
    // // ImageUtils.shrinkFromFile(imagePath, imagePath, 400, 800);
    // break;
    //
    // }
    //
    // }
    // }

    public void createAnJianView(LinearLayout layout, String content) {
        View userView = layoutInflater.inflate(
                R.layout.anjian_approve_detail_child2, null, false);
        imageGrid = (GridView) userView.findViewById(R.id.photo_GridView01);
        photoButton = (Button) userView.findViewById(R.id.btn_take_photo);
        photoButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated
                // method stub
                if (imgAdapter != null) {

                    if (imgAdapter.getCount() == MAX_PICS) {
                        Toast.makeText(getActivity(),
                                "最多可上传" + MAX_PICS + "张照片", Toast.LENGTH_SHORT).show();
                    } else {
                        imagePath = sdPath + FileUtil.getUUid() + ".jpg";
                        File imageFile = new File(imagePath);
                        if (!imageFile.getParentFile().exists())
                            imageFile.getParentFile().mkdirs();
                        imageURI = Uri.fromFile(imageFile);
                        Intent i = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        i.putExtra(MediaStore.EXTRA_OUTPUT,
                                imageURI);
                        startActivityForResult(i, CAMERA_RESULT);
                    }
                }
            }
        });
        imageGrid.setPadding(
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10));
        initViewPhoto();
        initAdapterIMG();
        audioGrid = (GridView) userView.findViewById(R.id.audio_GridView01);
        audioButton = (Button) userView.findViewById(R.id.btn_take_audio);
        mSensor = new SoundMeter();
        audioButton.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 按下语音录制按钮时返回false执行父类OnTouch
                int[] location = new int[2];
                // btn_take_audio.getLocationInWindow(location);
                // //
                // 获取在当前窗口内的绝对坐标
                int btn_rc_Y = location[1];
                int btn_rc_X = location[0];
                int[] del_location = new int[2];
                del_re.getLocationInWindow(del_location);
                int del_Y = del_location[1];
                int del_x = del_location[0];
                if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
                    if (!Environment.getExternalStorageDirectory().exists()) {
                        Toast.makeText(getActivity(), "No SDCard",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {
                        // 判断手势按下的位置是否是语音录制按钮的范围内
                        audioButton
                                .setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
                        rcChat_popup.setVisibility(View.VISIBLE);
                        voice_rcd_hint_loading.setVisibility(View.VISIBLE);
                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        voice_rcd_hint_tooshort.setVisibility(View.GONE);
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                if (!isShosrt) {
                                    voice_rcd_hint_loading
                                            .setVisibility(View.GONE);
                                    voice_rcd_hint_rcding
                                            .setVisibility(View.VISIBLE);
                                }
                            }
                        }, 300);
                        img1.setVisibility(View.VISIBLE);
                        del_re.setVisibility(View.GONE);
                        startVoiceT = System.currentTimeMillis();
                        voiceName = FileUtil.getUUid() + ".mp3";
                        start(voiceName);
                        flag = 2;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP
                        && flag == 2) {// 松开手势时执行录制完成
                    audioButton.setBackgroundResource(R.drawable.anniu2);
                    if (event.getY() >= del_Y
                            && event.getY() <= del_Y + del_re.getHeight()
                            && event.getX() >= del_x
                            && event.getX() <= del_x + del_re.getWidth()) {
                        rcChat_popup.setVisibility(View.GONE);
                        img1.setVisibility(View.VISIBLE);
                        del_re.setVisibility(View.GONE);
                        stop();
                        flag = 1;
                        File file = new File(sdPath + voiceName);
                        if (file.exists()) {
                            file.delete();
                        }
                    } else {
                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        stop();
                        endVoiceT = System.currentTimeMillis();
                        System.out
                                .println(endVoiceT + "========" + startVoiceT);
                        flag = 1;
                        int time = (int) ((endVoiceT - startVoiceT) / 1000);
                        if (time < 1) {
                            isShosrt = true;
                            voice_rcd_hint_loading.setVisibility(View.GONE);
                            voice_rcd_hint_rcding.setVisibility(View.GONE);
                            voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    voice_rcd_hint_tooshort
                                            .setVisibility(View.GONE);
                                    rcChat_popup.setVisibility(View.GONE);
                                    isShosrt = false;
                                }
                            }, 500);
                            return false;
                        }
                        PhotoAudio photoAudio = new PhotoAudio();
                        photoAudio.setUrl(sdPath + voiceName);
                        photoAudio.setTimeLength(time + "");
                        audioList.add(photoAudio);
                        initAdapterAudio();
                    }
                }
                if (event.getY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内
                    System.out.println("5");
                    Animation mLitteAnimation = AnimationUtils.loadAnimation(
                            getActivity(), R.anim.cancel_rc);
                    Animation mBigAnimation = AnimationUtils.loadAnimation(
                            getActivity(), R.anim.cancel_rc2);
                    img1.setVisibility(View.GONE);
                    del_re.setVisibility(View.VISIBLE);
                    del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
                    if (event.getY() >= del_Y
                            && event.getY() <= del_Y + del_re.getHeight()
                            && event.getX() >= del_x
                            && event.getX() <= del_x + del_re.getWidth()) {
                        del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
                        sc_img1.startAnimation(mLitteAnimation);
                        sc_img1.startAnimation(mBigAnimation);
                    }
                } else {
                    img1.setVisibility(View.VISIBLE);
                    del_re.setVisibility(View.GONE);
                    del_re.setBackgroundResource(0);
                }

                return true;
            }
        });
        audioGrid.setPadding(
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10));
        initViewAudio();
        initAdapterAudio();
        final LinearLayout device_layout = (LinearLayout) userView
                .findViewById(R.id.device_layout);
        final Spinner aj_spinner = (Spinner) userView
                .findViewById(R.id.aj_spinner);

        final TextView tv_isYH = (TextView) userView.findViewById(R.id.tv_isYH);
        String[] strings = new String[4];
        strings = content.split(";#");
        TextView textView = (TextView) userView.findViewById(R.id.tv_left);
        TextView user_name = (TextView) userView.findViewById(R.id.user_name1);
        TextView user_address = (TextView) userView
                .findViewById(R.id.user_address);
        textView.setText("上次安检:" + strings[0] + " 正常");
        user_name.setText(strings[1]);
        user_address.setText(strings[2]);
        RelativeLayout user_viewLayout = (RelativeLayout) userView
                .findViewById(R.id.user_view);
        user_viewLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString("id", taskId);
                Fragment f = new AnJianUserInfoFragment(backToList,
                        targetContainer);
                f.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(f);
            }
        });
        aj_beizhu = (EditText) userView.findViewById(R.id.beizhu_edit);
        aj_tongzhidan = (EditText) userView.findViewById(R.id.zhenggaidan_edit);
        aj_biaozi = (EditText) userView.findViewById(R.id.biaozi_edit);
        aj_beizhu.setOnEditorActionListener(new emailFinish());
        final String[] options = new String[4];
        options[0] = "请选择";
        options[1] = "正常入户";
        options[2] = "到访不遇";
        options[3] = "拒绝入内";
        aj_spinner
                .setBackgroundResource(R.drawable.ic_approve_spinner_background);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                R.layout.spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aj_spinner.setAdapter(adapter);
        aj_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                aj_spinnerString = options[arg2];
                if (aj_spinnerString.equals("正常入户")) {
                    device_layout.setVisibility(View.VISIBLE);
                } else {
                    device_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        final List<String> list = new ArrayList<String>();
        list.add("正常");
        list.add("异常");
        // 支管
        TextView zhiguanTextView = (TextView) userView
                .findViewById(R.id.zhiguan);
        zhiguanTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        zhiguanTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString("id", taskId);
                bundle.putString("device_name", "支管");
                Fragment f = new AnJianDetailFormFragment();
                f.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(f);
            }
        });
        aj_zg_spinner = (Spinner) userView.findViewById(R.id.aj_zg_spinner);
        aj_zg_spinner.setAdapter(new SpinnerAdapter(mContext, list));
        aj_zg_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (list.get(arg2).equals("异常")) {
                    tv_isYH.setText("是");
                    isAQ++;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", taskId);
                    bundle.putString("device_name", "支管");
                    Fragment f = new AnJianDeviceDetailFragment();
                    f.setArguments(bundle);
                    ((ActivityInTab) getActivity()).navigateTo(f);
                } else {
                    if (isAQ > 0) {
                        isAQ--;
                    }
                    if (isAQ == 0) {
                        tv_isYH.setText("否");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        // 灶具管
        TextView zhaojuguanTextView = (TextView) userView
                .findViewById(R.id.zaojuguan);
        zhaojuguanTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        zhaojuguanTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString("id", taskId);
                bundle.putString("device_name", "灶具连接管");
                Fragment f = new AnJianDetailFormFragment();
                f.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(f);

            }
        });
        aj_zjg_spinner = (Spinner) userView.findViewById(R.id.aj_zjg_spinner);
        aj_zjg_spinner.setAdapter(new SpinnerAdapter(mContext, list));
        aj_zjg_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (list.get(arg2).equals("异常")) {
                    tv_isYH.setText("是");
                    isAQ++;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", taskId);
                    bundle.putString("device_name", "灶具连接管");
                    Fragment f = new AnJianDeviceDetailFragment();
                    f.setArguments(bundle);
                    ((ActivityInTab) getActivity()).navigateTo(f);
                } else {
                    if (isAQ > 0) {
                        isAQ--;
                    }
                    if (isAQ == 0) {
                        tv_isYH.setText("否");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        // 阀门
        TextView famenTextView = (TextView) userView.findViewById(R.id.famen);
        famenTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        famenTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString("id", taskId);
                bundle.putString("device_name", "阀门");
                Fragment f = new AnJianDetailFormFragment();
                f.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(f);
            }
        });
        aj_fm_spinner = (Spinner) userView.findViewById(R.id.aj_fm_spinner);
        aj_fm_spinner.setAdapter(new SpinnerAdapter(mContext, list));
        aj_fm_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (list.get(arg2).equals("异常")) {
                    tv_isYH.setText("是");
                    isAQ++;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", taskId);
                    bundle.putString("device_name", "阀门");
                    Fragment f = new AnJianDeviceDetailFragment();
                    f.setArguments(bundle);
                    ((ActivityInTab) getActivity()).navigateTo(f);
                } else {
                    if (isAQ > 0) {
                        isAQ--;
                    }
                    if (isAQ == 0) {
                        tv_isYH.setText("否");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        // 热水器
        TextView reshuiqiTextView = (TextView) userView
                .findViewById(R.id.reshuiqi);
        reshuiqiTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        reshuiqiTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString("id", taskId);
                bundle.putString("device_name", "热水器");
                Fragment f = new AnJianDetailFormFragment();
                f.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(f);
            }
        });
        aj_rsq_spinner = (Spinner) userView.findViewById(R.id.aj_rsq_spinner);
        aj_rsq_spinner.setAdapter(new SpinnerAdapter(mContext, list));
        aj_rsq_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (list.get(arg2).equals("异常")) {
                    tv_isYH.setText("是");
                    isAQ++;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", taskId);
                    bundle.putString("device_name", "热水器");
                    Fragment f = new AnJianDeviceDetailFragment();
                    f.setArguments(bundle);
                    ((ActivityInTab) getActivity()).navigateTo(f);
                } else {
                    if (isAQ > 0) {
                        isAQ--;
                    }
                    if (isAQ == 0) {
                        tv_isYH.setText("否");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        // 表具
        TextView biaojuTextView = (TextView) userView.findViewById(R.id.biaoju);
        biaojuTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		/*
		 * biaojuTextView.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Bundle bundle = new Bundle(); bundle.putString("id", taskId);
		 * bundle.putString("device_name", "表具"); Fragment f = new
		 * AnJianDetailFormFragment(); f.setArguments(bundle); ((ActivityInTab)
		 * getActivity()).navigateTo(f); } });
		 */
        aj_bj_spinner = (Spinner) userView.findViewById(R.id.aj_bj_spinner);
        aj_bj_spinner.setAdapter(new SpinnerAdapter(mContext, list));
        aj_bj_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (list.get(arg2).equals("异常")) {
                    tv_isYH.setText("是");
                    isAQ++;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", taskId);
                    bundle.putString("device_name", "表具");
                    Fragment f = new AnJianDeviceDetailFragment();
                    f.setArguments(bundle);
                    ((ActivityInTab) getActivity()).navigateTo(f);
                } else {
                    if (isAQ > 0) {
                        isAQ--;
                    }
                    if (isAQ == 0) {
                        tv_isYH.setText("否");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        // 采暖炉
        TextView cainuanluTextView = (TextView) userView
                .findViewById(R.id.cainuanlu);
        cainuanluTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        cainuanluTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString("id", taskId);
                bundle.putString("device_name", "采暖炉");
                Fragment f = new AnJianDetailFormFragment();
                f.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(f);
            }
        });
        aj_cnl_spinner = (Spinner) userView.findViewById(R.id.aj_cnl_spinner);
        aj_cnl_spinner.setAdapter(new SpinnerAdapter(mContext, list));
        aj_cnl_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (list.get(arg2).equals("异常")) {
                    tv_isYH.setText("是");
                    isAQ++;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", taskId);
                    bundle.putString("device_name", "采暖炉");
                    Fragment f = new AnJianDeviceDetailFragment();
                    f.setArguments(bundle);
                    ((ActivityInTab) getActivity()).navigateTo(f);
                } else {
                    if (isAQ > 0) {
                        isAQ--;
                    }
                    if (isAQ == 0) {
                        tv_isYH.setText("否");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        // 灶具
        TextView zaojuTextView = (TextView) userView.findViewById(R.id.zaoju);
        zaojuTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        zaojuTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString("id", taskId);
                bundle.putString("device_name", "灶具");
                Fragment f = new AnJianDetailFormFragment();
                f.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(f);
            }
        });
        aj_zj_spinner = (Spinner) userView.findViewById(R.id.aj_zj_spinner);
        aj_zj_spinner.setAdapter(new SpinnerAdapter(mContext, list));
        aj_zj_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (list.get(arg2).equals("异常")) {
                    tv_isYH.setText("是");
                    isAQ++;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", taskId);
                    bundle.putString("device_name", "灶具");
                    Fragment f = new AnJianDeviceDetailFragment();
                    f.setArguments(bundle);
                    ((ActivityInTab) getActivity()).navigateTo(f);
                } else {
                    if (isAQ > 0) {
                        isAQ--;
                    }
                    if (isAQ == 0) {
                        tv_isYH.setText("否");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        // 其他
        TextView qita = (TextView) userView.findViewById(R.id.qita);
        qita.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        aj_qt_spinner = (Spinner) userView.findViewById(R.id.aj_qt_spinner);
        aj_qt_spinner.setAdapter(new SpinnerAdapter(mContext, list));
        aj_qt_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (list.get(arg2).equals("异常")) {
                    tv_isYH.setText("是");
                    isAQ++;
                    Bundle bundle = new Bundle();
                    bundle.putString("id", taskId);
                    bundle.putString("device_name", "其他");
                    Fragment f = new AnJianDeviceDetailFragment();
                    f.setArguments(bundle);
                    ((ActivityInTab) getActivity()).navigateTo(f);
                } else {
                    if (isAQ > 0) {
                        isAQ--;
                    }
                    if (isAQ == 0) {
                        tv_isYH.setText("否");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        layout.addView(userView);
    }

    public class SpinnerAdapter extends BaseAdapter {
        private List<String> mList;
        private Context mContext;

        public SpinnerAdapter(Context pContext, List<String> pList) {
            this.mContext = pContext;
            this.mList = pList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 下面是重要代码
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
            convertView = _LayoutInflater.inflate(R.layout.anjian_spinner_item,
                    null);
            if (convertView != null) {
                TextView _TextView1 = (TextView) convertView
                        .findViewById(R.id.spinner_item);
                ImageView _ImageView = (ImageView) convertView
                        .findViewById(R.id.spinner_iv);
                _TextView1.setText(mList.get(position));
                if (mList.get(position).equals("正常")) {
                    _ImageView.setBackgroundResource(R.drawable.zhengchang);
                } else {
                    _ImageView.setBackgroundResource(R.drawable.yichang);
                }
            }

            return convertView;
        }
    }

    // 拍照
    private void createPhoto(LinearLayout layout, final String content) {

        View userView = layoutInflater.inflate(R.layout.cb_photo_layout, null,
                false);
        imageGrid = (GridView) userView.findViewById(R.id.GridView01);

        photoButton = (Button) userView.findViewById(R.id.btn_take_photo);
        photoButton.setText("拍照");
        photoButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated
                // method stub
                if (imgAdapter != null) {

                    if (imgAdapter.getCount() == MAX_PICS) {
                        Toast.makeText(getActivity(),
                                "最多可上传" + MAX_PICS + "张照片", Toast.LENGTH_SHORT).show();
                    } else {
                        // imagePath = sdPath + FileUtil.getUUid() + ".jpg";
                        imagePath = sdPath + FileUtil.getUUid() + ".jpg";
                        File imageFile = new File(imagePath);
                        if (!imageFile.getParentFile().exists())
                            imageFile.getParentFile().mkdirs();
                        imageURI = Uri.fromFile(imageFile);
                        Intent i = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        i.putExtra(MediaStore.EXTRA_OUTPUT,
                                imageURI);
                        startActivityForResult(i, CAMERA_RESULT);
                    }
                }
            }
        });
        imageGrid.setPadding(
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10));
        if (content.contains("(photo)2")) {
            photoButton.setVisibility(View.GONE);
            imgList.clear();
            if (replacePhoto1(content) != "") {
                if (replacePhoto1(content).contains(separator)) {
                    String[] strings = replacePhoto1(content).split(separator);
                    imgList.clear();
                    for (int j = 0; j < strings.length; j++) {
                        PhotoAudio photoAudio = new PhotoAudio();
                        photoAudio.setUrl(strings[j]);
                        imgList.add(photoAudio);
                    }
                } else {
                    PhotoAudio photoAudio = new PhotoAudio();
                    photoAudio.setUrl(replacePhoto1(content));
                    imgList.add(photoAudio);
                }

            }
        }
        initViewPhoto();
        initAdapterIMG();
        layout.addView(userView);

    }

    // 录音
    private void createAudio(LinearLayout layout, String content) {
        View userView = layoutInflater.inflate(R.layout.cb_photo_layout, null,
                false);
        audioGrid = (GridView) userView.findViewById(R.id.GridView01);

        audioButton = (Button) userView.findViewById(R.id.btn_take_photo);
        audioButton.setText("录音");
        mSensor = new SoundMeter();
        audioButton.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 按下语音录制按钮时返回false执行父类OnTouch
                int[] location = new int[2];
                // btn_take_audio.getLocationInWindow(location);
                // //
                // 获取在当前窗口内的绝对坐标
                int btn_rc_Y = location[1];
                int btn_rc_X = location[0];
                int[] del_location = new int[2];
                del_re.getLocationInWindow(del_location);
                int del_Y = del_location[1];
                int del_x = del_location[0];
                if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
                    if (!Environment.getExternalStorageDirectory().exists()) {
                        Toast.makeText(getActivity(), "No SDCard",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {
                        // 判断手势按下的位置是否是语音录制按钮的范围内
                        audioButton
                                .setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
                        rcChat_popup.setVisibility(View.VISIBLE);
                        voice_rcd_hint_loading.setVisibility(View.VISIBLE);
                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        voice_rcd_hint_tooshort.setVisibility(View.GONE);
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                if (!isShosrt) {
                                    voice_rcd_hint_loading
                                            .setVisibility(View.GONE);
                                    voice_rcd_hint_rcding
                                            .setVisibility(View.VISIBLE);
                                }
                            }
                        }, 300);
                        img1.setVisibility(View.VISIBLE);
                        del_re.setVisibility(View.GONE);
                        startVoiceT = System.currentTimeMillis();
                        voiceName = FileUtil.getUUid() + ".mp3";
                        start(voiceName);
                        flag = 2;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP
                        && flag == 2) {// 松开手势时执行录制完成
                    audioButton.setBackgroundResource(R.drawable.anniu2);
                    if (event.getY() >= del_Y
                            && event.getY() <= del_Y + del_re.getHeight()
                            && event.getX() >= del_x
                            && event.getX() <= del_x + del_re.getWidth()) {
                        rcChat_popup.setVisibility(View.GONE);
                        img1.setVisibility(View.VISIBLE);
                        del_re.setVisibility(View.GONE);
                        stop();
                        flag = 1;
                        File file = new File(sdPath + voiceName);
                        if (file.exists()) {
                            file.delete();
                        }
                    } else {
                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        stop();
                        endVoiceT = System.currentTimeMillis();
                        System.out
                                .println(endVoiceT + "========" + startVoiceT);
                        flag = 1;
                        int time = (int) ((endVoiceT - startVoiceT) / 1000);
                        if (time < 1) {
                            isShosrt = true;
                            voice_rcd_hint_loading.setVisibility(View.GONE);
                            voice_rcd_hint_rcding.setVisibility(View.GONE);
                            voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    voice_rcd_hint_tooshort
                                            .setVisibility(View.GONE);
                                    rcChat_popup.setVisibility(View.GONE);
                                    isShosrt = false;
                                }
                            }, 500);
                            return false;
                        }
                        PhotoAudio photoAudio = new PhotoAudio();
                        photoAudio.setUrl(sdPath + voiceName);
                        photoAudio.setTimeLength(time + "");
                        audioList.add(photoAudio);
                        initAdapterAudio();
                    }
                }
                if (event.getY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内
                    System.out.println("5");
                    Animation mLitteAnimation = AnimationUtils.loadAnimation(
                            getActivity(), R.anim.cancel_rc);
                    Animation mBigAnimation = AnimationUtils.loadAnimation(
                            getActivity(), R.anim.cancel_rc2);
                    img1.setVisibility(View.GONE);
                    del_re.setVisibility(View.VISIBLE);
                    del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
                    if (event.getY() >= del_Y
                            && event.getY() <= del_Y + del_re.getHeight()
                            && event.getX() >= del_x
                            && event.getX() <= del_x + del_re.getWidth()) {
                        del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
                        sc_img1.startAnimation(mLitteAnimation);
                        sc_img1.startAnimation(mBigAnimation);
                    }
                } else {
                    img1.setVisibility(View.VISIBLE);
                    del_re.setVisibility(View.GONE);
                    del_re.setBackgroundResource(0);
                }

                return true;
            }
        });
        audioGrid.setPadding(
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10),
                cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(getActivity(), 10));
        if (content.contains("(audio)2")) {
            audioButton.setVisibility(View.GONE);
            audioList.clear();
            if (replaceAudio1(content) != "") {
                if (replacePhoto1(content).contains(separator)) {
                    String[] strings = replaceAudio1(content).split(separator);
                    audioList.clear();
                    for (int j = 0; j < strings.length; j++) {
                        PhotoAudio photoAudio = new PhotoAudio();
                        photoAudio.setUrl(strings[j]);
                        photoAudio.setTimeLength("2");
                        audioList.add(photoAudio);
                    }
                } else {
                    PhotoAudio photoAudio = new PhotoAudio();
                    photoAudio.setUrl(replacePhoto1(content));
                    photoAudio.setTimeLength("3");
                    audioList.add(photoAudio);
                }

            }
        }
        initViewAudio();
        initAdapterAudio();
        layout.addView(userView);

    }

    // 转换html为文本
    public String replaceUser(String str) {
        str = str.replaceAll("\\(user\\)", "");
        return str.toString().trim();
    }

    // 转换html为文本
    public String replaceDevice(String str) {
        str = str.replaceAll("\\(device\\)", "");
        return str.toString().trim();
    }

    // 历史表字
    public String replaceLSBZ(String str) {
        str = str.replaceAll("\\(biaozi\\)", "");
        return str.toString().trim();
    }

    public String replaceQL(String str) {
        str = str.replaceAll("\\(qiliang\\)", "");
        return str.toString().trim();
    }

    public String replaceQF(String str) {
        str = str.replaceAll("\\(qifei\\)", "");
        return str.toString().trim();
    }

    public String replaceBZType(String str) {
        str = str.replaceAll("\\(bztype\\)", "");
        return str.toString().trim();
    }

    public String replaceBeiZhu(String str) {
        str = str.replaceAll("\\(beizhu\\)", "");
        return str.toString().trim();
    }

    public String replacePhoto(String str) {
        str = str.replaceAll("\\(photo\\)", "");
        return str.toString().trim();
    }

    public String replaceAudio(String str) {
        str = str.replaceAll("\\(audio\\)", "");
        return str.toString().trim();
    }

    public String replaceAnJian(String str) {
        str = str.replaceAll("\\(anjian\\)", "");
        return str.toString().trim();
    }

    public String replacePhoto1(String str) {
        str = str.replaceAll("\\(photo\\)2", "");
        return str.toString().trim();
    }

    public String replaceAudio1(String str) {
        str = str.replaceAll("\\(audio\\)2", "");
        return str.toString().trim();
    }

    // 转换html为文本
    public String replaceDate(String str) {
        str = str.replaceAll("\\(date\\)", "");
        return str.toString().trim();
    }

    // 转换html为文本
    public String replaceHtml(String str) {
        str = str.replaceAll("\\(html\\)", "");
        return Html.fromHtml(str).toString();
    }

    // 转换html
    public String replaceHtmlWebView(String str) {
        str = str.replaceAll("\\(html\\)", "");
        return str;
    }

    // 转换image
    public String replaceImageWebView(String str) {
        str = str.replaceAll("\\(image\\)", "");
        return str;
    }

    private class emailFinish implements OnEditorActionListener {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // 收起键盘
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        }
    }

    public class MyBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // String action = intent.getAction();
            String imageUrl = intent.getStringExtra("signBitmap");
            // if(action.equals(Constants.GET_KEHU_QIAN_MING)){
            // // 显示图片
            // }
            iv_pic_qianming.setImageBitmap(BitmapFactory.decodeFile(imageUrl));
            // tv_qianming.setVisibility(View.GONE);
            // radiogroup.setFocusable(false);//签名后不可修改服务评价
        }
    }

    ;

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.GET_KEHU_QIAN_MING);
        myBroadCast = new MyBroadCast();
        getActivity().registerReceiver(myBroadCast, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (myBroadCast != null) {
            getActivity().unregisterReceiver(myBroadCast);
        }

        if(instance!=null){
            try{
                instance.close();
            }catch (Exception e){
                e.printStackTrace();
            }


        }

        if(databaseHelperInstance.helper!=null){
            try{
                databaseHelperInstance.helper.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        DatabaseHelperInstance.closeConn();
//    }
}
