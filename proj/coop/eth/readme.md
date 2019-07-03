
bootnode --genkey=boot.key                                                          

#nohup bootnode --nodekey=boot.key &> /tmp/priv/node1/boot.log &

bkey=$(bootnode --nodekeyhex $(cat /data/ethdata/boot.key) -writeaddress)
nohup bootnode --nodekey=/data/ethdata/boot.key &> /data/ethdata/boot.log &

nohup geth --identity=node1 --networkid=11 --maxpeers=50 --port=30313 --syncmode=full --gcmode=archive --gasprice=0 --targetgaslimit=471238800 --rpc --rpcapi=db,eth,net,web3,personal,miner,admin,debug,txpool --rpcaddr=0.0.0.0 --rpccorsdomain=* --rpcport=8545 --ws --wsapi=db,eth,net,web3,personal,miner,admin,debug,txpool --wsaddr=0.0.0.0 --wsorigins=* --wsport=8546 --mine --minerthreads=1 --etherbase=0x20a809b2434363f8b3f3eef8794d13ff2aff466b --unlock=0x20a809b2434363f8b3f3eef8794d13ff2aff466b --password=./node1/node.pass --extradata="node1/127.0.0.1:8546" --datadir=./node1 --bootnodes=enode://${bkey}@127.0.0.1:30301 &> /tmp/priv/node1/node1.log &


geth --identity=node2 --networkid=11 --maxpeers=50 --port=30323 --syncmode=full --gcmode=archive --gasprice=0 --targetgaslimit=471238800 --rpc --rpcapi=db,eth,net,web3,personal,miner,admin,debug,txpool --rpcaddr=0.0.0.0 --rpccorsdomain=* --rpcport=8555 --ws --wsapi=db,eth,net,web3,personal,miner,admin,debug,txpool --wsaddr=0.0.0.0 --wsorigins=* --wsport=8556 --mine --minerthreads=1 --etherbase=57a5f5da95a747919f651b7a56b7798dab0b8f4f --unlock=57a5f5da95a747919f651b7a56b7798dab0b8f4f --password=./node2/node.pass --extradata="node2/127.0.0.1:8556" --datadir=./node2 --bootnodes=enode://${bkey}@127.0.0.1:30301

geth --identity=node3 --networkid=11 --maxpeers=50 --port=30333 --syncmode=full --gcmode=archive --gasprice=0 --targetgaslimit=471238800 --rpc --rpcapi=db,eth,net,web3,personal,miner,admin,debug,txpool --rpcaddr=0.0.0.0 --rpccorsdomain=* --rpcport=8565 --ws --wsapi=db,eth,net,web3,personal,miner,admin,debug,txpool --wsaddr=0.0.0.0 --wsorigins=* --wsport=8566 --mine --minerthreads=1 --etherbase=f496a30ed5305cb8d20df744917236ea3a339ffb --unlock=f496a30ed5305cb8d20df744917236ea3a339ffb --password=./node3/node.pass --extradata="node3/127.0.0.1:8566" --datadir=./node3 --bootnodes=enode://${bkey}@127.0.0.1:30301
