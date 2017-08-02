package cn.sbx.deeper.moblie.fargments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sunboxsoft.monitor.utils.PerfUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.sbx.deeper.moblie.ExitApplication;
import cn.sbx.deeper.moblie.activity.SinopecAllMenuActivity;
import cn.sbx.deeper.moblie.activity.SwitchMenuActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.SQLiteData;
import cn.sbx.deeper.moblie.domian.ContactPeople;
import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.logic.MainProcess;
import cn.sbx.deeper.moblie.util.APNUtils;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCache;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.DateHepler;
import cn.sbx.deeper.moblie.util.FileCache;
import cn.sbx.deeper.moblie.util.MenuTypeUtil;
import cn.sbx.deeper.moblie.util.TextUtils;
import cn.sbx.deeper.moblie.util.UserInfo;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.OpenFolder;
import cn.sbx.deeper.moblie.view.ProgressHUD;
import petrochina.ghzy.a10fieldwork.R;

/**
 * @author terry.C
 */
public class MainMenuFragment extends BaseFragment implements OnClickListener,
        OnItemClickListener {
    private List<Object> menus = new ArrayList<Object>();
    private SquaredTypeAdapter typeAdapter;
    // private ImageLoader imageLoader;
    int column = 0;
    private String menuDisplayType;
    GridView gv_squared_menu, gv_squared_contact;
    OpenFolder openFolder;
    HorizontalScrollView folderview;
    private LayoutInflater inflater;
    private LinearLayout ll_open_container;
    private Context mContext;
    private ImageView iv_logout;
    ProgressBar progressBar;
    int count = 0;
    private SharedPreferences sp;
    TextView username_text;
    TextView usertime_text;
    private TextView userxingqi_text;
    private View rootView;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    // 呼叫
    public static final String ACTION_CALL = "com.zed3.sipua.call";
    public static final String CALL_TYPE = "call_type";
    public static final String NUMBER = "number";
    public static final int AUDIO_CALL = 1;
    public static final int VIDEO_CALL = 2;
    // 登陆
    public static final String ACTION_LOGIN = "com.zed3.sipua.login_gqt";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PROXY = "proxy";
    public static final String PORT = "port";
    private boolean isExpand = false;

    // 查询待办数据
    public SQLiteDatabase db;// 操作数据库的工具类
    StringBuilder sb;

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    public int selectCBSize() {
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        db = SQLiteDatabase.openOrCreateDatabase(Constants.db_path, null);
        String sqlstring = "select * from custInfo where cmMrState='1'";
        // 查询数据返回游标对象e
        Cursor c1 = db.rawQuery(sqlstring, null);
        int i = c1.getCount();
        c1.close();
        return i;
    }

    public int selectCBSizeAccount() {
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        Connection db = null;
        Statement state = null;
        int i = 0;
//		try {
//			db = SQLiteData.openOrCreateDatabase();
//			state = db.createStatement();
//			String sqlstring = "select * from custInfo ";
//			// String sqlstring = "select * from custInfo where cmMrState='1'";
//			// 查询数据返回游标对象e
//			ResultSet c1 = state.executeQuery(sqlstring);
//			c1.last();// 将指针移动至最后一行
//			i = c1.getRow();// 获取当前行号
//			c1.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			if (state != null) {
//				try {
//					state.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if (db != null) {
//					try {
//						db.close();
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//			}
//		}
        return i;
    }

    public int selectAJSize() {
        Connection db = null;
        Statement state = null;
        int i = 0;
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        try {
            db = SQLiteData.openOrCreateDatabase();

            state = db.createStatement();
            String sqlstring = "select * from custInfo_ju_aj";
            // 查询数据返回游标对象
            ResultSet c1 = state.executeQuery(sqlstring);
            c1.last();
            i = c1.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public int selectWXSize() {
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        db = SQLiteDatabase.openOrCreateDatabase(Constants.db_path, null);
        String sqlstring = "select * from weixiu_data";
        // 查询数据返回游标对象
        Cursor c1 = db.rawQuery(sqlstring, null);
        int i = c1.getCount();
        c1.close();
        return i;
    }

    public void updateAJ() {
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        db = SQLiteDatabase.openOrCreateDatabase(Constants.db_path, null);
        String sqlstring = "update anjian_data set state='2' where state='3'";
        // 查询数据返回游标对象
        db.execSQL(sqlstring);

    }

    public void updateCB() {
        // (数据库所在的路径,游标工厂(null表示使用默认的游标工厂))
        db = SQLiteDatabase.openOrCreateDatabase(Constants.db_path, null);
        String sqlstring = "update chaobiao_data set state='2' where state='3'";
        // 查询数据返回游标对象
        db.execSQL(sqlstring);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        sp = mContext.getSharedPreferences("sys_config", Context.MODE_PRIVATE);
        openFolder = OpenFolder.getInstance(getActivity());
        inflater = LayoutInflater.from(getActivity());
        // imageLoader = new ImageLoader(getActivity());
        menus = DataCache.sinopecMenu.menuObject;
        try {
            if (DataCache.sinopecMenu != null)
                column = Integer.parseInt(DataCache.sinopecMenu.columns);
        } catch (Exception e) {
            column = 0;
            e.printStackTrace();
        }
        if (DataCache.sinopecMenu != null) {
            menuDisplayType = DataCache.sinopecMenu.itemTemplate;
        }

        if (column != 0)
            gv_squared_menu.setNumColumns(2);
        gv_squared_menu.setAdapter(typeAdapter = new SquaredTypeAdapter(menus));
        gv_squared_menu.setOnItemClickListener(this);
        // TextView tv_squared_userinfo = (TextView)
        // findViewById(R.id.tv_squared_userinfo);
        // tv_squared_userinfo.setText(getString(R.string.tv_squared_title_userinfo,
        // UserInfo.getInstance().getUsername()));
        registerChildReceivers();
        // skinManager = new SkinManager(mContext);
        // display = getActivity().getWindowManager().getDefaultDisplay();
        // sp = mContext.getSharedPreferences("sys_config",
        // Context.MODE_PRIVATE);
        // if (savedInstanceState != null) {
        // System.out.println("savedInstanceState.getInt()"
        // + savedInstanceState.getInt("curChoice", 0));
        // }
        // loadData();
        IntentFilter intentFilter = new IntentFilter(
                Constants.MODIFY_APP_MENU_NUM);
        getActivity().registerReceiver(mainReceiver, intentFilter);

        // 时间太长了，把加载进度条取消掉。。。。
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, 20 * 1000);
        IntentFilter intentfreshFilter = new IntentFilter(
                Constants.GET_APP_MENU_REFRESH);
        getActivity().registerReceiver(mainfreshReceiver, intentfreshFilter);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        username_text.setText(sp.getString("username", ""));
        usertime_text.setText(date);
        userxingqi_text.setText(DateHepler.StringData());

        // 数据库的目录
//		String dirPath = "/data/data/com.sunboxsoft.deeper.moblie.app.chaobiao/databases/";
        String dirPath = "/data/data/GuiHuaZongYuan.guandaoA10Ukey.app/databases/";
        File dir = new File(dirPath);
        if (dir.exists()) {
            DataCache.taskCount.put("10194", selectCBSizeAccount());
            // DataCache.taskCount.put("10201", selectAJSize());
            // DataCache.taskCount.put("10197", selectWXSize());
            List<String[]> list = new ArrayList<String[]>();
            String[] strings = new String[2];
            // String[] strings1 = new String[2];
            // String[] strings2 = new String[2];
            strings[0] = "10194";
            strings[1] = selectCBSizeAccount() + "";
            // strings1[0] = "10201";
            // strings1[1] = selectAJSize() + "";
            // strings2[0] = "10197";
            // strings2[1] = selectWXSize() + "";
            Intent intentNum = new Intent(Constants.MODIFY_APP_MENU_NUM);
            intentNum.putExtra("type", "updateTabNum");
            intentNum.putExtra("value", (Serializable) list);
            mActivity.sendBroadcast(intentNum);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // System.out.println("MainMenuFragment.onCreateView()");
        rootView = inflater.inflate(R.layout.layout_left_menu, null);
        username_text = (TextView) rootView.findViewById(R.id.username_text);
        usertime_text = (TextView) rootView.findViewById(R.id.usertime_text);
        userxingqi_text = (TextView) rootView
                .findViewById(R.id.userxingqi_text);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        gv_squared_menu = (GridView) rootView
                .findViewById(R.id.gv_squared_menu);
        gv_squared_contact = (GridView) rootView
                .findViewById(R.id.gv_squared_contact);
        iv_logout = (ImageView) rootView.findViewById(R.id.iv_logout);
        re_contact_person = (RelativeLayout) rootView
                .findViewById(R.id.re_contact_person);
        ll_contact_detail = (LinearLayout) rootView
                .findViewById(R.id.ll_contact_detail);

        ll_contact_detail.setVisibility(View.GONE);
        re_contact_person.setOnClickListener(this);

        iv_logout.setOnClickListener(this);
        new ContactTask().execute(UserInfo.getInstance().getUsername());
        return rootView;
    }

    View backView;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getActivity().getParent().findViewById(R.id.ll_tabhost_bottom)
                .setVisibility(View.VISIBLE);
        //

        //
        if (falgarg != -1) {
            // rootView.invalidate();
            // final Handler handler = new Handler();
            // new Thread(new Runnable() {
            // @Override
            // public void run() {
            // handler.post(new Runnable() {
            // public void run() {
            // rootView.postInvalidate();
            // }
            // });
            // }
            // }).start();
            Constants.backView = getActivity().getWindow().getDecorView();
            int[] location = new int[2];
            gv_squared_menu.getChildAt(falgarg).getLocationOnScreen(location);
            int y = location[1];
            WindowManager wm = (WindowManager) getActivity().getSystemService(
                    Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            openFolder.openFolderView2(gv_squared_menu.getChildAt(falgarg),
                    Constants.backView, 0, folderview, 105, y < height / 2 ? 1
                            : 0);//
            // openFolder.openFolderView(y,Constants.backView, folderview, 105,
            // y<height/2?1:0);
            falgarg = -1;
        }
        // if (falgarg!=-1) {
        // // typeAdapter.notifyDataSetChanged();
        // rootView.invalidate();
        // gv_squared_menu.performItemClick(gv_squared_menu.getChildAt(falgarg),
        // falgarg, gv_squared_menu.getItemIdAtPosition(falgarg));
        // }
        // if (b) {
        // openFolder.prepareLayout();
        // openFolder.startOpenAnimation();
        // }
        // Handler handler = new Handler();
        // Runnable runnable = new Runnable() {
        // @Override
        // public void run() {
        // DataCache.sinopecMenu.menuObject.clear();
        // }
        // };
        // handler.postDelayed(runnable, 10000);// 打开定时器，执行操作

        // 这个效果在baseFragment里做了
        // if(DataCache.sinopecMenu.menuObject==null ||
        // DataCache.sinopecMenu.menuObject.size()==0){
        // Intent intent = new Intent(getActivity(), SwitchMenuActivity.class);
        // startActivity(intent);
        // getActivity().finish();
        // }
    }

    private void registerChildReceivers() {
        for (int i = 0; i < menus.size(); i++) {
            Constants.setBroadcastNamePairs(i);
        }
    }

    private ViewHolder holder;

    static class ViewHolder {
        ImageView imageView;
        TextView text;
        TextView phone;
        TextView count;
        TextView tv_msg_count1;
    }

    public void perpareIndicator(ViewHolder view, String iconName,
                                 String iconName1, final String text, final String count,
                                 final String mclass) {
        final ImageView ivIcon = view.imageView;
        TextView textView = view.text;
        final TextView tv_msg_count = view.count;
        view.tv_msg_count1.setVisibility(View.GONE);

        if (count == null || "0".equals(count) || "".equals(count)) {
            tv_msg_count.setVisibility(View.GONE);
            tv_msg_count.setBackgroundResource(R.drawable.ic_num_loading);
        } else {
            tv_msg_count.setVisibility(View.VISIBLE);
            tv_msg_count.setText(count);
            tv_msg_count.setBackgroundResource(R.drawable.frame_bg);
        }

        // Handler handler = new Handler();
        // Runnable runnable = new Runnable() {
        // @Override
        // public void run() {
        // if (count == null || "0".equals(count) || "".equals(count)) {
        // if (MainProcess.numsList.contains(mclass)) {
        // tv_msg_count.setVisibility(View.GONE);
        // tv_msg_count.setBackgroundResource(R.drawable.ic_num_loading);
        // }
        // }
        // }
        // };
        // handler.postDelayed(runnable, 1000);// 打开定时器，执行操作

        textView.setText(text);

        // imageLoader.displayImage2(iconName, ivIcon,
        // R.drawable.ic_tab_default);
        // String urlString = Scheme.ASSETS.wrap(iconName);

        if (PerfUtils.getBoolean(getActivity(), "isOnline", true)) {
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(iconName, ivIcon, options,
                            animateFirstListener);
        } else {
            // 显示模块图片
            String urlString = "";
            if ("同步数据".equals(text)) {
                urlString = Scheme.ASSETS.wrap("c.png");
            } else if ("抄表".equals(text)) {
                urlString = Scheme.ASSETS.wrap("a.png");
            } else if ("安检".equals(text)) {
                urlString = Scheme.ASSETS.wrap("b.png");
            } else if ("维修".equals(text)) {
                urlString = Scheme.ASSETS.wrap("d.png");
            } else if ("系统设置".equals(text)) {
                urlString = Scheme.ASSETS.wrap("e.png");
            }
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(urlString, ivIcon, options,
                            animateFirstListener);
        }

        switch (MenuTypeUtil.chooseMenuDisplayType(menuDisplayType)) {
            case PICTURE:
                textView.setVisibility(View.GONE);
                break;
            case WORD:
                ivIcon.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                break;
            default:
                textView.setVisibility(View.GONE);
                break;
        }

        // StateListDrawable drawable =
        // skinManager.newSelectorFromResourceByMenu(mContext, iconName,
        // iconName1, screenWidth/3, screenWidth/3);
        // ivIcon.setBackgroundDrawable(drawable);
    }

    public void perpareIndicator(ImageView ivIcon, TextView textView,
                                 TextView tv_msg_count, String iconName, String iconName1,
                                 final String text) {
        // final ImageView ivIcon = view.imageView;
        // TextView textView = view.text;
        // TextView tv_msg_count = view.count;
        // if (count == null || "0".equals(count) || "".equals(count)) {
        // tv_msg_count.setVisibility(View.GONE);
        // } else {
        // tv_msg_count.setVisibility(View.VISIBLE);
        // }
        textView.setText(text);
        // imageLoader.displayImage2(iconName, ivIcon,
        // R.drawable.ic_tab_default);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                .displayImage(iconName, ivIcon, options, animateFirstListener);

        switch (MenuTypeUtil.chooseMenuDisplayType(menuDisplayType)) {
            case PICTURE:
                textView.setVisibility(View.GONE);

                break;
            case WORD:
                ivIcon.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                break;
            default:
                textView.setVisibility(View.GONE);
                break;
        }

        // StateListDrawable drawable =
        // skinManager.newSelectorFromResourceByMenu(mContext, iconName,
        // iconName1, screenWidth/3, screenWidth/3);
        // ivIcon.setBackgroundDrawable(drawable);
    }

    String iconName = "";
    String iconname1 = "";
    String titleName = "";

    private class SquaredTypeAdapter extends BaseAdapter {

        List<Object> menus = new ArrayList<Object>();

        public SquaredTypeAdapter(List<Object> menus) {
            for (Object object : menus) {
                if (object instanceof SinopecMenuModule) {
                    if (((SinopecMenuModule) object).isGroupModule) {
                        continue;
                    }
                }
                if (!((SinopecMenuModule) object).caption
                        .equalsIgnoreCase("巡检模块")) {
                    this.menus.add(object);
                }

            }
        }

        @Override
        public int getCount() {
            count = menus.size();
            return menus.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) (getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                        .inflate(R.layout.item_syn_nav_squared_menu, null);
                // convertView.setLayoutParams(new AbsListView.LayoutParams(
                // (int) getResources().getDimension(
                // R.dimen.main_menu_item_width),
                // (int) getResources().getDimension(
                // R.dimen.main_menu_item_height)));
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView
                        .findViewById(R.id.iv_syn_icon);
                holder.text = (TextView) convertView
                        .findViewById(R.id.tv_syn_text);
                holder.count = (TextView) convertView
                        .findViewById(R.id.tv_msg_count);
                holder.tv_msg_count1 = (TextView) convertView
                        .findViewById(R.id.tv_msg_count1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String iconName = "";
            String iconname1 = "";
            String titleName = "";
            Object o = menus.get(position);

            if (o instanceof SinopecMenuGroup) {
                SinopecMenuGroup group = (SinopecMenuGroup) o;
                iconName = group.itemImg1;
                iconname1 = group.itemImg2;
                titleName = group.caption;


                // added by wangst
                // boolean flag = false;
                // for (int i = 0; i < group.menuobjObjects.size(); i++) {
                // final SinopecMenuModule module = (SinopecMenuModule)
                // group.menuobjObjects
                // .get(i);
                // if(MainProcess.numsList.contains(((SinopecMenuModule)
                // module).mClass)){
                // flag = true;
                // }
                // }
                // if(flag){
                // holder.count.setVisibility(View.VISIBLE);
                // holder.count.setBackgroundResource(R.drawable.ic_num_loading);
                // holder.count.setText("");
                // }
                // else
                // holder.count.setVisibility(View.GONE);

                perpareIndicator(holder, iconName, iconname1, titleName,
                        calculateTabNumber(group), titleName);
            } else if (o instanceof SinopecMenuModule) {
                SinopecMenuModule module = (SinopecMenuModule) o;
                iconName = module.itemImg1;
                iconname1 = module.itemImg2;
                titleName = module.caption;


                URL url = null;
                try {
                    url = new URL(iconName);
                    URL url1 = new URL(WebUtils.rootUrl);//登录设置的url
                    String replacement = url1.getHost();
                    if (WebUtils.rootUrl.contains("127.0.0.1")) {//加密机通道,地址含有端口号
//                        replacement += ":" + url1.getPort();
                    }

                    //将图片主机名改为加密机通道.
                    iconName = iconName.replace(url.getHost(), replacement);//

                    System.out.println("url1.getHost():" + url1.getHost() + " url1.getPort():" + url1.getPort());
                    System.out.println("菜单url: " + iconName);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                // added by wangst
                if (MainProcess.numsType[0]
                        .equalsIgnoreCase((((SinopecMenuModule) o).mClass))) {
                    holder.count
                            .setBackgroundResource(R.drawable.ic_num_loading);
                    holder.count.setText("");
                    holder.count.setVisibility(View.VISIBLE);
                    holder.tv_msg_count1.setVisibility(View.VISIBLE);
                }
                // else
                // holder.count.setVisibility(View.GONE);

                if (DataCache.taskCount.containsKey(module.id) && !titleName.equals("安检") && !titleName.equals("抄表")) {
                    int count = DataCache.taskCount.get(module.id);
                    System.out.println("view in count : " + count);
                    perpareIndicator(holder, iconName, iconname1, titleName,
                            String.valueOf(count), module.mClass);
                }

//				隐藏数字
                /*else if (titleName.equals("抄表")) {
                    // 显示抄表模块的待抄数量
					perpareIndicator(holder, iconName, iconname1, titleName, ""
							+ selectCBSizeAccount() + "", module.mClass);
				} else if (titleName.equals("安检")) {
					// 显示抄表模块的待抄数量
					perpareIndicator(holder, iconName, iconname1, titleName, ""
							+ selectAJSize() + "", module.mClass);
				} */
                else {

                    perpareIndicator(holder, iconName, iconname1, titleName,
                            "0", module.mClass);
                }
                // perpareIndicator(view, iconName,
                // titleName, "");
            }

            return convertView;
        }
    }

    private boolean b = false;
    private int falgarg = -1;

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                            long arg3) {
        Object o = menus.get(arg2);
        if (o instanceof SinopecMenuGroup) {
            // SinopecMenuGroup group = (SinopecMenuGroup) o;
            // Intent groupIntent = new Intent(getActivity(),
            // MobileOAGroupActivity.class);
            // groupIntent.putExtra("entry", group);
            // groupIntent.putExtra("pageIndex", arg2);
            // groupIntent.putExtra("backprecious", "squared");
            // startActivity(groupIntent);

            // folderview = (LinearLayout)
            // inflater.inflate(R.layout.layout_seperator_footview, null);
            // final String titleCode = mainMenus.get(arg2).get(2);
            // ApprovalGroup approvalGroup = DataCache.groups.get(titleCode);
            // List<ApprovalItem> items = approvalGroup.getItem();
            // for(int i=0; i<items.size(); i++) {
            // final ApprovalItem approvalItem = items.get(i);
            // Button button = new Button(mContext);
            // button.setText(approvalItem.getCaption());
            // button.setOnClickListener(new OnClickListener() {
            // @Override
            // public void onClick(View v) {
            // openFolder.dismiss();
            // chooseBroadcastType(titleCode,approvalItem);
            // MainActivity.horizontalScrollView.scrollTo(100 * arg2, 0);
            // MainActivity.setCurrentTab(arg2 + 1);
            // }
            // });
            // folderview.addView(button,
            // LinearLayout.LayoutParams.WRAP_CONTENT,
            // LinearLayout.LayoutParams.WRAP_CONTENT);
            // }
            // openFolder.openFolderView(arg1, getActivity().getParent()
            // .getWindow().getDecorView(), folderview, 70, 1);
            // openFolder
            // .setmOnFolderClosedListener(new
            // OpenFolder.OnFolderClosedListener() {
            // @Override
            // public void onClosed() {
            // // Toast.makeText(getBaseContext(),
            // // "close folder btn1", Toast.LENGTH_SHORT)
            // // .show();
            // }
            // });

            folderview = (HorizontalScrollView) inflater.inflate(
                    R.layout.layout_seperator_footview, null);
            // LinearLayout.LayoutParams paramsfolder = new
            // LinearLayout.LayoutParams(
            // LayoutParams.MATCH_PARENT,getResources().getDimensionPixelOffset(R.dimen.main_bar_item_height));
            // folderview.setLayoutParams(paramsfolder);
            ll_open_container = (LinearLayout) folderview
                    .findViewById(R.id.ll_open_container);
            final SinopecMenuGroup group = (SinopecMenuGroup) DataCache.sinopecMenu.menuObject
                    .get(arg2);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    openFolder.dip2px(mContext, 90), openFolder.dip2px(
                    mContext, 90));
            params.leftMargin = openFolder.dip2px(mContext, 10);
            params.gravity = Gravity.CENTER_VERTICAL;
            params.topMargin = openFolder.dip2px(mContext, 5);
            params.bottomMargin = openFolder.dip2px(mContext, 8);
            for (int i = 0; i < group.menuobjObjects.size(); i++) {
                final SinopecMenuModule module = (SinopecMenuModule) group.menuobjObjects
                        .get(i);
                if (!module.isGroupModule) {
                    View v = inflater.inflate(R.layout.layout_openfolder_item,
                            null);
                    TextView tv_syn_text = (TextView) v
                            .findViewById(R.id.tv_syn_text);
                    TextView tv_msg_count = (TextView) v
                            .findViewById(R.id.tv_msg_count);
                    ImageView iv_syn_icon = (ImageView) v
                            .findViewById(R.id.iv_syn_icon);
                    tv_syn_text.setText(module.caption);
                    // StateListDrawable bitmapDrawable =
                    // skinManager.newSelectorFromResourceByMenu(mContext,
                    // module.itemImg1.substring(0,
                    // module.itemImg1.indexOf(".")),module.itemImg2.substring(0,
                    // module.itemImg2.indexOf(".")), display.getWidth() / 3,
                    // display.getWidth() / 3);
                    if (DataCache.taskCount.containsKey((module.id))
                            && DataCache.taskCount.get((module.id)) != null
                            && DataCache.taskCount.get((module.id)) > 0) {
                        tv_msg_count.setVisibility(View.VISIBLE);
                        tv_msg_count
                                .setText(TextUtils.parseText(String
                                        .valueOf((DataCache.taskCount
                                                .get((module.id))))));
                    } else {
                        tv_msg_count.setVisibility(View.GONE);
                    }
                    // iv_syn_icon.setBackgroundDrawable(bitmapDrawable);
                    iv_syn_icon.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openFolder.dismiss();
                            falgarg = arg2;
                            b = true;
                            Constants.istab = !module.caption
                                    .equals(((SinopecMenuModule) group.menuobjObjects
                                            .get(0)).caption);
                            Constants.menuModule = module;
                            System.out.println("arg:" + arg2 + " "
                                    + module.mClass + " " + module.caption);
                            // deleted on 1031
                            // Constants.currentGroupModule = module.caption;
                            SinopecAllMenuActivity.setCurrentTab(arg2 + 1);

                            chooseBroadcastType(arg2, module.mClass, module);
                            SinopecAllMenuActivity.horizontalScrollView
                                    .smoothScrollBy(100 * arg2, 0);
                            // if(currentGroupModule.equals(module.caption)){
                            // }else
                            // currentGroupModule = module.caption;
                            //
                            // if(isFirst){
                            // currentGroupModule = module.caption;
                            // SinopecAllMenuActivity.setCurrentTab(arg2 + 1);
                            // isFirst = false;
                            // }
                            // Handler handler = new Handler();
                            // Runnable runnable = new Runnable(){
                            // @Override
                            // public void run() {
                            // // TODO Auto-generated method stub
                            // // 在此处添加执行的代码
                            // //
                            // System.out.print("===============================2000");
                            // }
                            // };
                            // handler.postDelayed(runnable, 100);// 打开定时器，执行操作

                        }
                    });
                    perpareIndicator(iv_syn_icon, tv_syn_text, tv_msg_count,
                            module.itemImg1, module.itemImg2, module.caption);
                    ll_open_container.addView(v, params);
                }
                // Button button = new Button(mContext);
                // button.setText(module.caption);
                // button.setOnClickListener(new OnClickListener() {
                // @Override
                // public void onClick(View v) {
                // openFolder.dismiss();
                // chooseBroadcastType(arg2, module.mClass, module);
                // MainActivity.horizontalScrollView.smoothScrollBy(
                // 100 * arg2, 0);
                // MainActivity.setCurrentTab(arg2 + 1);
                // }
                // });
                // folderview.addView(button,
                // LinearLayout.LayoutParams.WRAP_CONTENT,
                // LinearLayout.LayoutParams.WRAP_CONTENT);
            }
            int[] location = new int[2];
            arg1.getLocationOnScreen(location);
            // int x = location[0];
            int y = location[1];
            WindowManager wm = (WindowManager) getActivity().getSystemService(
                    Context.WINDOW_SERVICE);

            // int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            // if(menus.size()>6){
            // double j = 3 *( Math.ceil(menus.size()/3.0) - 1) ;
            // openFolder.openFolderView(arg1, getActivity().getParent()
            // .getWindow().getDecorView(), folderview, 105, arg2<j? 1:0);
            // }
            // else
            openFolder.openFolderView(arg1, getActivity().getParent()
                            .getWindow().getDecorView(), getResources()
                            .getDimensionPixelOffset(R.dimen.main_bar_item_height),
                    folderview, 105, y < height / 2 ? 1 : 0);

            openFolder
                    .setmOnFolderClosedListener(new OpenFolder.OnFolderClosedListener() {
                        @Override
                        public void onClosed() {
                            // Toast.makeText(getBaseContext(),
                            // "close folder btn1", Toast.LENGTH_SHORT)
                            // .show();
                            // b=false;
                            // falgarg=-1;

                        }
                    });

        } else if (o instanceof SinopecMenuModule) {
            SinopecMenuModule module = (SinopecMenuModule) o;
            // Intent moduleIntent = new Intent(getActivity(),
            // MobileOAAllActivity.class);
            // moduleIntent.putExtra("entry", module);
            // moduleIntent.putExtra("pageIndex", arg2);
            // moduleIntent.putExtra("backprecious", "squared");
            // startActivity(moduleIntent);

            // // Intent intent = new Intent(
            // Constants.getBroadcastNamePairsString(arg2));
            // intent.putExtra("entry", module);
            // getActivity().sendBroadcast(intent);

            // Constants.currentGroupModule = module.caption;
            SinopecAllMenuActivity.horizontalScrollView.smoothScrollBy(
                    100 * arg2, 0);
            SinopecAllMenuActivity.setCurrentTab(arg2 + 1);
        }
    }

    protected void chooseBroadcastType(int pageIndex, String titleCode,
                                       SinopecMenuModule module) {
        // Map<String, String> map = Constants.getBroadcastNamePairs();
        // String action = map.get(titleCode);
        // System.out.println("module caption:"+module.caption);
        Intent intent = new Intent(
                Constants.getBroadcastNamePairsString(pageIndex));
        intent.putExtra("entry", module);

        getActivity().sendBroadcast(intent);
    }

    // private static boolean isExit = false;
    // @Override
    // public void onBackPressed() {
    // if (isExit == false) {
    // isExit = true;
    // Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
    // Timer timer = new Timer();
    // timer.schedule(new TimerTask() {
    // @Override
    // public void run() {
    // // TODO Auto-generated method stub
    // isExit = false;
    // }
    // }, 2000);
    // } else {
    // finish();
    // android.os.Process.killProcess(android.os.Process.myPid());
    // }
    // }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_logout:
                showPopupWindow(iv_logout);
                break;
            case R.id.re_contact_person:
                if (!isExpand) {

                    ll_contact_detail.setVisibility(View.VISIBLE);
                    isExpand = true;
                } else {
                    ll_contact_detail.setVisibility(View.GONE);
                    isExpand = false;
                }
                break;
        }
    }

    public void showPopupWindow(View iv_logout) {
        final PopupWindow popupWindow = new PopupWindow(getActivity());
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.popupwindow_menu_top, null);
        LinearLayout ll_view = (LinearLayout) view.findViewById(R.id.ll_view);
        TextView tv_refresh = (TextView) view.findViewById(R.id.tv_refresh);
//		TextView tv_setting = (TextView) view.findViewById(R.id.tv_setting);
//		TextView tv_upload = (TextView) view.findViewById(R.id.upload);
//		TextView tv_download = (TextView) view.findViewById(R.id.download);
//		TextView tv_feedback = (TextView) view.findViewById(R.id.tv_backfeed);
        TextView tv_exit = (TextView) view.findViewById(R.id.tv_exit);
        ll_view.setBackgroundResource(R.drawable.ic_main_menu_top_right_bg);
        // try {
        // ll_view.setBackgroundDrawable(new
        // BitmapDrawable(BitmapFactory.decodeStream(mContext.getAssets().open(
        // Constants.skin_folder + File.separator +
        // "ic_main_menu_top_right_bg.png"))));
        // } catch (Exception e) {
        // // TODO: handle exception
        // }
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(TextUtils.Dp2Px(mContext, 80));
        popupWindow.setHeight(TextUtils.Dp2Px(mContext, 100));
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(null);

        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(iv_logout, 0, TextUtils.Dp2Px(mContext, 5));

        tv_refresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
                // DataCache.sinopecMenu.menuObject.clear();
                Intent intent = new Intent(getActivity(),
                        SwitchMenuActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

//		tv_setting.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				popupWindow.dismiss();
//				// if (count > 0) {
//				// MainActivity.horizontalScrollView.smoothScrollBy(100 * count,
//				// 0);
//				// MainActivity.setCurrentTab(count);
//				// }
//				Intent intent = new Intent(getActivity(), MoreActivity.class);
//				startActivity(intent);
//				getActivity().overridePendingTransition(
//						R.anim.fragment_slide_right_enter,
//						R.anim.fragment_slide_left_exit);
//			}
//		});

//		tv_feedback.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				popupWindow.dismiss();
//				Intent intent = new Intent(getActivity(),
//						FeedBackActivity.class);
//				startActivity(intent);
//				getActivity().overridePendingTransition(
//						R.anim.fragment_slide_right_enter,
//						R.anim.fragment_slide_left_exit);
//			}
//		});
        tv_exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();

                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("确定退出?")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 删除保密附件
                                        FileCache.deleteSecretFile();
                                        // restoreApn();//apn 还原
                                        ExitApplication.getInstance().exit();

                                        // Intent intent=new Intent();
                                        // intent.setClass(mContext,
                                        // LoginActivity.class);
                                        // Editor editor = sp.edit();
                                        // editor.putBoolean("_cbAutoLogon",
                                        // false);
                                        // editor.commit();
                                        // startActivity(intent);
                                        getActivity().finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }).show();
            }
        });

//		// 下载（同步数据）
//		tv_download.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				popupWindow.dismiss();
//
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("entry", Constants.menuModule);
//				Fragment f = new SynchronousDataFragment();
//				f.setArguments(bundle);
//				((ActivityInTab) getActivity()).navigateTo(f);
//				// final ProgressHUD overlayProgress = AlertUtils.showDialog(
//				// getActivity(), null, null, false);
//				// // 时间太长了，把加载进度条取消掉。。。。
//				// Handler handler = new Handler();
//				// handler.postDelayed(new Runnable() {
//				//
//				// @Override
//				// public void run() {
//				// // TODO Auto-generated method stub
//				// if (overlayProgress != null) {
//				// overlayProgress.dismiss();
//				// }
//				//
//				// // new SynchronousDataTask().execute();
//				//
//				// importInitDatabase();
//				// DataCache.taskCount.put("10194", selectCBSize());
//				// // DataCache.taskCount.put("10201", selectAJSize());
//				// // DataCache.taskCount.put("10197", selectWXSize());
//				// List<String[]> list = new ArrayList<String[]>();
//				// String[] strings = new String[2];
//				// // String[] strings1 = new String[2];
//				// // String[] strings2 = new String[2];
//				// strings[0] = "10194";
//				// strings[1] = selectCBSize() + "";
//				// // strings1[0] = "10201";
//				// // strings1[1] = selectAJSize() + "";
//				// // strings2[0] = "10197";
//				// // strings2[1] = selectWXSize() + "";
//				//
//				// Intent intentNum = new Intent(
//				// Constants.MODIFY_APP_MENU_NUM);
//				// intentNum.putExtra("type", "updateTabNum");
//				// intentNum.putExtra("value", (Serializable) list);
//				// mActivity.sendBroadcast(intentNum);
//				// mActivity.sendBroadcast(new Intent(
//				// Constants.MODIFY_APP_MENU_NUM));
//				// Toast.makeText(getActivity(), "下载完成", 1).show();
//				// }
//				// }, 2000);
//
//			}
//		});
//		// 上传
//		tv_upload.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				popupWindow.dismiss();
//				final ProgressHUD overlayProgress = AlertUtils.showDialog(
//						getActivity(), null, null, false);
//				// 时间太长了，把加载进度条取消掉。。。。
//				Handler handler = new Handler();
//				handler.postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if (overlayProgress != null) {
//							overlayProgress.dismiss();
//						}
//						updateAJ();
//						updateCB();
//						Toast.makeText(getActivity(), "上传完成", 1).show();
//					}
//				}, 2000);
//			}
//		});
    }

    public String calculateTabNumber(SinopecMenuGroup group) {
        int result = 0;
        for (int i = 0; i < group.menuobjObjects.size(); i++) {
            final SinopecMenuModule module = (SinopecMenuModule) group.menuobjObjects
                    .get(i);
            if (DataCache.taskCount.containsKey(module.id)) {
                int count = DataCache.taskCount.get(module.id);
                result += count;
            }
        }
        return String.valueOf(result);
    }

    public void restoreApn() {
        // APN设置
        // 将APN接入点还原
        int apnAsDefault = sp.getInt("checked_position", 0);
        if (apnAsDefault == 3) {
            int currApnId = sp.getInt("PREF_CURR_NORMAL_APN_ID", -1);
            int yapnId = -1;
            try {
                yapnId = APNUtils.fetchCurrentApnId();
                APNUtils.setContext(mContext);
                if (-1 != currApnId && currApnId != yapnId) {
                    APNUtils.deleteAPN(yapnId);
                    APNUtils.SetDefaultAPN(currApnId);
                } else {
                    // APN出现错误，提示用户手动修改
                    if (-1 != yapnId) {
                        Intent intent = new Intent(
                                Intent.ACTION_MAIN);
                        intent.setClassName("com.android.settings",
                                "com.android.settings.ApnSettings");
                        startActivity(intent);
                        Toast.makeText(mContext, "请将接入点置回，以正常访问网络！",
                                Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (-1 != yapnId) {
                    Intent intent = new Intent(
                            Intent.ACTION_MAIN);
                    intent.setClassName("com.android.settings",
                            "com.android.settings.ApnSettings");
                    startActivity(intent);
                    Toast.makeText(mContext, "请将接入点置回，以正常访问网络！",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    int nums = 1;
    // 跟新数据条数
    BroadcastReceiver mainReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("typeAdapter refresh");
            if (typeAdapter != null) {
                typeAdapter.notifyDataSetChanged();
            }
            nums++;
            if (DataCache.numsOfMoudleHasNotification != 0) {
                progressBar
                        .incrementProgressBy(100 / DataCache.numsOfMoudleHasNotification);
                if (nums >= DataCache.numsOfMoudleHasNotification) {
                    progressBar.incrementProgressBy(100);
                    progressBar.setVisibility(View.GONE);
                    nums = 1;
                }
            }
        }
    };
    BroadcastReceiver mainfreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            if (null != type && "refresh".equalsIgnoreCase(type)) {
                Intent intent1 = new Intent(getActivity(),
                        SwitchMenuActivity.class);
                startActivity(intent1);
                getActivity().finish();
            }
        }
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mainReceiver != null) {
            getActivity().unregisterReceiver(mainReceiver);
        }
        if (mainfreshReceiver != null) {
            getActivity().unregisterReceiver(mainfreshReceiver);
        }
    }

    /**
     * 图片加载第一次显示监听器
     *
     * @author Administrator
     */
    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private class SquaredContactAdapter extends BaseAdapter {
        List<ContactPeople> contactList = new ArrayList<ContactPeople>();
        Context context;

        public SquaredContactAdapter(Context context,
                                     List<ContactPeople> contactList) {
            this.context = context;
            this.contactList = contactList;
        }

        @Override
        public int getCount() {
            // count = contactList.size();
            return contactList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) (getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                        .inflate(R.layout.item_syn_nav_squared_contact, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView
                        .findViewById(R.id.iv_syn_icon);
                holder.text = (TextView) convertView
                        .findViewById(R.id.tv_name_text);
                holder.phone = (TextView) convertView
                        .findViewById(R.id.tv_phone_text);
                holder.count = (TextView) convertView
                        .findViewById(R.id.tv_msg_count);
                holder.tv_msg_count1 = (TextView) convertView
                        .findViewById(R.id.tv_msg_count1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(contactList.get(position).getName());
            holder.phone.setText(contactList.get(position).getPhone());
            // 拨打电话
            holder.imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialogUpdate(mContext, contactList.get(position));
                }
            });
            return convertView;
        }
    }

    class ContactTask extends AsyncTask<String, Void, List<ContactPeople>> {
        private ProgressHUD overlayProgress;

        @SuppressLint("InlinedApi")
        @Override
        protected void onPostExecute(final List<ContactPeople> result) {
            super.onPostExecute(result);
            if (overlayProgress != null)
                overlayProgress.dismiss();
            if (result != null) {
                gv_squared_contact.setAdapter(new SquaredContactAdapter(
                        getActivity(), result));
            }
            // // 登陆成功之后 发送广播登陆集群通
            // if (!WebUtils.jqtusername.equalsIgnoreCase("")) {
            // Intent intentLogin = new Intent(ACTION_LOGIN);
            // Bundle extras = new Bundle();
            // extras.putString(USERNAME, WebUtils.jqtusername);
            // extras.putString(PASSWORD, WebUtils.jqtusername);
            // extras.putString(PROXY, WebUtils.jqtUrl);
            // extras.putString(PORT, WebUtils.jqtport);
            // intentLogin.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            // intentLogin.putExtras(extras);
            // getActivity().sendBroadcast(intentLogin);
            // }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            overlayProgress = AlertUtils.showDialog(getActivity(), null, this,
                    false);
        }

        @Override
        protected List<ContactPeople> doInBackground(String... params) {
            InputStream inputStream = null;
            List<ContactPeople> contactPeoples = null;
            try {
                inputStream = getActivity().getAssets().open("contact.xml");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String username = params[0];
            return contactPeoples = DataCollectionUtils.getContactList(
                    username, inputStream);

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

    AlertDialog softUpdateDialog;
    private RelativeLayout re_contact_person;
    private LinearLayout ll_contact_detail;

    private void dialogUpdate(final Context mContext,
                              final ContactPeople contactPeople) {
        // 如果本地已经下载完了包
        LayoutInflater factoryHis = LayoutInflater.from(getActivity());// 提示框
        View viewDialog = null;
        viewDialog = factoryHis.inflate(R.layout.operation_dialog_layout, null);
        softUpdateDialog = new AlertDialog.Builder(mContext)
                .setView(viewDialog).setCancelable(false).create();
        softUpdateDialog.setView(viewDialog, 0, 0, 0, 0);
        Button btn_dialog_ok = (Button) viewDialog
                .findViewById(R.id.btn_dialog_ok);
        TextView textView = (TextView) viewDialog.findViewById(R.id.content);
        textView.setText("是否拨打电话:" + contactPeople.getName() + "?");
        textView.setTextSize(18);
        btn_dialog_ok.setOnClickListener(new OnClickListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(String
                        .format("tel:%s", contactPeople.getPhone()))));
                softUpdateDialog.dismiss();
            }
        });
        Button btn_dialog_cancel = (Button) viewDialog
                .findViewById(R.id.btn_dialog_cancel);
        btn_dialog_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), "取消", 1).show();

                softUpdateDialog.dismiss();
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

    @Override
    public void refreshButtonAndText() {
        // TODO Auto-generated method stub
        super.refreshButtonAndText();

        // gv_squared_menu.setAdapter(typeAdapter = new
        // SquaredTypeAdapter(menus));
        typeAdapter.notifyDataSetChanged();
    }

}
