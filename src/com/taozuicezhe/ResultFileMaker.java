package com.taozuicezhe;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ResultFileMaker {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */	
	
	private static final Logger log = Logger.getLogger(ResultFileMaker.class);
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if(args.length == 0){
			log.error("Your parameters mustn't be empty,we need" +
					" one para to choose whitch DB you want to connect to (Total surpport 3 DBs)-Please using 1,2,3 to change DB!");
		}else if(args.length>1){
			log.error("Only need one param here!You've used " +
					"more than one param, but using "+args.length+" params!");
		}else{
			ArrayList<String> fileNameList = new ArrayList<String>();
			List<ArrayList<String>> executedResultList = new ArrayList<ArrayList<String>>();
			Util util = (Util) Class.forName("com.taozuicezhe.Util").newInstance();
			fileNameList = (ArrayList<String>) util.getSourceSQLFileNameList();
			if(!fileNameList.isEmpty()){
				for(String sqlFileName:fileNameList){
					log.info("Begin to execute SQL in SQL file name="+sqlFileName);
					executedResultList = util.executeSQL(sqlFileName, Integer.valueOf(args[0]));
					if(!executedResultList.isEmpty()){						
						String outPutFileName = sqlFileName.substring(0, sqlFileName.length()-4)+".Result";
						util.writeSQLExecutingResultToFile(executedResultList,outPutFileName);
						executedResultList.clear();
					}else{
						log.warn("The SQL FILE name=["+sqlFileName+"] executed no return data records!");
					}
				}				
			}else{
				log.warn("The source SQL folder contains no SQL files,please check!");
			}
		}		
	}

}
