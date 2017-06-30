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
package fr.bmartel.groupsms.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import fr.bmartel.groupsms.R;
import fr.bmartel.groupsms.fragment.GroupFragment;
import fr.bmartel.groupsms.fragment.MessageFragment;
import fr.bmartel.groupsms.fragment.OutboxFragment;
import fr.bmartel.groupsms.fragment.TopicFragment;

/**
 * The only activity in this app.
 *
 * @author Bertrand Martel
 */
public class MainActivity extends BaseActivity {

    /**
     * one dialog to show above the activity. We dont want to have multiple Dialog above each other.
     */
    private Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setLayout(R.layout.activity_main);

        super.onCreate(savedInstanceState);

        mFragment = new GroupFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, mFragment).commit();

        mBottombar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fm.beginTransaction().remove(mFragment).commit();
                switch (tabId) {
                    case R.id.tab_group:
                        mFragment = new GroupFragment();
                        fm.beginTransaction().replace(R.id.fragment_frame, mFragment).commit();
                        break;
                    case R.id.tab_message:
                        mFragment = new MessageFragment();
                        fm.beginTransaction().replace(R.id.fragment_frame, mFragment).commit();
                        break;
                    case R.id.tab_send:
                        mFragment = new TopicFragment();
                        fm.beginTransaction().replace(R.id.fragment_frame, mFragment).commit();
                        break;
                    case R.id.tab_outbox:
                        mFragment = new OutboxFragment();
                        fm.beginTransaction().replace(R.id.fragment_frame, mFragment).commit();
                        break;
                }
            }
        });

        mBottombar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_group:
                        break;
                    case R.id.tab_message:
                        break;
                    case R.id.tab_send:
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }


    @Override
    public void setCurrentDialog(Dialog dialog) {
        mDialog = dialog;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}