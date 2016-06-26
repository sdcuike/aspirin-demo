package com.doctor.dnsjava.api.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.xbill.DNS.Address;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月22日 下午5:39:51
 * 
 *         http://www.dnsjava.org/dnsjava-current/examples.html
 */
public class DnsjavaExamples {

    public static void main(String[] args) throws UnknownHostException, TextParseException {
        // get the IP address associated with a name:
        System.out.println("====get the IP address associated with a name:");
        InetAddress byAddress = Address.getByName("www.google.com");
        System.out.println(byAddress);

        // Get the MX target and preference of a name:
        System.out.println("====Get the MX target and preference of a name:");

        Record[] records = new Lookup("qq.com", Type.MX).run();
        for (Record record : records) {
            MXRecord mxRecord = (MXRecord) record;
            System.out.println("Host:" + mxRecord.getTarget() + " has preference " + mxRecord.getPriority());
        }

        // Query a remote name server for its version:
        System.out.println("====Query a remote name server for its version:");
        Lookup lookup = new Lookup("version.bind", Type.TXT, DClass.CH);
        lookup.setResolver(new SimpleResolver("www.baidu.com"));
        lookup.run();
        if (lookup.getResult() == Lookup.SUCCESSFUL) {
            System.err.println(lookup.getAnswers()[0].rdataToString());
        }
    }

}
