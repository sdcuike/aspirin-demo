package com.doctor.javamail.demo;

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
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        mimeMessage.setSubject("test hello");
        mimeMessage.setSender(new InternetAddress("doctor@qq.com"));
        mimeMessage.setFrom(new InternetAddress("doctorq@qqq.com"));
        mimeMessage.setRecipient(RecipientType.TO, new InternetAddress("xxxx@quancheng-ec.com"));
        mimeMessage.setText("test send mail", "utf-8");
        Pair<Boolean, String> result = SendEmailUtil.sendMail(mimeMessage);
        System.out.println("send result:" + result);
    }

}
