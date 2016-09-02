package ren.solid.ganhuoio.library.http.provider;

import android.content.Context;

import com.squareup.picasso.Picasso;

import ren.solid.ganhuoio.R;
import ren.solid.ganhuoio.library.SolidApplication;
import ren.solid.ganhuoio.library.http.provider.base.IImageLoaderProvider;
import ren.solid.ganhuoio.library.http.request.ImageRequest;
import ren.solid.ganhuoio.library.utils.SettingUtils;


/**
 * Created by _SOLID
 * Date:2016/5/13
 * Time:10:27
 */
public class PicassoImageLoaderProvider implements IImageLoaderProvider {
    @Override
    public void loadImage(ImageRequest request) {
        if (!SettingUtils.onlyWifiLoadImage()) {
            Picasso.with(SolidApplication.getInstance()).load(request.getUrl()).placeholder(request.getPlaceHolder()).into(request.getImageView());
        } else {
            request.getImageView().setImageResource(R.drawable.default_load_img);
        }
    }

    @Override
    public void loadImage(Context context, ImageRequest request) {
        if (!SettingUtils.onlyWifiLoadImage()) {
            Picasso.with(context).load(request.getUrl()).placeholder(request.getPlaceHolder()).into(request.getImageView());
        } else {
            request.getImageView().setImageResource(R.drawable.default_load_img);
        }
    }
}
