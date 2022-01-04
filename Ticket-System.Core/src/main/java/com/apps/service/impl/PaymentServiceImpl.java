package com.apps.service.impl;

import com.apps.config.kafka.Message;
import com.apps.contants.*;
import com.apps.domain.entity.*;
import com.apps.domain.entity.PaymentMethod;
import com.apps.domain.repository.PaymentCustomRepository;
import com.apps.mapper.PaymentDto;
import com.apps.mybatis.mysql.PaymentRepository;
import com.apps.mybatis.mysql.PromotionRepository;
import com.apps.service.*;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    private final PaymentCustomRepository paymentCustomRepository;

    private final UserService userService;

    private final OrdersService ordersService;

    private final PromotionRepository promotionRepository;

    private final KafkaTemplate<String, Message> kafkaTemplate;

    private final SeatService seatService;

    private final MemberService memberService;

    private final AuditLogService auditLogService;


    private void newAuditLog(AuditLog auditLog){
        this.auditLogService.insert(auditLog);
    }

    @Override
    public int insert(Payment payment) {
        int inserted = paymentRepository.insert(payment);
        if(inserted > 0){
            var user = this.userService.findById(this.userService.getUserFromContext());
            if(user != null){
                var auditLog = AuditLog.builder()
                        .objectName(ObjectName.Payment.name())
                        .accountName(user.getEmail())
                        .resourceName(Resource.Payment.name())
                        .actionDate(Utilities.getCurrentTime())
                        .objectName(ObjectType.AL.name())
                        .action(AuditAction.CE.name())
                        .actionStatus(ActionStatus.Success.name())
                        .build();
                newAuditLog(auditLog);
            }
        }
        return inserted;

    }

    @Override
    public List<Payment> findAll(Integer limit, Integer offset, String sort, String order, String createdDate, String useFor, String status,
                                 Integer creation,Integer method) {
        var userId = userService.getUserFromContext();
        return this.paymentRepository.findAll(limit,offset,sort,order,createdDate,useFor,status,
                this.userService.isSeniorManager(userId) ? null :
                        userId,method > 0 ? method :null);
    }

    @Override
    public int findAllCount(String createdDate, String useFor, String status,
                            Integer creation,Integer method) {
        var userId = userService.getUserFromContext();
        return this.paymentRepository.findAllCount(createdDate,useFor,status,
                this.userService.isSeniorManager(userId) ? null : userId,
                method > 0 ? method :null);
    }

    @Override
    public List<com.apps.domain.entity.PaymentMethod> findAllPaymentMethod() {
        return this.paymentRepository.findAllPaymentMethod();
    }

    @Override
    public int findCountPaymentMethod() {
        return this.paymentRepository.findAllCountPaymentMethod();
    }

    private void createPaymentCash(PaymentDto paymentDto){

    }

    private double discountAmountMember(double amount,double discount){
        return (amount / 100) * discount;
    }
    private double pointFromAmount(double amount){
        return amount / 1000;
    }

    private double amountRemain(double amount,double discount){
        return amount - discount;
    }

    private int insertWithPaypal(PaymentDto paymentDto) throws ExecutionException, InterruptedException, SQLException {
        var payment = new Payment();
        payment.setStatus(PaymentStatus.Complete.getValue());
        String sql = "Insert into payment(part_id,payment_method_id," +
                "creation,status,transaction_id,created_date,amount,use_for," +
                "note,user_id) values(?,?,?,?,?,?,?,?,?,?)";
        payment.setPaymentMethodId(paymentDto.getPaymentMethodId());
        payment.setPaymentMethodId(paymentDto.getPaymentMethodId());
        payment.setCreation(userService.getUserFromContext());
        payment.setCreatedDate(userService.getNowDateTime());
        payment.setPartId(paymentDto.getPartId());
        payment.setUserId(paymentDto.getUserId());
        payment.setUseFor(payment.getUseFor().equals("Ticket") ? PaymentFor.TICKET.getValue() :
                payment.getUseFor().equals("Gift") ? PaymentFor.GIFT.getValue() : PaymentFor.MEMBER_CASH.getValue());
        payment.setNote(paymentDto.getNote());
        payment.setAmount(paymentDto.getAmount());
        payment.setUserId(paymentDto.getUserId());

        int result = this.paymentCustomRepository.insert(payment, sql);
        if (result > 0) {
            var user = this.userService.findById(this.userService.getUserFromContext());
            if (user != null) {
                var auditLog = AuditLog.builder()
                        .objectName(ObjectName.Payment.name())
                        .accountName(user.getEmail())
                        .resourceName(Resource.Payment.name())
                        .actionDate(Utilities.getCurrentTime())
                        .objectName(ObjectType.AL.name())
                        .action(AuditAction.CE.name())
                        .actionStatus(ActionStatus.Success.name())
                        .build();
                newAuditLog(auditLog);
            }

            var order = this.ordersService.findById(payment.getPartId());
            var orders = Orders.builder()
                    .id(order.getId())
                    .updatedBy(userService.getUserFromContext())
                    .updatedAt(Utilities.getCurrentTime())
                    .status(OrderStatus.PAYMENT.getStatus())
                    .build();
            if (!paymentDto.getCode().isEmpty()) {
                var offerCode =
                        this.promotionRepository.checkPromotionCode(paymentDto.getCode());
                var offer = this.promotionRepository.findById(offerCode.getOfferId());
                int remainUse = offer.getMaxTotalUsage();
                if (remainUse > 0) {
                    var discountAmount = Utilities.getDiscountByCode(payment.getCode(), order.getTotalAmount(), offer);
                    var offerHistory = OfferHistory.builder()
                            .offerId(offer.getId())
                            .orderId(payment.getPartId())
                            .userId(order.getUserId())
                            .timeUsed(Utilities.getCurrentTime())
                            .status(OfferStatus.USED.name())
                            .code(offerCode.getCode())
                            .totalDiscount(discountAmount)
                            .build();
                    int inserted = this.promotionRepository.insertOfferHistory(offerHistory);
                    if (inserted > 0) {
                        if (user != null) {
                            var auditLog = AuditLog.builder()
                                    .objectName(ObjectName.OfferHistory.name())
                                    .accountName(user.getEmail())
                                    .resourceName(Resource.OfferHistory.name())
                                    .actionDate(Utilities.getCurrentTime())
                                    .objectName(ObjectType.AL.name())
                                    .action(AuditAction.CE.name())
                                    .actionStatus(ActionStatus.Success.name())
                                    .build();
                            newAuditLog(auditLog);
                        }
                    }
                    int updated = this.promotionRepository.updateMaxTotalUsage(offer.getMaxTotalUsage() - 1, offer.getId());
                    if (updated > 0) {
                        if (user != null) {
                            var auditLog = AuditLog.builder()
                                    .objectName(ObjectName.Offer.name())
                                    .accountName(user.getEmail())
                                    .resourceName(Resource.Offer.name())
                                    .actionDate(Utilities.getCurrentTime())
                                    .objectName(ObjectType.AL.name())
                                    .action(AuditAction.MO.name())
                                    .actionStatus(ActionStatus.Success.name())
                                    .build();
                            newAuditLog(auditLog);
                        }
                    }
                }
            }
            int updated = this.ordersService.update(orders);
            if (updated > 0) {
                if (user != null) {
                    var auditLog = AuditLog.builder()
                            .objectName(ObjectName.Order.name())
                            .accountName(user.getEmail())
                            .resourceName(Resource.Orders.name())
                            .actionDate(Utilities.getCurrentTime())
                            .objectName(ObjectType.AL.name())
                            .action(AuditAction.MO.name())
                            .actionStatus(ActionStatus.Success.name())
                            .build();
                    newAuditLog(auditLog);
                }
            }
            this.ordersService.sendDataToClient();
            this.seatService.sendDataToClient(order.getShowTimesDetailId());
        }
        return result;
    }

    private int paymentWithout(PaymentDto paymentDto) throws SQLException, ExecutionException, InterruptedException {
        var payment = new Payment();
        payment.setStatus(PaymentStatus.Verified.getValue());
        String sql = "Insert into payment(part_id,payment_method_id," +
                "creation,status,transaction_id,created_date,amount,use_for," +
                "note,user_id) values(?,?,?,?,?,?,?,?,?,?)";
        payment.setPaymentMethodId(paymentDto.getPaymentMethodId());
        payment.setPaymentMethodId(paymentDto.getPaymentMethodId());
        payment.setCreation(userService.getUserFromContext());
        payment.setCreatedDate(userService.getNowDateTime());
        payment.setPartId(paymentDto.getPartId());
        payment.setUserId(paymentDto.getUserId());
        payment.setUseFor(payment.getUseFor().equals("Ticket") ? PaymentFor.TICKET.getValue() :
                payment.getUseFor().equals("Gift") ? PaymentFor.GIFT.getValue() : PaymentFor.MEMBER_CASH.getValue());
        payment.setNote(paymentDto.getNote());
        payment.setAmount(paymentDto.getAmount());
        payment.setUserId(paymentDto.getUserId());
        if(paymentDto.getNumberMember() > 0){
            var member = this.memberService.findById(paymentDto.getNumberMember());
            if(member != null){
                var level = this.memberService.checkLevelMember(member.getPoint());
                if(level.equals(MemberEnum.MEMBER.name())){
                    var discount = this.discountAmountMember(paymentDto.getAmount(),
                            DiscountMember.MEMBER_DISCOUNT);
                    var amountRemain = paymentDto.getAmount() - discount;
                    payment.setAmount(amountRemain);
                }else if(level.equals(MemberEnum.VIP.name())){
                    var discount = this.discountAmountMember(paymentDto.getAmount(),
                            DiscountMember.VIP_DISCOUNT);
                    var amountRemain = paymentDto.getAmount() - discount;
                    payment.setAmount(amountRemain);
                }else if(level.equals(MemberEnum.SVIP.name())){
                    var discount = this.discountAmountMember(paymentDto.getAmount(),
                            DiscountMember.S_VIP_DISCOUNT);
                    var amountRemain = paymentDto.getAmount() - discount;
                    payment.setAmount(amountRemain);
                }else{

                }
                member.setPoint(member.getPoint() +  this.pointFromAmount(paymentDto.getAmount()));
                this.memberService.update(member);
            }
        }

        int result = this.paymentCustomRepository.insert(payment,sql);
        if(result > 0){
            var user = this.userService.findById(this.userService.getUserFromContext());
            if(user != null){
                var auditLog = AuditLog.builder()
                        .objectName(ObjectName.Payment.name())
                        .accountName(user.getEmail())
                        .resourceName(Resource.Payment.name())
                        .actionDate(Utilities.getCurrentTime())
                        .objectName(ObjectType.AL.name())
                        .action(AuditAction.CE.name())
                        .actionStatus(ActionStatus.Success.name())
                        .build();
                newAuditLog(auditLog);
            }

            var order = this.ordersService.findById(payment.getPartId());
            var orders = Orders.builder()
                    .id(order.getId())
                    .updatedBy(userService.getUserFromContext())
                    .updatedAt(Utilities.getCurrentTime())
                    .status(OrderStatus.ORDERED.getStatus())
                    .build();
            if(!paymentDto.getCode().isEmpty()){
                var offerCode =
                        this.promotionRepository.checkPromotionCode(paymentDto.getCode());
                var offer = this.promotionRepository.findById(offerCode.getOfferId());
                int remainUse = offer.getMaxTotalUsage();
                if(remainUse > 0){
                    var discountAmount = Utilities.getDiscountByCode(payment.getCode(),order.getTotalAmount(),offer);
                    var offerHistory = OfferHistory.builder()
                            .offerId(offer.getId())
                            .orderId(payment.getPartId())
                            .userId(order.getUserId())
                            .timeUsed(Utilities.getCurrentTime())
                            .status(OfferStatus.USED.name())
                            .code(offerCode.getCode())
                            .totalDiscount(discountAmount)
                            .build();
                    int inserted = this.promotionRepository.insertOfferHistory(offerHistory);
                    if(inserted > 0){
                        if(user != null){
                            var auditLog = AuditLog.builder()
                                    .objectName(ObjectName.OfferHistory.name())
                                    .accountName(user.getEmail())
                                    .resourceName(Resource.OfferHistory.name())
                                    .actionDate(Utilities.getCurrentTime())
                                    .objectName(ObjectType.AL.name())
                                    .action(AuditAction.CE.name())
                                    .actionStatus(ActionStatus.Success.name())
                                    .build();
                            newAuditLog(auditLog);
                        }
                    }
                    int updated = this.promotionRepository.updateMaxTotalUsage(offer.getMaxTotalUsage() - 1,offer.getId());
                    if(updated > 0){
                        if(user != null){
                            var auditLog = AuditLog.builder()
                                    .objectName(ObjectName.Offer.name())
                                    .accountName(user.getEmail())
                                    .resourceName(Resource.Offer.name())
                                    .actionDate(Utilities.getCurrentTime())
                                    .objectName(ObjectType.AL.name())
                                    .action(AuditAction.MO.name())
                                    .actionStatus(ActionStatus.Success.name())
                                    .build();
                            newAuditLog(auditLog);
                        }
                    }
                }
            }
            int updated = this.ordersService.update(orders);
            if(updated > 0){
                if(user != null) {
                    var auditLog = AuditLog.builder()
                            .objectName(ObjectName.Order.name())
                            .accountName(user.getEmail())
                            .resourceName(Resource.Orders.name())
                            .actionDate(Utilities.getCurrentTime())
                            .objectName(ObjectType.AL.name())
                            .action(AuditAction.MO.name())
                            .actionStatus(ActionStatus.Success.name())
                            .build();
                    newAuditLog(auditLog);
                }
            }
            this.ordersService.sendDataToClient();
            this.seatService.sendDataToClient(order.getShowTimesDetailId());
        }
        return result;
    }
    @Override
    public int insertReturnedId(PaymentDto paymentDto) throws SQLException, ExecutionException, InterruptedException {
        var paymentMethod = this.paymentRepository.findPaymentMethodById(paymentDto.getPaymentMethodId());
        if(paymentMethod == null){
            return 0;
        }
        if(paymentMethod.getCode().equals(com.apps.contants.PaymentMethod.PAYPAL.name())){
            return this.insertWithPaypal(paymentDto);
        }else{
            return this.paymentWithout(paymentDto);
        }
    }

    @Override
    public com.apps.domain.entity.PaymentMethod findPaymentMethodById(int id) {
        return this.paymentRepository.findPaymentMethodById(id);
    }

    @Override
    public int deleteByOrder(int id) {
        var payment = this.paymentRepository.findByIdOrder(id);
        return this.paymentRepository.deleteByOrder(payment.getId());
    }

    @Override
    public Payment findByOrder(int idOrder) {
        return this.paymentRepository.findByIdOrder(idOrder);
    }

    @Override
    public PaymentDto findById(int id) {
        return this.paymentRepository.findById(id);
    }

    @Override
    public int update(PaymentDto paymentDto) throws ExecutionException, InterruptedException {
        var payment = this.findById(paymentDto.getId());
        if(paymentDto.getUseFor().equals(PaymentFor.TICKET.getValue())){
            var order = this.ordersService.findOrdersById(paymentDto.getPartId());
            order.setStatus(OrderStatus.PAYMENT.getStatus());
            order.setUpdatedAt(Utilities.getCurrentTime());
            order.setUpdatedBy(this.userService.getUserFromContext());
            this.ordersService.update(order);
            this.ordersService.sendDataToClient();
        }
        payment.setStatus(paymentDto.getStatus());
        payment.setAmount(paymentDto.getAmount());
        int updated = this.paymentRepository.update(payment);
        if(updated > 0){
            var user = this.userService.findById(this.userService.getUserFromContext());
            if(user != null){
                var auditLog = AuditLog.builder()
                        .objectName(ObjectName.Payment.name())
                        .accountName(user.getEmail())
                        .resourceName(Resource.Payment.name())
                        .actionDate(Utilities.getCurrentTime())
                        .objectName(ObjectType.AL.name())
                        .action(AuditAction.MO.name())
                        .actionStatus(ActionStatus.Success.name())
                        .build();
                newAuditLog(auditLog);
            }
        }
        return updated;
    }

    @Override
    @Cacheable(value = "PaymentService" ,key = "'ffindAllByDate_'+#date+'-'+#method", unless = "#result == null")
    public List<Payment> findAllByDate(String date,Integer method) {
        return this.paymentRepository.findAllByDate(Utilities.convertIsoToDate(date),method);
    }

    @Override
    @Cacheable(value = "PaymentService" ,key = "'findByCode_'+#code+'-'+#method", unless = "#result == null")
    public PaymentMethod findByCode(String code) {
        return this.paymentRepository.findByCode(code);
    }


}
