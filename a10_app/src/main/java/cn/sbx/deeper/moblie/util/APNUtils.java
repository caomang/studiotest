package cn.sbx.deeper.moblie.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

public class APNUtils {
	private static final String mTAG = "APNUtils";
	static Context mContext;
	static String cmcc = null;
	static String cmnc = null;
	static String cnumeric = null;
	final static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");
	final static Uri APN_TABLE_URI = Uri.parse("content://telephony/carriers");

	public static void setContext(Context context) {
		mContext = context;
	}

	public static int insertAPN(String name, String apn_addr) {
		int id = -1;
		prepareApnCodes();
		ContentResolver resolver = mContext.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("apn", apn_addr);

		values.put("mcc", cmcc != null ? cmcc : "460");
		values.put("mnc", cmnc != null ? cmnc : "02");
		values.put("numeric", cnumeric != null ? cnumeric : "46002");

		Cursor c = null;
		try {
			Uri newRow = resolver.insert(APN_TABLE_URI, values);
			if (newRow != null) {
				c = resolver.query(newRow, null, null, null, null);

				int idindex = c.getColumnIndex("_id");
				c.moveToFirst();
				id = c.getShort(idindex);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (c != null)
			c.close();
		return id;
	}

	/**
	 * delete the apn item by apn id
	 * 
	 * @param id
	 *            apn id to delete
	 * @return
	 */
	public static int deleteAPN(int id) {
		ContentResolver resolver = mContext.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("_id", id);
		return resolver.delete(APN_TABLE_URI, "_id=" + id, null);
	}

	/*
	 * Set an apn to be the default apn for web traffic Require an input of the
	 * apn id to be set
	 */
	public static boolean SetDefaultAPN(int id) {
		boolean res = false;
		ContentResolver resolver = mContext.getContentResolver();
		ContentValues values = new ContentValues();

		// See /etc/apns-conf.xml. The TelephonyProvider uses this file to
		// provide
		// content://telephony/carriers/preferapn URI mapping
		values.put("apn_id", id);
		try {
			resolver.update(PREFERRED_APN_URI, values, null, null);
			Cursor c = resolver.query(PREFERRED_APN_URI, new String[] { "name",
					"apn" }, "_id=" + id, null, null);
			if (c != null) {
				res = true;
				c.close();
			}
		} catch (SQLException e) {
			Log.d(mTAG, e.getMessage());
		}
		return res;
	}

	/**
	 * check if apn exists,if exists,then current apn id will be returned,else
	 * returns -1
	 * 
	 * @param apnName
	 *            APN name
	 * @return
	 */
	public static int fetchAPNId(String apnName) {
		int apnId = -1;
		ContentResolver resolver = mContext.getContentResolver();

		try {
			Cursor c = resolver.query(APN_TABLE_URI, new String[] { "_id" },
					"apn='" + apnName + "'", null, null);

			if (c != null) {
				if (c.moveToFirst()) {
					apnId = c.getInt(c.getColumnIndex("_id"));
				}
				c.close();
			}
		} catch (SQLException e) {
			Log.d(mTAG, e.getMessage());
			apnId = -1;
		}
		return apnId;
	}

	public static int fetchCurrentApnId() {
		ContentResolver resolver = mContext.getContentResolver();
		Cursor c = null;
		try {
			c = resolver.query(PREFERRED_APN_URI, new String[] { "_id" }, null,
					null, null);
			if (c != null) {
				if (c.moveToNext()){
//					System.out.println("currtentId=> "+c.getInt(c.getColumnIndex("_id")));
					return c.getInt(0);
				}

			}
		} catch (SQLException e) {
			Log.d(mTAG, e.getMessage());
		} finally {
			if (c != null)
				c.close();
		}
		return -1;
	}

	private static void prepareApnCodes() {
		ContentResolver resolver = mContext.getContentResolver();
		Cursor c = null;
		try {
			c = resolver.query(PREFERRED_APN_URI, new String[] { "mcc", "mnc",
					"numeric" }, null, null, null);
			if (c != null) {
				if (c.moveToNext()) {
					cmcc = c.getString(0);
					cmnc = c.getString(1);
					cnumeric = c.getString(2);
				}
			}
		} catch (SQLException e) {
			Log.d(mTAG, e.getMessage());
		} finally {
			if (c != null)
				c.close();
		}
	}

}
