package cn.goldenpotato.maxhealth.Command.SubCommands;

import cn.goldenpotato.maxhealth.Command.SubCommand;
import cn.goldenpotato.maxhealth.Maxhealth;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Add extends SubCommand
{
    private static final String _usage = "§cUsage: /mh add [player] [delta]:给一名玩家的血量上限加上delta（可以为负值）";
    public Add()
    {
        super("add","mh.admin" ,_usage);
    }

    @Override
    public void onCommand(Player player, String[] args)
    {
        if(args.length<2)
        {
            player.sendMessage(_usage);
            return;
        }
        int delta;
        try
        {
            delta = Integer.parseInt(args[1]);
        } catch (NumberFormatException e)
        {
            player.sendMessage("§cInvalid number!");
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target==null)
        {
            player.sendMessage("§cPlayer not found!");
            return;
        }
//        player.sendMessage(target.getDisplayName() + " " + args[0]);
        int maxHealth = Maxhealth.GetMaxHealth(target.getUniqueId()) + delta;
        Maxhealth.SetMaxHealth(target.getUniqueId(),maxHealth);
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        List<String> result = new ArrayList<>();
        if(args.length==1)
            for(Player p : Bukkit.getOnlinePlayers())
                result.add(p.getName());
        else if (args.length==2)
            result.add("1");
        return result;
    }
}
