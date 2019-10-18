package tj.zdaroviyRebonyk.Utils;

import android.content.Context;

import android.webkit.JavascriptInterface;

public class WebAppInterfaceHtml {
    Context mContext;

    public WebAppInterfaceHtml(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void getHtml(String s) {
        html=s;
    }

    public static String  html;

}