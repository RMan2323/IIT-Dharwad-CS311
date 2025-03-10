    .data
a:
    5
b:
    10
c:
    2
d:
    19
    .text
main:
    load %x0, $a, %x3   //x3 = 5
    load %x0, $b, %x4   //x4 = 10
    add %x3, %x4, %x5   //x5 = 15
    sub %x5, %x3, %x6   //x6 = 10
    load %x0, $c, %x7   //x7 = 2
    load %x0, $d, %x8   //x8 = 19
    add %x7, %x6, %x8   //x8 = 12
    mul %x8, %x7, %x10  //x10 = 24
    addi %x5, 5, %x11   //x11 = 20
    end