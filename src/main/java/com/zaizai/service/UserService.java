package com.zaizai.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.zaizai.shared.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);
	UserDto updateUser(String userId, UserDto user);
	void deleteUser(String userId);
	UserDto getUser(String email);
	List<UserDto> getUsers(int page, int limit);
	UserDto getUserByUserId(String userId);
}
