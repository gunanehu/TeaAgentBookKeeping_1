package com.teaagent.ui.report.xcel;

import util.GeneralUtils;

public class Constants {
    // EXCEL
    public static final String EXCEL_FILE_NAME = "Trading-Journal"+
            GeneralUtils.Companion.convertDisplayDate(System.currentTimeMillis())+".xls";

    public static final String EXCEL_SHEET_NAME = "Full Trades";
}
