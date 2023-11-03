package cn.akfang.berry.model;

import cn.akfang.berry.common.constants.EsConstants;
import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.Analyzer;
import org.dromara.easyes.annotation.rely.FieldType;

@Data
@IndexName(EsConstants.VIDEO_INDEX)
public class VideoEsPO {
    @IndexId
    private String id;
    @IndexField(fieldType = FieldType.LONG)
    private Long videoId;
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String content;
}