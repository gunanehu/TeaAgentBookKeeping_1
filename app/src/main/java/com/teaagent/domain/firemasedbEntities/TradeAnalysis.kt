package com.teaagent.domain.firemasedbEntities

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
    constructor() : this("", "", "", "", "","", "", "", "", "", "", "", "", "")

    override fun toString(): String {
        return " TradeIncomeType='     $tradeIncomeType    '," +
                " stockName=$stockName  ," +
                " EntryPrice=$EntryPrice   ," +
                " SLPrice=$SLPrice   ," +
                " ExitPrice=$ExitPrice  ," +
                " HTFLocation=$HTFLocation   , " +
                "HTFTrend=$HTFTrend   ," +
                " ITFTrend=$ITFTrend   , " +
                "ExecutionZone=$ExecutionZone   , " +
                "entryEmotion=$entryEmotion   , " +

                //  GeneralUtils.convertDate(timestampTradePlanned?.toLong()!!)+
                "note=$note "
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