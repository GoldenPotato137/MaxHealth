package cn.goldenpotato.maxhealth.Command;

import cn.goldenpotato.maxhealth.Command.SubCommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter
{
    public static ArrayList<SubCommand> subCommands;

    public CommandManager()
    {
        subCommands = new ArrayList<>();
        subCommands.add(new Add());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        if(args.length==0)
        {
//            Util.Message(sender,Message.SubCommand_Help_Usage);
            return true;
        }
        for (SubCommand subCommand : subCommands)
            if(subCommand.name.equals(args[0]))
            {
                if(!sender.hasPermission(subCommand.permission))
                    sender.sendMessage("§cYou don't have permission to use this command!");
                else
                    subCommand.onCommand((Player) sender, Arrays.copyOfRange(args,1,args.length));
                return true;
            }
        sender.sendMessage("§cUnknown subcommand!");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args)
    {
        if(!(sender instanceof Player))
            return null;
        if(args.length==1)
        {
            List<String> result = new ArrayList<>();
            for (SubCommand subCommand : subCommands)
                if(sender.hasPermission(subCommand.permission))
                    result.add(subCommand.name);
            return result;
        }
        for (SubCommand subCommand : subCommands)
            if(subCommand.name.equals(args[0]) && sender.hasPermission(subCommand.permission))
                return subCommand.onTab((Player) sender, Arrays.copyOfRange(args,1,args.length));
        return null;
    }
}