	.data
a:
	14
	.text
main:
	load %x0, $a, %x3		//x3 = a
	addi %x0, 2, %x4			//x4 = 2
	blt %x3, %x4, endProg	//a < 2 then end
	addi %x0, 1, %x10		//x10 = 1 (assume prime)
loop:
	beq %x4, %x3, endProg	//x4 == x3 then end
	div %x3, %x4, %x5		//x5 = x3/x4
	beq %x31, %x0, notPrime	//remainder == 0 then notPrime
	addi %x4, 1, %x4		//x4++
	jmp loop
notPrime:
	subi %x10, 2, %x10		//x10 = -1
	end
endProg:
	end