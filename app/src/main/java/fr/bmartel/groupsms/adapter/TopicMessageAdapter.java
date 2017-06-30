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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.bmartel.groupsms.R;
import fr.bmartel.groupsms.model.Message;

/**
 * Topic subview message Adapter.
 *
 * @author Bertrand Martel
 */
public class TopicMessageAdapter extends RecyclerView.Adapter<TopicMessageAdapter.ViewHolder> {

    private List<Message> messageList = new ArrayList<>();

    public TopicMessageAdapter(List<Message> list) {
        this.messageList = list;
    }

    @Override
    public TopicMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_message_item, parent, false);
        return new TopicMessageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicMessageAdapter.ViewHolder holder, final int position) {
        Message item = messageList.get(position);
        holder.messageTitle.setText("Title : " + item.getTitle());
        if (item.getBody().length() < 30) {
            holder.messageBody.setText(item.getBody().substring(0, item.getBody().length()));
        } else {
            holder.messageBody.setText(item.getBody().substring(0, 30));
        }
        holder.checkBox.setChecked(item.isChecked());
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    /**
     * ViewHolder for Contact item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * contact layout
         */
        public LinearLayout layout;

        /**
         * message title
         */
        public TextView messageTitle;

        public TextView messageBody;

        public CheckBox checkBox;

        /**
         * ViewHolder for Contact item
         *
         * @param v
         */
        public ViewHolder(View v) {
            super(v);
            messageTitle = v.findViewById(R.id.message_title);
            messageBody = v.findViewById(R.id.message_body);
            checkBox = v.findViewById(R.id.contact_check);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    messageList.get(getPosition()).setChecked(isChecked);
                }
            });
            layout = v.findViewById(R.id.group_layout);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBox.toggle();
                }
            });
        }
    }

}
