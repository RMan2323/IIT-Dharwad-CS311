.data:
n:
    1000
    .text:
main:
    load %x0, $n, %x3   //x3 = n
loop:
    load %x0, 3, %x5
    beq %x4, %x3, endProg
    addi %x4, 1, %x4    //x4++
    addi %x5, 1, %x5
    addi %x5, 1, %x5
    addi %x5, 1, %x5
    addi %x5, 1, %x5
    addi %x5, 1, %x5
    addi %x5, 1, %x5
    addi %x5, 1, %x5
    addi %x5, 1, %x5
    jmp loop
endProg:
    end