package com.taozuicezhe;

import java.util.ArrayList;
import java.util.List;

public interface UtilInterface {
	List<String> getSourceSQLFileNameList();
	void writeSQLExecutingResultToFile(List<ArrayList<String>> result,String outPutFileName);
	List<ArrayList<String>> executeSQL(String sqlFileName, int dbNumber);
}
