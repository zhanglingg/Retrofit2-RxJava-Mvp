package ren.solid.ganhuoio.presenter.impl;

import android.util.Log;

import java.util.List;

import ren.solid.ganhuoio.model.IRecentlyModel;
import ren.solid.ganhuoio.model.bean.GanHuoRecentlyWrapper;
import ren.solid.ganhuoio.model.bean.GanHuoTitleBean;
import ren.solid.ganhuoio.model.impl.RecentlyModelImpl;
import ren.solid.ganhuoio.presenter.IRecentlyPresenter;
import ren.solid.ganhuoio.ui.view.IRecentlyView;

import ren.solid.ganhuoio.library.rx.retrofit.HttpResult;
import ren.solid.ganhuoio.library.rx.retrofit.TransformUtils;
import ren.solid.ganhuoio.library.rx.retrofit.subscriber.HttpResultSubscriber;
import rx.functions.Func2;

/**
 * Created by _SOLID
 * Date:2016/5/18
 * Time:15:04
 */
public class RecentlyPresenterImpl implements IRecentlyPresenter {

    private IRecentlyView view;
    private IRecentlyModel model;

    public RecentlyPresenterImpl(IRecentlyView view) {
        this.view = view;
        model = new RecentlyModelImpl();
    }

    @Override
    public void getRecentlyDate() {

        Log.e("RecentlyPresenterImpl", "RecentlyPresenterImpl");
        //view.showLoading();

//        model.loadRecentlyDate().subscribe(new HttpResultSubscriber<List<String>>() {
//            @Override
//            public void onSuccess(List<String> stringList) {
//                view.hideLoading();
//                if (stringList != null && stringList.size() > 5)
//                    view.setDate(stringList.subList(0, 5));
//            }
//
//            @Override
//            public void _onError(Throwable e) {
//                view.hideLoading();
//                view.showError(e.getMessage());
//            }
//        });

        model
                .loadRecentlyTitle()
                .zipWith(model.loadRecentlyDate(), new Func2<HttpResult<List<GanHuoTitleBean>>, HttpResult<List<String>>, HttpResult<GanHuoRecentlyWrapper>>() {
                    @Override
                    public HttpResult<GanHuoRecentlyWrapper> call(HttpResult<List<GanHuoTitleBean>> listHttpResult, HttpResult<List<String>> listHttpResult2) {
                        HttpResult<GanHuoRecentlyWrapper> wrapper = new HttpResult<>();
                        wrapper.error = false;
                        wrapper.results = new GanHuoRecentlyWrapper();
                        //  if (stringList != null && stringList.size() > 5)
                        wrapper.results.dateList = listHttpResult2.results.subList(0, 5);
                        wrapper.results.titleList = listHttpResult.results;
                        for (int i = 0; i < wrapper.results.dateList.size(); i++) {
                            String title = wrapper.results.titleList.get(i).getTitle();
                            title = "[" + wrapper.results.dateList.get(i) + "] :" + title;
                            wrapper.results.titleList.get(i).setTitle(title);
                        }
                        return wrapper;
                    }
                }).compose(TransformUtils.<HttpResult<GanHuoRecentlyWrapper>>defaultSchedulers())
                .subscribe(new HttpResultSubscriber<GanHuoRecentlyWrapper>() {
                    @Override
                    public void onSuccess(GanHuoRecentlyWrapper ganHuoRecentlyWrapper) {
                        view.hideLoading();
                        view.setDate(ganHuoRecentlyWrapper);
                    }

                    @Override
                    public void _onError(Throwable e) {
                        view.hideLoading();
                        Log.e("showError", "出错了 end");
                        view.showError(e.getMessage());
                    }
                });

    }
}
