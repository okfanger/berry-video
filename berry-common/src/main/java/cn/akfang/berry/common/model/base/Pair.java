package cn.akfang.berry.common.model.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class Pair<A, B> implements Serializable {
    private static final long serialVersionUID = 1L;
    private A a;
    private B b;
}
