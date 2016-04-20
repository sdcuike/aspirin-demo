package com.doctor.aspirin.demo.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月20日
 * 
 * @see http://www.ietf.org/rfc/rfc821.txt 4.2. SMTP REPLIES
 *      4.2.1. REPLY CODES BY FUNCTION GROUPS
 * 
 *      500 Syntax error, command unrecognized
 *      [This may include errors such as command line too long]
 *      501 Syntax error in parameters or arguments
 *      502 Command not implemented
 *      503 Bad sequence of commands
 *      504 Command parameter not implemented
 * 
 *      211 System status, or system help reply
 *      214 Help message
 *      [Information on how to use the receiver or the meaning of a
 *      particular non-standard command; this reply is useful only
 *      to the human user]
 * 
 *      220 <domain> Service ready
 *      221 <domain> Service closing transmission channel
 *      421 <domain> Service not available,
 *      closing transmission channel
 *      [This may be a reply to any command if the service knows it
 *      must shut down]
 * 
 *      250 Requested mail action okay, completed
 *      251 User not local; will forward to <forward-path>
 *      450 Requested mail action not taken: mailbox unavailable
 *      [E.g., mailbox busy]
 *      550 Requested action not taken: mailbox unavailable
 *      [E.g., mailbox not found, no access]
 *      451 Requested action aborted: error in processing
 *      551 User not local; please try <forward-path>
 *      452 Requested action not taken: insufficient system storage
 *      552 Requested mail action aborted: exceeded storage allocation
 *      553 Requested action not taken: mailbox name not allowed
 *      [E.g., mailbox syntax incorrect]
 *      354 Start mail input; end with <CRLF>.<CRLF>
 *      554 Transaction failed
 * 
 * 
 *      4.2.2. NUMERIC ORDER LIST OF REPLY CODES
 * 
 *      211 System status, or system help reply
 *      214 Help message
 *      [Information on how to use the receiver or the meaning of a
 *      particular non-standard command; this reply is useful only
 *      to the human user]
 *      220 <domain> Service ready
 *      221 <domain> Service closing transmission channel
 *      250 Requested mail action okay, completed
 *      251 User not local; will forward to <forward-path>
 * 
 *      354 Start mail input; end with <CRLF>.<CRLF>
 * 
 *      421 <domain> Service not available,
 *      closing transmission channel
 *      [This may be a reply to any command if the service knows it
 *      must shut down]
 *      450 Requested mail action not taken: mailbox unavailable
 *      [E.g., mailbox busy]
 *      451 Requested action aborted: local error in processing
 *      452 Requested action not taken: insufficient system storage
 * 
 *      500 Syntax error, command unrecognized
 *      [This may include errors such as command line too long]
 *      501 Syntax error in parameters or arguments
 *      502 Command not implemented
 *      503 Bad sequence of commands
 *      504 Command parameter not implemented
 *      550 Requested action not taken: mailbox unavailable
 *      [E.g., mailbox not found, no access]
 *      551 User not local; please try <forward-path>
 *      552 Requested mail action aborted: exceeded storage allocation
 *      553 Requested action not taken: mailbox name not allowed
 *      [E.g., mailbox syntax incorrect]
 *      554 Transaction failed
 * 
 */
public enum EmailSendStatus implements IEnumDescription {
    Opened(0, "MailWasOpened", "mail was opened"),
    Send(1, "MailWasSend", "mail was send"),
    Unsubscribe(2, "unsubscribe", "mail was unsubscribed"),
    SUCCESS(3, "250", "250 Ok,send finished"),
    MailboxNotFound(550, "MailboxNotFound", "Mailbox not found"),
    MailContentDenied(551, "MailContentDenied", "550 Mail content denied"),
    RecipientAddressRejected(552, "RecipientAddressRejected", "550 Recipient address rejected"),
    NULLSenderIsNotAllowed(553, "NULLSenderIsNotAllowed", "553 Requested action not taken: NULL sender is not allowed"),
    TransactionFailed(554, "554", "554 Transaction failed"),
    EmptyEnvelopeSendersNotAllowed(555, "EmptyEnvelopeSendersNotAllowed", "550 Empty envelope senders not allowed"),
    UserLocked(556, "UserLocked", "550 user locked"),
    UserNotFound(557, "UserNotFound", "550 User not found"),
    UserNotExist(558, "UserNotExist", "550 user not exist");

    private int index;
    private String name;
    private String description;

    private EmailSendStatus(int index, String name, String description) {
        this.index = index;
        this.name = name;
        this.description = description;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
