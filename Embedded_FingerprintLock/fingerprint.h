/*
 * Seungjae Moon, Dylan Lam
 * This header file contains the required addresses 
 * for implementing the fingerprint sensor and a stepping motor.
 */

#ifndef FINGERPRINT_H
#define FINGERPRINT_H

// struct for state
typedef enum {
  locked,
  unlocked
} state;
   
// function prototype
void initialDisplay();
void Timer0_Init();
void Timer1_Init(int start);
void resetTimer0();
void I2C_Init();
unsigned char I2C0_Receive();
void Locked_Handler(int data);
void Unlocked_Handler(int data);
void initialDisplay();
void deniedDisplay();
void enterDisplay();
void exitDisplay();
void lockedDisplay();
void unlockedDisplay();

void Motor_Init(void);
void motorStart(void);
void motorStop(void);
void turnLock(void);

// GPIO Registers
#define RCGCGPIO (*((volatile uint32_t *)0x400FE608))
// GPIO Port A
#define GPIOPCTL_A (*((volatile uint32_t *)0x4000452C))
#define GPIOAMSEL_A (*((volatile uint32_t *)0x40004528))
#define GPIOAFSEL_A (*((volatile uint32_t *)0x40004420))
#define GPIODIR_A (*((volatile uint32_t *)0x40004400))
#define GPIODEN_A (*((volatile uint32_t *)0x4000451C))
#define GPIODATA_A (*((volatile uint32_t *)0x400043FC))
// GPIO Port E
#define GPIOPCTL_D (*((volatile uint32_t *)0x4000752C))
#define GPIOAFSEL_D (*((volatile uint32_t *)0x40007420))
#define GPIODEN_D (*((volatile uint32_t *)0x4000751C))
#define GPIOLOCK_D (*((volatile uint32_t *)0x40007520))
#define GPIOCR_D (*((volatile uint32_t *)0x40007524))
#define GPIOODR_D (*((volatile uint32_t *)0x4000750C))
// GPIO Port C
#define GPIODIR_C (*((volatile unsigned long *)0x40006400))
#define GPIODEN_C (*((volatile unsigned long *)0x4000651C))
#define GPIODR8R_C (*((volatile unsigned long *)0x40006508))    
#define GPIODATA_C (*((volatile unsigned long *)0x400063FC)) 

// I2C Registers
#define RCGCI2C (*((volatile uint32_t *)0x400FE620))
#define I2CMCS3 (*((volatile uint32_t *)0x40023004))
#define I2CMCR3 (*((volatile uint32_t *)0x40023020))
#define I2CSOAR3 (*((volatile uint32_t *)0x40023800))
#define I2CSCSR3 (*((volatile uint32_t *)0x40023804))
#define I2CSDR3 (*((volatile uint32_t *)0x40023808))

// Timer Registers
#define RCGCTIMER (*((volatile uint32_t *)0x400FE604))
// Timer 0 Registers
#define GPTMCTL0 (*((volatile uint32_t *) 0x4003000C))
#define GPTMCFG0 (*((volatile uint32_t *) 0x40030000))
#define GPTMTAMR0 (*((volatile uint32_t *) 0x40030004))
#define GPTMTAILR0 (*((volatile uint32_t *) 0x40030028))
// Timer 1 Registers
#define GPTMCTL1 (*((volatile unsigned long *)0x4003100C))
#define GPTMCFG1 (*((volatile unsigned long *)0x40031000))
#define GPTMTAMR1 (*((volatile unsigned long *)0x40031004))
#define GPTMTAILR1 (*((volatile unsigned long *)0x40031028))

// Interrupt Registers
#define NVICEN0 (*((volatile uint32_t *)0xE000E100))
#define GPIOIM_A (*((volatile uint32_t *) 0x40004410))
#define GPIOMIS_A (*((volatile uint32_t *) 0x40004418))
#define GPIOICR_A (*((volatile uint32_t *) 0x4000441C))
#define GPTMIMR0 (*((volatile uint32_t *) 0x40030018))
#define GPTMRIS0 (*((volatile uint32_t *) 0x4003001C))
#define GPTMICR0 (*((volatile uint32_t *) 0x40030024))
#define GPTMIMR1 (*((volatile uint32_t *) 0x40031018))
#define GPTMRIS1 (*((volatile uint32_t *) 0x4003101C))
#define GPTMICR1 (*((volatile uint32_t *) 0x40031024))
#define PRI7 (*((volatile unsigned long *)0xE000E41C))

#endif // FINGERPRINT_H