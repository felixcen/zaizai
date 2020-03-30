package com.zaizai.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

import com.zaizai.exceptions.UserServiceException;
import com.zaizai.io.entity.UserEntity;
import com.zaizai.io.repository.UserRepository;
import com.zaizai.service.UserService;
import com.zaizai.shared.UserDto;
import com.zaizai.shared.Utils;
import com.zaizai.shared.ZaizaiEncryptor;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository _userRepository;
	private Utils _utils;
	private ZaizaiEncryptor _encryptor;

	public UserServiceImpl(UserRepository userRepository, Utils utils, ZaizaiEncryptor encryptor) {
		_userRepository = userRepository;
		_utils = utils;
		_encryptor = encryptor;
	}

	@Override
	public UserDto createUser(UserDto user) {

		UserEntity exist = _userRepository.findByEmail(user.getEmail());
		if (exist != null) {
			throw new UserServiceException("User already exists.");
		}

		UserEntity userEntity = new UserEntity();

		BeanUtils.copyProperties(user, userEntity);

		String userId = _utils.generateUserId(14);

		userEntity.setEncryptedPassword(_encryptor.bCryptPasswordEncoder().encode(user.getPassword()));
		userEntity.setUserId(userId);

		UserEntity created = _userRepository.save(userEntity);

		UserDto returnValue = new UserDto();

		BeanUtils.copyProperties(created, returnValue);

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = _userRepository.findByEmail(username);

		if (user == null)
			throw new UsernameNotFoundException(username);

		return new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity user = _userRepository.findByEmail(email);
		if (user == null)
			throw new UsernameNotFoundException(email);

		UserDto returnValue = new UserDto();

		BeanUtils.copyProperties(user, returnValue);
		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String userId) {

		UserEntity user = _userRepository.findByUserId(userId);

		if (user == null)
			throw new UserServiceException("User with ID '" + userId + "' was not found");
		;

		UserDto returnValue = new UserDto();

		BeanUtils.copyProperties(user, returnValue);

		return returnValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto user) {
		UserEntity userEdit = _userRepository.findByUserId(userId);
		if (userEdit == null) {
			throw new UserServiceException("User was not found.");
		}

		userEdit.setFirstName(user.getFirstName());
		userEdit.setLastName(user.getLastName());

		UserEntity edited = _userRepository.save(userEdit);

		UserDto returnValue = new UserDto();

		BeanUtils.copyProperties(edited, returnValue);

		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userDelete = _userRepository.findByUserId(userId);
		if (userDelete == null) {
			throw new UserServiceException("User was not found.");
		}
		_userRepository.delete(userDelete);

	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> toReturn = new ArrayList<>();

		if(page>0)
			page -=1;
		
		Pageable pageRequest = PageRequest.of(page, limit);
		Page<UserEntity> usersPaged =  _userRepository.findAll(pageRequest);
		List<UserEntity> users = usersPaged.getContent();
		
		for(UserEntity user : users) {
	
			UserDto returnValue = new UserDto();
			BeanUtils.copyProperties(user, returnValue);
			toReturn.add(returnValue);
		}
		
		return toReturn;
	}

}
