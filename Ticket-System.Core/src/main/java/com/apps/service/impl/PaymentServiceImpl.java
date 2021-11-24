package com.apps.service.impl;

import com.apps.contants.*;
import com.apps.domain.entity.*;
import com.apps.domain.repository.PaymentCustomRepository;
import com.apps.mybatis.mysql.PaymentRepository;
import com.apps.mybatis.mysql.PromotionRepository;
import com.apps.service.OrdersService;
import com.apps.service.PaymentService;
import com.apps.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    private final PaymentCustomRepository paymentCustomRepository;

    private final UserService userService;

    private final OrdersService ordersService;

    private final PromotionRepository promotionRepository;


    @Override
    public int insert(Payment payment) {
        return paymentRepository.insert(payment);
    }

    @Override
    public List<Payment> findAll(int limit, int offset, String sort, String order, String createdDate, String useFor, String status, Integer creation,Integer method) {

        return this.paymentRepository.findAll(limit,offset,sort,order,createdDate,useFor,status,userService.getUserFromContext(),method > 0 ? method :null);
    }

    @Override
    public int findAllCount(String createdDate, String useFor, String status, Integer creation,Integer method) {
        return this.paymentRepository.findAllCount(createdDate,useFor,status,userService.getUserFromContext(),method > 0 ? method :null);
    }

    @Override
    public List<PaymentMethod> findAllPaymentMethod() {
        return this.paymentRepository.findAllPaymentMethod();
    }

    @Override
    public int findCountPaymentMethod() {
        return this.paymentRepository.findAllCountPaymentMethod();
    }

    @Override
    public int insertReturnedId(Payment payment) throws SQLException {
        payment.setStatus(PaymentStatus.Verified.getValue());
        String sql = "Insert into payment(part_id,payment_method_id," +
                "creation,status,transaction_id,created_date,amount,use_for," +
                "note,user_id) values(?,?,?,?,?,?,?,?,?,?)";
        payment.setCreation(userService.getUserFromContext());
        payment.setCreatedDate(userService.getNowDateTime());
        payment.setUseFor(payment.getUseFor().equals("Ticket") ? PaymentFor.TICKET.getValue() :
                payment.getUseFor().equals("Gift") ? PaymentFor.GIFT.getValue() : PaymentFor.MEMBER_CASH.getValue());
        int result = this.paymentCustomRepository.insert(payment,sql);
        if(result > 0){
            var order = this.ordersService.findById(payment.getPartId());
            var orders = Orders.builder()
                    .id(order.getId())
                    .updatedBy(userService.getUserFromContext())
                    .updatedAt(Utilities.getCurrentTime())
                    .status(OrderStatus.PAYMENT.getStatus())
                    .build();
            if(!payment.getCode().isEmpty()){
                var offerCode =
                        this.promotionRepository.checkPromotionCode(payment.getCode());
                var offer = this.promotionRepository.findById(offerCode.getOfferId());
                var discountAmount = Utilities.getDiscountByCode(payment.getCode(),order.getTotalAmount(),offer);
                var offerHistory = OfferHistory.builder()
                        .offerId(offer.getId())
                        .orderId(payment.getPartId())
                        .userId( order.getUserId())
                        .timeUsed(Utilities.getCurrentTime())
                        .status(OfferStatus.USED.name())
                        .code(offerCode.getCode())
                        .totalDiscount(discountAmount)
                        .build();
                this.promotionRepository.insertOfferHistory(offerHistory);
            }
            this.ordersService.update(orders);
        }
        return result;
    }



    @Override
    public PaymentMethod findPaymentMethodById(int id) {
        return this.paymentRepository.findPaymentMethodById(id);
    }

    @Override
    public int deleteByOrder(int id) {
        var payment = this.paymentRepository.findByIdOrder(id);
        return this.paymentRepository.deleteByOrder(payment.getId());
    }

    @Override
    public Payment findByOrder(int idOrder) {
        var payment = this.paymentRepository.findByIdOrder(idOrder);
        return payment;
    }


}
