package cn.akfang.berry.model;

import cn.akfang.berry.common.constants.EsConstants;
import lombok.Data;
import org.dromara.easyes.annotation.*;
import org.dromara.easyes.annotation.rely.Analyzer;
import org.dromara.easyes.annotation.rely.FieldType;
import org.dromara.easyes.annotation.rely.IdType;

@Data
@IndexName(EsConstants.VIDEO_INDEX)
public class VideoEsPO {
    @IndexId(type = IdType.CUSTOMIZE)
    private String id;
    @IndexField(fieldType = FieldType.LONG)
    private Long videoId;
    @HighLight(mappingField = "highlightContent")
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String content;
    @IndexField(fieldType = FieldType.KEYWORD, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_SMART)
    private String tags;

    @IndexField(fieldType = FieldType.DATE, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    private String highlightContent;

    @Score
    private Float score;
}