package com.saucecode.testbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button btnPaired;
    ListView pairedList ;

    //bluetooth

    private BluetoothAdapter myBT = null;
    private Set<BluetoothDevice> pairedDevices; // an array of bluetooth devices
    private OutputStream outStream = null;
    public static String EXTRA_ADDRESS = "device_address";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPaired = (Button)findViewById(R.id.btnPaired);
        pairedList = (ListView)findViewById(R.id.pairedList);


        //check if the device has bluetooth
        myBT = BluetoothAdapter.getDefaultAdapter();

        if(myBT == null){
            //Toast a message : No bluetooth adapter
            Toast.makeText(getApplicationContext(), "This device does not have a bluetooth adapter", Toast.LENGTH_LONG).show();

            //close program

        }

        else {
            if(myBT.isEnabled()){

            }

            else{
                //ask to turn on bluetooth
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);

            }
        }


        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Came Here1.", Toast.LENGTH_LONG).show();
                pairedDevicesList();

            }
        });


    }







    private void pairedDevicesList(){

        pairedDevices = myBT.getBondedDevices();
        ArrayList list = new ArrayList();

        Toast.makeText(getApplicationContext(), "Came Hee2.", Toast.LENGTH_LONG).show();

        if(pairedDevices.size()>0){

            Toast.makeText(getApplicationContext(), "Came Here.", Toast.LENGTH_LONG).show();

            for(BluetoothDevice bt:pairedDevices){ //loop through the paired list

                list.add(bt.getName()+"\n"+bt.getAddress()); // get device name and address


            }
        }

        else{
            //print a message : No paired devices
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        pairedList.setAdapter(adapter);
        pairedList.setOnItemClickListener(myListClickListener); //called when one of the devices is clicked



    }

   private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener(){
       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //Getting the mac address : The last 17 Chars in the view

           String info = ((TextView) view).getText().toString();
           String address = info.substring(info.length()-17);

           //create intent to start next activity here

           Intent i =new Intent(MainActivity.this,Meter.class);

           //change the activity

           i.putExtra(EXTRA_ADDRESS,address); //meter will recieve

           startActivity(i);

       }
   };









}
