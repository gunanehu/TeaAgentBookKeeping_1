package com.teaagent.domain.firemasedbEntities

import util.GeneralUtils
import java.io.Serializable

open class TradeAnalysis(
    var id: String,
    open var phoneUserName: String?,

    var tradeIncomeType: String,
    var stockName: String?,
    var isBuy: Boolean,

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
    var executionTrend: String?,

    //Execution time frame-type2/3
    var ExecutionZone: String?,
    var entryEmotion: String?,

    var tradeManagementType: String?,
    var tradeExitPostAnalysisTypeType: String?,
    var missedTradeType: String?,
    var mentalState: String?,
    var confidenceLevel: String?,
    var exitNote: String?,

//timestamp when trde is planned
    var timestampTradePlanned: String?,
    var timestampTradeExited: String?,

    var note: String?,

    val quantity: Long?

) :
    Serializable {
    constructor() : this(
        "",
        "",
        "",
        "",
        false,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "", "",
        
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "", "",0
    )

    override fun toString(): String {
        val str = GeneralUtils.convertDisplayDate(timestampTradePlanned?.toLong()!!) + "\n" +
                " " + " $stockName  \n" +
                " Type='     $tradeIncomeType    \n" +

                " isBuy='     $isBuy    \n" +
                " EntryPrice  = $EntryPrice   " +
                " SLPrice = $SLPrice    " +
                " ExitPrice = $ExitPrice  \n" +
                " quantity = $quantity  \n" +
                " sl val = $quantity*( $EntryPrice -$EntryPrice) \n" +
                " profit val = $quantity *$quantity*( $ExitPrice -$EntryPrice) \n" +
                /*  " HTFLocation=$HTFLocation   , " +
                  "HTFTrend=$HTFTrend   ," +
                  " ITFTrend=$ITFTrend   , " +*/

                /* "ExecutionZone=$ExecutionZone    \n " +
                 "entryEmotion=$entryEmotion   \n" +
                 "note=$note                                                               " +

                 " tradeManagementType=$tradeManagementType," +
                 " tradeExitPostAnalysisTypeType=$tradeExitPostAnalysisTypeType," +
                 " missedTradeType=$missedTradeType, " +
                 "mentalState=$mentalState," +
                 " confidenceLevel=$confidenceLevel," +
                 "exitNote=$exitNote                                                               " +*/
//                GeneralUtils.convertDisplayDate(timestampTradeExited?.toLong()!!) + "\n" +

                "" +
                ""
        return str
    }

}