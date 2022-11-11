package com.teaagent.ui.report.xcel;

import static com.teaagent.ui.report.xcel.Constants.EXCEL_SHEET_NAME;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.teaagent.domain.firemasedbEntities.TradeAnalysis;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import util.GeneralUtils;

public class ExcelUtils {
    public static final String TAG = "ExcelUtil";
    private static Cell cell;
    private static Sheet sheet;
    private static Workbook workbook;
    private static CellStyle headerCellStyle;


    /**
     * Export Data into Excel Workbook
     *
     * @param context  - Pass the application context
     * @param fileName - Pass the desired fileName for the output excel Workbook
     * @param dataList - Contains the actual data to be displayed in excel
     */
    public static boolean exportDataIntoWorkbook(Context context, String fileName,
                                                 List<TradeAnalysis> dataList) {
        boolean isWorkbookWrittenIntoStorage;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();

        setHeaderCellStyle();

        // Creating a New Sheet and Setting width for each column
        createRowHeader();

        setHeaderRow();
        fillDataIntoExcel(dataList);
        isWorkbookWrittenIntoStorage = storeExcelInStorage(context, fileName);

        return isWorkbookWrittenIntoStorage;
    }

    private static void createRowHeader() {
        sheet = workbook.createSheet(EXCEL_SHEET_NAME);
        sheet.setColumnWidth(0, (15 * 150));
        sheet.setColumnWidth(1, (15 * 150));
        sheet.setColumnWidth(2, (15 * 150));
        sheet.setColumnWidth(3, (15 * 150));
        sheet.setColumnWidth(4, (15 * 150));
        sheet.setColumnWidth(5, (15 * 150));
        sheet.setColumnWidth(6, (15 * 800));
        sheet.setColumnWidth(7, (15 * 800));
        sheet.setColumnWidth(8, (15 * 150));
        sheet.setColumnWidth(9, (15 * 150));


        sheet.setColumnWidth(10, (15 * 150));
        sheet.setColumnWidth(11, (15 * 150));
        sheet.setColumnWidth(12, (15 * 150));
        sheet.setColumnWidth(13, (15 * 150));
        sheet.setColumnWidth(14, (15 * 150));
        sheet.setColumnWidth(15, (15 * 150));
        sheet.setColumnWidth(16, (15 * 150));
        sheet.setColumnWidth(17, (15 * 150));
        sheet.setColumnWidth(18, (15 * 150));
        sheet.setColumnWidth(19, (15 * 150));
        sheet.setColumnWidth(20, (15 * 150));
    }

    /**
     * Checks if Storage is READ-ONLY
     *
     * @return boolean
     */
    private static boolean isExternalStorageReadOnly() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState);
    }

    /**
     * Checks if Storage is Available
     *
     * @return boolean
     */
    private static boolean isExternalStorageAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);
    }

    /**
     * Setup header cell style
     */
    private static void setHeaderCellStyle() {
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    }

    /**
     * Setup Header Row
     */
    private static void setHeaderRow() {
        Row headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellValue("Name");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(1);
        cell.setCellValue("Buy/Sell");
        cell.setCellStyle(headerCellStyle);


        cell = headerRow.createCell(2);
        cell.setCellValue("Income type");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(3);
        cell.setCellValue("EntryPrice");

        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(4);
        cell.setCellValue("SLPrice");


        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(5);
        cell.setCellValue("ExitPrice");

        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(6);
        cell.setCellValue("Note");

        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(7);
        cell.setCellValue("ExitNote");


//            var sLLevel: String?,
//            var targetLevel: String?,
        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(8);
        cell.setCellValue("SLLevel");


        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(9);
        cell.setCellValue("TargetLevel");

//            var HTFLocation: String?,
//            var HTFTrend: String?,
        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(10);
        cell.setCellValue("HTFLocation");


        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(11);
        cell.setCellValue("HTFTrend");


        //    Intermediate time frame
//            var ITFTrend: String?,
        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(12);
        cell.setCellValue("ITFTrend");


//
//            //Execution time frame-type2/3
//            var ExecutionZone: String?,
//            var entryEmotion: String?,
        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(12);
        cell.setCellValue("ExecutionZone");


        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(13);
        cell.setCellValue("EntryEmotion");

//            var tradeManagementType: String?,
//            var tradeExitPostAnalysisTypeType: String?,
//            var missedTradeType: String?,
//            var mentalState: String?,
//            var confidenceLevel: String?,
//            var exitNote: String?,
        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(14);
        cell.setCellValue("TradeManagementType");

        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(15);
        cell.setCellValue("TradeExitPostAnalysisTypeType");

        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(16);
        cell.setCellValue("MissedTradeType");

        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(17);
        cell.setCellValue("ConfidenceLevel");

        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(18);
        cell.setCellValue("ExecutionZone");


//            var timestampTradePlanned: String?,
//            var timestampTradeExited: String?,
        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(19);
        cell.setCellValue("TimestampTradePlanned");

        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(20);
        cell.setCellValue("TimestampTradeExited");
        cell.setCellStyle(headerCellStyle);


    }

    /**
     * Fills Data into Excel Sheet
     * <p>
     * NOTE: Set row index as i+1 since 0th index belongs to header row
     *
     * @param dataList - List containing data to be filled into excel
     */
    private static void fillDataIntoExcel(List<TradeAnalysis> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            // Create a New Row for every new entry in list
            Row rowData = sheet.createRow(i + 1);

            // Create Cells for each row
            cell = rowData.createCell(0);
            cell.setCellValue(dataList.get(i).getStockName());

            cell = rowData.createCell(1);
            cell.setCellValue(dataList.get(i).isBuy());


            cell = rowData.createCell(2);
            cell.setCellValue(dataList.get(i).getTradeIncomeType());


            cell = rowData.createCell(3);
            cell.setCellValue(dataList.get(i).getEntryPrice());

            cell = rowData.createCell(4);
            cell.setCellValue(dataList.get(i).getSLPrice());

            cell = rowData.createCell(5);
            cell.setCellValue(dataList.get(i).getExitPrice());

            cell = rowData.createCell(6);
            cell.setCellValue(dataList.get(i).getNote());

            cell = rowData.createCell(7);
            cell.setCellValue(dataList.get(i).getExitNote());


//            var sLLevel: String?,
//            var targetLevel: String?,
            cell = rowData.createCell(8);
            cell.setCellValue(dataList.get(i).getSLLevel());
            cell = rowData.createCell(9);
            cell.setCellValue(dataList.get(i).getTargetLevel());

//            var HTFLocation: String?,
//            var HTFTrend: String?,
            cell = rowData.createCell(10);
            cell.setCellValue(dataList.get(i).getHTFLocation());
            cell = rowData.createCell(11);
            cell.setCellValue(dataList.get(i).getHTFTrend());

            //    Intermediate time frame
//            var ITFTrend: String?,
            cell = rowData.createCell(12);
            cell.setCellValue(dataList.get(i).getITFTrend());

//
//            //Execution time frame-type2/3
//            var ExecutionZone: String?,
//            var entryEmotion: String?,
            cell = rowData.createCell(12);
            cell.setCellValue(dataList.get(i).getExecutionZone());
            cell = rowData.createCell(13);
            cell.setCellValue(dataList.get(i).getEntryEmotion());
//            var tradeManagementType: String?,
//            var tradeExitPostAnalysisTypeType: String?,
//            var missedTradeType: String?,
//            var mentalState: String?,
//            var confidenceLevel: String?,
//            var exitNote: String?,
            cell = rowData.createCell(14);
            cell.setCellValue(dataList.get(i).getTradeManagementType());
            cell = rowData.createCell(15);
            cell.setCellValue(dataList.get(i).getTradeExitPostAnalysisTypeType());
            cell = rowData.createCell(16);
            cell.setCellValue(dataList.get(i).getMissedTradeType());
            cell = rowData.createCell(17);
            cell.setCellValue(dataList.get(i).getConfidenceLevel());
            cell = rowData.createCell(18);
            cell.setCellValue(dataList.get(i).getExecutionZone());


//            var timestampTradePlanned: String?,
//            var timestampTradeExited: String?,
            cell = rowData.createCell(19);
            if (dataList.get(i).getTimestampTradePlanned() != null && !dataList.get(i).getTimestampTradePlanned().isEmpty()) {
                String date1 = dataList.get(i).getTimestampTradePlanned();
                cell.setCellValue(GeneralUtils.Companion.convertDisplayDate(Long.parseLong(date1)));
            }

            cell = rowData.createCell(20);
            if (dataList.get(i).getTimestampTradeExited() != null && !dataList.get(i).getTimestampTradeExited().isEmpty()) {
                String date2 = dataList.get(i).getTimestampTradeExited();
                cell.setCellValue(GeneralUtils.Companion.convertDisplayDate(Long.parseLong(date2)));
            }

        }
    }


    /**
     * Store Excel Workbook in external storage
     *
     * @param context  - application context
     * @param fileName - name of workbook which will be stored in device
     * @return boolean - returns state whether workbook is written into storage or not
     */
    private static boolean storeExcelInStorage(Context context, String fileName) {
        boolean isSuccess;

        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Log.i(TAG, "Writing file" + file);
            isSuccess = true;
        } catch (IOException e) {
            Log.e(TAG, "Error writing Exception: ", e);
            isSuccess = false;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save file due to Exception: ", e);
            isSuccess = false;
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        ShareViaEmail(context,file);

        return isSuccess;
    }
    private static void ShareViaEmail(Context context, File file) {
        try {

            Intent intent = new Intent(Intent.ACTION_SENDTO);
//            intent.setType("text/csv");
            String message="File to be shared is " + "dede" + ".";
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse( "file://"+file));
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setData(Uri.parse("mailto:gunanehu@gmail.com"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch(Exception e)  {
            Log.e(TAG,"is exception raises during sending mail"+e);
        }
    }
    /*  *//**
     * Retrieve excel from External Storage
     *
     * @param context  - application context
     * @param fileName - name of workbook to be read
     * @return importedExcelData
     *//*
    private static List<TradeAnalysis> retrieveExcelFromStorage(Context context, String fileName) {
      ArrayList  importedExcelData = new ArrayList<>();

        File file = new File(context.getExternalFilesDir(null), fileName+".xls");
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            Log.e(TAG, "Reading from Excel" + file);

            // Create instance having reference to .xls file
            workbook = new HSSFWorkbook(fileInputStream);

            // Fetch sheet at position 'i' from the workbook
            sheet = workbook.getSheetAt(0);

            // Iterate through each row
            for (Row row : sheet) {
                int index = 0;
                List<String> rowDataList = new ArrayList<>();

                if (row.getRowNum() > 0) {
                    // Iterate through all the columns in a row (Excluding header row)
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        // Check cell type and format accordingly
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:

                                break;
                            case Cell.CELL_TYPE_STRING:
                                rowDataList.add(index, cell.getStringCellValue());
                                index++;
                                break;
                        }
                    }

                    // Adding cells with phone numbers to phoneNumberList
                    for (int i = 1; i < rowDataList.size(); i++) {
                        Log.i("jb",rowDataList.get(i).toUpperCase());
                    }


                }

            }

        } catch (IOException e) {
            Log.e(TAG, "Error Reading Exception: ", e);

        } catch (Exception e) {
            Log.e(TAG, "Failed to read file due to Exception: ", e);

        } finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return importedExcelData;
    }*/

}
