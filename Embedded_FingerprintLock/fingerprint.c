// Seungjae Moon, Dylan Lam
// This project implements a fingerprint lock system
// for security applications using the I2C bus for communicating
// with the fingerprint sensor, motor driver for the lock, and 
// the LCD display for the GUI.

#include <stdlib.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include "SSD2119.h"
#include "fingerprint.h"

#define GPIO_PIN_4      0x10
#define GPIO_PIN_5      0x20
#define GPIO_PIN_6      0x40
#define GPIO_PIN_7      0x80

#define STEP_ALL_OFF    (GPIODATA_C  &= ~(GPIO_PIN_4 + GPIO_PIN_5 + GPIO_PIN_6 + GPIO_PIN_7))
#define AIN1_LOW        (GPIODATA_C &= ~GPIO_PIN_4)
#define AIN1_HIGH       (GPIODATA_C |= GPIO_PIN_4)
#define BIN2_LOW        (GPIODATA_C &= ~GPIO_PIN_5)
#define BIN2_HIGH       (GPIODATA_C |= GPIO_PIN_5)
#define CIN3_LOW        (GPIODATA_C &= ~GPIO_PIN_6)
#define CIN3_HIGH       (GPIODATA_C |= GPIO_PIN_6)
#define DIN4_LOW        (GPIODATA_C &= ~GPIO_PIN_7)
#define DIN4_HIGH       (GPIODATA_C |= GPIO_PIN_7)

#define SPEED           50000
#define FULL_REV        2048
#define HALF_REV        1024
#define QUARTER_REV     512

int motorStarted = 0;  // off = 0; on = 1
int direction = 0;     // forward = 0; reverse = 1
int step = 0;          // current step 
int totalSteps = 0;    // total steps taken

state s = locked;

int main()
{ 
  LCD_Init();
  lockedDisplay();
  Timer0_Init();
  Timer1_Init(SPEED);
  Motor_Init();
  I2C_Init();
  int data;
  while(1)
  {
    data = (I2C0_Receive() - '0') + 48;
    resetTimer0();
    printf("%d\r\n", data);
    switch(s)
    {
      case locked:
        Locked_Handler(data);
        break;
          
      case unlocked:
        Unlocked_Handler(data);
        break;
        
      default:
        break;
    }
  }
  return 0;
}

// Activates the motor and LCD in the lock state
// based on the info given by the fingerprint sensor,
// then updates the state.
void Locked_Handler(int data)
{
  direction = 0;
  GPTMCTL0 |= (1<<0); // enable Timer 0
  if (data == 1) 
  {
    motorStart();
    enterDisplay("Brian.");
    s = unlocked;
  } 
  else if (data == 2) 
  {
    motorStart();
    enterDisplay("Dylan.");
    s = unlocked;
  } 
  else if (data > 2 && data < 240) 
  {
    motorStart();
    enterDisplay("Guest."); 
    s = unlocked;
  } 
  else if (data == 0) {
    deniedDisplay();
    s = locked;
  }
}

// Activates the motor and LCD in the unlock state
// based on the info given by the fingerprint sensor,
// then updates the state
void Unlocked_Handler(int data)
{
  direction = 1;
  GPTMCTL0 |= (1<<0); // enable Timer 0
  if (data == 1) 
  {
    motorStart();
    exitDisplay("Brian.");
    s = locked;
  } 
  else if (data == 2) 
  {
    motorStart();
    exitDisplay("Dylan.");
    s = locked;
  } 
  else if (data > 2 && data < 128) 
  {
    motorStart();
    exitDisplay("Guest.");
    s = locked;
  } 
  else if (data == 0) {
    deniedDisplay();
    s = unlocked;
  }
}

// set up Timer 0
void Timer0_Init(void)
{
  RCGCTIMER |= (1<<0);    // enable 16/32-bit Timer 
  GPTMCTL0 &= ~(1<<0);    // disable Timer  
  GPTMCFG0 &= ~(1<<0);    // select 32-bit mode 
  GPTMTAMR0 |= (0x2<<0);  // configure to be in periodic timer mode
  GPTMTAMR0 &= ~(1<<4);   // count down
  GPTMTAILR0 = 80000000;  // 5-sec rate (80mil)
  GPTMICR0 |= (1<<0);     // clear the bit of Timer
  NVICEN0 |= (1<<19);     // enable Timer0A interrupt
  GPTMIMR0 |= (1<<0);     // enable timer interrupt mask
}

// set up Timer 1
void Timer1_Init(int start)
{
  RCGCTIMER |= 0x2;		// enable timer 0
  GPTMCTL1 &= ~0x1;		// disable timer
  GPTMCFG1 = 0x0;		// set timer to 32-bit mode
  GPTMTAMR1 = 0x2;		// set timer to periodic mode
  GPTMTAMR1 &= ~0x10;		// set timer to count down
  GPTMTAILR1 = start;	        // load 16000000
  GPTMICR1 |= 0x1;		// clear any prior interrupt
  GPTMIMR1 |= 0x1;              // unmask interrupt
  NVICEN0 |= 0x200000;          // enable interrupt 21
}

// 5-sec timer interrupt service routine
void Timer0A_Handler( void ) 
{
  resetTimer0();
  if(s == locked)
  {
    lockedDisplay();
  } 
  else // unlocked state
  {
    unlockedDisplay();
  }
}

// Variable timer interrupt service routine
void Timer1A_Handler() 
{
  GPTMICR1 |= 0x1; // clear timer interrupt flag
  turnLock();
}

// Resets Tiemr 0A and keeps it disabled
void resetTimer0()
{
  GPTMCTL0 &= ~(1<<0);    // disable Timer 0
  GPTMICR0 |= (1<<0);     // clear the bit of Timer 0
  GPTMTAILR0 = 80000000; // reset Timer 0  
}

void I2C_Init() 
{
  // D0 = SCL, D1 = SDA
  RCGCI2C |= (1<<3); // enable I2C module 3 clock (Port D0/D1)
  RCGCGPIO |= (1<<3); // enable clock on Port D
  GPIOLOCK_D = 0x4C4F434B; // unlock
  GPIOCR_D |= (1<<0)|(1<<1); // uncommit
  GPIOAFSEL_D |= (1<<0)|(1<<1); // enable alternate function
  GPIOPCTL_D |= (3<<0)|(3<<4); // assign I2C signal to PMCn field
  GPIODEN_D |= (1<<0)|(1<<1); // enable digital output
  GPIOODR_D |= (1<<1); // enable open-drain operation for SDA
  
  I2CMCS3 = 0;
  I2CMCR3 = 0x20; // enable slave mode
  I2CSOAR3 = 0x08; // add TIVA as slave
  I2CSCSR3 = 1; // enable receive function
}

// receive function
unsigned char I2C0_Receive()
{
  while((I2CSCSR3 & (1<<0)) == 0);
  return I2CSDR3;
}

// set up motor
void Motor_Init() 
{
  volatile unsigned long delay;
  RCGCGPIO |= 0x4;
  delay = RCGCGPIO;
  GPIODIR_C |= 0xF0;
  GPIODEN_C |= 0xF0;
  GPIODR8R_C |= 0xF0;
}

// Start motor
void motorStart()
{
  motorStarted = 1;
  totalSteps = 0;
  Timer1_Init(SPEED);
  GPTMCTL1 |= 0x1;
}

// Stop motor
void motorStop()
{
  motorStarted = 0;
  totalSteps = 0;
  GPTMCTL1 &= ~0x1;
  GPTMTAILR1 = SPEED;
  STEP_ALL_OFF;
}

// Turn lock based on fingerprint protocol
void turnLock()
{
  if ((motorStarted == 0) || (totalSteps == QUARTER_REV))
  {
    motorStop();
    totalSteps = 0;
  } 
  else if (motorStarted == 1)
  {
    totalSteps++;
    if (direction == 0)
    {
      step++;
      if (step > 3) step = 0;
    }
    else
    {
      step--;
      if (step < 0) step = 3;
    }
    switch(step)
    { 
      case 0:
             AIN1_HIGH;
             BIN2_LOW;
             CIN3_LOW;
             DIN4_HIGH;
             break;
      case 1:
             AIN1_HIGH;
             BIN2_HIGH;
             CIN3_LOW;
             DIN4_LOW;     
             break;
      case 2:
             AIN1_LOW;
             BIN2_HIGH;
             CIN3_HIGH;
             DIN4_LOW;    
             break; 
      case 3:
             AIN1_LOW;
             BIN2_LOW;
             CIN3_HIGH;
             DIN4_HIGH;     
             break;
      default: break;
    }
  }
}

// The following display functions output the
// appropriate messages for the given state to the user:
// TODO: switch to activate the LCD display
void initialDisplay()
{
  LCD_ColorFill(convertColor(0, 0, 0)); // black background  
} 

void deniedDisplay() 
{
  LCD_ColorFill(convertColor(0xAA, 0, 0)); // red background
  LCD_SetCursor(0, 0);
  LCD_PrintString("Access Denied. Try again.");
}

void enterDisplay(char* user)
{
  LCD_ColorFill(convertColor(0, 0xAA, 0)); // green background
  LCD_SetCursor(0, 0);
  char* str = "Access Granted. Welcome, ";
  char* msg = (char*)malloc(1 + strlen(str) + strlen(user));
  strcpy(msg, str);
  strcat(msg, user);
  LCD_PrintString(msg);
}

void exitDisplay(char* user)
{
  LCD_ColorFill(convertColor(0, 0xAA, 0)); // green background
  LCD_SetCursor(0, 0);
  char* str = "Access Granted. Goodbye, ";
  char* msg = (char*)malloc(1 + strlen(str) + strlen(user));
  strcpy(msg, str);
  strcat(msg, user);
  LCD_PrintString(msg);
}

void lockedDisplay()
{
  LCD_ColorFill(convertColor(0xAA, 0xAA, 0xAA)); // white background
  LCD_SetCursor(0, 0);
  LCD_PrintString("Scan your finger to unlock.");
}

void unlockedDisplay()
{
  LCD_ColorFill(convertColor(0xAA, 0xAA, 0xAA)); // white background
  LCD_SetCursor(0, 0);
  LCD_PrintString("Scan your finger to lock.");
}


  
