package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ConflictsActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conflicts);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        final String med1 = extras.getString("med1");
        final String med2 = extras.getString("med2");

        final WebView webView = (WebView) findViewById(R.id.conflictView);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webView, url);
                System.out.println("running on PageFinished");
                webView.loadUrl("javascript:(function(){document.getElementById('q').value='" + med1 + "; " + med2 + "';document.getElementsByClassName('btn btn-drugbank-primary btn-sm mr-2')[0].click();})()");
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.drugbank.ca/interax/multi_search");
    }
}
