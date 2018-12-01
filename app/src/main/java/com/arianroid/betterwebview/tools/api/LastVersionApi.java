package com.arianroid.betterwebview.tools.api;


import com.arianroid.betterwebview.tools.CustomOkhttp.BaseOkhttpRunnable;

public class LastVersionApi {

    // ****************** URL *************************
    private static final String LAST_VERSION_URL = "https://onlineime.agah.com/Mobile/GetLatestVersion";

    // ****************** API ********************
    public static void getVersion(BaseOkhttpRunnable runnable) {
        GeneralApi.getItemList(LAST_VERSION_URL, runnable);
    }


}
