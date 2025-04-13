.data:
n:
    1000
    .text:
main:
    load %x0, $n, %x3
loop:
    load %x0, 3, %x5
    load %x0, 4, %x5
    load %x0, 5, %x5
    load %x0, 6, %x5
    load %x0, 7, %x5
    load %x0, 8, %x5
    beq %x4, %x3, endProg
    addi %x4, 1, %x4
    jmp loop
endProg:
    end