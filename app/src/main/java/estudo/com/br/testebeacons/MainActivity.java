package estudo.com.br.testebeacons;

import android.app.Activity;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
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

private BeaconManager beaconManager = new BeaconManager(this);
private BeaconManager beaconManager2 = new BeaconManager(this);
private BeaconManager beaconManager3 = new BeaconManager(this);
private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
private static final Region ICE_ESTIMOTE_BEACONS = new Region("regionId",ESTIMOTE_PROXIMITY_UUID,59941,46227);
private static final Region MINT_ESTIMOTE_BEACONS = new Region("regionId",ESTIMOTE_PROXIMITY_UUID,8238,38423);
private static final Region BLUEBERRY_ESTIMOTE_BEACONS = new Region("regionId",ESTIMOTE_PROXIMITY_UUID,28222,19199);

    @ViewById(R.id.txBeaconBlueberry)
    TextView txBeaconBlueberry;

    @ViewById(R.id.txBeaconIce)
    TextView txBeaconIce;

    @ViewById(R.id.txBeaconMint)
    TextView txBeaconMint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EstimoteSDK.initialize(this, "app_0x92ljsn74","4777d092bdafa5bfa4aa6dc92766a3b8");
        EstimoteSDK.enableDebugLogging(true);
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(0), 0);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                txBeaconIce.setText("Ice");
            }

            @Override
            public void onExitedRegion(Region region) {
                txBeaconIce.setText("No beacons here");
            }
        }

        );
        beaconManager2.setMonitoringListener(new BeaconManager.MonitoringListener() {
             @Override
             public void onEnteredRegion(Region region, List<Beacon> beacons) {
                 txBeaconMint.setText("Mint");
             }

             @Override
             public void onExitedRegion(Region region) {
                 txBeaconMint.setText("No beacons here");
             }
         });
        beaconManager3.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                txBeaconBlueberry.setText("Blueberry");
            }

            @Override
            public void onExitedRegion(Region region) {
                txBeaconBlueberry.setText("No beacons here");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        beaconManager.connect( new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try{
                    beaconManager.startMonitoring(ICE_ESTIMOTE_BEACONS);
                    Log.d("Estimoteste","startou 1");
                }catch(RemoteException e){
                    Log.e("Estimoteste1","Não foi possivel detectar beacons",e);
                }
            }
        });
        beaconManager2.connect( new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try{
                    beaconManager2.startMonitoring(MINT_ESTIMOTE_BEACONS);
                    Log.d("Estimoteste","startou 2");
                }catch(RemoteException e){
                    Log.e("Estimoteste","Não foi possivel detectar beacons",e);
                }
            }
        });
        beaconManager3.connect( new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try{
                    beaconManager3.startMonitoring(BLUEBERRY_ESTIMOTE_BEACONS);
                    Log.d("Estimoteste","startou 3");
                }catch(RemoteException e){
                    Log.e("Estimoteste","Não foi possivel detectar beacons",e);


                }
            }
        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        try{
            beaconManager.stopMonitoring(ICE_ESTIMOTE_BEACONS);
            beaconManager2.stopMonitoring(MINT_ESTIMOTE_BEACONS);
            beaconManager3.stopMonitoring(BLUEBERRY_ESTIMOTE_BEACONS);
        }
        catch(RemoteException e){
            Log.e("Estimoteste","Nao deu pra parar",e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
        beaconManager2.disconnect();
        beaconManager3.disconnect();
    }
}
