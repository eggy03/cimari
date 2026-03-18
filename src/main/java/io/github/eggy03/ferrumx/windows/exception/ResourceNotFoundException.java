package io.github.eggy03.ferrumx.windows.exception;

import lombok.experimental.StandardException;

/**
 * Used to indicate that a critical resource could not be resolved, without which, it is impossible to
 * proceed with further operations.
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 4.1.0
 */
@StandardException
public class ResourceNotFoundException extends RuntimeException {
}
