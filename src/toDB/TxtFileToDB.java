package toDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TxtFileToDB {

	public String fileToString(File f, String encoding) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding));
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str).append("\r\n");
			}
			return sb.toString();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Map<String,String>> stringIntoList(String str, String[] keys) {
		List<Map<String,String>> adrList = new ArrayList<>();
		String[] address = str.split("\r\n");
		for (int i=0;i<address.length;i++) {
			Map<String, String> map = new HashMap<>();
			String[] values = address[i].split("\\|"); 
			for(int j=0;j<keys.length;j++) {
				map.put(keys[i], values[i]);	
			}
			adrList.add(map);
		}
		return adrList;
	}
	public String listInsertIntoDB() {
		
		return null;
	}

	public static void main(String[] args) {
		TxtFileToDB tf = new TxtFileToDB();
		String key = "DONG_CODE\r\n" + "SIDO_NAME\r\n" + "SIGUGUN_NAME\r\n" + "UMD_NAME\r\n" + "LEE_NAME\r\n"
				+ "IS_MNT\r\n" + "BUNJI\r\n" + "HO\r\n" + "ROAD_NAME_CODE\r\n" + "IS_BASE\r\n" + "BULID_MAIN_BUN\r\n"
				+ "BULID_SUB_BUN\r\n" + "JIBUN_UNIQUE_CODE";
		String[] keys = key.split("\r\n");

		File path = new File("C:\\java_study\\address");
		if (path.isDirectory()) {
			File[] files = path.listFiles();
			for (File file : files) {
				if (!file.isDirectory() && file.getName().indexOf("jibun_") == 0) {
					long sTime = System.currentTimeMillis();
					System.out.println(file.getName() + ": 입력 시작!");
					List<Map<String,String>> valueList = tf.stringIntoList(tf.fileToString(file, "MS949"), keys);
					long eTime = System.currentTimeMillis();
					System.out.println(file.getName() + ": 입력 종료 ,실행 시간 : " + (eTime - sTime));
				}
			}
		}
	}

}
