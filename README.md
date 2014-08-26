SQLResultMaker
==============
What dose this small tool used for?
We can execute a batch of source SQLs under folder named SQL and export the DB records to a .RESULT file 
under path 'OutputResultFile',just switching DB connection by using parameter 1,2,3

How to config the DB connection?
Currently, we only make a configuration it in the prepared executable JAR file, and change DB infos in the c3p0-config.xml under CONFIG folder.
Now,this tool only support three kinds of DB/DWH -- SQL-SERVER(using jtds from sourceforge) VERTICA NETEZZA,if you want to change it,just add new 
JDBC driver of new DB type and change the "driverClass" into new driver class name and re-generate a new executebal JAR file.

How to run this small tool?
You can try it by copy the ZIP file named 'SQLResultFileMaker.zip' , unzip it and double click the bat file, or using 
'java -jar SQLResultFileMaker.jar 1', in fact you have to pull the source code and change the DB config and import the 
new JDBC whith you want to use and meet with your requirements,re-package a new JAR file.


What will do next time?
This small tool is the part of my 'DATA-Comparing', it's just a beginning, to be continued... ...
