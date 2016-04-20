package com.doctor.aspirin.demo.common;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月7日 上午11:02:49
 * 
 *         为了方便mybatis统一处理enum类型，让其有共同接口
 */
public interface IEnumDescription {
    public int getIndex();

    public String getName();

    public String getDescription();

    public static <T extends IEnumDescription> T of(Class<T> type, int index) {
        T[] constants = type.getEnumConstants();
        for (T t : constants) {
            if (t.getIndex() == index) {
                return t;
            }
        }
        throw new RuntimeException(type + " not have a valid index value :" + index);
    }
}
