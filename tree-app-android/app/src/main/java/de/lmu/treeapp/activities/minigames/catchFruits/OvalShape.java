package de.lmu.treeapp.activities.minigames.catchFruits;

import android.graphics.Path;
import android.graphics.RectF;

import me.samlss.bloom.shape.ParticleShape;

public class OvalShape extends ParticleShape {
    /**
     * Construct the shape of particle.
     *
     * @param centerX The center x coordinate of the particle.
     * @param centerY The center y coordinate of the particle.
     * @param radius  The radius of the particle.
     */
    public OvalShape(float centerX, float centerY, float radius) {
        super(centerX, centerY, radius);
    }


    @Override
    public void generateShape(Path path) {
        path.addOval(new RectF(getCenterX() - getRadius(), getCenterY() - getRadius(), getCenterX() + getRadius(),
                getCenterY() + getRadius()), Path.Direction.CW);
    }
}
