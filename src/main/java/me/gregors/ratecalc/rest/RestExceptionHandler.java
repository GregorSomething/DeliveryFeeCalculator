package me.gregors.ratecalc.rest;

import me.gregors.ratecalc.rules.CalculationRuleException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Specifies how to handel CalculationRuleException when thrown in controller.
     * @param e exception
     * @return response object, with error message and status 204, because this error was expected form rules.
     */
    @ExceptionHandler(CalculationRuleException.class)
    protected ResponseEntity<?> handelFailedCalculation(CalculationRuleException e) {
        return new StatusResponse(HttpStatus.ACCEPTED, e.getMessage()).toResponseObj();
    }

    /**
     * Specifies how to handel IllegalArgumentException when thrown in controller
     * @param e exception
     * @return response object, with error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handelFailedCalculation(IllegalArgumentException e) {
        return new StatusResponse(HttpStatus.BAD_REQUEST, e.getMessage()).toResponseObj();
    }

    /**
     * Specifies how to handel NumberFormatException when thrown in controller
     * @param e exception
     * @return response object, with error message
     */
    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<?> handelFailedCalculation(NumberFormatException e) {
        return new StatusResponse(HttpStatus.BAD_REQUEST, e.getMessage()).toResponseObj();
    }
}
