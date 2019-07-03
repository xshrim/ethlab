package com.szyz.rock.test;

import com.szyz.rock.handle.contract.RS;
import com.szyz.rock.model.User;
import com.szyz.rock.util.Utils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;

import java.math.BigInteger;
import java.util.Arrays;

public class AddUserMain {

    public static void main(String[] args){
       // String userName = args[0];
        Web3j web3j = Web3j.build(new HttpService("http:\\\\172.16.201.191:8545"));
        Admin admin = Admin.build(new HttpService("http:\\\\172.16.201.191:8545"));
        User user = new User();
        user.setUserName("dsefesedsfedfff");
//        user.setUserName(userName);
        user.setCompanyId(0);
        user.setStatus((byte)2);
        user.setLevel((byte)6);

        String ethAddr = createAccount(user.getUserName(),admin);
        user.setEthAddr(ethAddr);

        RS rs = RS.load("0xd384B9f46a8072B5dFfB03683A98a40c03553917", web3j, new ClientTransactionManager(web3j, "0xdf16134350a56a12cdf970465670434029a1e927", 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        try{/*
            TransactionReceipt receipt = rs.addUser(ethAddr
                    , Arrays.asList(
                            Utils.bytes2bytes(user.getUserName().getBytes(), 32)
                            , Utils.bytes2bytes(user.getCompanyId() == null ? "0".getBytes() : user.getCompanyId().toString().getBytes(), 32)
                            , Utils.bytes2bytes(user.buildHash().getBytes(), 32)
                            , Utils.bytes2bytes(Utils.getUUID().getBytes(), 32)
                    )
                    , Arrays.asList(
                            BigInteger.valueOf(user.getLevel())
                            , BigInteger.valueOf(1)
                    )
                     , user.buildDetail("add","172.16.201.191:8545","")

                    , getBalance("0xdf16134350a56a12cdf970465670434029a1e927",web3j).divide(BigInteger.valueOf(10))
                    //,BigInteger.valueOf(500000000)
            ).send();
            System.out.println(receipt.getStatus());
            */
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String createAccount(String userPassword ,Admin admin){
//        Web3j web3j = Web3j.build(new HttpService(ConfigKeys.pro.getProperty("eth.server_url")));
        try{
            NewAccountIdentifier account = admin.personalNewAccount(userPassword).send();
            if(account != null && Utils.isNotBlank(account.getAccountId()))
                return account.getAccountId() ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static BigInteger getBalance(String accountAddress,Web3j web3j){
        try{
            EthGetBalance ethGetBalance = web3j.ethGetBalance(accountAddress, DefaultBlockParameter.valueOf("latest")).send();
            if(ethGetBalance !=null)
                return ethGetBalance.getBalance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return BigInteger.valueOf(0);
    }
}
