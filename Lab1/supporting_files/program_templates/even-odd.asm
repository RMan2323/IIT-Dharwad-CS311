	.data
a:
	3
	.text
main:
	load %x0, $a, %x3
	divi %x3, 2, %x4
	beq %x31, %x0, eve
	addi %x0, 1, %x10	//x10 = 1
	end
eve:
	subi %x0, 1, %x10	//x10 = -1
	end
