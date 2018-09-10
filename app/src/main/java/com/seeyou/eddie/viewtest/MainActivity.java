package com.seeyou.eddie.viewtest;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    protected static boolean mImageViewIsVisible = false;
    protected static ViewGroup mLayout;
    protected static VideoView mVideoView;
    protected static ImageView mImageView;
    protected static MainActivity mSingleton;
    protected static Handler commandHandler;
    protected static ConstraintLayout layoutroot;
    protected static String packageName;
    private HandlerThread handleThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutroot = (ConstraintLayout)findViewById(R.id.layoutroot1);
        packageName = getPackageName();
        mSingleton = this;
        commandHandler = new Handler();
        AsynShowVideoViewThread curThreadRunnable = new AsynShowVideoViewThread();
        Thread curThread = new Thread(curThreadRunnable);
        curThread.start();
        //new ThreadCustom().start();
        handleThread = new HandlerThread("HandlerThread");
        handleThread.start();
        final Handler handler = new Handler(handleThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                System.out.println("收到消息");
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);//模拟耗时操作
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    class ThreadCustom extends Thread{
        @Override
        public void run() {
            super.run();
            new ShowVideoViewTask().run();
        }
    }

    static class AsynShowVideoViewThread implements Runnable {
        @Override
        public void run() {
            commandHandler.post(new ShowVideoViewTask());
            //new ShowVideoViewTask().run();
        }
    }

    static class ShowVideoViewTask implements Runnable {
        @Override
        public void run() {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    -1, -1);
            // RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT

            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.CENTER_VERTICAL);

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                    300, 72);
            params1.rightMargin = 40;
            params1.topMargin = 40;
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //mLayout = new RelativeLayout(this);
            //setContentView(mLayout);

            //RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(-1, -1);
            //mLayout.setLayoutParams(param);

            mVideoView = new SeaShellVideoView(mSingleton);
            mVideoView.setLayoutParams(params);
            mVideoView.setClickable(false);
            mVideoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.opvideo));

            //mImageView = findViewById(R.id.imageView);
            mImageView = new ImageView(mSingleton);
            mImageView.setId(View.generateViewId());
            mImageView.setImageResource(R.drawable.tupian);
            mImageView.setLayoutParams(params1);

            layoutroot.addView(mVideoView);
            layoutroot.addView(mImageView);
            //mImageView.bringToFront();

            mVideoView.seekTo(0);
            mVideoView.requestFocus();
            mVideoView.start();
            mVideoView.setVisibility(View.VISIBLE);
            //mImageView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.INVISIBLE);
            mImageViewIsVisible = false;
        }
    }
}

class SeaShellVideoView extends VideoView {
    public SeaShellVideoView(Context context) {
        super(context);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            {
                if (MainActivity.mImageViewIsVisible)
                {
                }
                else
                {
                    MainActivity.mImageViewIsVisible = true;
                    MainActivity.mImageView.setVisibility(View.VISIBLE);
                    //MainActivity.mImageView.requestLayout();
                }
            }
            break;
        }
        return true;
    }
}

