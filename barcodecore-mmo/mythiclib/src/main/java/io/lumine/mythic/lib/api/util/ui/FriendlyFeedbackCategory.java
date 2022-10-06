package io.lumine.mythic.lib.api.util.ui;

/**
 * When you must tell the user something, what kind of topic does it relate to?
 *
 * @author Gunging
 */
public enum FriendlyFeedbackCategory {

    /**
     * Messages sent when the whole operation reached completion.
     */
    SUCCESS,

    /**
     * The list of messages concerning why the operation did not reach completion.
     * <p></p>
     * These <i>errors</i> are not really a fault of the user, in fact,
     * despite the operation not reaching completion, it is working
     * as it is supposed to (perhaps it was stopped by certain conditions),
     * and sometimes this same input produces a success.
     * <p></p>
     * Like the operation stopping due to a 'failed' RNG roll, that could
     * have been a success in a more lucky occasion.
     * @see #ERROR
     */
    FAILURE,

    /**
     * A list of neutral messages that you think the user should know about.
     * <p></p>
     * Like something deprecated or a soft warning.
     */
    INFORMATION,

    /**
     * messages concerning serious syntax errors.
     * <p></p>
     * These errors will never get fixed unless the user themselves
     * correct their input - Under no circumstances their input is
     * correct.
     * <p></p>
     * Like attempting to parse '<code>Potato</code>' as a number.
     * @see #FAILURE
     */
    ERROR,

    /**
     * Avoid using as much as possible.
     */
    OTHER
}
