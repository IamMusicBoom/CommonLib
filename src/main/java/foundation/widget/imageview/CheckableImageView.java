package foundation.widget.imageview;

import android.content.Context;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;

//import static u.aly.au.R;



/**
 * Created by li on 2016/12/29.
 */

public class CheckableImageView extends AppCompatImageView implements Checkable {
    private static final int[] DRAWABLE_STATE_CHECKED = new int[]{android.R.attr.state_checked};

    private boolean mChecked;

    public CheckableImageView(Context context) {
        this(context, null);
    }

    public CheckableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.imageButtonStyle);
    }

    public CheckableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegateCompat() {
            @Override
            public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onInitializeAccessibilityEvent(host, event);
                event.setChecked(isChecked());
            }

            @Override
            public void onInitializeAccessibilityNodeInfo(View host,
                                                          AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                info.setCheckable(true);
                info.setChecked(isChecked());
            }
        });
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            sendAccessibilityEvent(
                    AccessibilityEventCompat.TYPE_WINDOW_CONTENT_CHANGED);
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        if (mChecked) {
            return mergeDrawableStates(
                    super.onCreateDrawableState(extraSpace + DRAWABLE_STATE_CHECKED.length),
                    DRAWABLE_STATE_CHECKED);
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }
}
