package cn.akfang.berry.common.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EsVideoMetaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Float score;
    private String highlightContent;
    private String tags;

}
