package com.szyz.rock.controller.perm;

import com.szyz.rock.model.Log;
import com.szyz.rock.model.Page;
import com.szyz.rock.model.Perm;
import com.szyz.rock.model.User;
import com.szyz.rock.model.entity.PermInfo;
import com.szyz.rock.service.PermService;
import com.szyz.rock.util.DateTimeUtils;
import com.szyz.rock.util.Utils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping("**.action")
public class PermController {
    @Autowired
    private PermService permService;

    /**
     * 批量添加权限
     * @param perm
     * @param psliceStart 授权查看视频时间起始 没有设置则值为-1
     * @param psliceEnd 授权查看视频时间结束 没有设置则值为-1
     * @param ptimestampEnd 授权查看时间戳 过期时间戳 精确到秒 没有设置则值为-1
     * @param users 被勾选的用户数组 ，只传用户名
     * @return code:0成功 -1失败 msg：code为-1时返回
     */
    @RequestMapping("perm/batchAddPerm")
    public Map<String ,Object> batchAddPerm(Perm perm,Long psliceStart,Long psliceEnd ,Long ptimestampEnd,String[] users){
        Map<String,Object> resultMap = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        Perm tempPerm = new Perm();
        tempPerm.setTid(perm.getTid());
        tempPerm.setItemId(perm.getItemId());
        tempPerm.setDevice(perm.getDevice());
        tempPerm.setPmark(perm.getPmark());
        tempPerm.setPrvs(perm.getPrvs());
        tempPerm.setPtime(perm.getPtime());
        tempPerm.setPtimes(perm.getPtimes());
        tempPerm.setPtype(perm.getPtype());
        tempPerm.setSgerId(user.getUserName());

        tempPerm.setPslice(Arrays.asList(BigInteger.valueOf(psliceStart),BigInteger.valueOf(psliceEnd)));
        if(ptimestampEnd !=null && ptimestampEnd != 0)
            tempPerm.setPtimestamp(Arrays.asList(BigInteger.valueOf((new Date().getTime()/1000)) , BigInteger.valueOf(ptimestampEnd)));
        else
            tempPerm.setPtimestamp(Arrays.asList(BigInteger.valueOf(-1),BigInteger.valueOf(-1)));

        List failList = new ArrayList();
        for(String username :users){
            tempPerm.setUserId(username);
            tempPerm.setId(Utils.getUUID());
            int res = permService.savePerm(tempPerm);
            System.out.println("========" +res+"========");
            System.out.println(tempPerm);
            System.out.println("+==============");
            if(res !=1){
                failList.add(username);
            }
        }
        resultMap.put("code",0);
        resultMap.put("failList",failList);
        return resultMap;
    }

    /**
     *
     * @param perm
     * @param psliceStart 授权查看视频时间起始 没有设置则值为-1
     * @param psliceEnd 授权查看视频时间结束 没有设置则值为-1
     * @param ptimestampEnd 授权查看时间戳 过期时间戳 精确到秒 没有设置则值为-1
     * @return code:0成功 -1失败 msg：code为-1时返回
     */
    @RequestMapping("perm/addPerm")
    public Map<String,Object> addPerm(Perm perm,Long psliceStart,Long psliceEnd ,Long ptimestampEnd){
        System.out.println("-------");
        System.out.println(perm);
        System.out.println(psliceStart+" " + psliceEnd +" "+ ptimestampEnd);
        System.out.println("-------");
        Map<String,Object> resultMap = new HashMap<>();
        /*if(perm.getPtype() == 1){ //委托
            if(perm.getPrvs().get(0).compareTo(BigInteger.ZERO)!=0 &&
                    perm.getPrvs().get(1).compareTo(BigInteger.ZERO)!=0 &&
                    perm.getPrvs().get(2).compareTo(BigInteger.ZERO) !=0 &&
                    perm.getPrvs().get(3).compareTo(BigInteger.ONE)!=0
                    ){
                resultMap.put("code",-1);
                resultMap.put("msg","委托授权只能赋予[授权]权限");
                return resultMap;
            }
        }*/

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        perm.setId(Utils.getUUID());
        perm.setSgerId(user.getUserName());
        //perm.setTid("");
        perm.setStatus(1);

        perm.setPslice(Arrays.asList(BigInteger.valueOf(psliceStart),BigInteger.valueOf(psliceEnd)));
        if(ptimestampEnd !=null && ptimestampEnd != 0)
            perm.setPtimestamp(Arrays.asList(BigInteger.valueOf((new Date().getTime()/1000)) , BigInteger.valueOf(ptimestampEnd)));
        else
            perm.setPtimestamp(Arrays.asList(BigInteger.valueOf(-1),BigInteger.valueOf(-1)));

        //检查父权限
        if(Utils.isNotBlank(perm.getTid())){
            PermInfo permInfo = permService.queryPermById(perm.getTid());

            //没有查询到父权限信息
            if(permInfo ==null){
                resultMap.put("code",-1);
                resultMap.put("msg","没有父权限相关信息[父权限id："+perm.getTid()+"]");
                return resultMap;
            }
            if(Utils.isNotBlank(permInfo.getDevice())){
                //设备限制需保持一致
                if(!permInfo.getDevice().equalsIgnoreCase(perm.getDevice())){
                    resultMap.put("code",-1);
                    resultMap.put("msg","授权设备限制["+perm.getDevice()+"]和父权限设备限制["+permInfo.getDevice()+"]不相同");
                    return resultMap;
                }
            }
            if(permInfo.getPtimestamp().get(1).compareTo(BigInteger.valueOf(-1))!=0){
                //不能超过父权限过期时间
                if(perm.getPtimestamp().get(1).compareTo(permInfo.getPtimestamp().get(1))>0){
                    resultMap.put("code",-1);
                    resultMap.put("msg","授权过期时间不能超过父权限过期时间");
                    return resultMap;
                }
            }
            if(permInfo.getPtime().get(1).compareTo(BigInteger.valueOf(-1))!=0){
                //不能超过父权限播放时长
                if(perm.getPtime().get(1).compareTo(permInfo.getPtime().get(1))>0){
                    resultMap.put("code",-1);
                    resultMap.put("msg","授权播放时长不能超过父权限播放时长");
                    return resultMap;
                }
            }
            if(permInfo.getPtimes().get(1).compareTo(BigInteger.valueOf(-1))!=0){
                //不能超过父权限播放次数
                if(perm.getPtimes().get(1).compareTo(permInfo.getPtimes().get(1))>0){
                    resultMap.put("code",-1);
                    resultMap.put("msg","授权播放次数不能超过父权限播放次数");
                    return resultMap;
                }
            }

            for(int i=0;i<permInfo.getPrvs().size();i++){
                //不能授权父权限里没有的权限
                if(perm.getPrvs().get(i).compareTo(BigInteger.ONE) ==0
                        && permInfo.getPrvs().get(i).compareTo(BigInteger.ZERO) ==0) {
                    resultMap.put("code",-1);
                    resultMap.put("msg","父权限没有授此权限["+ i +"]");
                    return resultMap;
                }
            }

        }



        int res = permService.savePerm(perm);
        System.out.println("================");
        System.out.println(perm);
        System.out.println("+==============");


        resultMap.put("code",0);
        if (res !=1){
            resultMap.put("code",-1);
            resultMap.put("msg","授权失败");
        }
        return  resultMap;
    }

    @RequestMapping("perm/getPerms")
    public Map<String,Object> getPerms(String itemId){

        User user = (User)SecurityUtils.getSubject().getPrincipal();

        Log log = new Log();
        log.setItemId(itemId);
        List<PermInfo> permInfos = permService.queryPermsByUserIdAndItemId(user.getUserName(), log);
        List<PermInfo> resultPermInfos = new ArrayList<>();
        for(PermInfo p:permInfos){

            if(p.getPrvs().get(3).intValue() == 0)
                continue;

            if(p.getPtimes().get(1).compareTo(BigInteger.ONE) <0
                    && p.getPtime().get(1).compareTo(BigInteger.ONE)<0
                    && p.getPtimestamp().get(1).compareTo(BigInteger.valueOf(System.currentTimeMillis()/1000)) <0
                    )
                continue;
            resultPermInfos.add(p);
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("permList",resultPermInfos);
        //resultMap.put("userList",)  要看前端怎么显示
        return resultMap;
    }

    /**
     * 获取对应权限列表
     * @param page
     * @param itemId 资源id,不为空时只查该资源id下的所有授权记录列表，否则做分页查询当前用户相关的权限记录列表
     * @return
     *  code：0成功
     * 	page:{
     * 		curPage:上一页
     * 		goPage:当前页
     * 		totalSize:总条数
     * 		totalPage:总页数
     * 		pageSize:每页条数
     * 	}
     * 	permList:[
     * 		{
     * 			pid:权限id
     * 			sgerid:授权者
     * 			userId:被授权用户名
     * 			itemId:资源id
     * 			device:设备
     * 			pmark:水印
     * 			prvs:权限数组 [是否可查看, 是否可下载源文件, 是否可下载加密文件, 是否可向下授权]  0表示没有该权限 1表示有该权限
     * 			ptime：授权查看时间([总时间, 剩余时间])  若没有设置时间则值都为 -1
     * 			ptimes: 授权查看次数([总次数, 剩余次数])  若没有设置次数则值都为 -1
     * 			pslice:授权查看视频时间([开始时间, 结束时间]) 没有设置则值为-1
     * 			ptimestamp:授权时间([授权时间戳, 过期时间戳]) 精确到秒 没有设置则值为-1
     * 			ptype: 授权类型(0: 独立授权, 1: 委托授权)
     * 			mid：分页
     * 			item:{
     * 				title:资源名
     * 				content:描述
     * 				createTime:创建时间
     * 				type：类别
     * 			}
     * 		}
     * 	]
     */
    @RequestMapping("perm/getPermsList")
    public Map<String,Object> getPermsByItemId(Page page , String itemId){
        Map<String,Object> resultMap = new HashMap<>();
        List<PermInfo> permInfos = null;
        if(Utils.isNotBlank(itemId))
            permInfos = permService.queryPermsByItemId(itemId);
        else{
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            page.setModel(user.getUserName());
            permInfos = permService.queryPermsPageList(page);
        }

        resultMap.put("code",0);
        resultMap.put("permList",permInfos);
        resultMap.put("page",page);
        return resultMap;
    }

    /**
     * 通过权限id获取授权信息接口
     * @param permId 权限id
     * @return
     *  code:0成功
     * 	perm:{
     * 		pid:权限id
     * 		sgerid:授权者
     * 		userId:被授权用户名
     * 		itemId:资源id
     * 		device:设备
     * 		pmark:水印
     * 		prvs:权限数组 [是否可查看, 是否可下载源文件, 是否可下载加密文件, 是否可向下授权]  0表示没有该权限 1表示有该权限
     * 		ptime：授权查看时间([总时间, 剩余时间])  若没有设置时间则值都为 -1
     * 		ptimes: 授权查看次数([总次数, 剩余次数])  若没有设置次数则值都为 -1
     * 		pslice:授权查看视频时间([开始时间, 结束时间]) 没有设置则值为-1
     * 		ptimestamp:授权时间([授权时间戳, 过期时间戳]) 精确到秒 没有设置则值为-1
     * 		ptype: 授权类型(0: 独立授权, 1: 委托授权)
     * 	}
     */
    @RequestMapping("perm/getPermById")
    public Map<String,Object> getPermById(String permId){

        PermInfo permInfo = permService.queryPermById(permId);
        Map<String,Object> resultMap = new HashMap<>();

        resultMap.put("code",0);
        resultMap.put("sysTimestamp", System.currentTimeMillis()/1000);
        resultMap.put("perm",permInfo);
        return resultMap;
    }

    /**
     * 撤销权限接口
     * @param permId 权限id
     * @param type 0单个撤销，1级联撤销
     * @return code：0成功 -1 失败 msg：code为-1时返回
     */
    @RequestMapping("perm/cancelPerm")
    public Map<String,Object> cancelPerm(String permId,Integer type){

        Map<String,Object> resultMap = new HashMap<>();

        PermInfo permInfo = permService.queryPermById(permId);
        if(permInfo !=null){
            resultMap.put("code",0);
            int res = permService.setPermStatusById(permId, 2);// 授权权限状态(0: 不存在, 1: 正常, 2：冻结, 6: 不可用)
            if(res !=0){
                resultMap.put("code",-1);
                resultMap.put("msg","撤销失败");
            }else{
                if(type ==1){
                    List<PermInfo> permInfos = permService.queryPermByTid(permId);
                    handlePerm(permInfos,2);
                }
            }
        }
        return resultMap;
    }


    private void handlePerm(List<PermInfo> permInfos,Integer status){
        for(PermInfo p : permInfos){
            int res = permService.setPermStatusById(p.getPid(), status);// 授权权限状态(0: 不存在, 1: 正常, 2：冻结, 6: 不可用)
            List<PermInfo> ps = permService.queryPermByTid(p.getPid()); //查询pid下的所以子权限
            if(ps.size() ==0) //递归条件
                continue;
            handlePerm(ps,status); //递归处理
        }
    }

}
