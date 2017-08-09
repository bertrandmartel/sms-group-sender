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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import fr.bmartel.groupsms.R;
import fr.bmartel.groupsms.inter.IViewHolderCheckListener;
import fr.bmartel.groupsms.model.Contact;

/**
 * Contact adapter.
 *
 * @author Bertrand Martel
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    List<Contact> contactList = new ArrayList<>();

    /**
     * Android context
     */
    private Context context = null;

    /**
     * click listener
     */
    private IViewHolderCheckListener mListener;

    public ContactAdapter(List<Contact> list, Context context, IViewHolderCheckListener listener) {
        this.contactList = list;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact item = contactList.get(position);
        if (item.getPhoneNumber().size() == 1) {
            holder.phoneNumberTv.setVisibility(View.VISIBLE);
            holder.phoneNumberSpinner.setVisibility(View.GONE);
            holder.phoneNumberTv.setText(item.getPhoneNumber().get(0));
        } else {
            holder.phoneNumberTv.setVisibility(View.GONE);
            holder.phoneNumberSpinner.setVisibility(View.VISIBLE);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
                    R.layout.contact_phonenum_spinner, item.getPhoneNumber());
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.phoneNumberSpinner.setAdapter(dataAdapter);
            for (int i = 0; i < item.getPhoneNumber().size(); i++) {
                if (item.getPhoneNumber().get(i).equals(item.getSelectedPhoneNumber())) {
                    holder.phoneNumberSpinner.setSelection(i);
                    break;
                }
            }
        }
        //holder.phoneNumberSpinner.setText("" + item.getPhoneNumber().replaceAll("\\s+", "").replaceAll("\\+[0-9]{2}", "0").replaceAll("-", ""));
        holder.displayName.setText("" + item.getDisplayName());
        holder.checkBox.setChecked(item.isChecked());
    }

    public void setContactList(List<Contact> list) {
        contactList = list;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
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
         * contact name
         */
        public TextView displayName;

        /**
         * phone number
         */
        public Spinner phoneNumberSpinner;

        /**
         * phone number textview
         */
        public TextView phoneNumberTv;

        public CheckBox checkBox;

        /**
         * click listener
         */
        public IViewHolderCheckListener mListener;

        /**
         * ViewHolder for Contact item
         *
         * @param v
         * @param listener
         */
        public ViewHolder(View v, final IViewHolderCheckListener listener) {
            super(v);
            mListener = listener;
            displayName = v.findViewById(R.id.contact_display_name);
            phoneNumberSpinner = v.findViewById(R.id.contact_phone_number);
            phoneNumberTv = v.findViewById(R.id.contact_phone_number_tv);
            checkBox = v.findViewById(R.id.contact_check);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    contactList.get(getPosition()).setChecked(isChecked);
                    listener.onContactsChecked(contactList.get(getPosition()), isChecked);
                }
            });
            phoneNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    contactList.get(getPosition()).setSelectedPhoneNumber(adapterView.getItemAtPosition(i).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            layout = v.findViewById(R.id.contact_layout);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBox.toggle();
                }
            });
        }
    }

}
