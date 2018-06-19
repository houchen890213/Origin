package com.yulin.common.page;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 网页Page
 * emoney
 *
 */
public class WebPage extends Page {

	public final static String EXTRA_KEY_URL = "key_url";
	
	private String mUrl = null;
	private WebView mWebView = null;


	@Override
	protected void initPage() {
		// TODO Auto-generated method stub
		mWebView = new WebView(getActivity());
		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);//支持javascript
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.requestFocus();
		
		setContentView(mWebView);
	}

	public WebView getWebView()
	{
		return mWebView;
	}
	@Override
	public void receiveData(Bundle bundle) {
		// TODO Auto-generated method stub
		if(bundle == null)
		{
			return;
		}
		if(bundle.containsKey(EXTRA_KEY_URL))
		{
			mUrl = bundle.getString(EXTRA_KEY_URL);
		}
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		if(mUrl != null)
		{
			mWebView.loadUrl(mUrl);
		}
	}
	public void setUrl(String url)
	{
		mUrl = url;
	}
	protected class MyWebViewClient extends WebViewClient
	{

		@Override
		// 在WebView中显示页面,而不是默认浏览器中显示页面 
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			if(url.startsWith("tel:")){
				String[] strAry = url.split(":");
				if(strAry.length > 1 && strAry[1].length() > 0){
					String callTo = strAry[1];
					Uri callToUri = Uri.parse("tel:"+callTo);
					Intent intent = new Intent(Intent.ACTION_DIAL, callToUri);
					getActivity().startActivity(intent);
				}
			}else if(url.endsWith(".apk"))
			{
				getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
			else
			{
				view.loadUrl(url);
			}
			return true;
		}
		
		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			onFinishLoad(view, url);
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			onStartLoad(view, url);
		}
//		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){  
//			//接受证书
//			handler.proceed();
//		}
	}
	public class MyWebChromeClient extends WebChromeClient
	{
		public boolean onJsAlert(WebView view, String url, String message, final JsResult result)
		{
			//构建一个Builder来显示网页中的alert对话框  
			Builder builder = new Builder(getActivity());
			builder.setTitle("提示对话框");
			builder.setMessage(message);
			builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
			{
			
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					result.confirm();
				}
				
			});
			builder.setCancelable(false);
			builder.create();
			builder.show();
			return true;
		}

		public boolean onJsConfirm(WebView view, String url, String message, final JsResult result)
		{
			Builder builder = new Builder(getActivity());
			builder.setTitle("带选择的对话框");
			builder.setMessage(message);
			builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					result.confirm();
				}

			});
			builder.setNeutralButton(android.R.string.cancel, new AlertDialog.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					result.cancel();
				}

			});
			builder.setCancelable(false);
			builder.create();
			builder.show();
			return true;
		}

		//设置网页加载的进度条  
		public void onProgressChanged(WebView view, int newProgress)
		{
			super.onProgressChanged(view, newProgress);

            if(newProgress < 100){
                setProgress(newProgress);
            }else{
                onFinishRefresh();
            }
		}

		//设置应用程序的标题  
		public void onReceivedTitle(WebView view, String title)
		{
			super.onReceivedTitle(view, title);
		}
	}
	
	public void setProgress(int progress)
	{
		
	}
	
	public void onStartLoad(WebView wv, String url)
	{
		
	}
	public void onFinishLoad(WebView wv, String url)
	{
		
	}
	public void onFinishRefresh()
	{
		
	}

	@Override
	public boolean isAdded2Stack() {
		// TODO Auto-generated method stub
		return isAdded();
	}
}
