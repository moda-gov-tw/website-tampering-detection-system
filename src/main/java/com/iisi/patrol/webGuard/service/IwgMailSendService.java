package com.iisi.patrol.webGuard.service;

import com.iisi.patrol.webGuard.service.dto.AdmMailSendDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class IwgMailSendService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender javaMailSender;

    public IwgMailSendService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     *
     * @param receivers 收件者們,用逗號分隔
     * @param content 信件內容
     */

    public void saveAdmMailWithReceiverAndContent(String receivers,String content){
        if(StringUtils.isBlank(receivers)) return;
        List<String> receiverList = Arrays.asList(receivers.split(",", -1));
        receiverList.forEach(receiver->{
            AdmMailSendDTO mail = new AdmMailSendDTO();
            mail.setSender(sender);
            mail.setReceiver(receiver);
            mail.setMailType("IWG");
            mail.setSubject("檔案異動通知");
            mail.setContent("注意!!檢測出檔案有異動:"+content);
            mail.setStatus("W");
            mail.setIsHtml(false);
            mail.setCreateUser("iwg");
            mail.setSourceId("iwg");
            mail.setCreateTime(Instant.now());
            try {
                sendEmail(mail);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendEmail(AdmMailSendDTO mail) throws MessagingException {
        // 建立 MimeMessage
        MimeMessage message = javaMailSender.createMimeMessage();

        // 使用 MimeMessageHelper 幫助填寫郵件訊息內容
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // 設定寄件者、收件者、主旨與內容
        helper.setFrom(mail.getSender());
        helper.setTo(mail.getReceiver());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent(), mail.getIsHtml()); // 第二個參數設為 true 代表支援 HTML 格式

        // 發送郵件
        javaMailSender.send(message);
    }
}
