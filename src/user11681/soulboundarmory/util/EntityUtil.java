package user11681.soulboundarmory.util;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import user11681.soulboundarmory.asm.access.entity.BossEntityAccess;

public class EntityUtil {
    public static double speed(Entity entity) {
        Vec3d velocity = entity.getVelocity();

        return Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y + velocity.z * velocity.z);
    }

    public static Entity entity(UUID id) {
        for (ServerWorld world : Util.server().getWorlds()) {
            Entity entity = world.getEntity(id);

            if (entity != null) {
                return entity;
            }
        }

        return null;
    }

    public static boolean isBoss(Entity entity) {
        return ((BossEntityAccess) entity).isBoss();
    }
}