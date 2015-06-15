package com.example.eric.myapplication;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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


public class MainActivity extends ActionBarActivity implements BeaconConsumer {
    private final String TAG = "MainActivity";

    private final String THE_BEACON = "theBeacon";
    private final String THE_BEACON_DISTANCE = "theBeaconDistance";

    private final String ID1 = "id1";
    private final String ID2 = "id2";
    private final String ID3 = "id3";

    private final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private BeaconManager beaconManager;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEditor;

    private TextView tv2;

    private int mId = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        prefEditor = prefs.edit();

        String id1 = prefs.getString(ID1, "");
        String id2 = prefs.getString(ID2, "");
        String id3 = prefs.getString(ID3, "");
        String str = "id1: " + id1 + " id2: " + id2 + " id3: " + id3;
        Toast.makeText(this, "Last beacon was " + str, Toast.LENGTH_SHORT).show();

        beaconManager = BeaconManager.getInstanceForApplication(this);

        Log.w(TAG, "is the beacon manager bound?" + beaconManager.isAnyConsumerBound());

        List<BeaconParser> parsers = beaconManager.getBeaconParsers();
        BeaconParser iBeaconParser = new BeaconParser();
        iBeaconParser.setBeaconLayout(IBEACON_LAYOUT);
        parsers.add(iBeaconParser);
        // parsers.add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        beaconManager.bind(this);

        tv2 = (TextView) this.findViewById(R.id.TxtBeaconDistance);

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

//    @Override
//    public void onROXIMITYEngineStarted() {
//        Log.i("main", "started");
//    }
//
//    @Override
//    public void onROXIMITYEngineStopped() {
//        Log.i("main", "stopped");
//    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        if (beaconManager.isBound(this)) {
            //  Log.i(TAG, "bound");
            beaconManager.setBackgroundMode(false);
        }

//        TextView tv = (TextView) this.findViewById(R.id.TxtBeaconId);
//        if (!prefs.getString(THE_BEACON, "").isEmpty()) {
//            tv.setText(prefs.getString(THE_BEACON, ""));
//        }
//
//        if (!prefs.getString(THE_BEACON_DISTANCE, "").isEmpty()) {
//            tv2.setText(prefs.getString(THE_BEACON_DISTANCE, ""));
//        }

//        Log.i(TAG, "ROXIMITY Engine running: " + ROXIMITYEngine.isROXIMITYEngineRunning());
//
//        if (getIntent().hasExtra(ROXConsts.EXTRA_MESSAGE_DATA)){
//            Log.d(TAG, "Activity launched with ROXIMITY intent containing message data: " + this.getIntent().getStringExtra(ROXConsts.EXTRA_MESSAGE_DATA));
//        }
    }

//    private void createBroadcastRecievers() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ROXConsts.MESSAGE_FIRED);
//        intentFilter.addAction(ROXConsts.BEACON_RANGE_UPDATE);
//        intentFilter.addAction(ROXConsts.WEBHOOK_POSTED);
//
//        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
//    }
//
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(ROXConsts.MESSAGE_FIRED)) {
//                MessageParcel messageParcel = (MessageParcel) intent.getParcelableExtra(ROXConsts.EXTRA_MESSAGE_PARCEL);
//                handleMessageFired(messageParcel);
//            } else if (intent.getAction().equals(ROXConsts.BEACON_RANGE_UPDATE)) {
//                String rangeJson = intent.getStringExtra(ROXConsts.EXTRA_RANGE_DATA);
//                // handleBeaconRangeUpdate(rangeJson);
//                Object triggerData = intent.getExtras().get(ROXConsts.EXTRA_MESSAGE_TRIGGER);
//                Log.i("main", "trigger data is " + triggerData.getClass());
//            } else if (intent.getAction().equals(ROXConsts.WEBHOOK_POSTED)) {
//                String webhookJson = intent.getStringExtra(ROXConsts.EXTRA_BROADCAST_JSON);
//                handleWebhookPosted(webhookJson);
//            }
//        }
//    };
//
//
//    public void handleMessageFired(MessageParcel messageParcel) {
//        try {
//            Log.i(TAG, "Message fired:" + messageParcel.show(this));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void handleWebhookPosted(String webhookJson) {
//        Log.d(TAG, "Webhook posted: " + webhookJson);
//    }
//
//
//    public void handleBeaconRangeUpdate(String rangeUpdate) {
//        Log.i(TAG, "Received a beacon range update:" + rangeUpdate);
//    }

    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG, "onBeaconServiceConnect");

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(final Region region) {
                Log.i(TAG, "I just saw an beacon for the first time! " + region.toString());
                // Toast.makeText(MainActivity.this, "I just saw an beacon for the first time!", Toast.LENGTH_LONG).show();
//                prefEditor.putString(THE_BEACON, region.toString());
//                prefEditor.commit();

                showNotification(region, "Welcome to our shop", "This is the nth time you have been to our shop");
            }

            @Override
            public void didExitRegion(final Region region) {
                Log.i(TAG, "I no longer see an beacon");
                // Toast.makeText(MainActivity.this, "I no longer see an beacon", Toast.LENGTH_LONG).show();
//                prefEditor.putString(THE_BEACON, "");
//                prefEditor.commit();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        TextView tv2 = (TextView) MainActivity.this.findViewById(R.id.TxtBeaconId);
//                        tv2.setText("Beacon "+region.toString() + " is gone");
//                    }
//                });
                showNotification(region, "Bye bye", "Hope to see you again soon");
            }

            @Override
            public void didDetermineStateForRegion(int state, final Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
                Toast.makeText(MainActivity.this, "I have just switched from seeing/not seeing beacons: " + state, Toast.LENGTH_LONG).show();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        TextView tv2 = (TextView) MainActivity.this.findViewById(R.id.TxtBeaconId);
//                        tv2.setText("Beacon "+region.toString() + " changes visibility");
//                    }
//                });
            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    final Beacon beacon = beacons.iterator().next();
                    prefEditor.putString(THE_BEACON_DISTANCE, "" + beacon.getDistance());
                    prefEditor.commit();

                    Log.i(TAG, "The first beacon I see is about " + beacon.getDistance() + " meters away." + beacon.getId1() + ";" + beacon.getId2() + ";" + beacon.getId3());
                    if (beacon.getDistance() < 1.0) {
                        Log.i(TAG, "beacon is intermediate");
                        Toast.makeText(MainActivity.this, "beacon is closer than 1m", Toast.LENGTH_SHORT).show();
                        showNotification(region, "Special promotion 50% off", "Product ABC until tomorrow only");
                    } else {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                TextView tv2 = (TextView) MainActivity.this.findViewById(R.id.TxtBeaconDistance);
//                                tv2.setText("Distance: " + beacon.getDistance() + "m");
//                            }
//                        });
                        Log.i(TAG, "beacon is not near");
                        showNotification(region, "", "Distance: "+beacon.getDistance());
                    }

                    // Toast.makeText(MainActivity.this, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.", Toast.LENGTH_LONG).show();
                }
            }
        });

        try {
            Region region;
            // Region region = new Region("com.example.eric.myapplication", Identifier.parse("144e71a8-e817-dd8e-488f-29c742921b49"), Identifier.parse("27323"), Identifier.parse("64474"));
            String id1 = prefs.getString(ID1, "");
            String id2 = prefs.getString(ID2, "");
            String id3 = prefs.getString(ID3, "");

            if (!id1.isEmpty() && !id2.isEmpty() && !id3.isEmpty()) {
                region = new Region("com.example.eric.myapplication", Identifier.parse(prefs.getString(ID1, "")), Identifier.parse(prefs.getString(ID2, "")), Identifier.parse(prefs.getString(ID3, "")));
            } else {
                region = new Region("com.example.eric.myapplication", null, null, null);
            }
            // beaconManager.startMonitoringBeaconsInRegion(region);
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        } catch (Exception e1) {
            Log.e(TAG, "", e1);
        }
    }

    private void showNotification(final Region region, final String title, final String msg) {
        Log.i(TAG, "showNotification");
        if (!isAppOnForeground()) {
            Log.w(TAG, "app is not in foreground, showing notification");
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.stat_notify_chat)
                            .setContentTitle(title)
                            .setContentText(msg);
// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(mId, mBuilder.build());
        } else {
            Log.w(TAG, "app is in foreground, not showing notification");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tv2 = (TextView) MainActivity.this.findViewById(R.id.TxtBeaconId);
                    tv2.setText(title + "\n" + msg);
                }
            });
        }
    }

    private boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(this.getPackageName().toString())) {
            isActivityFound = true;
        }

        if (isActivityFound) {
            return true;
        } else {
            // write your code to build a notification.
            // return the notification you built here
            return false;
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
        if (beaconManager.isBound(this)) {
            // Log.i(TAG, "bound");

            beaconManager.setBackgroundMode(true);
        }
    }
}
