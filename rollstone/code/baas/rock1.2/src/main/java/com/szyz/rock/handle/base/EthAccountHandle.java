package com.szyz.rock.handle.base;

import com.szyz.rock.handle.contract.RS;
import com.szyz.rock.model.User;
import com.szyz.rock.util.ConfigKeys;
import com.szyz.rock.util.Utils;
import org.apache.shiro.SecurityUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import sun.security.krb5.Config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class EthAccountHandle {

    /**
     * 添加用户入链
     * @param user
     * @return
     */
    public boolean addUser(User user){
        String ethAddr = createAccount(user.getUserName(),ConfigKeys.admins[0]);
        user.setEthAddr(ethAddr);
        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j, getCoinbase(web3j), 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);
        User curUser = (User)SecurityUtils.getSubject().getPrincipal();
        try{
            TransactionReceipt receipt = rs.addUser(ethAddr
                    , Arrays.asList(
                            Utils.bytes2bytes(user.getUserName().getBytes(), 32)
                            , Utils.bytes2bytes(user.getCompanyId() == null ? "0".getBytes() : user.getCompanyId().toString().getBytes(), 32)
                            , Utils.bytes2bytes(user.buildHash().getBytes(), 32)
                    )
                    , Arrays.asList(
                            BigInteger.valueOf(user.getLevel())
                            , BigInteger.valueOf(1)
                    )
                    , Utils.bytes2bytes(Utils.getUUID().getBytes(), 32)
                    , user.buildDetail("add",ConfigKeys.nodes[0],"")
                    , getBalance(curUser.getEthAddr()).divide(BigInteger.valueOf(10))
            ).send();
            if("0x1".equals(receipt.getStatus()))
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改用户hash
     * @param user
     * @return
     */
    public String updateUserHash(User user){
        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j, getCoinbase(web3j), 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);
        User curUser = (User)SecurityUtils.getSubject().getPrincipal();

        try{
            TransactionReceipt receipt = rs.setUserHashById(
                    Utils.bytes2bytes(user.getUserName().getBytes(), 32),
                    Utils.bytes2bytes(user.buildHash().getBytes(), 32),
                    Utils.bytes2bytes(Utils.getUUID().getBytes(), 32),
                    user.buildDetail("upd", ConfigKeys.nodes[0], "")
            ).send();

            return receipt.getStatus();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "0x0";
    }

    /**
     * 创建账户地址
     * @param userPassword admin
     * @return 地址
     */
    public String createAccount(String userPassword ,Admin admin){
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

    /**
     * 获取节点的挖矿奖励地址
     * @return 返回节点的挖矿奖励地址
     */
    public static String getCoinbase(Web3j web3j){
        try{
            return web3j.ethCoinbase().send().getAddress();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取账户以太币
     * @param accountAddress
     * @return
     */
    public BigInteger getBalance(String accountAddress){
        try{
            EthGetBalance ethGetBalance = ConfigKeys.web3js[0].ethGetBalance(accountAddress, DefaultBlockParameter.valueOf("latest")).send();
            if(ethGetBalance !=null)
                return ethGetBalance.getBalance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return BigInteger.valueOf(0);
    }

    /**
     * 转账
     * @param toAddress
     * @return
     */
    public String transf(String toAddress,Web3j web3j){
      /*  try{
//            Credentials credentials = Credentials.create("xxxxxxxx");
            Credentials credentials = WalletUtils.loadCredentials(ConfigKeys.ethPassword,);

            TransactionReceipt receipt = Transfer.sendFunds(
                    web3j, credentials, toAddress,
                    BigDecimal.valueOf(100), Convert.Unit.ETHER).send();
            System.out.println(receipt.getStatus());
            return receipt.getStatus();
        }catch (Exception e){
            e.printStackTrace();
        }*/
        return null;
    }
}
