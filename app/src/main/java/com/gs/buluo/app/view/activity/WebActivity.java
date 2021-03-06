package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;

import butterknife.Bind;

/**
 * Created by hjn on 2017/2/28.
 */
public class WebActivity extends BaseActivity{
    @Bind(R.id.web_view)
    WebView webView;
    @Override
    protected void bindView(Bundle savedInstanceState) {
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webView.requestFocusFromTouch();
//        webView.requestFocus();
//        webView.requestFocus(View.FOCUS_DOWN|View.FOCUS_UP);
        webView.getSettings().setLightTouchEnabled(true);
        webView.loadUrl(Constant.Base.BASE+"page.html");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_protocol;
    }
}
