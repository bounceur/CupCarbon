atget id myid
loop
wait
read x
rdata $x a b
data p $myid $b
send $p * $a