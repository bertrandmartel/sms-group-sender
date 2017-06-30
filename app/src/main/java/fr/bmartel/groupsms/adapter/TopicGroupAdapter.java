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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.bmartel.groupsms.R;
import fr.bmartel.groupsms.common.SimpleDividerItemDecoration;
import fr.bmartel.groupsms.inter.IViewHolderCheckListener;
import fr.bmartel.groupsms.model.Contact;
import fr.bmartel.groupsms.model.Group;

/**
 * Topic subview group Adapter.
 *
 * @author Bertrand Martel
 */
public class TopicGroupAdapter extends RecyclerView.Adapter<TopicGroupAdapter.ViewHolder> {

    private List<Group> groupList = new ArrayList<>();

    /**
     * Android context
     */
    private Context context = null;

    public TopicGroupAdapter(List<Group> list, Context context) {
        this.groupList = list;
        this.context = context;
    }

    @Override
    public TopicGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_contact_item, parent, false);
        return new TopicGroupAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicGroupAdapter.ViewHolder holder, final int position) {
        Group item = groupList.get(position);
        holder.groupName.setText("Group : " + item.getName());

        holder.contactList = item.getContactList();

        holder.adapter = new ContactAdapter(holder.contactList, context, new IViewHolderCheckListener() {
            @Override
            public void onContactsChecked(Contact contact, boolean checked) {
                List<Contact> contactList = groupList.get(position).getContactList();

                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).getDisplayName().equals(contact.getDisplayName()) &&
                            contactList.get(i).getPhoneNumber().equals(contact.getPhoneNumber())) {
                        contactList.get(i).setChecked(checked);
                    }
                }

            }
        });

        holder.contactListView.setLayoutManager(new GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false));

        holder.contactListView.addItemDecoration(new SimpleDividerItemDecoration(
                context.getApplicationContext()
        ));

        holder.contactListView.setAdapter(holder.adapter);

        holder.contactListView.setNestedScrollingEnabled(false);
        holder.contactListView.setHasFixedSize(true);
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
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
         * topic name
         */
        public TextView groupName;

        public RecyclerView contactListView;

        public ContactAdapter adapter;

        public List<Contact> contactList;

        /**
         * ViewHolder for Contact item
         *
         * @param v
         */
        public ViewHolder(View v) {
            super(v);
            groupName = v.findViewById(R.id.group_name);
            layout = v.findViewById(R.id.group_layout);
            contactListView = v.findViewById(R.id.contact_list);
        }
    }
}
