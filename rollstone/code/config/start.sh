# MongoDB
# node1
mongod -f /data/mondata/node1/conf/mongod.conf
# node2
mongod -f /data/mondata/node2/conf/mongod.conf
# arbiter
mongod -f /data/mondata/arbiter/conf/mongod.conf

# Ethereum
# bootnode
nohup bootnode --nodekey=/data/ethdata/boot.key &> /data/ethdata/boot.log &

# node1
nohup geth --identity=node1 --networkid=1111 --maxpeers=50 --port=30313 --syncmode=full --gcmode=archive --gasprice=1 --targetgaslimit=471238800 --rpc --rpcapi=db,eth,net,web3,personal,miner,admin,debug,txpool --rpcaddr=0.0.0.0 --rpccorsdomain=* --rpcport=8515 --ws --wsapi=db,eth,net,web3,personal,miner,admin,debug,txpool --wsaddr=0.0.0.0 --wsorigins=* --wsport=8516 --mine --minerthreads=1 --etherbase=0x395a3762f621dff79dc1d48cdb910a7e65773303 --unlock=0x395a3762f621dff79dc1d48cdb910a7e65773303 --password=/data/ethdata/node1/node.pass --extradata="node1/172.17.0.2:8516" --datadir=/data/ethdata/node1 --bootnodes=enode://a209373a63fae209b7c17ad51d485555e5f0f42828c21a39d2e437f090d5d758e9202db04458e1adbd43b4df582207320ea60121b3e7060855258d198cae52b6@127.0.0.1:30301 &> /data/ethdata/node1/node.log &
# node2
nohup geth --identity=node2 --networkid=1111 --maxpeers=50 --port=30323 --syncmode=full --gcmode=archive --gasprice=1 --targetgaslimit=471238800 --rpc --rpcapi=db,eth,net,web3,personal,miner,admin,debug,txpool --rpcaddr=0.0.0.0 --rpccorsdomain=* --rpcport=8525 --ws --wsapi=db,eth,net,web3,personal,miner,admin,debug,txpool --wsaddr=0.0.0.0 --wsorigins=* --wsport=8526 --mine --minerthreads=1 --etherbase=0xea4052a1dda3cc51471f6216ff0d6bddf022e397 --unlock=0xea4052a1dda3cc51471f6216ff0d6bddf022e397 --password=/data/ethdata/node2/node.pass --extradata="node2/172.17.0.2:8526" --datadir=/data/ethdata/node2 --bootnodes=enode://a209373a63fae209b7c17ad51d485555e5f0f42828c21a39d2e437f090d5d758e9202db04458e1adbd43b4df582207320ea60121b3e7060855258d198cae52b6@127.0.0.1:30301 &> /data/ethdata/node2/node.log &
# node3
nohup geth --identity=node3 --networkid=1111 --maxpeers=50 --port=30333 --syncmode=full --gcmode=archive --gasprice=1 --targetgaslimit=471238800 --rpc --rpcapi=db,eth,net,web3,personal,miner,admin,debug,txpool --rpcaddr=0.0.0.0 --rpccorsdomain=* --rpcport=8535 --ws --wsapi=db,eth,net,web3,personal,miner,admin,debug,txpool --wsaddr=0.0.0.0 --wsorigins=* --wsport=8536 --mine --minerthreads=1 --etherbase=0x54409929cc86052babd761b62dea90aa63d2b34f --unlock=0x54409929cc86052babd761b62dea90aa63d2b34f --password=/data/ethdata/node3/node.pass --extradata="node3/172.17.0.2:8536" --datadir=/data/ethdata/node3 --bootnodes=enode://a209373a63fae209b7c17ad51d485555e5f0f42828c21a39d2e437f090d5d758e9202db04458e1adbd43b4df582207320ea60121b3e7060855258d198cae52b6@127.0.0.1:30301 &> /data/ethdata/node3/node.log &

# ChainEventDriver
nohup python3 -u /data/eventdriver/ev.py &> /data/eventdriver/ev.log &

# ChainExplorer
# server
forever start /data/explorer/server.js
# client
## nginx

