package cn.akfang.berry.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long fileId;
    private String content;
    private List<String> tags;
    private Long authorId;
    private Long channelId;
    private Integer visible;
}
