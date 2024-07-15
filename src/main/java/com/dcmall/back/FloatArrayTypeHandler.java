package com.dcmall.back;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes(ArrayList.class)
public class FloatArrayTypeHandler implements TypeHandler<ArrayList<Double>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, ArrayList<Double> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            Double[] array = parameter.toArray(new Double[0]);
            Array sqlArray = ps.getConnection().createArrayOf("float8", array);
            ps.setArray(i, sqlArray);
        } else {
            ps.setNull(i, Types.ARRAY);
        }
    }

    @Override
    public ArrayList<Double> getResult(ResultSet rs, String columnName) throws SQLException {
        Array sqlArray = rs.getArray(columnName);
        if (sqlArray != null) {
            Double[] array = (Double[]) sqlArray.getArray();
            return new ArrayList<>(Arrays.asList(array));
        }
        return null;
    }

    @Override
    public ArrayList<Double> getResult(ResultSet rs, int columnIndex) throws SQLException {
        Array sqlArray = rs.getArray(columnIndex);
        if (sqlArray != null) {
            Double[] array = (Double[]) sqlArray.getArray();
            return new ArrayList<>(Arrays.asList(array));
        }
        return null;
    }

    @Override
    public ArrayList<Double> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array sqlArray = cs.getArray(columnIndex);
        if (sqlArray != null) {
            Double[] array = (Double[]) sqlArray.getArray();
            return new ArrayList<>(Arrays.asList(array));
        }
        return null;
    }
}
