package com.fsd.service;

public interface MailService {
	
	/**
     * send simple mail
     *
     * @param to      Receiver
     * @param subject Subject
     * @param content Content
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * send Html mail
     *
     * @param to      Receiver
     * @param subject Subject
     * @param content Content
     */
    void sendHtmlMail(String to, String subject, String content);

    /**
     * send mail with attachment
     *
     * @param to      Receiver
     * @param subject Subject
     * @param content Content
     * @param filePath Attachment
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath);

    /**
     * send model mail 
     * @param to Receiver
     * @param subject Subject
     * @param fileName Model file name
     * @param model Model
     */
    void sendModelMail(String to, String subject, String fileName, Object model);

}
