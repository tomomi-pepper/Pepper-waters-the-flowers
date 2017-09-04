package com.sample.nttdocomo.android.linkingpairingdemo.pairing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sample.nttdocomo.android.linkingpairingdemo.R;
import com.nttdocomo.android.sdaiflib.SendOther;

public class LedDemoActivity extends AppCompatActivity implements View.OnClickListener{

    Button redButton;
    Button greenButton;
    Button orangeButton;

    /**
     * 開発者ボードのLEDを点灯させる際のパターン設定と色設定を
     * 行うための固定ID。
     */
    private static final byte LINKING_IF_PATTERN_ID = 0x20;
    private static final byte LINKING_IF_COLOR_ID = 0x30;

    /**
     * 開発者ボードの3色のLEDを点灯させるデモです。
     * 他のデバイス点灯時はデバイス情報取得IFにより取得された
     * イルミネーション情報に含まれた値を参照すること。
     */
    private static final byte COLOR_ID_RED = 0x01;
    private static final byte COLOR_ID_GREEN = 0x02;
    private static final byte COLOR_ID_ORANGE = 0x03;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_demo_button);

        redButton = (Button) findViewById(R.id.led_red_demo);
        redButton.setOnClickListener(this);

        greenButton = (Button) findViewById(R.id.led_green_demo);
        greenButton.setOnClickListener(this);

        orangeButton = (Button) findViewById(R.id.led_orange_demo);
        orangeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.led_red_demo:
                sendOther(COLOR_ID_RED);
                break;
            case R.id.led_green_demo:
                sendOther(COLOR_ID_GREEN);
                break;
            case R.id.led_orange_demo:
                sendOther(COLOR_ID_ORANGE);
                break;
        }
    }

    /**
     * Linkingに対してその他通知を送信するためのIFを使用する
     * @param colorId 点灯させたいLEDの色ID
     */
    public void sendOther(byte colorId) {

        //TODO その他通知によりLEDを点灯させる場合の設定をここで行う。
        //通知種別や通知ごとに設定可能なパラメータはLinking API仕様書を別途確認ください

        SendOther sendOther = new SendOther(this);
        sendOther.setIllumination(
                new byte[] {
                        LINKING_IF_PATTERN_ID,  //LEDパターンの設定項目ID（固定値）
                        0x22,                   //指定したパターンID TODO 点灯パターンを変更したい場合はこの値を変える
                        LINKING_IF_COLOR_ID,    //LED色の設定項目ID（固定値）
                        colorId                 //指定した色のID
                });
        sendOther.send();
        Toast.makeText(this, getResources().getString(R.string.lighting_log), Toast.LENGTH_SHORT).show();
    }
}
