package com.zaizai.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.zaizai.exceptions.UserServiceException;
import com.zaizai.model.request.UserEditRequestModel;
import com.zaizai.model.response.ErrorMessages;
import com.zaizai.model.response.OperationName;
import com.zaizai.model.response.OperationStatusModel;
import com.zaizai.model.response.RequestOperationStatus;
import com.zaizai.model.response.UserEditResponseModel;
import com.zaizai.service.UserService;
import com.zaizai.shared.UserDto;

@RestController
@RequestMapping("users")
public class UserController {

	private UserService _userService;

	public UserController(UserService userService) {
		_userService = userService;
	}

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserEditResponseModel getUser(@PathVariable String id) {

		UserDto user = _userService.getUserByUserId(id);
		UserEditResponseModel response = new UserEditResponseModel();
		BeanUtils.copyProperties(user, response);
		return response;
	}

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserEditResponseModel> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "20") int limit) {

		List<UserEditResponseModel> response = new ArrayList<>();

		List<UserDto> users = _userService.getUsers(page, limit);

		for (UserDto user : users) {
			UserEditResponseModel model = new UserEditResponseModel();
			BeanUtils.copyProperties(user, model);
			response.add(model);
		}

		return response;
	}

	@PostMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserEditResponseModel createUser(@RequestBody UserEditRequestModel userEdit) throws Exception {

		if (userEdit.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEdit, userDto);

		UserDto createdUser = _userService.createUser(userDto);

		UserEditResponseModel response = new UserEditResponseModel();
		BeanUtils.copyProperties(createdUser, response);

		return response;
	}

	@PutMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserEditResponseModel updateUser(@PathVariable String id, @RequestBody UserEditRequestModel userEdit) {

		if (userEdit.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEdit, userDto);

		UserDto editedUser = _userService.updateUser(id, userDto);

		UserEditResponseModel response = new UserEditResponseModel();
		BeanUtils.copyProperties(editedUser, response);

		return response;
	}

	@DeleteMapping(path = "/{id}")
	public OperationStatusModel deleteUser(@PathVariable String id) {

		_userService.deleteUser(id);
		OperationStatusModel returnModel = new OperationStatusModel(OperationName.DELETE.name(),
				RequestOperationStatus.SUCCESS.name());
		return returnModel;
	}
}
