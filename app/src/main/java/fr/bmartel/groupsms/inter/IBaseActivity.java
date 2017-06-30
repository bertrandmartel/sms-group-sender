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
package fr.bmartel.groupsms.inter;

import android.app.Dialog;
import android.support.v7.widget.Toolbar;

import java.util.List;

import fr.bmartel.groupsms.model.Contact;
import fr.bmartel.groupsms.model.Group;
import fr.bmartel.groupsms.model.Message;
import fr.bmartel.groupsms.model.SendConfiguration;
import fr.bmartel.groupsms.model.SmsTask;
import fr.bmartel.groupsms.model.SmsTaskMode;

/**
 * Interface used for the fragment to communicate with the main activity.
 *
 * @author Bertrand Martel
 */
public interface IBaseActivity {

    /**
     * set opened dialog in activity
     *
     * @param dialog
     */
    void setCurrentDialog(Dialog dialog);

    List<Group> getGroupList();

    List<Message> getMessageList();

    void saveGroup();

    void saveMessages();

    boolean checkDuplicateGroup(String name);

    Toolbar getToolbar();

    void deleteGroup(String name);

    void setToolbarTitle(String title);

    void deleteMessage(String title);

    boolean checkDuplicateMessage(String title);

    void setDeletionListener(IDeletionListener listener);

    void startSmsTask(SmsTaskMode mode, List<Message> messageList, List<Contact> contactList);

    void startSmsTask(SmsTask task);

    void setStatusListener(ISmsStatusListener listener);

    List<SmsTask> getOutboxList();

    void deleteOutbox();

    List<Message> getMessagesForTopic(String topic);

    void hideMenuButton();

    void openOutbox();

    void saveSendConfiguration(SendConfiguration configuration);

    List<SendConfiguration> getSendConfigurationList();
}
