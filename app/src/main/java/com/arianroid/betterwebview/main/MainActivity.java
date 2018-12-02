package com.arianroid.betterwebview.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arianroid.betterwebview.R;
import com.arianroid.betterwebview.tools.exceptionHandler.CustomThreadExceptionHandler;
import com.arianroid.betterwebview.tools.views.message.MessageHelper;
import com.arianroid.betterwebview.tools.views.web.CustomeWebViewClient;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity
        extends
        AppCompatActivity
        implements
        IMainView {

    private WebView webView;
    private Dialog splashDialog;
    private TextView tryAgainTxt;
    private MainPresenter presenter;
    private ProgressBar progressBar;
    private int progressbarValue = 0;
    private ThreeBounce threeBounce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivity();
        createPresenter();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initActivity() {
        Thread.setDefaultUncaughtExceptionHandler(
                new CustomThreadExceptionHandler());
        setContentView(R.layout.activity_main);
        initFont();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);


        webView = findViewById(R.id.webView1);

        // set custom webview client
        webView.setWebViewClient(new CustomeWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setBackgroundColor(getColor(R.color.alpha));
        } else webView.setBackgroundColor(getResources().getColor(R.color.alpha));

        // enable zoom feature
        webView.getSettings().setSupportZoom(true);

        // allow pinch to zoom
        webView.getSettings().setBuiltInZoomControls(true);

        // disable the default zoom controls on the page
        webView.getSettings().setDisplayZoomControls(false);

        // Enable responsive layout
        webView.getSettings().setUseWideViewPort(true);

        // Zoom out if the content width is greater than the width of the viewport
        webView.getSettings().setLoadWithOverviewMode(true);

        //enable java script
        webView.getSettings().setJavaScriptEnabled(true);

        //set a scrollbar
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // speed up performance for older device
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        //init views
        splashDialog = new Dialog(this, R.style.myDialog);
        splashDialog.setContentView(R.layout.dialog_splash);
        splashDialog.show();

        progressBar = splashDialog.findViewById(R.id.progressBar);
        threeBounce = new ThreeBounce();
        threeBounce.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        progressBar.setIndeterminateDrawable(threeBounce);
        progressBar.setVisibility(View.VISIBLE);

        tryAgainTxt = splashDialog.findViewById(R.id.tryAgainTxt);
        tryAgainTxt.setVisibility(View.INVISIBLE);
        tryAgainTxt.setOnClickListener(v -> presenter.viewIsReady());

        //connect progressbar with chrome web view
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, final int newProgress) {
                runOnUiThread(() -> setProgressValue(newProgress));
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    private void createPresenter() {
        presenter = new MainPresenter();
        presenter.initial(this);
        presenter.viewIsReady();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IranSansRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    public void showMessage(int msgId) {
        MessageHelper.showMessage(this, msgId);
    }

    @Override
    public void showInternetConnectionError() {
        showMessage(R.string.internetError);
        progressBar.setVisibility(View.INVISIBLE);
        tryAgainTxt.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissInternetConnectionErrror() {
        tryAgainTxt.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadWebViewUrl() {
        webView.loadUrl("https://onlineime.agah.com");

    }

    @Override
    public void closeSplashDialog() {
        splashDialog.dismiss();
    }

    @Override
    public boolean isWebViewClientDataLoaded() {
        return CustomeWebViewClient.isDataLoaded();
    }

    @Override
    public int getProgressBarValue() {
        return progressbarValue;
    }

    @Override
    public void setProgressValue(int progress) {
        runOnUiThread(() -> {
            progressbarValue = progress;
        });
    }

    @Override
    public void showProgressbar() {
        progressBar.setIndeterminate(true);
        progressBar.setIndeterminateDrawable(threeBounce);
        progressBar.setVisibility(View.VISIBLE);
    }

}
