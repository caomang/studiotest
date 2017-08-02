package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sunboxsoft.monitor.utils.FTPUtils;
import com.sunboxsoft.monitor.utils.PerfUtils;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.domian.CmMrCommInfo_Dic;
import cn.sbx.deeper.moblie.domian.CmMrComm_Dic;
import cn.sbx.deeper.moblie.domian.CmScAjrhInfo_Dic;
import cn.sbx.deeper.moblie.domian.CmScAjrh_Dic;
import cn.sbx.deeper.moblie.domian.CmScBusiTypeInfo_Dic;
import cn.sbx.deeper.moblie.domian.CmScBusiType_Dic;
import cn.sbx.deeper.moblie.domian.CmScCnlPffs;
import cn.sbx.deeper.moblie.domian.CmScCnlPffsInfo;
import cn.sbx.deeper.moblie.domian.CmScLgfmCz;
import cn.sbx.deeper.moblie.domian.CmScLgfmCzInfo;
import cn.sbx.deeper.moblie.domian.CmScLgfmWz;
import cn.sbx.deeper.moblie.domian.CmScLgfmWzInfo;
import cn.sbx.deeper.moblie.domian.CmScLjgCzInfo;
import cn.sbx.deeper.moblie.domian.CmScResType;
import cn.sbx.deeper.moblie.domian.CmScResTypeInfo;
import cn.sbx.deeper.moblie.domian.CmScRsqPffs;
import cn.sbx.deeper.moblie.domian.CmScRsqPffsInfo;
import cn.sbx.deeper.moblie.domian.CmScShItemInfo;
import cn.sbx.deeper.moblie.domian.CmScShType_Dic;
import cn.sbx.deeper.moblie.domian.CmScSpItem;
import cn.sbx.deeper.moblie.domian.CmScSpItemInfo;
import cn.sbx.deeper.moblie.domian.CmScUserType;
import cn.sbx.deeper.moblie.domian.CmScUserTypeInfo;
import cn.sbx.deeper.moblie.domian.CmScYhzg;
import cn.sbx.deeper.moblie.domian.CmScYhzgInfo;
import cn.sbx.deeper.moblie.domian.CmScZjXhbh;
import cn.sbx.deeper.moblie.domian.CmScZjXhbhInfo;
import cn.sbx.deeper.moblie.domian.CmScZjYs;
import cn.sbx.deeper.moblie.domian.CmScZjYsInfo;
import cn.sbx.deeper.moblie.domian.CustInfo;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.MScLjgCz;
import cn.sbx.deeper.moblie.domian.ManufacturerList;
import cn.sbx.deeper.moblie.domian.MeterType;
import cn.sbx.deeper.moblie.domian.MeterTypeInfo;
import cn.sbx.deeper.moblie.domian.Model;
import cn.sbx.deeper.moblie.domian.ModelInfo;
import cn.sbx.deeper.moblie.domian.OutPut;
import cn.sbx.deeper.moblie.domian.PerPhone;
import cn.sbx.deeper.moblie.domian.PerPhone_Anjian;
import cn.sbx.deeper.moblie.domian.PerSh;
import cn.sbx.deeper.moblie.domian.PhoneTypeInfo;
import cn.sbx.deeper.moblie.domian.PhotoAudio;
import cn.sbx.deeper.moblie.domian.ReadTypeInfo;
import cn.sbx.deeper.moblie.domian.SchedInfo;
import cn.sbx.deeper.moblie.domian.SchedInfoResidents;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.SinopecMenuPage;
import cn.sbx.deeper.moblie.domian.cmScShItem;
import cn.sbx.deeper.moblie.domian.phoneType;
import cn.sbx.deeper.moblie.domian.readType;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCache;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.LogUtil;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 同步数据界面
 *
 * @author terry.C
 */
@SuppressLint("ValidFragment")
public class SynchronousDataFragment extends BaseFragment implements
        IRefreshButtonAndText, OnClickListener {

    public static final int SUCCESS = 10;
    public static final int FAIL = 20;
    private static final int INSERT_OVER_CB = 30;
    private static final int INSERT_OVER_AJ = 50;
    private static final int INSERT_OVER_Dic = 40;
    private static final int INSERT_OVER_Dic_for_down = 1000;
    private static final int NOSCHEDINFO = 60;
    private static final String TAG = "SynchronousDataFragment";

    public String tag = "SynchronousDataFragment";
    Context mContext;
    private IApproveBackToList backToList;
    private int targetContainer;
    BaseActivity activity;
    private TextView tv_title_common_download, tv_title_upload_picture,
            tv_title_difference_download, tv_title_upload_schedinfo, cb_ptxz,
            cb_cbxz, cb_sc, aj_ptxz, aj_qzxz, aj_sc, tv_schedinfo, tv_des1,
            tv_des, tv_des2, tv_des3, tv_title_more;
    private Button btn_back;

    private String userName_login;
    private String deviceId;
    private String userID;
    private String meterReadRoute;
    private String meterReadCycle;
    private String scheduledSelectionDate;
    private String spMeterHistoryId;
    private String meterConfigurationId;
    private String cmMrDttm;
    private String cmMr;
    private String readType;
    private String cmMrRefVol;
    private String cmMrRefDebt;
    private String cmMrNotiPrtd;
    private String cmMrCommCd;
    private String cmMrRemark;
    private SharedPreferences sp;
    private ProgressHUD overlayProgress = null;
    // 存放已上传的用户
    private ArrayList<String> schedList = new ArrayList<String>();
    private ArrayList<ManufacturerList> manufacturerLists;
    private ArrayList<CmScShType_Dic> cmScShType_list;
    // j记录已上传的图片
    ArrayList<String> photoname_List = new ArrayList<String>();
    private long starttime;
    private boolean tagForDown = false;

    private Handler mHandle = new Handler() {
        public void handleMessage(Message msg) {

            if (overlayProgress != null) {
                overlayProgress.dismiss();
            }
            switch (msg.what) {

                case 30:
                    overlayProgress = (ProgressHUD) msg.obj;
                    if (overlayProgress != null) {
                        overlayProgress.dismiss();
                    }
                    Toast.makeText(getActivity(), "抄表 任务下载完成", Toast.LENGTH_LONG)
                            .show();
                    // tv_schedinfo.setText("下载结果:");
                    if (schedInfo_CB != 0) {
                        tv_des1.setText("抄表 : " + "共下载计划" + schedInfo_CB + "个、任务"
                                + renwu_CB + "条。");
                    } else {
                        tv_des1.setText("抄表 : 没有可下载任务。");
                    }
                    str = "";

                    String s = new String();

                    break;

                case 50:
                    overlayProgress = (ProgressHUD) msg.obj;
                    if (overlayProgress != null) {
                        overlayProgress.dismiss();
                    }

                    Toast.makeText(getActivity(), "安检 任务下载完成", Toast.LENGTH_LONG)
                            .show();
                    // 下载字典
                    // 下载字典,判断数据字典是否已经在字典模块下载过
                    tv_schedinfo.setText("下载结果:");
                    if (schedinfo_AJ != 0) {
                        tv_des2.setText("安检 : " + "共下载计划" + schedinfo_AJ + "个、任务"
                                + renwu_AJ + "条。");
                    } else {
                        tv_des2.setText("安检 : 没有可下载任务。");
                    }
                    break;
                case 40:
                    overlayProgress = (ProgressHUD) msg.obj;
                    if (overlayProgress != null) {
                        overlayProgress.dismiss();
                    }

                    Toast.makeText(mContext, "字典下载完成", Toast.LENGTH_LONG).show();
                    // 标记字典已经下载 ,在下载任务时 就不需下载
                    isDownLoad = true;
                    PerfUtils.putBoolean(mContext, "isDownLoad", isDownLoad);
                    tv_des.setText("字典 : 下载完成.");
                    break;
                case 1000:

                    if (tagForDown) {
                        new SynchronousDataTask_anjian().execute();
                    } else {
                        new SynchronousDataTask_anjian_pt().execute();
                    }


                    break;

                default:
                    break;
            }

        }

        ;
    };

    public SynchronousDataFragment(IApproveBackToList backToList,
                                   int targetContainer) {
        this.backToList = backToList;
        this.targetContainer = targetContainer;
    }

    public SynchronousDataFragment() {
    }

    private SinopecMenuModule menuModule;
    String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        activity = (BaseActivity) getActivity();
        mContext = getActivity();
        DatabaseHelper.encrypt(mContext);
        Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);

        btn_next.setText("字典下载");
        btn_next.setVisibility(View.GONE);
        btn_next.setPadding(3, 0, 3, 0);
        TextView tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
        tv_title.setText("同步数据");
        btn_next.setOnClickListener(this);
//        userName_login = PerfUtils.getString(mContext, "userName",
//                Constants.loginName);// 默认当前登录人
        userName_login = Constants.loginName;
        LogUtil.i(TAG, "登录用户:" + userName_login);
        str = "";

    }


    @Override
    public void onResume() {
        super.onResume();
//隐藏历史显示记录
        tv_schedinfo.setVisibility(View.GONE);
        tv_des.setVisibility(View.GONE);
        tv_des1.setVisibility(View.GONE);
        tv_des2.setVisibility(View.GONE);
        tv_des3.setVisibility(View.GONE);

    }


    private void initPathParams(SinopecMenuModule item) {
        for (SinopecMenuPage page : item.menuPages) {
            if ("DownTask".equalsIgnoreCase(page.code)) {
                path = WebUtils.rootUrl + URLUtils.baseContentUrl + page.id;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_synchdata_ui_new,
                container, false);
        tv_title_common_download = (TextView) view
                .findViewById(R.id.tv_title_common_download);

        // 获得抄表按钮
        cb_ptxz = (TextView) view.findViewById(R.id.cb_ptxz);
        cb_cbxz = (TextView) view.findViewById(R.id.cb_cbxz);
        cb_sc = (TextView) view.findViewById(R.id.cb_sc);
        aj_ptxz = (TextView) view.findViewById(R.id.aj_ptxz);
        aj_qzxz = (TextView) view.findViewById(R.id.aj_qzxz);
        aj_sc = (TextView) view.findViewById(R.id.aj_sc);

        tv_schedinfo = (TextView) view.findViewById(R.id.tv_schedinfo);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        tv_des1 = (TextView) view.findViewById(R.id.tv_des1);
        tv_des2 = (TextView) view.findViewById(R.id.tv_des2);
        tv_des3 = (TextView) view.findViewById(R.id.tv_des3);

        tv_schedinfo.setVisibility(View.GONE);
        tv_des.setVisibility(View.GONE);
        tv_des1.setVisibility(View.GONE);
        tv_des2.setVisibility(View.GONE);

        tv_title_difference_download = (TextView) view
                .findViewById(R.id.tv_title_difference_download);
        tv_title_upload_schedinfo = (TextView) view
                .findViewById(R.id.tv_title_upload_schedinfo);

        tv_title_upload_picture = (TextView) view
                .findViewById(R.id.tv_title_upload_picture);
        // 下载 上传
        tv_title_common_download.setOnClickListener(listener_putong);
        tv_title_difference_download.setOnClickListener(listener_cftong);
        tv_title_upload_schedinfo.setOnClickListener(listener_upload1);
        tv_title_upload_picture.setOnClickListener(listener_upload_picture);

        iv_pic_jiben = (ImageView) view.findViewById(R.id.iv_pic_jiben);
        iv_pic_more = (ImageView) view.findViewById(R.id.iv_pic_more);
        ll_moredes = (LinearLayout) view.findViewById(R.id.ll_moredes);
        tv_title_more = (TextView) view.findViewById(R.id.tv_title_more);
        // tv_title_more.setOnClickListener(listener_showMoreDes);
        iv_pic_more.setOnClickListener(listener_showMoreDes);

        // 抄表模块下载 上传
        // 差别下载cb_sc
        /*
         * cb_cbxz.setOnClickListener(listener_download_CB);
		 * cb_ptxz.setOnClickListener(listener_download_CB_PT);
		 * cb_sc.setOnClickListener(listener_upload_CB); // 安检模块 下载 上传
		 * aj_ptxz.setOnClickListener(listener_download_AJ_PT);
		 * aj_qzxz.setOnClickListener(listener_download_AJ);
		 * aj_sc.setOnClickListener(listener_upload_AJ);
		 */

        // 普通 下载
        // tv_title_common_download.setOnClickListener(new OnClickListener() {
        // 上传数据
        // tv_title_upload.setOnClickListener(new OnClickListener() {

        return view;
    }


    // 图片补传
    private OnClickListener listener_upload_picture = new OnClickListener() {
        @Override
        public void onClick(View v) {
            /*
             * starttime = System.currentTimeMillis(); ArrayList<PhotoAudio>
			 * allpicture = getAllpicture(userName_login); if (allpicture.size()
			 * > 0) { new UploadUtilsAsync(mActivity, allpicture).execute(); }
			 * else { Toast.makeText(mContext, "没有要上传的图片", 1).show(); }
			 */
            tv_des.setVisibility(View.GONE);
            tv_des1.setVisibility(View.GONE);
            tv_des2.setVisibility(View.GONE);
            tv_des3.setVisibility(View.GONE);
            tv_schedinfo.setText("正在进行的任务 :");
            tv_schedinfo.setVisibility(View.VISIBLE);
            // 安检图片补传'
            new AnJianUpdate(mContext).upload_Photo_Buchuan(tv_schedinfo,
                    tv_des3);
        }
    };

    boolean isOpen = false;
    // 显示更多信息
    private OnClickListener listener_showMoreDes = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewPropertyAnimator.animate(iv_pic_more).rotationBy(180)
                    .setDuration(350).start();
            if (!isOpen) {
                ll_moredes.setVisibility(View.VISIBLE);
                isOpen = !isOpen;
            } else {
                ll_moredes.setVisibility(View.GONE);
                isOpen = !isOpen;
            }
        }
    };
    // 普通下载
    private OnClickListener listener_putong = new OnClickListener() {
        @Override
        public void onClick(View v) {
            tv_schedinfo.setText("");
            tv_des.setVisibility(View.GONE);
            tv_des1.setVisibility(View.GONE);
            tv_des2.setVisibility(View.GONE);
            tv_des3.setVisibility(View.GONE);
            str = "";

            tagForDown = false;

            // 字典下载
            isDownLoad = PerfUtils.getBoolean(mContext, "isDownLoad", false);
            // 下载字典,判断数据字典是否已经在字典模块下载过
            // 抄表下载
            if (!isDownLoad) {
                tv_des.setText("字典 : 正在下载...");
                tv_des.setVisibility(View.VISIBLE);
                new SynchronousCM_H_DataDic().execute();
            }
            //抄表下载目前模块隐藏
//            new SynchronousDataTask().execute();

            // 安检下载
            if (isDownLoad) {
                new SynchronousDataTask_anjian_pt().execute();
            }


        }
    };
    // 重复下载
    private OnClickListener listener_cftong = new OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(getActivity())
                    .setCancelable(false)
                    .setTitle(R.string.alertTitle)
                    .setMessage(R.string.alertmessage)
                    .setIcon(R.drawable.jinggao)
                    .setPositiveButton("重复下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            anjian_download_again();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }
    };

    //重复下载
    private void anjian_download_again() {
        tv_schedinfo.setText("");
        tv_des.setVisibility(View.GONE);
        tv_des1.setVisibility(View.GONE);
        tv_des2.setVisibility(View.GONE);
        tv_des3.setVisibility(View.GONE);
        str = "";
        tagForDown = true;
        // 字典下载
        isDownLoad = PerfUtils.getBoolean(mContext, "isDownLoad", false);
        // 下载字典,判断数据字典是否已经在字典模块下载过
        if (!isDownLoad) {
            tv_des.setText("字典 : 正在下载...");
            tv_des.setVisibility(View.VISIBLE);
            new SynchronousCM_H_DataDic().execute();
            Log.e("----------->", "1");
        }
        // 抄表下载
        //抄表模块暂时隐藏
//        new SynchronousData_differenceTask().execute();

        // 安检下载
        if (isDownLoad) {
            new SynchronousDataTask_anjian().execute();
        }

    }

    // 上传
    private OnClickListener listener_upload = new OnClickListener() {
        @Override
        public void onClick(View v) {
            tv_schedinfo.setVisibility(View.GONE);
            tv_des.setVisibility(View.GONE);
            tv_des1.setVisibility(View.GONE);
            tv_des2.setVisibility(View.GONE);
            tv_des3.setVisibility(View.GONE);
            tv_schedinfo.setText("正在进行的任务 :");
            tv_schedinfo.setVisibility(View.VISIBLE);
            // 抄表
            // UploadDate uploadDate = new UploadDate(mContext);
            // ArrayList<SchedInfo> schedInfos = uploadDate
            // .selectSchedInfos(userName_login);
            // uploadDate.upload_CB(schedInfos, mContext, userName_login,
            // tv_des1);
            // // 安检上传
            // ArrayList<SchedInfoResidents> schedInfos_AJ = uploadDate
            // .selectSchedInfos_AJ(userName_login);
            // uploadDate.upData_AJ(mContext, userName, schedInfos_AJ,
            // tv_schedinfo, tv_des2);
        }
    };
    // 上传
    private OnClickListener listener_upload1 = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Bundle bundle = new Bundle();
            // bundle.putString("id", taskId);
            // bundle.putSerializable("custinfo", custInfo);
            // bundle.putSerializable("upcustInfo", uploadcustInfo);
            Fragment f = new ApplicationSchedInfoUpload(tv_schedinfo, tv_des,
                    tv_des1, tv_des2, tv_des3);
            f.setArguments(bundle);
            ((ActivityInTab) getActivity()).navigateTo(f);
        }
    };

    // 抄表差别下载
    private OnClickListener listener_download_CB = new OnClickListener() {
        @Override
        public void onClick(View v) {

            tv_schedinfo.setVisibility(View.GONE);
            tv_des.setVisibility(View.GONE);
            tv_des1.setVisibility(View.GONE);
            tv_des2.setVisibility(View.GONE);
            // 判断登录状态
            boolean boolean_Login = PerfUtils.getBoolean(mContext, "isOnline",
                    true);
            isDownLoad = PerfUtils.getBoolean(mContext, "isDownLoad", false);
            if (boolean_Login) {

                new SynchronousData_differenceTask().execute();

                // 下载字典,判断数据字典是否已经在字典模块下载过
                if (!isDownLoad) {
                    new SynchronousCM_H_DataDic().execute();
                    Log.e("----------->", "2");
                }

                DataCache.taskCount.put("10982", selectCBSize());
                // DataCache.taskCount.put("10201", selectAJSize());
                // DataCache.taskCount.put("10197", selectWXSize());
                List<String[]> list = new ArrayList<String[]>();
                String[] strings = new String[2];
                // String[] strings1 = new String[2];
                // String[] strings2 = new String[2];
                strings[0] = "10982";
                strings[1] = selectCBSize() + "";
                // strings1[0] = "10201";
                // strings1[1] = selectAJSize() + "";
                // strings2[0] = "10197";
                // strings2[1] = selectWXSize() + "";

                Intent intentNum = new Intent(Constants.MODIFY_APP_MENU_NUM);
                intentNum.putExtra("type", "updateTabNum");
                intentNum.putExtra("value", (Serializable) list);
                mActivity.sendBroadcast(intentNum);
                mActivity.sendBroadcast(new Intent(
                        Constants.MODIFY_APP_MENU_NUM));

            } else {
                Toast.makeText(mContext, "请在线登录后进行操作", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 抄表普通下载
    private OnClickListener listener_download_CB_PT = new OnClickListener() {

        @Override
        public void onClick(View v) {
            tv_schedinfo.setVisibility(View.GONE);
            tv_des.setVisibility(View.GONE);
            tv_des1.setVisibility(View.GONE);
            tv_des2.setVisibility(View.GONE);

            // 判断登录状态
            boolean boolean_Login = PerfUtils.getBoolean(mContext, "isOnline",
                    true);
            isDownLoad = PerfUtils.getBoolean(mContext, "isDownLoad", false);
            if (boolean_Login) {

                new SynchronousDataTask().execute();

                // 下载字典,判断数据字典是否已经在字典模块下载过
                if (!isDownLoad) {
                    new SynchronousCM_H_DataDic().execute();
                    Log.e("----------->", "3");
                }

                DataCache.taskCount.put("10982", selectCBSize());
                // DataCache.taskCount.put("10201", selectAJSize());
                // DataCache.taskCount.put("10197", selectWXSize());
                List<String[]> list = new ArrayList<String[]>();
                String[] strings = new String[2];
                // String[] strings1 = new String[2];
                // String[] strings2 = new String[2];
                strings[0] = "10982";
                strings[1] = selectCBSize() + "";
                // strings1[0] = "10201";
                // strings1[1] = selectAJSize() + "";
                // strings2[0] = "10197";
                // strings2[1] = selectWXSize() + "";

                Intent intentNum = new Intent(Constants.MODIFY_APP_MENU_NUM);
                intentNum.putExtra("type", "updateTabNum");
                intentNum.putExtra("value", (Serializable) list);
                mActivity.sendBroadcast(intentNum);
                mActivity.sendBroadcast(new Intent(
                        Constants.MODIFY_APP_MENU_NUM));

            } else {
                Toast.makeText(mContext, "请在线登录后进行操作", Toast.LENGTH_SHORT).show();
            }
        }
    };
    // 抄表上传
    private OnClickListener listener_upload_CB = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // 判断登录状态
            boolean boolean_Login = PerfUtils.getBoolean(mContext, "isOnline",
                    true);

            if (boolean_Login) {
                // 弹出选择上传任务列表

                PollingUpdateSchedInfosFragment pollingSchedInfosFragment = new PollingUpdateSchedInfosFragment();
                Bundle bundle = new Bundle();

                bundle.putString("user_name", userName_login);
                pollingSchedInfosFragment.setArguments(bundle);
                ((ActivityInTab) getActivity())
                        .navigateTo(pollingSchedInfosFragment);

            } else {
                Toast.makeText(mContext, "请在线登陆后进行操作", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 安检普通下载
    private OnClickListener listener_download_AJ_PT = new OnClickListener() {

        @Override
        public void onClick(View v) {
            tv_schedinfo.setVisibility(View.GONE);
            tv_des.setVisibility(View.GONE);
            tv_des1.setVisibility(View.GONE);
            tv_des2.setVisibility(View.GONE);

            boolean_Login = PerfUtils.getBoolean(mContext, "isOnline", true);
            if (boolean_Login) {
                new SynchronousDataTask_anjian_pt().execute();
                // 下载字典,判断数据字典是否已经在字典模块下载过
                isDownLoad = PerfUtils
                        .getBoolean(mContext, "isDownLoad", false);
                if (!isDownLoad) {
                    new SynchronousCM_H_DataDic().execute();
                    Log.e("----------->", "4");
                }
                DataCache.taskCount.put("10982", selectCBSize());
                // DataCache.taskCount.put("10201", selectAJSize());
                // DataCache.taskCount.put("10197", selectWXSize());
                List<String[]> list = new ArrayList<String[]>();
                String[] strings = new String[2];
                // String[] strings1 = new String[2];
                // String[] strings2 = new String[2];
                strings[0] = "10982";
                strings[1] = selectCBSize() + "";
                // strings1[0] = "10201";
                // strings1[1] = selectAJSize() + "";
                // strings2[0] = "10197";
                // strings2[1] = selectWXSize() + "";

                Intent intentNum = new Intent(Constants.MODIFY_APP_MENU_NUM);
                intentNum.putExtra("type", "updateTabNum");
                intentNum.putExtra("value", (Serializable) list);
                mActivity.sendBroadcast(intentNum);
                mActivity.sendBroadcast(new Intent(
                        Constants.MODIFY_APP_MENU_NUM));
            } else {
                Toast.makeText(mContext, "请在线登录后进行操作", Toast.LENGTH_SHORT).show();
            }
        }
    };
    // 安检强制下载
    private OnClickListener listener_download_AJ = new OnClickListener() {

        @Override
        public void onClick(View v) {
            tv_schedinfo.setVisibility(View.GONE);
            tv_des.setVisibility(View.GONE);
            tv_des1.setVisibility(View.GONE);
            tv_des2.setVisibility(View.GONE);

            boolean_Login = PerfUtils.getBoolean(mContext, "isOnline", true);
            if (boolean_Login) {
                new SynchronousDataTask_anjian().execute();

                // 下载字典,判断数据字典是否已经在字典模块下载过
                isDownLoad = PerfUtils
                        .getBoolean(mContext, "isDownLoad", false);
                if (!isDownLoad) {
                    new SynchronousCM_H_DataDic().execute();
                    Log.e("----------->", "5");
                }
                DataCache.taskCount.put("10982", selectCBSize());
                // DataCache.taskCount.put("10201", selectAJSize());
                // DataCache.taskCount.put("10197", selectWXSize());
                List<String[]> list = new ArrayList<String[]>();
                String[] strings = new String[2];
                // String[] strings1 = new String[2];
                // String[] strings2 = new String[2];
                strings[0] = "10982";
                strings[1] = selectCBSize() + "";
                // strings1[0] = "10201";
                // strings1[1] = selectAJSize() + "";
                // strings2[0] = "10197";
                // strings2[1] = selectWXSize() + "";

                Intent intentNum = new Intent(Constants.MODIFY_APP_MENU_NUM);
                intentNum.putExtra("type", "updateTabNum");
                intentNum.putExtra("value", (Serializable) list);
                mActivity.sendBroadcast(intentNum);
                mActivity.sendBroadcast(new Intent(
                        Constants.MODIFY_APP_MENU_NUM));

            } else {
                Toast.makeText(mContext, "请在线登录后进行操作", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 安检上传
    private OnClickListener listener_upload_AJ = new OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean_Login = PerfUtils.getBoolean(mContext, "isOnline", true);
            if (boolean_Login) {
                // 弹出选择上传任务列表
                AnJianUpdateSchedInfosFragment pollingSchedInfosFragment = new AnJianUpdateSchedInfosFragment();
                Bundle bundle = new Bundle();

                bundle.putString("user_name", userName_login);
                pollingSchedInfosFragment.setArguments(bundle);
                ((ActivityInTab) getActivity())
                        .navigateTo(pollingSchedInfosFragment);

            } else {

                Toast.makeText(mContext, "请在线登陆后进行操作", Toast.LENGTH_SHORT).show();
            }
        }
    };

    protected ArrayList<PhotoAudio> getAllpicture(String userName_login) {

        String sql = "select p.cmScFileName,p.cmScFileRoute,p.cmScdate from perFile_aj as p left join schedInfo_aj  as s where s.userID = '"
                + userName_login
                + "' and s.cmSchedId = p.cmSchedId and p.cmMrState = 'N'";
        // 存放上传的图片
        ArrayList<PhotoAudio> photos = new ArrayList<PhotoAudio>();
        try {
            Connection conne = SQLiteData.openOrCreateDatabase(getActivity());

            PreparedStatement ps = conne.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            PhotoAudio photoAudio = null;

            while (resultSet.next()) {
                photoAudio = new PhotoAudio();
                photoAudio.setCmScFileName(resultSet.getString("cmScFileName"));
                photoAudio.setCmScFileRoute(resultSet
                        .getString("cmScFileRoute"));
                photoAudio.setCmScdate(resultSet.getString("cmScdate"));
                photos.add(photoAudio);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photos;

    }

    // 下载 上传按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next: // 字典下载

                new SynchronousCM_H_DataDic().execute();

                break;

            default:
                break;
        }

    }

    public void InitFTPServerSetting() {
        ftpUtils = FTPUtils.getInstance();
        flag = ftpUtils.initFTPSetting("11.10.116.4", 21, "htest", "@htest");
    }


    // 图片上传
    public class UploadUtilsAsync extends AsyncTask<String, Integer, String> {

        private Context context;
        private String date;
        private String userid;
        private String FilePath;
        private String FileName;
        private ProgressDialog progressDialog;
        private ArrayList<PhotoAudio> photos;

        public UploadUtilsAsync(Context context, ArrayList<PhotoAudio> arraylist) {
            this.context = context;
            this.photos = arraylist;
        }

        @Override
        protected void onPreExecute() {// 执行前的初始化
            // TODO Auto-generated method stub
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("请稍等...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(true);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {// 执行进度
            // TODO Auto-generated method stub
            Log.i("info", "values:" + values[0]);
            progressDialog.setProgress((int) values[0]);// 更新进度条
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {// 执行结果
            // TODO Auto-generated method stub
            Log.i("info", result);
            long endTime = System.currentTimeMillis();
            long Time = (endTime - starttime) / 1000;
            System.out.println("上传图片耗时:" + Time);
            // Toast.makeText(mContext, "上传图片共:" + result + "张,耗时:" + Time, 1)
            // .show();
            progressDialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... params) {
            // 初始化和FTP服务器交互的类
            InitFTPServerSetting();
            String sdPath = android.os.Environment
                    .getExternalStorageDirectory().toString();
            if (flag) {// 登录成功
                if (photos.size() > 0) {
                    int a = 0;
                    for (int j = 0; j < photos.size(); j++) {
                        PhotoAudio audio = photos.get(j);
                        boolean uploadFile = ftpUtils.uploadFile(
                                audio.getCmScdate(), userName_login, sdPath
                                        + audio.getCmScFileRoute(),
                                audio.getCmScFileName());
                        // 上传成功后将其添加到集合,修改图片上传状态,并更新进度条
                        if (uploadFile) {
                            photoname_List.add(audio.getCmScFileName());
                        }
                        a++;
                        progressDialog.setProgress(100 / photos.size() * a);
                    }
                    // 关闭链接
                    ftpUtils.closeClient();
                }
            }
            // 更新数据库图片上传状态
            try {
                changeState_Photo(photoname_List);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return photos.size() + "";
        }
    }

    // 安检数据下载
    public class SynchronousDataTask_anjian extends
            AsyncTask<Void, Void, OutPut> {
        ProgressHUD overlayProgress = null;
        private long time_s;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            overlayProgress = AlertUtils.showDialog(getActivity(), null, null,
                    false);
            tv_schedinfo.setText("正在进行的任务:");
            tv_schedinfo.setVisibility(View.VISIBLE);
            // tv_des1.setText(str += "\n正在下载安检计划...");
            tv_des2.setText("安检 : 正在下载...");
            tv_des2.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(final OutPut result) {
            super.onPostExecute(result);
            // if (overlayProgress != null) {
            // overlayProgress.dismiss();
            // }
            if (result == null) {
                Toast.makeText(getActivity(), "安检数据下载失败，请重新下载", Toast.LENGTH_SHORT).show();
                if (overlayProgress != null) {
                    overlayProgress.dismiss();
                }
                return;
            }
            if (result != null && result.getSchedInfoResidents() != null) {

                if (!PerfUtils.getString(mContext, "dataDicVersion","").equals(result.getDataDicVersion())) {
                    PerfUtils.putString(mContext, "dataDicVersion", result.getDataDicVersion());
                }
                // 插入数据 ,耗时操作
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        System.out.println("安检开始插入数据库:" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(System.currentTimeMillis()));
                        insertAnJianData_A10(result);
                        System.out.println("安检插入数据完成:" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(System.currentTimeMillis()));

                        // 通过handler 关闭进度条,在所有数据出入后进行关闭
                        Message message = mHandle.obtainMessage();
                        message.what = INSERT_OVER_AJ;
                        message.obj = overlayProgress;
                        mHandle.sendMessage(message);
                    }
                }).start();
//				下载数据字典
//                String dicVersion = PerfUtils.getString(mContext, "dataDicVersion", "");
//                if (!dicVersion.equals(result.getDataDicVersion())) {
//                    tv_des.setText("字典 : 正在下载...");
//                    tv_des.setVisibility(View.VISIBLE);
//                    new SynchronousCM_H_DataDic().execute();
//                    Log.e("----------->","6");
//                    //			将数据字典标识保存在本地
//                    PerfUtils.putString(mContext, "dataDicVersion", result.getDataDicVersion());
//                }
            } else {
                if (overlayProgress != null) {
                    overlayProgress.dismiss();
                }
                // Toast.makeText(getActivity(), "安检计划没有可下载任务", 1).show();
                tv_schedinfo.setText("下载结果 :");
                tv_des2.setText("安检 : 没有可下载任务。");
                tv_des2.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected OutPut doInBackground(Void... params) {

            String key1 = "{\"root\":{\"list\":{\"CM_H_SCStDwn\":{\"@faultStyle\":\"wsdl\",\"input\":[{\"user\":\""
                    + userName_login
                    + "\",\"deviceId\":\"test\",\"operateType\":\"20\"}]}}}}";
            Log.e(" WebUtils.downloadurl_anjian", WebUtils.downloadurl_anjian);
            String string = DataCollectionUtils.SynchronousData(
                    WebUtils.downloadurl_anjian, key1);
            if (string.contains("HttpCookie失效")) {
                return null;
            }

            OutPut outPut = null;
            // 解析数据

            if (!TextUtils.isEmpty(string)) {
                Gson gson = new Gson();
                JSONObject json = null;
                String dataStr = null;
                // -- 基本信息f
                try {
                    json = new JSONObject(string);
                    String string1 = json.optString("soapenv:Envelope");
                    json = new JSONObject(string1);
                    String string2 = json.optString("soapenv:Body");
                    json = new JSONObject(string2);
                    String string3 = json.optString("CM_H_SCStDwn");
                    json = new JSONObject(string3);
                    // dataStr = json.optString("output");
                    // json = new JSONObject(dataStr);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                JSONArray jsonArrayOutPut = (JSONArray) json.opt("output"); // --
                if (jsonArrayOutPut != null && jsonArrayOutPut.length() > 0) {
                    List<OutPut> list = new ArrayList<OutPut>();

                    for (int y = 0; y < jsonArrayOutPut.length(); y++) {

                        try {
                            String soutput = jsonArrayOutPut.get(y).toString();

                            if (!"".equals(soutput.toString())
                                    && soutput != null)

                                outPut = gson.fromJson(soutput, OutPut.class);

                            // 电话无法解析 单独解析电话,
                            json = new JSONObject(soutput);
                            JSONArray jsonArray = (JSONArray) json
                                    .opt("schedInfoResidents"); // --
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String s = "";
                                    s = jsonArray.get(i).toString();
                                    json = new JSONObject(s);
                                    JSONArray jsonArrayCustInfo = (JSONArray) json
                                            .opt("custInfo"); // --
                                    if (jsonArrayCustInfo != null
                                            && jsonArrayCustInfo.length() > 0)
                                        for (int j = 0; j < jsonArrayCustInfo
                                                .length(); j++) {
                                            String s1 = "";
                                            s1 = jsonArrayCustInfo.get(j)
                                                    .toString();
                                            List<PerPhone_Anjian> listPerPhone = new ArrayList<PerPhone_Anjian>();
                                            json = new JSONObject(s1);
                                            JSONArray jsonArrayPerPhone = (JSONArray) json
                                                    .opt("perPhone"); // --
                                            if (jsonArrayPerPhone != null
                                                    && jsonArrayPerPhone
                                                    .length() > 0)
                                                for (int h = 0; h < jsonArrayPerPhone
                                                        .length(); h++) {
                                                    String s11 = "";
                                                    s11 = jsonArrayPerPhone
                                                            .get(h).toString();
                                                    PerPhone_Anjian perPhone = null;
                                                    if (!"".equals(s11
                                                            .toString())
                                                            && s11 != null)
                                                        perPhone = gson
                                                                .fromJson(
                                                                        s11.toString(),
                                                                        PerPhone_Anjian.class);
                                                    listPerPhone.add(perPhone);
                                                }

                                            // 获取对应任务下的客户并设置电话
                                            outPut.getSchedInfoResidents()
                                                    .get(i).getCustInfo()
                                                    .get(j)
                                                    .setPerPhones(listPerPhone);
                                        }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // System.out.println("==========数量======="
            // + outPut.getSchedInfoResidents().size());
            return outPut;
        }
    }

    // private boolean isDowDic = true;//当普通下载没有数据时 字典也不下载
    // 安检数据普通下载
    public class SynchronousDataTask_anjian_pt extends
            AsyncTask<Void, Void, OutPut> {
        ProgressHUD overlayProgress = null;
        private long time_s;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            overlayProgress = AlertUtils.showDialog(getActivity(), null, null,
                    false);

            // tv_des1.setText(str += "\n正在下载安检计划...");
            tv_schedinfo.setText("正在进行的任务:");
            tv_schedinfo.setVisibility(View.VISIBLE);
            tv_des2.setText("安检 : 正在下载...");
            tv_des2.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(final OutPut result) {
            super.onPostExecute(result);
            // if (overlayProgress != null) {
            // overlayProgress.dismiss();
            // }
            if (result != null && result.getSchedInfoResidents() != null) {
                if (!PerfUtils.getString(mContext, "dataDicVersion","").equals(result.getDataDicVersion())) {
                    PerfUtils.putString(mContext, "dataDicVersion", result.getDataDicVersion());
                }
                // 插入数据 ,耗时操作

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        insertAnJianData_A10(result);

                        // 通过handler 关闭进度条,在所有数据出入后进行关闭
                        Message message = mHandle.obtainMessage();
                        message.what = INSERT_OVER_AJ;
                        message.obj = overlayProgress;
                        mHandle.sendMessage(message);
                    }
                }).start();
//				下载数据字典
                String dicVersion = PerfUtils.getString(mContext, "dataDicVersion", "");
                if (!dicVersion.equals(result.getDataDicVersion())) {
                    tv_des.setText("字典 : 正在下载...");
                    tv_des.setVisibility(View.VISIBLE);
                    new SynchronousCM_H_DataDic().execute();
                    Log.e("----------->", "7");
                    //			将数据字典标识保存在本地
//                    PerfUtils.putString(mContext, "dataDicVersion", result.getDataDicVersion());
                }
            } else {
                if (overlayProgress != null) {
                    overlayProgress.dismiss();
                }
                tv_schedinfo.setText("下载结果 :");
                tv_des2.setText("安检 : 没有可下载任务。");
                tv_des2.setVisibility(View.VISIBLE);
                // Toast.makeText(getActivity(), "安检计划没有可下载任务", 1).show();
            }
        }

        @Override
        protected OutPut doInBackground(Void... params) {

            String key1 = "{\"root\":{\"list\":{\"CM_H_SCStDwn\":{\"@faultStyle\":\"wsdl\",\"input\":[{\"user\":\""
                    + userName_login
                    + "\",\"deviceId\":\"test\",\"operateType\":\"10\"}]}}}}";
            // String key1 =
            // "{\"root\":{\"list\":{\"CM_H_SCStDwn\":{\"@faultStyle\":\"wsdl\",\"input\":[{\"user\":\"110002\",\"deviceId\":\"test\",\"operateType\":\"20\"}]}}}}";
            String string = DataCollectionUtils.SynchronousData(
                    WebUtils.downloadurl_anjian, key1);
            OutPut outPut = null;
            // 解析数据
            if (!TextUtils.isEmpty(string)) {
                Gson gson = new Gson();
                JSONObject json = null;
                String dataStr = null;
                // -- 基本信息f
                try {
                    json = new JSONObject(string);
                    String string1 = json.optString("soapenv:Envelope");
                    json = new JSONObject(string1);
                    String string2 = json.optString("soapenv:Body");
                    json = new JSONObject(string2);
                    String string3 = json.optString("CM_H_SCStDwn");
                    json = new JSONObject(string3);
                    // dataStr = json.optString("output");
                    // json = new JSONObject(dataStr);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                JSONArray jsonArrayOutPut = (JSONArray) json.opt("output"); // --
                if (jsonArrayOutPut != null && jsonArrayOutPut.length() > 0) {
                    List<OutPut> list = new ArrayList<OutPut>();

                    for (int y = 0; y < jsonArrayOutPut.length(); y++) {

                        try {
                            String soutput = jsonArrayOutPut.get(y).toString();

                            if (!"".equals(soutput.toString())
                                    && soutput != null)

                                outPut = gson.fromJson(soutput, OutPut.class);

                            // 电话无法解析 单独解析电话,
                            json = new JSONObject(soutput);
                            JSONArray jsonArray = (JSONArray) json
                                    .opt("schedInfoResidents"); // --
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String s = "";
                                    s = jsonArray.get(i).toString();
                                    json = new JSONObject(s);
                                    JSONArray jsonArrayCustInfo = (JSONArray) json
                                            .opt("custInfo"); // --
                                    if (jsonArrayCustInfo != null
                                            && jsonArrayCustInfo.length() > 0)
                                        for (int j = 0; j < jsonArrayCustInfo
                                                .length(); j++) {
                                            String s1 = "";
                                            s1 = jsonArrayCustInfo.get(j)
                                                    .toString();
                                            List<PerPhone_Anjian> listPerPhone = new ArrayList<PerPhone_Anjian>();
                                            json = new JSONObject(s1);
                                            JSONArray jsonArrayPerPhone = (JSONArray) json
                                                    .opt("perPhone"); // --
                                            if (jsonArrayPerPhone != null
                                                    && jsonArrayPerPhone
                                                    .length() > 0)
                                                for (int h = 0; h < jsonArrayPerPhone
                                                        .length(); h++) {
                                                    String s11 = "";
                                                    s11 = jsonArrayPerPhone
                                                            .get(h).toString();
                                                    PerPhone_Anjian perPhone = null;
                                                    if (!"".equals(s11
                                                            .toString())
                                                            && s11 != null)
                                                        perPhone = gson
                                                                .fromJson(
                                                                        s11.toString(),
                                                                        PerPhone_Anjian.class);
                                                    listPerPhone.add(perPhone);
                                                }

                                            // 获取对应任务下的客户并设置电话
                                            outPut.getSchedInfoResidents()
                                                    .get(i).getCustInfo()
                                                    .get(j)
                                                    .setPerPhones(listPerPhone);
                                        }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return outPut;
        }
    }

    public SQLiteDatabase db;// 操作数据库的工具类
    private String userName;

    private boolean isDownLoad;
    private boolean boolean_Login;
    private FTPUtils ftpUtils;
    private boolean flag;
    private String str;
    private int renwu_CB;
    private int schedInfo_CB;
    private int schedinfo_AJ;
    private int renwu_AJ;
    private LinearLayout ll_moredes;
    private ImageView iv_pic_jiben, iv_pic_more;

    public int selectCBSize() {
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//        String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db";
//        String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/a10_db.db";
//        db = SQLiteDatabase.openOrCreateDatabase(path, null);
        String sqlstring = "select * from custInfo where cmMrState='1'";
        // 查询数据返回游标对象
        Cursor c1 = db.rawQuery(sqlstring, null);
        int i = c1.getCount();
        c1.close();
        return i;
    }

    public void changeState_Photo(ArrayList<String> photoname_List2)
            throws SQLException {

        Connection conne = SQLiteData.openOrCreateDatabase(getActivity());
        conne.setAutoCommit(false);
        String sqlString = "update  perFile_aj set cmMrState = 'Y' where cmScFileName = ?";
        PreparedStatement ps = conne.prepareStatement(sqlString);
        if (photoname_List2.size() > 0) {
            for (int a = 0; a < photoname_List2.size(); a++) {
                ps.setString(1, photoname_List2.get(a));
                ps.addBatch();
            }
        }
        ps.executeBatch();
        ps.close();
        conne.commit();
        conne.close();
    }


    private class SynchronousDataTask extends
            AsyncTask<Void, Void, List<OutPut>> {
        ProgressHUD overlayProgress = null;
        private long time_s;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            overlayProgress = AlertUtils.showDialog(getActivity(), null, null,
                    false);
            tv_schedinfo.setText("正在进行的任务:");
            tv_schedinfo.setVisibility(View.VISIBLE);
            // tv_des1.setText(str += "\n正在下载抄表计划...");
            tv_des1.setText("抄表 : 正在下载...");
            tv_des1.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(final List<OutPut> result) {
            super.onPostExecute(result);
            // if (overlayProgress != null) {
            // overlayProgress.dismiss();
            // }
            if (result != null && result.size() > 0
                    && result.get(0).getSchedInfos() != null) {
                // 插入数据 ,耗时操作

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // insertDowlaodData(result.get(0));
                        insertDowlaodData_A10(result.get(0));

                        // 通过handler 关闭进度条,在所有数据出入后进行关闭
                        Message message = mHandle.obtainMessage();
                        message.what = INSERT_OVER_CB;
                        message.obj = overlayProgress;
                        mHandle.sendMessage(message);
                    }
                }).start();
//                下载数据字典
                String dicVersion = PerfUtils.getString(mContext, "dataDicVersion", "");
                if (!dicVersion.equals(result.get(0).getDataDicVersion())) {
                    tv_des.setText("字典 : 正在下载...");
                    tv_des.setVisibility(View.VISIBLE);
                    new SynchronousCM_H_DataDic().execute();
                    Log.e("----------->", "8");
                    //			将数据字典标识保存在本地
//                    PerfUtils.putString(mContext, "dataDicVersion", result.get(0).getDataDicVersion());
                }
            } else {
                if (overlayProgress != null) {
                    overlayProgress.dismiss();
                }
                tv_des1.setText("抄表 : 没有可下载任务。");
                tv_des1.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "抄表计划没有可下载任务", Toast.LENGTH_LONG)
                        .show();
            }
        }

        @Override
        protected List<OutPut> doInBackground(Void... params) {
            List<OutPut> listOutPuts = new ArrayList<OutPut>();
            String key1 = "{\"root\":{\"list\":{\"CM_H_MRStDwn\":{\"@faultStyle\":\"wsdl\",\"input\":{\"user\":\""
                    + userName_login
                    + "\",\"deviceId\":\"TEST\",\"operateType\":\"10\"}}}}}";
            String string = DataCollectionUtils.SynchronousData(
                    WebUtils.downloadurl, key1);

            long time_end = System.currentTimeMillis();
            Log.i(tag, (time_end) / 1000 + ":===========下载结束");
            Log.i(tag, (time_s - time_end) / 1000 + ":===========下载耗时");

            // String string = "";
            // try {
            // InputStream inputStream = getActivity().getAssets().open(
            // "a.txt");
            // string = StreamUtils.retrieveContent(inputStream);
            // } catch (IOException e) {
            // e.printStackTrace();
            // }

            long time_jiexi = System.currentTimeMillis();
            Log.i(tag, (time_jiexi) / 1000 + ":===========解析");

            if (!TextUtils.isEmpty(string)) {
                Gson gson = new Gson();
                JSONObject json = null;
                String dataStr = null;
                // -- 基本信息f
                try {
                    json = new JSONObject(string);
                    String string1 = json.optString("soapenv:Envelope");
                    json = new JSONObject(string1);
                    String string2 = json.optString("soapenv:Body");
                    json = new JSONObject(string2);
                    String string3 = json.optString("CM_H_MRStDwn");
                    json = new JSONObject(string3);
                    // dataStr = json.optString("output");
                    // json = new JSONObject(dataStr);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                JSONArray jsonArrayOutPut = (JSONArray) json.opt("output"); // --
                if (jsonArrayOutPut != null && jsonArrayOutPut.length() > 0) {
                    for (int y = 0; y < jsonArrayOutPut.length(); y++) {
                        String soutput = "";
                        try {
                            soutput = jsonArrayOutPut.get(y).toString();
                            OutPut outPut = null;
                            if (!"".equals(soutput.toString())
                                    && soutput != null)
                                outPut = gson.fromJson(soutput.toString(),
                                        OutPut.class);
                            listOutPuts.add(outPut);
                            List<SchedInfo> list = new ArrayList<SchedInfo>();
                            json = new JSONObject(soutput);
                            JSONArray jsonArray = (JSONArray) json
                                    .opt("schedInfo"); // --
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String s = "";
                                    try {
                                        s = jsonArray.get(i).toString();
                                        SchedInfo schedInfo = null;
                                        if (!"".equals(s.toString())
                                                && s != null)
                                            schedInfo = gson.fromJson(
                                                    s.toString(),
                                                    SchedInfo.class);
                                        list.add(schedInfo);

                                        List<CustInfo> listCustInfo = new ArrayList<CustInfo>();
                                        json = new JSONObject(s);
                                        JSONArray jsonArrayCustInfo = (JSONArray) json
                                                .opt("custInfo"); // --
                                        if (jsonArrayCustInfo != null
                                                && jsonArrayCustInfo.length() > 0)
                                            for (int j = 0; j < jsonArrayCustInfo
                                                    .length(); j++) {
                                                String s1 = "";
                                                try {
                                                    s1 = jsonArrayCustInfo.get(
                                                            j).toString();
                                                    CustInfo custInfo = null;
                                                    if (!"".equals(s1
                                                            .toString())
                                                            && s1 != null)
                                                        custInfo = gson
                                                                .fromJson(
                                                                        s1.toString(),
                                                                        CustInfo.class);
                                                    custInfo.setCmMrState("1");// 设置默认值为1
                                                    listCustInfo.add(custInfo);

                                                    List<PerPhone> listPerPhone = new ArrayList<PerPhone>();
                                                    json = new JSONObject(s1);
                                                    JSONArray jsonArrayPerPhone = (JSONArray) json
                                                            .opt("perPhone"); // --
                                                    if (jsonArrayPerPhone != null
                                                            && jsonArrayPerPhone
                                                            .length() > 0)
                                                        for (int h = 0; h < jsonArrayPerPhone
                                                                .length(); h++) {
                                                            String s11 = "";
                                                            try {
                                                                s11 = jsonArrayPerPhone
                                                                        .get(h)
                                                                        .toString();
                                                                PerPhone perPhone = null;
                                                                if (!"".equals(s11
                                                                        .toString())
                                                                        && s11 != null)
                                                                    perPhone = gson
                                                                            .fromJson(
                                                                                    s11.toString(),
                                                                                    PerPhone.class);
                                                                listPerPhone
                                                                        .add(perPhone);
                                                            } catch (JSONException e) {
                                                                // TODO
                                                                // Auto-generated
                                                                // catch
                                                                // block
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    custInfo.setPerPhones(listPerPhone);
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch
                                                    // block
                                                    e.printStackTrace();
                                                }

                                            }
                                        schedInfo.setCustInfos(listCustInfo);
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    outPut.setSchedInfos(list);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            } else {
                Message message = mHandle.obtainMessage();
                message.what = FAIL;
                mHandle.sendMessage(message);
            }

            long time_jiexi_end = System.currentTimeMillis();
            Log.i(tag, (time_jiexi_end) / 1000 + ":===========解析over");
            Log.i(tag, (time_jiexi_end - time_jiexi) / 1000
                    + ":===========解析耗时");
            return listOutPuts;
        }
    }

    private class SynchronousData_differenceTask extends
            AsyncTask<Void, Void, List<OutPut>> {
        ProgressHUD overlayProgress = null;
        private long time_start;
        private long time_end;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_schedinfo.setText("正在进行的任务:");
            tv_schedinfo.setVisibility(View.VISIBLE);
            // tv_des1.setText(str += "\n正在下载抄表计划...");
            tv_des1.setText("抄表 : 正在下载...");
            tv_des1.setVisibility(View.VISIBLE);
            tv_des2.setText("");
            overlayProgress = AlertUtils.showDialog(getActivity(), null, null,
                    false);
            // 时间太长了，把加载进度条取消掉。。。。
        }

        @Override
        protected void onPostExecute(final List<OutPut> result) {
            super.onPostExecute(result);
            // if (overlayProgress != null) {
            // overlayProgress.dismiss();
            // }
            if (result == null) {
                Toast.makeText(getActivity(), "抄表任务下载失败，请重新下载", Toast.LENGTH_SHORT).show();
                if (overlayProgress != null) {
                    overlayProgress.dismiss();
                }
                return;
            }

            if (result != null && result.size() > 0
                    && result.get(0).getSchedInfos() != null) {
                // 插入数据 ,耗时操作
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        long charu_start = System.currentTimeMillis();
                        Log.i(tag, (charu_start) / 1000
                                + ":=================插入数据开始");

                        // 插入数据前先清空保存表
                        // insertDowlaodData(result.get(0));
                        insertDowlaodData_A10(result.get(0));

                        // 货获取下载的抄表计划任务和 任务数量

                        long charu_end = System.currentTimeMillis();
                        Log.i(tag, (charu_end) / 1000
                                + ":=================插入数据结束");
                        Log.i(tag, (charu_end - charu_start) / 1000
                                + ":=================插入数据耗时");

                        // 通过handler 关闭进度条,在所有数据出入后进行关闭

                        Message message = mHandle.obtainMessage();
                        message.what = INSERT_OVER_CB;
                        message.obj = overlayProgress;
                        mHandle.sendMessage(message);

                    }
                }).start();

                // tv_schedinfo.setText("抄表计划任务下载完成.");
                // tv_schedinfo.setVisibility(View.VISIBLE);

//				下载数据字典
                String dicVersion = PerfUtils.getString(mContext, "dataDicVersion", "");
                if (!dicVersion.equals(result.get(0).getDataDicVersion())) {
                    tv_des.setText("字典 : 正在下载...");
                    tv_des.setVisibility(View.VISIBLE);
                    new SynchronousCM_H_DataDic().execute();
                    Log.e("----------->", "9");
                    //			将数据字典标识保存在本地
//                    PerfUtils.putString(mContext, "dataDicVersion", result.get(0).getDataDicVersion());
                }

            } else {
                Toast.makeText(getActivity(), "抄表计划没有可下载任务", Toast.LENGTH_LONG).show();
                if (overlayProgress != null) {
                    overlayProgress.dismiss();
                }
                tv_des1.setText("抄表 : 没有可下载任务。");
                tv_des1.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected List<OutPut> doInBackground(Void... params) {
            time_start = System.currentTimeMillis();
            Log.i(tag, (time_start) / 1000 + ":===========下载开始");

            List<OutPut> listOutPuts = new ArrayList<OutPut>();
            String key1 = "{\"root\":{\"list\":{\"CM_H_MRStDwn\":{\"@faultStyle\":\"wsdl\",\"input\":{\"user\":\""
                    + userName_login
                    + "\",\"deviceId\":\"TEST\",\"operateType\":\"20\"}}}}}";
            String string = DataCollectionUtils.SynchronousData(
                    WebUtils.downloadurl, key1);

            if (string.contains("HttpCookie失效")) {
                return null;
            }
            long time_end = System.currentTimeMillis();
            Log.i(tag, (time_end) / 1000 + ":===========" + "" + "结束");
            Log.i(tag, (time_end - time_start) / 1000 + ":===========" + "耗时");

            //
            // String string = "";
            // try {
            // InputStream inputStream = getActivity().getAssets().open(
            // "a.txt");
            // string = StreamUtils.retrieveContent(inputStream);
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            long jiexi_start = System.currentTimeMillis();
            Log.i(tag, jiexi_start + ":===========解析开始");
            if (!TextUtils.isEmpty(string)) {
                Gson gson = new Gson();
                JSONObject json = null;
                String dataStr = null;
                // -- 基本信息f
                try {
                    json = new JSONObject(string);
                    String string1 = json.optString("soapenv:Envelope");
                    json = new JSONObject(string1);
                    String string2 = json.optString("soapenv:Body");
                    json = new JSONObject(string2);
                    String string3 = json.optString("CM_H_MRStDwn");
                    json = new JSONObject(string3);
                    // dataStr = json.optString("output");
                    // json = new JSONObject(dataStr);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                JSONArray jsonArrayOutPut = (JSONArray) json.opt("output"); // --
                // output 节点 按对象来解析
                if (jsonArrayOutPut != null && jsonArrayOutPut.length() > 0) {
                    for (int y = 0; y < jsonArrayOutPut.length(); y++) {
                        String soutput = "";
                        try {
                            soutput = jsonArrayOutPut.get(y).toString();
                            // soutput = json.optString("output");
                            OutPut outPut = null;
                            if (!"".equals(soutput.toString())
                                    && soutput != null)
                                outPut = gson.fromJson(soutput.toString(),
                                        OutPut.class);
                            listOutPuts.add(outPut);
                            List<SchedInfo> list = new ArrayList<SchedInfo>();
                            json = new JSONObject(soutput);

                            JSONArray jsonArray = (JSONArray) json
                                    .opt("schedInfo"); // --
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String s = "";
                                    try {
                                        s = jsonArray.get(i).toString();
                                        SchedInfo schedInfo = null;
                                        if (!"".equals(s.toString())
                                                && s != null)
                                            schedInfo = gson.fromJson(
                                                    s.toString(),
                                                    SchedInfo.class);
                                        list.add(schedInfo);

                                        List<CustInfo> listCustInfo = new ArrayList<CustInfo>();
                                        json = new JSONObject(s);
                                        JSONArray jsonArrayCustInfo = (JSONArray) json
                                                .opt("custInfo"); // --
                                        if (jsonArrayCustInfo != null
                                                && jsonArrayCustInfo.length() > 0)
                                            for (int j = 0; j < jsonArrayCustInfo
                                                    .length(); j++) {
                                                String s1 = "";
                                                try {
                                                    s1 = jsonArrayCustInfo.get(
                                                            j).toString();
                                                    CustInfo custInfo = null;
                                                    if (!"".equals(s1
                                                            .toString())
                                                            && s1 != null)
                                                        custInfo = gson
                                                                .fromJson(
                                                                        s1.toString(),
                                                                        CustInfo.class);
                                                    custInfo.setCmMrState("1");// 设置默认值为1
                                                    listCustInfo.add(custInfo);

                                                    List<PerPhone> listPerPhone = new ArrayList<PerPhone>();
                                                    json = new JSONObject(s1);
                                                    JSONArray jsonArrayPerPhone = (JSONArray) json
                                                            .opt("perPhone"); // --
                                                    if (jsonArrayPerPhone != null
                                                            && jsonArrayPerPhone
                                                            .length() > 0)
                                                        for (int h = 0; h < jsonArrayPerPhone
                                                                .length(); h++) {
                                                            String s11 = "";
                                                            try {
                                                                s11 = jsonArrayPerPhone
                                                                        .get(h)
                                                                        .toString();
                                                                PerPhone perPhone = null;
                                                                if (!"".equals(s11
                                                                        .toString())
                                                                        && s11 != null)
                                                                    perPhone = gson
                                                                            .fromJson(
                                                                                    s11.toString(),
                                                                                    PerPhone.class);
                                                                listPerPhone
                                                                        .add(perPhone);
                                                            } catch (JSONException e) {
                                                                // TODO
                                                                // Auto-generated
                                                                // catch
                                                                // block
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    custInfo.setPerPhones(listPerPhone);
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch
                                                    // block
                                                    e.printStackTrace();
                                                }

                                            }
                                        schedInfo.setCustInfos(listCustInfo);
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    outPut.setSchedInfos(list);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            } else {
                Message message = mHandle.obtainMessage();
                message.what = FAIL;
                mHandle.sendMessage(message);
            }
            long jiexi_end = System.currentTimeMillis();
            Log.i(tag, jiexi_end + ":===========解析结束");
            Log.i(tag, (jiexi_end - jiexi_start) / 1000 + ":===========解析耗时");

            return listOutPuts;

        }
    }

    // 下载字典数据
    private class SynchronousCM_H_DataDic extends AsyncTask<Void, Void, OutPut> {
        ProgressHUD overlayProgress = null;
        private ManufacturerList manufacturer;
        private CmScShType_Dic cmScShType;

        @Override
        protected void onPostExecute(final OutPut result) {
            super.onPostExecute(result);
            /*
             * if (overlayProgress != null) { overlayProgress.dismiss(); }
			 */

            // 将数据插入到字典数据库

            if (result == null) {
                Toast.makeText(getActivity(), "字典下载失败，请重新下载", Toast.LENGTH_SHORT).show();
                if (overlayProgress != null) {
                    overlayProgress.dismiss();
                }
                return;
            }

            if (result != null) {
//                result.getresult.get(0).getDataDicVersion()

                // 插入数据 ,耗时操作
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Log.e("======================>", "下载");
                        // 插入数据前先清空保存表
                        // insertDowlaodData(result.get(0));
                        dicDataInsterIntoDB_A10(result);
                        PerfUtils.putString(mContext, "dataDicVersion", result.getDataDicVersion());
                        // 通过handler 关闭进度条,在所有数据出入后进行关闭
                        Message message = mHandle.obtainMessage();
                        message.what = INSERT_OVER_Dic;
                        message.obj = overlayProgress;
                        mHandle.sendMessage(message);
                    }
                }).start();

            } else {
                Toast.makeText(mContext, "字典下载失败,请检查网络", Toast.LENGTH_SHORT).show();
                isDownLoad = false;
                PerfUtils.putBoolean(mContext, "isDownLoad", isDownLoad);
                if (overlayProgress != null) {
                    overlayProgress.dismiss();
                }
                tv_des.setText("字典 : 下载失败,请检查网络");
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            overlayProgress = AlertUtils.showDialog(getActivity(), null, null,
                    false);
            tv_schedinfo.setText("正在进行的任务:");
            tv_schedinfo.setVisibility(View.VISIBLE);
            tv_des.setText("字典 : 正在下载...");
            tv_des.setVisibility(View.VISIBLE);
        }

        @Override
        protected OutPut doInBackground(Void... params) {
            String key1 = "{\"root\":{\"list\":{\"CM_H_DataDic\":{\"@faultStyle\":\"wsd\",\"input\":{\"userId\":\""
                    + userName_login + "\",\"deviceId\":\"00000000\"}}}}}";
            String string = DataCollectionUtils.SynchronousData(
                    WebUtils.downloadDataDicUrl, key1);
            if (string.contains("HttpCookie失效")) {
                return null;
            }

            // InputStream inputStream;
            // String string = null;
            // try {
            // inputStream = getActivity().getAssets().open("zidian.txt");
            // string = retrieveContent(inputStream);
            // } catch (IOException e1) {
            // // TODO Auto-generated catch block
            // e1.printStackTrace();
            // }

            // 创建集合 存放所有数据
            ArrayList<List<Object>> arrayList = new ArrayList<List<Object>>();
            // 返回的对象
            OutPut outPut = null;
            // 解析

            if (!TextUtils.isEmpty(string)) {
                Gson gson = new Gson();
                JSONObject json = null;
                String dataStr = null;
                // -- 基本信息f
                try {
                    json = new JSONObject(string);
                    String string1 = json.optString("soapenv:Envelope");
                    json = new JSONObject(string1);
                    String string2 = json.optString("soapenv:Body");
                    json = new JSONObject(string2);
                    String string3 = json.optString("CM_H_DataDic");
                    json = new JSONObject(string3);
                    // dataStr = json.optString("output");
                    // json = new JSONObject(dataStr);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                JSONArray jsonArrayOutPut = (JSONArray) json.opt("output"); // --
                if (jsonArrayOutPut != null && jsonArrayOutPut.length() > 0) {
                    List<OutPut> list = new ArrayList<OutPut>();
                    for (int y = 0; y < jsonArrayOutPut.length(); y++) {

                        try {
                            String soutput = jsonArrayOutPut.get(y).toString();

                            if (!"".equals(soutput.toString())
                                    && soutput != null)

                                outPut = gson.fromJson(soutput, OutPut.class);

                            // 单独解析
                            json = new JSONObject(soutput);
                            JSONArray jsonArray = (JSONArray) json
                                    .opt("manufacturerInfo"); // --
                            if (jsonArray != null && jsonArray.length() > 0) {

                                manufacturerLists = new ArrayList<ManufacturerList>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String s = jsonArray.get(i).toString();
                                    JSONObject json1 = new JSONObject(s);
                                    JSONArray jsonArrayManufacturerList = (JSONArray) json1
                                            .opt("manufacturerList");
                                    if (jsonArrayManufacturerList != null
                                            && jsonArrayManufacturerList
                                            .length() > 0)
                                        for (int h = 0; h < jsonArrayManufacturerList
                                                .length(); h++) {

                                            String s11 = jsonArrayManufacturerList
                                                    .get(h).toString();

                                            // 第 15 个字节点应该是集合(但现在是个对象),里面只有一个对象
                                            // if(h==15){
                                            // continue;
                                            // }

                                            if (!"".equals(s11.toString())
                                                    && s11 != null) {
                                                manufacturer = gson.fromJson(
                                                        s11.toString(),
                                                        ManufacturerList.class);
                                            }

                                            manufacturerLists.add(manufacturer);
                                        }
                                }
                            }
                            JSONArray jsonArray_cmScShTypeInfo = (JSONArray) json
                                    .opt("cmScShTypeInfo"); // --

                            if (jsonArray_cmScShTypeInfo != null
                                    && jsonArray_cmScShTypeInfo.length() > 0) {

                                cmScShType_list = new ArrayList<CmScShType_Dic>();

                                for (int i = 0; i < jsonArray_cmScShTypeInfo
                                        .length(); i++) {
                                    String s = jsonArray_cmScShTypeInfo.get(i)
                                            .toString();

                                    JSONObject json1 = new JSONObject(s);
                                    JSONArray jsonArraycmScShType = (JSONArray) json1
                                            .opt("cmScShType");

                                    if (jsonArraycmScShType != null
                                            && jsonArraycmScShType.length() > 0)
                                        for (int h = 0; h < jsonArraycmScShType
                                                .length(); h++) {

                                            String s11 = jsonArraycmScShType
                                                    .get(h).toString();

                                            if (!"".equals(s11.toString())
                                                    && s11 != null) {
                                                cmScShType = gson.fromJson(
                                                        s11.toString(),
                                                        CmScShType_Dic.class);
                                            }
                                            cmScShType_list.add(cmScShType);
                                        }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("字典解析异常");
                        }
                    }
                }
            }

            return outPut;
        }
    }

    // 插入抄表数据
    private void insertDowlaodData_A10(OutPut outPut) {
        Connection conne = null;
        Statement state = null;
        try {


            conne = SQLiteData.openOrCreateDatabase(getActivity());
            state = conne.createStatement();
            // ----------------------
            // 根据条件查询任务表
            // 是否有记录。有 更新记录，然后获取用户信息的accoundid,是否存在，更新或插入
            // 没有 直接插入数据库
            String sql_task;
            conne.setAutoCommit(false);// 取消自动提交
            if (outPut.getSchedInfos() != null) {
                schedInfo_CB = outPut.getSchedInfos().size();// 记录任务数量
                renwu_CB = 0;
                for (SchedInfo schedInfo : outPut.getSchedInfos()) {
                    int _id = 0;
                    sql_task = "select rowid,userID from schedInfo where meterReadRoute="
                            + "'"
                            + schedInfo.getMeterReadRoute()
                            + "'"
                            + " and meterReadCycle="
                            + "'"
                            + schedInfo.getMeterReadCycle()
                            + "'"
                            + " and scheduledSelectionDate="
                            + "'"
                            + schedInfo.getScheduledSelectionDate() + "'" + "";
                    ResultSet rawQuery_task = state.executeQuery(sql_task);

                    String userId = null;
                    // 该条计划任务的主键，如果该任务要更新，需要加上 主键 为条件
                    while (rawQuery_task.next()) {
                        _id = rawQuery_task.getInt(1);
                        userId = rawQuery_task.getString("userID");
                    }

                    // 清空任务表中的该条任务
                    String sql_deleteTask = "delete  from custInfo where schedInfoID="
                            + "'" + _id + "'" + "";
                    state.executeUpdate(sql_deleteTask);
                    if (userId == null) {
                        // // 没有 直接插入数据库
                        // 循环任务list

                        String sql = "insert into schedInfo (userID,meterReadRoute,cmMrRteDescr,"
                                + "meterReadCycle,cmMrCycDescr,scheduledSelectionDate,scheduledReadDate) "
                                + "values " + "(" + "'"
                                + userName_login
                                + "'"
                                + ","
                                + "'"
                                + schedInfo.getMeterReadRoute()
                                + "'"
                                + ","
                                + "'"
                                + schedInfo.getCmMrRteDescr()
                                + "'"
                                + ","
                                + "'"
                                + schedInfo.getMeterReadCycle()
                                + "'"
                                + ","
                                + "'"
                                + schedInfo.getCmMrCycDescr()
                                + "'"
                                + ","
                                + "'"
                                + schedInfo.getScheduledSelectionDate()
                                + "'"
                                + ","
                                + "'"
                                + schedInfo.getScheduledReadDate()
                                + "'" + ")";
                        // 插入数据返回游标对象,生成主键
                        state.executeUpdate(sql);

                        // 获取到最新插入的rowid（也就是主键）,作为custInfo表的主键
                        // state.executeUpdate(sql,
                        // Statement.RETURN_GENERATED_KEYS);
                        // ResultSet c = state.getGeneratedKeys();

                        String sql_selectMax = "select  max(rowid) from  schedInfo";
                        ResultSet c = state.executeQuery(sql_selectMax);
                        int id = 0;
                        while (c.next()) {
                            id = c.getInt(1);
                        }
                        renwu_CB += schedInfo.getCustInfos().size();
                        for (CustInfo custInfo : schedInfo.getCustInfos()) {

                            // 用户
                            String sql_insertCustInfo = "insert into custInfo (schedInfoID,meterReadCycleRouteSequence,accountId,entityName,customerClass,cmCustClDescr,cmMrAddress,cmMrDistrict,cmMrStreet,cmMrCommunity,"
                                    + "cmMrBuilding,cmMrUnit,cmMrRoomNum,spMeterHistoryId,meterConfigurationId,cmMrMtrBarCode,"
                                    + "fullScale,cmMrAvgVol,rateSchedule,cmRsDescr,cmMrLastBal,cmMrOverdueAmt,cmMrDebtStatDt,cmMrLastMrDttm,readType,cmMrLastMr,cmMrLastVol,"
                                    + "cmMrLastDebt,cmMrLastSecchkDt,cmMrRemark,cmMrState,cmMrDate) values "
                                    + "(" + "'"
                                    + id
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getMeterReadCycleRouteSequence()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getAccountId()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getEntityName()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCustomerClass()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmCustClDescr()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrAddress()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrDistrict()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrStreet()
                                    + "'"
                                    + ","

                                    + "'"
                                    + custInfo.getCmMrCommunity()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrBuilding()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrUnit()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrRoomNum()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getSpMeterHistoryId()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getMeterConfigurationId()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrMtrBarCode()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getFullScale()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrAvgVol()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getRateSchedule()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmRsDescr()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrLastBal()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrOverdueAmt()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrDebtStatDt()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrLastMrDttm()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getReadType()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrLastMr()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrLastVol()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrLastDebt()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrLastSecchkDt()
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrRemark()
                                    + "'"
                                    + ","
                                    + "'"
                                    + 1
                                    + "'"
                                    + ","
                                    + "'"
                                    + custInfo.getCmMrDate() + "'" + ")";

                            state.executeUpdate(sql_insertCustInfo);

                            // 在数据库中插入电话数据
                            for (PerPhone perPhone : custInfo.getPerPhones()) {

                                // 首先判断电话表中该用户是否已经存在,如果已经存在 同一个用户电话表中对应一次记录
                                String sql_selectPhone = "select * from perPhone where accountId = "
                                        + "'"
                                        + custInfo.getAccountId()
                                        + "'"
                                        + "";
                                ResultSet cursor_Phone = state
                                        .executeQuery(sql_selectPhone);

                                if (!cursor_Phone.next()) {

                                    String spl_insertPerPhone = "insert into perPhone(accountId,sequence,phoneType,phone,extension,cmPhoneOprtType)"
                                            + "values(" + "'"
                                            + custInfo.getAccountId()
                                            + "'"
                                            + ","
                                            + "'"
                                            + perPhone.getSequence()
                                            + "'"
                                            + ","
                                            + "'"
                                            + perPhone.getPhoneType()
                                            + "'"
                                            + ","
                                            + "'"
                                            + perPhone.getPhone()
                                            + "'"
                                            + ","
                                            + "'"
                                            + ("" + perPhone.getExtension())
                                            + "'"
                                            + ","
                                            + "'"
                                            + perPhone.getCmPhoneOprtType()
                                            + "'" + ")";
                                    state.executeUpdate(spl_insertPerPhone);
                                }
                            }
                        }

                    } else {
                        // 有数据 更新记录，需获取用户信息的accoundid,是否存在，更新或插入
                        // 更新任务表

                        String sql_updata = "update schedInfo set userID="
                                + "'"
                                + userId
                                + "'"
                                + ",meterReadRoute="
                                + "'"
                                + schedInfo.getMeterReadRoute()
                                + "'"
                                + ",cmMrRteDescr="
                                + "'"
                                + schedInfo.getCmMrRteDescr()
                                + "'"
                                + ",meterReadCycle="
                                + "'"
                                + schedInfo.getMeterReadCycle()
                                + "'"
                                + ",cmMrCycDescr="
                                + "'"
                                + schedInfo.getCmMrCycDescr()
                                + "'"
                                + ",scheduledSelectionDate="
                                + "'"
                                + schedInfo.getScheduledSelectionDate()
                                + "'"
                                + ",scheduledReadDate="
                                + "'"
                                + schedInfo.getScheduledReadDate()
                                + "'"
                                + "  where userID="
                                + "'"
                                + userId
                                + "'"
                                + " and meterReadRoute="
                                + "'"
                                + schedInfo.getMeterReadRoute()
                                + "'"
                                + " and meterReadCycle="
                                + "'"
                                + schedInfo.getMeterReadCycle()
                                + "'"
                                + " and scheduledSelectionDate="
                                + "'"
                                + schedInfo.getScheduledSelectionDate()
                                + "'"
                                + "";
                        state.executeUpdate(sql_updata);

                        // 获取到该操作员的rowid（也就是主键）,如有新增用户作为custInfo表的主键
                        String sql_selectMax = "select  rowid from  schedInfo where userID= "
                                + "'"
                                + userId
                                + "'"
                                + "and meterReadRoute="
                                + "'"
                                + schedInfo.getMeterReadRoute()
                                + "'"
                                + " and meterReadCycle="
                                + "'"
                                + schedInfo.getMeterReadCycle()
                                + "'"
                                + " and scheduledSelectionDate="
                                + "'"
                                + schedInfo.getScheduledSelectionDate()
                                + "'"
                                + "";
                        PreparedStatement pstmt = (PreparedStatement) conne
                                .prepareStatement(sql_selectMax);
                        ResultSet c = pstmt.executeQuery();
                        int add_id = 0;
                        while (c.next()) {
                            add_id = c.getInt(1);
                        }
                        renwu_CB += schedInfo.getCustInfos().size();
                        // 插入或跟新用户信息
                        for (CustInfo custInfo : schedInfo.getCustInfos()) {
                            // 删除该用户相关的信息(信息表,上传表,电话表)

                            // // 清空任务表中的该条任务
                            // String sql_deleteCust =
                            // "delete  from custInfo where schedInfoID="
                            // + "'" + add_id + "'" + "";
                            // state.executeUpdate(sql_deleteCust);
                            String sql_deleteCustUP = "delete  from uploadcustInfo where spMeterHistoryId="
                                    + "'"
                                    + custInfo.getSpMeterHistoryId()
                                    + "'  " + "";
                            state.executeUpdate(sql_deleteCustUP);

                            String sql_deleteCustPhone = "delete  from perPhone where accountId="
                                    + "'"
                                    + custInfo.getAccountId()
                                    + "'  "
                                    + "";
                            state.executeUpdate(sql_deleteCustPhone);

                            // 根据accoundid查询该用户是否存在，存在 跟新 否则 插入
                            String sql_selectAccount = "select accountId from custInfo where schedInfoID = "
                                    + "'"
                                    + add_id
                                    + "'"
                                    + " and spMeterHistoryId="
                                    + "'"
                                    + custInfo.getSpMeterHistoryId() + "'" + "";

                            ResultSet rawQuery_Account = state
                                    .executeQuery(sql_selectAccount);

                            String accountId = null;
                            while (rawQuery_Account.next()) {
                                accountId = rawQuery_Account
                                        .getString("accountId");
                            }

                            if (TextUtils.isEmpty(accountId)) {
                                // 新增任务用户信息， 插入表中（包括电话信息）
                                // 用户
                                String sql_insertCustInfo = "insert into custInfo (schedInfoID,meterReadCycleRouteSequence,accountId,entityName,customerClass,cmCustClDescr,cmMrAddress,cmMrDistrict,cmMrStreet,cmMrCommunity,"
                                        + "cmMrBuilding,cmMrUnit,cmMrRoomNum,spMeterHistoryId,meterConfigurationId,cmMrMtrBarCode,"
                                        + "fullScale,cmMrAvgVol,rateSchedule,cmRsDescr,cmMrLastBal,cmMrOverdueAmt,cmMrDebtStatDt,cmMrLastMrDttm,readType,cmMrLastMr,cmMrLastVol,"
                                        + "cmMrLastDebt,cmMrLastSecchkDt,cmMrRemark,cmMrState,cmMrDate) values "
                                        + "(" + "'"
                                        + add_id
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo
                                        .getMeterReadCycleRouteSequence()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getAccountId()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getEntityName()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCustomerClass()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmCustClDescr()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrAddress()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrDistrict()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrStreet()
                                        + "'"
                                        + ","

                                        + "'"
                                        + custInfo.getCmMrCommunity()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrBuilding()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrUnit()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrRoomNum()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getSpMeterHistoryId()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getMeterConfigurationId()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrMtrBarCode()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getFullScale()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrAvgVol()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getRateSchedule()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmRsDescr()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrLastBal()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrOverdueAmt()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrDebtStatDt()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrLastMrDttm()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getReadType()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrLastMr()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrLastVol()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrLastDebt()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrLastSecchkDt()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrRemark()
                                        + "'"
                                        + ","
                                        + "'"
                                        + 1
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getCmMrDate() + "'" + ")";
                                state.execute(sql_insertCustInfo);
                                for (PerPhone perPhone : custInfo
                                        .getPerPhones()) {

                                    String spl_insertPerPhone = "insert into perPhone(accountId,sequence,phoneType,phone,extension,cmPhoneOprtType)"
                                            + "values(" + "'"
                                            + custInfo.getAccountId()
                                            + "'"
                                            + ","
                                            + "'"
                                            + perPhone.getSequence()
                                            + "'"
                                            + ","
                                            + "'"
                                            + perPhone.getPhoneType()
                                            + "'"
                                            + ","
                                            + "'"
                                            + perPhone.getPhone()
                                            + "'"
                                            + ","
                                            + "'"
                                            + ("" + perPhone.getExtension())
                                            + "'"
                                            + ","
                                            + "'"
                                            + perPhone.getCmPhoneOprtType()
                                            + "'" + ")";
                                    // db.execSQL(spl_insertPerPhone);
                                    state.execute(spl_insertPerPhone);
                                }

                            } else {
                                // 原有任务用户信息，更新用户信息及电话信息，根据查询到的accountid

                                String sql_updateCustInfo = "update custInfo set schedInfoID="
                                        + "'"
                                        + add_id
                                        + "'"
                                        + ",meterReadCycleRouteSequence="
                                        + "'"
                                        + custInfo
                                        .getMeterReadCycleRouteSequence()
                                        + "'"
                                        + ",entityName="
                                        + "'"
                                        + custInfo.getEntityName()
                                        + "'"
                                        + ",customerClass="
                                        + "'"
                                        + custInfo.getCustomerClass()
                                        + "'"
                                        + ",cmCustClDescr="
                                        + "'"
                                        + custInfo.getCmCustClDescr()
                                        + "'"
                                        + ",cmMrAddress="
                                        + "'"
                                        + custInfo.getCmMrAddress()
                                        + "'"
                                        + ",cmMrDistrict="
                                        + "'"
                                        + custInfo.getCmMrDistrict()
                                        + "'"
                                        + ",cmMrStreet="
                                        + "'"
                                        + custInfo.getCmMrStreet()
                                        + "'"
                                        + ",cmMrCommunity="
                                        + "'"
                                        + custInfo.getCmMrCommunity()
                                        + "'"
                                        + ","
                                        + "cmMrBuilding="
                                        + "'"
                                        + custInfo.getCmMrBuilding()
                                        + "'"
                                        + ",cmMrUnit="
                                        + "'"
                                        + custInfo.getCmMrUnit()
                                        + "'"
                                        + ",cmMrRoomNum="
                                        + "'"
                                        + custInfo.getCmMrRoomNum()
                                        + "'"
                                        + ",spMeterHistoryId="
                                        + "'"
                                        + custInfo.getSpMeterHistoryId()
                                        + "'"
                                        + ",meterConfigurationId="
                                        + "'"
                                        + custInfo.getMeterConfigurationId()
                                        + "'"
                                        + ",cmMrMtrBarCode="
                                        + "'"
                                        + custInfo.getCmMrMtrBarCode()
                                        + "'"
                                        + ","
                                        + "fullScale="
                                        + "'"
                                        + custInfo.getFullScale()
                                        + "'"
                                        + ",cmMrAvgVol="
                                        + "'"
                                        + custInfo.getCmMrAvgVol()
                                        + "'"
                                        + ",rateSchedule="
                                        + "'"
                                        + custInfo.getRateSchedule()
                                        + "'"
                                        + ",cmRsDescr="
                                        + "'"
                                        + custInfo.getCmRsDescr()
                                        + "'"
                                        + ",cmMrLastBal="
                                        + "'"
                                        + custInfo.getCmMrLastBal()
                                        + "'"
                                        + ",cmMrOverdueAmt="
                                        + "'"
                                        + custInfo.getCmMrOverdueAmt()
                                        + "'"
                                        + ",cmMrDebtStatDt="
                                        + "'"
                                        + custInfo.getCmMrDebtStatDt()
                                        + "'"
                                        + ",cmMrLastMrDttm="
                                        + "'"
                                        + custInfo.getCmMrLastMrDttm()
                                        + "'"
                                        + ",readType="
                                        + "'"
                                        + custInfo.getReadType()
                                        + "'"
                                        + ",cmMrLastMr="
                                        + "'"
                                        + custInfo.getCmMrLastMr()
                                        + "'"
                                        + ",cmMrLastVol="
                                        + "'"
                                        + custInfo.getCmMrLastVol()
                                        + "'"
                                        + ","
                                        + "cmMrLastDebt="
                                        + "'"
                                        + custInfo.getCmMrLastDebt()
                                        + "'"
                                        + ",cmMrLastSecchkDt="
                                        + "'"
                                        + custInfo.getCmMrLastSecchkDt()
                                        + "'"
                                        + ",cmMrRemark="
                                        + "'"
                                        + custInfo.getCmMrRemark()
                                        + "'"
                                        + ",cmMrState="
                                        + "'"
                                        + 1
                                        + "'"
                                        + ",cmMrDate="
                                        + "'"
                                        + custInfo.getCmMrDate()
                                        + "'"
                                        + " where accountId="
                                        + "'"
                                        + accountId
                                        + "'"
                                        + " and schedInfoID = "
                                        + "'"
                                        + add_id + "'" + " ";
                                state.execute(sql_updateCustInfo);

                                // 更新或插入电话
                                for (PerPhone perPhone : custInfo
                                        .getPerPhones()) {

                                    // 查询更新的该用户是否电话，有 更新电话 否 则 插入电话
                                    String sql_selectPhone = "select * from perPhone where accountId = "
                                            + "'" + accountId + "'" + "";

                                    ResultSet cursor_Phone = state
                                            .executeQuery(sql_selectPhone);
                                    int count = 0;
                                    if (!cursor_Phone.next()) {
                                        // 该用户之前没有电话
                                        String spl_insertPerPhone = "insert into perPhone(accountId,sequence,phoneType,phone,extension,cmPhoneOprtType)"
                                                + "values(" + "'"
                                                + custInfo.getAccountId()
                                                + "'"
                                                + ","
                                                + "'"
                                                + perPhone.getSequence()
                                                + "'"
                                                + ","
                                                + "'"
                                                + perPhone.getPhoneType()
                                                + "'"
                                                + ","
                                                + "'"
                                                + perPhone.getPhone()
                                                + "'"
                                                + ","
                                                + "'"
                                                + ("" + perPhone.getExtension())
                                                + "'"
                                                + ","
                                                + "'"
                                                + perPhone.getCmPhoneOprtType()
                                                + "'" + ")";
                                        // db.execSQL(spl_insertPerPhone);
                                        state.executeUpdate(spl_insertPerPhone);

                                    } else {
                                        // 有电话 进行更新,根据用户accountId

                                        String spl_updatePerPhone = "update  perPhone  set sequence="
                                                + "'"
                                                + perPhone.getSequence()
                                                + "'"
                                                + ",phoneType="
                                                + "'"
                                                + perPhone.getPhoneType()
                                                + "'"
                                                + ",phone="
                                                + "'"
                                                + perPhone.getPhone()
                                                + "'"
                                                + ",extension="
                                                + "'"
                                                + ("" + perPhone.getExtension())
                                                + "'"
                                                + ",cmPhoneOprtType="
                                                + "'"
                                                + perPhone.getCmPhoneOprtType()
                                                + "'"
                                                + "  where accountId ="
                                                + "'" + accountId + "'" + "";
                                        // db.execSQL(spl_updatePerPhone);
                                        state.executeUpdate(spl_updatePerPhone);
                                    }

                                }

                            }

                        }

                    }

                }

                conne.commit();// 提交

            }

        } catch (SQLException e) {
            // 插入数据异常
            e.printStackTrace();
        } finally {
            // 关闭连接
            if (state != null)
                try {
                    state.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (conne != null)
                try {
                    conne.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }

    // 插入安检数据
    private void insertAnJianData_A10(OutPut outPut) {
//        Connection conne = null;
//        Statement state = null;
//        PreparedStatement prepared = null;
        DatabaseHelper db= null;
        SQLiteDatabase writableDatabase= null;

        try {


            db = new DatabaseHelper(mContext);

            writableDatabase = db.getWritableDatabase();
//            conne = SQLiteData.openOrCreateDatabase(getActivity());
//            state = conne.createStatement();

            String sql_task;
//            conne.setAutoCommit(false);// 取消自动提交
            if (outPut.getSchedInfoResidents() != null) {
                schedinfo_AJ = outPut.getSchedInfoResidents().size();
                renwu_AJ = 0;
                writableDatabase.beginTransaction();
                for (SchedInfoResidents schedInfoResidents : outPut
                        .getSchedInfoResidents()) {
                    // 根据本次计划任务id 将表中的 符合本次任务id 的数据删除,重新插入数据,如 客户信息表,计划任务表,上传表
                    // 隐患表 等等

                    // 清空计划任务表中的该条任务
                    String sql_deleteTask = "delete  from schedInfo_aj where cmSchedId="
                            + "'"
                            + schedInfoResidents.getCmSchedId()
                            + "'"
                            + " and description="
                            + "'"
                            + schedInfoResidents.getDescription()
                            + "'"
                            + " and cmScTypeCd="
                            + "'"
                            + schedInfoResidents.getCmScTypeCd() + "'" + "";
//                    prepared = conne.prepareStatement(sql_deleteTask);
//                    prepared.executeUpdate();
//                    prepared.close();
                    writableDatabase.execSQL(sql_deleteTask);

//                    state.executeUpdate(sql_deleteTask);
//                    conne.commit();
                    // 清空客户信息表中本次任务下的客户

                    String sql_deleteCustInfo = "delete  from custInfo_ju_aj where cmSchedId="
                            + "'" + schedInfoResidents.getCmSchedId() + "'";
                    writableDatabase.execSQL(sql_deleteCustInfo);
                    //删除数据库中存在的图片
                    String delete_photo="delete  from perFile_aj where cmSchedId="+ "'" + schedInfoResidents.getCmSchedId() + "'";


                    writableDatabase.execSQL(delete_photo);



//                    prepared = conne.prepareStatement(sql_deleteCustInfo);
//                    prepared.executeUpdate();
//                    prepared.close();
//                    state.executeUpdate(sql_deleteCustInfo);
//                    conne.commit();
                    // 清空该任务的上传表,

                    String uploadcustInfo_aj = "delete  from uploadcustInfo_aj where cmSchedId="
                            + "'" + schedInfoResidents.getCmSchedId() + "'";
                    writableDatabase.execSQL(uploadcustInfo_aj);
//                    state.executeUpdate(uploadcustInfo_aj);
//                    prepared = conne.prepareStatement(uploadcustInfo_aj);
//                    prepared.executeUpdate();
//                    prepared.close();
//                    conne.commit();
                    // if (userId == null) {
                    // 当前登的用户
                    // String userName = Constants.loginName;
                    String sql = "insert into schedInfo_aj (userID,cmSchedId,description,"
                            + "cmScTypeCd,spType,scheduleDateTimeStart) "
                            + "values " + "(" + "'"
                            + userName_login
                            + "'"
                            + ","
                            + "'"
                            + schedInfoResidents.getCmSchedId()
                            + "'"
                            + ","
                            + "'"
                            + schedInfoResidents.getDescription()
                            + "'"
                            + ","
                            + "'"
                            + schedInfoResidents.getCmScTypeCd()
                            + "'"
                            + ","
                            + "'"
                            + schedInfoResidents.getSpType()
                            + "'"
                            + ","
                            + "'"
                            + schedInfoResidents.getScheduleDateTimeStart()
                            + "'" + ")";
                    // 插入数据返回游标对象,生成主键

                    writableDatabase.execSQL(sql);
//                    state.executeUpdate(sql);
//                    conne.commit();
//                    prepared = conne.prepareStatement(sql);
//                    prepared.executeUpdate();
//                    prepared.close();
                    renwu_AJ += schedInfoResidents.getCustInfo().size();

                    for (CustInfo_AnJian custInfo : schedInfoResidents
                            .getCustInfo()) {
                        // 用户
                        String sql_insertCustInfo = "insert into custInfo_ju_aj ("
                                + "cmSchedId,fieldActivityId,servicePointId,spType,badgeNumber,meterConfigurationId,accountId,"
                                + "entityName,customerClass,cmCustClDescr,cmMrAddress,cmMrDistrict"
                                + ",cmMrStreet,cmMrCommunity,cmMrBuilding,cmMrUnit,cmMrRoomNum,"
                                + "cmScOpenDttm,cmScResType,cmScUserType,meterType,manufacturer"
                                + ",model,serialNumber,cmMrMtrBarCode,cmMlr,cmScLgfmGj"
                                + ",cmScLgfmWz,cmScLgfmCz,cmScZjPp,cmScZjYs,cmScZjXhbh"
                                + ",cmScZjSyrq,cmScLjgCz,cmScCnlPp,cmScCnlPffs,cmScCnlSyrq,"
                                + "cmScRsqPp,cmScRsqPffs,cmScRsqSyrq,cmScBjqPp,cmScBjqSyrq,"
                                + " cmMrLastSecchkDt,cmScIntvl,cmScAqyh,cmScYhzg,cmMrState,cmMrDate) values "
                                + "(" + "'"
                                + custInfo.getCmSchedId()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getFieldActivityId()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getServicePointId()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getSpType()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getBadgeNumber()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getMeterConfigurationId()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getAccountId()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getEntityName()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCustomerClass()
                                + "'"
                                + ","

                                + "'"
                                + custInfo.getCmCustClDescr()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrAddress()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrDistrict()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrStreet()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrCommunity()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrBuilding()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrUnit()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrRoomNum()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScOpenDttm()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScResType()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScUserType()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getMeterType()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getManufacturer()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getModel()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getSerialNumber()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrMtrBarCode()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMlr()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScLgfmGj()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScLgfmWz()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScLgfmCz()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScZjPp()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScZjYs()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScZjXhbh()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScZjSyrq()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScLjgCz()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScCnlPp()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScCnlPffs()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScCnlSyrq()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScRsqPp()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScRsqPffs()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScRsqSyrq()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScBjqPp()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScBjqSyrq()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrLastSecchkDt()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScIntvl()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScAqyh()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmScYhzg()
                                + "'"
                                + ","
                                + "'" + 1 // 标识,下载还是保存或上传
                                + "'" + "," + "'0'" + ")"; // 0 初始默认的上传时间

//                        state.executeUpdate(sql_insertCustInfo);
//                        prepared = conne.prepareStatement(sql_insertCustInfo);
//                        prepared.executeUpdate();
//                        prepared.close();
                        writableDatabase.execSQL(sql_insertCustInfo);

                        ArrayList<PerPhone_Anjian> perPhones = custInfo
                                .getPerPhones();
                        // 在数据库中插入电话数据
                        // for (PerPhone perPhone : custInfo.getPerPhones()) {
                        if (perPhones != null) {
                            for (int i = 0; i < perPhones.size(); i++) {
                                PerPhone_Anjian perPhone = perPhones.get(i);
                                // 首先判断电话表中该用户是否已经存在,如果已经存在 同一个用户电话表中对应一次记录
//                                String sql_selectPhone = "select * from perPhone where accountId = "
//                                        + "'"
//                                        + custInfo.getAccountId()
//                                        + "'"
//                                        + "";

//                                String select_user = "select * from login_user where username = ?";
//
//                                net.sqlcipher.Cursor cursor = writableDatabase.rawQuery(select_user,new String[]{Constants.loginName});
                                String sql_selectPhone = "select * from perPhone where accountId = ?";
//                                ResultSet cursor_Phone = state
//                                        .executeQuery(sql_selectPhone);

                               Cursor cursor_Phone = writableDatabase.rawQuery(sql_selectPhone, new String[]{custInfo.getAccountId()});

                                if (!cursor_Phone.moveToLast()) {
                                    {
                                        String spl_insertPerPhone = "insert into perPhone(accountId,sequence,phoneType,phone,extension,cmPhoneOprtType)"
                                                + "values(" + "'"
                                                + custInfo.getAccountId()
                                                + "'"
                                                + ","
                                                + "'"
                                                + perPhone.getSequence()
                                                + "'"
                                                + ","
                                                + "'"
                                                + perPhone.getPhoneType()
                                                + "'"
                                                + ","
                                                + "'"
                                                + perPhone.getPhone()
                                                + "'"
                                                + ","
                                                + "'"
                                                + ("" + perPhone.getExtension())
                                                + "'"
                                                + "," + "'" + 0 // 电话类型默认操作类型
                                                + "'" + ")";
//                                        state.executeUpdate(spl_insertPerPhone);
//                                        prepared = conne.prepareStatement(spl_insertPerPhone);
//                                        prepared.executeUpdate();
//                                        prepared.close();
                                        writableDatabase.execSQL(spl_insertPerPhone);


                                    }
                                }
                                cursor_Phone.close();
                            }
                        }
//                        conne.commit();
                        // 首先隐患表中删除该用户,然后插入隐患信息
                        String sql_deletePerSh = "delete from perSh_aj where accountId = "
                                + "'" + custInfo.getAccountId() + "'" + "";
//                        state.executeUpdate(sql_deletePerSh);
//                        prepared = conne.prepareStatement(sql_deletePerSh);
//                        prepared.executeUpdate();
//                        prepared.close();
                        writableDatabase.execSQL(sql_deletePerSh);
//                        ArrayList<PerSh> perShs = custInfo.getPerSh();
//                        if (perShs != null) {
//                            // for (PerSh perSh : custInfo.getPerSh()) {
//                            for (int i = 0; i < perShs.size(); i++) {
//                                PerSh perSh = perShs.get(i);
//                                // 从字典表中获取隐患编码对应的描述
//                                // 隐患类型描述
////                                String sql_selectDes = "select cmScShTypeDescr from dic_cmScShItem_aj where cmScShType = "
////                                        + "'"
////                                        + perSh.getCmScShType()
////                                        + "'"
////                                        + "";
//                                String sql_selectDes = "select cmScShTypeDescr from dic_cmScShItem_aj where cmScShType = '"+perSh.getCmScShType()   + "'"+ "";
//
//                                String cmScShTypeDescr = "";
////                                ResultSet resultSet_LX = state
////                                        .executeQuery(sql_selectDes);
//                                Cursor resultSet_LX = writableDatabase.rawQuery(sql_selectDes, new String[]{});
//                                if (resultSet_LX.moveToFirst()) {
//                                    cmScShTypeDescr = resultSet_LX.getString(0);
//                                }
//                                resultSet_LX.close();
//                                // 隐患选项描述
//                                String sql_selectDes2 = "select cmScShItemDescr from dic_cmScShItem_aj where cmScShItem =  '"+perSh.getCmScShItem()   + "'"+ "";
//
//                                String cmScShItemDescr = "";
////                                ResultSet resultSet_XX = state
////                                        .executeQuery(sql_selectDes2);
//                                Cursor resultSet_XX = writableDatabase.rawQuery(sql_selectDes2, new String[]{});
//
//                                if (resultSet_XX.moveToFirst()) {
//                                    cmScShItemDescr = resultSet_XX.getString(0);
//                                }
//                                resultSet_XX.close();
//
//                                String spl_insertPer = "insert into perSh_aj(cmSchedId,accountId,cmScShType,cmScShTypeDescr,cmScShItem,cmScShItemDescr,cmScShIsOld,cmScShCheck)"
//                                        + "values(" + "'"
//                                        + custInfo.getCmSchedId()
//                                        + "'"
//                                        + ","
//                                        + "'"
//                                        + custInfo.getAccountId()
//                                        + "'"
//                                        + ","
//                                        + "'"
//                                        + perSh.getCmScShType()
//                                        + "'"
//                                        + ","
//                                        + "'"
//                                        + cmScShTypeDescr
//                                        + "'"
//                                        + ","
//                                        + "'"
//                                        + perSh.getCmScShItem()
//                                        + "'"
//                                        + ","
//                                        + "'"
//                                        + cmScShItemDescr
//                                        + "'"
//                                        + ","
//                                        + "'Y'"
//                                        + ","
//                                        + "'"
//                                        + perSh.getCmScShCheck() + "'" + ")";
////                                state.executeUpdate(spl_insertPer);
//                                writableDatabase.execSQL(spl_insertPer);
////                                prepared = conne.prepareStatement(spl_insertPer);
////                                prepared.executeUpdate();
////                                prepared.close();
//                            }
////                            conne.commit();
//                        }
                        // }else {
                        // }
                        //
                        //这个是增加的隐患列表 首先隐患表中删除该用户,然后插入隐患信息
                        String sql_deletePerSh1 = "delete from perSh_aj1 where accountId = "
                                + "'" + custInfo.getAccountId() + "'" + "";
//                        state.executeUpdate(sql_deletePerSh);
//                        prepared = conne.prepareStatement(sql_deletePerSh);
//                        prepared.executeUpdate();
//                        prepared.close();
                        writableDatabase.execSQL(sql_deletePerSh1);
                        ArrayList<PerSh> perShs1 = custInfo.getPerSh();
                        if (perShs1 != null) {
                            // for (PerSh perSh : custInfo.getPerSh()) {
                            for (int i = 0; i < perShs1.size(); i++) {
                                PerSh perSh1 = perShs1.get(i);
                                // 从字典表中获取隐患编码对应的描述
                                // 隐患类型描述
//                                String sql_selectDes = "select cmScShTypeDescr from dic_cmScShItem_aj where cmScShType = "
//                                        + "'"
//                                        + perSh.getCmScShType()
//                                        + "'"
//                                        + "";
                                String sql_selectDes = "select cmScShTypeDescr from dic_cmScShItem_aj where cmScShType = '"+perSh1.getCmScShType()   + "'"+ "";

                                String cmScShTypeDescr = "";
//                                ResultSet resultSet_LX = state
//                                        .executeQuery(sql_selectDes);
                                Cursor resultSet_LX = writableDatabase.rawQuery(sql_selectDes, new String[]{});
                                if (resultSet_LX.moveToFirst()) {
                                    cmScShTypeDescr = resultSet_LX.getString(0);
                                }
                                resultSet_LX.close();
                                // 隐患选项描述
                                String sql_selectDes2 = "select cmScShItemDescr from dic_cmScShItem_aj where cmScShItem =  '"+perSh1.getCmScShItem()   + "'"+ "";

                                String cmScShItemDescr = "";
//                                ResultSet resultSet_XX = state
//                                        .executeQuery(sql_selectDes2);
                                Cursor resultSet_XX = writableDatabase.rawQuery(sql_selectDes2, new String[]{});

                                if (resultSet_XX.moveToFirst()) {
                                    cmScShItemDescr = resultSet_XX.getString(0);
                                }
                                resultSet_XX.close();

                                String spl_insertPer = "insert into perSh_aj1(cmSchedId,accountId,cmScShType,cmScShTypeDescr,cmScShItem,cmScShItemDescr,cmScShIsOld,cmScShCheck)"
                                        + "values(" + "'"
                                        + custInfo.getCmSchedId()
                                        + "'"
                                        + ","
                                        + "'"
                                        + custInfo.getAccountId()
                                        + "'"
                                        + ","
                                        + "'"
                                        + perSh1.getCmScShType()
                                        + "'"
                                        + ","
                                        + "'"
                                        + cmScShTypeDescr
                                        + "'"
                                        + ","
                                        + "'"
                                        + perSh1.getCmScShItem()
                                        + "'"
                                        + ","
                                        + "'"
                                        + cmScShItemDescr
                                        + "'"
                                        + ","
                                        + "'Y'"
                                        + ","
                                        + "'"
                                        + perSh1.getCmScShCheck() + "'" + ")";
//                                state.executeUpdate(spl_insertPer);
                                writableDatabase.execSQL(spl_insertPer);
//                                prepared = conne.prepareStatement(spl_insertPer);
//                                prepared.executeUpdate();
//                                prepared.close();
                            }
//                            conne.commit();
                        }




                    }
                }
//                conne.commit();// 提交
                writableDatabase.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            Toast.makeText(getActivity(), "插入数据异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            // 关闭连接
            writableDatabase.endTransaction();
            if (db != null)
                try {
                    db.close();
                } catch (SQLiteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (writableDatabase != null)
                try {
                    writableDatabase.close();
                } catch (SQLiteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


        }
    }



    // 下载的数据加入到数据库
    private void insertDowlaodData(OutPut outPut) {
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//        db = SQLiteDatabase.openOrCreateDatabase(Constants.db_path, null);
        db.beginTransaction();
        // 根据条件查询任务表
        // 是否有记录。有 更新记录，然后获取用户信息的accoundid,是否存在，更新或插入
        // 没有 直接插入数据库
        String sql_task;
        for (SchedInfo schedInfo : outPut.getSchedInfos()) {

            sql_task = "select  userID from schedInfo where meterReadRoute="
                    + "'" + schedInfo.getMeterReadRoute() + "'"
                    + " and meterReadCycle=" + "'"
                    + schedInfo.getMeterReadCycle() + "'"
                    + " and scheduledSelectionDate=" + "'"
                    + schedInfo.getScheduledSelectionDate() + "'" + "";

            // select userID from schedInfo where meterReadRoute='1405_012' and
            // meterReadCycle='1405' and scheduledSelectionDate='2015-10-21'

            Cursor rawQuery_task = db.rawQuery(sql_task, null);

            String userId = null;
            // 该条任务的主键，如果该任务要更新，需要加上 主键 为条件
            while (rawQuery_task.moveToNext()) {
                userId = rawQuery_task.getString(0);
            }
            if (userId == null) {
                // // 没有 直接插入数据库
                // 循环任务list
                // insert into schedInfo
                // (userID,meterReadRoute,cmMrRteDescr,meterReadCycle,cmMrCycDescr,scheduledSelectionDate,scheduledReadDate)
                // values
                // ('1','1405_012','环宇','1405','宝应公司（每双月抄表）','2015-10-21','2015-10-22')

                String sql = "insert into schedInfo (userID,meterReadRoute,cmMrRteDescr,"
                        + "meterReadCycle,cmMrCycDescr,scheduledSelectionDate,scheduledReadDate) "
                        + "values " + "(" + "'"
                        + userName_login
                        + "'"
                        + ","
                        + "'"
                        + schedInfo.getMeterReadRoute()
                        + "'"
                        + ","
                        + "'"
                        + schedInfo.getCmMrRteDescr()
                        + "'"
                        + ","
                        + "'"
                        + schedInfo.getMeterReadCycle()
                        + "'"
                        + ","
                        + "'"
                        + schedInfo.getCmMrCycDescr()
                        + "'"
                        + ","
                        + "'"
                        + schedInfo.getScheduledSelectionDate()
                        + "'"
                        + ","
                        + "'" + schedInfo.getScheduledReadDate() + "'" + ")";
                // 查询数据返回游标对象
                db.execSQL(sql);
                // 获取到最新插入的rowid（也就是主键）,作为custInfo表的主键
                String sql_selectMax = "select  max(rowid) from  schedInfo";

                Cursor c = db.rawQuery(sql_selectMax, null);
                int id = 0;
                while (c.moveToNext()) {
                    id = c.getInt(0);
                }
                for (CustInfo custInfo : schedInfo.getCustInfos()) {
                    // 用户
                    String sql_insertCustInfo = "insert into custInfo (schedInfoID,meterReadCycleRouteSequence,accountId,entityName,customerClass,cmCustClDescr,cmMrAddress,cmMrDistrict,cmMrStreet,cmMrCommunity,"
                            + "cmMrBuilding,cmMrUnit,cmMrRoomNum,spMeterHistoryId,meterConfigurationId,cmMrMtrBarCode,"
                            + "fullScale,cmMrAvgVol,rateSchedule,cmRsDescr,cmMrLastBal,cmMrOverdueAmt,cmMrDebtStatDt,cmMrLastMrDttm,readType,cmMrLastMr,cmMrLastVol,"
                            + "cmMrLastDebt,cmMrLastSecchkDt,cmMrRemark,cmMrState,cmMrDate) values "
                            + "(" + "'"
                            + id
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getMeterReadCycleRouteSequence()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getAccountId()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getEntityName()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCustomerClass()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmCustClDescr()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrAddress()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrDistrict()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrStreet()
                            + "'"
                            + ","

                            + "'"
                            + custInfo.getCmMrCommunity()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrBuilding()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrUnit()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrRoomNum()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getSpMeterHistoryId()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getMeterConfigurationId()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrMtrBarCode()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getFullScale()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrAvgVol()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getRateSchedule()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmRsDescr()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrLastBal()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrOverdueAmt()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrDebtStatDt()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrLastMrDttm()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getReadType()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrLastMr()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrLastVol()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrLastDebt()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrLastSecchkDt()
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrRemark()
                            + "'"
                            + ","
                            + "'"
                            + 1
                            + "'"
                            + ","
                            + "'"
                            + custInfo.getCmMrDate()
                            + "'"
                            + ")";
                    db.execSQL(sql_insertCustInfo);

                    //
                    // String spl1 =
                    // "select * from custInfo where schedInfoID=3620100000";
                    // Cursor cursor = db.rawQuery(spl1, null);
                    //
                    // int i = cursor.getCount();
                    //
                    // System.out.println("=============i"+i);

                    // 在数据库中插入电话数据
                    for (PerPhone perPhone : custInfo.getPerPhones()) {

                        // 首先判断电话表中该用户是否已经存在,如果已经存在 同一个用户电话表中对应一次记录
                        String sql_selectPhone = "select * from perPhone where accountId = "
                                + "'" + custInfo.getAccountId() + "'" + "";
                        Cursor cursor_Phone = db
                                .rawQuery(sql_selectPhone, null);
                        int count = 0;
                        /*
                         * while (cursor_Phone.moveToNext()) {
						 *
						 * }
						 */
                        count = cursor_Phone.getCount();

                        if (count == 0) {

                            String spl_insertPerPhone = "insert into perPhone(accountId,sequence,phoneType,phone,extension,cmPhoneOprtType)"
                                    + "values(" + "'"
                                    + custInfo.getAccountId()
                                    + "'"
                                    + ","
                                    + "'"
                                    + perPhone.getSequence()
                                    + "'"
                                    + ","
                                    + "'"
                                    + perPhone.getPhoneType()
                                    + "'"
                                    + ","
                                    + "'"
                                    + perPhone.getPhone()
                                    + "'"
                                    + ","
                                    + "'"
                                    + ("" + perPhone.getExtension())
                                    + "'"
                                    + ","
                                    + "'"
                                    + perPhone.getCmPhoneOprtType() + "'" + ")";
                            db.execSQL(spl_insertPerPhone);
                        }
                    }
                }

            } else {
                // 有数据 更新记录，需获取用户信息的accoundid,是否存在，更新或插入
                // 更新任务表
                // String userid = rawQuery_task.getString(0);

                String sql_updata = "update schedInfo set userID=" + "'"
                        + userId + "'" + ",meterReadRoute=" + "'"
                        + schedInfo.getMeterReadRoute() + "'"
                        + ",cmMrRteDescr=" + "'" + schedInfo.getCmMrRteDescr()
                        + "'" + ",meterReadCycle=" + "'"
                        + schedInfo.getMeterReadCycle() + "'"
                        + ",cmMrCycDescr=" + "'" + schedInfo.getCmMrCycDescr()
                        + "'" + ",scheduledSelectionDate=" + "'"
                        + schedInfo.getScheduledSelectionDate() + "'"
                        + ",scheduledReadDate=" + "'"
                        + schedInfo.getScheduledReadDate() + "'"
                        + "  where userID=" + "'" + userId + "'"
                        + " and meterReadRoute=" + "'"
                        + schedInfo.getMeterReadRoute() + "'"
                        + " and meterReadCycle=" + "'"
                        + schedInfo.getMeterReadCycle() + "'"
                        + " and scheduledSelectionDate=" + "'"
                        + schedInfo.getScheduledSelectionDate() + "'" + "";
                db.execSQL(sql_updata);

                // 获取到该操作员的rowid（也就是主键）,如有新增用户作为custInfo表的主键
                String sql_selectMax = "select  rowid from  schedInfo where userID= "
                        + "'"
                        + userId
                        + "'"
                        + "and meterReadRoute="
                        + "'"
                        + schedInfo.getMeterReadRoute()
                        + "'"
                        + " and meterReadCycle="
                        + "'"
                        + schedInfo.getMeterReadCycle()
                        + "'"
                        + " and scheduledSelectionDate="
                        + "'"
                        + schedInfo.getScheduledSelectionDate() + "'" + "";

                Cursor c = db.rawQuery(sql_selectMax, null);

                int add_id = 0;
                while (c.moveToNext()) {
                    add_id = c.getInt(0);
                }
                // 插入或跟新用户信息
                for (CustInfo custInfo : schedInfo.getCustInfos()) {
                    // 根据accoundid查询该用户是否存在，存在 跟新 否则 插入
                    String sql_selectAccount = "select accountId from custInfo where schedInfoID = "
                            + "'"
                            + add_id
                            + "'"
                            + " and spMeterHistoryId="
                            + "'" + custInfo.getSpMeterHistoryId() + "'" + "";

                    Cursor rawQuery_Account = db.rawQuery(sql_selectAccount,
                            null);
                    String accountId = null;
                    while (rawQuery_Account.moveToNext()) {
                        accountId = rawQuery_Account.getString(0);
                    }

                    if (TextUtils.isEmpty(accountId)) {
                        // 新增任务用户信息， 插入表中（包括电话信息）
                        // 用户
                        String sql_insertCustInfo = "insert into custInfo (schedInfoID,meterReadCycleRouteSequence,accountId,entityName,customerClass,cmCustClDescr,cmMrAddress,cmMrDistrict,cmMrStreet,cmMrCommunity,"
                                + "cmMrBuilding,cmMrUnit,cmMrRoomNum,spMeterHistoryId,meterConfigurationId,cmMrMtrBarCode,"
                                + "fullScale,cmMrAvgVol,rateSchedule,cmRsDescr,cmMrLastBal,cmMrOverdueAmt,cmMrDebtStatDt,cmMrLastMrDttm,readType,cmMrLastMr,cmMrLastVol,"
                                + "cmMrLastDebt,cmMrLastSecchkDt,cmMrRemark,cmMrState,cmMrDate) values "
                                + "(" + "'"
                                + add_id
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getMeterReadCycleRouteSequence()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getAccountId()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getEntityName()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCustomerClass()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmCustClDescr()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrAddress()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrDistrict()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrStreet()
                                + "'"
                                + ","

                                + "'"
                                + custInfo.getCmMrCommunity()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrBuilding()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrUnit()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrRoomNum()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getSpMeterHistoryId()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getMeterConfigurationId()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrMtrBarCode()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getFullScale()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrAvgVol()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getRateSchedule()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmRsDescr()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrLastBal()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrOverdueAmt()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrDebtStatDt()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrLastMrDttm()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getReadType()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrLastMr()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrLastVol()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrLastDebt()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrLastSecchkDt()
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrRemark()
                                + "'"
                                + ","
                                + "'"
                                + 1
                                + "'"
                                + ","
                                + "'"
                                + custInfo.getCmMrDate() + "'" + ")";
                        db.execSQL(sql_insertCustInfo);
                        for (PerPhone perPhone : custInfo.getPerPhones()) {

                            String spl_insertPerPhone = "insert into perPhone(accountId,sequence,phoneType,phone,extension,cmPhoneOprtType)"
                                    + "values(" + "'"
                                    + custInfo.getAccountId()
                                    + "'"
                                    + ","
                                    + "'"
                                    + perPhone.getSequence()
                                    + "'"
                                    + ","
                                    + "'"
                                    + perPhone.getPhoneType()
                                    + "'"
                                    + ","
                                    + "'"
                                    + perPhone.getPhone()
                                    + "'"
                                    + ","
                                    + "'"
                                    + ("" + perPhone.getExtension())
                                    + "'"
                                    + ","
                                    + "'"
                                    + perPhone.getCmPhoneOprtType() + "'" + ")";
                            db.execSQL(spl_insertPerPhone);
                        }

                    } else {
                        // 原有任务用户信息，更新用户信息及电话信息，根据查询到的accountid

                        String sql_updateCustInfo = "update custInfo set schedInfoID="
                                + "'"
                                + add_id
                                + "'"
                                + ",meterReadCycleRouteSequence="
                                + "'"
                                + custInfo.getMeterReadCycleRouteSequence()
                                + "'"
                                + ",entityName="
                                + "'"
                                + custInfo.getEntityName()
                                + "'"
                                + ",customerClass="
                                + "'"
                                + custInfo.getCustomerClass()
                                + "'"
                                + ",cmCustClDescr="
                                + "'"
                                + custInfo.getCmCustClDescr()
                                + "'"
                                + ",cmMrAddress="
                                + "'"
                                + custInfo.getCmMrAddress()
                                + "'"
                                + ",cmMrDistrict="
                                + "'"
                                + custInfo.getCmMrDistrict()
                                + "'"
                                + ",cmMrStreet="
                                + "'"
                                + custInfo.getCmMrStreet()
                                + "'"
                                + ",cmMrCommunity="
                                + "'"
                                + custInfo.getCmMrCommunity()
                                + "'"
                                + ","
                                + "cmMrBuilding="
                                + "'"
                                + custInfo.getCmMrBuilding()
                                + "'"
                                + ",cmMrUnit="
                                + "'"
                                + custInfo.getCmMrUnit()
                                + "'"
                                + ",cmMrRoomNum="
                                + "'"
                                + custInfo.getCmMrRoomNum()
                                + "'"
                                + ",spMeterHistoryId="
                                + "'"
                                + custInfo.getSpMeterHistoryId()
                                + "'"
                                + ",meterConfigurationId="
                                + "'"
                                + custInfo.getMeterConfigurationId()
                                + "'"
                                + ",cmMrMtrBarCode="
                                + "'"
                                + custInfo.getCmMrMtrBarCode()
                                + "'"
                                + ","
                                + "fullScale="
                                + "'"
                                + custInfo.getFullScale()
                                + "'"
                                + ",cmMrAvgVol="
                                + "'"
                                + custInfo.getCmMrAvgVol()
                                + "'"
                                + ",rateSchedule="
                                + "'"
                                + custInfo.getRateSchedule()
                                + "'"
                                + ",cmRsDescr="
                                + "'"
                                + custInfo.getCmRsDescr()
                                + "'"
                                + ",cmMrLastBal="
                                + "'"
                                + custInfo.getCmMrLastBal()
                                + "'"
                                + ",cmMrOverdueAmt="
                                + "'"
                                + custInfo.getCmMrOverdueAmt()
                                + "'"
                                + ",cmMrDebtStatDt="
                                + "'"
                                + custInfo.getCmMrDebtStatDt()
                                + "'"
                                + ",cmMrLastMrDttm="
                                + "'"
                                + custInfo.getCmMrLastMrDttm()
                                + "'"
                                + ",readType="
                                + "'"
                                + custInfo.getReadType()
                                + "'"
                                + ",cmMrLastMr="
                                + "'"
                                + custInfo.getCmMrLastMr()
                                + "'"
                                + ",cmMrLastVol="
                                + "'"
                                + custInfo.getCmMrLastVol()
                                + "'"
                                + ","
                                + "cmMrLastDebt="
                                + "'"
                                + custInfo.getCmMrLastDebt()
                                + "'"
                                + ",cmMrLastSecchkDt="
                                + "'"
                                + custInfo.getCmMrLastSecchkDt()
                                + "'"
                                + ",cmMrRemark="
                                + "'"
                                + custInfo.getCmMrRemark()
                                + "'"
                                + ",cmMrState="
                                + "'"
                                + 1
                                + "'"
                                + ",cmMrDate="
                                + "'"
                                + custInfo.getCmMrDate()
                                + "'"
                                + " where accountId="
                                + "'"
                                + accountId
                                + "'"
                                + " and schedInfoID = "
                                + "'"
                                + add_id
                                + "'"
                                + " ";
                        db.execSQL(sql_updateCustInfo);

                        // 更新或插入电话
                        for (PerPhone perPhone : custInfo.getPerPhones()) {

                            // 查询更新的该用户是否电话，有 更新电话 否 则 插入电话
                            String sql_selectPhone = "select * from perPhone where accountId = "
                                    + "'" + accountId + "'" + "";
                            Cursor cursor_Phone = db.rawQuery(sql_selectPhone,
                                    null);
                            int count = 0;
                            /*
                             * while (cursor_Phone.moveToNext()) {
							 *
							 * }
							 */
                            count = cursor_Phone.getCount();

                            if (count == 0) {
                                // 该用户之前没有电话
                                String spl_insertPerPhone = "insert into perPhone(accountId,sequence,phoneType,phone,extension,cmPhoneOprtType)"
                                        + "values(" + "'"
                                        + custInfo.getAccountId()
                                        + "'"
                                        + ","
                                        + "'"
                                        + perPhone.getSequence()
                                        + "'"
                                        + ","
                                        + "'"
                                        + perPhone.getPhoneType()
                                        + "'"
                                        + ","
                                        + "'"
                                        + perPhone.getPhone()
                                        + "'"
                                        + ","
                                        + "'"
                                        + ("" + perPhone.getExtension())
                                        + "'"
                                        + ","
                                        + "'"
                                        + perPhone.getCmPhoneOprtType()
                                        + "'" + ")";
                                db.execSQL(spl_insertPerPhone);

                            } else {
                                // 有电话 进行更新,根据用户accountId

                                String spl_updatePerPhone = "update  perPhone  set sequence="
                                        + "'"
                                        + perPhone.getSequence()
                                        + "'"
                                        + ",phoneType="
                                        + "'"
                                        + perPhone.getPhoneType()
                                        + "'"
                                        + ",phone="
                                        + "'"
                                        + perPhone.getPhone()
                                        + "'"
                                        + ",extension="
                                        + "'"
                                        + ("" + perPhone.getExtension())
                                        + "'"
                                        + ",cmPhoneOprtType="
                                        + "'"
                                        + perPhone.getCmPhoneOprtType()
                                        + "'"
                                        + "  where accountId ="
                                        + "'"
                                        + accountId + "'" + "";
                                db.execSQL(spl_updatePerPhone);

                            }

                        }

                    }

                }

            }

        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    // 将数据字典插入数据库
    public boolean dicDataInsterIntoDB_A10(OutPut result) {

//        Connection db = null;
//        Statement state = null;
//        PreparedStatement ps =null;
        DatabaseHelper db = null;
        SQLiteDatabase writableDatabase = null;
        try {
//            db = SQLiteData.openOrCreateDatabase(getActivity());
//            state = db.createStatement();

            db = new DatabaseHelper(mContext);

            writableDatabase = db.getWritableDatabase();
            // 清空数据字典
            String sql_clear = "Delete from dictionaries";
//            state.execute(sql_clear);
            writableDatabase.execSQL(sql_clear);

            String sql_clear1 = "Delete from dic_cmScShItem_aj";
//            state.execute(sql_clear1);
            writableDatabase.execSQL(sql_clear1);
            String sql_clear2 = "Delete from dic_modelInfo_aj";
//            state.execute(sql_clear2);
            writableDatabase.execSQL(sql_clear2);
            OutPut outPut = result;

            String sql = "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) values (?,?,?)";
//            db.setAutoCommit(false);
//             ps = db.prepareStatement(sql);


            // 服务评价数据
            ArrayList<CmMrCommInfo_Dic> cmMrCommInfo = outPut.getCmMrCommInfo();
            if (cmMrCommInfo.size() > 0) {
                writableDatabase.beginTransaction();
                for (int i = 0; i < cmMrCommInfo.size(); i++) {
                    // 服务评价最里面集合
                    ArrayList<CmMrComm_Dic> cmMrComm = cmMrCommInfo.get(i)
                            .getCmMrComm();
                    if (cmMrComm.size() > 0) {
                        for (int a = 0; a < cmMrComm.size(); a++) {
                            CmMrComm_Dic cmMrComm_Dic = cmMrComm.get(a);
                            /*
                             * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmMrComm_Dic.getCmMrCommDescr() + "'"
							 *
							 * + "," + "'" + cmMrComm_Dic.getCmMrCommCd() + "'"
							 * + "," + "'cmMrComm'" + ")";
							 * state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{cmMrComm_Dic.getCmMrCommDescr(),
                                    cmMrComm_Dic.getCmMrCommCd(), "cmMrComm"});

//                            ps.setString(1, cmMrComm_Dic.getCmMrCommDescr());
//                            ps.setString(2, cmMrComm_Dic.getCmMrCommCd());
//                            ps.setString(3, "cmMrComm");
//                            ps.addBatch();
                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }


            // 安检入户情况
            ArrayList<CmScAjrhInfo_Dic> cmScAjrhInfo = outPut.getCmScAjrhInfo();
            if (cmScAjrhInfo.size() > 0) {
//                writableDatabase.beginTransaction();
//                ps = db.prepareStatement(sql);
                for (int i = 0; i < cmScAjrhInfo.size(); i++) {
                    // 安检入户情况最里面集合
                    ArrayList<CmScAjrh_Dic> cmScAjrhs = cmScAjrhInfo.get(i)
                            .getCmScAjrh();
                    if (cmScAjrhs.size() > 0) {
                        for (int a = 0; a < cmScAjrhs.size(); a++) {
                            CmScAjrh_Dic cmScAjrh = cmScAjrhs.get(a);
                            /*
                             * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScAjrh.getCmScAjrhDescr() + "'"
							 *
							 * + "," + "'" + cmScAjrh.getCmScAjrh() + "'" + ","
							 * + "'cmScAjrh'" + ")"; state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{cmScAjrh.getCmScAjrhDescr(),
                                    cmScAjrh.getCmScAjrh(), "cmScAjrh"});
//                            ps.setString(1, cmScAjrh.getCmScAjrhDescr());
//                            ps.setString(2, cmScAjrh.getCmScAjrh());
//                            ps.setString(3, "cmScAjrh");
//                            ps.addBatch();

                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 文件业务类型
            ArrayList<CmScBusiTypeInfo_Dic> cmScBusiTypeInfo = outPut
                    .getCmScBusiTypeInfo();
            if (cmScBusiTypeInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < cmScBusiTypeInfo.size(); i++) {
                    // 文件业务类型最里面集合
                    ArrayList<CmScBusiType_Dic> cmScBusiTypes = cmScBusiTypeInfo
                            .get(i).getCmScBusiType();
                    if (cmScBusiTypes.size() > 0) {
                        for (int a = 0; a < cmScBusiTypes.size(); a++) {
                            CmScBusiType_Dic cmScBusiType = cmScBusiTypes
                                    .get(a);
                            /*
                             * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScBusiType.getCmScBusiTypeDescr() + "'"
							 *
							 * + "," + "'" + cmScBusiType.getCmScBusiType() +
							 * "'" + "," + "'cmScBusiType'" + ")";
							 * state.executeUpdate(sql);
							 */

                            writableDatabase.execSQL(sql, new Object[]{cmScBusiType.getCmScBusiTypeDescr(),
                                    cmScBusiType.getCmScBusiType(), "cmScBusiType"});
//                            ps.setString(1, cmScBusiType.getCmScBusiTypeDescr());
//                            ps.setString(2, cmScBusiType.getCmScBusiType());
//                            ps.setString(3, "cmScBusiType");
//                            ps.addBatch();
                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 采暖炉-排放方式
            ArrayList<CmScCnlPffsInfo> cmScCnlPffsInfo = outPut
                    .getCmScCnlPffsInfo();
            if (cmScCnlPffsInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < cmScCnlPffsInfo.size(); i++) {
                    // 采暖炉-排放方式最里面集合
                    ArrayList<CmScCnlPffs> cmScCnlPffss = cmScCnlPffsInfo
                            .get(i).getCmScCnlPffs();
                    if (cmScCnlPffss.size() > 0) {
                        for (int a = 0; a < cmScCnlPffss.size(); a++) {
                            CmScCnlPffs cmScCnlPffs = cmScCnlPffss.get(a);
                            /*
                             * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScCnlPffs.getCmScCnlPffsDescr() + "'"
							 *
							 * + "," + "'" + cmScCnlPffs.getCmScCnlPffs() + "'"
							 * + "," + "'cmScCnlPffs'" + ")";
							 * state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{cmScCnlPffs.getCmScCnlPffsDescr(),
                                    cmScCnlPffs.getCmScCnlPffs(), "cmScCnlPffs"});
//                            ps.setString(1, cmScCnlPffs.getCmScCnlPffsDescr());
//                            ps.setString(2, cmScCnlPffs.getCmScCnlPffs());
//                            ps.setString(3, "cmScCnlPffs");
//                            ps.addBatch();
                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 立管阀门-材质
            ArrayList<CmScLgfmCzInfo> cmScLgfmCzInfo = outPut
                    .getCmScLgfmCzInfo();
            if (cmScLgfmCzInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < cmScLgfmCzInfo.size(); i++) {
                    // 立管阀门-材质最里面集合
                    ArrayList<CmScLgfmCz> cmScLgfmCzs = cmScLgfmCzInfo.get(i)
                            .getCmScLgfmWz();
                    if (cmScLgfmCzs.size() > 0) {
                        for (int a = 0; a < cmScLgfmCzs.size(); a++) {
                            CmScLgfmCz cmScLgfmCz = cmScLgfmCzs.get(a);
                            /*
                             * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScLgfmCz.getCmScLgfmCzDescr() + "'"
							 *
							 * + "," + "'" + cmScLgfmCz.getCmScLgfmCz() + "'" +
							 * "," + "'cmScLgfmCz'" + ")";
							 * state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{cmScLgfmCz.getCmScLgfmCzDescr(),
                                    cmScLgfmCz.getCmScLgfmCz(), "cmScLgfmCz"});
//                            ps.setString(1, cmScLgfmCz.getCmScLgfmCzDescr());
//                            ps.setString(2, cmScLgfmCz.getCmScLgfmCz());
//                            ps.setString(3, "cmScLgfmCz");
//                            ps.addBatch();
                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 立管阀门-位置
            ArrayList<CmScLgfmWzInfo> cmScLgfmWzInfo = outPut
                    .getCmScLgfmWzInfo();
            if (cmScLgfmWzInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();

                for (int i = 0; i < cmScLgfmWzInfo.size(); i++) {
                    // 立管阀门-位置最里面集合
                    ArrayList<CmScLgfmWz> cmScLgfmWzs = cmScLgfmWzInfo.get(i)
                            .getCmScLgfmWz();
                    if (cmScLgfmWzs.size() > 0) {
                        for (int a = 0; a < cmScLgfmWzs.size(); a++) {
                            CmScLgfmWz cmScLgfmWz = cmScLgfmWzs.get(a);
                            /*
                             * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScLgfmWz.getCmScLgfmWzDescr() + "'"
							 *
							 * + "," + "'" + cmScLgfmWz.getCmScLgfmWz() + "'" +
							 * "," + "'cmScLgfmWz'" + ")";
							 * state.executeUpdate(sql);
							 */

                            writableDatabase.execSQL(sql, new Object[]{cmScLgfmWz.getCmScLgfmWzDescr(),
                                    cmScLgfmWz.getCmScLgfmWz(), "cmScLgfmWz"});
//                            ps.setString(1, cmScLgfmWz.getCmScLgfmWzDescr());
//                            ps.setString(2, cmScLgfmWz.getCmScLgfmWz());
//                            ps.setString(3, "cmScLgfmWz");
//                            ps.addBatch();

                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 连接管-材质
            ArrayList<CmScLjgCzInfo> cmScLjgCzInfo = outPut.getCmScLjgCzInfo();
            if (cmScLjgCzInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < cmScLjgCzInfo.size(); i++) {
                    // 连接管-材质最里面集合
                    ArrayList<MScLjgCz> mScLjgCzs = cmScLjgCzInfo.get(i)
                            .getmScLjgCz();
                    if (mScLjgCzs.size() > 0) {
                        for (int a = 0; a < mScLjgCzs.size(); a++) {
                            MScLjgCz mScLjgCz = mScLjgCzs.get(a);
                            /*
                             * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + mScLjgCz.getCmScLjgCzDescr() + "'"
							 *
							 * + "," + "'" + mScLjgCz.getCmScLjgCz() + "'" + ","
							 * + "'mScLjgCz'" + ")"; state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{mScLjgCz.getCmScLjgCzDescr(),
                                    mScLjgCz.getCmScLjgCz(), "mScLjgCz"});
//                            ps.setString(1, mScLjgCz.getCmScLjgCzDescr());
//                            ps.setString(2, mScLjgCz.getCmScLjgCz());
//                            ps.setString(3, "mScLjgCz");
//                            ps.addBatch();

                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 住宿类型
            ArrayList<CmScResTypeInfo> cmScResTypeInfo = outPut
                    .getCmScResTypeInfo();
            if (cmScResTypeInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();

                for (int i = 0; i < cmScResTypeInfo.size(); i++) {
                    // 住宿类型最里面集合
                    ArrayList<CmScResType> cmScResTypes = cmScResTypeInfo
                            .get(i).getCmScResType();
                    if (cmScResTypes.size() > 0) {
                        for (int a = 0; a < cmScResTypes.size(); a++) {
                            CmScResType cmScResType = cmScResTypes.get(a);
                            /*
                             * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScResType.getCmScResTypeDescr() + "'"
							 *
							 * + "," + "'" + cmScResType.getCmScResType() + "'"
							 * + "," + "'cmScResType'" + ")";
							 * state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{cmScResType.getCmScResTypeDescr(),
                                    cmScResType.getCmScResType(), "cmScResType"});

//                            ps.setString(1, cmScResType.getCmScResTypeDescr());
//                            ps.setString(2, cmScResType.getCmScResType());
//                            ps.setString(3, "cmScResType");
//                            ps.addBatch();

                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 热水器-排放方式
            ArrayList<CmScRsqPffsInfo> cmScRsqPffsInfo = outPut
                    .getCmScRsqPffsInfo();
            if (cmScRsqPffsInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < cmScRsqPffsInfo.size(); i++) {
                    // 热水器-排放方式最里面集合
                    ArrayList<CmScRsqPffs> cmScRsqPffss = cmScRsqPffsInfo
                            .get(i).getCmScRsqPffs();
                    if (cmScRsqPffss.size() > 0) {
                        for (int a = 0; a < cmScRsqPffss.size(); a++) {
                            CmScRsqPffs cmScRsqPffs = cmScRsqPffss.get(a);
							/*
							 * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScRsqPffs.getCmScRsqPffsDescr() + "'"
							 *
							 * + "," + "'" + cmScRsqPffs.getCmScRsqPffs() + "'"
							 * + "," + "'cmScRsqPffs'" + ")";
							 * state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{cmScRsqPffs.getCmScRsqPffsDescr(),
                                    cmScRsqPffs.getCmScRsqPffs(), "cmScRsqPffs"});
//                            ps.setString(1, cmScRsqPffs.getCmScRsqPffsDescr());
//                            ps.setString(2, cmScRsqPffs.getCmScRsqPffs());
//                            ps.setString(3, "cmScRsqPffs");
//                            ps.addBatch();
                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 安全讲解选项
            ArrayList<CmScSpItemInfo> cmScSpItemInfo = outPut
                    .getCmScSpItemInfo();
            if (cmScSpItemInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < cmScSpItemInfo.size(); i++) {
                    // 安全讲解选项最里面集合
                    ArrayList<CmScSpItem> cmScSpItems = cmScSpItemInfo.get(i)
                            .getCmScSpItem();
                    if (cmScSpItems.size() > 0) {
                        for (int a = 0; a < cmScSpItems.size(); a++) {
                            CmScSpItem cmScSpItem = cmScSpItems.get(a);
							/*
							 * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScSpItem.getCmScSpItemDescr() + "'"
							 *
							 * + "," + "'" + cmScSpItem.getCmScSpItem() + "'" +
							 * "," + "'cmScSpItem'" + ")";
							 * state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{cmScSpItem.getCmScSpItemDescr(),
                                    cmScSpItem.getCmScSpItem(), "cmScSpItem"});
//                            ps.setString(1, cmScSpItem.getCmScSpItemDescr());
//                            ps.setString(2, cmScSpItem.getCmScSpItem());
//                            ps.setString(3, "cmScSpItem");
//                            ps.addBatch();
                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 特殊用户
            ArrayList<CmScUserTypeInfo> cmScUserTypeInfo = outPut
                    .getCmScUserTypeInfo();
            if (cmScUserTypeInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < cmScUserTypeInfo.size(); i++) {
                    // 特殊用户最里面集合
                    ArrayList<CmScUserType> cmScUserTypes = cmScUserTypeInfo
                            .get(i).getCmScUserType();
                    if (cmScUserTypes.size() > 0) {
                        for (int a = 0; a < cmScUserTypes.size(); a++) {
                            CmScUserType cmScUserType = cmScUserTypes.get(a);
                            // String sql =
                            // "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
                            // + "values "
                            // + "("
                            //
                            // + "'"
                            // + cmScUserType.getCmScUserTypeDescr()
                            // + "'"
                            //
                            // + ","
                            // + "'"
                            // + cmScUserType.getCmScUserType()
                            // + "'"
                            // + "," + "'cmScUserType'" + ")";
                            // state.executeUpdate(sql);
                            writableDatabase.execSQL(sql, new Object[]{cmScUserType.getCmScUserTypeDescr(),
                                    cmScUserType.getCmScUserType(), "cmScUserType"});
//                            ps.setString(1, cmScUserType.getCmScUserTypeDescr());
//                            ps.setString(2, cmScUserType.getCmScUserType());
//                            ps.setString(3, "cmScUserType");
//                            ps.addBatch();

                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 隐患整改情况
            ArrayList<CmScYhzgInfo> cmScYhzgInfo = outPut.getCmScYhzgInfo();
            if (cmScYhzgInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < cmScYhzgInfo.size(); i++) {
                    // 隐患整改情况最里面集合
                    ArrayList<CmScYhzg> cmScYhzgs = cmScYhzgInfo.get(i)
                            .getCmScYhzg();
                    if (cmScYhzgs.size() > 0) {
                        for (int a = 0; a < cmScYhzgs.size(); a++) {
                            CmScYhzg cmScYhzg = cmScYhzgs.get(a);
							/*
							 * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScYhzg.getCmScYhzgDescr() + "'"
							 *
							 * + "," + "'" + cmScYhzg.getCmScYhzg() + "'" + ","
							 * + "'cmScYhzg'" + ")"; state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{cmScYhzg.getCmScYhzgDescr(),
                                    cmScYhzg.getCmScYhzg(), "cmScYhzg"});
//                            ps.setString(1, cmScYhzg.getCmScYhzgDescr());
//                            ps.setString(2, cmScYhzg.getCmScYhzg());
//                            ps.setString(3, "cmScYhzg");
//                            ps.addBatch();

                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 灶具-熄火保护
            ArrayList<CmScZjXhbhInfo> cmScZjXhbhInfo = outPut
                    .getCmScZjXhbhInfo();
            if (cmScZjXhbhInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < cmScZjXhbhInfo.size(); i++) {
                    // 灶具-熄火保护最里面集合
                    ArrayList<CmScZjXhbh> cmScZjXhbhs = cmScZjXhbhInfo.get(i)
                            .getCmScZjXhbh();
                    if (cmScZjXhbhs.size() > 0) {
                        for (int a = 0; a < cmScZjXhbhs.size(); a++) {
                            CmScZjXhbh cmScZjXhbh = cmScZjXhbhs.get(a);
							/*
							 * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScZjXhbh.getCmScZjXhbhDescr() + "'"
							 *
							 * + "," + "'" + cmScZjXhbh.getCmScZjXhbh() + "'" +
							 * "," + "'cmScZjXhbh'" + ")";
							 * state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{cmScZjXhbh.getCmScZjXhbhDescr(),
                                    cmScZjXhbh.getCmScZjXhbh(), "cmScZjXhbh"});
//                            ps.setString(1, cmScZjXhbh.getCmScZjXhbhDescr());
//                            ps.setString(2, cmScZjXhbh.getCmScZjXhbh());
//                            ps.setString(3, "cmScZjXhbh");
//                            ps.addBatch();

                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 灶具-样式
            ArrayList<CmScZjYsInfo> cmScZjYsInfo = outPut.getCmScZjYsInfo();
            if (cmScZjYsInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < cmScZjYsInfo.size(); i++) {
                    // 灶具-样式最里面集合
                    ArrayList<CmScZjYs> cmScZjYss = cmScZjYsInfo.get(i)
                            .getCmScZjYs();
                    if (cmScZjYss.size() > 0) {
                        for (int a = 0; a < cmScZjYss.size(); a++) {
                            CmScZjYs cmScZjYs = cmScZjYss.get(a);
							/*
							 * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + cmScZjYs.getCmScZjYsDescr() + "'"
							 *
							 * + "," + "'" + cmScZjYs.getCmScZjYs() + "'" + ","
							 * + "'cmScZjYs'" + ")"; state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{cmScZjYs.getCmScZjYsDescr(),
                                    cmScZjYs.getCmScZjYs(), "cmScZjYs"});
//                            ps.setString(1, cmScZjYs.getCmScZjYsDescr());
//                            ps.setString(2, cmScZjYs.getCmScZjYs());
//                            ps.setString(3, "cmScZjYs");
//                            ps.addBatch();

                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 表具-计量表类型
            ArrayList<MeterTypeInfo> meterTypeInfo = outPut.getMeterTypeInfo();
            if (meterTypeInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < meterTypeInfo.size(); i++) {
                    // 表具-计量表类型最里面集合
                    ArrayList<MeterType> meterTypes = meterTypeInfo.get(i)
                            .getMeterType();
                    if (meterTypes.size() > 0) {
                        for (int a = 0; a < meterTypes.size(); a++) {
                            MeterType meterType = meterTypes.get(a);
							/*
							 * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + meterType.getMeterTypeDescr() + "'"
							 *
							 * + "," + "'" + meterType.getMeterType() + "'" +
							 * "," + "'meterType'" + ")";
							 * state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{meterType.getMeterTypeDescr(),
                                    meterType.getMeterType(), "meterType"});
//                            ps.setString(1, meterType.getMeterTypeDescr());
//                            ps.setString(2, meterType.getMeterType());
//                            ps.setString(3, "meterType");
//                            ps.addBatch();
                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 电话类型

            ArrayList<PhoneTypeInfo> phoneTypeInfo = outPut.getPhoneTypeInfo();
            if (phoneTypeInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < phoneTypeInfo.size(); i++) {
                    // 电话最里面集合
                    ArrayList<phoneType> phoneTypes = phoneTypeInfo.get(i)
                            .getPhoneType();
                    if (phoneTypes.size() > 0) {
                        for (int a = 0; a < phoneTypes.size(); a++) {
                            phoneType phonetype = phoneTypes.get(a);
							/*
							 * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + phonetype.getPhoneTypeDescr() + "'"
							 *
							 * + "," + "'" + phonetype.getPhoneType() + "'"
							 *
							 * + "," + "'phoneType'"
							 *
							 * + ")"; state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{phonetype.getPhoneTypeDescr(),
                                    phonetype.getPhoneType(), "phoneType"});
//                            ps.setString(1, phonetype.getPhoneTypeDescr());
//                            ps.setString(2, phonetype.getPhoneType());
//                            ps.setString(3, "phoneType");
//                            ps.addBatch();
                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 读数类型数据
            ArrayList<ReadTypeInfo> readTypeInfo = outPut.getReadTypeInfo();
            if (readTypeInfo.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (int i = 0; i < readTypeInfo.size(); i++) {
                    // 读数类型最里面集合
                    ArrayList<readType> readTypes = readTypeInfo.get(i)
                            .getReadType();
                    if (readTypes.size() > 0) {
                        for (int a = 0; a < readTypes.size(); a++) {
                            readType readType = readTypes.get(a);
							/*
							 * String sql =
							 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
							 * + "values " + "("
							 *
							 * + "'" + readType.getReadTypeDescr() + "'"
							 *
							 * + "," + "'" + readType.getReadType() + "'" + ","
							 * + "'readType'"
							 *
							 * + ")"; state.executeUpdate(sql);
							 */
                            writableDatabase.execSQL(sql, new Object[]{readType.getReadTypeDescr(),
                                    readType.getReadType(), "readType"});
//                            ps.setString(1, readType.getReadTypeDescr());
//                            ps.setString(2, readType.getReadType());
//                            ps.setString(3, "readType");
//                            ps.addBatch();
                        }
                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 表具-制造商/型号,manufacturerLists 单独解析出来的对象所在的集合
            if (manufacturerLists.size() > 0) {
//                ps = db.prepareStatement(sql);
//                writableDatabase.beginTransaction();
                for (ManufacturerList manufacturerList : manufacturerLists) {
                    // 将类型编码和描述插入到数据字典
					/*
					 * String sql =
					 * "insert into dictionaries (dictionaryDescr,dictionaryCode,parentID) "
					 * + "values " + "("
					 *
					 * + "'" + manufacturerList.getManufacturerDescr() + "'"
					 *
					 * + "," + "'" + manufacturerList.getManufacturer() + "'" +
					 * "," + "'manufacturerList'" + ")";
					 * state.executeUpdate(sql);
					 */
//                    ps.setString(1, manufacturerList.getManufacturerDescr());
//                    ps.setString(2, manufacturerList.getManufacturer());
//                    ps.setString(3, "manufacturerList");
//                    ps.addBatch();
                    writableDatabase.execSQL(sql, new Object[]{manufacturerList.getManufacturerDescr(),
                            manufacturerList.getManufacturer(), "manufacturerList"});

                    ArrayList<ModelInfo> modelInfos = manufacturerList
                            .getModelInfo();
                    if (modelInfos.size() > 0) {

//                        Connection db2 = SQLiteData.openOrCreateDatabase(getActivity());
//                        db.setAutoCommit(false);
                        String sql1 = "insert into dic_modelInfo_aj (manufacturer ,manufacturerDescr,model,modelDescr ) values (?,?,?,?) ";
//                        PreparedStatement ps1 = db.prepareStatement(sql1);
//                        SQLiteDatabase WritableDatabase1 = db.getWritableDatabase();
//                        WritableDatabase1.beginTransaction();
                        for (ModelInfo modelInfo : modelInfos) {
                            ArrayList<Model> models = modelInfo.getModel();
                            if (models.size() > 0) {
                                for (Model model : models) {

                                    // 将数据插入到 { 数据字典_表具-型号(dic_modelInfo_aj) }
									/*
									 * String sql1 =
									 * "insert into dic_modelInfo_aj (manufacturer ,manufacturerDescr,model,modelDescr ) "
									 * + "values " + "("
									 *
									 * + "'" +
									 * manufacturerList.getManufacturer() + "'"
									 *
									 * + "," + "'" +
									 * manufacturerList.getManufacturerDescr() +
									 * "'"
									 *
									 * + "," + "'" + model.getModel() + "'"
									 *
									 * + "," + "'" + model.getModelDescr() + "'"
									 * + ")"; state.executeUpdate(sql1);
									 */
                                    writableDatabase.execSQL(sql1, new Object[]{manufacturerList.getManufacturer(),
                                            manufacturerList
                                                    .getManufacturerDescr(), model.getModel(), model.getModelDescr()});


//                                    ps1.setString(1,
//                                            manufacturerList.getManufacturer());
//                                    ps1.setString(2, manufacturerList
//                                            .getManufacturerDescr());
//                                    ps1.setString(3, model.getModel());
//                                    ps1.setString(4, model.getModelDescr());
//                                    ps1.addBatch();
                                }
                            }
                        }
//                        WritableDatabase1.setTransactionSuccessful();
//                        WritableDatabase1.close();
//                        ps1.executeBatch();
//                        ps1.close();
//                        db.commit();
//                        db.close();

                    }
                }
//                writableDatabase.setTransactionSuccessful();
//                ps.executeBatch();
//                ps.close();
//                db.commit();
            }

            // 安全隐患,cmScShType_list 单独解析出来的对象所在的集合
            if (cmScShType_list.size() > 0) {
//                ps = db.prepareStatement(sql);
                // dictsequence,表示隐患类型的序号,将来根据序号决定显示的先后顺序

//                writableDatabase.beginTransaction();
                for (CmScShType_Dic cmScShType : cmScShType_list) {
                    // 将类型编码和描述插入到数据字典
					/*String sql = "insert into dictionaries (dictionaryDescr,dictionaryCode,dictsequence,parentID) "
							+ "values "
							+ "("

							+ "'"
							+ cmScShType.getCmScShTypeDescr()
							+ "'"

							+ ","
							+ "'"
							+ cmScShType.getCmScShType()
							+ "'"
							+ ","
							+ "'"
							+ cmScShType.getSequence()
							+ "'"
							+ ","
							+ "'cmScShType'" + ")";
					state.executeUpdate(sql);*/
                    writableDatabase.execSQL(sql, new Object[]{cmScShType.getCmScShTypeDescr(),
                            cmScShType.getCmScShType(), "cmScShType"});
//                    ps.setString(1, cmScShType.getCmScShTypeDescr());
//                    ps.setString(2, cmScShType.getCmScShType());
//                    ps.setString(3, "cmScShType");
//                    ps.addBatch();

                    ArrayList<CmScShItemInfo> cmScShItemInfos = cmScShType
                            .getCmScShItemInfo();
                    if (cmScShItemInfos.size() > 0) {

//                        Connection db2 = SQLiteData.openOrCreateDatabase(getActivity());
//                        db.setAutoCommit(false);
                        String sql1 = "insert into dic_cmScShItem_aj (cmScShType,cmScShTypeDescr,cmScShNO,cmScShItem,cmScShItemDescr )  values (?,?,?,?,?) ";
//                        PreparedStatement ps1 = db.prepareStatement(sql1);
//                        SQLiteDatabase WritableDatabase1 = db.getWritableDatabase();
//                        WritableDatabase1.beginTransaction();

                        for (CmScShItemInfo cmScShItemInfo : cmScShItemInfos) {
                            ArrayList<cmScShItem> cmScShItems = cmScShItemInfo
                                    .getCmScShItem();
                            if (cmScShItems.size() > 0) {
                                for (cmScShItem cmScShItem : cmScShItems) {

                                    // 将数据插入到 { 数据字典_表具-型号(dic_modelInfo_aj) }
									/*String sql1 = "insert into dic_cmScShItem_aj (cmScShType,cmScShTypeDescr,cmScShNO,cmScShItem,cmScShItemDescr ) "
											+ "values "
											+ "("

											+ "'"
											+ cmScShType.getCmScShType()
											+ "'"

											+ ","
											+ "'"
											+ cmScShType.getCmScShTypeDescr()
											+ "'"

											+ ","
											+ "'"
											+ cmScShItem.getSequence()
											+ "'"

											+ ","
											+ "'"
											+ cmScShItem.getCmScShItem()
											+ "'"

											+ ","
											+ "'"
											+ cmScShItem.getCmScShItemDescr()
											+ "'" + ")";
									state.executeUpdate(sql1);*/

                                    writableDatabase.execSQL(sql1, new Object[]{cmScShType.getCmScShType(),
                                            cmScShType.getCmScShTypeDescr(), cmScShItem.getSequence()
                                            , cmScShItem.getCmScShItem(), cmScShItem.getCmScShItemDescr()});
//                                    ps1.setString(1, cmScShType.getCmScShType());
//                                    ps1.setString(2, cmScShType.getCmScShTypeDescr());
//                                    ps1.setString(3, cmScShItem.getSequence());
//                                    ps1.setString(4, cmScShItem.getCmScShItem());
//                                    ps1.setString(5, cmScShItem.getCmScShItemDescr());
//                                    ps1.addBatch();
                                }
                            }
                        }
//                        WritableDatabase1.setTransactionSuccessful();
//                        WritableDatabase1.close();
//                        ps1.executeBatch();
//                        ps1.close();
//                        db.commit();
//                        db.close();
                    }
                }
//                ps.executeBatch();
//                ps.close();
//                db.commit();
                writableDatabase.setTransactionSuccessful();
            }

//			ps.close();
//            if(db!=null){
//                db.close();
//                db=null;
//            }


            return true;
        } catch (SQLiteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            writableDatabase.endTransaction();
//            if (state != null) {
//                try {
//                    state.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if (db != null) {
//                try {
//                    db.close();
//                } catch (SQLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if(ps!=null){
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }

            if (writableDatabase != null) {
                try {
                    writableDatabase.close();
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }

            }
            if (db != null) {
                try {
                    db.close();
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }

            }

            Message message = mHandle.obtainMessage();
            message.what = INSERT_OVER_Dic_for_down;
//            message.obj = overlayProgress;
            mHandle.sendMessage(message);

        }
        return false;
    }

    // 解析上传后返回信息

    public List<OutPut> getResult(String string) {
        List<OutPut> list = new ArrayList<OutPut>();
        // 创建集合 存放所有数据
        OutPut outPut = null;
        // 解析
        if (string != "") {
            Gson gson = new Gson();
            JSONObject json = null;
            String dataStr = null;
            // -- 基本信息f
            try {
                json = new JSONObject(string);
                String string1 = json.optString("soapenv:Envelope");
                json = new JSONObject(string1);
                String string2 = json.optString("soapenv:Body");
                json = new JSONObject(string2);
                String string3 = json.optString("CM_H_MRStUp");
                if (string3 == null || string3.equals("")) {
                    return list;
                }
                json = new JSONObject(string3);
                // dataStr = json.optString("output");
                // json = new JSONObject(dataStr);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JSONArray jsonArrayOutPut = (JSONArray) json.opt("output"); // --

            if (jsonArrayOutPut != null && jsonArrayOutPut.length() > 0) {

                for (int y = 0; y < jsonArrayOutPut.length(); y++) {

                    String soutput;
                    try {
                        soutput = jsonArrayOutPut.get(y).toString();
                        outPut = new OutPut();
                        if (!"".equals(soutput.toString()) && soutput != null)
                            outPut = gson.fromJson(soutput.toString(),
                                    OutPut.class);
                        list.add(outPut);
                        json = new JSONObject(soutput);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }
        return list;

    }

}
