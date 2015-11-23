# Android - Custom WebView

## Current Support

1. Camera and gallery with WebViewInterface.
2. Bug fix for drag element inside of viewpager (or fragment) using ParentView for event.preventDefault() (an approach). **(new Webview from android 5.0 > cause an ANR, read more in [Chromium Issue](https://code.google.com/p/chromium/issues/detail?can=2&start=0&num=100&q=&colspec=ID%20Pri%20M%20Stars%20ReleaseBlock%20Cr%20Status%20Owner%20Summary%20OS%20Modified&groupby=&sort=&id=501901), maybe android 6.0 > fix it)**
3. Mailto url call a native android application.
4. Google Play url call Play Store.
5. Support to onAppear function to stat an animation when the user completely see the WebView.
6. Enabled database (websql), localStorage and cache event (manifest).
