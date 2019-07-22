package ca.uwaterloo.cs446.medaid.medaid;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
        import java.util.ArrayList;
        import java.util.UUID;


public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "MainActivity";
    private String userType;

    BluetoothAdapter mBluetoothAdapter;
    Button btnEnableDisable_Discoverable;

    BluetoothConnectionService mBluetoothConnection;

    Button btnStartConnection;
    Button btnSend;
    TextView txtInfo;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mBTDevice;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    public DeviceListAdapter mDeviceListAdapter;

    ListView lvNewDevices;


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };




    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.bluetooth_connections_block, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    //inside BroadcastReceiver4
                    mBTDevice = mDevice;
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
        //mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overlay_doctor_bluetooth_request);
        Button btnONOFF = (Button) findViewById(R.id.btnBluetoothOnOff);
        btnEnableDisable_Discoverable = (Button) findViewById(R.id.btnEnableDiscoverable);
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        mBTDevices = new ArrayList<>();

        SharePreferences sharePreferences = new SharePreferences(this);
        userType = sharePreferences.getPref("userType");

        btnStartConnection = (Button) findViewById(R.id.btnConnect);

        btnSend = (Button) findViewById(R.id.btnSendInfo);
        txtInfo = (TextView) findViewById(R.id.txtConnect);

        switch (userType) {
            case "0" :
                txtInfo.setText("Please connect to your doctor");
                break;
            case "1" :
                break;
            case "2" :
                txtInfo.setText("Please connect to your patient");
                break;
        }

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        lvNewDevices.setOnItemClickListener(BluetoothActivity.this);


        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: enabling/disabling bluetooth.");
                enableDisableBT();
            }
        });

        btnStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnection();
            }
        });

        switch (userType) {
            case "0" :
                // Single User
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Callback callback = new Callback() {
                            @Override
                            public void onValueReceived(String value) {
                                byte[] bytes = value.getBytes(Charset.defaultCharset());
                                if (mBluetoothConnection != null) {
                                    mBluetoothConnection.write(bytes);
                                }
                            }

                            @Override
                            public void onFailure() {

                            }
                        };

                        // TODO: Send patient info
                        String testText = "Hello, this is patient info";
                        DatabaseHelperModel dbHelperModel = new DatabaseHelperModel(getBaseContext());
                        dbHelperModel.getAllMedication(callback);
                        // dbHelperModel.getAllVacinations(callback);
                    }
                });
                break;
            case "1" :
                // Multi User
                break;
            case "2" :
                // Doctor
                btnSend.setText("WAITING FOR PATIENT TO SEND DATA");
                btnSend.setEnabled(false);
                break;
        }

    }

    //create method for starting connection
//***remember the conncction will fail and app will crash if you haven't paired first
    public void startConnection(){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device,uuid);
    }



    public void enableDisableBT(){
        if(mBluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }


    public void btnEnableDisable_Discoverable(View view) {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);

    }

    public void btnDiscover(View view) {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering()){

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.
        mBluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG, "Trying to pair with " + deviceName);
            mBTDevices.get(i).createBond();

            mBTDevice = mBTDevices.get(i);
            mBluetoothConnection = new BluetoothConnectionService(BluetoothActivity.this);
        }
    }

    public class BluetoothConnectionService {
        private static final String TAG = "BluetoothConnectionServ";

        private static final String appName = "MYAPP";

        private final UUID MY_UUID_INSECURE =
                UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

        private final BluetoothAdapter mBluetoothAdapter;
        Context mContext;

        private AcceptThread mInsecureAcceptThread;

        private ConnectThread mConnectThread;
        private BluetoothDevice mmDevice;
        private UUID deviceUUID;
        ProgressDialog mProgressDialog;

        private ConnectedThread mConnectedThread;

        public BluetoothConnectionService(Context context) {
            mContext = context;
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            start();
        }


        /**
         * This thread runs while listening for incoming connections. It behaves
         * like a server-side client. It runs until a connection is accepted
         * (or until cancelled).
         */
        private class AcceptThread extends Thread {

            // The local server socket
            private final BluetoothServerSocket mmServerSocket;

            public AcceptThread(){
                BluetoothServerSocket tmp = null;

                // Create a new listening server socket
                try{
                    tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                    Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
                }catch (IOException e){
                    Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
                }

                mmServerSocket = tmp;
            }

            public void run(){
                Log.d(TAG, "run: AcceptThread Running.");

                BluetoothSocket socket = null;

                try{
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    Log.d(TAG, "run: RFCOM server socket start.....");

                    socket = mmServerSocket.accept();

                    Log.d(TAG, "run: RFCOM server socket accepted connection.");

                }catch (IOException e){
                    Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
                }

                //talk about this is in the 3rd
                if(socket != null){
                    connected(socket,mmDevice);
                }

                Log.i(TAG, "END mAcceptThread ");
            }

            public void cancel() {
                Log.d(TAG, "cancel: Canceling AcceptThread.");
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
                }
            }

        }

        /**
         * This thread runs while attempting to make an outgoing connection
         * with a device. It runs straight through; the connection either
         * succeeds or fails.
         */
        private class ConnectThread extends Thread {
            private BluetoothSocket mmSocket;

            public ConnectThread(BluetoothDevice device, UUID uuid) {
                Log.d(TAG, "ConnectThread: started.");
                mmDevice = device;
                deviceUUID = uuid;
            }

            public void run(){
                BluetoothSocket tmp = null;
                Log.i(TAG, "RUN mConnectThread ");

                // Get a BluetoothSocket for a connection with the
                // given BluetoothDevice
                try {
                    Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                            +MY_UUID_INSECURE );
                    tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
                } catch (IOException e) {
                    Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
                }

                mmSocket = tmp;

                // Always cancel discovery because it will slow down a connection
                mBluetoothAdapter.cancelDiscovery();

                // Make a connection to the BluetoothSocket

                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    mmSocket.connect();

                    Log.d(TAG, "run: ConnectThread connected.");
                } catch (IOException e) {
                    // Close the socket
                    try {
                        mmSocket.close();
                        Log.d(TAG, "run: Closed Socket.");
                    } catch (IOException e1) {
                        Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                    }
                    Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE );
                }

                //will talk about this in the 3rd video
                connected(mmSocket,mmDevice);
            }
            public void cancel() {
                try {
                    Log.d(TAG, "cancel: Closing Client Socket.");
                    mmSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
                }
            }
        }



        /**
         * Start the chat service. Specifically start AcceptThread to begin a
         * session in listening (server) mode. Called by the Activity onResume()
         */
        public synchronized void start() {
            Log.d(TAG, "start");

            // Cancel any thread attempting to make a connection
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
            if (mInsecureAcceptThread == null) {
                mInsecureAcceptThread = new AcceptThread();
                mInsecureAcceptThread.start();
            }
        }

        /**
         AcceptThread starts and sits waiting for a connection.
         Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
         **/

        public void startClient(BluetoothDevice device,UUID uuid){
            Log.d(TAG, "startClient: Started.");

            //initprogress dialog
            mProgressDialog = ProgressDialog.show(mContext,"Connecting Bluetooth"
                    ,"Please Wait...",true);

            mConnectThread = new ConnectThread(device, uuid);
            mConnectThread.start();
        }

        /**
         Finally the ConnectedThread which is responsible for maintaining the BTConnection, Sending the data, and
         receiving incoming data through input/output streams respectively.
         **/
        private class ConnectedThread extends Thread {
            private final BluetoothSocket mmSocket;
            private final InputStream mmInStream;
            private final OutputStream mmOutStream;

            public ConnectedThread(BluetoothSocket socket) {
                Log.d(TAG, "ConnectedThread: Starting.");

                mmSocket = socket;
                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                //dismiss the progressdialog when connection is established
                try{
                    mProgressDialog.dismiss();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                try {
                    tmpIn = mmSocket.getInputStream();
                    tmpOut = mmSocket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mmInStream = tmpIn;
                mmOutStream = tmpOut;
            }

            public void run(){
                byte[] buffer = new byte[1024];  // buffer store for the stream

                int bytes; // bytes returned from read()

                // Keep listening to the InputStream until an exception occurs
                while (true) {
                    // Read from the InputStream
                    try {
                        bytes = mmInStream.read(buffer);
                        String incomingMessage = new String(buffer, 0, bytes);
                        Log.d(TAG, "InputStream: " + incomingMessage);
                        doctorViewPatientInfo(incomingMessage);
                        break;
                        // TODO: Deal with information given

                    } catch (IOException e) {
                        Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                        break;
                    }
                }
            }

            public void doctorViewPatientInfo(String incomingMessage) {
                Intent intent = new Intent(getBaseContext(), DoctorMainActivity.class);
                startActivity(intent);
                // TODO: Break out of
            }

            //Call this from the main activity to send data to the remote device
            public void write(byte[] bytes) {
                String text = new String(bytes, Charset.defaultCharset());
                Log.d(TAG, "write: Writing to outputstream: " + text);
                try {
                    mmOutStream.write(bytes);
                } catch (IOException e) {
                    Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
                }
            }

            /* Call this from the main activity to shutdown the connection */
            public void cancel() {
                try {
                    mmSocket.close();
                } catch (IOException e) { }
            }
        }

        private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
            Log.d(TAG, "connected: Starting.");

            // Start the thread to manage the connection and perform transmissions
            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        }

        /**
         * Write to the ConnectedThread in an unsynchronized manner
         *
         * @param out The bytes to write
         * @see ConnectedThread#write(byte[])
         */
        public void write(byte[] out) {
            // Create temporary object
            ConnectedThread r;

            // Synchronize a copy of the ConnectedThread
            Log.d(TAG, "write: Write Called.");
            //perform the write
            mConnectedThread.write(out);
        }

    }
}