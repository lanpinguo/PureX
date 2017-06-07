package com.jw.android.huddroid;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.pure.purex.R;

public class HUDDroid {

    private TimeSpan length;
    private static HUDDroid instance;
    private Dialog currentDialog;
    private ProgressWheel progressWheel;

    public static HUDDroid getInstance() {
        if (instance == null) {
            instance = new HUDDroid();
        }

        return instance;
    }

    private HUDDroid() {}

    /** Basic builders **/

    public Builder build(Context activityContext) {
        dismissCurrent();

        currentDialog = buildBasicHUD(activityContext);
        Builder builder = new Builder().setIcon(IconType.Progress_Indeterminant);
        return builder;
    }

    public Builder buildToast(Context activityContext, String status) {
        dismissCurrent();

        currentDialog = buildToastHUD(activityContext);
        Builder builder = new Builder().setMessage(status);
        return builder;
    }


    /** Convenience builders **/

    public Builder build(Context activityContext, int progress) {
        return build(activityContext)
                .setIcon(IconType.Progress)
                .setProgress(progress);
    }

    public Builder buildSuccess(Context activityContext) {
        return build(activityContext)
                .setIcon(IconType.Success);
    }

    public Builder buildFailure(Context activityContext) {
        return build(activityContext)
                .setIcon(IconType.Failure);
    }

    public Builder buildSuccessWithStatus(Context activityContext, String status) {
        return build(activityContext)
                .setIcon(IconType.Success)
                .setMessage(status);
    }

    public Builder buildFailureWithStatus(Context activityContext, String status) {
        return build(activityContext)
                .setIcon(IconType.Failure)
                .setMessage(status);
    }

    public Builder buildImage(Context activityContext, int resId) {
        return build(activityContext)
                .setIcon(IconType.Custom, resId);
    }

    public Builder buildImage(Context activityContext, Drawable drawable) {
        return build(activityContext)
                .setIcon(IconType.Custom, drawable);
    }


    /** internal builder methods **/

    private Dialog buildBasicHUD(Context activityContext) {
        Dialog dialog = new Dialog(activityContext, R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_hud);

        dialog.getWindow().getAttributes().gravity=Gravity.CENTER;
        return dialog;
    }

    private static Dialog buildToastHUD(Context activityContext) {
        Dialog dialog = new Dialog(activityContext, R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.toast_hud);

        dialog.getWindow().getAttributes().gravity=Gravity.BOTTOM;
        return dialog;
    }


    /** util **/

    public void dismissCurrent() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();

            currentDialog = null;
            progressWheel = null;
            length = null;
        }

    }

    public void updateProgress(int progress) {
        if (currentDialog == null || progressWheel == null) {
            return;
        }

        progressWheel.setProgress(progress);
    }

    /** internal builder class **/

    public class Builder {

        private Builder() { }

        public Builder setMessage(String message) {
            if(message == null || message.length() == 0) {
                currentDialog.findViewById(R.id.message).setVisibility(View.GONE);
            } else {
                TextView txt = (TextView)currentDialog.findViewById(R.id.message);
                txt.setText(message);
            }
            return this;
        }

        public Builder setMaskType(MaskType maskType) {

            float dimAmount = 0.0f;

            if (maskType == MaskType.Black) {
                dimAmount = 0.7f;
            }

            WindowManager.LayoutParams lp = currentDialog.getWindow().getAttributes();
            lp.dimAmount= dimAmount;
            currentDialog.getWindow().setAttributes(lp);

            return this;
        }

        public Builder setTimeSpan(TimeSpan time) {
            length = time;
            return this;
        }


        public Builder setOnClickListener(View.OnClickListener listener) {
            currentDialog.findViewById(R.id.container_view).setOnClickListener(listener);
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener listener) {
            currentDialog.setCancelable(true);
            currentDialog.setOnCancelListener(listener);
            return this;
        }



        private Builder setProgress(int progress) {
            progressWheel.setProgress(progress);
            return this;
        }

        private Builder setIcon(IconType type) {
            return setIcon(type, -1, null);
        }

        private Builder setIcon(IconType type, Drawable drawable) {
            return setIcon(type, -1, drawable);
        }

        private Builder setIcon(IconType type, int resId) {
            return setIcon(type, resId, null);
        }

        private Builder setIcon(IconType type, int resId, Drawable drawable) {
            ImageView icon = (ImageView) currentDialog.findViewById(R.id.spinnerImageView);
            progressWheel = (ProgressWheel) currentDialog.findViewById(R.id.progressWheel);

            icon.setVisibility(View.VISIBLE);
            progressWheel.setVisibility(View.GONE);

            switch (type) {
                case Success:
                    icon.setImageResource(R.drawable.ic_successstatus);
                    break;
                case Failure:
                    icon.setImageResource(R.drawable.ic_errorstatus);
                    break;
                case Progress_Indeterminant:
                    progressWheel.spin();
                case Progress:
                    progressWheel.setVisibility(View.VISIBLE);
                    icon.setVisibility(View.GONE);
                    break;
                case Custom:
                    if (resId != -1) {
                        icon.setImageResource(resId);
                    } else if (drawable != null) {
                        icon.setImageDrawable(drawable);
                    }
            }

            return this;
        }



        public Dialog show() {
            currentDialog.show();

            if (length != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissCurrent();
                    }
                }, length.millis);
            }

            return currentDialog;
        }

    }





}