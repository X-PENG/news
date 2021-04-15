package com.peng.news.mybatisTypeHandler;

import com.peng.news.model.enums.NewsStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理NewsStatus的类型处理器
 * @author PENG
 * @version 1.0
 * @date 2021/4/14 17:50
 */
public class NewsStatusTypeHandler extends BaseTypeHandler<NewsStatus> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, NewsStatus newsStatus, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, newsStatus.getCode());
    }

    @Override
    public NewsStatus getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return NewsStatus.fromCode(resultSet.getInt(columnName));
    }

    @Override
    public NewsStatus getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return NewsStatus.fromCode(resultSet.getInt(columnIndex));
    }

    @Override
    public NewsStatus getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return  NewsStatus.fromCode(callableStatement.getInt(columnIndex));
    }
}
