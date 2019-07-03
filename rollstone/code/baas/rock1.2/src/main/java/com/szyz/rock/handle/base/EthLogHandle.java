package com.szyz.rock.handle.base;

import com.szyz.rock.handle.contract.RS;
import com.szyz.rock.model.Log;
import com.szyz.rock.model.User;
import com.szyz.rock.util.ConfigKeys;
import com.szyz.rock.util.Utils;
import org.apache.shiro.SecurityUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;

import java.util.Arrays;
import java.util.List;

public class EthLogHandle {

    /**
     * 添加log入链 对资源权限会有所更改
     * @param log
     * @return
     */
    public String addLog(Log log){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        Web3j web3j = ConfigKeys.web3js[0];
        RS rs = RS.load(ConfigKeys.ethContractAddress, web3j, new ClientTransactionManager(web3j,user.getEthAddr() , 40, 200), ConfigKeys.ethGasPrice, ConfigKeys.ethGasLimit);

        //@param bts: bytes32类型数据[用户id, 资源id, 操作类型]
        //@param pids: 使用的权限列表
        //@param duration: 日志操作时长(-1=只作记录不修改权限, 0=只减次数不减时长)
        //@param sn: 交易流水号
        //@param details: 交易详细信息
        log.setId(Utils.getUUID());
        List<byte[]> btn = Arrays.asList(
                Utils.bytes2bytes(log.getUserId().getBytes(),32),
                Utils.bytes2bytes(log.getItemId().getBytes(),32),
                Utils.bytes2bytes(log.getAction().getBytes(),32)
        );
        List<byte[]> pids = Arrays.asList(Utils.bytes2bytes(log.getPermId().getBytes(),32));
        //可能size不能为 0
       // pids.add(Utils.bytes2bytes(log.getPermId().getBytes(),32));
        try{
            Admin admin = ConfigKeys.admins[0];
            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user.getEthAddr(), user.getUserName()).send();// 暂时userName是密码
            System.out.println(unlockAccount.accountUnlocked() + " " + unlockAccount.getResult());
            TransactionReceipt receipt = rs.addLog(
                    btn,
                    pids,
                    log.getDuration(),
                    Utils.bytes2bytes(log.getId().getBytes(), 32),
                    log.buildDetails(ConfigKeys.nodes[0], "")).send();

            return receipt.getStatus();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
