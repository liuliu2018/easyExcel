package com.alibaba.excel.metadata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jipengfei
 */
public class ExcelColumnProperty implements Comparable<ExcelColumnProperty> {

    /**
     */
    private Field field;

    /**
     */
    private int index = 99999;

    /**
     */
    private List<String> head = new ArrayList<String>();

    /**
     */
    private String format;
    
    private String keyValue;
    
    private Boolean date;

    private Boolean shrink;
    
    private String shrinkValue;

	public Boolean getDate() {
		return date;
	}

	public void setDate(Boolean date) {
		this.date = date;
	}

	public Boolean getShrink() {
		return shrink;
	}

	public void setShrink(Boolean shrink) {
		this.shrink = shrink;
	}

	public String getShrinkValue() {
		return shrinkValue;
	}

	public void setShrinkValue(String shrinkValue) {
		this.shrinkValue = shrinkValue;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<String> getHead() {
        return head;
    }

    public void setHead(List<String> head) {
        this.head = head;
    }

    public int compareTo(ExcelColumnProperty o) {
        int x = this.index;
        int y = o.getIndex();
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}