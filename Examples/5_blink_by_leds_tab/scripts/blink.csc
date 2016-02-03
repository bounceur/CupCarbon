tab t 5 1
tset t 0 0 5
tset t 1 0 4
tset t 2 0 3
tset t 3 0 2
tset t 4 0 1
loop
for i 0 4
 tget t $i 0 x
 led 1 $x
 delay 1000
endfor