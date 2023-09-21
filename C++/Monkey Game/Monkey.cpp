#include <iostream>
#include <windows.h>
#include <string>
#include <thread>
#include <chrono>
#include "wtypes.h"
#include <fstream>


int MonkeyPos[2] = {960, 540}; // Position of monkey that windows works with, also known as the top left of the window
int MonkeyPos2[2] = {960, 540}; // Bottom right corner of the monkey windows, which is used for collision detection
int MonkeyMiddle[2] = {116, 109};
int BananaPos[2] = {400, 400};
int BananaPos2[2] = {566, 580};


int length;
int LastDash = 0;
std::string CurDirectory;
std::string Monkeypath;
std::string Bananapath;
uint64_t LastTime = 0;
std::string MonkeyString;
std::string strX;
std::string strY;
int MoveIncriment = 25;
int horizontal = 0;
int vertical = 0;
int BananaTotal = 0;


void GetDesktopResolution(int& horizontal, int& vertical)
{
   RECT desktop;
   // Get a handle to the desktop window
   const HWND hDesktop = GetDesktopWindow();
   // Get the size of screen to the variable desktop
   GetWindowRect(hDesktop, &desktop);
   // The top left corner will have coordinates (0,0)
   // and the bottom right corner will have coordinates
   // (horizontal, vertical)
   horizontal = desktop.right;
   vertical = desktop.bottom;
}

uint64_t timeSinceEpochMillisec() {
  using namespace std::chrono;
  return duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
}

void MkMonkey(){
	
	std::string MStart = "Frameless.exe \"Monkey.png\" aot=yes noresize=yes trans=alpha x=" + std::to_string(MonkeyPos[0]) +  " y=" + std::to_string(MonkeyPos[1]);
	
	const char *MStartChar = MStart.c_str();
	
	system(MStartChar);
	
	std::cout << MStartChar;
	
}

void MkBanana(){
	
	system("Frameless.exe \"Banana.png\" aot=yes noresize=yes trans=alpha x=400 y=400");
	
}


int points = 0;

bool TouchBanana(){
	//Banana is 166x180
	//This checks to see if each side of monkey is past the opposite side of banana. If both x and y are, then there is a collision

	if((MonkeyPos[0] <= BananaPos2[0]) && (MonkeyPos2[0] >= BananaPos[0])){ //Logicaly these should be or's but and's make it work fucking somehow
		points++;
		//printf("X INTERSECT\n");
	}

	if((MonkeyPos[1] <= BananaPos2[1]) && (MonkeyPos2[1] >= BananaPos[1])){
		points++;
		//printf("Y INTERSECT\n");
	}

	if(points > 1){
		points = 0;
		return 1;
	}

	points = 0;

	return 0; // Zero means banana was not touched
}

void Virus(){

	//This is the part where you could potentially put code for a virus to run on a separate thread

}


int main(){	

	std::ifstream cmdow;
	cmdow.open("cmdow.exe");

	if(!cmdow){
		printf("Please allow cmdow access through Windows Security, it is a false flag and the game won't work without it\nIt most likely did it quietyly so make sure to check");
		Sleep(10000);
		return 0;
	}

	srand(time(NULL));
	GetDesktopResolution(horizontal, vertical);
   	std::cout << horizontal << "x" << vertical << '\n';
	
	MonkeyPos[0] = horizontal / 2;
	MonkeyPos[1] = vertical / 2;
	
	std::thread MonPNG(MkMonkey);
	std::thread BanPNG(MkBanana);
	
	MonPNG.detach(); //Makes the PNGs a seperate thing so that the compiler doesn't through a fit over not joining the threads
	BanPNG.detach();
	
    TCHAR buffer[MAX_PATH] = { 0 };
	length = GetModuleFileName( NULL, buffer, MAX_PATH ); //Gets the directory of the exe and sets 'length' to the length of that string
	
	for(int d = 0; d < length; d++) //Goes through the buffer and finds the last '\' within it
		if(buffer[d] == '\\')
			LastDash = d;
	
	CurDirectory = buffer;
	
	for(int i = 0; i < (length - LastDash) - 1; i++) //Gets rid of the .exe part, leaving the path 
		CurDirectory.pop_back();

	//std::cout << CurDirectory;
	
	Monkeypath = "cmdow \"Frameless - " + CurDirectory + "Monkey.png\" /ren Monkey"; // CMDOW IS STILL NEEDED FOR THIS, FIND IN SOURCE CODE HOW THIS IS DONE SO CMDOW NOT NEEDED
	Bananapath = "cmdow \"Frameless - " + CurDirectory + "Banana.png\" /ren Banana";
	
	const char* print = Monkeypath.c_str();
	system(print);

	const char* print2 = Bananapath.c_str();
	system(print2);
	
	while(true){
		
		//Runs the game and all checks at 20hz (cmdow slows it though);
		Sleep(25);
			
			LastTime = timeSinceEpochMillisec();

			if(TouchBanana()){
				while(TouchBanana()){// Makes sure new position does not colide with player
					BananaPos[0] = rand() % (horizontal - 166);
					BananaPos[1] = rand() % (vertical - 180);

					BananaPos2[0] = BananaPos[0] + 166;
					BananaPos2[1] = BananaPos[1] + 180;
				}
				MoveWindow(FindWindow(NULL, "Banana"), BananaPos[0], BananaPos[1], 854, 480, false);
				BananaTotal++;
				PlaySoundA("BananaGet.wav", NULL, SND_FILENAME | SND_ASYNC);
			}



			
			if(GetKeyState(VK_SHIFT) & 0x8000)
				MoveIncriment = 0.0111 * vertical; //Speed is changed depending on screen size
			else
				MoveIncriment = 0.00555 * vertical;
			
			//Monkey Move Checks
			if(GetKeyState('W') & 0x8000)
				MonkeyPos[1] -= MoveIncriment; //Backwords cause up is down to Windows
			
			if(GetKeyState('S') & 0x8000)
				MonkeyPos[1] += MoveIncriment; //Backwords cause down is up to Windows
			
			if(GetKeyState('A') & 0x8000)
				MonkeyPos[0] -= MoveIncriment;
		
			if(GetKeyState('D') & 0x8000)
				MonkeyPos[0] += MoveIncriment;
			//End Monkey Move Checks
			
			//Checks to keep monkey in bounds
			for(int i = 0; i != 2; i++)//Bottom Check
				if(MonkeyPos[i] < 0)
					MonkeyPos[i] = 2; //std::to_string rounds down to this needs to be two instead of 1
			

			MonkeyPos2[0] = MonkeyPos[0] + 220;//updates the other monkey position
			MonkeyPos2[1] = MonkeyPos[1] + 199;

			if(MonkeyPos2[0] > 2560)
				MonkeyPos[0] = 2340;

			if(MonkeyPos2[1] > 1440)
				MonkeyPos[1] = 1241;



			/*
			if(MonkeyPos[0] > horizontal - 220) // ASSUMES SIZE OF PLAYER PNG
				MonkeyPos[0] = horizontal - 220;
			
			if(MonkeyPos[1] > vertical - 199) // ASSUMES SIZE OF PLAYER PNG
				MonkeyPos[1] = vertical - 199;
			*/
				
			//std::cout << MonkeyPos[0] << ", " << MonkeyPos[1] << std::endl;
			
			MonkeyMiddle[0] = MonkeyPos[0] + 116; //Legacy code, middle position not used currently
			MonkeyMiddle[1] = MonkeyPos[1] + 109;
			
			std::cout << BananaTotal << ": " << MonkeyPos2[0] << ", " << MonkeyPos2[1] << std::endl;
			
			for(int i = 0; i != 2; i++)//Fix Edge Collision
				if(MonkeyPos[i] == 2)
					MonkeyPos[i] = 0;
			
			MoveWindow(FindWindow(NULL, "Monkey"), MonkeyPos[0], MonkeyPos[1], 854, 480, false);// "ConST chAR * Is iNcomPatIBl-" SHUT THE FUCK UP YES IT IS
			
			
			
			//system("cmdow Monkey /mov 10 10");
			
			//system(MOVE MONKEY HERE, change pos in if's);
	
	}
	
	return 0;
	
}