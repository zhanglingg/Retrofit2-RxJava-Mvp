package ren.solid.ganhuoio.library.http.provider.base;

import android.content.Context;

import ren.solid.ganhuoio.library.http.request.ImageRequest;

/**
 * Created by _SOLID
 * Date:2016/5/13
 * Time:10:25
 */
public interface IImageLoaderProvider {

    void loadImage(ImageRequest request);

    void loadImage(Context context, ImageRequest request);
}
