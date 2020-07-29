package toDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import common.Connector;

public class TxtFileToDB
{

	public String fileToString(File f, String encoding)
	{
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding));
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = br.readLine()) != null)
			{
				sb.append(str).append("\r\n");
			}
			return sb.toString();
		} catch (UnsupportedEncodingException | FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public List<Map<String, String>> stringToList(String str, String[] keys)
	{
		List<Map<String, String>> adrList = new ArrayList<>();
		String[] address = str.split("\r\n");
		for (int i = 0; i < address.length; i++)
		{
			Map<String, String> map = new HashMap<>();
			String[] values = address[i].split("\\|");
			for (int j = 0; j < keys.length; j++)
			{
				map.put(keys[j], values[j]);
			}
			adrList.add(map);
		}
		return adrList;
	}

	public static void listInsertIntoDB(List<Map<String,String>> list,String[] keys) {
		Connection conn = Connector.getConnection();
		String sql = "insert into jibun_address(";
		String value = " values(";
		try	{
			int cnt = 0;
			PreparedStatement ps = null;
				for(int i=0;i<keys.length;i++) {
					sql = sql + keys[i]+","; 
					value = value + "?,";
				}
				sql = sql.substring(0,sql.length()-1)+")";
				value = value.substring(0,value.length()-1)+")";
				sql = sql + value;
				ps = conn.prepareStatement(sql);
				for(Map<String,String> map:list) {
					for(int i=0;i<keys.length;i++) {
						ps.setString((i+1), map.get(keys[i]));
					}
				ps.addBatch();
				cnt++;
				if(cnt%1000==0) {
					ps.executeBatch();
					ps.clearBatch();
					System.out.println(cnt);
					}
				}
			if(list.size()%1000!=0) {
				ps.executeBatch();
				}
			conn.commit();
			System.out.println(cnt+" 건 입력완료"); 
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)	{
		TxtFileToDB tf = new TxtFileToDB();
		String key = "DONG_CODE\r\n" + "SIDO_NAME\r\n" + "SIGUGUN_NAME\r\n" + "UMD_NAME\r\n" + "LEE_NAME\r\n"
				+ "IS_MNT\r\n" + "BUNJI\r\n" + "HO\r\n" + "ROAD_NAME_CODE\r\n" + "IS_BASE\r\n" + "BULID_MAIN_BUN\r\n"
				+ "BULID_SUB_BUN\r\n" + "JIBUN_UNIQUE_CODE";
		String[] keys = key.split("\r\n");

		File path = new File("C:\\java_study\\address");
		if (path.isDirectory())	{
			File[] files = path.listFiles();
			for (File file : files)	{
				if (!file.isDirectory() && file.getName().indexOf("jibun_") == 0){
					long sTime = System.currentTimeMillis();
					System.out.println(file.getName() + ": 입력 시작!");
					List<Map<String, String>> valueList = tf.stringToList(tf.fileToString(file, "MS949"), keys);
					listInsertIntoDB(valueList,keys);
					long eTime = System.currentTimeMillis();
					System.out.println(file.getName() + ": 입력 종료 ,실행 시간 : " + (eTime - sTime));
				}
			}
		}
	}

}
