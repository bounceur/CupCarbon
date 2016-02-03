atget id x
loop
dreadsensor y
if ($y==1)
send $x
send $y
else
send $x
send 0
endif
delay 500