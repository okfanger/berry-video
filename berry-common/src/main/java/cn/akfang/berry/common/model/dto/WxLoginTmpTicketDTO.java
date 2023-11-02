package cn.akfang.berry.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WxLoginTmpTicketDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ticket;
    private Integer expire_seconds;
    private String url;
    private String id;
}
