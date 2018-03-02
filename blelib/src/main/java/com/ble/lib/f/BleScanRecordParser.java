package com.ble.lib.f;

import java.util.ArrayList;
import java.util.Arrays;
import android.text.TextUtils;
import com.ble.lib.util.CommonBleUtils;
/**
 * BLE广播数据解析。
 * @author E
 */
public class BleScanRecordParser {

	private static BleScanRecordParser bleScanRecordParser = null;
	
	public static BleScanRecordParser getInstance(){
		if (null == bleScanRecordParser) {
			bleScanRecordParser = new BleScanRecordParser();
		}
		return bleScanRecordParser;
	}
	
	/**
	 * 解析广播数据并返回结果。律动适用。
	 */
	public ScanRecordResult parseRecord(byte[] scanRecord){
		ArrayList<SubRecord> list = parseSubRecords(scanRecord);
		
		ScanRecordResult scanRecordResult = null;
		
		int size = list.size();
		if (6 == size) {
			SubRecord nameSubRecord = list.get(0);
			String name = CommonBleUtils.hexStr2Str(ByteArrayToString(nameSubRecord.data));
			
			SubRecord snSubRecord = list.get(5);
			String snString = ByteArrayToString(snSubRecord.data);
			
			snString = snString.replace("FFFF", "").trim();
			String sn = CommonBleUtils.hexStr2Str(snString).trim();
			
			scanRecordResult = new ScanRecordResult(name, sn);
		}
		if (null == scanRecordResult) {
			scanRecordResult = new ScanRecordResult();
		}
		return scanRecordResult;
	}
	
	/**
	 * 针对跑步机盒子。
	 * @param scanRecord
	 */
	public ScanRecordResult parseScanRecord(byte[] scanRecord){
		String result = ByteArrayToString(scanRecord);
		if (TextUtils.isEmpty(result)) {
			return new ScanRecordResult();
		}
		String flagString = "FFFFFF";
		if (!result.contains(flagString)) {
			return new ScanRecordResult();
		}
		int index = result.indexOf(flagString);
		
		String leftResult = result.substring(index + 6);
		
		if (TextUtils.isEmpty(leftResult) || leftResult.length() <= 2) {
			return new ScanRecordResult();
		}
		//后面的位数
		int devCodeLength = Integer.valueOf(leftResult.substring(0, 2) , 16);
		
		String leftResult_1 = leftResult.substring(2);
		
		if (TextUtils.isEmpty(leftResult_1) || leftResult_1.length() < devCodeLength*2) {
			return new ScanRecordResult();
		}
		
		ScanRecordResult scanRecordResult = new ScanRecordResult();
		
		String devCode = CommonBleUtils.hexStr2Str(leftResult_1.substring(0, devCodeLength*2)).trim();
		
		scanRecordResult.setDevCode(devCode);
		
		//sn
		String leftResult_2 = leftResult_1.substring(devCodeLength*2);
		if (TextUtils.isEmpty(leftResult_2) && leftResult_2.length() <=2) {
			return scanRecordResult;
		}
		//SN的位数
		int snLength = Integer.valueOf(leftResult_2.substring(0, 2) , 16);
		
		String leftResult_3 = leftResult_2.substring(2);
		if (TextUtils.isEmpty(leftResult_3) || leftResult_3.length() < snLength*2) {
			return scanRecordResult;
		}
		String sn = CommonBleUtils.hexStr2Str(leftResult_3.substring(0, snLength*2)).trim();
		
		scanRecordResult.setSn(sn);
		
		//Log.e("wx", "--广播信息--:   "+ devCodeLength + "  ::  "  + devCode  +  "  -   "+ snLength + " : " + sn);
		
		return scanRecordResult;
	}
	
	
	private ArrayList<SubRecord> parseSubRecords(byte[] scanRecord){
		ArrayList<SubRecord> list = new ArrayList<SubRecord>();
		int index = 0;
		while (index < scanRecord.length){
			int length = scanRecord[index++];
            //Done once we run out of records
            if (length == 0) break;
            int type = scanRecord[index];
            //Done if our record isn't a valid type
            if (type == 0) break;
            byte[] data = Arrays.copyOfRange(scanRecord, index+1, index+length);
            
            SubRecord subRecord = new SubRecord(length, type, data);
            
            list.add(subRecord);
            //Advance
            index += length;
		}
		return list;
	}
	
	public static String ByteArrayToString(byte[] data){
		StringBuilder builder = new StringBuilder();
		for (byte byteChar : data) {
			builder.append(String.format("%02X", byteChar));
		}
		return builder.toString();
	}	
	
	public class ScanRecordResult{
		private String name = null;
		private String sn = null;
		private String devCode = null;
		private String uuid = null;
		
		public ScanRecordResult() {
			super();
		}

		public ScanRecordResult(String name, String sn) {
			super();
			this.name = name;
			this.sn = sn;
		}
		
		public String getDevCode() {
			return devCode;
		}
		public void setDevCode(String devCode) {
			this.devCode = devCode;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSn() {
			return sn;
		}
		public void setSn(String sn) {
			this.sn = sn;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		@Override
		public String toString() {
			return "name: " + name + "   sn: " + sn + "   devCode: " + devCode;
		}
	}
	
	public class SubRecord{
		public int length = 0;
		public int type = 0;
		public byte[] data = null;
		
		public SubRecord(int length, int type, byte[] data) {
			super();
			this.length = length;
			this.type = type;
			this.data = data;
		}
	}

}
