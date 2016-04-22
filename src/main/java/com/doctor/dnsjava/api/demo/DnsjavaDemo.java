package com.doctor.dnsjava.api.demo;

import java.io.IOException;

import org.xbill.DNS.DClass;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.Section;
import org.xbill.DNS.Type;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月22日 下午5:23:03
 * 
 *         http://archive.oreilly.com/pub/post/reverse_dns_lookup_and_java.html
 *         http://www.ioncannon.net/system-administration/58/using-java-to-get-detailed-dns-information/
 *         http://www.dnsjava.org/dnsjava-current/examples.html
 */
public class DnsjavaDemo {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String hostIp = "2.0.0.127";
        ExtendedResolver resolver = new ExtendedResolver();
        Name address = ReverseMap.fromAddress(hostIp);
        Record record = Record.newRecord(address, Type.PTR, DClass.IN);
        Message query = Message.newQuery(record);
        Message response = resolver.send(query);
        Record[] answers = response.getSectionArray(Section.ANSWER);
        if (answers.length == 0) {
            System.out.println(hostIp);
        } else {
            String host = answers[0].rdataToString();
            System.out.println(host);
        }
    }

}
