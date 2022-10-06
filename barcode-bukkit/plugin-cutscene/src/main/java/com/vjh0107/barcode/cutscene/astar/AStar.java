package com.vjh0107.barcode.cutscene.astar;

import com.vjh0107.barcode.cutscene.exceptions.BlockedPathException;
import com.vjh0107.barcode.cutscene.utils.CutsceneUtils;
import com.vjh0107.barcode.cutscene.utils.VersionDetector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.Gate;

import java.util.*;

import static com.vjh0107.barcode.cutscene.utils.CutsceneUtils.isMovable;

@SuppressWarnings("deprecation")
public class AStar {

	private final int sx, sy, sz, ex, ey, ez;
	private final World w;

	private PathingResult result;

	private HashMap<String, Tile> open = new HashMap<String, Tile>();
	private HashMap<String, Tile> closed = new HashMap<String, Tile>();

	public static ArrayList<Location> handle(Location start, Location end, final int range){
		try {
			if(start.getBlock().getType()==Material.AIR) {
				for(int y = start.getBlockY(); y > 0; y--) {
					Location test = start.clone();
					test.setY(y);
					if(test.getBlock().getType()!=Material.AIR) {
						start = test;
						break;
					}
				}
			}
			if(end.getBlock().getType()==Material.AIR) {
				for(int y = end.getBlockY(); y > 0; y--) {
					Location test = end.clone();
					test.setY(y);
					if(test.getBlock().getType()!=Material.AIR) {
						end = test;
						break;
					}
				}
			}
	        AStar path = new AStar(start, end, range);
	        ArrayList<Tile> route = path.iterate();
	        PathingResult result = path.getPathingResult();
	 
	        try {
	        switch(result){
	        case SUCCESS : 
	        	ArrayList<Location> locs = new ArrayList<Location>();
	    	    for(Tile t : route){    	
	    	    	locs.add(inc(t.getLocation(start),0.5,0,0.5));
	    	    }
	    	    return locs;
	        case NO_PATH :
	            break;
	        }
	        }catch(Exception e) {
	        	
	        }
	    } catch (Exception e) {
	    }
		return new ArrayList<Location>();
	}
	public static Location inc(Location l, double x, double y, double z) {
		return l.clone().add(x,y,z);
	}
	
	private void addToOpenedList(Tile t, boolean modify) {
		if (open.containsKey(t.getUID())) {
			if (modify) {
				open.put(t.getUID(), t);
			}
		} else {
			open.put(t.getUID(), t);
		}
	}

	private void addToClosedList(Tile t) {
		if (!closed.containsKey(t.getUID())) {
			closed.put(t.getUID(), t);
		}
	}

	private final int range;
	private final String endUID;

	public AStar(Location start, Location end, int range) throws BlockedPathException {

		count = 0;

		var isStartLocationWalkable = CutsceneUtils.isWalkable(start);
		var isEndLocationWalkable = CutsceneUtils.isWalkable(end);

		if (!isStartLocationWalkable || !isEndLocationWalkable) {
			throw new BlockedPathException(isStartLocationWalkable, isEndLocationWalkable);
		}

		this.w = start.getWorld();
		this.sx = start.getBlockX();
		this.sy = start.getBlockY();
		this.sz = start.getBlockZ();
		this.ex = end.getBlockX();
		this.ey = end.getBlockY();
		this.ez = end.getBlockZ();

		this.range = range;

		short sh = 0;
		Tile t = new Tile(sh, sh, sh, null);
		t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
		this.open.put(t.getUID(), t);
		this.processAdjacentTiles(t);

		StringBuilder b = new StringBuilder();
		b.append(ex - sx).append(ey - sy).append(ez - sz);
		this.endUID = b.toString();
	}

	public Location getEndLocation() {
		return new Location(w, ex, ey, ez);
	}

	public PathingResult getPathingResult() {
		return this.result;
	}

	boolean checkOnce = false;
	
	private int abs(int i) {
		return (i < 0 ? -i : i);
	}

	public ArrayList<Tile> iterate() {

		if (!checkOnce) {
			// invert the boolean flag
			checkOnce ^= true;
			if((abs(sx - ex) > range) || (abs(sy - ey) > range) || (abs(sz - ez) > range)){
				this.result = PathingResult.NO_PATH;
				return null;//jump out
			}
		}
		// while not at end
		Tile current = null;

		while (canContinue()) {

			// get lowest F cost square on open list
			current = this.getLowestFTile();

			// process tiles
			this.processAdjacentTiles(current);
		}

		if (this.result != PathingResult.SUCCESS) {
			return null;
		} else {
			// path found
			LinkedList<Tile> routeTrace = new LinkedList<Tile>();
			Tile parent;

			routeTrace.add(current);

			while ((parent = current.getParent()) != null) {
				routeTrace.add(parent);
				current = parent;
			}

			Collections.reverse(routeTrace);

			return new ArrayList<Tile>(routeTrace);
		}
	}

	int count = 0;
	
	private boolean canContinue() {
		if(count > 1000){
			return false;
		}
		
		count++;
		// check if open list is empty, if it is no path has been found
		if (open.size() == 0) {
			this.result = PathingResult.NO_PATH;
			return false;
		} else {
			if (closed.containsKey(this.endUID)) {
				this.result = PathingResult.SUCCESS;
				return false;
			} else {
				return true;
			}
		}
	}

	private Tile getLowestFTile() {
		double f = 0;
		Tile drop = null;

		// get lowest F cost square
		for (Tile t : open.values()) {
			if (f == 0) {
				t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
				f = t.getF();
				drop = t;
			} else {
				t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
				double posF = t.getF();
				if (posF < f) {
					f = posF;
					drop = t;
				}
			}
		}

		// drop from open list and add to closed

		this.open.remove(drop.getUID());
		this.addToClosedList(drop);

		return drop;
	}

	private boolean isOnClosedList(Tile t) {
		return closed.containsKey(t.getUID());
	}

	// pass in the current tile as the parent
	private void processAdjacentTiles(Tile current) {

		// set of possible walk to locations adjacent to current tile
		HashSet<Tile> possible = new HashSet<Tile>(26);

		for (byte x = -1; x <= 1; x++) {
			for (byte y = -1; y <= 1; y++) {
				for (byte z = -1; z <= 1; z++) {

					if (x == 0 && y == 0 && z == 0) {
						continue;// don't check current square
					}

					Tile t = new Tile((short) (current.getX() + x), (short) (current.getY() + y), (short) (current.getZ() + z), current);

					if (!t.isInRange(this.range)) {
						// if block is out of bounds continue
						continue;
					}

					if (x != 0 && z != 0 && (y == 0 || y == 1)) {
						// check to stop jumping through diagonal blocks
						Tile xOff = new Tile((short) (current.getX() + x), (short) (current.getY() + y), (short) (current.getZ()), current), zOff = new Tile((short) (current.getX()),
								(short) (current.getY() + y), (short) (current.getZ() + z), current);
						if (!this.isTileWalkable(xOff) && !this.isTileWalkable(zOff)) {
							continue;
						}
					}

					if (this.isOnClosedList(t)) {
						// ignore tile
						continue;
					}

					// only process the tile if it can be walked on
					if (this.isTileWalkable(t)) {
						t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
						possible.add(t);
					}

				}
			}
		}

		for (Tile t : possible) {
			// get the reference of the object in the array
			Tile openRef = null;
			if ((openRef = this.isOnOpenList(t)) == null) {
				// not on open list, so add
				this.addToOpenedList(t, false);
			} else {
				// is on open list, check if path to that square is better using
				// G cost
				if (t.getG() < openRef.getG()) {
					// if current path is better, change parent
					openRef.setParent(current);
					// force updates of F, G and H values.
					openRef.calculateBoth(sx, sy, sz, ex, ey, ez, true);
				}

			}
		}

	}

	private Tile isOnOpenList(Tile t) {
		return (open.containsKey(t.getUID()) ? open.get(t.getUID()) : null);
		/*
		 * for (Tile o : open) { if (o.equals(t)) { return o; } } return null;
		 */
	}

	@SuppressWarnings("deprecation")
	private boolean isTileWalkable(Tile t) {
		Location l = new Location(w, (sx + t.getX()), (sy + t.getY()), (sz + t.getZ()));
		Block b = l.getBlock();
		Material i = b.getType();

		// lava, fire, wheat and ladders cannot be walked on, and of course air
		// 85, 107 and 113 stops npcs climbing fences and fence gates
		if (i != Material.LAVA && i != Material.WATER &&i != Material.FIRE && i != Material.WHEAT && !CutsceneUtils.isMovable(i) && !i.toString().contains("FENCE")) {
			// make sure the blocks above are air

			var mayFence = b.getRelative(0, 1, 0).getType();
			if (Tag.FENCE_GATES.isTagged(mayFence)) {
				// fench gate check, if closed continue
				Gate g = new Gate(b.getRelative(0, 1, 0).getData());
				return (g.isOpen() && (b.getRelative(0, 2, 0).getType().isAir()));
			}
			return (CutsceneUtils.isMovable(b.getRelative(0, 1, 0).getType()) && b.getRelative(0, 2, 0).getType() == Material.AIR);

		} else {
			return false;
		}
	}

    public class Tile {

    	// as offset from starting point
    	private final short x, y, z;

    	private double g = -1, h = -1;

    	private Tile parent = null;
    
    	private final String uid;

    	public Tile(short x, short y, short z, Tile parent) {
    		this.x = x;
    		this.y = y;
    		this.z = z;
    		this.parent = parent;
    
    		StringBuilder b = new StringBuilder();
    		b.append(x);
    		b.append(y);
    		b.append(z);
    		uid = b.toString();
    
    	}
	
    	public boolean isInRange(int range){
    		return ((range - abs(x) >= 0) && (range - abs(y) >= 0) && (range - abs(z) >= 0));
    	}

    	public void setParent(Tile parent) {
    		this.parent = parent;
    	}

    	public Location getLocation(Location start) {
    		return new Location(start.getWorld(), start.getBlockX() + x, start.getBlockY() + y, start.getBlockZ() + z);
	    }

        public Tile getParent() {
	    	return this.parent;
	    }

    	public short getX() {
    		return x;
    	}

    	public int getX(Location i) {
    		return (i.getBlockX() + x);
    	}

    	public short getY() {
    		return y;
    	}

    	public int getY(Location i) {
    		return (i.getBlockY() + y);
    	}

    	public short getZ() {
    		return z;
    	}

    	public int getZ(Location i) {
    		return (i.getBlockZ() + z);
    	}

	    public String getUID() {
		    return this.uid;
	    }

	    public boolean equals(Tile t) {
		    return (t.getX() == x && t.getY() == y && t.getZ() == z);
	    }

	    public void calculateBoth(int sx, int sy, int sz, int ex, int ey, int ez, boolean update) {
	    	this.calculateG(sx, sy, sz, update);
	    	this.calculateH(sx, sy, sz, ex, ey, ez, update);
	    }

	    public void calculateH(int sx, int sy, int sz, int ex, int ey, int ez, boolean update) {
	    	// only update if h hasn't been calculated or if forced
	    	if ((!update && h == -1) || update) {
	    		int hx = sx + x, hy = sy + y, hz = sz + z;
	    		this.h = this.getEuclideanDistance(hx, hy, hz, ex, ey, ez);
	    	}
	    }

	    // G = the movement cost to move from the starting point A to a given square
	    // on the grid, following the path generated to get there.
	    public void calculateG(int sx, int sy, int sz, boolean update) {
    		if ((!update && g == -1) || update) {
    			// only update if g hasn't been calculated or if forced
    			Tile currentParent = this.getParent(), currentTile = this;
    			int gCost = 0;
    			// follow path back to start
    			while ((currentParent = currentTile.getParent()) != null) {

    				int dx = currentTile.getX() - currentParent.getX(), dy = currentTile.getY() - currentParent.getY(), dz = currentTile.getZ() - currentParent.getZ();
    				dx = abs(dx);
    				dy = abs(dy);
    				dz = abs(dz);

				    if (dx == 1 && dy == 1 && dz == 1) {
				    	gCost += 1.7;
				    } else if (((dx == 1 || dz == 1) && dy == 1) || ((dx == 1 || dz == 1) && dy == 0)) {
				    	gCost += 1.4;
				    } else {
				    	gCost += 1.0;
				    }
				    // move backwards a tile
				    currentTile = currentParent;
			    }
			    this.g = gCost;
		    }
	    }

	    public double getG() {
	    	return g;
	    }

    	public double getH() {
    		return h;
    	}

    	public double getF() {
    		// f = h + g
    		return (h + g);
    	}

    	private double getEuclideanDistance(int sx, int sy, int sz, int ex, int ey, int ez) {
    		double dx = sx - ex, dy = sy - ey, dz = sz - ez;
    		return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    	}

    	private int abs(int i) {
    		return (i < 0 ? -i : i);
    	}

    }
    
    public enum PathingResult {
	
	    SUCCESS(0),
	    NO_PATH(-1);

	    private final int ec;
	
	    PathingResult(int ec){
	    	this.ec = ec;
	    }
	
	    public int getEndCode(){
	    	return this.ec;
	    }

    }

}