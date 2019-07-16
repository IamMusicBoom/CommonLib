package foundation.base.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.common.library.R;

import java.util.ArrayList;

import foundation.enums.RefreshState;
import foundation.widget.recyclerView.GridSpaceItemDecoration;
import foundation.widget.recyclerView.SpaceItemDecoration;
import foundation.widget.refresh.RefreshRecyclerView;
import foundation.widget.refresh.adapter.BaseRecyclerViewAdapter;

/**
 * Created by wfpb on 15/6/25.
 * <p/>
 * 列表Activity
 */
public abstract class BaseRecyclerViewActivity<T> extends BaseActivity implements RefreshRecyclerView.PullLoadMoreListener {

    protected final int kPageSize = 10;
    public int kPage = 1;
    //网络列
    protected int spanCount = 2;
    //网格方向
    protected int orientation = 1;
    protected int spacingInPixels;
    public RecyclerView mRecyclerView;

    public RefreshRecyclerView _refreshLayout;

    protected ArrayList<T> _dataSource = new ArrayList<>();
    private RefreshState _RefreshState;
    protected BaseRecyclerViewAdapter _adapter;


    public ViewGroup getListLayout() {
        return _refreshLayout;
    }

    @Override
    protected View onCreateContentView() {
        _refreshLayout = new RefreshRecyclerView(this);
        mRecyclerView = _refreshLayout.getRecyclerView();
        return _refreshLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        loadData();

    }

    protected void replaceItem(int position, T item) {
        _adapter.replaceItem(position, item);
    }

    protected void setRefreshEnabeld(boolean enabled) {
        _refreshLayout.setEnabled(enabled);
    }

    private int preScrollState;

    @SuppressLint("ResourceAsColor")
    private void initView() {
        _refreshLayout.setOnPullLoadMoreListener(this);
        spacingInPixels = getSpacingInPixels();
        orientation = getOrientation();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.getItemAnimator().setChangeDuration(0);

        if (getSortType()) {
            if (!showDivider()) {
                mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            if (!showDivider()) {
                mRecyclerView.addItemDecoration(new GridSpaceItemDecoration(getSpanCount(), spacingInPixels, false));
            }
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, getSpanCount(), orientation, false));

        }

        _adapter = new ListViewAdapter(mContext, _dataSource);
        _refreshLayout.setAdapter(_adapter);
        //设置空页面
        _refreshLayout.setEmptyView(emptyView());

    }

    public void onPause() {
        super.onPause();
        if (_refreshLayout.isRefreshing()) {
            _refreshLayout.setRefreshing(false);
        }
    }

    protected int pageSize() {
        return kPageSize;
    }

    protected boolean isPaged() {
        return true;
    }

    //是否是网格
    protected boolean getSortType() {
        return true;
    }

    //方向
    protected int getSpanCount() {
        return spanCount;
    }

    //方向
    protected int getOrientation() {
        return orientation;
    }

    protected int getSpacingInPixels() {
        return getResources().getDimensionPixelSize(R.dimen.dimen_div_5);
    }

    private void updateData(final boolean showLoading) {
        if (showLoading) {
            this.showLoading();
        }


        loadListData();
    }


    private void reloadData(ArrayList<T> dataSource) {
        if (dataSource != null) {
            if (_RefreshState == RefreshState.LS_INIT) {
                _adapter.setDatas(dataSource);
            } else if (_RefreshState == RefreshState.LS_Refresh) {
                _dataSource = dataSource;
                _adapter.setDatas(_dataSource);
                //下拉刷新
                _refreshLayout.setPullLoadMoreCompleted();
                if (dataSource.size() < kPageSize) {
                    _refreshLayout.setIsLoadMoreEnabled(false);
                    _refreshLayout.setNoMore(true);
                } else {
                    _refreshLayout.setNoMore(false);
                }
            } else {
                //上拉加载更多
                _refreshLayout.setPullLoadMoreCompleted();
                if (dataSource.size() == 0) {
                    _refreshLayout.setNoMore(true);
                }
                _adapter.addItems(dataSource);
            }
            _dataSource = _adapter.getData();
        } else {
            _adapter.setDatas(new ArrayList());
        }

    }


    protected void loadData() {
        _RefreshState = RefreshState.LS_INIT;
        kPage = 1;
        this.updateData(true);
    }

    // region abstract methods
    public abstract void loadListData();

    protected boolean showDivider() {
        return false;
    }

    //自己复写这个方法
    public void loadDataFinish(ArrayList<T> arrayList) {
        reloadData(arrayList);
        hideLoading();
    }


    // endregion

    protected View emptyView() {
        View view = getEmptyView();
        if (view == null) {
            view = inflateContentView(R.layout.empty_view);
        }
        view.setVisibility(View.GONE);
        return view;
    }


    protected int getItemCount() {
        return pageSize();
    }


    protected class ListViewAdapter extends BaseRecyclerViewAdapter {

        public ListViewAdapter(Context context, ArrayList datas) {
            super(context, datas);
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return getViewHolder(viewType);
        }

        @Override
        public int getItemViewType(int position) {
            return getListItemViewType(position);
        }

        @Override
        public void setUpData(RecyclerView.ViewHolder holder, int position, int viewType, Object data) {
            BindViewHolder(holder, viewType, position, data);
        }

    }


    protected View getEmptyView() {
        return null;
    }

    protected int getCount() {
        return _dataSource.size();
    }

    protected T getItem(int index) {
        return _dataSource.get(index);
    }


    public int getListItemViewType(int position) {
        return 0;
    }

    public abstract RecyclerView.ViewHolder getViewHolder(int viewType);

    public abstract void BindViewHolder(RecyclerView.ViewHolder viewHolder, int viewType, int position, Object data);


    @Override
    public void onRefresh() {
        // 下拉刷新
        _RefreshState = RefreshState.LS_Refresh;
        kPage = 1;
        this.updateData(false);
    }

    @Override
    public void onLoadMore() {
        //        // 上拉加载更多
        _RefreshState = RefreshState.LS_LoadMore;
        kPage += 1;
        this.updateData(false);
    }
}
