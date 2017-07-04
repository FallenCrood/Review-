
package com.vv.androidreview.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.vv.androidreview.R;
import com.vv.androidreview.base.BaseActivity;
import com.vv.androidreview.cache.CacheHelper;
import com.vv.androidreview.cache.ReadCacheAsyncTask;
import com.vv.androidreview.cache.SaveCacheAsyncTask;
import com.vv.androidreview.entity.Content;
import com.vv.androidreview.ui.fragment.ReviewContentListFragment;
import com.vv.androidreview.ui.view.LoadingLayout;
import com.vv.androidreview.ui.view.ScrollViewEx;
import com.vv.androidreview.utils.TDevice;
import com.vv.androidreview.utils.WebViewHelper;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

public class DetailActivity extends BaseActivity {

    private Content mContent;
    private TextView mContentTitle, mAuthor, mCreateTime, mSource;
    private WebView mWebView;
    private String mPonitName;

    //加载状态的布局
    private LoadingLayout mLoadingLayout;
    //内容根布局
    private ScrollViewEx mScrollViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initArguments();
        initToolBar();
        showOrHideToolBarNavigation(true);
        initView();
        setStatusBarCompat();
        loadData();
    }

    private void loadData() {
        //是否有缓存
        boolean hasCache = CacheHelper.isExistDataCache(this, CacheHelper.CONTENT_CACHE_KEY + mContent.getObjectId());
        //缓存是否过期
        boolean isCacheOverTiem = CacheHelper.isCacheDataFailure(this, CacheHelper.CONTENT_CACHE_KEY + mContent.getObjectId());
        //是否开启缓存过期设置
        boolean isOpenCacheOverTime = CacheHelper.isOpenCacheOverTime();
        //有网络并且有没缓存||有网络且有启动缓存过期设置且缓存过期  就请求网络数据 否则 读取缓存


        if (TDevice.hasInternet() && (!hasCache || (isOpenCacheOverTime && isCacheOverTiem))) {
            //从网络上读取数据
            loadDataByNet();
            Logger.e("detail_page hasCache : " + hasCache + "\n"
                    + "detail_page isCacheOverTiem : " + isCacheOverTiem + "\n"
                    + "detail_page isOpenCacheOverTime : " + isOpenCacheOverTime + "\n"
                    + "detail_page request net");
        } else {
            //用AsynTask读取缓存
            readCache();
            Logger.e("detail_page hasCache : " + hasCache + "\n"
                    + "detail_page isCacheOverTiem : " + isCacheOverTiem + "\n"
                    + "detail_page isOpenCacheOverTime : " + isOpenCacheOverTime + "\n"
                    + "detail_page readCache");
        }

    }

    private void initView() {
        mLoadingLayout = (LoadingLayout) findViewById(R.id.ly_loading);
        mScrollViewEx = (ScrollViewEx) findViewById(R.id.ly_main);

        mContentTitle = (TextView) findViewById(R.id.tv_content_title);
        mAuthor = (TextView) findViewById(R.id.tv_author);
        mCreateTime = (TextView) findViewById(R.id.tv_create_time);
        mSource = (TextView) findViewById(R.id.tv_source);
        initWebView();

    }

    private void readCache() {
        ReadCacheAsyncTask<Content> readAsyncTask = new ReadCacheAsyncTask<>(this);
        readAsyncTask.setOnReadCacheToDo(new ReadCacheAsyncTask.OnReadCacheToDo<Content>() {
            @Override
            public void preExecute() {
                mLoadingLayout.setLoadingLayout(LoadingLayout.NETWORK_LOADING);
                mScrollViewEx.setVisibility(View.GONE);
            }

            @Override
            public void postExectue(Content data) {
                if (data == null) {
                    loadDataByNet();
                } else {
                    mLoadingLayout.setLoadingLayout(LoadingLayout.HIDE_LAYOUT);
                    mScrollViewEx.setVisibility(View.VISIBLE);
                    //加载数据
                    setData(data);
                }
            }
        });
        readAsyncTask.execute(CacheHelper.CONTENT_CACHE_KEY + mContent.getObjectId());
    }

    //加载数据
    private void setData(Content data) {
        mContent = data;
        mContentTitle.setText(mContent.getTitle());
        mAuthor.setText(getString(R.string.label_author) + mContent.getAuthor());
        mSource.setText(getString(R.string.label_resource) + mContent.getSource());
        mCreateTime.setText(mContent.getCreatedAt().split(" ")[0]);
        mWebView.loadDataWithBaseURL(null, WebViewHelper.getWebViewHtml(mContent), "text/html", "UTF-8", null);
    }

    private void loadDataByNet() {
        BmobQuery<Content> query = new BmobQuery<>();
        query.getObject(DetailActivity.this, mContent.getObjectId(), new GetListener<Content>() {
            @Override
            public void onSuccess(Content content) {
                setData(content);
                //把数据缓存到本地
                SaveCacheAsyncTask savecaheTask = new SaveCacheAsyncTask(DetailActivity.this, content, CacheHelper.CONTENT_CACHE_KEY + mContent.getObjectId());
                savecaheTask.execute();
                mLoadingLayout.setLoadingLayout(LoadingLayout.HIDE_LAYOUT);
                mScrollViewEx.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int i, String s) {
                if (mLoadingLayout.getState() == LoadingLayout.STATE_REFRESH) {
                    mLoadingLayout.setLoadingLayout(LoadingLayout.NETWORK_ERROR);
                }
            }
        });
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.content_webview);
        WebViewHelper.initWebViewSettings(mWebView);


    }


    private void initArguments() {
        Intent intent = getIntent();
        if (intent != null) {
            mContent = (Content) intent.getSerializableExtra(ReviewContentListFragment.ARGUMENT_CONTEN_KEY);
            mPonitName = mContent.getPoint().getName();
        }

    }

    @Override
    public String returnToolBarTitle() {
        if (mPonitName != null) {
            return mPonitName;
        } else {
            return getString(R.string.app_name);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }
}
