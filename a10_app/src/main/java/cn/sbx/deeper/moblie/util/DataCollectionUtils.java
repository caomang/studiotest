package cn.sbx.deeper.moblie.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cn.sbx.deeper.moblie.adapter.UnionOfficeAdapter;
import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.AppAccount;
import cn.sbx.deeper.moblie.domian.ApproveTab;
import cn.sbx.deeper.moblie.domian.Attachment;
import cn.sbx.deeper.moblie.domian.ChildAccountsBean;
import cn.sbx.deeper.moblie.domian.ContactDepartment;
import cn.sbx.deeper.moblie.domian.ContactPeople;
import cn.sbx.deeper.moblie.domian.Employee;
import cn.sbx.deeper.moblie.domian.Folders;
import cn.sbx.deeper.moblie.domian.GroupedEvent;
import cn.sbx.deeper.moblie.domian.GroupedEvent.SubGroupedEvent;
import cn.sbx.deeper.moblie.domian.LocucationInfo;
import cn.sbx.deeper.moblie.domian.MailContact;
import cn.sbx.deeper.moblie.domian.MeetingPdf;
import cn.sbx.deeper.moblie.domian.MobileNewLieb;
import cn.sbx.deeper.moblie.domian.MonitorDevice;
import cn.sbx.deeper.moblie.domian.MonitorDeviceGroup;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.ApprovalDetailTableInfo;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.ApprovalProcessInfo;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.ApprovalProcessInfo.ProcessSection;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup.ProcessGroupItem;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup.ProcessGroupItem.ProcessGroupItemTableInfo;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.SubmitInfo;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.SubmitInfo.SubRouters;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.SubmitInfo.SubRouters.SubRouter;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption.SubItem;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubTextView;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.SubmitInfo.SubTextBoard;
import cn.sbx.deeper.moblie.domian.NewOADetailInfo.SubmitInfo.SubTextBoard.TextBoardItem;
import cn.sbx.deeper.moblie.domian.NewOAInfo;
import cn.sbx.deeper.moblie.domian.NewOAInfo.AppContentListInfo;
import cn.sbx.deeper.moblie.domian.NewOAInfo.ApprovalSearchInfo;
import cn.sbx.deeper.moblie.domian.NewOAInfo.ApprovalSearchInfo.ApprovaldropDownList;
import cn.sbx.deeper.moblie.domian.NewOAInfo.ApprovalSearchInfo.ApproveContacts;
import cn.sbx.deeper.moblie.domian.NewOAInfo.ApprovalSearchInfo.ApproveDatePicker;
import cn.sbx.deeper.moblie.domian.NewOAInfo.ApprovalSearchInfo.ApproveDatePicker.ApproveDateItem;
import cn.sbx.deeper.moblie.domian.NewOAInfo.ApprovalSearchInfo.ApproveTextBox;
import cn.sbx.deeper.moblie.domian.NewsPaper;
import cn.sbx.deeper.moblie.domian.NewsPaperMain;
import cn.sbx.deeper.moblie.domian.NoticeDetail;
import cn.sbx.deeper.moblie.domian.PageShowTab;
import cn.sbx.deeper.moblie.domian.ReceiveAnnotateInfo;
import cn.sbx.deeper.moblie.domian.ReceiveAnnotateInfo.HostInfo;
import cn.sbx.deeper.moblie.domian.Receiver;
import cn.sbx.deeper.moblie.domian.ResultInfo;
import cn.sbx.deeper.moblie.domian.Router;
import cn.sbx.deeper.moblie.domian.Row;
import cn.sbx.deeper.moblie.domian.ScheduleData;
import cn.sbx.deeper.moblie.domian.ScheduleDocData;
import cn.sbx.deeper.moblie.domian.SchedulePersonalData;
import cn.sbx.deeper.moblie.domian.SearchCondition.UIDate;
import cn.sbx.deeper.moblie.domian.SearchCondition.UIDropOption;
import cn.sbx.deeper.moblie.domian.SearchCondition.UIDropOrderBy;
import cn.sbx.deeper.moblie.domian.SearchCondition.UIDropSelect;
import cn.sbx.deeper.moblie.domian.SearchCondition.UITextInput;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.Other;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTD;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTR;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.SinopecTable;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UICommonphrase;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UIInput;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UIOpinion;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UIOption;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UISelect;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UISubtitle;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UITreeuser;
import cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UIUser;
import cn.sbx.deeper.moblie.domian.SoftInfo;
import cn.sbx.deeper.moblie.domian.Task;
import cn.sbx.deeper.moblie.domian.TaskKey;
import cn.sbx.deeper.moblie.domian.TaskView;
import cn.sbx.deeper.moblie.domian.TypeItem;
import cn.sbx.deeper.moblie.domian.UnionOffice;
import cn.sbx.deeper.moblie.fargments.MobileIntranetNewsDetailFragment;
import petrochina.ghzy.a10fieldwork.R;

public class DataCollectionUtils {
    private static final String TAG = "DataCollectionUtils";
    private static String tag = "DataCollectionUtils";

    /**
     * @param path
     * @return
     */
    public static List<PageShowTab> receivePageshowTabs(String path) {
        Log.i(TAG, "receivePageshowTabs--path:" + path);
        List<PageShowTab> pageShowTabs = new ArrayList<PageShowTab>();
        PageShowTab pageShowTab = null;
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpGet = new HttpPost(path);
            httpGet.setHeader("EquipType", "Android");
            httpGet.setHeader("EquipSN", WebUtils.deviceId);
            httpGet.setHeader("Soft", WebUtils.packageName);
            httpGet.setHeader("Tel", WebUtils.phoneNumber);
            httpGet.setHeader("Cookie", WebUtils.cookie);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (200 == httpResponse.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                is = httpResponse.getEntity().getContent();
                // String resultData = StreamUtils.retrieveContent(is);
                // Log.i(TAG, "resultData:" + resultData);
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                String eleName = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            eleName = parser.getName();
                            if ("item".equalsIgnoreCase(eleName)) {
                                pageShowTab = new PageShowTab();
                                pageShowTabs.add(pageShowTab);
                                pageShowTab.name = parser.getAttributeValue(null,
                                        "name");
                                pageShowTab.pageId = parser.getAttributeValue(null,
                                        "pageID");
                            }
                            break;
                    }
                    eventType = parser.next();
                }
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
        return pageShowTabs;
    }

    /**
     * 获取视频设备信息
     *
     * @param path
     * @return
     */
    public static List<Object> retrieveMonitorDevicesInfo(String path) {
        List<Object> objects = new ArrayList<Object>();
        MonitorDevice monitorDevice = null;
        MonitorDeviceGroup deviceGroup = null;
        InputStream is = null;
        try {
            if (Constants.DEBUG) {
                Log.i(TAG, "load monitor pathL:" + path);
            }
            is = getConnectByHttpPost(path);
            if (is != null) {
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(is);
                Element element = document.getDocumentElement();
                NodeList rootList = element.getChildNodes();
                for (int i = 0; i < rootList.getLength(); i++) {
                    try {
                        Node ele = rootList.item(i);
                        if (ele.getNodeType() == Node.ELEMENT_NODE) {
                            if ("Group".equalsIgnoreCase(ele.getNodeName())) {
                                Element ele1 = (Element) ele;
                                deviceGroup = new MonitorDeviceGroup();
                                objects.add(deviceGroup);
                                deviceGroup.caption = ele1
                                        .getAttribute("Caption");
                                deviceGroup.id = ele1.getAttribute("ID");
                                readdGroup(ele1, deviceGroup);
                            } else if ("MonitorDevice".equalsIgnoreCase(ele
                                    .getNodeName())) {
                                Element element2 = (Element) ele;
                                monitorDevice = new MonitorDevice();
                                objects.add(monitorDevice);
                                monitorDevice.displayName = element2
                                        .getAttribute("DisplayName");// todo
                                monitorDevice.deviceAddr = element2
                                        .getAttribute("DeviceAddr");
                                monitorDevice.requestProtocol = element2
                                        .getAttribute("RequestProtocol");
                                monitorDevice.videoEncFormat = element2
                                        .getAttribute("VideoEncFormat");
                                monitorDevice.videoWidth = element2
                                        .getAttribute("VideoWidth");
                                monitorDevice.videoHeight = element2
                                        .getAttribute("VideoHeight");
                                monitorDevice.videoRate = element2
                                        .getAttribute("VideoRate");
                                monitorDevice.additionalParams = element2
                                        .getAttribute("AdditionalParams");
                                monitorDevice.paramSequence = element2
                                        .getAttribute("ParamSequence");
                                monitorDevice.dataStorageMode = element2
                                        .getAttribute("DataStorageMode");
                                monitorDevice.responseAssertion = element2
                                        .getAttribute("ResponseAssertion");
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            } else {
                return objects;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return objects;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return objects;
    }

    private static void readdGroup(Element ele2, MonitorDeviceGroup deviceGroup) {
        if (ele2.hasChildNodes()) {
            NodeList nodeList = ele2.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element ele = (Element) node;
                    if ("Group".equalsIgnoreCase(ele.getNodeName())) {
                        MonitorDeviceGroup monitorGroup = new MonitorDeviceGroup();
                        deviceGroup.deviceGroups.add(monitorGroup);
                        monitorGroup.caption = ele.getAttribute("Caption");// todo
                        monitorGroup.id = ele.getAttribute("ID");
                        System.out.println("group:" + deviceGroup.caption
                                + "-----" + deviceGroup.id);
                        readdGroup(ele, monitorGroup);
                    } else if ("MonitorDevice".equalsIgnoreCase(ele
                            .getNodeName())) {
                        MonitorDevice monitorDevice = new MonitorDevice();
                        deviceGroup.monitorDevices.add(monitorDevice);
                        monitorDevice.displayName = ele
                                .getAttribute("DisplayName");// todo
                        System.out.println("Device:"
                                + monitorDevice.displayName);
                        monitorDevice.deviceAddr = ele
                                .getAttribute("DeviceAddr");
                        monitorDevice.requestProtocol = ele
                                .getAttribute("RequestProtocol");
                        monitorDevice.videoEncFormat = ele
                                .getAttribute("VideoEncFormat");
                        monitorDevice.videoWidth = ele
                                .getAttribute("VideoWidth");
                        monitorDevice.videoHeight = ele
                                .getAttribute("VideoHeight");
                        monitorDevice.videoRate = ele.getAttribute("VideoRate");
                        monitorDevice.additionalParams = ele
                                .getAttribute("AdditionalParams");
                        monitorDevice.paramSequence = ele
                                .getAttribute("ParamSequence");
                        monitorDevice.dataStorageMode = ele
                                .getAttribute("DataStorageMode");
                        monitorDevice.responseAssertion = ele
                                .getAttribute("ResponseAssertion");
                    }
                }
            }
        }
    }

    /**
     * 获取每页数量
     *
     * @param url 地址
     * @return
     */
    public static int getPageSize() {
        int pageSize = 10;
        try {
            String url = WebUtils.rootUrl + "/Integrated/GetPageSize.aspx";

            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpGet get = new HttpGet(url);
            HttpResponse pageSizeResponse = httpClient.execute(get);
            HttpEntity pageSizeEntity = pageSizeResponse.getEntity();
            pageSize = Integer.parseInt(EntityUtils.toString(pageSizeEntity,
                    "UTF-8"));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageSize;
    }

    /**
     * 常用语的解析
     *
     * @param path
     * @return
     */
    public static List<String> getLocutionData(String path) {
        List<String> list = new ArrayList<String>();
        LocucationInfo info = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpGet httpGet = new HttpGet(path);
            setHeader(httpGet);
            HttpResponse httpResponse = httpClient.execute(httpGet);

            if (httpResponse != null
                    && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream is = httpResponse.getEntity().getContent();
                // System.out.println("InputStream is:"+StreamUtils.retrieveContent(is));
                XmlPullParserFactory factory = XmlPullParserFactory
                        .newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(is, "utf-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if ("item".equalsIgnoreCase(parser.getName())) {
                                info = new LocucationInfo();
                            } else if ("DictionaryCode".equalsIgnoreCase(parser
                                    .getName())) {
                                info.setDictionaryCode(parser.nextText());
                            } else if ("TypeCode"
                                    .equalsIgnoreCase(parser.getName())) {
                                info.setTypeCode(parser.nextText());
                            } else if ("Name".equalsIgnoreCase(parser.getName())) {
                                String value = parser.nextText();
                                info.setName(value);
                                list.add(value);
                            } else if ("Description".equalsIgnoreCase(parser
                                    .getName())) {
                                info.setDescription(parser.nextText());
                            } else if ("AttachField1".equalsIgnoreCase(parser
                                    .getName())) {
                                info.setAttachField1(parser.nextText());
                            }
                            break;

                        default:
                            break;
                    }
                    eventType = parser.next();
                }
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            return new ArrayList<String>();
        }
    }

    /**
     * 常用语的解析
     *
     * @param path
     * @return
     */
    public static List<String> getLocutionDataNew(String path) {
        List<String> list = new ArrayList<String>();
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpGet httpGet = new HttpGet(path);
            setHeader(httpGet);
            HttpResponse httpResponse = httpClient.execute(httpGet);

            if (httpResponse != null
                    && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream is = httpResponse.getEntity().getContent();
                // System.out.println("InputStream is:"+StreamUtils.retrieveContent(is));
                XmlPullParserFactory factory = XmlPullParserFactory
                        .newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(is, "utf-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if ("option".equalsIgnoreCase(parser.getName())) {
                                String value = parser.nextText();
                                list.add(value);
                            }
                            break;

                        default:
                            break;
                    }
                    eventType = parser.next();
                }
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            return new ArrayList<String>();
        }
    }

    public static SoftInfo receiverUpdateData() {

        InputStream is = null;
        boolean isAndroid = true;
        SoftInfo info = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(WebUtils.rootUrl
                    + URLUtils.updateServer);
            setHeader(httpPost);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("ProgID",
                    Constants.testPackage));
            nameValuePairs.add(new BasicNameValuePair("DeviceType", "3"));
            nameValuePairs.add(new BasicNameValuePair("AppType", "1"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // int len = 0;
            // byte[] arr = new byte[1024];
            // while ((len = is.read(arr)) != 0) {
            // System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+new
            // String(arr, 0, len));
            // }
            // byte[] data1 = LoadUtils.load(is);
            // System.out.println("new String(----update-----): " + new
            // String(data1, "UTF-8"));
            if (200 == response.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        // tempName = parser.getName();
                        case XmlPullParser.START_TAG:

                            if ("Software".equalsIgnoreCase(parser.getName())) {
                                if (Constants.testPackage.equalsIgnoreCase(parser
                                        .getAttributeValue(null, "progid"))) {
                                    info = new SoftInfo();
                                    info.setProgID(parser.getAttributeValue(null,
                                            "progid"));
                                    info.setVersion(parser.getAttributeValue(null,
                                            "version"));
                                    info.setDownloadUrl(parser.getAttributeValue(
                                            null, "downloadurl"));
                                    info.setForced(parser.getAttributeValue(null,
                                            "forced"));
                                    if (info != null && "".equals(info.getForced())) {
                                        info.setForced("0");
                                    }
                                }
                            } else if (info != null
                                    && "updatelog".equalsIgnoreCase(parser
                                    .getName())) {
                                info.setUpdateLog(parser.nextText());
                            }

                            break;
                        case XmlPullParser.END_TAG:

                            break;
                    }
                    eventType = parser.next();
                }
            } else if (500 == response.getStatusLine().getStatusCode()) {
                return info;
            }
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
        return info;

    }

    /**
     * 审批列表动态获取标签和列表
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static List<Map<TaskKey, List<Task>>> receiverApproveTag(String path)
            throws Exception {
        List<Map<TaskKey, List<Task>>> taskMaps = new ArrayList<Map<TaskKey, List<Task>>>();
        Map<TaskKey, List<Task>> itemEntry = null;
        List<Task> tasks = null;
        TaskKey taskKey = null;
        Task task = null;
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            Log.i(TAG, "receiverApproveTag:" + path);
            setHeader(httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // if (200 == response.getStatusLine().getStatusCode()) {
            // String resultData = StreamUtils.retrieveContent(is);
            // Log.i(TAG, "resultData:" + resultData);

            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("approve_list.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            Element element = document.getDocumentElement();
            NodeList rootList = element.getChildNodes();
            for (int i = 0; i < rootList.getLength(); i++) {
                Node ele = rootList.item(i);
                if (ele.getNodeType() == Node.ELEMENT_NODE) {
                    itemEntry = new HashMap<TaskKey, List<Task>>();
                    taskKey = new TaskKey();
                    tasks = new ArrayList<Task>();
                    itemEntry.put(taskKey, tasks);
                    taskMaps.add(itemEntry);
                    Element e1 = (Element) ele;
                    taskKey.typeKey = e1.getNodeName();
                    taskKey.caption = e1.getAttribute("Caption");
                    if (e1.hasChildNodes()) {
                        NodeList e1List = e1.getChildNodes();
                        for (int j = 0; j < e1List.getLength(); j++) {
                            Node e2 = e1List.item(j);
                            if (e2.getNodeType() == Node.ELEMENT_NODE) {
                                if (e2.hasChildNodes()) {
                                    NodeList e2List = e2.getChildNodes();
                                    for (int p = 0; p < e2List.getLength(); p++) {
                                        Node n1 = e2List.item(p);
                                        if (n1.getNodeType() == Node.ELEMENT_NODE) {
                                            task = new Task();
                                            tasks.add(task);
                                            if (n1.hasChildNodes()) {
                                                NodeList itemNodes = n1
                                                        .getChildNodes();
                                                for (int z = 0; z < itemNodes
                                                        .getLength(); z++) {
                                                    Node itemNode = itemNodes
                                                            .item(z);
                                                    if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                                                        Element itemEle = (Element) itemNode;
                                                        if ("ID".equalsIgnoreCase(itemEle
                                                                .getNodeName())) {
                                                            task.setId(itemEle
                                                                    .getTextContent() == null ? ""
                                                                    : itemEle
                                                                    .getTextContent());
                                                        } else if ("Title"
                                                                .equalsIgnoreCase(itemEle
                                                                        .getNodeName())) {
                                                            task.setTitle(itemEle
                                                                    .getTextContent() == null ? ""
                                                                    : itemEle
                                                                    .getTextContent());
                                                        } else if ("Requestor"
                                                                .equalsIgnoreCase(itemEle
                                                                        .getNodeName())) {
                                                            task.setRequestor(itemEle
                                                                    .getTextContent() == null ? ""
                                                                    : itemEle
                                                                    .getTextContent());
                                                        } else if ("Date"
                                                                .equalsIgnoreCase(itemEle
                                                                        .getNodeName())) {
                                                            task.setDate(itemEle
                                                                    .getTextContent() == null ? ""
                                                                    : itemEle
                                                                    .getTextContent());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Log.i(TAG, "taskMaps.size():" + taskMaps.size());
            return taskMaps;
            // }
        } catch (Exception e) {
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
        return taskMaps;
    }

    @SuppressWarnings("finally")
    public static List<AppAccount> receiverAppAccountData() {
        List<AppAccount> appAccounts = new ArrayList<AppAccount>();
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(WebUtils.rootUrl
                    + URLUtils.loginAccountActivityURL + "?group=oa");
            if (Constants.DEBUG)
                Log.i(TAG, "receiver app count list:" + WebUtils.rootUrl
                        + URLUtils.loginAccountActivityURL + "?group=oa");
            setHeader(httpPost);
            // HttpResponse response = httpClient.execute(httpPost);
            // HttpEntity entity = response.getEntity();
            // is = entity.getContent();

            // local data
            is = DataCollectionUtils.class.getClassLoader()
                    .getResourceAsStream("app_account.xml");

            // Log.i("logi", new String (LoadUtils.load(is)));

            // if (200 == response.getStatusLine().getStatusCode()) {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("detail".equalsIgnoreCase(parser.getName())) {
                            AppAccount appAccount = new AppAccount();
                            appAccounts.add(appAccount);
                            appAccount.setTypeNo_key(parser.getAttributeValue(null,
                                    "typeNo"));
                            appAccount.setTypeNo_value(parser.getAttributeValue(
                                    null, "typeNo"));
                            appAccount.setTypeName_key(parser.getAttributeValue(
                                    null, "typeName"));
                            appAccount.setTypeName_value(parser.getAttributeValue(
                                    null, "typeName"));
                            appAccount.setName_name(parser.getAttributeValue(null,
                                    "name"));
                            appAccount.setName_value(parser.getAttributeValue(null,
                                    "name"));
                            appAccount.setPassword_name(parser.getAttributeValue(
                                    null, "passWord"));
                            appAccount.setPassword_value(parser.getAttributeValue(
                                    null, "passWord"));
                            appAccount.setUseMain_name(Integer.parseInt(parser
                                    .getAttributeValue(null, "useMain")));
                            appAccount.setUseMain_value(Integer.parseInt(parser
                                    .getAttributeValue(null, "useMain")));
                        }
                        break;
                }
                eventType = parser.next();
            }
            // }

        } catch (Exception e) {
            if (Constants.DEBUG)
                e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    return appAccounts;
                } catch (IOException e) {
                    if (Constants.DEBUG)
                        e.printStackTrace();
                }
            }
            return appAccounts;
        }

    }

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
        // Log.i("logi", new String(LoadUtils.load(in)));
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
                        // row = new Row();
                        rows.add(row);
                    } else if ("Label".equalsIgnoreCase(parser.getName())) {
                        // row.setLable(parser.nextText());
                    } else if ("Text".equalsIgnoreCase(parser.getName())) {
                        if (parser.getAttributeCount() > 0) {
                            // row.setType(parser.getAttributeValue(0));
                        }
                        // row.setText(parser.nextText());
                        // row.setType(parser.getAttributeValue(0));

                        // if(parser.getAttributeName(0) != null) {
                        // row.setType(parser.getAttributeValue(0));
                        // }
                    } else if ("Submit".equalsIgnoreCase(parser.getName())) {
                        routers = new ArrayList<Router>();
                        taskView.setRouters(routers);
                    } else if ("Item".equalsIgnoreCase(parser.getName())) {
                        router = new Router();

                        String receiverRequired = parser.getAttributeValue(null,
                                "receiverRequired");
                        if (receiverRequired != null
                                && !receiverRequired.equals("")) {
                            router.setReceiverRequired(receiverRequired); // 接收人
                        } else {
                            router.setReceiverRequired("1"); // 接收人
                        }

                        String commentRequired = parser.getAttributeValue(null,
                                "commentRequired");
                        if (commentRequired != null && !commentRequired.equals("")) {
                            router.setCommentRequired(commentRequired); // 意见填写
                        } else {
                            router.setCommentRequired("1"); // 意见填写
                        }

                        String commentVisible = parser.getAttributeValue(null,
                                "commentVisible");
                        if (commentVisible != null && !commentVisible.equals("")) {
                            router.setCommentVisible(commentVisible); // 意见框是否可见
                        } else {
                            router.setCommentVisible("1"); // 意见框是否可见
                        }
                        String initOuid = parser
                                .getAttributeValue(null, "initOUID");
                        router.setInitOuid(null == initOuid ? "" : initOuid);

                        if (parser.getAttributeCount() > 0) {
                            router.setAction(parser.getAttributeValue(0));
                        }
                        routers.add(router);
                        inReceiver = false;
                    } else if ("ID".equalsIgnoreCase(parser.getName())
                            && !inReceiver) {
                        router.setId(parser.nextText());
                    } else if ("CaptionText".equalsIgnoreCase(parser.getName())) {
                        router.setCaptionText(parser.nextText());
                    } else if ("PeoplePicker".equalsIgnoreCase(parser.getName())) {
                        router.setPeoplePicker(parser.nextText());
                    } else if ("Receivers".equalsIgnoreCase(parser.getName())) {
                        receivers = new ArrayList<Receiver>();
                        router.setMultiple(Integer.parseInt(parser
                                .getAttributeValue(0) == null ? "0" : parser
                                .getAttributeValue(0)));
                        router.setReceivers(receivers);
                    } else if ("Receiver".equalsIgnoreCase(parser.getName())) {
                        receiver = new Receiver();
                        receivers.add(receiver);
                        inReceiver = true;
                    } else if ("ID".equalsIgnoreCase(parser.getName())
                            && inReceiver) {
                        receiver.setId(parser.nextText());
                    } else if ("Name".equalsIgnoreCase(parser.getName())
                            && inReceiver) {
                        receiver.setName(parser.nextText());
                    } else if ("JobTitle".equalsIgnoreCase(parser.getName())
                            && inReceiver) {
                        receiver.setJobTitle(parser.nextText());
                    } else if ("Department".equalsIgnoreCase(parser.getName())
                            && inReceiver) {
                        receiver.setDepartment(parser.nextText());
                    }
                    break;
            }
            event = parser.next();
        }
        return taskView;
    }

    /**
     * 获取门户信息标题列表
     */
    public static List<TypeItem> receiverTypeItems(String path) {
        InputStream is = null;
        List<TypeItem> items = new ArrayList<TypeItem>();
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            TypeItem item = null;
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("item".equalsIgnoreCase(parser.getName())) {
                            item = new TypeItem();
                            items.add(item);
                            item.name = parser.getAttributeValue(null, "name");
                            item.listid = parser.getAttributeValue(null, "listid");
                            item.detailid = parser.getAttributeValue(null,
                                    "detailid");
                        }
                        break;
                }
                event = parser.next();
            }
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
        return items;
    }

    /**
     * 获取门户信息标题列表 test
     */
    public static List<NewsPaper> receiverDemoItems(String path) {
        InputStream is = null;
        List<NewsPaper> elements = new ArrayList<NewsPaper>();
        NewsPaper element = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            Log.i(TAG, "load intranet url:" + path);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("element".equalsIgnoreCase(parser.getName())) {
                            element = new NewsPaper();
                            elements.add(element);
                        } else if ("id".equalsIgnoreCase(parser.getName())) {
                            element.id = parser.nextText();

                        } else if ("nextUrl".equalsIgnoreCase(parser.getName())) {
                            element.detailUrl = parser.nextText();

                        } else if ("Title".equalsIgnoreCase(parser.getName())) {
                            element.title = parser.nextText();

                        } else if ("date".equalsIgnoreCase(parser.getName())) {
                            element.date = parser.nextText();

                        }
                        break;
                }
                event = parser.next();
            }
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
        return elements;
    }

    /**
     * 获取门户信息内容列表
     */
    public static List<NewsPaper> receiverIntranetsList(String path) {
        InputStream is = null;
        NewsPaper intranet = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            List<NewsPaper> intranets = null;
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("ArticleList".equalsIgnoreCase(parser.getName())) {
                            intranets = new ArrayList<NewsPaper>();
                        } else if ("ListItem".equalsIgnoreCase(parser.getName())) {
                            intranet = new NewsPaper();
                            intranets.add(intranet);
                        } else if ("ItemID".equalsIgnoreCase(parser.getName())) {
                            intranet.id = parser.nextText();
                            // Log.v("Id",intranet.id);
                        } else if ("Title".equalsIgnoreCase(parser.getName())) {
                            intranet.title = parser.nextText();
                        } else if ("Date".equalsIgnoreCase(parser.getName())) {
                            intranet.date = parser.nextText();
                        } else if ("IMAGEADDRESS"
                                .equalsIgnoreCase(parser.getName())) {
                            intranet.imageAddress = parser.nextText();
                            Log.v("image address:", intranet.imageAddress);
                        } else if ("Summary".equalsIgnoreCase(parser.getName())) {
                            intranet.summary = parser.nextText();
                        }
                        break;
                }
                event = parser.next();
            }
            return intranets;
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
        return null;
    }

    /**
     * 获取门户信息详细列表
     *
     * @param path
     */
    public static NoticeDetail getNoticeDetail(String path) {
        InputStream is = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("pageid",
                    MobileIntranetNewsDetailFragment.listPathpage));
            nameValuePairs.add(new BasicNameValuePair("key1",
                    MobileIntranetNewsDetailFragment.id));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            setHeader(httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            // int leng = 0;
            // byte[] arr = new byte[1024];
            // while ((leng = is.read(arr)) > 0){
            // System.out.println(new String(arr, 0, leng) + "数据的大小");
            // }

            NoticeDetail noticeDetail = new NoticeDetail();
            List<Attachment> attachments = null;
            List<MeetingPdf> imgUrls = null;
            MeetingPdf pdf = null;
            Attachment attachment = null;
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("view".equalsIgnoreCase(parser.getName())) {
                            noticeDetail = new NoticeDetail();
                        } else if ("title".equalsIgnoreCase(parser.getName())) {
                            noticeDetail.setTitle(parser.nextText());
                        } else if ("date".equalsIgnoreCase(parser.getName())) {
                            noticeDetail.setPublishDate(parser.nextText());
                        } else if ("author".equalsIgnoreCase(parser.getName())) {
                            noticeDetail.setAuthor(parser.nextText());
                        } else if ("content".equalsIgnoreCase(parser.getName())) {
                            noticeDetail.setContent(parser.getAttributeValue(0));
                            String newContent = parser.nextText();
                            noticeDetail.setNewContent(newContent);
                        } else if ("Files".equalsIgnoreCase(parser.getName())) {// 附件列表
                            attachments = new ArrayList<Attachment>();
                        } else if ("File".equalsIgnoreCase(parser.getName())) {// 附件
                            attachment = new Attachment();
                            attachment.url = parser.getAttributeValue(null, "url");
                            attachment.title = parser.nextText();
                            attachments.add(attachment);
                        }
                        break;
                }
                event = parser.next();
            }
            return noticeDetail;

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
        return null;
    }

    /**
     * 获取门户信息详细列表
     *
     * @param path
     */
    public static NoticeDetail getNoticeDetail_trans2p0(String path) {
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load menhu data:" + path);
            HttpPost httpPost = new HttpPost(path);
            httpPost.setHeader("EquipType", "Android");
            httpPost.setHeader("EquipSN", WebUtils.deviceId);
            httpPost.setHeader("Soft", WebUtils.packageName);
            httpPost.setHeader("Tel", WebUtils.phoneNumber);
            httpPost.setHeader("Cookie", WebUtils.cookie);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            NoticeDetail noticeDetail = new NoticeDetail();
            List<Attachment> attachments = null;
            List<MeetingPdf> imgUrls = null;
            MeetingPdf pdf = null;
            Attachment attachment = null;
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("ContentView".equalsIgnoreCase(parser.getName())) {
                            noticeDetail = new NoticeDetail();
                        } else if ("Title".equalsIgnoreCase(parser.getName())) {
                            noticeDetail.title = parser.nextText();
                        } else if ("PublishDate".equalsIgnoreCase(parser.getName())) {
                            noticeDetail.publishDate = parser.nextText();
                        } else if ("Publisher".equalsIgnoreCase(parser.getName())) {
                            noticeDetail.publisher = parser.nextText();
                        } else if ("Content".equalsIgnoreCase(parser.getName())) {
                            noticeDetail.content = parser.nextText();
                        } else if ("Attachments".equalsIgnoreCase(parser.getName())) {
                            attachments = new ArrayList<Attachment>();
                            noticeDetail.attachment_list = attachments;
                        } else if ("Attachment".equalsIgnoreCase(parser.getName())) {
                            attachment = new Attachment();
                            if (attachments != null) {
                                attachments.add(attachment);
                                attachment.pdf = parser.getAttributeValue(0);
                                attachment.key2 = parser.nextText();
                            }
                        } else if ("imgs".equalsIgnoreCase(parser.getName())) {
                            imgUrls = new ArrayList<MeetingPdf>();
                            noticeDetail.imgUrls = imgUrls;
                        } else if ("property".equalsIgnoreCase(parser.getName())) {
                            pdf = new MeetingPdf();
                            pdf.setPdfKey(parser.getAttributeValue(2));// 图片id
                            pdf.setValue(parser.getAttributeValue(1));// 图片URL
                            imgUrls.add(pdf);
                        }
                        break;
                }
                event = parser.next();
            }
            return noticeDetail;

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
        return null;
    }

    /**
     * 获取审批标签和搜索条件
     *
     * @param path
     */
    public static List<ApproveTab> receiveApproveTabCondition1(
            InputStream inputStream, String path, Context mContext) {
        InputStream is = null;
        List<ApproveTab> approveTabs = new ArrayList<ApproveTab>();
        ApproveTab approveTab = null;
        UIDropOption option = null;
        UITextInput input = null;
        UIDropSelect select = null;
        UIDropOrderBy orderBy = null;
        UIDate uiDate = null;
        boolean isBatch = false;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve condition data:" + path);
            boolean inSelect = false;
            boolean inOrderBy = false;
            boolean issearch = false;
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("ProgID",
                    WebUtils.packageName));
            nameValuePairs.add(new BasicNameValuePair("DeviceType", "3"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            // HttpResponse response = httpClient.execute(httpPost);
            // HttpEntity entity = response.getEntity();
            // is = entity.getContent();
            // System.out.println("is:"+StreamUtils.retrieveContent(is));

            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_tabs.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("batchtabs".equalsIgnoreCase(parser.getName())) {
                            isBatch = true;
                        } else if ("Tab".equalsIgnoreCase(parser.getName())) {
                            approveTab = new ApproveTab();
                            approveTabs.add(approveTab);
                            approveTab.title = parser.getAttributeValue(null,
                                    "title");
                            approveTab.id = parser.getAttributeValue(null, "id");
                            approveTab.isBatch = isBatch;
                        } else if ("select".equalsIgnoreCase(parser.getName())) {
                            inSelect = true;
                            select = approveTab.searchCondition.new UIDropSelect();
                            approveTab.searchCondition.selects.add(select);
                            select.name = parser.getAttributeValue(null, "name");
                            select.required = parser.getAttributeValue(null,
                                    "required");
                            select.title = parser.getAttributeValue(null, "title");
                        } else if ("OrderBy".equalsIgnoreCase(parser.getName())) {
                            inOrderBy = true;
                            orderBy = approveTab.searchCondition.new UIDropOrderBy();
                            approveTab.searchCondition.orderBys.add(orderBy);
                            orderBy.name = parser.getAttributeValue(null, "name");
                            orderBy.required = parser.getAttributeValue(null,
                                    "required");
                            orderBy.title = parser.getAttributeValue(null, "title");
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inSelect) {
                            option = approveTab.searchCondition.new UIDropOption();
                            select.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.checked = parser.getAttributeValue(null,
                                    "checked");
                            option.name = parser.nextText();
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inOrderBy) {
                            option = approveTab.searchCondition.new UIDropOption();
                            orderBy.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.checked = parser.getAttributeValue(null,
                                    "checked");
                            option.name = parser.nextText();
                        } else if ("input".equalsIgnoreCase(parser.getName())) {
                            input = approveTab.searchCondition.new UITextInput();
                            approveTab.searchCondition.inputs.add(input);
                            input.type = parser.getAttributeValue(null, "type");
                            input.name = parser.getAttributeValue(null, "name");
                            input.required = parser.getAttributeValue(null,
                                    "required");
                        } else if ("date".equalsIgnoreCase(parser.getName())) {
                            uiDate = approveTab.searchCondition.new UIDate();
                            uiDate.dateName = parser
                                    .getAttributeValue(null, "name");
                            uiDate.dateTitle = parser.getAttributeValue(null,
                                    "title");
                            uiDate.dateType = parser
                                    .getAttributeValue(null, "type");
                            uiDate.dateValue = parser.getAttributeValue(null,
                                    "value");
                            System.out.println("http response data name :"
                                    + uiDate.dateName + " datevalue:"
                                    + uiDate.dateValue);
                            approveTab.searchCondition.dates.add(uiDate);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("batchtabs".equalsIgnoreCase(parser.getName())) {
                            isBatch = false;
                        } else if ("select".equalsIgnoreCase(parser.getName())) {
                            inSelect = false;
                        } else if ("OrderBy".equalsIgnoreCase(parser.getName())) {
                            inOrderBy = false;
                        }
                        if ("search".equalsIgnoreCase(parser.getName())) {
                            issearch = false;
                        }
                        break;
                }
                event = parser.next();
            }
            return approveTabs;

        } catch (ConnectTimeoutException e) {
            Toast.makeText(mContext, R.string.no_available_net, 0).show();
            e.printStackTrace();
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
        return approveTabs;
    }

    /**
     * 获取审批标签和搜索条件
     *
     * @param path
     */
    public static List<ApproveTab> receiveApproveTabCondition(String path,
                                                              Context mContext) {
        InputStream is = null;
        List<ApproveTab> approveTabs = new ArrayList<ApproveTab>();
        ApproveTab approveTab = null;
        UIDropOption option = null;
        UITextInput input = null;
        UIDropSelect select = null;
        UIDropOrderBy orderBy = null;
        UIDate uiDate = null;
        boolean isBatch = false;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve condition data:" + path);
            boolean inSelect = false;
            boolean inOrderBy = false;
            boolean issearch = false;
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("ProgID",
                    WebUtils.packageName));
            nameValuePairs.add(new BasicNameValuePair("DeviceType", "3"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println("is:"+StreamUtils.retrieveContent(is));

            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_tabs.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("batchtabs".equalsIgnoreCase(parser.getName())) {
                            isBatch = true;
                        } else if ("Tab".equalsIgnoreCase(parser.getName())) {
                            approveTab = new ApproveTab();
                            approveTabs.add(approveTab);
                            approveTab.title = parser.getAttributeValue(null,
                                    "title");
                            approveTab.id = parser.getAttributeValue(null, "id");
                            approveTab.isBatch = isBatch;
                        } else if ("select".equalsIgnoreCase(parser.getName())) {
                            inSelect = true;
                            select = approveTab.searchCondition.new UIDropSelect();
                            approveTab.searchCondition.selects.add(select);
                            select.name = parser.getAttributeValue(null, "name");
                            select.required = parser.getAttributeValue(null,
                                    "required");
                            select.title = parser.getAttributeValue(null, "title");
                        } else if ("OrderBy".equalsIgnoreCase(parser.getName())) {
                            inOrderBy = true;
                            orderBy = approveTab.searchCondition.new UIDropOrderBy();
                            approveTab.searchCondition.orderBys.add(orderBy);
                            orderBy.name = parser.getAttributeValue(null, "name");
                            orderBy.required = parser.getAttributeValue(null,
                                    "required");
                            orderBy.title = parser.getAttributeValue(null, "title");
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inSelect) {
                            option = approveTab.searchCondition.new UIDropOption();
                            select.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.checked = parser.getAttributeValue(null,
                                    "checked");
                            option.name = parser.nextText();
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inOrderBy) {
                            option = approveTab.searchCondition.new UIDropOption();
                            orderBy.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.checked = parser.getAttributeValue(null,
                                    "checked");
                            option.name = parser.nextText();
                        } else if ("input".equalsIgnoreCase(parser.getName())) {
                            input = approveTab.searchCondition.new UITextInput();
                            approveTab.searchCondition.inputs.add(input);
                            input.type = parser.getAttributeValue(null, "type");
                            input.name = parser.getAttributeValue(null, "name");
                            input.required = parser.getAttributeValue(null,
                                    "required");
                        } else if ("date".equalsIgnoreCase(parser.getName())) {
                            uiDate = approveTab.searchCondition.new UIDate();
                            uiDate.dateName = parser
                                    .getAttributeValue(null, "name");
                            uiDate.dateTitle = parser.getAttributeValue(null,
                                    "title");
                            uiDate.dateType = parser
                                    .getAttributeValue(null, "type");
                            uiDate.dateValue = parser.getAttributeValue(null,
                                    "value");
                            System.out.println("http response data name :"
                                    + uiDate.dateName + " datevalue:"
                                    + uiDate.dateValue);
                            approveTab.searchCondition.dates.add(uiDate);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("batchtabs".equalsIgnoreCase(parser.getName())) {
                            isBatch = false;
                        } else if ("select".equalsIgnoreCase(parser.getName())) {
                            inSelect = false;
                        } else if ("OrderBy".equalsIgnoreCase(parser.getName())) {
                            inOrderBy = false;
                        }
                        if ("search".equalsIgnoreCase(parser.getName())) {
                            issearch = false;
                        }
                        break;
                }
                event = parser.next();
            }
            return approveTabs;

        } catch (ConnectTimeoutException e) {
            Toast.makeText(mContext, R.string.no_available_net, 0).show();
            e.printStackTrace();
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
        return approveTabs;
    }

    /**
     * @param path
     */
    public static List<Task> receiveApproveTabList(String path, String type,
                                                   String searchXML, String rownumber, String pagesize, String itemId,
                                                   Context mContext) {
        InputStream is = null;
        List<Task> tasks = new ArrayList<Task>();
        Task task = null;
        boolean b = true;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve tab list data:" + path);
            Log.i(TAG, "key1:" + type + "...key2:" + searchXML + "...key3:"
                    + rownumber + "...key4:" + pagesize + "...key5:" + itemId);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", type));
            nameValuePairs.add(new BasicNameValuePair("key2", searchXML));
            nameValuePairs.add(new BasicNameValuePair("key3", rownumber));
            nameValuePairs.add(new BasicNameValuePair("key4", pagesize));
            nameValuePairs.add(new BasicNameValuePair("key5", itemId));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println("is:"+StreamUtils.retrieveContent(is));
            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("approve.xml");

            // int leng = 0;
            // byte[] arr = new byte[1024];
            // while ((leng = is.read(arr)) != 0) {
            // System.out.println(new String(arr, 0, leng));
            // }

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("listitem".equalsIgnoreCase(parser.getName())) {
                            task = new Task();
                            task.setFlag(b);
                            tasks.add(task);
                        } else if ("itemid".equalsIgnoreCase(parser.getName())) {
                            task.setId(parser.nextText());
                        } else if ("title".equalsIgnoreCase(parser.getName())) {
                            task.setTitle(parser.nextText());
                        } else if ("description".equalsIgnoreCase(parser.getName())) {
                            task.setDescription(parser.nextText());
                        } else if ("datetime".equalsIgnoreCase(parser.getName())) {
                            task.setDate(parser.nextText());
                        } else if ("field1".equalsIgnoreCase(parser.getName())) {
                            task.setField1(parser.nextText());
                        } else if ("field2".equalsIgnoreCase(parser.getName())) {
                            task.setField2(parser.nextText());
                        } else if ("field3".equalsIgnoreCase(parser.getName())) {
                            task.setField3(parser.nextText());
                        } else if ("currNode".equalsIgnoreCase(parser.getName())) {
                            task.setCurrNode(parser.nextText());
                        } else if ("displayDevice".equalsIgnoreCase(parser
                                .getName())) {
                            task.setDisplayDevice(parser.nextText());
                        } else if ("deviceResult"
                                .equalsIgnoreCase(parser.getName())) {
                            task.setDeviceResult(parser.nextText());
                        }

                        break;
                }
                b = !b;
                event = parser.next();
            }
            return tasks;

        } catch (ConnectTimeoutException e) {
            Toast.makeText(mContext, R.string.no_available_net, 0).show();
            e.printStackTrace();
        } catch (Exception e) {
            if (Constants.DEBUG)
                e.printStackTrace();
        }
        // finally {
        // if (is != null) {
        // try {
        // is.close();
        // } catch (IOException e) {
        // if (Constants.DEBUG)
        // e.printStackTrace();
        // }
        // }
        // }
        return tasks;
    }

    /**
     * @param path
     */
    public static List<Task> receiveApproveTabList1(InputStream inputStream,
                                                    String path, String type, String searchXML, String rownumber,
                                                    String pagesize, String itemId, Context mContext) {
        InputStream is = null;
        List<Task> tasks = new ArrayList<Task>();
        Task task = null;
        boolean b = true;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve tab list data:" + path);
            Log.i(TAG, "key1:" + type + "...key2:" + searchXML + "...key3:"
                    + rownumber + "...key4:" + pagesize + "...key5:" + itemId);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", type));
            nameValuePairs.add(new BasicNameValuePair("key2", searchXML));
            nameValuePairs.add(new BasicNameValuePair("key3", rownumber));
            nameValuePairs.add(new BasicNameValuePair("key4", pagesize));
            nameValuePairs.add(new BasicNameValuePair("key5", itemId));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            // HttpResponse response = httpClient.execute(httpPost);
            // HttpEntity entity = response.getEntity();
            // is = entity.getContent();
            // System.out.println("is:"+StreamUtils.retrieveContent(is));
            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("approve.xml");

            // int leng = 0;
            // byte[] arr = new byte[1024];
            // while ((leng = is.read(arr)) != 0) {
            // System.out.println(new String(arr, 0, leng));
            // }

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("listitem".equalsIgnoreCase(parser.getName())) {
                            task = new Task();
                            task.setFlag(b);
                            tasks.add(task);
                        } else if ("itemid".equalsIgnoreCase(parser.getName())) {
                            task.setId(parser.nextText());
                        } else if ("title".equalsIgnoreCase(parser.getName())) {
                            task.setTitle(parser.nextText());
                        } else if ("description".equalsIgnoreCase(parser.getName())) {
                            task.setDescription(parser.nextText());
                        } else if ("datetime".equalsIgnoreCase(parser.getName())) {
                            task.setDate(parser.nextText());
                        } else if ("field1".equalsIgnoreCase(parser.getName())) {
                            task.setField1(parser.nextText());
                        } else if ("field2".equalsIgnoreCase(parser.getName())) {
                            task.setField2(parser.nextText());
                        } else if ("field3".equalsIgnoreCase(parser.getName())) {
                            task.setField3(parser.nextText());
                        } else if ("currNode".equalsIgnoreCase(parser.getName())) {
                            task.setCurrNode(parser.nextText());
                        } else if ("displayDevice".equalsIgnoreCase(parser
                                .getName())) {
                            task.setDisplayDevice(parser.nextText());
                        } else if ("deviceResult"
                                .equalsIgnoreCase(parser.getName())) {
                            task.setDeviceResult(parser.nextText());
                        }

                        break;
                }
                b = !b;
                event = parser.next();
            }
            return tasks;

        } catch (ConnectTimeoutException e) {
            Toast.makeText(mContext, R.string.no_available_net, 0).show();
            e.printStackTrace();
        } catch (Exception e) {
            if (Constants.DEBUG)
                e.printStackTrace();
        }
        // finally {
        // if (is != null) {
        // try {
        // is.close();
        // } catch (IOException e) {
        // if (Constants.DEBUG)
        // e.printStackTrace();
        // }
        // }
        // }
        return tasks;
    }

    /**
     * @param path
     */
    public static List<Task> receiveSearchApproveTabList(String path,
                                                         String type, String xml, String rowNumber, String pageSize,
                                                         String itemid) {
        InputStream is = null;
        List<Task> tasks = new ArrayList<Task>();
        Task task = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve tab list data:" + path);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", type));
            nameValuePairs.add(new BasicNameValuePair("key2", xml));
            nameValuePairs.add(new BasicNameValuePair("key3", rowNumber));
            nameValuePairs.add(new BasicNameValuePair("key4", pageSize));
            nameValuePairs.add(new BasicNameValuePair("key5", itemid));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_list.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("listitem".equalsIgnoreCase(parser.getName())) {
                            task = new Task();
                            tasks.add(task);
                        } else if ("itemid".equalsIgnoreCase(parser.getName())) {
                            task.setId(parser.nextText());
                        } else if ("title".equalsIgnoreCase(parser.getName())) {
                            task.setTitle(parser.nextText());
                        } else if ("description".equalsIgnoreCase(parser.getName())) {
                            task.setDescription(parser.nextText());
                        } else if ("datetime".equalsIgnoreCase(parser.getName())) {
                            task.setDate(parser.nextText());
                        } else if ("field1".equalsIgnoreCase(parser.getName())) {
                            task.setField1(parser.nextText());
                        } else if ("field2".equalsIgnoreCase(parser.getName())) {
                            task.setField2(parser.nextText());
                        } else if ("field3".equalsIgnoreCase(parser.getName())) {
                            task.setField3(parser.nextText());
                        }

                        break;
                }
                event = parser.next();
            }
            return tasks;

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
        return tasks;
    }

    /**
     * @param path
     */
    public static List<TypeItem> receiveApproveNumberTotal(String path) {
        List<TypeItem> numbers = new ArrayList<TypeItem>();
        InputStream is = null;
        TypeItem item = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpClient httpClient = ProxyCheck.myHttpClient();
            Log.i(TAG, "begin load approve tab number data1 :" + path);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            // params -----IsDev CVersion SCode
            // nameValuePairs.add(new BasicNameValuePair("IsDev",
            // Constants.developerMode));
            // nameValuePairs.add(new BasicNameValuePair("CVersion",
            // VersionUtils.getVersionName()));
            // nameValuePairs.add(new BasicNameValuePair("SCode",
            // WebUtils.packageName));
            // nameValuePairs.add(new BasicNameValuePair("CVersion", "0.0"));
            nameValuePairs.add(new BasicNameValuePair("SCode",
                    Constants.testPackage));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println("tab Num:"+StreamUtils.retrieveContent(is));
            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_list.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("module".equalsIgnoreCase(parser.getName())) {
                            item = new TypeItem();
                            item.id = parser.getAttributeValue(null, "id");
                            item.count = parser.getAttributeValue(null, "count");
                            // System.out.println("id:"+number[0]+"  count:"+number[1]);
                            numbers.add(item);
                        }
                        break;
                }
                event = parser.next();
            }
            return numbers;

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
        return numbers;
    }

    /**
     * @param path
     */
    public static List<String[]> receiveApproveNumberNew(String path) {
        List<String[]> numbers = new ArrayList<String[]>();
        String[] number = null;
        InputStream is = null;
        try {
            //
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve tab number data1 :" + path);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println("tab Num:"+StreamUtils.retrieveContent(is));
            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_list.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            boolean bool = false;
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("tab".equalsIgnoreCase(parser.getName()) && bool) {
                            number = new String[2];
                            numbers.add(number);
                            number[0] = parser.getAttributeValue(null, "id");
                            number[1] = parser.getAttributeValue(null, "count");
                            // System.out.println("id:"+number[0]+"  count:"+number[1]);
                        } else if ("tabs".equalsIgnoreCase(parser.getName())) {
                            bool = true;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("tabs".equalsIgnoreCase(parser.getName())) {
                            bool = false;
                        }
                        break;
                }
                event = parser.next();
            }
            return numbers;

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
        return numbers;
    }

    /**
     * @param path
     */
    public static List<String[]> receiveApproveNumberNew1(
            InputStream inputStream, String path) {
        List<String[]> numbers = new ArrayList<String[]>();
        String[] number = null;
        InputStream is = null;
        try {
            //
            // HttpParams httpParameters = new BasicHttpParams();
            // DefaultHttpClient httpClient = HttpUtils
            // .initHttpClient(httpParameters);
            // Log.i(TAG, "begin load approve tab number data1 :" + path);
            // HttpPost httpPost = new HttpPost(path);
            // setHeader(httpPost);
            // // params
            // List<NameValuePair> nameValuePairs = new
            // ArrayList<NameValuePair>();
            // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
            // "UTF-8"));

            // HttpResponse response = httpClient.execute(httpPost);
            // HttpEntity entity = response.getEntity();
            // is = entity.getContent();
            // System.out.println("tab Num:"+StreamUtils.retrieveContent(is));
            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_list.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            int event = parser.getEventType();
            boolean bool = false;
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("tab".equalsIgnoreCase(parser.getName()) && bool) {
                            number = new String[2];
                            numbers.add(number);
                            number[0] = parser.getAttributeValue(null, "id");
                            number[1] = parser.getAttributeValue(null, "count");
                            // System.out.println("id:"+number[0]+"  count:"+number[1]);
                        } else if ("tabs".equalsIgnoreCase(parser.getName())) {
                            bool = true;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("tabs".equalsIgnoreCase(parser.getName())) {
                            bool = false;
                        }
                        break;
                }
                event = parser.next();
            }
            return numbers;

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
        return numbers;
    }

    /**
     * @param path
     */
    public static List<String[]> receiveApproveNumber(String path) {
        List<String[]> numbers = new ArrayList<String[]>();
        String[] number = null;
        InputStream is = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve tab number data:" + path);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println("tab Num:"+StreamUtils.retrieveContent(is));
            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_list.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("tab".equalsIgnoreCase(parser.getName())) {
                            number = new String[2];
                            numbers.add(number);
                            number[0] = parser.getAttributeValue(null, "id");
                            number[1] = parser.getAttributeValue(null, "count");
                            // System.out.println("id:"+number[0]+"  count:"+number[1]);
                        }
                        break;
                }
                event = parser.next();
            }
            return numbers;

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
        return numbers;
    }

    /**
     * @param path
     */
    public static String receiveApproveHtml(String path, String key1,
                                            String key2) {
        InputStream is = null;
        String html = "";
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve detail html data:" + path
                    + "...key1:" + key1 + "...key2:" + key2);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", key1));
            nameValuePairs.add(new BasicNameValuePair("key2", key2));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            html = new String(LoadUtils.load(is));
            Log.i(TAG, "new String(LoadUtils.load(is)+" + html);
            return html;
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
        return null;
    }

    /**
     * @param path
     */
    public static SinopecApproveDetailEntry receiveApproveDetail(String path,
                                                                 String tabType, String taskId) {
        InputStream is = null;
        SinopecApproveDetailEntry approveDetailEntry = new SinopecApproveDetailEntry();
        SinopecTable table = null;
        SinopecTR tr = null;
        SinopecTD td = null;
        UIInput input = null;
        UIUser user = null;
        UITreeuser treeuser = null;
        // UIOptions optionsList = null;
        UIOption option = null;
        UIOption subOption = null;
        UIOpinion opinion = null;
        UICommonphrase commonphrase = null;
        UICommonphrase phrase = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve detail data:" + path);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", tabType));
            nameValuePairs.add(new BasicNameValuePair("key2", taskId));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // is =
            // DataCollectionUtils.class.getClassLoader().getResourceAsStream("审批详细test.xml");
            // System.out.println("approve detail:"+StreamUtils.retrieveContent(is));
            XmlPullParser parser = Xml.newPullParser();
            boolean inForm = false;
            boolean inOpera = false;
            boolean inUser = false;
            boolean inTreeuser = false;
            String trParent = "";
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("form".equalsIgnoreCase(parser.getName())) {
                            approveDetailEntry.form = approveDetailEntry.new SinopecForm();
                            inForm = true;
                        } else if ("table".equalsIgnoreCase(parser.getName())
                                && inForm) {
                            table = approveDetailEntry.new SinopecTable();
                            approveDetailEntry.form.sinopecTables.add(table);
                            table.id = parser.getAttributeValue(null, "id");
                            table.title = parser.getAttributeValue(null, "title");
                            table.name = parser.getAttributeValue(null, "name");
                            table.expand = parser.getAttributeValue(null, "expend");
                            table.EditField = parser.getAttributeValue(null,
                                    "editfield");
                            table.columns = parser.getAttributeValue(null,
                                    "columns");
                            table.Type = parser.getAttributeValue(null, "Type");
                        } else if ("Operation".equalsIgnoreCase(parser.getName())) {
                            approveDetailEntry.operater = approveDetailEntry.new SinopecOperater();
                            inOpera = true;
                        } else if ("table".equalsIgnoreCase(parser.getName())
                                && inOpera) {
                            table = approveDetailEntry.new SinopecTable();
                            approveDetailEntry.operater.sinopecTables.add(table);
                            table.id = parser.getAttributeValue(null, "id");
                            table.title = parser.getAttributeValue(null, "title");
                            table.name = parser.getAttributeValue(null, "name");
                            table.expand = parser.getAttributeValue(null, "expand");
                            table.EditField = parser.getAttributeValue(null,
                                    "editfield");
                            table.Type = parser.getAttributeValue(null, "Type");
                        } else if ("tr".equalsIgnoreCase(parser.getName())) {
                            tr = approveDetailEntry.new SinopecTR();
                            trParent = parser.getAttributeValue(null, "parent");
                            tr.parent = trParent;
                            table.trs.add(tr);
                        } else if ("td".equalsIgnoreCase(parser.getName())) {
                            td = approveDetailEntry.new SinopecTD();
                            tr.tds.add(td);
                            int ev = parser.next();
                            if (ev == XmlPullParser.TEXT) {
                                td.content = parser.getText();
                            } else if (ev == XmlPullParser.START_TAG) {
                                td.content = "";
                                if ("input".equalsIgnoreCase(parser.getName())) {
                                    input = approveDetailEntry.new UIInput();
                                    td.sinopecViews.add(input);
                                    input.id = parser.getAttributeValue(null, "id");
                                    input.type = parser.getAttributeValue(null,
                                            "type");
                                    input.name = parser.getAttributeValue(null,
                                            "name");
                                    input.value = parser.getAttributeValue(null,
                                            "value");
                                    input.title = parser.getAttributeValue(null,
                                            "title");
                                    input.required = parser.getAttributeValue(null,
                                            "required");
                                    input.checked = parser.getAttributeValue(null,
                                            "checked");
                                    input.maxlength = parser.getAttributeValue(
                                            null, "maxlength");
                                    input.minlength = parser.getAttributeValue(
                                            null, "minlength");
                                    input.regex = parser.getAttributeValue(null,
                                            "regex");
                                    input.message = parser.getAttributeValue(null,
                                            "message");
                                    input.alt = parser.getAttributeValue(null,
                                            "alt");
                                    input.closevalue = parser.getAttributeValue(
                                            null, "closevalue");
                                    String value = parser.getAttributeValue(null,
                                            "submitflag");
                                    input.submitflag = TextUtils.isEmpty(value) ? ""
                                            : value;
                                    td.submitflag = input.submitflag;
                                    input.parent = trParent;
                                } else if ("user"
                                        .equalsIgnoreCase(parser.getName())) {
                                    inUser = true;
                                    user = approveDetailEntry.new UIUser();
                                    td.sinopecViews.add(user);
                                    user.id = parser.getAttributeValue(null, "id");
                                    user.name = parser.getAttributeValue(null,
                                            "name");
                                    user.value = parser.getAttributeValue(null,
                                            "value");
                                    user.required = parser.getAttributeValue(null,
                                            "required");
                                    user.alt = parser
                                            .getAttributeValue(null, "alt");
                                    user.closevalue = parser.getAttributeValue(
                                            null, "closevalue");
                                    user.type = parser.getAttributeValue(null,
                                            "type");
                                    user.mDefault = parser.getAttributeValue(null,
                                            "default");
                                    user.filter = parser.getAttributeValue(null,
                                            "filter");
                                    user.multi = parser.getAttributeValue(null,
                                            "multi");
                                    user.title = parser.getAttributeValue(null,
                                            "title");
                                    user.message = parser.getAttributeValue(null,
                                            "message");
                                    user.userchoice = parser.getAttributeValue(
                                            null, "userchoice");
                                    user.isreplace = parser.getAttributeValue(null,
                                            "isreplace");
                                    String value = parser.getAttributeValue(null,
                                            "submitflag");
                                    user.submitflag = TextUtils.isEmpty(value) ? ""
                                            : value;
                                    td.submitflag = user.submitflag;
                                    user.parent = trParent;
                                } else if ("commonphrase".equalsIgnoreCase(parser
                                        .getName())) {
                                    phrase = approveDetailEntry.new UICommonphrase();
                                    td.sinopecViews.add(phrase);
                                    phrase.title = parser.getAttributeValue(null,
                                            "title");
                                    phrase.name = parser.getAttributeValue(null,
                                            "name");
                                    phrase.value = parser.getAttributeValue(null,
                                            "value");
                                    phrase.required = parser.getAttributeValue(
                                            null, "required");
                                    phrase.alt = parser.getAttributeValue(null,
                                            "alt");
                                    phrase.closevalue = parser.getAttributeValue(
                                            null, "closevalue");
                                    phrase.type = parser.getAttributeValue(null,
                                            "type");
                                    phrase.maxlength = parser.getAttributeValue(
                                            null, "maxlength");
                                    phrase.minlength = parser.getAttributeValue(
                                            null, "minlength");
                                    phrase.regex = parser.getAttributeValue(null,
                                            "regex");
                                    phrase.message = parser.getAttributeValue(null,
                                            "message");
                                    phrase.id = parser
                                            .getAttributeValue(null, "id");
                                    String value = parser.getAttributeValue(null,
                                            "submitflag");
                                    phrase.submitflag = TextUtils.isEmpty(value) ? ""
                                            : value;
                                    td.submitflag = phrase.submitflag;
                                    phrase.parent = trParent;
                                    phrase.mDefault = parser.getAttributeValue(
                                            null, "default");
                                    phrase.custom = parser.getAttributeValue(null,
                                            "custom");
                                }
                            }
                        } else if ("input".equalsIgnoreCase(parser.getName())) {
                            input = approveDetailEntry.new UIInput();
                            td.sinopecViews.add(input);
                            input.id = parser.getAttributeValue(null, "id");
                            input.type = parser.getAttributeValue(null, "type");
                            input.name = parser.getAttributeValue(null, "name");
                            input.value = parser.getAttributeValue(null, "value");
                            input.title = parser.getAttributeValue(null, "title");
                            input.required = parser.getAttributeValue(null,
                                    "required");
                            input.checked = parser.getAttributeValue(null,
                                    "checked");
                            input.maxlength = parser.getAttributeValue(null,
                                    "maxlength");
                            input.minlength = parser.getAttributeValue(null,
                                    "minlength");
                            input.regex = parser.getAttributeValue(null, "regex");
                            input.message = parser.getAttributeValue(null,
                                    "message");
                            input.alt = parser.getAttributeValue(null, "alt");
                            input.closevalue = parser.getAttributeValue(null,
                                    "closevalue");
                            String value = parser.getAttributeValue(null,
                                    "submitflag");
                            input.submitflag = TextUtils.isEmpty(value) ? ""
                                    : value;
                            td.submitflag = input.submitflag;
                            input.parent = trParent;
                        } else if ("commonphrase"
                                .equalsIgnoreCase(parser.getName())) {
                            phrase = approveDetailEntry.new UICommonphrase();
                            td.sinopecViews.add(phrase);
                            phrase.title = parser.getAttributeValue(null, "title");
                            phrase.name = parser.getAttributeValue(null, "name");
                            phrase.id = parser.getAttributeValue(null, "id");
                            phrase.value = parser.getAttributeValue(null, "value");
                            phrase.required = parser.getAttributeValue(null,
                                    "required");
                            phrase.alt = parser.getAttributeValue(null, "alt");
                            phrase.closevalue = parser.getAttributeValue(null,
                                    "closevalue");
                            phrase.type = parser.getAttributeValue(null, "type");
                            phrase.maxlength = parser.getAttributeValue(null,
                                    "maxlength");
                            phrase.minlength = parser.getAttributeValue(null,
                                    "minlength");
                            phrase.regex = parser.getAttributeValue(null, "regex");
                            phrase.message = parser.getAttributeValue(null,
                                    "message");
                            String value = parser.getAttributeValue(null,
                                    "submitflag");
                            phrase.submitflag = TextUtils.isEmpty(value) ? ""
                                    : value;
                            td.submitflag = phrase.submitflag;
                            phrase.parent = trParent;
                            phrase.mDefault = parser.getAttributeValue(null,
                                    "default");
                            phrase.custom = parser
                                    .getAttributeValue(null, "custom");
                        } else if ("user".equalsIgnoreCase(parser.getName())) {
                            inUser = true;
                            user = approveDetailEntry.new UIUser();
                            td.sinopecViews.add(user);
                            user.id = parser.getAttributeValue(null, "id");
                            user.name = parser.getAttributeValue(null, "name");
                            user.value = parser.getAttributeValue(null, "value");
                            user.required = parser.getAttributeValue(null,
                                    "required");
                            user.alt = parser.getAttributeValue(null, "alt");
                            user.closevalue = parser.getAttributeValue(null,
                                    "closevalue");
                            user.type = parser.getAttributeValue(null, "type");
                            user.mDefault = parser.getAttributeValue(null,
                                    "default");
                            user.filter = parser.getAttributeValue(null, "filter");
                            user.multi = parser.getAttributeValue(null, "multi");
                            user.title = parser.getAttributeValue(null, "title");
                            user.message = parser
                                    .getAttributeValue(null, "message");
                            user.userchoice = parser.getAttributeValue(null,
                                    "userchoice");
                            String value = parser.getAttributeValue(null,
                                    "submitflag");
                            user.submitflag = TextUtils.isEmpty(value) ? "" : value;
                            td.submitflag = user.submitflag;
                            user.parent = trParent;
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inUser) {
                            option = approveDetailEntry.new UIOption();
                            user.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.checked = parser.getAttributeValue(null,
                                    "checked");
                            option.readonly = parser.getAttributeValue(null,
                                    "readonly");
                            option.name = parser.nextText();
                        } else if ("treeuser".equalsIgnoreCase(parser.getName())) {// 树形选人结构
                            inTreeuser = true;
                            treeuser = approveDetailEntry.new UITreeuser();
                            td.sinopecViews.add(treeuser);
                            treeuser.id = parser.getAttributeValue(null, "id");
                            treeuser.name = parser.getAttributeValue(null, "name");
                            treeuser.value = parser
                                    .getAttributeValue(null, "value");
                            treeuser.required = parser.getAttributeValue(null,
                                    "required");
                            treeuser.alt = parser.getAttributeValue(null, "alt");
                            // treeuser.closevalue = parser.getAttributeValue(null,
                            // "closevalue");
                            treeuser.type = parser.getAttributeValue(null, "type");
                            treeuser.mDefault = parser.getAttributeValue(null,
                                    "default");
                            treeuser.filter = parser.getAttributeValue(null,
                                    "filter");
                            treeuser.multi = parser
                                    .getAttributeValue(null, "multi");
                            treeuser.title = parser
                                    .getAttributeValue(null, "title");
                            treeuser.message = parser.getAttributeValue(null,
                                    "message");
                            // treeuser.userchoice = parser.getAttributeValue(null,
                            // "userchoice");
                            treeuser.link = parser.getAttributeValue(null, "link");
                            String value = parser.getAttributeValue(null,
                                    "submitflag");
                            treeuser.submitflag = TextUtils.isEmpty(value) ? ""
                                    : value;
                            td.submitflag = treeuser.submitflag;
                            treeuser.parent = trParent;
                        } else if ("options".equalsIgnoreCase(parser.getName())
                                && inTreeuser) {
                            // optionsList = approveDetailEntry.new UIOptions();
                            // optionsList.value = parser.getAttributeValue(null,
                            // "value");
                            // optionsList.checked = parser.getAttributeValue(null,
                            // "checked");
                            // optionsList.readonly = parser.getAttributeValue(null,
                            // "readonly");
                            // optionsList.batchselect =
                            // parser.getAttributeValue(null, "batchselect");
                            // // optionsList.name = parser.nextText();
                            // optionsList.name = optionsList.value;

                            option = approveDetailEntry.new UIOption();
                            option.value = parser.getAttributeValue(null, "value");
                            option.checked = parser.getAttributeValue(null,
                                    "checked");
                            option.readonly = parser.getAttributeValue(null,
                                    "readonly");
                            // option.name = parser.nextText();
                            option.name = option.value;

                            treeuser.optionList.add(option);
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inTreeuser) {
                            subOption = approveDetailEntry.new UIOption();
                            subOption.value = parser.getAttributeValue(null,
                                    "value");
                            subOption.checked = parser.getAttributeValue(null,
                                    "checked");
                            subOption.readonly = parser.getAttributeValue(null,
                                    "readonly");
                            // option.name = parser.nextText();
                            subOption.name = subOption.value;
                            option.options.add(subOption);
                        } else if ("showopinion".equalsIgnoreCase(parser.getName())) {
                            opinion = approveDetailEntry.new UIOpinion();
                            td.sinopecViews.add(opinion);
                            opinion.title = parser.getAttributeValue(null, "title");
                            opinion.name = parser.getAttributeValue(null, "name");
                            String value = parser.getAttributeValue(null,
                                    "submitflag");
                            opinion.alt = parser.getAttributeValue(null, "alt");
                            opinion.required = parser.getAttributeValue(null,
                                    "required");
                            opinion.value = parser.getAttributeValue(null, "value");
                            opinion.mDefault = parser.getAttributeValue(null,
                                    "default");
                            opinion.rules = parser.getAttributeValue(null, "rules");
                            opinion.submitflag = TextUtils.isEmpty(value) ? ""
                                    : value;
                            td.submitflag = opinion.submitflag;
                            opinion.parent = trParent;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("form".equalsIgnoreCase(parser.getName())) {
                            inForm = false;
                        } else if ("Operation".equalsIgnoreCase(parser.getName())) {
                            inOpera = false;
                        } else if ("user".equalsIgnoreCase(parser.getName())) {
                            inUser = false;
                        } else if ("treeuser".equalsIgnoreCase(parser.getName())) {
                            inTreeuser = false;
                        }
                        break;
                }
                event = parser.next();
            }
            return approveDetailEntry;
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
        return approveDetailEntry;
    }

    /**
     * Dom 解析多级
     *
     * @param path
     */
    public static SinopecApproveDetailEntry receiveApproveDetailDom(
            InputStream inputStream, String path, String tabType, String taskId) {
        InputStream is = null;
        SinopecApproveDetailEntry approveDetailEntry = new SinopecApproveDetailEntry();
        SinopecTable table = null;
        SinopecTR tr = null;
        SinopecTD td = null;
        cn.sbx.deeper.moblie.domian.SinopecApproveDetailEntry.UIDate date = null;
        UIInput input = null;
        UIUser user = null;
        UISelect select = null;
        UITreeuser treeuser = null;
        // UIOptions optionsList = null;
        UIOption option = null;
        UIOption subOption = null;
        UIOpinion opinion = null;
        UICommonphrase commonphrase = null;
        UICommonphrase phrase = null;
        UISubtitle subtitle = null;

        Other other = null;

        //
        String trParent = "";
        Document document;
        try {
            // HttpParams httpParameters = new BasicHttpParams();
            // DefaultHttpClient httpClient = HttpUtils
            // .initHttpClient(httpParameters);
            // Log.i(TAG, "begin load approve detail data:" + path);
            // HttpPost httpPost = new HttpPost(path);
            // setHeader(httpPost);
            //
            // // params
            // List<NameValuePair> nameValuePairs = new
            // ArrayList<NameValuePair>();
            // nameValuePairs.add(new BasicNameValuePair("key1", tabType));
            // nameValuePairs.add(new BasicNameValuePair("key2", taskId));
            // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
            // "UTF-8"));
            // HttpResponse response = httpClient.execute(httpPost);
            // HttpEntity entity = response.getEntity();
            // is = entity.getContent();
            // is
            // =DataCollectionUtils.class.getClassLoader().getResourceAsStream("new6.xml");
            // System.out.println("approve detail:"+StreamUtils.retrieveContent(is));
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();// 创建xml工厂
            DocumentBuilder builder = factory.newDocumentBuilder();// 拿到工厂的实例
            document = builder.parse(inputStream);// 生成document对象
            Element element = document.getDocumentElement();
            NodeList formNodes = element.getElementsByTagName("form");
            for (int i = 0; i < formNodes.getLength(); i++) {
                Element pNode = (Element) formNodes.item(i);
                approveDetailEntry.form = approveDetailEntry.new SinopecForm();
                if (pNode.hasChildNodes()) {
                    NodeList tables = pNode.getChildNodes();
                    for (int j = 0; j < tables.getLength(); j++) {
                        Node tableN = tables.item(j);
                        if ("table".equalsIgnoreCase(tableN.getNodeName())) {
                            Element ele1 = (Element) tableN;
                            table = approveDetailEntry.new SinopecTable();
                            approveDetailEntry.form.sinopecTables.add(table);
                            table.id = ele1.getAttribute("id");
                            table.title = ele1.getAttribute("title");
                            table.name = ele1.getAttribute("name");
                            table.expand = ele1.getAttribute("expand");
                            table.EditField = ele1.getAttribute("editfield");
                            table.columns = ele1.getAttribute("columns");
                            table.Type = ele1.getAttribute("Type");
                            if (tableN.hasChildNodes()) {
                                NodeList trs = ele1.getChildNodes();
                                for (int k = 0; k < trs.getLength(); k++) {// tr
                                    Node trN = trs.item(k);
                                    if ("tr".equalsIgnoreCase(trN.getNodeName())) {
                                        Element eleTr = (Element) trN;
                                        tr = approveDetailEntry.new SinopecTR();
                                        trParent = eleTr.getAttribute("parent");
                                        tr.parent = trParent;
                                        table.trs.add(tr);
                                        if (trN.hasChildNodes()) {
                                            NodeList tdN = trN.getChildNodes();
                                            for (int g = 0; g < tdN.getLength(); g++) {
                                                Node nodeTd = tdN.item(g);
                                                // 判断是否为元素类型
                                                if (nodeTd.getNodeType() == Node.ELEMENT_NODE) {
                                                    if ("td".equalsIgnoreCase(nodeTd
                                                            .getNodeName())) {
                                                        Element childNode = (Element) nodeTd;
                                                        td = approveDetailEntry.new SinopecTD();
                                                        tr.tds.add(td);
                                                        td.content = childNode
                                                                .getFirstChild() != null ? childNode
                                                                .getFirstChild()
                                                                .getNodeValue()
                                                                : "";
                                                        // System.out
                                                        // .println("td.content:"+td.content);
                                                        if (nodeTd
                                                                .hasChildNodes()) {
                                                            NodeList tdF = nodeTd
                                                                    .getChildNodes();
                                                            for (int q = 0; q < tdF
                                                                    .getLength(); q++) {
                                                                Node nodeForm = tdF
                                                                        .item(q);
                                                                if (nodeForm
                                                                        .getNodeType() == Node.ELEMENT_NODE) {
                                                                    Element childNodeF = (Element) nodeForm;
                                                                    td.content = "";
                                                                    if ("input"
                                                                            .equalsIgnoreCase(childNodeF
                                                                                    .getNodeName())) {
                                                                        input = approveDetailEntry.new UIInput();
                                                                        td.sinopecViews
                                                                                .add(input);
                                                                        input.id = childNodeF
                                                                                .getAttribute("id");
                                                                        input.type = childNodeF
                                                                                .getAttribute("type");
                                                                        input.name = childNodeF
                                                                                .getAttribute("name");
                                                                        input.value = childNodeF
                                                                                .getAttribute("value");
                                                                        input.title = childNodeF
                                                                                .getAttribute("title");
                                                                        input.required = childNodeF
                                                                                .getAttribute("required");
                                                                        input.checked = childNodeF
                                                                                .getAttribute("checked");
                                                                        input.maxlength = childNodeF
                                                                                .getAttribute("maxlength");
                                                                        input.minlength = childNodeF
                                                                                .getAttribute("minlength");
                                                                        input.regex = childNodeF
                                                                                .getAttribute("regex");
                                                                        input.message = childNodeF
                                                                                .getAttribute("message");
                                                                        input.regexmessage = childNodeF
                                                                                .getAttribute("regexmessage");
                                                                        input.alt = childNodeF
                                                                                .getAttribute("alt");
                                                                        input.closevalue = childNodeF
                                                                                .getAttribute("closevalue");
                                                                        String value = childNodeF
                                                                                .getAttribute("submitflag");
                                                                        input.submitflag = TextUtils
                                                                                .isEmpty(value) ? ""
                                                                                : value;
                                                                        td.submitflag = input.submitflag;
                                                                        input.parent = trParent;
                                                                    } else if ("user"
                                                                            .equalsIgnoreCase(childNodeF
                                                                                    .getNodeName())) {
                                                                        user = approveDetailEntry.new UIUser();
                                                                        td.sinopecViews
                                                                                .add(user);
                                                                        user.id = childNodeF
                                                                                .getAttribute("id");
                                                                        user.name = childNodeF
                                                                                .getAttribute("name");
                                                                        user.value = childNodeF
                                                                                .getAttribute("value");
                                                                        user.required = childNodeF
                                                                                .getAttribute("required");
                                                                        user.alt = childNodeF
                                                                                .getAttribute("alt");
                                                                        user.closevalue = childNodeF
                                                                                .getAttribute("closevalue");
                                                                        user.type = childNodeF
                                                                                .getAttribute("type");
                                                                        user.mDefault = childNodeF
                                                                                .getAttribute("default");
                                                                        user.filter = childNodeF
                                                                                .getAttribute("filter");
                                                                        user.multi = childNodeF
                                                                                .getAttribute("multi");
                                                                        user.title = childNodeF
                                                                                .getAttribute("title");
                                                                        user.message = childNodeF
                                                                                .getAttribute("message");
                                                                        user.userchoice = childNodeF
                                                                                .getAttribute("userchoice");
                                                                        user.isreplace = childNodeF
                                                                                .getAttribute("isreplace");
                                                                        String value = childNodeF
                                                                                .getAttribute("submitflag");
                                                                        user.submitflag = TextUtils
                                                                                .isEmpty(value) ? ""
                                                                                : value;
                                                                        td.submitflag = user.submitflag;
                                                                        user.parent = trParent;
                                                                        if (childNodeF
                                                                                .hasChildNodes()) {
                                                                            NodeList opNodes = childNodeF
                                                                                    .getChildNodes();
                                                                            for (int p = 0; p < opNodes
                                                                                    .getLength(); p++) {
                                                                                Node nodeOp = opNodes
                                                                                        .item(p);
                                                                                if ("option"
                                                                                        .equalsIgnoreCase(nodeOp
                                                                                                .getNodeName())) {
                                                                                    Element eleOp = (Element) nodeOp;
                                                                                    option = approveDetailEntry.new UIOption();
                                                                                    user.options
                                                                                            .add(option);
                                                                                    option.value = eleOp
                                                                                            .getAttribute("value");
                                                                                    option.checked = eleOp
                                                                                            .getAttribute("checked");
                                                                                    option.readonly = eleOp
                                                                                            .getAttribute("readonly");
                                                                                    option.name = eleOp
                                                                                            .getFirstChild() != null ? eleOp
                                                                                            .getFirstChild()
                                                                                            .getNodeValue()
                                                                                            : "";
                                                                                }
                                                                            }
                                                                        }
                                                                    } else if ("commonphrase"
                                                                            .equalsIgnoreCase(childNodeF
                                                                                    .getNodeName())) {
                                                                        phrase = approveDetailEntry.new UICommonphrase();
                                                                        td.sinopecViews
                                                                                .add(phrase);
                                                                        phrase.title = childNodeF
                                                                                .getAttribute("title");
                                                                        phrase.name = childNodeF
                                                                                .getAttribute("name");
                                                                        phrase.value = childNodeF
                                                                                .getAttribute("value");
                                                                        phrase.required = childNodeF
                                                                                .getAttribute("required");
                                                                        phrase.alt = childNodeF
                                                                                .getAttribute("alt");
                                                                        phrase.closevalue = childNodeF
                                                                                .getAttribute("closevalue");
                                                                        phrase.type = childNodeF
                                                                                .getAttribute("type");
                                                                        phrase.maxlength = childNodeF
                                                                                .getAttribute("maxlength");
                                                                        phrase.minlength = childNodeF
                                                                                .getAttribute("minlength");
                                                                        phrase.regex = childNodeF
                                                                                .getAttribute("regex");
                                                                        phrase.message = childNodeF
                                                                                .getAttribute("message");
                                                                        phrase.id = childNodeF
                                                                                .getAttribute("id");
                                                                        String value = childNodeF
                                                                                .getAttribute("submitflag");
                                                                        phrase.submitflag = TextUtils
                                                                                .isEmpty(value) ? ""
                                                                                : value;
                                                                        td.submitflag = phrase.submitflag;
                                                                        phrase.parent = trParent;
                                                                        phrase.mDefault = childNodeF
                                                                                .getAttribute("default");
                                                                        phrase.custom = childNodeF
                                                                                .getAttribute("custom");

                                                                    }// 增加subtitle节点
                                                                    else if ("subtitle"
                                                                            .equalsIgnoreCase(childNodeF
                                                                                    .getNodeName())) {
                                                                        subtitle = approveDetailEntry.new UISubtitle();
                                                                        td.sinopecViews
                                                                                .add(subtitle);
                                                                        subtitle.title = childNodeF
                                                                                .getAttribute("title");
                                                                        subtitle.titlecolor = childNodeF
                                                                                .getAttribute("titlecolor");
                                                                        subtitle.backgroudcolor = childNodeF
                                                                                .getAttribute("backgroudcolor");
                                                                        subtitle.fontsize = childNodeF
                                                                                .getAttribute("fontsize");
                                                                        subtitle.parent = trParent;
                                                                    } else if ("date"
                                                                            .equalsIgnoreCase(childNodeF
                                                                                    .getNodeName())) {
                                                                        date = approveDetailEntry.new UIDate();
                                                                        td.sinopecViews
                                                                                .add(date);
                                                                        date.name = childNodeF
                                                                                .getAttribute("title");
                                                                        date.type = childNodeF
                                                                                .getAttribute("type");
                                                                        date.value = childNodeF
                                                                                .getAttribute("value");
                                                                        date.Message = childNodeF
                                                                                .getAttribute("message");
                                                                        date.regex = childNodeF
                                                                                .getAttribute("timespan");
                                                                        date.timespan = childNodeF
                                                                                .getAttribute("timespan");
                                                                        date.required = childNodeF
                                                                                .getAttribute("required");
                                                                        date.submitflag = childNodeF
                                                                                .getAttribute("submitflag");
                                                                        date.current = childNodeF
                                                                                .getAttribute("current");
                                                                    } else if ("select"
                                                                            .equalsIgnoreCase(childNodeF
                                                                                    .getNodeName())) {
                                                                        select = approveDetailEntry.new UISelect();
                                                                        td.sinopecViews
                                                                                .add(select);
                                                                        select.name = childNodeF
                                                                                .getAttribute("name");
                                                                        select.value = childNodeF
                                                                                .getAttribute("value");
                                                                        select.required = childNodeF
                                                                                .getAttribute("required");
                                                                        select.alt = childNodeF
                                                                                .getAttribute("alt");
                                                                        select.title = childNodeF
                                                                                .getAttribute("title");
                                                                        if (childNodeF
                                                                                .hasChildNodes()) {
                                                                            NodeList opNodes = childNodeF
                                                                                    .getChildNodes();
                                                                            for (int p = 0; p < opNodes
                                                                                    .getLength(); p++) {
                                                                                Node nodeOp = opNodes
                                                                                        .item(p);
                                                                                if ("option"
                                                                                        .equalsIgnoreCase(nodeOp
                                                                                                .getNodeName())) {
                                                                                    Element eleOp = (Element) nodeOp;
                                                                                    option = approveDetailEntry.new UIOption();
                                                                                    select.options
                                                                                            .add(option);
                                                                                    option.value = eleOp
                                                                                            .getAttribute("value");
                                                                                    option.checked = eleOp
                                                                                            .getAttribute("checked");
                                                                                    option.name = eleOp
                                                                                            .getFirstChild() != null ? eleOp
                                                                                            .getFirstChild()
                                                                                            .getNodeValue()
                                                                                            : "";
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            NodeList operationNodes = element.getElementsByTagName("operation");// operation
            if (operationNodes != null && operationNodes.getLength() > 0) {
                for (int i = 0; i < operationNodes.getLength(); i++) {
                    Element operNode = (Element) operationNodes.item(i);
                    approveDetailEntry.operater = approveDetailEntry.new SinopecOperater();
                    if (operNode.hasChildNodes()) {
                        NodeList tables = operNode.getChildNodes();
                        for (int j = 0; j < tables.getLength(); j++) {
                            Node tableN = tables.item(j);
                            if ("table".equalsIgnoreCase(tableN.getNodeName())) {
                                Element ele1 = (Element) tableN;
                                table = approveDetailEntry.new SinopecTable();
                                approveDetailEntry.operater.sinopecTables
                                        .add(table);
                                table.id = ele1.getAttribute("id");
                                table.title = ele1.getAttribute("title");
                                table.name = ele1.getAttribute("name");
                                table.expand = ele1.getAttribute("expand");
                                table.EditField = ele1
                                        .getAttribute("editfield");
                                table.Type = ele1.getAttribute("Type");
                                if (tableN.hasChildNodes()) {
                                    NodeList trs = ele1.getChildNodes();
                                    for (int k = 0; k < trs.getLength(); k++) {// tr
                                        Node trN = trs.item(k);
                                        if ("tr".equalsIgnoreCase(trN
                                                .getNodeName())) {
                                            Element eleTr = (Element) trN;
                                            tr = approveDetailEntry.new SinopecTR();
                                            trParent = eleTr
                                                    .getAttribute("parent");
                                            tr.parent = trParent;
                                            table.trs.add(tr);
                                            if (trN.hasChildNodes()) {
                                                NodeList tdN = trN
                                                        .getChildNodes();
                                                for (int g = 0; g < tdN
                                                        .getLength(); g++) {
                                                    Node nodeTd = tdN.item(g);
                                                    // 判断是否为元素类型
                                                    if (nodeTd.getNodeType() == Node.ELEMENT_NODE) {
                                                        if ("td".equalsIgnoreCase(nodeTd
                                                                .getNodeName())) {
                                                            Element childNode = (Element) nodeTd;
                                                            td = approveDetailEntry.new SinopecTD();
                                                            tr.tds.add(td);
                                                            td.content = "";
                                                            // System.out
                                                            // .println("td.content:"+td.content);
                                                            if (nodeTd
                                                                    .hasChildNodes()) {
                                                                NodeList subN = nodeTd
                                                                        .getChildNodes();
                                                                for (int h = 0; h < subN
                                                                        .getLength(); h++) {
                                                                    Node nodeSub = subN
                                                                            .item(h);
                                                                    if (nodeSub
                                                                            .getNodeType() == Node.ELEMENT_NODE) {
                                                                        Element eleSub = (Element) nodeSub;
                                                                        if ("input"
                                                                                .equals(eleSub
                                                                                        .getNodeName())) {
                                                                            input = approveDetailEntry.new UIInput();
                                                                            td.sinopecViews
                                                                                    .add(input);
                                                                            input.id = eleSub
                                                                                    .getAttribute("id");
                                                                            input.type = eleSub
                                                                                    .getAttribute("type");
                                                                            input.name = eleSub
                                                                                    .getAttribute("name");
                                                                            input.value = eleSub
                                                                                    .getAttribute("value");
                                                                            input.title = eleSub
                                                                                    .getAttribute("title");
                                                                            input.required = eleSub
                                                                                    .getAttribute("required");
                                                                            input.checked = eleSub
                                                                                    .getAttribute("checked");
                                                                            input.maxlength = eleSub
                                                                                    .getAttribute("maxlength");
                                                                            input.minlength = eleSub
                                                                                    .getAttribute("minlength");
                                                                            input.regex = eleSub
                                                                                    .getAttribute("regex");
                                                                            input.message = eleSub
                                                                                    .getAttribute("message");
                                                                            input.alt = eleSub
                                                                                    .getAttribute("alt");
                                                                            input.closevalue = eleSub
                                                                                    .getAttribute("closevalue");
                                                                            String value = eleSub
                                                                                    .getAttribute("submitflag");
                                                                            input.submitflag = TextUtils
                                                                                    .isEmpty(value) ? ""
                                                                                    : value;
                                                                            td.submitflag = input.submitflag;
                                                                            input.parent = trParent;
                                                                        } else if ("user"
                                                                                .equalsIgnoreCase(eleSub
                                                                                        .getNodeName())) {
                                                                            user = approveDetailEntry.new UIUser();
                                                                            td.sinopecViews
                                                                                    .add(user);
                                                                            user.id = eleSub
                                                                                    .getAttribute("id");
                                                                            user.name = eleSub
                                                                                    .getAttribute("name");
                                                                            user.value = eleSub
                                                                                    .getAttribute("value");
                                                                            user.required = eleSub
                                                                                    .getAttribute("required");
                                                                            user.alt = eleSub
                                                                                    .getAttribute("alt");
                                                                            user.closevalue = eleSub
                                                                                    .getAttribute("closevalue");
                                                                            user.type = eleSub
                                                                                    .getAttribute("type");
                                                                            user.mDefault = eleSub
                                                                                    .getAttribute("default");
                                                                            user.filter = eleSub
                                                                                    .getAttribute("filter");
                                                                            user.multi = eleSub
                                                                                    .getAttribute("multi");
                                                                            user.title = eleSub
                                                                                    .getAttribute("title");
                                                                            user.message = eleSub
                                                                                    .getAttribute("message");
                                                                            user.userchoice = eleSub
                                                                                    .getAttribute("userchoice");
                                                                            user.isreplace = eleSub
                                                                                    .getAttribute("isreplace");
                                                                            String value = eleSub
                                                                                    .getAttribute("submitflag");
                                                                            user.submitflag = TextUtils
                                                                                    .isEmpty(value) ? ""
                                                                                    : value;
                                                                            td.submitflag = user.submitflag;
                                                                            user.parent = trParent;
                                                                            if (nodeSub
                                                                                    .hasChildNodes()) {
                                                                                NodeList opNodes = nodeSub
                                                                                        .getChildNodes();
                                                                                for (int p = 0; p < opNodes
                                                                                        .getLength(); p++) {
                                                                                    Node nodeOp = opNodes
                                                                                            .item(p);
                                                                                    if ("option"
                                                                                            .equalsIgnoreCase(nodeOp
                                                                                                    .getNodeName())) {
                                                                                        Element eleOp = (Element) nodeOp;
                                                                                        option = approveDetailEntry.new UIOption();
                                                                                        user.options
                                                                                                .add(option);
                                                                                        option.value = eleOp
                                                                                                .getAttribute("value");
                                                                                        option.checked = eleOp
                                                                                                .getAttribute("checked");
                                                                                        option.readonly = eleOp
                                                                                                .getAttribute("readonly");
                                                                                        option.name = eleOp
                                                                                                .getFirstChild() != null ? eleOp
                                                                                                .getFirstChild()
                                                                                                .getNodeValue()
                                                                                                : "";
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else if ("commonphrase"
                                                                                .equalsIgnoreCase(eleSub
                                                                                        .getNodeName())) {
                                                                            phrase = approveDetailEntry.new UICommonphrase();
                                                                            td.sinopecViews
                                                                                    .add(phrase);
                                                                            phrase.title = eleSub
                                                                                    .getAttribute("title");
                                                                            phrase.name = eleSub
                                                                                    .getAttribute("name");
                                                                            phrase.value = eleSub
                                                                                    .getAttribute("value");
                                                                            phrase.required = eleSub
                                                                                    .getAttribute("required");
                                                                            phrase.alt = eleSub
                                                                                    .getAttribute("alt");
                                                                            phrase.closevalue = eleSub
                                                                                    .getAttribute("closevalue");
                                                                            phrase.type = eleSub
                                                                                    .getAttribute("type");
                                                                            phrase.maxlength = eleSub
                                                                                    .getAttribute("maxlength");
                                                                            phrase.minlength = eleSub
                                                                                    .getAttribute("minlength");
                                                                            phrase.regex = eleSub
                                                                                    .getAttribute("regex");
                                                                            phrase.message = eleSub
                                                                                    .getAttribute("message");
                                                                            phrase.id = eleSub
                                                                                    .getAttribute("id");
                                                                            String value = eleSub
                                                                                    .getAttribute("submitflag");
                                                                            phrase.submitflag = TextUtils
                                                                                    .isEmpty(value) ? ""
                                                                                    : value;
                                                                            td.submitflag = phrase.submitflag;
                                                                            phrase.parent = trParent;
                                                                            phrase.mDefault = eleSub
                                                                                    .getAttribute("default");
                                                                            phrase.custom = eleSub
                                                                                    .getAttribute("custom");
                                                                        } else if ("treeuser"
                                                                                .equalsIgnoreCase(eleSub
                                                                                        .getNodeName())) {
                                                                            treeuser = approveDetailEntry.new UITreeuser();
                                                                            td.sinopecViews
                                                                                    .add(treeuser);
                                                                            treeuser.id = eleSub
                                                                                    .getAttribute("id");
                                                                            treeuser.name = eleSub
                                                                                    .getAttribute("name");
                                                                            treeuser.value = eleSub
                                                                                    .getAttribute("value");
                                                                            treeuser.required = eleSub
                                                                                    .getAttribute("required");
                                                                            treeuser.alt = eleSub
                                                                                    .getAttribute("alt");
                                                                            // treeuser.closevalue
                                                                            // =
                                                                            // eleSub.getAttribute("closevalue");
                                                                            treeuser.type = eleSub
                                                                                    .getAttribute("type");
                                                                            treeuser.mDefault = eleSub
                                                                                    .getAttribute("default");
                                                                            treeuser.filter = eleSub
                                                                                    .getAttribute("filter");
                                                                            treeuser.multi = eleSub
                                                                                    .getAttribute("multi");
                                                                            treeuser.title = eleSub
                                                                                    .getAttribute("title");
                                                                            treeuser.message = eleSub
                                                                                    .getAttribute("message");
                                                                            // treeuser.userchoice
                                                                            // =
                                                                            // eleSub.getAttribute("userchoice");
                                                                            treeuser.link = eleSub
                                                                                    .getAttribute("link");
                                                                            String value = eleSub
                                                                                    .getAttribute("submitflag");
                                                                            treeuser.submitflag = TextUtils
                                                                                    .isEmpty(value) ? ""
                                                                                    : value;
                                                                            td.submitflag = treeuser.submitflag;
                                                                            treeuser.parent = trParent;
                                                                            if (nodeSub
                                                                                    .hasChildNodes()) {
                                                                                NodeList opNodes = nodeSub
                                                                                        .getChildNodes();
                                                                                for (int p = 0; p < opNodes
                                                                                        .getLength(); p++) {
                                                                                    Node nodeOp = opNodes
                                                                                            .item(p);
                                                                                    if ("options"
                                                                                            .equalsIgnoreCase(nodeOp
                                                                                                    .getNodeName())) {
                                                                                        Element eleOp = (Element) nodeOp;
                                                                                        option = approveDetailEntry.new UIOption();
                                                                                        option.value = eleOp
                                                                                                .getAttribute("value");
                                                                                        option.checked = eleOp
                                                                                                .getAttribute("checked");
                                                                                        option.readonly = eleOp
                                                                                                .getAttribute("readonly");
                                                                                        option.name = eleOp
                                                                                                .getAttribute("title");
                                                                                        treeuser.optionList
                                                                                                .add(option);
                                                                                        if (nodeOp
                                                                                                .hasChildNodes()) {
                                                                                            NodeList optNodes = nodeOp
                                                                                                    .getChildNodes();
                                                                                            for (int q = 0; q < optNodes
                                                                                                    .getLength(); q++) {
                                                                                                Node nodeOpt = optNodes
                                                                                                        .item(q);
                                                                                                if ("option"
                                                                                                        .equalsIgnoreCase(nodeOpt
                                                                                                                .getNodeName())) {
                                                                                                    Element eleOpt = (Element) nodeOpt;
                                                                                                    subOption = approveDetailEntry.new UIOption();
                                                                                                    subOption.value = eleOpt
                                                                                                            .getAttribute("value");
                                                                                                    subOption.checked = eleOpt
                                                                                                            .getAttribute("checked");
                                                                                                    subOption.readonly = eleOpt
                                                                                                            .getAttribute("readonly");
                                                                                                    subOption.linkmessage = eleOpt
                                                                                                            .getAttribute("linkMessage");
                                                                                                    // option.name
                                                                                                    // =
                                                                                                    // parser.nextText();
                                                                                                    subOption.name = eleOpt
                                                                                                            .getFirstChild() != null ? eleOpt
                                                                                                            .getFirstChild()
                                                                                                            .getNodeValue()
                                                                                                            : "";
                                                                                                    option.options
                                                                                                            .add(subOption);
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    } else if ("option"
                                                                                            .equalsIgnoreCase(nodeOp
                                                                                                    .getNodeName())) {
                                                                                        Element eleOp = (Element) nodeOp;
                                                                                        option = approveDetailEntry.new UIOption();
                                                                                        option.value = eleOp
                                                                                                .getAttribute("value");
                                                                                        option.checked = eleOp
                                                                                                .getAttribute("checked");
                                                                                        option.readonly = eleOp
                                                                                                .getAttribute("readonly");
                                                                                        option.name = eleOp
                                                                                                .getFirstChild() != null ? eleOp
                                                                                                .getFirstChild()
                                                                                                .getNodeValue()
                                                                                                : "";
                                                                                        treeuser.optionList
                                                                                                .add(option);
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else if ("showopinion"
                                                                                .equalsIgnoreCase(eleSub
                                                                                        .getNodeName())) {
                                                                            opinion = approveDetailEntry.new UIOpinion();
                                                                            td.sinopecViews
                                                                                    .add(opinion);
                                                                            opinion.title = eleSub
                                                                                    .getAttribute("title");
                                                                            opinion.name = eleSub
                                                                                    .getAttribute("name");
                                                                            String value = eleSub
                                                                                    .getAttribute("submitflag");
                                                                            opinion.alt = eleSub
                                                                                    .getAttribute("alt");
                                                                            opinion.required = eleSub
                                                                                    .getAttribute("required");
                                                                            opinion.value = eleSub
                                                                                    .getAttribute("value");
                                                                            opinion.mDefault = eleSub
                                                                                    .getAttribute("default");
                                                                            opinion.rules = eleSub
                                                                                    .getAttribute("rules");
                                                                            opinion.submitflag = TextUtils
                                                                                    .isEmpty(value) ? ""
                                                                                    : value;
                                                                            td.submitflag = opinion.submitflag;
                                                                            opinion.parent = trParent;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
            NodeList otherNodes = element.getElementsByTagName("other");// operation
            if (otherNodes != null && otherNodes.getLength() > 0) {
                for (int i = 0; i < otherNodes.getLength(); i++) {
                    Element operNode = (Element) otherNodes.item(i);
                    approveDetailEntry.other = approveDetailEntry.new Other();
                    if (operNode.hasChildNodes()) {
                        NodeList tables = operNode.getChildNodes();
                        for (int j = 0; j < tables.getLength(); j++) {
                            Node tableN = tables.item(j);
                            if ("currNode".equalsIgnoreCase(tableN
                                    .getNodeName())) {
                                approveDetailEntry.other.currNode = tableN
                                        .getFirstChild() != null ? tableN
                                        .getFirstChild().getNodeValue() : "";
                            } else if ("displayDevice".equalsIgnoreCase(tableN
                                    .getNodeName())) {
                                approveDetailEntry.other.displayDevice = tableN
                                        .getFirstChild() != null ? tableN
                                        .getFirstChild().getNodeValue() : "";
                            } else if ("deviceResult".equalsIgnoreCase(tableN
                                    .getNodeName())) {
                                approveDetailEntry.other.deviceResult = tableN
                                        .getFirstChild() != null ? tableN
                                        .getFirstChild().getNodeValue() : "";
                            }
                        }
                    }
                }
            }
            return approveDetailEntry;
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
        return approveDetailEntry;
    }

    /**
     * @param path
     */
    public static SinopecApproveDetailEntry receiveApproveDetailForm(
            String path, String key1) {
        InputStream is = null;
        SinopecApproveDetailEntry approveDetailEntry = new SinopecApproveDetailEntry();
        SinopecTable table = null;
        SinopecTR tr = null;
        SinopecTD td = null;
        UIInput input = null;
        UIUser user = null;
        UIOption option = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve detail form data:" + path
                    + "...key1:" + key1);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", key1));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            XmlPullParser parser = Xml.newPullParser();
            boolean inForm = false;
            boolean inOpera = false;
            boolean inUser = false;
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("form".equalsIgnoreCase(parser.getName())) {
                            approveDetailEntry.form = approveDetailEntry.new SinopecForm();
                            inForm = true;
                        } else if ("table".equalsIgnoreCase(parser.getName())
                                && inForm) {
                            table = approveDetailEntry.new SinopecTable();
                            approveDetailEntry.form.sinopecTables.add(table);
                            table.id = parser.getAttributeValue(null, "id");
                            table.title = parser.getAttributeValue(null, "title");
                            table.name = parser.getAttributeValue(null, "name");
                            table.expand = parser.getAttributeValue(null, "expand");
                            table.EditField = parser.getAttributeValue(null,
                                    "EditField");
                            table.columns = parser.getAttributeValue(null,
                                    "columns");
                            table.Type = parser.getAttributeValue(null, "Type");
                        } else if ("Operation".equalsIgnoreCase(parser.getName())) {
                            approveDetailEntry.operater = approveDetailEntry.new SinopecOperater();
                            inOpera = true;
                        } else if ("table".equalsIgnoreCase(parser.getName())
                                && inOpera) {
                            table = approveDetailEntry.new SinopecTable();
                            approveDetailEntry.operater.sinopecTables.add(table);
                            table.id = parser.getAttributeValue(null, "id");
                            table.title = parser.getAttributeValue(null, "title");
                            table.name = parser.getAttributeValue(null, "name");
                            table.expand = parser.getAttributeValue(null, "expand");
                            table.EditField = parser.getAttributeValue(null,
                                    "EditField");
                            table.Type = parser.getAttributeValue(null, "Type");
                        } else if ("tr".equalsIgnoreCase(parser.getName())) {
                            tr = approveDetailEntry.new SinopecTR();
                            table.trs.add(tr);
                        } else if ("td".equalsIgnoreCase(parser.getName())) {
                            td = approveDetailEntry.new SinopecTD();
                            tr.tds.add(td);
                            int ev = parser.next();
                            if (ev == XmlPullParser.TEXT) {
                                td.content = parser.getText();
                            } else if (ev == XmlPullParser.START_TAG) {
                                td.content = "";
                                if ("input".equalsIgnoreCase(parser.getName())) {
                                    input = approveDetailEntry.new UIInput();
                                    td.sinopecViews.add(input);
                                    input.id = parser.getAttributeValue(null, "id");
                                    input.type = parser.getAttributeValue(null,
                                            "type");
                                    input.name = parser.getAttributeValue(null,
                                            "name");
                                    input.value = parser.getAttributeValue(null,
                                            "value");
                                    input.required = parser.getAttributeValue(null,
                                            "required");
                                    input.alt = parser.getAttributeValue(null,
                                            "alt");
                                    input.checked = parser.getAttributeValue(null,
                                            "checked");
                                    input.type = parser.getAttributeValue(null,
                                            "type");
                                    input.maxlength = parser.getAttributeValue(
                                            null, "maxlength");
                                    input.minlength = parser.getAttributeValue(
                                            null, "minlength");
                                    input.regex = parser.getAttributeValue(null,
                                            "regex");
                                    input.message = parser.getAttributeValue(null,
                                            "message");
                                    input.title = parser.getAttributeValue(null,
                                            "title");
                                } else if ("user"
                                        .equalsIgnoreCase(parser.getName())) {
                                    inUser = true;
                                    user = approveDetailEntry.new UIUser();
                                    td.sinopecViews.add(user);
                                    user.id = parser.getAttributeValue(null, "id");
                                    user.name = parser.getAttributeValue(null,
                                            "name");
                                    user.required = parser.getAttributeValue(null,
                                            "required");
                                    user.type = parser.getAttributeValue(null,
                                            "type");
                                    user.mDefault = parser.getAttributeValue(null,
                                            "default");
                                    user.filter = parser.getAttributeValue(null,
                                            "filter");
                                    user.multi = parser.getAttributeValue(null,
                                            "multi");
                                }
                            }
                        } else if ("input".equalsIgnoreCase(parser.getName())) {
                            input = approveDetailEntry.new UIInput();
                            td.sinopecViews.add(input);
                            input.id = parser.getAttributeValue(null, "id");
                            input.type = parser.getAttributeValue(null, "type");
                            input.name = parser.getAttributeValue(null, "name");
                            input.value = parser.getAttributeValue(null, "value");

                            input.required = parser.getAttributeValue(null,
                                    "required");
                            input.alt = parser.getAttributeValue(null, "alt");
                            input.checked = parser.getAttributeValue(null,
                                    "checked");
                            input.type = parser.getAttributeValue(null, "type");
                            input.maxlength = parser.getAttributeValue(null,
                                    "maxlength");
                            input.minlength = parser.getAttributeValue(null,
                                    "minlength");
                            input.regex = parser.getAttributeValue(null, "regex");
                            input.message = parser.getAttributeValue(null,
                                    "message");
                            input.title = parser.getAttributeValue(null, "title");
                        } else if ("user".equalsIgnoreCase(parser.getName())) {
                            inUser = true;
                            user = approveDetailEntry.new UIUser();
                            td.sinopecViews.add(user);
                            user.id = parser.getAttributeValue(null, "id");
                            user.name = parser.getAttributeValue(null, "name");
                            user.required = parser.getAttributeValue(null,
                                    "required");
                            user.type = parser.getAttributeValue(null, "type");
                            user.mDefault = parser.getAttributeValue(null,
                                    "default");
                            user.filter = parser.getAttributeValue(null, "filter");
                            user.multi = parser.getAttributeValue(null, "multi");
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inUser) {
                            option = approveDetailEntry.new UIOption();
                            user.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.name = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("form".equalsIgnoreCase(parser.getName())) {
                            inForm = false;
                        } else if ("Operation".equalsIgnoreCase(parser.getName())) {
                            inOpera = false;
                        } else if ("user".equalsIgnoreCase(parser.getName())) {
                            inUser = false;
                        }
                        break;
                }
                event = parser.next();
            }
            return approveDetailEntry;
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
        return approveDetailEntry;
    }

    /**
     * @param path
     */
    public static SinopecApproveDetailEntry receiveApproveDetailForm1(
            InputStream inputStream, String path, String key1) {
        InputStream is = null;
        SinopecApproveDetailEntry approveDetailEntry = new SinopecApproveDetailEntry();
        SinopecTable table = null;
        SinopecTR tr = null;
        SinopecTD td = null;
        UIInput input = null;
        UIUser user = null;
        UIOption option = null;
        try {
            // HttpParams httpParameters = new BasicHttpParams();
            // DefaultHttpClient httpClient = HttpUtils
            // .initHttpClient(httpParameters);
            // Log.i(TAG, "begin load approve detail form data:" + path
            // + "...key1:" + key1);
            // // HttpPost httpPost = new HttpPost(path);
            // setHeader(httpPost);
            // // params
            // List<NameValuePair> nameValuePairs = new
            // ArrayList<NameValuePair>();
            // nameValuePairs.add(new BasicNameValuePair("key1", key1));
            // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
            // "UTF-8"));

            // HttpResponse response = httpClient.execute(httpPost);
            // HttpEntity entity = response.getEntity();
            // is = entity.getContent();
            XmlPullParser parser = Xml.newPullParser();
            boolean inForm = false;
            boolean inOpera = false;
            boolean inUser = false;
            parser.setInput(inputStream, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("form".equalsIgnoreCase(parser.getName())) {
                            approveDetailEntry.form = approveDetailEntry.new SinopecForm();
                            inForm = true;
                        } else if ("table".equalsIgnoreCase(parser.getName())
                                && inForm) {
                            table = approveDetailEntry.new SinopecTable();
                            approveDetailEntry.form.sinopecTables.add(table);
                            table.id = parser.getAttributeValue(null, "id");
                            table.title = parser.getAttributeValue(null, "title");
                            table.name = parser.getAttributeValue(null, "name");
                            table.expand = parser.getAttributeValue(null, "expand");
                            table.EditField = parser.getAttributeValue(null,
                                    "EditField");
                            table.columns = parser.getAttributeValue(null,
                                    "columns");
                            table.Type = parser.getAttributeValue(null, "Type");
                        } else if ("Operation".equalsIgnoreCase(parser.getName())) {
                            approveDetailEntry.operater = approveDetailEntry.new SinopecOperater();
                            inOpera = true;
                        } else if ("table".equalsIgnoreCase(parser.getName())
                                && inOpera) {
                            table = approveDetailEntry.new SinopecTable();
                            approveDetailEntry.operater.sinopecTables.add(table);
                            table.id = parser.getAttributeValue(null, "id");
                            table.title = parser.getAttributeValue(null, "title");
                            table.name = parser.getAttributeValue(null, "name");
                            table.expand = parser.getAttributeValue(null, "expand");
                            table.EditField = parser.getAttributeValue(null,
                                    "EditField");
                            table.Type = parser.getAttributeValue(null, "Type");
                        } else if ("tr".equalsIgnoreCase(parser.getName())) {
                            tr = approveDetailEntry.new SinopecTR();
                            table.trs.add(tr);
                        } else if ("td".equalsIgnoreCase(parser.getName())) {
                            td = approveDetailEntry.new SinopecTD();
                            tr.tds.add(td);
                            int ev = parser.next();
                            if (ev == XmlPullParser.TEXT) {
                                td.content = parser.getText();
                            } else if (ev == XmlPullParser.START_TAG) {
                                td.content = "";
                                if ("input".equalsIgnoreCase(parser.getName())) {
                                    input = approveDetailEntry.new UIInput();
                                    td.sinopecViews.add(input);
                                    input.id = parser.getAttributeValue(null, "id");
                                    input.type = parser.getAttributeValue(null,
                                            "type");
                                    input.name = parser.getAttributeValue(null,
                                            "name");
                                    input.value = parser.getAttributeValue(null,
                                            "value");
                                    input.required = parser.getAttributeValue(null,
                                            "required");
                                    input.alt = parser.getAttributeValue(null,
                                            "alt");
                                    input.checked = parser.getAttributeValue(null,
                                            "checked");
                                    input.type = parser.getAttributeValue(null,
                                            "type");
                                    input.maxlength = parser.getAttributeValue(
                                            null, "maxlength");
                                    input.minlength = parser.getAttributeValue(
                                            null, "minlength");
                                    input.regex = parser.getAttributeValue(null,
                                            "regex");
                                    input.message = parser.getAttributeValue(null,
                                            "message");
                                    input.title = parser.getAttributeValue(null,
                                            "title");
                                } else if ("user"
                                        .equalsIgnoreCase(parser.getName())) {
                                    inUser = true;
                                    user = approveDetailEntry.new UIUser();
                                    td.sinopecViews.add(user);
                                    user.id = parser.getAttributeValue(null, "id");
                                    user.name = parser.getAttributeValue(null,
                                            "name");
                                    user.required = parser.getAttributeValue(null,
                                            "required");
                                    user.type = parser.getAttributeValue(null,
                                            "type");
                                    user.mDefault = parser.getAttributeValue(null,
                                            "default");
                                    user.filter = parser.getAttributeValue(null,
                                            "filter");
                                    user.multi = parser.getAttributeValue(null,
                                            "multi");
                                }
                            }
                        } else if ("input".equalsIgnoreCase(parser.getName())) {
                            input = approveDetailEntry.new UIInput();
                            td.sinopecViews.add(input);
                            input.id = parser.getAttributeValue(null, "id");
                            input.type = parser.getAttributeValue(null, "type");
                            input.name = parser.getAttributeValue(null, "name");
                            input.value = parser.getAttributeValue(null, "value");

                            input.required = parser.getAttributeValue(null,
                                    "required");
                            input.alt = parser.getAttributeValue(null, "alt");
                            input.checked = parser.getAttributeValue(null,
                                    "checked");
                            input.type = parser.getAttributeValue(null, "type");
                            input.maxlength = parser.getAttributeValue(null,
                                    "maxlength");
                            input.minlength = parser.getAttributeValue(null,
                                    "minlength");
                            input.regex = parser.getAttributeValue(null, "regex");
                            input.message = parser.getAttributeValue(null,
                                    "message");
                            input.title = parser.getAttributeValue(null, "title");
                        } else if ("user".equalsIgnoreCase(parser.getName())) {
                            inUser = true;
                            user = approveDetailEntry.new UIUser();
                            td.sinopecViews.add(user);
                            user.id = parser.getAttributeValue(null, "id");
                            user.name = parser.getAttributeValue(null, "name");
                            user.required = parser.getAttributeValue(null,
                                    "required");
                            user.type = parser.getAttributeValue(null, "type");
                            user.mDefault = parser.getAttributeValue(null,
                                    "default");
                            user.filter = parser.getAttributeValue(null, "filter");
                            user.multi = parser.getAttributeValue(null, "multi");
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inUser) {
                            option = approveDetailEntry.new UIOption();
                            user.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.name = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("form".equalsIgnoreCase(parser.getName())) {
                            inForm = false;
                        } else if ("Operation".equalsIgnoreCase(parser.getName())) {
                            inOpera = false;
                        } else if ("user".equalsIgnoreCase(parser.getName())) {
                            inUser = false;
                        }
                        break;
                }
                event = parser.next();
            }
            return approveDetailEntry;
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
        return approveDetailEntry;
    }

    /**
     * @param path
     */
    public static SinopecApproveDetailEntry receiveApproveRouteSubmit(
            String path, String key1, String key2, String key3, String key4) {
        InputStream is = null;
        boolean submitFlag = false;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve submit:" + path + "...key1:" + key1
                    + "...key2:" + key2 + "...key3:" + key3);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", URLEncoder
                    .encode(key1, "utf-8")));
            nameValuePairs.add(new BasicNameValuePair("key2", key2));
            nameValuePairs.add(new BasicNameValuePair("key3", key3));
            nameValuePairs.add(new BasicNameValuePair("pageId", key4));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println("result is:"+StreamUtils.retrieveContent(is));
            // Log.i(TAG, "result:" + new String(LoadUtils.load(is)));

            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_list.xml");

            // int leng ;
            // byte[] arr = new byte[1024];
            // while ((leng = is.read(arr)) >0) {
            // System.out.println(new String(arr, 0, leng)+ "新闻列表的数据");
            // }
            SinopecApproveDetailEntry entry = new SinopecApproveDetailEntry();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("result".equalsIgnoreCase(parser.getName())) {
                            String result = parser.nextText();
                            if ("1".equalsIgnoreCase(result)) {
                                // submitFlag = true;
                                entry.submitFlag = true;
                            }
                        } else if ("message".equalsIgnoreCase(parser.getName())) {
                            entry.message = parser.nextText();
                        }
                        // <?xml version="1.0" encoding="utf-8"
                        // ?><root><result>0</result><message>请保存行项目,在进行提交</message></root>

                        break;
                }
                event = parser.next();
            }
            return entry;

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
        return null;
    }

    // /**
    // * @param path
    // */
    // public static SinopecApproveDetailEntry receiveApproveDetail(String path)
    // {
    // InputStream is = null;
    // SinopecApproveDetailEntry approveDetailEntry = new
    // SinopecApproveDetailEntry();
    // SinopecTable table = null;
    // SinopecTR tr = null;
    // SinopecTD td = null;
    // UIInput input = null;
    // try {
    // HttpParams httpParameters = new BasicHttpParams();DefaultHttpClient
    // httpClient = HttpUtils.initHttpClient(httpParameters);
    // Log.i(TAG, "begin load approve detail data:" + path);
    // // HttpPost httpPost = new HttpPost(path);
    // // setHeader(httpPost);
    // // params
    // // List<NameValuePair> nameValuePairs = new
    // // ArrayList<NameValuePair>();
    // // nameValuePairs.add(new BasicNameValuePair("ProgID",
    // // WebUtils.packageName));
    // // nameValuePairs.add(new BasicNameValuePair("DeviceType", "3"));
    // // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
    // // "UTF-8"));
    //
    // // HttpResponse response = httpClient.execute(httpPost);
    // // HttpEntity entity = response.getEntity();
    // // is = entity.getContent();
    //
    // // local data
    // is = DataCollectionUtils.class.getClassLoader()
    // .getResourceAsStream("sinopec_approve_detail.xml");
    // DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    // DocumentBuilder builder = factory.newDocumentBuilder();
    // Document document = builder.parse(is);
    // NodeList rootList = document.getElementsByTagName("root");
    // for (int i = 0; i < rootList.getLength(); i++) {
    // Node ele = rootList.item(i);
    // if (ele.getNodeType() == Node.ELEMENT_NODE) {
    // if ("form".equalsIgnoreCase(ele.getNodeName())) {
    // Element ele1 = (Element) ele;
    // approveDetailEntry.form = approveDetailEntry.new SinopecForm();
    // if (ele1.hasChildNodes()) {
    // NodeList nodeList1 = ele1.getChildNodes();
    // for (int j = 0; j < nodeList1.getLength(); j++) {
    // Node node = nodeList1.item(j);
    // if (node.getNodeType() == Node.ELEMENT_NODE) {
    // Element ele2 = (Element) node;
    // if ("table".equalsIgnoreCase(ele2
    // .getNodeName())) {
    // table = approveDetailEntry.new SinopecTable();
    // approveDetailEntry.form.sinopecTables.add(table);
    // table.id = ele2.getAttribute("id");
    // table.title = ele2.getAttribute("title");
    // table.name = ele2.getAttribute("name");
    // table.expand = ele2.getAttribute("expand");
    // table.EditField = ele2.getAttribute("EditField");
    // table.Type = ele2.getAttribute("Type");
    // if (ele2.hasChildNodes()) {
    // NodeList nodeList2 = ele2
    // .getChildNodes();
    // for (int k = 0; k < nodeList2
    // .getLength(); k++) {
    // Node node2 = nodeList2.item(k);
    // if (node2.getNodeType() == Node.ELEMENT_NODE) {
    // Element ele3 = (Element) node2;
    // if("tr".equalsIgnoreCase(ele3.getNodeName())) {
    // tr = approveDetailEntry.new SinopecTR();
    // table.trs.add(tr);
    // if(ele3.hasChildNodes()) {
    // NodeList nodelist3 = ele3.getChildNodes();
    // for(int p=0; p<nodelist3.getLength(); p++) {
    // Node node3 = nodelist3.item(p);
    // if (node2.getNodeType() == Node.ELEMENT_NODE) {
    // Element ele4 = (Element) node3;
    // if("td".equalsIgnoreCase(ele4.getNodeName())) {
    // td = approveDetailEntry.new SinopecTD();
    // tr.tds.add(td);
    // }
    // }
    // }
    // }
    //
    //
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // return approveDetailEntry;
    // } catch (Exception e) {
    // if (Constants.DEBUG)
    // e.printStackTrace();
    // } finally {
    // if (is != null) {
    // try {
    // is.close();
    // } catch (IOException e) {
    // if (Constants.DEBUG)
    // e.printStackTrace();
    // }
    // }
    // }
    // return approveDetailEntry;
    // }

    /**
     * 获取门户新闻列表
     */
    public static List<List<MobileNewLieb>> receiverMobileNewLieb(String path,
                                                                  String newLeibId, String temId, int rowNum, int pageSize) {
        InputStream is = null;
        List<List<MobileNewLieb>> allList = new ArrayList<List<MobileNewLieb>>();
        List<MobileNewLieb> imgList = null;
        List<MobileNewLieb> itemLists = null;
        MobileNewLieb items = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            Log.i(TAG, path + "===" + newLeibId + "====" + temId + "==="
                    + rowNum + "====" + pageSize);
            nameValuePairs.add(new BasicNameValuePair("pageid", newLeibId));
            nameValuePairs.add(new BasicNameValuePair("key1", temId + ""));
            nameValuePairs.add(new BasicNameValuePair("key2", rowNum + ""));
            nameValuePairs.add(new BasicNameValuePair("key3", pageSize + ""));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            setHeader(httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println(StreamUtils.retrieveContent(is));
            // is =
            // DataCollectionUtils.class.getClassLoader().getResourceAsStream("yansan_list.xml");
            boolean isList = false;
            boolean isimage = false;
            itemLists = new ArrayList<MobileNewLieb>();
            imgList = new ArrayList<MobileNewLieb>();

            allList.add(itemLists);
            allList.add(imgList);

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("list".equalsIgnoreCase(parser.getName())) {
                            isList = true;
                        } else if ("imageview".equalsIgnoreCase(parser.getName())) {
                            isimage = true;
                        } else if ("item".equalsIgnoreCase(parser.getName())
                                && isimage) {
                            items = new MobileNewLieb();
                            imgList.add(items);
                        } else if ("item".equalsIgnoreCase(parser.getName())
                                && isList) {
                            items = new MobileNewLieb();
                            boolean level = Boolean.parseBoolean(parser
                                    .getAttributeValue(null, "level"));
                            items.level = level;
                            itemLists.add(items);
                        } else if ("id".equalsIgnoreCase(parser.getName())) {
                            String id = parser.nextText();
                            items.setId(id);

                        } else if ("title".equalsIgnoreCase(parser.getName())) {
                            event = parser.next();
                            if (event == XmlPullParser.TEXT) {
                                String title = parser.getText();
                                System.out.println("title:" + title);
                                items.setTitle(title);
                            }
                        } else if ("date".equalsIgnoreCase(parser.getName())) {
                            String date = parser.nextText();
                            items.setDate(date);
                        } else if ("author".equalsIgnoreCase(parser.getName())) {
                            String author = parser.nextText();
                            items.setAuthor(author);
                        } else if ("img".equalsIgnoreCase(parser.getName())) {
                            String img = parser.nextText();
                            items.setImg(img);
                        } else if ("summary".equalsIgnoreCase(parser.getName())) {
                            String summary = parser.nextText();
                            items.setSummary(summary);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equalsIgnoreCase(parser.getName())) {
                            items = null;
                        } else if ("list".equalsIgnoreCase(parser.getName())) {
                            isList = false;
                        } else if ("imageview".equalsIgnoreCase(parser.getName())) {
                            isimage = false;
                        }
                        break;
                }
                event = parser.next();
            }
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
        return allList;
    }

    private static void setHeader(HttpPost httpPost) {
        httpPost.setHeader("EquipType", "Android");
        httpPost.setHeader("EquipSN", WebUtils.deviceId);
        httpPost.setHeader("Soft", WebUtils.packageName);
        httpPost.setHeader("Tel", WebUtils.phoneNumber);
        httpPost.setHeader("Cookie", WebUtils.cookie);
        httpPost.setHeader("network", WebUtils.networkType);
//        httpPost.setHeader("Content-Length", "0");
    }

    private static void setHeader(HttpGet httpPost) {
        httpPost.setHeader("EquipType", "Android");//
        httpPost.setHeader("EquipSN", WebUtils.deviceId);
        httpPost.setHeader("Soft", WebUtils.packageName);
        httpPost.setHeader("Tel", WebUtils.phoneNumber);
        httpPost.setHeader("Cookie", WebUtils.cookie);
        httpPost.setHeader("network", WebUtils.networkType);
    }

    /**
     * 获取部门/主任 日程数据
     *
     * @param url 地址
     * @return
     */
    public static HashMap<String, List<ScheduleData>> getUsualScheduleData(
            String url, String pageId, String startTime, String endTime) {

        HashMap<String, List<ScheduleData>> scheduleMap = new HashMap<String, List<ScheduleData>>();
        // List<ScheduleData> scheduleDataList = new ArrayList<ScheduleData>();
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document document = null;
        InputStream inputStream = null;

        factory = DocumentBuilderFactory.newInstance();
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost post = new HttpPost(url);
            List<BasicNameValuePair> commentParams = new LinkedList<BasicNameValuePair>();
            commentParams.add(new BasicNameValuePair("pageid", pageId));
            commentParams.add(new BasicNameValuePair("EquipType", "Android"));
            commentParams.add(new BasicNameValuePair("EquipSN",
                    WebUtils.deviceId));

            commentParams.add(new BasicNameValuePair("key1", ""));
            commentParams.add(new BasicNameValuePair("key2", ""));
            commentParams.add(new BasicNameValuePair("key3", ""));
            commentParams.add(new BasicNameValuePair("key4", ""));
            commentParams.add(new BasicNameValuePair("key5", startTime));
            commentParams.add(new BasicNameValuePair("key6", endTime));
            setHeader(post);
            post.setEntity(new UrlEncodedFormEntity(commentParams, "UTF-8"));
            HttpResponse commentResponse = httpClient.execute(post);
            HttpEntity commentEntity = commentResponse.getEntity();
            inputStream = commentEntity.getContent();
            // byte a[] = new byte[1024];
            // int tt = 0;
            // while ((tt = inputStream.read(a)) > 0) {
            // System.out.println(new String(a, 0, tt)
            // + "!!!!!!!!!!!!!!!!!!!!!!!");
            // }
            // inputStream = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("schedule_list.xml");

            builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
            Element root = document.getDocumentElement();

            if (root != null) {
                NodeList nodes = root.getElementsByTagName("items");
                int maxCount = nodes.getLength();
                List<ScheduleData> scheduleDataList = null;
                for (int i = 0; i < maxCount; i++) {
                    Element scheduleElement = (Element) nodes.item(i);
                    scheduleDataList = new ArrayList<ScheduleData>();
                    if (scheduleElement != null) {
                        NodeList schedules = scheduleElement
                                .getElementsByTagName("item");
                        ScheduleData item = null;
                        for (int j = 0; j < schedules.getLength(); j++) {
                            item = new ScheduleData();
                            Element riverElement = (Element) (schedules.item(j));

                            Element itemID = (Element) riverElement
                                    .getElementsByTagName("itemid").item(0);
                            Element Title = (Element) riverElement
                                    .getElementsByTagName("title").item(0);
                            Element StartTime = (Element) riverElement
                                    .getElementsByTagName("startdate").item(0);
                            Element Endtime = (Element) riverElement
                                    .getElementsByTagName("enddate").item(0);
                            Element localtion = (Element) riverElement
                                    .getElementsByTagName("localtion").item(0);
                            Element creator = (Element) riverElement
                                    .getElementsByTagName("creator").item(0);
                            Element organizer = (Element) riverElement
                                    .getElementsByTagName("organizer").item(0);
                            Element scope = (Element) riverElement
                                    .getElementsByTagName("scope").item(0);

                            item.setItemID(itemID.getFirstChild() != null ? itemID
                                    .getFirstChild().getNodeValue() : "");
                            item.setTitle(Title.getFirstChild() != null ? Title
                                    .getFirstChild().getNodeValue() : "");
                            item.setStartTime(StartTime.getFirstChild() != null ? StartTime
                                    .getFirstChild().getNodeValue() : "");
                            item.setEndtime(Endtime.getFirstChild() != null ? Endtime
                                    .getFirstChild().getNodeValue() : "");
                            item.setLocaltion(localtion.getFirstChild() != null ? localtion
                                    .getFirstChild().getNodeValue() : "");
                            item.setCreator(creator.getFirstChild() != null ? creator
                                    .getFirstChild().getNodeValue() : "");
                            item.setOrganizer(organizer.getFirstChild() != null ? organizer
                                    .getFirstChild().getNodeValue() : "");
                            item.setScope(scope.getFirstChild() != null ? scope
                                    .getFirstChild().getNodeValue() : "");

                            scheduleDataList.add(item);
                        }
                        scheduleMap.put(scheduleElement
                                        .getAttributeNode("date").getValue(),
                                scheduleDataList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
        }

        return scheduleMap;

    }

    /**
     * 获取公司 日程数据
     *
     * @param url 地址
     * @return
     */
    public static List<ScheduleDocData> getScheduleDocData(String url,
                                                           String pageId) {
        List<ScheduleDocData> scheduleDocDatas = new ArrayList<ScheduleDocData>();
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document document = null;
        InputStream inputStream = null;

        factory = DocumentBuilderFactory.newInstance();
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost post = new HttpPost(url);
            List<BasicNameValuePair> commentParams = new LinkedList<BasicNameValuePair>();
            commentParams.add(new BasicNameValuePair("pageid", pageId));
            commentParams.add(new BasicNameValuePair("EquipType", "Android"));
            commentParams.add(new BasicNameValuePair("EquipSN",
                    WebUtils.deviceId));

            commentParams.add(new BasicNameValuePair("key1", "0"));
            commentParams.add(new BasicNameValuePair("key2", "10"));
            commentParams.add(new BasicNameValuePair("key3", ""));
            commentParams.add(new BasicNameValuePair("key4", "0"));
            commentParams.add(new BasicNameValuePair("key5", ""));
            commentParams.add(new BasicNameValuePair("key6", ""));
            setHeader(post);
            post.setEntity(new UrlEncodedFormEntity(commentParams, "UTF-8"));
            HttpResponse commentResponse = httpClient.execute(post);
            HttpEntity commentEntity = commentResponse.getEntity();
            inputStream = commentEntity.getContent();
            // inputStream = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("schedule_doc_list.xml");

            builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
            Element root = document.getDocumentElement();

            if (root != null) {
                NodeList nodes = root.getElementsByTagName("doc");
                int maxCount = nodes.getLength();
                ScheduleDocData item = null;
                for (int i = 0; i < maxCount; i++) {
                    item = new ScheduleDocData();
                    Element itemElement = (Element) nodes.item(i);

                    String Id = itemElement.getAttributeNode("id").getValue();
                    String Title = itemElement.getAttributeNode("title")
                            .getValue();
                    String FileUri = itemElement.getAttributeNode("fileuri")
                            .getValue();
                    String CategoryId = itemElement.getAttributeNode(
                            "categoryid").getValue();

                    item.setId(Id);
                    item.setTitle(Title);
                    item.setFileUri(FileUri);
                    item.setCategoryId(CategoryId);

                    scheduleDocDatas.add(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
        }
        return scheduleDocDatas;
    }

    /**
     * 获取个人 日程每页数量
     *
     * @param url 地址
     * @return
     */
    public static int getSchedulePersonalPageSize() {
        int pageSize = 10;
        try {
            String url = "http://10.238.192.250/DataMutualCenter/Integrated/GetPageSize.aspx";
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpGet get = new HttpGet(url);
            HttpResponse pageSizeResponse = httpClient.execute(get);
            HttpEntity pageSizeEntity = pageSizeResponse.getEntity();
            pageSize = Integer.parseInt(EntityUtils.toString(pageSizeEntity,
                    "UTF-8"));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageSize;
    }

    /**
     * 获取个人 日程数据
     *
     * @param url 地址
     * @return
     */
    public static List<SchedulePersonalData> getSchedulePersonalData(
            String url, String pageId, int rowNum, int pageSize, String temID,
            String mType) {
        List<SchedulePersonalData> schedulePersonalDatas = new ArrayList<SchedulePersonalData>();
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document document = null;
        InputStream inputStream = null;

        factory = DocumentBuilderFactory.newInstance();
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost post = new HttpPost(url);
            List<BasicNameValuePair> commentParams = new LinkedList<BasicNameValuePair>();
            commentParams.add(new BasicNameValuePair("pageid", pageId));
            commentParams.add(new BasicNameValuePair("EquipType", "Android"));
            commentParams.add(new BasicNameValuePair("EquipSN",
                    WebUtils.deviceId));

            commentParams.add(new BasicNameValuePair("key1", rowNum + ""));
            commentParams.add(new BasicNameValuePair("key2", pageSize + ""));
            commentParams.add(new BasicNameValuePair("key3", temID));
            commentParams.add(new BasicNameValuePair("key4", mType));
            commentParams.add(new BasicNameValuePair("key5", ""));
            commentParams.add(new BasicNameValuePair("key6", ""));
            setHeader(post);

            post.setEntity(new UrlEncodedFormEntity(commentParams, "UTF-8"));
            HttpResponse commentResponse = httpClient.execute(post);
            HttpEntity commentEntity = commentResponse.getEntity();
            inputStream = commentEntity.getContent();
            // byte a[] = new byte[1024];
            // int tt = 0;
            // while ((tt = inputStream.read(a)) > 0) {
            // System.out.println(new String(a, 0, tt)
            // + "￥￥￥￥￥￥￥￥￥￥￥￥￥");
            // }
            // inputStream = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("schedule_personal_list.xml");

            builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
            Element root = document.getDocumentElement();

            if (root != null) {
                NodeList nodes = root.getElementsByTagName("item");
                int maxCount = nodes.getLength();
                SchedulePersonalData item = null;
                for (int i = 0; i < maxCount; i++) {
                    item = new SchedulePersonalData();
                    Element itemElement = (Element) nodes.item(i);

                    Element ID = (Element) itemElement.getElementsByTagName(
                            "itemid").item(0);
                    Element Title = (Element) itemElement.getElementsByTagName(
                            "title").item(0);
                    Element StartDate = (Element) itemElement
                            .getElementsByTagName("startdate").item(0);
                    Element EndDate = (Element) itemElement
                            .getElementsByTagName("enddate").item(0);
                    Element Localtion = (Element) itemElement
                            .getElementsByTagName("localtion").item(0);
                    Element Status = (Element) itemElement
                            .getElementsByTagName("status").item(0);
                    Element Creator = (Element) itemElement
                            .getElementsByTagName("creator").item(0);
                    Element Organizer = (Element) itemElement
                            .getElementsByTagName("organizer").item(0);
                    Element Scope = (Element) itemElement.getElementsByTagName(
                            "scope").item(0);
                    Element Operation = (Element) itemElement
                            .getElementsByTagName("operation").item(0);

                    item.setItemid(ID.getFirstChild() != null ? ID
                            .getFirstChild().getNodeValue() : "");
                    item.setTitle(Title.getFirstChild() != null ? Title
                            .getFirstChild().getNodeValue() : "");
                    item.setStartdate(StartDate.getFirstChild() != null ? StartDate
                            .getFirstChild().getNodeValue() : "");
                    item.setEnddate(EndDate.getFirstChild() != null ? EndDate
                            .getFirstChild().getNodeValue() : "");
                    item.setLocaltion(Localtion.getFirstChild() != null ? Localtion
                            .getFirstChild().getNodeValue() : "");
                    item.setStatus(Status.getFirstChild() != null ? Status
                            .getFirstChild().getNodeValue() : "");
                    item.setCreator(Creator.getFirstChild() != null ? Creator
                            .getFirstChild().getNodeValue() : "");
                    item.setOrganizer(Organizer.getFirstChild() != null ? Organizer
                            .getFirstChild().getNodeValue() : "");
                    item.setScope(Scope.getFirstChild() != null ? Scope
                            .getFirstChild().getNodeValue() : "");
                    item.setOperation(Operation.getFirstChild() != null ? Operation
                            .getFirstChild().getNodeValue() : "");

                    schedulePersonalDatas.add(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
        }
        return schedulePersonalDatas;
    }

    /**
     * 获取日程数据
     *
     * @param url
     *            地址
     * @param pageId
     *            pageId
     * @param rowNum
     *            已加载的数据数量
     * @param pageSize
     *            每页数据条数
     * @param itemID
     *            最后一条数据的ID
     * @param mType
     *            日程类型
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    /*public static ScheduleDatas getScheduleData(String url, String pageId,
            int rowNum, int pageSize, String itemID, String mType,
			String startTime, String endTime) {
		ScheduleDatas scheduleDatas = new ScheduleDatas();
		List<SchedulePersonalData> schedulePersonalDatas = new ArrayList<SchedulePersonalData>();
		HashMap<String, List<ScheduleData>> scheduleMap = new HashMap<String, List<ScheduleData>>();
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		InputStream inputStream = null;

		factory = DocumentBuilderFactory.newInstance();
		try {
			// HttpParams httpParameters = new
			// BasicHttpParams();DefaultHttpClient httpClient =
			// HttpUtils.initHttpClient(httpParameters);
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);
			List<BasicNameValuePair> commentParams = new LinkedList<BasicNameValuePair>();
			commentParams.add(new BasicNameValuePair("pageid", pageId));
			commentParams.add(new BasicNameValuePair("EquipType", "Android"));
			commentParams.add(new BasicNameValuePair("EquipSN",
					WebUtils.deviceId));

			commentParams.add(new BasicNameValuePair("key1", rowNum + ""));
			commentParams.add(new BasicNameValuePair("key2", pageSize + ""));
			commentParams.add(new BasicNameValuePair("key3", itemID));
			commentParams.add(new BasicNameValuePair("key4", mType));
			commentParams.add(new BasicNameValuePair("key5", startTime));
			commentParams.add(new BasicNameValuePair("key6", endTime));
			setHeader(post);

			post.setEntity(new UrlEncodedFormEntity(commentParams, "UTF-8"));
			HttpResponse commentResponse = httpClient.execute(post);
			HttpEntity commentEntity = commentResponse.getEntity();
			inputStream = commentEntity.getContent();
			// byte a[] = new byte[1024];
			// int tt = 0;
			// while ((tt = inputStream.read(a)) > 0) {
			// System.out.println(new String(a, 0, tt));
			// }
			// inputStream = DataCollectionUtils.class.getClassLoader()
			// .getResourceAsStream("schedule_personal_list.xml");

			builder = factory.newDocumentBuilder();
			document = builder.parse(inputStream);
			Element root = document.getDocumentElement();

			if (root != null) {
				if (root.getTagName().equalsIgnoreCase("weekly")) {

					SchedulesFragmentNew.isExpand = true;
					NodeList nodes = root.getElementsByTagName("items");
					int maxCount = nodes.getLength();
					List<ScheduleData> scheduleDataList = null;
					for (int i = 0; i < maxCount; i++) {
						Element scheduleElement = (Element) nodes.item(i);
						scheduleDataList = new ArrayList<ScheduleData>();
						if (scheduleElement != null) {
							NodeList schedules = scheduleElement
									.getElementsByTagName("item");
							ScheduleData item = null;
							for (int j = 0; j < schedules.getLength(); j++) {
								item = new ScheduleData();
								Element riverElement = (Element) (schedules
										.item(j));

								Element itemId = (Element) riverElement
										.getElementsByTagName("itemid").item(0);
								Element Title = (Element) riverElement
										.getElementsByTagName("title").item(0);
								Element StartTime = (Element) riverElement
										.getElementsByTagName("startdate")
										.item(0);
								Element Endtime = (Element) riverElement
										.getElementsByTagName("enddate")
										.item(0);
								Element localtion = (Element) riverElement
										.getElementsByTagName("localtion")
										.item(0);
								Element creator = (Element) riverElement
										.getElementsByTagName("creator")
										.item(0);
								Element organizer = (Element) riverElement
										.getElementsByTagName("organizer")
										.item(0);
								Element scope = (Element) riverElement
										.getElementsByTagName("scope").item(0);

								item.setItemID(itemId.getFirstChild() != null ? itemId
										.getFirstChild().getNodeValue() : "");
								item.setTitle(Title.getFirstChild() != null ? Title
										.getFirstChild().getNodeValue() : "");
								item.setStartTime(StartTime.getFirstChild() != null ? StartTime
										.getFirstChild().getNodeValue() : "");
								item.setEndtime(Endtime.getFirstChild() != null ? Endtime
										.getFirstChild().getNodeValue() : "");
								item.setLocaltion(localtion.getFirstChild() != null ? localtion
										.getFirstChild().getNodeValue() : "");
								item.setCreator(creator.getFirstChild() != null ? creator
										.getFirstChild().getNodeValue() : "");
								item.setOrganizer(organizer.getFirstChild() != null ? organizer
										.getFirstChild().getNodeValue() : "");
								item.setScope(scope.getFirstChild() != null ? scope
										.getFirstChild().getNodeValue() : "");

								scheduleDataList.add(item);
							}
							scheduleMap.put(
									scheduleElement.getAttributeNode("date")
											.getValue(), scheduleDataList);
						}
					}

				} else if (root.getTagName().equalsIgnoreCase("root")) {

					SchedulesFragmentNew.isExpand = false;
					NodeList nodes = root.getElementsByTagName("item");
					int maxCount = nodes.getLength();
					SchedulePersonalData item = null;
					for (int i = 0; i < maxCount; i++) {
						item = new SchedulePersonalData();
						Element itemElement = (Element) nodes.item(i);

						Element ID = (Element) itemElement
								.getElementsByTagName("itemid").item(0);
						Element Title = (Element) itemElement
								.getElementsByTagName("title").item(0);
						Element StartDate = (Element) itemElement
								.getElementsByTagName("startdate").item(0);
						Element EndDate = (Element) itemElement
								.getElementsByTagName("enddate").item(0);
						Element Localtion = (Element) itemElement
								.getElementsByTagName("localtion").item(0);
						Element Status = (Element) itemElement
								.getElementsByTagName("status").item(0);
						Element Creator = (Element) itemElement
								.getElementsByTagName("creator").item(0);
						Element Organizer = (Element) itemElement
								.getElementsByTagName("organizer").item(0);
						Element Scope = (Element) itemElement
								.getElementsByTagName("scope").item(0);
						Element Operation = (Element) itemElement
								.getElementsByTagName("operation").item(0);

						item.setItemid(ID.getFirstChild() != null ? ID
								.getFirstChild().getNodeValue() : "");
						item.setTitle(Title.getFirstChild() != null ? Title
								.getFirstChild().getNodeValue() : "");
						item.setStartdate(StartDate.getFirstChild() != null ? StartDate
								.getFirstChild().getNodeValue() : "");
						item.setEnddate(EndDate.getFirstChild() != null ? EndDate
								.getFirstChild().getNodeValue() : "");
						item.setLocaltion(Localtion.getFirstChild() != null ? Localtion
								.getFirstChild().getNodeValue() : "");
						item.setStatus(Status.getFirstChild() != null ? Status
								.getFirstChild().getNodeValue() : "");
						item.setCreator(Creator.getFirstChild() != null ? Creator
								.getFirstChild().getNodeValue() : "");
						item.setOrganizer(Organizer.getFirstChild() != null ? Organizer
								.getFirstChild().getNodeValue() : "");
						item.setScope(Scope.getFirstChild() != null ? Scope
								.getFirstChild().getNodeValue() : "");
						item.setOperation(Operation.getFirstChild() != null ? Operation
								.getFirstChild().getNodeValue() : "");

						schedulePersonalDatas.add(item);
					}

				}
			}
			scheduleDatas.setScheduleMap(scheduleMap);
			scheduleDatas.setPersonalDatas(schedulePersonalDatas);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
		}
		return scheduleDatas;

	}*/

    /**
     * 获取 日程详情数据
     *
     * @param path 地址
     * @param McId 日程id
     * @return
     */
    public static SinopecApproveDetailEntry getScheduleDetailsData(String path,
                                                                   String McId, String pageid) {
        InputStream is = null;
        SinopecApproveDetailEntry approveDetailEntry = new SinopecApproveDetailEntry();
        SinopecTable table = null;
        SinopecTR tr = null;
        SinopecTD td = null;
        UIInput input = null;
        UIUser user = null;
        UIOption option = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve detail data:" + path);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("pageid", pageid));
            nameValuePairs.add(new BasicNameValuePair("key1", McId));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println("detail:"+StreamUtils.retrieveContent(is));
            // byte a[] = new byte[1024];
            // int tt = 0;
            // System.out.println("@@"+McId+"@@");
            // while ((tt = is.read(a)) > 0) {
            // System.out.println(new String(a, 0, tt)
            // + "************************************");
            // }
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("schedule_details.xml");

            // Log.i(TAG, new String(LoadUtils.load(is)));

            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_detail.xml");
            XmlPullParser parser = Xml.newPullParser();
            boolean inForm = false;
            boolean inOpera = false;
            boolean inUser = false;
            String trParent = "";
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("form".equalsIgnoreCase(parser.getName())) {
                            approveDetailEntry.form = approveDetailEntry.new SinopecForm();
                            inForm = true;
                        } else if ("table".equalsIgnoreCase(parser.getName())
                                && inForm) {
                            table = approveDetailEntry.new SinopecTable();
                            approveDetailEntry.form.sinopecTables.add(table);
                            table.id = parser.getAttributeValue(null, "id");
                            table.title = parser.getAttributeValue(null, "title");
                            table.name = parser.getAttributeValue(null, "name");
                            table.expand = parser.getAttributeValue(null, "expand");
                            table.EditField = parser.getAttributeValue(null,
                                    "EditField");
                            table.Type = parser.getAttributeValue(null, "Type");
                        } else if ("Operation".equalsIgnoreCase(parser.getName())) {
                            approveDetailEntry.operater = approveDetailEntry.new SinopecOperater();
                            inOpera = true;
                        } else if ("table".equalsIgnoreCase(parser.getName())
                                && inOpera) {
                            table = approveDetailEntry.new SinopecTable();
                            approveDetailEntry.operater.sinopecTables.add(table);
                            table.id = parser.getAttributeValue(null, "id");
                            table.title = parser.getAttributeValue(null, "title");
                            table.name = parser.getAttributeValue(null, "name");
                            table.expand = parser.getAttributeValue(null, "expand");
                            table.EditField = parser.getAttributeValue(null,
                                    "EditField");
                            table.Type = parser.getAttributeValue(null, "Type");
                        } else if ("tr".equalsIgnoreCase(parser.getName())) {
                            tr = approveDetailEntry.new SinopecTR();
                            trParent = parser.getAttributeValue(null, "parent");
                            tr.parent = trParent;
                            table.trs.add(tr);
                        } else if ("td".equalsIgnoreCase(parser.getName())) {
                            td = approveDetailEntry.new SinopecTD();
                            tr.tds.add(td);
                            int ev = parser.next();
                            if (ev == XmlPullParser.TEXT) {
                                td.content = parser.getText();
                            } else if (ev == XmlPullParser.START_TAG) {
                                td.content = "";
                                if ("input".equalsIgnoreCase(parser.getName())) {
                                    input = approveDetailEntry.new UIInput();
                                    td.sinopecViews.add(input);
                                    input.id = parser.getAttributeValue(null, "id");
                                    input.parent = trParent;
                                    input.type = parser.getAttributeValue(null,
                                            "type");
                                    input.name = parser.getAttributeValue(null,
                                            "name");
                                    input.value = parser.getAttributeValue(null,
                                            "value");
                                    input.required = parser.getAttributeValue(null,
                                            "required");
                                    input.alt = parser.getAttributeValue(null,
                                            "alt");
                                    input.checked = parser.getAttributeValue(null,
                                            "checked");
                                    input.type = parser.getAttributeValue(null,
                                            "type");
                                    input.maxlength = parser.getAttributeValue(
                                            null, "maxlength");
                                    input.minlength = parser.getAttributeValue(
                                            null, "minlength");
                                    input.regex = parser.getAttributeValue(null,
                                            "regex");
                                    input.message = parser.getAttributeValue(null,
                                            "message");
                                    input.title = parser.getAttributeValue(null,
                                            "title");
                                } else if ("user"
                                        .equalsIgnoreCase(parser.getName())) {
                                    inUser = true;
                                    user = approveDetailEntry.new UIUser();
                                    td.sinopecViews.add(user);
                                    user.id = parser.getAttributeValue(null, "id");
                                    user.title = parser.getAttributeValue(null,
                                            "title");
                                    user.name = parser.getAttributeValue(null,
                                            "name");
                                    user.required = parser.getAttributeValue(null,
                                            "required");
                                    user.type = parser.getAttributeValue(null,
                                            "type");
                                    user.mDefault = parser.getAttributeValue(null,
                                            "default");
                                    user.filter = parser.getAttributeValue(null,
                                            "filter");
                                    user.multi = parser.getAttributeValue(null,
                                            "multi");
                                    user.parent = trParent;
                                }
                            }
                        } else if ("input".equalsIgnoreCase(parser.getName())) {
                            input = approveDetailEntry.new UIInput();
                            td.sinopecViews.add(input);
                            input.id = parser.getAttributeValue(null, "id");
                            input.parent = trParent;
                            input.type = parser.getAttributeValue(null, "type");
                            input.name = parser.getAttributeValue(null, "name");
                            input.value = parser.getAttributeValue(null, "value");

                            input.required = parser.getAttributeValue(null,
                                    "required");
                            input.alt = parser.getAttributeValue(null, "alt");
                            input.checked = parser.getAttributeValue(null,
                                    "checked");
                            input.type = parser.getAttributeValue(null, "type");
                            input.maxlength = parser.getAttributeValue(null,
                                    "maxlength");
                            input.minlength = parser.getAttributeValue(null,
                                    "minlength");
                            input.regex = parser.getAttributeValue(null, "regex");
                            input.message = parser.getAttributeValue(null,
                                    "message");
                            input.title = parser.getAttributeValue(null, "title");
                        } else if ("user".equalsIgnoreCase(parser.getName())) {
                            inUser = true;
                            user = approveDetailEntry.new UIUser();
                            td.sinopecViews.add(user);
                            user.title = parser.getAttributeValue(null, "title");
                            user.id = parser.getAttributeValue(null, "id");
                            user.name = parser.getAttributeValue(null, "name");
                            user.required = parser.getAttributeValue(null,
                                    "required");
                            user.type = parser.getAttributeValue(null, "type");
                            user.mDefault = parser.getAttributeValue(null,
                                    "default");
                            user.filter = parser.getAttributeValue(null, "filter");
                            user.multi = parser.getAttributeValue(null, "multi");
                            user.parent = trParent;
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inUser) {
                            option = approveDetailEntry.new UIOption();
                            user.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.checked = parser.getAttributeValue(null,
                                    "checked");
                            option.readonly = parser.getAttributeValue(null,
                                    "readonly");
                            option.name = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("form".equalsIgnoreCase(parser.getName())) {
                            inForm = false;
                        } else if ("Operation".equalsIgnoreCase(parser.getName())) {
                            inOpera = false;
                        } else if ("user".equalsIgnoreCase(parser.getName())) {
                            inUser = false;
                        }
                        break;
                }
                event = parser.next();
            }
            return approveDetailEntry;
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
        return approveDetailEntry;
    }

    /**
     * 获取个人日程搜索条件
     *
     * @param path
     */
    public static List<ApproveTab> receiveSchedulePersonalTabCondition(
            String path, String pageId) {
        InputStream is = null;
        List<ApproveTab> approveTabs = new ArrayList<ApproveTab>();
        ApproveTab approveTab = null;
        UIDropOption option = null;
        UITextInput input = null;
        UIDropSelect select = null;
        UIDropOrderBy orderBy = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve condition data:" + path);
            boolean inSelect = false;
            boolean inOrderBy = false;

            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("pageId", pageId));
            nameValuePairs.add(new BasicNameValuePair("EquipType", "Android"));
            nameValuePairs.add(new BasicNameValuePair("EquipSN",
                    WebUtils.deviceId));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            // byte a[] = new byte[1024];
            // int tt = 0;
            // while ((tt = is.read(a)) > 0) {
            // System.out.println(new String(a, 0, tt)
            // + "************************************");
            // }

            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_tabs.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Tab".equalsIgnoreCase(parser.getName())) {
                            approveTab.title = parser.getAttributeValue(null,
                                    "title");
                            approveTab.id = parser.getAttributeValue(null, "id");
                        } else if ("select".equalsIgnoreCase(parser.getName())) {
                            inSelect = true;
                            select = approveTab.searchCondition.new UIDropSelect();
                            approveTab.searchCondition.selects.add(select);
                            select.name = parser.getAttributeValue(null, "name");
                            select.required = parser.getAttributeValue(null,
                                    "required");
                            select.title = parser.getAttributeValue(null, "title");
                        } else if ("OrderBy".equalsIgnoreCase(parser.getName())) {
                            inOrderBy = true;
                            orderBy = approveTab.searchCondition.new UIDropOrderBy();
                            approveTab.searchCondition.orderBys.add(orderBy);
                            orderBy.name = parser.getAttributeValue(null, "name");
                            orderBy.required = parser.getAttributeValue(null,
                                    "required");
                            orderBy.title = parser.getAttributeValue(null, "title");
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inSelect) {
                            option = approveTab.searchCondition.new UIDropOption();
                            select.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.name = parser.nextText();
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inOrderBy) {
                            option = approveTab.searchCondition.new UIDropOption();
                            orderBy.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.name = parser.nextText();
                        } else if ("input".equalsIgnoreCase(parser.getName())) {
                            approveTab = new ApproveTab();
                            approveTabs.add(approveTab);
                            input = approveTab.searchCondition.new UITextInput();
                            approveTab.searchCondition.inputs.add(input);
                            input.title = parser.getAttributeValue(null, "title");
                            input.checked = parser.getAttributeValue(null,
                                    "checked");
                            input.value = parser.getAttributeValue(null, "value");
                            input.type = parser.getAttributeValue(null, "type");
                            input.name = parser.getAttributeValue(null, "name");
                            input.required = parser.getAttributeValue(null,
                                    "required");

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("select".equalsIgnoreCase(parser.getName())) {
                            inSelect = false;
                        } else if ("OrderBy".equalsIgnoreCase(parser.getName())) {
                            inOrderBy = false;
                        }
                        break;
                }
                event = parser.next();
            }
            return approveTabs;

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
        return approveTabs;
    }

    /**
     * 获取新建/编辑 日程地址 这个接口没配吧
     *
     * @param path
     */
    public static String receiveScheduleCreateUrl(String path, String pageId) {
        String Url = "";
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);

            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("pageId", pageId));
            nameValuePairs.add(new BasicNameValuePair("EquipType", "Android"));
            nameValuePairs.add(new BasicNameValuePair("EquipSN",
                    WebUtils.deviceId));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            Url = EntityUtils.toString(entity, "UTF-8");
            String Urls[] = Url.split("#");
            Url = Urls[0] + "?userId=" + Urls[1];
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Url;
    }

    /**
     * 获取文档类型数据
     *
     * @param url
     *            地址
     * @return
     */
    /*public static List<DocTypeData> getDocTypeData(String url, String pageId) {
        List<DocTypeData> typeDatas = new ArrayList<DocTypeData>();
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		InputStream inputStream = null;

		factory = DocumentBuilderFactory.newInstance();
		try {
			// HttpParams httpParameters = new
			// BasicHttpParams();DefaultHttpClient httpClient =
			// HttpUtils.initHttpClient(httpParameters);
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);
			List<BasicNameValuePair> commentParams = new LinkedList<BasicNameValuePair>();
			commentParams.add(new BasicNameValuePair("pageid", pageId));
			commentParams.add(new BasicNameValuePair("EquipType", "Android"));
			commentParams.add(new BasicNameValuePair("EquipSN",
					WebUtils.deviceId));

			commentParams.add(new BasicNameValuePair("key1", "0"));
			commentParams.add(new BasicNameValuePair("key2", "10"));
			commentParams.add(new BasicNameValuePair("key3", ""));
			commentParams.add(new BasicNameValuePair("key4", "0"));
			commentParams.add(new BasicNameValuePair("key5", ""));
			commentParams.add(new BasicNameValuePair("key6", ""));
			setHeader(post);
			post.setEntity(new UrlEncodedFormEntity(commentParams, "UTF-8"));
			HttpResponse commentResponse = httpClient.execute(post);
			HttpEntity commentEntity = commentResponse.getEntity();
			inputStream = commentEntity.getContent();
			// inputStream = DataCollectionUtils.class.getClassLoader()
			// .getResourceAsStream("schedule_doc_list.xml");

			builder = factory.newDocumentBuilder();
			document = builder.parse(inputStream);
			Element root = document.getDocumentElement();

			if (root != null) {
				NodeList nodes = root.getElementsByTagName("category");
				int maxCount = nodes.getLength();
				DocTypeData item = null;
				for (int i = 0; i < maxCount; i++) {
					item = new DocTypeData();
					Element itemElement = (Element) nodes.item(i);

					String Id = itemElement.getAttributeNode("id").getValue();
					String Name = itemElement.getAttributeNode("name")
							.getValue();
					String Description = itemElement.getAttributeNode(
							"description").getValue();
					String Type = itemElement.getAttributeNode("type")
							.getValue();
					String Count = itemElement.getAttributeNode("count")
							.getValue();
					String Parentid = itemElement.getAttributeNode("parentid")
							.getValue();

					item.setId(Id);
					item.setName(Name);
					item.setDescription(Description);
					item.setType(Type);
					item.setCount(Count);
					item.setParentid(Parentid);

					typeDatas.add(item);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
		}
		return typeDatas;
	}
*/
    /**
     * 获取文档列表数据
     *
     * @param url
     *            地址
     * @return
     */
//	public static List<DocFileData> getDocData(String url, String pageId) {
//		List<DocFileData> docDatas = new ArrayList<DocFileData>();
//		DocumentBuilderFactory factory = null;
//		DocumentBuilder builder = null;
//		Document document = null;
//		InputStream inputStream = null;
//
//		factory = DocumentBuilderFactory.newInstance();
//		try {
//			// HttpParams httpParameters = new
//			// BasicHttpParams();DefaultHttpClient httpClient =
//			// HttpUtils.initHttpClient(httpParameters);
//			HttpParams httpParameters = new BasicHttpParams();
//			DefaultHttpClient httpClient = HttpUtils
//					.initHttpClient(httpParameters);
//			HttpPost post = new HttpPost(url);
//			List<BasicNameValuePair> commentParams = new LinkedList<BasicNameValuePair>();
//			commentParams.add(new BasicNameValuePair("pageid", pageId));
//			commentParams.add(new BasicNameValuePair("EquipType", "Android"));
//			commentParams.add(new BasicNameValuePair("EquipSN",
//					WebUtils.deviceId));
//
//			commentParams.add(new BasicNameValuePair("key1", "0"));
//			commentParams.add(new BasicNameValuePair("key2", "10"));
//			commentParams.add(new BasicNameValuePair("key3", ""));
//			commentParams.add(new BasicNameValuePair("key4", "0"));
//			commentParams.add(new BasicNameValuePair("key5", ""));
//			commentParams.add(new BasicNameValuePair("key6", ""));
//			setHeader(post);
//			post.setEntity(new UrlEncodedFormEntity(commentParams, "UTF-8"));
//			HttpResponse commentResponse = httpClient.execute(post);
//			HttpEntity commentEntity = commentResponse.getEntity();
//			inputStream = commentEntity.getContent();
//			// inputStream = DataCollectionUtils.class.getClassLoader()
//			// .getResourceAsStream("schedule_doc_list.xml");
//
//			builder = factory.newDocumentBuilder();
//			document = builder.parse(inputStream);
//			Element root = document.getDocumentElement();
//
//			if (root != null) {
//				NodeList nodes = root.getElementsByTagName("doc");
//				int maxCount = nodes.getLength();
//				DocFileData item = null;
//				for (int i = 0; i < maxCount; i++) {
//					item = new DocFileData();
//					Element itemElement = (Element) nodes.item(i);
//
//					String Id = itemElement.getAttributeNode("id").getValue();
//					String Title = itemElement.getAttributeNode("title")
//							.getValue();
//					String Fileuri = itemElement.getAttributeNode("fileuri")
//							.getValue();
//					String Categoryid = itemElement.getAttributeNode(
//							"categoryid").getValue();
//					String Date = itemElement.getAttributeNode("date")
//							.getValue();
//					String Author = itemElement.getAttributeNode("author")
//							.getValue();
//					String Filetype = itemElement.getAttributeNode("filetype")
//							.getValue();
//					String Field1 = itemElement.getAttributeNode("field1")
//							.getValue();
//
//					item.setId(Id);
//					item.setTitle(Title);
//					item.setFileuri(Fileuri);
//					item.setCategoryid(Categoryid);
//					item.setDate(Date);
//					item.setAuthor(Author);
//					item.setFiletype(Filetype);
//					item.setField1(Field1);
//
//					docDatas.add(item);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		} finally {
//		}
//		return docDatas;
//	}

    // 邮箱新增
    /**
     * 获取组织机构部门
     *
     * @param path
     * @return
     */
    /*public static List<MailGroup> receiverMailGroup(String path) {
        List<MailGroup> list = new ArrayList<MailGroup>();
		MailGroup mailGroup = null;
		InputStream is = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpGet httpGet = new HttpGet(path);
			setHeader(httpGet);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (200 == httpResponse.getStatusLine().getStatusCode()) {
				XmlPullParser parser = Xml.newPullParser();
				is = httpResponse.getEntity().getContent();
				// String resultData = StreamUtils.retrieveContent(is);
				// Log.i(TAG, "resultData:" + resultData);
				// System.out.println(StreamUtils.retrieveContent(is));
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				String eleName = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						eleName = parser.getName();
						if ("Department".equalsIgnoreCase(eleName)) {
							mailGroup = new MailGroup();
							mailGroup.setDepartmentId(parser.getAttributeValue(
									null, "departmentId") + "");
							mailGroup.setDepartmentName(parser
									.getAttributeValue(null, "departmentName")
									+ "");
							mailGroup.setHasPerson(parser.getAttributeValue(
									null, "hasPerson") + "");
							mailGroup.setParentId(parser.getAttributeValue(
									null, "parentId") + "");
							list.add(mailGroup);
						}
						break;
					}
					eventType = parser.next();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ArrayList<MailGroup>();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
*/

    /**
     * 获取邮件联系人
     *
     * @param path
     * @return
     */
    public static List<MailContact> receiverMailContacts(String path) {
        List<MailContact> mailContacts = new ArrayList<MailContact>();
        MailContact contact = null;
        InputStream is = null;
        System.out.println("receiverMailContacts path:" + path);
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpGet httpGet = new HttpGet(path);
            setHeader(httpGet);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (200 == httpResponse.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                is = httpResponse.getEntity().getContent();
                // String resultData = StreamUtils.retrieveContent(is);
                // Log.i(TAG, "resultData:" + resultData);
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                String eleName = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            eleName = parser.getName();
                            if ("User".equalsIgnoreCase(eleName)) {
                                contact = new MailContact();
                                String userId = parser.getAttributeValue(null,
                                        "userId") + "";
                                if (userId.equals("null")) {
                                    contact.setUserId(parser.getAttributeValue(
                                            null, "userId") + "");
                                } else {
                                    contact.setUserId(parser.getAttributeValue(
                                            null, "userId") + "");
                                }
                                String fullname = parser.getAttributeValue(null,
                                        "fullname") + "";
                                if (fullname.equals("null")) {
                                    contact.setFullname(parser.getAttributeValue(
                                            null, "fullName") + "");
                                } else {
                                    contact.setFullname(parser.getAttributeValue(
                                            null, "fullname") + "");
                                }
                                String altname = parser.getAttributeValue(null,
                                        "altname") + "";
                                if (altname.equals("null")) {
                                    contact.setAltname(parser.getAttributeValue(
                                            null, "altName") + "");
                                } else {
                                    contact.setAltname(parser.getAttributeValue(
                                            null, "altname") + "");
                                }

                                String departmentId = parser.getAttributeValue(
                                        null, "departmentId") + "";
                                if (departmentId.equals("null")) {
                                    contact.setDepartmentId(parser
                                            .getAttributeValue(null, "departmentid")
                                            + "");
                                } else {
                                    contact.setDepartmentId(parser
                                            .getAttributeValue(null, "departmentId")
                                            + "");
                                }

                                String departmentName = parser.getAttributeValue(
                                        null, "departmentName") + "";
                                if (departmentName.equals("null")) {
                                    contact.setDepartmentName(parser
                                            .getAttributeValue(null,
                                                    "departmentname")
                                            + "");
                                } else {
                                    contact.setDepartmentName(parser
                                            .getAttributeValue(null,
                                                    "departmentName")
                                            + "");
                                }
                                mailContacts.add(contact);
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<MailContact>();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mailContacts;
    }

    /**
     * 首页获取email标签数量 added by
     */
    public static List<String[]> receiverEmailNumDataHome(String path) {
        // path = WebUtils.rootUrl + URLUtils.mobileOfficeNewslistMain;
        List<String[]> numbers = new ArrayList<String[]>();
        String[] number = null;
        String emailNum = "";

        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            if (Constants.DEBUG) {
                Log.i(TAG, "begin get email number:" + path);
            }
            setHeader(httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            if (200 == response.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");
                int event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if ("TotalCount".equalsIgnoreCase(parser.getName())) {
                                emailNum = parser.nextText();
                                number = new String[2];
                                numbers.add(number);
                                number[0] = "dbsy";
                                number[1] = emailNum;
                            }
                            break;
                        default:
                            break;
                    }
                    event = parser.next();
                }
            }
            return numbers;
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

        return numbers;
    }

    /**
     * 获取email标签数量
     */
    public static String receiverEmailNumData(String path) {
        // path = WebUtils.rootUrl + URLUtils.mobileOfficeNewslistMain;
        String emailNum = "";

        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            if (Constants.DEBUG) {
                Log.i(TAG, "begin get email number:" + path);
            }
            setHeader(httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            if (200 == response.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");
                int event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if ("TotalCount".equalsIgnoreCase(parser.getName())) {
                                emailNum = parser.nextText();
                            }
                            break;
                        default:
                            break;
                    }
                    event = parser.next();
                }
            }
            return emailNum;
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

        return emailNum;
    }

    // ----------------------------------新模块
    // 合同审批2.0------------------------------------------

    /**
     * 审批详细信息内容(待办)
     *
     * @param path      路径
     * @param parameter 这个参数表示二级界面传过来的值，如果不是二级界面请求数据，传空字符串，由于这个值的出现，当前这个方法不只是待办用的，
     *                  只要有二级页面的，都会用到这个方法
     * @return
     */
    public static NewOADetailInfo getPeddingApprovalDetail(String path,
                                                           String parameter) {
        String key1 = null;
        if (!parameter.equals("")) {
            String param1 = parameter.substring(0, parameter.indexOf("="));
            String value1 = parameter.substring(parameter.indexOf("=") + 1,
                    parameter.indexOf("&"));
            String param2 = parameter.substring(parameter.indexOf("&") + 1,
                    parameter.lastIndexOf("="));
            String value2 = parameter.substring(parameter.lastIndexOf("=") + 1);

            StringBuilder sb = new StringBuilder();
            sb.append("<Root>");
            sb.append("<SubmitItem>");
            sb.append("<Item>");
            sb.append("<ParaName>" + param1 + "</ParaName>");
            sb.append("<Value>" + value1 + "</Value>");
            sb.append("</Item>");
            sb.append("<Item>");
            sb.append("<ParaName>" + param2 + "</ParaName>");
            sb.append("<Value>" + value2 + "</Value>");
            sb.append("</Item>");
            sb.append("</SubmitItem>");
            sb.append("</Root>");

            key1 = sb.toString();
            key1 = key1.replace("<", "&lt;");
            key1 = key1.replace(">", "&gt;");
        }

        NewOADetailInfo approvalDetailInfo = new NewOADetailInfo();
        // 表格
        boolean bool_tableRow = false;
        ApprovalDetailTableInfo tableInfo = null;
        List<String> approvalDetailTableTitles = null;
        List<List<String>> tableRows = null;
        List<String> rowItems = null;

        // 合同流程节点
        boolean bool_section = false;
        ApprovalProcessInfo processInfo = null;
        List<ProcessSection> sections = null;
        ProcessSection sectionInfo = null;
        List<ProcessGroupList> groupLists = null;
        ProcessGroupList groupList = null;
        List<ProcessGroup> groups = null;
        ProcessGroup group = null;
        String groupName = null; // 组的名称
        List<ProcessGroupItem> groupItems = null;
        ProcessGroupItem groupItem = null;

        // 开始表格了，注意下
        ProcessGroupItemTableInfo groupItemTableInfo = null;
        List<String> showInList = new ArrayList<String>();
        List<String> headerList = new ArrayList<String>();
        List<List<String[]>> contentList = new ArrayList<List<String[]>>();
        List<String[]> contentRowList = new ArrayList<String[]>();

        // 操作选项
        SubmitInfo submitInfo = null;
        SubRouters subRouters = null;
        List<SubRouter> routerList = null;
        boolean bool_router = false;
        SubRouter subRouter = null;
        List<SubOption> optionList = null;
        SubOption subOption = null;
        List<SubItem> subItems = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption.SubItem>();
        SubItem subItem = null;
        SubTextView subTextView = null;

        SubTextBoard subTextBoard = null;
        boolean bool_textBoard = false;
        List<TextBoardItem> itemList = null;
        TextBoardItem boardItem = null;

        InputStream is = null;
        try {

            HttpPost httpPost = null;
            HttpResponse response = null;
            HttpEntity entity = null;
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 120 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);
            DefaultHttpClient httpClient = HttpUtils.initHttpClient(httpParams);
            httpPost = new HttpPost(path);
            httpPost.addHeader("Cookie", WebUtils.cookie);
            httpPost.addHeader("DeviceId", WebUtils.deviceId);
            httpPost.addHeader("EquipType", "Android");
            httpPost.addHeader("EquipSN", WebUtils.deviceId);
            httpPost.addHeader("Soft", WebUtils.packageName);
            httpPost.addHeader("Tel", WebUtils.phoneNumber);
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");

            if (!parameter.equals("")) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("key1", key1));
                entity = new UrlEncodedFormEntity(pairs, "utf-8");
                httpPost.setEntity(entity);
            }

            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            is = entity.getContent();
            // if (!parameter.equals("")) {
            // System.out.println("---" + new String(LoadUtils.load(is),
            // "utf-8"));
            // }

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int event = parser.getEventType();
            String tempName = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                tempName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        /**
                         * 解析表格
                         */
                        if ("Table".equalsIgnoreCase(tempName) && !bool_section) {
                            bool_tableRow = true;
                            tableInfo = approvalDetailInfo.new ApprovalDetailTableInfo();
                            approvalDetailInfo.setTableInfo(tableInfo);
                        } else if ("Header".equalsIgnoreCase(tempName)
                                && bool_tableRow) {
                            approvalDetailTableTitles = new ArrayList<String>();
                            tableInfo.setLists(approvalDetailTableTitles);
                        } else if ("Column".equalsIgnoreCase(tempName)
                                && bool_tableRow) {
                            approvalDetailTableTitles.add(parser.nextText() + "");
                        } else if ("Content".equalsIgnoreCase(tempName)
                                && bool_tableRow) {
                            tableRows = new ArrayList<List<String>>();
                            tableInfo.setListRows(tableRows);
                        } else if ("Row".equalsIgnoreCase(tempName)
                                && bool_tableRow) {
                            rowItems = new ArrayList<String>();
                            tableRows.add(rowItems);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_tableRow) {
                            rowItems.add(parser.nextText() + "");
                        }

                        /**
                         * 合同流程节点
                         */
                        else if ("Process".equalsIgnoreCase(tempName)) {
                            bool_section = true;
                            processInfo = approvalDetailInfo.new ApprovalProcessInfo();
                            sections = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection>();
                            processInfo.setSections(sections);
                            approvalDetailInfo.setProcessInfo(processInfo);
                        }

                        // 这个节点在待办里面是没有的，但是由于是Xml.newPullParser()解析，他的nextText有个bug，bug我觉得是，只要有节点没有解析的，就可能会出现一些异常，
                        else if ("GroupList".equalsIgnoreCase(tempName)) {
                            sectionInfo = processInfo.new ProcessSection();
                            sections.add(sectionInfo);
                            sectionInfo.setState(parser.getAttributeValue(null,
                                    "State") + "");
                            groupLists = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList>();
                            sectionInfo.setGroupLists(groupLists);
                            sectionInfo.setCaption("Caption");
                            groupList = sectionInfo.new ProcessGroupList();
                            groupLists.add(groupList);
                            groups = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup>();
                            groupList.setGroups(groups);
                        } else if ("Group".equalsIgnoreCase(tempName)) {
                            group = groupList.new ProcessGroup();
                            groupName = parser.getAttributeValue(null, "Name") + "";
                            group.setName(groupName);
                            group.setExpand(parser
                                    .getAttributeValue(null, "Expand") + "");
                            groupItems = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup.ProcessGroupItem>();
                            group.setGroupItems(groupItems);
                            groups.add(group);
                        } else if ("Label".equalsIgnoreCase(tempName)) {
                            groupItem = group.new ProcessGroupItem();
                            groupItem.setLabel(parser.nextText() + "");
                            groupItems.add(groupItem);
                        } else if ("Text".equalsIgnoreCase(tempName)) {
                            groupItem.setType(parser
                                    .getAttributeValue(null, "Type") + "");
                            groupItem.setpCode(parser.getAttributeValue(null,
                                    "PCode") + "");
                            groupItem.setText(parser.nextText());
                        }

                        // 表格里面的数据，这个需要注意下
                        else if ("Table".equalsIgnoreCase(tempName) && bool_section) {
                            groupItem = group.new ProcessGroupItem();
                            groupItem.setIfTableData(true);
                            groupItems.add(groupItem);
                            groupItemTableInfo = groupItem.new ProcessGroupItemTableInfo();
                            groupItem.setTableInfo(groupItemTableInfo);
                        } else if ("Header".equalsIgnoreCase(tempName)
                                && bool_section) {
                            showInList = new ArrayList<String>();
                            headerList = new ArrayList<String>();
                            groupItemTableInfo.setShowInlList(showInList);
                            groupItemTableInfo.setHeaderList(headerList);
                        } else if ("Column".equalsIgnoreCase(tempName)
                                && bool_section) {
                            String showInValue = parser.getAttributeValue(null,
                                    "ShowInList");
                            if (showInValue == null) {
                                showInList.add("0");
                            } else {
                                showInList.add(showInValue);
                            }
                            String columnValue = parser.nextText();
                            if (columnValue == null) {
                                headerList.add("");
                            } else {
                                headerList.add(columnValue);
                            }
                        } else if ("Content".equalsIgnoreCase(tempName)
                                && bool_section) {
                            contentList = new ArrayList<List<String[]>>();
                            groupItemTableInfo.setContentList(contentList);
                        } else if ("Items".equalsIgnoreCase(tempName)
                                && bool_section) {
                            contentRowList = new ArrayList<String[]>();
                            contentList.add(contentRowList);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_section) {
                            String[] arr = new String[2];
                            if (parser.getAttributeValue(null, "Type") == null) {
                                arr[0] = "";
                            } else {
                                arr[0] = parser.getAttributeValue(null, "Type");
                            }

                            String contentRowValue = parser.nextText();
                            if (contentRowValue == null) {
                                arr[1] = "";
                            } else {
                                arr[1] = contentRowValue;
                            }
                            contentRowList.add(arr);
                        }

                        /**
                         * 提交
                         */
                        else if ("Submit".equalsIgnoreCase(tempName)) {
                            submitInfo = approvalDetailInfo.new SubmitInfo();
                            approvalDetailInfo.setSubmitInfo(submitInfo);
                        }
                        // routers
                        else if ("Routers".equalsIgnoreCase(tempName)) {
                            subRouters = submitInfo.new SubRouters();
                            submitInfo.setSubRouters(subRouters);
                            subRouters.setParaName(parser.getAttributeValue(null,
                                    "ParaName") + "");
                            subRouters.setTitle(parser.getAttributeValue(null,
                                    "Title") + "");
                            routerList = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter>();
                            subRouters.setRouterList(routerList);
                        } else if ("Router".equalsIgnoreCase(tempName)) {
                            bool_router = true;
                            subRouter = subRouters.new SubRouter();
                            routerList.add(subRouter);
                        } else if ("Key".equalsIgnoreCase(tempName)) {
                            subRouter.setKey(parser.nextText() + "");
                        } else if ("Caption".equalsIgnoreCase(tempName)) {
                            subRouter.setCaption(parser.nextText() + "");
                        } else if ("OperationList".equalsIgnoreCase(tempName)) {
                            optionList = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption>();
                            subRouter.setOptionList(optionList);
                        } else if ("Option".equalsIgnoreCase(tempName)) {
                            subOption = subRouter.new SubOption();
                            optionList.add(subOption);
                            subOption.setMultiple(parser.getAttributeValue(null,
                                    "Multiple") + "");
                            subOption.setParaName(parser.getAttributeValue(null,
                                    "ParaName") + "");
                            subOption.setCaption(parser.getAttributeValue(null,
                                    "Caption") + "");
                            subOption.setType(parser
                                    .getAttributeValue(null, "Type") + "");
                            subOption.setIsChoose(parser.getAttributeValue(null,
                                    "IsChoose") + "");
                            subOption.setRequired(parser.getAttributeValue(null,
                                    "Required") + "");
                        } else if ("Contacts".equalsIgnoreCase(tempName)) {
                            subOption.setAvailable(parser.getAttributeValue(null,
                                    "Available") + "");
                            subOption.setInitOUID(parser.getAttributeValue(null,
                                    "InitOUID") + "");
                            subOption.setSource(parser.getAttributeValue(null,
                                    "Source") + "");
                            subOption.setDisplay(parser.getAttributeValue(null,
                                    "Display") + "");
                        } else if ("Items".equalsIgnoreCase(tempName)
                                && bool_router) {
                            subItems = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption.SubItem>();
                            subOption.setItems(subItems);
                        } else if ("Item".equalsIgnoreCase(tempName) && bool_router) {
                            subItem = subOption.new SubItem();
                            subItems.add(subItem);
                            subItem.setKey(parser.getAttributeValue(null, "Key")
                                    + "");
                            subItem.setSelected(parser.getAttributeValue(null,
                                    "Selected") + "");
                            subItem.setName(parser.nextText() + "");
                        }
                        // textView
                        else if ("TextView".equalsIgnoreCase(tempName)) {
                            subTextView = subRouter.new SubTextView();
                            subRouter.setSubTextView(subTextView);
                            subTextView.setParaName(parser.getAttributeValue(null,
                                    "ParaName") + "");
                            subTextView.setTitle(parser.getAttributeValue(null,
                                    "Title") + "");
                            subTextView.setDefautValue(parser.getAttributeValue(
                                    null, "DefautValue") + "");
                            subTextView.setResultValue(parser.getAttributeValue(
                                    null, "DefautValue") + "");
                            subTextView.setRequired(parser.getAttributeValue(null,
                                    "Required") + "");
                        } else if ("Locution".equalsIgnoreCase(tempName)) {
                            subTextView.setDisplay(parser.getAttributeValue(null,
                                    "Display") + "");
                            subTextView.setAvailable(parser.getAttributeValue(null,
                                    "Available") + "");
                        }
                        // textBoard
                        else if ("TextBoard".equalsIgnoreCase(tempName)) {
                            bool_textBoard = true;
                            subTextBoard = submitInfo.new SubTextBoard();
                            submitInfo.setSubTextBoard(subTextBoard);
                            subTextBoard.setTitle(parser.getAttributeValue(null,
                                    "Title") + "");
                            itemList = new ArrayList<NewOADetailInfo.SubmitInfo.SubTextBoard.TextBoardItem>();
                            subTextBoard.setItemList(itemList);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_textBoard) {
                            boardItem = subTextBoard.new TextBoardItem();
                            boardItem.setTitle(parser.getAttributeValue(null,
                                    "Title") + "");
                            boardItem.setSubtitle(parser.getAttributeValue(null,
                                    "Subtitle") + "");
                            itemList.add(boardItem);
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("Table".equalsIgnoreCase(tempName)) {
                            bool_tableRow = false;
                        } else if ("Router".equalsIgnoreCase(tempName)) {
                            bool_router = false;
                        } else if ("TextBoard".equalsIgnoreCase(tempName)) {
                            bool_textBoard = false;
                        } else if ("Process".equalsIgnoreCase(tempName)) {
                            bool_section = false;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();

            }
            return approvalDetailInfo;
        } catch (Exception e) {
            e.printStackTrace();
            approvalDetailInfo = null;

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
        return approvalDetailInfo;

    }

    /**
     * 审批详细信息内容(已办)
     */
    public static NewOADetailInfo getCompletedApprovalDetail(String path) {
        NewOADetailInfo approvalDetailInfo = null;
        // 表格
        boolean bool_tableRow = false;
        ApprovalDetailTableInfo tableInfo = null;
        List<String> approvalDetailTableTitles = null;
        List<List<String>> tableRows = null;
        List<String> rowItems = null;

        // 合同流程节点
        ApprovalProcessInfo processInfo = null;
        List<ProcessSection> sections = null;
        ProcessSection sectionInfo = null;
        boolean bool_section = false;
        List<ProcessGroupList> groupLists = null;
        ProcessGroupList groupList = null;
        List<ProcessGroup> groups = null;
        ProcessGroup group = null;
        String groupName = null; // 组的名称
        List<ProcessGroupItem> groupItems = null;
        ProcessGroupItem groupItem = null;

        // 开始表格了，注意下
        ProcessGroupItemTableInfo groupItemTableInfo = null;
        List<String> showInList = new ArrayList<String>();
        List<String> headerList = new ArrayList<String>();
        List<List<String[]>> contentList = new ArrayList<List<String[]>>();
        List<String[]> contentRowList = new ArrayList<String[]>();

        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpGet httpGet = new HttpGet(path);
            setHeader(httpGet);
            HttpResponse response = httpClient.execute(httpGet);
            is = response.getEntity().getContent();

            // System.out.println("---"+new String(LoadUtils.load(is),"utf-8"));

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int event = parser.getEventType();
            String tempName = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                tempName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Root".equalsIgnoreCase(tempName)) {
                            approvalDetailInfo = new NewOADetailInfo();
                        }
                        /**
                         * 解析表格
                         */
                        else if ("Table".equalsIgnoreCase(tempName)
                                && !bool_section) {
                            bool_tableRow = true;
                            tableInfo = approvalDetailInfo.new ApprovalDetailTableInfo();
                            approvalDetailInfo.setTableInfo(tableInfo);
                        } else if ("Header".equalsIgnoreCase(tempName)
                                && bool_tableRow && !bool_section) {
                            approvalDetailTableTitles = new ArrayList<String>();
                            tableInfo.setLists(approvalDetailTableTitles);
                        } else if ("Column".equalsIgnoreCase(tempName)
                                && bool_tableRow && !bool_section) {
                            approvalDetailTableTitles.add(parser.nextText() + "");
                        } else if ("Content".equalsIgnoreCase(tempName)
                                && bool_tableRow && !bool_section) {
                            tableRows = new ArrayList<List<String>>();
                            tableInfo.setListRows(tableRows);
                        } else if ("Row".equalsIgnoreCase(tempName)
                                && bool_tableRow && !bool_section) {
                            rowItems = new ArrayList<String>();
                            tableRows.add(rowItems);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_tableRow && !bool_section) {
                            rowItems.add(parser.nextText() + "");
                        }

                        /**
                         * 合同流程节点
                         */
                        else if ("Process".equalsIgnoreCase(tempName)) {
                            processInfo = approvalDetailInfo.new ApprovalProcessInfo();
                            sections = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection>();
                            processInfo.setSections(sections);
                            approvalDetailInfo.setProcessInfo(processInfo);
                        } else if ("Section".equalsIgnoreCase(tempName)) {
                            bool_section = true;
                            sectionInfo = processInfo.new ProcessSection();
                            sections.add(sectionInfo);
                            sectionInfo.setState(parser.getAttributeValue(null,
                                    "State") + "");
                            groupLists = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList>();
                            sectionInfo.setGroupLists(groupLists);
                        } else if ("Caption".equalsIgnoreCase(tempName)
                                && bool_section) {
                            sectionInfo.setCaption(parser.nextText() + "");
                        } else if ("GroupList".equalsIgnoreCase(tempName)
                                && bool_section) {
                            groupList = sectionInfo.new ProcessGroupList();
                            groupLists.add(groupList);
                            groups = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup>();
                            groupList.setGroups(groups);
                        } else if ("Group".equalsIgnoreCase(tempName)
                                && bool_section) {
                            group = groupList.new ProcessGroup();
                            groupName = parser.getAttributeValue(null, "Name") + "";
                            group.setName(groupName);
                            group.setExpand(parser
                                    .getAttributeValue(null, "Expand"));
                            groupItems = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup.ProcessGroupItem>();
                            group.setGroupItems(groupItems);
                            groups.add(group);
                        } else if ("Label".equalsIgnoreCase(tempName)
                                && bool_section) {
                            groupItem = group.new ProcessGroupItem();
                            groupItem.setLabel(parser.nextText() + "");
                            groupItems.add(groupItem);
                        } else if ("Text".equalsIgnoreCase(tempName)
                                && bool_section) {
                            groupItem.setType(parser
                                    .getAttributeValue(null, "Type") + "");
                            groupItem.setpCode(parser.getAttributeValue(null,
                                    "PCode") + "");
                            groupItem.setText(parser.nextText() + "");
                        }
                        // 表格里面的数据，这个需要注意下
                        else if ("Table".equalsIgnoreCase(tempName) && bool_section) {
                            groupItem = group.new ProcessGroupItem();
                            groupItem.setIfTableData(true);
                            groupItems.add(groupItem);
                            groupItemTableInfo = groupItem.new ProcessGroupItemTableInfo();
                            groupItem.setTableInfo(groupItemTableInfo);
                        } else if ("Header".equalsIgnoreCase(tempName)
                                && bool_section) {
                            showInList = new ArrayList<String>();
                            headerList = new ArrayList<String>();
                            groupItemTableInfo.setShowInlList(showInList);
                            groupItemTableInfo.setHeaderList(headerList);
                        } else if ("Column".equalsIgnoreCase(tempName)
                                && bool_section) {
                            String showInValue = parser.getAttributeValue(null,
                                    "ShowInList");
                            if (showInValue == null) {
                                showInList.add("0");
                            } else {
                                showInList.add(showInValue);
                            }
                            String columnValue = parser.nextText();
                            if (columnValue == null) {
                                headerList.add("");
                            } else {
                                headerList.add(columnValue);
                            }
                        } else if ("Content".equalsIgnoreCase(tempName)
                                && bool_section) {
                            contentList = new ArrayList<List<String[]>>();
                            groupItemTableInfo.setContentList(contentList);
                        } else if ("Items".equalsIgnoreCase(tempName)
                                && bool_section) {
                            contentRowList = new ArrayList<String[]>();
                            contentList.add(contentRowList);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_section) {
                            String[] arr = new String[2];
                            if (parser.getAttributeValue(null, "Type") == null) {
                                arr[0] = "";
                            } else {
                                arr[0] = parser.getAttributeValue(null, "Type");
                            }

                            String contentRowValue = parser.nextText();
                            if (contentRowValue == null) {
                                arr[1] = "";
                            } else {
                                arr[1] = contentRowValue;
                            }
                            contentRowList.add(arr);
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("Table".equalsIgnoreCase(tempName)) {
                            bool_tableRow = false;
                        } else if ("Section".equalsIgnoreCase(tempName)) {
                            bool_section = false;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();

            }
            return approvalDetailInfo;
        } catch (Exception e) {
            e.printStackTrace();
            approvalDetailInfo = null;

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
        return approvalDetailInfo;

    }

    /**
     * @param path
     * @throws Exception
     * @author minjiewu
     * @see 获取审批选项卡信息
     */
	/*public static List<NewOATabInfo> getApprovalTabData(String path) {

		// 审批信息选项卡内容
		List<NewOATabInfo> tabInfos = new ArrayList<NewOATabInfo>();
		NewOATabInfo tabInfo = null;

		InputStream is = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(path);
			Log.i(TAG, "getPendingCompleteData2:" + path);
			setHeader(httpPost);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			// System.out.println(new String(LoadUtils.load(is),"utf-8"));
			// // 这里获取的是本地的数据
			// is =
			// DataCollectionUtils.class.getClassLoader().getResourceAsStream("approval_XML.xml");

			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "utf-8");
			int event = parser.getEventType();
			String tempName = null;
			while (event != XmlPullParser.END_DOCUMENT) {
				tempName = parser.getName();
				switch (event) {
				case XmlPullParser.START_TAG:

					// 解析选项卡名称
					if ("List".equalsIgnoreCase(tempName)) {
						tabInfos = new ArrayList<NewOATabInfo>();
					} else if ("TagItem".equalsIgnoreCase(tempName)) {
						tabInfo = new NewOATabInfo();
						String name = parser.getAttributeValue(null, "Name");
						if (name == null) {
							name = parser.getAttributeValue(null, "name");
						}
						tabInfo.setName(name + "");

						String listID = parser
								.getAttributeValue(null, "ListID");
						if (listID == null) {
							listID = parser.getAttributeValue(null, "listID");
						}
						tabInfo.setListID(listID + "");

						String detailID = parser.getAttributeValue(null,
								"DetailID");
						if (detailID == null) {
							detailID = parser.getAttributeValue(null,
									"detailID");
						}
						tabInfo.setDetailID(detailID + "");
						tabInfos.add(tabInfo);
					}

					break;
				default:
					break;
				}
				event = parser.next();

			}
			return tabInfos;
		} catch (Exception e) {
			e.printStackTrace();
			tabInfos = null;

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

		return tabInfos;
	}*/

    // 获取审批下列表数据,这个用于查询的，
    public static NewOAInfo getApprovalListData(String path, String key1) {

        NewOAInfo approvalInfo = null;

        // 搜索
        ApprovalSearchInfo searchInfo = null;
        // 下拉列表
        List<ApprovaldropDownList> listDropDownLists = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApprovaldropDownList>();
        ApprovaldropDownList dropDownList = null;
        List<String[]> listItems = new ArrayList<String[]>();
        boolean bool_dropDown = false;
        // 日期
        List<ApproveDatePicker> datePickers = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker>();
        ApproveDatePicker datePicker = null;
        List<ApproveDateItem> dateItems = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker.ApproveDateItem>();
        ApproveDateItem dateItem = null;
        // 搜索狂
        List<ApproveTextBox> listTexTextBoxs = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveTextBox>();
        ApproveTextBox textBox = null;
        // 联系人
        List<ApproveContacts> listContacts = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveContacts>();
        ApproveContacts contacts = null;

        // 列表信息
        List<AppContentListInfo> contentLists = null;
        AppContentListInfo contentList = null;
        String type = "0";
        String detailStyle = "";

        InputStream is = null;
        try {

            HttpPost httpPost = null;
            HttpResponse response = null;
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 120 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);
            DefaultHttpClient httpClient = HttpUtils.initHttpClient(httpParams);
            httpPost = new HttpPost(path);
            httpPost.addHeader("Cookie", WebUtils.cookie);
            httpPost.addHeader("DeviceId", WebUtils.deviceId);
            httpPost.addHeader("EquipType", "Android");
            httpPost.addHeader("EquipSN", WebUtils.deviceId);
            httpPost.addHeader("Soft", WebUtils.packageName);
            httpPost.addHeader("Tel", WebUtils.phoneNumber);
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("key1", key1));
            HttpEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                is = response.getEntity().getContent();
            }

            // System.out.println("----" + new String(LoadUtils.load(is),
            // "utf-8"));
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int event = parser.getEventType();
            String tempName = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                tempName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Root".equalsIgnoreCase(tempName)) {
                            approvalInfo = new NewOAInfo();
                        }
                        // 解析合同审批列表
                        else if ("SearchControl".equalsIgnoreCase(tempName)) {
                            searchInfo = approvalInfo.new ApprovalSearchInfo();
                            approvalInfo.setSearchInfo(searchInfo);
                            approvalInfo.setAutoSearch(parser.getAttributeValue(
                                    null, "AutoSearch"));
                            searchInfo.setListDropDownLists(listDropDownLists);
                            searchInfo.setListDatePickers(datePickers);
                            searchInfo.setListContacts(listContacts);
                            searchInfo.setLisTextBoxs(listTexTextBoxs);
                        } else if ("DropDownList".equalsIgnoreCase(tempName)) {
                            bool_dropDown = true;
                            dropDownList = searchInfo.new ApprovaldropDownList();
                            listDropDownLists.add(dropDownList);
                            dropDownList.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            dropDownList.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            dropDownList.setDefaultValue(parser.getAttributeValue(
                                    null, "DefaultValue"));
                            dropDownList.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                            listItems = new ArrayList<String[]>();
                            dropDownList.setListItems(listItems);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_dropDown) {
                            String[] arr = new String[2];
                            arr[0] = parser.getAttributeValue(null, "key");
                            arr[1] = parser.nextText();
                            listItems.add(arr);
                        } else if ("DatePicker".equalsIgnoreCase(tempName)) {
                            datePicker = searchInfo.new ApproveDatePicker();
                            datePickers.add(datePicker);
                            datePicker.setType(parser.getAttributeValue(null,
                                    "Type"));
                            dateItems = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker.ApproveDateItem>();
                            datePicker.setDateItems(dateItems);
                        } else if ("DateItem".equalsIgnoreCase(tempName)) {
                            dateItem = datePicker.new ApproveDateItem();
                            dateItems.add(dateItem);
                            dateItem.setType(datePicker.getType());
                            dateItem.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            dateItem.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            dateItem.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            dateItem.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("TextBox".equalsIgnoreCase(tempName)) {
                            textBox = searchInfo.new ApproveTextBox();
                            listTexTextBoxs.add(textBox);
                            textBox.setTitle(parser
                                    .getAttributeValue(null, "Title"));
                            textBox.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            textBox.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            textBox.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("Contacts".equalsIgnoreCase(tempName)) {
                            contacts = searchInfo.new ApproveContacts();
                            listContacts.add(contacts);
                            contacts.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            contacts.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            contacts.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            contacts.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("Source".equalsIgnoreCase(tempName)) {
                            contacts.setSourse(parser.nextText());
                        } else if ("ArticleList".equalsIgnoreCase(tempName)) {
                            contentLists = new ArrayList<AppContentListInfo>();
                            approvalInfo.setContentListInfos(contentLists);
                            detailStyle = parser.getAttributeValue(null,
                                    "DetailStyle");
                            type = parser.getAttributeValue(null, "Type");
                        } else if ("ListItem".equalsIgnoreCase(tempName)) {
                            contentList = approvalInfo.new AppContentListInfo();
                            contentLists.add(contentList);
                            contentList.setDetailStyle(detailStyle);
                            contentList.setType(type);
                        } else if ("ItemID".equalsIgnoreCase(tempName)) {
                            contentList.setItemID(parser.nextText());
                        } else if ("Title".equalsIgnoreCase(tempName)) {
                            contentList.setTitle(parser.nextText());
                        } else if ("SubTitle".equalsIgnoreCase(tempName)) {
                            contentList.setSubTitle(parser.nextText());
                        } else if ("Addition".equalsIgnoreCase(tempName)) {
                            contentList.setAddition(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("DropDownList".equalsIgnoreCase(tempName)) {
                            bool_dropDown = false;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();

            }
            return approvalInfo;
        } catch (Exception e) {
            e.printStackTrace();
            approvalInfo = null;

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
        return approvalInfo;
    }

    // 获取审批下列表数据
    public static NewOAInfo getApprovalListData(String path) {

        NewOAInfo approvalInfo = null;

        // 搜索
        ApprovalSearchInfo searchInfo = null;
        // 下拉列表
        List<ApprovaldropDownList> listDropDownLists = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApprovaldropDownList>();
        ApprovaldropDownList dropDownList = null;
        List<String[]> listItems = new ArrayList<String[]>();
        boolean bool_dropDown = false;
        // 日期
        List<ApproveDatePicker> datePickers = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker>();
        ApproveDatePicker datePicker = null;
        String dateType = "";
        List<ApproveDateItem> dateItems = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker.ApproveDateItem>();
        ApproveDateItem dateItem = null;
        // 搜索狂
        List<ApproveTextBox> listTexTextBoxs = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveTextBox>();
        ApproveTextBox textBox = null;
        // 联系人
        List<ApproveContacts> listContacts = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveContacts>();
        ApproveContacts contacts = null;

        // 列表信息
        List<AppContentListInfo> contentLists = null;
        AppContentListInfo contentList = null;
        String type = "0";
        String detailStyle = "";

        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            Log.i(TAG, "getPendingCompleteData2:" + path);
            setHeader(httpPost);

            HttpResponse response = httpClient.execute(httpPost);
            is = response.getEntity().getContent();
            // System.out.println(new String(LoadUtils.load(is),"utf-8"));
            // 这里获取的是本地的数据
            // is =
            // DataCollectionUtils.class.getClassLoader().getResourceAsStream("approval_XML.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int event = parser.getEventType();
            String tempName = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                tempName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Root".equalsIgnoreCase(tempName)) {
                            approvalInfo = new NewOAInfo();
                        }
                        // 解析合同审批列表
                        else if ("SearchControl".equalsIgnoreCase(tempName)) {
                            searchInfo = approvalInfo.new ApprovalSearchInfo();
                            approvalInfo.setSearchInfo(searchInfo);
                            approvalInfo.setAutoSearch(parser.getAttributeValue(
                                    null, "AutoSearch"));
                            searchInfo.setListDropDownLists(listDropDownLists);
                            searchInfo.setListDatePickers(datePickers);
                            searchInfo.setListContacts(listContacts);
                            searchInfo.setLisTextBoxs(listTexTextBoxs);
                        } else if ("DropDownList".equalsIgnoreCase(tempName)) {
                            bool_dropDown = true;
                            dropDownList = searchInfo.new ApprovaldropDownList();
                            listDropDownLists.add(dropDownList);
                            dropDownList.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            dropDownList.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            dropDownList.setDefaultValue(parser.getAttributeValue(
                                    null, "DefaultValue"));
                            dropDownList.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                            listItems = new ArrayList<String[]>();
                            dropDownList.setListItems(listItems);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_dropDown) {
                            String[] arr = new String[2];
                            arr[0] = parser.getAttributeValue(null, "key");
                            arr[1] = parser.nextText();
                            listItems.add(arr);
                        } else if ("DatePicker".equalsIgnoreCase(tempName)) {
                            datePicker = searchInfo.new ApproveDatePicker();
                            datePickers.add(datePicker);
                            dateType = parser.getAttributeValue(null, "Type");
                            datePicker.setType(dateType);
                            dateItems = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker.ApproveDateItem>();
                            datePicker.setDateItems(dateItems);
                        } else if ("DateItem".equalsIgnoreCase(tempName)) {
                            dateItem = datePicker.new ApproveDateItem();
                            dateItems.add(dateItem);
                            dateItem.setType(dateType);
                            dateItem.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            dateItem.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            dateItem.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            dateItem.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("TextBox".equalsIgnoreCase(tempName)) {
                            textBox = searchInfo.new ApproveTextBox();
                            listTexTextBoxs.add(textBox);
                            textBox.setTitle(parser
                                    .getAttributeValue(null, "Title"));
                            textBox.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            textBox.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            textBox.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("Contacts".equalsIgnoreCase(tempName)) {
                            contacts = searchInfo.new ApproveContacts();
                            listContacts.add(contacts);
                            contacts.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            contacts.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            contacts.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            contacts.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("Source".equalsIgnoreCase(tempName)) {
                            contacts.setSourse(parser.nextText());
                        } else if ("ArticleList".equalsIgnoreCase(tempName)) {
                            contentLists = new ArrayList<AppContentListInfo>();
                            approvalInfo.setContentListInfos(contentLists);
                            detailStyle = parser.getAttributeValue(null,
                                    "DetailStyle");
                            type = parser.getAttributeValue(null, "Type");
                        } else if ("ListItem".equalsIgnoreCase(tempName)) {
                            contentList = approvalInfo.new AppContentListInfo();
                            contentLists.add(contentList);
                            contentList.setDetailStyle(detailStyle);
                            contentList.setType(type);
                        } else if ("ItemID".equalsIgnoreCase(tempName)) {
                            contentList.setItemID(parser.nextText());
                        } else if ("Title".equalsIgnoreCase(tempName)) {
                            contentList.setTitle(parser.nextText());
                        } else if ("SubTitle".equalsIgnoreCase(tempName)) {
                            contentList.setSubTitle(parser.nextText());
                        } else if ("Addition".equalsIgnoreCase(tempName)) {
                            contentList.setAddition(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("DropDownList".equalsIgnoreCase(tempName)) {
                            bool_dropDown = false;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();

            }
            return approvalInfo;
        } catch (Exception e) {
            e.printStackTrace();
            approvalInfo = null;

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
        return approvalInfo;
    }

    // 审批提交
    public static String[] approvalSubmit(String path, String key1) {
        InputStream is = null;
        String[] arr = new String[2];
        try {

            HttpPost httpPost = null;
            HttpResponse response = null;
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 120 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);
            DefaultHttpClient httpClient = HttpUtils.initHttpClient(httpParams);
            httpPost = new HttpPost(path);
            httpPost.addHeader("Cookie", WebUtils.cookie);
            httpPost.addHeader("DeviceId", WebUtils.deviceId);
            httpPost.addHeader("EquipType", "Android");
            httpPost.addHeader("EquipSN", WebUtils.deviceId);
            httpPost.addHeader("Soft", WebUtils.packageName);
            httpPost.addHeader("Tel", WebUtils.phoneNumber);
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("key1", key1));
            HttpEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                is = response.getEntity().getContent();
            }
            // String s = new String(LoadUtils.load(is), "utf-8");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String tempName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Result".equalsIgnoreCase(tempName)) {
                            arr[0] = parser.nextText();
                        } else if ("Message".equalsIgnoreCase(tempName)) {
                            arr[1] = parser.nextText();
                        }
                        break;
                    default:
                        break;
                }

                event = parser.next();
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
    }

    // 下载合同审批下的部门
/*	public static List<NewOAChooseDepartmentInfo> ChooseDepartmentForNewOA(
			String path) throws Exception {
		System.out.println("部门地址为：" + path);

		List<NewOAChooseDepartmentInfo> lists = new ArrayList<NewOAChooseDepartmentInfo>();
		NewOAChooseDepartmentInfo info = new NewOAChooseDepartmentInfo();

		HttpParams httpParameters = new BasicHttpParams();
		DefaultHttpClient httpClient = HttpUtils.initHttpClient(httpParameters);
		HttpGet httpGet = new HttpGet(path);
		setHeader(httpGet);
		HttpResponse httpResponse = httpClient.execute(httpGet);

		if (httpResponse != null
				&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			InputStream is = httpResponse.getEntity().getContent();
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(is, "utf-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName(); // 节点名称
				switch (event) {
				case XmlPullParser.START_TAG:
					if ("Department".equalsIgnoreCase(name)) {
						info = new NewOAChooseDepartmentInfo();
						info.setParentid(parser.getAttributeValue(null,
								"ParentId"));
						info.setHasPersons(parser.getAttributeValue(null,
								"HasPersons"));
						info.setSubSector(parser.getAttributeValue(null,
								"SubSector"));
						info.setName(parser.getAttributeValue(null, "Name"));
						info.setId(parser.getAttributeValue(null, "Id"));
						lists.add(info);
					}
					break;

				default:
					break;
				}
				event = parser.next();
			}
		}
		return lists;
	}
*/
    // 下载合同审批下的人员
//	public static List<NewOAChooseUserInfo> ChooseUserForNewOA(String path)
//			throws Exception {
//
//		List<NewOAChooseUserInfo> lists = new ArrayList<NewOAChooseUserInfo>();
//		NewOAChooseUserInfo info = new NewOAChooseUserInfo();
//		HttpParams httpParameters = new BasicHttpParams();
//		DefaultHttpClient httpClient = HttpUtils.initHttpClient(httpParameters);
//		HttpGet httpGet = new HttpGet(path);
//		setHeader(httpGet);
//		HttpResponse httpResponse = httpClient.execute(httpGet);
//
//		if (httpResponse != null
//				&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//			InputStream is = httpResponse.getEntity().getContent();
//			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//			XmlPullParser parser = factory.newPullParser();
//			parser.setInput(is, "utf-8");
//			int event = parser.getEventType();
//			while (event != XmlPullParser.END_DOCUMENT) {
//				String name = parser.getName();
//				switch (event) {
//				case XmlPullParser.START_TAG:
//					if ("User".equalsIgnoreCase(name)) {
//						info = new NewOAChooseUserInfo();
//						info.setId(parser.getAttributeValue(null, "Id") + "");
//						info.setFullName(parser.getAttributeValue(null,
//								"FullName") + "");
//						info.setAltName(parser.getAttributeValue(null,
//								"AltName") + "");
//						info.setMobile(parser.getAttributeValue(null, "Mobile")
//								+ "");
//						info.setOnline(parser.getAttributeValue(null, "Online")
//								+ "");
//						info.setCompany(parser.getAttributeValue(null,
//								"Company") + "");
//						info.setDepartment(parser.getAttributeValue(null,
//								"Department") + "");
//						info.setOfficer(parser.getAttributeValue(null,
//								"Officer") + "");
//						info.setPhone(parser.getAttributeValue(null, "Phone")
//								+ "");
//						info.setEmail(parser.getAttributeValue(null, "Email")
//								+ "");
//						info.setFax(parser.getAttributeValue(null, "Fax") + "");
//						lists.add(info);
//					}
//					break;
//
//				default:
//					break;
//				}
//				event = parser.next();
//			}
//		}
//		return lists;
//	}

    /**
     * 运行监控，tab数据下载
     */
    // public static List<MonitorTabInfo> getMonitorTabDatas(String path) {
    // NewMonitorInfo info = new NewMonitorInfo();
    // List<MonitorTabInfo> tabInfos = new
    // ArrayList<NewMonitorInfo.MonitorTabInfo>();
    // MonitorTabInfo tabInfo = info.new MonitorTabInfo();
    // try {
    // HttpParams httpParameters = new BasicHttpParams();DefaultHttpClient
    // httpClient = HttpUtils.initHttpClient(httpParameters);
    // HttpGet httpGet = new HttpGet(path);
    // httpGet.setHeader("EquipType", "Android");
    // httpGet.setHeader("EquipSN", WebUtils.deviceId);
    // httpGet.setHeader("Soft", WebUtils.packageName);
    // httpGet.setHeader("Tel", WebUtils.phoneNumber);
    // httpGet.setHeader("Cookie", WebUtils.cookie);
    // HttpResponse httpResponse = httpClient.execute(httpGet);
    //
    // if (httpResponse != null && httpResponse.getStatusLine().getStatusCode()
    // == HttpStatus.SC_OK) {
    // InputStream is = httpResponse.getEntity().getContent();
    // XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    // XmlPullParser parser = factory.newPullParser();
    // parser.setInput(is, "utf-8");
    // int event = parser.getEventType();
    // while (event != XmlPullParser.END_DOCUMENT) {
    // String name = parser.getName();
    // switch (event) {
    // case XmlPullParser.START_TAG:
    // if ("list".equalsIgnoreCase(name)) {
    // tabInfo = info.new MonitorTabInfo();
    // tabInfos.add(tabInfo);
    // } else if ("item".equalsIgnoreCase(name)) {
    // tabInfo.setName(getNotNullValue(parser.getAttributeValue(null, "Name")));
    // tabInfo.setPageID(getNotNullValue(parser.getAttributeValue(null,
    // "PageID")));
    // }
    // break;
    //
    // default:
    // break;
    // }
    // event = parser.next();
    // }
    // }
    // return tabInfos;
    // } catch (Exception e) {
    // // TODO: handle exception
    // e.printStackTrace();
    // return new ArrayList<NewMonitorInfo.MonitorTabInfo>();
    // }
    // }

    /**
     * 运行监控，tab数据下载
     */
    // public static MonitorContentInfo getMonitorConDatas(String path) {
    // NewMonitorInfo info = new NewMonitorInfo();
    // MonitorContentInfo contentInfo = null;
    // try {
    // HttpParams httpParameters = new BasicHttpParams();DefaultHttpClient
    // httpClient = HttpUtils.initHttpClient(httpParameters);
    // HttpGet httpGet = new HttpGet(path);
    // httpGet.setHeader("EquipType", "Android");
    // httpGet.setHeader("EquipSN", WebUtils.deviceId);
    // httpGet.setHeader("Soft", WebUtils.packageName);
    // httpGet.setHeader("Tel", WebUtils.phoneNumber);
    // httpGet.setHeader("Cookie", WebUtils.cookie);
    // HttpResponse httpResponse = httpClient.execute(httpGet);
    //
    // if (httpResponse != null && httpResponse.getStatusLine().getStatusCode()
    // == HttpStatus.SC_OK) {
    // InputStream is = httpResponse.getEntity().getContent();
    // XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    // XmlPullParser parser = factory.newPullParser();
    // parser.setInput(is, "utf-8");
    // int event = parser.getEventType();
    // while (event != XmlPullParser.END_DOCUMENT) {
    // String name = parser.getName();
    // switch (event) {
    // case XmlPullParser.START_TAG:
    // if ("Paging".equalsIgnoreCase(name)) {
    // contentInfo = info.new MonitorContentInfo();
    // contentInfo.setDisplay(getNotNullValue(parser.getAttributeValue(null,
    // "display")));
    // } else if ("Total".equalsIgnoreCase(name)) {
    // contentInfo.setTotal(getNotNullValue(parser.nextText()));
    // } else if ("PageCont".equalsIgnoreCase(name)) {
    // contentInfo.setPageCont(getNotNullValue(parser.nextText()));
    // } else if ("CurrentPage".equalsIgnoreCase(name)) {
    // contentInfo.setCurrentPage(getNotNullValue(parser.nextText()));
    // } else if ("EachPageAmount".equalsIgnoreCase(name)) {
    // contentInfo.setEachPageAmount(getNotNullValue(parser.nextText()));
    // } else if ("Content".equalsIgnoreCase(name)) {
    // contentInfo.setContent(getNotNullValue(parser.nextText()));
    // }
    // break;
    //
    // default:
    // break;
    // }
    // event = parser.next();
    // }
    // }
    // return contentInfo;
    // } catch (Exception e) {
    // // TODO: handle exception
    // e.printStackTrace();
    // return null;
    // }
    // }

    // 对获取的属性值做非空处理
    public static String getNotNullValue(String value) {
        if (value == null) {
            value = "";
        }
        return value;
    }

    public static List<String> receiverHelpUrl(String path) {
        List<String> docs = new ArrayList<String>();
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            // String url = WebUtils.rootUrl +
            // URLUtils.wireless_getWirelessStatus + "?meetingid=" + meetId +
            // "&deviceid=" + WebUtils.deviceId;
            HttpGet httpGet = new HttpGet(path);
            setHeader(httpGet);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (200 == httpResponse.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                is = httpResponse.getEntity().getContent();
                // String resultData = StreamUtils.retrieveContent(is);
                // Log.i(TAG, "resultData:" + resultData);
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                String eleName = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            eleName = parser.getName();
                            if ("doc".equalsIgnoreCase(eleName)) {
                                docs.add(parser.getAttributeValue(null, "Url"));
                            }
                            break;
                    }
                    eventType = parser.next();
                }
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
        return docs;
    }

    // ///////////////////////////// added by wangst

    /**
     * 获取审批标签和搜索条件
     *
     * @param path
     */
    public static List<ApproveTab> receiveListTab(String path) {
        InputStream is = null;
        List<ApproveTab> approveTabs = new ArrayList<ApproveTab>();
        ApproveTab approveTab = null;
        UIDropOption option = null;
        UITextInput input = null;
        UIDropSelect select = null;
        UIDropOrderBy orderBy = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve condition data:" + path);
            boolean inSelect = false;
            boolean inOrderBy = false;

            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("ProgID",
                    WebUtils.packageName));
            nameValuePairs.add(new BasicNameValuePair("DeviceType", "3"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println("is:"+StreamUtils.retrieveContent(is));

            // local data
            // is = DataCollectionUtils.class.getClassLoader()
            // .getResourceAsStream("sinopec_approve_tabs.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Tab".equalsIgnoreCase(parser.getName())) {
                            approveTab = new ApproveTab();
                            approveTabs.add(approveTab);
                            approveTab.title = parser.getAttributeValue(null,
                                    "title");
                            approveTab.id = parser.getAttributeValue(null, "id");
                        } else if ("select".equalsIgnoreCase(parser.getName())) {
                            inSelect = true;
                            select = approveTab.searchCondition.new UIDropSelect();
                            approveTab.searchCondition.selects.add(select);
                            select.name = parser.getAttributeValue(null, "name");
                            select.required = parser.getAttributeValue(null,
                                    "required");
                            select.title = parser.getAttributeValue(null, "title");
                        } else if ("OrderBy".equalsIgnoreCase(parser.getName())) {
                            inOrderBy = true;
                            orderBy = approveTab.searchCondition.new UIDropOrderBy();
                            approveTab.searchCondition.orderBys.add(orderBy);
                            orderBy.name = parser.getAttributeValue(null, "name");
                            orderBy.required = parser.getAttributeValue(null,
                                    "required");
                            orderBy.title = parser.getAttributeValue(null, "title");
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inSelect) {
                            option = approveTab.searchCondition.new UIDropOption();
                            select.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.checked = parser.getAttributeValue(null,
                                    "checked");
                            option.name = parser.nextText();
                        } else if ("option".equalsIgnoreCase(parser.getName())
                                && inOrderBy) {
                            option = approveTab.searchCondition.new UIDropOption();
                            orderBy.options.add(option);
                            option.value = parser.getAttributeValue(null, "value");
                            option.checked = parser.getAttributeValue(null,
                                    "checked");
                            option.name = parser.nextText();
                        } else if ("input".equalsIgnoreCase(parser.getName())) {
                            input = approveTab.searchCondition.new UITextInput();
                            approveTab.searchCondition.inputs.add(input);
                            input.type = parser.getAttributeValue(null, "type");
                            input.name = parser.getAttributeValue(null, "name");
                            input.required = parser.getAttributeValue(null,
                                    "required");
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("select".equalsIgnoreCase(parser.getName())) {
                            inSelect = false;
                        } else if ("OrderBy".equalsIgnoreCase(parser.getName())) {
                            inOrderBy = false;
                        }
                        break;
                }
                event = parser.next();
            }
            return approveTabs;

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
        return approveTabs;
    }

    /**
     * 列表组件列表 itemId,rownumber,APPROVE_PAGE_SIZE,s, "", type
     */
//	public static SinopecListTask receiverMobileNewList(String path,
//			String temId, int rownumber, int pageSize, String search,
//			String parentItemId, String tabId, Context mContext) {
//		SinopecListTask sinopecListTask = new SinopecListTask();
//		InputStream is = null;
//		UIDate uiDate = null;
//		UIDropSelect uiDropSelect = null;
//		UIDropOption uiOption = null;
//		UITextInput input = null;
//		// List<List<MobileNewLieb>> allList = new
//		// ArrayList<List<MobileNewLieb>>();
//		ArrayList<MobileNewLieb> imgList = null;
//		ArrayList<MobileNewLieb> itemLists = null;
//		MobileNewLieb items = null;
//		try {
//			// HttpParams httpParameters = new
//			// BasicHttpParams();DefaultHttpClient httpClient =
//			// HttpUtils.initHttpClient(httpParameters);
//			HttpParams httpParameters = new BasicHttpParams();
//			DefaultHttpClient httpClient = HttpUtils
//					.initHttpClient(httpParameters);
//			HttpPost httpPost = new HttpPost(path);
//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//			Log.i(TAG, path + "==temId==" + temId + "==rownumber=" + rownumber
//					+ "==tabId===" + tabId + "=pageSize=" + pageSize
//					+ " search:" + search);
//			// nameValuePairs.add(new BasicNameValuePair("pageid", newLeibId));
//			nameValuePairs.add(new BasicNameValuePair("key1", temId + ""));
//			nameValuePairs.add(new BasicNameValuePair("key2", rownumber + ""));
//			nameValuePairs.add(new BasicNameValuePair("key3", pageSize + ""));
//			nameValuePairs.add(new BasicNameValuePair("key4", search + ""));
//			nameValuePairs
//					.add(new BasicNameValuePair("key5", parentItemId + ""));
//			nameValuePairs.add(new BasicNameValuePair("key6", tabId + ""));
//			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//			setHeader(httpPost);
//			HttpResponse response = httpClient.execute(httpPost);
//			HttpEntity entity = response.getEntity();
//			is = entity.getContent();
//			// if(!"".equals(parentItemId)){
//			// System.out.println(StreamUtils.retrieveContent(is));
//			// }
//			// System.out.println("list is:"+StreamUtils.retrieveContent(is));
//			// is =
//			// DataCollectionUtils.class.getClassLoader().getResourceAsStream("yansan_list.xml");
//			boolean issearch = false;
//			boolean isList = false;
//			boolean isimage = false;
//			boolean b = true;
//			itemLists = new ArrayList<MobileNewLieb>();
//			imgList = new ArrayList<MobileNewLieb>();
//
//			sinopecListTask.mobileNewLiebList.add(itemLists);
//			sinopecListTask.mobileNewLiebList.add(imgList);
//
//			XmlPullParser parser = Xml.newPullParser();
//			parser.setInput(is, "UTF-8");
//			int event = parser.getEventType();
//			while (event != XmlPullParser.END_DOCUMENT) {
//				switch (event) {
//				case XmlPullParser.START_TAG:
//					if ("list".equalsIgnoreCase(parser.getName())) {
//						isList = true;
//					} else if ("search".equalsIgnoreCase(parser.getName())) {
//						issearch = true;
//					} else if ("date".equalsIgnoreCase(parser.getName())
//							&& issearch) {
//						uiDate = sinopecListTask.searchCondition.new UIDate();
//						uiDate.dateName = parser
//								.getAttributeValue(null, "name");
//						uiDate.dateTitle = parser.getAttributeValue(null,
//								"title");
//						uiDate.dateType = parser
//								.getAttributeValue(null, "type");
//						uiDate.dateValue = parser.getAttributeValue(null,
//								"value");
//						System.out.println("http response data name :"
//								+ uiDate.dateName + " datevalue:"
//								+ uiDate.dateValue);
//						sinopecListTask.searchCondition.dates.add(uiDate);
//					} else if ("select".equalsIgnoreCase(parser.getName())
//							&& issearch) {
//						uiDropSelect = sinopecListTask.searchCondition.new UIDropSelect();
//						uiDropSelect.name = parser.getAttributeValue(null,
//								"name");
//						uiDropSelect.title = parser.getAttributeValue(null,
//								"title");
//						uiDropSelect.value = parser.getAttributeValue(null,
//								"value");
//						uiDropSelect.required = parser.getAttributeValue(null,
//								"required");
//						uiDropSelect.alt = parser
//								.getAttributeValue(null, "alt");
//						sinopecListTask.searchCondition.selects
//								.add(uiDropSelect);
//					} else if ("option".equalsIgnoreCase(parser.getName())) {
//						uiOption = sinopecListTask.searchCondition.new UIDropOption();
//						uiDropSelect.options.add(uiOption);
//						uiOption.value = parser
//								.getAttributeValue(null, "value");
//						uiOption.checked = parser.getAttributeValue(null,
//								"checked");
//						uiOption.name = parser.nextText();
//					} else if ("input".equalsIgnoreCase(parser.getName())) {
//						input = sinopecListTask.searchCondition.new UITextInput();
//						sinopecListTask.searchCondition.inputs.add(input);
//						input.title = parser.getAttributeValue(null, "title");
//						input.type = parser.getAttributeValue(null, "type");
//						input.checked = parser.getAttributeValue(null,
//								"checked");
//						input.name = parser.getAttributeValue(null, "name");
//						input.required = parser.getAttributeValue(null,
//								"required");
//						input.value = parser.getAttributeValue(null, "value");
//					} else if ("imageview".equalsIgnoreCase(parser.getName())) {
//						isimage = true;
//					} else if ("item".equalsIgnoreCase(parser.getName())
//							&& isimage) {
//						items = new MobileNewLieb();
//						imgList.add(items);
//					} else if ("item".equalsIgnoreCase(parser.getName())
//							&& isList) {
//						items = new MobileNewLieb();
//
//						boolean level = Boolean.parseBoolean(parser
//								.getAttributeValue(null, "level"));
//						items.level = level;
//						items.type = parser.getAttributeValue(null, "type");
//						itemLists.add(items);
//					} else if ("id".equalsIgnoreCase(parser.getName())) {
//						String id = parser.nextText();
//						items.setId(id);
//						items.setFlag(b);
//					} else if ("title".equalsIgnoreCase(parser.getName())) {
//						event = parser.next();
//						if (event == XmlPullParser.TEXT) {
//							String title = parser.getText();
//							items.setTitle(title);
//						}
//					} else if ("date".equalsIgnoreCase(parser.getName())
//							&& !issearch) {// modified
//						String date = parser.nextText();
//						items.setDate(date);
//					} else if ("author".equalsIgnoreCase(parser.getName())) {
//						String author = parser.nextText();
//						items.setAuthor(author);
//					} else if ("img".equalsIgnoreCase(parser.getName())) {
//						String img = parser.nextText();
//						items.setImg(img);
//					} else if ("summary".equalsIgnoreCase(parser.getName())) {
//						String summary = parser.nextText();
//						items.setSummary(summary);
//					} else if ("file".equalsIgnoreCase(parser.getName())) {
//						items.fileUrl = parser.nextText();
//					} else if ("video".equalsIgnoreCase(parser.getName())) {
//						items.videoUrl = parser.nextText();
//					} else if ("socketvideo".equalsIgnoreCase(parser.getName())) {
//						items.socketIp = parser.getAttributeValue(null, "ip");
//						items.socketPort = parser.getAttributeValue(null,
//								"port");
//					}
//					break;
//				case XmlPullParser.END_TAG:
//					if ("search".equalsIgnoreCase(parser.getName())) {
//						issearch = false;
//					} else if ("item".equalsIgnoreCase(parser.getName())) {
//						items = null;
//					} else if ("list".equalsIgnoreCase(parser.getName())) {
//						isList = false;
//					} else if ("imageview".equalsIgnoreCase(parser.getName())) {
//						isimage = false;
//					}
//					break;
//				}
//				b = !b;
//				event = parser.next();
//			}
//		} catch (ConnectTimeoutException e) {
//			Toast.makeText(mContext, R.string.no_available_net, 0).show();
//			e.printStackTrace();
//		} catch (Exception e) {
//			if (Constants.DEBUG)
//				e.printStackTrace();
//		} finally {
//			if (is != null) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					if (Constants.DEBUG)
//						e.printStackTrace();
//				}
//			}
//		}
//		return sinopecListTask;
//	}

    /**
     * 获取新列表详细
     *
     * @param path
     */
	/*public static NoticeDetail getNoticeDetailNewList(String path,
			String searchXml) {
		InputStream is = null;
		try {
			// HttpParams httpParameters = new
			// BasicHttpParams();DefaultHttpClient httpClient =
			// HttpUtils.initHttpClient(httpParameters);
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(path);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("pageid",
					MobileOfficeListDetailNewFragment.listPathpage));
			nameValuePairs.add(new BasicNameValuePair("key1",
					MobileOfficeListDetailNewFragment.id));
			// nameValuePairs.add(new BasicNameValuePair("key2", searchXml));
			nameValuePairs.add(new BasicNameValuePair("SCode",
					Constants.testPackage));
			nameValuePairs.add(new BasicNameValuePair("key3",
					MobileOfficeListDetailNewFragment.tabId));
			nameValuePairs.add(new BasicNameValuePair("key4",
					MobileOfficeListDetailNewFragment.imgType));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			Log.v("item.id", MobileOfficeListDetailNewFragment.id);
			Log.v("searchXml", searchXml);
			Log.v("tabId", MobileOfficeListDetailNewFragment.tabId);
			setHeader(httpPost);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			// is =
			// DataCollectionUtils.class.getClassLoader().getResourceAsStream("yansan_list_detail.xml");
			// System.out.println("detail is:"+StreamUtils.retrieveContent(is));
			UIDate uiDate = null;
			NoticeDetail noticeDetail = new NoticeDetail();
			List<Attachment> attachments = null;
			List<MeetingPdf> imgUrls = null;
			MeetingPdf pdf = null;
			Attachment attachment = null;
			boolean viewFlag = false;
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if ("url".equalsIgnoreCase(parser.getName())) {// viewType
																	// webview
						noticeDetail.layout = parser.getAttributeValue(null,
								"layout");
						noticeDetail.viewType = "url";
						noticeDetail.url = parser.nextText();
					} else if ("file".equalsIgnoreCase(parser.getName())
							&& !viewFlag) {// viewType
											// file
						noticeDetail.viewType = "file";
						noticeDetail.fileUrl = parser.nextText();
					} else if ("Video".equalsIgnoreCase(parser.getName())) {// viewType
																			// Video
						noticeDetail.viewType = "Video";
						noticeDetail.videoUrl = parser.nextText();
					} else if ("view".equalsIgnoreCase(parser.getName())) {// viewType
																			// webview
						noticeDetail.viewType = "view";
						viewFlag = true;
					} else if ("title".equalsIgnoreCase(parser.getName())
							&& viewFlag) {
						noticeDetail.setTitle(parser.nextText());
					} else if ("date".equalsIgnoreCase(parser.getName())
							&& viewFlag) {
						noticeDetail.setPublishDate(parser.nextText());
					} else if ("author".equalsIgnoreCase(parser.getName())
							&& viewFlag) {
						noticeDetail.setAuthor(parser.nextText());
					} else if ("content".equalsIgnoreCase(parser.getName())
							&& viewFlag) {
						noticeDetail.setContent(parser.getAttributeValue(null,
								"type"));
						String newContent = parser.nextText();
						noticeDetail.setNewContent(newContent);
					} else if ("Files".equalsIgnoreCase(parser.getName())
							&& viewFlag) {// 附件列表
						attachments = new ArrayList<Attachment>();
						noticeDetail.attachment_list = attachments;
					} else if ("File".equalsIgnoreCase(parser.getName())
							&& viewFlag) {// 附件
						attachment = new Attachment();
						attachment.url = parser.getAttributeValue(null, "url");
						attachment.title = parser.nextText();
						attachments.add(attachment);
					} else if ("date".equalsIgnoreCase(parser.getName())) {// 时间查询组建
						uiDate = noticeDetail.searchCondition.new UIDate();
						uiDate.dateName = parser
								.getAttributeValue(null, "name");
						uiDate.dateTitle = parser.getAttributeValue(null,
								"title");
						uiDate.dateType = parser
								.getAttributeValue(null, "type");
						uiDate.dateValue = parser.getAttributeValue(null,
								"value");
						noticeDetail.searchCondition.dates.add(uiDate);
					}
					break;
				case XmlPullParser.END_TAG:
					if ("view".equalsIgnoreCase(parser.getName())) {
						viewFlag = false;
					}
				}
				event = parser.next();
			}
			return noticeDetail;

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
		return null;
	}*/

    // ///////////报表2.0移植
    /**
     * 一次性获取报表内容
     *
     * @return
     */
//	public static List<BIitems> retrieveBiGroupAndPages(String path) {
//		List<BIitems> groupedOResults = new ArrayList<BIitems>();
//		BIitems bIitem = null;
//		ChartItem ci = null; // 每个缩略图对应一个ChartItem
//		ChartItem.ChartItemFilter cf = null;
//		// String groupurl = WebUtils.rootUrl +
//		// "/ForIpad/Mobile_BI/PredefinedGroup.aspx"; // 预定义
//		InputStream in = null;
//		try {
//			DefaultHttpClient httpClient = HttpUtils.getWrappedSSLHttpClient();
//			HttpGet request = new HttpGet(path);
//			setHeader(request);
//			HttpResponse response = httpClient.execute(request);
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				in = response.getEntity().getContent();
//				// String sdString=StreamUtils.retrieveContent(in);
//				// System.out.println(sdString);
//				// in = DataCollectionUtils.class.getClassLoader()
//				// .getResourceAsStream("chartdemo.xml");
//
//				// 使用PULL解析器解析数据
//				XmlPullParser parser = Xml.newPullParser();
//				parser.setInput(in, "UTF-8");
//				int eventType = parser.getEventType();
//				while (eventType != XmlPullParser.END_DOCUMENT) {
//					if (eventType == XmlPullParser.START_TAG) {
//						if ("Group".equalsIgnoreCase(parser.getName())) {
//							bIitem = new BIitems();
//							String grouptype = parser.getAttributeValue(3);
//							String groupItem = parser.getAttributeValue(2);
//							bIitem.setId(parser.getAttributeValue(0));
//							bIitem.setTitle(groupItem);
//							bIitem.setGrouptype(grouptype);
//							if (parser.getAttributeCount() > 1) {
//								String imageUrl = parser.getAttributeValue(1);
//								bIitem.setImageUrl(imageUrl);
//							}
//							System.out.println("item toString:"
//									+ bIitem.getImageUrl());
//							groupedOResults.add(bIitem);
//						} else if ("Page".equals(parser.getName())) {
//							ci = new ChartItem();
//							bIitem.chartItems.add(ci);
//							ci.chartBgUrl = parser.getAttributeValue(null,
//									"BgImg");
//							ci.tnUrl = parser.getAttributeValue(null,
//									"Thumbnail");
//
//						} else if ("Title".equalsIgnoreCase(parser.getName())) {
//							ci.chartTitle = parser.getAttributeValue(null,
//									"Name");
//						} else if ("Uri".equalsIgnoreCase(parser.getName())) {
//							ci.chartUrls.add(parser.nextText().trim());
//						} else if ("ParaItem"
//								.equalsIgnoreCase(parser.getName())) {
//							String filterKey = parser.getAttributeValue(null,
//									"Key");
//							String filterTitle = parser.getAttributeValue(null,
//									"Title");
//							String filterType = parser.getAttributeValue(null,
//									"ParaType");
//							String filterDefaultValue = parser
//									.getAttributeValue(null, "DefaultValue");
//							cf = ci.new ChartItemFilter();
//							ci.chartItemFilters.add(cf);
//							cf.filterKey = filterKey;
//							cf.filterTitle = filterTitle;
//							cf.filterType = filterType;
//							cf.defaultValue = filterDefaultValue;
//						} else if ("Item".equalsIgnoreCase(parser.getName())) {
//							String fvalue = parser.getAttributeValue(null,
//									"Key");
//							String fkey = parser.nextText();
//							cf.filterItem.put(fkey, fvalue);
//						} else if ("Info".equalsIgnoreCase(parser.getName())) {
//							ci.chartCategory = parser.getAttributeValue(null,
//									"Info");
//						}
//					}
//					eventType = parser.next();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return groupedOResults;
//	}

    /**
     * 单页列表组件列表
     */
    public static List<GroupedEvent> receiverMobileSingleList(String path) {
        InputStream is = null;
        List<GroupedEvent> events = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            // nameValuePairs.add(new BasicNameValuePair("pageid", newLeibId));
            // nameValuePairs.add(new BasicNameValuePair("key1", temId + ""));
            // nameValuePairs.add(new BasicNameValuePair("key2", rownumber +
            // ""));
            // nameValuePairs.add(new BasicNameValuePair("key3", pageSize +
            // ""));
            // nameValuePairs.add(new BasicNameValuePair("key4", search + ""));
            // nameValuePairs.add(new BasicNameValuePair("key5", parentItemId +
            // ""));
            // nameValuePairs.add(new BasicNameValuePair("key6", tabId + ""));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            setHeader(httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println(StreamUtils.retrieveContent(is));
            // is =
            // DataCollectionUtils.class.getClassLoader().getResourceAsStream("yansan_single_list_temp.xml");

            try {
                XmlPullParser parser = Xml.newPullParser();
                // 设置解析的数据源
                parser.setInput(is, "UTF-8");
                // 取得事件类型
                int eventType = parser.getEventType();
                GroupedEvent event = null;
                String eleName = null;
                SubGroupedEvent sgevent = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        // 元素开始
                        case XmlPullParser.START_TAG:
                            eleName = parser.getName();
                            if ("List".equalsIgnoreCase(eleName)) {
                                events = new ArrayList<GroupedEvent>();
                            } else if ("Item".equalsIgnoreCase(eleName)) {
                                event = new GroupedEvent();
                                if (events != null)
                                    events.add(event);
                            } else if ("Title".equalsIgnoreCase(eleName)) {
                                event.title = parser.nextText().trim();
                            } else if ("Field1".equalsIgnoreCase(eleName)) {// added
                                event.Field1 = parser.nextText().trim();
                            } else if ("Field2".equalsIgnoreCase(eleName)) {
                                event.Field2 = parser.nextText().trim();
                            } else if ("Field3".equalsIgnoreCase(eleName)) {// added
                                event.Field3 = parser.nextText().trim();
                            } else if ("content".equalsIgnoreCase(eleName)) {
                                sgevent = event.new SubGroupedEvent();
                                sgevent.text = parser.nextText().trim();
                                event.sges.add(sgevent);
                            }
                            // else if ("DateTime".equalsIgnoreCase(eleName)) {
                            // event.dateTime = parser.nextText().trim();
                            // } else if ("Location".equalsIgnoreCase(eleName)) {
                            // event.location = parser.nextText().trim();
                            // } else if ("Row".equalsIgnoreCase(eleName)) {
                            // sgevent = event.new SubGroupedEvent();
                            // event.sges.add(sgevent);
                            // } else if ("Label".equalsIgnoreCase(eleName)) {
                            // sgevent.label = parser.nextText().trim();
                            // } else if ("Text".equalsIgnoreCase(eleName)) {
                            // sgevent.text = parser.nextText().trim();
                            // }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return events;

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
        return events;
    }

    // /////////newspaper

    /**
     * 手机报 NewsPaperMain
     */
    public static LinkedHashMap<String, List<NewsPaperMain>> receiverNewsPaperShelfData(
            String path, String name) {
        // path = WebUtils.rootUrl + URLUtils.mobileOfficeNewslistMain;
        LinkedHashMap<String, List<NewsPaperMain>> newsPapers = new LinkedHashMap<String, List<NewsPaperMain>>();
        List<NewsPaperMain> news = null;
        NewsPaperMain newsPaper = null;
        if (!"".equals(name)) {
            path = path + "&key1=" + name;
        }
        if (DataCache.newsPaperShelf == null
                || DataCache.newsPaperShelf.size() == 0) {
            InputStream is = null;
            try {
                HttpParams httpParameters = new BasicHttpParams();
                DefaultHttpClient httpClient = HttpUtils
                        .initHttpClient(httpParameters);
                HttpPost httpPost = new HttpPost(path);// +"&key1=2014"
                if (Constants.DEBUG) {
                    Log.i(TAG, "begin load mobile paper data:" + path);
                }
                setHeader(httpPost);
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                // System.out.println(StreamUtils.retrieveContent(is));
                if (200 == response.getStatusLine().getStatusCode()) {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(is, "UTF-8");
                    int event = parser.getEventType();
                    while (event != XmlPullParser.END_DOCUMENT) {
                        switch (event) {
                            case XmlPullParser.START_TAG:
                                if ("item".equalsIgnoreCase(parser.getName())) {
                                    news = new ArrayList<NewsPaperMain>();
                                    if (newsPapers.get(parser.getAttributeValue(0)) == null
                                            || newsPapers.get(
                                            parser.getAttributeValue(0))
                                            .size() == 0) {
                                        newsPapers.put(parser.getAttributeValue(0),
                                                news);
                                    }
                                    // DataCache.papers.put(parser.getAttributeValue(0),
                                    // news);
                                    // System.out.println("nespaper item:"+parser.getAttributeName(0));
                                } else if ("element".equalsIgnoreCase(parser
                                        .getName())) {
                                    newsPaper = new NewsPaperMain();
                                    news.add(newsPaper);
                                } else if ("id".equalsIgnoreCase(parser.getName())) {
                                    newsPaper.id = parser.nextText();
                                } else if ("nextUrl".equalsIgnoreCase(parser
                                        .getName())) {
                                    newsPaper.nextUrl = parser.nextText();
                                } else if ("Name"
                                        .equalsIgnoreCase(parser.getName())) {
                                    newsPaper.name = parser.nextText();
                                } else if ("subName".equalsIgnoreCase(parser
                                        .getName())) {
                                    newsPaper.subName = parser.nextText();
                                } else if ("period".equalsIgnoreCase(parser
                                        .getName())) {
                                    newsPaper.periodType = parser
                                            .getAttributeValue(null, "type");// added
                                    newsPaper.period = parser.nextText();
                                } else if ("date"
                                        .equalsIgnoreCase(parser.getName())) {
                                    newsPaper.date = parser.nextText();
                                } else if ("imgurl".equalsIgnoreCase(parser
                                        .getName())) {
                                    newsPaper.imgUrl = parser.nextText();
                                } else if ("pdf".equalsIgnoreCase(parser.getName())) {
                                    newsPaper.pdfUrl = parser.nextText();
                                }
                                break;
                            default:
                                break;
                        }
                        event = parser.next();
                    }
                }
                return newsPapers;
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
        }
        return newsPapers;
    }

    /**
     * 手机报 NewsPaperMain
     */
    public static LinkedHashMap<String, List<NewsPaperMain>> receiverNewsPaperShelfData(
            String path) {
        // path = WebUtils.rootUrl + URLUtils.mobileOfficeNewslistMain;
        LinkedHashMap<String, List<NewsPaperMain>> papers = new LinkedHashMap<String, List<NewsPaperMain>>();
        List<NewsPaperMain> news = null;
        NewsPaperMain newsPaper = null;
        if (DataCache.newsPaperShelf == null
                || DataCache.newsPaperShelf.size() == 0) {
            InputStream is = null;
            try {
                HttpParams httpParameters = new BasicHttpParams();
                DefaultHttpClient httpClient = HttpUtils
                        .initHttpClient(httpParameters);
                HttpPost httpPost = new HttpPost(path);
                if (Constants.DEBUG) {
                    Log.i(TAG, "begin load mobile paper data:" + path);
                }
                httpPost.setHeader("EquipType", "Android");
                httpPost.setHeader("EquipSN", WebUtils.deviceId);
                httpPost.setHeader("Soft", WebUtils.packageName);
                httpPost.setHeader("Tel", WebUtils.phoneNumber);
                httpPost.setHeader("Cookie", WebUtils.cookie);
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                if (200 == response.getStatusLine().getStatusCode()) {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(is, "UTF-8");
                    int event = parser.getEventType();
                    while (event != XmlPullParser.END_DOCUMENT) {
                        switch (event) {
                            case XmlPullParser.START_TAG:
                                if ("item".equalsIgnoreCase(parser.getName())) {
                                    news = new ArrayList<NewsPaperMain>();
                                    papers.put(parser.getAttributeValue(0), news);
                                } else if ("element".equalsIgnoreCase(parser
                                        .getName())) {
                                    newsPaper = new NewsPaperMain();
                                    news.add(newsPaper);
                                } else if ("id".equalsIgnoreCase(parser.getName())) {
                                    newsPaper.id = parser.nextText();
                                } else if ("nextUrl".equalsIgnoreCase(parser
                                        .getName())) {
                                    newsPaper.nextUrl = parser.nextText();
                                } else if ("Name"
                                        .equalsIgnoreCase(parser.getName())) {
                                    newsPaper.name = parser.nextText();
                                } else if ("subName".equalsIgnoreCase(parser
                                        .getName())) {
                                    newsPaper.subName = parser.nextText();
                                } else if ("period".equalsIgnoreCase(parser
                                        .getName())) {
                                    newsPaper.period = parser.nextText();
                                } else if ("date"
                                        .equalsIgnoreCase(parser.getName())) {
                                    newsPaper.date = parser.nextText();
                                } else if ("imgurl".equalsIgnoreCase(parser
                                        .getName())) {
                                    newsPaper.imgUrl = parser.nextText();
                                } else if ("pdf".equalsIgnoreCase(parser.getName())) {
                                    newsPaper.pdfUrl = parser.nextText();
                                }
                                break;
                            default:
                                break;
                        }
                        event = parser.next();
                    }
                }
                return papers;
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
        }
        return papers;
    }

    /**
     * 手机报 NewsPaperList
     */
    public static LinkedHashMap<String, List<NewsPaper>> receiverNewsPaperListData(
            String path) {
        LinkedHashMap<String, List<NewsPaper>> papers = new LinkedHashMap<String, List<NewsPaper>>();
        List<NewsPaper> news = null;
        NewsPaper newsPaper = null;
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            Log.i(TAG, "receiverNewsPaperListData:" + path);
            setHeader(httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println("list:"+StreamUtils.retrieveContent(is));
            if (200 == response.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");
                int event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if ("item".equalsIgnoreCase(parser.getName())) {
                                news = new ArrayList<NewsPaper>();
                                papers.put(parser.getAttributeValue(0), news);
                            } else if ("element".equalsIgnoreCase(parser.getName())) {
                                newsPaper = new NewsPaper();
                                news.add(newsPaper);
                            } else if ("id".equalsIgnoreCase(parser.getName())) {
                                newsPaper.id = parser.nextText();
                            } else if ("nextUrl".equalsIgnoreCase(parser.getName())) {
                                newsPaper.detailUrl = parser.nextText();
                            } else if ("Title".equalsIgnoreCase(parser.getName())) {
                                newsPaper.title = parser.nextText();
                            } else if ("date".equalsIgnoreCase(parser.getName())) {
                                newsPaper.date = parser.nextText();
                            }
                            break;
                        default:
                            break;
                    }
                    event = parser.next();
                }
            }

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
        return papers;
    }

	/*public static IntegrateDetail getIntegrateDetail(InputStream is)
			throws Exception {
		IntegrateDetail integrateDetail = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("Title".equalsIgnoreCase(parser.getName())) {
					integrateDetail = new IntegrateDetail();
					integrateDetail.title = parser.nextText();
				} else if ("Content".equalsIgnoreCase(parser.getName())) {
					// integrateDetail.content = parser.nextText();
				} else if ("html".equalsIgnoreCase(parser.getName())) {
					integrateDetail.content = parser.nextText();
				}
				break;
			}
			eventType = parser.next();
		}
		return integrateDetail;
	}*/

    public static List<TypeItem> retrieveTypeItems(InputStream in) {
        List<TypeItem> typeItem = new ArrayList<TypeItem>();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("WorkType".equals(parser.getName())) {
                        TypeItem item = new TypeItem();
                        item.id = parser.getAttributeValue(null, "type");
                        item.name = parser.getAttributeValue(null, "text");
                        typeItem.add(item);
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeItem;
    }

    public static ArrayList<UnionOffice> retrieveUnionOfficeContentsDocuments(
            String uopath, String groupName, String type, String date,
            String cookie, final UnionOfficeAdapter uoa) {
        ArrayList<UnionOffice> contents = new ArrayList<UnionOffice>();
        InputStream is;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpGet httpGet = new HttpGet(uopath + "&key1=" + groupName
                    + "&key2=" + type + "&key3=" + date);
            // HttpPost httpPost = new HttpPost(uopath);
            // List<NameValuePair> nameValuePairs = new
            // ArrayList<NameValuePair>();
            // nameValuePairs.add(new BasicNameValuePair("key1", groupName));
            // nameValuePairs.add(new BasicNameValuePair("key2", type));
            // nameValuePairs.add(new BasicNameValuePair("key3", date));
            // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
            // "UTF-8"));
            // setHeader(httpPost);
            System.out.println("get path:" + uopath + "&key1=" + groupName
                    + "&key2=" + type + "&key3=" + date);
            setHeader(httpGet);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // byte[] data1 = LoadUtils.load(is);
            // System.out.println("new String(data, charset): " + new
            // String(data1, "UTF-8"));
            boolean inGroup = false;
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("MenuItem".equals(parser.getName())) {
                        String item = parser.getAttributeValue(0);
                        if (item != null && item.equals(groupName)) {
                            inGroup = true;
                        }
                    } else if ("Block".equals(parser.getName()) && inGroup) {
                        final UnionOffice uo = new UnionOffice();
                        uo.title = parser.getAttributeValue(null, "Caption");
                        String turl = parser.getAttributeValue(null, "Url");
                        // uo.url = uopath + "19&prm1="
                        // + URLEncoder.encode(turl, "UTF-8");
                        // uo.url = URLDecoder.decode(turl, "UTF-8");
                        // uo.url = turl;
                        uo.url = URLUtils.pageDetail + "&key1="
                                + URLEncoder.encode(turl, "UTF-8");
                        System.out.println("item url:" + uo.url);
                        contents.add(uo);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("MenuItem".equals(parser.getName())) {
                        if (inGroup) {
                            inGroup = false;
                            break;
                        }
                    }
                }
                eventType = parser.next();
            }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }

    public static UnionOffice checkWorkDetailLogic(String time, String usrname,
                                                   String type, String cookie) {
        UnionOffice contents = new UnionOffice();
        String uourl = URLUtils.IsCheckSerach + "&key1=" + time + "&key2="
                + usrname + "&key3=" + type;
        System.out.println("uourl:" + uourl);
        try {
            // 使用PULL解析器解析数据
            URL u = new URL(uourl);
            URLConnection uc = u.openConnection();
            uc.setRequestProperty("cookie", cookie);
            uc.setRequestProperty("EquipType", "Android");
            uc.setRequestProperty("EquipSN", WebUtils.deviceId);
            uc.setRequestProperty("Soft", WebUtils.packageName);
            uc.setRequestProperty("Tel", WebUtils.phoneNumber);
            InputStream in = uc.getInputStream();
            // byte[] data1 = LoadUtils.load(in);
            // System.out.println("new String(data, charset): " + new
            // String(data1, "UTF-8"));
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("TopMenu".equals(parser.getName())) {
                    } else if ("Fid".equals(parser.getName())) {
                        contents.msgId = parser.nextText();
                    } else if ("ContentDetail".equals(parser.getName())) {
                        contents.content = parser.nextText();
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                }
                eventType = parser.next();
            }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }

    public static String sendNewDocuments(String uopath, String time,
                                          String username, String type, String content, String cookie,
                                          String fid) {
        String result = null;
        String pageID = "";
        // if ("001002005007".equals(type) || "001002005003".equals(type)
        // || "001002005002".equals(type) || "001002005001".equals(type)) {
        // pageID = "147";//小结
        // } else if ("001002004006".equals(type) || "001002004003".equals(type)
        // || "001002004002".equals(type)) {
        // pageID = "148";//计划
        // }
        // String uourl = uopath + pageID + "&key1=" + time + "&key2=" +
        // username
        // + "&key3=" + type+ "&key4=" + content+ "&key5=" + fid;
        String uourl = uopath + "&key1=" + time + "&key2=" + username
                + "&key3=" + type + "&key4=" + content + "&key5=" + fid;
        System.out.println("uourl=>" + uourl);
        try {
            // 使用PULL解析器解析数据
            URL u = new URL(uourl);
            URLConnection uc = u.openConnection();
            uc.setRequestProperty("cookie", cookie);
            uc.setRequestProperty("EquipType", "Android");
            uc.setRequestProperty("EquipSN", WebUtils.deviceId);
            uc.setRequestProperty("Soft", WebUtils.packageName);
            uc.setRequestProperty("Tel", WebUtils.phoneNumber);
            InputStream in = uc.getInputStream();
            // byte[] data1 = LoadUtils.load(in);
            // System.out.println("new String(data, charset): " + new
            // String(data1, "UTF-8"));
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if ("TopMenu".equals(parser.getName())) {
                    } else if ("Result".equals(parser.getName())) {
                        result = parser.nextText();
                    }
                }
                eventType = parser.next();
            }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param path
     */

    public static String addCommonAdviceData(String path, String key1) {

        InputStream is = null;
        boolean submitFlag = false;
        String flage = "0";
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", key1));
            nameValuePairs.add(new BasicNameValuePair("key2", key1));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("result".equalsIgnoreCase(parser.getName())) {
                            String result = parser.nextText();
                            if ("1".equalsIgnoreCase(result)) {
                                // submitFlag = true;
                                flage = result;
                            }
                        } else if ("message".equalsIgnoreCase(parser.getName())) {
                            // entry.message = parser.nextText();
                        }

                        break;
                }
                event = parser.next();
            }
            return flage;

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
        return null;
    }

    public static Integer receiveApproveNotification(String path) {
        InputStream is = null;
        int notification = 0;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            // List<NameValuePair> nameValuePairs = new
            // ArrayList<NameValuePair>();
            // nameValuePairs.add(new BasicNameValuePair("key1", key1));
            // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
            // "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("TotalCount".equalsIgnoreCase(parser.getName())) {
                            notification = Integer.valueOf(parser.nextText());
                        }
                        break;
                }
                event = parser.next();
            }
            return notification;

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
        return notification;

    }

    public static Integer getMobileOANumber(String path) {
        InputStream is = null;
        int notification = 0;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            // List<NameValuePair> nameValuePairs = new
            // ArrayList<NameValuePair>();
            // nameValuePairs.add(new BasicNameValuePair("key1", key1));
            // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
            // "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("TotalCount".equalsIgnoreCase(parser.getName())) {
                            notification = Integer.valueOf(parser.nextText());
                        }
                        break;
                }
                event = parser.next();
            }
            return notification;

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
        return notification;

    }

    /**
     * 审批详细信息内容(已办)
     */
    public static NewOADetailInfo getCompletedApprovalDetail(String path,
                                                             String key1, String key2) {
        NewOADetailInfo approvalDetailInfo = null;
        // 表格
        boolean bool_tableRow = false;
        ApprovalDetailTableInfo tableInfo = null;
        List<String> approvalDetailTableTitles = null;
        List<List<String>> tableRows = null;
        List<String> rowItems = null;

        // 合同流程节点
        ApprovalProcessInfo processInfo = null;
        List<ProcessSection> sections = null;
        ProcessSection sectionInfo = null;
        boolean bool_section = false;
        List<ProcessGroupList> groupLists = null;
        ProcessGroupList groupList = null;
        List<ProcessGroup> groups = null;
        ProcessGroup group = null;
        String groupName = null; // 组的名称
        List<ProcessGroupItem> groupItems = null;
        ProcessGroupItem groupItem = null;

        // 开始表格了，注意下
        ProcessGroupItemTableInfo groupItemTableInfo = null;
        List<String> showInList = new ArrayList<String>();
        List<String> headerList = new ArrayList<String>();
        List<List<String[]>> contentList = new ArrayList<List<String[]>>();
        List<String[]> contentRowList = new ArrayList<String[]>();

        InputStream is = null;
        try {

            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);

            Log.i(TAG, "getPendingCompleteData2:" + path);
            setHeader(httpPost);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("key1", key1));
            pairs.add(new BasicNameValuePair("key2", key2));
            HttpEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            is = response.getEntity().getContent();

            // System.out.println("---"+new String(LoadUtils.load(is),"utf-8"));

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int event = parser.getEventType();
            String tempName = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                tempName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Root".equalsIgnoreCase(tempName)) {
                            approvalDetailInfo = new NewOADetailInfo();
                        }
                        /**
                         * 解析表格
                         */
                        else if ("Table".equalsIgnoreCase(tempName)
                                && !bool_section) {
                            bool_tableRow = true;
                            tableInfo = approvalDetailInfo.new ApprovalDetailTableInfo();
                            approvalDetailInfo.setTableInfo(tableInfo);
                        } else if ("Header".equalsIgnoreCase(tempName)
                                && bool_tableRow && !bool_section) {
                            approvalDetailTableTitles = new ArrayList<String>();
                            tableInfo.setLists(approvalDetailTableTitles);
                        } else if ("Column".equalsIgnoreCase(tempName)
                                && bool_tableRow && !bool_section) {
                            approvalDetailTableTitles.add(parser.nextText() + "");
                        } else if ("Content".equalsIgnoreCase(tempName)
                                && bool_tableRow && !bool_section) {
                            tableRows = new ArrayList<List<String>>();
                            tableInfo.setListRows(tableRows);
                        } else if ("Row".equalsIgnoreCase(tempName)
                                && bool_tableRow && !bool_section) {
                            rowItems = new ArrayList<String>();
                            tableRows.add(rowItems);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_tableRow && !bool_section) {
                            rowItems.add(parser.nextText() + "");
                        }

                        /**
                         * 合同流程节点
                         */
                        else if ("Process".equalsIgnoreCase(tempName)) {
                            processInfo = approvalDetailInfo.new ApprovalProcessInfo();
                            sections = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection>();
                            processInfo.setSections(sections);
                            approvalDetailInfo.setProcessInfo(processInfo);
                        } else if ("Section".equalsIgnoreCase(tempName)) {
                            bool_section = true;
                            sectionInfo = processInfo.new ProcessSection();
                            sections.add(sectionInfo);
                            sectionInfo.setState(parser.getAttributeValue(null,
                                    "State") + "");
                            groupLists = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList>();
                            sectionInfo.setGroupLists(groupLists);
                        } else if ("Caption".equalsIgnoreCase(tempName)
                                && bool_section) {
                            sectionInfo.setCaption(parser.nextText() + "");
                        } else if ("GroupList".equalsIgnoreCase(tempName)
                                && bool_section) {
                            groupList = sectionInfo.new ProcessGroupList();
                            groupLists.add(groupList);
                            groups = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup>();
                            groupList.setGroups(groups);
                        } else if ("Group".equalsIgnoreCase(tempName)
                                && bool_section) {
                            group = groupList.new ProcessGroup();
                            groupName = parser.getAttributeValue(null, "Name") + "";
                            group.setName(groupName);
                            group.setExpand(parser
                                    .getAttributeValue(null, "Expand"));
                            groupItems = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup.ProcessGroupItem>();
                            group.setGroupItems(groupItems);
                            groups.add(group);
                        } else if ("Label".equalsIgnoreCase(tempName)
                                && bool_section) {
                            groupItem = group.new ProcessGroupItem();
                            groupItem.setLabel(parser.nextText() + "");
                            groupItems.add(groupItem);
                        } else if ("Text".equalsIgnoreCase(tempName)
                                && bool_section) {
                            groupItem.setType(parser
                                    .getAttributeValue(null, "Type") + "");
                            groupItem.setpCode(parser.getAttributeValue(null,
                                    "PCode") + "");
                            groupItem.setText(parser.nextText() + "");
                        }
                        // 表格里面的数据，这个需要注意下
                        else if ("Table".equalsIgnoreCase(tempName) && bool_section) {
                            groupItem = group.new ProcessGroupItem();
                            groupItem.setIfTableData(true);
                            groupItems.add(groupItem);
                            groupItemTableInfo = groupItem.new ProcessGroupItemTableInfo();
                            groupItem.setTableInfo(groupItemTableInfo);
                        } else if ("Header".equalsIgnoreCase(tempName)
                                && bool_section) {
                            showInList = new ArrayList<String>();
                            headerList = new ArrayList<String>();
                            groupItemTableInfo.setShowInlList(showInList);
                            groupItemTableInfo.setHeaderList(headerList);
                        } else if ("Column".equalsIgnoreCase(tempName)
                                && bool_section) {
                            String showInValue = parser.getAttributeValue(null,
                                    "ShowInList");
                            if (showInValue == null) {
                                showInList.add("0");
                            } else {
                                showInList.add(showInValue);
                            }
                            String columnValue = parser.nextText();
                            if (columnValue == null) {
                                headerList.add("");
                            } else {
                                headerList.add(columnValue);
                            }
                        } else if ("Content".equalsIgnoreCase(tempName)
                                && bool_section) {
                            contentList = new ArrayList<List<String[]>>();
                            groupItemTableInfo.setContentList(contentList);
                        } else if ("Items".equalsIgnoreCase(tempName)
                                && bool_section) {
                            contentRowList = new ArrayList<String[]>();
                            contentList.add(contentRowList);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_section) {
                            String[] arr = new String[2];
                            if (parser.getAttributeValue(null, "Type") == null) {
                                arr[0] = "";
                            } else {
                                arr[0] = parser.getAttributeValue(null, "Type");
                            }

                            String contentRowValue = parser.nextText();
                            if (contentRowValue == null) {
                                arr[1] = "";
                            } else {
                                arr[1] = contentRowValue;
                            }
                            contentRowList.add(arr);
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("Table".equalsIgnoreCase(tempName)) {
                            bool_tableRow = false;
                        } else if ("Section".equalsIgnoreCase(tempName)) {
                            bool_section = false;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();

            }
            return approvalDetailInfo;
        } catch (Exception e) {
            e.printStackTrace();
            approvalDetailInfo = null;

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
        return approvalDetailInfo;

    }

    // ----------------------------------新模块
    // 合同审批2.0------------------------------------------

    /**
     * 审批详细信息内容(待办)
     *
     * @param path      路径
     * @param parameter 这个参数表示二级界面传过来的值，如果不是二级界面请求数据，传空字符串，由于这个值的出现，当前这个方法不只是待办用的，
     *                  只要有二级页面的，都会用到这个方法
     * @return
     */
    public static NewOADetailInfo getPeddingApprovalDetail(String path,
                                                           String key1, String key2) {
        // // String key1 = null;
        // // if (!parameter.equals("")) {
        // // String param1 = parameter.substring(0, parameter.indexOf("="));
        // // String value1 = parameter.substring(parameter.indexOf("=") + 1,
        // parameter.indexOf("&"));
        // // String param2 = parameter.substring(parameter.indexOf("&") + 1,
        // parameter.lastIndexOf("="));
        // // String value2 = parameter.substring(parameter.lastIndexOf("=") +
        // 1);
        //
        // StringBuilder sb = new StringBuilder();
        // sb.append("<Root>");
        // sb.append("<SubmitItem>");
        // sb.append("<Item>");
        // sb.append("<ParaName>" + param1 + "</ParaName>");
        // sb.append("<Value>" + value1 + "</Value>");
        // sb.append("</Item>");
        // sb.append("<Item>");
        // sb.append("<ParaName>" + param2 + "</ParaName>");
        // sb.append("<Value>" + value2 + "</Value>");
        // sb.append("</Item>");
        // sb.append("</SubmitItem>");
        // sb.append("</Root>");
        //
        // key1 = sb.toString();
        // key1 = key1.replace("<", "&lt;");
        // key1 = key1.replace(">", "&gt;");
        // }

        NewOADetailInfo approvalDetailInfo = new NewOADetailInfo();
        // 表格
        boolean bool_tableRow = false;
        ApprovalDetailTableInfo tableInfo = null;
        List<String> approvalDetailTableTitles = null;
        List<List<String>> tableRows = null;
        List<String> rowItems = null;

        // 合同流程节点
        boolean bool_section = false;
        ApprovalProcessInfo processInfo = null;
        List<ProcessSection> sections = null;
        ProcessSection sectionInfo = null;
        List<ProcessGroupList> groupLists = null;
        ProcessGroupList groupList = null;
        List<ProcessGroup> groups = null;
        ProcessGroup group = null;
        String groupName = null; // 组的名称
        List<ProcessGroupItem> groupItems = null;
        ProcessGroupItem groupItem = null;

        // 开始表格了，注意下
        ProcessGroupItemTableInfo groupItemTableInfo = null;
        List<String> showInList = new ArrayList<String>();
        List<String> headerList = new ArrayList<String>();
        List<List<String[]>> contentList = new ArrayList<List<String[]>>();
        List<String[]> contentRowList = new ArrayList<String[]>();

        // 操作选项
        SubmitInfo submitInfo = null;
        SubRouters subRouters = null;
        List<SubRouter> routerList = null;
        boolean bool_router = false;
        SubRouter subRouter = null;
        List<SubOption> optionList = null;
        SubOption subOption = null;
        List<SubItem> subItems = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption.SubItem>();
        SubItem subItem = null;
        SubTextView subTextView = null;

        SubTextBoard subTextBoard = null;
        boolean bool_textBoard = false;
        List<TextBoardItem> itemList = null;
        TextBoardItem boardItem = null;

        InputStream is = null;
        try {

            HttpPost httpPost = null;
            HttpResponse response = null;
            HttpEntity entity = null;
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 120 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);
            DefaultHttpClient httpClient = HttpUtils.initHttpClient(httpParams);
            httpPost = new HttpPost(path);
            httpPost.addHeader("Cookie", WebUtils.cookie);
            httpPost.addHeader("DeviceId", WebUtils.deviceId);
            httpPost.addHeader("EquipType", "Android");
            httpPost.addHeader("EquipSN", WebUtils.deviceId);
            httpPost.addHeader("Soft", WebUtils.packageName);
            httpPost.addHeader("Tel", WebUtils.phoneNumber);
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");

            // if (!parameter.equals("")) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("key1", key1));
            pairs.add(new BasicNameValuePair("key2", key2));
            entity = new UrlEncodedFormEntity(pairs, "utf-8");
            httpPost.setEntity(entity);
            // }

            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            is = entity.getContent();
            // is =
            // DataCollectionUtils.class.getClassLoader().getResourceAsStream("2.0合同详细.xml");
            // if (!parameter.equals("")) {
            // System.out.println("---" + new String(LoadUtils.load(is),
            // "utf-8"));
            // }
            // int leng = 0;
            // byte[] arr1 = new byte[1024];
            // while ((leng = is.read(arr1)) != 0) {
            // System.out.println(new String(arr1, 0, leng));
            // }
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int event = parser.getEventType();
            String tempName = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                tempName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Message".equalsIgnoreCase(tempName)) {
                            approvalDetailInfo.setMessage(parser.nextText());
                        } else
                        /**
                         * 解析表格
                         */
                            if ("Table".equalsIgnoreCase(tempName) && !bool_section) {
                                bool_tableRow = true;
                                tableInfo = approvalDetailInfo.new ApprovalDetailTableInfo();
                                approvalDetailInfo.setTableInfo(tableInfo);
                            } else if ("Header".equalsIgnoreCase(tempName)
                                    && bool_tableRow) {
                                approvalDetailTableTitles = new ArrayList<String>();
                                tableInfo.setLists(approvalDetailTableTitles);
                            } else if ("Column".equalsIgnoreCase(tempName)
                                    && bool_tableRow) {
                                approvalDetailTableTitles.add(parser.nextText() + "");
                            } else if ("Content".equalsIgnoreCase(tempName)
                                    && bool_tableRow) {
                                tableRows = new ArrayList<List<String>>();
                                tableInfo.setListRows(tableRows);
                            } else if ("Row".equalsIgnoreCase(tempName)
                                    && bool_tableRow) {
                                rowItems = new ArrayList<String>();
                                tableRows.add(rowItems);
                            } else if ("Item".equalsIgnoreCase(tempName)
                                    && bool_tableRow) {
                                rowItems.add(parser.nextText() + "");
                            }

                            /**
                             * 合同流程节点
                             */
                            else if ("Process".equalsIgnoreCase(tempName)) {
                                bool_section = true;
                                processInfo = approvalDetailInfo.new ApprovalProcessInfo();
                                sections = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection>();
                                processInfo.setSections(sections);
                                approvalDetailInfo.setProcessInfo(processInfo);
                            }

                            // 这个节点在待办里面是没有的，但是由于是Xml.newPullParser()解析，他的nextText有个bug，bug我觉得是，只要有节点没有解析的，就可能会出现一些异常，
                            else if ("GroupList".equalsIgnoreCase(tempName)) {
                                sectionInfo = processInfo.new ProcessSection();
                                sections.add(sectionInfo);
                                sectionInfo.setState(parser.getAttributeValue(null,
                                        "State") + "");
                                groupLists = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList>();
                                sectionInfo.setGroupLists(groupLists);
                                sectionInfo.setCaption("Caption");
                                groupList = sectionInfo.new ProcessGroupList();
                                groupLists.add(groupList);
                                groups = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup>();
                                groupList.setGroups(groups);
                            } else if ("Group".equalsIgnoreCase(tempName)) {
                                group = groupList.new ProcessGroup();
                                groupName = parser.getAttributeValue(null, "Name") + "";
                                group.setName(groupName);
                                group.setExpand(parser
                                        .getAttributeValue(null, "Expand") + "");
                                groupItems = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup.ProcessGroupItem>();
                                group.setGroupItems(groupItems);
                                groups.add(group);
                            } else if ("Label".equalsIgnoreCase(tempName)) {
                                groupItem = group.new ProcessGroupItem();
                                groupItem.setLabel(parser.nextText() + "");
                                groupItems.add(groupItem);
                            } else if ("Text".equalsIgnoreCase(tempName)) {
                                groupItem.setType(parser
                                        .getAttributeValue(null, "Type") + "");
                                groupItem.setpCode(parser.getAttributeValue(null,
                                        "PCode") + "");
                                groupItem.setText(parser.nextText());
                            }

                            // 表格里面的数据，这个需要注意下
                            else if ("Table".equalsIgnoreCase(tempName) && bool_section) {
                                groupItem = group.new ProcessGroupItem();
                                groupItem.setIfTableData(true);
                                groupItems.add(groupItem);
                                groupItemTableInfo = groupItem.new ProcessGroupItemTableInfo();
                                groupItem.setTableInfo(groupItemTableInfo);
                            } else if ("Header".equalsIgnoreCase(tempName)
                                    && bool_section) {
                                showInList = new ArrayList<String>();
                                headerList = new ArrayList<String>();
                                groupItemTableInfo.setShowInlList(showInList);
                                groupItemTableInfo.setHeaderList(headerList);
                            } else if ("Column".equalsIgnoreCase(tempName)
                                    && bool_section) {
                                String showInValue = parser.getAttributeValue(null,
                                        "ShowInList");
                                if (showInValue == null) {
                                    showInList.add("0");
                                } else {
                                    showInList.add(showInValue);
                                }
                                String columnValue = parser.nextText();
                                if (columnValue == null) {
                                    headerList.add("");
                                } else {
                                    headerList.add(columnValue);
                                }
                            } else if ("Content".equalsIgnoreCase(tempName)
                                    && bool_section) {
                                contentList = new ArrayList<List<String[]>>();
                                groupItemTableInfo.setContentList(contentList);
                            } else if ("Items".equalsIgnoreCase(tempName)
                                    && bool_section) {
                                contentRowList = new ArrayList<String[]>();
                                contentList.add(contentRowList);
                            } else if ("Item".equalsIgnoreCase(tempName)
                                    && bool_section) {
                                String[] arr = new String[2];
                                if (parser.getAttributeValue(null, "Type") == null) {
                                    arr[0] = "";
                                } else {
                                    arr[0] = parser.getAttributeValue(null, "Type");
                                }

                                String contentRowValue = parser.nextText();
                                if (contentRowValue == null) {
                                    arr[1] = "";
                                } else {
                                    arr[1] = contentRowValue;
                                }
                                contentRowList.add(arr);
                            }

                            /**
                             * 提交
                             */
                            else if ("Submit".equalsIgnoreCase(tempName)) {
                                submitInfo = approvalDetailInfo.new SubmitInfo();
                                approvalDetailInfo.setSubmitInfo(submitInfo);
                            }
                            // routers
                            else if ("Routers".equalsIgnoreCase(tempName)) {
                                subRouters = submitInfo.new SubRouters();
                                submitInfo.setSubRouters(subRouters);
                                subRouters.setParaName(parser.getAttributeValue(null,
                                        "ParaName") + "");
                                subRouters.setTitle(parser.getAttributeValue(null,
                                        "Title") + "");
                                routerList = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter>();
                                subRouters.setRouterList(routerList);
                            } else if ("Router".equalsIgnoreCase(tempName)) {
                                bool_router = true;
                                subRouter = subRouters.new SubRouter();
                                routerList.add(subRouter);
                            } else if ("Key".equalsIgnoreCase(tempName)) {
                                subRouter.setKey(parser.nextText() + "");
                            } else if ("Caption".equalsIgnoreCase(tempName)) {
                                subRouter.setCaption(parser.nextText() + "");
                            } else if ("OperationList".equalsIgnoreCase(tempName)) {
                                optionList = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption>();
                                subRouter.setOptionList(optionList);
                            } else if ("Option".equalsIgnoreCase(tempName)) {
                                subOption = subRouter.new SubOption();
                                optionList.add(subOption);
                                subOption.setMultiple(parser.getAttributeValue(null,
                                        "Multiple") + "");
                                subOption.setParaName(parser.getAttributeValue(null,
                                        "ParaName") + "");
                                subOption.setCaption(parser.getAttributeValue(null,
                                        "Caption") + "");
                                subOption.setType(parser
                                        .getAttributeValue(null, "Type") + "");
                                subOption.setIsChoose(parser.getAttributeValue(null,
                                        "IsChoose") + "");
                                subOption.setRequired(parser.getAttributeValue(null,
                                        "Required") + "");
                            } else if ("Contacts".equalsIgnoreCase(tempName)) {
                                subOption.setAvailable(parser.getAttributeValue(null,
                                        "Available") + "");
                                subOption.setInitOUID(parser.getAttributeValue(null,
                                        "InitOUID") + "");
                                subOption.setSource(parser.getAttributeValue(null,
                                        "Source") + "");
                                subOption.setDisplay(parser.getAttributeValue(null,
                                        "Display") + "");
                            } else if ("Items".equalsIgnoreCase(tempName)
                                    && bool_router) {
                                subItems = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption.SubItem>();
                                subOption.setItems(subItems);
                            } else if ("Item".equalsIgnoreCase(tempName) && bool_router) {
                                subItem = subOption.new SubItem();
                                subItems.add(subItem);
                                subItem.setKey(parser.getAttributeValue(null, "Key")
                                        + "");
                                subItem.setSelected(parser.getAttributeValue(null,
                                        "Selected") + "");
                                subItem.setName(parser.nextText() + "");
                            }
                            // textView
                            else if ("TextView".equalsIgnoreCase(tempName)) {
                                subTextView = subRouter.new SubTextView();
                                subRouter.setSubTextView(subTextView);
                                subTextView.setParaName(parser.getAttributeValue(null,
                                        "ParaName") + "");
                                subTextView.setTitle(parser.getAttributeValue(null,
                                        "Title") + "");
                                subTextView.setDefautValue(parser.getAttributeValue(
                                        null, "DefautValue") + "");
                                subTextView.setResultValue(parser.getAttributeValue(
                                        null, "DefautValue") + "");
                                subTextView.setRequired(parser.getAttributeValue(null,
                                        "Required") + "");
                            } else if ("Locution".equalsIgnoreCase(tempName)) {
                                subTextView.setDisplay(parser.getAttributeValue(null,
                                        "Display") + "");
                                subTextView.setAvailable(parser.getAttributeValue(null,
                                        "Available") + "");
                            }
                            // textBoard
                            else if ("TextBoard".equalsIgnoreCase(tempName)) {
                                bool_textBoard = true;
                                subTextBoard = submitInfo.new SubTextBoard();
                                submitInfo.setSubTextBoard(subTextBoard);
                                subTextBoard.setTitle(parser.getAttributeValue(null,
                                        "Title") + "");
                                itemList = new ArrayList<NewOADetailInfo.SubmitInfo.SubTextBoard.TextBoardItem>();
                                subTextBoard.setItemList(itemList);
                            } else if ("Item".equalsIgnoreCase(tempName)
                                    && bool_textBoard) {
                                boardItem = subTextBoard.new TextBoardItem();
                                boardItem.setTitle(parser.getAttributeValue(null,
                                        "Title") + "");
                                boardItem.setSubtitle(parser.getAttributeValue(null,
                                        "Subtitle") + "");
                                itemList.add(boardItem);
                            }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("Table".equalsIgnoreCase(tempName)) {
                            bool_tableRow = false;
                        } else if ("Router".equalsIgnoreCase(tempName)) {
                            bool_router = false;
                        } else if ("TextBoard".equalsIgnoreCase(tempName)) {
                            bool_textBoard = false;
                        } else if ("Process".equalsIgnoreCase(tempName)) {
                            bool_section = false;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();

            }
            return approvalDetailInfo;
        } catch (Exception e) {
            e.printStackTrace();
            approvalDetailInfo = null;

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
        return approvalDetailInfo;

    }

    public static NewOADetailInfo getPeddingApprovalDetailTwo(String path,
                                                              String parameter) {
        String key1 = null;
        if (!parameter.equals("")) {
            String param1 = parameter.substring(0, parameter.indexOf("="));
            String value1 = parameter.substring(parameter.indexOf("=") + 1,
                    parameter.indexOf("&"));
            String param2 = parameter.substring(parameter.indexOf("&") + 1,
                    parameter.lastIndexOf("="));
            String value2 = parameter.substring(parameter.lastIndexOf("=") + 1);

            StringBuilder sb = new StringBuilder();
            sb.append("<Root>");
            sb.append("<SubmitItem>");
            sb.append("<Item>");
            sb.append("<ParaName>" + param1 + "</ParaName>");
            sb.append("<Value>" + value1 + "</Value>");
            sb.append("</Item>");
            sb.append("<Item>");
            sb.append("<ParaName>" + param2 + "</ParaName>");
            sb.append("<Value>" + value2 + "</Value>");
            sb.append("</Item>");
            sb.append("</SubmitItem>");
            sb.append("</Root>");

            key1 = sb.toString();
            key1 = key1.replace("<", "&lt;");
            key1 = key1.replace(">", "&gt;");
        }

        NewOADetailInfo approvalDetailInfo = new NewOADetailInfo();
        // 表格
        boolean bool_tableRow = false;
        ApprovalDetailTableInfo tableInfo = null;
        List<String> approvalDetailTableTitles = null;
        List<List<String>> tableRows = null;
        List<String> rowItems = null;

        // 合同流程节点
        boolean bool_section = false;
        ApprovalProcessInfo processInfo = null;
        List<ProcessSection> sections = null;
        ProcessSection sectionInfo = null;
        List<ProcessGroupList> groupLists = null;
        ProcessGroupList groupList = null;
        List<ProcessGroup> groups = null;
        ProcessGroup group = null;
        String groupName = null; // 组的名称
        List<ProcessGroupItem> groupItems = null;
        ProcessGroupItem groupItem = null;

        // 开始表格了，注意下
        ProcessGroupItemTableInfo groupItemTableInfo = null;
        List<String> showInList = new ArrayList<String>();
        List<String> headerList = new ArrayList<String>();
        List<List<String[]>> contentList = new ArrayList<List<String[]>>();
        List<String[]> contentRowList = new ArrayList<String[]>();

        // 操作选项
        SubmitInfo submitInfo = null;
        SubRouters subRouters = null;
        List<SubRouter> routerList = null;
        boolean bool_router = false;
        SubRouter subRouter = null;
        List<SubOption> optionList = null;
        SubOption subOption = null;
        List<SubItem> subItems = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption.SubItem>();
        SubItem subItem = null;
        SubTextView subTextView = null;

        SubTextBoard subTextBoard = null;
        boolean bool_textBoard = false;
        List<TextBoardItem> itemList = null;
        TextBoardItem boardItem = null;

        InputStream is = null;
        try {

            HttpPost httpPost = null;
            HttpResponse response = null;
            HttpEntity entity = null;
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 120 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);
            DefaultHttpClient httpClient = HttpUtils.initHttpClient(httpParams);
            httpPost = new HttpPost(path);
            httpPost.addHeader("Cookie", WebUtils.cookie);
            httpPost.addHeader("DeviceId", WebUtils.deviceId);
            httpPost.addHeader("EquipType", "Android");
            httpPost.addHeader("EquipSN", WebUtils.deviceId);
            httpPost.addHeader("Soft", WebUtils.packageName);
            httpPost.addHeader("Tel", WebUtils.phoneNumber);
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");

            if (!parameter.equals("")) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair("key1", key1));
                // pairs.add(new BasicNameValuePair("key2", key2));
                entity = new UrlEncodedFormEntity(pairs, "utf-8");
                httpPost.setEntity(entity);
            }

            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            is = entity.getContent();
            // if (!parameter.equals("")) {
            // System.out.println("---" + new String(LoadUtils.load(is),
            // "utf-8"));
            // }

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int event = parser.getEventType();
            String tempName = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                tempName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        /**
                         * 解析表格
                         */
                        if ("Table".equalsIgnoreCase(tempName) && !bool_section) {
                            bool_tableRow = true;
                            tableInfo = approvalDetailInfo.new ApprovalDetailTableInfo();
                            approvalDetailInfo.setTableInfo(tableInfo);
                        } else if ("Header".equalsIgnoreCase(tempName)
                                && bool_tableRow) {
                            approvalDetailTableTitles = new ArrayList<String>();
                            tableInfo.setLists(approvalDetailTableTitles);
                        } else if ("Column".equalsIgnoreCase(tempName)
                                && bool_tableRow) {
                            approvalDetailTableTitles.add(parser.nextText() + "");
                        } else if ("Content".equalsIgnoreCase(tempName)
                                && bool_tableRow) {
                            tableRows = new ArrayList<List<String>>();
                            tableInfo.setListRows(tableRows);
                        } else if ("Row".equalsIgnoreCase(tempName)
                                && bool_tableRow) {
                            rowItems = new ArrayList<String>();
                            tableRows.add(rowItems);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_tableRow) {
                            rowItems.add(parser.nextText() + "");
                        }

                        /**
                         * 合同流程节点
                         */
                        else if ("Process".equalsIgnoreCase(tempName)) {
                            bool_section = true;
                            processInfo = approvalDetailInfo.new ApprovalProcessInfo();
                            sections = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection>();
                            processInfo.setSections(sections);
                            approvalDetailInfo.setProcessInfo(processInfo);
                        }

                        // 这个节点在待办里面是没有的，但是由于是Xml.newPullParser()解析，他的nextText有个bug，bug我觉得是，只要有节点没有解析的，就可能会出现一些异常，
                        else if ("GroupList".equalsIgnoreCase(tempName)) {
                            sectionInfo = processInfo.new ProcessSection();
                            sections.add(sectionInfo);
                            sectionInfo.setState(parser.getAttributeValue(null,
                                    "State") + "");
                            groupLists = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList>();
                            sectionInfo.setGroupLists(groupLists);
                            sectionInfo.setCaption("Caption");
                            groupList = sectionInfo.new ProcessGroupList();
                            groupLists.add(groupList);
                            groups = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup>();
                            groupList.setGroups(groups);
                        } else if ("Group".equalsIgnoreCase(tempName)) {
                            group = groupList.new ProcessGroup();
                            groupName = parser.getAttributeValue(null, "Name") + "";
                            group.setName(groupName);
                            group.setExpand(parser
                                    .getAttributeValue(null, "Expand") + "");
                            groupItems = new ArrayList<NewOADetailInfo.ApprovalProcessInfo.ProcessSection.ProcessGroupList.ProcessGroup.ProcessGroupItem>();
                            group.setGroupItems(groupItems);
                            groups.add(group);
                        } else if ("Label".equalsIgnoreCase(tempName)) {
                            groupItem = group.new ProcessGroupItem();
                            groupItem.setLabel(parser.nextText() + "");
                            groupItems.add(groupItem);
                        } else if ("Text".equalsIgnoreCase(tempName)) {
                            groupItem.setType(parser
                                    .getAttributeValue(null, "Type") + "");
                            groupItem.setpCode(parser.getAttributeValue(null,
                                    "PCode") + "");
                            groupItem.setText(parser.nextText());
                        }

                        // 表格里面的数据，这个需要注意下
                        else if ("Table".equalsIgnoreCase(tempName) && bool_section) {
                            groupItem = group.new ProcessGroupItem();
                            groupItem.setIfTableData(true);
                            groupItems.add(groupItem);
                            groupItemTableInfo = groupItem.new ProcessGroupItemTableInfo();
                            groupItem.setTableInfo(groupItemTableInfo);
                        } else if ("Header".equalsIgnoreCase(tempName)
                                && bool_section) {
                            showInList = new ArrayList<String>();
                            headerList = new ArrayList<String>();
                            groupItemTableInfo.setShowInlList(showInList);
                            groupItemTableInfo.setHeaderList(headerList);
                        } else if ("Column".equalsIgnoreCase(tempName)
                                && bool_section) {
                            String showInValue = parser.getAttributeValue(null,
                                    "ShowInList");
                            if (showInValue == null) {
                                showInList.add("0");
                            } else {
                                showInList.add(showInValue);
                            }
                            String columnValue = parser.nextText();
                            if (columnValue == null) {
                                headerList.add("");
                            } else {
                                headerList.add(columnValue);
                            }
                        } else if ("Content".equalsIgnoreCase(tempName)
                                && bool_section) {
                            contentList = new ArrayList<List<String[]>>();
                            groupItemTableInfo.setContentList(contentList);
                        } else if ("Items".equalsIgnoreCase(tempName)
                                && bool_section) {
                            contentRowList = new ArrayList<String[]>();
                            contentList.add(contentRowList);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_section) {
                            String[] arr = new String[2];
                            if (parser.getAttributeValue(null, "Type") == null) {
                                arr[0] = "";
                            } else {
                                arr[0] = parser.getAttributeValue(null, "Type");
                            }

                            String contentRowValue = parser.nextText();
                            if (contentRowValue == null) {
                                arr[1] = "";
                            } else {
                                arr[1] = contentRowValue;
                            }
                            contentRowList.add(arr);
                        }

                        /**
                         * 提交
                         */
                        else if ("Submit".equalsIgnoreCase(tempName)) {
                            submitInfo = approvalDetailInfo.new SubmitInfo();
                            approvalDetailInfo.setSubmitInfo(submitInfo);
                        }
                        // routers
                        else if ("Routers".equalsIgnoreCase(tempName)) {
                            subRouters = submitInfo.new SubRouters();
                            submitInfo.setSubRouters(subRouters);
                            subRouters.setParaName(parser.getAttributeValue(null,
                                    "ParaName") + "");
                            subRouters.setTitle(parser.getAttributeValue(null,
                                    "Title") + "");
                            routerList = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter>();
                            subRouters.setRouterList(routerList);
                        } else if ("Router".equalsIgnoreCase(tempName)) {
                            bool_router = true;
                            subRouter = subRouters.new SubRouter();
                            routerList.add(subRouter);
                        } else if ("Key".equalsIgnoreCase(tempName)) {
                            subRouter.setKey(parser.nextText() + "");
                        } else if ("Caption".equalsIgnoreCase(tempName)) {
                            subRouter.setCaption(parser.nextText() + "");
                        } else if ("OperationList".equalsIgnoreCase(tempName)) {
                            optionList = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption>();
                            subRouter.setOptionList(optionList);
                        } else if ("Option".equalsIgnoreCase(tempName)) {
                            subOption = subRouter.new SubOption();
                            optionList.add(subOption);
                            subOption.setMultiple(parser.getAttributeValue(null,
                                    "Multiple") + "");
                            subOption.setParaName(parser.getAttributeValue(null,
                                    "ParaName") + "");
                            subOption.setCaption(parser.getAttributeValue(null,
                                    "Caption") + "");
                            subOption.setType(parser
                                    .getAttributeValue(null, "Type") + "");
                            subOption.setIsChoose(parser.getAttributeValue(null,
                                    "IsChoose") + "");
                            subOption.setRequired(parser.getAttributeValue(null,
                                    "Required") + "");
                        } else if ("Contacts".equalsIgnoreCase(tempName)) {
                            subOption.setAvailable(parser.getAttributeValue(null,
                                    "Available") + "");
                            subOption.setInitOUID(parser.getAttributeValue(null,
                                    "InitOUID") + "");
                            subOption.setSource(parser.getAttributeValue(null,
                                    "Source") + "");
                            subOption.setDisplay(parser.getAttributeValue(null,
                                    "Display") + "");
                        } else if ("Items".equalsIgnoreCase(tempName)
                                && bool_router) {
                            subItems = new ArrayList<NewOADetailInfo.SubmitInfo.SubRouters.SubRouter.SubOption.SubItem>();
                            subOption.setItems(subItems);
                        } else if ("Item".equalsIgnoreCase(tempName) && bool_router) {
                            subItem = subOption.new SubItem();
                            subItems.add(subItem);
                            subItem.setKey(parser.getAttributeValue(null, "Key")
                                    + "");
                            subItem.setSelected(parser.getAttributeValue(null,
                                    "Selected") + "");
                            subItem.setName(parser.nextText() + "");
                        }
                        // textView
                        else if ("TextView".equalsIgnoreCase(tempName)) {
                            subTextView = subRouter.new SubTextView();
                            subRouter.setSubTextView(subTextView);
                            subTextView.setParaName(parser.getAttributeValue(null,
                                    "ParaName") + "");
                            subTextView.setTitle(parser.getAttributeValue(null,
                                    "Title") + "");
                            subTextView.setDefautValue(parser.getAttributeValue(
                                    null, "DefautValue") + "");
                            subTextView.setResultValue(parser.getAttributeValue(
                                    null, "DefautValue") + "");
                            subTextView.setRequired(parser.getAttributeValue(null,
                                    "Required") + "");
                        } else if ("Locution".equalsIgnoreCase(tempName)) {
                            subTextView.setDisplay(parser.getAttributeValue(null,
                                    "Display") + "");
                            subTextView.setAvailable(parser.getAttributeValue(null,
                                    "Available") + "");
                        }
                        // textBoard
                        else if ("TextBoard".equalsIgnoreCase(tempName)) {
                            bool_textBoard = true;
                            subTextBoard = submitInfo.new SubTextBoard();
                            submitInfo.setSubTextBoard(subTextBoard);
                            subTextBoard.setTitle(parser.getAttributeValue(null,
                                    "Title") + "");
                            itemList = new ArrayList<NewOADetailInfo.SubmitInfo.SubTextBoard.TextBoardItem>();
                            subTextBoard.setItemList(itemList);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_textBoard) {
                            boardItem = subTextBoard.new TextBoardItem();
                            boardItem.setTitle(parser.getAttributeValue(null,
                                    "Title") + "");
                            boardItem.setSubtitle(parser.getAttributeValue(null,
                                    "Subtitle") + "");
                            itemList.add(boardItem);
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if ("Table".equalsIgnoreCase(tempName)) {
                            bool_tableRow = false;
                        } else if ("Router".equalsIgnoreCase(tempName)) {
                            bool_router = false;
                        } else if ("TextBoard".equalsIgnoreCase(tempName)) {
                            bool_textBoard = false;
                        } else if ("Process".equalsIgnoreCase(tempName)) {
                            bool_section = false;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();

            }
            return approvalDetailInfo;
        } catch (Exception e) {
            e.printStackTrace();
            approvalDetailInfo = null;

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
        return approvalDetailInfo;

    }

    // 获取审批下列表数据,这个用于查询的，
    public static NewOAInfo getApprovalListData(String path, String key1,
                                                String key2, String none) {

        NewOAInfo approvalInfo = null;

        // 搜索
        ApprovalSearchInfo searchInfo = null;
        // 下拉列表
        List<ApprovaldropDownList> listDropDownLists = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApprovaldropDownList>();
        ApprovaldropDownList dropDownList = null;
        List<String[]> listItems = new ArrayList<String[]>();
        boolean bool_dropDown = false;
        // 日期
        List<ApproveDatePicker> datePickers = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker>();
        ApproveDatePicker datePicker = null;
        List<ApproveDateItem> dateItems = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker.ApproveDateItem>();
        ApproveDateItem dateItem = null;
        // 搜索狂
        List<ApproveTextBox> listTexTextBoxs = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveTextBox>();
        ApproveTextBox textBox = null;
        // 联系人
        List<ApproveContacts> listContacts = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveContacts>();
        ApproveContacts contacts = null;

        // 列表信息
        List<AppContentListInfo> contentLists = null;
        AppContentListInfo contentList = null;
        String type = "0";
        String detailStyle = "";

        InputStream is = null;
        try {

            HttpPost httpPost = null;
            HttpResponse response = null;
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 120 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);
            DefaultHttpClient httpClient = HttpUtils.initHttpClient(httpParams);
            httpPost = new HttpPost(path);
            httpPost.addHeader("Cookie", WebUtils.cookie);
            httpPost.addHeader("DeviceId", WebUtils.deviceId);
            httpPost.addHeader("EquipType", "Android");
            httpPost.addHeader("EquipSN", WebUtils.deviceId);
            httpPost.addHeader("Soft", WebUtils.packageName);
            httpPost.addHeader("Tel", WebUtils.phoneNumber);
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded");

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("key1", key1));
            pairs.add(new BasicNameValuePair("key2", key2));
            HttpEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                is = response.getEntity().getContent();
            }

            // System.out.println("----" + new String(LoadUtils.load(is),
            // "utf-8"));
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int event = parser.getEventType();
            String tempName = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                tempName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Root".equalsIgnoreCase(tempName)) {
                            approvalInfo = new NewOAInfo();
                        }
                        // 解析合同审批列表
                        else if ("SearchControl".equalsIgnoreCase(tempName)) {
                            searchInfo = approvalInfo.new ApprovalSearchInfo();
                            approvalInfo.setSearchInfo(searchInfo);
                            approvalInfo.setAutoSearch(parser.getAttributeValue(
                                    null, "AutoSearch"));
                            searchInfo.setListDropDownLists(listDropDownLists);
                            searchInfo.setListDatePickers(datePickers);
                            searchInfo.setListContacts(listContacts);
                            searchInfo.setLisTextBoxs(listTexTextBoxs);
                        } else if ("DropDownList".equalsIgnoreCase(tempName)) {
                            bool_dropDown = true;
                            dropDownList = searchInfo.new ApprovaldropDownList();
                            listDropDownLists.add(dropDownList);
                            dropDownList.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            dropDownList.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            dropDownList.setDefaultValue(parser.getAttributeValue(
                                    null, "DefaultValue"));
                            dropDownList.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                            listItems = new ArrayList<String[]>();
                            dropDownList.setListItems(listItems);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_dropDown) {
                            String[] arr = new String[2];
                            arr[0] = parser.getAttributeValue(null, "key");
                            arr[1] = parser.nextText();
                            listItems.add(arr);
                        } else if ("DatePicker".equalsIgnoreCase(tempName)) {
                            datePicker = searchInfo.new ApproveDatePicker();
                            datePickers.add(datePicker);
                            datePicker.setType(parser.getAttributeValue(null,
                                    "Type"));
                            dateItems = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker.ApproveDateItem>();
                            datePicker.setDateItems(dateItems);
                        } else if ("DateItem".equalsIgnoreCase(tempName)) {
                            dateItem = datePicker.new ApproveDateItem();
                            dateItems.add(dateItem);
                            dateItem.setType(datePicker.getType());
                            dateItem.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            dateItem.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            dateItem.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            dateItem.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("TextBox".equalsIgnoreCase(tempName)) {
                            textBox = searchInfo.new ApproveTextBox();
                            listTexTextBoxs.add(textBox);
                            textBox.setTitle(parser
                                    .getAttributeValue(null, "Title"));
                            textBox.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            textBox.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            textBox.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("Contacts".equalsIgnoreCase(tempName)) {
                            contacts = searchInfo.new ApproveContacts();
                            listContacts.add(contacts);
                            contacts.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            contacts.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            contacts.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            contacts.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("Source".equalsIgnoreCase(tempName)) {
                            contacts.setSourse(parser.nextText());
                        } else if ("ArticleList".equalsIgnoreCase(tempName)) {
                            contentLists = new ArrayList<AppContentListInfo>();
                            approvalInfo.setContentListInfos(contentLists);
                            detailStyle = parser.getAttributeValue(null,
                                    "DetailStyle");
                            type = parser.getAttributeValue(null, "Type");
                        } else if ("ListItem".equalsIgnoreCase(tempName)) {
                            contentList = approvalInfo.new AppContentListInfo();
                            contentLists.add(contentList);
                            contentList.setDetailStyle(detailStyle);
                            contentList.setType(type);
                        } else if ("ItemID".equalsIgnoreCase(tempName)) {
                            contentList.setItemID(parser.nextText());
                        } else if ("Title".equalsIgnoreCase(tempName)) {
                            contentList.setTitle(parser.nextText());
                        } else if ("SubTitle".equalsIgnoreCase(tempName)) {
                            contentList.setSubTitle(parser.nextText());
                        } else if ("Addition".equalsIgnoreCase(tempName)) {
                            contentList.setAddition(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("DropDownList".equalsIgnoreCase(tempName)) {
                            bool_dropDown = false;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();

            }
            return approvalInfo;
        } catch (Exception e) {
            e.printStackTrace();
            approvalInfo = null;

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
        return approvalInfo;
    }

    // 获取审批下列表数据
    public static NewOAInfo getApprovalListData(String path, String key1,
                                                String key2) {

        NewOAInfo approvalInfo = null;

        // 搜索
        ApprovalSearchInfo searchInfo = null;
        // 下拉列表
        List<ApprovaldropDownList> listDropDownLists = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApprovaldropDownList>();
        ApprovaldropDownList dropDownList = null;
        List<String[]> listItems = new ArrayList<String[]>();
        boolean bool_dropDown = false;
        // 日期
        List<ApproveDatePicker> datePickers = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker>();
        ApproveDatePicker datePicker = null;
        String dateType = "";
        List<ApproveDateItem> dateItems = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker.ApproveDateItem>();
        ApproveDateItem dateItem = null;
        // 搜索狂
        List<ApproveTextBox> listTexTextBoxs = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveTextBox>();
        ApproveTextBox textBox = null;
        // 联系人
        List<ApproveContacts> listContacts = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveContacts>();
        ApproveContacts contacts = null;

        // 列表信息
        List<AppContentListInfo> contentLists = null;
        AppContentListInfo contentList = null;
        String type = "0";
        String detailStyle = "";

        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);

            Log.i(TAG, "getPendingCompleteData2:" + path);
            setHeader(httpPost);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("key1", key1));
            pairs.add(new BasicNameValuePair("key2", key2));
            HttpEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            is = response.getEntity().getContent();

            // System.out.println(new String(LoadUtils.load(is),"utf-8"));
            // 这里获取的是本地的数据
            // is =
            // DataCollectionUtils.class.getClassLoader().getResourceAsStream("approval_XML.xml");

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "utf-8");
            int event = parser.getEventType();
            String tempName = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                tempName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Root".equalsIgnoreCase(tempName)) {
                            approvalInfo = new NewOAInfo();
                        }
                        // 解析合同审批列表
                        else if ("SearchControl".equalsIgnoreCase(tempName)) {
                            searchInfo = approvalInfo.new ApprovalSearchInfo();
                            approvalInfo.setSearchInfo(searchInfo);
                            approvalInfo.setAutoSearch(parser.getAttributeValue(
                                    null, "AutoSearch"));
                            searchInfo.setListDropDownLists(listDropDownLists);
                            searchInfo.setListDatePickers(datePickers);
                            searchInfo.setListContacts(listContacts);
                            searchInfo.setLisTextBoxs(listTexTextBoxs);
                        } else if ("DropDownList".equalsIgnoreCase(tempName)) {
                            bool_dropDown = true;
                            dropDownList = searchInfo.new ApprovaldropDownList();
                            listDropDownLists.add(dropDownList);
                            dropDownList.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            dropDownList.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            dropDownList.setDefaultValue(parser.getAttributeValue(
                                    null, "DefaultValue"));
                            dropDownList.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                            listItems = new ArrayList<String[]>();
                            dropDownList.setListItems(listItems);
                        } else if ("Item".equalsIgnoreCase(tempName)
                                && bool_dropDown) {
                            String[] arr = new String[2];
                            arr[0] = parser.getAttributeValue(null, "key");
                            arr[1] = parser.nextText();
                            listItems.add(arr);
                        } else if ("DatePicker".equalsIgnoreCase(tempName)) {
                            datePicker = searchInfo.new ApproveDatePicker();
                            datePickers.add(datePicker);
                            dateType = parser.getAttributeValue(null, "Type");
                            datePicker.setType(dateType);
                            dateItems = new ArrayList<NewOAInfo.ApprovalSearchInfo.ApproveDatePicker.ApproveDateItem>();
                            datePicker.setDateItems(dateItems);
                        } else if ("DateItem".equalsIgnoreCase(tempName)) {
                            dateItem = datePicker.new ApproveDateItem();
                            dateItems.add(dateItem);
                            dateItem.setType(dateType);
                            dateItem.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            dateItem.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            dateItem.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            dateItem.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("TextBox".equalsIgnoreCase(tempName)) {
                            textBox = searchInfo.new ApproveTextBox();
                            listTexTextBoxs.add(textBox);
                            textBox.setTitle(parser
                                    .getAttributeValue(null, "Title"));
                            textBox.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            textBox.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            textBox.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("Contacts".equalsIgnoreCase(tempName)) {
                            contacts = searchInfo.new ApproveContacts();
                            listContacts.add(contacts);
                            contacts.setTitle(parser.getAttributeValue(null,
                                    "Title"));
                            contacts.setParaName(parser.getAttributeValue(null,
                                    "ParaName"));
                            contacts.setDefaultValue(parser.getAttributeValue(null,
                                    "DefaultValue"));
                            contacts.setRequired(parser.getAttributeValue(null,
                                    "Required"));
                        } else if ("Source".equalsIgnoreCase(tempName)) {
                            contacts.setSourse(parser.nextText());
                        } else if ("ArticleList".equalsIgnoreCase(tempName)) {
                            contentLists = new ArrayList<AppContentListInfo>();
                            approvalInfo.setContentListInfos(contentLists);
                            detailStyle = parser.getAttributeValue(null,
                                    "DetailStyle");
                            type = parser.getAttributeValue(null, "Type");
                        } else if ("ListItem".equalsIgnoreCase(tempName)) {
                            contentList = approvalInfo.new AppContentListInfo();
                            contentLists.add(contentList);
                            contentList.setDetailStyle(detailStyle);
                            contentList.setType(type);
                        } else if ("ItemID".equalsIgnoreCase(tempName)) {
                            contentList.setItemID(parser.nextText());
                        } else if ("Title".equalsIgnoreCase(tempName)) {
                            contentList.setTitle(parser.nextText());
                        } else if ("SubTitle".equalsIgnoreCase(tempName)) {
                            contentList.setSubTitle(parser.nextText());
                        } else if ("Addition".equalsIgnoreCase(tempName)) {
                            contentList.setAddition(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("DropDownList".equalsIgnoreCase(tempName)) {
                            bool_dropDown = false;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();

            }
            return approvalInfo;
        } catch (Exception e) {
            e.printStackTrace();
            approvalInfo = null;

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
        return approvalInfo;
    }

    // document
    @SuppressWarnings("finally")
	/*public static List<AnalyReportItem> receiverAnalyReportData(String path) {
		List<AnalyReportItem> reports = new ArrayList<AnalyReportItem>();
		AnalyReportItem analyReportItem = null;
		InputStream is = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(path);
			Log.i(TAG, "receiverAnalyReportDatalist:" + path);
			httpPost.setHeader("EquipType", "Android");
			httpPost.setHeader("EquipSN", WebUtils.deviceId);
			httpPost.setHeader("Soft", WebUtils.packageName);
			httpPost.setHeader("Tel", WebUtils.phoneNumber);
			httpPost.setHeader("Cookie", WebUtils.cookie);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			if (200 == response.getStatusLine().getStatusCode()) {
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						if ("Category".equalsIgnoreCase(parser.getName())) {
							analyReportItem = new AnalyReportItem();
							reports.add(analyReportItem);
							analyReportItem.id = parser.getAttributeValue(null,
									"Id");
							analyReportItem.name = parser.getAttributeValue(
									null, "Name");
							analyReportItem.description = parser
									.getAttributeValue(null, "Description");
						}
						break;
					}
					eventType = parser.next();
				}
			} else if (500 == response.getStatusLine().getStatusCode()) {
			}

		} catch (Exception e) {
			if (Constants.DEBUG)
				e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					return reports;
				} catch (IOException e) {
					if (Constants.DEBUG)
						e.printStackTrace();
				}
			}
			return reports;
		}

	}*/

    public static InputStream getConnectByHttpPost(String path) {
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            httpPost.setHeader("EquipType", "Android");
            httpPost.setHeader("EquipSN", WebUtils.deviceId);
            httpPost.setHeader("Soft", WebUtils.packageName);
            httpPost.setHeader("Tel", WebUtils.phoneNumber);
            httpPost.setHeader("Cookie", WebUtils.cookie);
            HttpResponse response;
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (200 == response.getStatusLine().getStatusCode()) {
                return entity.getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取通讯录组织机构
    public static void receiverContactGroup(String path,
                                            ContactDepartment parentDept) {
        if (parentDept == null)
            parentDept = DataCache.topDept;
        if (parentDept.subDepts == null || parentDept.subDepts.size() == 0) {
            InputStream is = null;
            ContactDepartment dept = null;
            try {
                // is = MainActivity.class.getClassLoader().getResourceAsStream(
                // "contact.xml");
                is = getConnectByHttpPost(path);
                if (is != null) {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(is, "UTF-8");
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if ("Department".equalsIgnoreCase(parser.getName())) {
                                dept = new ContactDepartment();
                                dept.id = parser.getAttributeValue(null, "id");
                                dept.name = parser.getAttributeValue(null,
                                        "name");
                                dept.subsector = parser.getAttributeValue(null,
                                        "subsector");
                                dept.haspersons = parser.getAttributeValue(
                                        null, "haspersons");

                                dept.personsid = parser.getAttributeValue(null,
                                        "personsid");
                                dept.parentDepartment = parentDept;
                                parentDept.subDepts.add(dept);
                            }
                        }
                        eventType = parser.next();
                    }
                }
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
        }
    }

    // 通过关键字查询
    public static ContactDepartment retrieveSearchedContacts(String path,
                                                             String keywords) {
        InputStream is = null;
        ContactDepartment virtualDept = new ContactDepartment();
        Employee emp = null;
        try {
            is = getConnectByHttpPost(path);
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
                            emp.name = parser
                                    .getAttributeValue(null, "altname");
                            emp.dept = virtualDept;
                            if (android.text.TextUtils.isEmpty(emp.dept.name)) {
                                emp.deptName = parser.getAttributeValue(null,
                                        "DepartmentName");
                            }
                            virtualDept.employees.add(emp);
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
        return virtualDept;
    }

    // 获取人员信息
    public static void retrieveEmployees(String path, ContactDepartment dept) {
        if (dept.employees == null || dept.employees.size() == 0) {
            InputStream is = null;
            Employee emp = null;
            try {
                // is = MainActivity.class.getClassLoader().getResourceAsStream(
                // "employees.xml");
                is = getConnectByHttpPost(path);
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

                                emp.faxNo = parser.getAttributeValue(null,
                                        "fax");

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

    public static Employee retrieveEmployeeDetails(String path, Employee emp) {
        if (emp == null || android.text.TextUtils.isEmpty(emp.company)
                || android.text.TextUtils.isEmpty(emp.email)
                || android.text.TextUtils.isEmpty(emp.faxNo)
                || android.text.TextUtils.isEmpty(emp.officeNo)
                || android.text.TextUtils.isEmpty(emp.mobileNo)
                || android.text.TextUtils.isEmpty(emp.position)) {
            InputStream is = null;
            try {
                is = getConnectByHttpPost(path);
                if (is != null) {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(is, "UTF-8");
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if ("UserDetail".equalsIgnoreCase(parser.getName())) {
                                emp.email = parser.getAttributeValue(null,
                                        "Email");
                                emp.faxNo = parser.getAttributeValue(null,
                                        "Fax");
                                emp.mobileNo = parser.getAttributeValue(null,
                                        "Mobile");
                                emp.position = parser.getAttributeValue(null,
                                        "Officer");
                                emp.officeNo = parser.getAttributeValue(null,
                                        "Phone");
                                emp.company = parser.getAttributeValue(null,
                                        "FromCompany");
                                if (!android.text.TextUtils
                                        .isEmpty(emp.dept.name)) {
                                    emp.deptName = parser.getAttributeValue(
                                            null, "DepartmentName");
                                }
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
        return emp;
    }

    /**
     * document manage 获取要下载的文档信息
     *
     * @param treeEntry
     * @param path
     * @return
     */
    @SuppressWarnings("finally")
	/*public static List<Object> receiverAnalyReportTreeData(String path) {
		List<Object> analyReports = new ArrayList<Object>();
		AnalyReportCategory category = null;
		AnalyReportDoc doc = null;
		InputStream is = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(path);
			Log.i(TAG, "receiverAnalyReportTreeData:" + path);
			httpPost.setHeader("EquipType", "Android");
			httpPost.setHeader("EquipSN", WebUtils.deviceId);
			httpPost.setHeader("Soft", WebUtils.packageName);
			httpPost.setHeader("Tel", WebUtils.phoneNumber);
			httpPost.setHeader("Cookie", WebUtils.cookie);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			if (200 == response.getStatusLine().getStatusCode()) {

				// byte[] data1 = LoadUtils.load(is);
				// System.out.println("new String(receiverAnalyReportTreeData): "
				// + new String(data1, "UTF-8"));

				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						if ("Category".equalsIgnoreCase(parser.getName())) {
							category = new AnalyReportCategory();
							analyReports.add(category);
							category.id = parser.getAttributeValue(null, "Id");
							category.name = parser.getAttributeValue(null,
									"Name");
							category.description = parser.getAttributeValue(
									null, "Description");
							category.parentId = parser.getAttributeValue(null,
									"ParentId");
							category.isLeaf = parser.getAttributeValue(null,
									"IsLeaf");
						} else if ("Doc".equalsIgnoreCase(parser.getName())) {
							doc = new AnalyReportDoc();
							analyReports.add(doc);
							doc.id = parser.getAttributeValue(null, "Id");
							doc.title = parser.getAttributeValue(null, "Title");
							doc.fileUrl = parser.getAttributeValue(null,
									"FileUri");
							doc.categoryId = parser.getAttributeValue(null,
									"CategoryId");

							doc.fileUrl = parser.getAttributeValue(null,
									"FileUri");
							doc.categoryId = parser.getAttributeValue(null,
									"CategoryId");
							doc.level = parser.getAttributeValue(null, "Level");
						}
						break;
					}
					eventType = parser.next();
				}
			} else if (500 == response.getStatusLine().getStatusCode()) {
			}

		} catch (Exception e) {
			if (Constants.DEBUG)
				e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					return analyReports;
				} catch (IOException e) {
					if (Constants.DEBUG)
						e.printStackTrace();
				}
			}
			return analyReports;
		}
	}*/

    public static LinkedHashMap<TaskKey, List<Task>> getPendingCompleteData2(
            String path) throws Exception {
        @SuppressWarnings("unused")
        boolean isParse = false;
        LinkedHashMap<TaskKey, List<Task>> taskMaps = new LinkedHashMap<TaskKey, List<Task>>();
        List<Task> tasks = null;
        TaskKey taskKey = null;
        Task task = null;
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            Log.i(TAG, "getPendingCompleteData2:" + path);
            httpPost.setHeader("EquipType", "Android");
            httpPost.setHeader("EquipSN", WebUtils.deviceId);
            httpPost.setHeader("Soft", WebUtils.packageName);
            httpPost.setHeader("Tel", WebUtils.phoneNumber);
            httpPost.setHeader("Cookie", WebUtils.cookie);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            if (200 == response.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");
                int event = parser.getEventType();
                String tempName = null;
                String ispage = null;
                while (event != XmlPullParser.END_DOCUMENT) {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            tempName = parser.getName();
                            // if (type.equalsIgnoreCase(tempName)) {
                            // isParse = true;
                            // }
                            if ("TaskList".equalsIgnoreCase(tempName)) {
                                ispage = parser.getAttributeValue(null, "ispage");
                            } else if ("Pending".equalsIgnoreCase(tempName)) {
                                tasks = new ArrayList<Task>();
                                taskKey = new TaskKey();
                                taskKey.typeKey = tempName;
                                taskKey.ispage = ispage;
                                taskKey.caption = parser.getAttributeValue(null,
                                        "Caption");
                                taskMaps.put(taskKey, tasks);
                            } else if ("Completed".equalsIgnoreCase(tempName)) {
                                tasks = new ArrayList<Task>();
                                taskKey = new TaskKey();
                                taskKey.typeKey = tempName;
                                taskKey.caption = parser.getAttributeValue(null,
                                        "Caption");
                                taskMaps.put(taskKey, tasks);
                            } else if ("Closed".equalsIgnoreCase(tempName)) {
                                tasks = new ArrayList<Task>();
                                taskKey = new TaskKey();
                                taskKey.typeKey = tempName;
                                taskKey.caption = parser.getAttributeValue(null,
                                        "Caption");
                                taskMaps.put(taskKey, tasks);
                            } else if ("Unread".equalsIgnoreCase(tempName)) {
                                tasks = new ArrayList<Task>();
                                taskKey = new TaskKey();
                                taskKey.typeKey = tempName;
                                taskKey.caption = parser.getAttributeValue(null,
                                        "Caption");
                                taskMaps.put(taskKey, tasks);
                            } else if ("Viewed".equalsIgnoreCase(tempName)) {
                                tasks = new ArrayList<Task>();
                                taskKey = new TaskKey();
                                taskKey.typeKey = tempName;
                                taskKey.caption = parser.getAttributeValue(null,
                                        "Caption");
                                taskMaps.put(taskKey, tasks);
                            } else
                                // if (isParse) {
                                if ("Item".equalsIgnoreCase(tempName)) {
                                    task = new Task();
                                    tasks.add(task);
                                } else if ("ID".equalsIgnoreCase(tempName)) {
                                    task.setId(parser.nextText());
                                } else if ("Title".equalsIgnoreCase(tempName)) {
                                    task.setTitle(parser.nextText());
                                } else if ("Requestor".equalsIgnoreCase(tempName)) {
                                    task.setRequestor(parser.nextText());
                                } else if ("Date".equalsIgnoreCase(tempName)) {
                                    task.setDate(parser.nextText());
                                }
                            // }
                            break;
                        // case XmlPullParser.END_TAG:
                        // if (type.equalsIgnoreCase(parser.getName())) {
                        // isParse = false;
                        // }
                        // break;
                    }
                    event = parser.next();
                }
                return taskMaps;
            }
        } catch (Exception e) {
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
        return taskMaps;
    }

    /**
     * 审批列表动态获取标签和列表
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static Map<TaskKey, List<Task>> receiverApproveTag(String path,
                                                              TaskKey key, Map<TaskKey, List<Task>> itemEntry) throws Exception {
        List<Task> tasks = null;
        TaskKey taskKey = null;
        Task task = null;
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            Log.i(TAG, "receiverApproveTag:" + path);
            httpPost.setHeader("EquipType", "Android");
            httpPost.setHeader("EquipSN", WebUtils.deviceId);
            httpPost.setHeader("Soft", WebUtils.packageName);
            httpPost.setHeader("Tel", WebUtils.phoneNumber);
            httpPost.setHeader("Cookie", WebUtils.cookie);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            if (200 == response.getStatusLine().getStatusCode()) {
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(is);
                Element element = document.getDocumentElement();
                NodeList rootList = element.getChildNodes();
                for (int i = 0; i < rootList.getLength(); i++) {
                    Node ele = rootList.item(i);
                    if (ele.getNodeType() == Node.ELEMENT_NODE) {
                        taskKey = new TaskKey();
                        Element e1 = (Element) ele;
                        taskKey.typeKey = e1.getNodeName();
                        taskKey.caption = e1.getAttribute("Caption");
                        taskKey.isClick = e1.getAttribute("isClick");
                        taskKey.TypeData = e1.getAttribute("TypeData");
                        if (!key.TypeData.equals(taskKey.TypeData)) {
                            continue;
                        }

                        tasks = new ArrayList<Task>();
                        itemEntry.put(key, tasks);
                        if (e1.hasChildNodes()) {
                            NodeList e1List = e1.getChildNodes();
                            for (int j = 0; j < e1List.getLength(); j++) {
                                Node e2 = e1List.item(j);
                                if (e2.getNodeType() == Node.ELEMENT_NODE) {
                                    if (e2.hasChildNodes()) {
                                        NodeList e2List = e2.getChildNodes();
                                        for (int p = 0; p < e2List.getLength(); p++) {
                                            Node n1 = e2List.item(p);
                                            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                                                if (n1.getNodeName()
                                                        .equalsIgnoreCase(
                                                                "ItemList")) {
                                                    continue;
                                                }
                                                task = new Task();
                                                tasks.add(task);
                                                if (n1.hasChildNodes()) {
                                                    NodeList itemNodes = n1
                                                            .getChildNodes();
                                                    for (int z = 0; z < itemNodes
                                                            .getLength(); z++) {
                                                        Node itemNode = itemNodes
                                                                .item(z);
                                                        if (itemNode
                                                                .getNodeType() == Node.ELEMENT_NODE) {
                                                            Element itemEle = (Element) itemNode;
                                                            if ("ID".equalsIgnoreCase(itemEle
                                                                    .getNodeName())) {
                                                                task.setId(itemEle
                                                                        .getTextContent() == null ? ""
                                                                        : itemEle
                                                                        .getTextContent());
                                                            } else if ("Title"
                                                                    .equalsIgnoreCase(itemEle
                                                                            .getNodeName())) {
                                                                task.setTitle(itemEle
                                                                        .getTextContent() == null ? ""
                                                                        : itemEle
                                                                        .getTextContent());
                                                            } else if ("Requestor"
                                                                    .equalsIgnoreCase(itemEle
                                                                            .getNodeName())) {
                                                                task.setRequestor(itemEle
                                                                        .getTextContent() == null ? ""
                                                                        : itemEle
                                                                        .getTextContent());
                                                            } else if ("Date"
                                                                    .equalsIgnoreCase(itemEle
                                                                            .getNodeName())) {
                                                                task.setDate(itemEle
                                                                        .getTextContent() == null ? ""
                                                                        : itemEle
                                                                        .getTextContent());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return itemEntry;
            }
        } catch (Exception e) {
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
        return itemEntry;
    }

    /**
     * 审批列表动态获取标签和列表
     *
     * @param path
     * @param a
     * @return
     * @throws Exception
     */
    public static List<Map<TaskKey, List<Task>>> receiverApproveTag2p0(
            String path, Activity a) throws Exception {

        List<Map<TaskKey, List<Task>>> taskMaps = new ArrayList<Map<TaskKey, List<Task>>>();
        Map<TaskKey, List<Task>> itemEntry = null;
        List<Task> tasks = null;
        TaskKey taskKey = null;
        Task task = null;
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            Log.i(TAG, "receiverApproveTag:" + path);
            httpPost.setHeader("EquipType", "Android");
            httpPost.setHeader("EquipSN", WebUtils.deviceId);
            httpPost.setHeader("Soft", WebUtils.packageName);
            httpPost.setHeader("Tel", WebUtils.phoneNumber);
            httpPost.setHeader("Cookie", WebUtils.cookie);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // 本地模拟
            is = a.getClassLoader().getResourceAsStream("test_approve.xml");
            // is = entity.getContent();
            // byte[] data1 = LoadUtils.load(is);
            // System.out.println("new String(本地模拟test_approve.xml): "
            // + new String(data1, "utf-8"));
            if (200 == response.getStatusLine().getStatusCode()) {
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(is);
                Element element = document.getDocumentElement();
                String ispage = element.getAttribute("ispage");
                NodeList rootList = element.getChildNodes();
                for (int i = 0; i < rootList.getLength(); i++) {
                    Node ele = rootList.item(i);
                    if (ele.getNodeType() == Node.ELEMENT_NODE) {
                        itemEntry = new HashMap<TaskKey, List<Task>>();
                        taskKey = new TaskKey();
                        tasks = new ArrayList<Task>();
                        itemEntry.put(taskKey, tasks);
                        taskMaps.add(itemEntry);
                        Element e1 = (Element) ele;
                        taskKey.ispage = ispage;
                        taskKey.typeKey = e1.getNodeName();
                        taskKey.caption = e1.getAttribute("Caption");
                        taskKey.isClick = e1.getAttribute("isClick");
                        taskKey.TypeData = e1.getAttribute("TypeData");
                        if (e1.hasChildNodes()) {
                            NodeList e1List = e1.getChildNodes();
                            for (int j = 0; j < e1List.getLength(); j++) {
                                Node e2 = e1List.item(j);
                                if (e2.getNodeType() == Node.ELEMENT_NODE) {
                                    if (e2.hasChildNodes()) {
                                        NodeList e2List = e2.getChildNodes();
                                        for (int p = 0; p < e2List.getLength(); p++) {
                                            Node n1 = e2List.item(p);
                                            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                                                if (n1.getNodeName()
                                                        .equalsIgnoreCase(
                                                                "ItemList")) {
                                                    continue;
                                                }
                                                task = new Task();
                                                tasks.add(task);
                                                if (n1.hasChildNodes()) {
                                                    NodeList itemNodes = n1
                                                            .getChildNodes();
                                                    for (int z = 0; z < itemNodes
                                                            .getLength(); z++) {
                                                        Node itemNode = itemNodes
                                                                .item(z);
                                                        if (itemNode
                                                                .getNodeType() == Node.ELEMENT_NODE) {
                                                            Element itemEle = (Element) itemNode;
                                                            if ("ID".equalsIgnoreCase(itemEle
                                                                    .getNodeName())) {
                                                                task.setId(itemEle
                                                                        .getTextContent() == null ? ""
                                                                        : itemEle
                                                                        .getTextContent());
                                                            } else if ("Title"
                                                                    .equalsIgnoreCase(itemEle
                                                                            .getNodeName())) {
                                                                task.setTitle(itemEle
                                                                        .getTextContent() == null ? ""
                                                                        : itemEle
                                                                        .getTextContent());
                                                            } else if ("Requestor"
                                                                    .equalsIgnoreCase(itemEle
                                                                            .getNodeName())) {
                                                                task.setRequestor(itemEle
                                                                        .getTextContent() == null ? ""
                                                                        : itemEle
                                                                        .getTextContent());
                                                            } else if ("Date"
                                                                    .equalsIgnoreCase(itemEle
                                                                            .getNodeName())) {
                                                                task.setDate(itemEle
                                                                        .getTextContent() == null ? ""
                                                                        : itemEle
                                                                        .getTextContent());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Log.i(TAG, "taskMaps.size():" + taskMaps.size());
                return taskMaps;
            }
        } catch (Exception e) {
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
        return taskMaps;
    }

	/*
	 * 下载门户信息附件
	 */

    @SuppressWarnings("finally")
	/*public static List<ScheduleItem> collectScheduleData(String path) {
		List<ScheduleItem> scheduleItems = new ArrayList<ScheduleItem>();
		List<ScheduleChartItem> scheduleChartItems = null;
		List<ScheduleTimeItem> scheduleTimeItems = null;
		List<MettingsPdf> pdfs = null;
		MettingsPdf pdf = null;
		ScheduleItem scheduleItem = null;
		ScheduleChartItem scheduleChartItem = null;
		ScheduleTimeItem scheduleTimeItem = null;
		InputStream is = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(path);
			httpPost.setHeader("EquipType", "Android");
			httpPost.setHeader("EquipSN", WebUtils.deviceId);
			httpPost.setHeader("Soft", WebUtils.packageName);
			httpPost.setHeader("Tel", WebUtils.phoneNumber);
			httpPost.setHeader("Cookie", WebUtils.cookie);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			// byte[] data1 = LoadUtils.load(is);
			// System.out.println("new String(data, charset): " + new
			// String(data1, "UTF-8"));
			if (200 == response.getStatusLine().getStatusCode()) {
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						if ("Item".equalsIgnoreCase(parser.getName())) {
							scheduleItem = new ScheduleItem();
							scheduleItem.setName(parser.getAttributeValue(0));
							scheduleItems.add(scheduleItem);
							scheduleChartItems = new ArrayList<ScheduleChartItem>();
						} else if ("ChartItem".equalsIgnoreCase(parser
								.getName())) {
							scheduleChartItem = new ScheduleChartItem();
							scheduleChartItem.setName(parser
									.getAttributeValue(0));
							scheduleChartItems.add(scheduleChartItem);
							scheduleItem.setChartItems(scheduleChartItems);
							scheduleTimeItems = new ArrayList<ScheduleTimeItem>();
						} else if ("TimeItem"
								.equalsIgnoreCase(parser.getName())) {
							scheduleTimeItem = new ScheduleTimeItem();
							scheduleTimeItem.setKey(Integer.parseInt(parser
									.getAttributeValue(0)));
							scheduleTimeItems.add(scheduleTimeItem);
							scheduleChartItem.setTimeItems(scheduleTimeItems);
						} else if ("Date".equalsIgnoreCase(parser.getName())) {
							scheduleTimeItem.setDate(parser.nextText());
						} else if ("Time".equalsIgnoreCase(parser.getName())) {
							scheduleTimeItem.setTime(parser.nextText());
						} else if ("Plan".equalsIgnoreCase(parser.getName())) {
							scheduleTimeItem.setPlan(parser.nextText());
						} else if ("Content".equalsIgnoreCase(parser.getName())) {
							scheduleTimeItem.setContent(parser.nextText());
						} else if ("Grade".equalsIgnoreCase(parser.getName())) {
							scheduleTimeItem.setGrade(parser.nextText());
						} else if ("Promoters".equalsIgnoreCase(parser
								.getName())) {
							scheduleTimeItem.setPromoters(parser.nextText());
						} else if ("Participants".equalsIgnoreCase(parser
								.getName())) {
							scheduleTimeItem.setParticipants(parser.nextText());
						} else if ("Pdfs".equalsIgnoreCase(parser.getName())) {
							pdfs = new ArrayList<MettingsPdf>();
							scheduleTimeItem.setPdfs(pdfs);
						} else if ("Pdf".equalsIgnoreCase(parser.getName())) {
							pdf = new MettingsPdf();
							pdf.setPdfKey(parser.getAttributeValue(0));
							pdf.setPdfValue(parser.nextText());
							pdfs.add(pdf);
						} else if ("Locate".equalsIgnoreCase(parser.getName())) {
							scheduleTimeItem.setLocate(parser.nextText());
						}
						break;

					}
					eventType = parser.next();
				}
			} else if (500 == response.getStatusLine().getStatusCode()) {
			}

		} catch (Exception e) {
			if (Constants.DEBUG)
				e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					return scheduleItems;
				} catch (IOException e) {
					if (Constants.DEBUG)
						e.printStackTrace();
				}
			}
			return scheduleItems;
		}
	}
*/
    // 获取通讯录组织机构
	/*public static ContactTotalDepartmentInfo getContactGroup(String path) {
		ContactTotalDepartmentInfo info = new ContactTotalDepartmentInfo();
		InputStream is = null;
		ContactDepartment dept = null;
		try {
			// is = MainActivity.class.getClassLoader().getResourceAsStream(
			// "contact.xml");
			is = getConnectByHttpPost(path);
			if (is != null) {
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						if ("Department".equalsIgnoreCase(parser.getName())) {
							dept = new ContactDepartment();
							dept.id = parser.getAttributeValue(null, "id");
							dept.name = parser.getAttributeValue(null, "name");
							dept.subsector = parser.getAttributeValue(null,
									"subsector");
							dept.haspersons = parser.getAttributeValue(null,
									"haspersons");
							dept.personsid = parser.getAttributeValue(null,
									"personsid");
							info.list.add(dept);
						} else if ("MembersOfDepartment"
								.equalsIgnoreCase(parser.getName())) {
							info.has = parser.getAttributeValue(null, "has");
						}
					}
					eventType = parser.next();
				}
			}
			return info;
		} catch (Exception e) {
			if (Constants.DEBUG)
				e.printStackTrace();
			return info;
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

	}*/

    // 获取人员信息
    public static List<Employee> retrieveEmployees(String path) {
        List<Employee> lists = new ArrayList<Employee>();

        InputStream is = null;
        Employee emp = new Employee();
        List<cn.sbx.deeper.moblie.domian.Employee.Row> listRows = new ArrayList<Employee.Row>();
        cn.sbx.deeper.moblie.domian.Employee.Row row = emp.new Row();
        try {
            is = getConnectByHttpPost(path);
            // String resultData = StreamUtils.retrieveContent(is);
            // System.out.println("logData:" + resultData);
            if (is != null) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if ("user".equalsIgnoreCase(parser.getName())) {
                            emp = new Employee();
                            emp.setId(parser.getAttributeValue(null, "id") + "");
                            listRows = new ArrayList<Employee.Row>();
                            emp.setListRows(listRows);
                            lists.add(emp);
                        } else if ("row".equalsIgnoreCase(parser.getName())) {
                            row = emp.new Row();
                            row.setLabel(parser
                                    .getAttributeValue(null, "Label") + "");
                            row.setText(parser.getAttributeValue(null, "Text")
                                    + "");
                            row.setCategory(parser.getAttributeValue(null,
                                    "Category") + "");
                            listRows.add(row);
                        }
                    }
                    eventType = parser.next();
                }
            }
            return lists;
        } catch (Exception e) {
            e.printStackTrace();
            lists = new ArrayList<Employee>();
            return lists;
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

    /**
     * @param path
     *            无线会议2.0新接口
     */
	/*public static LinkedHashMap<String, LinkedHashMap<String, MonthItem>> receiverMeetingData2(
			String path) {
		LinkedHashMap<String, LinkedHashMap<String, MonthItem>> meetingData = new LinkedHashMap<String, LinkedHashMap<String, MonthItem>>();
		LinkedHashMap<String, MonthItem> monthMap = null;
		MonthItem monthItem = null;
		List<Meeting> meetings = null;
		List<MeetingPdf> meetingpdfs = null;
		Meeting meeting = null;
		MeetingPdf meetingPdf = null;
		InputStream is = null;
		String label = null;
		String text = null;
		Map<String, String> content = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(path);
			httpPost.setHeader("EquipType", "Android");
			httpPost.setHeader("EquipSN", WebUtils.deviceId);
			httpPost.setHeader("Soft", WebUtils.packageName);
			httpPost.setHeader("Tel", WebUtils.phoneNumber);
			httpPost.setHeader("Cookie", WebUtils.cookie);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			// byte[] data1 = LoadUtils.load(is);
			// System.out.println("new String(data, charset): " + new
			// String(data1, "UTF-8"));
			if (200 == response.getStatusLine().getStatusCode()) {
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				String eleName;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						eleName = parser.getName();
						if ("Item".equalsIgnoreCase(eleName)) {
							monthMap = new LinkedHashMap<String, MonthItem>();
							String yearName = parser.getAttributeValue(null,
									"Name");
							meetingData.put(yearName, monthMap);
						} else if ("MonthItem".equalsIgnoreCase(eleName)) {
							monthItem = new MonthItem();
							meetings = new ArrayList<Meeting>();
							String name = parser
									.getAttributeValue(null, "Name");
							monthItem.setName(name);
							monthItem.setCount(parser.getAttributeValue(null,
									"Count"));
							monthMap.put(name, monthItem);
						} else if ("MeetingList".equalsIgnoreCase(eleName)) {
							meeting = new Meeting();
							content = new HashMap<String, String>();
							meetingpdfs = new ArrayList<MeetingPdf>();// 创建会议资料列表
							monthItem.setMeetingList(meetings);
							meeting.setMettingKey(Integer.parseInt(parser
									.getAttributeValue(null, "Key")));
							meetings.add(meeting);
						} else if ("Title".equalsIgnoreCase(eleName)) {
							meeting.setTitle(parser.nextText());
						} else if ("Status".equalsIgnoreCase(eleName)) {
							meeting.setStatus(parser.nextText());
						} else if ("CollectStatus".equalsIgnoreCase(eleName)) {
							meeting.setCollectStatus(parser.nextText());
							System.out.println(meeting.getCollectStatus()
									+ "-----------");
						} else if ("Content".equalsIgnoreCase(eleName)) {
							String participantsId = parser.getAttributeValue(
									null, "ParticipantsId");// 这里有权限控制
							meeting.setParticipantIds(participantsId);
						} else if ("Row".equalsIgnoreCase(eleName)) {

						} else if ("Label".equalsIgnoreCase(eleName)) {
							label = parser.nextText();
							// System.out.println("label: "+label);
							meeting.setContent(content);
							if ("会议资料".equalsIgnoreCase(label)) {
								meeting.setPdf(meetingpdfs);
								meetingPdf = new MeetingPdf();
								// meetingPdf.setPdfKey(label);
								meetingpdfs.add(meetingPdf);
							}
						} else if ("Text".equalsIgnoreCase(eleName)) {
							String docID = parser.getAttributeValue(null,
									"DocID");
							text = parser.nextText();
							if ("会议资料".equalsIgnoreCase(label)) {

								meetingPdf.setPdf(text); // 正则字符串
								// {Secret:0} 0表示不保密，1表示保密
								String secret = text.substring(
										text.indexOf("{") + 1,
										text.lastIndexOf("}"));
								meetingPdf.setSecret(secret.substring(secret
										.indexOf(":") + 1));

								String[] nameValuePair = cn.sbx.deeper.moblie.util.TextUtils
										.transPdf(text);
								meetingPdf.setDocID(docID);
								meetingPdf.setPdfKey(nameValuePair[0]);
								meetingPdf.setValue(nameValuePair[1]);
								// meetingPdf.setValue(text);// 将会议资料加入队列
							} else {
								content.put(label, text);
							}
							// System.out.println("text : "+text);
						}
						break;
					}
					eventType = parser.next();
				}
			} else if (500 == response.getStatusLine().getStatusCode()) {
			}

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
		// List<Meeting> tempMeeting = new ArrayList<Meeting>();
		// for (int i = 0; i < mettings.size(); i++) {
		// Meeting meeting1 = mettings.get(i);
		// if (meeting1.getParticipantIds().contains(
		// UserInfo.getInstance().getUsername().trim())
		// || meeting1
		// .getPromterIds()
		// .trim()
		// .contains(
		// UserInfo.getInstance().getUsername().trim())) {
		// tempMeeting.add(meeting1);
		// }
		// }
		return meetingData;
	}*/

    /**
     * @param pdf 发送的坐标的方法
     * @return
     */
    public static void receiverJoinMeetStatus2(String path, int mid,
                                               String content) {
        Log.i(TAG, "receiverJoinMeetStatus--path:" + path);
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            httpPost.setHeader("EquipType", "Android");
            httpPost.setHeader("EquipSN", WebUtils.deviceId);
            httpPost.setHeader("Soft", WebUtils.packageName);
            httpPost.setHeader("Tel", WebUtils.phoneNumber);
            httpPost.setHeader("Cookie", WebUtils.cookie);

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("meetingid", "" + mid));
            pairs.add(new BasicNameValuePair("deviceid", WebUtils.deviceId));
            pairs.add(new BasicNameValuePair("content", content));

            HttpEntity httpEntity = new UrlEncodedFormEntity(pairs, "utf-8");
            httpPost.setEntity(httpEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (200 == httpResponse.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                is = httpResponse.getEntity().getContent();
                // if (content.equals("")) {
                // String resultData = StreamUtils.retrieveContent(is);
                // System.out.println("logData:" + resultData);
                // }

                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                String eleName = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            eleName = parser.getName();
                            if ("result".equalsIgnoreCase(eleName)) {
                                if ("0".equalsIgnoreCase(parser.nextText())) {
                                    // return true;
                                } else if ("1".equalsIgnoreCase(parser.nextText())) {
                                    // return false;
                                }
                            }
                            break;
                    }
                    eventType = parser.next();
                }
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
        // return false;
    }

    /**
     * 获取无线会议状态
     *
     * @param path
     * @return
     */
	/*public static WirelessMeetStatu receiverWirelessStatus(String path) {
		WirelessMeetStatu meetStatu = null;
		InputStream is = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			// String url = WebUtils.rootUrl +
			// URLUtils.wireless_getWirelessStatus + "?meetingid=" + meetId +
			// "&deviceid=" + WebUtils.deviceId;
			HttpGet httpGet = new HttpGet(path);
			httpGet.setHeader("EquipType", "Android");
			httpGet.setHeader("EquipSN", WebUtils.deviceId);
			httpGet.setHeader("Soft", WebUtils.packageName);
			httpGet.setHeader("Tel", WebUtils.phoneNumber);
			httpGet.setHeader("Cookie", WebUtils.cookie);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (200 == httpResponse.getStatusLine().getStatusCode()) {
				XmlPullParser parser = Xml.newPullParser();
				is = httpResponse.getEntity().getContent();
				// String resultData = StreamUtils.retrieveContent(is);
				// Log.i(TAG, "resultData:" + resultData);
				parser.setInput(is, "UTF-8");
				int eventType = parser.getEventType();
				String eleName = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						eleName = parser.getName();
						if ("root".equalsIgnoreCase(eleName)) {
							meetStatu = new WirelessMeetStatu();
						} else if ("hostID".equalsIgnoreCase(eleName)) {
							meetStatu.hostId = parser.nextText();
						} else if ("screenSize".equalsIgnoreCase(eleName)) {
							meetStatu.screenSize = parser.nextText();
						} else if ("jsonString".equalsIgnoreCase(eleName)) {
							meetStatu.jsonString = parser.nextText();
						} else if ("meetingStatus".equalsIgnoreCase(eleName)) {
							meetStatu.meetingStatu = parser.nextText();
						}
						break;
					}
					eventType = parser.next();
				}
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
		return meetStatu;
	}
*/

    /**
     * 发送批注的方法
     *
     * @param path      路径
     * @param docid
     * @param annttrode
     * @param docwah
     * @param doclength
     * @param annttinfo
     * @return
     */
    public static String[] sendAnnotate(String path, String docid,
                                        String annttrode, String doclength, String annttinfo) {
        String[] arr = new String[2];
        InputStream is = null;
        BasicHttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10 * 1000); // 设置请求超时的时间
        HttpConnectionParams.setSoTimeout(params, 10 * 1000); // 设置等待数据超时的时间
        HttpClient httpClient = new DefaultHttpClient(params);
        HttpPost httpPost = new HttpPost(path);
        httpPost.setHeader("EquipType", "Android");
        httpPost.setHeader("EquipSN", WebUtils.deviceId);
        httpPost.setHeader("Soft", WebUtils.packageName);
        httpPost.setHeader("Tel", WebUtils.phoneNumber);
        httpPost.setHeader("Cookie", WebUtils.cookie);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("docid", docid));
            nameValuePairs.add(new BasicNameValuePair("annttrode", annttrode));
            nameValuePairs.add(new BasicNameValuePair("docwah", "docwah"));
            nameValuePairs.add(new BasicNameValuePair("doclength", doclength));
            nameValuePairs.add(new BasicNameValuePair("annttinfo", annttinfo));

            HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs,
                    "utf-8");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                XmlPullParser parser = Xml.newPullParser();
                is = response.getEntity().getContent();
                parser.setInput(is, "utf-8");
                int type = parser.getEventType();
                String eleName = "";
                while (type != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            eleName = parser.getName();
                            if (eleName.equals("result")) {
                                arr[0] = parser.nextText();
                            } else if (eleName.equals("message")) {
                                arr[1] = parser.nextText();
                            }
                            break;
                        default:
                            break;
                    }
                    type = parser.next();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
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

        return arr;
    }

    /**
     * 接收批注的方法
     *
     * @param path      路径
     * @param docid
     * @param annttrode
     * @param docwah
     * @param doclength
     * @param annttinfo
     * @return
     */
    public static ReceiveAnnotateInfo receiveAnnotate(String path,
                                                      String docid, String annttrode, String tpoint) {

        ReceiveAnnotateInfo annotateInfo = ReceiveAnnotateInfo.getInfo();
        List<HostInfo> hostInfos = new ArrayList<ReceiveAnnotateInfo.HostInfo>();
        HostInfo hostInfo = null;
        boolean host_bool = false;

        InputStream is = null;
        BasicHttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10 * 1000); // 设置请求超时的时间
        HttpConnectionParams.setSoTimeout(params, 10 * 1000); // 设置等待数据超时的时间
        HttpClient httpClient = new DefaultHttpClient(params);
        HttpPost httpPost = new HttpPost(path);
        httpPost.setHeader("EquipType", "Android");
        httpPost.setHeader("EquipSN", WebUtils.deviceId);
        httpPost.setHeader("Soft", WebUtils.packageName);
        httpPost.setHeader("Tel", WebUtils.phoneNumber);
        httpPost.setHeader("Cookie", WebUtils.cookie);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("docid", docid));
            nameValuePairs.add(new BasicNameValuePair("annttrode", annttrode));
            nameValuePairs.add(new BasicNameValuePair("tpoint", tpoint));

            HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs,
                    "utf-8");
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                is = response.getEntity().getContent();
                // String resultData = StreamUtils.retrieveContent(is);
                // System.out.println("resultData:" + resultData);
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "utf-8");
                int type = parser.getEventType();
                String tagName = "";
                while (type != XmlPullParser.END_DOCUMENT) {
                    switch (type) {
                        case XmlPullParser.START_TAG:
                            tagName = parser.getName();
                            if (tagName.equals("DocID")) {
                                annotateInfo.setDocId(parser.nextText());
                            } else if (tagName.equals("Host")) {
                                host_bool = true;
                                annotateInfo.setHostInfos(hostInfos);
                            } else if (tagName.equals("TPoint") && host_bool) {
                                hostInfo = annotateInfo.new HostInfo();
                                hostInfos.add(hostInfo);
                                ReceiveAnnotateInfo.setLastTPoint(parser
                                        .getAttributeValue(null, "ID"));
                                hostInfo.setId(parser.getAttributeValue(null, "ID"));
                                hostInfo.setDocAttr(parser.getAttributeValue(null,
                                        "DocAttr"));
                                hostInfo.setDocLength(parser.getAttributeValue(
                                        null, "DocLength"));
                                hostInfo.setAnnotate(parser.nextText());
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            if (tagName.equals("Host")) {
                                host_bool = false;
                            }
                            break;
                        default:
                            break;
                    }

                    type = parser.next();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
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

        return annotateInfo;
    }

    /**
     * @param path
     * @return
     */
    public static Boolean receiverJoinMeetStatus(String path) {
        Log.i(TAG, "receiverJoinMeetStatus--path:" + path);
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpGet httpGet = new HttpGet(path);
            httpGet.setHeader("EquipType", "Android");
            httpGet.setHeader("EquipSN", WebUtils.deviceId);
            httpGet.setHeader("Soft", WebUtils.packageName);
            httpGet.setHeader("Tel", WebUtils.phoneNumber);
            httpGet.setHeader("Cookie", WebUtils.cookie);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (200 == httpResponse.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                is = httpResponse.getEntity().getContent();
                // String resultData = StreamUtils.retrieveContent(is);
                // Log.i(TAG, "resultData:" + resultData);
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                String eleName = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            eleName = parser.getName();
                            if ("result".equalsIgnoreCase(eleName)) {
                                if ("0".equalsIgnoreCase(parser.nextText())) {
                                    return true;
                                } else if ("1".equalsIgnoreCase(parser.nextText())) {
                                    return false;
                                }
                            }
                            break;
                    }
                    eventType = parser.next();
                }
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
        return false;
    }

    public static ChildAccountsBean checkChildAccounts(String path,
                                                       String name, String pwd, String moduleId) {
        ChildAccountsBean accounts = new ChildAccountsBean();
        // List<ChildAccountsBean> moreUserInfoList = new
        // ArrayList<ChildAccountsBean>();
        // List<String> moreUserInfoList = new ArrayList<String>();
        // HttpClient client = new DefaultHttpClient();
        HttpClient client = ProxyCheck.myHttpClient();
        HttpPost httpPost = new HttpPost(path);
        System.out.println("2222222:" + path);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        setHeader(httpPost);
        nameValuePairs.add(new BasicNameValuePair("key1", URLEncoder
                .encode(name)));// WebUtils.packageName
        nameValuePairs.add(new BasicNameValuePair("key2", URLEncoder
                .encode(pwd)));// WebUtils.packageName
        nameValuePairs.add(new BasicNameValuePair("key3", URLEncoder
                .encode(moduleId)));
        InputStream is = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse httpResponse = client.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            is = entity.getContent();
            // System.out.println(StreamUtils.retrieveContent(is));
            // int leng;
            // byte[] arr = new byte[1024];
            // while ((leng = is.read(arr)) != -1) {
            // System.out.println(new String(arr, 0, leng) + "返回来的数据");
            // }

            if (200 == httpResponse.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if ("code".equalsIgnoreCase(parser.getName())) {
                            accounts.resault = parser.nextText();
                        } else if ("username"
                                .equalsIgnoreCase(parser.getName())) {
                            // accounts = new ChildAccountsBean();
                            // accounts.setName(username);
                            // accounts.setPassWord(password);
                            // accounts.setDetaname(parser.getAttributeValue(0));
                            // accounts.setDetapassWord(parser.getAttributeValue(1));
                            // accounts.setId(parser.getAttributeValue(2));
                            // accounts.setBsId(parser.getAttributeValue(3));
                            // accounts.setBsName(parser.getAttributeValue(4));
                            // moreUserInfoList.add(accounts);
                            accounts.detaname = parser.nextText();
                            System.out.println("accounts.detaname:"
                                    + accounts.detaname);
                        } else if ("password"
                                .equalsIgnoreCase(parser.getName())) {
                            accounts.detapassWord = parser.nextText();
                        }
                    }
                    eventType = parser.next();
                }
            } else if (500 == httpResponse.getStatusLine().getStatusCode()) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return accounts;
    }

    /**
     * @param path
     */
    public static ResultInfo submitApproveBatch(String path, String tabId,
                                                String itemids, String opinion) {
        ResultInfo resultInfo = new ResultInfo();
        InputStream is = null;
        try {
            // HttpParams httpParameters = new
            // BasicHttpParams();DefaultHttpClient httpClient =
            // HttpUtils.initHttpClient(httpParameters);
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load submitApproveBatch data:" + path);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", tabId));
            nameValuePairs.add(new BasicNameValuePair("key2", itemids));
            nameValuePairs.add(new BasicNameValuePair("key3", opinion));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println("submitApproveBatch Num:"+StreamUtils.retrieveContent(is));

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("result".equalsIgnoreCase(parser.getName())) {
                            resultInfo.result = parser.nextText();
                            // System.out.println("id:"+number[0]+"  count:"+number[1]);
                        } else if ("message".equalsIgnoreCase(parser.getName())) {
                            resultInfo.message = parser.nextText();
                            // System.out.println("id:"+number[0]+"  count:"+number[1]);
                        }
                        break;
                }
                event = parser.next();
            }
            return resultInfo;

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
        return resultInfo;
    }

    // 发送短信
    public static String sendTextMessage(String path, String username,
                                         String recipents, String msgContent, String msgType, String postback) {
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            // HttpPost httpPost = new
            // HttpPost(Constants.MESSAGE_TYPE_TEXT.equals(msgType) ?
            // Constants.BASE_URL_ALL + Constants.URL_SEND_TEXT_MSG
            // : Constants.BASE_URL_ALL + Constants.URL_SEND_MULTIMEDIA_MSG);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            // GUID

            // nameValuePairs.add(new BasicNameValuePair(Constants.Params.GUID,
            // Constants.Variables.GUID));
            // nameValuePairs.add(new
            // BasicNameValuePair(Constants.Params.RECIPIENTS, recipents));
            // nameValuePairs.add(new
            // BasicNameValuePair(Constants.Params.MSG_CONTENT, msgContent));
            nameValuePairs.add(new BasicNameValuePair("key1", URLEncoder
                    .encode(msgContent, "utf-8")));
            nameValuePairs.add(new BasicNameValuePair("key2", username));
            nameValuePairs.add(new BasicNameValuePair("key3", recipents));
            nameValuePairs.add(new BasicNameValuePair("key4", msgType));
            nameValuePairs.add(new BasicNameValuePair("key5", postback));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            if (200 == response.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if ("result".equals(parser.getName())) {
                            return parser.nextText().trim();
                        }
                    }
                    eventType = parser.next();
                }
            } else if (500 == response.getStatusLine().getStatusCode()) {
                // AlertUtils.showNetworkUnavailable();
                if (Constants.DEBUG)
                    Log.e(TAG, StreamUtils.retrieveContent(is));
            }
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
        return "0";
    }

    // 发送短信
    public static String getMessage(String path, String search, String postback) {
        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            setHeader(httpPost);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", URLEncoder
                    .encode(search, "utf-8")));
            nameValuePairs.add(new BasicNameValuePair("key2", postback));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // Log.e(TAG, StreamUtils.retrieveContent(is));
            if (200 == response.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if ("sendmessage".equals(parser.getName())) {
                            return parser.nextText();
                        }
                    }
                    eventType = parser.next();
                }
            } else if (500 == response.getStatusLine().getStatusCode()) {
                if (Constants.DEBUG)
                    Log.e(TAG, StreamUtils.retrieveContent(is));
            }
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
        return "0";
    }

/*	public static MessageSend requestSendpage(String sendPage) {
		MessageSend ms = null;
		try {
			InputStream is = null;
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient httpClient = HttpUtils
					.initHttpClient(httpParameters);
			HttpGet httpGet = new HttpGet(sendPage);
			httpGet.setHeader("EquipType", "Android");
			httpGet.setHeader("EquipSN", WebUtils.deviceId);
			httpGet.setHeader("Soft", WebUtils.packageName);
			httpGet.setHeader("Tel", WebUtils.phoneNumber);
			httpGet.setHeader("Cookie", WebUtils.cookie);
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			// System.out.println(StreamUtils.retrieveContent(is));
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if ("root".equals(parser.getName())) {
						ms = new MessageSend();
					} else if ("pagelabel".equals(parser.getName())) {
						ms.pagelabel = parser.nextText();
					} else if ("orginaldep".equals(parser.getName())) {
						ms.orginaldep = parser.nextText();
					} else if ("sendmessage".equals(parser.getName())) {
						ms.sm.alt = parser.getAttributeValue(null, "alt");
						ms.sm.canmodify = parser.getAttributeValue(null,
								"canmodify");
						ms.sm.sendmessage = parser.nextText();
					} else if ("buttons".equals(parser.getName())) {
						ms.btns.canmodify = parser.getAttributeValue(null,
								"canmodify");
					} else if ("button".equals(parser.getName())) {
						MessageSend.Button btn = ms.new Button();
						btn.id = parser.getAttributeValue(null, "id");
						btn.title = parser.getAttributeValue(null, "title");
						ms.buttons.add(btn);
					} else if ("postback".equals(parser.getName())) {
						ms.postback = parser.nextText();
					}
				}
				eventType = parser.next();
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ms;

	}
*/
    // 文档增加
    /**
     * 获取文档列表
     */
    /**
     * @param path
     * @param key1
     * @param key2 当前显示的数量
     * @param key3 期望返回的条数
     * @param key4 当前显示的数据最后一条的id
     * @param key5 搜索的xml
     * @return
     */
    public static List<NewsPaper> receiverManualList(String path, String key1,
                                                     String key2, String key3, String key4, String key5) {
        InputStream is = null;
        List<NewsPaper> intranets = null;
        NewsPaper intranet = null;
        Folders folders = null;
        List<Folders> folders2 = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(path);
            httpPost.setHeader("EquipType", "Android");
            httpPost.setHeader("EquipSN", WebUtils.deviceId);
            httpPost.setHeader("Soft", WebUtils.packageName);
            httpPost.setHeader("Tel", WebUtils.phoneNumber);
            httpPost.setHeader("Cookie", WebUtils.cookie);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", key1 + ""));
            nameValuePairs.add(new BasicNameValuePair("key2", key2 + ""));
            nameValuePairs.add(new BasicNameValuePair("key3", key3 + ""));
            nameValuePairs.add(new BasicNameValuePair("key4", key4 + ""));
            nameValuePairs.add(new BasicNameValuePair("key5", URLEncoder
                    .encode(key5 + "", "utf-8")));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = hc.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            // System.out.println(StreamUtils.retrieveContent(is));

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("Docs".equalsIgnoreCase(parser.getName())) {
                            intranets = new ArrayList<NewsPaper>();
                        } else if ("Doc".equalsIgnoreCase(parser.getName())) {
                            intranet = new NewsPaper();
                            intranets.add(intranet);
                            intranet.id = parser.getAttributeValue(null, "id");
                            intranet.author = parser.getAttributeValue(null,
                                    "author");
                            intranet.date = parser.getAttributeValue(null, "date");
                            intranet.title = parser
                                    .getAttributeValue(null, "title");
                            intranet.docUrl = parser.getAttributeValue(null,
                                    "fileuri");
                            intranet.filetype = parser.getAttributeValue(null,
                                    "filetype");
                            intranet.field1 = parser.getAttributeValue(null,
                                    "field1");
                        } else if ("folders".equalsIgnoreCase(parser.getName())) {
                            intranet = new NewsPaper();
                            intranets.add(intranet);
                            intranet.folders = new Folders();
                        } else if ("folder".equalsIgnoreCase(parser.getName())) {
                            intranet.folders.id = parser.getAttributeValue(null,
                                    "id");
                            intranet.folders.title = parser.getAttributeValue(null,
                                    "title");
                            intranet.folders.count = parser.getAttributeValue(null,
                                    "count");
                            intranet.folders.parentid = parser.getAttributeValue(
                                    null, "parentid");
                        }

                        break;
                }
                event = parser.next();
            }
            return intranets;
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
        return null;
    }

    /**
     *
     * @param path
     * @param key1
     * @param key2
     *            当前显示的数量
     * @param key3
     *            期望返回的条数
     * @param key4
     *            当前显示的数据最后一条的id
     * @param key5
     *            搜索的xml
     * @return
     */
	/*public static DocData receiverTreeList(String path, String key1,
			String key2, String key3, String key4, String key5) {
		InputStream is = null;
		DocData docData = new DocData();
		List<DocFileData> docFileDatas = null;
		List<DocTypeData> docTypeDatas = null;
		DocFileData docFileData = null;
		DocTypeData docTypeData = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(path);
			httpPost.setHeader("EquipType", "Android");
			httpPost.setHeader("EquipSN", WebUtils.deviceId);
			httpPost.setHeader("Soft", WebUtils.packageName);
			httpPost.setHeader("Tel", WebUtils.phoneNumber);
			httpPost.setHeader("Cookie", WebUtils.cookie);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("key1", key1 + ""));
			nameValuePairs.add(new BasicNameValuePair("key2", key2 + ""));
			nameValuePairs.add(new BasicNameValuePair("key3", key3 + ""));
			nameValuePairs.add(new BasicNameValuePair("key4", key4 + ""));
			nameValuePairs.add(new BasicNameValuePair("key5", URLEncoder
					.encode(key5 + "", "utf-8")));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = hc.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			// System.out.println(StreamUtils.retrieveContent(is));

			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if ("Docs".equalsIgnoreCase(parser.getName())) {
						docFileDatas = new ArrayList<DocFileData>();
						docData.setDocFileDatas(docFileDatas);
					} else if ("Doc".equalsIgnoreCase(parser.getName())) {
						docFileData = new DocFileData();
						docFileDatas.add(docFileData);
						docFileData.setId(parser.getAttributeValue(null, "id"));
						docFileData.setAuthor(parser.getAttributeValue(null,
								"author"));
						docFileData.setDate(parser.getAttributeValue(null,
								"date"));
						docFileData.setTitle(parser.getAttributeValue(null,
								"title"));
						docFileData.setFileuri(parser.getAttributeValue(null,
								"fileuri"));
						docFileData.setFiletype(parser.getAttributeValue(null,
								"filetype"));
						docFileData.setField1(parser.getAttributeValue(null,
								"field1"));
					} else if ("folders".equalsIgnoreCase(parser.getName())) {
						docTypeDatas = new ArrayList<DocTypeData>();
						docData.setDocTypeDatas(docTypeDatas);
					} else if ("folder".equalsIgnoreCase(parser.getName())) {
						docTypeData = new DocTypeData();
						docTypeDatas.add(docTypeData);
						docTypeData.setType("folder");
						docTypeData.setId(parser.getAttributeValue(null, "id"));
						docTypeData.setName(parser.getAttributeValue(null,
								"title"));
						docTypeData.setCount(parser.getAttributeValue(null,
								"count"));
						docTypeData.setParentid(parser.getAttributeValue(null,
								"parentid"));
					}
					// else if
					// ("currentfiles".equalsIgnoreCase(parser.getName())) {
					// docTypeData = new DocTypeData();
					// docTypeDatas.add(docTypeData);
					// docTypeData.setType("current");
					// docTypeData.setId(parser.getAttributeValue(null, "id"));
					// docTypeData.setName(parser.getAttributeValue(null,
					// "title"));
					// docTypeData.setCount(parser.getAttributeValue(null,
					// "count"));
					// docTypeData.setParentid(parser.getAttributeValue(null,
					// "parentid"));
					// }
					break;
				}
				event = parser.next();
			}
			return docData;
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
		return null;
	}
*/

    /**
     * 检查开发者
     *
     * @param userName
     */
    public static void checkRole(String userName) {

        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
            hc.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            HttpPost httpPost = new HttpPost(WebUtils.rootUrl
                    + URLUtils.devCheckUrl);
            httpPost.setHeader("EquipType", "Android");
            httpPost.setHeader("EquipSN", WebUtils.deviceId);
            httpPost.setHeader("Soft", WebUtils.packageName);
            httpPost.setHeader("Tel", WebUtils.phoneNumber);
            httpPost.setHeader("Cookie", WebUtils.cookie);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", userName));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = hc.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // System.out.println(StreamUtils.retrieveContent(is));
            // byte[] data1 = LoadUtils.load(is);
            // System.out.println("new String(receiverAnalyReportTreeData): "
            // + new String(data1, "UTF-8"));
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("message".equalsIgnoreCase(parser.getName())) {
                            WebUtils.role = parser.nextText();
                            break;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

    }

    public static List<String> receiverimageNumDataHome(String path) {
        // path = WebUtils.rootUrl + URLUtils.mobileOfficeNewslistMain;
        List<String> numbers = new ArrayList<String>();
        String emailNum = "";

        InputStream is = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            HttpGet httpPost = new HttpGet(path);
            if (Constants.DEBUG) {
                Log.i(TAG, "begin get email number:" + path);
            }
            setHeader(httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            // is = MainActivity.class.getClassLoader().getResourceAsStream(
            // "contact.xml");
            if (200 == response.getStatusLine().getStatusCode()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(is, "UTF-8");
                int event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if ("tab".equalsIgnoreCase(parser.getName())) {
                                emailNum = new String();
                                emailNum = parser.getAttributeValue(null, "Count");
                                numbers.add(emailNum);
                            }
                            break;
                        default:
                            break;
                    }
                    event = parser.next();
                }
            }
            return numbers;
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

        return numbers;
    }

    /**
     * 集群通软件注销
     */
    public static final String ACTION_LOGOUT = "com.zed3.sipua.logout";

    public static void logoutSendBroadcast(Context context) {
        Intent intent = new Intent(ACTION_LOGOUT);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }

    // 获取紧急联系人
    public static List<ContactPeople> getContactList(String username,
                                                     InputStream inputStream) {
        List<ContactPeople> contactPeoples = new ArrayList<ContactPeople>();
        InputStream is = null;
        ContactPeople contactPeople = null;
        boolean b_item = false;
        boolean b_currUser = false;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(WebUtils.contactUrl);
            System.out.println("WebUtils.contactUrl=============="
                    + WebUtils.contactUrl);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", username));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            setHeader(httpPost);
            // HttpResponse response = hc.execute(httpPost);
            // HttpEntity entity = response.getEntity();
            // is = entity.getContent();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("currUser".equalsIgnoreCase(parser.getName())) {
                            b_currUser = true;
                            b_item = false;
                        } else if ("phone".equalsIgnoreCase(parser.getName())
                                && b_currUser && !b_item) {
                            WebUtils.jqtusername = safeNextText(parser);
                        } else if ("item".equalsIgnoreCase(parser.getName())) {
                            b_item = true;
                            contactPeople = new ContactPeople();
                            contactPeoples.add(contactPeople);
                        } else if ("name".equalsIgnoreCase(parser.getName())
                                && b_item) {
                            contactPeople.setName(safeNextText(parser));
                        } else if ("phone".equalsIgnoreCase(parser.getName())
                                && b_item) {
                            contactPeople.setPhone(safeNextText(parser));
                        } else if ("member".equalsIgnoreCase(parser.getName())
                                && b_item) {
                            contactPeople.setMember(safeNextText(parser));
                        } else if ("status".equalsIgnoreCase(parser.getName())
                                && b_item) {
                            contactPeople.setStatus(safeNextText(parser));
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage() + "=============e.getMessage()");
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
        return contactPeoples;

    }

    private static String safeNextText(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        String result = parser.nextText();
        if (parser.getEventType() != XmlPullParser.END_TAG) {
            parser.nextTag();
        }
        return result;
    }

    // 获取紧急联系人
    public static String getSubmitUrl(String username) {
        InputStream is = null;
        String string = "";
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient hc = HttpUtils.initHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(WebUtils.getsubmitUrl);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            // nameValuePairs.add(new BasicNameValuePair("key1", username));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            setHeader(httpPost);
            HttpResponse response = hc.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            byte[] data1 = LoadUtils.load(is);
            string = new String(data1, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
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
        return string;

    }

    /**
     * 同步数据
     */
    public static String SynchronousData(String path, String key1) {


        InputStream is = null;
        String result = "";
        try {
            HttpParams httpParameters = new BasicHttpParams();
            DefaultHttpClient httpClient = HttpUtils
                    .initHttpClient(httpParameters);
            Log.i(TAG, "begin load approve tab list data:" + path);
            HttpPost httpPost = new HttpPost(path);
            // 设置请求超时

//			DataOutputStream os = new DataOutputStream( httpPost.getOutputStream() );
//			os.write( "".getBytes("UTF-8"), 0, 0);
//			os.flush();
//			os.close();


            int timeoutConnection = 100 * 1000;
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    timeoutConnection);
            // 设置响应超时
            int timeoutSocket = 100 * 1000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            setHeader(httpPost);
//            httpPost.setHeader("Content-Length", "" + key1.length());
            // params
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("key1", key1));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {

                HttpEntity entity = response.getEntity();
                is = entity.getContent();

                return result = JsonUtil.InputStreamTOString(is);
            }

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

        return result;
    }
}
