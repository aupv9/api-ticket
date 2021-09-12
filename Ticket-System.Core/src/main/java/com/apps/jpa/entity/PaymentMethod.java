package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "payment_method", schema = "booksystem", catalog = "")
public class PaymentMethod implements Serializable {
    private int id;
    private String code;
    private String name;
    private Collection<Payment> paymentsById;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "code", nullable = true, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethod that = (PaymentMethod) o;
        return id == that.id && Objects.equals(code, that.code) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name);
    }

    @OneToMany(mappedBy = "paymentMethodByPaymentMethodId")
    public Collection<Payment> getPaymentsById() {
        return paymentsById;
    }

    public void setPaymentsById(Collection<Payment> paymentsById) {
        this.paymentsById = paymentsById;
    }
}
