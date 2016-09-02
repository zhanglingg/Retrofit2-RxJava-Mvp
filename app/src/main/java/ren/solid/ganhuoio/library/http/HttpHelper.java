package ren.solid.ganhuoio.library.http;

import ren.solid.ganhuoio.library.http.provider.base.IHttpProvider;
import ren.solid.ganhuoio.library.http.provider.OKHttpProvider;

/**
 * Created by _SOLID
 * Date:2016/5/13
 * Time:11:19
 */
@Deprecated
public class HttpHelper {

    private static volatile IHttpProvider mProvider;

    public static IHttpProvider getProvider() {
        if (mProvider == null) {
            synchronized (HttpHelper.class) {
                if (mProvider == null) {
                    mProvider = new OKHttpProvider();
                }
            }
        }
        return mProvider;
    }
}
