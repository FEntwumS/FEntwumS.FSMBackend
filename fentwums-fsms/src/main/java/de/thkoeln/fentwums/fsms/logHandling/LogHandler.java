package de.thkoeln.fentwums.fsms.logHandling;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

/**
 * This class manages setting and querying of errors, warnings and messages. 
 * Only not yet queried errors, warnings and messages can be queried.
 *
 * @author Marvin Jolk
 */
public class LogHandler {

    private static LogHandler instance;

    private List<String> errorsList;
    private int unknownErrorTracker;

    private List<String> warningList;
    private int unknownWarningTracker;

    private List<String> messageList;
    private int unknownMessageTracker;

    private JTextArea jTextAreaLog;

    private LogHandler() {
        errorsList = new ArrayList<String>();
        unknownErrorTracker = 0;

        warningList = new ArrayList<String>();
        unknownWarningTracker = 0;

        messageList = new ArrayList<String>();
        unknownMessageTracker = 0;
    }

    /**
     * This class is to get the singleton Instance of the ErrorHandler.
     *
     * @return Instance ErrorHandler
     * @author Marvin Jolk
     */
    public static LogHandler getInstance() {
        if (instance == null) {
            instance = new LogHandler();
        }
        return instance;
    }

    /**
     * Reset all Errors and Warnings
     * @author Marvin Jolk
     */
    public void reset() {
        errorsList = new ArrayList<String>();
        unknownErrorTracker = 0;

        warningList = new ArrayList<String>();
        unknownWarningTracker = 0;

        messageList = new ArrayList<String>();
        unknownMessageTracker = 0;
        if (!(jTextAreaLog == null)) {
            jTextAreaLog.setText("");
        }
    }

    public void setjTextAreaLog(JTextArea jTextAreaLog) {
        this.jTextAreaLog = jTextAreaLog;
    }

    /**
     * This class is to set/report a new Error.
     *
     * @param error Error message that shall be added
     * @author Marvin Jolk
     */
    public void addError(String error) {
        errorsList.add(error);
        if (!(jTextAreaLog == null)) {
            jTextAreaLog.append(error + System.lineSeparator());
        }
    }

    /**
     * Set/report a new Warning.
     *
     * @param warning warning message that shall be added
     * @author Marvin Jolk
     */
    public void addWarning(String warning) {
        warningList.add(warning);
        if (!(jTextAreaLog == null)) {
            jTextAreaLog.append(warning + System.lineSeparator());
        }
    }

    /**
     * Set/report a new Messages.
     *
     * @param message message that shall be added
     * @author Marvin Jolk
     */
    public void addMessage(String message) {
        messageList.add(message);
        if (!(jTextAreaLog == null)) {
            jTextAreaLog.append(message + System.lineSeparator());
        }
    }

    /**
     * Get all errors.
     *
     * @return List of all erorrs.
     * @author Marvin Jolk
     */
    public List getErrors() {
        return errorsList;
    }

    /**
     * Get all Warnings.
     *
     * @return List of all Warnings.
     * @author Marvin Jolk
     */
    public List getWarnings() {
        return warningList;
    }

    /**
     * Get all Messages.
     *
     * @return List of all Messages.
     * @author Marvin Jolk
     */
    public List getMessages() {
        return messageList;
    }

    /**
     * This function returns all errors that have not yet been queried.
     *
     * @return all new Errors
     * @author Marvin Jolk
     */
    public List<String> getUnknownErrors() {
        List<String> unknownErrorsList = new ArrayList<String>();

        while (unknownErrorTracker < errorsList.size()) {
            unknownErrorsList.add(errorsList.get(unknownErrorTracker));
            unknownErrorTracker++;
        }

        return unknownErrorsList;
    }

    /**
     * This function returns all warnings that have not yet been queried.
     *
     * @return all new Warnings
     * @author Marvin Jolk
     */
    public List<String> getUnknownWarnings() {
        List<String> unknownWarningsList = new ArrayList<String>();

        while (unknownWarningTracker < warningList.size()) {
            unknownWarningsList.add(warningList.get(unknownWarningTracker));
            unknownWarningTracker++;
        }

        return unknownWarningsList;
    }

    /**
     * This function returns all messages that have not yet been queried.
     *
     * @return all new Messages
     * @author Marvin Jolk
     */
    public List<String> getUnknownMessages() {
        List<String> unknownMessageList = new ArrayList<String>();

        while (unknownMessageTracker < messageList.size()) {
            unknownMessageList.add(messageList.get(unknownMessageTracker));
            unknownMessageTracker++;
        }

        return unknownMessageList;
    }

}
