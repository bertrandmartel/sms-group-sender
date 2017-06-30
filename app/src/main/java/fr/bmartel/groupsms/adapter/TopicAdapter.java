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

/**
 * Topic Adapter
 *
 * @author Bertrand Martel
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    List<String> topicList = new ArrayList<>();

    /**
     * Android context
     */
    private Context context = null;

    /**
     * click listener
     */
    private IViewHolderClickListener mListener;

    private IBaseActivity mActivity;

    public TopicAdapter(List<String> list, Context context, IBaseActivity activity, IViewHolderClickListener listener) {
        this.topicList = list;
        this.context = context;
        this.mListener = listener;
        this.mActivity = activity;
    }

    @Override
    public TopicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String item = topicList.get(position);
        holder.topicName.setText(item + " (" +
                mActivity.getMessagesForTopic(item).size() +
                new String(Character.toChars(0x2709))
                + ")");
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return topicList.size();
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
         * topic name
         */
        public TextView topicName;

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
            topicName = (TextView) v.findViewById(R.id.topic_name);
            layout = (LinearLayout) v.findViewById(R.id.topic_layout);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }

}
