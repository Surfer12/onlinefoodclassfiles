package payment;

import java.time.LocalDateTime;

public class Payment {
   private Long paymentId;
   private Long orderId;
   private String paymentMethod;
   private double amount;
   private LocalDateTime paymentTime;
   private boolean isProcessed;
   private boolean isRefunded;

   public Payment(Long orderId, String paymentMethod, double amount) {
      this.orderId = orderId;
      this.paymentMethod = paymentMethod;
      this.amount = amount;
      this.isProcessed = false;
      this.isRefunded = false;
   }

   public boolean processPayment() {
      if (!isProcessed && !isRefunded) {
         // Here you would typically integrate with a payment gateway
         this.paymentTime = LocalDateTime.now();
         this.isProcessed = true;
         return true;
      }
      return false;
   }

   public boolean refundPayment() {
      if (isProcessed && !isRefunded) {
         // Here you would typically integrate with a payment gateway for refund
         this.isRefunded = true;
         return true;
      }
      return false;
   }

   // Getters
   public Long getPaymentId() {
      return paymentId;
   }

   public Long getOrderId() {
      return orderId;
   }

   public String getPaymentMethod() {
      return paymentMethod;
   }

   public double getAmount() {
      return amount;
   }

   public LocalDateTime getPaymentTime() {
      return paymentTime;
   }

   public boolean isProcessed() {
      return isProcessed;
   }

   public boolean isRefunded() {
      return isRefunded;
   }
}