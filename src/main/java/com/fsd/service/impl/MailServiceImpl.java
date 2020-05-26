package com.fsd.service.impl;

import java.io.IOException;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.fsd.service.MailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MailServiceImpl implements MailService{

	@Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration configuration;
    
    @Value("${spring.mail.from}")
    private String from;
    
    
	@Override
	public void sendSimpleMail(String to, String subject, String content) {
		// TODO Auto-generated method stub
		//创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(from);
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        mailSender.send(message);		
	}

	@Override
	public void sendHtmlMail(String to, String subject, String content) {
		// TODO Auto-generated method stub
		//获取MimeMessage对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            //邮件发送人
            messageHelper.setFrom(from);
            //邮件接收人
            messageHelper.setTo(to);
            //邮件主题
            message.setSubject(subject);
            //邮件内容，html格式
            messageHelper.setText(content, true);
            //发送
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
		// TODO Auto-generated method stub
		MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            //FileSystemResource file = new FileSystemResource(new File(filePath));
            ClassPathResource resource = new ClassPathResource(filePath);
            FileSystemResource file = new FileSystemResource(resource.getFile());
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            //可以同时添加多个附件,只需要在这里直接添加第2,第3...附件就行了.
            //helper.addAttachment(fileName2, file2);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public void sendModelMail(String to, String subject, String fileName, Object model) {
		// TODO Auto-generated method stub
		MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            Template template = configuration.getTemplate(fileName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

}
