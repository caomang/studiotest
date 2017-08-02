package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.sbx.deeper.moblie.activity.TimePickerActivity;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import cn.sbx.deeper.moblie.util.DatabaseHelperInstance;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 审批组件详细界面
 *
 * @author terry.C
 */
@SuppressLint("ValidFragment")
public class AnJianDetailFormFragment_ZaoJU extends BaseFragment implements
        IRefreshButtonAndText, OnClickListener {
    private EditText et_guanjing;
    TextView et_ksri;
    private Spinner sp_location, sp_caizhi;
    private String code_YS = "";
    private String code_BH = "";
    DatabaseHelperInstance databaseHelperInstance;
    SQLiteDatabase instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            custInfo = (CustInfo_AnJian) bundle.getSerializable("custinfo");
            taskId = bundle.getString("id");
            device_name = bundle.getString("device_name");
        }
        databaseHelperInstance = new DatabaseHelperInstance();

        instance = databaseHelperInstance.getInstance(getActivity());
        // 数据字典
        dicDate_YS = getDicDate(taskId, "cmScZjYs");
        mContext = getActivity();


        // 获取保存表中的数据,回显
        getUpDate(taskId);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getUpDate(taskId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.layout_approve_detail_device_zaoju, container, false);
        et_guanjing = (EditText) view.findViewById(R.id.et_guanjing);
        et_ksri = (TextView) view.findViewById(R.id.et_ksri);
        sp_location = (Spinner) view.findViewById(R.id.sp_location);
        sp_caizhi = (Spinner) view.findViewById(R.id.sp_caizhi);
        btn_next = (Button) mActivity.findViewById(R.id.btn_next);
        btn_next.setVisibility(View.VISIBLE);
        btn_next.setText("确定");
        btn_next.setOnClickListener(this);
        et_ksri.setOnClickListener(this);

        final String[] strings = dicDate_YS.get(1);

        // 样式
        sp_location
                .setBackgroundResource(R.drawable.ic_approve_spinner_background);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                R.layout.spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_location.setAdapter(adapter);

        // 有下载数据时
        if (!TextUtils.isEmpty(custInfo.getCmScZjYs())
                && custInfo.getCmScZjYs() != null
                && !custInfo.getCmScZjYs().equals(null)) {
            // 如果保存表数据为"" ,显示下载数据,否则显示保存表数据
            if (TextUtils.isEmpty(save_YS)) {
                for (int a = 0; a < dicDate_YS.get(0).length; a++) {
                    if (dicDate_YS.get(0)[a].equals(custInfo.getCmScZjYs())) {
                        sp_location.setSelection(a);

                    }
                }
            } else {
                // 保存表不为空,回显保存表
                for (int a = 0; a < dicDate_YS.get(0).length; a++) {
                    if (dicDate_YS.get(0)[a].equals(save_YS)) {
                        sp_location.setSelection(a);

                    }
                }
            }

        } else {
            // 没有下载数据,显示保存表,且保存表有数据
            if (!TextUtils.isEmpty(save_YS)) {
                for (int a = 0; a < dicDate_YS.get(0).length; a++) {
                    if (dicDate_YS.get(0)[a].equals(save_YS)) {
                        sp_location.setSelection(a);

                    }
                }
            }

        }

        sp_location.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                code_BH = dicDate_YS.get(0)[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        // 熄火保护
        final ArrayList<String[]> dicDate_Xhbh = getDicDate(taskId,
                "cmScZjXhbh");
        String[] strings2 = dicDate_Xhbh.get(1);
        // 位置
        sp_caizhi
                .setBackgroundResource(R.drawable.ic_approve_spinner_background);

        ArrayAdapter<String> adapter_caizhi = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, strings2);
        adapter_caizhi
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_caizhi.setAdapter(adapter_caizhi);

        // 有下载数据时
        if (!TextUtils.isEmpty(custInfo.getCmScZjXhbh())
                && custInfo.getCmScZjXhbh() != null
                && !custInfo.getCmScZjXhbh().equals(null)) {
            // 如果保存表数据为"" ,显示下载数据,否则显示保存表数据
            if (TextUtils.isEmpty(save_Bh)) {
                for (int a = 0; a < dicDate_Xhbh.get(0).length; a++) {
                    if (dicDate_Xhbh.get(0)[a].equals(custInfo.getCmScZjXhbh())) {
                        sp_caizhi.setSelection(a);

                    }
                }
            } else {
                // 保存表不为空,回显保存表说明改变类型了
                for (int a = 0; a < dicDate_Xhbh.get(0).length; a++) {
                    if (dicDate_Xhbh.get(0)[a].equals(save_Bh)) {
                        sp_caizhi.setSelection(a);

                    }
                }
            }

        } else {
            // 没有下载数据,显示保存表,且保存表有数据
            if (!TextUtils.isEmpty(save_Bh)) {
                for (int a = 0; a < dicDate_Xhbh.get(0).length; a++) {
                    if (dicDate_Xhbh.get(0)[a].equals(save_Bh)) {
                        sp_caizhi.setSelection(a);

                    }
                }
            }

        }

        sp_caizhi.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                code_YS = dicDate_Xhbh.get(0)[arg2];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // pin牌
        // 保存表中的数据不为 "" ,显示保存表数据
//		if (!TextUtils.isEmpty(custInfo.getCmScZjPp()) && save_PPai != null
//				&& !save_PPai.equals("")) {
//			et_guanjing.setText(save_PPai);
//		}
//		// 保存表中无数据时,显示下载数据
//		if (!TextUtils.isEmpty(custInfo.getCmScZjPp()) && save_PPai != null
//				&& !save_PPai.equals("")) {
//			et_guanjing.setText(custInfo.getCmScZjPp());
//		}
        /*if (save_PPai==""||save_PPai==null) {
			if (custInfo.getCmScZjPp().equals("null")) {
				et_guanjing.setText("");
			}
			et_guanjing.setText(custInfo.getCmScZjPp());
		}
		et_guanjing.setText(save_PPai);
		if (save_RQ==""||save_RQ==null) {
			if (custInfo.getCmScZjPp().equals("null")) {
				et_ksri.setText("");
			}
			et_ksri.setText(custInfo.getCmScZjSyrq());
		}
		et_ksri.setText(save_RQ);*/

//		 保存表中数据
        if (!TextUtils.isEmpty(save_PPai) && !save_PPai.equals("null")) {
            et_guanjing.setText(save_PPai);
        } else if (!TextUtils.isEmpty(custInfo.getCmScZjPp()) && !custInfo.getCmScZjPp().equals("null")) {
            // 保存表中无数据时,显示下载数据
            et_guanjing.setText(custInfo.getCmScZjPp());
        }
//		 保存表中数据  日期
        if (!TextUtils.isEmpty(save_RQ) && !save_RQ.equals("null")) {
            et_ksri.setText(save_RQ);
        } else if (!TextUtils.isEmpty(custInfo.getCmScZjSyrq()) && !custInfo.getCmScZjSyrq().equals("null")) {
            // 保存表中无数据时,显示下载数据
            et_ksri.setText(custInfo.getCmScZjSyrq());
        }


    }

    // 查询待办数据
//	public Connection db;// 操作数据库的工具类
    StringBuilder sb;
    //	private Connection conne;
//	private Statement state;
//	private ResultSet c;
    private String taskId;
    private String device_name;

    private ArrayList<String[]> arrayList;
    private ArrayList<String[]> dicDate_YS;
    private Context mContext;
    private CustInfo_AnJian custInfo;
    private Button btn_next;
    private String pinpai = "";
    private String yangshi = "";
    private String xihuobaohu = "", ksrq = "";
    private String save_PPai = "";
    private String save_YS = "";
    private String save_Bh = "";
    private String save_RQ = "";

    private ArrayList<String[]> getDicDate(String taskId, String parendID) {
        String[] codeStrings;
        String[] descStrings;
        String describe = "";
        Cursor set = null;
        arrayList = new ArrayList<String[]>();
        try {
            String sql_dic = "select dictionaryCode,dictionaryDescr from dictionaries where  parentID = '"
                    + parendID + "'";
            set = instance.rawQuery(sql_dic, new String[]{});
//			set.last();
            codeStrings = new String[set.getCount()];
            descStrings = new String[set.getCount()];
//			set.beforeFirst();
            int a = 0;
            if (set.moveToFirst()) {
                codeStrings[a] = set.getString(0);
                descStrings[a] = set.getString(1);
                a++;
                while (set.moveToNext()) {
                    codeStrings[a] = set.getString(0);
                    descStrings[a] = set.getString(1);
                    a++;
                }
            }

            arrayList.add(codeStrings);
            arrayList.add(descStrings);
        } catch (SQLiteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (SQLiteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

        return arrayList;
    }

    @Override
    public void onClick(View v) {

        // 确定,立管信息插入到保存表中
        switch (v.getId()) {
            case R.id.btn_next:
                try {
                    saveData(taskId);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.et_ksri:
                startActivityForResult(new Intent(mContext,
                        TimePickerActivity.class), 100);
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String time = data.getStringExtra("CHEEL_TIME");
                if (!TextUtils.isEmpty(time)) {
                    try {
                        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
                        Date beginDate = dd.parse(time);
                        String format = dd.format(new Date());
                        Date enDate = dd.parse(format);

                        long day = (enDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
                        if (day >= 0) {
                            et_ksri.setText(time);
                        } else {
                            Toast.makeText(mContext, "所选时间已超出当前日期", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }
    }

    // 获取保存表中的数据
    public void getUpDate(String taskId) {

        String string = "select distinct cmScZjPp,cmScZjYs,cmScZjXhbh,cmScZjSyrq from uploadcustInfo_aj where cmSchedId = '"
                + custInfo.getCmSchedId()
                + "' and accountId = '"
                + taskId
                + "'";
        try {
//			conne = SQLiteData.openOrCreateDatabase(getActivity());
//			state = conne.createStatement();

            Cursor execute = instance.rawQuery(string, new String[]{});

            if (execute.moveToFirst()) {
                save_PPai = execute.getString(0);
                save_YS = execute.getString(1);
                save_Bh = execute.getString(2);
                save_RQ = execute.getString(3);
                execute.moveToNext();
            }

//			while (execute.next()) {
//				save_PPai = execute.getString("cmScZjPp");
//				save_YS = execute.getString("cmScZjYs");
//				save_Bh = execute.getString("cmScZjXhbh");
//				save_RQ = execute.getString("cmScZjSyrq");
//			}

        } catch (SQLiteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // 保存数据,首先判断要保存的数据和 客户表中的数据是否一致,如不同 在进行保存
    public void saveData(String userid) throws SQLException {

        if (custInfo.getCmScZjPp() != null
                && custInfo.getCmScZjPp().equals("null")
                && !custInfo.getCmScZjPp().equals(
                et_guanjing.getText().toString().trim())) {
            pinpai = et_guanjing.getText().toString().trim();
        }
        if (custInfo.getCmScZjYs() != null
                && custInfo.getCmScZjYs().equals("null")
                && !custInfo.getCmScZjYs().equals(code_BH)) {
            yangshi = code_BH;
        }
        if (custInfo.getCmScZjXhbh() != null
                && custInfo.getCmScZjXhbh().equals("null")
                && !custInfo.getCmScZjXhbh().equals(code_YS)) {
            xihuobaohu = code_YS;
        }
        if (custInfo.getCmScZjPp() != null
                && custInfo.getCmScZjPp().equals("null")
                && !custInfo.getCmScZjPp().equals(
                et_ksri.getText().toString().trim())) {
            ksrq = et_ksri.getText().toString().trim();
        }

        String sql_isSave = "select * from uploadcustInfo_aj where  accountId='"
                + taskId
                + "' and cmSchedId = '"
                + custInfo.getCmSchedId()
                + "'";
//		ResultSet executeQuery = null;
//		conne = SQLiteData.openOrCreateDatabase(getActivity());
//
//		state = conne.createStatement();
        Cursor executeQuery;
        executeQuery = instance.rawQuery(sql_isSave, new String[]{});

        if (!executeQuery.moveToNext()) {
            // 表中不存在该用户信息,插入

            String sql = "insert into uploadcustInfo_aj (cmSchedId,accountId,cmScZjPp,cmScZjYs,cmScZjXhbh,cmScZjSyrq) "
                    + "values ("
                    + "'"
                    + custInfo.getCmSchedId()
                    + "',"
                    + "'"
                    + taskId
                    + "',"
                    + "'"
                    + pinpai
                    + "',"
                    + "'"
                    + yangshi
                    + "'," + "'" + xihuobaohu + "'," + "'" + ksrq + "')";

            instance.execSQL(sql);
        } else {
            // 表中存在该用户,更新

            String sql = "update  uploadcustInfo_aj set cmScZjPp = '" + pinpai
                    + "',cmScZjYs= '" + yangshi + "', cmScZjXhbh= '"
                    + xihuobaohu + "', cmScZjSyrq= '" + ksrq
                    + "' where  accountId='" + taskId + "' and cmSchedId = '"
                    + custInfo.getCmSchedId() + "'";
            instance.execSQL(sql);

        }
        if (executeQuery != null) {
            try {
                executeQuery.close();
            } catch (SQLiteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//		if (state != null) {
//			try {
//				state.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		if (conne != null) {
//			try {
//				conne.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (instance != null) {
            instance.close();
        }
    }

}
