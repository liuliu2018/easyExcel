package com.alibaba.excel.analysis;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.event.AnalysisEventRegisterCenter;
import com.alibaba.excel.event.OneRowAnalysisFinishEvent;
import com.alibaba.excel.metadata.ExcelHeadProperty;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.TypeUtil;

import java.util.*;

/**
 * @author jipengfei
 */
public abstract class BaseSaxAnalyser implements AnalysisEventRegisterCenter, ExcelAnalyser {

    protected AnalysisContext analysisContext;

    private LinkedHashMap<String, AnalysisEventListener> listeners = new LinkedHashMap<String, AnalysisEventListener>();

    /**
     * execute method
     */
    protected abstract void execute();


    @Override
    public void appendLister(String name, AnalysisEventListener listener) {
        if (!listeners.containsKey(name)) {
            listeners.put(name, listener);
        }
    }

    @Override
    public void analysis(Sheet sheetParam) {
        execute();
    }

    @Override
    public void analysis() {
        execute();
    }

    /**
     */
    @Override
    public void cleanAllListeners() {
        listeners = new LinkedHashMap<String, AnalysisEventListener>();
    }

    @Override
    public void notifyListeners(OneRowAnalysisFinishEvent event) {
        analysisContext.setCurrentRowAnalysisResult(event.getData());
        /** Parsing header content **/
        if (analysisContext.getCurrentRowNum() < analysisContext.getCurrentSheet().getHeadLineMun()) {
            if (analysisContext.getCurrentRowNum() <= analysisContext.getCurrentSheet().getHeadLineMun() - 1) {
                analysisContext.buildExcelHeadProperty(null,
                    (List<String>)analysisContext.getCurrentRowAnalysisResult());
                //如果是第一行。把EXCEL表头存起来。
                if (analysisContext.getExcelType() == ExcelTypeEnum.XLSX){
                    if (analysisContext.getExcelHead() == null || analysisContext.getExcelHead().size() == 0){
                        analysisContext.setExcelHead((List<String>)analysisContext.getCurrentRowAnalysisResult());
                    }
                }
            }
        } else {
            List<String> content = converter((List<String>)event.getData());
            //校验这一行的数据是不是都是null  POI中有DELETE和右键删除的差别问题。
            if (content.stream().allMatch(s -> Objects.isNull(s))){
                return ;
            }
            /** Parsing Analyze the body content **/
            analysisContext.setCurrentRowAnalysisResult(content);
            if (listeners.size() == 1) {
                analysisContext.setCurrentRowAnalysisResult(content);
            }
            /**  notify all event listeners **/
            for (Map.Entry<String, AnalysisEventListener> entry : listeners.entrySet()) {
                entry.getValue().invoke(analysisContext.getCurrentRowAnalysisResult(), analysisContext);
            }
        }
    }

    private List<String> converter(List<String> data) {
        List<String> list = new ArrayList<String>();
        if (data != null) {
            for (String str : data) {
                list.add(TypeUtil.formatFloat(str));
            }
        }
        return list;
    }

}
