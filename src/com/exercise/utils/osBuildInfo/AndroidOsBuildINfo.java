package com.exercise.utils.osBuildInfo;

import com.exercise.utils.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AndroidOsBuildINfo extends Fragment {
	private TextView board;
    private TextView bootloader;
    private TextView brand;
    private TextView cpu_abi;
    private TextView cpu_abi2;
    private TextView device;
    private TextView display;
    private TextView fingerprint;
    private TextView hardware;
    private TextView host;
    private TextView id;
    private TextView manufacturer;
    private TextView model;
    private TextView product;
    private TextView radio;
    private TextView serial;
    private TextView tags;
    private TextView time;
    private TextView type;
    private TextView user;
    private TextView version_codename;
    private TextView version_incremental;
    private TextView version_release;
    private TextView version_sdk;
    private TextView version_sdk_int;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.android_os_build_info, container, false);
        
        board = (TextView) view.findViewById(R.id.board_info);
        bootloader = (TextView) view.findViewById(R.id.bootloader_info);
        brand = (TextView) view.findViewById(R.id.brand_info);
        cpu_abi = (TextView) view.findViewById(R.id.cpu_abi_info);
        cpu_abi2 = (TextView) view.findViewById(R.id.cpu_abi2_info);
        device = (TextView) view.findViewById(R.id.device_info);
        display = (TextView) view.findViewById(R.id.display_info);
        fingerprint = (TextView) view.findViewById(R.id.fingerprint_info);
        hardware = (TextView) view.findViewById(R.id.hardware_info);
        host = (TextView) view.findViewById(R.id.host_info);
        id = (TextView) view.findViewById(R.id.id_info);
        manufacturer= (TextView) view.findViewById(R.id.manufacturer_info);
        model = (TextView) view.findViewById(R.id.model_info);
        product = (TextView) view.findViewById(R.id.product_info);
        radio = (TextView) view.findViewById(R.id.radio_info);
        serial = (TextView) view.findViewById(R.id.serial_info);
        tags = (TextView) view.findViewById(R.id.tags_info);
        time = (TextView) view.findViewById(R.id.time_info);
        type = (TextView) view.findViewById(R.id.type_info);
        user = (TextView) view.findViewById(R.id.user_info);
        version_codename = (TextView) view.findViewById(R.id.version_codename_info);
        version_incremental = (TextView) view.findViewById(R.id.version_incremental_info);
        version_release = (TextView) view.findViewById(R.id.version_release_info);
        version_sdk = (TextView) view.findViewById(R.id.version_sdk_info);
        version_sdk_int = (TextView) view.findViewById(R.id.version_sdk_int_info);
        
        return view;
    }
	
	@Override
	public void onResume() {
		super.onResume();

		board.setText(android.os.Build.BOARD);
		bootloader.setText(android.os.Build.BOOTLOADER);
		brand.setText(android.os.Build.BRAND);
		cpu_abi.setText(android.os.Build.CPU_ABI);
		cpu_abi2.setText(android.os.Build.CPU_ABI2);
		device.setText(android.os.Build.DEVICE);
		display.setText(android.os.Build.DISPLAY);
		fingerprint.setText(android.os.Build.FINGERPRINT);
		hardware.setText(android.os.Build.HARDWARE);
		host.setText(android.os.Build.HOST);
		id.setText(android.os.Build.ID);
		manufacturer.setText(android.os.Build.MANUFACTURER);
		model.setText(android.os.Build.MODEL);
		product.setText(android.os.Build.PRODUCT);
		radio.setText(android.os.Build.getRadioVersion());
		serial.setText(android.os.Build.SERIAL);
		tags.setText(android.os.Build.TAGS);
		time.setText(String.valueOf(android.os.Build.TIME));
		type.setText(android.os.Build.TYPE);
		user.setText(android.os.Build.USER);
		version_codename.setText(android.os.Build.VERSION.CODENAME);
		version_incremental.setText(android.os.Build.VERSION.INCREMENTAL);
		version_release.setText(android.os.Build.VERSION.RELEASE);
		version_sdk.setText(android.os.Build.VERSION.SDK);
		version_sdk_int.setText(String.valueOf(android.os.Build.VERSION.SDK_INT));
	}
    
}
