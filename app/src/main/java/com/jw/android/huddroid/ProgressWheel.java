package com.jw.android.huddroid;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Jon Weissenburger on 5/12/16.
 */
public class ProgressWheel extends View {
    public ProgressWheel(Context context) {
        this(context, null, 0);
    }

    public ProgressWheel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressWheel (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        circleRadius = 80;
        barLength = 60;
        barWidth = 20;
        rimWidth = 20;
        textSize = 20;

        //Padding (with defaults)
        wheelPaddingTop = 5;
        wheelPaddingBottom = 5;
        wheelPaddingLeft = 5;
        wheelPaddingRight = 5;

        //Colors (with defaults)
        barColor = Color.WHITE;
        circleColor = Color.TRANSPARENT;
        rimColor = Color.GRAY;
        textColor = Color.WHITE;

        //Animation
        //The amount of pixels to move the bar by on each draw
        spinSpeed = 2;
        //The number of milliseconds to wait inbetween each draw
        delayMillis = 0;

        spinHandler = new SpinHandler(new SpinAction() {
            @Override
            public void onUpdate() {
                invalidate();

                if (isSpinning)
                {
                    progress += spinSpeed;
                    if (progress > 360)
                        progress = 0;

                    spinHandler.sendEmptyMessageDelayed (0, delayMillis);
                }

            }
        });


        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                setupBounds();
                setupPaints();
                return false;
            }
        });


        //ParseAttributes(context.ObtainStyledAttributes(attrs, null)); //TODO: swap null for R.styleable.ProgressWheel
    }

//		public string Text
//		{
//			get { return text; }
//			set { text = value; splitText = text.Split('\n'); }
//		}

    //public string[] SplitText { get { return splitText; } }


    public int circleRadius;
    public int barLength;
    public int barWidth;
    public int textSize;
    public int wheelPaddingTop;
    public int wheelPaddingBottom;
    public int wheelPaddingLeft;
    public int wheelPaddingRight;
    public int barColor;
    public int circleColor;
    public int rimColor;
    public Shader rimShader;
    public int textColor;
    public int spinSpeed;
    public int rimWidth;
    public int delayMillis;

    //Sizes (with defaults)
    int fullRadius = 100;

    //Paints
    Paint barPaint = new Paint();
    Paint circlePaint = new Paint();
    Paint rimPaint = new Paint();
    Paint textPaint = new Paint();

    //Rectangles
    //RectF rectBounds = new RectF();
    RectF circleBounds = new RectF();

    int progress = 0;
    boolean isSpinning = false;
    SpinHandler spinHandler;

    int version = Build.VERSION.SDK_INT;

    //Other
    //string text = "";
    //string[] splitText = new string[]{};


//    @Override
//    public void addOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
//        super.addOnAttachStateChangeListener(listener);
//    }

//    @Override
//    protected void onAttachedToWindow ()
//    {
//        base.OnAttachedToWindow ();
//
//        SetupBounds ();
//        SetupPaints ();
//
//        Invalidate ();
//    }

    void setupPaints()
    {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Paint.Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);

        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
    }

    void setupBounds()
    {
        circleBounds = new RectF(wheelPaddingLeft + barWidth,
                wheelPaddingTop + barWidth,
                this.getLayoutParams().width - wheelPaddingRight - barWidth,
                this.getLayoutParams().height - wheelPaddingBottom - barWidth);

        fullRadius = (this.getLayoutParams().width - wheelPaddingRight - barWidth)/2;
        circleRadius = (fullRadius - barWidth) + 1;
    }

//		void ParseAttributes(Android.Content.Res.TypedArray a)
//		{
//			BarWidth = (int) a.GetDimension(R.styleable.ProgressWheel_barWidth, barWidth);
//			RimWidth = (int) a.GetDimension(R.styleable.ProgressWheel_rimWidth, rimWidth);
//			SpinSpeed = (int) a.GetDimension(R.styleable.ProgressWheel_spinSpeed, spinSpeed);
//			DelayMillis = (int) a.GetInteger(R.styleable.ProgressWheel_delayMillis, delayMillis);
//
//			if(DelayMillis < 0)
//				DelayMillis = 0;
//
//			BarColor = a.GetColor(R.styleable.ProgressWheel_barColor, barColor);
//			BarLength = (int) a.GetDimension(R.styleable.ProgressWheel_barLength, barLength);
//			TextSize = (int) a.GetDimension(R.styleable.ProgressWheel_textSize, textSize);
//			TextColor = (int) a.GetColor(R.styleable.ProgressWheel_textColor, textColor);
//
//			Text = a.GetString(R.styleable.ProgressWheel_text);
//
//			RimColor = (int) a.GetColor(R.styleable.ProgressWheel_rimColor, rimColor);
//			CircleColor = (int) a.GetColor(R.styleable.ProgressWheel_circleColor, circleColor);
//		}



    //----------------------------------
    //Animation stuff
    //----------------------------------
    @Override
    protected void onDraw (Canvas canvas)
    {
        super.onDraw(canvas);

        //Draw the rim
        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);

        //Draw the bar
        if(isSpinning)
            canvas.drawArc(circleBounds, progress - 90, barLength, false, barPaint);
        else
            canvas.drawArc(circleBounds, -90, progress, false, barPaint);

        //Draw the inner circle
        canvas.drawCircle((circleBounds.width() / 2) + rimWidth + wheelPaddingLeft,
                (circleBounds.height() / 2) + rimWidth + wheelPaddingTop,
                circleRadius,
                circlePaint);

        //Draw the text (attempts to center it horizontally and vertically)
//			int offsetNum = 2;
//
//			foreach (var s in splitText)
//			{
//				float offset = textPaint.MeasureText(s) / 2;
//
//				canvas.DrawText(s, this.Width / 2 - offset,
//				                this.Height / 2 + (TextSize * (offsetNum))
//				                - ((splitText.Length - 1) * (TextSize / 2)), textPaint);
//				offsetNum++;
//			}
    }

    public void resetCount()
    {
        progress = 0;
        //Text = "0%";
        invalidate();
    }

    public void stopSpinning()
    {
        isSpinning = false;
        progress = 0;
        spinHandler.removeMessages(0);
    }


    public void spin()
    {
        isSpinning = true;
        spinHandler.sendEmptyMessage(0);
    }

    public void incrementProgress()
    {
        isSpinning = false;
        progress++;
        //Text = Math.Round(((float)progress/(float)360)*(float)100) + "%";
        spinHandler.sendEmptyMessage(0);
    }

    public void setProgress(int i) {
        isSpinning = false;
        int newProgress = (int)((float)i / (float)100 * (float)360);

        if (version >= Build.VERSION_CODES.HONEYCOMB)
        {
            ValueAnimator va =
                    (ValueAnimator)ValueAnimator.ofInt(progress, newProgress).setDuration (250);

            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    progress = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });

            //Text = Math.Round(((float)interimValue/(float)360)*(float)100) + "%";



            va.start ();
        } else {
            progress = newProgress;
            invalidate();
        }

        spinHandler.sendEmptyMessage(0);
    }



    class SpinHandler extends Handler {

        SpinAction action;

        public SpinHandler(SpinAction action) {
            this.action = action;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            action.onUpdate();
        }


    }

    interface SpinAction {
        void onUpdate();
    }


}

