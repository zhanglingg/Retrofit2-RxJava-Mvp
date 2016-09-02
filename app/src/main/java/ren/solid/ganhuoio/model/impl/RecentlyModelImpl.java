package ren.solid.ganhuoio.model.impl;

import android.util.Log;

import java.util.List;

import ren.solid.ganhuoio.api.GankService;
import ren.solid.ganhuoio.model.IRecentlyModel;
import ren.solid.ganhuoio.model.bean.GanHuoTitleBean;

import ren.solid.ganhuoio.library.rx.retrofit.HttpResult;
import ren.solid.ganhuoio.library.rx.retrofit.TransformUtils;
import ren.solid.ganhuoio.library.rx.retrofit.factory.ServiceFactory;
import rx.Observable;

/**
 * Created by _SOLID
 * Date:2016/5/18
 * Time:14:58
 */
public class RecentlyModelImpl implements IRecentlyModel {

    @Override
    public Observable<HttpResult<List<String>>> loadRecentlyDate() {
        Log.e("RecentlyModelImpl","loadRecentlyDate");
        GankService gankService = ServiceFactory.getInstance().createService(GankService.class);
        return gankService.getRecentlyDate().compose(TransformUtils.<HttpResult<List<String>>>defaultSchedulers());
    }

    @Override
    public Observable<HttpResult<List<GanHuoTitleBean>>> loadRecentlyTitle() {
        GankService gankService = ServiceFactory.getInstance().createService(GankService.class);
        Log.e("RecentlyModelImpl","loadRecentlyTitle");
        return gankService.getTitles();

    }
}
