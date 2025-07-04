package br.com.ifsp.tsi.bugtrackerbackend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String to, String resetCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Código de Recuperação de Senha - BugTracker");
            message.setText("Seu código de recuperação de senha é: " + resetCode + "\n\n" +
                    "Este código expira em 5 minutos e só pode ser usado uma vez.\n\n" +
                    "Se você não solicitou este código, ignore este email.");
            message.setFrom("bugtracker.naoresponda@gmail.com");

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email: " + e.getMessage());
        }
    }

}
