package cn.akfang.berry.manager.wxmp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WxMpConstant {
    CLICK_MENU_KEY("CLICK_MENU_KEY"),
    WX_AUTH_CODE_KEY("WX_AUTH_CODE_KEY");
    private final String value;
}
