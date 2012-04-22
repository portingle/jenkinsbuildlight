jenkinsbuildlight
=================

Jenkins CI Build Light using KMTronics USB relay

See the project page http://portingle.github.com/jenkinsbuildlight

The CI Build Light approach I describe here is suitable/convenient if you need simple on/off control and/or if you want the option of controlling higher power loads than a solid state solution would permit.

By contast, I have a buddy who uses a power LED for his build light and he wants control of the brightness of the LED, for this he uses Pulse Width Modulation (PWM) to control the brightness of the LED, and so 
a solution like mine doesn't work for him. In his case he has used a Arduino UNO and wired it's PWM pins control a transistor that drives the power LED.
Like mine, his board is powered by the USB connection to the PC and the load on the USB is around 400ma.