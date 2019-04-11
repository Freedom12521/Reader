package com.cheng.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cheng.main.databinding.MainItemBinding;
import com.cheng.room.bean.Book;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends ListAdapter<Book, MainAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    protected MainAdapter() {
        super(new MainDiffCallback());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //绑定item
        return new ViewHolder(MainItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //赋值和缓存
        Book book = getItem(position);
        holder.bind(createOnClickListener(book.getId())
                , createOnLongClickListener(book.getId())
                , book);
        holder.itemView.setTag(book);
    }

    private View.OnLongClickListener createOnLongClickListener(int id) {
        return mOnItemClickListener != null ? mOnItemClickListener.onLongClick(id) : null;
    }

    private View.OnClickListener createOnClickListener(int id) {
        return mOnItemClickListener != null ? mOnItemClickListener.onClick(id) : null;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    //把点击事件和长按事件，交给activity处理
    public interface OnItemClickListener {
        View.OnClickListener onClick(int id);

        View.OnLongClickListener onLongClick(int id);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private MainItemBinding mainItemBinding;

        public ViewHolder(MainItemBinding binding) {
            super(binding.getRoot());
            this.mainItemBinding = binding;
        }

        //更新dataBinding的数据
        public void bind(View.OnClickListener listener, View.OnLongClickListener longClickListener, Book book) {
            mainItemBinding.setBook(book);
            mainItemBinding.setClickListener(listener);
            mainItemBinding.setLongClickListener(longClickListener);
            mainItemBinding.executePendingBindings();
        }
    }

    //对比条目
    private static class MainDiffCallback extends DiffUtil.ItemCallback<Book> {
        @Override
        public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem == newItem;
        }
    }

}
