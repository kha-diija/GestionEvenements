package application.utils;

import javax.mail.*;
import javax.mail.MessagingException;
import java.util.List;

import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailUtil {

    // Adresse email et mot de passe d'application
    private static final String fromEmail = "khadijakartouche368@gmail.com";  // Ton adresse Gmail
    private static final String password = "vofc uhlw tblw aiaf";              // Ton mot de passe d'application Gmail

    // Méthode pour générer un code de réinitialisation à 6 chiffres
    public static String generateResetCode() {
        int code = (int) (Math.random() * 900000) + 100000;  // Génère un code entre 100000 et 999999
        return String.valueOf(code);
    }
    
    
    
 // Méthode pour envoyer un email de confirmation d'inscription
    public static void sendConfirmationEmail(String userEmail, String nomUser, String nomEvenement)
            throws MessagingException, UnsupportedEncodingException {

        String subject = "Confirmation d'inscription à l'événement";
        String body = "Bonjour " + nomUser + ",\n\n"
                    + "Votre inscription à l'événement \"" + nomEvenement + "\" a bien été enregistrée.\n\n"
                    + "Merci de votre participation !\n"
                    + "Cordialement,\nÉquipe Événements.";

        // Paramètres SMTP pour Gmail
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Création de la session avec authentification
        javax.mail.Session session = javax.mail.Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        
        
        // Création du message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail, "Équipe Événements"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
        message.setSubject(subject);
        message.setText(body);

        // Envoi de l'email
        Transport.send(message);
        System.out.println("✅ Email de confirmation envoyé à : " + userEmail);
    }


 // Méthode pour envoyer un email d'annulation d'événement à plusieurs utilisateurs
    public static void sendCancellationEmail(List<String> userEmails, String nomEvenement, String dateEvenement)
            throws MessagingException, UnsupportedEncodingException {

        String subject = "Annulation de l'événement : " + nomEvenement;
        String body = "Bonjour,\n\n"
                    + "Nous vous informons que l'événement \"" + nomEvenement + "\" prévu pour le " + dateEvenement + " a été annulé.\n\n"
                    + "Nous vous remercions pour votre compréhension.\n"
                    + "Cordialement,\nÉquipe Événements.";

        // Paramètres SMTP pour Gmail
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Création de la session avec authentification
        javax.mail.Session session = javax.mail.Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        for (String userEmail : userEmails) {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "Équipe Événements"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("✅ Email d'annulation envoyé à : " + userEmail);
        }
    }


    
    // Méthode pour envoyer le code de réinitialisation à un utilisateur
    public static void sendResetCode(String userEmail, String code) throws MessagingException, UnsupportedEncodingException {
        // Sujet et contenu de l'e-mail
        String subject = "Réinitialisation de votre mot de passe";
        String body = "Bonjour,\n\nVoici votre code de réinitialisation : " + code + "\n\nCordialement,\nVotre équipe.";

        // Paramètres SMTP pour Gmail
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Création de la session avec authentification
        javax.mail.Session session = javax.mail.Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // Création du message
        MimeMessage message = new MimeMessage(session);
        
        // ✅ Expéditeur avec nom personnalisé
        message.setFrom(new InternetAddress(fromEmail, "Équipe Événements"));
        
        // Destinataire
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
        message.setSubject(subject);
        message.setText(body);

        // Envoi du message
        Transport.send(message);
        System.out.println("✅ Code de réinitialisation envoyé à : " + userEmail);
    }
}
