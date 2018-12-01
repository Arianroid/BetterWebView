package com.arianroid.betterwebview.main;


public interface IMainView {

    void showMessage(int msgId);

    void showInternetConnectionError();

    void dismissInternetConnectionErrror();

    void loadWebViewUrl();

    void closeSplashDialog();

    boolean isWebViewClientDataLoaded();

    int getProgressBarValue();

    void setProgressValue(int progress);

    void showProgressbar();

}
