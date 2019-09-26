/*
 * Seungjae Moon
 * EE 474 Lab 2
 * This program communicates with the TIVA Board to
 * emit a red of blinking blue LEDS based on the
 * interrupt of the switches.
 */

#include "led.h"
#include <stdint.h>

int main() {
  GPIO_F_init();
  timer_init();
  while(1) 
  {
  }
  return 0;
}

// sets up Port F of the GPIO
void GPIO_F_init()
{
  RCGCGPIO = (1<<5);       // enable port F GPIO 
  GPIOLOCK_F = 0x4C4F434B; // unlocks GPIOCR for modification
  GPIOCR_F = 0x11;         // enables write for GPIOPUR
  GPIOPUR_F = 0x11;        // enables pull-up resistor
  GPIODIR_F = 0x0E;        // sets the switches as inputs, and leds as outputs
  GPIODEN_F = 0x1F;        // enables pins 0-4
  GPIOICR_F |= (1<<0);     // clear GPIO interrupt
  NVICEN0 |= (1<<30);      // enable GPIO interrupt
  GPIOIM_F = 0x11;         // enable interrupt mask for SW1(0x10) and SW2(0x1)
}

// sets up the timer
void timer_init()
{
  RCGCTIMER |= (1<<0);  // enable 16/32-bit Timer 0
  GPTMCTL &= ~(1<<0);   // disable Timer A
  GPTMCFG &= ~(1<<0);   // select 32-bit mode
  GPTMTAMR |= (0x2<<0); // configure to be in periodic timer mode
  GPTMTAMR &= ~(1<<4);  // count down
  GPTMTAILR = 0xF42400; // achieve a 1Hz blink rate using the 16 MHz oscillator
  GPTMICR |= (1<<0);    // clear timer interrupt
  NVICEN0 |= (1<<19);   // enable timer interrupt
  GPTMIMR |= (1<<0);    // enable timer interrupt mask
  GPTMCTL |= (1<<0);    // enable Timer A
}

// handle interrupt from the timer
void Timer0A_Handler( void )
{
  GPTMICR |= (1<<0); // clear the flag
  GPIODATA_F ^= BLUE; // blink blue LED
}

// handle interrupt from the switches
void PortF_Handler( void )
{
  if (GPIOMIS_F & 1<<0) // SW2 pressed
  {
    GPIOICR_F |= 1<<0; // clear interrupt for SW2
    GPTMIMR &= ~(1<<0); // disable timer interrupt
    GPIODATA_F = RED; // turn on red LED
  } 
  else if (GPIOMIS_F & 1<<4) // SW1 pressed
  {
    GPIOICR_F |= 1<<4; // clear interrupt for SW1
    GPTMIMR |= 1<<0; // enable timer interrupt
    GPIODATA_F &= ~RED; // turn off red LED
  }
}

