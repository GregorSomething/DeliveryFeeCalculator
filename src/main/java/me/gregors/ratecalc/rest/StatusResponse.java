package me.gregors.ratecalc.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Object to represent general status response with message, eg error
 * @param status status display and use
 * @param message  message that will be in response
 */
public record StatusResponse(HttpStatus status, String message) {

    /**
     * Converts this object to response object
     * @return response object with data in this class.
     */
    public ResponseEntity<StatusResponse> toResponseObj() {
        return new ResponseEntity<>(this, this.status);
    }
}
