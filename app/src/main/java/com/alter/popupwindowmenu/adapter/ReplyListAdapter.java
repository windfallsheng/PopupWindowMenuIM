package com.alter.popupwindowmenu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alter.popupwindowmenu.R;

import java.util.List;

/**
 * @CreateDate: 2018/2/27
 * @Author: luzhaosheng
 * @Description: 展示未提醒的消息列表的适配器
 * @Version:
 */
public class ReplyListAdapter extends RecyclerView.Adapter<ReplyListAdapter.ViewHolderReply> {

    private Context mContext;
    private List<String> mReplies;
    private OnRecyclerViewItemLongClick mOnRecyclerViewItemLongClick;

    public ReplyListAdapter(Context context) {
        mContext = context;
    }

    public void setOnRecyclerViewItemLongClick(OnRecyclerViewItemLongClick onRecyclerViewItemLongClick) {
        mOnRecyclerViewItemLongClick = onRecyclerViewItemLongClick;
    }

    @Override
    public ReplyListAdapter.ViewHolderReply onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderReply viewHolderRminder = new ViewHolderReply(
                LayoutInflater.from(mContext).inflate(R.layout.item_list_reply, parent, false));
        return viewHolderRminder;
    }

    @Override
    public void onBindViewHolder(final ReplyListAdapter.ViewHolderReply holder, final int position) {
        if (mReplies != null && mReplies.size() > 0) {
            String reply = mReplies.get(position);
        }
    }

    @Override
    public int getItemCount() {
//        if (mReplies != null && mReplies.size() > 0) {
//            return mReplies.size();
//        }
        return 5;
    }

    class ViewHolderReply extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView tvReply;
        MotionEvent event;

        public ViewHolderReply(View itemView) {
            super(itemView);

            tvReply = (TextView) itemView.findViewById(R.id.textview_reply);

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent e) {
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            event = e;
                            break;
                        default:
                            break;
                    }
                    // 如果onTouch返回false,首先是onTouch事件的down事件发生，此时，如果长按，触发onLongClick事件；
                    // 然后是onTouch事件的up事件发生，up完毕，最后触发onClick事件。
                    return false;
                }
            });
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if (mOnRecyclerViewItemLongClick != null) {
//                Log.d("tag_hipadim", "ReplyListAdapter->onItemLongClick");
                mOnRecyclerViewItemLongClick.onItemLongClick(view, event, getAdapterPosition());
            }
            // true不再触发短按OnClick事件,false会之后触发短按OnClick事件
            return true;
        }

    }

    /**
     * item长按接口
     */
    public interface OnRecyclerViewItemLongClick {
        void onItemLongClick(View childView, MotionEvent e, int position);
    }

}