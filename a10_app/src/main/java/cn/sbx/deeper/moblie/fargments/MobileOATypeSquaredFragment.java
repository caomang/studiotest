package cn.sbx.deeper.moblie.fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.sbx.deeper.moblie.domian.SinopecMenuGroup;
import cn.sbx.deeper.moblie.domian.SinopecMenuModule;
import cn.sbx.deeper.moblie.util.ImageLoader;
import cn.sbx.deeper.moblie.util.MenuTypeUtil;
import cn.sbx.deeper.moblie.util.UserInfo;
import petrochina.ghzy.a10fieldwork.R;

/**
 * 九宫格样式界面
 * @author terry.C
 *
 */
public class MobileOATypeSquaredFragment extends BaseFragment implements OnItemClickListener{

	private SinopecMenuGroup group;
	List<Object> objects = new ArrayList<Object>();
	SquaredTypeAdapter typeAdapter;
	private String menuDisplayType;
	int column = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageLoader = new ImageLoader(getActivity());
		Bundle bundle = getArguments();
		group = (SinopecMenuGroup) bundle
				.getSerializable("entry");
//		int pageIndex = bundle.getInt("pageIndex");
		objects = group.menuobjObjects;
		try {
			column = Integer.parseInt(group.columns);
		} catch (Exception e) {
			column = 0;
			e.printStackTrace();
		}
		if(group != null) {
			menuDisplayType = group.itemTemplate;
		}
		
		String ftitle = bundle.getString("ftitle");
		if(null != ftitle && !"".equalsIgnoreCase(ftitle)) {
			TextView tv = (TextView) getActivity().findViewById(R.id.tv_title);
			if(tv!=null) {
				tv.setText(ftitle);
			}
		}
		
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_type_squared, null);
		GridView gv_squared_menu = (GridView) view.findViewById(R.id.gv_squared_menu);
		if(column != 0) gv_squared_menu.setNumColumns(column);
		gv_squared_menu.setAdapter(typeAdapter = new SquaredTypeAdapter(objects));
		gv_squared_menu.setOnItemClickListener(this);
		TextView tv_squared_userinfo = (TextView) view.findViewById(R.id.tv_squared_userinfo);
		tv_squared_userinfo.setText(UserInfo.getInstance().getUsername());
		return view;
	}
	
	private ViewHolder holder;
	private ImageLoader imageLoader;
	static class ViewHolder {
		ImageView imageView;
		TextView text;
		TextView count;
	}
	public void perpareIndicator(ViewHolder view, String iconName,
			String iconName1, final String text, String count) {
		final ImageView ivIcon = view.imageView;
		TextView textView = view.text;
		TextView tv_msg_count = view.count;
		if (count == null || "0".equals(count) || "".equals(count)) {
			tv_msg_count.setVisibility(View.GONE);
		} else {
			tv_msg_count.setVisibility(View.VISIBLE);
		}
		textView.setText(text);
		imageLoader.displayImage2(iconName, ivIcon, R.drawable.ic_default_item1);
		
		switch (MenuTypeUtil.chooseMenuDisplayType(menuDisplayType)) {
		case PICTURE:
			textView.setVisibility(View.GONE);
			break;
		case WORD:
			ivIcon.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		
		
//		StateListDrawable drawable = skinManager.newSelectorFromResourceByMenu(getActivity(), iconName, iconName1, screenWidth/3, screenWidth/3);
//		ivIcon.setBackgroundDrawable(drawable);
	}
	
	private class SquaredTypeAdapter extends BaseAdapter {
		
		List<Object> menus = new ArrayList<Object>();
		
		public SquaredTypeAdapter(List<Object> menus) {
			for(Object object : menus) {
				if(object instanceof SinopecMenuModule) {
					if(((SinopecMenuModule)object).isGroupModule) {
						continue;
					}
				}
				this.menus.add(object);
			}
		}
		
		@Override
		public int getCount() {
			return menus.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.item_syn_nav_squared_menu, null);
//				convertView.setLayoutParams(new AbsListView.LayoutParams(
//						(int) getResources().getDimension(
//								R.dimen.main_menu_item_width),
//						(int) getResources().getDimension(
//								R.dimen.main_menu_item_height)));
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.iv_syn_icon);
				holder.text = (TextView) convertView
						.findViewById(R.id.tv_syn_text);
				holder.count = (TextView) convertView
						.findViewById(R.id.tv_msg_count);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String iconName = "";
			String iconname1 = "";
			String titleName = "";
			Object o = menus.get(position);
			if (o instanceof SinopecMenuGroup) {
				SinopecMenuGroup group = (SinopecMenuGroup) o;
				iconName = group.itemImg1;
				iconname1 = group.itemImg2;
				titleName = group.caption;
				perpareIndicator(holder, iconName, iconname1, titleName, "0");
			} else if (o instanceof SinopecMenuModule) {
				SinopecMenuModule module = (SinopecMenuModule) o;
				iconName = module.itemImg1;
				iconname1 = module.itemImg2;
				titleName = module.caption;
//				if (DataCache.taskCount.containsKey(((SinopecMenuModule) o).id)) {
//					int count = DataCache.taskCount.get(((SinopecMenuModule) o).id);
//					System.out.println("view in count : " + count);
//					perpareIndicator(holder, iconName, iconname1, titleName,
//							String.valueOf(count));
//				} else
					perpareIndicator(holder, iconName, iconname1, titleName, "0");
				// perpareIndicator(view, iconName,
				// titleName, "");
			}
			
			return convertView;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Object o = objects.get(arg2);
		if(o instanceof SinopecMenuGroup) {
			SinopecMenuGroup group = (SinopecMenuGroup) o;
			Bundle bundle = new Bundle();
			bundle.putSerializable("entry", group);
			bundle.putSerializable("ftitle", group.caption);
			navSquaredGroup(bundle);
		}else if(o instanceof SinopecMenuModule) {
//			Toast.makeText(getActivity(), "module clicked", 0).show();
			SinopecMenuModule module = (SinopecMenuModule) o;
			navdModule(module);
		}
	}
	
}
