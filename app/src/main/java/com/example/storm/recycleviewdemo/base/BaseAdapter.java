package com.example.storm.recycleviewdemo.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.storm.recycleviewdemo.ViewHolder.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Storm on 2017/3/25.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_COMMON_VIEW = 100001; // 普通类型的view
    public static final int TYPE_FOOTER_VIEW = 100002;  // footer 类型 view
    public static final int TYPE_EMPTY_VIEW = 100003; //empty  空view 即初始化加载时的提示view
    public static final int TYPE_NODATE_VIEW = 100004; // 初次加载无数据时候显示的view
    public static final int TYPE_RELOAD_VIEW = 100005;  //初次加载没有数据 点击可以重新加载的view  提示view


    protected Context mContext; // 上下文
    protected List<T> mDatas;// 数据

    private boolean mIsOpenLoadMore; // 是否开启加载更多
    private boolean isRemoveEmptyView;
    private boolean isAutoLoadMore = true; // 是否开启自动加载

    private View mLoadEndView;  // 下拉加载数据完成时候的view
    private View mLoadFailView; // 初始加载失败布局
    private View mLoadingView;// 分页加载更多时候的提示view 正在加载...
    private View mReloadView; // 点击重新加载的view
    private View mEmptyView; // 初次加载时候的view
    private RelativeLayout mFooterLayout; // footer view de buju
    private int footViewCount;

    private OnLoadMoreListener mOnLoadMoreListener;

    protected abstract int getViewType(int position, T t);

    public BaseAdapter(Context context, boolean isOpenLoadMore) {
        mContext = context;
        mIsOpenLoadMore = isOpenLoadMore;
        mDatas = new ArrayList<T>();
        Log.d("TAG", "设置的数据");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("TAG", "onCreateViewHolder");
        ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_FOOTER_VIEW:
                if (mFooterLayout == null) {
                    mFooterLayout = new RelativeLayout(mContext);
                }
                viewHolder = ViewHolder.create(mFooterLayout);
                break;
            case TYPE_EMPTY_VIEW: //shu shi hua
                viewHolder = ViewHolder.create(mEmptyView);
                break;
            case TYPE_COMMON_VIEW:
                viewHolder = ViewHolder.create(new View(mContext));
                break;
            case TYPE_RELOAD_VIEW:
                viewHolder = ViewHolder.create(mReloadView);
                break;

        }

        return viewHolder;

    }


    /**
     * 获取item的数量
     *
     * @return
     */
    @Override
    public int getItemCount() {


        if (mDatas.isEmpty() && mEmptyView != null) { //数据为空且设置了加载为空的view
            return 1;
        }

        Log.d("TAG", "getItemCount" + (mDatas.size() + getFootViewCount()));
        return mDatas.size() + getFootViewCount();

    }


    /**
     * 获取item view 的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        Log.d("TAG", "getItemViewType");
        if (mDatas.isEmpty()) { //数据为空的时候显示
            if (mEmptyView != null && !isRemoveEmptyView) { //首次预加载时显示的view 并且没有失败
                return TYPE_EMPTY_VIEW;
            }

            if (isRemoveEmptyView && mReloadView != null) {
                return TYPE_RELOAD_VIEW; // 再次加载的view
            } else {
                return TYPE_NODATE_VIEW; // 没有移除空的view 显示无数据的view
            }

        }

        if (isFootView(position)) { //当前的position 是foot
            return TYPE_FOOTER_VIEW;
        }
        Log.d("TAG", "getItemViewType");

        return getViewType(position, mDatas.get(position));
    }


    /**
     * 根据positon 获取data
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        if (mDatas.isEmpty()) {
            return null;
        }

        return mDatas.get(position);
    }


    /**
     * 是否为footview
     *
     * @param position
     * @return
     */
    private boolean isFootView(int position) {
        return mIsOpenLoadMore && position >= getItemCount() - 1;


    }

    /**
     * 判断是不是常用类型
     *
     * @param viewType
     * @return
     */
    protected boolean isCommonItemView(int viewType) {
        return viewType != TYPE_EMPTY_VIEW && viewType != TYPE_FOOTER_VIEW
                && viewType != TYPE_NODATE_VIEW && viewType != TYPE_RELOAD_VIEW;

    }

    /**
     * StaggeredGridLayoutManager 时候 footView 可以占据一行
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFootView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) lp;
                params.setFullSpan(true);

            }
        }

    }

    /**
     * GridLayoutManager 布局 footview 可占据一行
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager
                    = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFootView(position)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }

            });
        }
        //开始加载更多
        startLoadMore(recyclerView, layoutManager);

    }

    /**
     * 判断列表是否滑动到底部
     *
     * @param recyclerView
     * @param layoutManager
     */
    private void startLoadMore(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {

        if (!mIsOpenLoadMore || mOnLoadMoreListener == null) {//没有开启加载或者没有加载更多的监听
            return;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isAutoLoadMore && findLastVisableItemPoition(layoutManager) + 1 == getItemCount()) {
                        scrollLoadMore();
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isAutoLoadMore && findLastVisableItemPoition(layoutManager) + 1 == getItemCount()) {
                    scrollLoadMore();
                } else if (isAutoLoadMore) {
                    isAutoLoadMore = false;
                }
            }
        });

    }

    private void scrollLoadMore() {
        if (mFooterLayout.getChildAt(0) == mLoadingView) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.OnloadMore(false);
            }
        }
    }


    private int findLastVisableItemPoition(RecyclerView.LayoutManager layoutManager) {

        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            int max = lastVisibleItemPositions[0];
            for (int value : lastVisibleItemPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }

        return -1;
    }


    /**
     * 清空footerview
     */
    private void removeFootView() {
        mFooterLayout.removeAllViews();

    }

    /**
     * 添加新的footerview
     *
     * @param footView
     */
    private void addFootView(View footView) {
        if (footView == null) {
            return;
        }

        if (mFooterLayout == null) {
            mFooterLayout = new RelativeLayout(mContext);
        }
        removeFootView();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterLayout.addView(footView);
    }

    /**
     * 刷新加载更多的数据
     *
     * @param data
     */
    public void refreshLoadMoreData(List<T> data) {
        if (data != null && data.size() > 0) {
            int size = data.size();
            mDatas.addAll(data);
            notifyItemInserted(size);
        }
    }


    /**
     * 加载数据 (包括下拉刷新 是重新获取的数据)
     *
     * @param datas
     */
    public void refreshData(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }

    }

    /**
     * 下拉刷新获取的数据
     *
     * @param datas
     */
    public void refreshNewData(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            //如果下拉刷新时候得到的是新的数据
            mDatas.addAll(0, datas);
            notifyDataSetChanged();
        }

    }

    /**
     * 移除数据
     *
     * @param position
     */
    public void remove(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 初始化加载中的布局
     *
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
        addFootView(mLoadingView);
    }

    public void setLoadingView(int loadingViewId) {

        if (loadingViewId <= 0) {
            return;
        }
        View loadingView = LayoutInflater.from(mContext).inflate(loadingViewId, null);
        setLoadingView(loadingView);
    }

    /**
     * 初始化加载失败的布局
     *
     * @param loadFailView
     */
    public void setloadFailView(View loadFailView) {
        if (loadFailView == null) {
            return;
        }
        mLoadFailView = loadFailView;
        addFootView(mLoadFailView);

        mLoadFailView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addFootView(mLoadingView); //显示加载中的view
                if (mOnLoadMoreListener != null) {
                    mOnLoadMoreListener.OnloadMore(true);

                }
            }
        });
    }

    public void setLoadFailView(int loadFailViewId) {
        if (loadFailViewId <= 0) {
            return;
        }
        View loadFailView = LayoutInflater.from(mContext).inflate(loadFailViewId, null);
        setloadFailView(loadFailView);
    }

    /**
     * 初始化加载完成的视图
     *
     * @param loadEndView
     */
    public void setLoadEndView(View loadEndView) {
        mLoadEndView = loadEndView;
        addFootView(mLoadEndView);
    }

    public void setLoadEndView(int loadEndViewId) {
        if (loadEndViewId <= 0) {
            return;
        }
        View loadEndView = LayoutInflater.from(mContext).inflate(loadEndViewId, null);
        setLoadEndView(loadEndView);
    }

    /**
     * 初始化空的view
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    /**
     * 移除空的view
     */
    public void removeEmptyView() {
        isRemoveEmptyView = true;
        notifyDataSetChanged();
    }

    /**
     * 初次预加载失败 或者无数据显示的时候显示该view  进行重新加载提示用户无数据
     *
     * @param reloadView
     */
    public void setReloadView(View reloadView) {
        mReloadView = reloadView;
        isRemoveEmptyView = true;
        notifyDataSetChanged();
    }


    /**
     * 获取footview的数量
     *
     * @return
     */
    public int getFootViewCount() {
        //开启加载更多  数据不为空返回1
        return mIsOpenLoadMore && !mDatas.isEmpty() ? 1 : 0;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;

    }

}
