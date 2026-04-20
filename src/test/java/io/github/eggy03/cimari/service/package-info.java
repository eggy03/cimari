/**
 * Unit tests for {@link io.github.eggy03.cimari.service.compounded.HardwareIdService}.
 *
 * <p>
 * This test suite verifies the behavior of the service layer in isolation by mocking its
 * dependencies:
 * <ul>
 *     <li>{@link io.github.eggy03.cimari.terminal.TerminalService} – responsible for executing the underlying script</li>
 *     <li>{@link io.github.eggy03.cimari.mapping.CommonMappingInterface} – responsible for mapping JSON output to domain objects</li>
 * </ul>
 * </p>
 *
 * <p>
 * The tests ensure that the service:
 * <ul>
 *     <li>Invokes the correct enums when executing the terminal command</li>
 *     <li>Delegates JSON deserialization to the mapper</li>
 *     <li>Returns the mapper result without modification</li>
 *     <li>Propagates exceptions thrown by the mapper</li>
 *     <li>Correctly handles empty results</li>
 * </ul>
 * </p>
 *
 * @since 0.1.0
 */
package io.github.eggy03.cimari.service;