package com.szyz.rock.controller.perm;

import com.szyz.rock.handle.base.EthLogHandle;
import com.szyz.rock.model.Log;
import com.szyz.rock.model.Perm;
import com.szyz.rock.model.User;
import com.szyz.rock.model.entity.LogInfo;
import com.szyz.rock.model.entity.PermInfo;
import com.szyz.rock.service.LogService;
import com.szyz.rock.service.PermService;
import com.szyz.rock.util.DateTimeUtils;
import com.szyz.rock.util.Utils;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("**.action")
public class LogPermController {

    private static org.apache.commons.logging.Log LOG = LogFactory.getLog(LogPermController.class);

    @Autowired
    private PermService permService;
    @Autowired
    private LogService logService;

    /**
     * 记录日志入链
     * @param log 实体类
     * @return
     *  code：0成功 -1失败
     * 	msg:code为-1时返回
     * 	perm:{
     * 		prvs:权限数组 [是否可查看, 是否可下载源文件, 是否可下载加密文件, 是否可向下授权]  0表示没有该权限 1表示有该权限
     * 		ptime：授权查看时间([总时间, 剩余时间])  若没有设置时间则值都为 -1
     * 		ptimes: 授权查看次数([总次数, 剩余次数])  若没有设置次数则值都为 -1
     * 		ptimestampEnd:授权查看时间戳 过期时间戳 精确到秒 没有设置则值为-1
     * 	}
     */
    @RequestMapping("log/addLog")
    public Map<String,Object> addLog(Log log){

        Map<String,Object> resultMap = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        if(Utils.isBlank(log.getItemId())){
            resultMap.put("code",-1);
            resultMap.put("msg","缺少必要参数");
            return resultMap;
        }

       // Perm perm = getPerm(user ,log);
        if(!"stop".equals(log.getAction())){
            /*Perm perm = checkPerm(user,log);
            if(perm == null){
                resultMap.put("code",-1);
                resultMap.put("msg","没有操作权限");
                return resultMap;
            }

            if(perm.getResult() ==-1){
                resultMap.put("code",-1);
                resultMap.put("msg","权限已过期");
                return resultMap;
            }
            resultMap.put("perm",perm);*/
            PermInfo permInfo = checkPermInfo(log);
            if(permInfo == null){
                resultMap.put("code",-1);
                resultMap.put("msg","没有操作权限");
                return resultMap;
            }
            if(permInfo.getStatus() ==-1){
                resultMap.put("code",-1);
                resultMap.put("msg","时间已用完");
                return resultMap;
            }
            if(permInfo.getStatus() ==-2){
                resultMap.put("code",-1);
                resultMap.put("msg","次数已用完");
                return resultMap;
            }
            if(permInfo.getStatus() ==-3){
                resultMap.put("code",-1);
                resultMap.put("msg","权限已过期");
                return resultMap;
            }
            resultMap.put("perm",permInfo);
        }



        if ("play".equals(log.getAction())){
            log.setDuration(BigInteger.valueOf(0));
        }else if("stop".equals(log.getAction())){
            //log.setDuration();
        }else{
            log.setDuration(BigInteger.valueOf(-1));
        }
        log.setUserId(user.getUserName());
        EthLogHandle ethLogHandle = new EthLogHandle();
        //FIXME 记得打开
        String res = ethLogHandle.addLog(log);
        LOG.info(res);
        //String res = "0x1";
        resultMap.put("code",0);
        if(!"0x1".equals(res)) {
            resultMap.put("code", -1);
            resultMap.put("msg", "操作失败["+log.getAction()+"]");
        }


        return resultMap;
    }

    /**
     * 获取日志列表
     * @param timestamp：时间戳（秒）
     * @return
     *  code:0成功
     * 	logInfo:{
     * 		sn:流水号
     * 		duration：持续时间
     * 		itemId：资源id
     * 		operate:操作
     * 		sender:操作者
     * 		timestamp：时间戳（秒）
     * 		userId：被操作者
     * 		details:详情
     * 	}
     */
    @RequestMapping("log/getLogs")
    public Map<String,Object> getLogs(Long timestamp){

        User user = (User)SecurityUtils.getSubject().getPrincipal();

        Map<String,Object> resultMap = new HashMap<>();

        LogInfo logInfo = new LogInfo();
        logInfo.setSender(user.getUserName());
        Long[] dates = DateTimeUtils.getOneDayStartAndEndTime(timestamp * 1000);
        logInfo.setTimeStart(dates[0]);
        logInfo.setTimeEnd(dates[1]);

        List<LogInfo> logInfos = logService.queryLogs(logInfo);
        resultMap.put("code",0);
        resultMap.put("list",logInfos);
        return resultMap;
    }


    public Map<String,Object> getItempPathByPerm(String permId){
        Map<String,Object> resultMap = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
//        Log log = new Log();
//        log.setPermId(permId);
//        List<PermInfo> permInfos = permService.queryPermsByUserIdAndItemId(user.getUserName(), log);
        PermInfo permInfo = permService.queryPermById(permId);
        if(permInfo ==null){
            resultMap.put("code",-1);
            resultMap.put("msg","没有权限");
            return resultMap;
        }



        return resultMap;
    }

    private Perm getPerm(User user ,Log log){
        List<PermInfo> permInfos = permService.queryPermsByUserIdAndItemId(user.getUserName(), log);
        if(permInfos.size()==0){
            return null;
        }

        Perm perm = new Perm();
        perm.setPrvs(Arrays.asList(BigInteger.valueOf(0),BigInteger.valueOf(0),
                BigInteger.valueOf(0),BigInteger.valueOf(0)));
        perm.setPtimes(Arrays.asList(BigInteger.ZERO,BigInteger.ZERO));
        perm.setPtime(Arrays.asList(BigInteger.ZERO,BigInteger.ZERO));
        perm.setPtimestamp(Arrays.asList(BigInteger.ZERO,BigInteger.ZERO));


        for(PermInfo p:permInfos){
            if (p.getStatus()!=1)
                continue;
            //获取总权限
            for(int i = 0 ;i<p.getPrvs().size();i++){
                if(BigInteger.ONE.compareTo(p.getPrvs().get(i))==0){
                    perm.getPrvs().set(i,BigInteger.ONE);
                }
            }
            //计算总次数
            for(int i =0;i<p.getPtimes().size();i++){
                // -1不参与计算
                if(p.getPtimes().get(i).compareTo(BigInteger.valueOf(-1))>0)
                    perm.getPtimes().set(i,perm.getPtimes().get(i).add(p.getPtimes().get(i)));
            }
            // 获取总时长
            for (int i=0;i<p.getPtime().size();i++){
                if(p.getPtime().get(i).compareTo(BigInteger.valueOf(-1))>0)
                    perm.getPtime().set(i,perm.getPtime().get(i).add(p.getPtime().get(i)));
            }
            //获取最大过期时间
            for(int i = 0 ;i< p.getPtimestamp().size();i++){
                if(p.getPtimestamp().get(i).compareTo(BigInteger.valueOf(-1))>0)
                    if(perm.getPtimestamp().get(i).compareTo(p.getPtimestamp().get(i))<0)
                        perm.getPtimestamp().set(i,p.getPtimestamp().get(i));

            }
        }
        return perm;
    }

/*
    private Perm checkPerm(User user ,Log log){
        Perm perm = getPerm(user, log);
        if(perm==null)
            return null; // 没有权限
        perm.setResult(0);
        if(perm.getPtimes().get(1).compareTo(BigInteger.ONE) <0 &&
                perm.getPtime().get(1).compareTo(BigInteger.ONE) <0 &&
                perm.getPtimestamp().get(1).compareTo(BigInteger.valueOf(System.currentTimeMillis()/1000))<0)
          perm.setResult(-1);




        return perm;
    }
*/
    private PermInfo checkPermInfo(Log log){
        PermInfo permInfo = permService.queryPermById(log.getPermId());
        if(permInfo == null)
            return null;

        permInfo.setStatus(0);

        if(permInfo.getPtime().get(1).compareTo(BigInteger.ZERO) ==0)
            permInfo.setStatus(-1);
        else if(permInfo.getPtimes().get(1).compareTo(BigInteger.ZERO) == 0)
            permInfo.setStatus(-2);
        else if(permInfo.getPtimestamp().get(1).compareTo(BigInteger.valueOf(System.currentTimeMillis()/1000)) <0
                && permInfo.getPtimestamp().get(1).compareTo(BigInteger.valueOf(-1))!=0)
            permInfo.setStatus(-3);


        return permInfo;

    }
}
