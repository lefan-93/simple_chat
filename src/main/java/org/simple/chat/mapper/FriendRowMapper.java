package org.simple.chat.mapper;

import org.simple.chat.model.dto.response.FriendDto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendRowMapper implements RowMapper<FriendDto> {

    @Override
    public FriendDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        var user = new FriendDto();
        user.setId(rs.getString("id"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("username"));
        return user;
}
}
