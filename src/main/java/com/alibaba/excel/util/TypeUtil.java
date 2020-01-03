package com.alibaba.excel.util;

import com.alibaba.excel.metadata.ExcelColumnProperty;
import com.alibaba.excel.metadata.ExcelHeadProperty;
import com.alibaba.fastjson.JSONObject;
import net.sf.cglib.beans.BeanMap;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jipengfei
 */
public class TypeUtil {

    private static List<String> DATE_FORMAT_LIST = new ArrayList<String>(4);

    static {
        DATE_FORMAT_LIST.add("yyyy/MM/dd HH:mm:ss");
        DATE_FORMAT_LIST.add("yyyy-MM-dd HH:mm:ss");
        DATE_FORMAT_LIST.add("yyyyMMdd HH:mm:ss");
    }

    private static int getCountOfChar(String value, char c) {
        int n = 0;
        if (value == null) {
            return 0;
        }
        char[] chars = value.toCharArray();
        for (char cc : chars) {
            if (cc == c) {
                n++;
            }
        }
        return n;
    }

    public static Object convert(String value, Field field, String format, boolean us) {
        if (!StringUtils.isEmpty(value)) {
            if (Float.class.equals(field.getType())) {
                return Float.parseFloat(value);
            }
            if (Integer.class.equals(field.getType()) || int.class.equals(field.getType())) {
                return Integer.parseInt(value);
            }
            if (Double.class.equals(field.getType()) || double.class.equals(field.getType())) {
                if (null != format && !"".equals(format)) {
                    int n = getCountOfChar(value, '0');
                    return Double.parseDouble(TypeUtil.formatFloat0(value, n));
                } else {
                    return Double.parseDouble(TypeUtil.formatFloat(value));
                }
            }
            if (Boolean.class.equals(field.getType()) || boolean.class.equals(field.getType())) {
                String valueLower = value.toLowerCase();
                if (valueLower.equals("true") || valueLower.equals("false")) {
                    return Boolean.parseBoolean(value.toLowerCase());
                }
                Integer integer = Integer.parseInt(value);
                if (integer == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            if (Long.class.equals(field.getType()) || long.class.equals(field.getType())) {
                return Long.parseLong(value);
            }
            if (Date.class.equals(field.getType())) {
                if (value.contains("-") || value.contains("/") || value.contains(":")) {
                    return getSimpleDateFormatDate(value, format);
                } else {
                    Double d = Double.parseDouble(value);
                    return HSSFDateUtil.getJavaDate(d, us);
                }
            }
            if (BigDecimal.class.equals(field.getType())) {
                return new BigDecimal(value);
            }
            if(String.class.equals(field.getType())){
                return formatFloat(value);
            }
            if (Byte.class.equals(field.getType())) {
				return new String(value);
			}
        }
        return null;
    }

    public static Boolean isNum(Field field) {
        if (field == null) {
            return false;
        }
        if (Integer.class.equals(field.getType()) || int.class.equals(field.getType())) {
            return true;
        }
        if (Double.class.equals(field.getType()) || double.class.equals(field.getType())) {
            return true;
        }

        if (Long.class.equals(field.getType()) || long.class.equals(field.getType())) {
            return true;
        }

        if (BigDecimal.class.equals(field.getType())) {
            return true;
        }
        return false;
    }
    
    public static Boolean isEmptyJsonObject(String keyValue) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSONObject.parseObject(keyValue);
        } catch (Exception e) {
        }
        return (ObjectUtils.isEmpty(jsonObject) || jsonObject.size() == 0) ? true : false;
    }
   
    public static Boolean isNum(Object cellValue) {
        if (cellValue instanceof Integer
            || cellValue instanceof Double
            || cellValue instanceof Short
            || cellValue instanceof Long
            || cellValue instanceof Float
            || cellValue instanceof BigDecimal) {
            return true;
        }
        return false;
    }

    public static String getDefaultDateString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);

    }

    public static Date getSimpleDateFormatDate(String value, String format) {
        if (!StringUtils.isEmpty(value)) {
            Date date = null;
            if (!StringUtils.isEmpty(format)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                try {
                    date = simpleDateFormat.parse(value);
                    return date;
                } catch (ParseException e) {
                }
            }
            for (String dateFormat : DATE_FORMAT_LIST) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                    date = simpleDateFormat.parse(value);
                } catch (ParseException e) {
                }
                if (date != null) {
                    break;
                }
            }

            return date;

        }
        return null;

    }


    public static String formatFloat(String value) {
        if (null != value && value.contains(".")) {
            if (isNumeric(value)) {
                try {
                    BigDecimal decimal = new BigDecimal(value);
                    BigDecimal setScale = decimal.setScale(10, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros();
                    return setScale.toPlainString();
                } catch (Exception e) {
                }
            }
        }
        return value;
    }

    public static String formatFloat0(String value, int n) {
        if (null != value && value.contains(".")) {
            if (isNumeric(value)) {
                try {
                    BigDecimal decimal = new BigDecimal(value);
                    BigDecimal setScale = decimal.setScale(n, BigDecimal.ROUND_HALF_DOWN);
                    return setScale.toPlainString();
                } catch (Exception e) {
                }
            }
        }
        return value;
    }

    public static final Pattern pattern = Pattern.compile("[\\+\\-]?[\\d]+([\\.][\\d]*)?([Ee][+-]?[\\d]+)?$");

    private static boolean isNumeric(String str) {
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String formatDate(Date cellValue, String format) {
        SimpleDateFormat simpleDateFormat;
        if(!StringUtils.isEmpty(format)) {
             simpleDateFormat = new SimpleDateFormat(format);
        }else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        if (ObjectUtils.isEmpty(cellValue)) {
            return "";
        }
        return simpleDateFormat.format(cellValue);
    }

    public static String getFieldStringValue(BeanMap beanMap, String fieldName, String format, String keyValue, 
    		Boolean isDate, Boolean shrink, String shrinkValue, String percent) {
        String cellValue = null;
        Object value = beanMap.get(fieldName);
        if (value != null) {
        	try {
        		if (value instanceof Date) {
        			cellValue = TypeUtil.formatDate((Date)value, format);
        		} else if (value instanceof Long && (!StringUtils.isEmpty(format) || isDate)) {
					cellValue = TypeUtil.formatDate((Long)value, format);
        		} else if (value instanceof Long && shrink) {
					cellValue = TypeUtil.formatShrink((Long)value, shrinkValue, percent);
        		} else {
        			JSONObject jsonObject = JSONObject.parseObject(keyValue);
        			if (null != jsonObject && jsonObject.size() > 0) {
						cellValue = String.valueOf(jsonObject.get( value.toString()));
					} else {
						cellValue = value.toString();						
					}
        		}
			} catch (Exception e) {
				cellValue = value.toString();	
			}
        }
        return cellValue;
    }

    private static String formatShrink(Long value, String shrinkValue, String percent) {
    	StringBuffer cellValue = new StringBuffer();
    	BigDecimal valueBig = new BigDecimal(value);
    	if (!StringUtils.isEmpty(shrinkValue)) {
    		BigDecimal shrinkValueBig = new BigDecimal(shrinkValue);
    		cellValue.append(valueBig.divide(shrinkValueBig).toString());
		}else {
			cellValue.append(valueBig.divide(new BigDecimal(10000)).toString());
		}
    	if (!StringUtils.isEmpty(percent)) {
    		cellValue.append(percent);
		}
    	return cellValue.toString();
	}

	private static String formatDate(Long cellValue, String format) {
    	SimpleDateFormat simpleDateFormat;
        if(!StringUtils.isEmpty(format)) {
             simpleDateFormat = new SimpleDateFormat(format);
        }else {
		     simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        if (ObjectUtils.isEmpty(cellValue) || ObjectUtils.nullSafeEquals(0L,cellValue)) {
            return "";
        }
        Date date = new Date(cellValue);
        return simpleDateFormat.format(date);
	}

	public static Map getFieldValues(List<String> stringList, ExcelHeadProperty excelHeadProperty,
                                     Boolean use1904WindowDate, List<String> excelHead) {
        Map map = new HashMap();
        Map<Object, ExcelColumnProperty> excelColumnPropertyMap2 = excelHeadProperty.getExcelColumnPropertyMap2();
//        if (!excelColumnPropertyMap2.isEmpty()){
//            if (excelColumnPropertyMap2.keySet().size() != stringList.size()){
//                throw new RuntimeException("按名称导入，但是模板表头名称与实体类value标注的名称数量不相等");
//            }
//        }
        for (int i = 0; i < stringList.size(); i++) {
            ExcelColumnProperty columnProperty = null;
            //先按value解析。找不到再按index解析
            if (!excelColumnPropertyMap2.isEmpty()){
                columnProperty = excelColumnPropertyMap2.get(excelHead.get(i));
                if (columnProperty == null){
                    throw new RuntimeException(String.format("按名称导入，但是模板表头名称[%s]在实体类value标注的名称里面找不到", excelHead.get(i)));
                }
            }else {
                columnProperty = excelHeadProperty.getExcelColumnProperty(i);
            }

            if (columnProperty != null) {
                Object value = TypeUtil.convert(stringList.get(i), columnProperty.getField(),
                    columnProperty.getFormat(), use1904WindowDate);
                if (value != null) {
                    map.put(columnProperty.getField().getName(),value);
                }
            }
        }
        return map;
    }

	public static Boolean isToDate(String format, Boolean isDate) {
		if (isDate) {
			return true;
		}else if (!StringUtils.isEmpty(format)) {
			return true;
		}
		return false;
	}

}
