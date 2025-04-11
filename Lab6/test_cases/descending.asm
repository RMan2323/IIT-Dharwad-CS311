.data
a:
	40
	20
	50
	60
	80
	30
	10
	70
n:
	8
	.text
main:
9	sub %x3, %x3, %x3
10	sub %x4, %x4, %x4
11	load %x0, $n, %x8
outerloop:
12	blt %x3, %x8, innerloop
13	end
14	addi %x3, 1, %x4
innerloop:
15	addi %x3, 1, %x4
innerloopz:
16	blt %x4, %x8, swap
17	addi %3, 1, %x3
18	jmp outerloop
swap:
19	load %x3, $a, %x5
20	load %x4, $a, %x6
21	blt %x5, %x6, exchange
22	addi %x4, 1, %x4
23	jmp innerloopz
exchange:
	sub %x7, %x7, %x7
	add %x0, %x5, %x7
	store %x6, 0, %x3
	store %x7, 0, %x4
	addi %x4, 1, %x4
	jmp innerloopz