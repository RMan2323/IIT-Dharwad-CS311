    .data
a:
    5
b:
    10
    .text
main:
    load %x0, $a, %x3
    load %x0, $b, %x4
    blt %x3, %x4, less
    jmp endProg
    load %x0, $a, %x4
    end
endProg:
    end
less:
    addi %x0, 1, %x10
    end