package com.cbase.demo.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cbase.demo.R;
import com.cbase.sample.SimpleListFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * @author : zhouyx
 * @date : 2017/9/17
 * @description :
 */
public class ListFragment extends SimpleListFragment {

    private static final int DURATION = 2000;
    private static final int LOAD_SIZE = 30;

    private DataAdapter adapter;
    private List<String> mData = new ArrayList<>();

    public static ListFragment getInstance() {
        return new ListFragment();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        if (adapter == null) {
            adapter = new DataAdapter();
        }
        return adapter;
    }

    @Override
    protected void initFragment(@Nullable Bundle savedInstanceState, @Nullable Bundle inputBundle) {
        super.initFragment(savedInstanceState, inputBundle);
        mBGARefreshLayout.setRefreshViewHolder(getRefreshViewHolder(2));
    }

    private BGARefreshViewHolder getRefreshViewHolder(int type) {
        BGARefreshViewHolder holder;
        if (type == 1) {
            holder = new BGAStickinessRefreshViewHolder(mActivity, true);
            ((BGAStickinessRefreshViewHolder) holder).setRotateImage(R.drawable.bga_refresh_stickiness);
            ((BGAStickinessRefreshViewHolder) holder).setStickinessColor(R.color.colorAccent);
        } else {
            holder = new BGANormalRefreshViewHolder(mActivity, true);
        }
        return holder;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBGARefreshLayout.beginRefreshing();
    }

    @Override
    protected void onPullDown() {
        new MyAsyncTask(this, 1).execute();
    }

    @Override
    protected boolean onPullUp() {
        new MyAsyncTask(this, 2).execute();
        return true;
    }

    private static final class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<ListFragment> mReference;
        private int mType;

        MyAsyncTask(ListFragment fragment, int type) {
            this.mReference = new WeakReference<>(fragment);
            this.mType = type;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mReference == null || mReference.get() == null) {
                return;
            }
            ListFragment fragment = mReference.get();
            switch (mType) {
                case 1:
                    fragment.mBGARefreshLayout.endRefreshing();
                    break;
                case 2:
                    fragment.mBGARefreshLayout.endLoadingMore();
                    break;
                default:
                    break;
            }
            for (int i = 0; i < LOAD_SIZE; i++) {
                fragment.mData.add(i + "");
            }
            fragment.adapter.notifyDataSetChanged();
        }
    }

    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

        @NonNull
        @Override
        public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_function, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull DataAdapter.ViewHolder viewHolder, int i) {
            viewHolder.content.setText(mData.get(i));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.content)
            TextView content;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
