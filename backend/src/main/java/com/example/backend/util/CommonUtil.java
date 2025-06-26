package com.example.backend.util;

import com.example.backend.model.entity.Order;
import com.example.backend.model.entity.OrderItem;
import com.example.backend.model.entity.User;
import com.example.backend.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("daspabitra55@gmail.com", "Shooping Cart");
        helper.setTo(reciepentEmail);

        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
                + "\">Change my password</a></p>";
        helper.setSubject("Password Reset");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {

        // http://localhost:8080/forgot-password
        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace(request.getServletPath(), "");
    }

    String msg=null;;

    public Boolean sendMailForProductOrder(Order order, String status) throws Exception {
        if (order == null || order.getUser() == null) {
            throw new IllegalArgumentException("Order or user is null");
        }

        StringBuilder msg = new StringBuilder();
        msg.append("<p>Hello <b>")
                .append(order.getUser().getName())
                .append("</b>,</p>")
                .append("<p>Thank you for your order. Current status: <b>")
                .append(status)
                .append("</b>.</p>")
                .append("<p><b>Order Details:</b></p>")
                .append("<table border='1' cellpadding='5' cellspacing='0'>")
                .append("<tr><th>Product</th><th>Category</th><th>Qty</th><th>Price</th><th>Total</th></tr>");

        for (OrderItem item : order.getItems()) {
            msg.append("<tr>")
                    .append("<td>").append(item.getProduct().getTitle()).append("</td>")
                    .append("<td>").append(item.getProduct().getCategory()).append("</td>")
                    .append("<td>").append(item.getQuantity()).append("</td>")
                    .append("<td>").append(item.getPrice()).append("</td>")
                    .append("<td>").append(item.getTotal()).append("</td>")
                    .append("</tr>");
        }

        msg.append("</table>")
                .append("<p><b>Total Order Price:</b> $").append(order.getTotal()).append("</p>")
                .append("<p><b>Payment Type:</b> ").append(order.getPaymentType()).append("</p>");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("daspabitra55@gmail.com", "Shopping Cart");
        helper.setTo(order.getUser().getEmail());
        helper.setSubject("Order Confirmation - Status: " + status);
        helper.setText(msg.toString(), true);

        mailSender.send(message);
        return true;
    }


    public User getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        User User = userService.getUserByEmail(email);
        return User;
    }


}
