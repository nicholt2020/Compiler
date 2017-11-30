;Start Of Main Method
	SUBSP	2,i
	CALL	main
	ADDSP	2,i
	STOP

;putint will print out an integer value.
putint:	DECO	2,s
	LDWA	2,s
	STWA	4,s
	RET

;getint will retrieve input.
getint:	DECI	2,s
	RET

;getchar will retrieve input and store the ASCII equivelant.
getchar:	LDWA	0,i
	LDBA	charIn,d
	STWA	2,s
	RET

;putchar will print out an ASCII equivelant.
putchar:	LDWA	2,s
	STBA	charOut,d
	STWA	4,s
	RET
;skyfun2_a 2

skyfun2:	NOP0
	LDWA	2,s
	STWA	2,s
	SUBSP	2,i
	STWA	0,s
	LDWA	7,i
	ADDA	0,s
	STWA	0,s
	ADDSP	2,i
	STWA	2,s
	RET
;skyFun_a 8
;skyFun_b 6
;skyFun_c 4
;skyFun_d 2

skyFun:	NOP0
	LDWA	8,s
	STWA	8,s
	LDWA	17,i
	STWA	8,s
	LDWA	0,i
	CPWA	0,i
	BREQ	__else2
	SUBSP	2,i
	LDWA	10,s
	STWA	10,s
	SUBSP	2,i
	STWA	0,s
	CALL	putint
	ADDSP	4,i
	LDWA	-2,s
	BR	__end2
__else2:	NOP0
	SUBSP	2,i
	SUBSP	2,i
	LDWA	12,s
	STWA	12,s
	SUBSP	2,i
	STWA	0,s
	CALL	skyfun2
	ADDSP	4,i
	LDWA	-2,s
	SUBSP	2,i
	STWA	0,s
	CALL	putint
	ADDSP	4,i
	LDWA	-2,s
__end2:	NOP0
	LDWA	0,i
	STWA	2,s
	RET

main:	NOP0
	SUBSP	2,i
	LDWA	7,i
	SUBSP	2,i
	STWA	0,s
	LDWA	23,i
	SUBSP	2,i
	STWA	0,s
	LDWA	19,i
	SUBSP	2,i
	STWA	0,s
	LDWA	4,i
	SUBSP	2,i
	STWA	0,s
	CALL	skyFun
	ADDSP	10,i
	LDWA	-2,s
	STWA	2,s
	RET
	.END
