android-sqrl
============

Creating proof of concept code for Steve Gibson's SQRL. 
As some of you may know Steve Gibson has suggested a new way to do authentication. You can see the details here https://www.grc.com/sqrl/sqrl.htm. It has potential and I would like to see some proof of concept code. I therefore started throwing something together for Android. It now more or less works, but it needs a lot of improvements. If any one wants to work on it please feel free.

The app now works with the test code here https://github.com/geir54/php-sqrl. Check out http://sqrl.host56.com/ for a demo

If you need help building the code or have questions come join us in #SQRL on efnet (IRC)

The old ECC code has been replaced by https://github.com/dazoe/Android.Ed25519 and the code is now really fast

#### To build
1. git submodule init
2. git submodule update
3. Import in eclipse and it should work


