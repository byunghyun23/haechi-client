#!/bin/bash
sudo docker run --rm -v $(pwd):/sources ethereum/solc:0.4.25 --overwrite --ast-compact-json /sources/$1 -o /sources/solidity/web
