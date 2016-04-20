package com.doctor.aspirin.demo.common;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.masukomi.aspirin.Aspirin;
import org.masukomi.aspirin.config.Configuration;
import org.masukomi.aspirin.listener.AspirinListener;
import org.masukomi.aspirin.listener.ResultState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月19日 下午6:28:02
 */
@Component("aspirinListener")
public class AspirinListenerImpl implements AspirinListener {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void delivered(String mailId, String recipient, ResultState state, String resultContent) {
        log.info("mailId:{}, recipient:{}, state:{}, resultContent:{}", mailId, recipient, state, resultContent);
        String rs = StringUtils.deleteWhitespace(resultContent).toLowerCase();
        switch (state) {
        // case SENT:
        // emailStatisticManager.updateStatusById(Long.valueOf(mailId.trim()), EmailSendStatus.Send);
        // break;
        // case FAILED:
        // if (rs.contains(EmailSendStatus.MailboxNotFound.getName().toLowerCase())) {
        // emailStatisticManager.updateStatusById(Long.valueOf(mailId.trim()), EmailSendStatus.MailboxNotFound);
        // } else if (rs.contains(EmailSendStatus.MailContentDenied.getName().toLowerCase())) {
        // emailStatisticManager.updateStatusById(Long.valueOf(mailId.trim()), EmailSendStatus.MailContentDenied);
        // }
        //
        // break;
        case FINISHED:
            if (rs.contains(EmailSendStatus.SUCCESS.getName().toLowerCase())) {
                // TODO:
            } else if (rs.contains(EmailSendStatus.MailContentDenied.getName().toLowerCase())) {
                // TODO:
            } else if (rs.contains(EmailSendStatus.RecipientAddressRejected.getName().toLowerCase())) {
                // TODO:
            } else if (rs.contains(EmailSendStatus.UserNotExist.getName().toLowerCase())) {
                // TODO:
            } else if (rs.contains(EmailSendStatus.MailboxNotFound.getName().toLowerCase())) {
                // TODO:
            } else if (rs.contains(EmailSendStatus.UserLocked.getName().toLowerCase())) {
                // TODO:
            } else if (rs.contains(EmailSendStatus.EmptyEnvelopeSendersNotAllowed.getName().toLowerCase())) {
                // TODO:
            } else if (rs.contains(EmailSendStatus.NULLSenderIsNotAllowed.getName().toLowerCase())) {
                // TODO:
            } else if (rs.contains(EmailSendStatus.TransactionFailed.getName().toLowerCase())) {
                // TODO:
            } else if (rs.contains(EmailSendStatus.UserNotFound.getName().toLowerCase())) {
                // TODO:
            }

        default:
            break;
        }

    }

    @Value("${email.replyTo}")
    String emailReplyTo = "sdcuike@aol.com";

    @Value("${email.deliveryAttemptCount}")
    int emailDeliveryAttemptCount = 1;

    @Value("${email.deliveryAttemptDelay}")
    int deliveryAttemptDelay = 60000;
    @Value("${email.deliveryTimeout}")
    int deliveryTimeout = 15000;

    @PostConstruct
    public void init() {
        Configuration configuration = Aspirin.getConfiguration();
        configuration.setPostmasterEmail(emailReplyTo);
        configuration.setDeliveryAttemptCount(emailDeliveryAttemptCount);
        configuration.setDeliveryAttemptDelay(deliveryAttemptDelay);
        configuration.setDeliveryTimeout(deliveryTimeout);
        Aspirin.addListener(this);
    }

}
