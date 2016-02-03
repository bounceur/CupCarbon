loop
rand x
if ($x<0.5)
 mark 1
 println GOOD
 println $x
else
 mark 0
 println BAD
 println $x
endif
delay 1000