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

    String hash;
    String key;
    String title;
    List<String> tags;
    Long authorId;
    Integer visible;
}
