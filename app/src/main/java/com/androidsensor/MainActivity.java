package com.androidsensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView _txView,_txView2;
    SensorManager _sM;
    List _listSenAcc, _listSenProx;
    ConstraintLayout here;
    SensorEventListener sel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _sM = (SensorManager) getSystemService(SENSOR_SERVICE);
        _txView = findViewById(R.id.textView1);
        _txView2 = findViewById(R.id.textView2);
        _listSenAcc = _sM.getSensorList(Sensor.TYPE_ACCELEROMETER);
        _listSenProx = _sM.getSensorList(Sensor.TYPE_PROXIMITY);
        here = findViewById(R.id.background);

        sel = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Sensor sensor = event.sensor;
                float[] valueP, valueA;
                if (sensor.getType() == Sensor.TYPE_PROXIMITY){
                    valueP = event.values;
                    _txView2.setText("Proximity : "+valueP[0]);
                    if(valueP[0]<8){
                        here.setBackgroundColor(Color.parseColor("red"));
                    } else {
                        here.setBackgroundColor(Color.parseColor("green"));
                    }
                } else if (sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                    valueA = event.values;
                    _txView.setText(Arrays.toString(valueA));
                    //_txView.setText("X : "+value[0]+" Y : "+value[1]+" z : "+value[2]);
                    if(valueA[0]<-5){
                        here.setBackgroundColor(Color.parseColor("yellow"));
                    } else if (valueA[0]>5){
                        here.setBackgroundColor(Color.parseColor("blue"));
                    } else if (valueA[1]<=1){
                        here.setBackgroundColor(Color.parseColor("green"));
                    }
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if(_listSenAcc.size()>0)
        {
            _sM.registerListener(sel,(Sensor)_listSenAcc.get(0),SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(_listSenProx.size()>0)
        {
            _sM.registerListener(sel,(Sensor)_listSenProx.get(0),SensorManager.SENSOR_DELAY_NORMAL);
        }

    }
    @Override
    protected void onStop(){
        _sM.unregisterListener(sel);
        super.onStop();
    }

    @Override
    protected void onResume(){
        super.onResume();
        _sM.registerListener(sel,(Sensor)_listSenAcc.get(0),SensorManager.SENSOR_DELAY_NORMAL);
        _sM.registerListener(sel,(Sensor)_listSenProx.get(0),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        _sM.unregisterListener(sel);
        super.onPause();
    }
}

