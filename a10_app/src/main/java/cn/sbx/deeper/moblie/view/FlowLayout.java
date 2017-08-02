package cn.sbx.deeper.moblie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 流式的标签布局
 * @author Administrator
 *
 */
public class FlowLayout extends ViewGroup{
	private int horizontalSpacing = 0;//水平间距
	private int verticalSpacing = 0;//行与行的垂直间距
	//用于存放所有line对象
	private ArrayList<Line> lineList = new ArrayList<Line>();
	private int width;
	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlowLayout(Context context) {
		super(context);
	}
	
	/**
	 * 设置水平间距
	 * @param horizontalSpacing
	 */
	public void setHorizontalSpacing(int horizontalSpacing){
		this.horizontalSpacing = horizontalSpacing;
	}
	public int getVerticalSpacing() {
		return verticalSpacing;
	}
	/**
	 * 设置垂直间距
	 * @param verticalSpacing
	 */
	public void setVerticalSpacing(int verticalSpacing) {
		this.verticalSpacing = verticalSpacing;
	}
	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	/**
	 * 遍历子View，进行分行的操作,就是排座位
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//清空lineList
		lineList.clear();
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		//2.得打除去padding的宽度,就是我们用于实际比较的总宽度
		int noPaddingWidth = width - getPaddingLeft()-getPaddingRight();
		
		//3.遍历所有子view，进行分行
		Line line = null;
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);//获取当前子view
			childView.measure(0, 0);//通知父view测量childView
			
			if(line==null){
				line = new Line();//如果不换行，还是同一个line，如果换行就变了
			}
			//4.如果当前line一个子view都木有，则直接放入line，不用比较，因为保证每行至少有一个子View
			if(line.getViewList().size()<2){
				line.addChild(childView);
			}else  {
				//5.如果当前line的宽+水平间距+childView宽如果大于noPaddingWidth,需要换行
				lineList.add(line);//先将之前的line存放起来
				
				line = new Line();//重新创建line，将childView放入到新行中
				line.addChild(childView);
			}
			
			//7.如果当前childView是最后一个子View，会造成最后一行line丢失
			if(i==(getChildCount()-1)){
				lineList.add(line);//将最后的一行line存放起来
			}
		}
		
		//for循环结束，lineList中就存放了所有的line，
		//计算layout所有行的时候需要的高度
//		int height = getPaddingTop()+getPaddingBottom();//首先加上padding值
		int height = 0;
		for (int i = 0; i < lineList.size(); i++) {
			height += lineList.get(i).getHeight();//再加上所有行的高度
		}
		height += (lineList.size()-1)*verticalSpacing;//最后加上所有行的垂直间距
				
		//向父View申请所需要的宽高
//		setMeasuredDimension(width, height-33*4);
		setMeasuredDimension(width, height);
	}
	
	/**
	 * 对lineList中的line的所有子View进行布局，就是让每个人坐到自己的位置上
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int paddingLeft = getPaddingLeft();
//		int paddingTop = getPaddingTop();
		int paddingTop = 0;
		
		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);//获取到当前的line对象
			
			//后面每一行的top值要相应的增加,当前行的top是上一行的top值+height+垂直间距
			if(i>0){
//				paddingTop += lineList.get(i-1).getHeight();
				paddingTop += lineList.get(i-1).getViewList().get(0).getMeasuredHeight();
				
			}
			
			ArrayList<View> viewList = line.getViewList();//获取line的子View集合
			
			//1.计算出留白的区域(宽和高):noPaddingWidht-当前line的宽度
			float remainSpace = getMeasuredWidth()-getPaddingLeft()-getPaddingRight()-line.getWidth();
			//2.计算出每个子View平均分到的宽度
			float perSpace = remainSpace/viewList.size();
			int remainSpace_height = getMeasuredHeight()-line.getHeight()*lineList.size();
			float perSpace_height = remainSpace_height/lineList.size();
			
			for (int j = 0; j < viewList.size(); j++) {
				View childView = viewList.get(j);//获取当前的子View
				//3.将每个子View得到宽度增加到原来的宽度上面
				int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth()+perSpace),MeasureSpec.EXACTLY);
				int widthMeasureSpec_h = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredHeight()+perSpace_height),MeasureSpec.EXACTLY);
//					
					childView.measure(widthMeasureSpec, 0);//重新测量childView
				
				if(j==0){
						if(viewList.size()==1){
							childView.layout(paddingLeft, paddingTop,width,
									paddingTop+childView.getMeasuredHeight());
						}else{
							//每行的第一个子View,需要靠左边摆放
							childView.layout(paddingLeft, paddingTop,width/2,
									paddingTop+childView.getMeasuredHeight());
						}
						
				}else {
					//摆放后面的子View，需要参考前一个子View的right
					View preView = viewList.get(j-1);//获取前一个子View
					int left = preView.getRight()+horizontalSpacing;//前一个VIew的right+水平间距
					childView.layout(width/2+paddingLeft, preView.getTop(),width-paddingLeft,preView.getBottom());
				}
			}
		}
	}

	/**
	 * 封装每行的数据，包括每行的子View，行宽和行高
	 * @author Administrator
	 *
	 */
	class Line{
		private ArrayList<View> viewList;//用来存放当前行的所有子TextView
		private int width;//当前行的所有子View的宽+水平间距
		private int height;//当前行的行高
		public Line(){
			viewList = new ArrayList<View>();
		}
		
		/**
		 * 存放子view到viewList中
		 * @param view
		 */
		public void addChild(View view){
			if(!viewList.contains(view)){
				
				//更新宽高
				if(viewList.size()==0){
					//说明没有子view，当前view是第一个加入的，此时是不需要加horizontalSpacing
					width = view.getMeasuredWidth();
				}else {
					//说明不是第一个，那么需要加上水平间距
					width += horizontalSpacing + view.getMeasuredWidth();
				}
				//height应该是所有子view中高度最大的那个
				height = Math.max(view.getMeasuredHeight(), height);
//				height = view.getMeasuredHeight();
				
				viewList.add(view);
			}
		}
		
		/**
		 * 获取子View的集合
		 * @return
		 */
		public ArrayList<View> getViewList(){
			return viewList;
		}
		/**
		 * 获取当前行的宽度
		 * @return
		 */
		public int getWidth(){
			return width;
		}
		/**
		 * 获取行高
		 * @return
		 */
		public int getHeight(){
			return height;
		}
	}
}
