include("autorun/sh_CustomAddon.lua") -- Includes the shared lua file

hook.Add("HUDPaint", "CustomAddonHUD", function() -- Paints stuff to the HUD every frame

    local CustomAddon_Player = LocalPlayer() -- Sets the player object to the local player
    local CustomAddon_scrw, CustomAddon_scrh = ScrW(), ScrH() -- Gets the client's resolution
    local CustomAddon_backw = CustomAddon_scrw * 0.25 -- Width of the black box
    local CustomAddon_backh = CustomAddon_scrh * 0.07 -- Height of the black box
    local CustomAddon_redMaxw = CustomAddon_scrw * 0.12 -- Distance away from the center
    local CustomAddon_HealthPercent = CustomAddon_Player:Health() / CustomAddon_Player:GetMaxHealth() -- Finds the players health percentage
        
    surface.SetDrawColor(0, 0, 0, 255) -- Sets draw color to black

    surface.DrawRect(CustomAddon_scrw * 0.375, CustomAddon_scrh * 0.9, CustomAddon_backw, CustomAddon_backh) -- Draws the black rectangle

    surface.SetDrawColor(255, 0, 0, 255) -- Sets draw color to red
        
    surface.DrawRect(CustomAddon_scrw * 0.5 - CustomAddon_redMaxw * CustomAddon_HealthPercent, CustomAddon_scrh * 0.91, CustomAddon_redMaxw * 2 * CustomAddon_HealthPercent, CustomAddon_scrh * 0.05) -- Dynamically draws the centered red rectangle by health

end)
