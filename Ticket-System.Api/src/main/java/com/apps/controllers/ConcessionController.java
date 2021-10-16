package com.apps.controllers;

import com.apps.domain.entity.Concession;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.ConcessionsService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@CrossOrigin(value = "*")
public class ConcessionController {


    private final ConcessionsService concessionsService;

    public ConcessionController(ConcessionsService concessionsService) {
        this.concessionsService = concessionsService;
    }


    @GetMapping("concessions")
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false) int size,
                                          @RequestParam(value = "page", required = false)int page,
                                          @RequestParam(value = "sort", required = false)String sort,
                                          @RequestParam(value = "order", required = false)String order,
                                          @RequestParam(value = "search", required = false)String name,
                                          @RequestParam(value = "category_id", required = false) Integer categoryId){
        List<Concession> resultList = null;
        int totalElements = 0;
        try{
            resultList = this.concessionsService.findAll(page - 1,size,sort,order,name,categoryId);
            totalElements = this.concessionsService.findCountAll(name,categoryId);
        }catch (NullPointerException nullPointerException){
            resultList = this.concessionsService.findAll(page - 1,size,sort,order,name,null);
            totalElements = this.concessionsService.findCountAll(name,null);
        }

        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElements)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("concessions/{id}")
    public ResponseEntity<?> getCategory(@PathVariable(value = "id", required = false) Integer id){
        var result = this.concessionsService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("concessions")
    public ResponseEntity<?> createCategory(@RequestBody Concession concessions) throws SQLException {
        int idReturned = this.concessionsService.insert(concessions);
        concessions.setId(idReturned);
        return ResponseEntity.ok(concessions);
    }

    @PutMapping("concessions/{id}")
    public ResponseEntity<?> updateLocations(@PathVariable(value = "id") Integer id,
                                             @RequestBody Concession concessions){
        concessions.setId(id);
        var resultUpdate = this.concessionsService.update(concessions);
        var response = RAResponseUpdate.builder()
                .id(resultUpdate)
                .previousData(concessions)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping ("concessions/{id}")
    public ResponseEntity<?> deleteLocations(@PathVariable(value = "id", required = false) Integer id){
        this.concessionsService.delete(id);
        return ResponseEntity.ok(id);
    }
}
