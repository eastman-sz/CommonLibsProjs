package com.libs.module.ble.dfu;

import android.content.Context;
import android.net.Uri;
import com.ble.lib.dfu.Ld2Uuids;
import com.common.libs.util.ILog;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;
/**
 * @author E
 */
public class DfuActivityHelper {
	
	private Context context = null;
	
	private String deviceAddress = "E3:81:F6:10:DA:64";//"F0:10:00:82:2E:75"
	private String deviceName = "LD DFU";
	private String mFilePath = "/storage/emulated/0/tencent/QQfile_recv/z105V29.bin"; ///storage/sdcard1/Tencent/QQfile_recv/w309releasev00.12.bin
	//private String mFilePath = "/storage/emulated/0/tencent/QQfile_recv/f597f0190f59b570b6178dad4290c6a8.bin"; ///storage/sdcard1/Tencent/QQfile_recv/w309releasev00.12.bin
//	private String mFilePath = "storage/sdcard1/Tencent/QQfile_recv/z105V29.12.bin";
	private Uri mFileStreamUri = null;
	private String mInitFilePath = null;
	private Uri mInitFileStreamUri = null;
	private int mFileType = 4;
//	private int mFileTypeTmp = 0; // This value is being used when user is selecting a file not to overwrite the old value (in case he/she will cancel selecting file)
	
	public DfuActivityHelper(Context context) {
		super();
		this.context = context;
	}

	private final DfuProgressListener mDfuProgressListener = new DfuProgressListener() {
		@Override
		public void onProgressChanged(String deviceAddress, int percent,
				float speed, float avgSpeed, int currentPart, int partsTotal) {
			ILog.e("--onProgressChanged--: " + deviceAddress + "  :  " + percent);
			
			if (null != dfuListener) {
				dfuListener.onProgressChanged(deviceAddress, percent, speed, avgSpeed, currentPart, partsTotal);
			}
		}
		@Override
		public void onFirmwareValidating(String deviceAddress) {
			ILog.e("--onFirmwareValidating--: " + deviceAddress);
			
			if (null != dfuListener) {
				dfuListener.onFirmwareValidating(deviceAddress);
			}
		}
		@Override
		public void onError(String deviceAddress, int error, int errorType,
				String message) {
			ILog.e("--onError--: " + deviceAddress + " :  "+ message);
			
			if (null != dfuListener) {
				dfuListener.onError(deviceAddress, error, errorType, message);
			}
		}
		@Override
		public void onEnablingDfuMode(String deviceAddress) {
			ILog.e("--onEnablingDfuMode--: " + deviceAddress);
			
			if (null != dfuListener) {
				dfuListener.onEnablingDfuMode(deviceAddress);
			}
		}
		@Override
		public void onDfuProcessStarting(String deviceAddress) {
			ILog.e("--onDfuProcessStarting--: " + deviceAddress);
			
			if (null != dfuListener) {
				dfuListener.onDfuProcessStarting(deviceAddress);
			}
		}
		@Override
		public void onDfuProcessStarted(String deviceAddress) {
			ILog.e("--onDfuProcessStarted--: " + deviceAddress);
			
			if (null != dfuListener) {
				dfuListener.onDfuProcessStarted(deviceAddress);
			}
		}
		@Override
		public void onDfuCompleted(String deviceAddress) {
			ILog.e("--onDfuCompleted--: " + deviceAddress);
			
			if (null != dfuListener) {
				dfuListener.onDfuCompleted(deviceAddress);
			}
		}
		@Override
		public void onDfuAborted(String deviceAddress) {
			ILog.e("--onDfuAborted--: " + deviceAddress);
			
			if (null != dfuListener) {
				dfuListener.onDfuAborted(deviceAddress);
			}
		}
		@Override
		public void onDeviceDisconnecting(String deviceAddress) {
			ILog.e("--onDeviceDisconnecting--: " + deviceAddress);
			
			if (null != dfuListener) {
				dfuListener.onDeviceDisconnecting(deviceAddress);
			}
		}
		@Override
		public void onDeviceDisconnected(String deviceAddress) {
			ILog.e("--onDeviceDisconnected--: " + deviceAddress);
			
			if (null != dfuListener) {
				dfuListener.onDeviceDisconnected(deviceAddress);
			}
		}
		@Override
		public void onDeviceConnecting(String deviceAddress) {
			ILog.e("--onDeviceConnecting--: " + deviceAddress);
			
			if (null != dfuListener) {
				dfuListener.onDeviceConnecting(deviceAddress);
			}
		}
		@Override
		public void onDeviceConnected(String deviceAddress) {
			ILog.e("--onDeviceConnected--: " + deviceAddress);
			
			if (null != dfuListener) {
				dfuListener.onDeviceConnected(deviceAddress);
			}
		}
	};
	
	public void onResume(){
		DfuServiceListenerHelper.registerProgressListener(context, mDfuProgressListener);
	}
	
	public void onPause(){
		DfuServiceListenerHelper.unregisterProgressListener(context, mDfuProgressListener);
	}
	
	public void startDfuSteps(){
		//取得DFU的Bin文件
		startDfuSerVice();
	}
	
	@SuppressWarnings("deprecation")
	private void startDfuSerVice(){
		DfuServiceInitiator starter = new DfuServiceInitiator(deviceAddress)
		        .setDeviceName(deviceName)
		        .setKeepBond(false);
                starter.setBinOrHex(mFileType, mFileStreamUri, mFilePath)
                .setDfuServiceParams(Ld2Uuids.SP102_DFU_SERVICE_UUID , Ld2Uuids.SP102_DFU_CONTROL_POINT_UUID , Ld2Uuids.SP102_DFU_PACKET_UUID)
		        .setInitFile(mInitFileStreamUri, mInitFilePath);
		;
		starter.start(context, DfuService.class);
	}
	
	//DFU过程的监听
	public DFUListener dfuListener = null;
	
	public void setDfuListener(DFUListener dfuListener) {
		this.dfuListener = dfuListener;
	}

	//DFU过程的监听
    public interface DFUListener {
        void onProgressChanged(String deviceAddress, int percent,
				float speed, float avgSpeed, int currentPart, int partsTotal) ;
        void onFirmwareValidating(String deviceAddress) ;
        void onError(String deviceAddress, int error, int errorType,String message) ;
        void onEnablingDfuMode(String deviceAddress) ;
        void onDfuProcessStarting(String deviceAddress) ;
        void onDfuProcessStarted(String deviceAddress);
        void onDfuCompleted(String deviceAddress) ;
        void onDfuAborted(String deviceAddress);
        void onDeviceDisconnecting(String deviceAddress);
        void onDeviceDisconnected(String deviceAddress) ;
        void onDeviceConnecting(String deviceAddress) ;
        void onDeviceConnected(String deviceAddress);
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getmFilePath() {
		return mFilePath;
	}
	public void setmFilePath(String mFilePath) {
		this.mFilePath = mFilePath;
	}
}
