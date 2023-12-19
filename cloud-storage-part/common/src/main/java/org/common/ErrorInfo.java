package org.common;


/**
 * The record represents information about an error that occurred during the execution
 * of a command. It contains a flag indicating whether an error occurred, and an error message
 * providing more details about the error.
 */
public record ErrorInfo(Boolean hasError, String errorMessage) {
}
