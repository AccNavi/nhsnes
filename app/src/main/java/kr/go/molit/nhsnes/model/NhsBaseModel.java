package kr.go.molit.nhsnes.model;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by jongrakmoon on 2017. 4. 1..
 */

public class NhsBaseModel implements Serializable{
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");

        Class tmpClass = getClass();

        do {
            Field[] fields = tmpClass.getDeclaredFields();

            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object data = field.get(this);
                    if (data != null) {
                        builder.append(field.getName())
                                .append(":")
                                .append(data.toString())
                                .append(",");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        } while ((tmpClass = tmpClass.getSuperclass()) != null);

        builder.deleteCharAt(builder.lastIndexOf(","))
                .append("}");
        return builder.toString();
    }
}
