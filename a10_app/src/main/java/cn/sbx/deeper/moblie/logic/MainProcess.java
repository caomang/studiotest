package cn.sbx.deeper.moblie.logic;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sunboxsoft.monitor.utils.PerfUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.sbx.deeper.moblie.activity.SplashActivity;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.SinopecMenu;
import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.SinopecMenuPage;
import cn.sbx.deeper.moblie.domian.SoftInfo;
import cn.sbx.deeper.moblie.domian.Task;
import cn.sbx.deeper.moblie.domian.TaskKey;
import cn.sbx.deeper.moblie.interfaces.IApproveNumInterface;
import cn.sbx.deeper.moblie.util.ConnectionManager;
import cn.sbx.deeper.moblie.util.DataCache;
import cn.sbx.deeper.moblie.util.DataCollectionUtils;
import cn.sbx.deeper.moblie.util.StreamUtils;
import cn.sbx.deeper.moblie.util.URLUtils;
import cn.sbx.deeper.moblie.util.UserInfo;
import cn.sbx.deeper.moblie.util.WebUtils;
import cn.sbx.deeper.moblie.util.XmlParserLogic;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 这里获取布局的样式
 *
 * @author admin
 */

public class MainProcess {
    private static final String TAG = MainProcess.class.getSimpleName();
    Context mContext;
    MainLoadingListener loadingListener;
    IApproveNumInterface numInterface;

    public void initState(Context context,
                          MainLoadingListener mainLoadingListener) {
        this.mContext = context;
        this.loadingListener = mainLoadingListener;
        // getApprovalCountNew("", WebUtils.rootUrl
        // + "/Integrated/GetMoblieApproveCount.aspx", "");
        // new UpdateTask().execute();// 20140922

        int sdkVersion = SplashActivity.getAndroidSDKVersion();
        // if(sdkVersion < 11){
        new MainMenuTask().execute();
        // }else
        // new MainMenuTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static String[] numsType = {"ACC005", "ACC001", "ApprovalTow",
            "dpemail"};// 审批
    // 合同
    public static List<String> numsList = new ArrayList<String>();
    private List<Object> moduleGroupMenus;

    /**
     * 默认的组件，显示界面
     *
     * @param context
     * @param numInterface
     * @param moduleGroupMenus
     */
    public void calApproveNum(Context context,
                              IApproveNumInterface numInterface, List<Object> moduleGroupMenus) {
        this.mContext = context;
        this.numInterface = numInterface;
        this.moduleGroupMenus = moduleGroupMenus;
        new ApproveNumTaskNew() {
        }.execute();
    }

    class ApproveNumTaskNew extends AsyncTask<Void, Void, List<String[]>> {
        @Override
        protected void onPostExecute(List<String[]> result) {
            super.onPostExecute(result);
            if (numInterface != null)
                numInterface.refreshNumber(result);
        }

        @Override
        protected List<String[]> doInBackground(Void... params) {
            List<String[]> iCounts = new ArrayList<String[]>();
            try {
                if (moduleGroupMenus == null)
                    return null;

                for (Map.Entry<String, Integer> entry : DataCache.taskCount
                        .entrySet()) {
                    String[] idCounts = new String[2];
                    idCounts[0] = entry.getKey();
                    idCounts[1] = String.valueOf(entry.getValue());
                    iCounts.add(idCounts);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return iCounts;
        }

    }

    public int calculateTabNumber(List<String[]> numbers) {
        int result = 0;
        for (String[] number : numbers) {
            int tempNumber = 0;
            try {
                tempNumber = Integer.parseInt(number[1]);
            } catch (Exception e) {
                tempNumber = 0;
                e.printStackTrace();
            }
            result += tempNumber;
        }
        return result;
    }

    class UpdateTask extends AsyncTask<Void, Void, SoftInfo> {
        @Override
        protected void onPostExecute(SoftInfo result) {
            super.onPostExecute(result);
            if (loadingListener != null)
                loadingListener.checkUpdate(result);
        }

        @Override
        protected SoftInfo doInBackground(Void... params) {
            return DataCollectionUtils.receiverUpdateData();
        }

    }

    /**
     * 获取应用的menu的
     *
     * @author admin
     */
    public class MainMenuTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (DataCache.sinopecMenu != null) {
                if (loadingListener != null)
                    loadingListener.initMainView();
                // ThreadPool threadPool = new ThreadPool(15); //
                // 创建一个有个3工作线程的线程池
                // try {
                // Thread.sleep(500);
                // 休眠500毫秒,以便让线程池中的工作线程全部运行
                // 获取数量
                for (int i = 0; i < DataCache.sinopecMenu.menuObject.size(); i++) {
                    Object o = DataCache.sinopecMenu.menuObject.get(i);
                    if (o instanceof SinopecMenuModule) {
                        if (((SinopecMenuModule) o).caption
                                .equalsIgnoreCase("巡检模块")) {
                            for (SinopecMenuPage page : ((SinopecMenuModule) o).menuPages) {
                                if ("gpsInfo".equalsIgnoreCase(page.code)) {
                                    WebUtils.uploadgpsUrl = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                } else if ("linkman"
                                        .equalsIgnoreCase(page.code)) {
                                    WebUtils.contactUrl = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                } else if ("submit".equalsIgnoreCase(page.code)) {
                                    WebUtils.getsubmitUrl = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                }

                            }

                        }
                        if (((SinopecMenuModule) o).caption
                                .equalsIgnoreCase("抄表")) {
                            for (SinopecMenuPage page : ((SinopecMenuModule) o).menuPages) {
                                if ("DownTask".equalsIgnoreCase(page.code)) {
                                    WebUtils.downloadurl = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                }
                                if ("DicTask".equalsIgnoreCase(page.code)) {
                                    WebUtils.downloadDataDicUrl = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                    Log.e("-=-=-=-=-=-=>", WebUtils.downloadDataDicUrl);
                                }
                                if ("UploadTask".equalsIgnoreCase(page.code)) {
                                    WebUtils.uploadchaobiaoUrl = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                }
                                if ("Billing".equalsIgnoreCase(page.code)) {
                                    WebUtils.chaoBiaoMoneyUrl = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                }

                            }

                        }
                        if (((SinopecMenuModule) o).caption
                                .equalsIgnoreCase("同步数据")) {
                            for (SinopecMenuPage page : ((SinopecMenuModule) o).menuPages) {
                                if ("DownTask".equalsIgnoreCase(page.code)) {
                                    WebUtils.downloadDataDicUrl = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                    Log.e("-=-=-=-=-=-=>", WebUtils.downloadDataDicUrl);
                                }
                            }

                        }
                        if (((SinopecMenuModule) o).caption
                                .equalsIgnoreCase("安检")) {
                            for (SinopecMenuPage page : ((SinopecMenuModule) o).menuPages) {
                                if ("DownTask".equalsIgnoreCase(page.code)) {
                                    WebUtils.downloadurl_anjian = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                    Log.e("-=-=-=-=-=-=>", WebUtils.downloadurl_anjian);
                                }
                                if ("UploadTask".equalsIgnoreCase(page.code)) {
                                    WebUtils.uploadUrl_anjian = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                }
                            }

                        }
                        if (numsList.contains(((SinopecMenuModule) o).mClass)) {

                            // 1显示导航栏数字
                            // 0 不显示导航栏数字
                            // if ("1".equals(((SinopecMenuModule)
                            // o).notification)) {
                            // if ("numUrl".equalsIgnoreCase(page.code)) {
                            // }
                            // if ("ACC005".equals(((SinopecMenuModule)
                            // o).mClass)) {
                            // String path = ((SinopecMenuModule) o).numUrl;
                            // getApprovalCountNew2(((SinopecMenuModule)
                            // o).id, path, "0");// 0
                            // }
                            for (SinopecMenuPage page : ((SinopecMenuModule) o).menuPages) {
                                if ("notification".equalsIgnoreCase(page.code)) {
                                    String path = WebUtils.rootUrl
                                            + URLUtils.baseContentUrl + page.id;
                                    if ("dpemail"
                                            .equals(((SinopecMenuModule) o).mClass)) {
                                        DownLoaderAsyncTask(
                                                ((SinopecMenuModule) o).id,
                                                path, "0");
                                        // threadPool.execute();
                                    } else
                                        DownLoaderAsyncTask(
                                                ((SinopecMenuModule) o).id,
                                                path, "1");
                                    // threadPool.execute();
                                }
                                // if ("numUrl"
                                // .equalsIgnoreCase(page.code)) {

                            }
                            // }
                        }
                    } else if (o instanceof SinopecMenuGroup) {
                        SinopecMenuGroup group = (SinopecMenuGroup) o;
                        for (int j = 0; j < group.menuobjObjects.size(); j++) {
                            SinopecMenuModule module = (SinopecMenuModule) group.menuobjObjects
                                    .get(j);
                            if (numsList.contains(module.mClass)) {
                                // 1 显示导航栏数字
                                // 0 不显示导航栏数字
                                // if ("1".equals(((SinopecMenuModule)
                                // module).notification)) {

                                for (SinopecMenuPage page : ((SinopecMenuModule) module).menuPages) {
                                    if ("notification"
                                            .equalsIgnoreCase(page.code)) {
                                        String path = WebUtils.rootUrl
                                                + URLUtils.baseContentUrl
                                                + page.id;
                                        if ("dpemail".equals(module.mClass)) {
                                            DownLoaderAsyncTask(module.id,
                                                    path, "0");
                                            // threadPool.execute();
                                        } else
                                            DownLoaderAsyncTask(module.id,
                                                    path, "1");
                                        // threadPool.execute();
                                    }
                                }
                            }
                            // }
                        }
                    }
                }
                // } catch (InterruptedException e) {
                // e.printStackTrace();
                // }

                // threadPool.waitFinish(); // 等待所有任务执行完毕
                // threadPool.closePool(); // 关闭线程池
            }
            // 获取要提交的url
            // new getSubmitUrlAsyncTask().execute("");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < numsType.length; i++) {
                numsList.add(numsType[i]);
            }

            InputStream is = null;

            try {
                // remote data
                String path = WebUtils.rootUrl + URLUtils.sinopecMainMenu;
                // 导航数据(全部)de

                boolean loginState = PerfUtils.getBoolean(mContext, "loginState", false);
				if(loginState){
                is = ConnectionManager.receiveMainStream(path);
                String s = StreamUtils.retrieveContent(is);
                System.out.println("导航菜单数据:=" + s);
                is = new ByteArrayInputStream(s.getBytes());

				}
                if (is == null) {
                    // 从本地sp中获取数据。离线状态，如果sp为空 先进行登录
                    String str = PerfUtils.getString(mContext, "navigate_All",
                            "");
                    is = new ByteArrayInputStream(str.getBytes());
                    if (is == null) {
                        Toast.makeText(mContext, "请链接网络获取用户信息", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 将is 保存至sp中,首先移除上次导航数据
                    PerfUtils.removeKey(mContext, "navigate_All");
                    PerfUtils.putString(mContext, "navigate_All",
                            streamToString(is));
                    String str = PerfUtils.getString(mContext, "navigate_All",
                            "");
                    is = new ByteArrayInputStream(str.getBytes());
                    System.out.println("========导航====" + str);
                }

                // String sdString=StreamUtils.retrieveContent(is);
                // System.out.println(sdString);
                // InputStream inputStream = mContext.getAssets().open(
                // "daohang.xml");
                DataCache.sinopecMenu = XmlParserLogic
                        .receiverBeanMainMenuLists(is);

                // remote data
                String authPath = WebUtils.rootUrl
                        + URLUtils.sinopecMainAuthMenu;
                // 导航数据(根据用户权限)

                is = null;
                if (loginState) {
                    is = ConnectionManager.receiveMainAuthStream(authPath,
                            DataCache.sinopecMenu.version);
                }
                // 将该用户的导航界面数据进行保存
                // 获取该用户名
                String username = UserInfo.getInstance().getUsername();
                if (is == null) {
                    // 从本地sp中获取数据。离线状态，如果sp为空 先进行登录
                    String str = PerfUtils.getString(mContext, "navigate_user", "");//如果不同的登录人看到的界面菜单不同,这里的 导航 需要根据用户进行保存
                    is = new ByteArrayInputStream(str.getBytes());
                    if (is == null) {

                        Toast.makeText(mContext, "请链接网络获取用户信息", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    // 将is 保存至sp中,首先移除上次导航数据
                    PerfUtils.removeKey(mContext, "navigate_user");
                    PerfUtils.putString(mContext, "navigate_user", streamToString(is));
                    String str = PerfUtils.getString(mContext, "navigate_user", "");
                    is = new ByteArrayInputStream(str.getBytes());
                    System.out.println("========导航  2====" + str);
                }

                // String sdString=StreamUtils.retrieveContent(is);
                // System.out.println(sdString);
                // InputStream inputStream1 = mContext.getAssets().open(
                // "daohang.xml");

                SinopecMenu authMenu = XmlParserLogic.receiveMenuAuth(is);
                if (authMenu != null) {
                    switch (authMenu.menuAuthType) {
                        case ALLMENU:
                            Log.i(TAG, "authMenu:" + authMenu.menuAuthType);
                            break;
                        case NONEMENU:
                            Log.i(TAG, "authMenu:" + authMenu.menuAuthType);
                            break;
                        case MATCHMENU:
                            Log.i(TAG, "authMenu:" + authMenu.menuAuthType);
                            matchMenu(authMenu);
                            break;
                        case CLAERMENU:
                            Log.i(TAG, "authMenu:" + authMenu.menuAuthType);
                            DataCache.sinopecMenu.menuObject = new ArrayList<Object>();
                            break;
                        default:
                            break;
                    }
                } else {
                    Log.i(TAG, "authMenu is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String streamToString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public void getApprovalCount(final String id, final String path) {// 取得待办数量
        new AsyncTask<Void, Void, List<Map<TaskKey, List<Task>>>>() {
            @Override
            protected void onPostExecute(List<Map<TaskKey, List<Task>>> result) {
                if (result != null) {
                    for (Map<TaskKey, List<Task>> entry : result) {
                        TaskKey taskKey = entry.entrySet().iterator().next()
                                .getKey();
                        String name = taskKey.caption;
                        if (mContext.getString(R.string.tv_task_pending)
                                .equalsIgnoreCase(name)) {// {Pending,待办}
                            DataCache.taskCount.put(id, entry.entrySet()
                                    .iterator().next().getValue().size());
                            // System.out.println("module cation: " +
                            // approvalItem.id);
                            // System.out.println("entry counts: "+
                            // entry.getValue().size());
                        }
                    }
                    Intent intent = new Intent(Constants.MODIFY_APP_MAIN_SKIN);
                    intent.putExtra("type", "updateTabNum");
                    mContext.sendBroadcast(intent);
                    mContext.sendBroadcast(new Intent(
                            Constants.MODIFY_APP_MENU_NUM));
                }
            }

            ;

            @Override
            protected List<Map<TaskKey, List<Task>>> doInBackground(
                    Void... para) {
                try {
                    return DataCollectionUtils.receiverApproveTag(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public void DownLoaderAsyncTask(final String id, final String path,
                                    final String type) {
        new Thread(new Runnable() {
            public void run() {
                // DownLoaderAsyncTask asyncTask = new DownLoaderAsyncTask(id,
                // path, type);
                // asyncTask.execute(path);
                List<String[]> list = new ArrayList<String[]>();
                if (null == path || "".equalsIgnoreCase(path)) {
                } else {

                    if ("0".equals(type)) {// 邮箱取数量
                        list = DataCollectionUtils
                                .receiverEmailNumDataHome(path);
                    } else
                        list = DataCollectionUtils
                                .receiveApproveNumberNew(path);
                }
                Message msg = mUIHandler.obtainMessage(1);
                msg.obj = list;
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                msg.setData(bundle);
                msg.sendToTarget();
            }
        }).start();
    }

    /**
     * 线程处理结果
     */
    private Handler mUIHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            // initConditions2(currentTab);
            switch (msg.what) {
                case 1:
                    List<String[]> result = (List<String[]>) msg.obj;
                    String id = msg.getData().getString("id");
                    if (result != null && result.size() > 0) {
                        results = result;
                        for (String[] numbers : result) {
                            String tabId = numbers[0];
                            String count = numbers[1];
                            // if ("dbsy".equalsIgnoreCase(tabId)) {
                            int total = 0;
                            for (String[] number : result) {
                                int tempNumber = 0;
                                try {
                                    if (!TextUtils.isEmpty(number[1])) {
                                        tempNumber = Integer.parseInt(number[1]);
                                    }
                                } catch (Exception e) {
                                    tempNumber = 0;
                                    e.printStackTrace();
                                }
                                total += tempNumber;
                            }
                            try {
                                System.out.println("id:" + id + "  count:" + count);
                                DataCache.taskCount.put(id, total);
                                Intent intentNum = new Intent(
                                        Constants.MODIFY_APP_MENU_NUM);
                                intentNum.putExtra("type", "updateTabNum");
                                intentNum.putExtra("number", total);
                                mContext.sendBroadcast(intentNum);
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        Intent intentNum = new Intent(Constants.MODIFY_APP_MENU_NUM);
                        intentNum.putExtra("type", "updateTabNum");
                        mContext.sendBroadcast(intentNum);
                    }
                    break;
            }
        }
    };

    List<String[]> results;

    public List<String[]> getApprovalCountNew(final String id,
                                              final String path, final String type) {// 取得待办数量
        results = new ArrayList<String[]>();
        new AsyncTask<String, Void, List<String[]>>() {
            @Override
            protected void onPostExecute(List<String[]> result) {
                super.onPostExecute(result);
                if (result != null && result.size() > 0) {
                    results = result;
                    for (String[] numbers : result) {
                        String tabId = numbers[0];
                        String count = numbers[1];
                        // if ("dbsy".equalsIgnoreCase(tabId)) {
                        int total = 0;
                        for (String[] number : result) {
                            int tempNumber = 0;
                            try {
                                if (!TextUtils.isEmpty(number[1])) {
                                    tempNumber = Integer.parseInt(number[1]);
                                }
                            } catch (Exception e) {
                                tempNumber = 0;
                                e.printStackTrace();
                            }
                            total += tempNumber;
                        }
                        try {
                            System.out.println("id:" + id + "  count:" + count);
                            DataCache.taskCount.put(id, total);
                            Intent intentNum = new Intent(
                                    Constants.MODIFY_APP_MAIN_SKIN);
                            intentNum.putExtra("type", "updateTabNum");
                            intentNum.putExtra("number", total);
                            mContext.sendBroadcast(intentNum);
                            mContext.sendBroadcast(new Intent(
                                    Constants.MODIFY_APP_MENU_NUM));
                        } catch (Exception e) {
                        }
                    }
                }
            }

            @Override
            protected List<String[]> doInBackground(String... params) {
                if (null == path || "".equalsIgnoreCase(path)) {
                    return null;
                }
                if ("0".equals(type)) {// 邮箱取数量
                    return DataCollectionUtils.receiverEmailNumDataHome(path);
                } else
                    return DataCollectionUtils.receiveApproveNumberNew(path);
            }
        }.execute();
        return null;
    }

    private void matchMenu(SinopecMenu authMenu) {
        SinopecMenu sourceMenu = DataCache.sinopecMenu;
        SinopecMenu sinopecMenu = new SinopecMenu();
        sinopecMenu.layout = sourceMenu.layout;
        sinopecMenu.version = sourceMenu.version;
        sinopecMenu.columns = sourceMenu.columns;
        sinopecMenu.itemTemplate = sourceMenu.itemTemplate;
        for (int i = 0; i < authMenu.menuObject.size(); i++) {
            Object object = authMenu.menuObject.get(i);
            for (Object sObj : sourceMenu.menuObject) {
                if (sObj instanceof SinopecMenuGroup
                        && object instanceof SinopecMenuGroup) {
                    if (((SinopecMenuGroup) object).id
                            .equalsIgnoreCase(((SinopecMenuGroup) sObj).id)) {
                        if (((SinopecMenuGroup) object).menuobjObjects
                                .isEmpty()) {
                            sinopecMenu.menuObject.add(sObj);
                        } else {
                            SinopecMenuGroup group = new SinopecMenuGroup();
                            sinopecMenu.menuObject.add(group);
                            readdSubMenus(group, ((SinopecMenuGroup) object),
                                    ((SinopecMenuGroup) sObj));
                        }
                    }
                } else if (sObj instanceof SinopecMenuModule
                        && object instanceof SinopecMenuModule) {
                    if (((SinopecMenuModule) sObj).id
                            .equalsIgnoreCase(((SinopecMenuModule) object).id)) {
                        sinopecMenu.menuObject.add(sObj);
                    }
                }
            }
        }
        DataCache.sinopecMenu = sinopecMenu;
    }

    private void readdSubMenus(SinopecMenuGroup dustGroup,
                               SinopecMenuGroup authGroup, SinopecMenuGroup sourceGroup) {
        dustGroup.id = sourceGroup.id;
        dustGroup.layout = sourceGroup.layout;
        dustGroup.rows = sourceGroup.rows;
        dustGroup.columns = sourceGroup.columns;
        dustGroup.caption = sourceGroup.caption;
        dustGroup.itemImg1 = sourceGroup.itemImg1;
        dustGroup.itemImg2 = sourceGroup.itemImg2;
        dustGroup.itemTemplate = sourceGroup.itemTemplate;
        dustGroup.subModuleId = sourceGroup.subModuleId;
        for (Object authObj : authGroup.menuobjObjects) {
            for (Object sourceObj : sourceGroup.menuobjObjects) {
                if (authObj instanceof SinopecMenuGroup
                        && sourceObj instanceof SinopecMenuGroup) {
                    if (((SinopecMenuGroup) authObj).id
                            .equalsIgnoreCase(((SinopecMenuGroup) sourceObj).id)) {
                        if (((SinopecMenuGroup) authObj).menuobjObjects
                                .isEmpty()) {
                            dustGroup.menuobjObjects.add(sourceObj);
                        } else {
                            SinopecMenuGroup group = new SinopecMenuGroup();
                            dustGroup.menuobjObjects.add(group);
                            readdSubMenus(group, ((SinopecMenuGroup) authObj),
                                    ((SinopecMenuGroup) sourceObj));
                        }
                    }
                } else if (authObj instanceof SinopecMenuModule
                        && sourceObj instanceof SinopecMenuModule) {
                    if (((SinopecMenuModule) authObj).id
                            .equalsIgnoreCase(((SinopecMenuModule) sourceObj).id)) {
                        dustGroup.menuobjObjects.add(sourceObj);
                    }
                }
            }
        }
    }

    // 获取需要在菜单上面显示的个数
    private void fillEmailNumData(final String id, final String emailNumPath) {
        new AsyncTask<Void, Void, String>() {
            private Integer tabNum = 0;

            // private ProgressHUD overlayProgress;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null || !"".equals(result)) {
                    try {
                        tabNum = Integer.valueOf(result);
                    } catch (Exception e) {
                        tabNum = 0;
                    }
                    System.out.println("Mail Num:" + tabNum);
                    try {
                        DataCache.taskCount.put(id, tabNum);
                        Intent intent = new Intent(
                                Constants.MODIFY_APP_MAIN_SKIN);
                        intent.putExtra("type", "updateTabNum");
                        mContext.sendBroadcast(intent);
                        mContext.sendBroadcast(new Intent(
                                Constants.MODIFY_APP_MENU_NUM));
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    // 网络数据
                    return DataCollectionUtils
                            .receiverEmailNumData(emailNumPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }

    public String getApprovalCountNew2(final String id, final String path,
                                       final String type) {// 取得待办数量
        // results = new ArrayList<String[]>();
        new AsyncTask<String, Void, List<String>>() {
            @Override
            protected void onPostExecute(List<String> result) {
                super.onPostExecute(result);
                if (result != null && result.size() > 0) {
                    try {
                        int total = 0;
                        for (String number : result) {
                            int tempNumber = 0;
                            try {
                                if (!TextUtils.isEmpty(number)) {
                                    tempNumber = Integer.parseInt(number);
                                }
                            } catch (Exception e) {
                                tempNumber = 0;
                                e.printStackTrace();
                            }
                            total += tempNumber;
                        }

                        DataCache.taskCount.put(id, total);
                        Intent intentNum = new Intent(
                                Constants.MODIFY_APP_MAIN_SKIN);
                        intentNum.putExtra("type", "updateTabNum");
                        intentNum.putExtra("number", total);
                        mContext.sendBroadcast(intentNum);
                        mContext.sendBroadcast(new Intent(
                                Constants.MODIFY_APP_MENU_NUM));
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }

            @Override
            protected List<String> doInBackground(String... params) {
                if (null == path || "".equalsIgnoreCase(path)) {
                    return null;
                }
                return DataCollectionUtils.receiverimageNumDataHome(path);
            }

        }.execute();
        return null;
    }

    int i = 0;

    public class DownLoaderAsyncTask extends
            AsyncTask<String, Void, List<String[]>> {
        final String id;
        final String path;
        final String type;

        public DownLoaderAsyncTask(final String id, final String path,
                                   final String type) {
            // TODO Auto-generated constructor stub
            this.id = id;
            this.path = path;
            this.type = type;
            results = new ArrayList<String[]>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String[]> doInBackground(String... params) {
            if (null == path || "".equalsIgnoreCase(path)) {
                return null;
            }

            if ("0".equals(type)) {// 邮箱取数量
                return DataCollectionUtils.receiverEmailNumDataHome(path);
            } else
                return DataCollectionUtils.receiveApproveNumberNew(path);
        }

        @Override
        protected void onPostExecute(List<String[]> result) {
            super.onPostExecute(result);

            if (result != null && result.size() > 0) {
                results = result;
                for (String[] numbers : result) {
                    String tabId = numbers[0];
                    String count = numbers[1];
                    // if ("dbsy".equalsIgnoreCase(tabId)) {
                    int total = 0;
                    for (String[] number : result) {
                        int tempNumber = 0;
                        try {
                            if (!TextUtils.isEmpty(number[1])) {
                                tempNumber = Integer.parseInt(number[1]);
                            }
                        } catch (Exception e) {
                            tempNumber = 0;
                            e.printStackTrace();
                        }
                        total += tempNumber;
                    }
                    try {
                        System.out.println("id:" + id + "  count:" + count);
                        DataCache.taskCount.put(id, total);
                        Intent intentNum = new Intent(
                                Constants.MODIFY_APP_MAIN_SKIN);
                        intentNum.putExtra("type", "updateTabNum");
                        intentNum.putExtra("number", total);
                        mContext.sendBroadcast(intentNum);
                        mContext.sendBroadcast(new Intent(
                                Constants.MODIFY_APP_MENU_NUM));
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }
    }

    public class getSubmitUrlAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String string = DataCollectionUtils.getSubmitUrl(UserInfo
                    .getInstance().getUsername());
            return string;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!result.equals("")) {
                if (result.indexOf("#") > 0) {
                    String[] strings = result.split("#");
                    WebUtils.submitUrl = strings[0];
                } else {
                    WebUtils.submitUrl = result;
                }
                System.out.println("submit=======" + WebUtils.submitUrl);
            }

        }
    }

}
