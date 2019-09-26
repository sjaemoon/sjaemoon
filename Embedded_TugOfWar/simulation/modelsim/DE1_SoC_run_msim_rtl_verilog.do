transcript on
if {[file exists rtl_work]} {
	vdel -lib rtl_work -all
}
vlib rtl_work
vmap work rtl_work

vlog -sv -work work +incdir+C:/Users/sjaem/Desktop/EE271/lab5 {C:/Users/sjaem/Desktop/EE271/lab5/seg7.sv}
vlog -sv -work work +incdir+C:/Users/sjaem/Desktop/EE271/lab5 {C:/Users/sjaem/Desktop/EE271/lab5/userInput.sv}
vlog -sv -work work +incdir+C:/Users/sjaem/Desktop/EE271/lab5 {C:/Users/sjaem/Desktop/EE271/lab5/normalLight.sv}
vlog -sv -work work +incdir+C:/Users/sjaem/Desktop/EE271/lab5 {C:/Users/sjaem/Desktop/EE271/lab5/metastabilityFree.sv}
vlog -sv -work work +incdir+C:/Users/sjaem/Desktop/EE271/lab5 {C:/Users/sjaem/Desktop/EE271/lab5/victory.sv}
vlog -sv -work work +incdir+C:/Users/sjaem/Desktop/EE271/lab5 {C:/Users/sjaem/Desktop/EE271/lab5/centerLight.sv}
vlog -sv -work work +incdir+C:/Users/sjaem/Desktop/EE271/lab5 {C:/Users/sjaem/Desktop/EE271/lab5/counter.sv}
vlog -sv -work work +incdir+C:/Users/sjaem/Desktop/EE271/lab5 {C:/Users/sjaem/Desktop/EE271/lab5/LFSR.sv}
vlog -sv -work work +incdir+C:/Users/sjaem/Desktop/EE271/lab5 {C:/Users/sjaem/Desktop/EE271/lab5/computer.sv}
vlog -sv -work work +incdir+C:/Users/sjaem/Desktop/EE271/lab5 {C:/Users/sjaem/Desktop/EE271/lab5/DE1_SoC.sv}

