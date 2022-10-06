package net.Indyuce.mmocore.api.util.math;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class VectorRotation {
	private final float yaw, pitch;

	public VectorRotation(Location source) {
		this.yaw = source.getYaw();
		this.pitch = source.getPitch();
	}

	public Vector rotate(Vector vec) {
		vec = rotateX(vec, this.pitch / 180 * Math.PI);
		vec = rotateY(vec, -this.yaw / 180 * Math.PI);
		return vec;
	}

	private Vector rotateX(Vector vec, double a) {
		double y = vec.getY() * Math.cos(a) - vec.getZ() * Math.sin(a);
		double z = vec.getY() * Math.sin(a) + vec.getZ() * Math.cos(a);
		return vec.setY(y).setZ(z);
	}

	private Vector rotateY(Vector vec, double b) {
		double x = vec.getX() * Math.cos(b) + vec.getZ() * Math.sin(b);
		double z = vec.getX() * -Math.sin(b) + vec.getZ() * Math.cos(b);
		return vec.setX(x).setZ(z);
	}

	// private Vector rotateZ(Vector vec, double c) {
	// double x = vec.getX() * Math.cos(c) - vec.getY() * Math.sin(c);
	// double y = vec.getX() * Math.sin(c) + vec.getY() * Math.cos(c);
	// return vec.setX(x).setY(y);
	// }
}
