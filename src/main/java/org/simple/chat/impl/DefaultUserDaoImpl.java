package org.simple.chat.impl;

import lombok.AllArgsConstructor;
import org.simple.chat.UserDao;
import org.simple.chat.mapper.FriendRowMapper;
import org.simple.chat.mapper.ProfileRowMapper;
import org.simple.chat.model.dto.response.FriendDto;
import org.simple.chat.model.dto.response.ProfileDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class DefaultUserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<FriendDto> findUserByName(String username) {

        return jdbcTemplate.query("SELECT " +
            "id, username, email " +
            "FROM keycloak.user_entity " +
            "WHERE to_tsvector(username) @@ to_tsquery(?)", new FriendRowMapper(), username);
    }

    @Override
    public ProfileDto findUserById(String id) {
        return jdbcTemplate.queryForObject("SELECT " +
            "id, username, email " +
            "FROM keycloak.user_entity " +
            "WHERE id = ?", new ProfileRowMapper(), id);
    }
}
