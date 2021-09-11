package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Payment implements Serializable {
    private int id;
    private int paymentMethodId;
    private Double amount;
    private int ticketId;
    private String transactionId;
    private PaymentMethod paymentMethodByPaymentMethodId;
    private Ticket ticketByTicketId;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "payment_method_id", nullable = false)
    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    @Basic
    @Column(name = "amount", nullable = true, precision = 0)
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "ticket_id", nullable = false)
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    @Basic
    @Column(name = "transactionId", nullable = true, length = 255)
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id == payment.id && paymentMethodId == payment.paymentMethodId && ticketId == payment.ticketId && Objects.equals(amount, payment.amount) && Objects.equals(transactionId, payment.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentMethodId, amount, ticketId, transactionId);
    }

    @ManyToOne
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public PaymentMethod getPaymentMethodByPaymentMethodId() {
        return paymentMethodByPaymentMethodId;
    }

    public void setPaymentMethodByPaymentMethodId(PaymentMethod paymentMethodByPaymentMethodId) {
        this.paymentMethodByPaymentMethodId = paymentMethodByPaymentMethodId;
    }

    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Ticket getTicketByTicketId() {
        return ticketByTicketId;
    }

    public void setTicketByTicketId(Ticket ticketByTicketId) {
        this.ticketByTicketId = ticketByTicketId;
    }
}
