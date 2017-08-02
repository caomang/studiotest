package com.sunboxsoft.monitor.utils;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Handler;

import com.android.print.sdk.PrinterInstance;

public interface IPrinterOpertion {
	void open(String mac);

	void close();

	void chooseDevice();

	PrinterInstance getPrinter();

	void usbAutoConn(UsbManager manager);

	void btAutoConn(Context context, Handler mHandler);


}
