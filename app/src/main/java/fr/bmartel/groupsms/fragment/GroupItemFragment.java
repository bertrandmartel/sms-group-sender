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
import fr.bmartel.groupsms.inter.IFragment;
import fr.bmartel.groupsms.model.Group;

/**
 * Group Item Fragment.
 *
 * @author Bertrand Martel
 */
public class GroupItemFragment extends MainFragmentAbstr implements IFragment {

    private ContactFragment mFragment;

    private EditText mGroupNameEt;

    private int mGroupIndex = -1;

    public GroupItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.group_item_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mGroupIndex = (args != null) ? args.getInt("index", -1) : -1;

        mGroupNameEt = view.findViewById(R.id.group_name);
        mGroupNameEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        mFragment = new ContactFragment();

        if (mGroupIndex != -1) {
            getRootActivity().setToolbarTitle(getString(R.string.title_edit_group));
            Group group = getRootActivity().getGroupList().get(mGroupIndex);
            mGroupNameEt.setText(group.getName());
            Bundle ContactArgs = new Bundle();
            ContactArgs.putInt("index", mGroupIndex);
            mFragment.setArguments(ContactArgs);
        } else {
            getRootActivity().setToolbarTitle(getString(R.string.title_create_group));
        }
        getChildFragmentManager().beginTransaction().replace(R.id.contact_frame, mFragment).commit();

        Toolbar toolbar = getRootActivity().getToolbar();
        getRootActivity().hideMenuButton();
        MenuItem saveButton = toolbar.getMenu().findItem(R.id.button_save);
        saveButton.setVisible(true);

        saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                final String name = mGroupNameEt.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "group name can't be empty", Toast.LENGTH_SHORT).show();
                } else if (mGroupIndex == -1 && getRootActivity().checkDuplicateGroup(name)) {
                    Toast.makeText(getActivity(), "group " + name + " already exist", Toast.LENGTH_SHORT).show();
                } else if (mGroupIndex == -1) {
                    getRootActivity().getGroupList().add(new Group(name, mFragment.getSelectedContact()));
                    getRootActivity().saveGroup();
                    Toast.makeText(getActivity(), "group " + name + " has been saved", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    getRootActivity().getGroupList().get(mGroupIndex).setName(name);
                    getRootActivity().getGroupList().get(mGroupIndex).setContacts(mFragment.getSelectedContact());
                    getRootActivity().saveGroup();
                    Toast.makeText(getActivity(), "group " + name + " has been saved", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (mFragment != null) {
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}