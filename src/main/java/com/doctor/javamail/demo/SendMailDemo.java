package com.doctor.javamail.demo;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.doctor.javamail.util.Pair;
import com.doctor.javamail.util.SendEmailUtil;

/**
 * 发送邮件，毫无限制
 * 参考：https://github.com/masukomi/aspirin 实现核心原理
 * 
 * @author sdcuike
 *
 *         Create At 2016年6月7日 下午5:06:29
 * 
 *         邮件真实发送地址与邮件宣称的地址不一样如何解决：
 * @see http://kf.qq.com/faq/120511z22Uzq130911QnA73A.html
 * 
 *      对于您收到的邮件，如果它的真实发送地址（Sender）与宣称的发件人地址（From）不一致，QQ邮箱就会在读信页面里显示出“由某地址代发”的提示，我们建议您这时要谨慎判断、审视邮件内容的真实性。此情况出现的可能性有以下几种：
 *      1、这种情况可能是正常的邮件—就像QQ邮箱可以代收其他邮箱的邮件，也可以代发其他邮箱的邮件，这时对方看到的就是由QQ邮箱代发其他邮箱了，发件人是其他邮箱的地址；
 * 
 *      2、可能存在是欺诈邮件的可能—发件人宣称是某个地址（譬如宣称是某银行的地址、某网上商城的地址等等），但实际是用另外的邮箱来发出的，而邮件的内容，可能是为了骗取您的信任，让您进一步按照其指示，从而被骗取付费或中了木马病毒，由于邮件协议的开放性，虽然QQ邮箱针对欺诈邮件的行为做了大量的防卫措施，但仍然无法保证100%的识别出这种情况，因此系统在不确定的情况下，依然会展示这封邮件给您，但会提示您“由某地址代发”，建议您在看到这种提示时，谨慎判断、审视邮件内容的真实性，避免受骗，您也可以将您认为的欺诈邮件举报给我们，一起来加强QQ邮箱的安全防御能力。
 */
public class SendMailDemo {

    public static void main(String[] args) throws Throwable {

        Properties properties = new Properties();
        Session session = SendEmailUtil.getSession(properties);
        MimeMessage mimeMessage = SendEmailUtil.createMimeMessage();
        // Message-ID: <597255128.0.1465725132963.JavaMail.doctor@doctorwho-MacBook-Pro.local> 会暴露你的个人信息，请修改机器hostname和用户名

        mimeMessage.setSubject("The late blessing");

        // 邮件真实发送地址与邮件宣称的地址不一样如何解决:setSender /setFrom设置同一地址，不同则会出现上述文档所说内容
        String nickName = "神秘博士";
        // 设置Chinese nickname @see http://www.programmershare.com/2703983/
        InternetAddress sender = new InternetAddress(MimeUtility.encodeText(nickName) + " <doctor@aol.com>");
        mimeMessage.setSender(sender);
        mimeMessage.setFrom(sender);

        // mimeMessage.setRecipient(RecipientType.CC, new InternetAddress("xx@-ec.com"));
        mimeMessage.setRecipient(RecipientType.TO, new InternetAddress("xxxx@qq.com"));
        mimeMessage.setReplyTo(new Address[] { new InternetAddress("xxx@qq.com") });
        mimeMessage.setText("端午颂诉着古老的传说，粽子包裹着古老的风俗，艾叶凝聚着神秘的色彩，屈原坚守着民族的气节，让我们记住特殊的日子，时刻提醒勉励自己，幸福生活，努力工作，开心每一天。 from:老崔 ", "utf-8");
        Pair<Boolean, String> result = SendEmailUtil.sendMail(mimeMessage, session);
        System.out.println("send result:" + result);
    }

}
