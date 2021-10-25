package org.simple.chat;

import org.simple.chat.model.dto.response.FriendDto;
import org.simple.chat.model.dto.response.ProfileDto;

import java.util.List;

public interface UserDao {

    List<FriendDto> findUserByName(String username);

    ProfileDto findUserById(String id);
}
