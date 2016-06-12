package com.doctor.javamail.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import com.doctor.javamail.demo.SendMailDemo;
import com.doctor.javamail.util.Pair.PairBuider;
import com.sun.mail.smtp.SMTPTransport;

/**
 * @author sdcuike
 *
 *         Create At 2016年6月8日 下午3:29:39
 * 
 *         https://tools.ietf.org/html/rfc5321
 *         https://tools.ietf.org/html/rfc822#section-3.2
 * 
 *         MX record:
 *         https://dnsdb.cit.cornell.edu/explain_mx.html
 *         http://blog.onlymyemail.com/what-is-a-mail-exchanger-mx-record/
 *         http://support.hostgator.com/articles/specialized-help/technical/mail-exchange-record-what-to-put-for-your-mx-record
 * 
 */
public final class SendEmailUtil {
    private static final Logger log = LoggerFactory.getLogger(SendMailDemo.class);

    private static final String SMTP_PROTOCOL_PREFIX = "smtp://";

    private static final String mail_mime_charset = "mail.mime.charset";
    private static final String mail_smtp_connectiontimeout = "mail.smtp.connectiontimeout";
    private static final String mail_smtp_timeout = "mail.smtp.timeout";
    private static final String mail_smtp_host = "mail.smtp.host";
    private static final String mail_smtp_localhost = "mail.smtp.localhost";
    private static final String mail_smtp_debug = "mail.debug";

    public static Session getSession(Properties properties) {
        Properties config = getConfig(properties);
        return Session.getInstance(config, null);
    }

    public static MimeMessage createMimeMessage() {
        // MimeMessage mMesg = new MimeMessage((Session) null);
        MimeMessage mMesg = new MimeMessageExtend((Session) null);
        return mMesg;
    }

    public static Pair<Boolean, String> sendMail(MimeMessage mimeMessage, Session session) throws MessagingException, IOException {
        PairBuider<Boolean, String> pair = new PairBuider<>();
        pair.setLeft(Boolean.FALSE);
        Address[] allRecipients = mimeMessage.getAllRecipients();
        if (allRecipients == null || allRecipients.length == 0) {
            throw new IllegalArgumentException("Recipients is empty");
        }

        for (Address address : allRecipients) {
            String currentRecipient = address.toString();
            String hostName = currentRecipient.substring(currentRecipient.lastIndexOf("@") + 1);
            Set<URLName> mxRecordsForHost = getMXRecordsForHost(hostName);
            if (mxRecordsForHost.isEmpty()) {
                throw new IllegalArgumentException("mxRecords for " + currentRecipient + " is empty");
            }
            log.debug("currentRecipient:{} mxRecordsForHost:{}", currentRecipient, mxRecordsForHost);
            boolean sendSuccessfully = false;
            Iterator<URLName> recordIterator = mxRecordsForHost.iterator();
            while (!sendSuccessfully && recordIterator.hasNext()) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e1) {

                }
                SMTPTransport transport = null;
                try {
                    MimeMessage message = new MimeMessage(mimeMessage);
                    Properties props = session.getProperties();

                    if (message.getSender() == null) {
                        props.put("mail.smtp.from", "<>");
                    } else {
                        String sender = message.getSender().toString();
                        props.put("mail.smtp.from", sender);
                    }

                    URLName url = recordIterator.next();
                    log.debug("use url:{}", url);
                    transport = (SMTPTransport) session.getTransport(url);
                    transport.connect();
                    transport.sendMessage(message, new Address[] { address });
                    sendSuccessfully = true;
                    String lastServerResponse = transport.getLastServerResponse();

                    pair.setLeft(Boolean.TRUE);
                    pair.setRight(lastServerResponse);
                } catch (Exception e) {
                    log.debug("send fail", e);
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

    private static Properties getConfig(Properties properties) {
        Properties prop = System.getProperties();
        prop.setProperty(mail_mime_charset, "UTF-8");
        if (log.isDebugEnabled()) {
            prop.setProperty(mail_smtp_debug, "true");
        }
        prop.setProperty(mail_smtp_timeout, "30000");
        prop.setProperty(mail_smtp_connectiontimeout, "30000");
        prop.setProperty(mail_smtp_localhost, "doctorwho.com");
        prop.setProperty(mail_smtp_host, "doctorwho.com");
        for (Entry<Object, Object> es : properties.entrySet()) {
            prop.put(es.getKey(), es.getValue());
        }
        return prop;
    }

    private static Set<URLName> getMXRecordsForHost(String hostName) throws TextParseException {

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

    private static String getCause(Throwable e) {
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
