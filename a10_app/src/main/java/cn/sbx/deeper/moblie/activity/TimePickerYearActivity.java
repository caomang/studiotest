package cn.sbx.deeper.moblie.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunboxsoft.office.view.wheel.WheelView;
import com.sunboxsoft.office.view.wheel.adapters.ArrayWheelAdapter;
import com.sunboxsoft.office.view.wheel.adapters.NumericWheelAdapter;

import java.util.Calendar;

import petrochina.ghzy.a10fieldwork.R;

/**
 * 滚轮选择时间的组件
 * 
 * @author terry.C
 * 
 */
public class TimePickerYearActivity extends BaseActivity implements OnClickListener{
	private WheelView wheel_year;
//	private WheelView wheel_month;
//	private WheelView wheel_day;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_year_layout);
		this.setFinishOnTouchOutside(false);
		Calendar calendar = Calendar.getInstance();
		
		wheel_year = (WheelView) findViewById(R.id.wheel_year);
//		wheel_month = (WheelView) findViewById(R.id.wheel_month);
//		wheel_day = (WheelView) findViewById(R.id.wheel_day);
		
//		OnWheelChangedListener listener = new OnWheelChangedListener() {
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//            	updateDays(wheel_year, wheel_month, wheel_day);
//            }
//        };
		
        
     // year
//        int curYear = calendar.get(Calendar.YEAR);
//        wheel_year.setViewAdapter(new DateNumericAdapter(this, curYear, curYear+20, 0));
//        wheel_year.setCurrentItem(curYear);
//        wheel_year.addChangingListener(listener);
        int curYear = calendar.get(Calendar.YEAR);
        wheel_year.setViewAdapter(new DateNumericAdapter(this, curYear-10, curYear+20, 10));
        wheel_year.setCurrentItem(10, true);
//        wheel_year.addChangingListener(listener);
        wheel_year.setCurrentItem(10);
//     // month
//        int curMonth = calendar.get(Calendar.MONTH);
//        String months[] = new String[] {"一月", "二月", "三月", "四月", "五月",
//                "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
//        wheel_month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
//        wheel_month.setCurrentItem(curMonth);
//        wheel_month.addChangingListener(listener);
//        
//      //day
//        updateDays(wheel_year, wheel_month, wheel_day);
//        wheel_day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 3);
//        
        LinearLayout time_layout=(LinearLayout)findViewById(R.id.time_layout);
        time_layout.setOnClickListener(this);
        Button bt_cheel_choose = (Button) findViewById(R.id.bt_cheel_choose);
        bt_cheel_choose.setOnClickListener(this);
        Button bt_cheel_cancel = (Button) findViewById(R.id.bt_cheel_cancel);
        bt_cheel_cancel.setOnClickListener(this);
	}
	
	/**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem()); 
        
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 3));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }
    
    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
    
    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_cheel_choose://选择
			Calendar calendar = Calendar.getInstance();
			Intent intent = new Intent();
			intent.putExtra("CHEEL_TIME", ((calendar.get(Calendar.YEAR)-10)+wheel_year.getCurrentItem()) + "");
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		case R.id.bt_cheel_cancel://取消
			Intent intent1 = new Intent();
			setResult(Activity.RESULT_OK, intent1);
			finish();
			break;
//		case R.id.time_layout:
//			Intent intent2 = new Intent();
//			setResult(Activity.RESULT_OK, intent2);
//			finish();
//			break;
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent2 = new Intent();
		setResult(Activity.RESULT_OK, intent2);
		finish();
	}
}
