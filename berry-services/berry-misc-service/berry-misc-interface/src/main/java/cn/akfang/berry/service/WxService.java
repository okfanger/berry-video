package cn.akfang.berry.service;


import cn.akfang.berry.common.model.dto.WxLoginTmpTicketDTO;

public interface WxService {
    WxLoginTmpTicketDTO getWxLoginTicketInfo(String sceneStr, Integer expireTime);

    String reply(String requestXmlBody);

    Boolean checkSignature(String timestamp, String nonce, String signature);
}
