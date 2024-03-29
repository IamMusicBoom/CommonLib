package foundation.base.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.library.R;
import com.umeng.analytics.MobclickAgent;

import org.droidparts.Injector;

import app.AppManager;
import butterknife.ButterKnife;
import foundation.LoadingHandler;
import foundation.status.StatusBarUtil;
import foundation.toast.ToastUtil;
import foundation.util.StringUtil;
import foundation.widget.load.LoadingDialog;
import foundation.widget.titlebar.NavigationBar;


/**
 * 程序基类
 *
 * @author li 2014-3-21 22:43:27
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected String TAG = getClass().getSimpleName();
    protected LinearLayout _rootView;
    //    protected TitleBar _navBar;
    protected NavigationBar _navBar;
    protected RelativeLayout _containerLayout;
    protected View _contentView;
    protected int _contaninerViewId = 100;
    protected int _navBarViewId = 101;
    private boolean _visible;

    public LoadingDialog loadingDialog;//垃圾代码 解决bug
    protected View onCreateContentView() {
        return null;
    }

    protected View createView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        _rootView = new LinearLayout(this);
        _rootView.setOrientation(LinearLayout.VERTICAL);
        _rootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //头部
        _navBar = onCreateNavbar();
        if (_navBar != null) {
            _navBar.setLayoutParams(new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            _navBar.setId(_navBarViewId);
            _navBar.setBackgroundResource(R.drawable.nav_bar_bg);
            _rootView.addView(_navBar);
        }
        //内容区
        _containerLayout = new RelativeLayout(this);
        _containerLayout.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        //noinspection ResourceType
        _containerLayout.setId(_contaninerViewId);
        _rootView.addView(_containerLayout);

        //嵌入内容区
        _contentView = onCreateContentView();
        if (_contentView != null) {
            _containerLayout.addView(_contentView);
        }

        return _rootView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.createView());
        boolean isImmersive = false;
//		if (hasKitKat() && !hasLollipop()) {
//			isImmersive = true;
//			//透明状态栏
////			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
////                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		} else if (hasLollipop()) {
//			Window window = getWindow();
//			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//			window.setStatusBarColor(Color.TRANSPARENT);
//			isImmersive = true;
//		}

       Injector.inject(this);
        ButterKnife.bind(this);

        mContext = this;
        _loadingHandler = new LoadingHandler(this);

        AppManager.getAppManager().addActivity(this);
        AppManager.getAppManager().addMemory(this);

        showNavigationBar(false);
        onPostInject();

    }

    protected void onPostInject() {

    }

    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this);
    }

    public boolean isVisible() {
        return _visible;
    }

    @Override
    protected void onResume() {
        super.onResume();
        _visible = true;
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);          //统计时长
        if(loadingDialog!=null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    protected void onPause() {
        super.onPause();
        _visible = false;
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);          //统计时长
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        AppManager.getAppManager().finalize(this);
    }

    public void setDrawableTop(TextView textView, int resid) {

        if (textView != null) {
            Drawable drawable = ContextCompat.getDrawable(mContext, resid);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(null, drawable, null, null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    protected void setSoftInputMode() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void hideSoftInputView() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocus = getCurrentFocus();
            if (currentFocus != null) {
                manager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    // region Loading dialog

    private LoadingHandler _loadingHandler;

    public void showLoading() {
        if (_loadingHandler == null)
            _loadingHandler = new LoadingHandler(this);
        _loadingHandler.showLoading();
    }

    public void showLoading(String message) {
        if (_loadingHandler != null)
            _loadingHandler.showLoading(message);
    }

    public void updateLoading(String message) {

        if (_loadingHandler != null)
            _loadingHandler.updateLoading(message);
    }

    public void hideLoading() {
        if (_loadingHandler != null)
            _loadingHandler.hideLoading();
    }

    public void hideLoading(String msg) {
        _loadingHandler.hideLoading();
        showToast(msg);
    }

    public void showToast(String msg) {
        ToastUtil.showToast(msg);
    }

    public void showToast(int msg) {
        ToastUtil.showToast(msg);
    }

    // endregion


    protected ViewGroup getContainer() {
        return _containerLayout;
    }

    protected void showNavigationBar(boolean show) {

        if (_navBar == null)
            return;

        if (show) {
            _navBar.setVisibility(View.VISIBLE);
        } else {
            _navBar.setVisibility(View.GONE);
        }
    }

    protected NavigationBar onCreateNavbar() {
        return new NavigationBar(this);
    }

    protected void goBack() {
        finish();
    }

    protected void goNext() {

    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    protected void setRightImage(int resid) {
        if (_navBar == null)
            return;

        showNavigationBar(true);
        _navBar.registerRightImage(resid, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
    }

    protected void setTitle(String title) {
        if (_navBar == null)
            return;

        if (!StringUtil.isEmpty(title)) {
            showNavigationBar(true);
        }
        _navBar.setTitle(title);
    }

    public void setTitle(int title) {
        String str_title = getResourceString(title);
        setTitle(str_title);
    }


    protected void setRightTitle(String title) {
        if (_navBar == null)
            return;
        showNavigationBar(true);
        _navBar.registerRightTitle(title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
    }

    protected void setRightTitle(String title, int color) {
        if (_navBar == null)
            return;
        showNavigationBar(true);
        _navBar.registerRightTitle(title, color, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
    }

    protected void setRightTitle(int resId) {
        this.setRightTitle(getResourceString(resId));
    }


    protected String getResourceString(int resId) {
        String result = null;
        try {
            result = this.getResources().getString(resId);
        } catch (Exception e) {
        }
        return result;
    }

    protected void showBack(int resId) {
        if (_navBar == null)
            return;

        showNavigationBar(true);
        _navBar.registerBack(resId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    protected void showBack(View.OnClickListener onClickListener) {
        if (_navBar == null)
            return;

        showNavigationBar(true);
        _navBar.registerBack(R.drawable.back, onClickListener);
    }

    protected void showBack() {
        showBack(R.drawable.back);
    }

    protected View inflateContentView(int resId) {
        return getLayoutInflater().inflate(resId, _containerLayout, false);
    }


    /**
     * 子类可以重写改变状态栏颜色
     */
    protected int setStatusBarColor() {
        return getColorPrimary();
    }

    /**
     * 子类可以重写决定是否使用透明状态栏
     */
    protected boolean translucentStatusBar() {
        return false;
    }


    /**
     * 获取主题色
     */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取深主题色
     */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
