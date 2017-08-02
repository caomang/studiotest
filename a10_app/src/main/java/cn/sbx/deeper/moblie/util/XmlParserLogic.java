package cn.sbx.deeper.moblie.util;

import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cn.sbx.deeper.moblie.contrants.Constants;
import cn.sbx.deeper.moblie.domian.ApprovalDetail;
import cn.sbx.deeper.moblie.domian.ApprovalGroup;
import cn.sbx.deeper.moblie.domian.ApprovalItem;
import cn.sbx.deeper.moblie.domian.MenuGroup;
import cn.sbx.deeper.moblie.domian.MenuModule;
import cn.sbx.deeper.moblie.domian.MenuPage;
import cn.sbx.deeper.moblie.domian.SinopecMenu;
import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.domian.SinopecMenuPage;


public class XmlParserLogic {
	/**
	 * @parser MainMenuXml
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static LinkedHashMap<String, ApprovalGroup> receiveMainMenuList(
			InputStream is) throws Exception {
		// 分析系统
		LinkedHashMap<String, ApprovalGroup> groupChilds = new LinkedHashMap<String, ApprovalGroup>();
		ApprovalGroup group = null;
		String category = "";
		String captionGroup = "";
		List<ApprovalItem> itemList = null;
		List<ApprovalDetail> detailList = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		int event = parser.getEventType();
		String tempName = null;
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_TAG:
				tempName = parser.getName();
				if ("group".equals(tempName)) {
					category = parser.getAttributeValue(null, "category");
					captionGroup = parser.getAttributeValue(null, "caption");
				} else if ("module".equals(tempName)) {
					String id = parser.getAttributeValue(null, "id");
					String code = parser.getAttributeValue(null, "code");
					String caption = parser.getAttributeValue(null, "caption");
					String barImg1 = parser.getAttributeValue(null, "barImg1");
					String barImg2 = parser.getAttributeValue(null, "barImg2");
					String itemImg1 = parser
							.getAttributeValue(null, "itemImg1");
					String itemImg2 = parser
							.getAttributeValue(null, "itemImg2");
					String bgImg = parser.getAttributeValue(null, "bgImg");
					String submenu = parser.getAttributeValue(null, "submenu");

					group = new ApprovalGroup();
					group.setId(id);
					group.setCode(code);
					group.setCaption(caption);
					group.setBarImg1(barImg1);
					group.setBarImg2(barImg2);
					group.setItemImg1(itemImg1);
					group.setBarImg2(itemImg2);
					group.setBgImg(bgImg);
					group.setSubmenu(submenu);
					group.setCategory(category);
					group.setCaptionGroup(captionGroup);
					groupChilds.put(code, group);
					itemList = new ArrayList<ApprovalItem>();
					group.setItem(itemList);
					// System.out.println("group to String :"+group.toString());
				} else if ("page".equals(tempName)
						&& parser.getAttributeValue(null, "pageType").equals(
								"2")) {
					ApprovalItem item = new ApprovalItem();
					String id = parser.getAttributeValue(null, "id");
					String code = parser.getAttributeValue(null, "code");
					String caption = parser.getAttributeValue(null, "caption");
					String barImg1 = parser.getAttributeValue(null, "barImg1");
					String barImg2 = parser.getAttributeValue(null, "barImg2");
					String itemImg1 = parser
							.getAttributeValue(null, "itemImg1");
					String itemImg2 = parser
							.getAttributeValue(null, "itemImg2");
					String bgImg = parser.getAttributeValue(null, "bgImg");
					String pageType = parser
							.getAttributeValue(null, "pageType");
					item.setId(id);
					item.setCode(code);
					item.setCaption(caption);
					item.setBarImg1(barImg1);
					item.setBarImg2(barImg2);
					item.setItemImg1(itemImg1);
					item.setItemImg2(itemImg2);
					item.setBgImg(bgImg);
					item.setPageType(pageType);
					itemList.add(item);

					detailList = new ArrayList<ApprovalDetail>();
					item.setDetail(detailList);
					// System.out.println("items to String :"+item.toString());
				} else if ("page".equals(tempName)
						&& parser.getAttributeValue(null, "pageType").equals(
								"1")) {
					ApprovalDetail detail = new ApprovalDetail();
					String id = parser.getAttributeValue(null, "id");
					String code = parser.getAttributeValue(null, "code");
					String caption = parser.getAttributeValue(null, "caption");
					String pageType = parser
							.getAttributeValue(null, "pageType");

					detail.setId(id);
					detail.setCode(code);
					detail.setCaption(caption);
					detail.setPageType(pageType);
					detailList.add(detail);
				}
				break;
			}
			event = parser.next();
		}
		return groupChilds;
	}

	// /**
	// * 解析主菜单格式
	// * @param is
	// * @return
	// * @throws Exception
	// */
	// public static List<Object> receiverMainMenuLists(InputStream is)
	// throws Exception {
	// List<Object> allMenus = new ArrayList<Object>();
	// List<MenuFolder> menuFolders;
	// List<MenuModule> menuModules;
	// List<MenuPage> menuPages;
	// MenuFolder menuFolder = null;
	// MenuGroup menuGroup = null;
	// MenuModule menuModule = null;
	// MenuPage menuPage = null;
	// boolean inGroup = false;
	// boolean inFolder = false;
	// XmlPullParser parser = Xml.newPullParser();
	// parser.setInput(is, "UTF-8");
	// int event = parser.getEventType();
	// String tempName = null;
	// while (event != XmlPullParser.END_DOCUMENT) {
	// switch (event) {
	// case XmlPullParser.START_TAG:
	// tempName = parser.getName();
	// if("group".equalsIgnoreCase(tempName)) {
	// inGroup = true;
	// menuGroup = new MenuGroup();
	// menuGroup.caption = parser.getAttributeValue(null, "caption");
	// menuGroup.barImg1 = parser.getAttributeValue(null, "barImg1");
	// menuGroup.barImg2 = parser.getAttributeValue(null, "barImg2");
	// menuGroup.itemImg1 = parser.getAttributeValue(null, "itemImg1");
	// menuGroup.itemImg2 = parser.getAttributeValue(null, "itemImg2");
	// menuGroup.bgImg = parser.getAttributeValue(null, "bgImg");
	// allMenus.add(menuGroup);
	// }else if("module".equalsIgnoreCase(tempName)) {
	// if(inFolder) {
	//
	// }
	//
	// }else if("folder".equalsIgnoreCase(tempName)) {
	// if(inFolder){
	//
	// }
	// }
	//
	//
	// break;
	// case XmlPullParser.END_TAG:
	//
	// break;
	// }
	// event = parser.next();
	// }
	// return null;
	// }

	/**
	 * 解析主菜单格式
	 * object含有两种类型， MenuGroup and MenuModule
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static List<Object> receiverMainMenuLists(InputStream is)
			throws Exception {
		List<Object> allMenus = new ArrayList<Object>();
		MenuGroup menuGroup = null;
		MenuModule menuModule = null;
		MenuPage menuPage = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(is);
		Element element = document.getDocumentElement();
		NodeList rootList = element.getChildNodes();
		for (int i = 0; i < rootList.getLength(); i++) {
			Node ele = rootList.item(i);
			if (ele.getNodeType() == Node.ELEMENT_NODE) {
				if ("group".equalsIgnoreCase(ele.getNodeName())) {
					Element ele1 = (Element) ele;
					menuGroup = new MenuGroup();
					allMenus.add(menuGroup);
					menuGroup.caption = ele1.getAttribute("caption");// todo
					menuGroup.barImg1 = ele1.getAttribute("barImg1");
					menuGroup.barImg2 = ele1.getAttribute("barImg2");
					menuGroup.itemImg1 = ele1.getAttribute("itemImg1");
					menuGroup.itemImg2 = ele1.getAttribute("itemImg2");
					// System.out.println(ele1.getAttribute("caption"));
					if (ele1.hasChildNodes()) {
						NodeList nodeList1 = ele1.getChildNodes();
						for (int j = 0; j < nodeList1.getLength(); j++) {
							Node node = nodeList1.item(j);
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								Element ele2 = (Element) node;
								if ("module".equalsIgnoreCase(ele2
										.getNodeName())) {
									// System.out.println(ele2.getAttribute("caption"));
									menuModule = new MenuModule();
									menuGroup.menuModules.add(menuModule);
									menuModule.caption = ele2
											.getAttribute("caption");// todo
									menuModule.id = ele2.getAttribute("id");
									menuModule.mClass = ele2
											.getAttribute("class");
									menuModule.barImg1 = ele2.getAttribute("barImg1");
									menuModule.barImg2 = ele2.getAttribute("barImg2");
									menuModule.itemImg1 = ele2.getAttribute("itemImg1");
									menuModule.itemImg2 = ele2.getAttribute("itemImg2");
									if (ele2.hasChildNodes()) {
										NodeList nodeList2 = ele2
												.getChildNodes();
										for (int k = 0; k < nodeList2
												.getLength(); k++) {
											Node node2 = nodeList2.item(k);
											if (node2.getNodeType() == Node.ELEMENT_NODE) {
												Element element2 = (Element) node2;
												if ("page"
														.equalsIgnoreCase(element2
																.getNodeName())) {
													menuPage = new MenuPage();
													menuModule.menuPages
															.add(menuPage);
													menuPage.caption = element2
															.getAttribute("caption");// todo
													menuPage.id = element2
															.getAttribute("id");
													menuPage.code = element2
															.getAttribute("code");

													// System.out.println(element2.getAttribute("caption"));
												}
											}
										}
									}
								} else if ("folder".equalsIgnoreCase(ele2
										.getNodeName())) {
									// System.out.println(ele2.getAttribute("caption"));
//									menuFolder = new MenuFolder();
//									menuFolder.caption = ele2
//											.getAttribute("caption");// todo
//
//									menuGroup.menuFolders.add(menuFolder);
//									readdFolder(ele2, menuFolder);
								}
							}
						}
					}
				} else if ("module".equalsIgnoreCase(ele.getNodeName())) {
					Element element2 = (Element) ele;
					menuModule = new MenuModule();
					menuModule.caption = element2.getAttribute("caption");// todo
					menuModule.id = element2.getAttribute("id");
					menuModule.mClass = element2.getAttribute("class");
					menuModule.barImg1 = element2.getAttribute("barImg1");
					menuModule.barImg2 = element2.getAttribute("barImg2");
					menuModule.itemImg1 = element2.getAttribute("itemImg1");
					menuModule.itemImg2 = element2.getAttribute("itemImg2");
					// System.out.println(element2.getAttribute("caption"));
					allMenus.add(menuModule);

					if (element2.hasChildNodes()) {
						NodeList nodeList = element2.getChildNodes();
						for (int p = 0; p < nodeList.getLength(); p++) {
							Node node = nodeList.item(p);
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								Element element3 = (Element) node;
								if ("page".equalsIgnoreCase(element3
										.getNodeName())) {
									menuPage = new MenuPage();
									menuModule.menuPages.add(menuPage);
									menuPage.caption = element3
											.getAttribute("caption");// todo
									menuPage.id = element3.getAttribute("id");
									menuPage.code = element3
											.getAttribute("code");
									// System.out.println("111111111:" +
									// element3.getAttribute("caption"));
								}
							}
						}
					}

				}
			}
		}
		return allMenus;
	}

//	private static void readdFolder(Element ele2, MenuFolder menuFolder) {
//		if (ele2.hasChildNodes()) {
//			NodeList nodeList = ele2.getChildNodes();
//			for (int i = 0; i < nodeList.getLength(); i++) {
//				Node node = nodeList.item(i);
//				if (node.getNodeType() == Node.ELEMENT_NODE) {
//					Element ele = (Element) node;
//					if ("folder".equalsIgnoreCase(ele.getNodeName())) {
//						MenuFolder menuFolder2 = new MenuFolder();
//						menuFolder.menuFolders.add(menuFolder2);
//						menuFolder2.caption = ele.getAttribute("caption");// todo
//						readdFolder(ele, menuFolder2);
////						System.out.println(ele.getAttribute("caption"));
//					} else if ("module".equalsIgnoreCase(ele.getNodeName())) {
//						MenuModule menuModule = new MenuModule();
//						menuFolder.menuModules.add(menuModule);
//						menuModule.caption = ele.getAttribute("caption");// todo
//						menuModule.id = ele.getAttribute("id");
//						menuModule.mClass = ele.getAttribute("class");
//
//						// System.out.println(ele.getAttribute("caption"));
//					}
//				}
//			}
//		}
//	}
	public static SinopecMenu receiverBeanMainMenuLists(InputStream is)throws Exception{
		Document document;
		SinopecMenu menu = null;
		SinopecMenuGroup menuGroup = null;
		SinopecMenuModule menuModule = null;
		SinopecMenuPage menuPage = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();// 创建xml工厂
		DocumentBuilder builder = factory.newDocumentBuilder();// 拿到工厂的实例
		document = builder.parse(is);// 生成document对象
		Element element = document.getDocumentElement();
		NodeList rootList = element.getChildNodes();
		for (int i = 0; i < rootList.getLength(); i++) {//menu 下的所有节点
			Node ele = rootList.item(i);
			if ("menu".equalsIgnoreCase(ele.getNodeName())) {
				Element elmenu = (Element) ele;
				menu = new SinopecMenu();
//				menu.layout = elmenu.getAttribute("layout");
				menu.layout = "all";//test
				menu.notification =elmenu.getAttribute("notification");
				menu.level = elmenu.getAttribute("level");
				menu.version = elmenu.getAttribute("version");
				menu.columns = "3";//test
//				elmenu.getAttribute("columns");
				menu.itemTemplate = elmenu.getAttribute("itemTemplate");
			if (ele.hasChildNodes()){
			NodeList nodeLists = ele.getChildNodes();
			for (int g = 0; g < nodeLists.getLength(); g++) {//TDTO 这是group下的所有子节点
				Node eleg = nodeLists.item(g);
				if ("group".equalsIgnoreCase(eleg.getNodeName())) {
						Element ele1 = (Element) eleg;
						menuGroup = new SinopecMenuGroup();
						menuGroup.layout = ele1.getAttribute("layout");

						menuGroup.notification = ele1.getAttribute("notification");

						menuGroup.level = ele1.getAttribute("level");
						
						menuGroup.caption = ele1.getAttribute("caption");
						 
						menuGroup.rows = ele1.getAttribute("rows");
						 
						menuGroup.columns = ele1.getAttribute("columns");
						 
						menuGroup.barImg1 = ele1.getAttribute("barImg1");
						
						menuGroup.barImg2 = ele1.getAttribute("barImg2");
						 
						menuGroup.bgImg = ele1.getAttribute("bgImg");
						 
						menuGroup.itemImg1 = ele1.getAttribute("itemImg1");
						
						menuGroup.itemImg2 = ele1.getAttribute("itemImg2");
						
						menuGroup.id = ele1.getAttribute("id");
						
						menuGroup.itemTemplate = ele1.getAttribute("itemTemplate");
						menuGroup.subModuleId = ele1.getAttribute("subModuleId");
						//新增底部是否展示
						menuGroup.bottomShow = ele1.getAttribute("bottomShow");
						
						menu.menuObject.add(menuGroup);
						if (eleg.hasChildNodes()) {
							NodeList nodeList1 = eleg.getChildNodes();
							for (int j = 0; j < nodeList1.getLength(); j++) {//module下所有的节点
								Node node = nodeList1.item(j);
								if (node.getNodeType() == Node.ELEMENT_NODE) {
									Element ele2 = (Element) node;
//<module id='1' notification='1' class='approval' caption='公文' itemImg1='approval_oa_item1.png' itemImg2='approval_oa_item2.png'bgImg=''>
//<module id="portal" class="site" caption="公司门户"itemImg1="site_portal_item1.png" itemImg2="site_portal_item2.png"bgImg="site_portal_background.png">		
//<module id='1' class='document' caption='文档管理'barImg1='document_management_bar1.png' barImg2='document_management_bar2.png'itemImg1='document_management_item1.png' itemImg2='document_management_item2.png'bgImg=''>
//<module id='1' class='document' caption='文档管理'barImg1='document_management_bar1.png' barImg2='document_management_bar2.png'itemImg1='document_management_item1.png' itemImg2='document_management_item2.png'bgImg=''>
									if ("module".equalsIgnoreCase(ele2.getNodeName())) {
										menuModule = new SinopecMenuModule();
										menuGroup.menuobjObjects.add(menuModule);
										menuModule.id = ele2.getAttribute("id");// todo
										
										menuModule.notification = ele2.getAttribute("notification");
//										 if(menuModule.notification.equals("1")){
//											 DataCache.numsOfMoudleHasNotification ++ ;
//										 }
										menuModule.caption = ele2.getAttribute("caption");
										 
										menuModule.bgImg = ele2.getAttribute("bgImg");
										 
										menuModule.mClass = ele2.getAttribute("class");
												
										menuModule.barImg1 = ele2.getAttribute("barImg1");
										 
										menuModule.barImg2 = ele2.getAttribute("barImg2");
										 
										menuModule.itemImg1 = ele2.getAttribute("itemImg1");
										 
										menuModule.itemImg2 = ele2.getAttribute("itemImg2");
												
										if (ele2.hasChildNodes()) {
											NodeList nodeList2 = ele2.getChildNodes();
//											<page id='1-1-2-4' code='chooseUser' caption='选择人员'></page>
//											<page id='31' code='list' caption='列表' pageType='1'></page>
											for (int k = 0; k < nodeList2.getLength(); k++) {//遍历最后的节点
												Node node2 = nodeList2.item(k);
												if (node2.getNodeType() == Node.ELEMENT_NODE) {
													Element element2 = (Element) node2;
													if ("page".equalsIgnoreCase(element2.getNodeName())) {
														menuPage = new SinopecMenuPage();
																
														menuPage.caption = element2.getAttribute("caption");
//														 System.out.println(element2.getAttribute("caption"));		
														menuPage.id = element2.getAttribute("id");
//														 System.out.println(element2.getAttribute("id"));		
														menuPage.code = element2.getAttribute("code");
														if(menuPage.code.equalsIgnoreCase("notification")){
															 DataCache.numsOfMoudleHasNotification ++ ;
														 }
//														 System.out.println(element2.getAttribute("code"));
														menuPage.pageType = element2.getAttribute("pageType");
//														 System.out.println(element2.getAttribute("pageType"));
														menuModule.menuPages.add(menuPage);
													}
												}
											}
										}
									} else if ("group".equalsIgnoreCase(ele2.getNodeName())) {
										SinopecMenuGroup sinopecMenuGroup = new SinopecMenuGroup();
										
										sinopecMenuGroup.layout = ele2.getAttribute("layout");

										sinopecMenuGroup.notification = ele2.getAttribute("notification");

										sinopecMenuGroup.level = ele2.getAttribute("level");
										
										sinopecMenuGroup.caption = ele2.getAttribute("caption");
										 
										sinopecMenuGroup.rows = ele2.getAttribute("rows");
										 
										sinopecMenuGroup.columns = ele2.getAttribute("columns");
										 
										sinopecMenuGroup.barImg1 = ele2.getAttribute("barImg1");
										
										sinopecMenuGroup.barImg2 = ele2.getAttribute("barImg2");
										 
										sinopecMenuGroup.bgImg = ele2.getAttribute("bgImg");
										 
										sinopecMenuGroup.itemImg1 = ele2.getAttribute("itemImg1");
										
										sinopecMenuGroup.itemImg2 = ele2.getAttribute("itemImg2");
										
										sinopecMenuGroup.id = ele2.getAttribute("id");
										
										sinopecMenuGroup.itemTemplate = ele2.getAttribute("itemTemplate");
										
										sinopecMenuGroup.subModuleId = ele2.getAttribute("subModuleId");
										
//										menuGroup.menuFolders.add(menu);
										menuGroup.menuobjObjects.add(sinopecMenuGroup);
										readdFolder(ele2, sinopecMenuGroup);
									}else if("groupModule".equalsIgnoreCase(ele2.getNodeName())) {
										menuModule = new SinopecMenuModule();
										menuGroup.menuobjObjects.add(menuModule);
										menuModule.id = ele2.getAttribute("id");// todo
										
										menuModule.notification = ele2.getAttribute("notification");
										 
										menuModule.caption = ele2.getAttribute("caption");
										 
										menuModule.bgImg = ele2.getAttribute("bgImg");
										 
										menuModule.mClass = ele2.getAttribute("class");
												
										menuModule.barImg1 = ele2.getAttribute("barImg1");
										 
										menuModule.barImg2 = ele2.getAttribute("barImg2");
										 
										menuModule.itemImg1 = ele2.getAttribute("itemImg1");
										 
										menuModule.itemImg2 = ele2.getAttribute("itemImg2");
												
										menuModule.isGroupModule = true;
										if (ele2.hasChildNodes()) {
											NodeList nodeList2 = ele2.getChildNodes();
//											<page id='1-1-2-4' code='chooseUser' caption='选择人员'></page>
//											<page id='31' code='list' caption='列表' pageType='1'></page>
											for (int k = 0; k < nodeList2.getLength(); k++) {//遍历最后的节点
												Node node2 = nodeList2.item(k);
												if (node2.getNodeType() == Node.ELEMENT_NODE) {
													Element element2 = (Element) node2;
													if ("page".equalsIgnoreCase(element2.getNodeName())) {
														menuPage = new SinopecMenuPage();
																
														menuPage.caption = element2.getAttribute("caption");
//														 System.out.println(element2.getAttribute("caption"));		
														menuPage.id = element2.getAttribute("id");
//														 System.out.println(element2.getAttribute("id"));		
														menuPage.code = element2.getAttribute("code");
														if(menuPage.code.equalsIgnoreCase("notification")){
															 DataCache.numsOfMoudleHasNotification ++ ;
														 }
//														 System.out.println(element2.getAttribute("code"));
														menuPage.pageType = element2.getAttribute("pageType");
//														 System.out.println(element2.getAttribute("pageType"));
														menuModule.menuPages.add(menuPage);
													}
												}
											}
										}
										for (int q = 0; q < menuGroup.menuobjObjects.size(); q++) {//added by wangst
											SinopecMenuModule moduleItem = (SinopecMenuModule) menuGroup.menuobjObjects.get(j);
											Constants.setBroadcastNameNewPairs(moduleItem.caption);
										}
									}
								}
							}
						}
					} else if ("module".equalsIgnoreCase(eleg.getNodeName())) {
//<module id="email" class="dpemail" caption="邮箱"barImg1="dpemail_email_bar1.png" barImg2="dpemail_email_bar2.png"itemImg1="dpemail_email_item1.png" itemImg2="dpemail_email_item2.png"bgImg="dpemail_email_background.png" originalDoc="0">
						Element element2 = (Element) eleg;
						menuModule = new SinopecMenuModule();
						menuModule.caption = element2.getAttribute("caption");// todo
						
						menuModule.id = element2.getAttribute("id");
						
						menuModule.notification = element2.getAttribute("notification");
//						 if(menuModule.notification.equals("1")){
//							 DataCache.numsOfMoudleHasNotification ++ ;
//						 }
						menuModule.mClass = element2.getAttribute("class");
						
						menuModule.barImg1 = element2.getAttribute("barImg1");
						
						menuModule.barImg2 = element2.getAttribute("barImg2");
						
						menuModule.itemImg1 = element2.getAttribute("itemImg1");
						
						menuModule.itemImg2 = element2.getAttribute("itemImg2");
						
						menuModule.bgImg = element2.getAttribute("bgImg");
						
						menuModule.originalDoc = element2.getAttribute("originalDoc");
						//新增底部是否展示
						menuModule.bottomShow = element2.getAttribute("bottomShow");
						menu.menuObject.add(menuModule);

						if (element2.hasChildNodes()) {
							NodeList nodeList = element2.getChildNodes();
							for (int p = 0; p < nodeList.getLength(); p++) {
//								<page id='33' code='attachment' caption='附件' pageType='1'></page>
								Node node = nodeList.item(p);
								if (node.getNodeType() == Node.ELEMENT_NODE) {
									Element element3 = (Element) node;
									if ("page".equalsIgnoreCase(element3.getNodeName())) {
											
										menuPage = new SinopecMenuPage();
										menuPage.caption = element3.getAttribute("caption");// todo
//										System.out.println(element3.getAttribute("caption"));
										menuPage.id = element3.getAttribute("id");
//										System.out.println(element3.getAttribute("id"));	
										
										menuPage.code = element3.getAttribute("code");
										if(menuPage.code.equalsIgnoreCase("notification")){
											 DataCache.numsOfMoudleHasNotification ++ ;
										 }
//										System.out.println(element3.getAttribute("code"));
										
										menuPage.pageType = element3.getAttribute("pageType");
//										System.out.println(element3.getAttribute("pageType"));	
										menuModule.menuPages.add(menuPage);
									}
								}
							}
						}

					}
				}
			}
			}
		 }
		return menu;
	}
	
	private static void readdFolder(Element group, SinopecMenuGroup menuGroup) {
		if (group.hasChildNodes()) {
			NodeList nodeList = group.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element ele = (Element) node;
					if ("group".equalsIgnoreCase(ele.getNodeName())) {
						SinopecMenuGroup sinopecMenuGroup = new SinopecMenuGroup();
						menuGroup.menuobjObjects.add(sinopecMenuGroup);
						sinopecMenuGroup.layout = ele.getAttribute("layout");

						sinopecMenuGroup.notification = ele.getAttribute("notification");

						sinopecMenuGroup.level = ele.getAttribute("level");
						
						sinopecMenuGroup.caption = ele.getAttribute("caption");
						 
						sinopecMenuGroup.rows = ele.getAttribute("rows");
						 
						sinopecMenuGroup.columns = ele.getAttribute("columns");
						 
						sinopecMenuGroup.barImg1 = ele.getAttribute("barImg1");
						
						sinopecMenuGroup.barImg2 = ele.getAttribute("barImg2");
						 
						sinopecMenuGroup.bgImg = ele.getAttribute("bgImg");
						 
						sinopecMenuGroup.itemImg1 = ele.getAttribute("itemImg1");
						
						sinopecMenuGroup.itemImg2 = ele.getAttribute("itemImg2");
						
						sinopecMenuGroup.id = ele.getAttribute("id");
						
						sinopecMenuGroup.subModuleId = ele.getAttribute("subModuleId");
						
						sinopecMenuGroup.itemTemplate = ele.getAttribute("itemTemplate");
						 readdFolder(ele, sinopecMenuGroup);
						 System.out.println(ele.getAttribute("caption"));
					} else if ("module".equalsIgnoreCase(ele.getNodeName())) {
						SinopecMenuModule menuModule = new SinopecMenuModule();
						menuGroup.menuobjObjects.add(menuModule);
						menuModule.id = ele.getAttribute("id");// todo
						menuModule.notification = ele.getAttribute("notification");
						menuModule.caption = ele.getAttribute("caption");
						 
						menuModule.bgImg = ele.getAttribute("bgImg");
						 
						menuModule.mClass = ele.getAttribute("class");
								
						menuModule.barImg1 = ele.getAttribute("barImg1");
						 
						menuModule.barImg2 = ele.getAttribute("barImg2");
						 
						menuModule.itemImg1 = ele.getAttribute("itemImg1");
						 
						menuModule.itemImg2 = ele.getAttribute("itemImg2");

						if (ele.hasChildNodes()) {
							NodeList nodeList2 = ele.getChildNodes();
//							<page id='1-1-2-4' code='chooseUser' caption='选择人员'></page>
//							<page id='31' code='list' caption='列表' pageType='1'></page>
							for (int k = 0; k < nodeList2.getLength(); k++) {//遍历最后的节点
								Node node2 = nodeList2.item(k);
								if (node2.getNodeType() == Node.ELEMENT_NODE) {
									Element element2 = (Element) node2;
									if ("page".equalsIgnoreCase(element2.getNodeName())) {
										SinopecMenuPage menuPage = new SinopecMenuPage();
												
										menuPage.caption = element2.getAttribute("caption");// todo
//										 System.out.println(element2.getAttribute("caption"));		
										menuPage.id = element2.getAttribute("id");
//										 System.out.println(element2.getAttribute("id"));		
										menuPage.code = element2.getAttribute("code");
//										 System.out.println(element2.getAttribute("code"));
										menuPage.pageType = element2.getAttribute("pageType");
//										 System.out.println(element2.getAttribute("pageType"));
										menuModule.menuPages.add(menuPage);
									}
								}
							}
						}
						
					}
				}
			}
		}
	}
	
	
	public static SinopecMenu receiveMenuAuth(InputStream is) throws Exception{
		Document document;
		SinopecMenu menu = new SinopecMenu();
		SinopecMenuGroup menuGroup = null;
		SinopecMenuModule menuModule = null;
		SinopecMenuPage menuPage = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();// 创建xml工厂
		DocumentBuilder builder = factory.newDocumentBuilder();// 拿到工厂的实例
		
		
		
//		String sdString=StreamUtils.retrieveContent(is);
//		 System.out.println(sdString);
		 
		 
		 
		document = builder.parse(is);// 生成document对象
		Element element = document.getDocumentElement();
		NodeList rootList = element.getChildNodes();
		NodeList menuList = document.getElementsByTagName("menu");
		if (menuList==null) {
			menu.menuAuthType = MenuAuthType.CLAERMENU;
			return menu;
		}
		if(menuList.getLength()>0) {
			NodeList groupList = document.getElementsByTagName("group");
			NodeList moduleList = document.getElementsByTagName("module");
			if(groupList.getLength() ==0 && moduleList.getLength() ==0) {
				menu.menuAuthType = MenuAuthType.ALLMENU;
				return menu;
			}
		}else {
			menu.menuAuthType = MenuAuthType.NONEMENU;
			return menu;
		}
		for (int i = 0; i < rootList.getLength(); i++) {//menu 下的所有节点
			Node ele = rootList.item(i);
			if ("menu".equalsIgnoreCase(ele.getNodeName())) {
				Element elmenu = (Element) ele;
				menu = new SinopecMenu();
				menu.menuAuthType = MenuAuthType.MATCHMENU;
				menu.layout = elmenu.getAttribute("layout");
				menu.notification =elmenu.getAttribute("notification");
				menu.level = elmenu.getAttribute("level");
				menu.version = elmenu.getAttribute("version");
			if (ele.hasChildNodes()){
			NodeList nodeLists = ele.getChildNodes();
			for (int g = 0; g < nodeLists.getLength(); g++) {//TDTO 这是group下的所有子节点
				Node eleg = nodeLists.item(g);
				if ("group".equalsIgnoreCase(eleg.getNodeName())) {
						Element ele1 = (Element) eleg;
						menuGroup = new SinopecMenuGroup();
						menuGroup.layout = ele1.getAttribute("layout");

						menuGroup.notification = ele1.getAttribute("notification");

						menuGroup.level = ele1.getAttribute("level");
						
						menuGroup.caption = ele1.getAttribute("caption");
						 
						menuGroup.rows = ele1.getAttribute("rows");
						 
						menuGroup.columns = ele1.getAttribute("columns");
						 
						menuGroup.barImg1 = ele1.getAttribute("barImg1");
						
						menuGroup.barImg2 = ele1.getAttribute("barImg2");
						 
						menuGroup.bgImg = ele1.getAttribute("bgImg");
						 
						menuGroup.itemImg1 = ele1.getAttribute("itemImg1");
						
						menuGroup.itemImg2 = ele1.getAttribute("itemImg2");
						
						menuGroup.id = ele1.getAttribute("id");
						
						menuGroup.itemTemplate = ele1.getAttribute("itemTemplate");
						menuGroup.subModuleId = ele1.getAttribute("subModuleId");
						
						menu.menuObject.add(menuGroup);
						if (eleg.hasChildNodes()) {
							NodeList nodeList1 = eleg.getChildNodes();
							for (int j = 0; j < nodeList1.getLength(); j++) {//module下所有的节点
								Node node = nodeList1.item(j);
								if (node.getNodeType() == Node.ELEMENT_NODE) {
									Element ele2 = (Element) node;
//<module id='1' notification='1' class='approval' caption='公文' itemImg1='approval_oa_item1.png' itemImg2='approval_oa_item2.png'bgImg=''>
//<module id="portal" class="site" caption="公司门户"itemImg1="site_portal_item1.png" itemImg2="site_portal_item2.png"bgImg="site_portal_background.png">		
//<module id='1' class='document' caption='文档管理'barImg1='document_management_bar1.png' barImg2='document_management_bar2.png'itemImg1='document_management_item1.png' itemImg2='document_management_item2.png'bgImg=''>
//<module id='1' class='document' caption='文档管理'barImg1='document_management_bar1.png' barImg2='document_management_bar2.png'itemImg1='document_management_item1.png' itemImg2='document_management_item2.png'bgImg=''>
									if ("module".equalsIgnoreCase(ele2.getNodeName())) {
										menuModule = new SinopecMenuModule();
										menuGroup.menuobjObjects.add(menuModule);
										menuModule.id = ele2.getAttribute("id");// todo
										
										menuModule.notification = ele2.getAttribute("notification");
										 
										menuModule.caption = ele2.getAttribute("caption");
										 
										menuModule.bgImg = ele2.getAttribute("bgImg");
										 
										menuModule.mClass = ele2.getAttribute("class");
												
										menuModule.barImg1 = ele2.getAttribute("barImg1");
										 
										menuModule.barImg2 = ele2.getAttribute("barImg2");
										 
										menuModule.itemImg1 = ele2.getAttribute("itemImg1");
										 
										menuModule.itemImg2 = ele2.getAttribute("itemImg2");
												
										if (ele2.hasChildNodes()) {
											NodeList nodeList2 = ele2.getChildNodes();
//											<page id='1-1-2-4' code='chooseUser' caption='选择人员'></page>
//											<page id='31' code='list' caption='列表' pageType='1'></page>
											for (int k = 0; k < nodeList2.getLength(); k++) {//遍历最后的节点
												Node node2 = nodeList2.item(k);
												if (node2.getNodeType() == Node.ELEMENT_NODE) {
													Element element2 = (Element) node2;
													if ("page".equalsIgnoreCase(element2.getNodeName())) {
														menuPage = new SinopecMenuPage();
																
														menuPage.caption = element2.getAttribute("caption");// todo
//														 System.out.println(element2.getAttribute("caption"));		
														menuPage.id = element2.getAttribute("id");
//														 System.out.println(element2.getAttribute("id"));		
														menuPage.code = element2.getAttribute("code");
//														 System.out.println(element2.getAttribute("code"));
														menuPage.pageType = element2.getAttribute("pageType");
//														 System.out.println(element2.getAttribute("pageType"));
														menuModule.menuPages.add(menuPage);
													}
												}
											}
										}
									} else if ("group".equalsIgnoreCase(ele2.getNodeName())) {
										SinopecMenuGroup sinopecMenuGroup = new SinopecMenuGroup();
										
										sinopecMenuGroup.layout = ele2.getAttribute("layout");

										sinopecMenuGroup.notification = ele2.getAttribute("notification");

										sinopecMenuGroup.level = ele2.getAttribute("level");
										
										sinopecMenuGroup.caption = ele2.getAttribute("caption");
										 
										sinopecMenuGroup.rows = ele2.getAttribute("rows");
										 
										sinopecMenuGroup.columns = ele2.getAttribute("columns");
										 
										sinopecMenuGroup.barImg1 = ele2.getAttribute("barImg1");
										
										sinopecMenuGroup.barImg2 = ele2.getAttribute("barImg2");
										 
										sinopecMenuGroup.bgImg = ele2.getAttribute("bgImg");
										 
										sinopecMenuGroup.itemImg1 = ele2.getAttribute("itemImg1");
										
										sinopecMenuGroup.itemImg2 = ele2.getAttribute("itemImg2");
										
										sinopecMenuGroup.id = ele2.getAttribute("id");
										
										sinopecMenuGroup.itemTemplate = ele2.getAttribute("itemTemplate");
										
										sinopecMenuGroup.subModuleId = ele2.getAttribute("subModuleId");
										
//										menuGroup.menuFolders.add(menu);
										menuGroup.menuobjObjects.add(sinopecMenuGroup);
										readdFolder(ele2, sinopecMenuGroup);
									}
								}
							}
						}
					} else if ("module".equalsIgnoreCase(eleg.getNodeName())) {
//<module id="email" class="dpemail" caption="邮箱"barImg1="dpemail_email_bar1.png" barImg2="dpemail_email_bar2.png"itemImg1="dpemail_email_item1.png" itemImg2="dpemail_email_item2.png"bgImg="dpemail_email_background.png" originalDoc="0">
						Element element2 = (Element) eleg;
						menuModule = new SinopecMenuModule();
						menuModule.caption = element2.getAttribute("caption");// todo
						
						menuModule.id = element2.getAttribute("id");
						
						menuModule.mClass = element2.getAttribute("class");
						
						menuModule.barImg1 = element2.getAttribute("barImg1");
						
						menuModule.barImg2 = element2.getAttribute("barImg2");
						
						menuModule.itemImg1 = element2.getAttribute("itemImg1");
						
						menuModule.itemImg2 = element2.getAttribute("itemImg2");
						
						menuModule.bgImg = element2.getAttribute("bgImg");
						
						menuModule.originalDoc = element2.getAttribute("originalDoc");
						menu.menuObject.add(menuModule);

						if (element2.hasChildNodes()) {
							NodeList nodeList = element2.getChildNodes();
							for (int p = 0; p < nodeList.getLength(); p++) {
//								<page id='33' code='attachment' caption='附件' pageType='1'></page>
								Node node = nodeList.item(p);
								if (node.getNodeType() == Node.ELEMENT_NODE) {
									Element element3 = (Element) node;
									if ("page".equalsIgnoreCase(element3.getNodeName())) {
											
										menuPage = new SinopecMenuPage();
										menuPage.caption = element3.getAttribute("caption");// todo
//										System.out.println(element3.getAttribute("caption"));
										menuPage.id = element3.getAttribute("id");
//										System.out.println(element3.getAttribute("id"));	
										
										menuPage.code = element3.getAttribute("code");
//										System.out.println(element3.getAttribute("code"));
										
										menuPage.pageType = element3.getAttribute("pageType");
//										System.out.println(element3.getAttribute("pageType"));	
										menuModule.menuPages.add(menuPage);
									}
								}
							}
						}

					}
				}
			}
			}
		 }
		return menu;
	}
	
}
