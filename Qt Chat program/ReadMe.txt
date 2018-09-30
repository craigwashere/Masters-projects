This is a simple chat program that runs through a central server.  Each "client" runs the "splitter" program and connects to a host running "server".  These compiled and ran on Ubuntu version 12 through Qt Creator.

I learned just enough Qt to write this program.  It makes use of the Signals and Slots language for Qt.	

Splitter:
When a client side is invoked, it will prompt the user to select a user ID. This ID is used by other users to identify from whom a message is generated. A client can join or leave a chat session at anytime. A user announces his/her intent to join a chat session by sending the chosen ID to the chat server. The server, in turn, will broadcast the new comer’s ID to all current users. 

A <ctrl-d> by itself on a line indicates a user’s intent to terminate a chat session. A message showing a client has left must be displayed on all other clients’ screens. When there is only one user online, he/she must be told by the server to wait until at least one more user has joined the chat session.

The main.cpp just creates the Qt window, everything is handled in the member functions of the "MainWindow".

dia_server.cpp is a simple dialog box created from clicking "Server" on the main window.  It provides texts boxes for the server IP address, port number, and user name.


Sever:
After the server has been started, up to m clients can communicate with each other via the server at the same time. Each client is identified at the user level by a user selected ID, which can be of any combination of letters and numbers. Messages sent from one client will be displayed on all other clients. Each message is prefixed by a symbol that consists of its user’s chosen ID, followed by a colon (:). It is the server’s responsibility to make sure that each message be sent in its entirety before sending another message. Let’s define a message be a sequence of characters terminated by a carriage return.
	

The server can be terminated by the system administrator via the SIGUSR1 signal. Before the server terminates, it must send a message to all clients indicating its upcoming shutdown.
