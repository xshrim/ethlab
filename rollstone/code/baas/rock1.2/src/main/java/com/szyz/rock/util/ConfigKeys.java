package com.szyz.rock.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

@Component
public class ConfigKeys {
    public static  String ethContractAddress;
    public static String ethServersUrl;
    public static BigInteger ethGasPrice;
    public static BigInteger ethGasLimit;
    public static String ethPassword;

    public static String uploadpath;
    public static String basepath;

    public static Web3j[] web3js;
    public static Admin[] admins;
    public static String[] nodes;
    @Value("${file.basepath_up}")
    public void setUploadpath(String path){
        uploadpath = path;
    }
    @Value("${file.basepath}")
    public void setBasepath(String path){
        basepath = path;
    }

    @Value("${eth.servers_url}")
    public void setEthServersUrl(String serversUrl){
        ethServersUrl = serversUrl;
        if(Utils.isNotBlank(ethServersUrl)){
            String[] urls = ethServersUrl.split("\\|");
            nodes = new String[urls.length];
            for(int i = 0;i<urls.length;i++)
                nodes[i] = urls[i].replace("http://","");
            web3js = new Web3j[urls.length];
            admins = new Admin[urls.length];
            for (int i=0;i<urls.length;i++){
                HttpService httpService = new HttpService(urls[i]);
                web3js[i] = Web3j.build(httpService);
                admins[i] = Admin.build(httpService);
            }

        }
    }
    @Value("${eth.contract_address}")
    public void setEthContractAddress(String addr){
        ethContractAddress = addr;
    }
    @Value("${eth.gas_price}")
    public void setEthGasPrice(BigInteger gasPrice){
        ethGasPrice = gasPrice;
    }
    @Value("${eth.gas_limit}")
    public void setEthGasLimit(BigInteger gasLimit){
        ethGasLimit = gasLimit;
    }
    @Value("${eth.password}")
    public void setEthPassword(String password){
        ethPassword = password;
    }
}
