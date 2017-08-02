package cn.sbx.deeper.moblie.logic;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.domian.Receiver;
import cn.sbx.deeper.moblie.domian.Router;
import cn.sbx.deeper.moblie.domian.Row;
import cn.sbx.deeper.moblie.domian.TaskView;



/**
 * 所有待办已办数据解析类
 * 
 * @author terry.C
 * 
 */
public class DataCollectionLogic {

	public static TaskView getTaskView(InputStream in) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		TaskView taskView = null;
		List<Row> rows = null;
		List<Router> routers = null;
		Router router = null;
		Row row = null;
		boolean inReceiver = false;
		List<Receiver> receivers = null;
		Receiver receiver = null;
//		Log.i("logi", new String(LoadUtils.load(in)));
		
		parser.setInput(in, "UTF-8");
		int event = parser.getEventType();
		// for (int event = parser.getEventType(); event !=
		// XmlPullParser.END_DOCUMENT; event = parser
		// .next()) {
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_TAG:
				if ("TaskView".equalsIgnoreCase(parser.getName())) {
					taskView = new TaskView();
				} else if ("ItemID".equalsIgnoreCase(parser.getName())) {
					taskView.setItemId(parser.nextText());
				} else if ("Title".equalsIgnoreCase(parser.getName())) {
					taskView.setTitle(parser.nextText());
				} else if ("Subtitle".equalsIgnoreCase(parser.getName())) {
					taskView.setSubTitle(parser.nextText());
				} else if ("Priority".equalsIgnoreCase(parser.getName())) {
					taskView.setPriority(parser.nextText());
				} else if ("Confidential".equalsIgnoreCase(parser.getName())) {
					taskView.setConfidential(parser.nextText());
				} else if ("Content".equalsIgnoreCase(parser.getName())) {
					rows = new ArrayList<Row>();
					taskView.setRows(rows);
				} else if ("Row".equalsIgnoreCase(parser.getName())) {
					row = new Row();
					rows.add(row);
				} else if ("Label".equalsIgnoreCase(parser.getName())) {
					row.setLabel(parser.nextText());
				} else if ("Text".equalsIgnoreCase(parser.getName())) {
					if (parser.getAttributeCount() > 0) {
						row.setType(parser.getAttributeValue(0));
					}
					row.setText(parser.nextText());
					// row.setType(parser.getAttributeValue(0));

					// if(parser.getAttributeName(0) != null) {
					// row.setType(parser.getAttributeValue(0));
					// }
				} else if ("Submit".equalsIgnoreCase(parser.getName())) {
					routers = new ArrayList<Router>();
					taskView.setRouters(routers);
				} else if ("Item".equalsIgnoreCase(parser.getName())) {
					router = new Router();

					String receiverRequired = parser.getAttributeValue(null, "receiverRequired");
					if (receiverRequired != null && !receiverRequired.equals("")) {
						router.setReceiverRequired(receiverRequired); // 接收人
					} else {
						router.setReceiverRequired("1"); // 接收人
					}

					String commentRequired = parser.getAttributeValue(null, "commentRequired");
					if (commentRequired != null && !commentRequired.equals("")) {
						router.setCommentRequired(commentRequired); // 意见填写
					} else {
						router.setCommentRequired("1"); // 意见填写
					}

					String commentVisible = parser.getAttributeValue(null, "commentVisible");
					if (commentVisible != null && !commentVisible.equals("")) {
						router.setCommentVisible(commentVisible); // 意见框是否可见
					} else {
						router.setCommentVisible("1"); // 意见框是否可见
					}
					String initOuid=parser.getAttributeValue(null,"initOUID");
					router.setInitOuid(null == initOuid?"":initOuid);

					if (parser.getAttributeCount() > 0) {
						router.setAction(parser.getAttributeValue(0));
					}
					routers.add(router);
					inReceiver = false;
				} else if ("ID".equalsIgnoreCase(parser.getName()) && !inReceiver) {
					router.setId(parser.nextText());
				} else if ("CaptionText".equalsIgnoreCase(parser.getName())) {
					router.setCaptionText(parser.nextText());
				} else if("PeoplePicker".equalsIgnoreCase(parser.getName())){
					router.setPeoplePicker(parser.nextText());
				}
				else if ("Receivers".equalsIgnoreCase(parser.getName())) {
					receivers = new ArrayList<Receiver>();
					router.setMultiple(Integer.parseInt(parser.getAttributeValue(0) == null ? "0" : parser.getAttributeValue(0)));
					router.setReceivers(receivers);
				} else if ("Receiver".equalsIgnoreCase(parser.getName())) {
					receiver = new Receiver();
					receivers.add(receiver);
					inReceiver = true;
				} else if ("ID".equalsIgnoreCase(parser.getName()) && inReceiver) {
					receiver.setId(parser.nextText());
				} else if ("Name".equalsIgnoreCase(parser.getName()) && inReceiver) {
					receiver.setName(parser.nextText());
				} else if ("JobTitle".equalsIgnoreCase(parser.getName()) && inReceiver) {
					receiver.setJobTitle(parser.nextText());
				} else if ("Department".equalsIgnoreCase(parser.getName()) && inReceiver) {
					receiver.setDepartment(parser.nextText());
				}
				break;
			}
			event = parser.next();
		}
		return taskView;
	}

	/**
	 * 取得待办数目
	 */
	public static int receiverPendingCounts(InputStream is) throws Exception {
		int i = 0;
		boolean isPending = false;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		try {
			for (int event = parser.getEventType(); event != XmlPullParser.END_DOCUMENT; event = parser.next())
				switch (event) {
				case XmlPullParser.START_TAG:
					if ("Pending".equalsIgnoreCase(parser.getName())) {
						isPending = true;
					} else if ("Completed".equalsIgnoreCase(parser.getName())) {
						isPending = false;
					} else if ("Closed".equalsIgnoreCase(parser.getName())) {
						isPending = false;
					} else if ("Item".equalsIgnoreCase(parser.getName())) {
						if (isPending) {
							i = i + 1;
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if ("Pending".equalsIgnoreCase(parser.getName())) {
						isPending = false;
						return i;
					}
					break;
				}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return i;
	}
}
