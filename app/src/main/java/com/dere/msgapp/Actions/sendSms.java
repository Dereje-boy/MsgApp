package com.dere.msgapp.Actions;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class sendSms {
    Context context;
    String phoneno;
    String msg;
    int amount;
    PendingIntent sent_intent = null,delivery_intent = null;

    public sendSms(Context context, String phoneno, String msg) {
        this.context = context;
        this.phoneno = phoneno;
        this.msg = msg;
    }

    public sendSms(Context context, String phoneno, int amount, PendingIntent sent_intent, PendingIntent delivery_intent){
        this.context = context;
        this.amount = amount;
        this.phoneno = phoneno;
        this.sent_intent = sent_intent;
        this.delivery_intent = delivery_intent;
    }

    public void doIt(int amount){
        SmsManager smsManager = SmsManager.getDefault();
        for(int i=0; i<amount; i++){
            smsManager.sendTextMessage(phoneno,null,String.valueOf(amount-i),sent_intent,delivery_intent);
        }
    }

}
