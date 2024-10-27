package com.interview.etravli.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.interview.etravli.dto.etraveli.EtraveliExceptionDTO;
import com.interview.etravli.enums.ExceptionMessages;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class EtraveliExceptionHanding {

    private static final Logger LOGGER = LoggerFactory.getLogger(EtraveliExceptionHanding.class);

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<EtraveliExceptionDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        EtraveliExceptionDTO error = new EtraveliExceptionDTO();
        error.setMessage(ExceptionMessages.DATA_INTEGRITY_VIOLATION.name());
        error.setCode(400);
        error.setStatus(HttpStatus.BAD_REQUEST);
        logException(e.getStackTrace(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<EtraveliExceptionDTO> handleValidationExceptions(Exception e) {
        EtraveliExceptionDTO error = new EtraveliExceptionDTO();
        String detailedErrorMessage = "Unknown validation error";
        if (e instanceof MethodArgumentNotValidException ex) {
            detailedErrorMessage = ex.getBindingResult().getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else if (e instanceof ConstraintViolationException ex) {
            detailedErrorMessage = ex.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
        }
        error.setMessage(detailedErrorMessage);
        error.setCode(400);
        error.setStatus(HttpStatus.BAD_REQUEST);
        logException(e.getStackTrace(), e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JWTVerificationException.class})
    public ResponseEntity<EtraveliExceptionDTO> handleJWTVerificationException(JWTVerificationException e) {
        EtraveliExceptionDTO error = new EtraveliExceptionDTO();
        error.setMessage(e.getMessage());
        error.setCode(403);
        error.setStatus(HttpStatus.FORBIDDEN);
        logException(e.getStackTrace(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<EtraveliExceptionDTO> handleBadRequestException(BadRequestException e) {
        EtraveliExceptionDTO error = new EtraveliExceptionDTO();
        error.setMessage(e.getMessage());
        error.setCode(400);
        error.setStatus(HttpStatus.BAD_REQUEST);
        logException(e.getStackTrace(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({FeignException.class})
    public ResponseEntity<EtraveliExceptionDTO> handleFeignException(FeignException e) {
        EtraveliExceptionDTO error = new EtraveliExceptionDTO();
        if (e.status() == 429) {
            error.setMessage(ExceptionMessages.EXTERNAL_API_RATE_LIMIT_EXCEEDED.toString());
            error.setCode(429);
            error.setStatus(HttpStatus.TOO_MANY_REQUESTS);
            logException(e.getStackTrace(), e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.TOO_MANY_REQUESTS);
        }
        error.setMessage(ExceptionMessages.EXTERNAL_API_ERROR.toString());
        error.setCode(e.status());
        error.setStatus(HttpStatus.valueOf(e.status()));
        logException(e.getStackTrace(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.valueOf(e.status()));
    }

    @ExceptionHandler({AuthenticationServiceException.class})
    public ResponseEntity<EtraveliExceptionDTO> handleAuthenticationException(AuthenticationServiceException e) {
        EtraveliExceptionDTO error = new EtraveliExceptionDTO();
        error.setMessage(e.getMessage());
        error.setCode(500);
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        logException(e.getStackTrace(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AuthorizationException.class})
    public ResponseEntity<EtraveliExceptionDTO> handleAuthorizationException(AuthorizationException e) {
        EtraveliExceptionDTO error = new EtraveliExceptionDTO();
        error.setMessage(e.getMessage());
        error.setCode(500);
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        logException(e.getStackTrace(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<EtraveliExceptionDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        EtraveliExceptionDTO error = new EtraveliExceptionDTO();
        error.setMessage(e.getMessage());
        error.setCode(404);
        error.setStatus(HttpStatus.NOT_FOUND);
        logException(e.getStackTrace(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<EtraveliExceptionDTO> handleGenericException(Exception e) {
        EtraveliExceptionDTO error = new EtraveliExceptionDTO();
        error.setMessage("Oops something went wrong. Please contact your administrator.");
        error.setCode(500);
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        logException(e.getStackTrace(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logException(StackTraceElement[] stackTrace, String customMessage) {
        try {
            String stackTraceString = Arrays.stream(stackTrace)
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n\t"));
            LOGGER.error(customMessage);
            LOGGER.error(stackTraceString);
        } catch (Exception ex) {
            LOGGER.error(customMessage);
        }
    }


}
