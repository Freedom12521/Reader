package com.cheng.search;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cheng.parse.bean.Book;
import com.cheng.room.bean.History;
import com.cheng.search.databinding.SearchMainItemBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends ListAdapter<Book, SearchAdapter.ViewHolder> {

    private static final String TAG = "SearchAdapter";

    private OnSearchItemClickListener onSearchItemClickListener;

    public SearchAdapter() {
        super(new SearchDiffCallback());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(SearchMainItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = getItem(position);
        //Log.i(TAG, "onBindViewHolder: "+ book.toString());
        holder.bind(createListener(book), book);
        holder.itemView.setTag(book);
    }

    private View.OnClickListener createListener(Book book) {
        return onSearchItemClickListener == null ? null : onSearchItemClickListener.onClick(book);
    }

    public void setOnSearchItemClickListener(OnSearchItemClickListener onSearchItemClickListener) {
        this.onSearchItemClickListener = onSearchItemClickListener;
    }

    public interface OnSearchItemClickListener {
        View.OnClickListener onClick(Book book);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        SearchMainItemBinding searchMainItemBinding;

        ViewHolder(SearchMainItemBinding searchMainItemBinding) {
            super(searchMainItemBinding.getRoot());
            this.searchMainItemBinding = searchMainItemBinding;
        }

        void bind(View.OnClickListener listener, Book book) {
            searchMainItemBinding.setClickListener(listener);
            searchMainItemBinding.setBook(book);
            searchMainItemBinding.executePendingBindings();
        }
    }

    //对比条目
    private static class SearchDiffCallback extends DiffUtil.ItemCallback<Book> {
        @Override
        public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem.name.equals(newItem.name) && oldItem.author.equals(newItem.author);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem == newItem;
        }
    }
}
