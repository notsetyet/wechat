package com.example.myapplication.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.ycbjie.webviewlib.InterWebListener;
import com.ycbjie.webviewlib.X5WebUtils;
import com.ycbjie.webviewlib.X5WebView;

public class WebActivity extends AppCompatActivity {
    private TextView tv_title;
    private ImageView iv_back;
    private X5WebView webView;
    private String url;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.web_view);
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("name");
        tv_title.setText(title);
        webView.loadUrl(url);
        webView.getX5WebChromeClient().setWebListener(interWebListener);
        webView.getX5WebViewClient().setWebListener(interWebListener);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private InterWebListener interWebListener = new InterWebListener() {
        @Override
        public void hindProgressBar() {
        }

        @Override
        public void showErrorView(@X5WebUtils.ErrorType int type) {
            switch (type){
                //没有网络
                case X5WebUtils.ErrorMode.NO_NET:
                    break;
                //404，网页无法打开
                case X5WebUtils.ErrorMode.STATE_404:

                    break;
                //onReceivedError，请求网络出现error
                case X5WebUtils.ErrorMode.RECEIVED_ERROR:

                    break;
                //在加载资源时通知主机应用程序发生SSL错误
                case X5WebUtils.ErrorMode.SSL_ERROR:

                    break;
                default:
                    break;
            }
        }

        @Override
        public void startProgress(int newProgress) {
        }

        @Override
        public void showTitle(String title) {

        }
    };


    @Override
    protected void onDestroy() {
        try {
            if (webView != null) {
                webView.destroy();
                webView = null;
            }
        } catch (Exception e) {
            Log.e("X5WebViewActivity", e.getMessage());
        }
        super.onDestroy();
    }
}
