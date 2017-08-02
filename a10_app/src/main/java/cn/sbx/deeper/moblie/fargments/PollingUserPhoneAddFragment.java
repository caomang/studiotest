package cn.sbx.deeper.moblie.fargments;

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
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.regex.Pattern;

import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.interfaces.IRefreshButtonAndText;
import petrochina.ghzy.a10fieldwork.R;

public class PollingUserPhoneAddFragment extends BaseFragment implements
        IRefreshButtonAndText {
    private String taskId;
    private Spinner spinner;
    private EditText et_phone;
    private String typeStrin;
    private Button bt_add_newphone;
    private String[] readType_des;
    private String[] readType_codes;
    private DatabaseHelper db;
    private SQLiteDatabase state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            taskId = bundle.getString("id");
        }


        // 获取电话类型从字典中
        selectZD();
    }

    private void selectZD() {
        try {
            db = new DatabaseHelper(mActivity);
             state = db.getWritableDatabase();
            String sql1 = "select dictionaryDescr,dictionaryCode from dictionaries where parentID = "
                    + "'phoneType'" + "";
            Cursor cursor = state.rawQuery(sql1,null);
            cursor.moveToLast();
            // 获取resultSet 的行号
            readType_des = new String[cursor.getCount()];
            readType_codes = new String[cursor.getCount()];
            int a = 1;

            if(cursor.moveToFirst()){
                readType_des[0] = cursor.getString(0);
                readType_codes[0] = cursor.getString(1);
                while (cursor.moveToNext()) {
                    readType_des[a] = cursor.getString(0);
                    readType_codes[a] = cursor.getString(1);
                    a++;
                }
            }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),
                R.layout.layout_userphone_addphone, null);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        bt_add_newphone = (Button) view.findViewById(R.id.bt_add_newphone);

        bt_add_newphone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取类型 和电话

                String type_phone = "";
                for (int a = 0; a < readType_des.length; a++) {

                    if (typeStrin.equals(readType_des[a])) {
                        type_phone = readType_codes[a];
                        String num = et_phone.getText().toString().trim();

                        if (!TextUtils.isEmpty(type_phone)
                                && !TextUtils.isEmpty(num)) {
                            //进行电话号码的正则表达匹配

                            if (patternForWork(num) || patternForHome(num) || patternPhone(num)) {
                                insterPhoneNumber(num, type_phone);
                                Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
                                backPrecious();

                            } else {
                                Toast.makeText(mActivity, "您输入的电话号码没有匹配", Toast.LENGTH_SHORT).show();
                            }

                            // 插入电话,插入前需要判断该条电话是否已经存在-----未实现-------

                        } else {
                            Toast.makeText(mActivity, "请输入电话", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        return view;
    }

    private boolean patternForWork(String nmuString) {
        return Pattern.matches("\\d{4}-\\d{8}", nmuString);
    }

    private boolean patternForHome(String nmuString) {
        return Pattern.matches("\\d{3}-\\d{8}", nmuString);
    }

    private boolean patternPhone(String nmuString) {

        return Pattern.matches("[1][3587]\\d{9}", nmuString);

    }


    protected void insterPhoneNumber(String num, String type_phone) {
        // 获取该用户电话最大序号
        String max_Sequence = phoneSequence(taskId);
        int Max_code = 1;// 电话序号默认从 1 开始
        if (max_Sequence != null) {
            Max_code = Integer.parseInt(max_Sequence) + 1;
        }
        String inster_phone = " insert into perPhone  (accountId,extension,sequence,phoneType ,phone,cmPhoneOprtType) values("
                + "'"
                + taskId
                + "'"
                + ","
                + "'"
                + ""
                + "'"
                + ","
                + "'"
                + Max_code

                + "'"
                + ","
                + "'"
                + type_phone
                + "'"
                + ","
                + "'"
                + num
                + "'"
                + " ,'10')";

        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        try {
            db =new DatabaseHelper(mActivity);
            state = db.getWritableDatabase();
            // 查询数据返回游标对象
            state.execSQL(inster_phone);

            state.close();
            db.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // final String[] options = new String[4];
        // options[0] = "工作电话";
        // options[1] = "手机";
        // options[2] = "电话";
        // options[3] = "住宅电话";
        spinner.setBackgroundResource(R.drawable.ic_approve_spinner_background);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity,
                R.layout.spinner_item, readType_des);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // 设置点击事件
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // 获取选中的电话类型

                typeStrin = readType_des[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    // 讲电话 插入到数据库中,首先获取该用户的最大电话序号,然后插入
    private String phoneSequence(String taskId) {
        String select_phone = "select MAX(sequence) from perPhone where accountId="
                + "'" + taskId + "'";

        String maxCode = null;
        try {
            // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
            db = new DatabaseHelper(getActivity());
            state = db.getWritableDatabase();
            // 查询数据返回游标对象
            Cursor rawQuery = state.rawQuery(select_phone,null);

            maxCode = null;
            while (rawQuery.moveToNext()) {
                maxCode = rawQuery.getString(0);
            }
            rawQuery.close();
            state.close();
            db.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return maxCode;

    }
}
