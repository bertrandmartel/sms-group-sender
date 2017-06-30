/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2017 Bertrand Martel
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.bmartel.groupsms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.bmartel.groupsms.R;
import fr.bmartel.groupsms.inter.IBaseActivity;
import fr.bmartel.groupsms.inter.IViewHolderClickListener;
import fr.bmartel.groupsms.model.Message;

/**
 * Message Adapter
 *
 * @author Bertrand Martel
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<Message> messageList = new ArrayList<>();

    /**
     * Android context
     */
    private Context context = null;

    /**
     * click listener
     */
    private IViewHolderClickListener mListener;

    private int selected_position = -1;

    private IBaseActivity mActivity;

    public MessageAdapter(IBaseActivity activity, List<Message> list, Context context, IViewHolderClickListener listener) {
        this.messageList = list;
        this.context = context;
        this.mActivity = activity;
        this.mListener = listener;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Message item = messageList.get(position);
        holder.messageTitle.setText("" + item.getTitle());
        holder.messageTopic.setText("" + item.getTopic());

        if (selected_position == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#e1e1e1"));
            mActivity.getToolbar().getMenu().findItem(R.id.button_delete).setVisible(true);
        } else {
            mActivity.getToolbar().getMenu().findItem(R.id.button_delete).setVisible(false);
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                notifyItemChanged(selected_position);
                selected_position = position;
                notifyItemChanged(selected_position);
                return true;
            }
        });
    }

    public boolean isSelected(int position) {
        return (selected_position == position);
    }

    public void unselect() {
        selected_position = -1;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public int getSelectedItem() {
        return selected_position;
    }

    /**
     * ViewHolder for Contact item
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * contact layout
         */
        public LinearLayout layout;

        /**
         * message title
         */
        public TextView messageTitle;

        /**
         * topic name
         */
        public TextView messageTopic;

        /**
         * click listener
         */
        public IViewHolderClickListener mListener;

        /**
         * ViewHolder for Contact item
         *
         * @param v
         * @param listener
         */
        public ViewHolder(View v, IViewHolderClickListener listener) {
            super(v);
            mListener = listener;
            messageTitle = (TextView) v.findViewById(R.id.message_title);
            messageTopic = (TextView) v.findViewById(R.id.message_topic);
            layout = (LinearLayout) v.findViewById(R.id.group_layout);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }

}
