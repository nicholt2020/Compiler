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

printSky:	NOP0
	LDWA	0,i
	STWA	2,s
	RET
;fun_x 6
;fun_y 4
;fun_z 2

fun:	NOP0
	SUBSP	2,i
	SUBSP	2,i
	CALL	getchar
	ADDSP	2,i
	LDWA	-4,s
	SUBSP	2,i
	STWA	0,s
	CALL	putint
	ADDSP	4,i
	LDWA	-2,s
	LDWA	4,s
	STWA	4,s
	LDWA	1,i
	SUBSP	2,i
	STWA	0,s
	LDWA	3,i
	SUBSP	2,i
	STWA	0,s
	LDWA	9,i
	NEGA	
	ADDA	0,s
	STWA	0,s
	ADDSP	2,i
	NEGA
	STWA	6,s
	SUBSP	2,i
	STWA	0,s
	LDWA	90,i
	ADDA	0,s
	STWA	0,s
	LDWA	1,i
	ADDA	0,s
	STWA	0,s
	ADDSP	2,i
	SUBSP	2,i
	LDWA	8,s
	STWA	8,s
	SUBSP	2,i
	STWA	0,s
	CALL	putint
	ADDSP	4,i
	LDWA	-2,s
	LDWA	4,s
	STWA	4,s
	CPWA	0,i
	BREQ	__else2
	LDWA	4,s
	STWA	4,s
	LDWA	1,i
	STWA	4,s
	BR	__end2
__else2:	NOP0
	LDWA	4,s
	STWA	4,s
	LDWA	3,i
	STWA	4,s
__end2:	NOP0
	SUBSP	2,i
	LDWA	6,s
	STWA	6,s
	SUBSP	2,i
	STWA	0,s
	CALL	putint
	ADDSP	4,i
	LDWA	-2,s
	LDWA	0,i
	STWA	2,s
	RET

main:	NOP0
	SUBSP	2,i
	SUBSP	2,i
	LDWA	3,i
	SUBSP	2,i
	STWA	0,s
	LDWA	6,i
	SUBSP	2,i
	STWA	0,s
	LDWA	99,i
	SUBSP	2,i
	STWA	0,s
	CALL	fun
	ADDSP	8,i
	LDWA	-4,s
	SUBSP	2,i
	STWA	0,s
	CALL	putint
	ADDSP	4,i
	LDWA	-2,s
	STWA	2,s
	RET
	.END
