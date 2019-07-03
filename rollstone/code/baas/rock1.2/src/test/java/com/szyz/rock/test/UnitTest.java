package com.szyz.rock.test;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.szyz.rock.handle.contract.RS;
import com.szyz.rock.model.Item;
import com.szyz.rock.model.Perm;
import com.szyz.rock.model.User;
import com.szyz.rock.util.ConfigKeys;
import com.szyz.rock.util.DateTimeUtils;
import com.szyz.rock.util.MD5;
import com.szyz.rock.util.Utils;
import jnr.ffi.Struct;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Test;
import org.web3j.abi.datatypes.Bool;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthCoinbase;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.ClientTransactionManager;

import java.math.BigInteger;
import java.util.*;

public class UnitTest {
    private String nodeAddr="0xdf16134350a56a12cdf970465670434029a1e927";

    private String user1Name = "caixiaofan0010";
    private String user1Addr = "0x0ade480103e515805d97c384688bf2d6d58b7234";
    private String user2Name = "caixiaofan0011";
    private String user2Addr = "0x8d0461fb34ac6cd591c01c3c956f59b347a335e6";
    private String user3Name = "caixiaofan0012";
    private String user3Addr = "0x86fdfa65bebbb592fb01ec788e7dd15b1bf45299";
    private String user4Name = "caixiaofan0013";
    private String user4Addr = "0x6c835bf80c935e018d2574d551f098f010234717";

    private String contractAddr = "0xFc6a22b3eb9c5767B764528E59dFd043F80A38D4";
    private String itemId = "5305152ce237421b94b49f22d0b37330";
    //e5115daa0d214cdebeb99f07111353f3
    //81b4f2371b6745539c0b4d6ce8e01082

    @Test
    public void tt(){
        /*Iterator<Object> iterator = System.getProperties().keySet().iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();

            System.out.println(next);
        }*/
        try{
            Thread.sleep(100000);
            Thread.sleep(1000);
        }catch(Exception e){

        }
    }

    @Test
    public void getPerms(){

        Web3j web3j = Web3j.build(new HttpService("http:\\\\192.168.103.196:8515"));
        RS rs = RS.load("0x16b1fEe5672e07e70D2Fe8Ceaf1121426C6efa32",
                web3j, new ClientTransactionManager(web3j, "0x24a1adc2d353bc084786149ad3d6af342d100582",
                        40, 200),
                BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        try{
            BigInteger send = rs.getItemNum().send();
            System.out.println("num " + send.intValue());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Test
    public void getPermByIdx(){
        Web3j web3j = Web3j.build(new HttpService("http:\\\\172.16.201.191:8545"));
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, user2Addr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        try{
            Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>> result = rs.getPermByIdx(BigInteger.valueOf(1)).send();
            System.out.println(new String(result.getValue1().get(0)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getUIPermByIdx(){
       /* Web3j web3j = Web3j.build(new HttpService("http:\\\\172.16.201.191:8545"));
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, user2Addr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        try{
            String  send = rs.getUIPerms(Arrays.asList(
                    Utils.bytes2bytes("7c4aa406846540d3b7c83c7a10996cf6".getBytes(),32)),
                    BigInteger.valueOf(0)).send();
            System.out.println();

        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Test
    public void addSecPerm(){
        Web3j web3j = Web3j.build(new HttpService("http:\\\\172.16.201.191:8545"));
        Admin admin = Admin.build(new HttpService("http:\\\\172.16.201.191:8545"));
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, user2Addr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        Perm perm = new Perm();
        perm.setId(Utils.getUUID());
        perm.setTid("4dcb9df6149246caab15fd5288dd9381");
        perm.setUserId(user3Name);
        perm.setItemId("7c4aa406846540d3b7c83c7a10996cf6");
        perm.setPhash(Utils.getUUID());
        perm.setDevice("");
        perm.setPmark("cxfsy");
        perm.setPrvs(Arrays.asList(BigInteger.valueOf(1),BigInteger.valueOf(1),BigInteger.valueOf(1),BigInteger.valueOf(1)));
        perm.setPtime(Arrays.asList(BigInteger.valueOf(-1),BigInteger.valueOf(-1)));
        perm.setPtimes(Arrays.asList(BigInteger.valueOf(5),BigInteger.valueOf(5)));
        perm.setPslice(Arrays.asList(BigInteger.valueOf(-1),BigInteger.valueOf(-1)));
        perm.setPtimestamp(Arrays.asList(BigInteger.valueOf(-1),BigInteger.valueOf(-1)));
        perm.setPtype(0);
        perm.setStatus(1);

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

        try{


            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user2Addr, user2Name).send();// 暂时userName是密码
            System.out.println(unlockAccount.accountUnlocked() + " " + unlockAccount.getResult());
            TransactionReceipt receipt = rs.addPerm(
                    bts,
                    perm.getPrvs(),
                    its,
                    Utils.bytes2bytes(Utils.getUUID().getBytes(), 32),
                    "details"
            ).send();
            System.out.println(receipt.getStatus());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void addPerm(){

        String fromUserAddr = user1Addr;
        String fromUserName =  user1Name;
        String toUserAddr = user4Addr;
        String toUserName =  user4Name;

        Web3j web3j = Web3j.build(new HttpService("http:\\\\172.16.201.191:8545"));
        Admin admin = Admin.build(new HttpService("http:\\\\172.16.201.191:8545"));
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, fromUserAddr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        Perm perm = new Perm();
        perm.setId(Utils.getUUID());
        System.out.println(perm.getId());
        perm.setTid("");
        perm.setUserId(toUserName);
        perm.setItemId(itemId);
        perm.setPhash(Utils.getUUID());
        perm.setDevice("");
        perm.setPmark("cxfsy");
        perm.setPrvs(Arrays.asList(BigInteger.valueOf(1),BigInteger.valueOf(1),BigInteger.valueOf(1),BigInteger.valueOf(1)));
        perm.setPtime(Arrays.asList(BigInteger.valueOf(300),BigInteger.valueOf(300)));
        perm.setPtimes(Arrays.asList(BigInteger.valueOf(4),BigInteger.valueOf(4)));
        perm.setPslice(Arrays.asList(BigInteger.valueOf(-1),BigInteger.valueOf(-1)));
        perm.setPtimestamp(Arrays.asList(BigInteger.valueOf(System.currentTimeMillis()/1000),BigInteger.valueOf(System.currentTimeMillis()/1000+1000)));
        perm.setPtype(0);
        perm.setStatus(1);

        String detail = perm.buildDetail("add", "172.16.201.191:8545", "");
        System.out.println(detail);



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

        try{


            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(fromUserAddr, fromUserName).send();// 暂时userName是密码
            System.out.println(unlockAccount.accountUnlocked() + " " + unlockAccount.getResult());
            TransactionReceipt receipt = rs.addPerm(
                    bts,
                    perm.getPrvs(),
                    its,
                    Utils.bytes2bytes(Utils.getUUID().getBytes(), 32),
                    perm.buildDetail("add","172.16.201.191:8454","")
            ).send();
            System.out.println(receipt.getStatus());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getItems(){
        Web3j web3j = Web3j.build(new HttpService("http:\\\\172.16.201.191:8545"));

        String userEthAddr = user1Addr;

        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, userEthAddr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        try{
            List<Item> items = new ArrayList<>();
            int num = rs.getItemNum().send().intValue();
            for(int i=0;i<num;i++){
                Tuple2<List<byte[]>, List<BigInteger>> result = rs.getItemByIdx(BigInteger.valueOf(i))
                        .send();
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


            System.out.println(items);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void addItem(){
        Web3j web3j = Web3j.build(new HttpService("http:\\\\172.16.201.191:8545"));
        Admin admin = Admin.build(new HttpService("http:\\\\172.16.201.191:8545"));
        String node = "172.16.201.191:8545";

        String userEthAddr = user1Addr;
        String userName = user1Name;

        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, userEthAddr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        Item item = new Item();
        item.setIid(Utils.getUUID());
        System.out.println(item.getIid());
        item.setTid("");
        item.setUserId(userName);
        item.setShash(Utils.getUUID());
        item.setXhash(Utils.getUUID());
        item.setCipher("sha1");
        item.setIkey("szyz");
        item.setIopen(0);
        item.setLevel(1);
        item.setStatus(1);
        item.setTitle("你好");
        item.setContent("this ...");
        item.setType(1);
        item.setPath("thisispath");

        item.setIhash(item.buildHash());
        try{
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
                PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(userEthAddr, userName).send();// 暂时userName是密码
                System.out.println(unlockAccount.accountUnlocked()  + " "+ unlockAccount.getResult());
                TransactionReceipt receipt = rs.uplItem(bts, its, Utils.bytes2bytes(Utils.getUUID().getBytes(), 32), item.buildDetail("add",node, "")).send();
                System.out.println(receipt.getStatus());

            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void md5Tst(){
        System.out.println(MD5.crypt("GRGDGRGR").length());
    }

    @Test
    public void addUser(){
        Web3j web3j = Web3j.build(new HttpService("http:\\\\172.16.201.191:8545"));
        Admin admin = Admin.build(new HttpService("http:\\\\172.16.201.191:8545"));

        String nodeUserAddr = "0xdf16134350a56a12cdf970465670434029a1e927";

        User user = new User();
        user.setUserName("caixiaofan0013");
        user.setCompanyId(0);
        user.setStatus((byte)2);
        user.setLevel((byte)6);

        //FIXME 先注释
        String ethAddr = createAccount(user.getUserName(),admin);
        // String ethAddr = "dfesfefefefef";
        user.setEthAddr(ethAddr);
        System.out.println( "----------------------");
        System.out.println( ethAddr);
        user.setEmail("3rwfesf@net.com");
        user.setPhone("12333333333");
        user.setSex((byte)1);
        user.setBirthdayStr("2018-09-01");
        user.setIdcard("5933333333333333");
        user.setIdcardPhoto("pathidcard");
        user.setRealName("xiaofan");
        user.setCreateTime(new Date());

        user.setHash(user.buildHash());
        String detail = user.buildDetail("add", "172.16.201.191:8545", "");
        System.out.println(detail);

        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, nodeUserAddr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        try{
            TransactionReceipt receipt = rs.addUser(ethAddr
                    , Arrays.asList(
                            Utils.bytes2bytes(user.getUserName().getBytes(), 32)
                            , Utils.bytes2bytes(user.getCompanyId() == null ? "0".getBytes() : user.getCompanyId().toString().getBytes(), 32)
                            , Utils.bytes2bytes("hash".getBytes(), 32)

                    )
                    , Arrays.asList(
                            BigInteger.valueOf(user.getLevel())
                            , BigInteger.valueOf(1)
                    )
                    , Utils.bytes2bytes(Utils.getUUID().getBytes(), 32)
                    // , user.buildDetail("add","http://dd","")
                    ,"detail"
                    , getBalance(nodeUserAddr,web3j).divide(BigInteger.valueOf(10))
                    //,BigInteger.valueOf(500000000)
            ).send();
            System.out.println(receipt.getStatus());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getBalance(){
        try{
            Web3j web3j = Web3j.build(new HttpService("http:\\\\172.16.201.191:8545"));
            // EthAccounts send = web3j.ethAccounts().send();
            //System.out.println(send.getAccounts().get(0) +" --- " + send.getAccounts().size());
            // String s = web3j.ethCoinbase().send().getAddress();
            EthGetBalance ethGetBalance =web3j.ethGetBalance("0xdf16134350a56a12cdf970465670434029a1e927", DefaultBlockParameter.valueOf("latest")).send();
            System.out.println("==== " + ethGetBalance.getBalance().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void mailText(){

        try{
            String host = "smtp.yeah.net";
            int port = 465;
            String userName = "maybe_code@yeah.net";
            String password = "xiaofan123";
            String to = "839284942@qq.com";

            SimpleEmail mail = new SimpleEmail();
            // 设置邮箱服务器信息
            //  mail.setSmtpPort(port);
            mail.setHostName(host);
            // 设置密码验证器
            mail.setAuthentication(userName, password);
            // 设置邮件发送者
            mail.setFrom(userName);
            // 设置邮件接收者
            mail.addTo(to);
            // 设置邮件编码
            mail.setCharset("UTF-8");
            // 设置邮件主题
            mail.setSubject("Test Email");
            // 设置邮件内容
            mail.setMsg("this is a test Text mail");
            // 设置邮件发送时间
            mail.setSentDate(new Date());
            // 发送邮件
            mail.send();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void mailHtml(){
        try{
            String host = "smtp.yeah.net";
            int port = 465;
            String userName = "maybe_code@yeah.net";
            String password = "xiaofan123";
            String to = "839284942@qq.com";

            HtmlEmail htmlEmail = new HtmlEmail();
            htmlEmail.setHostName(host);
            htmlEmail.setAuthentication(userName,password);
            htmlEmail.setFrom(userName);
            htmlEmail.addTo(to);

            htmlEmail.setCharset("utf-8");
            htmlEmail.setSubject("你好，小坏蛋");

            String html="<html><head><meta charset='UTF-8'><meta http-equiv='Expires'content='0'><meta http-equiv='Pragma'content='no-cache'><meta http-equiv='Cache-control'content='no-cache'><meta http-equiv='Cache'content='no-cache'><title>视频播放器</title></head><body><video id='video1'width='320'height='240'controls poster='timg.jpg'><source src='http://172.16.201.189:8080/source/1536303677.mp4'type='video/mp4'>您的浏览器太老了，小可爱换个新点的浏览器吧。</video></body></html>";
            htmlEmail.setHtmlMsg(html);
            String res = htmlEmail.send();
            System.out.println(res);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getVersion(){
       //
//        Admin admin = Admin.build(new HttpService("http:\\\\192.168.103.211:8515"));
//        createAccount("aaa",admin);

        try{
//            Web3ClientVersion send =
//                    Web3j.build(new HttpService("http:\\\\114.115.253.45:8515")).web3ClientVersion().send();
            EthCoinbase send1 =
                    Admin.build(new HttpService("http:\\\\114.115.253.45:8515")).ethCoinbase().send();
            System.out.println(send1.getAddress());

        }catch (Exception e){

        }

    }

    public String createAccount(String userPassword ,Admin admin){
        try{
            NewAccountIdentifier account = admin.personalNewAccount(userPassword).send();
            if(account != null && Utils.isNotBlank(account.getAccountId()))
                return account.getAccountId() ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public BigInteger getBalance(String accountAddress,Web3j web3j){
        try{
            EthGetBalance ethGetBalance = web3j.ethGetBalance(accountAddress, DefaultBlockParameter.valueOf("latest")).send();
            if(ethGetBalance !=null)
                return ethGetBalance.getBalance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return BigInteger.valueOf(0);
    }

    @Test
    public void ttttt(){
        try {


            MongoClient client = new MongoClient("192.168.103.211");
            MongoDatabase md = client.getDatabase("rsdb");
            // DB db = client.getDB( "rsdb" );
            //DBCollection coll = db.getCollection( "item" );

            //long count = coll.count();
            MongoIterable<String> strings = md.listCollectionNames();
            System.out.println(strings.first());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
