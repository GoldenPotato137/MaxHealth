package cn.goldenpotato.maxhealth;

import cn.goldenpotato.maxhealth.Command.CommandManager;
import cn.goldenpotato.maxhealth.Util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class Maxhealth extends JavaPlugin implements Listener
{
    private static Map<UUID, Integer> _playerHealth;

    @Override
    public void onEnable()
    {
        Bukkit.getPluginManager().registerEvents(this, this);
        //register commands
        Objects.requireNonNull(getCommand("mh")).setExecutor(new CommandManager());

        _playerHealth = new HashMap<>();
        Load();
    }

    @Override
    public void onDisable()
    {
        Save();
    }

    private void Load()
    {
        JsonArray array = JsonUtil.LoadJsonArray("playerHealth.json", getDataFolder());
        for (JsonElement element : array)
        {
            UUID uuid = UUID.fromString(element.getAsJsonObject().get("uuid").getAsString());
            int health = element.getAsJsonObject().get("health").getAsInt();
            _playerHealth.put(uuid, health);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static int GetMaxHealth(UUID uuid)
    {
        if (!_playerHealth.containsKey(uuid))
            _playerHealth.put(uuid, (int) Bukkit.getPlayer(uuid).getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
        return _playerHealth.get(uuid);
    }

    public static void SetMaxHealth(UUID uuid, int health)
    {
        Player target = Bukkit.getPlayer(uuid);
        if (target == null) return;
        _playerHealth.put(uuid, health);
        Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
    }

    private void Save()
    {
        JsonArray result = new JsonArray();
        for (UUID uuid : _playerHealth.keySet())
        {
            JsonObject object = new JsonObject();
            object.addProperty("uuid", uuid.toString());
            object.addProperty("health", _playerHealth.get(uuid));
            result.add(object);
        }
        JsonUtil.SaveJson("playerHealth.json", getDataFolder(), result);
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e)
    {
        SetMaxHealth(e.getPlayer().getUniqueId(), GetMaxHealth(e.getPlayer().getUniqueId()));
    }
}
