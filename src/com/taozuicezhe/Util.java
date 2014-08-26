package com.taozuicezhe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author shou
 *
 */

public class Util implements UtilInterface{
	private final Logger log = Logger.getLogger(Util.class);
	private final String sourceSQLFolderPath = System.getProperty("user.dir") + "/SQL/";
	private final String targetSQLResultFolderPath = System.getProperty("user.dir") + "/OutputResultFile/";
	private Connection connection = null;
	
	@Override
	public List<String> getSourceSQLFileNameList() {
		long startTime = System.nanoTime();
		List<String> sqlFileNameList = new ArrayList<String>();
		File file = new File(sourceSQLFolderPath);
		if(file.isDirectory()){
			String[] nameList = file.list();
			if(nameList != null){
				for(String name:nameList){
					sqlFileNameList.add(name);
				}
			}
		}
		long endTime = System.nanoTime() - startTime;
		log.info("Got "+sqlFileNameList.size()+" SQL files in SQL path!Time costing: "+endTime + " ns");
		return sqlFileNameList;
	}

	@Override
	public List<ArrayList<String>> executeSQL(String sqlFileName, int dbNumber) {
		List<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
//Set seperator as '~' - HardCode	
		String seperator = "~";
		Connection conn = null;
		String sql = "";
		String line = "";

		log.info("Start to read SQLs in each source SQL files... ...");
		log.info("Reading SQL File name=["+sqlFileName+"]");
//GET SQL
			try {
				FileReader fr = new FileReader(new File(sourceSQLFolderPath+"/"+sqlFileName));
				BufferedReader br = new BufferedReader(fr);
//SQL中可用REM和#去做注释				
				while ((line = br.readLine()) != null) {
					if (!line.toLowerCase().startsWith("rem"))
					{
						if (!line.startsWith("#"))
						{
							sql = sql + line;
						} 
					}
				}
				if (sql.endsWith(";")) {
					sql = sql.substring(0, sql.length() - 1);
					log.info("Finding SQL: " + sql);
				}else{
					log.info("Finding SQL: " + sql);
				}
				if(br != null){
					br.close();
				}
			} catch (IOException e1) {
				log.error(e1.getLocalizedMessage());
			}
			
//Execute SQL			
			if(conn ==  null){
				log.info("Begin to connect to NO."+dbNumber+" DataBase... ...");
				conn = getConnection(dbNumber);
				if(conn != null){
					log.info("Success to connect to NO."+dbNumber+" DataBase!");
					log.info("Start to execute SQL... ...");
					long startTime = System.currentTimeMillis();
					try {
						conn.setAutoCommit(false);
						PreparedStatement prstmt = conn.prepareStatement(sql);
						ResultSet rs = prstmt.executeQuery();
						ResultSetMetaData rsmd = rs.getMetaData();
						int numberOfColumns = rsmd.getColumnCount();
						while(rs.next()){
							List<String> lineList = new ArrayList<String>();
							for (int i = 1; i <= numberOfColumns; i++) {
								lineList.add(rs.getString(i)+seperator);	
							}
							resultList.add((ArrayList<String>) lineList);
						}
						conn.setAutoCommit(true);
						rs.close();
						prstmt.close();
						long endTime = System.currentTimeMillis() - startTime;
						log.info("Finish to execute SQL,time costing: "+endTime/1000+" s");
					} catch (SQLException e) {
						log.error(e.getLocalizedMessage());
					}
				}else{
					log.error("Failed to connect to NO."+dbNumber+" DataBase!");
				}
			}			

		if(conn != null){
			DBPool.closeConnection();
			try {
				if(conn.isClosed()){
					log.info("DB connection has closed!");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
		return resultList;
	}

	@Override
	public void writeSQLExecutingResultToFile(List<ArrayList<String>> result,String outputFileName) {
		FileWriter fstream;
		try {
			fstream = new FileWriter(targetSQLResultFolderPath+"/"+outputFileName,true);
			BufferedWriter writer = new BufferedWriter(fstream);
			for(ArrayList<String> lineList:result){
				for(String resultData:lineList){
					writer.write(resultData);
				}
				writer.write("\n");
			}
			if(writer != null){
				writer.close();
				log.info("["+targetSQLResultFolderPath+"/"+outputFileName + "] result file has built up!Under path="+targetSQLResultFolderPath);
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
	}
	
	private Connection getConnection(int number) {
		if(number == 1){
			return DBPool.connectToDb(1);
		}
		else if(number == 2){
			return DBPool.connectToDb(2);
		}
		else if(number == 3){
			return DBPool.connectToDb(3);
		}
		else{
			log.error("Please using 1,2,3 to choose DB connection!");
		}
		return this.connection;
	}
}
