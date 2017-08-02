package cn.sbx.deeper.moblie.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.print.sdk.PrinterConstants.Command;
import com.android.print.sdk.PrinterConstants.Connect;
import com.android.print.sdk.PrinterInstance;
import com.android.print.sdk.bluetooth.BluetoothPort;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import cn.sbx.deeper.moblie.domian.DictionariesHiddenDanger;
import cn.sbx.deeper.moblie.domian.NoticePayment;
import cn.sbx.deeper.moblie.domian.SecurityCheck;
import cn.sbx.deeper.moblie.domian.SecurityCheckHiddenDanger;
import cn.sbx.deeper.moblie.fargments.AnJianDetailFragment;

public class MobilePrint {

    private Activity act;

    public MobilePrint(Activity act) {
        this.act = act;
    }

    public BluetoothAdapter adapter;
    public PrinterInstance mPrinter;
    public boolean isConnected;
    public String address;
    public MyBlueToothReceiver receiver;
    public boolean isAleadyregist = false;

    // ------------打印机调用方法-------------
    public Boolean GetBluetoothData() {
        Boolean result = false;
        // 获取蓝牙
        adapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> device = adapter.getBondedDevices();
        if (device.size() > 0) {
            for (BluetoothDevice bd : device) {
                if (bd.getName().contains("T10 BT Printer")) {
                    mPrinter = new BluetoothPort().btConnnect(act,
                            bd.getAddress(), adapter, mHandler);
                    result = true;
                }else if(bd.getName().contains("T12 BT Printer")){
                    mPrinter = new BluetoothPort().btConnnect(act,
                            bd.getAddress(), adapter, mHandler);
                    result = true;
                }

//				if (bd.getAddress().equals("00:1B:35:0D:AD:E4")) {
//					mPrinter = new BluetoothPort().btConnnect(act,
//							bd.getAddress(), adapter, mHandler);
//					result = true;
//				}
            }
        }
        return result;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Connect.SUCCESS:
                    isConnected = true;
                    // mPrinter = new BluetoothPort().btConnnect(getActivity(),
                    // address, adapter, mHandler);

                    Toast.makeText(act, "蓝牙已连接", Toast.LENGTH_SHORT).show();
                    // mPrinter = myOpertion.getPrinter();
                    break;

                default:
                    break;
            }
        }
    };

    public void initBluetooth() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        receiver = new MyBlueToothReceiver();
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        act.registerReceiver(receiver, intentFilter);
        isAleadyregist = true;

        if (!adapter.isEnabled()) {
            // 打开蓝牙
            adapter.enable();

        }
        if (!GetBluetoothData()) {
            // 已配对列表中获取设备信息为false 时重新扫描
            adapter.startDiscovery();
        }

    }

    public class MyBlueToothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //
                if (device.getAddress().equals("00:1B:35:0D:AD:E4")) {

                    address = device.getAddress();
                    isConnected = true;
                    mPrinter = new BluetoothPort().btConnnect(act, address,
                            adapter, mHandler);

                    // 停止扫描蓝牙
                    adapter.cancelDiscovery();
                }
            }
        }
    }

    // 抄表打印表单
    public void printNote(Resources resources, PrinterInstance mPrinter,
                          boolean is58mm, NoticePayment notice) {
        mPrinter.init();

        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
        mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

        StringBuffer sb = new StringBuffer();

        mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
        mPrinter.setCharacterMultiple(1, 1);
        mPrinter.printText("缴费通知单" + "\n");
        mPrinter.printText("测试样例" + "\n");
        mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
        mPrinter.setCharacterMultiple(0, 0);
        sb.append("客户编号：" + notice.userNo + "\n");
        sb.append("地址：" + notice.path + "\n");
        sb.append("年阶梯气量基数：" + "0-600方\n");
        sb.append("                " + "601-1200方\n");
        sb.append("                " + "1201方以上\n");
        sb.append("抄表日期：" + notice.accounting + "\n");
        sb.append("上期读数：" + notice.theReadings + "\n");
        sb.append("本期读数：" + notice.currentReadings + "\n");
        sb.append("本期气量：" + notice.currentCapacity + "方\n");
        sb.append("本期气费：" + notice.currentGasFee + "元\n");
        sb.append("    第一阶梯 气价 ：" + notice.firstGasPrice + "\n");
        sb.append("             气量 ：" + notice.firstGasVolume + "方\n");
        sb.append("             气费 ：" + notice.currentGasFee + "元\n");
        sb.append("--------------------------------\n");
        sb.append("抄表前账户余额：" + notice.pastArrears + "元\n");
        sb.append("统计日期：" + notice.statisticalDate + "\n");
        sb.append("--------------------------------\n");
        sb.append("打印日期：" + notice.printDate + "\n");
        sb.append("抄表员：" + notice.userName + "\n");
        sb.append("联系电话：" + "15855556666" + "\n");
        // sb.append("联系电话：" + notice.contactNumber + "\n");
        sb.append("请您在本月25日（出账）之后缴费。");
        mPrinter.printText(sb.toString()); // 打印

        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
        mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

    }

    // 安全检查工作单（用户留存）--不存在安全隐患
    public void SafetyChecklist(Resources resources, PrinterInstance mPrinter,
                                boolean is58mm, SecurityCheck sc, String imagePath, String loginNameForSign) {
        // 获取当前日期
        SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 打印2份小票
        for (int i = 1; i < 3; i++) {
            mPrinter.init();

            mPrinter.setFont(0, 0, 0, 0);
            mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
            mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            StringBuffer sb = new StringBuffer();

            mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
            mPrinter.setCharacterMultiple(1, 1);
//            mPrinter.setCharacterMultiple(4, 4);

            mPrinter.printText("安全检查工作单" + "\n");
            mPrinter.printText("测试样例" + "\n");
            if (i == 1) {
                mPrinter.printText("（用户留存）" + "\n");
            } else {
                mPrinter.printText("（单位留存）" + "\n");
            }

            mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
            mPrinter.setCharacterMultiple(0, 0);
            sb.append("客户编号：" + sc.userNo + "\n");
            sb.append("地址：" + sc.path + "\n");
            sb.append("安检日期：" + sc.checkDate + "\n");
            sb.append("是否存在安全隐患：" + "否" + "\n");
            sb.append("----------具体安检结果----------\n");
            sb.append("立管：" + "正常" + "\n");
            sb.append("支管：" + "正常" + "\n");
            sb.append("阀门：" + "正常" + "\n");
            sb.append("表具 ：" + "正常" + "\n");
            sb.append("连接管：" + "正常" + "\n");
            sb.append("灶具：" + "正常" + "\n");
            sb.append("采暖炉 ：" + "正常" + "\n");
            sb.append("热水器 ：" + "正常" + "\n");
            sb.append("其它 ：" + "正常" + "\n");
            sb.append("--------------------------------\n");
            if (sc.cmMrCommCd != null)
                sb.append("服务评价：" + sc.cmMrCommCd + "\n\n");
            else
                sb.append("服务评价：" + "\n");
            sb.append("客户签字：" + "\n\n");
//			sb.append("安检员签字：" + "\n\n");
//			sb.append("打印日期：" + Date.format(new java.util.Date()) + "\n");
            mPrinter.printText(sb.toString()); // 打印

            mPrinter.setFont(0, 0, 0, 0);
            mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
//			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2; //图片宽高都为原来的二分之一，即图片为原来的四分之一
            Bitmap bitmap1 = BitmapFactory.decodeFile(imagePath, options);

            if(bitmap1!=null){
                int width = bitmap1.getWidth();
                int height = bitmap1.getHeight();
                int newWidth = 400;
                int newHeight = 240;
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的图片
                Bitmap newbm = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix,
                        true);
                if (newbm != null) {
                    mPrinter.printImage(newbm);
                }

            }




//            if (bitmap1 != null) {
//                mPrinter.printImage(bitmap1);
//            }

            mPrinter.printText("" + "\n");
            mPrinter.printText("安检员签字：" + "\n");
            mPrinter.printText(loginNameForSign + "\n\n");
            mPrinter.printText("打印日期：" + Date.format(new java.util.Date()) + "\n");
            mPrinter.setFont(0, 0, 0, 0);
            mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
            mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
        }
    }

    // 安全检查工作单（用户留存）--存在安全隐患
    public void SafetyChecklist_HiddenDanger(Resources resources,
                                             PrinterInstance mPrinter, boolean is58mm, SecurityCheck sc,
                                             List<DictionariesHiddenDanger> dicList, String taskId,
                                             String imagePath, String loginNameForSign,Context context) {
        // 获取当前日期
        SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 打印2份小票
        for (int i = 1; i < 3; i++) {
            mPrinter.init();

            mPrinter.setFont(0, 0, 0, 0);
            mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
            mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);

            StringBuffer sb = new StringBuffer();

            mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
            mPrinter.setCharacterMultiple(1, 1);
            // mPrinter.setCharacterMultiple(1, 1);
            mPrinter.printText("安全检查工作单" + "\n");
            mPrinter.printText("测试样例" + "\n");

            if (i == 1) {
                mPrinter.printText("（用户留存）" + "\n");
            } else {
                mPrinter.printText("（单位留存）" + "\n");
            }

            mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
            mPrinter.setCharacterMultiple(0, 0);
            sb.append("客户编号：" + sc.userNo + "\n");
            sb.append("地址：" + sc.path + "\n");
            sb.append("安检日期：" + sc.checkDate + "\n");
            sb.append("是否存在安全隐患：" + "是" + "\n");
            sb.append("----------具体安检结果----------\n");
            // 创建类对象
            AnJianDetailFragment aj = new AnJianDetailFragment();
            // 遍历所有的隐患选项
            for (DictionariesHiddenDanger dic : dicList) {
                // 判断隐患类型 是否存在
                List<SecurityCheckHiddenDanger> hiddenDanger = aj
                        .SelectHiddenDanger(taskId, dic.cmScShType,context);
                // 存在安全隐患
                if (hiddenDanger.size() > 0) {
                    // 打印异常
                    sb.append(dic.cmScShTypeDescr + "：" + "异常" + "\n");
                    // --- 遍历异常数据 ---
                    for (SecurityCheckHiddenDanger danger : hiddenDanger) {
                        // 打印异常信息
//                        sb.append("    " + danger.cmScShItemDescr + "\n");
                        sb.append("    " + danger.cmScShItemDescr + "\n");
                    }
                } else {
                    sb.append(dic.cmScShTypeDescr + "：" + "正常" + "\n");
                }
            }
            sb.append("--------------------------------\n");
            if (sc.cmMrCommCd != null)
                sb.append("服务评价：" + sc.cmMrCommCd + "\n\n");
            else

                sb.append("服务评价：" + "\n");
            sb.append("客户签字：" + "\n\n");
//			sb.append("安检员签字：" + "\n\n");
//			sb.append("打印日期：" + Date.format(new java.util.Date()) + "\n");

            mPrinter.printText(sb.toString()); // 打印

            mPrinter.setFont(0, 0, 0, 0);
            mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
//			mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);


            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2; //图片宽高都为原来的二分之一，即图片为原来的四分之一
            Bitmap bitmap1 = BitmapFactory.decodeFile(imagePath, options);

            if(bitmap1!=null){
                int width = bitmap1.getWidth();
                int height = bitmap1.getHeight();
                int newWidth = 400;
                int newHeight = 240;

                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;

                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的图片
                Bitmap newbm = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix,
                        true);


//			Bitmap bitmap1 =BitmapFactory.decodeResource(resources, R.drawable.aaaaa,options);
//            if (bitmap1 != null) {
//                mPrinter.printImage(bitmap1);
//            }
                if (newbm != null) {
                    mPrinter.printImage(newbm);
                }
            }


            mPrinter.printText("" + "\n");
            mPrinter.printText("安检员签字：" + "\n");
            mPrinter.printText(loginNameForSign + "\n\n");
            mPrinter.printText("打印日期：" + Date.format(new java.util.Date()) + "\n");
            mPrinter.setFont(0, 0, 0, 0);
            mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
            mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
        }
    }
}
