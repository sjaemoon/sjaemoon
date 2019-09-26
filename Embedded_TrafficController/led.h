/*
 * Seungjae Moon
 * EE 474 Lab 2
 * This header file contains macro definition for 
 * the LED colors and the required addresses for 
 * operating the LEDs, swithces, and timers on the TIVA board.
 */

#ifndef LED_H
#define LED_H

#define RED 0x2
#define BLUE 0x4
#define PURPLE 0x6 // red, blue
#define GREEN 0x8
#define YELLOW 0xA // red, green
#define CYAN 0xC // blue, green
#define WHITE  0x0E // red, blue, green
#define SIZE 7 // number of colors

void colors_init();
void GPIO_F_init();
void timer_init();
void cycle_colors(int colors[], int* i);

// GPIO Registers 
#define RCGCGPIO (*((volatile uint32_t *)0x400FE608))
#define GPIODIR_F (*((volatile uint32_t *)0x40025400))
#define GPIODEN_F (*((volatile uint32_t *)0x4002551C))
#define GPIODATA_F (*((volatile uint32_t *)0x400253FC))

#define GPIOLOCK_F (*((volatile uint32_t *)0x40025520))
#define GPIOCR_F (*((volatile uint32_t *)0x40025524))
#define GPIOPUR_F (*((volatile uint32_t *)0x40025510))

// Timer Registers
#define RCGCTIMER (*((volatile uint32_t *)0x400FE604))
#define GPTMCTL (*((volatile uint32_t *) 0x4003000C))
#define GPTMCFG (*((volatile uint32_t *) 0x40030000))
#define GPTMTAMR (*((volatile uint32_t *) 0x40030004))
#define GPTMTAILR (*((volatile uint32_t *) 0x40030028))

// Interrupt Registers
#define NVICEN0 (*((volatile uint32_t *) 0xE000E100))
#define GPIOIM_F (*((volatile uint32_t *) 0x40025410))
#define GPIOMIS_F (*((volatile uint32_t *) 0x40025418))
#define GPIOICR_F (*((volatile uint32_t *) 0x4002541C))
#define GPTMIMR (*((volatile uint32_t *) 0x40030018))
#define GPTMRIS (*((volatile uint32_t *) 0x4003001C))
#define GPTMICR (*((volatile uint32_t *) 0x40030024))

#endif // LED_H