/*
 * Seungjae Moon
 * EE 474 Lab 2
 * This header file contains the enumerated type for 
 * the states of traffic along with required addresses 
 * for simulating the traffic controller using timers.
 */

#ifndef TRAFFIC_CONTROLLER_H
#define TRAFFIC_CONTROLLER_H

typedef enum {
  go,
  warn,
  stop,
  off
} state;

void LED_init(void);
void sw_init(void);
void timer_init(void);
void turn_off(void);
void stop_LED(void);
void warn_LED(void);
void go_LED(void);
int sys_timeout(void);
int ped_timeout(void);
int base_timeout(void);
void reset_5sec_timer(void);
void reset_2sec_timer(void);
state stop_event_handler(state s);
state go_event_handler(state s);
state warn_event_handler(state s);
state off_event_handler(state s);

// GPIO Registers
#define RCGCGPIO (*((volatile uint32_t *)0x400FE608))
#define GPIOPCTL_A (*((volatile uint32_t *)0x4000452C))
#define GPIOAMSEL_A (*((volatile uint32_t *)0x40004528))
#define GPIOAFSEL_A (*((volatile uint32_t *)0x40004420))
#define GPIODIR_A (*((volatile uint32_t *)0x40004400))
#define GPIODEN_A (*((volatile uint32_t *)0x4000451C))
#define GPIODATA_A (*((volatile uint32_t *)0x400043FC))

// Timer 0 Registers
#define RCGCTIMER (*((volatile uint32_t *)0x400FE604))
#define GPTMCTL0 (*((volatile uint32_t *) 0x4003000C))
#define GPTMCFG0 (*((volatile uint32_t *) 0x40030000))
#define GPTMTAMR0 (*((volatile uint32_t *) 0x40030004))
#define GPTMTAILR0 (*((volatile uint32_t *) 0x40030028))

// Timer 1 Registers
#define GPTMCTL1 (*((volatile uint32_t *) 0x4003100C))
#define GPTMCFG1 (*((volatile uint32_t *) 0x40031000))
#define GPTMTAMR1 (*((volatile uint32_t *) 0x40031004))
#define GPTMTAILR1 (*((volatile uint32_t *) 0x40031028))

// Interrupt Registers
#define NVICEN0 (*((volatile uint32_t *)0xE000E100))
#define PRI0 (*((volatile uint32_t *)0xE000E400))
#define PRI4 (*((volatile uint32_t *)0xE000E410))
#define PRI5 (*((volatile uint32_t *)0xE000E414))
#define GPIOIM_A (*((volatile uint32_t *) 0x40004410))
#define GPIOMIS_A (*((volatile uint32_t *) 0x40004418))
#define GPIOICR_A (*((volatile uint32_t *) 0x4000441C))
#define GPTMIMR0 (*((volatile uint32_t *) 0x40030018))
#define GPTMRIS0 (*((volatile uint32_t *) 0x4003001C))
#define GPTMICR0 (*((volatile uint32_t *) 0x40030024))
#define GPTMIMR1 (*((volatile uint32_t *) 0x40031018))
#define GPTMRIS1 (*((volatile uint32_t *) 0x4003101C))
#define GPTMICR1 (*((volatile uint32_t *) 0x40031024))

#endif // TRAFFIC_CONTROLLER_H