package com.apps.controllers;

import com.apps.domain.entity.Service;
import com.apps.response.RAResponseUpdate;
import com.apps.response.ResponseRA;
import com.apps.service.ServiceRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceRoom serviceRoom;


    @GetMapping("services")
    public ResponseEntity<?> getLocations(@RequestParam(value = "pageSize", required = false) Integer size,
                                          @RequestParam(value = "page", required = false)Integer page,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "order", required = false) String order,
                                          @RequestParam(value = "search", required = false) String search){
        var resultList = this.serviceRoom.findAll(size,(page - 1) * size,sort,order,search);
        var totalElement = resultList.size();
        var response = ResponseRA.builder()
                .content(resultList)
                .totalElements(totalElement)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("services/{id}")
    public ResponseEntity<?> getLocations(@PathVariable(value = "id", required = false) Integer id){
        return ResponseEntity.ok(this.serviceRoom.findById(id));
    }

    @PostMapping(value = "/services")
    public ResponseEntity<?> createRoom(@RequestBody Service service) throws SQLException {
        int id = this.serviceRoom.insert(service);
        service.setId(id);
        return ResponseEntity.ok(service);
    }

    @PutMapping(value = "/services/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable("id") Integer id,@RequestBody Service service){
        service.setId(id);
        int result = this.serviceRoom.update(service);
        var response = RAResponseUpdate.builder()
                .id(result)
                .previousData(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/services/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable("id") Integer id){
        this.serviceRoom.delete(id);
        return ResponseEntity.ok(id);
    }
}
