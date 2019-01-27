package com.cbase.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cbase.demo.base.AppActivity;
import com.cbase.demo.model.ItemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author : zhouyx
 * @date : 2019/1/18
 * @description :
 */
public class MainActivity extends AppActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<ItemBean> mData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initActivity(Bundle savedInstanceState, Bundle inputBundle) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        FunctionAdapter adapter = new FunctionAdapter();
        recyclerView.setAdapter(adapter);
        mData.addAll(ItemBean.getList());
        adapter.notifyDataSetChanged();
    }

    public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_function, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.content.setText(mData.get(position).getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FunctionActivity.open(MainActivity.this, mData.get(holder.getLayoutPosition()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        final class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.content)
            TextView content;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
