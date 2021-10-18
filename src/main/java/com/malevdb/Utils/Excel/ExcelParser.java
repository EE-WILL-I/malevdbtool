package com.malevdb.Utils.Excel;

import com.malevdb.Database.DatabaseConnector;
import com.malevdb.Database.InsertQueryBuilder;
import com.malevdb.Utils.FileResourcesUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.util.*;

public class ExcelParser {
    public Vector<List<XSSFCell>> read(String path) throws FileNotFoundException {
        Vector<List<XSSFCell>> cellVectorHolder = new Vector<>();
        try{
            FileInputStream myInput = FileResourcesUtils.getFileAsStream(path);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator<Row> rowIter = mySheet.rowIterator();
            while(rowIter.hasNext()){
                XSSFRow myRow = (XSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                List<XSSFCell> list = new ArrayList<>();
                while(cellIter.hasNext()){
                    XSSFCell myCell = (XSSFCell) cellIter.next();
                    list.add(myCell);
                }
                cellVectorHolder.addElement(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileNotFoundException("Can't load file");
        }
        System.out.println(cellVectorHolder.toString());
        return cellVectorHolder;
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
}
