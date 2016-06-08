package com.doctor.javamail.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import com.doctor.javamail.util.Pair;
import com.doctor.javamail.util.Pair.PairBuider;
import com.sun.mail.smtp.SMTPTransport;

/**
 * 发送邮件，毫无限制
 * 参考：https://github.com/masukomi/aspirin 实现核心原理
 * 
 * @author sdcuike
 *
 *         Create At 2016年6月7日 下午5:06:29
 */
public class SendMailDemo {
    private static final Logger log = LoggerFactory.getLogger(SendMailDemo.class);

    public static final String SMTP_PROTOCOL_PREFIX = "smtp://";

    public static final Properties properties = new Properties();;

    public static void main(String[] args) throws Throwable {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        mimeMessage.setSubject("test hello");
        mimeMessage.setSender(new InternetAddress("doctor@qq.com"));
        mimeMessage.setFrom(new InternetAddress("doctorq@qqq.com"));
        mimeMessage.setRecipient(RecipientType.TO, new InternetAddress("xxx@quancheng-ec.com"));
        mimeMessage.setText("test send mail", "utf-8");
        Pair<Boolean, String> result = sendMail(mimeMessage);
        System.out.println("send result:" + result);
    }

    public static Pair<Boolean, String> sendMail(MimeMessage mimeMessage) throws MessagingException, IOException {
        PairBuider<Boolean, String> pair = new PairBuider<>();
        pair.setLeft(Boolean.FALSE);
        Address[] allRecipients = mimeMessage.getAllRecipients();
        if (allRecipients == null || allRecipients.length == 0) {
            return null;
        }

        for (Address address : allRecipients) {
            String currentRecipient = address.toString();
            String hostName = currentRecipient.substring(currentRecipient.lastIndexOf("@") + 1);
            Set<URLName> mxRecordsForHost = getMXRecordsForHost(hostName);

            boolean sendSuccessfully = false;
            Iterator<URLName> recordIterator = mxRecordsForHost.iterator();
            while (!sendSuccessfully && recordIterator.hasNext()) {
                SMTPTransport transport = null;
                try {
                    Session session = Session.getDefaultInstance(properties);

                    MimeMessage message = new MimeMessage(mimeMessage);
                    Properties props = session.getProperties();

                    if (message.getSender() == null) {
                        props.put("mail.smtp.from", "<>");
                    } else {
                        String sender = message.getSender().toString();
                        props.put("mail.smtp.from", sender);
                    }

                    transport = (SMTPTransport) session.getTransport(recordIterator.next());
                    transport.connect();
                    transport.sendMessage(message, new Address[] { address });
                    sendSuccessfully = true;
                    String lastServerResponse = transport.getLastServerResponse();

                    pair.setLeft(Boolean.TRUE);
                    pair.setRight(lastServerResponse);
                } catch (Exception e) {
                    log.error("send", e);
                    pair.setRight(getCause(e));
                } finally {
                    if (transport != null) {
                        transport.close();
                        transport = null;
                    }
                }
            }
        }
        return pair.build();
    }

    public static Set<URLName> getMXRecordsForHost(String hostName) throws TextParseException {

        Set<URLName> recordsColl = new HashSet<>();

        boolean foundOriginalMX = true;
        Record[] records = new Lookup(hostName, Type.MX).run();

        /*
         * Sometimes we should send an email to a subdomain which does not
         * have own MX record and MX server. At this point we should find an
         * upper level domain and server where we can deliver our email.
         * 
         * Example: subA.subB.domain.name has not own MX record and
         * subB.domain.name is the mail exchange master of the subA domain
         * too.
         */
        if (records == null || records.length == 0) {
            foundOriginalMX = false;
            String upperLevelHostName = hostName;
            while (records == null &&
                    upperLevelHostName.indexOf(".") != upperLevelHostName.lastIndexOf(".") &&
                    upperLevelHostName.lastIndexOf(".") != -1) {
                upperLevelHostName = upperLevelHostName.substring(upperLevelHostName.indexOf(".") + 1);
                records = new Lookup(upperLevelHostName, Type.MX).run();
            }
        }

        if (records != null) {
            // Sort in MX priority (higher number is lower priority)
            Arrays.sort(records, new Comparator<Record>() {
                @Override
                public int compare(Record arg0, Record arg1) {
                    return ((MXRecord) arg0).getPriority() - ((MXRecord) arg1).getPriority();
                }
            });
            // Create records collection
            for (int i = 0; i < records.length; i++) {
                MXRecord mx = (MXRecord) records[i];
                String targetString = mx.getTarget().toString();
                URLName uName = new URLName(
                        SMTP_PROTOCOL_PREFIX +
                                targetString.substring(0, targetString.length() - 1));
                recordsColl.add(uName);
            }
        } else {
            foundOriginalMX = false;
        }

        /*
         * If we found no MX record for the original hostname (the upper
         * level domains does not matter), then we add the original domain
         * name (identified with an A record) to the record collection,
         * because the mail exchange server could be the main server too.
         * 
         * We append the A record to the first place of the record
         * collection, because the standard says if no MX record found then
         * we should to try send email to the server identified by the A
         * record.
         */
        if (!foundOriginalMX) {
            Record[] recordsTypeA = new Lookup(hostName, Type.A).run();
            if (recordsTypeA != null && recordsTypeA.length > 0) {
                recordsColl.add(new URLName(SMTP_PROTOCOL_PREFIX + hostName));
            }
        }

        return recordsColl;
    }

    public static String getCause(Throwable e) {
        List<String> messages = new ArrayList<>();

        Throwable cause = e;
        while (cause != null) {
            messages.add(cause.getMessage());
            cause = cause.getCause();
        }

        Collections.reverse(messages);

        return messages.stream().collect(Collectors.joining("; "));
    }
}
