/*
 * Seungjae Moon
 * EE 474 Lab 2
 * This program communicates with the TIVA Board to
 * emit a range of color combinations at a specific time
 * interval using the General Purpsoe Timer Module.
 */

#include "led.h"
#include <stdint.h>

int main() {
  int colors[SIZE];
  colors_init(colors);
  GPIO_F_init();
  timer_init();
  int i = 0;
  while(1)
  {
    cycle_colors(colors, &i);
  }
  return 0;
}

// set up the color cycle
void colors_init(int colors[]) 
{
  *colors = RED;
  *(colors + 1) = BLUE;
  *(colors + 2) = PURPLE;
  *(colors + 3) = GREEN;
  *(colors + 4) = YELLOW;
  *(colors + 5) = CYAN;
  *(colors + 6) = WHITE;
}

// set up Port F of the GPIO
void GPIO_F_init()
{
  RCGCGPIO = (1<<5); // enable port F GPIO 
  GPIODIR_F = 0xE;   // set PF1,2,3 as output   
  GPIODEN_F = 0xE;   // enable digital pin PF1,2,3
  GPIODATA_F = 0x0;  // start with no color
}

// set up the timer
void timer_init()
{
  RCGCTIMER |= (1<<0);  // enable 16/32-bit Timer 0
  GPTMCTL &= ~(1<<0);    // disable Timer A
  GPTMCFG &= ~(1<<0);    // select 32-bit mode
  GPTMTAMR |= (0x2<<0); // configure to be in periodic timer mode
  GPTMTAMR &= ~(1<<4);   // count down
  GPTMTAILR = 0xF42400; // achieve a 1Hz blink rate using the 16 MHz oscillator
  GPTMCTL |= (1<<0);    // enable Timer A
}

// cycle through the colors
void cycle_colors(int colors[], int* i)
{
  if(GPTMRIS & (1<<0)) // if Timer A has timed out
  {
    GPTMICR |= (1<<0); // clear the bit
    GPIODATA_F = colors[*i]; // set the color
    *i = (*i + 1) % SIZE; // reset index after cycling through all colors
  }
}