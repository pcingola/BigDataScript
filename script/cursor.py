#!/usr/bin/python

#--------------------------------------------------------------------------------
#
# Show X window's cursor position
#
#--------------------------------------------------------------------------------

from Xlib import X, display
import time

root = display.Display().screen().root

while 1 :
	a=root.query_pointer()
	b=a._data
	print "x=", b["root_x"], "y=", b["root_y"]
	time.sleep(0.1)
d.sync()

