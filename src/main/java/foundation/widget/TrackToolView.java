package foundation.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.common.library.R;

import java.util.ArrayList;


public class TrackToolView extends LinearLayout {
    ArrayList<ImageView> imgViews;
    ImageView currentView;
    Context context;
    Paint paint;
    OnselectItemListener myItemListener;
    Rect currentRect;
    Rect downRect;
    int movespeed = 60;
    int bottomheight = 5;
    Bitmap bgbitmap;

    public int getSum() {
        return sum;
    }

    int heght = 4; // padding 觉得控件高度的
    int width = 20; // 宽度的

    public int getCurrentindex() {
        return currentindex;
    }

    int currentindex = 0;

    public void setLanwidth(int lanwidth) {
        this.lanwidth = lanwidth;
        postInvalidate();
    }

    int lanwidth = 20; //0 是每个item底部画全

    public void setItemSelectListener(OnselectItemListener myItemListener1) {
        this.myItemListener = myItemListener1;
    }

    public TrackToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setWillNotDraw(false);
        setGravity(Gravity.CENTER);
        paint = new Paint();
        heght = (int) dp2px(context, heght);
        init();
    }

    void init() {
        addTab();
        bgbitmap = BitmapFactory.decodeResource(getResources(), bgimg);
    }

    //配置
    int sum = 3;
    int[] imgs = new int[]{R.drawable.loading_01, R.drawable.loading_01, R.drawable.loading_01};//默认的
    int[] onSelectimgs = new int[]{R.drawable.loading_01, R.drawable.loading_01, R.drawable.loading_01};//选择的时候默认的
    int bgimg = R.drawable.loading_01; //背景
    // 配置结束


    public void addTab() {
        if (imgViews == null)
            imgViews = new ArrayList<>();
        OnClickListener click = new OnClickListener() {
            public void onClick(View view) {
                currentView = (ImageView) view;
                downRect = new Rect(currentView.getLeft() + lanwidth,
                        6,
                        currentView.getRight() - lanwidth, getHeight()); // 这货用的是dip
                if (downRect.left == currentRect.left)
                    return;// 原来的 就不操作
                postInvalidate();
                for (int i = 0; i < sum; i++) {
                    if (currentView == imgViews.get(i)) {
                        currentindex = i;
                    }
                }
                updateState();//  改变被选择的图
                if (myItemListener != null)
                    myItemListener.selectItem(currentindex);
            }
        };
        for (int i = 0; i < sum; i++) {
            LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(imgs[i]);
            imageView.setPadding(width, heght, width, heght);// 扩大点击区
            imageView.setOnClickListener(click);
            addView(imageView, lp);
            imgViews.add(imageView);
        }
        currentindex = 1;
        setCurrentSelect(0);

    }

    public static int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * density + 0.5f);
    }

    void updateState() {
        for (int i = 0; i < imgViews.size(); i++) {
            if (i == currentindex)
                imgViews.get(i).setImageResource(onSelectimgs[i]);
            else
                imgViews.get(i).setImageResource(imgs[i]);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentRect == null) {// 还没画呢
            currentRect = new Rect(currentView.getLeft() + lanwidth,
                    6,
                    currentView.getRight()
                            - lanwidth,
                    this.getHeight()); // 这货用的是dip
            downRect = currentRect;
        }
        //canvas.drawRect(currentRect, paint);
        Rect mSrcRect = new Rect(0, 0, bgbitmap.getWidth(), bgbitmap.getHeight());
        canvas.drawBitmap(bgbitmap, mSrcRect, currentRect, paint);
        if (!isMoveComeple())
            postInvalidate();
    }

    public void setCurrentSelect(int item) {
        currentView = imgViews.get(item);
        downRect = new Rect(currentView.getLeft() + lanwidth,
                TrackToolView.this.getHeight() - bottomheight,
                currentView.getRight() - lanwidth,
                TrackToolView.this.getHeight());
        currentindex = item;
        updateState();
        postInvalidate();
    }

    public void setCurrentSelectCallBack(int item) {
        setCurrentSelect(item);
        if (myItemListener != null)
            myItemListener.selectItem(currentindex);
    }

    public interface OnselectItemListener {
        public void selectItem(int i);
    }

    boolean isMoveComeple() {// 当点击下的 绘制 是否移动完成了 移动完成返回真
        if (currentRect.left == downRect.left) {// 还要继续移动
            currentRect = downRect;
            return true;
        } else {
            if (Math.abs(currentRect.left - downRect.left) < movespeed) {
                currentRect.left = downRect.left;
                currentRect.right = downRect.right;
            } else {
                if (currentRect.left > downRect.left) { // 点下的在 当前的 左边
                    // current要变小
                    currentRect.left -= movespeed;
                    currentRect.right -= movespeed;
                } else {
                    currentRect.left += movespeed;
                    currentRect.right += movespeed;
                }
            }
            return false;
        }

    }

//    public void setInfoTexts(String text) {
//        txts = text.split(",");
//        sum = txts.length;
//        textViews = new TextView[sum];
//        removeAllViews();
//        Init();
//        postInvalidate();
//    }
}
