package org.fwoxford.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";

    /**
     * 数据状态：0000：无效，0001：有效
     */
    public static final String VALID = "0001";
    public static final String INVALID = "0000";
    /**
     * 冻存盒状态：2001：新建，2011：转运完成；2002：待入库，2003：已分装，2004：已入库，2090：已作废，2006：已上架，2008：待出库，2009：已出库，2010：已交接,2012：已销毁, 2013：归还中
     */
    public static final  String FROZEN_BOX_NEW = "2001" ;
    public static final  String FROZEN_BOX_TRANSHIP_COMPLETE = "2011" ;
    public static final  String FROZEN_BOX_STOCKING = "2002" ;
    public static final  String FROZEN_BOX_SPLITED = "2003" ;
    public static final  String FROZEN_BOX_STOCKED = "2004" ;
    public static final  String FROZEN_BOX_INVALID = "2090" ;
    public static final String FROZEN_BOX_PUT_SHELVES = "2006";
    public static final String FROZEN_BOX_SPLITING = "2007";
    public static final String FROZEN_BOX_STOCK_OUT_PENDING = "2008";
    public static final String FROZEN_BOX_STOCK_OUT_COMPLETED = "2009";
    public static final String FROZEN_BOX_STOCK_OUT_HANDOVER = "2010";
    public static final String FROZEN_BOX_DESTROY = "2012";
    public static final String FROZEN_BOX_RETURN_BACK = "2013" ;
    /**
     * 冻存管状态：3001：正常，3002：空管，3003：空孔；3004：异常; 3005:销毁；3006:不确定
     */
    public static final  String FROZEN_TUBE_NORMAL = "3001" ;
    public static final  String FROZEN_TUBE_EMPTY = "3002" ;
    public static final  String FROZEN_TUBE_HOLE_EMPTY = "3003" ;
    public static final  String FROZEN_TUBE_ABNORMAL = "3004" ;
    public static final  String FROZEN_TUBE_DESTROY = "3005" ;
    public static final  String FROZEN_TUBE_NOT_SURE = "3006" ;
    /**
     * 问题编码：Q
     */
    public static final String QUESTION_CODE = "Q";
    /**
     * 问题状态：2201：草拟中；2202：已提问；2203：已回复；2204已过期；2205：已结束
     */
    public static final String QUESTION_IN_DRAFT = "2201";
    public static final String QUESTION_ASKED = "2202";
    public static final String QUESTION_REPLY = "2203";
    public static final String QUESTION_OVERDUE= "2204";
    public static final String QUESTION_FINISHED= "2205";

    private Constants() {
    }
}
