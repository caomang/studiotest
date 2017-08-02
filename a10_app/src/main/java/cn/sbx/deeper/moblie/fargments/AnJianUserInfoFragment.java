package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.domian.CmScResType;
import cn.sbx.deeper.moblie.domian.CmScUserType;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.PerPhone;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.UploadcustInfo_AnJian;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DatabaseHelperInstance;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 审批组件详细界面
 *
 * @author terry.C
 */
@SuppressLint("ValidFragment")
public class AnJianUserInfoFragment extends BaseFragment implements
        OnClickListener, IRefreshButtonAndText {
    DatabaseHelperInstance databaseHelperInstance;
    Context mContext;
    private SinopecMenuModule menuModule;
    private String taskId, state_up;
    private IApproveBackToList backToList;
    private int targetContainer;
    private LayoutInflater layoutInflater;
    private LinearLayout view_gon_linearlayout;
    BaseActivity activity;
    private TextView user_no, user_name, user_detail_address;//, user_phone1
    private EditText m_phone, zc_phone, gs_phone;
    private ArrayList<PerPhone> phone_arrayList;
    private CmScUserType cmTSYH;
    private ArrayList<CmScUserType> commTeShuYongHu;
    private ArrayList<CmScUserType> cmCmScUserType_list;
    private CmScResType cmMrComm;
    protected String zhusuleixing = "";
    protected String teshuyonghu = "";
//    private Button call_phone;
    private ListView listview_for_callph;
    private CallphoneAdapter adapterForCall;
    private SQLiteDatabase instance;
    private View view;
    private RelativeLayout relativelayout_for_phone;
    private ProgressHUD progressHUD;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    try{
                        progressHUD.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    listview_for_callph=(ListView)view.findViewById(R.id.listview_for_callph);

                    adapterForCall = new CallphoneAdapter(mActivity);

                    listview_for_callph.setAdapter(adapterForCall);
                    user_no = (TextView) view.findViewById(R.id.user_no);
                    user_name = (TextView) view.findViewById(R.id.user_name);
//        user_phone1 = (TextView) view.findViewById(R.id.user_phone1);
//        call_phone = (Button) view.findViewById(R.id.call_phone);
//        user_phone1.setOnClickListener(this);
//        call_phone.setOnClickListener(this);
                    view_gon_linearlayout=(LinearLayout)view.findViewById(R.id.view_gon_linearlayout);
                    relativelayout_for_phone=(RelativeLayout) view
                            .findViewById(R.id.relativelayout_for_phone);
                    relativelayout_for_phone.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AnJianUserPhoneDetailFragment f = new AnJianUserPhoneDetailFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("id", taskId);
                            bundle.putSerializable("custInfo", custInfo);
                            f.setArguments(bundle);
                            ((ActivityInTab) getActivity()).navigateTo(f);
                        }
                    });
                    user_detail_address = (TextView) view
                            .findViewById(R.id.user_detail_address);
                    // user_type_code = (TextView) view.findViewById(R.id.user_type_code);
                    user_type = (TextView) view.findViewById(R.id.user_type);
                    user_zhusuleixing = (Spinner) view.findViewById(R.id.user_zhusuleixing);
                    user_teshuyonghu = (Spinner) view.findViewById(R.id.user_teshuyonghu);
                    user_type.setText(custInfo.getCmCustClDescr());
                    String[] options = new String[]{"居民", "商户"};

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                            R.layout.spinner_item, options);
                    // user_type
                    // .setBackgroundResource(R.drawable.ic_approve_spinner_background);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // user_type.setAdapter(adapter);

                    // 住宿类型
                    String[] opts = new String[commInfodata_list.size() + 1];
                    opts[0] = "请选择";
                    for (int i = 0; i < commInfodata_list.size(); i++) {
                        opts[i + 1] = commInfodata_list.get(i).getCmScResTypeDescr();
                    }

                    ArrayAdapter<String> adapter_serva_evaluate = new ArrayAdapter<String>(
                            mContext, R.layout.spinner_item, opts);
                    user_zhusuleixing
                            .setBackgroundResource(R.drawable.ic_approve_spinner_background);
                    adapter_serva_evaluate
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    user_zhusuleixing.setAdapter(adapter_serva_evaluate);

                    // 初始化
                    if (uploadcustInfo != null) {
                        String cmScResType = uploadcustInfo.getCmScResType();
                        // options2s.
                        for (int x = 0; x < commInfodata_list.size(); x++) {

                            if (commInfodata_list.get(x).getCmScResType()
                                    .equals(cmScResType)) {
                                user_zhusuleixing.setSelection(x + 1);
                            }
                        }
                    }

                    user_zhusuleixing
                            .setOnItemSelectedListener(new OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent,
                                                           View view, int position, long id) {
                                    if (position != 0) {
                                        zhusuleixing = commInfodata_list.get(position - 1)
                                                .getCmScResType();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // TODO Auto-generated method stub

                                }
                            });

                    // 特殊用户
                    String[] optstsyh = new String[cmCmScUserType_list.size() + 1];
                    optstsyh[0] = "请选择";
                    for (int i = 0; i < cmCmScUserType_list.size(); i++) {
                        optstsyh[i + 1] = cmCmScUserType_list.get(i).getCmScUserTypeDescr();
                    }

                    ArrayAdapter<String> adapter_serva_teshu = new ArrayAdapter<String>(
                            mContext, R.layout.spinner_item, optstsyh);
                    user_teshuyonghu
                            .setBackgroundResource(R.drawable.ic_approve_spinner_background);
                    adapter_serva_teshu
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    user_teshuyonghu.setAdapter(adapter_serva_teshu);
                    // 初始化
                    if (uploadcustInfo != null) {
                        String cmScUserType = uploadcustInfo.getCmScUserType();
                        // options2s.
                        for (int x = 0; x < cmCmScUserType_list.size(); x++) {

                            if (cmCmScUserType_list.get(x).getCmScUserType()
                                    .equals(cmScUserType)) {
                                user_teshuyonghu.setSelection(x + 1);
                            }
                        }
                    }

                    user_teshuyonghu
                            .setOnItemSelectedListener(new OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent,
                                                           View view, int position, long id) {
                                    if (position != 0) {
                                        teshuyonghu = cmCmScUserType_list.get(position - 1)
                                                .getCmScUserType();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // TODO Auto-generated method stub

                                }
                            });

                    user_no.setText(custInfo.getAccountId());
                    user_name.setText(custInfo.getEntityName());
                    user_detail_address.setText(custInfo.getCmMrAddress());
                    // user_type_code.setText(custInfo.getCustomerClass());

                    showAcconutPhone();

                    break;
                default:
                    break;


            }


        }
    };


    public AnJianUserInfoFragment(IApproveBackToList backToList,
                                  int targetContainer) {
        this.backToList = backToList;
        this.targetContainer = targetContainer;
    }

    public AnJianUserInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
        mContext = getActivity();

        databaseHelperInstance = new DatabaseHelperInstance();
        instance = databaseHelperInstance.getInstance(mContext);

        Bundle bundle = getArguments();
        if (bundle != null) {
            custInfo = (CustInfo_AnJian) bundle.getSerializable("custinfo");

            taskId = bundle.getString("id");
            state_up = bundle.getString("state_up");
        }
        Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
        btn_next.setText("确定");
        btn_next.setOnClickListener(this);
        if (state_up.equals("2")) {
            btn_next.setVisibility(View.INVISIBLE);
        } else {
            btn_next.setVisibility(View.VISIBLE);
        }
        tv_title = (TextView) mActivity.findViewById(R.id.tv_title);
        tv_title.setText("客户信息");
        layoutInflater = getActivity().getLayoutInflater();
        // 初始化用户电话

        progressHUD = AlertUtils.showDialog(mActivity, null, null, false);
        progressHUD.show();

        PollingUserPhoneDetailFragment_new userPhoneDetailFragmentnew = new PollingUserPhoneDetailFragment_new();
        phone_arrayList = userPhoneDetailFragmentnew.selectPhone(taskId,mContext);
        // 住宿类型
        commInfodata_list = getCommInfodata();
        cmCmScUserType_list = getCmScUserType();

        // 查询上传表,初始化各选项
        uploadcustInfo = getUpLoadDate();

//        new  Thread(new Runnable() {
//            @Override
//            public void run() {
//                PollingUserPhoneDetailFragment_new userPhoneDetailFragmentnew = new PollingUserPhoneDetailFragment_new();
//                phone_arrayList = userPhoneDetailFragmentnew.selectPhone(taskId,mContext);
//                // 住宿类型
//                commInfodata_list = getCommInfodata();
//                cmCmScUserType_list = getCmScUserType();
//
//                // 查询上传表,初始化各选项
//                uploadcustInfo = getUpLoadDate();
//            }
//        }).start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.cb_user_info_layout, container,
                false);


        return view;
    }

    // 住宿类型
    public ArrayList<CmScResType> getCommInfodata() {
        String selectDic = "select dictionaryCode,dictionaryDescr from dictionaries where parentID = 'cmScResType' ";
        Cursor DicSet = null;

        ArrayList<CmScResType> cmScResType_list = new ArrayList<CmScResType>();
//        DatabaseHelper conne = null;
//        SQLiteDatabase state = null;
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(selectDic,null);
            while (DicSet.moveToNext()) {
                cmMrComm = new CmScResType();
                cmMrComm.cmScResType = DicSet.getString(0);
                cmMrComm.cmScResTypeDescr = DicSet.getString(1);
                cmScResType_list.add(cmMrComm);
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
        return cmScResType_list;

    }

    ;

    // 特殊用户
    public ArrayList<CmScUserType> getCmScUserType() {
        String selectDic = "select dictionaryCode,dictionaryDescr from dictionaries where parentID = 'cmScUserType' ";
        Cursor DicSet = null;
        CmScResType cmMrComm = null;
        cmCmScUserType_list = new ArrayList<CmScUserType>();
//        DatabaseHelper conne = null;
//        SQLiteDatabase state = null;
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(selectDic,null);
            while (DicSet.moveToNext()) {
                cmTSYH = new CmScUserType();
                cmTSYH.cmScUserType = DicSet.getString(0);
                cmTSYH.cmScUserTypeDescr = DicSet.getString(1);
                cmCmScUserType_list.add(cmTSYH);
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
        return cmCmScUserType_list;

    }

    ;

    // 查询上传表
    public UploadcustInfo_AnJian getUpLoadDate() {
        String sql1 = "select distinct cmScResType,cmScUserType from uploadcustInfo_aj where accountId ='"
                + taskId
                + "' and cmSchedId = '"
                + custInfo.getCmSchedId()
                + "' ";
        Cursor DicSet = null;
//        DatabaseHelper conne = null;
//        SQLiteDatabase state = null;
        UploadcustInfo_AnJian uploadcustInfo = null;
        try {
//            conne = new DatabaseHelper(mContext);
//            state = conne.getWritableDatabase();
            DicSet = instance.rawQuery(sql1,null);
            while (DicSet.moveToNext()) {
                uploadcustInfo = new UploadcustInfo_AnJian();
                uploadcustInfo.cmScResType = DicSet.getString(0);
                uploadcustInfo.cmScUserType = DicSet.getString(1);
            }
            Message msg = Message.obtain();
            msg.what=100;
            mHandler.sendMessage(msg);



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
        return uploadcustInfo;

    }

    ;

    private void showAcconutPhone() {
        if (phone_arrayList != null && phone_arrayList.size() > 0) {
            listview_for_callph.setVisibility(View.VISIBLE);
            view_gon_linearlayout.setVisibility(View.GONE);
            adapterForCall.getDataForItem(phone_arrayList);
            adapterForCall.notifyDataSetChanged();
//            for (int a = 0; a < phone_arrayList.size(); a++) {
//                PerPhone phone = phone_arrayList.get(a);
//                if (phone.getPhoneType().equals("HOME")
//                        && !TextUtils.isEmpty(phone.getPhone())) {
//                    user_phone1.setText("住宅电话");
//                    user_phone1.setText(phone.getPhone());
//                    user_phone1.setTextColor(Color.BLACK);
//                    break;
//                } else if (phone.getPhoneType().equals("CELL")
//                        && !TextUtils.isEmpty(phone.getPhone())) {
//                    user_phone1.setText("移动电话");
//                    user_phone1.setText(phone.getPhone());
//                    user_phone1.setTextColor(Color.BLACK);
//                    break;
//                } else if (phone.getPhoneType().equals("BUSN")
//                        && !TextUtils.isEmpty(phone.getPhone())) {
//                    user_phone1.setText("公司电话");
//                    user_phone1.setText(phone.getPhone());
//                    user_phone1.setTextColor(Color.BLACK);
//                    break;
//                } else {
//                    // 尚无电话 点击添加
//                    user_phone1.setText("尚无电话 点击添加");
//                    user_phone1.setTextColor(Color.GRAY);
//                }
//            }
        } else {

            listview_for_callph.setVisibility(View.GONE);
            view_gon_linearlayout.setVisibility(View.VISIBLE);

            // 尚无电话 点击添加
//            user_phone1.setText("尚无电话 点击添加");
//            user_phone1.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_next:
                try {
                    saveData(taskId);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                }
                backPrecious();
                Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
            /*
			 * String string = updateUserInfo(taskId,
			 * m_phone.getText().toString() .trim(),
			 * zc_phone.getText().toString().trim(), gs_phone
			 * .getText().toString().trim()); if (string.equals("m")) {
			 * Toast.makeText(getActivity(), "手机号码格式不正确", 1).show(); } else if
			 * (string.equals("zc")) { Toast.makeText(getActivity(),
			 * "请输入住宅电话 例如：010-88888888", 1) .show();
			 * 
			 * } else if (string.equals("gs")) { Toast.makeText(getActivity(),
			 * "请输入公司电话 例如：010-88888888", 1) .show(); } else {
			 * Toast.makeText(getActivity(), "提交成功", 1).show();
			 * selectList(taskId); }
			 */
                break;

//            case R.id.user_phone1:
//
//                AnJianUserPhoneDetailFragment f = new AnJianUserPhoneDetailFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("id", taskId);
//                bundle.putSerializable("custInfo", custInfo);
//                f.setArguments(bundle);
//                ((ActivityInTab) getActivity()).navigateTo(f);
//
//                break;

//            case R.id.call_phone:
//                String trim = user_phone1.getText().toString().trim();
//                if(trim!=null&&trim!=""&&!trim.equals("尚无电话 点击添加")){
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:" + trim));
//                    //开启系统拨号器
//                    startActivity(intent);
//                }else {
//                    Toast.makeText(mContext,"没有可拨打的电话",Toast.LENGTH_SHORT).show();
//                }



//                break;

        }
    }

    // 查询待办数据
//    public DatabaseHelper db;// 操作数据库的工具类
    StringBuilder sb;
    TextView user_type;
    private Spinner user_teshuyonghu, user_zhusuleixing;
    private TextView tv_title;
    private CustInfo_AnJian custInfo;
    private ArrayList<CmScResType> commInfodata_list;
    private UploadcustInfo_AnJian upCustInfo;
    private UploadcustInfo_AnJian uploadcustInfo;
    private String wZ = "";
    private String cZ = "";
//    private DatabaseHelper conne;

    public void selectList(String user_id) {
        String sql = "select distinct * from anjian_data where user_id=" + "'"
                + user_id + "'";
//        String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//        db = new DatabaseHelper(mContext);
        // 查询数据返回游标对象
        Cursor c = instance.rawQuery(sql, null);
        if(c.moveToFirst()){
            while (c.moveToNext()) {
                // 通过游标对象获取值

                user_no.setText(c.getString(0));
                user_name.setText(c.getString(1));
                if (c.getString(3).contains("手机")) {
                    m_phone.setText(c.getString(3).substring(3,
                            c.getString(3).length()));
                } else {
                    m_phone.setText(c.getString(3));
                }
                zc_phone.setText(c.getString(46));
                gs_phone.setText(c.getString(47));
            }
        }

        c.close();
//        db.close();
    }

    // 更新电话号码
    public String updateUserInfo(String id, String m_phone, String zc_phone,
                                 String gs_phone) {
        if (isMobileNO(m_phone)) {
            if (isPhoneNumberValid(zc_phone)) {
                if (isPhoneNumberValid(gs_phone)) {
                    String sql = "update anjian_data set user_phone=" + m_phone
                            + ", zc_phone=" + "\"" + zc_phone + "\""
                            + " ,gs_phone=" + "\"" + gs_phone + "\""
                            + " where user_id=" + id + "";
                    String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
                    // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//                    db = new DatabaseHelper(mContext);
                    // 查询数据返回游标对象
                    instance.execSQL(sql);
//                    db.close();
                    return "true";
                } else {
                    return "gs";
                }
            } else {
                return "zc";
            }
        } else {
            return "m";
        }

    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        Pattern patternPhone = Pattern.compile(
                "^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcherPhone = patternPhone.matcher(phoneNumber);
        boolean booleanPhone = matcherPhone.matches();
        return booleanPhone;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    // 保存数据,首先判断要保存的数据和 客户表中的数据是否一致,如不同 在进行保存
    public void saveData(String userid) throws SQLException {

        if (custInfo.getCmScResType() != null
                && custInfo.getCmScResType().equals("null")
                && !custInfo.getCmScResType().equals(zhusuleixing)) {
            wZ = zhusuleixing;
        } else {
            wZ = zhusuleixing;
        }

        if (custInfo.getCmScUserType() != null
                && custInfo.getCmScUserType().equals("null")
                && !custInfo.getCmScUserType().equals(teshuyonghu)) {
            cZ = teshuyonghu;
        } else {
            cZ = teshuyonghu;
        }

        String sql_isSave = "select * from uploadcustInfo_aj where  accountId='"
                + taskId
                + "' and cmSchedId = '"
                + custInfo.getCmSchedId()
                + "'";
        Cursor executeQuery = null;
//        conne = new DatabaseHelper(mContext);

//        SQLiteDatabase state = conne.getWritableDatabase();

        executeQuery = instance.rawQuery(sql_isSave,null);

        if (!executeQuery.moveToNext()) {
            // 表中不存在该用户信息,插入

            String sql = "insert into uploadcustInfo_aj (cmSchedId,accountId,cmScResType,cmScUserType) "
                    + "values ("
                    + "'"
                    + custInfo.getCmSchedId()
                    + "',"
                    + "'"
                    + taskId + "'," + "'" + wZ + "'," + "'" + cZ + "')";

            instance.execSQL(sql);
        } else {
            // 表中存在该用户,更新

            String sql = "update  uploadcustInfo_aj set cmScResType= '" + wZ
                    + "', cmScUserType= '" + cZ + "' where  accountId='"
                    + taskId + "' and cmSchedId = '" + custInfo.getCmSchedId()
                    + "'";
            instance.execSQL(sql);

        }
        if (executeQuery != null) {
            try {
                executeQuery.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//        if (state != null) {
//            try {
//                state.close();
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        if (conne != null) {
//            try {
//                conne.close();
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void refreshButtonAndText() {
        // TODO Auto-generated method stub
        super.refreshButtonAndText();
        tv_title.setText("客户信息");
        Button btn_next = (Button) getActivity().findViewById(R.id.btn_next);
        btn_next.setText("确定");
        btn_next.setOnClickListener(this);
        if (state_up.equals("2")) {
            btn_next.setVisibility(View.INVISIBLE);
        } else {
            btn_next.setVisibility(View.VISIBLE);
        }

        // 重新加载电话
        // 初始化用户电话
        PollingUserPhoneDetailFragment_new userPhoneDetailFragmentnew = new PollingUserPhoneDetailFragment_new();
        phone_arrayList = userPhoneDetailFragmentnew.selectPhone(taskId,mContext);

        showAcconutPhone();

    }

    class CallphoneAdapter extends BaseAdapter{
        private Context mContext;
        private ArrayList<PerPhone> mList;
        private LayoutInflater layoutInflater;;

        public CallphoneAdapter(Context context){
            this.mContext=context;

            layoutInflater = LayoutInflater.from(mContext);
        }

        private void getDataForItem(ArrayList<PerPhone> phone_arrayList){
            this.mList=phone_arrayList;
        }

        @Override
        public int getCount() {
            return mList==null?0:mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=layoutInflater.inflate(R.layout.call_phone,null);
                holder.user_phone1_call=(TextView)convertView.findViewById(R.id.user_phone1_call);
                holder.call_phone_call=(Button) convertView.findViewById(R.id.call_phone_call);
                holder.image_for_next_call=(ImageView) convertView.findViewById(R.id.image_for_next_call);
                convertView.setTag(holder);

            }else{
                holder=(ViewHolder)convertView.getTag();
            }

           final PerPhone perPhone = mList.get(position);

            if(TextUtils.isEmpty(perPhone.getPhone())){
                holder.user_phone1_call.setText("尚无电话 点击添加");
                holder.user_phone1_call.setTextColor(Color.GRAY);
            }else {
                holder.user_phone1_call.setText(perPhone.getPhone());
                holder.user_phone1_call.setTextColor(Color.BLACK);

            }

            holder.user_phone1_call.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnJianUserPhoneDetailFragment f = new AnJianUserPhoneDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", taskId);
                    bundle.putSerializable("custInfo", custInfo);
                    f.setArguments(bundle);
                    ((ActivityInTab) getActivity()).navigateTo(f);
                }
            });



            holder.call_phone_call.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String trim = perPhone.getPhone();
                    if(trim!=null&&trim!=""&&!trim.equals("尚无电话 点击添加")){
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + trim));
                        //开启系统拨号器
                        startActivity(intent);
                    }else {
                        Toast.makeText(mContext,"没有可拨打的电话",Toast.LENGTH_SHORT).show();
                    }
                }
            });


            return convertView;
        }

        class ViewHolder{
            TextView user_phone1_call;
            Button call_phone_call;
            ImageView image_for_next_call;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
