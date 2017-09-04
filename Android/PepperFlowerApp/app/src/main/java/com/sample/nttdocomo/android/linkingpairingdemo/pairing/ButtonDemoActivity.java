package com.sample.nttdocomo.android.linkingpairingdemo.pairing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.sample.nttdocomo.android.linkingpairingdemo.R;
import com.nttdocomo.android.sdaiflib.NotifyNotification.NotificationInterface;

import java.util.ArrayList;

public class ButtonDemoActivity extends AppCompatActivity {

    private static final boolean DBG = false;
    private static final String TAG = "ButtonDemoActivity";

    private ListView listView;
    private ArrayList<String> notifyList;
    private ArrayAdapter<String> adapter;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_demo);

        listView = (ListView) findViewById(R.id.notify_result);
        notifyList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notifyList);
        listView.setAdapter(adapter);
        mHandler = new Handler();

    }

    @Override
    public void onResume() {
        super.onResume();
        register();
    }

    @Override
    public void onPause() {
        super.onPause();
        /** BroadcastReceiverの解除 **/
        unregisterReceiver(localReceiver);
    }

    /** ペアリングデバイスからの通知をLinking経由で受け取るためのBroadcastReceiverを登録 **/
    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PairingConst.ACTION_NOTIFY);
        registerReceiver(localReceiver, filter);

    }

    /** ライブラリの使用によるデバイスからの通知受信 **/
    NotificationInterface receiver = new NotificationInterface() {
        @Override
        public void onNotify() {
        //TODO 通知された情報（IntentのExtraデータ）はSharedPreferencesに保存されている
        // （アプリの構成に応じて、必要な際にSharedPreferencesから読み出す）
        }
    };

    /** ライブラリを使用せずに、自作BroadcastReceiverによる通知受信 **/
    BroadcastReceiver localReceiver = new BroadcastReceiver() {

        int buttonId = -1;

        @Override
        public void onReceive(Context context, Intent intent) {

            //SharedPreferencesの記録ではなく、通知情報をリアルタイムでハンドリングしたいのであれば
            //BroadcastReceiverでIntentから情報を取得する

            //TODO ボタンIDを取得することを想定、それ以外のパラメータ更新時はreturnする
            // 取得したい通知情報を変更したい場合はPairingConstのExtra定義を参照
            buttonId = intent.getIntExtra(PairingConst.EXTRA_BUTTON_ID, -1);

            if(buttonId == -1) {
                return;
            }

            if(DBG) Log.d(TAG, "received pairing notification : buttonId[" + buttonId + "]");

            mHandler.postDelayed(listUpdateTask, 0);
        }

        //ListViewの更新
        Runnable listUpdateTask = new Runnable() {

            @Override
            public void run() {
                String log = String.format("%s buttonId[%d]", PairingConst.timeLogFormat(System.currentTimeMillis()), buttonId);

                if(log.equals("")) {
                    return;
                }

                if (notifyList.size() == 0) {
                    notifyList.add(log);
                } else if (notifyList.size() == 1) {
                    notifyList.add(notifyList.get(0));
                    notifyList.set(0, log);
                } else {
                    if (notifyList.size() < 100) {
                        notifyList.add(notifyList.get(notifyList.size() - 1));
                        for (int i = 1; i < notifyList.size(); i++) {
                            notifyList.set(notifyList.size() - i, notifyList.get(notifyList.size() - i - 1));
                        }
                        notifyList.set(0, log);
                    } else {
                        for (int i = 1; i < notifyList.size() && i < 100; i++) {
                            notifyList.set(notifyList.size() - i, notifyList.get(notifyList.size() - i - 1));
                        }
                        notifyList.set(0, log);
                    }
                }

                adapter.notifyDataSetChanged();

            }

        };
    };


}
