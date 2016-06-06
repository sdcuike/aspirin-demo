package com.doctor.javamail.demo;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Java Mail api Demo
 * 
 * @author sdcuike
 *
 *         Create At 2016年6月6日 下午3:49:25
 */
public class JavaMailDemo {

    /**
     * @param args
     * @throws MessagingException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws MessagingException, InterruptedException {

        String bounceAddr = "xxxx@sina.com";
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.163.com");
        mailSender.setPort(25);
        mailSender.setProtocol("smtp");
        String username = "xx@163.com";//
        mailSender.setUsername(username);
        String passwd = "xx";
        mailSender.setPassword(passwd);

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");

        properties.setProperty("mail.smtp.starttls.enable", "false");

        properties.setProperty("mail.smtp.quitwait", "true");
        properties.setProperty("mail.smtp.sendpartial", "true");
        properties.put("mail.debug", "true");

        // properties.put("mail.smtp.from", bounceAddr);

        mailSender.setJavaMailProperties(properties);

        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        // mimeMessage.setHeader("Disposition-Notification-To", "quanchengfeikong@sina.com");
        // mimeMessage.setHeader("Return-Receipt-To", "cuikexiang@quancheng-ec.com");
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

        message.setSubject("test");
        // message.setFrom(new InternetAddress("doctor" + " <" + username + ">"));
        message.setFrom(new InternetAddress(username));
        message.setTo("xxx");
        message.setReplyTo(new InternetAddress(bounceAddr));
        message.setText("test test。", true /* isHtml */);
        mailSender.send(mimeMessage);

        TimeUnit.MINUTES.sleep(1);

    }

}
