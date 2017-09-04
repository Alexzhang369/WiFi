package com.codingbingoZTT.BanmaReader.base.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

import com.avos.avoscloud.LogUtil;
import com.codingbingoZTT.BanmaReader.Constants;
import com.codingbingoZTT.BanmaReader.FRApplication;
import com.codingbingoZTT.BanmaReader.base.BaseActivity;
import com.codingbingoZTT.BanmaReader.dao.Book;
import com.codingbingoZTT.BanmaReader.dao.BookDao;
import com.codingbingoZTT.BanmaReader.dao.ChapterDao;
import com.codingbingoZTT.BanmaReader.dao.DaoSession;
import com.codingbingoZTT.BanmaReader.model.eventbus.StyleChangeEvent;
import com.codingbingoZTT.BanmaReader.utils.ScreenUtils;
import com.codingbingoZTT.BanmaReader.view.readview.BookStatus;
import com.codingbingoZTT.BanmaReader.view.readview.PageFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import zttokhttp.util.ZTTLogUtils;

/**
 * Created by bingo on 2017/1/4.
 */

public abstract class BaseReadView extends View {
    protected Context mContext;

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected PointF mTouch = new PointF();
    protected float actiondownX, actiondownY;
    protected float touch_down = 0; // 当前触摸点与按下时的点的差值
    private  static float x1 ;
    private  static float y1 ;


    protected Bitmap mCurPageBitmap, mNextPageBitmap;
    protected Canvas mCurrentPageCanvas, mNextPageCanvas;
    protected PageFactory pagefactory = null;

    protected long bookId = BaseActivity.NO_BOOK_ID;
    protected String bookPath;
    protected Book book;
    protected boolean isPrepared = true;

    private Toast toast;
    protected Scroller mScroller;

    protected DaoSession daoSession;
    protected BookDao bookDao;
    protected ChapterDao chapterDao;


    public BaseReadView(Context context){
        this(context, null, 0);
    }

    public BaseReadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BaseReadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mScreenHeight = ScreenUtils.getScreenHeight(context);

        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurrentPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);

        mScroller = new Scroller(getContext());
        pagefactory = new PageFactory(context);


        //数据库
        daoSession = ((FRApplication) context.getApplicationContext()).getDaoSession();
        bookDao = daoSession.getBookDao();
        chapterDao = daoSession.getChapterDao();
    }


    protected synchronized void init() {
        try {
            book = bookDao.load(bookId);

            pagefactory.onDraw(mCurrentPageCanvas);
            //主动刷新界面
            postInvalidate();
        } catch (Exception e) {
        }
    }

    public void setBookId(long bookId, boolean needRefresh) {
        LogUtil.log.e("bookId: " + bookId + " ------ this.bookId: " + this.bookId);
        LogUtil.log.e("book is null: " + (book == null));
        LogUtil.log.e("book process status: " + (book != null ? book.getProcessStatus() : "null"));

        if (book == null || book.getProcessStatus() != Constants.BOOK_PROCESSED || this.bookId != bookId || needRefresh == true) {
            this.bookId = bookId;
            pagefactory.openBook(bookId);
        } else {
            pagefactory.clearParams();
        }

        init();
    }

    public void setBookPath(String bookPath) {
        if (this.bookPath == null || !this.bookPath.equals(bookPath)) {
            this.bookPath = bookPath;
            pagefactory.openBook(bookPath);
        } else {
            pagefactory.clearParams();
        }

        init();
    }

    private int dx, dy;
    private long et = 0;
    private boolean left = false;
    private boolean right = false;
    private boolean center = false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = e.getX();
                y1 = e.getY();

                break;
            case MotionEvent.ACTION_MOVE:


//                int mx = (int) e.getX();
//                int my = (int) e.getY();
//
//
//
//                mTouch.x = mx;
//                mTouch.y = my;
//                //touch_down = mTouch.x - actiondownX;
//                right = (actiondownX > mScreenWidth /2 && mTouch.x-actiondownX>0) || (actiondownY > mScreenHeight / 2 && mTouch.y-actiondownY>0);
//                left = (actiondownX < mScreenWidth /2 && mTouch.x-actiondownX<0) || (actiondownY < mScreenHeight / 2 && mTouch.y-actiondownY<0);
//
//                //postInvalidate();
//                ZTTLogUtils.e("actiondownX","actiondownX------------------"+actiondownX);
//                ZTTLogUtils.e("mTouch.x","mTouch.x------------------"+mTouch.x);
//                ZTTLogUtils.e("actiondownY","actiondownY------------------"+actiondownY);
//                ZTTLogUtils.e("mTouch.y","mTouch.y------------------"+mTouch.y);
//                ZTTLogUtils.e("right","right------------------"+right);
//                ZTTLogUtils.e("left","left------------------"+left);
//                if(right) {
//                    //nextPage();
//                    prePage();
//                }
//                if(left) {
//                    //nextPage();
//                    nextPage();
//                }
                float x2 = e.getX();
                float y2 = e.getY();
                if(y1 - y2 > 15&&x1 - x2 > 15) {
                    //Toast.makeText(MainActivity.this, "向上滑", Toast.LENGTH_SHORT).show(); 上
                    ZTTLogUtils.e("向上滑","向上滑---------y1-y2---------"+ String.valueOf(y1-y2));
                    ZTTLogUtils.e("向上滑","向上滑---------x1-x2---------"+ String.valueOf(x1-x2));
                    prePage();
                } else if(y2 - y1 > 15&&x2 - x1 > 15) {
                    //Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show(); 下
                    ZTTLogUtils.e("向下滑","向下滑---------y2 - y1---------"+String.valueOf(y2-y1));
                    ZTTLogUtils.e("向下滑","向下滑---------x2 - x1 ---------"+String.valueOf(x2 - x1 ));
                    nextPage();
                }
                if(y1 - y2 > 15&&x2 - x1 > 15) {
                    //Toast.makeText(MainActivity.this, "向上滑", Toast.LENGTH_SHORT).show(); 上
                    ZTTLogUtils.e("向上滑","向上滑---------y1-y2---------"+ String.valueOf(y1-y2));
                    ZTTLogUtils.e("向上滑","向上滑---------x1-x2---------"+ String.valueOf(x1-x2));
                    prePage();
                } else if(y2 - y1 > 15&&x1 - x2 > 15) {
                    //Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show(); 下
                    ZTTLogUtils.e("向下滑","向下滑---------y2 - y1---------"+String.valueOf(y2-y1));
                    ZTTLogUtils.e("向下滑","向下滑---------x2 - x1 ---------"+String.valueOf(x2 - x1 ));
                    nextPage();
                }

                break;
            case MotionEvent.ACTION_UP:

                resetTouchPoint();

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;

    }

    /**
     * 复位触摸点位
     */
    protected void resetTouchPoint() {
        mTouch.x = 0.1f;
        mTouch.y = 0.1f;
        touch_down = 0;
        calcCornerXY(mTouch.x, mTouch.y);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        calcPoints();
        drawCurrentPageArea(canvas);
        drawNextPageAreaAndShadow(canvas);
        drawCurrentPageShadow(canvas);
        drawCurrentBackArea(canvas);
    }

    protected abstract void drawNextPageAreaAndShadow(Canvas canvas);

    protected abstract void drawCurrentPageShadow(Canvas canvas);

    protected abstract void drawCurrentBackArea(Canvas canvas);

    protected abstract void drawCurrentPageArea(Canvas canvas);

    protected abstract void calcPoints();

    protected abstract void calcCornerXY(float x, float y);

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //取消订阅事件
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //订阅事件
        if (EventBus.getDefault().isRegistered(this) == false) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveStyleChangeEvent(StyleChangeEvent styleChangeEvent){
        if (pagefactory != null) {
            pagefactory.refreshAccordingToStyle(mCurrentPageCanvas);
            invalidate();
        }
    }

    /**
     * 显示Toast提示
     * @param content
     */
    protected void showToast(String content){
        if (toast == null) {
            toast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }

    public void nextPage() {
        BookStatus status = pagefactory.nextPage();
        if (status == BookStatus.NO_NEXT_PAGE) {
            showToast("没有下一页啦");
            return;
        } else if (status == BookStatus.LOAD_SUCCESS) {
            if (isPrepared) {
                pagefactory.onDraw(mCurrentPageCanvas);
                postInvalidate();
            }
        } else {
            return;
        }

    }

    public void prePage() {
        BookStatus status = pagefactory.prePage();
        if (status == BookStatus.NO_PRE_PAGE) {
            showToast("没有上一页啦");
            return;
        } else if (status == BookStatus.LOAD_SUCCESS) {
            if (isPrepared) {
                pagefactory.onDraw(mCurrentPageCanvas);
                postInvalidate();
            }
        } else {
            return;
        }
    }
}
