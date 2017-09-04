package com.sample.nttdocomo.android.linkingpairingdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sample.nttdocomo.android.linkingpairingdemo.pairing.ButtonDemoActivity;
import com.sample.nttdocomo.android.linkingpairingdemo.pairing.DeviceInfoActivity;
import com.sample.nttdocomo.android.linkingpairingdemo.pairing.LedDemoActivity;
import com.sample.nttdocomo.android.linkingpairingdemo.pairing.PairingConst;
import com.sample.nttdocomo.android.linkingpairingdemo.pairing.SensorDemoActivity;
import com.sample.nttdocomo.android.linkingpairingdemo.pairing.VibrateDemoActivity;
import com.sample.nttdocomo.android.linkingpairingdemo.util.SensorTypeDialogFragment;

/** 各機能に画面遷移するだけ **/
public class MainActivity extends AppCompatActivity implements SensorTypeDialogFragment.OnButtonClickListener {

    /**
     * ペアリングデモ
     **/
    private Button ledButton;
    private Button vibButton;
    private Button accelButton;
    private Button angularButton;
    private Button orientButton;
    private Button buttonIdButton;
    private Button deviceInfoButton;
    private Button batteryButton;
    private Button temperatureButton;
    private Button humidityButton;
    private Button pressureButton;
    private Button openCloseButton;
    private Button humanButton;
    private Button moveButton;
    private Button extensionButton;

    /**
     * センサータイプ選択ダイアログ
     **/
    private SensorTypeDialogFragment sensorTypeDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorTypeDialogFragment = new SensorTypeDialogFragment();

        deviceInfoButton = (Button) findViewById(R.id.device_info_button);
        deviceInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(MainActivity.this, DeviceInfoActivity.class);
                startActivity(infoIntent);
            }
        });

        ledButton = (Button) findViewById(R.id.led_button);
        ledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ledIntent = new Intent(MainActivity.this, LedDemoActivity.class);
                startActivity(ledIntent);
            }
        });

        vibButton = (Button) findViewById(R.id.vibration_button);
        vibButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vibIntent = new Intent(MainActivity.this, VibrateDemoActivity.class);
                startActivity(vibIntent);
            }
        });

        accelButton = (Button) findViewById(R.id.accel_button);
        accelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accelIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
                accelIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, PairingConst.SENSOR_ID_ACCEL);
                startActivity(accelIntent);
            }
        });

        angularButton = (Button) findViewById(R.id.angular_button);
        angularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent angularIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
                angularIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, PairingConst.SENSOR_ID_ANGULAR);
                startActivity(angularIntent);
            }
        });

        orientButton = (Button) findViewById(R.id.orient_button);
        orientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
                orientIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, PairingConst.SENSOR_ID_ORIENT);
                startActivity(orientIntent);
            }
        });

        buttonIdButton = (Button) findViewById(R.id.button_id_button);
        buttonIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buttonIntent = new Intent(MainActivity.this, ButtonDemoActivity.class);
                startActivity(buttonIntent);
            }
        });

        batteryButton = (Button) findViewById(R.id.battery_button);
        batteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
                orientIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, PairingConst.SENSOR_ID_BATTERY);
                startActivity(orientIntent);
            }
        });

        temperatureButton = (Button) findViewById(R.id.pairing_temperature_button);
        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
                orientIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, PairingConst.SENSOR_ID_TEMPERATURE);
                startActivity(orientIntent);
            }
        });

        humidityButton = (Button) findViewById(R.id.pairing_humidity_button);
        humidityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
                orientIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, PairingConst.SENSOR_ID_HUMIDITY);
                startActivity(orientIntent);
            }
        });

        pressureButton = (Button) findViewById(R.id.pairing_pressure_button);
        pressureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
                orientIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, PairingConst.SENSOR_ID_PRESSURE);
                startActivity(orientIntent);
            }
        });

        openCloseButton = (Button) findViewById(R.id.pairing_open_close_button);
        openCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
                orientIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, PairingConst.SENSOR_ID_OPEN_CLOSE);
                startActivity(orientIntent);
            }
        });

        humanButton = (Button) findViewById(R.id.pairing_human_button);
        humanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
                orientIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, PairingConst.SENSOR_ID_HUMAN);
                startActivity(orientIntent);
            }
        });

        moveButton = (Button) findViewById(R.id.pairing_move_button);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orientIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
                orientIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, PairingConst.SENSOR_ID_MOVE);
                startActivity(orientIntent);
            }
        });

        extensionButton = (Button) findViewById(R.id.pairing_extension_button);
        extensionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorTypeDialogFragment.show(getSupportFragmentManager(), SensorTypeDialogFragment.class.getName());
            }
        });
    }

    @Override
    public void onSensorTypeSubmitClick(Integer sensorType) {
        Intent orientIntent = new Intent(MainActivity.this, SensorDemoActivity.class);
        orientIntent.putExtra(PairingConst.INTENT_EXTRA_SENSOR_ID, sensorType);
        startActivity(orientIntent);
    }
}