package com.teaagent.domain.firemasedbEntities

import android.graphics.fonts.Font
import util.GeneralUtils
import java.io.Serializable

open class TradeAnalysis(
    var id: String,
    open var phoneUserName: String?,

    var tradeIncomeType: String,
    var stockName: String?,

    //Trade entry/sl/exit planned prices
    var EntryPrice: String?,
    var SLPrice: String?,
    var ExitPrice: String?,

//sl/target levels
    var sLLevel: String?,
    var targetLevel: String?,

    //Trade analysis
//    Higher time frame
    var HTFLocation: String?,
    var HTFTrend: String?,

    //    Intermediate time frame
    var ITFTrend: String?,

    //Execution time frame-type2/3
    var ExecutionZone: String?,

    var entryEmotion: String?,

//timestamp when trde is planned
    var timestampTradePlanned: String?,
    var note: String?

) :
    Serializable {
    constructor() : this("", "", "", "","","", "", "", "", "", "", "", "", "", "", "")

    override fun toString(): String {
        val str=                  GeneralUtils.convertDisplayDate(timestampTradePlanned?.toLong()!!)+"\n" +
                " " + " $stockName  \n" +
                " Type='     $tradeIncomeType    \n" +
                " EntryPrice  = $EntryPrice   " +
                " SLPrice = $SLPrice    " +
                " ExitPrice = $ExitPrice  \n" +
                /*  " HTFLocation=$HTFLocation   , " +
                  "HTFTrend=$HTFTrend   ," +
                  " ITFTrend=$ITFTrend   , " +*/


                "ExecutionZone=$ExecutionZone    \n " +
                "entryEmotion=$entryEmotion   \n" +
                "note=$note                                                               " +
                "" +
                ""

        return str

    }

    /* override fun toString(): String {*/
    /*     return "  type='$tradeIncomeType  | bankName=$stockName | " +*/
    /*             "institutionCode=$EntryPrice | " +*/
    /*             "address=$SLPrice | acNo=$ExitPrice |" +*/
    /*             " netBankingUserName=$HTFLocation |" +*/
    /*             " password=$HTFTrend | " +*/
    /*             "atmNo=$timestampTradePlanned | " +*/
    /*             "atmPin=$note " +*/
    /*             "id=$id "*/
    /* }*/
}