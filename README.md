# Android - Custom WebView

## Current Support

1. Camera and gallery with WebViewInterface.
2. Bug fix for dragging elements inside of viewpager (or fragment) using ParentView for event.preventDefault() (an approach). **(new Webview from android 5.0 > cause an ANR, read more in [Chromium Issue](https://code.google.com/p/chromium/issues/detail?can=2&start=0&num=100&q=&colspec=ID%20Pri%20M%20Stars%20ReleaseBlock%20Cr%20Status%20Owner%20Summary%20OS%20Modified&groupby=&sort=&id=501901), maybe android 6.0 > fixed it)**
3. Mailto url calls a native android application.
4. Google Play url calls Play Store.
5. Support for onAppear function to start an animation when the user completely sees the WebView.
6. Enabled database (websql), localStorage and cache event (manifest).
7. Any redirect (href) creates a modal above the WebView.
8. Geolocation works only with projects stored in device.
9. Injected informations about your application in WebView (just an example: MaganizeData Object).
