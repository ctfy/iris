package com.itjiaozi.iris.db.easyandroid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EAJson {
    // ///////////
    // 枚举类型定义
    // ///////////
    /**
     * 解析方式
     */
    public static enum EAJsonMode {
        /** 该字段将支持编码和解码(java对象<->json字符串) */
        Both,
        /** 仅支持编码(java对象->json字符串) */
        Encode,
        /** 仅支持解码 (json字符串->java对象) */
        Decode
    };

    // ///////////
    // 公共方法定义
    // ///////////
    /**
     * json特性： <br/>
     * EAJsonMode.Both 支持java对象<->json字符串 EAJsonMode.Encode 支持java对象->json字符串
     * EAJsonMode.Decode 支持java对象<-json字符串
     */
    public EAJsonMode mode() default EAJsonMode.Both;

    /**
     * json字符串中该字段的主键名，默认值(空字符串)表示取在java类该字段的名字
     */
    public String jsonKey() default "";
}