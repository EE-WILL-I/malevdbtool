package com.malevdb.Utils.Excel;

import com.malevdb.Application.Logging.Logger;
import com.malevdb.Database.DataTable;
import com.malevdb.Database.DatabaseConnector;
import com.malevdb.Database.InsertQueryBuilder;
import com.malevdb.Utils.FileResourcesUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.util.*;

public class ExcelParser {
    public Vector<List<XSSFCell>> read(File file) throws FileNotFoundException {
        Vector<List<XSSFCell>> cellVectorHolder = new Vector<>();
        try {
            FileInputStream myInput = FileResourcesUtils.getFileAsStream(file);
            XSSFWorkbook myWorkBook;
            try {
                myWorkBook = new XSSFWorkbook(myInput);
            } catch (Exception e) {
                Logger.log(ExcelParser.class, e.getMessage(), 2);
                throw new FileNotFoundException("Can't load file: " + e.getLocalizedMessage());
            } finally {
                file.delete();
                myInput.close();
            }
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            int maxNumOfCells = mySheet.getRow(0).getLastCellNum();
            Iterator<Row> rowIter = mySheet.rowIterator();
            while(rowIter.hasNext()) {
                XSSFRow myRow = (XSSFRow) rowIter.next();
                List<XSSFCell> list = new ArrayList<>();
                for (int cellCounter = 0; cellCounter < maxNumOfCells; cellCounter++) {
                    XSSFCell cell;
                    if (myRow.getCell(cellCounter) == null) {
                        cell = myRow.createCell(cellCounter);
                    } else {
                        cell = myRow.getCell(cellCounter);
                    }
                    list.add(cell);
                }
                cellVectorHolder.addElement(list);
            }
        } catch (Exception e) {
            Logger.log(ExcelParser.class, e.getMessage(), 2);
            throw new FileNotFoundException("Error during file reading: " + e.getLocalizedMessage());
        } finally {
            file.delete();
        }
        System.out.println(cellVectorHolder.toString());
        return cellVectorHolder;
    }

    public List<List<String>> parseData(Vector<List<XSSFCell>> dataHolder) {
        List<List<String>> tableData = new ArrayList<>(dataHolder.size());
        for (List<XSSFCell> rowData : dataHolder) {
            List<String> data = new ArrayList<>(rowData.size());
            for(XSSFCell cell : rowData)
                data.add(cell.toString());
            tableData.add(data);
        }
        return tableData;
    }

    public DataTable getTable(Vector<List<XSSFCell>> dataHolder, String tableName) {
        DataTable table = new DataTable(tableName);
        List<List<String>> parsedData = parseData(dataHolder);
        table.populateColumns(parsedData.get(0));
        parsedData.remove(0);
        table.populateRows(parsedData);
        return table;
    }

    public PreparedStatement prepareStatement(String table, String insertSample, Vector<List<XSSFCell>> dataHolder, int argsBias) {
        InsertQueryBuilder queryBuilder = new InsertQueryBuilder(table, insertSample);
        queryBuilder.setArgsBias(argsBias);

        for (List<XSSFCell> rowData : dataHolder) {
            List<String> data = new ArrayList<>();
            for(XSSFCell cell : rowData)
                data.add(cell.toString());
            queryBuilder.addRow(data.toArray(new String[0]));
        }

        return queryBuilder.getStatement();
    }

    @Override
    public void finalize() {
        File tempFile = new File(FileResourcesUtils.RESOURCE_PATH + "temp/excelData.tmp");
        if(tempFile.exists())
            tempFile.delete();
    }
}
