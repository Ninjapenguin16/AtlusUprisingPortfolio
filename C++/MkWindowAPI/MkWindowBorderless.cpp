#ifndef UNICODE
#define UNICODE
#endif 

#include <windows.h>
#include <time.h>
#include <iostream>
#include <thread>
#include <chrono>

int OffsetX = 0;
int OffsetY = 0;
bool NeedPaint = 0;
int xPos = 300;
int yPos = 400;
char KeyPressed = 0;
char LastKey = 1;
RECT SquareRect;

void DrawFilledRectangle(HDC hdc, int x, int y, int w, int h, int r, int g, int b){ // hdc, LeftSidePos, TopSidePos, Width, Height, red, green, blue

    SquareRect = {x, y, x + w,  y + h};

    HBRUSH hBrush = CreateSolidBrush(RGB(r, g, b));

    FillRect(hdc, &SquareRect, hBrush);

    DeleteObject(hBrush);

}

void PaintWindow(HWND hwnd){
	PAINTSTRUCT ps;
	HDC hdc = BeginPaint(hwnd, &ps);

	// All painting occurs here, between BeginPaint and EndPaint.

	FillRect(hdc, &ps.rcPaint, (HBRUSH) (COLOR_WINDOW+2));
				
	// Get the size of the window's client area
	RECT clientRect;
	GetClientRect(hwnd, &clientRect);

    DrawFilledRectangle(hdc, xPos, yPos, 50, 50, 3, 182, 252);

    DrawFilledRectangle(hdc, clientRect.left, clientRect.bottom - 50, clientRect.right, clientRect.bottom, 11, 153, 68);
				
    /*
	int x = 50;
	int y = 50;
	for (int i = 0; i < 256; i++){
		COLORREF color = RGB(255, i, 0); // Red color (R=255, G=0, B=0)
		SetPixel(hdc, x+i+OffsetX, y+OffsetY, color);
	}
    */
				
	EndPaint(hwnd, &ps);
}


void NormalLoop(){
    while(true){

        if(KeyPressed != LastKey)
            std::cout << KeyPressed;


        LastKey = KeyPressed;

        

        std::this_thread::sleep_for(std::chrono::milliseconds(10));
    }
}


LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam);

int WINAPI wWinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, PWSTR pCmdLine, int nCmdShow){
    // Register the window class.
    const wchar_t CLASS_NAME[]  = L"Sample Window Class";
    
    WNDCLASS wc = { };

    wc.lpfnWndProc   = WindowProc;
    wc.hInstance     = hInstance;
    wc.lpszClassName = CLASS_NAME;

    RegisterClass(&wc);

    // Create the window.

    //HWND hwnd = CreateWindowEx(WS_EX_TOOLWINDOW, _T("MyWindowClass"), _T("My Window"), WS_POPUP, 100, 100, 400, 300, NULL, NULL, wc.hInstance, NULL);


    HWND hwnd = CreateWindowEx(
        0,                              // Optional window styles.
        CLASS_NAME,                     // Window class
        L"Cool Window",                 // Window text
        WS_POPUP,                    // Window style

        // Size and position
        CW_USEDEFAULT, CW_USEDEFAULT, CW_USEDEFAULT, CW_USEDEFAULT,

        NULL,       // Parent window    
        NULL,       // Menu
        hInstance,  // Instance handle
        NULL        // Additional application data
        );

    if(hwnd == NULL){
        return 0;
    }

    ShowWindow(hwnd, nCmdShow);

    std::thread LoopThread(NormalLoop);

    // Run the message loop.

    MSG msg = { };
    while(GetMessage(&msg, NULL, 0, 0) > 0){ // Checks for message
        TranslateMessage(&msg); // Passes windows' message to the WindowProc callback
        DispatchMessage(&msg);
    }

    return 0;
}


LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam){ // Funny Windows Messages: https://learn.microsoft.com/en-us/windows/win32/winmsg/about-messages-and-message-queues#system-defined-messages
    switch (uMsg){
		case WM_DESTROY: // Allows Windows to force close the window in case of disaster
			PostQuitMessage(0);
		return 0;
		
		case WM_CLOSE: // Asks user if they really wanna close the program
			if (MessageBox(hwnd, L"Fr Bro?", L"Cool Window", MB_YESNO) == IDYES){
				DestroyWindow(hwnd);
			}
		// Else: User canceled. Do nothing.
		return 0;

        case WM_SIZE:
            InvalidateRect(hwnd, NULL, TRUE);
        return 0;

		case WM_PAINT: //Remember, this is only done when Windows tells the program to do it, so mainly when resizing happens
            InvalidateRect(hwnd, NULL, TRUE);
			PaintWindow(hwnd);
		return 0;

        case WM_KEYDOWN:
            KeyPressed = wParam;
        return 0;

        case WM_MOUSEMOVE:
            xPos = LOWORD(lParam); 
            yPos = HIWORD(lParam);
            InvalidateRect(hwnd, &SquareRect, TRUE);
        return 0;

    }
    return DefWindowProc(hwnd, uMsg, wParam, lParam);
}