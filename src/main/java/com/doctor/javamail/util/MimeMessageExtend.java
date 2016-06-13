package com.doctor.javamail.util;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * @author sdcuike
 *
 *         Create At 2016年6月12日 下午6:17:44
 * 
 *         How do I set or change the SMTP Message-ID with javax.mail?
 * 
 * @see http://stackoverflow.com/questions/8366843/how-do-i-set-or-change-the-smtp-message-id-with-javax-mail
 */
public class MimeMessageExtend extends MimeMessage {

    public MimeMessageExtend(Session session) {
        super(session);
    }

    @Override
    protected void updateMessageID() throws MessagingException {

    }

    /**
     * 邮件真实发送地址与邮件宣称的地址不一样如何解决:setSender /setFrom设置同一地址，不同则会出现上述文档所说内容
     */
    @Override
    public void setFrom(Address address) throws MessagingException {
        super.setFrom(address);
        super.setSender(address);
    }

}
