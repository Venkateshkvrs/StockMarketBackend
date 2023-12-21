package com.example.strcube.controller;

import com.example.strcube.DTO.DataDto;
import com.example.strcube.DTO.DataDto2;
import com.example.strcube.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class DataContoller {
    private final DataService dataService;
    @GetMapping("get-logs")
    public List<List<Object>> sendLogs(@RequestParam("queryId") String queryId){
        return dataService.sendData(queryId,"logs");
    }   @GetMapping("get-data")
    public List<List<Object>> sendData(@RequestParam("queryId") String queryId){
        return dataService.sendData(queryId,"data");
    }  @GetMapping("get-queries")
    public List<List<Object>> sendData(){
        return dataService.sendData("q1","queries");
    }
    @GetMapping("get-all")
    public ResponseEntity<Object> getAll(){
        return new ResponseEntity<>(dataService.getAll(), HttpStatus.OK);
    }
    @PostMapping("get-something")
    public ResponseEntity<Object> getSomething(@RequestBody DataDto2 dataDto2) throws NoSuchAlgorithmException {
        return new ResponseEntity<>(dataService.getSomething(dataDto2,"simple"),HttpStatus.OK);
    }
    @PostMapping("get-something-logs")
    public ResponseEntity<Object> getSomethingLogs(@RequestBody DataDto2 dataDto2) throws NoSuchAlgorithmException {
        return new ResponseEntity<>(dataService.getSomething(dataDto2,"logs"),HttpStatus.OK);
    }
}
