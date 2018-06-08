#include <SoftwareSerial.h>
SoftwareSerial BT(0, 1); 
int water, clean, fb1, fb2, fb3;
int servoAngle=1500;
char serialData;
void setup()  
{
  // set digital pin to control as an output
  pinMode(2, OUTPUT);
  pinMode(7, INPUT);
  pinMode(8, INPUT);
  pinMode(9, INPUT);
  pinMode(10, INPUT);
  pinMode(11, INPUT);
  pinMode(13, OUTPUT);
  // set the data rate for the SoftwareSerial port
  BT.begin(9600);
  // Send test message to other device
  BT.println(0); // Initiate Table
}
char a; // stores incoming character from other device
void loop() 
{
  water = digitalRead(7);
  clean = digitalRead(8);
  fb1 = digitalRead(9);
  fb2 = digitalRead(10);
  fb3 = digitalRead(11);
   

    if(!water)
  {
    BT.println(4);  //  For Water
    serialData = 1;
    delay(500);
  }
  else if(!clean)
  {
    BT.println(5);  //  For Cleaning table
    serialData = 0;
    delay(500);
  }
  else if(!fb1)
  {
    BT.println(1);  //  FeedBack 1
    delay(500);
  }
  else if(!fb2)
  {
    BT.println(2);  //  FeedBack 2
    delay(500);
  }
  else if(!fb3)
  {
    BT.println(3);  //  FeedBack 3
    delay(500);
  }
   
  if (serialData == 0)
    servoAngle = 2500;
  else if (serialData == 1)
    servoAngle = 700;
  digitalWrite(2, HIGH);
  delayMicroseconds(servoAngle);
  digitalWrite(2, LOW);
  delay(15);
  if (BT.available())
  // if text arrived in from BT serial...
  {
    a=(BT.read());
    if (a=='1')
    {
      digitalWrite(13, HIGH);
      BT.println(6); //  LED on
    }
    if (a=='2')
    {
      digitalWrite(13, LOW);
      BT.println(7);  //LED off
    }
    if (a=='0')
    {
      BT.println(8);  // Send '1' to turn LED on & Send '2' to turn LED off
    }
    if (a=='.')
    {
      BT.println(9);  // Send '1' to turn LED on & Send '2' to turn LED off
    }
    // you can add more "if" statements with other characters to add more commands
  }
  
}
