package com.doctor.aspirin.api.demo;

import java.util.Collection;

import javax.mail.URLName;

import org.masukomi.aspirin.dns.DnsResolver;

/**
 * 基本概念：
 * 
 * https://en.wikipedia.org/wiki/MX_record
 * 
 * 
 * 
 * @author sdcuike
 *
 *         Create At 2016年4月22日 上午11:10:07
 * 
 *         MX记录
 * 
 *         mail exchanger record，MX记录记录了发送电子邮件时域名对应的服务器地址。电子邮件发送使用的是SMTP应用层协议。
 *         例如要发送邮件到abc@qq.com的时候，其中的域名部分为qq.com，MX记录描述了发送电子邮件时应该发往那个服务器。
 *         可以使用dig命令查询MX记录：
 * 
 *         dig qq.com mx
 * 
 *         输出为：
 * 
 *         ; <<>> DiG 9.9.5-3-Ubuntu <<>> qq.com mx
 * 
 *         ;; global options: +cmd
 * 
 *         ;; Got answer:
 * 
 *         ;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 48801
 * 
 *         ;; flags: qr rd ra; QUERY: 1, ANSWER: 3, AUTHORITY: 4, ADDITIONAL: 1
 * 
 * 
 * 
 *         ;; OPT PSEUDOSECTION:
 * 
 *         ; EDNS: version: 0, flags:; udp: 4096
 * 
 *         ;; QUESTION SECTION:
 * 
 *         ;qq.com. IN MX
 * 
 * 
 * 
 *         ;; ANSWER SECTION:
 * 
 *         qq.com. 5864 IN MX 30 mx1.qq.com.
 * 
 *         qq.com. 5864 IN MX 10 mx3.qq.com.
 * 
 *         qq.com. 5864 IN MX 20 mx2.qq.com.
 * 
 * 
 * 
 *         ;; AUTHORITY SECTION:
 * 
 *         qq.com. 65518 IN NS ns2.qq.com.
 * 
 *         qq.com. 65518 IN NS ns3.qq.com.
 * 
 *         qq.com. 65518 IN NS ns4.qq.com.
 * 
 *         qq.com. 65518 IN NS ns1.qq.com.
 * 
 * 
 * 
 *         ;; Query time: 88 msec
 * 
 *         ;; SERVER: 127.0.0.1#53(127.0.0.1)
 * 
 *         ;; WHEN: Mon Nov 03 16:53:27 CST 2014
 * 
 *         ;; MSG SIZE rcvd: 167
 * 
 * 
 * 
 *         MX记录从左到右各部分意义：
 * 
 *         例qq.com. 5864 IN MX 30 mx1.qq.com.
 * 
 *         1、对应的域名，这里是qq.com.，'.'代表根域名，com顶级域名，qq二级域名
 * 
 *         2、TTL，time ro live，缓存时间，单位秒。5864，代表缓存域名服务器，可以在缓存中保存5864秒该记录。
 * 
 *         3、class，要查询信息的类别，IN代表类别为IP协议，即Internet。还有其它类别，比如chaos等，由于现在都是互联网，所以其它基本不用。
 * 
 *         4、type，记录类型，MX记录，代表此记录为MX记录
 * 
 *         5、preference number 优先级，数值由域名管理者自定义，数值越小优先级越高。比如这里3条MX记录，分别对应的优先级30\10\20。电子邮件发送软件就可以根据返回的MX中的优先级数值选用合适的服务器。
 * 
 *         6、mx1.qq.com.为发送邮件时应选用的服务器地址，。由于仍然是域名，所以还需要进一步DNS查询才能得到其IP地址。
 * 
 * 
 */
public class DnsResolverDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // 请结合http://mxtoolbox.com/SuperTool.aspx?action=a%3agmx.net&run=toolpage# 测试程序

        System.out.println("Three domains with problematic MX records.");

        System.out.println("testing gmx.net");
        final Collection<URLName> mxRecords1 = DnsResolver.getMXRecordsForHost("gmx.net");
        System.out.println(mxRecords1);

        System.out.println("testing green.ch");
        final Collection<URLName> mxRecords2 = DnsResolver.getMXRecordsForHost("green.ch");
        System.out.println(mxRecords2);

        System.out.println("testing tschannen.cc");
        final Collection<URLName> mxRecords3 = DnsResolver.getMXRecordsForHost("tschannen.cc");
        System.out.println(mxRecords3);

    }

}
