package be.mystore.util;

public class EmailContentBuilder {

    public static String buildActivationEmail(String activationLink) {
        return "<p>Xin chào,</p>"
                + "<p>Vui lòng kích hoạt tài khoản của bạn bằng cách nhấp vào liên kết dưới đây:</p>"
                + "<p><a href=\"" + activationLink + "\">Bấm vào đây để kích hoạt tài khoản</a></p>"
                + "<p>Trân trọng,</p>"
                + "<p>Đội ngũ hỗ trợ MyStore</p>";
    }
    public static String buildPasswordResetEmail(String userName, String newPassword) {
        return "<p>Xin chào,</p>"
                + "<p>Tài khoản của bạn là: " + userName + "</p>"
                + "<p>Mật khẩu mới của bạn là: " + newPassword + "</p>"
                + "<p>Trân trọng,</p>"
                + "<p>Đội ngũ hỗ trợ MyStore</p>";
    }
}
