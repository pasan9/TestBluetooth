package com.saucecode.testbluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Meter extends AppCompatActivity {

    Button btnOn,btnOff,btnDis;
    SeekBar brightness;
    String address =null;
    private ProgressDialog progress;
    BluetoothAdapter myBT = null;
    BluetoothSocket btSocket =null;
    private boolean isBTConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);

        //recieve address

        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS);

        //widgets

        btnOn = (Button)findViewById(R.id.btnOn);
        btnOff = (Button)findViewById(R.id.btnOff);
        btnDis = (Button)findViewById(R.id.btnDis);
        brightness = (SeekBar)findViewById(R.id.barBrightness);

        ConnectBT conn = new ConnectBT();
        conn.execute();

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnLed();
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOffLed();
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
            }
        });
    }


    private class ConnectBT extends AsyncTask<Void,Void,Void>{//UI thread

        private boolean ConnectSuccess =true;
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Meter.this, "Connecting.. ", "Please Wait");
        }
        @Override
        protected Void doInBackground(Void...devices){
            try{
                if(btSocket == null || !isBTConnected){

                    myBT = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBT.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();

                }
            }
            catch (IOException e){
                ConnectSuccess = false;

            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if(!ConnectSuccess){
                Toast.makeText(getApplicationContext(), "Connection Failed :( Is it SPP BT?", Toast.LENGTH_LONG).show();
                finish();
            }

            else{
                Toast.makeText(getApplicationContext(), "Connected :)", Toast.LENGTH_LONG).show();
                isBTConnected = true;
            }

            progress.dismiss();
        }

    }


private void Disconnect(){
    if(btSocket!=null)//if bt socket busy
    {
        try{

            btSocket.close(); //close conn

        }
        catch (IOException e){

            Toast.makeText(getApplicationContext(), "Error Closing Connection", Toast.LENGTH_LONG).show();

        }

        finish();//return
    }


}


private void turnOffLed(){
    if(btSocket!=null){
        try{
            btSocket.getOutputStream().write("2".toString().getBytes());
        }
        catch(IOException e){
            Toast.makeText(getApplicationContext(), "Error Turning On.", Toast.LENGTH_LONG).show();
        }

    }
}

private void turnOnLed(){
    if(btSocket!=null){
        try{
            btSocket.getOutputStream().write("1".toString().getBytes());
        }
        catch(IOException e){
            Toast.makeText(getApplicationContext(), "Error Turning Off.", Toast.LENGTH_LONG).show();
        }
    }
}












}


