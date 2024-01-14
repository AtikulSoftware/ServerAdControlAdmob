package com.example.adcontrol;

import org.json.JSONException;
import org.json.JSONObject;

public class AdControl {
    public String APP_ID, BANNER_AD_UNIT, INTERSTITIAL_AD_UNIT;

    public AdControl() {
    }

    public AdControl(String APP_ID, String BANNER_AD_UNIT, String INTERSTITIAL_AD_UNIT) {
        this.APP_ID = APP_ID;
        this.BANNER_AD_UNIT = BANNER_AD_UNIT;
        this.INTERSTITIAL_AD_UNIT = INTERSTITIAL_AD_UNIT;
    }

    public String getAPP_ID() {
        return APP_ID;
    }

    public String getBANNER_AD_UNIT() {
        return BANNER_AD_UNIT;
    }

    public String getINTERSTITIAL_AD_UNIT() {
        return INTERSTITIAL_AD_UNIT;
    }

    public static AdControl fromJson(JSONObject json) throws JSONException {
        String appId = json.getJSONObject("ads_control").getString("app_id");
        String bannerAdUnit = json.getJSONObject("ads_control").getString("banner_ad_unit");
        String interstitialAdUnit = json.getJSONObject("ads_control").getString("interstitial_ad_unit");
        return new AdControl(appId, bannerAdUnit, interstitialAdUnit);
    }
}
