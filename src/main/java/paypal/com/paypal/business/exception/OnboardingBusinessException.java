package paypal.com.paypal.business.exception;

public class OnboardingBusinessException extends Exception{

    String message = "";
    StackTraceElement stackTraceElement;

    public void setMessage(Exception e) {
        this.message = e.getMessage();
        this.stackTraceElement = e.getStackTrace()[0];
    }
}
