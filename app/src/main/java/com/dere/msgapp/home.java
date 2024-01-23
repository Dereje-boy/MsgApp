package com.dere.msgapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.dere.msgapp.Actions.sendSms;
import com.dere.msgapp.Actions.Animations;

import java.util.ArrayList;
import java.util.List;


public class home extends Fragment implements View.OnClickListener,TextWatcher{

    boolean first = true;

    int i=0;

    int call_request_code = 1;
    TextView textView;
    ViewGroup viewGroup;
    Context context;

    public Button send,check_balance,get_phone_no;
    EditText recv_phone;
    EditText amount_of_msg;
    TextView to_view,amount_of_msg_view,price_view;

    ArrayList<String> sim_numbers;

    public SubscriptionManager subscriptionManager;
    public ArrayList<String> number = new ArrayList<>();
    public List<SubscriptionInfo> subscriptionInfoList;
    Animations animations1 = new Animations(0f,0f,10f,0f,1500l);


    String numbers = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        View view = getView();


        if (view != null) {

            get_phone_no = (Button) view.findViewById(R.id.get_phone_no);
            get_phone_no.setOnClickListener(this);

            send = (Button) view.findViewById(R.id.send);
            send.setOnClickListener(this);

           check_balance =  view.findViewById(R.id.check_balance);
            check_balance.setOnClickListener(this);

            recv_phone = (EditText) view.findViewById(R.id.recv_phone);
            amount_of_msg = (EditText) view.findViewById(R.id.amount_of_msg);
            to_view = (TextView) view.findViewById(R.id.to_view);
            amount_of_msg_view = (TextView) view.findViewById(R.id.amount_of_msg_view);
            price_view = (TextView) view.findViewById(R.id.price_view);

            recv_phone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    to_view.setText("To  : " + s);
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            amount_of_msg.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @SuppressLint({"DefaultLocale", "SetTextI18n"})
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()>=1) {
                        amount_of_msg_view.setText(s + " message");
                        int msg_amount_enterd = 0;
                        try {
                            msg_amount_enterd = Integer.valueOf(String.valueOf(s));
                        } catch (Exception e) {
                        }
                        if (!s.equals("") & msg_amount_enterd < 10000) {
                            if (15 < msg_amount_enterd) {
                                price_view.setText("please reduce the balance you have!\n" + String.valueOf(msg_amount_enterd) + " msg " +
                                        msg_amount_enterd * 0.2 +" birr is too much!");
                            } else {
                                price_view.setText(String.format("%s %f %s", "Price is ", msg_amount_enterd * 0.2, " birr"));
                            }
                        } else
                            price_view.setText("oohh.. something wrong");
                    }
                    else {
                        amount_of_msg_view.setText("0 message");
                        price_view.setText("0 birr");
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });

//            send.setAnimation(animations1);
//            check_balance.setAnimation(animations1);
//            amount_of_msg.setAnimation(animations1);
//            recv_phone.setAnimation(animations1);
//            get_phone_no.setAnimation(animations1);

        } else {
            Toast.makeText(context, "Buttons are not found please contact the developer", Toast.LENGTH_SHORT).show();
        }
    }

    boolean first_get_phone_no = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_phone_no:
                get_Dual_Sim_No();
                if (first_get_phone_no){
                    recv_phone.setText(number.get(0));
                    first_get_phone_no = false;
                }
                else {
                    recv_phone.setText(number.get(1));
                    first_get_phone_no = true;
                }
                get_phone_no.setText("Check again");
                break;
            case R.id.send:
                if (recv_phone.getText().toString().length()>=10 && amount_of_msg.getText().toString().length()>=1){
                try {
                    sendSms sendSms = new sendSms(context, recv_phone.getText().toString(), "hello this is the first msg");
                    sendSms.doIt(Integer.valueOf(amount_of_msg.getText().toString()));
                }catch (Exception e){
                    Toast.makeText(context, "Unable send message", Toast.LENGTH_SHORT).show();
                }
                }
                break;
            case R.id.check_balance:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:*804" + Uri.encode("#")));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                    return;
                } else
                    Toast.makeText(context, "please go to setting and give call permission to the app", Toast.LENGTH_LONG).show();
                break;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void get_Dual_Sim_No() {
        subscriptionManager = SubscriptionManager.from(context);
        if (ActivityCompat.checkSelfPermission
                (context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        for (SubscriptionInfo subscriptionInfo:subscriptionInfoList){
            number.add(subscriptionInfo.getNumber());
        }
    }
    public void check_data_connection(){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String datastate_String;
        int dataState = telephonyManager.getDataState();
        switch (dataState) {
            case TelephonyManager.DATA_DISCONNECTED:
                datastate_String = "Data is Disconnected";
                break;
            case TelephonyManager.DATA_SUSPENDED:
                datastate_String = "Data is Suspended";
                break;
            case TelephonyManager.DATA_CONNECTING:
                datastate_String = "Data is Connecting";
                break;
            case TelephonyManager.DATA_CONNECTED:
                datastate_String = "Data is Connected";
                break;
            default:
                datastate_String = "No information found";
                break;
        }
        Toast.makeText(context, datastate_String, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}