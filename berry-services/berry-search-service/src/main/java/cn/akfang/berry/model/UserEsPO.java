package cn.akfang.berry.model;


import cn.akfang.berry.common.constants.EsConstants;
import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.Analyzer;
import org.dromara.easyes.annotation.rely.FieldType;
import org.dromara.easyes.annotation.rely.IdType;

@Data
@IndexName(EsConstants.USER_INDEX)
public class UserEsPO {

    @IndexId(type = IdType.CUSTOMIZE)
    private String id;
    @IndexField(fieldType = FieldType.LONG)
    private Long userId;
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String nickName;
}
