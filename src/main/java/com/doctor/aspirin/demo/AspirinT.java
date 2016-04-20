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
import org.masukomi.aspirin.AspirinInternal;

import com.doctor.aspirin.demo.common.AspirinListenerImpl;

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
        String nick = "doctor";
        String replyTo = "sdcuike@aol.com";

        List<String> users = Arrays.asList("sdcuike@aol.com");
        List<String> users2 = Arrays.asList("sdcuike@qq.com");

        AspirinListenerImpl listenerImpl = new AspirinListenerImpl();
        listenerImpl.init();
        Aspirin.addListener(listenerImpl);
        int n = 1;
        while (n-- > 0) {
            for (String u : users) {
                MimeMessage message = AspirinInternal.createNewMimeMessage();

                message.addRecipient(Message.RecipientType.TO, new InternetAddress(u));
                message.setSubject(" - test to show it doesn't shut down");
                message.setText("This is the text");
                message.setFrom(new InternetAddress(nick + " <" + replyTo + ">"));

                message.setReplyTo(new InternetAddress[] { new InternetAddress(replyTo) });
                Aspirin.add(message);
            }

            for (String u : users2) {
                MimeMessage message = AspirinInternal.createNewMimeMessage();

                message.addRecipient(Message.RecipientType.TO, new InternetAddress(u));
                message.setSubject(" - test to show it doesn't shut down");
                message.setText("This is the text");
                message.setFrom(new InternetAddress(nick + " <" + replyTo + ">"));

                message.setReplyTo(new InternetAddress[] { new InternetAddress(replyTo) });
                Aspirin.add(message);
            }
        }
        TimeUnit.MINUTES.sleep(1);
    }

}
