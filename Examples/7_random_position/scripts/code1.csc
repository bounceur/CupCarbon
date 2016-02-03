getpos2 cx cy
loop
 rand x
 rand y
 minus x $x 0.5
 minus y $y 0.5
 div x $x 5000
 div y $y 5000
 plus cx $cx $x
 plus cy $cy $y
 println $cx
 coord $cx $cy 1000