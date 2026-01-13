package com.studyjava.typehandler;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes({List.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class LongListTypeHandler extends BaseTypeHandler<List<Long>> {
  @Override
  public void setNonNullParameter(
      PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, parameter.stream().map(String::valueOf).collect(Collectors.joining(",")));
  }

  @Override
  public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    if (value == null || value.isEmpty()) return Collections.emptyList();
    return Arrays.stream(value.split(","))
        .filter(s -> !s.isEmpty())
        .map(Long::parseLong)
        .collect(Collectors.toList());
  }

  @Override
  public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    if (value == null || value.isEmpty()) return Collections.emptyList();
    return Arrays.stream(value.split(","))
        .filter(s -> !s.isEmpty())
        .map(Long::parseLong)
        .collect(Collectors.toList());
  }

  @Override
  public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String value = cs.getString(columnIndex);
    if (value == null || value.isEmpty()) return Collections.emptyList();
    return Arrays.stream(value.split(","))
        .filter(s -> !s.isEmpty())
        .map(Long::parseLong)
        .collect(Collectors.toList());
  }
}
