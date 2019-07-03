// Code generated - DO NOT EDIT.
// This file is a generated binding and any manual changes will be lost.

package coin

import (
	"math/big"
	"strings"

	ethereum "github.com/ethereum/go-ethereum"
	"github.com/ethereum/go-ethereum/accounts/abi"
	"github.com/ethereum/go-ethereum/accounts/abi/bind"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/core/types"
	"github.com/ethereum/go-ethereum/event"
)

// CoinABI is the input ABI used to generate the binding from.
const CoinABI = "[{\"constant\":true,\"inputs\":[],\"name\":\"minter\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"}],\"name\":\"balances\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"receiver\",\"type\":\"address\"},{\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"mint\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"version\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"x\",\"type\":\"bytes32\"},{\"name\":\"y\",\"type\":\"string\"}],\"name\":\"show\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"receiver\",\"type\":\"address\"},{\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"send\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"ver\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"Sent\",\"type\":\"event\"}]"

// CoinBin is the compiled bytecode used for deploying new contracts.
const CoinBin = `"608060405234801561001057600080fd5b506040516108b03803806108b08339818101604052602081101561003357600080fd5b81019080805164010000000081111561004b57600080fd5b8281019050602081018481111561006157600080fd5b815185600182028301116401000000008211171561007e57600080fd5b505092919050505033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600090805190602001906100dd9291906100e4565b5050610189565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061012557805160ff1916838001178555610153565b82800160010185558215610153579182015b82811115610152578251825591602001919060010190610137565b5b5090506101609190610164565b5090565b61018691905b8082111561018257600081600090555060010161016a565b5090565b90565b610718806101986000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c8063075461721461006757806327e235e3146100b157806340c10f191461010957806354fd4d5014610157578063a38081e8146101da578063d0679d34146102b3575b600080fd5b61006f610301565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6100f3600480360360208110156100c757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610327565b6040518082815260200191505060405180910390f35b6101556004803603604081101561011f57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061033f565b005b61015f610390565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561019f578082015181840152602081019050610184565b50505050905090810190601f1680156101cc5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61029d600480360360408110156101f057600080fd5b81019080803590602001909291908035906020019064010000000081111561021757600080fd5b82018360208201111561022957600080fd5b8035906020019184600183028401116401000000008311171561024b57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061042e565b6040518082815260200191505060405180910390f35b6102ff600480360360408110156102c957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506104f1565b005b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60026020528060005260406000206000915090505481565b80600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055505050565b60008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104265780601f106103fb57610100808354040283529160200191610426565b820191906000526020600020905b81548152906001019060200180831161040957829003601f168201915b505050505081565b6000828233426040516020018085815260200184805190602001908083835b60208310610470578051825260208201915060208101905060208303925061044d565b6001836020036101000a0380198251168184511680821785525050505050509050018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b815260140182815260200194505050505060405160208183030381529060405280519060200120905092915050565b600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548111156105a6576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f496e73756666696369656e742062616c616e63652e000000000000000000000081525060200191505060405180910390fd5b80600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555080600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055507f3990db2d31862302a685e8086b5755072a6e2b5b780af1ee81ece35ee3cd3345338383604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a1505056fea265627a7a72305820102680bd2aecc1593b925bf790c649f740d08fe2b8b7675ce5df7ce8ac536e0764736f6c63430005090032"`

// DeployCoin deploys a new Ethereum contract, binding an instance of Coin to it.
func DeployCoin(auth *bind.TransactOpts, backend bind.ContractBackend, ver string) (common.Address, *types.Transaction, *Coin, error) {
	parsed, err := abi.JSON(strings.NewReader(CoinABI))
	if err != nil {
		return common.Address{}, nil, nil, err
	}
	address, tx, contract, err := bind.DeployContract(auth, parsed, common.FromHex(CoinBin), backend, ver)
	if err != nil {
		return common.Address{}, nil, nil, err
	}
	return address, tx, &Coin{CoinCaller: CoinCaller{contract: contract}, CoinTransactor: CoinTransactor{contract: contract}, CoinFilterer: CoinFilterer{contract: contract}}, nil
}

// Coin is an auto generated Go binding around an Ethereum contract.
type Coin struct {
	CoinCaller     // Read-only binding to the contract
	CoinTransactor // Write-only binding to the contract
	CoinFilterer   // Log filterer for contract events
}

// CoinCaller is an auto generated read-only Go binding around an Ethereum contract.
type CoinCaller struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// CoinTransactor is an auto generated write-only Go binding around an Ethereum contract.
type CoinTransactor struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// CoinFilterer is an auto generated log filtering Go binding around an Ethereum contract events.
type CoinFilterer struct {
	contract *bind.BoundContract // Generic contract wrapper for the low level calls
}

// CoinSession is an auto generated Go binding around an Ethereum contract,
// with pre-set call and transact options.
type CoinSession struct {
	Contract     *Coin             // Generic contract binding to set the session for
	CallOpts     bind.CallOpts     // Call options to use throughout this session
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// CoinCallerSession is an auto generated read-only Go binding around an Ethereum contract,
// with pre-set call options.
type CoinCallerSession struct {
	Contract *CoinCaller   // Generic contract caller binding to set the session for
	CallOpts bind.CallOpts // Call options to use throughout this session
}

// CoinTransactorSession is an auto generated write-only Go binding around an Ethereum contract,
// with pre-set transact options.
type CoinTransactorSession struct {
	Contract     *CoinTransactor   // Generic contract transactor binding to set the session for
	TransactOpts bind.TransactOpts // Transaction auth options to use throughout this session
}

// CoinRaw is an auto generated low-level Go binding around an Ethereum contract.
type CoinRaw struct {
	Contract *Coin // Generic contract binding to access the raw methods on
}

// CoinCallerRaw is an auto generated low-level read-only Go binding around an Ethereum contract.
type CoinCallerRaw struct {
	Contract *CoinCaller // Generic read-only contract binding to access the raw methods on
}

// CoinTransactorRaw is an auto generated low-level write-only Go binding around an Ethereum contract.
type CoinTransactorRaw struct {
	Contract *CoinTransactor // Generic write-only contract binding to access the raw methods on
}

// NewCoin creates a new instance of Coin, bound to a specific deployed contract.
func NewCoin(address common.Address, backend bind.ContractBackend) (*Coin, error) {
	contract, err := bindCoin(address, backend, backend, backend)
	if err != nil {
		return nil, err
	}
	return &Coin{CoinCaller: CoinCaller{contract: contract}, CoinTransactor: CoinTransactor{contract: contract}, CoinFilterer: CoinFilterer{contract: contract}}, nil
}

// NewCoinCaller creates a new read-only instance of Coin, bound to a specific deployed contract.
func NewCoinCaller(address common.Address, caller bind.ContractCaller) (*CoinCaller, error) {
	contract, err := bindCoin(address, caller, nil, nil)
	if err != nil {
		return nil, err
	}
	return &CoinCaller{contract: contract}, nil
}

// NewCoinTransactor creates a new write-only instance of Coin, bound to a specific deployed contract.
func NewCoinTransactor(address common.Address, transactor bind.ContractTransactor) (*CoinTransactor, error) {
	contract, err := bindCoin(address, nil, transactor, nil)
	if err != nil {
		return nil, err
	}
	return &CoinTransactor{contract: contract}, nil
}

// NewCoinFilterer creates a new log filterer instance of Coin, bound to a specific deployed contract.
func NewCoinFilterer(address common.Address, filterer bind.ContractFilterer) (*CoinFilterer, error) {
	contract, err := bindCoin(address, nil, nil, filterer)
	if err != nil {
		return nil, err
	}
	return &CoinFilterer{contract: contract}, nil
}

// bindCoin binds a generic wrapper to an already deployed contract.
func bindCoin(address common.Address, caller bind.ContractCaller, transactor bind.ContractTransactor, filterer bind.ContractFilterer) (*bind.BoundContract, error) {
	parsed, err := abi.JSON(strings.NewReader(CoinABI))
	if err != nil {
		return nil, err
	}
	return bind.NewBoundContract(address, parsed, caller, transactor, filterer), nil
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Coin *CoinRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Coin.Contract.CoinCaller.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Coin *CoinRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, error) {
	return _Coin.Contract.CoinTransactor.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Coin *CoinRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, error) {
	return _Coin.Contract.CoinTransactor.contract.Transact(opts, method, params...)
}

// Call invokes the (constant) contract method with params as input values and
// sets the output to result. The result type might be a single field for simple
// returns, a slice of interfaces for anonymous returns and a struct for named
// returns.
func (_Coin *CoinCallerRaw) Call(opts *bind.CallOpts, result interface{}, method string, params ...interface{}) error {
	return _Coin.Contract.contract.Call(opts, result, method, params...)
}

// Transfer initiates a plain transaction to move funds to the contract, calling
// its default method if one is available.
func (_Coin *CoinTransactorRaw) Transfer(opts *bind.TransactOpts) (*types.Transaction, error) {
	return _Coin.Contract.contract.Transfer(opts)
}

// Transact invokes the (paid) contract method with params as input values.
func (_Coin *CoinTransactorRaw) Transact(opts *bind.TransactOpts, method string, params ...interface{}) (*types.Transaction, error) {
	return _Coin.Contract.contract.Transact(opts, method, params...)
}

// Balances is a free data retrieval call binding the contract method 0x27e235e3.
//
// Solidity: function balances( address) constant returns(uint256)
func (_Coin *CoinCaller) Balances(opts *bind.CallOpts, arg0 common.Address) (*big.Int, error) {
	var (
		ret0 = new(*big.Int)
	)
	out := ret0
	err := _Coin.contract.Call(opts, out, "balances", arg0)
	return *ret0, err
}

// Balances is a free data retrieval call binding the contract method 0x27e235e3.
//
// Solidity: function balances( address) constant returns(uint256)
func (_Coin *CoinSession) Balances(arg0 common.Address) (*big.Int, error) {
	return _Coin.Contract.Balances(&_Coin.CallOpts, arg0)
}

// Balances is a free data retrieval call binding the contract method 0x27e235e3.
//
// Solidity: function balances( address) constant returns(uint256)
func (_Coin *CoinCallerSession) Balances(arg0 common.Address) (*big.Int, error) {
	return _Coin.Contract.Balances(&_Coin.CallOpts, arg0)
}

// Minter is a free data retrieval call binding the contract method 0x07546172.
//
// Solidity: function minter() constant returns(address)
func (_Coin *CoinCaller) Minter(opts *bind.CallOpts) (common.Address, error) {
	var (
		ret0 = new(common.Address)
	)
	out := ret0
	err := _Coin.contract.Call(opts, out, "minter")
	return *ret0, err
}

// Minter is a free data retrieval call binding the contract method 0x07546172.
//
// Solidity: function minter() constant returns(address)
func (_Coin *CoinSession) Minter() (common.Address, error) {
	return _Coin.Contract.Minter(&_Coin.CallOpts)
}

// Minter is a free data retrieval call binding the contract method 0x07546172.
//
// Solidity: function minter() constant returns(address)
func (_Coin *CoinCallerSession) Minter() (common.Address, error) {
	return _Coin.Contract.Minter(&_Coin.CallOpts)
}

// Show is a free data retrieval call binding the contract method 0xa38081e8.
//
// Solidity: function show(x bytes32, y string) constant returns(bytes32)
func (_Coin *CoinCaller) Show(opts *bind.CallOpts, x [32]byte, y string) ([32]byte, error) {
	var (
		ret0 = new([32]byte)
	)
	out := ret0
	err := _Coin.contract.Call(opts, out, "show", x, y)
	return *ret0, err
}

// Show is a free data retrieval call binding the contract method 0xa38081e8.
//
// Solidity: function show(x bytes32, y string) constant returns(bytes32)
func (_Coin *CoinSession) Show(x [32]byte, y string) ([32]byte, error) {
	return _Coin.Contract.Show(&_Coin.CallOpts, x, y)
}

// Show is a free data retrieval call binding the contract method 0xa38081e8.
//
// Solidity: function show(x bytes32, y string) constant returns(bytes32)
func (_Coin *CoinCallerSession) Show(x [32]byte, y string) ([32]byte, error) {
	return _Coin.Contract.Show(&_Coin.CallOpts, x, y)
}

// Version is a free data retrieval call binding the contract method 0x54fd4d50.
//
// Solidity: function version() constant returns(string)
func (_Coin *CoinCaller) Version(opts *bind.CallOpts) (string, error) {
	var (
		ret0 = new(string)
	)
	out := ret0
	err := _Coin.contract.Call(opts, out, "version")
	return *ret0, err
}

// Version is a free data retrieval call binding the contract method 0x54fd4d50.
//
// Solidity: function version() constant returns(string)
func (_Coin *CoinSession) Version() (string, error) {
	return _Coin.Contract.Version(&_Coin.CallOpts)
}

// Version is a free data retrieval call binding the contract method 0x54fd4d50.
//
// Solidity: function version() constant returns(string)
func (_Coin *CoinCallerSession) Version() (string, error) {
	return _Coin.Contract.Version(&_Coin.CallOpts)
}

// Mint is a paid mutator transaction binding the contract method 0x40c10f19.
//
// Solidity: function mint(receiver address, amount uint256) returns()
func (_Coin *CoinTransactor) Mint(opts *bind.TransactOpts, receiver common.Address, amount *big.Int) (*types.Transaction, error) {
	return _Coin.contract.Transact(opts, "mint", receiver, amount)
}

// Mint is a paid mutator transaction binding the contract method 0x40c10f19.
//
// Solidity: function mint(receiver address, amount uint256) returns()
func (_Coin *CoinSession) Mint(receiver common.Address, amount *big.Int) (*types.Transaction, error) {
	return _Coin.Contract.Mint(&_Coin.TransactOpts, receiver, amount)
}

// Mint is a paid mutator transaction binding the contract method 0x40c10f19.
//
// Solidity: function mint(receiver address, amount uint256) returns()
func (_Coin *CoinTransactorSession) Mint(receiver common.Address, amount *big.Int) (*types.Transaction, error) {
	return _Coin.Contract.Mint(&_Coin.TransactOpts, receiver, amount)
}

// Send is a paid mutator transaction binding the contract method 0xd0679d34.
//
// Solidity: function send(receiver address, amount uint256) returns()
func (_Coin *CoinTransactor) Send(opts *bind.TransactOpts, receiver common.Address, amount *big.Int) (*types.Transaction, error) {
	return _Coin.contract.Transact(opts, "send", receiver, amount)
}

// Send is a paid mutator transaction binding the contract method 0xd0679d34.
//
// Solidity: function send(receiver address, amount uint256) returns()
func (_Coin *CoinSession) Send(receiver common.Address, amount *big.Int) (*types.Transaction, error) {
	return _Coin.Contract.Send(&_Coin.TransactOpts, receiver, amount)
}

// Send is a paid mutator transaction binding the contract method 0xd0679d34.
//
// Solidity: function send(receiver address, amount uint256) returns()
func (_Coin *CoinTransactorSession) Send(receiver common.Address, amount *big.Int) (*types.Transaction, error) {
	return _Coin.Contract.Send(&_Coin.TransactOpts, receiver, amount)
}

// CoinSentIterator is returned from FilterSent and is used to iterate over the raw logs and unpacked data for Sent events raised by the Coin contract.
type CoinSentIterator struct {
	Event *CoinSent // Event containing the contract specifics and raw log

	contract *bind.BoundContract // Generic contract to use for unpacking event data
	event    string              // Event name to use for unpacking event data

	logs chan types.Log        // Log channel receiving the found contract events
	sub  ethereum.Subscription // Subscription for errors, completion and termination
	done bool                  // Whether the subscription completed delivering logs
	fail error                 // Occurred error to stop iteration
}

// Next advances the iterator to the subsequent event, returning whether there
// are any more events found. In case of a retrieval or parsing error, false is
// returned and Error() can be queried for the exact failure.
func (it *CoinSentIterator) Next() bool {
	// If the iterator failed, stop iterating
	if it.fail != nil {
		return false
	}
	// If the iterator completed, deliver directly whatever's available
	if it.done {
		select {
		case log := <-it.logs:
			it.Event = new(CoinSent)
			if err := it.contract.UnpackLog(it.Event, it.event, log); err != nil {
				it.fail = err
				return false
			}
			it.Event.Raw = log
			return true

		default:
			return false
		}
	}
	// Iterator still in progress, wait for either a data or an error event
	select {
	case log := <-it.logs:
		it.Event = new(CoinSent)
		if err := it.contract.UnpackLog(it.Event, it.event, log); err != nil {
			it.fail = err
			return false
		}
		it.Event.Raw = log
		return true

	case err := <-it.sub.Err():
		it.done = true
		it.fail = err
		return it.Next()
	}
}

// Error returns any retrieval or parsing error occurred during filtering.
func (it *CoinSentIterator) Error() error {
	return it.fail
}

// Close terminates the iteration process, releasing any pending underlying
// resources.
func (it *CoinSentIterator) Close() error {
	it.sub.Unsubscribe()
	return nil
}

// CoinSent represents a Sent event raised by the Coin contract.
type CoinSent struct {
	From   common.Address
	To     common.Address
	Amount *big.Int
	Raw    types.Log // Blockchain specific contextual infos
}

// FilterSent is a free log retrieval operation binding the contract event 0x3990db2d31862302a685e8086b5755072a6e2b5b780af1ee81ece35ee3cd3345.
//
// Solidity: e Sent(from address, to address, amount uint256)
func (_Coin *CoinFilterer) FilterSent(opts *bind.FilterOpts) (*CoinSentIterator, error) {

	logs, sub, err := _Coin.contract.FilterLogs(opts, "Sent")
	if err != nil {
		return nil, err
	}
	return &CoinSentIterator{contract: _Coin.contract, event: "Sent", logs: logs, sub: sub}, nil
}

// WatchSent is a free log subscription operation binding the contract event 0x3990db2d31862302a685e8086b5755072a6e2b5b780af1ee81ece35ee3cd3345.
//
// Solidity: e Sent(from address, to address, amount uint256)
func (_Coin *CoinFilterer) WatchSent(opts *bind.WatchOpts, sink chan<- *CoinSent) (event.Subscription, error) {

	logs, sub, err := _Coin.contract.WatchLogs(opts, "Sent")
	if err != nil {
		return nil, err
	}
	return event.NewSubscription(func(quit <-chan struct{}) error {
		defer sub.Unsubscribe()
		for {
			select {
			case log := <-logs:
				// New log arrived, parse the event and forward to the user
				event := new(CoinSent)
				if err := _Coin.contract.UnpackLog(event, "Sent", log); err != nil {
					return err
				}
				event.Raw = log

				select {
				case sink <- event:
				case err := <-sub.Err():
					return err
				case <-quit:
					return nil
				}
			case err := <-sub.Err():
				return err
			case <-quit:
				return nil
			}
		}
	}), nil
}
