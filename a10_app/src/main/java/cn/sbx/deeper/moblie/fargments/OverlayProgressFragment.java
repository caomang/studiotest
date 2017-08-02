package cn.sbx.deeper.moblie.fargments;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import petrochina.ghzy.a10fieldwork.R;


public class OverlayProgressFragment extends DialogFragment {
	private AsyncTask<?, ?, ?> asynTask;
	private String message;

	public static OverlayProgressFragment newInstance() {
		OverlayProgressFragment f = new OverlayProgressFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onCancel(dialog);
		if(asynTask != null)
		asynTask.cancel(true);
	}

	public void setAsynTask(AsyncTask<?, ?, ?> asynTask) {
		this.asynTask = asynTask;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getDialog().setCanceledOnTouchOutside(false);
		View _v = inflater.inflate(R.layout.fragment_overlay_progress, container, false);
		TextView tv_message = (TextView) _v.findViewById(R.id.tv_message);
		if (message == null || "".equals(message)) {
			tv_message.setText(getString(R.string.msg_wait));
		} else {
			tv_message.setText(message);
		}
		return _v;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (asynTask != null) {
			asynTask.cancel(true);
		}

	}
	@Override
	public int show(FragmentTransaction transaction, String tag) {
		return super.show(transaction, tag);
	}
}
