package org.fwoxford.web.rest.util;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.SerialNo;
import org.fwoxford.repository.SerialNoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * Created by gengluying on 2017/3/31.
 */
@Service
public class BankUtil {

    @Autowired
    private SerialNoRepository serialNoRepository;

    //由出生日期获得年龄
    public static int getAge(ZonedDateTime birthDay) throws Exception {
        Date date = Date.from(birthDay.toInstant());
        Calendar cal = Calendar.getInstance();

        if (cal.before(date)) {
            throw new IllegalArgumentException(
                "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(date);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }

    public static  String getPositionString(Object equipmentCode,Object areaCode,Object supportCode,Object columnsInshelf,Object rowsInshelf,Object tubeRows,Object tubeColumns) {
        String position = "";
        ArrayList<String> positions = new ArrayList<>();
        if (equipmentCode != null && equipmentCode.toString().length() > 0){
            positions.add(equipmentCode.toString());
        }

        if (areaCode != null && areaCode.toString().length() > 0) {
            positions.add(areaCode.toString());
        }

        if (supportCode != null && supportCode.toString().length() > 0){
            positions.add(supportCode.toString());
        }

        if (rowsInshelf != null && rowsInshelf.toString().length() > 0 && columnsInshelf != null && columnsInshelf.toString().length() > 0){
            positions.add(columnsInshelf.toString()+rowsInshelf.toString());
        }

        if (tubeRows != null && tubeRows.toString().toString().length() > 0 && tubeColumns != null && tubeColumns.toString().length() > 0){
            positions.add(tubeRows.toString()+tubeColumns.toString());
        }

        return String.join(".", positions);
    }

    public synchronized String getSerialRandom(String flag,LocalDate localDate) {
        String fourRandom = "";
        SerialNo serialNo = serialNoRepository.findlimitOneByMachineNoAndUsedDate(flag,localDate);
        int random = 0;
        if(serialNo != null){
            random = Integer.valueOf(serialNo.getSerialNo());
        }
        random = random >= 999 ? 1 : random + 1;
        String s = Integer.toString(random);
        String j = addLeftZero(s,3);
        SerialNo serial = new SerialNo().serialNo(String.valueOf(j)).machineNo(flag).usedDate(localDate).status(Constants.VALID);
        serialNoRepository.save(serial);
        return j;
    }
    public  static String addLeftZero(String s, int length) {
        int old = s.length();
        if (length > old) {
            char[] c = new char[length];
            char[] x = s.toCharArray();
            if (x.length > length) {
                throw new IllegalArgumentException(
                    "Numeric value is larger than intended length: " + s
                        + " LEN " + length);
            }
            int lim = c.length - x.length;
            for (int i = 0; i < lim; i++) {
                c[i] = '0';
            }
            System.arraycopy(x, 0, c, lim, x.length);
            return new String(c);
        }
        return s.substring(0, length);
    }

    public static String getFourRandom(){
        Random random = new Random();
        String fourRandom = random.nextInt(10000) + "";
        int randLength = fourRandom.length();
        if(randLength<4){
            for(int i=1; i<=4-randLength; i++)
                fourRandom = "0" + fourRandom  ;
        }
        return fourRandom;
    }

    public String getUniqueIDByDate(String flag,Date date) {
        String time = "";
        String timeServerUrl = "ntp5.aliyun.com";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateTime = dateFormat.format(date);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        String fourRandom = getSerialRandom(flag,localDate);
        if(flag.length()>1){
            dateTime = dateTime.substring(1,dateTime.length());
        }
        time = flag +dateTime+fourRandom;
        return time;
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("%" + Integer.toHexString(c));
        }

        return unicode.toString();
    }
    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("%");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    public static HashMap<String, Object> convertToMap(Object obj)
        throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            boolean accessFlag = fields[i].isAccessible();
            fields[i].setAccessible(true);

            Object o = fields[i].get(obj);
            if (o != null)
                map.put(varName, o.toString());

            fields[i].setAccessible(accessFlag);
        }

        return map;
    }
    /***
     *
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date,String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }
    /***
     * convert Date to cron ,eg.  "0 06 10 15 1 ? 2014"
     * @param date  : 时间点
     * @return
     */
    public static String getCron(Date date){
        String dateFormat="ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }
}
