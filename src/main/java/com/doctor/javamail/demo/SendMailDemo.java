package com.doctor.javamail.demo;

import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.doctor.javamail.util.Pair;
import com.doctor.javamail.util.SendEmailUtil;

/**
 * 发送邮件，毫无限制
 * 参考：https://github.com/masukomi/aspirin 实现核心原理
 * 
 * @author sdcuike
 *
 *         Create At 2016年6月7日 下午5:06:29
 */
public class SendMailDemo {

    public static void main(String[] args) throws Throwable {

        Properties properties = new Properties();
        Session session = SendEmailUtil.getSession(properties);
        MimeMessage mimeMessage = SendEmailUtil.createMimeMessage();
        mimeMessage.setSubject("端午节快乐");
        mimeMessage.setSender(new InternetAddress("doctor@aol.com"));
        mimeMessage.setFrom(new InternetAddress("doctorq@aol.com"));
        // mimeMessage.setRecipient(RecipientType.CC, new InternetAddress("xx@-ec.com"));
        mimeMessage.setRecipient(RecipientType.TO, new InternetAddress("xxx@qq.com"));
        // mimeMessage.setReplyTo(new Address[] { new InternetAddress("xxx@qq.com") });
        mimeMessage.setText("端午节快乐", "utf-8");
        Pair<Boolean, String> result = SendEmailUtil.sendMail(mimeMessage, session);
        System.out.println("send result:" + result);
    }

}
