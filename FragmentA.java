package com.example.phillyzoo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragmentA extends Fragment {

	public FragmentA() {
		// Required empty public constructor
	}
	

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_a, container, false);
        String url = ("file:///android_asset/index.html");
        WebView wv = (WebView)rootView.findViewById(R.id.wv);
        
        
        wv.loadUrl(url);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            
        });        
        return rootView;
    }
	
}
