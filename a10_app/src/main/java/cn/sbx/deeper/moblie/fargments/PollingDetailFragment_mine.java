package cn.sbx.deeper.moblie.fargments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sunboxsoft.monitor.utils.PerfUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.sbx.deeper.moblie.activity.ActivityInTab;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.domian.CustInfo;
import cn.sbx.deeper.moblie.domian.NoticePayment;
import cn.sbx.deeper.moblie.domian.OutPut;
import cn.sbx.deeper.moblie.domian.UploadcustInfo;
import cn.sbx.deeper.moblie.interfaces.IApproveBackToList;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.MobilePrint;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

public class PollingDetailFragment_mine extends BaseFragment implements
        OnClickListener, IRefreshButtonAndText {

    private ExpandableListView elv_details_mine;
    private Connection db;
    private CustInfo custInfo;
    private String[] strs;
    private IApproveBackToList backToList;
    private int targetContainer;
    public String detail = "PollingDetailFragment_mine";
    int childtype;
    private String taskId;
    // private BaseHolder baseHolder;
    // private CaoBiaoHolder caoBiaoHolder;
    private Button btn_next;
    private LinearLayout approve_detail, approve_detail2;
    private String read_num;
    private ImageView iv_pic1, iv_pic2;
    private TextView tv_caobiao_detail_read_num, tv_caobiao_detail_fujian,
            tv_caobiao_detail_consume, tv_title1, tv_title2,
            tv_caobiao_detail_air_measure, tv_caobiao_detail_standard;

    public EditText et_readdata, et_remark;

    private Spinner spinner_type, spinner_isprint, spinner_serve_isgood;
    private Button btn_print;
    private TextView tv_caobiao_detail_order;
    private TextView tv_caobiao_detail_client_num;
    private TextView tv_caobiao_detail_name;
    private TextView tv_caobiao_detail_client_address;
    private String[] options;
    private String[] options1s;
    private String[] options2s;
    private String isprint;
    private String isGood;
    private String type;
    private String[] mrcoom_codes;
    private String[] readType_codes;
    private String print;
    private UploadcustInfo uploadcustInfo;
    private ResultSet cursor_uploadcustInfos;
    private int approve_detail_Height;
    private int approve_detail_Height2;
    private boolean isOpen = true;
    private boolean isOpen2 = true;
    private NoticePayment notice;
    MobilePrint mp;

    // public PollingDetailFragment_mine(IApproveBackToList backToList,
    // int targetContainer) {
    // this.backToList = backToList;
    // this.targetContainer = targetContainer;
    // }

    // public PollingDetailFragment_mine(IApproveBackToList backToList,
    // int targetContainer) {
    // this.backToList = backToList;
    // this.targetContainer = targetContainer;
    // }

    public PollingDetailFragment_mine() {
    }

    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        btn_next = (Button) getActivity().findViewById(R.id.btn_next);
        btn_next.setVisibility(View.VISIBLE);
        btn_next.setText("保存");

        tv_title = (TextView) mActivity.findViewById(R.id.tv_title);


        btn_next.setOnClickListener(PollingDetailFragment_mine.this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            taskId = bundle.getString("id");
        }

		strs = new String[] { "基本信息", "抄表信息" };
		try {
			db = SQLiteData.openOrCreateDatabase();

            state = db.createStatement();
            String sql = "select  * from custInfo where accountId = " + "'"
                    + taskId + "'";
            // c = db.rawQuery(sql, null);
            ResultSet c = state.executeQuery(sql);
            while (c.next()) {
                custInfo = new CustInfo();

                custInfo.setSchedInfoID(c.getString(1));
                custInfo.setMeterReadCycleRouteSequence(c.getString(2));
                custInfo.setAccountId(c.getString(3));
                custInfo.setEntityName(c.getString(4));
                custInfo.setCustomerClass(c.getString(5));
                custInfo.setCmCustClDescr(c.getString(6));
                custInfo.setCmMrAddress(c.getString(7));
                custInfo.setCmMrDistrict(c.getString(8));
                custInfo.setCmMrStreet(c.getString(9));
                custInfo.setCmMrCommunity(c.getString(10));
                custInfo.setCmMrBuilding(c.getString(11));
                custInfo.setCmMrUnit(c.getString(12));
                custInfo.setCmMrRoomNum(c.getString(13));
                custInfo.setSpMeterHistoryId(c.getString(14));
                custInfo.setMeterConfigurationId(c.getString(15));
                custInfo.setCmMrMtrBarCode(c.getString(16));
                custInfo.setFullScale(c.getString(17));
                custInfo.setCmMrAvgVol(c.getString(18));
                custInfo.setRateSchedule(c.getString(19));
                custInfo.setCmRsDescr(c.getString(20));
                custInfo.setCmMrLastBal(c.getString(21));
                custInfo.setCmMrOverdueAmt(c.getString(22));

                custInfo.setCmMrLastMrDttm(c.getString(24));
                custInfo.setReadType(c.getString(25));
                custInfo.setCmMrLastMr(c.getString(26));
                custInfo.setCmMrLastVol(c.getString(27));
                custInfo.setCmMrLastDebt(c.getString(28));
                custInfo.setCmMrLastSecchkDt(c.getString(29));
                custInfo.setCmMrRemark(c.getString(30));
                custInfo.setCmMrState(c.getString(31));
                custInfo.setCmMrDate(c.getString(32));

            }
            // 该用户已上传 不可以修改 隐藏保存按钮
            if (custInfo.getCmMrState().equals("2")) {
                btn_next.setVisibility(View.INVISIBLE);
            }
            // 查询上传表
            String historyId = custInfo.getSpMeterHistoryId();

            String sql1 = "select spMeterHistoryId,meterConfigurationId,cmMr,readType,cmMrDttm,cmMrRefVol,cmMrRefDebt,cmMrNotiPrtd,cmMrCommCd,cmMrRemark from uploadcustInfo where spMeterHistoryId ="
                    + "\"" + historyId + "\"" + " ";

            // cursor_uploadcustInfos = db.rawQuery(sql1, null);
            cursor_uploadcustInfos = state.executeQuery(sql1);

            while (cursor_uploadcustInfos.next()) {
                uploadcustInfo = new UploadcustInfo();

                uploadcustInfo.setCmMr(cursor_uploadcustInfos.getString(3));
                uploadcustInfo.setReadType(cursor_uploadcustInfos.getString(4));
                uploadcustInfo.setCmMrDttm(cursor_uploadcustInfos.getString(5));
                uploadcustInfo.setCmMrRefVol(cursor_uploadcustInfos
                        .getString(6));

                uploadcustInfo.setCmMrNotiPrtd(cursor_uploadcustInfos
                        .getString(8));
                uploadcustInfo.setCmMrCommCd(cursor_uploadcustInfos
                        .getString(9));
                uploadcustInfo.setCmMrRemark(cursor_uploadcustInfos
                        .getString(10));

            }
            cursor_uploadcustInfos.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (state != null) {
                try {
                    state.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        mp = new MobilePrint(getActivity());
        // 判断打印及是否连接
        if (!mp.isConnected) {
            // 判断是否已和打印设备配对
            if (!mp.GetBluetoothData()) {
                // 没有 ,连接蓝牙设备
                mp.initBluetooth();
            }
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View newView = View.inflate(getActivity(),
                R.layout.layout_approve_detail_mine_new, null);

        // 标题tv_caobiao_detail_consume
        tv_title1 = (TextView) newView.findViewById(R.id.tv_title1);
        tv_title2 = (TextView) newView.findViewById(R.id.tv_title2);

        // 基本信息收缩图片
        iv_pic1 = (ImageView) newView.findViewById(R.id.iv_pic1);
        // 客户信息收缩图片
        iv_pic2 = (ImageView) newView.findViewById(R.id.iv_pic2);
        // 添加单击事件
        iv_pic1.setOnClickListener(this);
        iv_pic2.setOnClickListener(this);

        btn_print = (Button) newView.findViewById(R.id.btn_print);
        btn_print.setVisibility(View.GONE);
        btn_print.setOnClickListener(this);

        tv_caobiao_detail_order = (TextView) newView
                .findViewById(R.id.tv_caobiao_detail_order);
        tv_caobiao_detail_client_num = (TextView) newView
                .findViewById(R.id.tv_caobiao_detail_client_num);
        tv_caobiao_detail_name = (TextView) newView
                .findViewById(R.id.tv_caobiao_detail_name);
        tv_caobiao_detail_client_address = (TextView) newView
                .findViewById(R.id.tv_caobiao_detail_client_address);

        tv_caobiao_detail_read_num = (TextView) newView
                .findViewById(R.id.tv_caobiao_detail_read_num);
        tv_caobiao_detail_air_measure = (TextView) newView
                .findViewById(R.id.tv_caobiao_detail_air_measure);
        // tv_caobiao_detail_readertime = (TextView) newView
        // .findViewById(R.id.tv_caobiao_detail_readertime);
        tv_caobiao_detail_consume = (TextView) newView
                .findViewById(R.id.tv_caobiao_detail_consume);

        // tv_caobiao_detail_standard = (TextView) newView
        // .findViewById(R.id.tv_caobiao_detail_standard);

        tv_caobiao_detail_consume.setOnClickListener(this);

        // tv_caobiao_detail_fujian = (TextView) newView
        // .findViewById(R.id.tv_caobiao_detail_fujian);

        et_readdata = (EditText) newView.findViewById(R.id.et_readdata);
        et_readdata.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String readDate = et_readdata.getText().toString().trim();
                String readNum = tv_caobiao_detail_read_num.getText().toString()
                        .trim();

                // 获取本期气量,当edittext 未失去焦点时为气量赋值
                float b;// 本期气量
                // if (et_readdata.hasFocus()) {
                if(TextUtils.isEmpty(readDate)){
                    b =-1;
                }else{
                    b = Float.parseFloat(readDate) - Float.parseFloat(readNum);
                }



                if (b >= 0) {
                    DecimalFormat df = new DecimalFormat("0.00");// 两位小数
                    String valueOf = String.valueOf(df.format(b));
                    tv_caobiao_detail_air_measure.setText(valueOf);
                    float money = b * (float) 2.56;
                    String valueOf_money = String.valueOf(df.format(money));
                    tv_caobiao_detail_consume.setText(valueOf_money);

                }else{
                    tv_caobiao_detail_air_measure.setText("000");
                    tv_caobiao_detail_consume.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_remark = (EditText) newView.findViewById(R.id.et_remark);

        approve_detail = (LinearLayout) newView
                .findViewById(R.id.approve_detail);
        approve_detail2 = (LinearLayout) newView
                .findViewById(R.id.approve_detail2);

        spinner_type = (Spinner) newView.findViewById(R.id.spinner_type);
        // spinner_isprint = (Spinner)
        // newView.findViewById(R.id.spinner_isprint);
        spinner_serve_isgood = (Spinner) newView
                .findViewById(R.id.spinner_serve_isgood);
        /*
		 * // 设置当前时间 SimpleDateFormat dateFormat = new SimpleDateFormat(
		 * "yyyy-MM-dd HH:mm:ss"); String date = dateFormat.format(new
		 * java.util.Date());
		 */
        // tv_caobiao_detail_readertime.setText(date);

        tv_caobiao_detail_air_measure = (TextView) newView
                .findViewById(R.id.tv_caobiao_detail_air_measure);

        // 显示上次输入的内容，如果有内容进行设置
        // if (!(cursor_uploadcustInfos.getMetaData().getColumnCount() == 0)) {
        if (uploadcustInfo != null) {

            if (!TextUtils.isEmpty(uploadcustInfo.getCmMr())
                    && custInfo.getCmMrState().equals("2")) {
                // 已上传数据
                et_readdata.setText(uploadcustInfo.getCmMr());
                et_readdata.setEnabled(false);
                et_readdata.setTextColor(Color.GRAY);
            } else if (!TextUtils.isEmpty(uploadcustInfo.getCmMr())) {
                et_readdata.setText(uploadcustInfo.getCmMr());

            }
            if (!TextUtils.isEmpty(uploadcustInfo.getCmMrRefVol())) {
                tv_caobiao_detail_air_measure.setText(uploadcustInfo
                        .getCmMrRefVol());
            }

            if (!TextUtils.isEmpty(uploadcustInfo.getCmMrRemark())
                    && custInfo.getCmMrState().equals("2")) {
                et_remark.setText(uploadcustInfo.getCmMrRemark());

                et_remark.setEnabled(false);
                et_remark.setTextColor(Color.GRAY);
            } else if (!TextUtils.isEmpty(uploadcustInfo.getCmMrRemark())) {
                et_remark.setText(uploadcustInfo.getCmMrRemark());
            }
        }

        tv_caobiao_detail_read_num.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PollingUpDateReadFragment pollingUpDateReadFragment = new PollingUpDateReadFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Last_save", custInfo);
                pollingUpDateReadFragment.setArguments(bundle);
                ((ActivityInTab) getActivity())
                        .navigateTo(pollingUpDateReadFragment);
            }
        });

        // tv_caobiao_detail_fujian.setOnClickListener(this);

        spinner_serve_isgood
                .setBackgroundResource(R.drawable.ic_approve_spinner_background);
        // 从数据库库中获取全部文字，进行显示

		try {
			db = SQLiteData.openOrCreateDatabase();
			state = db.createStatement();
			String str = "select dictionaryDescr,dictionaryCode from dictionaries where parentID = "
					+ "'cmMrComm'" + "";

            // Cursor cursor = db.rawQuery(str, null);

            ResultSet cursor = state.executeQuery(str);
            // int count = cursor.getCount();
            // cursor.moveToFirst();
            // String string = cursor.getString(0);

            cursor.last();

            options = new String[cursor.getRow()];
            mrcoom_codes = new String[cursor.getRow()];
            cursor.beforeFirst();
            int i = 0;
            while (cursor.next()) {
                options[i] = cursor.getString(1);
                // 将编码放入数组
                mrcoom_codes[i] = cursor.getString(2);
                i++;
            }

            //
            // options[0] = "非常满意";
            // options[1] = "满意";
            // options[2] = "不满意";

            ArrayAdapter<String> isGood_Adapter = new ArrayAdapter<String>(
                    getActivity(), R.layout.spinner_item, options);
            isGood_Adapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_serve_isgood.setAdapter(isGood_Adapter);

            // 设置上次选择项
            // if (!(cursor_uploadcustInfos.getMetaData().getColumnCount() ==
            // 0)) {
            if (uploadcustInfo != null) {
                String cmMrCoom = uploadcustInfo.getCmMrCommCd();
                // options2s.
                for (int x = 0; x < mrcoom_codes.length; x++) {

                    if (mrcoom_codes[x].equals(cmMrCoom)) {
                        spinner_serve_isgood.setSelection(x);
                    }
                }
            }
            spinner_serve_isgood
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {
                            // 获取描述对应的编码

                            isGood = mrcoom_codes[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

            // spinner_isprint
            // .setBackgroundResource(R.drawable.ic_approve_spinner_background);

            options1s = new String[2];
            options1s[0] = "是";
            options1s[1] = "否";
            ArrayAdapter<String> isPrint_Adapter = new ArrayAdapter<String>(
                    getActivity(), R.layout.spinner_item, options1s);
            isPrint_Adapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // spinner_isprint.setAdapter(isPrint_Adapter);

            // 上次选项

            // if (!(uploadcustInfo == null)) {
            //
            // String notiPrtd = uploadcustInfo.getCmMrNotiPrtd();
            // if (notiPrtd.equals("Y")) {
            // spinner_isprint.setSelection(0);
            // } else {
            //
            // spinner_isprint.setSelection(1);
            // }
            // }

            // spinner_isprint
            // .setOnItemSelectedListener(new OnItemSelectedListener() {
            //
            // @Override
            // public void onItemSelected(AdapterView<?> parent,
            // View view, int position, long id) {
            //
            // isprint = options1s[position];
            // }
            //
            // @Override
            // public void onNothingSelected(AdapterView<?> parent) {
            //
            // }
            // });

            spinner_type
                    .setBackgroundResource(R.drawable.ic_approve_spinner_background);

            String sql1 = "select dictionaryDescr,dictionaryCode from dictionaries where parentID = "
                    + "'readType'" + "";

            // Cursor cursor1 = db.rawQuery(sql1, null);
            ResultSet cursor1 = state.executeQuery(sql1);
            cursor1.last();

            options2s = new String[cursor1.getRow()];
            readType_codes = new String[cursor1.getRow()];
            cursor1.beforeFirst();
            int a = 0;
            while (cursor1.next()) {
                options2s[a] = cursor1.getString(1);
                readType_codes[a] = cursor1.getString(2);
                if (readType_codes[a].equals("60")) {
                    meRen = a;
                }
                a++;
            }

            // options2s[0] = "入户";
            // options2s[1] = "估读";
            // options2s[2] = "自抄";
            ArrayAdapter<String> data_Adapter = new ArrayAdapter<String>(
                    getActivity(), R.layout.spinner_item, options2s);
            data_Adapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_type.setAdapter(data_Adapter);
            spinner_type.setSelection(meRen);
            // 回显
            if (uploadcustInfo != null) {
                String type1 = uploadcustInfo.getReadType();
                // options2s.
                for (int x = 0; x < readType_codes.length; x++) {

                    if (readType_codes[x].equals(type1)) {
                        spinner_type.setSelection(x);
                    }
                }
            } else {
                // 默认选中 普通选项
            }

            spinner_type
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {

                            type = readType_codes[position];
                            // 让读数框失去焦点
                            // et_readdata.setEnabled(false);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (cursor_uploadcustInfos != null) {

                try {
                    cursor_uploadcustInfos.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (state != null) {

                try {
                    state.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (db != null) {

                try {
                    db.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        return newView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置适配器
        // MyAdapter expandableListAdapter = new MyAdapter();
        //
        // elv_details_mine.setAdapter(expandableListAdapter);
        // 设置默认展开
        // for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
        //
        // elv_details_mine.expandGroup(i);
        //
        // }

        tv_title1.setText("基本信息");
        tv_title2.setText("抄表信息");
        TextPaint tp = tv_title1.getPaint();
        tp.setFakeBoldText(true);
        TextPaint tp2 = tv_title2.getPaint();
        tp2.setFakeBoldText(true);

        tv_caobiao_detail_name.setText(custInfo.getEntityName());
        tv_caobiao_detail_client_num.setText(custInfo.getAccountId());
        tv_caobiao_detail_client_address.setText(custInfo.getCmMrCommunity()
                + "#" + custInfo.getCmMrBuilding() + "-"
                + custInfo.getCmMrRoomNum());
        tv_caobiao_detail_read_num.setText(custInfo.getCmMrLastMr());

        // 监听读数输入框数据
        et_readdata.addTextChangedListener(watcher);

        // 失去焦点处理
        et_readdata.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // read_num = et_readdata.getText().toString();

                // 上期读数
                float LastMr = Float.parseFloat(custInfo.getCmMrLastMr());
				/*
				 * // 获取本期读数 String num = et_readdata.getText().toString();
				 */
                if (!TextUtils.isEmpty(read_num)) {
                    float read_num1 = Float.parseFloat(read_num);
                    float measure = read_num1 - LastMr;
                    // 计算气费 单价 2.56
                    float money = measure * (float) 2.56;

                    if (measure >= 0) {
                        // 本次读数大于上次读数0.7
                        // 计算本期气量
                        DecimalFormat df = new DecimalFormat("0.00");// 两位小数
                        String valueOf = String.valueOf(df.format(measure));
                        tv_caobiao_detail_air_measure.setText(valueOf);
                        String valueOf_money = String.valueOf(df.format(money));
                        tv_caobiao_detail_consume.setText(valueOf_money);
                    } else {

                        // tv_caobiao_detail_air_measure.setText("0.00");
                        Toast.makeText(mActivity, "本次气量小于上次气量", Toast.LENGTH_SHORT).show();
                        // 读数框重新获得焦点
                    }

                    // 当下次失去焦点 但并没有改变输入数据时 ，不需要弹出吐司
                    read_num = null;
                }

            }
        });

        // 设置点击事件
        tv_caobiao_detail_client_num.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PollingUserPhoneFragment phoneFragment = new PollingUserPhoneFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("mCustInfo", custInfo);
                phoneFragment.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(phoneFragment);

            }
        });

        // 记录基本 和抄表界面的原始高度
        // approve_detail.measure(0, 0);
        // approve_detail2.measure(0, 0);
        // approve_detail_Height = approve_detail.getMeasuredHeight();
        // approve_detail_Height2 = approve_detail2.getMeasuredHeight();
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // 上期读数
            float LastMr = Float.parseFloat(custInfo.getCmMrLastMr());
			/*
			 * // 获取本期读数 String num = et_readdata.getText().toString();
			 */

            read_num = et_readdata.getText().toString();

        }
    };
    private String isPrint2 = "N";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:// 单击 按钮，将数据保存至上传数据库
                // 设置当前时间
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                time = dateFormat.format(new Date());
                // 读数
                String readDate = et_readdata.getText().toString().trim();
                String readNum = tv_caobiao_detail_read_num.getText().toString()
                        .trim();

                // 获取本期气量,当edittext 未失去焦点时为气量赋值
                float CurrentCapacity;
                float b;// 本期气量
                // if (et_readdata.hasFocus()) {

                if (TextUtils.isEmpty(readDate)) {
                    Toast.makeText(getActivity(), "请输入读数", Toast.LENGTH_SHORT).show();
                    break;
                }
                b = Float.parseFloat(readDate) - Float.parseFloat(readNum);

                if (b >= 0) {
                    DecimalFormat df = new DecimalFormat("0.00");// 两位小数
                    String valueOf = String.valueOf(df.format(b));
                    tv_caobiao_detail_air_measure.setText(valueOf);
                    float money = b * (float) 2.56;
                    String valueOf_money = String.valueOf(df.format(money));
                    tv_caobiao_detail_consume.setText(valueOf_money);

                    CurrentCapacity = b;
                } else {
                    Toast.makeText(mActivity, "本次气量不得小于上次气量", Toast.LENGTH_SHORT).show();
                    break;
                }
                // } else {
			/*
			 * CurrentCapacity = Float
			 * .parseFloat(tv_caobiao_detail_air_measure.getText() .toString());
			 */
                // }

                // String air_measure = tv_caobiao_detail_air_measure.getText()
                // .toString();
                // 抄表日期
                // String time = tv_caobiao_detail_readertime.getText().toString();

                // 气费
                String money = tv_caobiao_detail_consume.getText().toString();

                String beizhu = et_remark.getText().toString();
                // 保存至上传数据库
                // path =
                // "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/a10_db.db";

                String historyId = custInfo.getSpMeterHistoryId();
                String meterConfigurationId = custInfo.getMeterConfigurationId();

                // if (isprint.equals("是")) {
                // print = "\"Y\"";
                // } else if (isprint.equals("否")) {
                // print = "\"N\"";
                // }

                if (TextUtils.isEmpty(readDate)) {
                    Toast.makeText(getActivity(), "请输入读数", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    if (beizhu.equals("")) {
                        beizhu = " ";
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
                    SimpleDateFormat Date = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    // 每次创建初始化对象
                    notice = new NoticePayment();
                    // 保存打印数据
                    notice.userNo = tv_caobiao_detail_client_num.getText()
                            .toString().trim();
                    notice.path = tv_caobiao_detail_client_address.getText()
                            .toString().trim();
                    notice.accounting = formatter.format(new Date());
                    notice.theReadings = SubString(tv_caobiao_detail_read_num
                            .getText().toString().trim());
                    notice.currentReadings = SubString(et_readdata.getText()
                            .toString().trim());
                    notice.currentCapacity = SubString(tv_caobiao_detail_air_measure
                            .getText().toString().trim());
                    notice.currentGasFee = tv_caobiao_detail_consume.getText()
                            .toString().trim();

				try {
					db = SQLiteData.openOrCreateDatabase();
					state = db.createStatement();

                        // 读取阶梯
                        String sql_Ladder = "select firstGasPrice,firstTolerance,firstGasFee,secondGasPrice,secondTolerance,secondGasFee,thirdGasPrice,thirdTolerance,thirdGasFee from UserLadder where  UserNo="
                                + "\"" + notice.userNo + "\"" + "";
                        // Cursor cLadder = db.rawQuery(sql_Ladder, null);
                        ResultSet cLadder = state.executeQuery(sql_Ladder);

                        boolean IsLadder = false;
                        while (cLadder.next()) {
                            notice.firstGasPrice = cLadder.getString(1);
                            notice.firstGasVolume = SubString(cLadder.getString(2));
                            notice.firstGasCharge = cLadder.getString(3);
                            IsLadder = true;
                        }
                        if (IsLadder == false) {
                            notice.firstGasPrice = "2.56元/方";
                            notice.firstGasVolume = SubString(tv_caobiao_detail_air_measure
                                    .getText().toString().trim()); // 本期气量
                            notice.firstGasCharge = tv_caobiao_detail_consume
                                    .getText().toString().trim();
                        }
                        // notice.currentCapacity = String.valueOf(CurrentCapacity);

                        String sql_cust = "select cmMrLastBal,cmMrOverdueAmt from custInfo where  accountId="
                                + "\"" + notice.userNo + "\"" + "";
                        ResultSet c = state.executeQuery(sql_cust);

                        notice.gasCharge = tv_caobiao_detail_consume.getText()
                                .toString().trim();
                        while (c.next()) {
                            notice.pastArrears = c.getString(1);
                            notice.lateFee = c.getString(2);
                        }
                        c.close();
                        notice.statisticalDate = formatter
                                .format(new Date());
                        notice.printDate = Date.format(new Date());
                        // 获取用户名
                        notice.userName = PerfUtils.getString(mActivity,
                                "userName", "") == null ? "1400017" : PerfUtils
                                .getString(mActivity, "userName", "");

                        String sql_phone = "select phone from perPhone where  accountId='"
                                + notice.userNo + "'";
                        ResultSet p = state.executeQuery(sql_phone);
                        while (p.next()) {
                            if (p.getString(1) == "null" || p.getString(1) == ""
                                    || p.getString(1) == null) {
                                notice.contactNumber = "";
                            } else {
                                notice.contactNumber = p.getString(1);
                            }
                        }
                        p.close();

                        // 提示是否打印
                        new AlertDialog.Builder(getActivity())
                                .setTitle("提示")
                                .setMessage("是否打印当前保存的数据？")
                                .setPositiveButton("打印",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {

                                                if (!mp.adapter.isEnabled()) {
                                                    Toast.makeText(mActivity,
                                                            "请开启蓝牙", Toast.LENGTH_SHORT).show();
                                                }
                                                // 判断是否成功连接蓝牙
                                                if (mp.GetBluetoothData()) {
                                                    // 调用打印方法

                                                    if (mp.mPrinter != null) {
                                                        mp.printNote(getActivity()
                                                                        .getResources(),
                                                                mp.mPrinter, true,
                                                                notice);
                                                        isPrint2 = "Y";
                                                    }

                                                } else {
                                                    new AlertDialog.Builder(
                                                            getActivity())
                                                            .setTitle("打印失败")
                                                            .setMessage(
                                                                    "当前设备尚未连接蓝牙打印机")
                                                            .show();
                                                    isPrint2 = "N";
                                                }

                                            }
                                        }).setNegativeButton("不打印", null).show();

                        String sql_isSave = "select * from uploadcustInfo where  spMeterHistoryId="
                                + "\"" + historyId + "\"" + "";

                        // Cursor rawQuery = db.rawQuery(sql_isSave, null);
                        ResultSet rawQuery = state.executeQuery(sql_isSave);
                        rawQuery.last();

                        if (rawQuery.getRow() == 0) {
                            // 判断必填字段

                            String sql = "insert into uploadcustInfo (spMeterHistoryId,meterConfigurationId,cmMr,readType,"
                                    + "cmMrDttm,cmMrRefVol,cmMrRefDebt,cmMrNotiPrtd,cmMrCommCd,cmMrRemark,cmMrState) "
                                    + "values (" + "'"
                                    + historyId
                                    + "',"
                                    + "'"
                                    + meterConfigurationId
                                    + "',"
                                    + "'"
                                    + readDate
                                    + "',"
                                    + "'"
                                    + type
                                    + "',"
                                    + "'"
                                    + time
                                    + "',"
                                    + "'"
                                    + CurrentCapacity
                                    + "',"
                                    + "'"
                                    + money
                                    + "',"
                                    + "'"
                                    + isPrint2
                                    + "',"
                                    // + "'"
                                    // + N
                                    // + "',"
                                    + isGood // 引号 + "\"Y\""
                                    + ","
                                    + "'"
                                    + beizhu
                                    + "',"
                                    + "'"
                                    + 2
                                    + "'"
                                    + ")";
						/*
						 * String sql =
						 * "insert into uploadcustInfo (spMeterHistoryId,meterConfigurationId,cmMr,readType,"
						 * +
						 * "cmMrDttm,cmMrRefVol,cmMrRefDebt,cmMrNotiPrtd,cmMrCommCd,cmMrRemark,cmMrState) "
						 * + "values (" + "\"" + historyId + "\"" + "," + "\"" +
						 * meterConfigurationId + "\"" + "," + readDate + "," +
						 * type + "," + "\"" + time + "\"" + "," +
						 * CurrentCapacity + "," + money + "," + print + "," +
						 * isGood // 引号 + "\"Y\"" // + + "," + "\"" + beizhu +
						 * "\"" + "," + "'" + 2 + "'" + ")";
						 */// db.execSQL(sql);
                            int executeUpdate = state.executeUpdate(sql);
                            // 更改用户信息表中的状态
                            String sql_update = "update custInfo set cmMrState = "
                                    + "'" + 3 + "'" + " where  spMeterHistoryId="
                                    + "'" + historyId + "'" + "";
                            // db.execSQL(sql_update);
                            state.executeUpdate(sql_update);

                            Toast.makeText(getActivity(), "已保存", Toast.LENGTH_SHORT).show();
                        } else {
                            // 更新
                            // 判断必填字段

                            String sql1 = "update  uploadcustInfo set meterConfigurationId="
                                    + "\""
                                    + meterConfigurationId
                                    + "\""
                                    + ","
                                    + "cmMr="
                                    + readDate
                                    + ",readType="
                                    + type
                                    + ","
                                    + "cmMrDttm="
                                    + "\""
                                    + time
                                    + "\",cmMrRefVol="
                                    + CurrentCapacity
                                    + ",cmMrRefDebt="
                                    + money
                                    + ",cmMrNotiPrtd="
                                    + print
                                    + ",cmMrCommCd="
                                    + isGood
                                    + ",cmMrRemark="
                                    + "'"
                                    + beizhu
                                    + "'"
                                    + "where spMeterHistoryId="
                                    + "\""
                                    + historyId
                                    + "\"" + " ";
                            // db.execSQL(sql1);
                            state.executeUpdate(sql1);
                            Toast.makeText(getActivity(), "已更新", Toast.LENGTH_SHORT).show();
                        }
                        rawQuery.close();

					/*
					 * String string2 =
					 * "select spMeterHistoryId,meterConfigurationId,cmMrDttm,readType,cmMr,cmMrRefVol,cmMrRefDebt,cmMrNotiPrtd,cmMrCommCd,cmMrRemark from uploadcustInfo where spMeterHistoryId = '"
					 * + historyId + "'"; ResultSet rest =
					 * state.executeQuery(string2);
					 * 
					 * rest.last(); int row = rest.getRow(); //
					 * rest.beforeFirst(); while (rest.next()) { String string =
					 * rest.getString(1); String string1 = rest.getString(2);
					 * String string4 = rest.getString(3); String string5 =
					 * rest.getString(4); String string6 = rest.getString(5);
					 * String string7 = rest.getString(6); String string8 =
					 * rest.getString(7); String string9 = rest.getString(9);
					 * String string90 = rest.getString(10); }
					 */

                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;

            // case R.id.tv_caobiao_detail_fujian:
            // // 跳转至附件界面
            // PollingFuJianFragment pollingFuJianFragment = new
            // PollingFuJianFragment(
            // backToList, targetContainer);
            //
            // ((ActivityInTab) getActivity()).navigateTo(pollingFuJianFragment);
            // break;
            case R.id.iv_pic1:
                // approve_detailAnimator(approve_detail_Height, approve_detail);
                ViewPropertyAnimator.animate(iv_pic1).rotationBy(180)
                        .setDuration(350).start();
                if (isOpen) {
                    approve_detail.setVisibility(View.GONE);
                    isOpen = !isOpen;
                } else {
                    approve_detail.setVisibility(View.VISIBLE);
                    isOpen = !isOpen;
                }
                break;

            case R.id.iv_pic2:
                ViewPropertyAnimator.animate(iv_pic2).rotationBy(180)
                        .setDuration(350).start();
                if (isOpen2) {
                    approve_detail2.setVisibility(View.GONE);
                    isOpen2 = !isOpen2;
                } else {
                    approve_detail2.setVisibility(View.VISIBLE);
                    isOpen2 = !isOpen2;
                }
                break;
            case R.id.tv_caobiao_detail_consume:// 气费说明界面

                // 跳转至附件界面
                PollingMoneyStandardFragment moneyStandardFragment = new PollingMoneyStandardFragment(
                        backToList, targetContainer);

//			获取气量和气费
                String qiliang = tv_caobiao_detail_air_measure.getText().toString().trim();
                String qifei = tv_caobiao_detail_consume.getText().toString().trim();

                Bundle bundle = new Bundle();
                bundle.putString("qiliang", qiliang);
                bundle.putString("qifei", qifei);
                moneyStandardFragment.setArguments(bundle);
                ((ActivityInTab) getActivity()).navigateTo(moneyStandardFragment);

			break;
		// case R.id.tv_caobiao_detail_consume:// 实时计费
		//
		// // new getMoneyDataTask().execute();
		//
		//
		// break;
		case R.id.btn_print:
			// 此处的打印需保存后进行,
			try {
				db = SQLiteData.openOrCreateDatabase();
				state = db.createStatement();

                    String sql = "select  cmMrState from custInfo where accountId = "
                            + "'" + taskId + "'";

                    // Cursor c = db.rawQuery(sql, null);
                    ResultSet c = state.executeQuery(sql);
                    String staly = null;
                    while (c.next()) {
                        staly = c.getString(0);
                    }
                    c.close();
                    db.close();
                    state.close();

                    if (staly.equals("3")) {

                        if (mp.GetBluetoothData()) {
                            // 设备已连接
                            if (mp.mPrinter != null) {
                                mp.printNote(getActivity().getResources(),
                                        mp.mPrinter, true, notice);
                            }
                        } else {

                            // 判断蓝牙是否打开,自动打开失败 提示 手动打开
                            if (!mp.adapter.isEnabled()) {
                                Toast.makeText(mActivity, "请开启蓝牙", Toast.LENGTH_SHORT).show();
                            }

                            if (!mp.GetBluetoothData()) {
                                // 在蓝牙匹配列表中未发现
                                mp.adapter.startDiscovery();
                            }

                            Toast.makeText(getActivity(), "正在连接蓝牙", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(getActivity(), "请保存后进行打印", Toast.LENGTH_SHORT).show();
                    }

                    break;
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            default:
                break;
        }
    }

    private String SubString(String str) {
        if (str.contains("."))
            return str.substring(0, str.indexOf("."));
        else
            return str;
    }

    private class getMoneyDataTask extends AsyncTask<Void, Void, String> {
        ProgressHUD overlayProgress = null;
        private long time_s;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            overlayProgress = AlertUtils.showDialog(getActivity(), null, null,
                    false);

            // 时间太长了，把加载进度条取消掉。。。。
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            if (overlayProgress != null) {
                overlayProgress.dismiss();
            }
            String s = result;
        }

        @Override
        protected String doInBackground(Void... params) {
            List<OutPut> listOutPuts = new ArrayList<OutPut>();
            // String key1 =
            // "{\"root\":{\"list\":{\"CM_H_Rating\":{\"@faultStyle\":\"wsdl\",\"input\":{\"user\":\""
            // + Constants.loginName
            // + "\",\"deviceId\":\"TEST\",\"operateType\":\"10\"}}}}}";
            String key1 = "{\"root\":{\"list\":{\"CM_H_Rating\":{\"@faultStyle\":\"wsdl\",\"input\":{\"accountId\":\""
                    + taskId
                    + "\",\"spMeterHistoryId\":\""
                    + custInfo.getSpMeterHistoryId()
                    + "\",\"meterConfigurationId\":\""
                    + custInfo.getMeterConfigurationId()
                    + "\",\"cmMrDttm\":\"2015-09-22-13.05.01\",\"cmMr\":\"150\",\"readType\":\"60\",\"user\":\"ZCD\",\"deviceId\":\"TEST\"}}}}}";

            String string = DataCollectionUtils.SynchronousData(
                    WebUtils.chaoBiaoMoneyUrl, key1);
            System.out.println("==========-=-=================" + string);

            return null;

        }
    }

    private Statement state;
    private int meRen;
    private String time;
    private TextView tv_title;

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mp.isAleadyregist) {
            getActivity().unregisterReceiver(mp.receiver);
        }

        mp.adapter.cancelDiscovery();
    }

    @Override
    public void refreshButtonAndText() {// 集中处理一些返回s
        // 显示保存按钮
        btn_next = (Button) getActivity().findViewById(R.id.btn_next);
        btn_next.setVisibility(View.VISIBLE);

        // 该用户已上传 不可以修改 隐藏保存按钮
        if (custInfo.getCmMrState().equals("2")) {
            btn_next.setVisibility(View.INVISIBLE);
        }

        btn_next.setText("保存");
        btn_next.setOnClickListener(PollingDetailFragment_mine.this);
        tv_title.setText("抄表");
        // getActivity().findViewById(R.id.t);
    }
}
