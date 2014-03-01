package com.exercise.utils.dial;

import java.util.List;

import com.exercise.utils.R;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class DialerFragment extends Fragment implements OnClickListener {
	
	final static String ARG_SCHEME = "scheme";
	final static String ARG_NUMBER = "number";
	
	private String mScheme;
	private String mNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
        	mScheme = savedInstanceState.getString(ARG_SCHEME);
        	mNumber = savedInstanceState.getString(ARG_NUMBER);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dial, container, false);
        
        view.findViewById(R.id.button_call).setOnClickListener(this);
        view.findViewById(R.id.button_dial).setOnClickListener(this);
        view.findViewById(R.id.button_phone_sip_setting).setOnClickListener(this);
        view.findViewById(R.id.button_safetone_sip_setting).setOnClickListener(this);
        view.findViewById(R.id.button_ptt).setOnClickListener(this);
        view.findViewById(R.id.button_ptt_stop).setOnClickListener(this);
        view.findViewById(R.id.button_sos).setOnClickListener(this);
        view.findViewById(R.id.button_ptt_set).setOnClickListener(this);
        view.findViewById(R.id.button_sos_set).setOnClickListener(this);
        
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        setScheme(mScheme);
        setNumber(mNumber);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mScheme = getScheme();
        mNumber = getNumber();
        outState.putString(ARG_SCHEME, mScheme);
        outState.putString(ARG_NUMBER, mNumber);
    }
    
	public void call(View view) {
		Intent intent = new Intent(Intent.ACTION_CALL, getUri());
		
//		intent.putExtra("fallback_behavior", 1);
		
		if (canStartActivity(intent))
			startActivity(intent);
	}

	public void dial(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL, getUri());
		
		if (canStartActivity(intent))
			startActivity(intent);
	}
	
	public void phoneSipSetting(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClassName("com.android.phone","com.android.phone.sip.SipSettings");     
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		if (canStartActivity(intent))
			startActivity(intent);
	}
	
	public void safetoneSipSetting(View view) {
		Intent intent = new Intent("net.safetone.voip.ui.action.PREFS_GLOBAL");

		if (canStartActivity(intent))
			startActivity(intent);
	}
	
	private boolean canStartActivity(Intent intent) {
		PackageManager packageManager = getActivity().getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		return activities.size() > 0;
	}
    
	private EditText getSchemeEditText() {
		return (EditText) getView().findViewById(R.id.edit_scheme);
	}
	
	private EditText getNumberEditText() {
		return (EditText) getView().findViewById(R.id.edit_num);
	}
	
    private void setScheme(String scheme) {
    	getSchemeEditText().setText(scheme);
    }
    
    private void setNumber(String number) {
    	getNumberEditText().setText(number);
    }

    private String getScheme() {
    	return getSchemeEditText().getText().toString();
    }
    
    private String getNumber() {
    	return getNumberEditText().getText().toString();
    }
    
	private Uri getUri() {
		return Uri.parse(getScheme() + ":" + getNumber());
	}
	
	public void ptt(View view) {
//		Intent intent = new Intent("net.safetone.intent.action.PTT");
//
//		if (canStartActivity(intent))
//			startActivity(intent);
		Intent intent = new Intent("android.intent.action.PTT_BUTTON");
		intent.putExtra(Intent.EXTRA_KEY_EVENT,
				new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_CAMERA));
		this.getActivity().sendBroadcast(intent);
	}
	
	public void pttStop(View view) {
//		Intent intent = new Intent("net.safetone.intent.action.HANGUP");
//
//		getActivity().sendBroadcast(intent);
		
		Intent intent = new Intent("android.intent.action.PTT_BUTTON");
		intent.putExtra(Intent.EXTRA_KEY_EVENT,
				new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_CAMERA));
		this.getActivity().sendBroadcast(intent);
	}
	
	public void sos(View view) {
//		Intent intent = new Intent("net.safetone.intent.action.SOS");
//
//		if (canStartActivity(intent))
//			startActivity(intent);
		
		Intent intent = new Intent("android.intent.action.SOS_BUTTON");
		intent.putExtra(Intent.EXTRA_KEY_EVENT,
				new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_CAMERA));
		this.getActivity().sendBroadcast(intent);
	}
	
    public void pttSetting(View view) {
		Intent intent = new Intent("net.safetone.intent.settings.PTT");

		if (canStartActivity(intent))
			startActivity(intent);
	}
    
    public void sosSetting(View view) {
		Intent intent = new Intent("net.safetone.intent.settings.SOS");

		if (canStartActivity(intent))
			startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_call: call(v); break;
		case R.id.button_dial: dial(v); break;
		case R.id.button_phone_sip_setting: phoneSipSetting(v); break;
		case R.id.button_safetone_sip_setting: safetoneSipSetting(v); break;
		case R.id.button_ptt: ptt(v); break;
		case R.id.button_ptt_stop: pttStop(v); break;
		case R.id.button_sos: sos(v); break;
		case R.id.button_ptt_set: pttSetting(v); break;
		case R.id.button_sos_set: sosSetting(v); break;
		default: break;
		}
	}
}