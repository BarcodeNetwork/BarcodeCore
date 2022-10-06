package net.Indyuce.mmocore.api.util.math;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class Line3D {

	private final Vector dir;
	private final Vector pt;

	public Line3D(Vector loc1, Vector loc2) {
		pt = loc1.clone();
		dir = loc1.clone().subtract(loc2);
	}

	public Line3D(Location pt, Vector dir) {
		this.dir = dir.clone();
		this.pt = pt.toVector();
	}

	public Line3D(double a, double b, double c, double e, double f, double g) {
		pt = new Vector(a, b, c);
		dir = new Vector(e, f, g);
	}

	public double distanceSquared(Entity entity) {
		return distanceSquared(entity.getLocation().add(0, entity.getHeight() / 2, 0).toVector());
	}

	public double distanceSquared(Vector loc) {

		// dir = (alpha ; beta ; gamma)
		// LINE : (x ; y ; z) = (a ; b ; c) + t * dir
		// loc = (e ; f ; g)

		// therefore, distance vector is:
		// ( a + t * alpha - e ; b + t * beta - f ; c + t * gamma - g )

		// length of vector is:
		// d = sqrt( (a-e)� + (b-f)� + (c-g)� + 2t( alpha(a-e) + beta(b-f) +
		// gamma(c-g) ) + t�(alpha� + beta� + gamma�) )

		// analysis: we find the value of t for which the distance d is the
		// smallest. (canonical form) axis of symetry is min = -b/2a therefore

		// we don't care about calculating the 0 degree constant of polynomial

		double a = dir.lengthSquared();
		double b = 2 * (dir.getX() * (pt.getX() - loc.getX()) + dir.getY() * (pt.getY() - loc.getY()) + dir.getZ() * (pt.getZ() - loc.getZ()));
		double min = -b / (2 * a);

		return loc.distanceSquared(getPoint(min));
	}

	public double distance(Vector loc) {
		return Math.sqrt(distanceSquared(loc));
	}

	public Vector getPoint(double t) {
		return pt.clone().add(dir.clone().multiply(t));
	}
}
