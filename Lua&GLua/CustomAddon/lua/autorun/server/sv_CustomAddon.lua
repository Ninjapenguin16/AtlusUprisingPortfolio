include("autorun/sh_CustomAddon.lua") -- Includes the shared lua file

hook.Add("PlayerSay", "CustomAddon_ChatCommands", function(sender, text, teamChat)

    if(text == "Fuck") then

        sender:ChatPrint("You just lost flashlight privilages")

        sender:Flashlight(false)

        sender:AllowFlashlight(false)
    
    elseif(text == "Sorry") then

        sender:ChatPrint("Ok you can have it back")

        sender:AllowFlashlight(true)

    end

end)


