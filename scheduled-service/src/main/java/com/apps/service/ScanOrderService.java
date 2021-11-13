package com.apps.service;


public class ScanOrderService {
//
//    private final OrdersRepository ordersRepository;
//
//    public ScanOrderService(OrdersRepository ordersRepository) {
//        this.ordersRepository = ordersRepository;
//    }
//
//
//    @Scheduled(fixedDelay = 100000)
//    public void reportCurrentTime() {
//        String currentTime = Utilities.getCurrentTime();
//        var listOrderExpire = this.ordersRepository.findAllOrderExpiredReserved(currentTime);
//        for (Integer order : listOrderExpire){
//            var listOrdersDetail = this.ordersRepository.findOrderDetailById(order);
//            for (Integer orderDetail: listOrdersDetail){
//                int deleted = this.ordersRepository.deleteOrderDetail(orderDetail);
//            }
//            var listOrdersSeat = this.ordersRepository.findOrderSeatById(order);
//            for (Integer orderSeat: listOrdersDetail){
//                int deleted = this.ordersRepository.deleteOrderSeat(orderSeat);
//            }
//            int deleted = this.ordersRepository.delete(order);
//        }
//    }
}
