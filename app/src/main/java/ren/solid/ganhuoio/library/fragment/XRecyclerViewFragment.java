package ren.solid.ganhuoio.library.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import ren.solid.ganhuoio.R;
import ren.solid.ganhuoio.library.rx.retrofit.TransformUtils;

import ren.solid.ganhuoio.library.fragment.base.BaseListFragment;
import ren.solid.ganhuoio.library.http.HttpClientManager;
import ren.solid.ganhuoio.library.utils.StringUtils;
import ren.solid.ganhuoio.library.utils.ViewUtils;
import rx.Observable;
import rx.Subscriber;


/**
 * Created by _SOLID
 * Date:2016/4/18
 * Time:17:36
 * <p/>
 * common fragment for list data display ,and you can extends this fragment for everywhere you want to display list data
 */
public abstract class XRecyclerViewFragment<T> extends BaseListFragment {

    private static String TAG = "XRecyclerViewFragment";

    private XRecyclerView mRecyclerView;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_xrecyclerview;
    }

    @Override
    protected void setUpView() {

        mRecyclerView = $(R.id.recyclerview);

        mRecyclerView.setLayoutManager(setLayoutManager());
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                switchActionAndLoadData(ACTION_REFRESH);
            }

            @Override
            public void onLoadMore() {
                switchActionAndLoadData(ACTION_LOAD_MORE);
            }
        });

        mRecyclerView.setLoadingMoreEnabled(isHaveLoadMore());
    }


    public boolean isHaveLoadMore() {
        return true;
    }

    boolean isAddHead = false;
    ImageView img_head;
    TextView txt_head;

    protected void addHeadTextAndImage(final String title, final String url) {
        if (img_head == null) {
            img_head = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            img_head.setLayoutParams(layoutParams);

            txt_head = new TextView(getContext());
            txt_head.setTextSize(ViewUtils.dp2px(getContext(), 6));
            txt_head.setTextColor(Color.BLACK);
            txt_head.getPaint().setFakeBoldText(true);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = ViewUtils.dp2px(getContext(), 10);
            marginLayoutParams.setMargins(margin, margin, margin, margin);
            txt_head.setLayoutParams(marginLayoutParams);
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (!isAddHead) {
                    mRecyclerView.addHeaderView(txt_head);
                    mRecyclerView.addHeaderView(img_head);
                }
                isAddHead = true;
                txt_head.setText(title);
                HttpClientManager.displayImage(img_head, url);
            }
        });
    }

    @Override
    protected void setUpData() {

        loadData();
    }

    @Override
    protected void onDataErrorReceived() {
        Log.i(TAG, "onDataErrorReceived");

        loadComplete();
    }

    @Override
    protected void onDataSuccessReceived(final String result) {
        Log.i(TAG, "onDataSuccessReceived");
        if (!StringUtils.isNullOrEmpty(result)) {
            Observable
                    .create(new Observable.OnSubscribe<List<T>>() {
                        @Override
                        public void call(Subscriber<? super List<T>> subscriber) {
                            List<T> list = parseData(result);
                            subscriber.onNext(list);
                            subscriber.onCompleted();
                        }
                    })
                    .compose(TransformUtils.<List<T>>defaultSchedulers())
                    .subscribe(new Subscriber<List<T>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                        }

                        @Override
                        public void onNext(List<T> list) {
                            mAdapter.addAll(list, mCurrentAction == ACTION_REFRESH);
                            if (mCurrentAction != ACTION_PRE_LOAD) loadComplete();

                        }
                    });

        } else {
            if (!(mCurrentAction == ACTION_PRE_LOAD))
                onDataErrorReceived();
        }
    }

    @Override
    protected void loadComplete() {
        if (mCurrentAction == ACTION_REFRESH)
            mRecyclerView.refreshComplete();
        if (mCurrentAction == ACTION_LOAD_MORE)
            mRecyclerView.loadMoreComplete();
    }

    protected int getHeadViewCount() {
        return 3;
    }


}
