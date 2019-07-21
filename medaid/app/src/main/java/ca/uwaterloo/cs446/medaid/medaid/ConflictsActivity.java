package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.webmd.com/interaction-checker/default.htm");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function(){document.getElementById(\"ICDrugs-1\").value="+med1+";})()");
            }
        });
    }
}
