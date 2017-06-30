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

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.roughike.bottombar.BottomBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import fr.bmartel.groupsms.R;
import fr.bmartel.groupsms.inter.IBaseActivity;
import fr.bmartel.groupsms.inter.IDeletionListener;
import fr.bmartel.groupsms.inter.ISmsStatusListener;
import fr.bmartel.groupsms.model.Contact;
import fr.bmartel.groupsms.model.Group;
import fr.bmartel.groupsms.model.Message;
import fr.bmartel.groupsms.model.MessageStatus;
import fr.bmartel.groupsms.model.SendConfiguration;
import fr.bmartel.groupsms.model.SmsTask;
import fr.bmartel.groupsms.model.SmsTaskMode;
import fr.bmartel.groupsms.utils.MenuUtils;
import fr.bmartel.groupsms.utils.SmsUtils;

/**
 * Abstract activity for all activities in Bluetooth LE Analyzer
 *
 * @author Bertrand Martel
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity {

    public final static String SMS_STATUS = "SMS_STATUS";

    public List<SmsTask> mTaskQueue = new ArrayList<>();
    /**
     * application toolbar
     */
    protected Toolbar toolbar = null;

    /**
     * navigationdrawer
     */
    protected DrawerLayout mDrawer = null;

    /**
     * toggle on the hamburger button
     */
    protected ActionBarDrawerToggle drawerToggle;

    /**
     * navigation view
     */
    protected NavigationView nvDrawer;

    /**
     * activity layout ressource id
     */
    private int layoutId;

    private List<Group> mGroupList;

    private List<Message> mMessageList;

    private List<SendConfiguration> mConfigurationList;

    /**
     * the current fragment.
     */
    protected Fragment mFragment;

    /**
     * set activity ressource id
     *
     * @param resId
     */
    protected void setLayout(int resId) {
        layoutId = resId;
    }

    protected SharedPreferences mSharedPref;

    protected IDeletionListener mDeletionListener;

    private ScheduledExecutorService mExecutor;

    private ISmsStatusListener mStatusListener;

    protected BottomBar mBottombar;

    public static int REQUEST_CODE = 0;

    protected ShareActionProvider mShareActionProvider;

    private BroadcastReceiver mSmsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(getApplicationContext().getPackageName() + "-" + BaseActivity.SMS_STATUS)) {
                MessageStatus status = MessageStatus.valueOf(intent.getStringExtra("status"));

                if (status != null) {
                    //notify new status
                    String id = intent.getStringExtra("id");

                    Log.v("test", "new status : " + status + " for id " + id);
                    switch (status) {
                        case SMS_CANCELED:
                        case SMS_ERROR_NULL_PDU:
                        case SMS_ERROR_RADIO_OFF:
                        case SMS_ERROR_SERVICE:
                        case SMS_FAILURE:
                            updateSmsMap(id, status);
                            break;
                        case SMS_SENT:
                            updateSmsMap(id, status);
                            break;
                        case SMS_DELIVERED:
                            updateSmsMap(id, status);
                            break;
                    }
                }
            }
        }
    };

    /*
    private void setSharedIntent() {
        File sharedFile = new File(mBtSnoopFilePath);

        String object = "HCI report " + timestampFormat.format(new Date().getTime());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, object);
        intent.putExtra(Intent.EXTRA_TEXT, object);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(sharedFile));

        mShareActionProvider.setShareIntent(intent);
    }
    */

    private void updateSmsMap(String id, MessageStatus status) {
        for (SmsTask task : mTaskQueue) {
            if (task != null && task.getid() != null && task.getid().equals(id) &&
                    !(task.getStatus() == MessageStatus.SMS_DELIVERED && status == MessageStatus.SMS_SENT)) {
                task.setStatus(status);
            }
        }
        notifyTask();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layoutId);

        mBottombar = findViewById(R.id.bottomBar);

        mSharedPref = getApplicationContext().getSharedPreferences(getApplicationContext().getPackageName(), 0);

        initModel();

        // Set a Toolbar to replace the ActionBar.
        toolbar = findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);

        setToolbarTitle();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.toolbar_menu);

        // Find our drawer view
        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.setDrawerListener(drawerToggle);
        nvDrawer = findViewById(R.id.nvView);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        IntentFilter filter = new IntentFilter();
        filter.addAction(getApplicationContext().getPackageName() + "-" + BaseActivity.SMS_STATUS);
        registerReceiver(mSmsReceiver, filter);

        mExecutor = Executors.newScheduledThreadPool(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSmsReceiver);
    }

    private void notifyTask() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mStatusListener != null) {
                    mStatusListener.onStatusChange(mTaskQueue);
                }
            }
        });
    }

    /**
     * initialize model.
     */
    private void initModel() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        mGroupList = gson.fromJson(mSharedPref.getString("groupList", "[]"), new TypeToken<List<Group>>() {
        }.getType());

        mMessageList = gson.fromJson(mSharedPref.getString("messageList", "[]"), new TypeToken<List<Message>>() {
        }.getType());

        mConfigurationList = gson.fromJson(mSharedPref.getString("configurationList", "[]"), new TypeToken<List<SendConfiguration>>() {
        }.getType());
    }

    /**
     * Set toolbar title in initialization or when USB device event occurs
     */
    protected void setToolbarTitle() {
        getSupportActionBar().setTitle(getResources().getString(R.string.app_title));
    }

    /**
     * setup navigation view
     *
     * @param navigationView
     */
    private void setupDrawerContent(final NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        MenuUtils.selectDrawerItem(menuItem, mDrawer, BaseActivity.this, BaseActivity.this);

                        return false;
                    }
                });
    }

    /**
     * setup action drawer
     *
     * @return
     */
    protected ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawer.isDrawerOpen(GravityCompat.START)) {
            this.mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        hideMenuButton();

        mFragment.onCreateOptionsMenu(menu, this.getMenuInflater());

        MenuItem deleteButton = toolbar.getMenu().findItem(R.id.button_delete);

        deleteButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (mDeletionListener != null) {
                    mDeletionListener.onDelete();
                }
                return false;
            }
        });

        MenuItem item = menu.findItem(R.id.button_share);

        if (item != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            //setSharedIntent();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void hideMenuButton() {
        toolbar.getMenu().findItem(R.id.button_save).setVisible(false);
        toolbar.getMenu().findItem(R.id.button_delete).setVisible(false);
        toolbar.getMenu().findItem(R.id.button_send).setVisible(false);
        toolbar.getMenu().findItem(R.id.button_replay).setVisible(false);
        toolbar.getMenu().findItem(R.id.button_add_message).setVisible(false);
        toolbar.getMenu().findItem(R.id.button_add_group).setVisible(false);
        toolbar.getMenu().findItem(R.id.button_share).setVisible(false);
    }

    @Override
    public boolean checkDuplicateGroup(String name) {
        for (Group group : mGroupList) {
            if (group.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkDuplicateMessage(String title) {
        for (Message message : mMessageList) {
            if (message.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Group> getGroupList() {
        return mGroupList;
    }

    @Override
    public List<Message> getMessageList() {
        return mMessageList;
    }

    @Override
    public void saveGroup() {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mGroupList);
        prefsEditor.putString("groupList", json);
        prefsEditor.commit();
    }

    @Override
    public void saveMessages() {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mMessageList);
        prefsEditor.putString("messageList", json);
        prefsEditor.commit();
    }

    @Override
    public void saveSendConfiguration(SendConfiguration configuration) {

        boolean found = false;
        for (int i = 0; i < mConfigurationList.size(); i++) {
            if (mConfigurationList.get(i).getTopic().equals(configuration.getTopic())) {
                mConfigurationList.set(i, configuration);
                found = true;
            }
        }
        if (!found) {
            mConfigurationList.add(configuration);
        }
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mConfigurationList);
        prefsEditor.putString("configurationList", json);
        prefsEditor.commit();
    }

    @Override
    public void deleteGroup(String name) {
        for (Group group : mGroupList) {
            if (group.getName().equals(name)) {
                mGroupList.remove(group);
                break;
            }
        }
    }

    @Override
    public void deleteMessage(String title) {
        for (Message message : mMessageList) {
            if (message.getTitle().equals(title)) {
                mMessageList.remove(message);
                break;
            }
        }
    }

    @Override
    public void setDeletionListener(IDeletionListener listener) {
        mDeletionListener = listener;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public List<SmsTask> getOutboxList() {
        return mTaskQueue;
    }

    @Override
    public void startSmsTask(final SmsTaskMode mode, final List<Message> messageList, final List<Contact> contactList) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mTaskQueue.clear();
                prepareTaskList(mode, messageList, contactList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BaseActivity.this,
                                "sending " + mTaskQueue.size() + " SMS...", Toast.LENGTH_SHORT).show();
                    }
                });

                processSmsTasks();
            }
        }).start();
    }

    @Override
    public void startSmsTask(final SmsTask task) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = mTaskQueue.size() - 1; i >= 0; i--) {
                    if (mTaskQueue.get(i).getid().equals(task.getid())) {
                        mTaskQueue.remove(i);
                    }
                }
                mTaskQueue.add(task);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BaseActivity.this,
                                "sending 1 SMS...", Toast.LENGTH_SHORT).show();
                    }
                });

                processSingleSmsTask(task);
            }
        }).start();
    }

    private void prepareTaskList(SmsTaskMode mode, final List<Message> messageList, final List<Contact> contactList) {
        mTaskQueue.clear();
        switch (mode) {
            case ALL_TO_ALL:
                for (int i = 0; i < contactList.size(); i++) {
                    for (int j = 0; j < messageList.size(); j++) {
                        mTaskQueue.add(new SmsTask(contactList.get(i), messageList.get(j), MessageStatus.IDLE));
                    }
                }
                break;
            case ONE_TO_ONE:
                int contactPos = 0;
                for (int i = 0; i < messageList.size(); i++) {
                    mTaskQueue.add(new SmsTask(contactList.get(contactPos), messageList.get(i), MessageStatus.IDLE));
                    contactPos++;
                    if (contactPos == contactList.size()) {
                        contactPos = 0;
                    }
                }
                break;
        }
    }

    private void processSmsTasks() {
        for (SmsTask task : mTaskQueue) {
            processSingleSmsTask(task);
        }
        notifyTask();
    }

    private void processSingleSmsTask(SmsTask task) {
        String id = UUID.randomUUID().toString();
        task.setId(id);
        task.setStatus(MessageStatus.PENDING);
        SmsUtils.sendSms(id, this, task.getContact().getPhoneNumber(), task.getMessage().getBody());
    }

    @Override
    public void setStatusListener(ISmsStatusListener listener) {
        mStatusListener = listener;
    }

    @Override
    public void deleteOutbox() {
        mTaskQueue.clear();
    }

    @Override
    public List<Message> getMessagesForTopic(String topic) {
        List<Message> ret = new ArrayList<>();
        for (Message message : mMessageList) {
            if (message.getTopic().equals(topic)) {
                ret.add(message);
            }
        }
        return ret;
    }

    @Override
    public void openOutbox() {
        mBottombar.selectTabWithId(R.id.tab_outbox);
    }

    @Override
    public List<SendConfiguration> getSendConfigurationList() {
        return mConfigurationList;
    }
}