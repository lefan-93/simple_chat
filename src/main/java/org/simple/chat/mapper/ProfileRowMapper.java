package org.simple.chat.mapper;

import org.simple.chat.model.dto.response.ProfileDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileRowMapper implements RowMapper<ProfileDto> {

    @Override
    public ProfileDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        var user = new ProfileDto();
        user.setId(rs.getString("id"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("username"));
        return user;
    }
}
