package application.utils;

import javax.mail.*;
import application.models.utilisateurs;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private static final String FROM_EMAIL = "khadijakartouche368@gmail.com"; // Remplace par ton adresse email
    private static final String FROM_PASSWORD = "vofc uhlw tblw aiaf";   // Mot de passe ou mot de passe d'application
    private static final String SMTP_HOST = "smtp.gmail.com";         // Par exemple pour Gmail
    private static final String SMTP_PORT = "587";

    
    
    private void envoyerEmailAnnulation(utilisateurs utilisateur, String nomEvenement) {
        String email = utilisateur.getEmail();
        String sujet = "Annulation de l'événement : " + nomEvenement;
        String message = "Bonjour " + utilisateur.getNom() + ",\n\n"
                + "Nous sommes désolés de vous informer que l'événement \"" + nomEvenement + "\" "
                + "auquel vous étiez inscrit a été annulé.\n\n"
                + "Merci de votre compréhension.\n\n"
                + "Cordialement,\nL’équipe organisatrice.";

        try {
            EmailService.envoyerEmail(email, sujet, message);
            System.out.println("✅ Email envoyé à : " + email);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi d'email à : " + email);
            e.printStackTrace();
        }
    }

    public static void envoyerEmail(String toEmail, String sujet, String messageText) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        javax.mail.Session session = javax.mail.Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
            }
        });


        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(sujet);
            message.setText(messageText);

            Transport.send(message);

            System.out.println("Email envoyé à : " + toEmail);

        } catch (MessagingException e) {
            System.err.println("Échec d'envoi de l'email à " + toEmail);
            e.printStackTrace();
        }

    }
}
