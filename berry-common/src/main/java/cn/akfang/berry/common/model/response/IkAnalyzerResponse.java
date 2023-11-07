package cn.akfang.berry.common.model.response;

import lombok.Data;

import java.util.List;

@Data
public class IkAnalyzerResponse {
    private List<Tokens> tokens;

    @Data
    public static class Tokens {
        private String token;
        private int start_offset;
        private int end_offset;
        private String type;
        private int position;
    }

}