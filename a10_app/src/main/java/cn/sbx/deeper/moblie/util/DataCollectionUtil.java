package cn.sbx.deeper.moblie.util;

import android.util.Xml;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.ContactDepartment;
import cn.sbx.deeper.moblie.domian.Employee;
import cn.sbx.deeper.moblie.domian.Employee2;
import cn.sbx.deeper.moblie.domian.Row;

public class DataCollectionUtil {
	// 获取通讯录组织机构
	public static List<ContactDepartment> getContactGroup(String pageId) {
		List<ContactDepartment> departments = new ArrayList<ContactDepartment>();
		InputStream is = null;
		ContactDepartment dept = null;
		try {

			// String paths =
			// "http://10.238.192.250/dataMutualCenter/Integrated/GetMobileData.aspx";
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(WebUtils.rootUrl
					+ URLUtils.getContact);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("pageId", pageId));
			// nameValuePairs.add(new BasicNameValuePair("key1", "1595"));
			nameValuePairs.add(new BasicNameValuePair("EquipType", "Android"));
			nameValuePairs.add(new BasicNameValuePair("EquipSN",
					WebUtils.deviceId));
			nameValuePairs.add(new BasicNameValuePair("SCode",
					Constants.testPackage));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

			setHeader(httpPost);
			HttpResponse response = hc.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			// byte[] len = new byte[1024];
			// int arr = 0;
			// while ((arr = is.read(len)) != 0) {
			// System.out.println(new String(len, 0, arr) +
			// "这通训录的组织机构==========================");
			// }

			// is = getConnectByHttpPost(path);
			// if (is != null) {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if ("Department".equalsIgnoreCase(parser.getName())) {
						dept = new ContactDepartment();
						String id = parser.getAttributeValue(null, "id");
						dept.id = id;
						// dept.setId(id);
						// System.out.println(id +
						// "===================================");
						dept.name = parser.getAttributeValue(null, "name");
						dept.parentid = parser.getAttributeValue(null,
								"parentid");
						departments.add(dept);
						System.out.println("aaaaaaaaaaaaaaaaa 网速怎么这么慢。。。。。。。。");
					}
				}
				eventType = parser.next();
			}
			// }
			return departments;
		} catch (Exception e) {
			if (true)
				e.printStackTrace();
			return new ArrayList<ContactDepartment>();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					if (true)
						e.printStackTrace();
				}
			}
		}

	}

	// 获取通讯录组织机构
	public static List<ContactDepartment> getContactGroups(String pagePath,
			String pageId) {
		List<ContactDepartment> departments = new ArrayList<ContactDepartment>();
		InputStream is = null;
		ContactDepartment dept = null;
		try {

			// String paths =
			// "http://10.238.192.250/dataMutualCenter/Integrated/GetMobileData.aspx";
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(WebUtils.rootUrl
					+ URLUtils.getContact);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("pageId", pagePath));
			nameValuePairs.add(new BasicNameValuePair("key1", pageId));
			nameValuePairs.add(new BasicNameValuePair("EquipType", "Android"));
			nameValuePairs.add(new BasicNameValuePair("EquipSN",
					WebUtils.deviceId));
			nameValuePairs.add(new BasicNameValuePair("SCode",
					Constants.testPackage));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

			setHeader(httpPost);
			HttpResponse response = hc.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			// byte[] len = new byte[1024];
			// int arr = 0;
			// while ((arr = is.read(len)) != 0) {
			// System.out.println(new String(len, 0, arr) +
			// "这通训录的组织机构==========================");
			// }

			// is = getConnectByHttpPost(path);
			// if (is != null) {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if ("Department".equalsIgnoreCase(parser.getName())) {
						dept = new ContactDepartment();
						String id = parser.getAttributeValue(null, "id");
						dept.id = id;
						// dept.setId(id);
						// System.out.println(id +
						// "===================================");
						dept.name = parser.getAttributeValue(null, "name");
						dept.parentid = parser.getAttributeValue(null,
								"parentid");
						departments.add(dept);
						System.out.println("aaaaaaaaaaaaaaaaa 网速怎么这么慢。。。。。。。。");
					}
				}
				eventType = parser.next();
			}
			// }
			return departments;
		} catch (Exception e) {
			if (true)
				e.printStackTrace();
			return new ArrayList<ContactDepartment>();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					if (true)
						e.printStackTrace();
				}
			}
		}

	}

	private static InputStream getConnectByHttpPost(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	// 获取人员信息
	public static List<Employee2> retrieveEmployees(String id, String pageId) {
		List<Employee2> lists = new ArrayList<Employee2>();
		lists.clear();
		InputStream is = null;
		Employee2 emp = null;
		try {

			// String paths =
			// "http://10.238.192.250/dataMutualCenter/Integrated/GetMobileData.aspx";
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(WebUtils.rootUrl
					+ URLUtils.getContact);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("pageId", pageId));
			nameValuePairs.add(new BasicNameValuePair("key1", id));
			nameValuePairs.add(new BasicNameValuePair("EquipType", "Android"));
			nameValuePairs.add(new BasicNameValuePair("EquipSN",
					WebUtils.deviceId));
			nameValuePairs.add(new BasicNameValuePair("SCode",
					Constants.testPackage));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

			setHeader(httpPost);
			HttpResponse response = hc.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			// byte[] len = new byte[1024];
			// int arr = 0;
			// while ((arr = is.read(len)) != 0) {
			// System.out.println(new String(len, 0, arr));
			// }

			// is = getConnectByHttpPost(path);
			// if (is != null) {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if ("user".equalsIgnoreCase(parser.getName())) {
						emp = new Employee2();
						emp.id = parser.getAttributeValue(null, "id");
						// emp.altname = parser.getAttributeValue(null,
						// "altname");
						// emp.company = parser.getAttributeValue(null,
						// "company");
						// emp.department = parser.getAttributeValue(null,
						// "department");
						// emp.email = parser.getAttributeValue(null, "email");
						// emp.faxNo = parser.getAttributeValue(null, "fax");
						// emp.fullname = parser.getAttributeValue(null,
						// "fullname");
						// emp.mobile = parser.getAttributeValue(null,
						// "mobile");
						// emp.officer = parser.getAttributeValue(null,
						// "officer");
						// emp.onLine = parser.getAttributeValue(null,
						// "online");
						// emp.phone = parser.getAttributeValue(null, "phone");
						lists.add(emp);
						emp.rows = new ArrayList<Row>();

						// id="linmuh.mmsh"
						// altname="林木和"
						// company=""
						// department=""
						// email=""
						// fax=""
						// fullname="林木和"
						// mobile=""
						// officer=""
						// online=""
						// phone="" />
					} else if ("row".equalsIgnoreCase(parser.getName())) {// added
																			// by
																			// wangst
						// <row label="姓名" text="郭新" category="name"/>
						Row row = new Row();
						row.setLabel(parser.getAttributeValue(null, "label")
								+ "");
						row.setText(parser.getAttributeValue(null, "text") + "");
						row.setCategory(parser.getAttributeValue(null,
								"category") + "");
						emp.rows.add(row);
					}
				}
				eventType = parser.next();
			}
			// }
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String getWebComponent(String pageId) {
		InputStream is = null;
		String webPate = null;
		try {
			String paths = WebUtils.rootUrl + "/Integrated/GetMobileData.aspx";
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(paths);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("pageId", pageId));
			nameValuePairs.add(new BasicNameValuePair("EquipType", "Android"));
			nameValuePairs.add(new BasicNameValuePair("EquipSN",
					WebUtils.deviceId));
			nameValuePairs.add(new BasicNameValuePair("SCode",
					Constants.testPackage));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

			setHeader(httpPost);
			HttpResponse response = hc.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			// System.out.println("web:"+StreamUtils.retrieveContent(is));
			//
			byte[] len = new byte[1024];
			int arr = 0;
			while ((arr = is.read(len)) != 0) {
				webPate = new String(len, 0, arr);
				System.out.println(webPate + "web s==========================");
			}

			// TypeItem item = null;
			// XmlPullParser parser = Xml.newPullParser();
			// parser.setInput(is, "UTF-8");
			// int event = parser.getEventType();
			// while (event != XmlPullParser.END_DOCUMENT) {
			// switch (event) {
			// case XmlPullParser.START_TAG:
			// if ("item".equalsIgnoreCase(parser.getName())) {
			// item = new TypeItem();
			// // items.add(item);
			// item.name = parser.getAttributeValue(null, "name");
			// item.listid = parser.getAttributeValue(null, "listid");
			// item.detailid = parser.getAttributeValue(null,
			// "detailid");
			// }
			// break;
			// }
			// event = parser.next();
			// }

		} catch (Exception e) {
			if (Constants.DEBUG)
				e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					if (Constants.DEBUG)
						e.printStackTrace();
				}
			}
		}
		return webPate;
	}

	private static void setHeader(HttpPost httpPost) {
		httpPost.setHeader("EquipType", "Android");
		httpPost.setHeader("EquipSN", WebUtils.deviceId);
		httpPost.setHeader("Soft", WebUtils.packageName);
		httpPost.setHeader("Tel", WebUtils.phoneNumber);
		httpPost.setHeader("Cookie", WebUtils.cookie);
	}

	// 获取人员信息
	public static void retrieveEmployees(String id, ContactDepartment dept) {
		if (dept.employees == null || dept.employees.size() == 0) {
			InputStream is = null;
			Employee emp = null;
			try {
				// is = MainActivity.class.getClassLoader().getResourceAsStream(
				// "employees.xml");
				// is = getConnectByHttpPost(path);
				HttpParams httpParameters = new BasicHttpParams();
				DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
				HttpPost httpPost = new HttpPost(WebUtils.rootUrl
						+ URLUtils.getContact);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs
						.add(new BasicNameValuePair("pageId",
								"3a7f1a38-cef6-4133-b82f-a3c6f4c3b986@5209@AddressBook"));
				nameValuePairs.add(new BasicNameValuePair("key1", id));
				nameValuePairs.add(new BasicNameValuePair("SCode",
						Constants.testPackage));
				System.out
						.println(id
								+ "aaaaaaaaaaaaaaaaaaaaaaaaaa=================================bbbbbbbbbbbbbbbbbbbbb");
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						"UTF-8"));

				setHeader(httpPost);
				HttpResponse response = hc.execute(httpPost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

				if (is != null) {
					XmlPullParser parser = Xml.newPullParser();
					parser.setInput(is, "UTF-8");
					int eventType = parser.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						if (eventType == XmlPullParser.START_TAG) {
							if ("User".equalsIgnoreCase(parser.getName())) {
								emp = new Employee();
								emp.id = parser.getAttributeValue(null, "id");
								emp.userName = parser.getAttributeValue(null,
										"fullname");
								emp.name = parser.getAttributeValue(null,
										"altname");

								emp.email = parser.getAttributeValue(null,
										"email");

								/*
								 * emp.faxNo = parser.getAttributeValue(null,
								 * "fax");
								 */

								emp.company = parser.getAttributeValue(null,
										"company");
								emp.department = parser.getAttributeValue(null,
										"department");

								emp.mobileNo = parser.getAttributeValue(null,
										"mobile");

								emp.officeNo = parser.getAttributeValue(null,
										"officer");

								emp.onLine = parser.getAttributeValue(null,
										"onLine");

								emp.phone = parser.getAttributeValue(null,
										"phone");
								emp.deptName = parser.getAttributeValue(null,
										"depid");

								emp.dept = dept;
								dept.employees.add(emp);
							}
						}
						eventType = parser.next();
					}
				} else {

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	

}
