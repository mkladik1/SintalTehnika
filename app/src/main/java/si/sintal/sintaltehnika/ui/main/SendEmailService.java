package si.sintal.sintaltehnika.ui.main;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class SendEmailService {
    private static SendEmailService instance = null;
    private static Context ctx;

    final String username = "web.server.bsd@sintal.si";
    final String password = "11dd44ss@";

    Properties prop;
    Session session;
    static final ExecutorService emailExecutor = Executors.newSingleThreadExecutor();

    public SendEmailService(Context context) {
        ctx = context;

        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        prop = new Properties();
        //prop.put("mail.smtp.host", "mail.zvd.si");
        //prop.put("mail.smtp.port", "25");
        //prop.put("mail.smtp.auth", "true");
        //prop.put("mail.smtp.starttls.enable", "false");
        prop.put("mail.smtp.host", "gw.sintal.si");
        prop.put("mail.from.alias", "Sintal tehnika");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "gw.sintal.si");

        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public static synchronized SendEmailService getInstance(Context context) {
        if(instance == null) {
            instance = new SendEmailService(context);
        }
        return instance;
    }

    public void SendEmail(Bitmap bitmap) {
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("receiver@email.com")
            );
            message.setSubject("Testing Email TLS");
            //message.setText("Welcome to Medium!");

            Multipart multipart = new MimeMultipart();

            //text
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<H1>Welcome to Medium!</H1>";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            //image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageInByte = baos.toByteArray();

            MimeBodyPart imageBodyPart = new MimeBodyPart();
            ByteArrayDataSource bds = new ByteArrayDataSource(imageInByte, "image/png");
            imageBodyPart.setDataHandler(new DataHandler(bds));
            imageBodyPart.setHeader("Content-ID", "<image>");

            imageBodyPart.setFileName("Example.png");
            multipart.addBodyPart(imageBodyPart);

            //attachment
            MimeBodyPart textBodyPart = new MimeBodyPart();
            ByteArrayDataSource tds = new ByteArrayDataSource("text".getBytes(Charset.forName("UTF-8")), "text/plain");
            textBodyPart.setDataHandler(new DataHandler(tds));
            textBodyPart.setHeader("Content-ID", "<text>");
            textBodyPart.setFileName("Example.txt");
            multipart.addBodyPart(textBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String SendEmail(String fileName, String prejemnik) {
             String sended_error;
             sended_error = "";
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(prejemnik)
            );
            message.setHeader("Content-Type", "text/html; charset=UTF-8");
            message.setSubject("Sintal tehnika servisni nalog");
            //message.setText("Welcome to Medium!");

            Multipart multipart = new MimeMultipart();
            TimeZone.getDefault();
            //text
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<p style='font-size:14px;'>V priponki vam posiljamo podpisan servisni nalog</p>";
            messageBodyPart.setContent(htmlText, "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);

            //image
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //byte[] imageInByte = baos.toByteArray();

            //MimeBodyPart imageBodyPart = new MimeBodyPart();
            //ByteArrayDataSource bds = new ByteArrayDataSource(imageInByte, "image/png");
            //imageBodyPart.setDataHandler(new DataHandler(bds));
            //imageBodyPart.setHeader("Content-ID", "<image>");

            //imageBodyPart.setFileName("Example.png");
            //multipart.addBodyPart(imageBodyPart);

            //attachment
            /*
            MimeBodyPart textBodyPart = new MimeBodyPart();
            ByteArrayDataSource tds = new ByteArrayDataSource("text".getBytes(Charset.forName("UTF-8")), "text/plain");
            textBodyPart.setDataHandler(new DataHandler(tds));
            textBodyPart.setHeader("Content-ID", "<text>");
            textBodyPart.setFileName("Example.txt");
            multipart.addBodyPart(textBodyPart);
            */
            String htmlFileName = fileName;
            /*
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(htmlFileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(htmlFileName);
            multipart.addBodyPart(messageBodyPart);
            */

            File imeDat = new File(htmlFileName);
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(imeDat);
            multipart.addBodyPart(attachmentBodyPart);
            message.setContent(multipart);



            //message.setContent(new MimeMultipart(part));
            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException | IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            sended_error = sw.toString();
            //sended_error = String.valueOf(e.printStackTrace());
        }
        return sended_error;
    }
}