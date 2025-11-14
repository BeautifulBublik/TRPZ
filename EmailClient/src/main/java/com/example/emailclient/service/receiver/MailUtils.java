package com.example.emailclient.service.receiver;
import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.MimeUtility;

import com.example.emailclient.model.Attachment;

public class MailUtils {

    public static List<Attachment> extractAttachments(Message message, String saveDir) throws Exception {
        List<Attachment> attachments = new ArrayList<>();
        Object content = message.getContent();

        if (content instanceof Multipart multipart) {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                processPart(part, saveDir, attachments);
            }
        }
        return attachments;
    }

    private static void processPart(Part part, String saveDir, List<Attachment> attachments) throws Exception {
        if (part.isMimeType("multipart/*")) {
            Multipart nested = (Multipart) part.getContent();
            for (int i = 0; i < nested.getCount(); i++) {
                processPart(nested.getBodyPart(i), saveDir, attachments);
            }
        } else {
            String disposition = part.getDisposition();
            String fileName = part.getFileName();

            if (fileName == null && (disposition == null || !disposition.equalsIgnoreCase(Part.ATTACHMENT))) {
                return; 
            }

            if (fileName != null) {
                try {
                    fileName = MimeUtility.decodeText(fileName);
                } catch (Exception e) {
                    System.err.println("Не вдалося декодувати ім’я файлу: " + e.getMessage());
                }
            } else {
                fileName = "attachment_" + System.currentTimeMillis() + ".bin";
            }


            File dir = new File(saveDir);
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, fileName);
            if (file.exists()) {
                System.out.println("Attachment already saved: " + fileName);
            }

            try (InputStream is = part.getInputStream();
                 FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            Attachment attach = new Attachment();
            attach.setFileName(fileName);
            attach.setFilePath(file.getAbsolutePath());
            attachments.add(attach);

            System.out.println("Attachment saved: " + fileName);
        }
    }
    public static String extractTextFromMessage(Message message) throws IOException, MessagingException {
		if (message.isMimeType("text/html")) {
			return cleanHtml((String) message.getContent());
		} else if (message.isMimeType("text/plain")) {
			return "<pre style='font-family:Segoe UI, sans-serif; white-space:pre-wrap;'>"
					+ message.getContent().toString() + "</pre>";
		} else if (message.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) message.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart part = multipart.getBodyPart(i);
				if (part.isMimeType("text/html")) {
					return cleanHtml((String) part.getContent());
				} else if (part.isMimeType("text/plain")) {
					return "<pre style='font-family:Segoe UI, sans-serif; white-space:pre-wrap;'>"
							+ part.getContent().toString() + "</pre>";
				}
			}
		}
		return "(немає тексту)";
	}

	private static String cleanHtml(String html) {
		html = html.replaceAll("(?is)<(script|style|meta|link)[^>]*>.*?</\\1>", "");
		html = html.replaceAll("(?is)<(input|button|form|textarea)[^>]*>", "");
		html = html.replaceAll("(?i)on[a-z]+\\s*=\\s*['\"].*?['\"]", "");
		html = html.replaceAll("(?i)<html[^>]*>",
				"<html><body style='font-family:Segoe UI, sans-serif; font-size:13px; color:#333;'>");
		html += "</body></html>";
		return html;
	}
}

