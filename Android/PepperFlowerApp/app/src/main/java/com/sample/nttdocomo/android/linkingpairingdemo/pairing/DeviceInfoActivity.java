package com.sample.nttdocomo.android.linkingpairingdemo.pairing;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sample.nttdocomo.android.linkingpairingdemo.R;
import com.nttdocomo.android.sdaiflib.DeviceInfo;
import com.nttdocomo.android.sdaiflib.GetDeviceInformation;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfoActivity extends AppCompatActivity {

    ListView listView;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        listView = (ListView) findViewById(R.id.device_info_list);


        /** ペアリングデバイス情報を取得 **/
        GetDeviceInformation deviceInfo = new GetDeviceInformation(this);
        List<DeviceInfo> devices = deviceInfo.getInformation();

        //ListViewに取得した情報をセットする
        adapter = new CustomAdapter(this, 0, devices);
        listView.setAdapter(adapter);

    }

    private class CustomAdapter extends ArrayAdapter<DeviceInfo> {

        Context context;
        LayoutInflater inflater;

        class ViewHolder{
            TextView pairingNo;
            TextView nameText;
            TextView modelIdText;
            TextView uniqueIdText;
            TextView bdAddressText;
            TextView stateText;
            TextView featureText;
            TextView sensorTypeText;
        }

        ViewHolder holder;

        public CustomAdapter(Context context, int textViewResourceId, List<DeviceInfo> items){
            super(context, textViewResourceId, new ArrayList<DeviceInfo>());
            this.context = context;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for(int i=0 ; i<items.size() ; i++){
                DeviceInfo info = items.get(i);
                add(info);
            }
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            DeviceInfo info = (DeviceInfo) getItem(position);

            if(convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.device_info_layout, null);
                holder.pairingNo = (TextView) convertView.findViewById(R.id.pairing_number);
                holder.nameText = (TextView) convertView.findViewById(R.id.info_name);
                holder.modelIdText = (TextView) convertView.findViewById(R.id.info_model_id);
                holder.uniqueIdText = (TextView) convertView.findViewById(R.id.info_uid);
                holder.bdAddressText = (TextView) convertView.findViewById(R.id.info_bd_address);
                holder.stateText = (TextView) convertView.findViewById(R.id.info_state);
                holder.featureText = (TextView) convertView.findViewById(R.id.info_feature);
                holder.sensorTypeText = (TextView) convertView.findViewById(R.id.info_sensor);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            /** APIにより取得したペアリングデバイスの詳細情報をViewに表示  **/
            holder.pairingNo.setText(String.format("ペアリングデバイス %d", (position + 1)));    /** ペアリング番号 **/
            holder.nameText.setText(info.getName());                                           /** デバイス名 **/
            holder.modelIdText.setText(String.format("%d", info.getModelId()));                /** デバイスID **/
            holder.uniqueIdText.setText(String.format("%d", info.getUniqueId()));              /** デバイス固有ID **/
            holder.bdAddressText.setText(info.getBdaddress());                                 /** BDアドレス **/
            holder.stateText.setText(String.format("%d", info.getState()));                    /** 接続状態 **/
            holder.featureText.setText("0b" + Integer.toBinaryString(info.getFeature())
                    + "(" + info.getFeature() + ")");                                          /** 機器能力 **/
            holder.sensorTypeText.setText(String.format("%d", info.getExSensorType()));        /** 拡張センサID **/

            return convertView;
        }

    }
}
