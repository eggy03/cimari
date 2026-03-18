package io.github.eggy03.ferrumx.windows.exception;

import lombok.experimental.StandardException;

/**
 * Indicates that a given annotation is not found during runtime.
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 4.1.0
 */
@StandardException
public class AnnotationNotFoundException extends RuntimeException {
}
