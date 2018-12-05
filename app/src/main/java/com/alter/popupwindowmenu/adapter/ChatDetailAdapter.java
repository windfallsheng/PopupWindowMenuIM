package com.alter.popupwindowmenu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alter.popupwindowmenu.R;
import com.alter.popupwindowmenu.command.C;
import com.alter.popupwindowmenu.model.ChatMessage;
import com.alter.popupwindowmenu.model.UserInfo;

import java.util.List;

/**
 * @CreateDate: 2018/1/26
 * @Author: lzsheng
 * @Description: 适配器，根据不同的数据类型，展示不同的UI效果
 * @Version:
 */
public class ChatDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ChatMessage> mChatMessages;
    private OnRecyclerViewItemLongClick mOnRecyclerViewItemLongClick;
    private OnRecyclerViewInnerItemLongClick mOnRecyclerViewInnerItemLongClick;
    private String likeUsers;

    private final int TYPE_MSG_SEND = C.TYPE_MSG_SEND;              //
    private final int TYPE_MSG_RECEIVE = C.TYPE_MSG_RECEIVE;          //

    public ChatDetailAdapter(Context context) {
        this.mContext = context;
        // 构造多个超链接的html, 通过选中的位置来获取用户名
        StringBuilder sbBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sbBuilder.append("username-" + i + "、");
        }
        likeUsers = sbBuilder.substring(0, sbBuilder.lastIndexOf("、")).toString();
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        mChatMessages = chatMessages;
    }

    public void setOnRecyclerViewItemLongClick(OnRecyclerViewItemLongClick onRecyclerViewItemLongClick) {
        mOnRecyclerViewItemLongClick = onRecyclerViewItemLongClick;
    }

    public void setOnRecyclerViewInnerItemLongClick(OnRecyclerViewInnerItemLongClick onRecyclerViewInnerItemLongClick) {
        mOnRecyclerViewInnerItemLongClick = onRecyclerViewInnerItemLongClick;
    }

    /**
     * @CreateDate: 2018/2/3
     * @Author: lzsheng
     * @Description: 根据数据的类型, 返回不同的ItemViewType
     * @Params: [position]
     * @Return: int
     */
    @Override
    public int getItemViewType(int position) {
        if (mChatMessages != null && mChatMessages.size() > 0) {
            int from = mChatMessages.get(position).getFrom();
            if (from == C.TYPE_MSG_SEND) {
                return TYPE_MSG_SEND;
            } else if (from == C.TYPE_MSG_RECEIVE) {
                return TYPE_MSG_RECEIVE;
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_MSG_SEND:
                HolderChatSend holderChatSend = new HolderChatSend(
                        LayoutInflater.from(mContext).inflate(R.layout.rv_item_chat_msg_send, parent, false));
                return holderChatSend;
            case TYPE_MSG_RECEIVE:
                HolderChatReceive holderChatReceive = new HolderChatReceive(
                        LayoutInflater.from(mContext).inflate(R.layout.rv_item_chat_msg_receive, parent, false));
                return holderChatReceive;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mChatMessages != null) {
            ChatMessage chatMessage = mChatMessages.get(position);
            int from = chatMessage.getFrom();
            switch (from) {
                case TYPE_MSG_SEND:
                    String msgContentSend = chatMessage.getMsgContent();
                    ((HolderChatSend) holder).tvMsgContent.setText(msgContentSend);
                    break;
                case TYPE_MSG_RECEIVE:
                    ReplyListAdapter replyListAdapter = new ReplyListAdapter(mContext);
                    UserInfo userInfo = chatMessage.getUserInfo();
                    ((HolderChatReceive) holder).tvNickName.setText(userInfo.getNickName());
                    String msgContentReceive = chatMessage.getMsgContent();
                    ((HolderChatReceive) holder).tvMsgContent.setText(msgContentReceive);
                    // 使用ClickableSpan的文本如果想真正实现点击作用，必须为TextView设置setMovementMethod方法，
                    // 否则没有点击相应，至于setHighlightColor方法则是控制点击是的背景色。
//                    ((HolderChatReceive) holder).tvPraise.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));
//                    ((HolderChatReceive) holder).tvPraise.setHighlightColor(mContext.getResources().getColor(R.color.colorPrimary));
                    ((HolderChatReceive) holder).tvPraise.setHighlightColor(Color.parseColor("#36969696"));
                    ((HolderChatReceive) holder).tvPraise.setText(addClickablePart(likeUsers), TextView.BufferType.SPANNABLE);
                    ((HolderChatReceive) holder).tvPraise.setMovementMethod(LinkMovementMethod.getInstance());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    ((HolderChatReceive) holder).rvReply.setLayoutManager(linearLayoutManager);
                    ((HolderChatReceive) holder).rvReply.setAdapter(replyListAdapter);
                    replyListAdapter.setOnRecyclerViewItemLongClick(new ReplyListAdapter.OnRecyclerViewItemLongClick() {
                        @Override
                        public void onItemLongClick(View childView, MotionEvent e, int position) {
                            if (mOnRecyclerViewInnerItemLongClick != null) {
                                mOnRecyclerViewInnerItemLongClick.onItemLongClick(childView, e, position);
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mChatMessages != null && mChatMessages.size() > 0) {
            return mChatMessages.size();
        }
        return 0;
    }

    class HolderChatSend extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView tvNickName;
        TextView tvMsgContent;
        LinearLayout layoutChat;
        MotionEvent event;

        public HolderChatSend(View itemView) {
            super(itemView);
            tvNickName = (TextView) itemView.findViewById(R.id.textview_nick_name);
            tvMsgContent = (TextView) itemView.findViewById(R.id.textview_message);
            layoutChat = (LinearLayout) itemView.findViewById(R.id.layout_message);
            layoutChat.setOnTouchListener(new View.OnTouchListener() {
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
            layoutChat.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnRecyclerViewItemLongClick != null) {
                mOnRecyclerViewItemLongClick.onItemLongClick(v, event, getAdapterPosition());
            }
            return false;
        }
    }

    class HolderChatReceive extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView tvNickName;
        TextView tvMsgContent;
        TextView tvPraise;
        LinearLayout layoutChat;
        RecyclerView rvReply;
        MotionEvent event;

        public HolderChatReceive(View itemView) {
            super(itemView);
            tvNickName = (TextView) itemView.findViewById(R.id.textview_nick_name);
            tvMsgContent = (TextView) itemView.findViewById(R.id.textview_message);
            tvPraise = (TextView) itemView.findViewById(R.id.textview_praise);
            layoutChat = (LinearLayout) itemView.findViewById(R.id.layout_message);
            rvReply = (RecyclerView) itemView.findViewById(R.id.recyclerview_reply);
            layoutChat.setOnTouchListener(new View.OnTouchListener() {
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
            layoutChat.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnRecyclerViewItemLongClick != null) {
                mOnRecyclerViewItemLongClick.onItemLongClick(v, event, getAdapterPosition());
            }
            return false;
        }
    }

    /**
     * item点击接口
     */
    public interface OnRecyclerViewItemLongClick {
        void onItemLongClick(View childView, MotionEvent event, int position);
    }

    /**
     * item点击接口
     */
    public interface OnRecyclerViewInnerItemLongClick {
        void onItemLongClick(View childView, MotionEvent event, int position);
    }

    /**
     * @param str
     * @return
     */
    private SpannableStringBuilder addClickablePart(String str) {
        // 第一个赞图标
        ImageSpan span = new ImageSpan(mContext, R.drawable.praise);
        SpannableString spanStr = new SpannableString("p.");
        spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(str);

        String[] likeUsers = str.split("、");

        if (likeUsers.length > 0) {
            // 最后一个
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = str.indexOf(name) + spanStr.length();
                ssb.setSpan(new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(mContext, name,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        // ds.setColor(Color.RED); // 设置文本颜色
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }

                }, start, start + name.length(), 0);
            }
        }
        return ssb.append("等"
                + likeUsers.length + "个人赞了您.");
    } // end of addClickablePart

}
