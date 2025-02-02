package com.example.webviewonandroid14

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.webviewonandroid14.ui.theme.WebViewOnAndroid14Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WebViewOnAndroid14Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // WebViewを埋め込むComposable
                    YoutubeIFrameWebView()
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YoutubeIFrameWebView() {
    // YouTube の iframe を埋め込んだシンプルなHTML
    val youtubeHtml = """
        <html>
            <head>
                <style>
                    html, body {
                        margin: 0;
                        padding: 0;
                        overflow: hidden;
                        background-color: #000000;
                    }
                    iframe {
                        width: 100%;
                        height: 0%;
                        border: none;
                    }
                </style>
                <script>
                    function adjustIframeHeight() {
                        let viewportHeight = window.innerHeight;
                        document.getElementById("myIframe").style.height = viewportHeight + "px";
                    }

                    window.onload = adjustIframeHeight;
                    window.onresize = adjustIframeHeight; // ウィンドウサイズ変更時も調整
                </script>
            </head>
            <body>
                <iframe id="myIframe" src="https://www.youtube.com/embed/6n7KpRXgNFE?autoplay=1&enablejsapi=1&controls=0" allowfullscreen></iframe>
            </body>
        </html>
    """.trimIndent()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // WebViewの設定
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                // YouTubeフルスクリーン対応やJSエラーを回避するためWebChromeClientを設定
                webChromeClient = WebChromeClient()
                // 外部ブラウザに飛ばさないようにする
                webViewClient = WebViewClient()

                // 上記のiframeを含むHTMLを読み込む
                loadDataWithBaseURL(null, youtubeHtml, "text/html", "UTF-8", null)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}