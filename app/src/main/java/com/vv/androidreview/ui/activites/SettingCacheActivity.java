
package com.vv.androidreview.ui.activites;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sevenheaven.iosswitch.ShSwitchView;
import com.vv.androidreview.R;
import com.vv.androidreview.base.BaseActivity;
import com.vv.androidreview.base.system.Settings;
import com.vv.androidreview.ui.view.RangeSliderViewEx;

public class SettingCacheActivity extends BaseActivity {

    public static final String MINUTE = "分钟";
    public static final String DAY = "天";
    public static final String Lable = "(自定义)";
    public static final int MINUTE_STEP = 30;
    public static final int DAY_STEP = 2;
    //过期控制开关
    private ShSwitchView mCacheSwich;
    private RelativeLayout mWifiSwichLayout, mOtherSwichLayout;
    //显示选择的分钟文本
    private TextView mLableWifi, mLableOther;
    //输入分钟
    private EditText mEtWifi, mEtOther;

    private RangeSliderViewEx mRsvWifi, mRsvOther;
    //用于保存分钟/天数，以便退出时候保存
    private int mWifiMin;
    private int mOtherDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_cache);
        initView();
        initToolBar();
        showOrHideToolBarNavigation(true);
        initListener();
        bindData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_suggest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                if (mEtWifi.getVisibility() == View.VISIBLE) {
                    String min = mEtWifi.getText().toString();
                    //严重时间是否为空
                    if (TextUtils.isEmpty(min)) {
                        Toast.makeText(SettingCacheActivity.this, getString(R.string.no_cache_over_time), Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        mWifiMin = Integer.parseInt(min);
                    }
                }
                if (mEtOther.getVisibility() == View.VISIBLE) {
                    String day = mEtOther.getText().toString();
                    //严重时间是否为空
                    if (TextUtils.isEmpty(day)) {
                        Toast.makeText(SettingCacheActivity.this, getString(R.string.no_cache_over_time), Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        mOtherDay = Integer.parseInt(day);
                    }

                }
                Settings.putInt(Settings.CACHE_OVERTIME_WIFI, mWifiMin);
                Settings.putInt(Settings.CACHE_OVERTIME_OTHER, mOtherDay);
                Settings.putBoolean(Settings.CACHE_OVERTIME, mCacheSwich.isOn());
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initListener() {
        mCacheSwich.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                showOrHideLayout(isOn);
            }
        });

        mRsvWifi.setOnSlideListener(new RangeSliderViewEx.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                //最后一段自定义时间
                int lastIndex = (mRsvWifi.getRangeCount() - 1);
                if (index == lastIndex) {
                    mEtWifi.setText(mWifiMin + "");
                    mEtWifi.setVisibility(View.VISIBLE);
                    mEtWifi.requestFocus();
                    mLableWifi.setText(MINUTE+Lable);
                } else {
                    mEtWifi.setVisibility(View.GONE);
                    mWifiMin = (index + 1) * MINUTE_STEP;
                    mLableWifi.setText(mWifiMin + MINUTE);
                }
            }
        });

        mRsvOther.setOnSlideListener(new RangeSliderViewEx.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                //最后一段自定义时间
                int lastIndex = (mRsvOther.getRangeCount() - 1);
                if (index == lastIndex) {
                    mEtOther.setText(mOtherDay + "");
                    mEtOther.setVisibility(View.VISIBLE);
                    mEtOther.requestFocus();
                    mLableOther.setText(DAY+Lable);
                } else {
                    mEtOther.setVisibility(View.GONE);
                    mOtherDay = (index + 1) * DAY_STEP;
                    mLableOther.setText(mOtherDay + DAY);
                }
            }
        });
    }

    private void bindData() {
        boolean isOpen = Settings.getBoolean(Settings.CACHE_OVERTIME, false);
        showOrHideLayout(isOpen);
        mCacheSwich.setOn(isOpen);

        int wifiOverTime = Settings.getInt(Settings.CACHE_OVERTIME_WIFI, 30);
        int otherOverTime = Settings.getInt(Settings.CACHE_OVERTIME_OTHER, 2);
        mWifiMin = wifiOverTime;
        mOtherDay = otherOverTime;

        bindWiFiRsv(wifiOverTime);
        bindDayRsv(otherOverTime);
    }

    private void bindDayRsv(int otherOverTime) {
        int dayStep = mOtherDay / DAY_STEP - 1;

        if (mOtherDay <= DAY_STEP * (mRsvOther.getRangeCount() - 1) && dayStep >= 0) {
            mRsvOther.setInitialIndex(dayStep);
            mLableOther.setText(otherOverTime + DAY);
        } else {
            if (mOtherDay > DAY_STEP) {
                mRsvOther.setInitialIndex(mRsvOther.getRangeCount() - 1);
                mEtOther.setVisibility(View.VISIBLE);
            } else {
                mRsvOther.setInitialIndex(0);
            }
            mEtOther.setText(mOtherDay + "");
            mLableOther.setText(DAY);
        }
    }

    private void bindWiFiRsv(int wifiOverTime) {
        int minStep = mWifiMin / MINUTE_STEP - 1;

        if (mWifiMin <= MINUTE_STEP * (mRsvWifi.getRangeCount() - 1) && minStep >= 0) {
            mRsvWifi.setInitialIndex(minStep);
            mLableWifi.setText(wifiOverTime + MINUTE);
        } else {
            if (minStep > MINUTE_STEP) {
                mRsvWifi.setInitialIndex(mRsvWifi.getRangeCount() - 1);
                mEtWifi.setVisibility(View.VISIBLE);
            } else {
                mRsvWifi.setInitialIndex(0);
            }
            mEtWifi.setText(mWifiMin + "");

            mLableWifi.setText(MINUTE);
        }
    }

    private void initView() {
        mWifiSwichLayout = (RelativeLayout) findViewById(R.id.rl_wifi_cache);
        mOtherSwichLayout = (RelativeLayout) findViewById(R.id.rl_other_cache);

        mLableWifi = (TextView) findViewById(R.id.tv_label_min_wifi);
        mLableOther = (TextView) findViewById(R.id.tv_label_min_other);

        mEtWifi = (EditText) findViewById(R.id.et_label_min_wifi);
        mEtOther = (EditText) findViewById(R.id.et_label_min_other);

        mRsvWifi = (RangeSliderViewEx) findViewById(R.id.rsv_wifi);
        mRsvOther = (RangeSliderViewEx) findViewById(R.id.rsv_other);

        mCacheSwich = (ShSwitchView) findViewById(R.id.switch_view);

    }

    @Override
    public String returnToolBarTitle() {
        return getString(R.string.label_cache);
    }

    private void showOrHideLayout(boolean isOpen) {
        if (isOpen) {
            mWifiSwichLayout.setVisibility(View.VISIBLE);
            mOtherSwichLayout.setVisibility(View.VISIBLE);
        } else {
            mWifiSwichLayout.setVisibility(View.INVISIBLE);
            mOtherSwichLayout.setVisibility(View.INVISIBLE);
        }
    }
}
