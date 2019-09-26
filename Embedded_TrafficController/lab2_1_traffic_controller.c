/*
 * Seungjae Moon
 * EE 474 Lab 2
 * This program communicates with the TIVA Board to
 * turn on the appropriate LEDS based on the time and the
 * switch input from the user to simulate a traffic controller.
 */

#include "traffic_controller.h"
#include <stdint.h>

int main()
{ 
  LED_init(); 
  sw_init();
  timer_init();
  state s = stop;
  // check for either button press every iteration
  while(1) 
  {
    switch(s) 
    { 
      case stop:
        stop_LED();
        s = stop_event_handler(s);
        break;
      
      case go:
        go_LED();
        s = go_event_handler(s);
        break;
        
      case warn:
        warn_LED();
        s = warn_event_handler(s);
        break;
        
      case off:
        turn_off();
        s = off_event_handler(s);
        break;
        
      default:
        break;
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
  GPTMICR0 |= (1<<0);     // clear the bit of Timer
  GPTMICR1 |= (1<<0); 
  GPTMCFG0 &= ~(1<<0);    // select 32-bit mode
  GPTMCFG1 &= ~(1<<0);    
  GPTMTAMR0 |= (0x2<<0);  // configure to be in periodic timer mode
  GPTMTAMR1 |= (0x2<<0);
  GPTMTAMR0 &= ~(1<<4);   // count down
  GPTMTAMR1 &= ~(1<<4);
  GPTMTAILR0 = 0x4C4B400; // 5-sec blink rate (80mil)
  GPTMTAILR1 = 0x1E84800; // 2-sec blink rate (32mil)
  GPTMCTL0 |= (1<<0);     // enable Timer 0
}

// check if any switches are pressed and handles the behavior
// when the traffic controller is in the "go" state
state go_event_handler(state s)
{
  if (sys_timeout())
  {
    reset_2sec_timer();
    reset_5sec_timer();
    return off;
  }
  if (ped_timeout()) 
  {
    reset_2sec_timer();
    reset_5sec_timer();
    GPTMCTL0 |= (1<<0); // enable Timer 0
    return warn;
  }
  if (base_timeout()) 
  {
    GPTMICR0 |= (1<<0); // clear the bit of Timer 0
    return stop;
  }
  else 
  {
    return go;
  } 
}

// check if any switches are pressed and handles the behavior
// when the traffic controller is in the "warn" state
state warn_event_handler(state s)
{
  if (sys_timeout()) 
  {
    reset_2sec_timer();
    reset_5sec_timer();
    return  off;
  }
  if (base_timeout()) 
  {
    GPTMICR0 |= (1<<0); // clear the bit of Timer 0
    return stop;
  } 
  else
  {
    return warn;
  }
}

// check if any switches are pressed and handles the behavior
// when the traffic controller is in the "stop" state
state stop_event_handler(state s)
{
  if (sys_timeout())
  {
    reset_2sec_timer();
    reset_5sec_timer();
    return off;
  }
  if (base_timeout()) 
  {
    GPTMICR0 |= (1<<0); // clear the bit of Timer 0
    return go;
  }
  else {
    return stop;
  }
}

// check if any switches are pressed and handles the behavior
// when the traffic controller is in the "off" state
state off_event_handler(state s)
{
  if (sys_timeout()) {
    reset_2sec_timer();
    GPTMCTL0 |= (1<<0); // enable Timer 0
    return stop;
  }
  else
  {
    return off;
  }
}

// check if the 2-sec timer timed out 
// while pressing the system power switch
int sys_timeout(void) 
{
  while (GPIODATA_A & 0x04) // power pressed
  {
    GPTMCTL1 |= (1<<0); // enable Timer 1
    if (base_timeout()) 
    {
      return 0;
    }
    if (GPTMRIS1 & (1<<0)) // Timer 0 timed out
    {
      return 1;
    }
  }
  reset_2sec_timer();
  return 0;
}

// check if the 2-sec timer timed out 
// while pressing the pedestrian switch
int ped_timeout(void) 
{
  while (GPIODATA_A & 0x08) // pedestrian pressed
  {
    GPTMCTL1 |= (1<<0); // enable Timer 1
    if (base_timeout()) 
    {
      return 0;
    }
    if (GPTMRIS1 & (1<<0)) 
    {
      return 1;
    }
  }
  reset_2sec_timer();
  return 0;
}

// check if base 5-sec timer timed out
int base_timeout(void)
{
  return (GPTMRIS0 & (1<<0)); 
}

// reset 5-sec timer
void reset_5sec_timer() {
  GPTMCTL0 &= ~(1<<0); // disable Timer 0
  GPTMICR0 |= (1<<0); // clear the bit of Timer 0
  GPTMTAILR0 = 0x4C4B400; // reset 5-sec timer
}

// reset 2-sec timer
void reset_2sec_timer() {
  GPTMCTL1 &= ~(1<<0); // disable Timer 1
  GPTMICR1 |= (1<<0); // clear the bit of Timer 1
  GPTMTAILR1 = 0x1E84800; // reset 2-sec timer 
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

