package com.example.strcube.service;

import com.example.strcube.DTO.DataDto;
import com.example.strcube.DTO.DataDto2;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

@Service
public class DataServiceImpl implements DataService{
    String url = "jdbc:mysql://localhost:3306/STOCKMARKETSTRCUBE?sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false&createDatabaseIfNotExist=true";
    String username = "root"; // replace with your username
    String password = "KVrsmck@21"; // replace with your password

    @Override
    public List<List<Object>> sendData(String queryId,String type) {
        List<List<Object>> result=new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            ResultSet rs;
            if(type.equals("logs"))
                rs= stmt.executeQuery("select * from QUERY_LOG_"+queryId);
            else if(type.equals("queries"))
                rs=stmt.executeQuery("select * from SUMMARY");
            else
                rs = stmt.executeQuery("select * from QUERY_RESULT_"+queryId);
            ResultSetMetaData rsMetaData=rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            List<Object> header=new ArrayList<>();
            for(int i = 1; i<=count; i++) {
                header.add(rsMetaData.getColumnName(i));
            }
            result.add(header);
            while(rs.next()){
                List<Object> row=new ArrayList<>();
                for(int i = 1; i<=count; i++) {
                    row.add(rs.getObject(i));
                }
                result.add(row);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public HashMap<String, List<String>> getAll() {
        try {
            List<String> factVariables=new ArrayList<>();
            List<String> aggregateFunctions=new ArrayList<>();
            List<String> dimensions=new ArrayList<>();

            HashMap<String,List<String>> result=new HashMap<>();
            File inputFile = new File("./src/main/resources/DMInstance.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList groupItemList = doc.getElementsByTagName("CuboidProperties");
            Element element=(Element) groupItemList.item(0);
            NodeList tempList=element.getElementsByTagName("AggregateFunctions");
            Element element2=(Element) tempList.item(0);
            NodeList aggregateFunctionList=element2.getElementsByTagName("AggregateFunction");
            NodeList tempList2=element.getElementsByTagName("Dimensions");
            Element element3=(Element) tempList2.item(0);
            NodeList dimensionList=element3.getElementsByTagName("Dimension");
            NodeList tempList3=element.getElementsByTagName("FactVariables");
            Element element4=(Element) tempList3.item(0);
            NodeList factVariableList=element4.getElementsByTagName("FactVariable");
            for(int i=0;i<aggregateFunctionList.getLength();i++)
                aggregateFunctions.add(aggregateFunctionList.item(i).getTextContent());
            for(int i=0;i<dimensionList.getLength();i++)
                dimensions.add(dimensionList.item(i).getTextContent());
            for(int i=0;i<factVariableList.getLength();i++)
                factVariables.add(factVariableList.item(i).getTextContent());

            result.put("aggregateFunctions",aggregateFunctions);
            result.put("factVariables",factVariables);
            result.put("dimensions",dimensions);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

    @Override
    public List<List<Object>> getSomething(DataDto2 dataDto2,String type) throws NoSuchAlgorithmException {
        List<String> dimensions=dataDto2.getDimensions();
        Collections.sort(dimensions);
        String dimCombination="";
        for(int i=0;i<dimensions.size();i++){
            dimCombination+=dimensions.get(i);
            if(i< dimensions.size()-1)
                dimCombination+=",";
        }
        System.out.println(dimCombination);
        String hashed=generateHash(dimCombination,dataDto2.getAggregateFunction(),dataDto2.getFactVariable());
        System.out.println(hashed);
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {

           String sql="select qid from CubeSummary where hash='"+hashed+"'";
           ResultSet rs=stmt.executeQuery(sql);
            System.out.println(sql);
            rs.next();
           String queryId=rs.getString(1);

           return type.equals("logs")?sendData(queryId,"logs"):sendData(queryId,"data");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static String generateHash(String dimCombination, String aggregateFunction , String factV) throws NoSuchAlgorithmException {

//        Set<String> mySet = new HashSet<>();
//        if(dimCombination.length() >0) {
//            mySet.add(dimCombination);
//        }
//        mySet.add(aggregateFunction);
//        mySet.add(factV);

        String concatenated = "";
        if(dimCombination.length() > 0){
            concatenated = String.join(",",aggregateFunction , factV , dimCombination);
        }
        else{
            concatenated = String.join(",",aggregateFunction , factV );
        }
        // Join the strings with a comma delimiter
        System.out.println("concatenated string required for hash "+ concatenated);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(concatenated.getBytes());

        // Convert the hash bytes to a hex string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        String hash = sb.toString();
        //System.out.println(hash);
        return hash;

    }
}
