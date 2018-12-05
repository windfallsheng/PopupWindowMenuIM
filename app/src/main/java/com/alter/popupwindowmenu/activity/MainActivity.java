package com.alter.popupwindowmenu.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alter.popupwindowmenu.R;
import com.alter.popupwindowmenu.adapter.ChatDetailAdapter;
import com.alter.popupwindowmenu.command.C;
import com.alter.popupwindowmenu.listener.OnRecyclerViewItemClick;
import com.alter.popupwindowmenu.model.ChatMessage;
import com.alter.popupwindowmenu.model.OptionEntity;
import com.alter.popupwindowmenu.model.UserInfo;
import com.alter.popupwindowmenu.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @CreateDate: 2018/3/9
 * @Author: lzsheng
 * @Description:
 * @Version:
 */
public class MainActivity extends Activity {

    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ChatDetailAdapter mChatDetailAdapter;
    private List<ChatMessage> mChatMessages;
    private PopupWindow mPopupWindow;
    private View mPopContentView;
    private int mPressedPos; // 被点击的位置
    private float mRawX;
    private float mRawY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariable();
        initViews();
        initData();
        setViewsData();
    }

    private void initVariable() {
        mChatMessages = new ArrayList<>();
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_messages);
        mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    private void initData() {
        for (int i = 'A'; i <= 'Z'; i++) {
            Log.d(TAG, "i = " + i + "\n===========分割线==============");
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setFrom(new Random().nextInt(2) + 1);
//            chatMessage.setFrom((int) (Math.random() * 2) + 1);
            if (chatMessage.getFrom() == C.TYPE_MSG_RECEIVE) {
                UserInfo userInfo = new UserInfo();
                userInfo.setNickName("Mia");
                chatMessage.setUserInfo(userInfo);
            }
//            int s = (int) (Math.random() * ('Z' - 'A' + 1)) + 'A';
//            int e = (int) (Math.random() * ('Z' - 'A' + 1)) + 'A';
            int s = (int) (Math.random() * 120) + 1;
            int e = (int) (Math.random() * 120) + 1;
            Log.d(TAG, "s = " + s);
            Log.d(TAG, "e = " + e);
            int min = 0;
            int max = 0;
            if (s <= e) {
                min = s;
                max = e;
            } else {
                min = e;
                max = s;
            }
            StringBuffer buffer = new StringBuffer();
            for (int j = min; j <= max; j++) {
                int c = (int) (Math.random() * ('Z' - 'A' + 1)) + 'A';
                Log.d(MainActivity.class.getSimpleName(), "c = " + c);
//                buffer.append(String.valueOf((char) (Math.random() * max) + min));
                buffer.append(String.valueOf((char) c));
            }
            chatMessage.setMsgContent(buffer.toString());
            buffer.setLength(0);
            // 或者：
            // buffer.delete(0, buffer.length());
            mChatMessages.add(chatMessage);
        }
    }

    private void setViewsData() {
        mChatDetailAdapter = new ChatDetailAdapter(MainActivity.this);
        mChatDetailAdapter.setChatMessages(mChatMessages);
        mRecyclerView.setAdapter(mChatDetailAdapter);
//        mRecyclerView.smoothScrollToPosition(mChatDetailAdapter.getItemCount());
//        mLinearLayoutManager.scrollToPositionWithOffset(mChatDetailAdapter.getItemCount() + 1, 0);
        mRecyclerView.scrollToPosition(mChatDetailAdapter.getItemCount() - 1);
        mChatDetailAdapter.setOnRecyclerViewItemLongClick(new ChatDetailAdapter.OnRecyclerViewItemLongClick() {
            @Override
            public void onItemLongClick(View childView, MotionEvent event, int position) {
                mRawX = event.getRawX();
                mRawY = event.getRawY();
                mPressedPos = position;
                Log.d(TAG, "e.getRawX()横坐标=" + mRawX + ", e.getRawY()纵坐标=" + mRawY);
                Log.d(TAG, "position=" + position);
                UserInfo userInfo = mChatMessages.get(position).getUserInfo();
                ChatMessage chatMessage = mChatMessages.get(position);
                int typeMsg = chatMessage.getFrom();
                String msgContent = chatMessage.getMsgContent();
                StringBuffer buffer = new StringBuffer();
                if (typeMsg == C.TYPE_MSG_RECEIVE) {
                    buffer.append("收到").append(userInfo.getNickName()).append("发送过来的消息：\n").append(msgContent);
                } else if (typeMsg == C.TYPE_MSG_SEND) {
                    buffer.append("我发出的消息：\n").append(msgContent);
                }
                Toast.makeText(MainActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
                initPopWindow(childView, position);
            }
        });

        mChatDetailAdapter.setOnRecyclerViewInnerItemLongClick(new ChatDetailAdapter.OnRecyclerViewInnerItemLongClick() {
            @Override
            public void onItemLongClick(View childView, MotionEvent event, int position) {
                mRawX = event.getRawX();
                mRawY = event.getRawY();
                mPressedPos = position;
                Log.d(TAG, "e.getRawX()横坐标=" + mRawX + ", e.getRawY()纵坐标=" + mRawY);
                Log.d(TAG, "position=" + position);
                UserInfo userInfo = mChatMessages.get(position).getUserInfo();
                ChatMessage chatMessage = mChatMessages.get(position);
                int typeMsg = chatMessage.getFrom();
                String msgContent = chatMessage.getMsgContent();
                StringBuffer buffer = new StringBuffer();
                if (typeMsg == C.TYPE_MSG_RECEIVE) {
                    buffer.append("收到").append(userInfo.getNickName()).append("发送过来的消息：\n").append(msgContent);
                } else if (typeMsg == C.TYPE_MSG_SEND) {
                    buffer.append("我发出的消息：\n").append(msgContent);
                }
//                Toast.makeText(MainActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "inner_position==" + position, Toast.LENGTH_SHORT).show();
                initPopWindow(childView, position);
            }
        });
    }

    private void initPopWindow(final View selectedView, final int position) {
        List<OptionEntity> optionEntities = new ArrayList<>();
        optionEntities.add(new OptionEntity(0, null, "复制"));
        optionEntities.add(new OptionEntity(0, null, "发送给朋友"));
        optionEntities.add(new OptionEntity(0, null, "收藏"));
        optionEntities.add(new OptionEntity(0, null, "提醒"));
        optionEntities.add(new OptionEntity(0, null, "翻译"));
        optionEntities.add(new OptionEntity(0, null, "删除"));
        optionEntities.add(new OptionEntity(0, null, "更多"));
        if (mPopContentView == null) {
            mPopContentView = View.inflate(this, R.layout.item_list_option_pop, null);
        }
        RecyclerView rvOptions = (RecyclerView) mPopContentView.findViewById(R.id.recyclerview_options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvOptions.setLayoutManager(linearLayoutManager);
        OptionsAdapter optionsAdapter = new OptionsAdapter();
        optionsAdapter.setOptionEntities(optionEntities);
        rvOptions.setAdapter(optionsAdapter);
//        LinearLayout layoutDelete = (LinearLayout) mPopContentView.findViewById(R.id.layout_delete);
        // 在popupWindow还没有弹出显示之前就测量获取其宽高（单位是px像素）
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mPopContentView.measure(w, h);
        int viewWidth = mPopContentView.getMeasuredWidth();//获取测量宽度px
        int viewHeight = mPopContentView.getMeasuredHeight();//获取测量高度px
        final int screenWidth = DensityUtil.getScreenWidth(this.getWindow().getDecorView().getContext());
        final int screenHeight = DensityUtil.getScreenHeight(this.getWindow().getDecorView().getContext());
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mPopContentView, viewWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setBackgroundDrawable(drawable);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        int offX = 20; // 可以自己调整偏移
        int offY = 20; // 可以自己调整偏移
        float rawX = mRawX;
        float rawY = mRawY;
        if (mRawX <= screenWidth / 2) {
            rawX = mRawX + offX;
            if (mRawY < screenHeight / 3) {
                rawY = mRawY;
                mPopupWindow.setAnimationStyle(R.style.pop_anim_left_top); //设置动画
            } else {
                rawY = mRawY - viewHeight - offY;
                mPopupWindow.setAnimationStyle(R.style.pop_anim_left_bottom); //设置动画
            }
        } else {
            rawX = mRawX - viewWidth - offX;
            if (mRawY < screenHeight / 3) {
                rawY = mRawY;
                mPopupWindow.setAnimationStyle(R.style.pop_anim_right_top); //设置动画
            } else {
                rawY = mRawY - viewHeight;
                mPopupWindow.setAnimationStyle(R.style.pop_anim_right_bottom); //设置动画
            }
        }
        mPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.NO_GRAVITY, (int) rawX, (int) rawY);
        /*layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                if (mChatMessages.size() <= 0) {
                    return;
                } else {
                    mChatMessages.remove(position);
                    mChatDetailAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "已删除此条聊天内容", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                selectedView.setSelected(false);
            }
        });
    }

    class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

        List<OptionEntity> optionEntities;
        OnRecyclerViewItemClick mOnRecyclerViewItemClick;

        public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
            mOnRecyclerViewItemClick = onRecyclerViewItemClick;
        }

        public void setOptionEntities(List<OptionEntity> optionEntities) {
            this.optionEntities = optionEntities;
        }

        @Override
        public OptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list_option, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            OptionEntity optionEntity = optionEntities.get(position);
            Drawable drawable = optionEntity.getDrawable();
            if (drawable != null) {
                holder.ivOptionIcon.setVisibility(View.VISIBLE);
                holder.ivOptionIcon.setImageDrawable(optionEntity.getDrawable());
            } else {
                holder.ivOptionIcon.setVisibility(View.GONE);
            }
            if (optionEntities != null && optionEntities.size() > 0) {
                int size = optionEntities.size();
                if (size > 1) {
                    if (position == 0) {
                        holder.itemView.setBackground(addStateDrawable(MainActivity.this, R.drawable.shape_top_corners_bg_normal,
                                R.drawable.shape_top_corners_bg_press, R.drawable.shape_top_corners_bg_press));
                    } else if (position == size - 1) {
                        holder.itemView.setBackground(addStateDrawable(MainActivity.this, R.drawable.shape_bottom_corners_bg_normal,
                                R.drawable.shape_bottom_corners_bg_press, R.drawable.shape_bottom_corners_bg_press));
                    } else {
                        holder.itemView.setBackground(addStateDrawable(MainActivity.this, R.drawable.shape_bg_normal,
                                R.drawable.shape_bg_press, R.drawable.shape_bg_press));
                    }
                } else {
                    holder.itemView.setBackground(addStateDrawable(MainActivity.this, R.drawable.shape_white_small_corners_bg,
                            R.drawable.shape_gray_small_corners_bg, R.drawable.shape_gray_small_corners_bg));
                }
                if (position == size - 1) {
                    holder.viewDividerLine.setVisibility(View.GONE);
                } else {
                    holder.viewDividerLine.setVisibility(View.VISIBLE);
                }
            }
            holder.tvOptionName.setText(optionEntity.getOptionName());
        }

        @Override
        public int getItemCount() {
            if (optionEntities != null && optionEntities.size() > 0) {
                return optionEntities.size();
            }
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            View itemView;
            ImageView ivOptionIcon;
            TextView tvOptionName;
            View viewDividerLine;

            public ViewHolder(View itemView) {
                super(itemView);
                ivOptionIcon = (ImageView) itemView.findViewById(R.id.imageview_option_icon);
                tvOptionName = (TextView) itemView.findViewById(R.id.textview_option_name);
                viewDividerLine = itemView.findViewById(R.id.view_divider_line);
                this.itemView = itemView;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mOnRecyclerViewItemClick != null) {
                    mOnRecyclerViewItemClick.onItemClick(view, getAdapterPosition());
                }
            }
        }
    }

    private StateListDrawable addStateDrawable(Context context, int idNormal, int idPressed, int idFocused) {
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable focus = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
        //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        sd.addState(new int[]{android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }
}
