package com.doctor.aspirin.demo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.masukomi.aspirin.Aspirin;
import org.masukomi.aspirin.core.AspirinInternal;
import org.masukomi.aspirin.core.listener.AspirinListener;
import org.masukomi.aspirin.core.listener.ResultState;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月19日
 */
public class AspirinT {

    /**
     * @param args
     * @throws MessagingException
     * @throws AddressException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws AddressException, MessagingException, InterruptedException {

        String user1 = "quanchengfeikong@sina.com";
        String user2 = "quanchengfeikong@sina.com";
        String user5 = "quanchengfeikong1@sina.com";
        String user3 = "lingxu@ssss.net";
        List<String> users = Arrays.asList("quanchengfeikong@sina.com", "xushida@quancheng-ec.com", "chenling@quancheng-ec.com", "xiamengting@quancheng-ec.com", "xitengteng@quancheng-ec.com",
                "zhouqing@quancheng-ec.com", "zhaojie@quancheng-ec.com");
        List<String> users2 = Arrays.asList("xushida11@quancheng-ec.com", "chenling11@quancheng-ec.com", "xiamengting11@quancheng-ec.com", "xitengteng11@quancheng-ec.com",
                "zhouqing11@quancheng-ec.com", "zhaojie11@quancheng-ec.com");

        Aspirin.addListener(new AspirinListener() {

            @Override
            public void delivered(String mailId, String recipient, ResultState state, String resultContent) {
                System.err.println("mailId:" + mailId + " recipient:" + recipient + " state:" + state + "  resultContent" + resultContent);

            }
        });
        int n = 100;
        while (n-- > 0) {
            for (String u : users) {
                MimeMessage message = AspirinInternal.createNewMimeMessage();

                message.setFrom(new InternetAddress(user2));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(u));
                message.setSubject(" - test to show it doesn't shut down");
                message.setText("This is the text");
                Aspirin.add(message);
            }

            for (String u : users2) {
                MimeMessage message = AspirinInternal.createNewMimeMessage();

                message.setFrom(new InternetAddress(user2));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(u));
                message.setSubject(" - test to show it doesn't shut down");
                message.setText("This is the text");
                Aspirin.add(message);
            }
        }
        TimeUnit.MINUTES.sleep(1);
    }

}
