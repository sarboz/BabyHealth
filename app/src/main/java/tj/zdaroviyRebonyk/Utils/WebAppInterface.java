package tj.zdaroviyRebonyk.Utils;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

import tj.zdaroviyRebonyk.DataActivity;

public class WebAppInterface {
    Context mContext;
    Activity act;

    public WebAppInterface(DataActivity c) {
        mContext = c;
        this.act = c;
    }

    public static String text = "";

    @JavascriptInterface
    public void showToast(final String toast) {

        act.runOnUiThread(new Runnable() {
            public void run() {
                if (!text.equals(toast)) {
                    text = toast;
                }
            }
        });
    }

}