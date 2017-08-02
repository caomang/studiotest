package com.sunboxsoft.monitor.model;

import com.sunboxsoft.monitor.utils.DataUtils;

public class VideoInfo {
	public VideoInfo() {

	}

	public VideoInfo(int cmd, String addr, int port, int vWidth, int vHeight,
			int vBitRate, int vFrameRate) {
		super();
		this.cmd = cmd;
		this.len = getByteSize();
		this.addr = addr;
		this.port = port;
		this.vWidth = vWidth;
		this.vHeight = vHeight;
		this.vBitRate = vBitRate;
		this.vFrameRate = vFrameRate;
	}

	public int cmd;
	public int len;
	public String addr;
	public int port;
	public int vWidth;
	public int vHeight;
	public int vBitRate;
	public int vFrameRate;

	public int getByteSize() {
		return 4 * 7 + 16;
	}

	public byte[] prepareSendData() {
		byte[] tmp = null;
		byte[] buff = new byte[this.getByteSize()];

		tmp = DataUtils.toLH(this.cmd);
		System.arraycopy(tmp, 0, buff, 0, 4);

		tmp = DataUtils.toLH(this.len);
		System.arraycopy(tmp, 0, buff, 4, 4);

		System.arraycopy(this.addr.getBytes(), 0, buff, 8, 10);

		tmp = DataUtils.toLH(this.port);
		System.arraycopy(tmp, 0, buff, 24, 4);

		tmp = DataUtils.toLH(this.vWidth);
		System.arraycopy(tmp, 0, buff, 28, 4);

		tmp = DataUtils.toLH(this.vHeight);
		System.arraycopy(tmp, 0, buff, 32, 4);

		tmp = DataUtils.toLH(this.vBitRate);
		System.arraycopy(tmp, 0, buff, 36, 4);

		tmp = DataUtils.toLH(this.vFrameRate);
		System.arraycopy(tmp, 0, buff, 40, 4);

		return buff;

	}
}