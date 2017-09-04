package com.sample.nttdocomo.android.linkingpairingdemo.pairing;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.nttdocomo.android.linkingpairingdemo.R;
import com.sample.nttdocomo.android.linkingpairingdemo.util.PairingUtil;
import com.nttdocomo.android.sdaiflib.ControlSensorData;
import com.nttdocomo.android.sdaiflib.DeviceInfo;
import com.nttdocomo.android.sdaiflib.ErrorCode;
import com.nttdocomo.android.sdaiflib.GetDeviceInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.sample.nttdocomo.android.linkingpairingdemo.pairing.SensorDemoActivity.InputStreamToString;

public class SensorDemoActivity extends AppCompatActivity {

    private static final boolean DBG = true;
    private static final String TAG = "SensorDemoActivity";

    private EditText intervalEdit;
    private Button sensorStartButton;
    private Button sensorStopButton;
    private Button timeFormatButton;

    private TextView listTitle;
    private TextView bdAddress;
    private ListView listView;

    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private Handler mHandler;

    /** ペアリングしているデバイスへセンサ情報取得要求などを設定するクラス **/
    private ControlSensorData mSensorData;
    /** デバイスからセンサ情報が取得された場合に、Linkingからデータを受け取るためのReceiver **/
    private SensorReceiver receiver;

    private int sensorId = -1;
    private boolean isLinkingTimeFormat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing_sensor_demo);

        intervalEdit = (EditText) findViewById(R.id.sensor_interval);
        sensorStartButton = (Button) findViewById(R.id.sensor_start_button);
        sensorStopButton = (Button) findViewById(R.id.sensor_stop_button);
        listTitle = (TextView) findViewById(R.id.sensor_log_title);
        bdAddress = (TextView) findViewById(R.id.sensor_bd_address);
        listView = (ListView) findViewById(R.id.sensor_result);

        /** sensorId取得不可の場合、Activityを終了する **/
        sensorId = getIntent().getIntExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, -1);
        if (sensorId == -1) {
            finish();
        }

        /** sensorId => SENSOR_ID_EXTENSION である場合、タイトルにIDを表記する **/
        if (sensorId >= PairingConst.SENSOR_ID_EXTENSION) {
            listTitle.setText(PairingConst.getSensorName(PairingConst.SENSOR_ID_EXTENSION) + "(ID:" + sensorId + ")");
        } else {
            listTitle.setText(PairingConst.getSensorName(sensorId));
        }

        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        receiver = new SensorReceiver();
        mSensorData = new ControlSensorData(this, receiver);
        mHandler = new Handler();

        /** センサー情報設定 **/
        getSensorData();

        /** 開始ボタンの動作設定 **/
        sensorStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intervalText = intervalEdit.getText().toString();
                if (intervalText == null || intervalText.isEmpty()) {
                    return;
                }
                mSensorData.setInterval(Integer.valueOf(intervalText));
                Integer result = mSensorData.start();
                Toast.makeText(getApplication(), "センサー開始要求 : " + result, Toast.LENGTH_SHORT).show();
            }
        });

        /** 停止ボタンの動作設定 **/
        sensorStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer result = mSensorData.stop();
                Toast.makeText(getApplication(), "センサー停止要求 : " + result, Toast.LENGTH_SHORT).show();
            }
        });

        timeFormatButton = (Button) findViewById(R.id.time_button);
        timeFormatButton.setAllCaps(false);
        timeFormatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLinkingTimeFormat = !isLinkingTimeFormat;
                if(isLinkingTimeFormat) {
                    timeFormatButton.setText(getResources().getText(R.string.time_linking));
                } else {
                    timeFormatButton.setText(getResources().getText(R.string.time_app));
                }
                list.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSensorData();
    }


    /** センサー情報の取得をリクエスト **/
    private void getSensorData() {

        /** ペアリングされているデバイスのリストを取得 **/
        GetDeviceInformation getDeviceInformation = new GetDeviceInformation(this);
        List<DeviceInfo> devices = getDeviceInformation.getInformation();

        if (devices.size() == 0) {
            Toast.makeText(this,  "デバイス情報が取得できませんでした", Toast.LENGTH_SHORT).show();
            return;
        }

        /** TODO デバイスが複数登録されている場合にはここで指定するデバイスを変更できるようにする **/
        DeviceInfo info = devices.get(0);

        /** 0 : ジャイロセンサー **/
        /** 1 : 加速度センサー **/
        /** 2 : 方位センサー **/
        mSensorData.setType(sensorId);                  /** リクエストするセンサID **/
        mSensorData.setBDaddress(info.getBdaddress());  /** リクエストするペアリングデバイスのBDアドレス **/

        /** 対象機器のBDアドレスを画面に表示 **/
        bdAddress.setText(info.getBdaddress());
    }

    /** センサ情報の取得を終了 **/
    private void stopSensorData() {
        mSensorData.stop();
        mSensorData.release();
    }

    /**
     * ペアリングしているデバイスのセンサ情報を取得するためのレシーバーを作成
     */
    class SensorReceiver implements ControlSensorData.SensorDataInterface {

        String bd;
        int type;
        float x;
        float y;
        float z;
        byte[] originalData;
        long time;

        @Override
        public void onStopSensor(final String bd, int type, int reason) {
            Toast.makeText(SensorDemoActivity.this, "センサ情報の取得を停止しました", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSensorData(String bd, int type, float x, float y, float z,
                                 byte[] originalData, long time) {

            this.bd = bd;
            this.type = type;
            this.x = x;
            this.y = y;
            this.z = z;
            this.originalData = originalData;
            this.time = time;

            if(DBG) Log.d(TAG, "[" + bd + "] のデータ[type:" + type + "]を受信 " +  "x:" + x + ", y : " + y +", z : " + z);

            mHandler.postDelayed(listUpdateTask, 0);

        }

        /** ListViewの更新 **/
        Runnable listUpdateTask = new Runnable() {
            @Override
            public void run() {
                String log;
                String timestamp;
                if(isLinkingTimeFormat) {
                    timestamp = PairingConst.timeLogFormat(System.currentTimeMillis());
                } else {
                    timestamp = PairingConst.timeLogFormat(time);
                }

                if (type == 3) {
                    /** 電池残量 **/
                    Float convertOriginalData = PairingUtil.convertOriginalData(type, originalData);
                    Boolean chargeFlag = PairingUtil.convertOriginalDataBatteryFlag(type, originalData);
                    log = String.format("%s\n[Address:%s, type:%d]\n chargeFlag:%b originalData:%.01f", timestamp, bd, type, chargeFlag.toString(), convertOriginalData);
                } else if (type == 4 || type == 5 || type == 6) {
                    /** 温度センサー、湿度センサー、気圧センサーの場合 **/
                    Float convertOriginalData = PairingUtil.convertOriginalData(type, originalData);
                    log = String.format("%s\n[Address:%s, type:%d]\n originalData:%.01f", timestamp, bd, type, convertOriginalData);

                    multiThread thread1 = new multiThread(convertOriginalData);
                    thread1.start();

                } else if (type == 7 || type == 8 || type == 9) {
                    /** 開閉センサー、人感センサー、振動センサーの場合 **/
                    Integer convertOriginalData = PairingUtil.convertOriginalDataCount(type, originalData);
                    Boolean flag = PairingUtil.convertOriginalDataFlag(type, originalData);
                    log = String.format("%s\n[Address:%s, type:%d]\n flag:%s originalData:%d", timestamp, bd, type, flag.toString(), convertOriginalData);
                } else if (type >= 10) {
                    /** 拡張センサーの場合 **/
                    if (originalData != null && originalData.length > 0) {
                        log = String.format("%s\n[Address:%s, type:%d]\n x:%.02f y:%.02f z:%.02f originalData:%s", timestamp, bd, type, x, y, z, originalData.toString());
                    } else {
                        log = String.format("%s\n[Address:%s, type:%d]\n x:%.02f y:%.02f z:%.02f", timestamp, bd, type, x, y, z);
                    }
                } else {
                    /** その他のセンサーの場合 **/
                    log = String.format("%s\n[Address:%s, type:%d]\n x:%.02f y:%.02f z:%.02f", timestamp, bd, type, x, y, z);
                }


                if (list.size() == 0) {
                    list.add(log);
                } else if (list.size() == 1) {
                    list.add(list.get(0));
                    list.set(0, log);
                } else {
                    if (list.size() < 100) {
                        list.add(list.get(list.size() - 1));
                        for (int i = 1; i < list.size(); i++) {
                            list.set(list.size() - i, list.get(list.size() - i - 1));
                        }
                        list.set(0, log);
                    } else {
                        for (int i = 1; i < list.size() && i < 100; i++) {
                            list.set(list.size() - i, list.get(list.size() - i - 1));
                        }
                        list.set(0, log);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** センサーデータ取得開始依頼の結果 **/
        int result = mSensorData.onActivityResult(requestCode, resultCode, data);
        if (result != ErrorCode.RESULT_OTHER_ERROR) {
            switch (result) {
                case ErrorCode.RESULT_OK:
                    Toast.makeText(this, "センサ情報の取得を開始します", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "エラー : " + result, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    // InputStream -> String
    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}

class multiThread extends Thread{
    //変数の宣言
    private float humidity;

    //スレッド作成時に実行される処理
    public multiThread(float humidity){
        this.humidity = humidity;
    }

    //スレッド実行時の処理
    public void run() {
        try {

            URL url = new URL("http://127.0.0.1/set_humidity.php?humidity=" + humidity);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            String str = InputStreamToString(con.getInputStream());
            Log.d("HTTP", str);
        } catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //スレッド終了時に呼び出し
    public void stopThread(){
    }
}
