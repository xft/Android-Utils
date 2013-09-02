package com.exercise.utils.wifi;

import java.util.List;

import com.exercise.utils.R;
import com.exercise.utils.util.IpAddress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WifiInfoFragment extends Fragment implements OnClickListener {
	private static final String TAG = "WlanInfoActivity";
	
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;
    private WifiManager wifiManager;
    
    private TextView iWifiState;
    private TextView iNetworkState;
    private TextView iSupplicantState;
    private TextView iRSSI;
    private TextView iBSSID;
    private TextView iSSID;
    private TextView iHiddenSSID;
    private TextView iIPAddr;
    private TextView iMACAddr;
    private TextView iNetworkId;
    private TextView iLinkSpeed;
    private TextView iScanList;
    private TextView iSupplicantStateHistory;
    
    private int textViewColor = Color.BLUE;
    
    long supplicantStateNotCompletedTime = 0;
    SupplicantState savedSupplicantState = SupplicantState.COMPLETED;
    String savedSupplicantStateString = "";
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
    	wlanInfoReceiver();
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.wifi_status, container, false);
        
        iWifiState = (TextView) view.findViewById(R.id.wifi_state);
        iNetworkState = (TextView) view.findViewById(R.id.network_state);
        iSupplicantState = (TextView) view.findViewById(R.id.supplicant_state);
        iRSSI = (TextView) view.findViewById(R.id.rssi);
        iBSSID = (TextView) view.findViewById(R.id.bssid);
        iSSID = (TextView) view.findViewById(R.id.ssid);
        iHiddenSSID = (TextView) view.findViewById(R.id.hidden_ssid);
        iIPAddr = (TextView) view.findViewById(R.id.ipaddr);
        iMACAddr = (TextView) view.findViewById(R.id.macaddr);
        iNetworkId = (TextView) view.findViewById(R.id.networkid);
        iLinkSpeed = (TextView) view.findViewById(R.id.link_speed);
        iScanList = (TextView) view.findViewById(R.id.scan_list);
        iSupplicantStateHistory = (TextView) view.findViewById(R.id.supplicant_state_history);
        
        view.findViewById(R.id.button_open_wlan).setOnClickListener(this);
        view.findViewById(R.id.button_close_wlan).setOnClickListener(this);
        
        return view;
    }
	
	@Override
	public void onResume() {
		super.onResume();

		getActivity().registerReceiver(broadcastReceiver, intentFilter);

		initStatus();
	}
	
    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(broadcastReceiver);
    }
    
    /* Open WLAN button */
    public void openWlan(View view) {
    	if (wifiManager == null)
    		return;
    	
    	wifiManager.setWifiEnabled(true);
    }
    
    /* Close WLAN button */
    public void closeWlan(View view) {
    	if (wifiManager == null)
    		return;
    	
    	wifiManager.setWifiEnabled(false);
    }
    
    /* WLAN  information broadcast receiver */
	private void wlanInfoReceiver() {
		intentFilter = new IntentFilter();

		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		intentFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        
		broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleEvent(context, intent);
            }
        };
	}
	
	/* handle the WLAN broadcast intent events */
    private void handleEvent(Context context, Intent intent) {
        String action = intent.getAction();
        
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
        	handleWifiStateChanged(intent);
        } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
        	handleScanResultsAvailable();
        } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
            handleSupplicantStateChanged(intent);
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
        	handleNetworkStateChanged(intent);
        } else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
        	handleSignalChanged(intent);
        } else if ((WifiManager.NETWORK_IDS_CHANGED_ACTION).equals(action)) {
        	handleNetworkIdsChanged();
        } else if (WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION.equals(action)) {
        	handleSupplicantConnectionChange(intent);
        } else {
            Log.e(TAG, "Received an unknown Wifi Intent");
        }
    }
    
    /* initialization the status show when Activity onResume */
    private void initStatus() {
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	updateInfo(wifiInfo);

        SupplicantState supplicantState = wifiInfo.getSupplicantState();
        setSupplicantStateText(supplicantState);
    }
    
    private void updateInfo(WifiInfo wifiInfo) {
    	if (wifiInfo == null)
    		return;
    	
        //setWifiStateText(wifiManager.getWifiState(), WifiManager.WIFI_STATE_UNKNOWN);
        iBSSID.setText(wifiInfo.getBSSID());
        iHiddenSSID.setText(String.valueOf(wifiInfo.getHiddenSSID()));
        iIPAddr.setText(IpAddress.ipv4Int2Str(wifiInfo.getIpAddress()));
        iLinkSpeed.setText(String.valueOf(wifiInfo.getLinkSpeed())+" Mbps");
        iMACAddr.setText(wifiInfo.getMacAddress());
        iNetworkId.setText(String.valueOf(wifiInfo.getNetworkId()));
        iRSSI.setText(String.valueOf(wifiInfo.getRssi()));
        iSSID.setText(wifiInfo.getSSID());
    }
    
    private void setWifiStateText(int wifiState, int prevState) {
        iWifiState.setText(getWifiStateString(wifiState) + " <- " + getWifiStateString(prevState));
    }
    
    private String getWifiStateString(int wifiState) {
    	String wifiStateString;
        switch(wifiState) {
            case WifiManager.WIFI_STATE_DISABLING:
                wifiStateString = getString(R.string.wifi_state_disabling);
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                wifiStateString = getString(R.string.wifi_state_disabled);
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                wifiStateString = getString(R.string.wifi_state_enabling);
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                wifiStateString = getString(R.string.wifi_state_enabled);
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                wifiStateString = getString(R.string.wifi_state_unknown);
                break;
            default:
                wifiStateString = "BAD";
                Log.e(TAG, "wifi state is bad");
                break;
        }
        
        return wifiStateString;
    }
    
    private void setSupplicantStateText(SupplicantState supplicantState) {
    	String supplicantStateString;
    	switch (supplicantState) {
    	case ASSOCIATED:
    		supplicantStateString = "ASSOCIATED";
    		break;
    	case ASSOCIATING:
    		supplicantStateString = "ASSOCIATING";
    		break;
    	case AUTHENTICATING:
    		supplicantStateString = "AUTHENTICATING";
    		break;
    	case COMPLETED:
    		supplicantStateString = "COMPLETED";
    		break;
    	case DISCONNECTED:
    		supplicantStateString = "DISCONNECTED";
    		iScanList.setText(null);
    		break;
    	case DORMANT:
    		supplicantStateString = "DORMANT";
    		break;
    	case FOUR_WAY_HANDSHAKE:
    		supplicantStateString = "FOUR WAY HANDSHAKE";
    		break;
    	case GROUP_HANDSHAKE:
    		supplicantStateString = "GROUP HANDSHAKE";
    		break;
    	case INACTIVE:
    		supplicantStateString = "INACTIVE";
    		break;
    	case INTERFACE_DISABLED:
    		supplicantStateString = "INTERFACE_DISABLED";
    		break;
    	case INVALID:
    		supplicantStateString = "INVALID";
    		break;
    	case SCANNING:
    		supplicantStateString = "SCANNING";
    		break;
    	case UNINITIALIZED:
    		supplicantStateString = "UNINITIALIZED";
    		break;
		default:
			supplicantStateString = "null";
            Log.e(TAG, "supplicant state is bad");
			break;
    	}
    	
    	/* calc roam time */
    	if (supplicantState.equals(SupplicantState.COMPLETED)) {
    		if (!savedSupplicantState.equals(SupplicantState.COMPLETED)) {
    			long time = System.currentTimeMillis() - supplicantStateNotCompletedTime;
    			supplicantStateString += " " + time/1000 + "." + time%1000 + "s";
    			savedSupplicantState = supplicantState;
    		}
    		iSupplicantStateHistory.setText(savedSupplicantStateString + supplicantStateString);
    		savedSupplicantStateString = "";
    	} else {
    		if (savedSupplicantState.equals(SupplicantState.COMPLETED)) {
    			supplicantStateNotCompletedTime = System.currentTimeMillis();
    			savedSupplicantState = supplicantState;
    		}
    		savedSupplicantStateString += supplicantStateString + " -> ";
    	}
    	
    	iSupplicantState.setText(supplicantStateString);
    	updateInfo(wifiManager.getConnectionInfo());
    	
    	/* set textView Color */
    	if (supplicantState.equals(SupplicantState.COMPLETED)) {
    		iSupplicantState.setTextColor(textViewColor);
    		iBSSID.setTextColor(textViewColor);
    		iSupplicantStateHistory.setTextColor(textViewColor);
    		textViewColor = textViewColor == Color.BLUE ? Color.RED : Color.BLUE;
    	}
    }
    
    private void handleWifiStateChanged(Intent intent) {
    	int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
    	int prevState = intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
    	
    	setWifiStateText(state, prevState);
    }

    private void handleSupplicantStateChanged(Intent intent) {
        if (intent.hasExtra(WifiManager.EXTRA_SUPPLICANT_ERROR)) {
        	int errorCode = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
        	switch (errorCode) {
        	case WifiManager.ERROR_AUTHENTICATING:
        		iSupplicantState.setText("ERROR AUTHENTICATING");
        		break;
        	default:
        		iSupplicantState.setText("unknown error");
        		break;
        	}
        } else {
        	SupplicantState state = (SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            setSupplicantStateText(state);
        }
    }
    
    private void handleScanResultsAvailable() {
        List<ScanResult> list = wifiManager.getScanResults();
        if (list == null)
        	return;
        
        StringBuffer scanList = new StringBuffer();

        for (ScanResult scanResult : list) {
        	if (scanResult == null)
        		continue;

        	if (TextUtils.isEmpty(scanResult.SSID))
        		continue;

        	scanList.append(scanResult.SSID+" ");
        }

        
        iScanList.setText(scanList);
    }
    
    private void handleNetworkStateChanged(Intent intent) {
    	NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
    	
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	DetailedState detailedState = networkInfo.getDetailedState();
    	
    	String summary = networkStateSummary(wifiInfo.getSSID(), detailedState);
    	iNetworkState.setText(summary);

    	if (detailedState == DetailedState.CONNECTED) {
        	String bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID);
        	if (bssid != null)
        		iBSSID.setText(bssid);
        	
        	wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
        	if (wifiInfo == null)
        		wifiInfo = wifiManager.getConnectionInfo();
        	updateInfo(wifiInfo);
    	}
    }
  
    private String networkStateSummary(String ssid, DetailedState state) {
    	String[] formats = getResources().getStringArray((ssid == null)
    			? R.array.wifi_status : R.array.wifi_status_with_ssid);
    	int index = state.ordinal();

    	if (index >= formats.length || formats[index].length() == 0) {
    		return null;
    	}
    	return String.format(formats[index], ssid);
    }
    
    private void handleSignalChanged(Intent intent) {
    	int rssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0);
        iRSSI.setText(String.valueOf(rssi));
    }
    
    private void handleNetworkIdsChanged() {
    	iNetworkId.setText(String.valueOf(wifiManager.getConnectionInfo().getNetworkId()));
    }

    private void handleSupplicantConnectionChange(Intent intent) {
    	if (intent.hasExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED)) {
    		if (!intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false))
    			iSupplicantState.setText("supplicant not connect");
    	}
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_open_wlan:
			openWlan(v);
			break;
		case R.id.button_close_wlan:
			closeWlan(v);
			break;
		default:
			break;
		}
	}
    
}
