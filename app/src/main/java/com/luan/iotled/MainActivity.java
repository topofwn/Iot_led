package com.luan.iotled;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "IOT_LED";
    private PeripheralManager service;
    private Gpio mLed;
    private GPIO led,button;

    private ButtonInputDriver mButtonInputDriver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        led = new GPIO(233);
        if(led.activationPin()){
            led.setInOut("out");

        }
        button = new GPIO(71);
        if(button.activationPin()){
            button.setInOut("in");
        }
        for(int i = 0; i < 6; i ++){

            //
            new ExecuteAsyncTask().execute( String.valueOf(i) );

        }
//        try {
//            mButtonInputDriver = new ButtonInputDriver(BUTTON_PIN_NAME, Button.LogicState.PRESSED_WHEN_LOW, KeyEvent.KEYCODE_SPACE);
//        } catch (IOException e) {
//            Log.e(TAG, "Error driver");
//        }
//        service = PeripheralManager.getInstance();
//        try {
//            mLed = service.openGpio(LED_PIN_NAME);
//            mLed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
   class ExecuteAsyncTask extends AsyncTask<Object, Void, String> {


        //
        protected String doInBackground(Object... task_idx) {

            //

            if(checkButtonState(button)){
                led.setState(1);
            }else{
                led.setState(0);
            }
            //
            Log.d(TAG, "check state button: ");

            // stop
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //

            return "state checked";
        }


        //
        protected void onPostExecute(String result) {

        }

    }

    private boolean checkButtonState(GPIO gpop1) {
        if(gpop1.getState() == 1){
            return true;
        }else{
            return false;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mButtonInputDriver.register();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mButtonInputDriver.unregister();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            // Handle button pressed event
            setLedState(mLed, true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            // Handle button released event
            setLedState(mLed, false);
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }


    private void setLedState(Gpio led, boolean state) {
        try {
            led.setValue(state);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLed != null) {
            try {
                mLed.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on Peripheral API");
            }
        }
    }


}
