package com.cheng.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cheng.room.bean.History;
import com.cheng.search.databinding.SearchMainHistoyItemBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends ListAdapter<History, HistoryAdapter.ViewHolder> {


    private OnHistoryItemClickListener onHistoryItemClickListener;


    public HistoryAdapter() {
        super(new HistoryDiffCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(SearchMainHistoyItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = getItem(position);
        holder.bind(createHistoryClickListener(history.getHistory()), history);
        holder.itemView.setTag(history);
    }

    private View.OnClickListener createHistoryClickListener(String history) {
        return onHistoryItemClickListener == null ? null : onHistoryItemClickListener.onHistoryItemClick(history);
    }

    public void setOnHistoryItemClickListener(OnHistoryItemClickListener listener) {
        this.onHistoryItemClickListener = listener;
    }

    public interface OnHistoryItemClickListener {
        View.OnClickListener onHistoryItemClick(String history);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SearchMainHistoyItemBinding searchMainItemBinding;

        ViewHolder(SearchMainHistoyItemBinding searchMainItemBinding) {
            super(searchMainItemBinding.getRoot());
            this.searchMainItemBinding = searchMainItemBinding;
        }

        void bind(View.OnClickListener listener, History history) {
            searchMainItemBinding.setHistory(history);
            searchMainItemBinding.setOnClickListener(listener);
        }
    }


    //对比条目
    private static class HistoryDiffCallback extends DiffUtil.ItemCallback<History> {
        @Override
        public boolean areItemsTheSame(@NonNull History oldItem, @NonNull History newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull History oldItem, @NonNull History newItem) {
            return oldItem == newItem;
        }
    }
}
