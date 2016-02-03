atget id myid
loop
data p $myid 1
send $p
delay 1000
data p $myid 0
send $p
delay 1000