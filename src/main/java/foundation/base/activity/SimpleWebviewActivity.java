package foundation.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import org.droidparts.annotation.inject.InjectBundleExtra;

public class SimpleWebviewActivity extends WebViewActivity {
    @InjectBundleExtra(key = "url")
    private String url;
    @InjectBundleExtra(key = "title")
    private String title;


    public static void showWebview(Context context, String url, String title) {
        Intent intent = new Intent(context, SimpleWebviewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        showBack();
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

    }

    public String getUrl() {
        return url;
    }
}
