package com.exercise.utils.downloader;

import com.exercise.utils.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class DownloaderFragment extends Fragment implements OnClickListener {
	
	final static String ARG_URL = "url";

	private String mUrl;
	
	private EditText mEditText;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
        	mUrl = savedInstanceState.getString(ARG_URL);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downloader, container, false);
        
        view.findViewById(R.id.button_download).setOnClickListener(this);
        mEditText = (EditText) view.findViewById(R.id.edit_url);
        
        return view;
    }
	
	@Override
    public void onStart() {
        super.onStart();

        mEditText.setText(mUrl);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mUrl = mEditText.getText().toString();
        outState.putString(ARG_URL, mUrl);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
