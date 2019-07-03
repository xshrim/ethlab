package com.szyz.rock.handle.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.abi.datatypes.generated.StaticArray2;
import org.web3j.abi.datatypes.generated.StaticArray4;
import org.web3j.abi.datatypes.generated.StaticArray8;
import org.web3j.abi.datatypes.generated.StaticArray9;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class RS extends Contract {
    private static final String BINARY = "";

    protected RS(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RS(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<TXReceiptEventResponse> getTXReceiptEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TXReceipt", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TXReceiptEventResponse> responses = new ArrayList<TXReceiptEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TXReceiptEventResponse typedResponse = new TXReceiptEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sn = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.userid = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.itemid = (byte[]) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.operate = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.senderid = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.duration = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.permids = (String) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.details = (String) eventValues.getNonIndexedValues().get(6).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TXReceiptEventResponse> tXReceiptEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TXReceipt", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TXReceiptEventResponse>() {
            @Override
            public TXReceiptEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TXReceiptEventResponse typedResponse = new TXReceiptEventResponse();
                typedResponse.log = log;
                typedResponse.sn = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.userid = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.itemid = (byte[]) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.operate = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.senderid = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.duration = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.permids = (String) eventValues.getNonIndexedValues().get(5).getValue();
                typedResponse.details = (String) eventValues.getNonIndexedValues().get(6).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> bytes32ArrayToString(List<byte[]> data, BigInteger idx) {
        final Function function = new Function("bytes32ArrayToString", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Bytes32>(
                        org.web3j.abi.Utils.typeMap(data, Bytes32.class)),
                new Int64(idx)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setItemIhashByIdx(BigInteger idx, byte[] newihash, byte[] sn, String details) {
        final Function function = new Function(
                "setItemIhashByIdx", 
                Arrays.<Type>asList(new Int64(idx),
                new Bytes32(newihash),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addUser(String addr, List<byte[]> bts, List<BigInteger> its, byte[] sn, String details, BigInteger weiValue) {
        final Function function = new Function(
                "addUser", 
                Arrays.<Type>asList(new Address(addr),
                new org.web3j.abi.datatypes.generated.StaticArray3<Bytes32>(
                        org.web3j.abi.Utils.typeMap(bts, Bytes32.class)),
                new StaticArray2<Int64>(
                        org.web3j.abi.Utils.typeMap(its, Int64.class)),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> setPermStatusByIdx(BigInteger idx, BigInteger newstatus, byte[] sn, String details) {
        final Function function = new Function(
                "setPermStatusByIdx", 
                Arrays.<Type>asList(new Int64(idx),
                new Int64(newstatus),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getUIPerms(List<byte[]> ids) {
        final Function function = new Function("getUIPerms", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Bytes32>(
                        org.web3j.abi.Utils.typeMap(ids, Bytes32.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setUserEidByIdx(BigInteger idx, byte[] neweid, byte[] sn, String details) {
        final Function function = new Function(
                "setUserEidByIdx", 
                Arrays.<Type>asList(new Int64(idx),
                new Bytes32(neweid),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setItemIhashById(byte[] iid, byte[] newihash, byte[] sn, String details) {
        final Function function = new Function(
                "setItemIhashById", 
                Arrays.<Type>asList(new Bytes32(iid),
                new Bytes32(newihash),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> kill() {
        final Function function = new Function(
                "kill", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getUIPermNum(List<byte[]> ids) {
        final Function function = new Function("getUIPermNum", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Bytes32>(
                        org.web3j.abi.Utils.typeMap(ids, Bytes32.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> addLog(List<byte[]> bts, List<byte[]> pids, BigInteger duration, byte[] sn, String details) {
        final Function function = new Function(
                "addLog", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.StaticArray3<Bytes32>(
                        org.web3j.abi.Utils.typeMap(bts, Bytes32.class)),
                new org.web3j.abi.datatypes.DynamicArray<Bytes32>(
                        org.web3j.abi.Utils.typeMap(pids, Bytes32.class)),
                new Int64(duration),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setUserHashByIdx(BigInteger idx, byte[] newhash, byte[] sn, String details) {
        final Function function = new Function(
                "setUserHashByIdx", 
                Arrays.<Type>asList(new Int64(idx),
                new Bytes32(newhash),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setPermStatusById(byte[] pid, BigInteger newstatus, byte[] sn, String details) {
        final Function function = new Function(
                "setPermStatusById", 
                Arrays.<Type>asList(new Bytes32(pid),
                new Int64(newstatus),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> lcheck(byte[] uid, byte[] iid) {
        final Function function = new Function("lcheck", 
                Arrays.<Type>asList(new Bytes32(uid),
                new Bytes32(iid)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> uplItem(List<byte[]> bts, List<BigInteger> its, byte[] sn, String details) {
        final Function function = new Function(
                "uplItem", 
                Arrays.<Type>asList(new StaticArray8<Bytes32>(
                        org.web3j.abi.Utils.typeMap(bts, Bytes32.class)),
                new org.web3j.abi.datatypes.generated.StaticArray3<Int64>(
                        org.web3j.abi.Utils.typeMap(its, Int64.class)),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setUserEidById(byte[] uid, byte[] neweid, byte[] sn, String details) {
        final Function function = new Function(
                "setUserEidById", 
                Arrays.<Type>asList(new Bytes32(uid),
                new Bytes32(neweid),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transItem(byte[] uid, byte[] iid, byte[] sn, String details) {
        final Function function = new Function(
                "transItem", 
                Arrays.<Type>asList(new Bytes32(uid),
                new Bytes32(iid),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> bytes32ToString(byte[] data) {
        final Function function = new Function("bytes32ToString", 
                Arrays.<Type>asList(new Bytes32(data)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setItemStatusById(byte[] iid, BigInteger newstatus, byte[] sn, String details) {
        final Function function = new Function(
                "setItemStatusById", 
                Arrays.<Type>asList(new Bytes32(iid),
                new Int64(newstatus),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setItemOLevelByIdx(BigInteger idx, BigInteger newiopen, BigInteger newlevel, byte[] sn, String details) {
        final Function function = new Function(
                "setItemOLevelByIdx", 
                Arrays.<Type>asList(new Int64(idx),
                new Int64(newiopen),
                new Int64(newlevel),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>>> getPermById(byte[] pid) {
        final Function function = new Function("getPermById", 
                Arrays.<Type>asList(new Bytes32(pid)),
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray8<Bytes32>>() {}, new TypeReference<StaticArray4<Int64>>() {}, new TypeReference<StaticArray2<Int64>>() {}, new TypeReference<StaticArray2<Int64>>() {}));
        return new RemoteCall<Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>>>(
                new Callable<Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>>>() {
                    @Override
                    public Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>>(
                                convertToNative((List<Bytes32>) results.get(0).getValue()), 
                                convertToNative((List<Int64>) results.get(1).getValue()), 
                                convertToNative((List<Int64>) results.get(2).getValue()), 
                                convertToNative((List<Int64>) results.get(3).getValue()));
                    }
                });
    }

    public RemoteCall<TransactionReceipt> setUserStatusById(byte[] uid, BigInteger newstatus, byte[] sn, String details) {
        final Function function = new Function(
                "setUserStatusById", 
                Arrays.<Type>asList(new Bytes32(uid),
                new Int64(newstatus),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setUserHashById(byte[] uid, byte[] newhash, byte[] sn, String details) {
        final Function function = new Function(
                "setUserHashById", 
                Arrays.<Type>asList(new Bytes32(uid),
                new Bytes32(newhash),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>>> getPermByIdx(BigInteger idx) {
        final Function function = new Function("getPermByIdx", 
                Arrays.<Type>asList(new Int64(idx)),
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray8<Bytes32>>() {}, new TypeReference<StaticArray4<Int64>>() {}, new TypeReference<StaticArray2<Int64>>() {}, new TypeReference<StaticArray2<Int64>>() {}));
        return new RemoteCall<Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>>>(
                new Callable<Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>>>() {
                    @Override
                    public Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>>(
                                convertToNative((List<Bytes32>) results.get(0).getValue()), 
                                convertToNative((List<Int64>) results.get(1).getValue()), 
                                convertToNative((List<Int64>) results.get(2).getValue()), 
                                convertToNative((List<Int64>) results.get(3).getValue()));
                    }
                });
    }

    public RemoteCall<Tuple2<List<byte[]>, List<BigInteger>>> getItemById(byte[] iid) {
        final Function function = new Function("getItemById", 
                Arrays.<Type>asList(new Bytes32(iid)),
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray9<Bytes32>>() {}, new TypeReference<StaticArray4<Int64>>() {}));
        return new RemoteCall<Tuple2<List<byte[]>, List<BigInteger>>>(
                new Callable<Tuple2<List<byte[]>, List<BigInteger>>>() {
                    @Override
                    public Tuple2<List<byte[]>, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<List<byte[]>, List<BigInteger>>(
                                convertToNative((List<Bytes32>) results.get(0).getValue()), 
                                convertToNative((List<Int64>) results.get(1).getValue()));
                    }
                });
    }

    public RemoteCall<TransactionReceipt> setUserLevelByIdx(BigInteger idx, BigInteger newlevel, byte[] sn, String details) {
        final Function function = new Function(
                "setUserLevelByIdx", 
                Arrays.<Type>asList(new Int64(idx),
                new Int64(newlevel),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setItemStatusByIdx(BigInteger idx, BigInteger newstatus, byte[] sn, String details) {
        final Function function = new Function(
                "setItemStatusByIdx", 
                Arrays.<Type>asList(new Int64(idx),
                new Int64(newstatus),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getUserNum() {
        final Function function = new Function("getUserNum", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>> getUserByIdx(BigInteger idx) {
        final Function function = new Function("getUserByIdx", 
                Arrays.<Type>asList(new Int64(idx)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}));
        return new RemoteCall<Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>>(
                new Callable<Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> setItemOLevelById(byte[] iid, BigInteger newiopen, BigInteger newlevel, byte[] sn, String details) {
        final Function function = new Function(
                "setItemOLevelById", 
                Arrays.<Type>asList(new Bytes32(iid),
                new Int64(newiopen),
                new Int64(newlevel),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addItem(List<byte[]> bts, List<BigInteger> its, byte[] sn, String details) {
        final Function function = new Function(
                "addItem", 
                Arrays.<Type>asList(new StaticArray9<Bytes32>(
                        org.web3j.abi.Utils.typeMap(bts, Bytes32.class)),
                new org.web3j.abi.datatypes.generated.StaticArray3<Int64>(
                        org.web3j.abi.Utils.typeMap(its, Int64.class)),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addPerm(List<byte[]> bts, List<BigInteger> prvs, List<BigInteger> its, byte[] sn, String details) {
        final Function function = new Function(
                "addPerm", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.StaticArray7<Bytes32>(
                        org.web3j.abi.Utils.typeMap(bts, Bytes32.class)),
                new StaticArray4<Int64>(
                        org.web3j.abi.Utils.typeMap(prvs, Int64.class)),
                new org.web3j.abi.datatypes.generated.StaticArray7<Int64>(
                        org.web3j.abi.Utils.typeMap(its, Int64.class)),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setUserStatusByIdx(BigInteger idx, BigInteger newstatus, byte[] sn, String details) {
        final Function function = new Function(
                "setUserStatusByIdx", 
                Arrays.<Type>asList(new Int64(idx),
                new Int64(newstatus),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getPermNum() {
        final Function function = new Function("getPermNum", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setUserLevelById(byte[] uid, BigInteger newlevel, byte[] sn, String details) {
        final Function function = new Function(
                "setUserLevelById", 
                Arrays.<Type>asList(new Bytes32(uid),
                new Int64(newlevel),
                new Bytes32(sn),
                new Utf8String(details)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getItemNum() {
        final Function function = new Function("getItemNum", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple2<List<byte[]>, List<BigInteger>>> getItemByIdx(BigInteger idx) {
        final Function function = new Function("getItemByIdx", 
                Arrays.<Type>asList(new Int64(idx)),
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray9<Bytes32>>() {}, new TypeReference<StaticArray4<Int64>>() {}));
        return new RemoteCall<Tuple2<List<byte[]>, List<BigInteger>>>(
                new Callable<Tuple2<List<byte[]>, List<BigInteger>>>() {
                    @Override
                    public Tuple2<List<byte[]>, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<List<byte[]>, List<BigInteger>>(
                                convertToNative((List<Bytes32>) results.get(0).getValue()), 
                                convertToNative((List<Int64>) results.get(1).getValue()));
                    }
                });
    }

    public RemoteCall<Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>> getUserByAddr(String addr) {
        final Function function = new Function("getUserByAddr", 
                Arrays.<Type>asList(new Address(addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}));
        return new RemoteCall<Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>>(
                new Callable<Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue());
                    }
                });
    }

    public RemoteCall<Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>> getUserById(byte[] uid) {
        final Function function = new Function("getUserById", 
                Arrays.<Type>asList(new Bytes32(uid)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}));
        return new RemoteCall<Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>>(
                new Callable<Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue());
                    }
                });
    }

    public static RemoteCall<RS> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployRemoteCall(RS.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static RemoteCall<RS> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployRemoteCall(RS.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static RS load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new RS(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static RS load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new RS(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TXReceiptEventResponse {
        public Log log;

        public byte[] sn;

        public byte[] userid;

        public byte[] itemid;

        public byte[] operate;

        public byte[] senderid;

        public String sender;

        public BigInteger duration;

        public BigInteger timestamp;

        public String permids;

        public String details;
    }
}
