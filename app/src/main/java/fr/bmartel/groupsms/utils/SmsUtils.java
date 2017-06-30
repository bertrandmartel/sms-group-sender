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
package fr.bmartel.groupsms.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

import fr.bmartel.groupsms.activity.BaseActivity;

/**
 * SMS utils functions.
 *
 * @author Bertrand Martel
 */
public class SmsUtils {

    private static String ACTION_SMS_SENT = "SMS_SENT";
    private static String ACTION_SMS_DELIVERED = "SMS_DELIVERED";

    /**
     * Send SMS.
     *
     * @param context
     * @param phoneNumber
     * @param message
     */
    public static void sendSms(String id, Context context, String phoneNumber, String message) {

        Intent sendIntent = new Intent(ACTION_SMS_SENT);
        sendIntent.putExtra("id", id);

        Intent deliveredIntent = new Intent(ACTION_SMS_DELIVERED);
        deliveredIntent.putExtra("id", id);

        PendingIntent piSent = PendingIntent.getBroadcast(context, BaseActivity.REQUEST_CODE, sendIntent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, BaseActivity.REQUEST_CODE, deliveredIntent, PendingIntent.FLAG_ONE_SHOT);
        BaseActivity.REQUEST_CODE++;

        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<String> messagelist = smsManager.divideMessage(message);
        ArrayList<PendingIntent> pendingSentList = new ArrayList<>();
        pendingSentList.add(piSent);
        ArrayList<PendingIntent> pendingDeliveredList = new ArrayList<>();
        pendingDeliveredList.add(piDelivered);
        smsManager.sendMultipartTextMessage(phoneNumber, null, messagelist, pendingSentList, pendingDeliveredList);
    }

}
