package com.itjiaozi.iris.db.easyandroid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.itjiaozi.iris.db.easyandroid.EADBField.EADBFieldMode;

public class EATool {
    public static Field[] getField(Class cls, Class an) {
        Field[] fs = cls.getDeclaredFields();
        List<Field> list = new ArrayList<Field>();
        for (Field t : fs) {
            if (t.isAnnotationPresent(an)) {
                list.add(t);
            }
        }
        return list.toArray(new Field[0]);
    }

    public static String getDbKeyFieldName(Class cls) {
        Field[] fs = getField(cls, EADBField.class);
        for (Field field : fs) {
            Annotation[] as = field.getDeclaredAnnotations();
            for (Annotation annotation : as) {
                if (annotation instanceof EADBField) {
                    EADBField eaf = (EADBField) annotation;
                    if (eaf.mode().equals(EADBFieldMode.Key)) {
                        return field.getName();
                    }
                }
            }
        }
        return "rowid";
    }
}
