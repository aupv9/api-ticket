package com.apps.service.impl;

import com.apps.contants.OfferStatus;
import com.apps.contants.OfferType;
import com.apps.contants.Utilities;
import com.apps.domain.entity.Offer;
import com.apps.domain.entity.OfferCode;
import com.apps.domain.entity.OfferDetail;
import com.apps.domain.repository.OfferCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.job.EmailJob;
import com.apps.job.NewsLetterJob;
import com.apps.mybatis.mysql.PromotionRepository;
import com.apps.request.EmailNewsletter;
import com.apps.request.OfferDto;
import com.apps.service.PromotionService;
import com.apps.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.downgoon.snowflake.Snowflake;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PromotionServiceImp implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final UserService userService;
    private final OfferCustomRepository offerCustomRepository;

    @Autowired
    private Scheduler scheduler;


    @Override
    public List<Offer> findAll(int limit, int offset, String sort, String order, String startDate, String endDate, Integer creationBy,
                               String creationDate, boolean anonProfile,
                               String promotionType, String method, boolean multi,String search) {
        return this.promotionRepository.findAll(limit,offset,sort,order,startDate,endDate,creationBy > 0 ? creationBy : null,creationDate,
                anonProfile,promotionType,method,multi,search);
    }

    @Override
    public int findAllCount(String startDate, String endDate, Integer creationBy,
                            String creationDate, boolean anonProfile, String promotionType,
                            String method, boolean multi,String search) {
        return this.promotionRepository.findAllCount(startDate,endDate,creationBy > 0 ? creationBy : null,creationDate,
                anonProfile,promotionType,method,multi,search);
    }

    @Override
    public int insertOffer(OfferDto offerDto) throws SQLException {
        String sql = "insert into offer(name,creation_date,start_date,end_date,type,method,creationBy," +
                "max_discount,max_total_usage,max_usage_per_user,rule,percentage,anon_profile," +
                "allow_multiple,message,status,discount_amount) values(?,?,?,?,?," +
                "?,?,?,?,?," +
                "?,?,?,?,?,?,?)";

        var offer = Offer.builder()
                .name(offerDto.getName()).type(offerDto.getType()).method(offerDto.getMethod())
                .startDate(convertISOtoLocalDatetime(offerDto.getStartDate())).endDate(
                        convertISOtoLocalDatetime(offerDto.getEndDate()))
                .creationBy(offerDto.getCreationBy()).creationDate(Utilities.getCurrentTime())
                .maxDiscount(offerDto.getMaxDiscount()).maxTotalUsage(offerDto.getMaxTotalUsage())
                .maxUsagePerUser(offerDto.getMaxUsagePerUser()).rule(offerDto.getRule())
                .percentage(offerDto.getPercentage()).anonProfile(offerDto.isAnonProfile())
                .allowMultiple(offerDto.isAllowMultiple()).message(offerDto.getMessage())
                .status(OfferStatus.NEW.name()).discountAmount(offerDto.getDiscountAmount())
                .build();

        int idOffer = this.offerCustomRepository.insert(offer,sql);
        if(idOffer > 0){
            switch (offer.getMethod()){
                case "Coupon":
                    this.insertOfferDetail(idOffer,offerDto.getCode());
                    this.insertOfferMovie(idOffer,offerDto.getMovieIds());
                    break;
                case "Voucher":
                    this.insertOfferMovie(idOffer,offerDto.getMovieIds());
                    this.insertCodeChoVoucher(idOffer,offerDto.getCountCode());
                    break;
            }
        }
        return idOffer;
    }

    @Override
    public OfferCode checkPromotionCode(String code,Integer movie) {

        var offerCode = this.promotionRepository.checkPromotionCode(code);
        if(offerCode != null){
            var offer = this.promotionRepository.findById(offerCode.getOfferId());
            if(offer.getMaxTotalUsage() <= 0){
                throw new NotFoundException("Promo Code of movie invalid");
            }
            var offerMovie = this.promotionRepository.findOfferMovie(offerCode.getOfferId(),movie);
            if(offerMovie == null){
                throw new NotFoundException("Promo Code of movie invalid");
            }
        }

        return offerCode;
    }

    @Override
    public Offer findById(int id) {
        return this.promotionRepository.findById(id);
    }

    @Override
    public List<OfferDetail> findAllOfferDetail(Integer limit, Integer offset, String sort, String order, Integer offer) {
        return this.promotionRepository.findAllOfferDetail(limit,offset,sort,order,offer > 0 ? offer : null);
    }

    @Override
    public int findAllCountOfferDetail(Integer offer) {
        return this.promotionRepository.findAllCountOfferDetail(offer > 0 ? offer : null);
    }

    @Override
    public int insertSubNewsLetter(String email) {
        return this.promotionRepository.insertNewsletter(email);
    }

    private JobDetail buildJobDetail(List<EmailNewsletter> emailNewsletters) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("data",emailNewsletters);
        return JobBuilder.newJob(NewsLetterJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Email Promotion Trigger")
                .startAt(Date.valueOf(LocalDate.now()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }



    @Override
    public int sendToSubscriber(List<Integer> offers) {
        var listEmail = this.findAllSubscriber();
        var listEmailNews = new ArrayList<EmailNewsletter>();
        for (var email : listEmail){
            var newsLetterEmail = new EmailNewsletter();
            newsLetterEmail.setEmail(email);
            listEmailNews.add(newsLetterEmail);
        }

        offers.forEach(offer ->{
            var listCode = this.promotionRepository.findAllCodeInOffer(offer);
            var offerOrigin = this.findById(offer);
            if(offerOrigin.getMethod().equals(OfferType.Coupon.name())){
                for (var email : listEmailNews){
                    email.setCode(listCode.get(0));
                }
            }else{
                var listCodeRemain = this.promotionRepository.findCodeRemain(offer);
                for (int i = 0; i < listEmailNews.size(); i++) {
                    if(listCodeRemain.size() >= listEmailNews.size()){
                        listEmailNews.get(i).setCode(listCodeRemain.get(i));
                    }
                }
            }
            JobDetail jobDetail = buildJobDetail(listEmailNews);
            Trigger trigger = buildJobTrigger(jobDetail);
            try {
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
        return 1;
    }

    @Override
    public List<String> findAllSubscriber() {
        return this.promotionRepository.findAllSubscriber();
    }

    private String convertISOtoLocalDatetime(String isoDate){
        return Timestamp.from(Instant.parse(isoDate)).toString();
    }

    private int insertOfferDetail(int offerId,String code){
        return this.promotionRepository.insertOfferDetail(offerId,code);
    }

    private void insertOfferMovie(int offerId,Integer[] movieIds){
        for (Integer movie:movieIds){
            this.promotionRepository.insertOfferMovie(offerId,movie);
        }
    }

    private void insertCodeChoVoucher(int offerId,int count){
//        String code = RandomStringUtils.random(7, true, true);
//        while (count > 0 && !this.checkCodeOfOffer(offerId,code)){
//            this.insertOfferDetail(offerId,code);
//            count--;
//        }
        Snowflake snowflake = new Snowflake(1, 1);
        for (int i = 0; i < count; i++) {
            long promoCode = snowflake.nextId();
            this.insertOfferDetail(offerId,String.valueOf(promoCode));
        }
    }

    private boolean checkCodeOfOffer(int offerId,String code){
        return this.promotionRepository.checkCodeOffer(offerId,code) > 0;
    }



}
