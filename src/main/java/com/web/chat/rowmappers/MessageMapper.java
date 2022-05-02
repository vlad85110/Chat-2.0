package com.web.chat.rowmappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<String> {
    @Override
    public String mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getString("name");
    }
}