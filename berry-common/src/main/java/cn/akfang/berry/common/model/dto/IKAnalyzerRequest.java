package cn.akfang.berry.common.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IKAnalyzerRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String words;
}
