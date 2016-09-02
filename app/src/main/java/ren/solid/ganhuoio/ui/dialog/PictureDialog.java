package ren.solid.ganhuoio.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import ren.solid.ganhuoio.R;
import ren.solid.ganhuoio.library.http.HttpClientManager;

/**
 * Created by _SOLID
 * Date:2016/6/2
 * Time:17:32
 */
public class PictureDialog extends Dialog {

    private ImageView mImageView;

    public PictureDialog(Context context) {
        super(context);
    }

    public PictureDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PictureDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_picture);
        setCanceledOnTouchOutside(true);
        mImageView = (ImageView) findViewById(R.id.iv_picture);
        mImageView.setImageResource(R.drawable.avastar);
    }

    public void setPicture(String url) {
        show();
        HttpClientManager.displayImage(mImageView, url);
    }


}
