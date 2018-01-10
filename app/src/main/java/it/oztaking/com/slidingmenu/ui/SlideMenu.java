package it.oztaking.com.slidingmenu.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Scroller;

/**
 * Created by Administrator on 2018-01-09.
 *
 * 测量           摆放          绘制
 * measure   ->  layout  ->   draw
 *    |            |            |
 * onMeasure ->  onLayout ->  onDraw  重写这些方法，实现自定义控件
 *
 *view 流程
 * onMeasure(在这个方法中指定自己的宽高)-onDraw()(绘制自己的内容)
 *
 *ViewGroup流程
 *onMeasure(制定自己的宽高，所有子View的宽高)->onLayout()(摆放所有的子View)->onDraw()(绘制内容)
 */

public class SlideMenu extends ViewGroup {

    private Scroller scroller;
    private int downx;
    private int downy;
    private int moveX;
    private int scrollX;
    //预设两种状态
    public static final int MAIN_STATE = 0;
    public static final int MENU_STATE = 1;
    private int currentState = MAIN_STATE;


    public SlideMenu(Context context) {
        super(context);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化滚动器，数字模拟器
        scroller = new Scroller(getContext());
    }


    /**
     * 测量并设置所有子View的宽高
     * @param widthMeasureSpec 当前控件的宽度的测量规则
     * @param heightMeasureSpec 当前空间的高度的测量规则
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        指定左面板的宽高
        View leftMenu = getChildAt(0);
        leftMenu.measure(leftMenu.getLayoutParams().width,heightMeasureSpec);

//        指定主面板的宽高
        View mainMenu = getChildAt(1);
        mainMenu.measure(widthMeasureSpec,heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     *
     * @param changed 当前控件的尺寸是否发生了变化
     * @param l 左边距
     * @param t 顶边距
     * @param r 右边界
     * @param b 下边界
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //左侧面板的位置
        View leftMenu = getChildAt(0);
        leftMenu.layout(-leftMenu.getMeasuredWidth(),0,0,b);

        //主面板的位置
        getChildAt(1).layout(l,t,r,b);
    }


    /**
     * 处理完成触摸事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downx = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                /*将要方发生的偏移量和变化量*/
                scrollX = downx - moveX;
                /*计算将要滚动的位置，判断是否会超出去，超出去不执行scrollBy*/
                int newScrollPosition = getScrollX() + scrollX;
                if (newScrollPosition < -getChildAt(0).getMeasuredWidth()){
                    scrollTo(-getChildAt(0).getMeasuredWidth(),0);
                }else if (newScrollPosition > 0){
                    scrollTo(0,0);
                }else {
                    scrollBy(scrollX,0);
                }
                downx = moveX;
                break;

            case MotionEvent.ACTION_UP:
                int leftCenter = (int) (- getChildAt(0).getMeasuredWidth() / 2.0f);
                if (getScrollX() < leftCenter){
                    currentState = MENU_STATE;
                    updateCurrentContent();
                }else {
                    currentState = MAIN_STATE;
                    updateCurrentContent();
                }
                break;
            default:
                break;

        }
        return true;//消费事件
    }


    //按钮事件的打开和关闭
    public void open(){
        currentState = MENU_STATE;
        updateCurrentContent();

    }

    public void close(){
        currentState = MAIN_STATE;
        updateCurrentContent();
    }

    public void switchState(){
        if (currentState == MAIN_STATE){
            open();
        }else {
            close();
        }
    }

    public int getCurrentState(){
        return currentState;
    }

    private void updateCurrentContent() {
        /*方法1*/
       /* if (currentState == MENU_STATE){
            scrollTo(-getChildAt(0).getMeasuredWidth(),0);
        }else {
            scrollTo(0,0);
        }*/
       /*方法2*/
        int startX = getScrollX();
        int dx = 0;
        if (currentState == MENU_STATE){
            dx = -getChildAt(0).getMeasuredWidth() -startX;
        }else {
            dx = 0 - startX;
        }


        int duration = Math.abs(dx * 2);
        /**
         * starX：开始的x值；
         * startY：卡是的Y值；
         * dx:将要发生的水平变化量，移动x的距离；
         * dy：将要发生的竖直变化量，移动y的距离；
         * duration：数据模拟持续的时长；
         */
        scroller.startScroll(startX,0,dx,0,duration);
        invalidate();//重新绘制界面
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            int currX = scroller.getCurrX();
            scrollTo(currX,0);//滚过去
            invalidate();//重新绘制界面；
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downx = (int) ev.getX();
                downy = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:

                float xOffset = Math.abs(ev.getX() - downx);
                float yOffset = Math.abs(ev.getY() - downy);

                if (xOffset > yOffset && xOffset > 5){
                    return true; //拦截此次的触摸事件
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }
}

















