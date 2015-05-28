package estudo.com.br.testebeacons;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.RemoteException;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.concurrent.TimeUnit;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

private BeaconManager beaconManager1 = new BeaconManager(this);
private BeaconManager beaconManager2 = new BeaconManager(this);
private BeaconManager beaconManager3 = new BeaconManager(this);
private BeaconManager beaconManager = new BeaconManager(this);
private static final String ice="ice";
private static final String mint="mint";
private static final String blueberry="blueberry";
private static final String flavor="Flavor: ";
private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
private static final Region ICE_ESTIMOTE_BEACONS = new Region("region1",ESTIMOTE_PROXIMITY_UUID,59941,46227);
private static final Region MINT_ESTIMOTE_BEACONS = new Region("region2",ESTIMOTE_PROXIMITY_UUID,8238,38423);
private static final Region BLUEBERRY_ESTIMOTE_BEACONS = new Region("region3",ESTIMOTE_PROXIMITY_UUID,28222,19199);
private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID,null,null);

    @ViewById(R.id.txBeaconBlueberry)
    TextView txBeaconBlueberry;

    @ViewById(R.id.txBeaconIce)
    TextView txBeaconIce;

    @ViewById(R.id.txBeaconMint)
    TextView txBeaconMint;

    @ViewById(R.id.LinearLayout1)
    LinearLayout activity_main;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EstimoteSDK.initialize(this, "app_0x92ljsn74","4777d092bdafa5bfa4aa6dc92766a3b8");
        EstimoteSDK.enableDebugLogging(true);
        beaconMonitoring();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,1234);
        } else {
            beaconStart();
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        try{
            /*beaconManager.stopMonitoring(ALL_ESTIMOTE_BEACONS);*/
            beaconManager1.stopMonitoring(ICE_ESTIMOTE_BEACONS);
            beaconManager2.stopMonitoring(MINT_ESTIMOTE_BEACONS);
            beaconManager3.stopMonitoring(BLUEBERRY_ESTIMOTE_BEACONS);
        }
        catch(RemoteException e){
            Log.e("EstimoTest","Something went wrong while stopping:",e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager1.disconnect();
        beaconManager2.disconnect();
        beaconManager3.disconnect();
    }

    public void beaconStart(){
        beaconManager1.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager1.startMonitoring(ICE_ESTIMOTE_BEACONS);
                    Log.d("EstimoTest", "beacon Manager 1 has start");
                } catch (RemoteException e) {
                    Log.e("EstimoTest", "May it's not possible start monitoring because:", e);
                }
            }
        });
        beaconManager2.connect( new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try{
                    beaconManager2.startMonitoring(MINT_ESTIMOTE_BEACONS);
                    Log.d("EstimoTest", "beacon Manager 2 has start");
                }catch(RemoteException e){
                    Log.e("EstimoTest", "May it's not possible start monitoring because:", e);
                }
            }
        });
        beaconManager3.connect( new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try{
                    beaconManager3.startMonitoring(BLUEBERRY_ESTIMOTE_BEACONS);
                    Log.d("EstimoTest", "beacon Manager 3 has start");
                }catch(RemoteException e){
                    Log.e("EstimoTest", "May it's not possible start monitoring because:", e);


                }
            }
        });
    }

    public void beaconMonitoring(){

        beaconManager1.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(0), 0);
        beaconManager2.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(0), 0);
        beaconManager3.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(0), 0);

        beaconManager1.setMonitoringListener(new BeaconManager.MonitoringListener() {
             @Override
             public void onEnteredRegion(Region region, List<Beacon> beacons) {
                 activity_main.setBackgroundColor(Color.parseColor("#08F0E0"));
                 txBeaconMint.setText(flavor + ice);
                 Log.d("EstimoTest", "Inside of Region 1");
             }

             @Override
             public void onExitedRegion(Region region) {
                 Log.d("EstimoTest", "Out of Region 1");
             }
        }
        );
        beaconManager2.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                activity_main.setBackgroundColor(Color.parseColor("#A2F008"));
                txBeaconMint.setText(flavor + mint);
                Log.d("EstimoTest", "Inside of Region 2");
            }

            @Override
            public void onExitedRegion(Region region) {
                Log.d("EstimoTest", "Out of Region 2");

            }
        });
        beaconManager3.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                activity_main.setBackgroundColor(Color.parseColor("#9708F0"));
                txBeaconMint.setText(flavor+blueberry);
                Log.d("EstimoTest", "Inside of Region 3");
            }

            @Override
            public void onExitedRegion(Region region) {
                Log.d("EstimoTest", "Out of Region 3");
            }
        });

    }
}
