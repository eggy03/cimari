package io.github.eggy03.ferrumx.windows.exception;

import lombok.experimental.StandardException;

/**
 * Indicates that a given annotation is not found during runtime.
 *
 *
 * @since 4.1.0
 */
@StandardException
public class AnnotationNotFoundException extends RuntimeException {
}
