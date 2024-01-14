package com.example.adcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AdControl adControl;
    private InterstitialAd mInterstitialAd;
    TextView tvDisplay;
    Button btnInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        btnInterstitialAd = findViewById(R.id.btnInterstitialAd);

        // get data
        getData();


        // button এর onClick লেখা হয়েছে ।
        btnInterstitialAd.setOnClickListener(v -> {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        loadInterstitialAd(adControl);
                        // go to your activity
                    }
                });
            } else {
                // to to your activity when not ad ready
            }
        });


    } // onCreate method end here =================

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://your_domain.com/apps/ads.json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            adControl = AdControl.fromJson(response);

                            // ad কে load করে দেওয়া হয়েছে ।
                            loadAppID(adControl);
                            loadBannerAd(adControl);
                            loadInterstitialAd(adControl);

                            // ad unit id show করানো হয়েছে ।
                            tvDisplay.append("APP_ID: " + adControl.getAPP_ID());
                            tvDisplay.append("\nBANNER_AD_UNIT : " + adControl.getBANNER_AD_UNIT());
                            tvDisplay.append("\nINTERSTITIAL_AD_UNIT : " + adControl.getINTERSTITIAL_AD_UNIT());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
    } // getData end here ======================

    private void loadBannerAd(AdControl adControl) {
        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(MainActivity.this);
        adView.setAdUnitId(adControl.getBANNER_AD_UNIT());
        LinearLayout container = findViewById(R.id.bannerAds);
        container.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(adRequest);
    }

    private void loadInterstitialAd(AdControl adControl) {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, adControl.getINTERSTITIAL_AD_UNIT(), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });

    } // loadInterstitialAd  end here ==========

    private void loadAppID(AdControl adControl){
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            applicationInfo.metaData.putString("com.google.android.gms.ads.APPLICATION_ID",adControl.getAPP_ID());
            // how to get ?
            String apiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
        } catch (Exception e){
            Log.d("APP_ID_SET", "Error App ID Set Failed (msg) : "+e.getMessage());
            Toast.makeText(this, "App ID Set Failed", Toast.LENGTH_SHORT).show();

        }

    } // loadAppID end here ==============

} // public class end here ========================