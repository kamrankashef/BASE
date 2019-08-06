#!/usr/bin/env groovy

import java.lang.String
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import groovy.sql.Sql
import java.io.File
import java.io.FileWriter
import java.io.BufferedWriter

final String jdbcURL = args[0]
final String userName = args[1]
final String password = args[2]
final String ngdbcPath = args[3]
final String mode = args[4]
final String query

this.getClass().classLoader.rootLoader.addURL(new File(ngdbcPath).toURL())

println "Getting connection"
sql = Sql.newInstance(jdbcURL, userName, password)
sql.withStatement { stmt -> stmt.fetchSize = 1000 }
println "Connected"

void exportData(connection, query, destination, tableName) {
    println "Called"
    File file = new File(destination)

    println "Deleting ${destination}"
    if (file.exists()) {
        assert file.delete()
        assert file.createNewFile()
    }

    boolean append = true
    FileWriter fileWriter = new FileWriter(file, append)
    BufferedWriter buffWriter = new BufferedWriter(fileWriter)

    println "Staring query"

    printedHeader = false

    sql.query query, { ResultSet rs ->
        ResultSetMetaData metaData = rs.getMetaData()
        lastIndex = metaData.columnCount

        if(!printedHeader) {
            colNames = []
            (1..lastIndex).each {
                colNames << metaData.getColumnName(it)
            }
            buffWriter.write colNames.join("\t")
            printedHeader = true
        }
        
        while (rs.next()) {
            row = []
            (1..lastIndex).each {
                type = metaData.getColumnType(it)
                val = rs.getObject(it)
                
                if(rs.wasNull()) {
                    val = ""; // For SQL Server import - possibly remove 
                } 
            
                row << val
            }
            buffWriter.write "\n" + row.join("\t")
        }
        buffWriter.flush()
    } 
    final loadCall = """BULK INSERT ${tableName} 
FROM 'C:\\\\${tableName}.tab'
WITH
(
  FIRSTROW = 2,
  FIELDTERMINATOR = '\\t',
  ROWTERMINATOR = '0x0a',
  TABLOCK
);"""
    println loadCall

}

if("complex" != mode) {
    println("Running simple data dump")
    final String destination = args[5]
    query = mode
    println "Running query " + query
    exportData(sql, query, destination, "DATA_DUMP")
    return
} else {
    println "Running complex dump"
    tables = args[5].split(",")
    if(args.length <= 6) {
        tables.each { tableName ->
            query =  "select * from \"Insight\".\"${tableName}\" "
            exportData(sql, query, "/tmp/" + tableName + ".tab", tableName)
        }
    } else {
        paramName = args.length > 6 ? args[6] : null
        java.lang.String[] params = paramName == null ? {null} : args[7].split(",")
        tables.each { tableName ->
    
            params.each{ year ->
                year = year == null ? "no-param" : year.toString()
                println "Working on param ${year} for ${tableName}"
                query =  "select * from \"Insight\".\"${tableName}\" " +
                (paramName == null ? "" : " where \"${paramName}\" = '" + year + "'")
                exportData(sql, query, "/tmp/" + tableName + "." + year + ".tab", tableName)
            }
        }
    }
    return
}
