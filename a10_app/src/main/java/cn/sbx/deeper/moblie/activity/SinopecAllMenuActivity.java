package cn.sbx.deeper.moblie.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sunboxsoft.monitor.utils.PerfUtils;

import net.arraynetworks.vpn.Common;
import net.arraynetworks.vpn.VPNManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sbx.deeper.moblie.MobileApplication;
import cn.sbx.deeper.moblie.activity.base.BaseTabActivity;
import cn.sbx.deeper.moblie.activity.base.MobileOAAllActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.db.DatabaseHelper;
import cn.sbx.deeper.moblie.domian.ResultInfo;
import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.SoftInfo;
import cn.sbx.deeper.moblie.interfaces.IApproveNumInterface;
import cn.sbx.deeper.moblie.logic.MainProcess;
import cn.sbx.deeper.moblie.service.LocalService;
import cn.sbx.deeper.moblie.service.LocalService.LocalBinder;
import cn.sbx.deeper.moblie.util.AlertUtils;
import cn.sbx.deeper.moblie.util.DataCache;
import cn.sbx.deeper.moblie.util.DataUtil;
import cn.sbx.deeper.moblie.util.MenuTypeUtil;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.view.SyncHorizontalScrollView;
import petrochina.ghzy.a10fieldwork.R;

/**
 * all菜单样式
 */
public class SinopecAllMenuActivity extends BaseTabActivity implements
        OnClickListener, OnTabChangeListener, IApproveNumInterface {
    private static final String TAG = "SinopecAllMenuActivity";
    public static TabHost tabHost;
    public static SyncHorizontalScrollView horizontalScrollView;
    Context mContext;
    // private ImageLoader imageLoader;
    private static Thread mThreadStopVpn = null;
    private SoftInfo info;
    ImageButton bt_menu_left;
    LinearLayout ll_left_container;
    LinearLayout fl_left_container;
    private String warningPushUsername;
    private String warningPushPassword;
    int indexOfWarningPush = -1;
    /*private QuickActionWidget mGrid;// 快捷栏控件*/
    public DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    MobileApplication application;
    List<Object> menus = new ArrayList<Object>();

    public static String locInfo = "";
    public static String status = "0";
    LocalService localService = null;

    public SinopecAllMenuActivity() {
        mContext = SinopecAllMenuActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = MobileApplication.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_tab_default)
                .cacheInMemory(true).cacheOnDisk(true).build();
        // imageLoader = new ImageLoader(mContext);
        DatabaseHelper.encrypt(SinopecAllMenuActivity.this);
        mainProcess = new MainProcess();
        if (DataCache.sinopecMenu != null) {
            menuDisplayType = DataCache.sinopecMenu.itemTemplate;
        }
        Intent intent = getIntent();
        if (intent != null) {
            warningPushUsername = intent.getStringExtra("warningPushUsername");
            warningPushPassword = intent.getStringExtra("warningPushPassword");
        }
        initMainView();
        IntentFilter intentFilter = new IntentFilter(
                Constants.MODIFY_APP_MENU_NUM);
        registerReceiver(mainReceiver, intentFilter);
        IntentFilter intentTabFilter = new IntentFilter(
                Constants.GET_APP_MENU_TAB);
        registerReceiver(mainTabReceiver, intentTabFilter);
        // openGPSSettings();
        // bindService();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainReceiver != null)
            unregisterReceiver(mainReceiver);
        if (mainTabReceiver != null)
            unregisterReceiver(mainTabReceiver);

        if (Constants.useVPN) {
            stopVpn();
        }
    }

    private View perpareIndicator(String icon1, String icon2, String text,
                                  String id, String bottomShow, String count) {// gridview图标改成图标1
        // tab图标改成图标2
        View view = LayoutInflater.from(this).inflate(
                R.layout.tab_main_nav_indicate, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_syn_text);
        TextView tv_msg_count = (TextView) view.findViewById(R.id.tv_msg_count);
        textView.setTag(id);
        // group 提醒数
        if (count == null || "0".equals(count) || "".equals(count)) {
            tv_msg_count.setVisibility(View.GONE);
        } else {
            tv_msg_count.setVisibility(View.VISIBLE);
            tv_msg_count.setText(count);
        }
        // added
        // for(Object o:DataCache.sinopecMenu.menuObject){
        // if(o instanceof SinopecMenuGroup){
        // if(id.equalsIgnoreCase(((SinopecMenuGroup) o).id)){
        // String counts = calculateTabNumberGroup((SinopecMenuGroup)o);
        // // int result = 0;
        // // for (int k = 0; k < ((SinopecMenuGroup) o).menuobjObjects.size();
        // k++) {
        // // final SinopecMenuModule imodule = (SinopecMenuModule)
        // ((SinopecMenuGroup) o).menuobjObjects
        // // .get(k);
        // // if (DataCache.taskCount.containsKey(imodule.id)) {
        // // int count = DataCache.taskCount.get(imodule.id);
        // // result += count;
        // // }
        // // }
        // // if(result>0){
        // // tv_msg_count.setVisibility(View.VISIBLE);
        // // tv_msg_count.setText(TextUtils.parseText(String.valueOf(result)));
        // // }else{
        // // tv_msg_count.setVisibility(View.GONE);
        // // }
        // System.out.println("counts:"+counts);
        // tv_msg_count.setText(TextUtils.parseText(counts));
        // }
        // }
        // }

        textView.setText(text);
        ImageView iv_syn_icon = (ImageView) view.findViewById(R.id.iv_syn_icon);
        // imageLoader
        // .displayImage2(icon2, iv_syn_icon, R.drawable.ic_tab_default);
        // String urlString = Scheme.ASSETS.wrap(icon2);

        URL url = null;
        try {
            url = new URL(icon2);
            URL url1 = new URL(WebUtils.rootUrl);//登录设置的url

            String replacement = url1.getHost();
            if (WebUtils.rootUrl.contains("127.0.0.1")) {//加密机通道,地址含有端口号
//                replacement += ":" + url1.getPort();
            }
            //将图片主机名改为加密机通道.
            icon2 = icon2.replace(url.getHost(), replacement);//
            System.out.println("底部图片url: " + icon2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (PerfUtils.getBoolean(SinopecAllMenuActivity.this, "isOnline",
                true)) {
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(icon2, iv_syn_icon, options,
                            animateFirstListener);
        } else {
            String urlString = "";
            if ("同步数据".equals(text)) {
                urlString = Scheme.ASSETS.wrap("c1.png");
            } else if ("抄表".equals(text)) {
                urlString = Scheme.ASSETS.wrap("a1.png");
            } else if ("安检".equals(text)) {
                urlString = Scheme.ASSETS.wrap("b1.png");
            } else if ("维修".equals(text)) {
                urlString = Scheme.ASSETS.wrap("d1.png");
            } else if ("系统设置".equals(text)) {
                urlString = Scheme.ASSETS.wrap("e1.png");
            }
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(urlString, iv_syn_icon, options,
                            animateFirstListener);
        }

        switch (MenuTypeUtil.chooseMenuDisplayType(menuDisplayType)) {
            case PICTURE:
                textView.setVisibility(View.GONE);
                break;
            case WORD:
                iv_syn_icon.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        view.setBackgroundResource(R.drawable.bt_main_tab_all_selector);
        view.setLayoutParams(new FrameLayout.LayoutParams((int) getResources()
                .getDimension(R.dimen.main_bar_item_width),
                (int) getResources().getDimension(R.dimen.main_bar_item_height)));
        if ("1".equals(bottomShow)) {
            view.setVisibility(View.VISIBLE);
        } else
            view.setVisibility(View.GONE);
        return view;
    }

    /**
     * 切换Tab
     */
    public static void setCurrentTab(int index) {
        tabHost.setCurrentTab(index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_menu_left:
                tabHost.setCurrentTab(0);
                horizontalScrollView.smoothScrollTo(0, 0);
                break;
            case R.id.fl_left_container:
                tabHost.setCurrentTab(0);
                horizontalScrollView.smoothScrollTo(0, 0);
                break;
            // case R.id.ll_left_container:
            // tabHost.setCurrentTab(0);
            // horizontalScrollView.smoothScrollTo(0, 0);
            // break;
            // case R.id.lv_left_scroll_image:
            // tabHost.setCurrentTab(0);
            // break;
            case R.id.lv_right_scroll_image:
                tabHost.setCurrentTab(tabHost.getTabWidget().getChildCount() - 1);
                horizontalScrollView.scrollTo(
                        horizontalScrollView.getMeasuredWidth(), 0);
            default:
                break;

        }
    }

    private LinearLayout ll_open_container;

    /**
     * 匹配导航布局进入相应的fragment
     */
    public void initMainView() {
        Log.i(TAG, "initMainView()");
        setContentView(R.layout.main_all);
        bt_menu_left = (ImageButton) findViewById(R.id.bt_menu_left);
        bt_menu_left.setOnClickListener(this);
        fl_left_container = (LinearLayout) findViewById(R.id.fl_left_container);
        fl_left_container.setOnClickListener(this);
        // ll_left_container =
        // (LinearLayout)findViewById(R.id.ll_left_container);
        // ll_left_container.setOnClickListener(this);
        horizontalScrollView = (SyncHorizontalScrollView) findViewById(R.id.hl_scroll_main);
        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);
        //
        tabHost.addTab(tabHost.newTabSpec("MOBILE_OFFICE_FIRST")
                .setIndicator("首页")
                .setContent(new Intent(this, MainMenuActivity.class)));

        List<Object> menus = DataCache.sinopecMenu.menuObject;

        // added by wangst
        if (menus == null || menus.size() == 0) {
            // if(DataCache.sinopecMenu.menuObject==null ||
            // DataCache.sinopecMenu.menuObject.size()==0){
            Toast.makeText(mContext, "未获取到菜单", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(SinopecAllMenuActivity.this,
            // SwitchMenuActivity.class);
            // startActivity(intent);
            // finish();
            // return;
        }
        for (int i = 0; i < menus.size(); i++) {
            Object object = menus.get(i);
            Constants.setBroadcastNamePairs(i);
            if (object instanceof SinopecMenuGroup) {
                SinopecMenuGroup group = (SinopecMenuGroup) object;
                SinopecMenuModule module = (SinopecMenuModule) group.menuobjObjects
                        .get(0);
                // System.out.println("group caption:"+group.caption+"  class:");
                // System.out.println("module caption:"+module.caption+"  class:"+module.mClass);
                Log.i(TAG, "group.barImg1:" + group.barImg1
                        + "...group.barImg2:" + group.barImg2);
                Log.i(TAG, "group.layout:" + group.layout);
                Log.i(TAG, "group.itemImg1:" + group.itemImg1
                        + "...group.itemImg2:..." + group.itemImg2 + "....");
                switch (MenuTypeUtil.chooseMenuType(group.layout)) {
                    case TOP:
                    /*tabHost.addTab(tabHost
                            .newTabSpec(String.valueOf(i))
							.setIndicator(
									perpareIndicator(group.itemImg1,
											group.itemImg2, group.caption,
											group.id, "1",
											calculateTabNumberGroup(group)))
							.setContent(
									new Intent(this,
											MobileOAGroupActivity.class)
											.putExtra("entry", group).putExtra(
													"pageIndex", i)));*/
                        break;
                    case SQUARED:
//					tabHost.addTab(tabHost
//							.newTabSpec(String.valueOf(i))
//							.setIndicator(
//									perpareIndicator(group.itemImg1,
//											group.itemImg2, group.caption,
//											group.id, "1",
//											calculateTabNumberGroup(group)))
//							.setContent(
//									new Intent(this,
//											MobileOAGroupActivity.class)
//											.putExtra("entry", group).putExtra(
//													"pageIndex", i)));
                        break;
                    case BOTTOM:// added
                        tabHost.addTab(tabHost
                                .newTabSpec(String.valueOf(i))
                                .setIndicator(
                                        perpareIndicator(group.itemImg1,
                                                group.itemImg2, group.caption,
                                                group.id, "1",
                                                calculateTabNumberGroup(group)))
                                .setContent(
                                        new Intent(this, MobileOAAllActivity.class)
                                                .putExtra("entry", group)
                                                .putExtra("pageIndex", i)
                                                .putExtra("type", "0")));
                        // System.out.println("module.info:"+module.toString());
                        break;
                    case ALL:// added
                        // tabHost.addTab(tabHost
                        // .newTabSpec(String.valueOf(i))
                        // .setIndicator(
                        // perpareIndicator(group.itemImg1,
                        // group.itemImg2, group.caption,
                        // "New"))
                        // .setContent(
                        // new Intent(this,
                        // MobileOAActivity.class)
                        // .putExtra("entry", group).putExtra(
                        // "pageIndex", i)));
                        tabHost.addTab(tabHost
                                .newTabSpec(String.valueOf(i))
                                .setIndicator(
                                        perpareIndicator(group.itemImg1,
                                                group.itemImg2, group.caption,
                                                group.id, group.bottomShow,
                                                calculateTabNumberGroup(group)))
                                .setContent(
                                        new Intent(this, MobileOAAllActivity.class)
                                                .putExtra("entry", group)
                                                .putExtra("pageIndex", i)
                                                .putExtra("type", "0")));// group 0
                        // 表示分组
                        // System.out.println("group.bottomShow:"+group.bottomShow);
                        final Intent intent = new Intent(
                                Constants.getBroadcastNamePairsString(i));
                        intent.putExtra("entry", module);
                        intent.putExtra("types", "0");
                        // Handler handler = new Handler();
                        // Runnable runnable = new Runnable(){
                        // @Override
                        // public void run() {
                        // // TODO Auto-generated method stub
                        // // 在此处添加执行的代码
                        // sendBroadcast(intent);
                        // }
                        // };
                        // handler.postDelayed(runnable, 2000);// 打开定时器，执行操作
                        break;
                    default:
                        break;
                }
            } else if (object instanceof SinopecMenuModule) {
                SinopecMenuModule module = (SinopecMenuModule) object;
                if (!module.caption.equalsIgnoreCase("巡检模块")) {

                    if (module.caption.equalsIgnoreCase("预警推送")) {
                        indexOfWarningPush = i;
                    }
                    // System.out.println("module caption:"+module.caption+"  class:"+module.mClass);
                    Log.i(TAG, "module.barImg1:" + module.barImg1
                            + "...module.barImg2:" + module.barImg2);
                    // if(module.isGroupModule){
                    tabHost.addTab(tabHost
                            .newTabSpec(String.valueOf(i))
                            .setIndicator(
                                    perpareIndicator(module.itemImg1,
                                            module.itemImg2, module.caption,
                                            module.id, module.bottomShow, ""))// 这里 "" 现实的是 数量
                            .setContent(
                                    new Intent(this, MobileOAAllActivity.class)
                                            .putExtra("entry", module)
                                            .putExtra("pageIndex", i)
                                            .putExtra("type", "1")));// module 1
                    // 模块
                    // System.out.println("module.bottomShow:"+module.bottomShow);
                    Intent intent = new Intent(
                            Constants.getBroadcastNamePairsString(i));
                    intent.putExtra("entry", module);
                    intent.putExtra("types", "1");
                    // sendBroadcast(intent);
                    // }
                }
            }
        }

        // tabHost.addTab(tabHost
        // .newTabSpec("MOBILE_OFFICE_MORE")
        // .setIndicator(
        // perpareIndicator("ic_more_nomal", "ic_more_selected",
        // getString(R.string.tv_main_more), ""))
        // .setContent(new Intent(this, MoreActivity.class)));

        // openFolder = OpenFolder.getInstance(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            final int j = i;
            tabHost.getTabWidget().getChildAt(i)
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tabHost.getCurrentTab() == j) {
                                Object o = DataCache.sinopecMenu.menuObject
                                        .get(j - 1);
                                if (o instanceof SinopecMenuGroup) {
                                    // 重新写个结构
                                /*	mGrid = new QuickActionGrid(
                                            SinopecAllMenuActivity.this);*/
                                    final SinopecMenuGroup group = (SinopecMenuGroup) DataCache.sinopecMenu.menuObject
                                            .get(j - 1);

                                    for (int i = 0; i < group.menuobjObjects
                                            .size(); i++) {
                                        final SinopecMenuModule module = (SinopecMenuModule) group.menuobjObjects
                                                .get(i);
                                        if (!module.isGroupModule) {
                                            // View item =
                                            // inflater.inflate(R.layout.layout_openfolder_item_bar,
                                            // null);
                                            // TextView tv_syn_text = (TextView)
                                            // item.findViewById(R.id.tv_syn_text);
                                            // TextView tv_msg_count =
                                            // (TextView)
                                            // item.findViewById(R.id.tv_msg_count);
                                            // ImageView iv_syn_icon =
                                            // (ImageView)
                                            // item.findViewById(R.id.iv_syn_icon);
                                            // tv_syn_text.setText(module.caption);
                                            // // StateListDrawable
                                            // bitmapDrawable =
                                            // skinManager.newSelectorFromResourceByMenu(mContext,
                                            // module.itemImg1.substring(0,
                                            // module.itemImg1.indexOf(".")),module.itemImg2.substring(0,
                                            // module.itemImg2.indexOf(".")),
                                            // display.getWidth() / 3,
                                            // display.getWidth() / 3);
                                            //
                                            // if(DataCache.taskCount.get(module.id)!=null){
                                            // if
                                            // (DataCache.taskCount.get((module.id))>0&&DataCache.taskCount.containsKey((module.id))){
                                            // tv_msg_count.setVisibility(View.VISIBLE);
                                            // tv_msg_count.setText(TextUtils.parseText(String.valueOf((DataCache.taskCount.get((module.id))))));
                                            // }else {
                                            // tv_msg_count.setVisibility(View.GONE);
                                            // }
                                            // }else
                                            // tv_msg_count.setVisibility(View.GONE);
                                            //
                                            // //
                                            // iv_syn_icon.setBackgroundDrawable(bitmapDrawable);
                                            //
                                            // iv_syn_icon.setOnClickListener(new
                                            // OnClickListener() {
                                            // @Override
                                            // public void onClick(View v) {
                                            // chooseBroadcastType(j - 1,
                                            // module.mClass, module);
                                            // //
                                            // MainActivity.horizontalScrollView.smoothScrollBy(
                                            // //
                                            // // 100 * (j - 1), 0);
                                            // SinopecAllMenuActivity.setCurrentTab((j
                                            // - 1) + 1);
                                            // }
                                            // });
                                            // perpareIndicator(iv_syn_icon,tv_syn_text,tv_msg_count,module.itemImg1,module.itemImg2,module.caption);
//											mGrid.addQuickAction(new MyQuickAction(
//													mContext,
//													R.drawable.ic_alarm_processing,
//													module));
                                        }
                                    }
                                    /*mGrid.show(v);
                                    mGrid.setOnQuickActionClickListener(new com.popwindow.widget.QuickActionWidget.OnQuickActionClickListener() {

										@Override
										public void onQuickActionClicked(
												QuickActionWidget widget,
												int position) {
											// TODO Auto-generated method stub
											SinopecMenuModule module = (SinopecMenuModule) group.menuobjObjects
													.get(position);

											chooseBroadcastType(j - 1,
													module.mClass, module);
											// MainActivity.horizontalScrollView.smoothScrollBy(
											// 100 * (j - 1), 0);
											SinopecAllMenuActivity
													.setCurrentTab((j - 1) + 1);

										}
									});*/

                                    // // Intent gotoModuleGroupIntent = new
                                    // Intent(
                                    // // Constants
                                    // // .getBroadcastNamePairsString(j));
                                    // // gotoModuleGroupIntent.putExtra("type",
                                    // // "gotoModuleGroup");
                                    // // sendBroadcast(gotoModuleGroupIntent);
                                    // //add folder
                                    //
                                    // folderview = (HorizontalScrollView)
                                    // inflater
                                    // .inflate(
                                    // R.layout.layout_seperator_footview,
                                    // null);
                                    // ll_open_container = (LinearLayout)
                                    // folderview.findViewById(R.id.ll_open_container);
                                    // LinearLayout.LayoutParams params = new
                                    // LinearLayout.LayoutParams(openFolder.dip2px(mContext,
                                    // 90), openFolder.dip2px(mContext, 90));
                                    // params.leftMargin =
                                    // openFolder.dip2px(mContext, 10);
                                    // SinopecMenuGroup group =
                                    // (SinopecMenuGroup)
                                    // DataCache.sinopecMenu.menuObject
                                    // .get(j - 1);
                                    //
                                    // for (int i = 0; i < group.menuobjObjects
                                    // .size(); i++) {
                                    // final SinopecMenuModule module =
                                    // (SinopecMenuModule) group.menuobjObjects
                                    // .get(i);
                                    // if(!module.isGroupModule){
                                    // View item =
                                    // inflater.inflate(R.layout.layout_openfolder_item,
                                    // null);
                                    // TextView tv_syn_text = (TextView)
                                    // item.findViewById(R.id.tv_syn_text);
                                    // TextView tv_msg_count = (TextView)
                                    // item.findViewById(R.id.tv_msg_count);
                                    // ImageView iv_syn_icon = (ImageView)
                                    // item.findViewById(R.id.iv_syn_icon);
                                    // tv_syn_text.setText(module.caption);
                                    // // StateListDrawable bitmapDrawable =
                                    // skinManager.newSelectorFromResourceByMenu(mContext,
                                    // module.itemImg1.substring(0,
                                    // module.itemImg1.indexOf(".")),module.itemImg2.substring(0,
                                    // module.itemImg2.indexOf(".")),
                                    // display.getWidth() / 3,
                                    // display.getWidth() / 3);
                                    //
                                    // if(DataCache.taskCount.get(module.id)!=null){
                                    // if
                                    // (DataCache.taskCount.get((module.id))>0&&DataCache.taskCount.containsKey((module.id))){
                                    // tv_msg_count.setVisibility(View.VISIBLE);
                                    // tv_msg_count.setText(TextUtils.parseText(String.valueOf((DataCache.taskCount.get((module.id))))));
                                    // }else {
                                    // tv_msg_count.setVisibility(View.GONE);
                                    // }
                                    // }else
                                    // tv_msg_count.setVisibility(View.GONE);
                                    //
                                    // //
                                    // iv_syn_icon.setBackgroundDrawable(bitmapDrawable);
                                    //
                                    // iv_syn_icon.setOnClickListener(new
                                    // OnClickListener() {
                                    // @Override
                                    // public void onClick(View v) {
                                    // openFolder.dismiss();
                                    // chooseBroadcastType(j - 1, module.mClass,
                                    // module);
                                    // //
                                    // MainActivity.horizontalScrollView.smoothScrollBy(
                                    // //
                                    // // 100 * (j - 1), 0);
                                    // SinopecAllMenuActivity.setCurrentTab((j -
                                    // 1) + 1);
                                    // }
                                    // });
                                    // perpareIndicator(iv_syn_icon,tv_syn_text,tv_msg_count,module.itemImg1,module.itemImg2,module.caption);
                                    // ll_open_container.addView(item,params);
                                    // }
                                    // }
                                    // openFolder.openFolderView(
                                    // tabHost.getCurrentTabView(),
                                    // getWindow().getDecorView(),
                                    // folderview, 100, 0);
                                    // openFolder
                                    // .setmOnFolderClosedListener(new
                                    // OpenFolder.OnFolderClosedListener() {
                                    //
                                    // @Override
                                    // public void onClosed() {
                                    // // Toast.makeText(getBaseContext(),
                                    // // "close folder btn1",
                                    // // Toast.LENGTH_SHORT)
                                    // // .show();
                                    // }
                                    // });
                                    //
                                    //
                                    // //注：这里可以将底部分组菜单做成悬浮窗口，而不必将界面顶上去，具体逻辑代码年后实现
                                    // // NavMenuDialog menuDialog = new
                                    // NavMenuDialog(mContext,
                                    // R.style.dialog_overlay);
                                    // // View hs_view =
                                    // getLayoutInflater().inflate(R.layout.layout_seperator_footview_dialog,
                                    // null);
                                    // // hs_view.setLayoutParams(new
                                    // LayoutParams(display.getWidth(), -2));
                                    // // menuDialog.setView(hs_view);
                                    // // LinearLayout ll_open_container =
                                    // (LinearLayout)
                                    // hs_view.findViewById(R.id.ll_open_container);
                                    // // Window window =
                                    // menuDialog.getWindow();
                                    // // WindowManager.LayoutParams params =
                                    // window.getAttributes();
                                    // // params.gravity = Gravity.BOTTOM |
                                    // Gravity.LEFT;
                                    // // params.x = 0;
                                    // // params.y = (int)
                                    // mContext.getResources().getDimension(R.dimen.main_bar_height);
                                    // //// params.verticalMargin =
                                    // openFolder.dip2px(mContext,
                                    // mContext.getResources().getDimension(R.dimen.main_bar_height));
                                    // //// params.x = 0;
                                    // //// params.y = display.getHeight() -
                                    // openFolder.dip2px(mContext,
                                    // mContext.getResources().getDimension(R.dimen.main_bar_height))
                                    // - openFolder.dip2px(mContext, 100);
                                    // // if(Constants.DEBUG) {
                                    // //// Log.i(TAG, "params.y:" + params.y);
                                    // // }
                                    // // window.setAttributes(params);
                                    // //
                                    // menuDialog.setCanceledOnTouchOutside(true);
                                    // // menuDialog.show();
                                } else {
                                    // tabHost.setCurrentTab(j);
                                }
                            } else {
                                tabHost.setCurrentTab(j);
                            }

                        }
                    });
        }
        tabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
        // mainProcess.calApproveNum(mContext, this,
        // DataCache.sinopecMenu.menuObject);
        mainProcess.calApproveNum(mContext, this,
                DataCache.sinopecMenu.menuObject);
        if (warningPushUsername != null && indexOfWarningPush != -1) {
            tabHost.setCurrentTab(indexOfWarningPush + 1);
        }

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
        // textView.setText(text);
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
                break;
            default:
                break;
        }

        // StateListDrawable drawable =
        // skinManager.newSelectorFromResourceByMenu(mContext, iconName,
        // iconName1, screenWidth/3, screenWidth/3);
        // ivIcon.setBackgroundDrawable(drawable);
    }

    HorizontalScrollView folderview;

    // OpenFolder openFolder;
    protected void chooseBroadcastType(int pageIndex, String titleCode,
                                       SinopecMenuModule module) {
        // Map<String, String> map = Constants.getBroadcastNamePairs();
        // String action = map.get(titleCode);
        Intent intent = new Intent(
                Constants.getBroadcastNamePairsString(pageIndex));
        intent.putExtra("entry", module);
        sendBroadcast(intent);
    }

    BroadcastReceiver mainReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "MainActivity.BroadcastReceiver");

            String type = intent.getStringExtra("type");
            if (null != type && "finish".equalsIgnoreCase(type)) {
                finish();
                return;
            }
            String groupId = intent.getStringExtra("id");
            List<String[]> numbers = (List<String[]>) intent
                    .getSerializableExtra("value");
            int number = intent.getIntExtra("number", -1);
            if (numbers != null) {// 这是个List ,需要计算
                updateTabNums(groupId, calculateTabNumber(numbers));
            } else if (number != -1) {// 可以直接显示的数字
                updateTabNums(groupId, number);
            }
        }
    };

    BroadcastReceiver mainTabReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int pageIndex = intent.getIntExtra("pageIndex", 0);
            if (pageIndex != 0) {
                tabHost.getTabWidget().getChildAt(pageIndex + 1).performClick();
            }
        }
    };

    public int calculateTabNumber(List<String[]> numbers) {
        int result = 0;
        for (String[] number : numbers) {
            int tempNumber = 0;
            try {
                if (!android.text.TextUtils.isEmpty(number[1])) {
                    tempNumber = Integer.parseInt(number[1]);
                }
            } catch (Exception e) {
                tempNumber = 0;
                e.printStackTrace();
            }
            result += tempNumber;
        }
        return result;
    }

    private void updateTabNums(final String groupId, final int count) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // List<Object> menus = DataCache.sinopecMenu.menuObject;
                // int tabIndex = 100;
                // for (int i = 0; i < menus.size(); i++) {
                // Object o = menus.get(i);
                // if (o instanceof SinopecMenuGroup) {
                // if (groupid
                // .equalsIgnoreCase(((SinopecMenuGroup) (o)).id)) {//&&
                // "0".equalsIgnoreCase(((SinopecMenuGroup) (o)).notification)
                // tabIndex = i;
                // final String counts =
                // calculateTabNumberGroup((SinopecMenuGroup)o);
                // if (tabIndex < tabHost.getTabWidget().getChildCount()) {
                // final View v = tabHost.getTabWidget().getChildAt(tabIndex+1);
                // final TextView tv_text_num = (TextView) v
                // .findViewById(R.id.tv_msg_count);
                // if (count != 0) {
                // runOnUiThread(new Runnable() {
                // @Override
                // public void run() {
                // tv_text_num.setText(TextUtils.parseText(String
                // .valueOf(counts)));
                // tv_text_num.setVisibility(View.VISIBLE);
                // }
                // });
                // } else {
                // runOnUiThread(new Runnable() {
                // @Override
                // public void run() {
                // tv_text_num.setVisibility(View.GONE);
                // }
                // });
                // }
                // }
                // break;
                // }
                // } else if (o instanceof SinopecMenuModule) {
                // if (groupId
                // .equalsIgnoreCase(((SinopecMenuModule) (o)).id)) {//&&
                // "0".equalsIgnoreCase(((SinopecMenuModule) (o)).notification)
                // tabIndex = i;
                // if (tabIndex < tabHost.getTabWidget().getChildCount()) {
                // final View v = tabHost.getTabWidget().getChildAt(tabIndex+1);
                // final TextView tv_text_num = (TextView) v
                // .findViewById(R.id.tv_msg_count);
                // if (count != 0) {
                // runOnUiThread(new Runnable() {
                // @Override
                // public void run() {
                // tv_text_num.setText(TextUtils.parseText(String
                // .valueOf(count)));
                // tv_text_num.setVisibility(View.VISIBLE);
                // }
                // });
                // } else {
                // runOnUiThread(new Runnable() {
                // @Override
                // public void run() {
                // tv_text_num.setVisibility(View.GONE);
                // }
                // });
                // }
                // }
                // break;
                // }
                // }
                // }
                // /////////////////暂时注掉//////////////////
                // // if (tabIndex < tabHost.getTabWidget().getChildCount()) {
                // // final View v =
                // tabHost.getTabWidget().getChildAt(tabIndex);
                // // final TextView tv_text_num = (TextView) v
                // // .findViewById(R.id.tv_msg_count);
                // // if (count != 0) {
                // // runOnUiThread(new Runnable() {
                // // @Override
                // // public void run() {
                // // tv_text_num.setText(TextUtils.parseText(String
                // // .valueOf(count)));
                // // tv_text_num.setVisibility(View.VISIBLE);
                // // }
                // // });
                // // } else {
                // // runOnUiThread(new Runnable() {
                // // @Override
                // // public void run() {
                // // tv_text_num.setVisibility(View.GONE);
                // // }
                // // });
                // // }
                // // }
                // return null;
                // }
                List<Object> menus = DataCache.sinopecMenu.menuObject;
                /*for (int i = 1; i < tabHost.getTabWidget().getChildCount(); i++) {
                    final Object object = menus.get(i - 1);
					if (object instanceof SinopecMenuGroup) {
						final String counts = calculateTabNumberGroup((SinopecMenuGroup) object);
						final View v = tabHost.getTabWidget().getChildAt(i);
						final TextView tv_text_num = (TextView) v
								.findViewById(R.id.tv_msg_count);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (counts != null && !counts.equals("0")) {
									tv_text_num.setText(counts);
									tv_text_num.setVisibility(View.VISIBLE);
								} else {
									tv_text_num.setVisibility(View.GONE);
								}
							}
						});
						continue;
					} else if (object instanceof SinopecMenuModule) {
						final View v = tabHost.getTabWidget().getChildAt(i);
						final TextView tv_text_num = (TextView) v
								.findViewById(R.id.tv_msg_count);
						final String moduleId = (String) v.findViewById(
								R.id.tv_syn_text).getTag();
						if (DataCache.taskCount.containsKey(moduleId)) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (DataCache.taskCount.get(moduleId) != null
											&& !(0 == DataCache.taskCount
													.get(moduleId))) {
										tv_text_num
												.setText(TextUtils.parseText(String
														.valueOf(DataCache.taskCount
																.get(moduleId))));
										tv_text_num.setVisibility(View.VISIBLE);
									} else {
										tv_text_num.setVisibility(View.GONE);
									}
								}
							});
						}
					}
				}*/
                return null;
            }
        }.execute();
    }

    private static boolean isExit = false;
    private MainProcess mainProcess;
    private String menuDisplayType;

    @Override
    public void onBackPressed() {
        if (isExit == false) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    isExit = false;
                }
            }, 2000);
        } else {
            stopVpn();
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public static int stopVpn() {
        Log.i(TAG, "stop vpn..............");
        if (Common.VpnStatus.IDLE == VPNManager.getInstance().getStatus()) {
            return 0;
        }
        Log.i(TAG, "stop l3vpn enter");
        if (mThreadStopVpn != null) {
            Log.w(TAG, "ThreadStopVpn is not null, will kill it.");
            mThreadStopVpn.interrupt();
            try {
                mThreadStopVpn.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mThreadStopVpn = null;
        }

        mThreadStopVpn = new Thread(new Runnable() {
            public void run() {
                VPNManager.getInstance().stopVPN();
                mThreadStopVpn = null;
            }
        });

        mThreadStopVpn.start();
        return 0;
    }

    @Override
    public void onTabChanged(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refreshNumber(List<String[]> idNumbers) {
        if (idNumbers != null && idNumbers.size() > 0) {
            for (String[] imn : idNumbers) {
                updateTabNums(imn[0], Integer.parseInt(imn[1]));
            }
        }
    }

    public String calculateTabNumberGroup(SinopecMenuGroup group) {
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // savedInstanceState.putSerializable("menus", (Serializable)
        // DataCache.sinopecMenu);
        // savedInstanceState.putString("rootUrl", WebUtils.rootUrl);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // DataCache.sinopecMenu = (SinopecMenu)
        // savedInstanceState.getSerializable("menus");
        // if(WebUtils.rootUrl==null||"".equalsIgnoreCase(WebUtils.rootUrl)){
        // WebUtils.rootUrl = savedInstanceState.getString("rootUrl");
        // }
        if (DataCache.sinopecMenu == null
                || (DataCache.sinopecMenu != null && DataCache.sinopecMenu.menuObject
                .size() <= 0)) {
            Intent intent = new Intent(SinopecAllMenuActivity.this,
                    LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // 退出程序
            application.clearActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
            System.out.println("GPS模块正常");
            return;
        }

        Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
    }

    /***************************/
    private void bindService() {
        Intent intent = new Intent(mContext, LocalService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /***************************/
    private void unBind() {
        unbindService(conn);
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            System.out.println("=====onServiceDisconnected======");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            System.out.println("=====onServiceConnected======");
            LocalBinder binder = (LocalBinder) service;
            localService = binder.getService();
            localService.init();
            // flag = true;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) // 按下返回键提醒用户是否退出系统
        {
            AlertUtils.showConfirm(this, "提示", "您确定要退出系统吗？", "确定", "取消",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // //增加开始坐标点
                            new uploadStatus() {
                            }.execute(SinopecAllMenuActivity.locInfo, "1");// 0
                            // 开始，1，结束，2运动中
                        }
                    }, null);
            return true; // 不需要继续传播事件
        }
        return super.onKeyDown(keyCode, event); // 交由系统处理
    }

    // 结束时提交
    class uploadStatus extends AsyncTask<String, Void, ResultInfo> {

        @Override
        protected ResultInfo doInBackground(String... params) {
            // TODO Auto-generated method stub
            // 本地模拟数据
            try {
                String sbsr = params[0];
                String status = params[1];
                final MobileApplication application = (MobileApplication) getApplication();
                return DataUtil.sendXML(sbsr);// 0
                // 开始，1，结束，2运动中
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResultInfo result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                if ("1".equals(result.key)) {
                    System.out.println("成功:" + result.message);
                } else
                    System.out.println("error:" + result.message);
            } else {
                System.out.println("error");
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    SinopecAllMenuActivity.status = "0";// 将状态重置成0
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }, 2000);
        }
    }
}
