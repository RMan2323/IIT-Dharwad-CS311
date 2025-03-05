	.data
a:
	-10
	.text
main:
	load %x0, $a, %x3		//x3 = a
	subi %x0, 1, %x10		//x10 = -1 (assume not palindrome)
	blt %x3, %x0, endProg	//x3 < 0 then endProg
	add %x0, %x3, %x5		//x5 = x3
loop:
	beq %x5, %x0, check		//x5 == 0 then check
	divi %x5, 10, %x5		//x5 = x5/10
	muli %x4, 10, %x4		//x4 = x4*10
	add %x4, %x31, %x4		//x4 = x4+remainder
	jmp loop
check:
	beq %x3, %x4, yes		//revNum == num then yes
	end
yes:
	addi %x0, 1, %x10		//x10 = 1
endProg:
	end