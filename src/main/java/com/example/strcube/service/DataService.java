package com.example.strcube.service;

import com.example.strcube.DTO.DataDto;
import com.example.strcube.DTO.DataDto2;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public interface DataService {
    List<List<Object>> sendData(String queryId,String type);

    HashMap<String,List<String>> getAll();

    List<List<Object>> getSomething(DataDto2 dataDto2,String type) throws NoSuchAlgorithmException;
}
