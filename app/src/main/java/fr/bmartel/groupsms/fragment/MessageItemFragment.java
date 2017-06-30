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
package fr.bmartel.groupsms.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import fr.bmartel.groupsms.R;
import fr.bmartel.groupsms.model.Message;

/**
 * Message item Fragment.
 *
 * @author Bertrand Martel
 */
public class MessageItemFragment extends MainFragmentAbstr {

    private EditText mMessageTitleEt;

    private EditText mMessageTopicEt;

    private EditText mMessageBody;

    private int mMessageIndex = -1;

    public MessageItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.message_item_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mMessageIndex = (args != null) ? args.getInt("index", -1) : -1;

        mMessageTitleEt = view.findViewById(R.id.message_title);
        mMessageTitleEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        mMessageBody = view.findViewById(R.id.message_body);
        mMessageTopicEt = view.findViewById(R.id.message_topic);
        mMessageTopicEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        if (mMessageIndex != -1) {
            getRootActivity().setToolbarTitle(getString(R.string.title_edit_message));
            Message message = getRootActivity().getMessageList().get(mMessageIndex);
            mMessageTitleEt.setText(message.getTitle());
            mMessageTopicEt.setText(message.getTopic());
            mMessageBody.setText(message.getBody());
        } else {
            getRootActivity().setToolbarTitle(getString(R.string.title_create_message));
        }

        getRootActivity().hideMenuButton();
        Toolbar toolbar = getRootActivity().getToolbar();
        MenuItem saveButton = toolbar.getMenu().findItem(R.id.button_save);
        MenuItem deleteButton = toolbar.getMenu().findItem(R.id.button_delete);
        saveButton.setVisible(true);
        deleteButton.setVisible(false);

        saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                final String title = mMessageTitleEt.getText().toString().trim();
                final String topic = mMessageTopicEt.getText().toString().trim();
                final String body = mMessageBody.getText().toString().trim();

                if (title.isEmpty()) {
                    Toast.makeText(getActivity(), "message title can't be empty", Toast.LENGTH_SHORT).show();
                } else if (topic.isEmpty()) {
                    Toast.makeText(getActivity(), "message topic can't be empty", Toast.LENGTH_SHORT).show();
                } else if (body.isEmpty()) {
                    Toast.makeText(getActivity(), "message body can't be empty", Toast.LENGTH_SHORT).show();
                } else if (mMessageIndex == -1 && getRootActivity().checkDuplicateMessage(title)) {
                    Toast.makeText(getActivity(), "message title " + title + " already exist", Toast.LENGTH_SHORT).show();
                } else if (mMessageIndex == -1) {
                    getRootActivity().getMessageList().add(new Message(title, topic, body));
                    getRootActivity().saveMessages();
                    Toast.makeText(getActivity(), "message " + title + " has been saved", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    getRootActivity().getMessageList().get(mMessageIndex).setTitle(title);
                    getRootActivity().getMessageList().get(mMessageIndex).setTopic(topic);
                    getRootActivity().getMessageList().get(mMessageIndex).setBody(body);
                    getRootActivity().saveMessages();
                    Toast.makeText(getActivity(), "message " + title + " has been saved", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                return false;
            }
        });
    }
}