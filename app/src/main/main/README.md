# A chat app that uses WiFi Direct for Peer-Peer communication

This app uses WiFi Direct P2P channel to exchange chat message.
It also support pub/sub style that group owner publish messages to all group member.

# Java NIO selector and channel.

Java NIO is widely used today by many projects, including Netty.
It provides non blocking IO facilities for writing scalable servers.

Java NIO provides buffer mechanism for buffering incoming data. Channels are pipes through which
byte buffer are transferred between two entities. Normally, channels are in non-blocking mode. 
For example, SocketChannels are operate on non-blocking mode and are selectable. In other word,
it is event-driven so you do not need a dedicate thread for each socket/channel to handle the request.

Android Messenger
=================

A device-to-device messenger app for Android that enables you to chat with your nearby buddies without being connected to the internet.

Check out the tutorial at [unclouded.io/tutorials/messenger](http://www.unclouded.io/tutorials/messenger).
