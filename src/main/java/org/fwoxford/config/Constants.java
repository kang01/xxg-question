package org.fwoxford.config;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

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
     * 问题状态：2201：草拟中；2202：已提问；2203：有回复；2204：已回复，2205已过期；2206：已结束.
     */
    public static final String QUESTION_IN_DRAFT = "2201";
    public static final String QUESTION_ASKED = "2202";
    public static final String QUESTION_REPLY_PART = "2203";
    public static final String QUESTION_REPLY = "2204";
    public static final String QUESTION_OVERDUE= "2205";
    public static final String QUESTION_FINISHED= "2206";

    //问题类型：2301：样本类型，2302：其他问题
    public static final String QUESTION_TYPE_SAMPLE= "2301";
    public static final String QUESTION_TYPE_OTHER= "2302";

    /**
     * 问题发送记录状态：2401：已发送，2402:回复中，2403: 已回复，2404:已过期2405:已重发
     */
    public static final String QUESTION_SENT = "2401";
    public static final String QUESTION_SEND_REPLY_PENDING= "2402";
    public static final String QUESTION_SEND_REPLIED= "2403";
    public static final String QUESTION_SEND_OVERDUE= "2404";
    public static final String QUESTION_SEND_RESEND= "2405";
    /**
     * 问题回复状态：2501：进行中，2502：已回复，2503：已过期
     */
    public static final String QUESTION_REPLY_PENDING= "2501";
    public static final String QUESTION_REPLY_FINISHED= "2502";
    public static final String QUESTION_REPLY_OVERDUE= "2503";

    public static final String AUTHORITY_ROLE_STRANGER = "ROLE_STRANGER";
    public static final Map<String,String> QUESTION_TYPE_MAP = new HashMap<String,String>(){
        {
            put("2301", "样本类型");
            put("2302", "其他问题");
        }};


    //问题样本处理方式 ： 2601：销毁样本，2602：样本正常
    public static final String QUESTION_SAMPLE_DESTROY= "2701";
    public static final String QUESTION_SAMPLE_NORMAL= "2702";
    public static final String QUESTION_SAMPLE_DELAY= "2703";


    public static final Integer TASK_ENABLE_STATUS_YES = 1;
    public static final Integer TASK_ENABLE_STATUS_NO = 0;
    /**
     * 任务已完成
     */
    public static final String TASK_STATUS_FINISHED = "0003";

    public static final String CLASS_NAME_TASK_MESSAGE ="org.fwoxford.service.ScheduleTaskMessage";
    public static final String CLASS_NAME_TASK_REPLY="org.fwoxford.service.ScheduleTaskReply";

    public static final String QUARTZ_GROUP_DELAY = "DELAY";
    public static final String QUARTZ_GROUP_MESSAGE = "MESSAGE";

    public static final String STRANGER_HTTP_URL =  "http://localhost:8082/#/question-management/?a=";
    public static final Integer APPLY_TIMES_MAX = 2;
    //测试时候为 60 正式的为 24*60
    public static final Integer INCREASE_MINUTES = 2*60;

    private Constants() {
    }
}
