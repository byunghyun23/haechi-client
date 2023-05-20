# HAECHI: SmartContract Security Diagnostic Tool - server(API)

Please see <a href='https://github.com/byunghyun23/haechi/'>here</a> for more details on the solution.   
This repository is an API that makes HAECHI available over TCP.   
Enter your Solidity code from the web through <a href='https://github.com/byunghyun23/haechi-web/'>here</a>.

## Try
* You can experience it <a href='http://13.209.7.107:8080'>here</a>!

## Requirements
* Windows or Linux
* solidity compiler 0.4.25 or 0.4.26

## Install (Only Linux)
* sudo snap install docker
* sudo docker pull ethereum/solc:0.4.25

## Details
* Default port (TCP)
```
5757
```
* TCP Steps
```
1. Solidity Code Size
2. Solidity Code
3. Analysis Results of Solidity Code
```

## Run (Linux)
* ./haechi_server.sh

## Run (Windows)
* haechi_server.bat

