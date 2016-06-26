package com.doctor.javamail.demo;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.sun.mail.smtp.SMTPTransport;

/**
 * @author sdcuike
 *
 *         Created on 2016年6月26日 下午6:46:12
 */
public class CodeForBlog {
    private static final String SMTP_PROTOCOL_PREFIX = "smtp://";

    private static final String mail_mime_charset = "mail.mime.charset";
    private static final String mail_smtp_connectiontimeout = "mail.smtp.connectiontimeout";
    private static final String mail_smtp_timeout = "mail.smtp.timeout";
    private static final String mail_smtp_host = "mail.smtp.host";
    private static final String mail_smtp_localhost = "mail.smtp.localhost";
    private static final String mail_smtp_debug = "mail.debug";

    public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        Properties prop = System.getProperties();
        prop.setProperty(mail_mime_charset, "UTF-8");
        prop.setProperty(mail_smtp_timeout, "30000");
        prop.setProperty(mail_smtp_connectiontimeout, "30000");
        prop.setProperty(mail_smtp_localhost, "doctorwho.com");
        prop.setProperty(mail_smtp_host, "doctorwho.com");
        Session session = Session.getInstance(prop, null);

        MimeMessage mimeMessage = new MimeMessage(session);

        mimeMessage.setSubject("550 Mail content denied.");

        String nickName = "注意开会";
        InternetAddress sender = new InternetAddress(MimeUtility.encodeText(nickName) + " <doctorwho@doctorwho.com>");
        mimeMessage.setSender(sender);
        mimeMessage.setFrom(sender);

        // mimeMessage.setRecipient(RecipientType.CC, new InternetAddress("xx@-ec.com"));
        mimeMessage.setRecipient(RecipientType.TO, new InternetAddress("sdckx@foxmail.com"));
        mimeMessage.setReplyTo(new Address[] { sender });
        mimeMessage.setText("Mail content denied. ");

        SMTPTransport transport = (SMTPTransport) session.getTransport(new URLName(SMTP_PROTOCOL_PREFIX + "mx3.qq.com"));
        transport.connect();
        transport.sendMessage(mimeMessage, new Address[] { new InternetAddress("sdckx@foxmail.com") });
        String lastServerResponse = transport.getLastServerResponse();
        System.out.println(lastServerResponse);
    }

}
