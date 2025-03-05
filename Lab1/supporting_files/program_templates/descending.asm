	.data
a:
	60
	23
	67
	867
	23
	76
	123
	967
	-32
	-75
	34
	-56
	234
n:
	13
	.text
main:
	load %x0, $n, %x10			//x10 = n
	subi %x10, 1, %x10			//x10 = n-1
loopOut:
	beq %x3, %x10, endProg		//i == n-1 then break
	sub %x4, %x4, %x4			//x4 = 0
loopIn:
	sub %x10, %x3, %x11			//x11 = n-1-i = n-i-1
	beq %x4, %x11, breakInner	//x4 == n-i-1 then break
	load %x4, $a, %x5			//x5 = a[j]
	addi %x4, 1, %x12			//x12 = j+1
	load %x12, $a, %x6			//x6 = a[j+1]
	blt %x5, %x6, swap			//a[j] < a[j+1] then swap
ret:
	addi %x4, 1, %x4			//j++
	jmp loopIn
	addi %x3, 1, %x3			//i++
	jmp loopOut
swap:
	store %x6, $a, %x4
	store %x5, $a, %x12
	jmp ret
endProg:
	end
breakInner:
	addi %x3, 1, %x3			//x3++
	jmp loopOut