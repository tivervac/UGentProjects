	.file	"recurse.c"
	.text
	.type	r, @function
r:
.LFB11:
	.cfi_startproc
	testl	%eax, %eax
	je	.L3
	pushl	%ebx
	.cfi_def_cfa_offset 8
	.cfi_offset 3, -8
	subl	$8, %esp
	.cfi_def_cfa_offset 16
	movl	%eax, %ebx
	imull	%eax, %ebx
	subl	$1, %eax
	call	r
	addl	%ebx, %eax
	jmp	.L2
.L3:
	.cfi_def_cfa_offset 4
	.cfi_restore 3
	movl	$0, %eax
	ret
.L2:
	.cfi_def_cfa_offset 16
	.cfi_offset 3, -8
	addl	$8, %esp
	.cfi_def_cfa_offset 8
	popl	%ebx
	.cfi_restore 3
	.cfi_def_cfa_offset 4
	.p2align 4,,1
	ret
	.cfi_endproc
.LFE11:
	.size	r, .-r
	.section	.rodata.str1.1,"aMS",@progbits,1
.LC0:
	.string	"%d\n"
	.text
	.globl	main
	.type	main, @function
main:
.LFB12:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	movl	%esp, %ebp
	.cfi_def_cfa_register 5
	andl	$-16, %esp
	subl	$16, %esp
	movl	$1000, %eax
	call	r
	movl	%eax, 4(%esp)
	movl	$.LC0, (%esp)
	call	printf
	movl	$0, %eax
	leave
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE12:
	.size	main, .-main
	.ident	"GCC: (Debian 4.7.2-5) 4.7.2"
	.section	.note.GNU-stack,"",@progbits
