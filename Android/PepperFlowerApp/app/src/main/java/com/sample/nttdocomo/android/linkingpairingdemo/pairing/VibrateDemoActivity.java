package com.sample.nttdocomo.android.linkingpairingdemo.pairing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sample.nttdocomo.android.linkingpairingdemo.R;
import com.nttdocomo.android.sdaiflib.SendOther;

import java.util.ArrayList;

public class VibrateDemoActivity extends AppCompatActivity {

    /** 開発者ボードのバイブレーションを鳴動させる際のパターン設定を行うための固定ID。**/
    private static final int LINKING_IF_PATTERN_ID = 0x10;

    ListView listView;

    ArrayList<String> items = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    SparseIntArray array = new SparseIntArray();

    /**
     * 開発者ボードのバイブレーション鳴動パターンをリストにセット
     * 選択された項目に応じてパターンIDがAPIにセットできるようにする
     */
    private void init() {

        items.add("OFF（停止）");
        items.add("Pattern1（常時動作）");
        items.add("Pattern2（1秒ごとに動作/停止）");
        items.add("Pattern3（2秒ごとに動作/停止）");
        items.add("Pattern4（3秒ごとに動作/停止）");

        array.append(0, 0x21);
        array.append(1, 0x22);
        array.append(2, 0x23);
        array.append(3, 0x24);
        array.append(4, 0x25);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate_demo);

        listView = (ListView) findViewById(R.id.vib_pattern_list);
        init();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendOther(array.get(position));
                Toast.makeText(VibrateDemoActivity.this, getResources().getString(R.string.vibration_log), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Linkingに対してその他通知を送信するためのIFを使用する
     * @param vibPatternId バイブレーションの鳴動パターンID
     */
    private void sendOther(int vibPatternId) {
        SendOther sendOther = new SendOther(this);
        sendOther.setVibration(
                new byte[] {
                        LINKING_IF_PATTERN_ID,  //バイブレーションパターンの設定項目ID（固定値）
                        (byte) vibPatternId     //指定したパターンID
                }
        );
        sendOther.send();
    }
}
