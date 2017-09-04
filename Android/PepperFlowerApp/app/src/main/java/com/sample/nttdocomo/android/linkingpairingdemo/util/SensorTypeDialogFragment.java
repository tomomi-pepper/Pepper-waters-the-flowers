package com.sample.nttdocomo.android.linkingpairingdemo.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sample.nttdocomo.android.linkingpairingdemo.R;

/** 拡張センサータイプの入力ダイアログ **/
public class SensorTypeDialogFragment extends DialogFragment {

    private Button sensorTypeSubmitButton;
    private EditText sensorTypeEditText;
    private OnButtonClickListener onButtonClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("センサータイプ入力");
        dialog.setContentView(R.layout.fragment_phone_register);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_phone_register, null);
        sensorTypeEditText = (EditText) view.findViewById(R.id.sensor_type_edit);

        sensorTypeSubmitButton = (Button) view.findViewById(R.id.sensor_type_submit);
        sensorTypeSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextStr = sensorTypeEditText.getText().toString();
                if (editTextStr.isEmpty()) {
                    dismiss();
                    return;
                }

                // 6〜255以外の数値ではセンサー開始を行わない
                Integer sensorType = Integer.parseInt(editTextStr);
                if (sensorType < 10 || sensorType > 255) {
                    Toast.makeText(getContext(), "10〜255の数値を入力して下さい。", Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                }

                // 呼び出し元となるActivityにおける処理実行
                onButtonClickListener.onSensorTypeSubmitClick(sensorType);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onButtonClickListener = (OnButtonClickListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface OnButtonClickListener {
        public void onSensorTypeSubmitClick(Integer sensorType);
    }
}
