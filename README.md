android-sqrl
============

Creating proof of concept code for Steve Gibson�s SQRL. 
As some of you may know Steve Gibson has suggested a new way to do authentication. You can see the details here https://www.grc.com/sqrl/sqrl.htm. It has potential and I would like to see some proof of concept code. I therefore started throwing something together for Android. It now more or less works, but it needs a lot of improvements. If any one wants to work on it please feel free.

The biggest issue right now is that the ECC code takes a couple of minuts to make a signature. The ECC code has been forked from k3d3. It had to be rewritten to work with Android. This part is kept as a separate project https://github.com/geir54/ed25519-java.

Looks like something is being done server side as well https://github.com/trianglman/sqrl
And for java https://github.com/TheBigS/SQRL

####To build
1. cd libs
2. git clone https://github.com/LivotovLabs/zxscanlib.git
3. cd android-sqrl\src\com\sqrl\crypto
4. git clone https://github.com/geir54/ed25519-java.git
5. Move the files out of ed25519-java and in to crypto
6. Import in eclipse and it should work

Also included a QR code for testing
![QR](https://raw.github.com/geir54/android-sqrl/master/qrcode.png)
