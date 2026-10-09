# Lab 1c: Socket Programming in Java

**Computer Networks 1 - Lab 4**  
Faculty of Computer Science and Engineering, HCMC University of Technology (HCMUT)

## Objectives
1. Practice with Socket programming in Java.
2. Build a multithreaded chat application using the client-server model.
3. Understand multithreading and thread termination mechanisms in network applications.

## Directory Structure
- [DownloadHomepage.java](file:///c:/Users/Acer/Downloads/LAB!/Socket-Programming-Lab1c/DownloadHomepage.java): Exercise 1 - Connects via HTTP socket to port 80 and downloads web homepages.
- [PrimeRun.java](file:///c:/Users/Acer/Downloads/LAB!/Socket-Programming-Lab1c/PrimeRun.java): Multithreading demonstration computing prime numbers concurrently.
- [StopTest.java](file:///c:/Users/Acer/Downloads/LAB!/Socket-Programming-Lab1c/StopTest.java): Demonstrates thread interrupt behavior vs. socket closing on blocking `accept()`.
- [ChatServer.java](file:///c:/Users/Acer/Downloads/LAB!/Socket-Programming-Lab1c/ChatServer.java): Exercise 2 & 3 - Multi-threaded Chat Server with GUI.
- [ChatClient.java](file:///c:/Users/Acer/Downloads/LAB!/Socket-Programming-Lab1c/ChatClient.java): Exercise 2 & 3 - Multi-threaded Chat Client with GUI.
- [homepage.html](file:///c:/Users/Acer/Downloads/LAB!/Socket-Programming-Lab1c/homepage.html): Downloaded HTML output from Exercise 1.
- [Lab_1c_Socket Programming in Java.pdf](file:///c:/Users/Acer/Downloads/LAB!/Socket-Programming-Lab1c/Lab_1c_Socket%20Programming%20in%20Java.pdf): Official lab prompt document.

## How to Run

### 1. Compile
```bash
javac -encoding UTF-8 *.java
```

### 2. Run Exercise 1
```bash
java DownloadHomepage www.google.com
```

### 3. Run Multithreading & Stop Thread Tests
```bash
java PrimeRun
java StopTest
```

### 4. Run Chat Application
```bash
# Terminal 1: Start Server
java ChatServer

# Terminal 2: Start Client
java ChatClient
```
