package io.georgeous.mcgenerations.systems.surrogate;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public class SurrogateEntity{

    private Villager entity;
    private Player followTarget;

    public SurrogateEntity(Player followTarget, String fullname){
        entity = (Villager) followTarget.getWorld().spawnEntity(followTarget.getLocation(), EntityType.VILLAGER);
        entity.setAI(false);
        entity.setCollidable(false);
        entity.setAge(-1);
        entity.setAgeLock(true);
        entity.setCustomNameVisible(true);
        entity.addScoreboardTag("surrogate");
        entity.setInvulnerable(true);
        entity.setSilent(true);
        entity.setCustomName(fullname);

        makeNonCollidable(followTarget);
        this.followTarget = followTarget;
    }

    private void makeNonCollidable(Player player){
        //Scoreboard sb = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        Scoreboard sb = player.getScoreboard();
        if(sb == null)
            return;

        Team noCollisionTeam = sb.getTeam("nocollision");
        if(noCollisionTeam == null){
            noCollisionTeam = sb.registerNewTeam("nocollision");
        }

        noCollisionTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        noCollisionTeam.addEntry(entity.getUniqueId().toString());
    }

    public void update(){
        PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY, 10, 1, false, false, false);
        followTarget.addPotionEffect(invis);
        Vector dir = followTarget.getLocation().getDirection().setY(0).multiply(0);
        Location pos = followTarget.getLocation().add(0, 0.0, 0);
        entity.teleport(pos.add(dir));
    }

    public void remove(){
        Location loc = new Location(entity.getWorld(), entity.getLocation().getX(), 0, entity.getLocation().getZ());
        entity.teleport(loc);
        entity.remove();
    }

    public Villager getEntity(){
        return this.entity;
    }
}
