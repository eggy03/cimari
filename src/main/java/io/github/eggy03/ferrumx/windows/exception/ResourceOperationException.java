package io.github.eggy03.ferrumx.windows.exception;

import lombok.experimental.StandardException;

/**
 * Usually thrown to indicate that necessary I/O operations on a resource has failed
 *
 *
 * @since 4.1.0
 */
@StandardException
public class ResourceOperationException extends RuntimeException {
}
