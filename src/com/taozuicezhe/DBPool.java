package com.taozuicezhe;

import java.sql.Connection;
import org.apache.log4j.Logger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBPool {
	private static final Logger log = Logger.getLogger(DBPool.class);
	private static ComboPooledDataSource ds = null;  	  
    private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
    
    public static Connection connectToDb(int number){
    	Connection conn = null;
    	if(number<0 || number >3){
    		log.error("Please using 1,2,3 to select what DB in c3p0-config.xml you want to use!");
    	}
    	switch(number){
    	case 1:
    		conn = connectToDB1();
    		break;
    	case 2:
    		conn = connectToDB2();
    		break;
    	case 3:
    		conn = connectToDB3();
    		break;
    		default:
    			break;
    	}
    	return conn;
    }
    
    public static Connection closeConnection() {         
    	try {  
            Connection conn = tl.get();    
            conn.close();   
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } finally {             
        	tl.remove();   
        }
		return null;
    }
        
    private static void iniDbConfig(String DbName){
    	long startTime = System.nanoTime();
    	if(ds == null)
    	ds = new ComboPooledDataSource(DbName);
    	long endTime = System.nanoTime() - startTime;
    	log.info(DbName + " initialize finished, total cost [" + endTime / (1000 * 1000) + "]ms");
    }
    
    private static Connection connectToDB1(){
        Connection connDB1 = tl.get();  
    	try {
			iniDbConfig("DataBase1");
			connDB1 = ds.getConnection();  
            tl.set(connDB1);  
            return connDB1;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    
    private static Connection connectToDB2(){
        Connection connDB2 = tl.get();  
    	try {
			iniDbConfig("DataBase2");
			connDB2 = ds.getConnection();  
            tl.set(connDB2);  
            return connDB2;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    
    private static Connection connectToDB3(){
        Connection connDB3 = tl.get();  
    	try {
			iniDbConfig("DataBase3");
			connDB3 = ds.getConnection();  
            tl.set(connDB3);  
            return connDB3;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
}
