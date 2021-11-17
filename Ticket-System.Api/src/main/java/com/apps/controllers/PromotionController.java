package com.apps.controllers;

import com.apps.request.OfferDto;
import com.apps.response.ResponseRA;
import com.apps.service.OfferHistoryService;
import com.apps.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class PromotionController {

    private final PromotionService promotionService;
    private final OfferHistoryService offerHistoryService;
    public PromotionController(PromotionService promotionService, OfferHistoryService offerHistoryService) {
        this.promotionService = promotionService;
        this.offerHistoryService = offerHistoryService;
    }

    @GetMapping("offers")
    public ResponseEntity<?> getOffers(@RequestParam(value =  "pageSize", required = false) Integer size,
                                       @RequestParam(value = "page", required = false)Integer page,
                                       @RequestParam(value = "sort", required = false) String sort,
                                       @RequestParam(value = "order", required = false) String order,
                                       @RequestParam(value = "created_date",  required = false) String createdDate,
                                       @RequestParam(value = "start_date",  required = false) String startDate,
                                       @RequestParam(value = "end_date",  required = false) String endDate,
                                       @RequestParam(value = "creation", required = false,
                                                 defaultValue = "0") Integer creation,
                                       @RequestParam(value = "method", required = false) String method,
                                       @RequestParam(value = "anon_profile", required = false) boolean anonProfile,
                                       @RequestParam(value = "type", required = false) String type,
                                       @RequestParam(value = "multi", required = false) boolean multi,
                                       @RequestParam(value = "search", required = false) String search
                                         ){

        var resultList = this.promotionService.findAll(size, (page - 1 ) * size,sort,order,startDate,
                endDate,creation,createdDate,anonProfile,type,method,multi,search);
        var totalElements = this.promotionService.findAllCount(startDate,endDate,creation,createdDate
                ,anonProfile,type,method,multi,search);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("offers")
    public ResponseEntity<?> createCategory(@RequestBody OfferDto offerDto) throws SQLException {
        int idReturned = this.promotionService.insertOffer(offerDto);
        offerDto.setId(idReturned);
        return ResponseEntity.ok(offerDto);
    }

    @GetMapping("offers-history")
    public ResponseEntity<?> getOfferHistory(@RequestParam(value =  "pageSize", required = false) Integer size,
                                             @RequestParam(value = "page", required = false)Integer page,
                                             @RequestParam(value = "sort", required = false) String sort,
                                             @RequestParam(value = "order", required = false) String order,
                                             @RequestParam(value = "offer_id", required = false)Integer offerId,
                                             @RequestParam(value = "user_id", required = false)Integer userId,
                                             @RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "time_used", required = false) String timeUsed

                                             ){

        var resultList = this.offerHistoryService.findAll(size, (page - 1 ) * size,sort,order,
                userId,offerId,status,timeUsed);
        var totalElements = this.offerHistoryService.findAllCount(userId,offerId,status,timeUsed);
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("offers-history")
    public ResponseEntity<?> createOfferHistory(@RequestBody OfferDto offerDto) throws SQLException {
        int idReturned = this.promotionService.insertOffer(offerDto);
        offerDto.setId(idReturned);
        return ResponseEntity.ok(offerDto);
    }
}
