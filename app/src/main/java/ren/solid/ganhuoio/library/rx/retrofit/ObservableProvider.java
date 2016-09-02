
package ren.solid.ganhuoio.library.rx.retrofit;

import android.util.Log;

import okhttp3.ResponseBody;
import ren.solid.ganhuoio.library.rx.retrofit.factory.ServiceFactory;
import ren.solid.ganhuoio.library.rx.retrofit.func.RetryWhenNetworkException;
import ren.solid.ganhuoio.library.rx.retrofit.service.CommonService;
import ren.solid.ganhuoio.library.rx.retrofit.func.ResultFunc;
import ren.solid.ganhuoio.library.rx.retrofit.func.StringFunc;
import ren.solid.ganhuoio.library.rx.retrofit.subscriber.DownLoadSubscribe;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by _SOLID
 * Date:2016/7/28
 * Time:9:22
 */
public class ObservableProvider {

    private CommonService mCommonService;

    private static class DefaultHolder {
        private static ObservableProvider INSTANCE = new ObservableProvider();
    }

    private ObservableProvider() {
        mCommonService = ServiceFactory.getInstance().createService(CommonService.class);

    }

    public static ObservableProvider getDefault() {
        return DefaultHolder.INSTANCE;
    }

    public Observable<String> loadString(String url) {

        Log.e("ObservableProvider", url);

        return mCommonService
                .loadString(url)
                .compose(TransformUtils.<ResponseBody>defaultSchedulers())
                .retryWhen(new RetryWhenNetworkException())
                .map(new StringFunc());
    }

    public <T> Observable<HttpResult<T>> loadResult(String url) {
        return loadString(url).map(new ResultFunc<T>());
    }

    public void download(String url, final DownLoadSubscribe subscribe) {
        mCommonService
                .download(url)
                .compose(TransformUtils.<ResponseBody>all_io())
                .doOnNext(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        subscribe.writeResponseBodyToDisk(responseBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        subscribe.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscribe.onError(e);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        //do nothing
                    }
                });

    }


}
