package com.example.phillyzoo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragmentC extends Fragment {

	public FragmentC() {
		// Required empty public constructor
	}
	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_c, container, false);
        String url = ("file:///android_asset/twitter.html");
        WebView wv2 = (WebView)rootView.findViewById(R.id.wv2);

        wv2.loadUrl(url);
        wv2.getSettings().setJavaScriptEnabled(true);
        wv2.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            
        });
        return rootView;
    }
	

	
}
	
/*
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_c, container, false);
	}

}
*/