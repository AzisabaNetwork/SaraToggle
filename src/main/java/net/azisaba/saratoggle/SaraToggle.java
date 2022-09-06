package net.azisaba.saratoggle;

import net.azisaba.azipluginmessaging.api.AziPluginMessaging;
import net.azisaba.azipluginmessaging.api.AziPluginMessagingProvider;
import net.azisaba.azipluginmessaging.api.protocol.Protocol;
import net.azisaba.azipluginmessaging.api.protocol.message.PlayerWithServerMessage;
import net.azisaba.azipluginmessaging.api.protocol.message.ProxyboundToggleSaraHideMessage;
import net.azisaba.azipluginmessaging.api.protocol.message.ProxyboundToggleSaraShowMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class SaraToggle extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Objects.requireNonNull(Bukkit.getPluginCommand("gamingsara")).setExecutor((sender, command, label, args) -> {
            AziPluginMessaging api = AziPluginMessagingProvider.get();
            Protocol.P_TOGGLE_GAMING_SARA.sendPacket(
                    api.getServer().getPacketSender(),
                    new PlayerWithServerMessage(api.getPlayerAdapter(Player.class).get((Player) sender)));
            return true;
        });
        Objects.requireNonNull(Bukkit.getPluginCommand("sara")).setExecutor((sender, command, label, args) -> {
            Set<Long> groups = Arrays.stream(args).map(Long::parseLong).collect(Collectors.toSet());
            AziPluginMessaging api = AziPluginMessagingProvider.get();
            if (getConfig().getBoolean("use-rank", false)) {
                Protocol.P_TOGGLE_SARA_SHOW.sendPacket(
                        api.getServer().getPacketSender(),
                        new ProxyboundToggleSaraShowMessage(api.getPlayerAdapter(Player.class).get((Player) sender), groups));
            } else {
                Protocol.P_TOGGLE_SARA_HIDE.sendPacket(
                        api.getServer().getPacketSender(),
                        new ProxyboundToggleSaraHideMessage(api.getPlayerAdapter(Player.class).get((Player) sender), groups));
            }
            return true;
        });
    }
}
