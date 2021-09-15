package com.apps.controllers;


import com.apps.domain.entity.Theater;
import com.apps.domain.entity.Tier;
import com.apps.service.TierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class TierController {

    @Autowired
    private TierService tierService;

    @GetMapping("tiers")
    public ResponseEntity<?> getTiers(@RequestParam("size") Integer size,
                                          @RequestParam("page") Integer page){
        return ResponseEntity.ok(this.tierService.findAll(page,size));
    }

    @PostMapping(value = "/tier",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createAccountStatus(@RequestBody Tier tier) throws SQLException {
        int id = this.tierService.insert(tier);
        log.info("Id return : "+ id);
        return ResponseEntity.ok(id);
    }
}
