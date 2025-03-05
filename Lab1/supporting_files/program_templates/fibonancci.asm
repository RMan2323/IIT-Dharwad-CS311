	.data
n:
	2
	.text
main:
	load %x0, $n, %x3	  //x3 = n
	addi %x0, 1, %x4	  //x4 = 1
	blt %x3, %x4, endProg //n < 1 then end
	beq %x3, %4, one	  //n == 1 then one
	addi %x0, 2, %x5	  //x5 = 2
	beq %x3, %x5, two	  //n == 2 then two
	jmp gt3				  //n >= 3 then gt3
	end
one:
	store %x0, 65535, %x0  //[65535] = 0
	end
two:
	store %x0, 65535, %x0  //[65535] = 0
	store %x4, 65534, %x0  //[65534] = 1
	end
gt3:
	store %x0, 65535, %x0  //[65535] = 0
	store %x4, 65534, %x0  //[65534] = 1
	subi %x3, 2, %x3		//x3-=2 (because 0, 1 have already been stored)
	sub %x4, %x4, %x4		//x4 = 0
	addi %x0, 1, %x5		//x5 = 1
start:
	add %x4, %x5, %x6	  	//x6 = x4+x5
	add %x5, %x0, %x4	  	//x4 = x5
	add %x6, %x0, %x5	  	//x5 = x6
	store %x6, 65533, %x10	//[65533-|x10|] = x6
	subi %x10, 1, %x10		//x10--
	subi %x3, 1, %x3		//x3--
	bgt %x3, %x0, start		//x3 > 0 then start
	end
endProg:
	end