package com.example;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.joe.imgscroll.MyImgScroll;
import com.joe.volley.MyImage;

public class MainActivity extends Activity {
	
	MyImgScroll myPager; // 图片容器
	LinearLayout ovalLayout; // 圆点容器
	private List<View> listViews; // ImageView组
	private List<String> urlList; //图片地址列表

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myPager = (MyImgScroll) findViewById(R.id.myvp);
		ovalLayout = (LinearLayout) findViewById(R.id.vb);
		geyImageUrl();//获取图片地址
		initViewPager();//初始化图片
		//开始滚动
		myPager.start(this, listViews, 4000, ovalLayout);
	}
	
	private void geyImageUrl(){
		urlList = new ArrayList<String>();
		urlList.add("http://img3.imgtn.bdimg.com/it/u=4062712383,3140853232&fm=21&gp=0.jpg");
		urlList.add("http://pic.58pic.com/58pic/15/16/00/49T58PICiZm_1024.jpg");
		urlList.add("http://www.6188.com/upload_6188s/Small_paper/tebie/3553/s800/2880view_008.jpg");
		urlList.add("http://img4.imgtn.bdimg.com/it/u=1254729582,2442676828&fm=21&gp=0.jpg");
	}
    
	@Override
	protected void onRestart() {
		myPager.startTimer();
		super.onRestart();
	}
    
	@Override
	protected void onStop() {
		myPager.stopTimer();
		super.onStop();
	}

	public void stop(View v) {
		myPager.stopTimer();
	}

	/**
	 * 初始化图片
	 */
	private void initViewPager() {
		listViews = new ArrayList<View>();
		//初始化Volley
		RequestQueue mQueue = Volley.newRequestQueue(this);
		ImageLoader imageLoader = new ImageLoader(mQueue, new ImageCache() {  
		    @Override  
		    public void putBitmap(String url, Bitmap bitmap) {  
		    }  
		  
		    @Override  
		    public Bitmap getBitmap(String url) {  
		        return null;  
		    }  
		});
		//循环添加ImageView
		for (int i = 0; i < urlList.size(); i++) {
			NetworkImageView imageView = new NetworkImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setDefaultImageResId(R.drawable.ic_launcher);
			imageView.setErrorImageResId(R.drawable.ic_launcher);
			imageView.setImageUrl(urlList.get(i), imageLoader);
			listViews.add(imageView);
		}
	}

}