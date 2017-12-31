package cn.zinus.shipping.util;

/**
 * Created by Spring on 2017/4/17.
 */

public class Constant {
    //SharedPreferences
    public static final String URL = "URL";
    public static final String VERSIONURL = "VERSIONURL";
    public static final String APKURL = "APKURL";
    public static final String UserID = "UserID";

    //LanguageType
    public static final String ZH_CN = "zh-CN";
    public static final String EN_US = "en-US";
    public static final String KO_KR = "ko-KR";

    //rule_command
    public static final String UPDATE = "U";
    public static final String CREATE = "C";

    public static final int RFIDSCAN = 111;
    public static final int UPDATEUI = 112;

    //sqlite
    public static final String DATEBASE_NAME = "zinusshipping";//数据库名称
    public static final int DATEBASE_VERSION = 1; //数据库版本号
    public static final String TABLE_NAME = "DataList";   //表名
    public static final String IMPORTDATE = "IMPORTDATE";
    public static final String PDAYN = "PDAYN";
    public static final String DataList = "DataList";
    public static final String TABLE_NAME_SUBSET = "ITEMLIST";   //表名
    public static final String CHECKYM = "CHECKYM";
    public static final String PLANORDER = "PLANORDER";
    public static final String TAGID = "TAGID";
    public static final String ITEMID = "ITEMID";
    public static final String ORIGINUNIT = "ORIGINUNIT";
    public static final String ORIGINQTY = "ORIGINQTY";
    public static final String CHECKUNIT = "CHECKUNIT";
    public static final String CHECKQTY = "CHECKQTY";
    public static final String LOCATIONID = "LOCATIONID";
    public static final String WORKERID = "WORKERID";
    public static final String STATE = "STATE";
    //
    public static final String WAREHOUSEID = "WAREHOUSEID";
    //common
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String CREATOR = "CREATOR";
    public static final String CREATEDTIME = "CREATEDTIME";
    public static final String MODIFIER = "MODIFIER";
    public static final String MODIFIEDTIME = "MODIFIEDTIME";
    public static final String LASTTXNID = "LASTTXNID";
    public static final String LASTTXNUSER = "LASTTXNUSER";
    public static final String LASTTXNTIME = "LASTTXNTIME";
    public static final String LASTTXNCOMMENT = "LASTTXNCOMMENT";
    public static final String LASTTXNHISTKEY = "LASTTXNHISTKEY";
    public static final String LASTGROUPHISTKEY = "LASTGROUPHISTKEY";
    public static final String VALIDSTATE = "VALIDSTATE";

    //SF_CODE表
    public static final String SF_CODE = "SF_CODE";
    public static final String CODEID = "CODEID";
    public static final String CODENAME = "CODENAME";
    public static final String CODECLASSID = "CODECLASSID";
    public static final String DICTIONARYNAME = "DICTIONARYNAME";
    public static final String LANGUAGETYPE = "LANGUAGETYPE";
    //CodeClass
    public static final String CONSUMEINBOUNDSTATE = "ConsumeInboundState";
    public static final String CONSUMEOUTBOUNDSTATE = "ConsumeOutboundState";
    public static final String CHECKSTATE = "CheckState";
    public static final String PROCESSTYPE = "ProcessType";
    public static final String CONSUMABLETYPE = "ConsumableType";
    public static final String WAREHOUSETYPE = "WAREHOUSETYPE";
    public static final String SHIPPINGPLANSTATE = "ShippingPlanState";

    //SF_INBOUNDORDER表
    public static final String SF_INBOUNDORDER = "SF_INBOUNDORDER";
    public static final String INBOUNDNO = "INBOUNDNO";
    public static final String INBOUNDDATE = "INBOUNDDATE";
    public static final String INBOUNDSTATE = "INBOUNDSTATE";
    public static final String INSPECTIONRESULT = "INSPECTIONRESULT";
    public static final String URGENCYTYPE = "URGENCYTYPE";
    public static final String SCHEDULEDATE = "SCHEDULEDATE";
    public static final String TEMPINBOUNDDATE = "TEMPINBOUNDDATE";
    public static final String ConsumeInboundState = "ConsumeInboundState";

    //SF_CONSUMEINBOUND
    public static final String SF_CONSUMEINBOUND = "SF_CONSUMEINBOUND";
    public static final String CONSUMABLEDEFID = "CONSUMABLEDEFID";
    public static final String CONSUMABLEDEFVERSION = "CONSUMABLEDEFVERSION";
    //public static final String WAREHOUSEID = "WAREHOUSEID";
    public static final String UNIT = "UNIT";
    public static final String INQTY = "INQTY";
    public static final String PLANQTY = "PLANQTY";
    //public static final String INBOUNDDATE = "INBOUNDDATE";

    //SF_CONSUMELOTINBOUND
    public static final String SF_CONSUMELOTINBOUND = "SF_CONSUMELOTINBOUND";
    public static final String CONSUMABLEDEFNAME = "CONSUMABLEDEFNAME";
    public static final String CONSUMABLELOTID = "CONSUMABLELOTID";

    //SF_CONSUMABLEDEFINITION
    public static final String SF_CONSUMABLEDEFINITION = "SF_CONSUMABLEDEFINITION";
    //public static final String CONSUMABLEDEFID = "CONSUMABLEDEFID";
    //public static final String CONSUMABLEDEFVERSION = "CONSUMABLEDEFVERSION";
    //public static final String CONSUMABLEDEFNAME = "CONSUMABLEDEFNAME";

    //SF_CONSUMEREQUEST
    public static final String SF_CONSUMEREQUEST = "SF_CONSUMEREQUEST";
    public static final String CONSUMEREQNO = "CONSUMEREQNO";
    public static final String AREAID = "AREAID";
    public static final String USERID = "USERID";
    public static final String REQUESTDATE = "REQUESTDATE";
    public static final String OUTBOUNDSTATE = "OUTBOUNDSTATE";

    //SF_CONSUMEOUTBOUND
    public static final String SF_CONSUMEOUTBOUND = "SF_CONSUMEOUTBOUND";
    public static final String REQUESTQTY = "REQUESTQTY";
    public static final String OUTQTY = "OUTQTY";
    public static final String FROMWAREHOUSEID = "FROMWAREHOUSEID";
    public static final String OUTBOUNDDATE = "OUTBOUNDDATE";

    //SF_CONSUMELOTOUTBOUND
    public static final String SF_CONSUMELOTOUTBOUND = "SF_CONSUMELOTOUTBOUND";

    //SF_STOCKCHECK
    public static final String SF_STOCKCHECK = "SF_STOCKCHECK";
    //public static final String WAREHOUSEID = "WAREHOUSEID";
    public static final String CHECKMONTH = "CHECKMONTH";
    public static final String STARTDATE = "STARTDATE";
    public static final String ENDDATE = "ENDDATE";
    //public static final String  STATE = "STATE";
    //public static final String  VALIDSTATE = "VALIDSTATE";

    //SF_STOCKCHECK
    public static final String SF_STOCKCHECKDETAIL = "SF_STOCKCHECKDETAIL";
    public static final String SF_STOCKLOTCHECKDETAIL = "SF_STOCKLOTCHECKDETAIL";
    public static final String QTY = "QTY";

    //SF_SHIPPINGPLAN
    public static final String SF_SHIPPINGPLAN = "SF_SHIPPINGPLAN";
    public static final String SHIPPINGPLANNO = "SHIPPINGPLANNO";
    public static final String POID = "POID";
    public static final String LINENO = "LINENO";
    public static final String PLANTID = "PLANTID";
    public static final String CUSTOMERID = "CUSTOMERID";
    public static final String ORDERTYPE = "ORDERTYPE";
    public static final String ORDERNO = "ORDERNO";
    public static final String PRODUCTDEFID = "PRODUCTDEFID";
    public static final String PRODUCTDEFVERSION = "PRODUCTDEFVERSION";
    public static final String PLANSTARTTIME = "PLANSTARTTIME";
    public static final String SHIPPINGPLANSEQ = "SHIPPINGPLANSEQ";
    public static final String CONTAINERSEQ = "CONTAINERSEQ";
    public static final String PRODUCTDEFNAME = "PRODUCTDEFNAME";
    public static final String LOADEDQTY = "LOADEDQTY";
    public static final String PLANENDTIME = "PLANENDTIME";
    //public static final String  PLANQTY= "PLANQTY";
    public static final String CONTAINERSPEC = "CONTAINERSPEC";
    public static final String WORKINGSHIFT = "WORKINGSHIFT";
    //public static final String  AREAID= "AREAID";
    //public static final String  STATE= "STATE";
    public static final String ISPDASHIPPING = "ISPDASHIPPING";
    public static final String ISOISAVE = "ISOISAVE";
    public static final String BOOKINGNO  = "BOOKINGNO";
    public static final String PLANDATE  = "PLANDATE";
    public static final String SHIPPINGPLANDATE  = "SHIPPINGPLANDATE";
    public static final String SHIPPINGENDPLANDATE  = "SHIPPINGENDPLANDATE";
    public static final String SHIPPINGENDDATE  = "SHIPPINGENDDATE";
    public static final String TRACKOUTTIME  = "TRACKOUTTIME";

    //SF_SHIPPINGPLANDETAIL
    public static final String SF_SHIPPINGPLANDETAIL = "SF_SHIPPINGPLANDETAIL";
    public static final String COMPLETETIME = "COMPLETETIME";


    public static final String SF_CONSUMESTOCK = "SF_CONSUMESTOCK";

    //SF_CONSUMABLELOT
    public static final String SF_CONSUMABLELOT = "SF_CONSUMABLELOT";
    public static final String CONSUMABLESTATE = "CONSUMABLESTATE";
    public static final String CREATEDQTY = "CREATEDQTY";

    //SF_AREA
    public static final String SF_AREA = "SF_AREA";
    public static final String AREANAME = "AREANAME";
    public static final String AREATYPE = "AREATYPE";
    public static final String PARENTAREAID = "PARENTAREAID";
    public static final String PROCESSSEGMENTID = "PROCESSSEGMENTID";
    public static final String PROCESSSEGMENTTVERSION = "PROCESSSEGMENTTVERSION";

    //SF_AREA
    public static final String SF_WAREHOUSE = "SF_WAREHOUSE";
    //public static final String WAREHOUSEID= "WAREHOUSEID";
    public static final String WAREHOUSENAME = "WAREHOUSENAME";
    //public static final String WAREHOUSETYPE= "WAREHOUSETYPE";

    //SF_SHIPPINGLOT
    public static final String SF_SHIPPINGLOT = "SF_SHIPPINGLOT";
    public static final String LOTID = "LOTID";
    public static final String SEALNO = "SEALNO";
    //public static final String  SHIPPINGPLANNO= "SHIPPINGPLANNO";
    public static final String CONTAINERNO = "CONTAINERNO";
    public static final String SHIPPINGDATE = "SHIPPINGDATE";

    //SF_LOT
    public static final String SF_LOT = "SF_LOT";
    //public static final String LOTID = "LOTID";
    public static final String PURCHASEORDERID = "PURCHASEORDERID";
    //public static final String PROCESSSEGMENTID = "PROCESSSEGMENTID";
    public static final String LOTSTATE = "LOTSTATE";
    //public static final String VALIDSTATE = "VALIDSTATE";
    //public static final String QTY = "QTY";
    public static final String RFID = "RFID";

    public static final String VALID = "Valid";
    public static final String INVALID = "Invalid";


    //通过USB Socket通信
    public static final int SOCKETLENGTH = 3;
    public static final String UPDATESHIPPINGSTART = "880";
    public static final String UPDATESTOCKINSTART = "881";
    public static final String UPDATESTOCKOUTSTART = "882";
    public static final String UPDATESTOCKCHECKSTART = "883";
    public static final String UPDATECOMMONSTART = "884";
    public static final String UPLOADSHIPPINGSTART = "885";
    public static final String UPLOADSTOCKINSTART = "886";
    public static final String UPLOADSTOCKOUTSTART = "887";
    public static final String UPLOADSTOCKCHECKSTART = "888";
    public static final String UPLOADEXIT = "889";
    public static final String UPDATEEXIT = "890";
    public static final String RETURNSHIPPINGSAVESTART = "891";

    public static final String SYNCSF_CODE = "801";
    public static final String SYNCSF_INBOUNDORDER = "802";
    public static final String SYNCSF_CONSUMEINBOUND = "803";
    public static final String SYNCSF_CONSUMELOTINBOUND = "804";
    public static final String SYNCSF_CONSUMABLEDEFINITION = "805";
    public static final String SYNCSF_CONSUMEREQUEST = "806";
    public static final String SYNCSF_CONSUMEOUTBOUND = "807";
    public static final String SYNCSF_CONSUMELOTOUTBOUND = "808";
    public static final String SYNCSF_STOCKCHECK = "809";
    public static final String SYNCSF_STOCKCHECKDETAIL = "810";
    public static final String SYNCSF_STOCKLOTCHECKDETAIL = "811";
    public static final String SYNCSF_SHIPPINGPLAN = "812";
    public static final String SYNCSF_LOTSHIPPING = "813";
    public static final String SYNCSF_CONSUMESTOCK = "814";
    public static final String SYNCSF_CONSUMABLELOT = "815";
    public static final String SYNCSF_AREA = "816";
    public static final String SYNCSF_WAREHOUSE = "817";
    public static final String SYNCSF_LOT = "818";
    public static final String SYNCSF_SHIPPINGPLANDETAIL = "819";

    public static final String IYNCSF_CODE = "701";
    public static final String IYNCSF_INBOUNDORDER = "702";
    public static final String IYNCSF_CONSUMEINBOUND = "703";
    public static final String IYNCSF_CONSUMELOTINBOUND = "704";
    public static final String IYNCSF_CONSUMABLEDEFINITION = "705";
    public static final String IYNCSF_CONSUMEREQUEST = "706";
    public static final String IYNCSF_CONSUMEOUTBOUND = "707";
    public static final String IYNCSF_CONSUMELOTOUTBOUND = "708";
    public static final String IYNCSF_STOCKCHECK = "709";
    public static final String IYNCSF_STOCKCHECKDETAIL = "710";
    public static final String IYNCSF_STOCKLOTCHECKDETAIL = "711";
    public static final String IYNCSF_SHIPPINGPLAN = "712";
    public static final String IYNCSF_LOTSHIPPING = "713";
    public static final String IYNCSF_CONSUMESTOCK = "714";
    public static final String IYNCSF_CONSUMABLELOT = "715";
    public static final String IYNCSF_AREA = "716";
    public static final String IYNCSF_WAREHOUSE = "717";
    public static final String IYNCSF_LOT = "718";
    public static final String IYNCSF_SHIPPINGPLANDETAIL = "719";

    public static final String UPLOADSHIPPINGPLANINFO = "601";
    public static final String UPLOADLOTSHIPPINGINFO = "602";

    public static final String UPLOADSHIPPINGPLAN = "681";
    public static final String UPLOADLOTSHIPPING = "682";


}
//1
//2
//3
//4
//5
//6
//7
//8
//9
//10
//11
//12
//13