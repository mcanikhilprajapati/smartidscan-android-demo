package ch.smartidlab.smartidscandemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import ch.icare.smartscan.CanvasDrawer;
import ch.icare.smartscan.DecodedResult;
import ch.icare.smartscan.OnDecoderResultFoundListener;
import ch.icare.smartscan.SmartScanner;

public class MainActivityFragment extends Fragment {

    SmartScanner mSmartScanner;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // don't forget to handle camera permission before loading SmartIdScan
        // depending on what API level you target
        mSmartScanner = new SmartScanner(getContext());
        mSmartScanner.setVibrateOn(true);
        mSmartScanner.setContinuousDecoding(false); // Stop decoding process when something is found
        mSmartScanner.setDecodingType(SmartScanner.CODE_1D2D_ALL); // Don't forget to set this accordingly to the decoding lib capabilities
        mSmartScanner.setOnResultListener(new OnDecoderResultFoundListener() {
            @Override
            public void onResultFound(DecodedResult result) {
                Log.d("MainActivityFragment",  "code found : " + result.getResultString());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(result.getResultString())
                        .setTitle(R.string.found_title)
                        .setPositiveButton(android.R.string.ok, null)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mSmartScanner.restartDecoding(); // restart decoding process if setContinuousDecoding was set to false.
                            }
                        })
                        .setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        mSmartScanner.setCanvasDrawer((CanvasDrawer) root.findViewById(R.id.canvasdrawer)); // target
        mSmartScanner.setSurfaceView((SurfaceView) root.findViewById(R.id.surfaceview)); // camera preview
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // initialize the SmartScanner library
        mSmartScanner.init();
    }

    @Override
    public void onPause() {
        // release resources held by the SmartScanner library before super call
        mSmartScanner.release();
        super.onPause();
    }
}
