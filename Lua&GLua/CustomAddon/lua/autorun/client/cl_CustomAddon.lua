include("autorun/sh_CustomAddon.lua") -- Includes the shared lua file

hook.Add("HUDPaint", "CustomAddonHUD", function() -- Paints stuff to the HUD every frame

    local CustomAddon_Player = LocalPlayer() -- Sets the player object to the local player
    local CustomAddon_scrw, CustomAddon_scrh = ScrW(), ScrH() -- Gets the client's resolution
    local CustomAddon_backw = CustomAddon_scrw * 0.25 -- Width of the black box
    local CustomAddon_backh = CustomAddon_scrh * 0.05 -- Height of the black box
    local CustomAddon_redMaxw = CustomAddon_scrw * 0.12 -- Distance away from the center
    local CustomAddon_HealthPercent = CustomAddon_Player:Health() / CustomAddon_Player:GetMaxHealth() -- Finds the players health percentage
    local CustomAddon_TrueHealthPercent = 1
    
    if(CustomAddon_HealthPercent > 1) then
        CustomAddon_TrueHealthPercent = 1
    else
        CustomAddon_TrueHealthPercent = CustomAddon_HealthPercent
    end
        
    surface.SetDrawColor(0, 0, 0, 255) -- Sets draw color to black

    surface.DrawRect(CustomAddon_scrw * 0.375, CustomAddon_scrh * 0.93, CustomAddon_backw, CustomAddon_backh) -- Draws the black rectangle

    surface.SetDrawColor(255, 0, 0, 255) -- Sets draw color to red
        
    surface.DrawRect(CustomAddon_scrw * 0.5 - CustomAddon_redMaxw * CustomAddon_TrueHealthPercent, CustomAddon_scrh * 0.94, CustomAddon_redMaxw * 2 * CustomAddon_TrueHealthPercent, CustomAddon_scrh * 0.03) -- Dynamically draws the centered red rectangle by health

    surface.SetTextColor(255, 255, 255) -- Sets color to white

    surface.SetFont("Trebuchet24")

    if(CustomAddon_HealthPercent < 0) then -- Prevents negative numbers
        CustomAddon_HealthPercent = 0
    end

    local CustomAddon_HPText = "" .. CustomAddon_HealthPercent * 100 .. "%" -- Creates the percent string
    
    local CustomAddon_HPTextw, CustomAddon_HPTexth = surface.GetTextSize(CustomAddon_HPText) -- Find the size of the new text
    
    surface.SetTextPos(CustomAddon_scrw * 0.5 - CustomAddon_HPTextw / 2, CustomAddon_scrh * 0.955 - CustomAddon_HPTexth / 2) -- Puts text in right place

	surface.DrawText(CustomAddon_HPText) -- Draws the text

    


end)
