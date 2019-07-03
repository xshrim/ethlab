package com.szyz.rock.handle.base;

import com.szyz.rock.handle.contract.RS;
import com.szyz.rock.model.Perm;
import com.szyz.rock.model.User;
import com.szyz.rock.util.ConfigKeys;
import com.szyz.rock.util.Utils;
import org.apache.shiro.SecurityUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.ClientTransactionManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class EthPermHandle {

    /**
     * 添加权限（包括二次、多次授权）
     * @param perm
     * @return
     */
    public String addPerm(Perm perm) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (Utils.isBlank(user.getEthAddr()))
            return "fail";
        //@param bts: bytes32类型字段[资源id, 资源父id, 上传者id, 所有者id, 文件hash,
        // 加密文件hash, 额外信息hash, 加密方式, 加密key, 交易流水号]

        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j, user.getEthAddr(), 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);
        // ids: [权限id, 父权限id, 授予用户id, 资源id]
        List<byte[]> bts = Arrays.asList(
                Utils.bytes2bytes(perm.getId().getBytes(), 32),
                Utils.bytes2bytes(perm.getTid().getBytes(), 32),
                Utils.bytes2bytes(perm.getUserId().getBytes(), 32),
                Utils.bytes2bytes(perm.getItemId().getBytes(), 32),
                Utils.bytes2bytes(perm.getPhash().getBytes(), 32),
                Utils.bytes2bytes(perm.getDevice().getBytes(), 32),
                Utils.bytes2bytes(perm.getPmark().getBytes(), 32)
        );

//int64类型字段[授权查看时间, 授权查看次数, 授权查看起始时间段, 授权查看终止时间段,
// 授权过期时间戳, 授权类型, 授权状态]
        List<BigInteger> its = Arrays.asList(
                perm.getPtime().get(1),
                perm.getPtimes().get(1),
                perm.getPslice().get(0),
                perm.getPslice().get(1),
                perm.getPtimestamp().get(1),
                BigInteger.valueOf(perm.getPtype()),
                BigInteger.valueOf(perm.getStatus())
        );
        try {
            Admin admin = ConfigKeys.admins[0];
            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user.getEthAddr(), user.getUserName()).send();// 暂时userName是密码
            System.out.println(unlockAccount.accountUnlocked() + " " + unlockAccount.getResult());
            TransactionReceipt receipt = rs.addPerm(
                    bts,
                    perm.getPrvs(),
                    its,
                    Utils.bytes2bytes(Utils.getUUID().getBytes(), 32),
                    perm.buildDetail("add",ConfigKeys.nodes[0],"")
            ).send();
            System.out.println(receipt.getStatus());
            return receipt.getStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取权限
     * @param id
     * @return
     */
    public Perm getPermById(String id) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j, user.getEthAddr(), 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);

        try {
            //@return 0: bytes32类型字段[权限id, 父权限id, 授予者id, 授予用户id, 资源id, 权限额外信息hash, 授权设备, 水印内容]
            //@return 1: 授权权限[查看权限, 源文件下载权限, 加密文件下载权限, 授予权限]
            //@return 2: int64[2]类型字段[授权查看时间, 授权查看次数, 授权查看时间段, 授权时间戳]
            //@return 3: int64类型字段[授权类型, 授权状态]
            Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>> result = rs.getPermById(Utils.bytes2bytes(id.getBytes(), 32)).send();
            Perm perm = new Perm();
            perm.setId(new String(result.getValue1().get(0)).trim());
            perm.setTid(new String(result.getValue1().get(1)).trim());
            perm.setSgerId(new String(result.getValue1().get(2)).trim());
            perm.setUserId(new String(result.getValue1().get(3)).trim());
            perm.setItemId(new String(result.getValue1().get(4)).trim());
            perm.setPhash(new String(result.getValue1().get(5)).trim());
            perm.setDevice(new String(result.getValue1().get(6)).trim());
            perm.setPmark(new String(result.getValue1().get(7)).trim());

            perm.setPrvs(result.getValue2());
            perm.setPtime(Arrays.asList(result.getValue3().get(0),result.getValue3().get(1)));
            perm.setPtimes(Arrays.asList(result.getValue3().get(2),result.getValue3().get(3)));
            perm.setPslice(Arrays.asList(result.getValue3().get(4),result.getValue3().get(5)));
            perm.setPtimestamp(Arrays.asList(result.getValue3().get(6),result.getValue3().get(7)));
            perm.setPtype(result.getValue4().get(0).intValue());
            perm.setStatus(result.getValue4().get(1).intValue());
            return perm;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 设置权限状态
     * @param permId
     * @param status
     * @return
     */
    public String setPermStatusById(String permId,Integer status){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (Utils.isBlank(user.getEthAddr())){
            return "fail";
        }

        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j, user.getEthAddr(), 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);

        try{
            Admin admin = ConfigKeys.admins[0];
            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user.getEthAddr(), user.getUserName()).send();// 暂时userName是密码
            System.out.println(unlockAccount.accountUnlocked() + " " + unlockAccount.getResult());

            TransactionReceipt receipt = rs.setPermStatusById(
                    Utils.bytes2bytes(permId.getBytes(), 32),
                    BigInteger.valueOf(status),
                    Utils.bytes2bytes(Utils.getUUID().getBytes(), 32),
                    ""
            ).send();
            System.out.println(receipt.getStatus());

            return receipt.getStatus();

        }catch (Exception e){
            e.printStackTrace();
        }


        return "fail";
    }
}
