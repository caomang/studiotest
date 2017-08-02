package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.activity.BaseActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.CmScShType_Dic;
import cn.sbx.deeper.moblie.domian.CustInfo_AnJian;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTD;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTR;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTable;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UIInput;
import cn.sbx.deeper.moblie.domian.cmScShItem;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.DatabaseHelperInstance;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 审批组件详细界面
 *
 * @author terry.C
 */
@SuppressLint("ValidFragment")
public class AnJianDeviceDetailFragment extends BaseFragment implements
        OnClickListener {

    private CheckBox cb_left;
    private ExpandableListView elv_details;
    Context mContext;
    private ProgressHUD prpgressHUD;
    private String formPath;
    private String htmlPath;
    private String filePath;
    private String taskId;
    private BaseActivity activity;
    //	String path = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/cb_db.db";
//	String path = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/cb_db.db";
    private String device_name = "";
    // private String biaojuString =
    // "表直通;#表漏气;#表接头漏气;#反装;#表电池负极边弹簧锈蚀;#与电气设施达不到安全间距;#表所在位置其他气味较浓;#包裹封装;#小流量不运行;#表显与实际用气不相符;#高位表改低位表;#同室住人;#达到报废年限;#表具倾斜;#表底不实;#外观损伤;#表与灶具无安全距离;#表与采暖炉无安全距离;#表与热水器无安全距离;#电磁阀运行异常;#通风条件不符合要求;#字轮损坏;#铭牌损坏;#铅封损坏;#外罩锈蚀";
    private String biaojuString = "";
    private String zaojuguanString = "软管老化;#软管超长;#软管无卡箍;#软管穿墙;#软管热辐射;#软管间有三通;#与周围燃具无安全距离;#软管漏气;#包裹封装;#暗装;#不锈钢波纹软管成直角;#软管连接处松动;#非专用软管";
    private String zaojuString = "损坏;#油污严重;#无熄火保护;#与电源无安全距离;#灶具漏气;#达到报废年限;#同室住人;#通风条件不符合要求;#火焰黄色;#火焰红色;#与其他热源无安全距离;#与木质家具无安全距离";
    private String cainuanluString = "连接不合格;#连接接头漏气;#烟道未伸到室外;#达到报废年限;#超出使用年限;#包裹封装;#采暖炉漏气;#损坏;#同室住人;#装在卫生间;#装在洗浴间;#与烟道无安全距离;#与木质家具无安全距离;#通风条件不符合要求;#火焰黄色;#火焰红色";
    private String reshuiqiString = "连接不合格;#超出使用年限;#烟道未伸到室外;#包裹封装;#达到报废年限;#直排式热水器;#热水器漏气;#损坏;#同室住人;#装在卫生间;#装在洗浴间;#与烟道无安全距离;#与木质家具无安全距离;#通风条件不符合要求;#火焰黄色;#火焰红色;#连接接头漏气";
    private String qitaString = "厨房他用;#私拆、私接燃气设施;#多种燃料共用;#燃气设施房间堆放杂物;#燃气设施房间堆放易燃易爆物;#燃气设施房间堆放腐蚀性物品;#未张贴警示标志;#偷气盗气";
    private String famenString = "灶前阀门采用双嘴阀;#灶前阀门内漏;#灶前阀门不能启闭;#立管阀门不能启闭;#立管阀门内漏;#表前阀门内漏;#表前阀门不能启闭";
    private String zhiguanString = "无支架;#支架松动;#支管漏气;#包裹封装;#暗埋;#与电气设施达不到安全间距;#支管轻微锈蚀;#支管严重锈蚀;#搭挂重物;#与电器接地线相连;#私改;#支管松动;#穿孔;#脱漆;#颜色涂改;#与火源无安全距离";
    private String liguanString = "包裹封装;#暗埋;#搭挂重物;#立管轻微锈蚀;#立管严重锈蚀;#立管漏气;#与电器接地线相连;#私改;#管卡松动;#立管松动;#颜色涂改;#脱漆;#穿孔;#与电气设施达不到安全间距;#与火源无安全距离";
    private String baojingqi = "";
    private List<CheckBox> listCheckBoxs = new ArrayList<CheckBox>();
    // 存放所点击选中的item
    private List<cmScShItem> list_isCheck = new ArrayList<cmScShItem>();
    Button btn_next;
    private String describe, code;
    // 存放隐换类型对象
    ArrayList<CmScShType_Dic> yHTypeList = new ArrayList<CmScShType_Dic>();
    // 存放隐换类选项对象
    ArrayList<cmScShItem> yHXuanXiangList = new ArrayList<cmScShItem>();
    private SQLiteDatabase instance;
    DatabaseHelperInstance databaseHelperInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        activity = (BaseActivity) getActivity();
        Bundle bundle = getArguments();
        taskId = bundle.getString("id");
        custInfo = (CustInfo_AnJian) bundle.getSerializable("custInfo");
        code = bundle.getString("code");
        describe = bundle.getString("describe");

        databaseHelperInstance = new DatabaseHelperInstance();
        instance = databaseHelperInstance.getInstance(mContext);
        // 查询异常选项,通过对应的异常类型编码

        prpgressHUD = AlertUtils.showDialog(mContext, null, null, false);
        prpgressHUD.show();
        getYiChDate(code);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_approve_detail_device,
                container, false);
        cb_left = (CheckBox) view.findViewById(R.id.cb_left);
        cb_left.setOnClickListener(this);
        cb_left.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.approve_back, 0, 0, 0);
        elv_details = (ExpandableListView) view.findViewById(R.id.elv_details);
        btn_next = (Button) getActivity().findViewById(R.id.btn_next);
        btn_next.setText("确定");
        btn_next.setVisibility(View.VISIBLE);
        btn_next.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        new ApproveDetailTask().execute(); // 下载数据
    }

    // 通过接收到的异常类型查询异常选项

    public void getYiChDate(String code) {
        yHTypeList.clear();
        yHXuanXiangList.clear();
        try {


//			conne = new DatabaseHelper(mContext);
//
//			 state = conne.getWritableDatabase();
//			state = conne.createStatement();
            // 按升序查询
            String dqlString = "select cmScShItemDescr,cmScShType,cmScShTypeDescr,cmScShItem,cmScShItemDescr from dic_cmScShItem_aj where cmScShType = '"
                    + code + "'  ORDER BY cmScShNO ASC ";

            Cursor set = instance.rawQuery(dqlString, null);
            set.moveToLast();
            int lastOne = set.getCount();
            int a = 1;
//			set.moveToPrevious();
            CmScShType_Dic cmScShType_Dic = null;
            cmScShItem cmScShItem = null;
            cmScShType_Dic = new CmScShType_Dic();

            if (set.moveToFirst()) {

                while (set.moveToNext()) {
                    // 拼接字符串
                    if (a == lastOne) {
                        biaojuString += set.getString(0);
                    } else {
                        biaojuString += set.getString(0) + ";#";
                    }
                    a++;

                    // 将隐患信息编码存入集合,在点击时,回去对应位置的编码及描述存入隐患信息表
                    cmScShItem = new cmScShItem();

                    cmScShType_Dic.cmScShType = set.getString(1);
                    cmScShType_Dic.cmScShTypeDescr = set
                            .getString(2);

                    cmScShItem.cmScShItem = set.getString(3);
                    cmScShItem.cmScShItemDescr = set.getString(4);
                    yHXuanXiangList.add(cmScShItem);
                }
            }

//			while (set.moveToNext()) {
//
//				// 拼接字符串
//				if (a == lastOne) {
//					biaojuString += set.getString(0);
//				} else {
//					biaojuString += set.getString(0) + ";#";
//				}
//				a++;
//
//				// 将隐患信息编码存入集合,在点击时,回去对应位置的编码及描述存入隐患信息表
//				cmScShItem = new cmScShItem();
//
//				cmScShType_Dic.cmScShType = set.getString(1);
//				cmScShType_Dic.cmScShTypeDescr = set
//						.getString(2);
//
//				cmScShItem.cmScShItem = set.getString(3);
//				cmScShItem.cmScShItemDescr = set.getString(4);
//				yHXuanXiangList.add(cmScShItem);
//			}
            yHTypeList.add(cmScShType_Dic);
            System.out.println("=====biaojuString================="
                    + biaojuString);

            prpgressHUD.dismiss();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // 查询待办数据
//	public DatabaseHelper db;// 操作数据库的工具类
    StringBuilder sb;
    //	private DatabaseHelper conne;
//	private SQLiteDatabase state;
    private String getBiaoJuString;
    private CustInfo_AnJian custInfo;
    private boolean isSuccess;
    private boolean isRepeat;

    public void selectList(String name) {
        sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root><form>");
        sb.append("<table id=" + "\"1\"" + " title=" + "\"" + describe + "\""
                + " expand=" + "\"true\"" + " editfield=" + "\"true\""
                + " columns=" + "\"2\"" + ">");
        sb.append("<tr parent=\"\"" + "><td>" + describe + "</td><td>"
                + biaojuString + "(device)" + "</td></tr>");
        sb.append("</table>");
        /*
		 * if (name.equals("表具")) { sb.append("<table id=" + "\"1\"" + " title="
		 * + "\"表具\"" + " expand=" + "\"true\"" + " editfield=" + "\"true\"" +
		 * " columns=" + "\"2\"" + ">"); sb.append("<tr parent=\"\"" + "><td>" +
		 * "表具" + "</td><td>" + biaojuString + "(device)" + "</td></tr>");
		 * sb.append("</table>"); } else if (name.equals("阀门")) {
		 * sb.append("<table id=" + "\"1\"" + " title=" + "\"阀门\"" + " expand="
		 * + "\"true\"" + " editfield=" + "\"true\"" + " columns=" + "\"2\"" +
		 * ">"); sb.append("<tr parent=\"\"" + "><td>" + "阀门" + "</td><td>" +
		 * famenString + "(device)" + "</td></tr>"); sb.append("</table>"); }
		 * else if (name.equals("连接管")) { sb.append("<table id=" + "\"1\"" +
		 * " title=" + "\"连接管\"" + " expand=" + "\"true\"" + " editfield=" +
		 * "\"true\"" + " columns=" + "\"2\"" + ">");
		 * sb.append("<tr parent=\"\"" + "><td>" + "灶具连接管" + "</td><td>" +
		 * zaojuguanString + "(device)" + "</td></tr>"); sb.append("</table>");
		 * } else if (name.equals("热水器")) { sb.append("<table id=" + "\"1\"" +
		 * " title=" + "\"热水器\"" + " expand=" + "\"true\"" + " editfield=" +
		 * "\"true\"" + " columns=" + "\"2\"" + ">");
		 * sb.append("<tr parent=\"\"" + "><td>" + "热水器" + "</td><td>" +
		 * reshuiqiString + "(device)" + "</td></tr>"); sb.append("</table>"); }
		 * else if (name.equals("支管")) { sb.append("<table id=" + "\"1\"" +
		 * " title=" + "\"支管\"" + " expand=" + "\"true\"" + " editfield=" +
		 * "\"true\"" + " columns=" + "\"2\"" + ">");
		 * sb.append("<tr parent=\"\"" + "><td>" + "支管" + "</td><td>" +
		 * zhiguanString + "(device)" + "</td></tr>"); sb.append("</table>"); }
		 * else if (name.equals("采暖炉")) { sb.append("<table id=" + "\"1\"" +
		 * " title=" + "\"采暖炉\"" + " expand=" + "\"true\"" + " editfield=" +
		 * "\"true\"" + " columns=" + "\"2\"" + ">");
		 * sb.append("<tr parent=\"\"" + "><td>" + "采暖炉" + "</td><td>" +
		 * cainuanluString + "(device)" + "</td></tr>"); sb.append("</table>");
		 * } else if (name.equals("灶具")) { sb.append("<table id=" + "\"1\"" +
		 * " title=" + "\"灶具\"" + " expand=" + "\"true\"" + " editfield=" +
		 * "\"true\"" + " columns=" + "\"2\"" + ">");
		 * sb.append("<tr parent=\"\"" + "><td>" + "灶具" + "</td><td>" +
		 * zaojuString + "(device)" + "</td></tr>"); sb.append("</table>"); }
		 * else if (name.equals("其他")) { sb.append("<table id=" + "\"1\"" +
		 * " title=" + "\"其他\"" + " expand=" + "\"true\"" + " editfield=" +
		 * "\"true\"" + " columns=" + "\"2\"" + ">");
		 * sb.append("<tr parent=\"\"" + "><td>" + "其他" + "</td><td>" +
		 * qitaString + "(device)" + "</td></tr>"); sb.append("</table>"); }
		 * else if (name.equals("报警器")) { sb.append("<table id=" + "\"1\"" +
		 * " title=" + "\"报警器\"" + " expand=" + "\"true\"" + " editfield=" +
		 * "\"true\"" + " columns=" + "\"2\"" + ">");
		 * sb.append("<tr parent=\"\"" + "><td>" + "报警器" + "</td><td>" +
		 * qitaString + "(device)" + "</td></tr>"); sb.append("</table>"); }
		 */
        sb.append("</form></root>");
        System.out.println(sb.toString());
    }

    private class ApproveDetailTask extends
            AsyncTask<Void, Void, SinopecApproveDetailEntry> {
        private ProgressHUD overlayProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            overlayProgress = AlertUtils.showDialog(getActivity(), null, this,
                    false);

        }

        @Override
        protected void onPostExecute(final SinopecApproveDetailEntry result) {
            super.onPostExecute(result);
            if (overlayProgress != null)
                overlayProgress.dismiss();
            if (result != null) {
                elv_details
                        .setAdapter(new MyAdapter(result.form.sinopecTables));
                for (int i = 0; i < result.form.sinopecTables.size(); i++) {
                    SinopecTable table = result.form.sinopecTables.get(i);
                    if ("true".equalsIgnoreCase(table.expand)) {
                        elv_details.expandGroup(i);
                    }
                }
            }
        }

        @Override
        protected SinopecApproveDetailEntry doInBackground(Void... params) {
            InputStream inputStream = null;
            try {
                // inputStream =
                // getActivity().getAssets().open("7539000000.xml");

                selectList(device_name);
                inputStream = new ByteArrayInputStream(sb.toString().getBytes(
                        "UTF-8"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return DataCollectionUtils.receiveApproveDetailForm1(inputStream,
                    formPath, taskId);
        }
    }

    public class MyAdapter extends BaseExpandableListAdapter {

        List<SinopecTable> groupList = new ArrayList<SinopecTable>();
        List<List<SinopecTR>> childList = new ArrayList<List<SinopecTR>>();
        LinearLayout.LayoutParams fwParams;

        public MyAdapter(List<SinopecTable> sinopecTables) {
            fwParams = new LinearLayout.LayoutParams(-1, -2);
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
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            int columns = Integer
                    .parseInt(groupList.get(groupPosition).columns);
            SinopecTR sinopecTR = childList.get(groupPosition).get(
                    childPosition);
            LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(
                    -1, -2);
            ll_params.setMargins(
                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 5),
                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 5),
                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 5),
                    cn.sbx.deeper.moblie.util.TextUtils.Dp2Px(mContext, 5));
            // 获取列的集合
            List<SinopecTD> sinopecTDList = sinopecTR.tds;
            if (columns == 1) { // 如果是1列，表示内容为html格式
                holder.ll_parent.setVisibility(View.VISIBLE);
                holder.vv_height.setVisibility(View.GONE);
                holder.ll_left.setVisibility(View.VISIBLE);
                holder.ll_right.setVisibility(View.GONE); //
                holder.ll_scroll.setVisibility(View.GONE); // 隐藏滚动视图
                holder.ll_left.removeAllViews(); // 移除所有的视图
                for (int i = 0; i < sinopecTDList.size(); i++) {
                    SinopecTD sinopecTD = sinopecTDList.get(i);
                    if (sinopecTD.content.contains("(html)")) {
                        createWebView(holder.ll_left, sinopecTD.content);
                    } else {
                        // 创建视图
                        TextView textView = new TextView(getActivity());
                        textView.setLayoutParams(new LinearLayout.LayoutParams(
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
                holder.ll_left.setVisibility(View.GONE);//将左侧隐藏,显示一列
                holder.ll_right.setVisibility(View.VISIBLE); //
                holder.ll_scroll.setVisibility(View.GONE); // 隐藏滚动视图
                holder.ll_left.removeAllViews();
                holder.ll_right.removeAllViews();

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
                // 获取异常选项,初始化用
                ArrayList<String> ycValues = getYCValues(custInfo);

                for (int i = 0; i < sinopecTDList.size(); i++) {
                    SinopecTD sinopecTD = sinopecTDList.get(i); // 获取一个td
                    if ((sinopecTD.sinopecViews == null || sinopecTD.sinopecViews
                            .size() == 0)) { // 表示没有input视图
                        if (i == 0) {
                            // 创建视图
                            TextView textView = new TextView(getActivity());
                            textView.setLayoutParams(new LinearLayout.LayoutParams(
                                    -1, -1));
                            holder.ll_left.addView(textView);
                            textView.setTextColor(Color.parseColor("#3773c0"));
                            textView.setText((sinopecTD.content
                                    .contains("(html)") ? replaceHtml(sinopecTD.content)
                                    : sinopecTD.content));
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
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
                            if (sinopecTD.content.contains("(html)")) {
                                createWebView(holder.ll_right,
                                        sinopecTD.content);
                            } else if (sinopecTD.content.contains("(image)")) {
                                createImageWebView(holder.ll_right,
                                        sinopecTD.content);
                            } else if (sinopecTD.content.contains("(device)")) {
                                String[] checkStrings = replaceDevice(
                                        sinopecTD.content).split(";#");
                                for (int j = 0; j < checkStrings.length; j++) {// 创建异常选项----------------
                                    final CheckBox checkBox = new CheckBox(
                                            mContext); // 创建checkbox
                                    checkBox.setLayoutParams(ll_params);
                                    checkBox.setTextAppearance(mContext,
                                            R.style.anjian_style);
                                    checkBox.setText(checkStrings[j]);
                                    checkBox.setPadding(
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(mContext, 10),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(mContext, 5),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(mContext, 15),
                                            cn.sbx.deeper.moblie.util.TextUtils
                                                    .Dp2Px(mContext, 5));
                                    checkBox.setButtonDrawable(new ColorDrawable(
                                            Color.TRANSPARENT));

                                    checkBox.setCompoundDrawablesWithIntrinsicBounds(
                                            0, 0, R.drawable.daijian, 0);

                                    final int temp = j;
                                    checkBox.setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            if (checkBox.isChecked()) {
                                                // listCheckBoxs.add(checkBox);
                                                // 将点击条目的异常选项编码和描述 的对象放入集合中
                                                list_isCheck
                                                        .add(yHXuanXiangList
                                                                .get(temp));

                                                checkBox.setCompoundDrawablesWithIntrinsicBounds(
                                                        0, 0,
                                                        R.drawable.daijian2, 0);
                                            } else {
                                                // listCheckBoxs.remove(checkBox);
                                                list_isCheck
                                                        .remove(yHXuanXiangList
                                                                .get(temp));
                                                checkBox.setCompoundDrawablesWithIntrinsicBounds(
                                                        0, 0,
                                                        R.drawable.daijian, 0);
                                            }
                                        }
                                    });

                                    if (ycValues.size() > 0) {// 初始化异常选项-----------
                                        if (ycValues.contains(checkStrings[j])) {
                                            checkBox.performClick();
                                        }
                                    }
									/*
									 * if (selectYCValues(device_name, taskId)
									 * != null) {// 初始化异常选项---原-------- if
									 * (selectYCValues(device_name, taskId)
									 * .contains(checkStrings[j])) {
									 * checkBox.performClick(); } }
									 */

                                    holder.ll_right.addView(checkBox);
                                    // 横线
                                    View horLine = new View(getActivity());
                                    horLine.setLayoutParams(new LinearLayout.LayoutParams(
                                            -1, 1));
                                    horLine.setBackgroundResource(R.drawable.shape_matters_router_child_divider);
                                    holder.ll_right.addView(horLine);
                                }

                            } else {
                                // 创建视图
                                TextView textView = new TextView(getActivity());
                                textView.setLayoutParams(new LinearLayout.LayoutParams(
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
                                        textView.setLayoutParams(new LinearLayout.LayoutParams(
                                                -1, -2));
                                        holder.ll_left.addView(textView);
                                        addInputView(textView, uiInput);
                                    }
                                } else {
                                    if ("text".equalsIgnoreCase(uiInput.type)) {
                                        EditText editText = new EditText(
                                                getActivity());
                                        editText.setLayoutParams(new LinearLayout.LayoutParams(
                                                -1, -2));
                                        holder.ll_right.addView(editText);
                                        addInputEditView(editText, uiInput);
                                    } else {
                                        TextView textView = new TextView(
                                                getActivity());
                                        textView.setLayoutParams(new LinearLayout.LayoutParams(
                                                -1, -2));
                                        holder.ll_right.addView(textView);
                                        addInputView(textView, uiInput);
                                    }
                                }
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
                creatViewThreadMoreView(holder.ll_scrll_left,
                        holder.ll_content, childList.get(groupPosition));
            }
            return convertView;
        }

        // 创建出一个webview视图
        public void createWebView(LinearLayout layout, String content) {
            WebView webView = new WebView(getActivity());
            webView.setLayoutParams(new LinearLayout.LayoutParams(-1,
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
            webView.loadDataWithBaseURL(null, replaceHtmlWebView(content),
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

        // 创建出一个imageWebview视图
        public void createImageWebView(LinearLayout layout, String content) {
            WebView webView = new WebView(getActivity());
            webView.setLayoutParams(new LinearLayout.LayoutParams(-1,
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
                textView.setLayoutParams(new LinearLayout.LayoutParams(-1,
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
                lineView.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
                lineView.setBackgroundResource(R.drawable.shape_matters_router_child_divider);
                ll_left.addView(lineView);
            }
        }

        public void addScrollView(LinearLayout layout, List<SinopecTD> list,
                                  int width, int height, int index) {
            // 添加第一个视图
            LinearLayout ll_first = new LinearLayout(getActivity());
            ll_first.setLayoutParams(new LinearLayout.LayoutParams(-2, height));
            ll_first.setOrientation(LinearLayout.HORIZONTAL);
            ll_first.setGravity(Gravity.CENTER);
            layout.addView(ll_first);
            for (int i = 0; i < list.size(); i++) {
                final SinopecTD sinopecTD = list.get(i);
                if (sinopecTD.sinopecViews.size() == 0) { // 如果是普通文本的话
                    final TextView textView = new TextView(getActivity());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
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
                            textView.setLayoutParams(new LinearLayout.LayoutParams(
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
                            editText.setLayoutParams(new LinearLayout.LayoutParams(
                                    width, -1));
                            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            editText.setText(uiInput.value);
                            editText.setHint("请输入");
                            editText.setTextColor(Color.BLACK);
                            editText.setBackgroundColor(Color.WHITE);
                            ll_first.addView(editText);
                            editText.addTextChangedListener(new MyTextWatcher(
                                    uiInput));
                        }
                    }
                }

                // 竖线
                View verLine = new View(getActivity());
                verLine.setLayoutParams(new LinearLayout.LayoutParams(1, height));
                verLine.setBackgroundResource(R.drawable.shape_matters_router_child_divider);
                ll_first.addView(verLine);
            }

            // 横线
            View horLine = new View(getActivity());
            horLine.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
            horLine.setBackgroundResource(R.drawable.shape_matters_router_child_divider);
            layout.addView(horLine);
        }

        // 转换html为文本
        public String replaceHtml(String str) {
            str = str.replaceAll("\\(html\\)", "");
            return Html.fromHtml(str).toString();
        }

        // 转换html为文本
        public String replaceDevice(String str) {
            str = str.replaceAll("\\(device\\)", "");
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
                if ("file".equalsIgnoreCase(uiInput.type)
                        || "form".equalsIgnoreCase(uiInput.type)
                        || "html".equalsIgnoreCase(uiInput.type)
                        || "url".equalsIgnoreCase(uiInput.type)) {
                    textView.setTextColor(Color.BLUE);
                    textView.setText(uiInput.title.trim());
                    textView.setTag(uiInput);
                } else if ("label".equalsIgnoreCase(uiInput.type)) {
                    textView.setTextColor(Color.parseColor("#3773c0"));
                    textView.setText(uiInput.title.trim());
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
            editText.setBackgroundColor(Color.WHITE);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            editText.setText(uiInput.value.trim());
            editText.setHint("请输入");
            editText.setTag(null);
            editText.addTextChangedListener(new MyTextWatcher(uiInput));
        }

        // 根据不同的类型跳转到不同的跳转界面上
        public void startFragmentView(final UIInput uiInput) {
		/*	if ("form".equalsIgnoreCase(uiInput.type)) {
				// SinopecApproveOADetailFormFragment formFragment = new
				// SinopecApproveOADetailFormFragment();
				// Bundle bundle = new Bundle();
				// bundle.putString("formpath", formPath);
				// bundle.putString("htmlPath", htmlPath);
				// bundle.putString("filePath", filePath);
				// bundle.putString("key", uiInput.value);
				// formFragment.setArguments(bundle);
				// ((ActivityInTab) getActivity()).navigateTo(formFragment);
				Toast.makeText(getActivity(), "暂不支持", 1).show();
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
    }

    // 提交
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (cb_left.getId() == v.getId()) {
            backPrecious(); // 返回
        } else if (btn_next.getId() == v.getId()) { // 提交

            if (list_isCheck.size() > 0) {
//                StringBuilder sb = new
//                        StringBuilder();
//                for (int i = 0; i < listCheckBoxs.size(); i++) {
//                    sb.append(listCheckBoxs.get(i).getText().toString() + ";#");
//                }
//                submitData(device_name, taskId, sb.substring(0,
//                        sb.lastIndexOf(";#")));
//                Toast.makeText(getActivity(), "提交成功",
//                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "请选择异常项",
                        Toast.LENGTH_SHORT).show();
            }

			CmScShType_Dic cmScShType_Dic = yHTypeList.get(0);
            if (list_isCheck.size() == 0) {
                // 隐换类型
                // 集合为空删除该隐患类型下的选项
                deleteData(cmScShType_Dic);
                Toast.makeText(mActivity, "已清除隐患信息", Toast.LENGTH_LONG).show();
            } else if (list_isCheck.size() > 0) {
                // 保存该类型隐患时,先删除该隐患类型下的选项
                deleteData(cmScShType_Dic);
                for (int i = 0; i < list_isCheck.size(); i++) {
                    try {
                        insterDate(list_isCheck.get(i), cmScShType_Dic);

                    } catch (SQLException e) {
                        isSuccess = true;
                        e.printStackTrace();
                    }
                }
                if (isSuccess) {
                    Toast.makeText(mActivity, "提交失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "提交成功", Toast.LENGTH_SHORT).show();
                    // list_isCheck.clear();
                    isRepeat = true;
                    Constants.isYiChang = "是";

                }
            } else {
                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getActivity(), "请不要重复提交", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "请选择异常项", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void deleteData(CmScShType_Dic cmScShType_Dic) {
        try {
//			conne = new DatabaseHelper(mContext);
//			state = conne.getWritableDatabase();

            // 提交时 ,先将当前用户隐患信息表中的当前隐换类型数据清除,
            String sql_delete = "delete from perSh_aj where cmSchedId = '"
                    + custInfo.getCmSchedId() + "' and accountId = '"
                    + custInfo.getAccountId() + "' and cmScShType = '" + cmScShType_Dic.getCmScShType()
                    + "' ";
            instance.execSQL(sql_delete);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
//			if (state != null) {
//				try {
//					state.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//
//			}
//			if (conne != null) {
//				try {
//					conne.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//
//			}
        }
    }

    private void insterDate(cmScShItem cmScShItem, CmScShType_Dic cmScShType_Dic)
            throws SQLException {
//		conne = new DatabaseHelper(mContext);
//		state = conne.getWritableDatabase();

        // 插入
        String sqlStr = "insert into perSh_aj (cmSchedId,accountId,cmScShType,cmScShTypeDescr,cmScShItem,cmScShItemDescr,cmScShCheck,cmScShIsOld) "
                + "values('"
                + custInfo.getCmSchedId()
                + "', "
                + "'"
                + custInfo.getAccountId()
                + "', "
                + "'"
                + cmScShType_Dic.getCmScShType()
                + "', "
                + "'"
                + cmScShType_Dic.getCmScShTypeDescr()
                + "', "
                + "'"
                + cmScShItem.getCmScShItem()
                + "', "
                + "'"
                + cmScShItem.getCmScShItemDescr() + "', " + "'Y' , 'N')"; // Y
        // 有异常
        // N
        // 不是历史隐患

        instance.execSQL(sqlStr);

//		if (state != null) {
//			try {
//				state.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//
//		}
//		if (conne != null) {
//			try {
//				conne.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//
//		}

    }

    // 提交
    public void submitData(String name, String userid, String values) {
        String sql = "";
        if (name.equals("表具")) {
            sql = "update anjian_data set bj_yes=" + "'" + values + "'"
                    + " where user_id=" + "'" + userid + "'" + "";
        } else if (name.equals("阀门")) {
            sql = "update anjian_data set lgfm_yes=" + "'" + values + "'"
                    + " where user_id=" + "'" + userid + "'" + "";
        } else if (name.equals("灶具连接管")) {
            sql = "update anjian_data set rg_yes=" + "'" + values + "'"
                    + " where user_id=" + "'" + userid + "'" + "";
        } else if (name.equals("热水器")) {
            sql = "update anjian_data set rsq_yes=" + "'" + values + "'"
                    + " where user_id=" + "'" + userid + "'" + "";
        } else if (name.equals("支管")) {
            sql = "update anjian_data set zhiguan_yes=" + "'" + values + "'"
                    + " where user_id=" + "'" + userid + "'" + "";
        } else if (name.equals("采暖炉")) {
            sql = "update anjian_data set bjq_yes=" + "'" + values + "'"
                    + " where user_id=" + "'" + userid + "'" + "";
        } else if (name.equals("灶具")) {
            sql = "update anjian_data set zj_yes=" + "'" + values + "'"
                    + " where user_id=" + "'" + userid + "'" + "";
        } else if (name.equals("其他")) {
            sql = "update anjian_data set qita_yes=" + "'" + values + "'"
                    + " where user_id=" + "'" + userid + "'" + "";
        }

        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//		db = new DatabaseHelper(mContext);
        // 查询数据返回游标对象
        instance.execSQL(sql);

    }

    // 查询是否有异常项
    public String selectYCValues(String name, String userid) {
        String sql = "select * from anjian_data where user_id=" + "'" + userid
                + "'" + "";
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
//		db = new DatabaseHelper(mContext);
        // 查询数据返回游标对象
        // 查询数据返回游标对象
        Cursor c = instance.rawQuery(sql, null);
        // 到了提交的时刻了
        String vString = "";
        while (c.moveToNext()) {
            if (name.equals("表具")) {
                vString = c.getString(50);
            } else if (name.equals("阀门")) {
                vString = c.getString(51);
            } else if (name.equals("灶具连接管")) {
                vString = c.getString(53);
            } else if (name.equals("热水器")) {
                vString = c.getString(54);
            } else if (name.equals("支管")) {
                vString = c.getString(57);
            } else if (name.equals("采暖炉")) {
                vString = c.getString(55);
            } else if (name.equals("灶具")) {
                vString = c.getString(52);
            } else if (name.equals("其他")) {
                vString = c.getString(56);
            }

        }
        c.close();
        return vString;
    }

    // 在隐患信息表中查询当前隐患类型中是否有异常选项,初始化spinner,根据当前的异常类型进行查询
    public ArrayList<String> getYCValues(CustInfo_AnJian custInfo) {

        String sqlString = "select distinct  cmScShCheck,cmScShItemDescr from perSh_aj where  cmSchedId = '"
                + custInfo.getCmSchedId()
                + "' and accountId = '"
                + custInfo.getAccountId()
                + "' and cmScShCheck = 'Y' and cmScShType = '" + code + "'";
        ArrayList<String> arrayList_YC = new ArrayList<String>();

        Cursor result_set = null;
        String isSafe = null;
        try {
//			conne = new DatabaseHelper(mContext);
//			state = conne.getWritableDatabase();
            result_set = instance.rawQuery(sqlString, null);
            if (result_set.moveToFirst()) {
                isSafe = result_set.getString(0);
                arrayList_YC.add(result_set.getString(1));
                while (result_set.moveToNext()) {
                    isSafe = result_set.getString(0);
                    arrayList_YC.add(result_set.getString(1));
                }
            }

//			while (result_set.moveToNext()) {
//				isSafe = result_set.getString(0);
//				arrayList_YC.add(result_set.getString(1));
//			}
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (result_set != null) {

                try {
                    result_set.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
//			if (state != null) {
//
//				try {
//					state.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (conne != null) {
//
//				try {
//					conne.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
        }

        return arrayList_YC;
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
            if (object instanceof UIInput) {
                UIInput uiInput = (UIInput) object;
                uiInput.value = s.toString();
                uiInput.tag.value = s.toString();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (instance != null) {
            try {
                instance.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (databaseHelperInstance.helper != null) {
            try {
                databaseHelperInstance.helper.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
