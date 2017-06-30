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
package fr.bmartel.groupsms.model;

/**
 * Sms Task model.
 *
 * @author Bertrand Martel
 */
public class SmsTask {

    private Contact mContact;

    private Message mMessage;

    private MessageStatus mStatus;

    private String mId;

    public SmsTask(Contact contact, Message message, MessageStatus status) {
        mContact = contact;
        mMessage = message;
        mStatus = status;
    }

    public String getid() {
        return mId;
    }

    public Contact getContact() {
        return mContact;
    }

    public Message getMessage() {
        return mMessage;
    }

    public MessageStatus getStatus() {
        return mStatus;
    }

    public void setStatus(MessageStatus status) {
        this.mStatus = status;
    }

    public void setId(String id) {
        mId = id;
    }
}
