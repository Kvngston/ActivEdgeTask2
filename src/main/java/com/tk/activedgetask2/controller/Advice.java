package com.tk.activedgetask2.controller;

import com.tk.activedgetask2.entity.dto.Error;
import com.tk.activedgetask2.entity.dto.Response;
import com.tk.activedgetask2.entity.enums.ResponseCodes;
import com.tk.activedgetask2.exception.NotFoundException;
import com.tk.activedgetask2.exception.SecurityException;
import com.tk.activedgetask2.exception.UptimeException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice(value = "com.tk")
public class Advice extends ResponseEntityExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		var response = new Response<Error>();
		response.setResponseCode(ResponseCodes.Bad_Request.getCode());
		response.setResponseMessage("Error parsing request");
		logger.error(String.valueOf(ex));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		var response = new Response<Error>();
		response.setResponseCode(ResponseCodes.Bad_Request.getCode());
		response.setResponseMessage("Parameters missing");
		logger.error(String.valueOf(ex));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		var response = new Response<Error>();
		response.setResponseCode(ResponseCodes.Bad_Request.getCode());
		response.setResponseMessage("Request Body is required");
		logger.error(String.valueOf(ex));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		var response = new Response<Error>();
		response.setResponseCode(ResponseCodes.Bad_Request.getCode());
		response.setResponseMessage("Validation errors occurred");
		BindingResult result = ex.getBindingResult();
		List<ObjectError> objectErrors = result.getAllErrors();
		List<Error> errors = new ArrayList<>();
		for (ObjectError objectError : objectErrors) {
			if (objectError instanceof FieldError) {
				FieldError fieldError = (FieldError) objectError;
				errors.add(new Error(fieldError.getField(), fieldError.getDefaultMessage()));
			} else {
				errors.add(new Error(objectError.getObjectName(), objectError.getDefaultMessage()));
			}
		}
		response.setErrors(errors);
		logger.error(String.valueOf(ex));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}



	@ExceptionHandler(InvalidParameterException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> handleInvalidParameterException(InvalidParameterException e,
	                                                         HttpServletRequest request) {
		var response = new Response<Error>();
		response.setResponseCode(ResponseCodes.Bad_Request.getCode());
		response.setResponseMessage("Invalid Parameters");
		logger.error(String.valueOf(e));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SecurityException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ResponseEntity<?> handleSecurityQuestionsException(SecurityException e, HttpServletRequest request) {
		return commonResponse(e, request);
	}

	@ExceptionHandler(UptimeException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleUptimeException(UptimeException e, HttpServletRequest request) {
		return commonResponse(e, request);
	}

	@ExceptionHandler(DateTimeParseException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleDateParseException(DateTimeParseException e, HttpServletRequest request) {
		var response = new Response<Error>();
		response.setResponseCode("AO1");
		response.setResponseMessage("Invalid Date format");
		log(request, e);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ResponseEntity<?> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
		var response = new Response<Error>();
		response.setResponseCode(ResponseCodes.Not_Found.getCode());
		response.setResponseMessage(e.getMessage());
		response.setErrors(null);
		log(request, e);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ResourceAccessException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<?> handleResourceAccessException(ResourceAccessException e, HttpServletRequest request) {
		var response = new Response<Error>();
		response.setResponseCode(ResponseCodes.Bad_Request.getCode());
		response.setResponseMessage("Unable to reach remote service");
		log(request, e);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
		var response = new Response<Error>();
		response.setResponseCode(ResponseCodes.Bad_Request.getCode());
		response.setResponseMessage("An error Occurred");
		log(request, e);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void log(HttpServletRequest request, Throwable e) {
		logger.error("Request [" + request.getRequestURI() + "] raised: ", e);
		logger.error(e.getMessage());
	}


	private ResponseEntity<?> commonResponse(Exception e, HttpServletRequest request) {
		var response = new Response<Error>();
		response.setResponseCode(ResponseCodes.Bad_Request.getCode());
		response.setResponseMessage(e.getMessage());
		response.setErrors(null);
		log(request, e);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
