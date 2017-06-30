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
import android.util.Log;

import fr.bmartel.groupsms.activity.BaseActivity;
import fr.bmartel.groupsms.model.MessageStatus;

/**
 * DeliveryBroadcastReceiver used to retrieve SMS delivery status.
 */
public class DeliveryBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION_SMS_DELIVERED = "SMS_DELIVERED";

    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals(ACTION_SMS_DELIVERED)) {

            String id = intent.getStringExtra("id");

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    sendBroadast(context, id, MessageStatus.SMS_DELIVERED);
                    break;
                case Activity.RESULT_CANCELED:
                    sendBroadast(context, id, MessageStatus.SMS_CANCELED);
                    break;
            }
        }
    }

    private void sendBroadast(Context context, String id, MessageStatus status) {
        Intent resourceIntent = new Intent(context.getApplicationContext().getPackageName() + "-" + BaseActivity.SMS_STATUS);
        resourceIntent.putExtra("status", status.toString());
        resourceIntent.putExtra("id", id);
        context.sendBroadcast(resourceIntent);
    }
}