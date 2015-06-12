package com.example.eric.myapplication;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.roximity.sdk.ROXIMITYEngineListener;
import com.roximity.sdk.external.ROXConsts;
import com.roximity.sdk.messages.MessageParcel;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONException;

import java.util.Collection;
import java.util.List;


public class MainActivity extends ActionBarActivity implements ROXIMITYEngineListener, BeaconConsumer {
    private final String TAG = "MainActivity";

    private final String THE_BEACON = "theBeacon";
    private final String THE_BEACON_DISTANCE = "theBeaconDistance";

    private BeaconManager beaconManager;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        prefEditor = prefs.edit();

        beaconManager = BeaconManager.getInstanceForApplication(this);

        Log.w(TAG, "is the beacon manager bound?" + beaconManager.isAnyConsumerBound());

        List<BeaconParser> parsers = beaconManager.getBeaconParsers();
        BeaconParser iBeaconParser = new BeaconParser();
        iBeaconParser.setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        parsers.add(iBeaconParser);
        // parsers.add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        beaconManager.bind(this);

//        BluetoothManager btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
//
//        BluetoothAdapter btAdapter = btManager.getAdapter();
//        if (btAdapter != null && !btAdapter.isEnabled()) {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent,1);
//        }

//        HashMap<String, Object> options = new HashMap<String, Object>();


//        options.put(ROXConsts.ENGINE_OPTIONS_START_LOCATION_DEACTIVATED, true);
//
//        try {
//            ROXIMITYEngine.startEngineWithOptions(this.getApplicationContext(), R.drawable.ic_launcher, options, this, null);
//            ROXIMITYEngine.activateLocation();
//            Log.i("main", "is location activated " + ROXIMITYEngine.isLocationActivated());
//
//            createBroadcastRecievers();
//        } catch (MissingApplicationIdException e) {
//            e.printStackTrace();
//        } catch (IncorrectContextException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesMissingException e) {
//            e.printStackTrace();
//        }


//        try {
//            btAdapter.startLeScan(leScanCallback);
//
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//        }
    }

//    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
//        @Override
//        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
//            Log.i(TAG, "onLeScan");
//            BluetoothGatt bluetoothGatt = bluetoothDevice.connectGatt(context, false, btleGattCallback);
//        }
//    };

    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // this will get called anytime you perform a read or write characteristic operation
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            // this will get called when a device connects or disconnects
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            // this will get called after the client initiates a            BluetoothGatt.discoverServices() call
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onROXIMITYEngineStarted() {
        Log.i("main", "started");
    }

    @Override
    public void onROXIMITYEngineStopped() {
        Log.i("main", "stopped");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (beaconManager.isBound(this)) {
            Log.i(TAG, "bound");
            beaconManager.setBackgroundMode(false);
        }

        TextView tv = (TextView) this.findViewById(R.id.TxtBeaconId);
        if (!prefs.getString(THE_BEACON, "").isEmpty()) {
            tv.setText(prefs.getString(THE_BEACON, ""));
        }
        TextView tv2 = (TextView) this.findViewById(R.id.TxtBeaconDistance);
        if (!prefs.getString(THE_BEACON_DISTANCE, "").isEmpty()) {
            tv2.setText(prefs.getString(THE_BEACON_DISTANCE, ""));
        }

//        Log.i(TAG, "ROXIMITY Engine running: " + ROXIMITYEngine.isROXIMITYEngineRunning());
//
//        if (getIntent().hasExtra(ROXConsts.EXTRA_MESSAGE_DATA)){
//            Log.d(TAG, "Activity launched with ROXIMITY intent containing message data: " + this.getIntent().getStringExtra(ROXConsts.EXTRA_MESSAGE_DATA));
//        }
    }

    private void createBroadcastRecievers() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ROXConsts.MESSAGE_FIRED);
        intentFilter.addAction(ROXConsts.BEACON_RANGE_UPDATE);
        intentFilter.addAction(ROXConsts.WEBHOOK_POSTED);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ROXConsts.MESSAGE_FIRED)) {
                MessageParcel messageParcel = (MessageParcel) intent.getParcelableExtra(ROXConsts.EXTRA_MESSAGE_PARCEL);
                handleMessageFired(messageParcel);
            } else if (intent.getAction().equals(ROXConsts.BEACON_RANGE_UPDATE)) {
                String rangeJson = intent.getStringExtra(ROXConsts.EXTRA_RANGE_DATA);
                // handleBeaconRangeUpdate(rangeJson);
                Object triggerData = intent.getExtras().get(ROXConsts.EXTRA_MESSAGE_TRIGGER);
                Log.i("main", "trigger data is " + triggerData.getClass());
            } else if (intent.getAction().equals(ROXConsts.WEBHOOK_POSTED)) {
                String webhookJson = intent.getStringExtra(ROXConsts.EXTRA_BROADCAST_JSON);
                handleWebhookPosted(webhookJson);
            }
        }
    };


    public void handleMessageFired(MessageParcel messageParcel) {
        try {
            Log.i(TAG, "Message fired:" + messageParcel.show(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleWebhookPosted(String webhookJson) {
        Log.d(TAG, "Webhook posted: " + webhookJson);
    }


    public void handleBeaconRangeUpdate(String rangeUpdate) {
        Log.i(TAG, "Received a beacon range update:" + rangeUpdate);
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG, "onBeaconServiceConnect");

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time! "+region.toString());
                // Toast.makeText(MainActivity.this, "I just saw an beacon for the first time!", Toast.LENGTH_LONG).show();
                prefEditor.putString(THE_BEACON, region.toString());
                prefEditor.commit();
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                // Toast.makeText(MainActivity.this, "I no longer see an beacon", Toast.LENGTH_LONG).show();
                prefEditor.putString(THE_BEACON, "");
                prefEditor.commit();
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
                Toast.makeText(MainActivity.this, "I have just switched from seeing/not seeing beacons: " + state, Toast.LENGTH_LONG).show();
            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Beacon beacon = beacons.iterator().next();
                    prefEditor.putString(THE_BEACON_DISTANCE, ""+beacon.getDistance());
                    prefEditor.commit();
                    Log.i(TAG, "The first beacon I see is about " + beacon.getDistance() + " meters away." + beacon.getId1() + ";" + beacon.getId2() + ";" + beacon.getId3());
                    // Toast.makeText(MainActivity.this, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.", Toast.LENGTH_LONG).show();
                }
            }
        });

        try {
            Region region = new Region("com.example.eric.myapplication", Identifier.parse("144e71a8-e817-dd8e-488f-29c742921b49"), Identifier.parse("27323"), Identifier.parse("64474"));
            beaconManager.startMonitoringBeaconsInRegion(region);
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Exception e1) {
            Log.e(TAG, "", e1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) {
            Log.i(TAG, "bound");

            beaconManager.setBackgroundMode(true);
        }
    }
}
