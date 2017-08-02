package cn.sbx.deeper.moblie.util;

import com.sunboxsoft.monitor.model.ThemeType;

import java.util.HashMap;
import java.util.Map;

public class ClientConfig {

	private static ClientConfig instance = null;

	private ClientConfig() {
	}

	public synchronized static ClientConfig getInstance() {
		if (null == instance) {
			instance = new ClientConfig();
		}
		return instance;
	}

	private Map<String, String> clientConfig = new HashMap<String, String>();

	public void setClientConfig(String id, String value) {
		clientConfig.put(id, value);
	}

	public boolean isMainMenuTextAlignCenter() {
		if (null == clientConfig || clientConfig.size() == 0) {
			return false;
		}
		String value = clientConfig.get("AlignCenter");
		if (null == value)
			return false;

		if ("0".equals(value)) {
			return false;
		} else if ("1".equals(value)) {
			return true;
		}
		return false;
	}

	public String getLogoName() {
		if (null == clientConfig || clientConfig.size() == 0) {
			return "";
		}
		String value = clientConfig.get("LogoName");
		if (null == value)
			return "";
		return value;
	}

	public boolean isRunBack() {
		if (null == clientConfig || clientConfig.size() == 0) {
			return false;
		}
		String value = clientConfig.get("RunBack");
		if (null == value)
			return false;

		if ("0".equals(value)) {
			return false;
		} else if ("1".equals(value)) {
			return true;
		}
		return false;
	}

	public boolean isShowUseranme() {
		if (null == clientConfig || clientConfig.size() == 0) {
			return true;
		}
		String value = clientConfig.get("ShowName");
		if (null == value)
			return true;

		if ("0".equals(value)) {
			return false;
		} else if ("1".equals(value)) {
			return true;
		}
		return true;
	}

	public boolean isShowPasswd() {
		if (null == clientConfig || clientConfig.size() == 0) {
			return true;
		}
		String value = clientConfig.get("ShowPwd");
		if (null == value)
			return false;

		if ("0".equals(value)) {
			return false;
		} else if ("1".equals(value)) {
			return true;
		}
		return true;
	}

	public boolean isShowRegarding() {
		if (null == clientConfig || clientConfig.size() == 0) {
			return true;
		}
		String value = clientConfig.get("ShowRegarding");
		if (null == value)
			return false;

		if ("0".equals(value)) {
			return false;
		} else if ("1".equals(value)) {
			return true;
		}
		return true;
	}

	public ThemeType getThemeType() {
		if (null == clientConfig || clientConfig.size() == 0) {
			return ThemeType.BLUE;
		}
		String value = clientConfig.get("Theme");
		if (null == value)
			return ThemeType.BLUE;
		if ("red".equalsIgnoreCase(value)) {
			return ThemeType.RED;
		} else if ("blue".equalsIgnoreCase(value)) {
			return ThemeType.BLUE;
		} else if ("default".equalsIgnoreCase(value)) {
			return ThemeType.DEFAULT;
		}
		return ThemeType.BLUE;
	}

	public boolean isShowVideoAll() {
		if (null == clientConfig || clientConfig.size() == 0) {
			return true;
		}
		String value = clientConfig.get("VideoShowAll");
		if (null == value)
			return true;
		if ("0".equals(value)) {
			return false;
		} else if ("1".equals(value)) {
			return true;
		}
		return true;
	}
	
	public boolean isShowHelp() {
		if (null == clientConfig || clientConfig.size() == 0) {
			return true;
		}
		String value = clientConfig.get("VideoShowAll");
		if (null == value)
			return true;
		if ("0".equals(value)) {
			return false;
		} else if ("1".equals(value)) {
			return true;
		}
		return true;
	}

	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : clientConfig.entrySet()) {
			sb.append("id:" + entry.getKey()).append(
					"  value:" + entry.getValue() + "\n");
		}
		return sb.toString();
	}
}
