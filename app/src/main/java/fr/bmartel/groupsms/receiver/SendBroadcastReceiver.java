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
package fr.bmartel.groupsms.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import fr.bmartel.groupsms.activity.BaseActivity;
import fr.bmartel.groupsms.model.MessageStatus;

/**
 * SendBroadcastReceiver used to retrieve SMS response status.
 */
public class SendBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION_SMS_SENT = "SMS_SENT";

    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals(ACTION_SMS_SENT)) {

            String id = intent.getStringExtra("id");

            switch (getResultCode()) {

                case Activity.RESULT_OK:
                    sendBroadcast(context, id, MessageStatus.SMS_SENT);
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    sendBroadcast(context, id, MessageStatus.SMS_FAILURE);
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    sendBroadcast(context, id, MessageStatus.SMS_ERROR_SERVICE);
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    sendBroadcast(context, id, MessageStatus.SMS_ERROR_NULL_PDU);
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    sendBroadcast(context, id, MessageStatus.SMS_ERROR_RADIO_OFF);
                    break;
            }
        }
    }

    private void sendBroadcast(Context context, String id, MessageStatus status) {
        Intent resourceIntent = new Intent(context.getApplicationContext().getPackageName() + "-" + BaseActivity.SMS_STATUS);
        resourceIntent.putExtra("status", status.toString());
        resourceIntent.putExtra("id", id);
        context.sendBroadcast(resourceIntent);
    }
}