package com.vechain.thorclient.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A java bean utility functions class.
 */
public class BeanRefUtils {
	/**
	 * get the mapping of attributes and values.
	 *
	 * @param bean
	 *            bean object.
	 * @return Map
	 */
	public static Map<String, Object> getFieldValueMap(Object bean) {
		Class<?> cls = bean.getClass();
		Map<String, Object> valueMap = new HashMap<String, Object>();
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldGetName = parGetName(field.getName());
				if (!checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
				valueMap.put(field.getName(), fieldVal);
			} catch (Exception e) {
				continue;
			}
		}
		return valueMap;
	}

	/**
	 * Set the attribute value to the bean object.
	 *
	 * @param bean
	 *            java bean object.
	 * @param valMap
	 *            value map.
	 */
	public static void setFieldValue(Object bean, Map<String, Object> valMap) {
		Class<?> cls = bean.getClass();
		// get all methods from the bean.
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();

		for (Field field : fields) {
			try {
				String fieldSetName = parSetName(field.getName());
				if (!checkSetMet(methods, fieldSetName)) {
					continue;
				}
				Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
				String fieldKeyName = field.getName();
				Object value = valMap.get(fieldKeyName);
				if (null != value && !"".equals(value)) {
					String fieldType = field.getType().getSimpleName();
					if ("String".equals(fieldType)) {
						fieldSetMet.invoke(bean, value);
					} else if ("Date".equals(fieldType)) {
						Date temp = parseDate(value.toString());
						fieldSetMet.invoke(bean, temp);
					} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
						Integer intval = Integer.parseInt(value.toString());
						fieldSetMet.invoke(bean, intval);
					} else if ("Long".equalsIgnoreCase(fieldType)) {
						Long temp = Long.parseLong(value.toString());
						fieldSetMet.invoke(bean, temp);
					} else if ("Double".equalsIgnoreCase(fieldType)) {
						Double temp = Double.parseDouble(value.toString());
						fieldSetMet.invoke(bean, temp);
					} else if ("Boolean".equalsIgnoreCase(fieldType)) {
						Boolean temp = Boolean.parseBoolean(value.toString());
						fieldSetMet.invoke(bean, temp);
					} else if ("byte[]".equalsIgnoreCase(fieldType) && value instanceof byte[]) {
						byte[] temp = (byte[]) value;
						fieldSetMet.invoke(bean, temp);
					} else if ("Byte".equalsIgnoreCase(fieldType)) {
						byte temp = Byte.parseByte(value.toString());
						fieldSetMet.invoke(bean, temp);
					} else {
						System.out.println("not support type" + fieldType);
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
	}

	/**
	 * Format the date
	 *
	 * @param datestr
	 *            data string like yyyy-MM-dd HH:mm:ss or yyyy-MM-dd
	 * @return date {@link Date}
	 */
	public static Date parseDate(String datestr) {
		if (null == datestr || "".equals(datestr)) {
			return null;
		}
		try {
			String fmtstr = null;
			if (datestr.indexOf(':') > 0) {
				fmtstr = "yyyy-MM-dd HH:mm:ss";
			} else {
				fmtstr = "yyyy-MM-dd";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
			return sdf.parse(datestr);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Date to String
	 *
	 * @param date
	 *            {@link Date}
	 * @return date string
	 */
	public static String fmtDate(Date date) {
		if (null == date) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Check if has the setter method.
	 * 
	 * @param methods
	 *            {@link Method} array.
	 * @param fieldSetMet
	 *            string setter method
	 * @return boolean true if it has, otherwise false.
	 */
	public static boolean checkSetMet(Method[] methods, String fieldSetMet) {
		for (Method met : methods) {
			if (fieldSetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if has get method.
	 * 
	 * @param methods
	 * @param fieldGetMet
	 * @return boolean
	 */
	public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
		for (Method met : methods) {
			if (fieldGetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get name
	 * 
	 * @param fieldName
	 *            field name
	 * @return String getName
	 */
	public static String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "get" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}

	/**
	 * Get set field name.
	 * 
	 * @param fieldName
	 * @return String
	 */
	public static String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "set" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}

	/**
	 * get key name
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String parKeyName(String fieldName) {
		String fieldGetName = parGetName(fieldName);
		if (fieldGetName != null && fieldGetName.trim() != "" && fieldGetName.length() > 3) {
			return fieldGetName.substring(3);
		}
		return fieldGetName;
	}

}