package com.arianroid.betterwebview.main;

import com.arianroid.betterwebview.tools.internet.InternetTools;

import java.util.Timer;
import java.util.TimerTask;


class MainPresenter {

    private final Timer timer = new Timer();
    private IMainView view;
    private TimerTask timerTask;

    void initial(IMainView view) {
        this.view = view;
    }

    void viewIsReady() {

        if (InternetTools.isOnline()) {
            view.dismissInternetConnectionErrror();
            view.showProgressbar();
            loadUrl();

        } else {
            view.showInternetConnectionError();
            view.setProgressValue(2);
        }
    }




    private void loadUrl() {
        view.loadWebViewUrl();

        new android.os.Handler().postDelayed(() -> {
            timerTask = new TimerTask() {
                @Override
                public void run() {

                    if (view.isWebViewClientDataLoaded() && view.getProgressBarValue() == 100) {
                        timer.cancel();
                        timerTask.cancel();
                        view.closeSplashDialog();
                    }

                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 500);
        }, 50);

    }

}
