package com.itjiaozi.iris.db.easyandroid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来注释数据库表的字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EADBField {
    // ///////////
    // 枚举类型定义
    // ///////////
    /**
     * 字段特性
     */
    public static enum EADBFieldMode {
        /** 默认模式： 非主键、不唯一并且可以为空值 */
        Default,
        /** 不能为空 */
        NotNull,
        /** 主键 */
        Key,
        /** 唯一 */
        Unique,
    };

    /** 字段类型 */
    public enum EADBFieldType {
        /** 默认根据java类型映射 */
        Default(""),
        /** 整数 */
        Integer("INTEGER"),
        /** 浮点数 */
        Real("REAL"),
        /** 文本 */
        Text("TEXT"),
        /** 二进制 */
        Blob("BLOB");

        private String mValue;

        EADBFieldType(String v) {
            mValue = v;
        }

        @Override
        public String toString() {
            return mValue;
        }
    };

    // ///////////
    // 公共方法定义
    // ///////////
    /**
     * 该字段的特性是 EADBTableMode枚举的数组 <br/>
     * 如： mode = {EADBTableMode.NotNull, EADBTableMode.Unique} 表示该字段为主键，并且
     */
    public EADBFieldMode[] mode() default { EADBFieldMode.Default };

    /** EADBFieldType 枚举值之一，默认为Default更加java类型自动映射 */
    public EADBFieldType type() default EADBFieldType.Default;

    /** 数据库中的字段名(默认值为空字符串表示使用java字段名作为字段名) */
    public String fieldName() default "";
}
