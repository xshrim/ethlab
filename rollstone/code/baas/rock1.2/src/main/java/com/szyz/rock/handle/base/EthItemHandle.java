package com.szyz.rock.handle.base;

import com.szyz.rock.handle.contract.RS;
import com.szyz.rock.mapper.ItemMapper;
import com.szyz.rock.model.Item;
import com.szyz.rock.model.User;
import com.szyz.rock.util.ConfigKeys;
import com.szyz.rock.util.Utils;
import org.apache.shiro.SecurityUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.ClientTransactionManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EthItemHandle {


    /**
     * 上传资源
     * @param item
     * @return
     */
    public String uplItem(Item item){

        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if(Utils.isBlank(user.getEthAddr()))
            return "fail";
        //@param bts: bytes32类型字段[资源id, 资源父id, 上传者id, 所有者id, 文件hash,
        // 加密文件hash, 额外信息hash, 加密方式, 加密key, 交易流水号]
        item.setIhash(item.buildHash());
        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j,user.getEthAddr() , 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);
        List<byte[]> bts = Arrays.asList(
                Utils.bytes2bytes(item.getIid().getBytes(), 32),
                Utils.bytes2bytes(item.getTid().getBytes(), 32),
               // Utils.bytes2bytes(item.getUperId().getBytes(), 32),
                Utils.bytes2bytes(item.getUserId().getBytes(), 32),
                Utils.bytes2bytes(item.getXhash().getBytes(), 32),
                Utils.bytes2bytes(item.getShash().getBytes(), 32),
                Utils.bytes2bytes(item.getIhash().getBytes(), 32),
                Utils.bytes2bytes(item.getCipher().getBytes(), 32),
                Utils.bytes2bytes(item.getIkey().getBytes(), 32)

        );
        //@param its: int64类型字段[资源公开度, 资源安全级别, 资源状态]
        List<BigInteger> its = Arrays.asList(
            BigInteger.valueOf(item.getIopen()),
            BigInteger.valueOf(item.getLevel()),
            BigInteger.valueOf(item.getStatus())
        );
        try{
            //解锁
            Admin admin = ConfigKeys.admins[0];
            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user.getEthAddr(), user.getUserName()).send();// 暂时userName是密码
            System.out.println(unlockAccount.accountUnlocked()  + " "+ unlockAccount.getResult());
            TransactionReceipt receipt = rs.uplItem(bts, its, Utils.bytes2bytes(Utils.getUUID().getBytes(), 32), item.buildDetail("add", ConfigKeys.nodes[0], "")).send();
            System.out.println(receipt.getStatus());
            return receipt.getStatus();

        }catch (Exception e){
            e.printStackTrace();
        }
        return "0x0";
    }

    /**
     * 设置资源hash
     * @param item
     * @return
     */
    public String setItemIhashById(Item item){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if(Utils.isBlank(user.getEthAddr()))
            return "fail";
        item.setIhash(item.buildHash());
        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j,user.getEthAddr() , 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);

        try{
            //解锁
            Admin admin = ConfigKeys.admins[0];
            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user.getEthAddr(), user.getUserName()).send();// 暂时userName是密码
            System.out.println(unlockAccount.accountUnlocked()  + " "+ unlockAccount.getResult());
            TransactionReceipt receipt = rs.setItemIhashById(Utils.bytes2bytes(item.getIid().getBytes(), 32)
                    , Utils.bytes2bytes(Utils.getUUID().getBytes(), 32)
                    , Utils.bytes2bytes(item.getIhash().getBytes(), 32)
                    , item.getDetail()).send();
            System.out.println(receipt.getStatus());
            return receipt.getStatus();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "0x0";
    }

    /**
     * 设置资源等级
     * @param item
     * @return
     */
    public String setItemOLevelById(Item item){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if(Utils.isBlank(user.getEthAddr()))
            return "fail";
        item.setIhash(item.buildHash());
        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j,user.getEthAddr() , 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);

        try{
            //解锁
            Admin admin = ConfigKeys.admins[0];
            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user.getEthAddr(), user.getUserName()).send();// 暂时userName是密码
            System.out.println(unlockAccount.accountUnlocked()  + " "+ unlockAccount.getResult());
            TransactionReceipt receipt = rs.setItemOLevelById(
                    Utils.bytes2bytes(item.getIid().getBytes(), 32),
                    BigInteger.valueOf(item.getIopen()),
                    BigInteger.valueOf(item.getLevel()),
                    Utils.bytes2bytes(Utils.getUUID().getBytes(), 32),
                    item.getDetail()
            ).send();
            System.out.println(receipt.getStatus());
            return receipt.getStatus();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "0x0";
    }

    /**
     * 设置资源状态
     * @param item
     * @return
     */
    public String setItemStatusById(Item item){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if(Utils.isBlank(user.getEthAddr()))
            return "fail";
        item.setIhash(item.buildHash());
        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j,user.getEthAddr() , 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);

        try{
            //解锁
            Admin admin = ConfigKeys.admins[0];
            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user.getEthAddr(), user.getUserName()).send();// 暂时userName是密码
            System.out.println(unlockAccount.accountUnlocked()  + " "+ unlockAccount.getResult());
            TransactionReceipt receipt = rs.setItemStatusById(
                    Utils.bytes2bytes(item.getIid().getBytes(), 32),
                    BigInteger.valueOf(item.getStatus()),
                    Utils.bytes2bytes(Utils.getUUID().getBytes(), 32),
                    item.getDetail()
            ).send();
            System.out.println(receipt.getStatus());
            return receipt.getStatus();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "0x0";
    }


    /**
     * 获取资源信息By资源id
     * @param id
     * @return
     */
    public Item  getItemById(String id){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        /*if(Utils.isBlank(user.getEthAddr()))
            return "fail";*/
        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j,user.getEthAddr() , 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);

        try{
            Tuple2<List<byte[]>, List<BigInteger>> result = rs.getItemById(Utils.bytes2bytes(id.getBytes(), 32)).send();

            if(result == null)
                return null;
            //@return 0: [资源id, 资源父id, 上传者id, 所有者id, 文件hash, 加密文件hash,
            // 额外信息hash, 加密方式, 加密key]
            //@return 1: [资源公开度, 资源安全级别, 资源生成时间戳, 资源状态]
            Item item = new Item();
            item.setIid(id);
            item.setTid(new String(result.getValue1().get(1)).trim());
            item.setUperId(new String(result.getValue1().get(2)).trim());
            item.setUserId(new String(result.getValue1().get(3)).trim());
            item.setXhash(new String(result.getValue1().get(4)).trim());
            item.setShash(new String(result.getValue1().get(5)).trim());
            item.setIhash(new String(result.getValue1().get(6)).trim());
            item.setCipher(new String(result.getValue1().get(7)).trim());
            item.setIkey(new String(result.getValue1().get(8)).trim());

            item.setIopen(result.getValue2().get(0).intValue());
            item.setLevel(result.getValue2().get(1).intValue());
            item.setTimestamp(result.getValue2().get(2).longValue());
            item.setStatus(result.getValue2().get(3).intValue());

            return item;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public List<Item> getAllItemByBC(){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j,user.getEthAddr() , 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);

        try{
            List<Item> items = new ArrayList<>();
            int num = rs.getItemNum().send().intValue();
            for(int i=0;i<num;i++){
                Tuple2<List<byte[]>, List<BigInteger>> result = rs.getItemByIdx(BigInteger.valueOf(i)).send();
                Item item = new Item();
                item.setIid(new String(result.getValue1().get(0)));
                item.setTid(new String(result.getValue1().get(1)).trim());
                item.setUperId(new String(result.getValue1().get(2)).trim());
                item.setUserId(new String(result.getValue1().get(3)).trim());
                item.setXhash(new String(result.getValue1().get(4)).trim());
                item.setShash(new String(result.getValue1().get(5)).trim());
                item.setIhash(new String(result.getValue1().get(6)).trim());
                item.setCipher(new String(result.getValue1().get(7)).trim());
                item.setIkey(new String(result.getValue1().get(8)).trim());

                item.setIopen(result.getValue2().get(0).intValue());
                item.setLevel(result.getValue2().get(1).intValue());
                item.setTimestamp(result.getValue2().get(2).longValue());
                item.setStatus(result.getValue2().get(3).intValue());
                items.add(item);
            }



            return items;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
