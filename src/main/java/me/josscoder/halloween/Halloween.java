package me.josscoder.halloween;

import cn.nukkit.Player;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockPumpkin;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class Halloween extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        getServer().getScheduler().scheduleRepeatingTask(this::handleWorldsTime, 20);
        getServer().getScheduler().scheduleRepeatingTask(this::handleScaryEffects, 20 * 60);

        getServer().getPluginManager().registerEvents(this, this);
    }

    private String getRandomDahmerPhrase() {
        return dahmerPhrases.get(ThreadLocalRandom.current().nextInt(dahmerPhrases.size()));
    }

    private final List<String> dahmerPhrases = Arrays.asList(
            "I bite",
            "I have to start eating more at home...",
            "I took it too far, that's for sure",
            "I no longer wanted to continue killing to be left alone with his corpse",
            "My dominant lust was to experience their bodies. I saw them as objects, as strangers. It's hard for me to believe that a human being could have done what I have done",
            "I don't even know if I have the capacity to feel normal emotions or not, because I haven't cried for a long time. You suffocate them for so long that you may lose them, at least partially. I do not know",
            "When I was a child, I was like any other person",
            "I don't care if I live or die. go ahead kill me",
            "I should have gone to college, gone into real estate and gotten an aquarium, that's what I should have done",
            "Yes, I have regrets, but I'm not even sure if it's as deep as it should be. I've always wondered why I don't feel more remorse",
            "Looking back on my life, I know that I have made others suffer as much as I have suffered.",
            "Somehow I think I wanted it to end, even if it meant my own destruction.",
            "Yes, I always had the feeling that I was wrong. I don't think anyone can kill someone and think it's right",
            "I decided that I was never going to get married because I never wanted to go through something like that",
            "I created my fantasy life more powerful than the real one",
            "I'd cook it, look at the pictures and jerk off",
            "It's hard for me to believe that a human being could have done what I've done, but I know I did.",
            "The ideas keep spinning over and over in my mind and they don't go away.",
            "If they killed me in jail, that would be a blessing now"
            //Taken and translated from https://www.psicopatas.es/frases-jeffrey-dahmer/#:~:text=A%20continuaci%C3%B3n%2C%20las%20%EE%80%80frases%20de%20Jeffrey%20Dahmer%EE%80%81%20que,seguir%20matando%20para%20quedarme%20solo%20con%20su%20cad%C3%A1ver%C2%BB.
    );

    private void handleWorldsTime() {
        getServer().getLevels().values().forEach(level -> {
            if (level != null) level.setTime(Level.TIME_MIDNIGHT);
        });
    }

    private void handleScaryEffects() {
        int rand = ThreadLocalRandom.current().nextInt(1, 4);

        switch (rand) {
            case 1:
                getServer().broadcastMessage("§eHerobrine joined the game");
                getServer().getScheduler().scheduleDelayedTask(() -> players(player -> {
                    player.getLevel().addSound(player, Sound.MOB_ENDERMEN_SCREAM, 1, 1, player);
                    sendEffect(player, Effect.BLINDNESS);
                    player.attack(0);
                }), 20 * 5);
                break;
            case 2:
                players(player -> {
                    player.getLevel().addSound(player, Sound.AMBIENT_WEATHER_LIGHTNING_IMPACT, 1, 1, player);
                    player.sendMessage("§cJeffry Dahmer: §e" + getRandomDahmerPhrase());
                    sendEffect(player, Effect.BLINDNESS);
                    player.attack(0);
                });
                break;
            case 3:
                players(player -> {
                    sendEffect(player, Effect.CONFUSION);
                    player.sendToast("Hearts in the fridge", "You have collected 5 hearts");
                });
                break;
            case 4:
                players(player -> {
                    player.getInventory().setHelmet(new BlockPumpkin().toItem());
                    sendEffect(player, Effect.SLOWNESS);
                    sendEffect(player, Effect.NAUSEA);
                });
                getServer().getScheduler().scheduleDelayedTask(() -> players(player -> player.getInventory().setHelmet(new BlockAir().toItem())), 20 * 5);
                //more soon
                break;
        }
    }

    private void sendEffect(Player player, int effectID) {
        player.addEffect(Effect.getEffect(effectID)
                .setAmplifier(5)
                .setDuration(20 * 5)
                .setVisible(false)
        );
    }

    private void players(Consumer<Player> action) {
        getServer().getOnlinePlayers().values().forEach(action);
    }
}
