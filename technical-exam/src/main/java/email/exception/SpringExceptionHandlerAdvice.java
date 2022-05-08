package email.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import email.web.api.error.ErrorMessage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class SpringExceptionHandlerAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringExceptionHandlerAdvice.class);

    private MessageSource messageSource;

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, List<ErrorMessage>> handleException(MethodArgumentNotValidException exception) {
        return transformBindErrors(exception.getBindingResult());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, List<ErrorMessage>> handleException(BadRequestException exception) {
        return createErrorMessage(exception.getMessage());
    }

    private Map<String, List<ErrorMessage>> transformBindErrors(BindingResult result) {
        return createErrorsMap(result.getFieldErrors().stream()
                .sorted(Comparator.comparing(FieldError::getField)
                        .thenComparing(FieldError::getDefaultMessage))
                .map(this::resolveErrors)
                .collect(Collectors.toList()));
    }

    static Map<String, List<ErrorMessage>> createErrorsMap(List<ErrorMessage> errors) {
        return Map.of("errors", errors);
    }

    static Map<String, List<ErrorMessage>> createErrorMessage(String msg) {
        LOGGER.info(msg);
        return createErrorsMap(Collections.singletonList(ErrorMessage.builder().msg(msg).build()));
    }

    private ErrorMessage resolveErrors(FieldError fe) {
        String localizedErrorMessage = messageSource.getMessage(fe, LocaleContextHolder.getLocale());
        return ErrorMessage.builder().msg(localizedErrorMessage).build();
    }
}
