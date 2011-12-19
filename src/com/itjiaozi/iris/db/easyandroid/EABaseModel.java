package com.itjiaozi.iris.db.easyandroid;

import java.lang.reflect.Field;

import android.database.Cursor;

import com.itjiaozi.iris.db.easyandroid.EADBField.EADBFieldMode;
import com.itjiaozi.iris.db.easyandroid.EADBField.EADBFieldType;

public class EABaseModel {
    @EADBField
    public long rowid;
    
    public void fill(BaseEASQLiteOpenHelper db, String sql) {
        Class eaDBTableClass = getClass();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            Field[] fs = EATool.getField(eaDBTableClass, EADBField.class);
            for (Field t : fs) {
                t.setAccessible(true);
                int columnIndex = cursor.getColumnIndex(t.getName());
                if (-1 == columnIndex) {
                    continue;
                } else {
                    try {
                        Class fieldType = t.getType();
                        if (fieldType == String.class) {
                            t.set(this, cursor.getString(columnIndex));
                        } else if (fieldType == Byte.class) {
                            t.setByte(this, (byte) cursor.getInt(columnIndex));
                        } else if (fieldType == Short.class) {
                            t.setShort(this, (short) cursor.getInt(columnIndex));
                        } else if (fieldType == Integer.class) {
                            t.setInt(this, (short) cursor.getInt(columnIndex));
                        } else if (fieldType == Long.class) {
                            t.setLong(this, (short) cursor.getLong(columnIndex));
                        } else if (fieldType == Float.class) {
                            t.setFloat(this, (short) cursor.getFloat(columnIndex));
                        } else if (fieldType == Double.class) {
                            t.setDouble(this, cursor.getDouble(columnIndex));
                        } else if (fieldType == byte[].class) {
                            t.set(this, cursor.getBlob(columnIndex));
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean store(BaseEASQLiteOpenHelper db) {
//        try {
//            ContentValues values = new ContentValues();
//            Class eaDBTableClass = getClass();
//            Field[] fields = EATool.getField(eaDBTableClass, EADBField.class);
//            for (Field f : fields) {
//                f.setAccessible(true);
//                Object tmp;
//                tmp = f.get(this);
//                if (tmp instanceof Integer) {
//                    values.put(f.getName(), (Integer) tmp);
//                } else if (tmp instanceof Long) {
//                    values.put(f.getName(), (Long) tmp);
//                } else if (tmp instanceof String) {
//                    values.put(f.getName(), (String) tmp);
//
//                } else if (tmp instanceof Float) {
//                    values.put(f.getName(), (Float) tmp);
//                } else if (tmp instanceof Byte) {
//                    values.put(f.getName(), (Byte) tmp);
//                } else if (tmp instanceof byte[]) {
//                    values.put(f.getName(), (byte[]) tmp);
//
//                } else if (tmp instanceof Boolean) {
//                    values.put(f.getName(), (Boolean) tmp);
//                } else if (tmp instanceof Double) {
//                    values.put(f.getName(), (Double) tmp);
//                } else if (tmp instanceof Short) {
//                    values.put(f.getName(), (Short) tmp);
//                }
//            }
//            String keyFieldName = EATool.getDbKeyFieldName(this.getClass());
//            
//            values.remove(keyFieldName);
//            db.update(eaDBTableClass, values, whereClause, whereArgs)
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
        return false;
    }

    public String encodeToJson() {
        return null;
    }

    public <T> T decodeFromJson(String jsonStr) {
        Class eaDBTableClass = getClass();
        Field[] fs = EATool.getField(eaDBTableClass, EAJson.class);

        return null;
    }

    public String createTableSql() {
        Class eaDBTableClass = getClass();
        String tableName = eaDBTableClass.getSimpleName();

        Field[] fs = EATool.getField(eaDBTableClass, EADBField.class);
        EADBField[] ans = new Object() {
            public EADBField[] getAnn(Field[] fs) {
                EADBField[] ts = new EADBField[fs.length];
                for (int i = 0; i < fs.length; i++) {
                    ts[i] = (EADBField) fs[i].getAnnotation(EADBField.class);
                }
                return ts;
            }
        }.getAnn(fs);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ans.length; i++) {
            Field tF = fs[i];
            EADBField tA = ans[i];

            EADBFieldType type = tA.type();
            EADBFieldMode[] model = tA.mode();

            boolean isKey = false;
            boolean isUnique = false;
            boolean notNull = false;
            for (EADBFieldMode t : model) {
                isKey = isKey ? true : (t == EADBFieldMode.Key);
                isUnique = isUnique ? true : (t == EADBFieldMode.Unique);
                notNull = t == EADBFieldMode.NotNull;
            }

            String fieldNameInDB = "".equals(tA.fieldName()) ? tF.getName() : tA.fieldName();
            // TODO 需要正确设置类型
            String fieldTypeInDB = EADBFieldType.Default == type ? "" : type.toString();
            String fieldIsKey = isKey ? " INTEGER PRIMARY KEY" : "";
            String fieldIsUnique = isUnique ? " UNIQUE" : "";
            String fieldAllowNull = notNull ? " NOT NULL" : "";

            String partSql = String.format("%s %s%s%s%s", fieldNameInDB, fieldTypeInDB, fieldIsKey, fieldIsUnique, fieldAllowNull);
            sb.append(partSql + ((i + 1) == ans.length ? "" : ","));
        }
        String createTableSql = String.format("CREATE TABLE %s(%s);", tableName, sb);
        System.out.println(createTableSql);
        return createTableSql;
    }

    public String dropTableSql() {
        Class eaDBTableClass = getClass();
        String dropTableSql = "DROP TABLE IF EXISTS " + eaDBTableClass.getSimpleName() + ";";
        return dropTableSql;
    }
}
