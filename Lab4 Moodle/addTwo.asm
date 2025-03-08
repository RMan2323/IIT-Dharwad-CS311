	.data
a:
	5
b:
	6
	.text
main:
	load %x0, $a, %x3
	load %x0, $b, %x4
	add %x3, %x4, %x5
	end