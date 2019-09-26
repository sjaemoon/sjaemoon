/*
 * Seungjae Moon
 * EE 474 Lab 2
 * This program communicates with the TIVA Board to
 * turn on the appropriate LEDS based on the time interrupt and the
 * switch interrupt from the user to simulate a traffic controller.
 */

#include "traffic_controller.h"
#include <stdint.h>
state s = stop;

int main()
{ 
  LED_init(); 
  sw_init();
  timer_init();
  while(1) 
  {
    // power or pedestrian pressed
    if(GPIODATA_A & 0x04 || GPIODATA_A & 0x08 && s != warn)
    {
      GPTMCTL1 |= (1<<0); // enable Timer 1
    } 
    else
    {
      GPTMCTL1 &= ~(1<<0); // disable Timer 1
      GPTMICR1 |= (1<<0); // clear the bit of Timer 1
      GPTMTAILR1 = 0x1E84800; // reset 2-sec timer 
    }
  }
  return 0;
}

// set up the LED
void LED_init(void) 
{
  RCGCGPIO = 0x01; // enables port A GPIO
  volatile unsigned long delay;
  RCGCGPIO |= 0x01;          // activate clock for Port A 
  delay = RCGCGPIO;          // allow time for clock to start
  GPIOPCTL_A &= ~0xFFF00000; // PCTL GPIO on PA5,6,7
  GPIOAMSEL_A &= ~0xE0;      // disable analog function of PA5,6,7
  GPIODIR_A |= 0xE0;         // set PA5,6,7 to output
  GPIOAFSEL_A &= ~0xE0;      // regular port function
  GPIODEN_A |= 0xE0;         // enable digital output on PA5,6,7
}

// set up the switch
void sw_init(void) 
{
  volatile unsigned long delay;
  RCGCGPIO |= 0x01;          // activate clock for Port A
  delay = RCGCGPIO;          // allow time for clock to start
  GPIOPCTL_A &= ~0x0000FF00; // PCTL GPIO on PA2,3
  GPIOAMSEL_A &= ~0x0C;      // disable analog function of PA2,3
  GPIODIR_A &= ~0x0C;        // set P2,3 to input
  GPIOAFSEL_A &= ~0x0C;      // regular port function
  GPIODEN_A |= 0x0C;         // enable digital output on PA2,3
}

// set up Timer 0,1
void timer_init(void)
{
  RCGCTIMER |= (1<<0);    // enable 16/32-bit Timer 
  RCGCTIMER |= (1<<1);    
  GPTMCTL0 &= ~(1<<0);    // disable Timer 
  GPTMCTL1 &= ~(1<<0);    
  GPTMCFG0 &= ~(1<<0);    // select 32-bit mode
  GPTMCFG1 &= ~(1<<0);    
  GPTMTAMR0 |= (0x2<<0);  // configure to be in periodic timer mode
  GPTMTAMR1 |= (0x2<<0);
  GPTMTAMR0 &= ~(1<<4);   // count down
  GPTMTAMR1 &= ~(1<<4);
  GPTMTAILR0 = 0x4C4B400; // 5-sec blink rate (80mil)
  GPTMTAILR1 = 0x1E84800; // 2-sec blink rate (32mil)
  GPTMICR0 |= (1<<0);     // clear the bit of Timer
  GPTMICR1 |= (1<<0); 
  NVICEN0 |= (1<<19);     // enable Timer0A interrupt
  NVICEN0 |= (1<<21);     // enable Timer1A interrupt
  GPTMIMR0 |= (1<<0);     // enable timer interrupt mask
  GPTMIMR1 |= (1<<0);     // enable timer interrupt mask
  GPTMCTL0 |= (1<<0);     // enable Timer 0
}

// 2-sec timer interrupt service routine
void Timer1A_Handler( void ) 
{
  GPTMCTL1 &= ~(1<<0); // disable Timer 1
  GPTMICR1 |= (1<<0); // clear the bit of Timer 1
  if (GPIODATA_A & 0x04 && s == off) // power pressed when off
  {
    s = stop;
    stop_LED();
    GPTMCTL0 |= (1<<0); // enable Timer 0
    
  }
  else if (GPIODATA_A & 0x04 && s != off) // power pressed when on
  {
    s = off;
    turn_off();
    GPTMCTL0 &= ~(1<<0); // disable Timer 0
    GPTMICR0 |= (1<<0); // clear the bit of Timer 0
    GPTMTAILR0 = 0x4C4B400; // reset Timer 0
    
  }
  else if (GPIODATA_A & 0x08 && s != stop) // pedestrian pressed in the go state
  {
    s = warn;
    warn_LED();
    GPTMCTL0 &= ~(1<<0);    // disable Timer 0
    GPTMICR0 |= (1<<0);     // clear the bit of Timer 0
    GPTMTAILR0 = 80000000; // reset Timer 0
    GPTMCTL0 |= (1<<0);     // enable Timer 0
  }
}

// 5-sec timer interrupt service routine
void Timer0A_Handler( void ) 
{
  GPTMICR0 |= (1<<0); // clear the bit of Timer 0
  GPTMCTL1 &= ~(1<<0); // disable Timer 1
  GPTMICR1 |= (1<<0); // clear the bit of Timer 1
  GPTMTAILR1 = 0x1E84800; // reset Timer 1
  if (s == go || s == warn) 
  { 
    s = stop; 
    stop_LED(); 
  }
  else if (s == stop) 
  { 
    s = go; 
    go_LED(); 
  }   
}

// turn off all LED connected to Port A
void turn_off(void) 
{
  GPIODATA_A &= ~0x80;
  GPIODATA_A &= ~0x40;
  GPIODATA_A &= ~0x20;
}

// turn on ONLY the red LED
void stop_LED(void) 
{
  GPIODATA_A |= 0x80;
  GPIODATA_A &= ~0x40;
  GPIODATA_A &= ~0x20;
}

// turn on ONLY the yellow LED
void warn_LED(void) 
{
  GPIODATA_A &= ~0x80;
  GPIODATA_A |= 0x40;
  GPIODATA_A &= ~0x20;
}

// turn on ONLY the green LED
void go_LED(void) 
{
  GPIODATA_A &= ~0x80;
  GPIODATA_A &= ~0x40;
  GPIODATA_A |= 0x20;
}

