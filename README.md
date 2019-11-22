# fog-computing
This project is an implementation of functionality of a node in a distributed system in the context of Fog Computing. The system consists of FogNode, IoTNode and CloudNode, each executed on different host machines. IoTNode is the request generator and FogNode, CloudNode do the processing of these requests.

This is implemented in java and needs java compiler version greater than or equal to 8.
All the java files have been created in the package com.utd.acn.project.

Below are the steps to execute the project:

1. Login to the host where a node needs to be run.
    ssh ixm190001@dc05.utdallas.edu   
2. Copy the project from local system to the remote machine
    {dc05:~} mkdir fog-computing
    ➜  ~ scp -r workspace/fog-computing/* ixm190001@dc05.utdallas.edu:~/fog-computing/    
3. Compile the java files
    {dc05:~/fog-computing} mkdir classes
    {dc05:~/fog-computing}  javac -d classes src/com/utd/acn/project/*.java 
4. Run the nodes(fog, IoT,cloud) by passing relevant arguments as given below.
    {dc05:~/fog-computing} cd classes/
    CloudNode:
    {dc05:~/fog-computing/classes} java com/utd/acn/project/CloudNode MY_TCP
    {dc05:~/fog-computing/classes} java com/utd/acn/project/CloudNode 5331 
    FogNode:
    {dc04:~/fog-computing/classes} java com/utd/acn/project/FogNode Max_Process_Time interval MY_TCP MY_UDP IP_C TCP_C IP_N1 TCP_N1 ...
    {dc04:~/fog-computing/classes} java com/utd/acn/project/FogNode 25 3 5326 9879 10.176.69.37 5331 10.176.69.36 5325
    IoTNode:
    {dc08:~/fog-computing/classes} java com/utd/acn/project/IoTNode interval MY_UDP IP_C UDP_C
    {dc08:~/fog-computing/classes} java com/utd/acn/project/IoTNode 10 9883 10.176.69.35 9879
5. Ctrl+C to stop program execution
