package com.doctor.aspirin.demo.common.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import com.doctor.aspirin.demo.common.IEnumDescription;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月7日 上午11:06:03
 */
public abstract class AbstractIEnumDescriptionHandler extends BaseTypeHandler<IEnumDescription> {
    private Class<IEnumDescription> type;

    @SuppressWarnings("unchecked")
    public AbstractIEnumDescriptionHandler() {
        MappedTypes annotation = getClass().getAnnotation(MappedTypes.class);
        if (annotation == null) {
            throw new RuntimeException("typehandler:" + getClass().getName() + " MappedTypes annotation value is empty ");
        }

        type = (Class<IEnumDescription>) annotation.value()[0];

    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IEnumDescription parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getIndex());

    }

    @Override
    public IEnumDescription getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int index = rs.getInt(columnName);
        return IEnumDescription.of(type, index);
    }

    @Override
    public IEnumDescription getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int index = rs.getInt(columnIndex);
        return IEnumDescription.of(type, index);
    }

    @Override
    public IEnumDescription getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int index = cs.getInt(columnIndex);
        return IEnumDescription.of(type, index);
    }

}
