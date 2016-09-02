package ren.solid.ganhuoio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ren.solid.ganhuoio.R;
import ren.solid.ganhuoio.constant.Apis;
import ren.solid.ganhuoio.model.bean.GanHuoDataBean;
import ren.solid.ganhuoio.model.bean.GanHuoRecentlyBean;
import ren.solid.ganhuoio.library.activity.ViewPicActivity;
import ren.solid.ganhuoio.library.activity.WebViewActivity;
import ren.solid.ganhuoio.library.adapter.SolidRVBaseAdapter;
import ren.solid.ganhuoio.library.fragment.XRecyclerViewFragment;
import ren.solid.ganhuoio.library.utils.DateUtils;
import ren.solid.ganhuoio.library.utils.StringStyleUtils;
import ren.solid.ganhuoio.library.utils.json.JsonConvert;

/**
 * Created by _SOLID
 * Date:2016/5/18
 * Time:15:24
 */
public class RecentlyListFragment extends XRecyclerViewFragment<GanHuoDataBean> {

    public static final String DATE_STRING = "dateString";
    public static final String TITLE = "fragment_index";

    private String date;
    private String title = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getArguments().getString(DATE_STRING).replace('-', '/');
        title = getArguments().getString(TITLE);
    }

    @Override
    protected String getUrl(int mCurrentPageIndex) {
        String url = Apis.Urls.GanHuoDataByDay + date;
        return url;
    }

    @Override
    protected List<GanHuoDataBean> parseData(String result) {
        GanHuoRecentlyBean recentlyBean;
        List<GanHuoDataBean> list = new ArrayList<>();
        JsonConvert<GanHuoRecentlyBean> jsonConvert = new JsonConvert<GanHuoRecentlyBean>() {
        };
        jsonConvert.setDataName("results");
        recentlyBean = jsonConvert.parseData(result);
        if (recentlyBean != null) {
            if (recentlyBean.get休息视频() != null) list.addAll(recentlyBean.get休息视频());
            if (recentlyBean.getAndroid() != null) list.addAll(recentlyBean.getAndroid());
            if (recentlyBean.getIOS() != null) list.addAll(recentlyBean.getIOS());
            if (recentlyBean.get瞎推荐() != null) list.addAll(recentlyBean.get瞎推荐());
            if (recentlyBean.get福利() != null)
                addHeadTextAndImage(title, recentlyBean.get福利().get(0).getUrl());
        }
        return list;
    }

    @Override
    protected SolidRVBaseAdapter setAdapter() {
        return new SolidRVBaseAdapter<GanHuoDataBean>(getMContext(), new ArrayList<GanHuoDataBean>()) {

            @Override
            protected void onBindDataToView(SolidCommonViewHolder holder, GanHuoDataBean bean, int position) {

                if (position == 0) {
                    holder.getView(R.id.tv_category).setVisibility(View.VISIBLE);
                } else {
                    boolean theCategoryOfLastEqualsToThis = mBeans.get(
                            position - 1).getType().equals(mBeans.get(position).getType());
                    if (theCategoryOfLastEqualsToThis) {
                        holder.getView(R.id.tv_category).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.tv_category).setVisibility(View.VISIBLE);
                    }
                }

                SpannableStringBuilder builder = new SpannableStringBuilder(bean.getDesc()).append(
                        StringStyleUtils.format(getMContext(), " (by " +
                                bean.getWho() + "   At: " + DateUtils.friendlyTime(bean.getPublishedAt().replace('T', ' ').replace('Z', ' ')) +
                                ")", R.style.ByTextAppearance));
                CharSequence descText = builder.subSequence(0, builder.length());
                holder.setText(R.id.tv_category, bean.getType());
                holder.setText(R.id.tv_title, descText);
            }

            @Override
            protected void onItemClick(int position) {
                String url = mBeans.get(position - getHeadViewCount()).getUrl();
                ArrayList<String> images = new ArrayList<String>();
                images.add(url);
                if (!isImage(url)) {
                    Intent intent = new Intent(getMContext(), WebViewActivity.class);
                    intent.putExtra(WebViewActivity.WEB_URL, url);
                    intent.putExtra(WebViewActivity.TITLE, mBeans.get(position - getHeadViewCount()).getDesc());
                    getMContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(getMContext(), ViewPicActivity.class);
                    intent.putStringArrayListExtra(ViewPicActivity.IMG_URLS, images);
                    getMContext().startActivity(intent);
                }
            }

            @Override
            public int getItemLayoutID(int viewType) {
                return R.layout.item_recently;
            }

        };
    }

    @Override
    protected RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getMContext());
    }

    @Override
    public boolean isHaveLoadMore() {
        return false;
    }

    public boolean isImage(String url) {
        return url.endsWith(".jpg") || url.endsWith(".png");
    }
}
