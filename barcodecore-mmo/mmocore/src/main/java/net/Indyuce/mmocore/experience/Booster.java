package net.Indyuce.mmocore.experience;

import java.util.Objects;
import java.util.UUID;

public class Booster {
	private final UUID uuid = UUID.randomUUID();
	private final long date = System.currentTimeMillis();
	private final Profession profession;
	private final double extra;
	private final String author;

	/**
	 * Length is not final because boosters can stacks. This allows to reduce
	 * the amount of boosters displayed in the main player menu
	 * 
	 * See {@link net.Indyuce.mmocore.manager.social.BoosterManager#register(Booster)}
	 */
	private long length;

	/**
	 * @param extra
	 *            1 for +100% experience, 3 for 300% etc.
	 * @param length
	 *            Booster length in milliseconds
	 */
	public Booster(double extra, long length) {
		this(null, null, extra, length);
	}

	/**
	 * Main class experience booster
	 *
	 * @param author The booster creator
	 * @param extra  1 for +100% experience, 3 for 300% etc.
	 * @param length Booster length in milliseconds
	 */
	public Booster(String author, double extra, long length) {
		this(author, null, extra, length);
	}

	/**
	 * Profession experience booster
	 *
	 * @param author
	 *            The booster creator
	 * @param profession
	 *            Either null for main level boosters or a specific profession
	 * @param extra
	 *            1 for +100% experience, 3 for 300% etc.
	 * @param length
	 *            Booster length in milliseconds
	 */
	public Booster(String author, Profession profession, double extra, long length) {
		this.author = author;
		this.length = length * 1000;
		this.profession = profession;
		this.extra = extra;
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public double getExtra() {
		return extra;
	}

	public boolean hasAuthor() {
		return author != null;
	}

	public String getAuthor() {
		return author;
	}

	public long getCreationDate() {
		return date;
	}

	public boolean hasProfession() {
		return profession != null;
	}

	public Profession getProfession() {
		return profession;
	}

	public boolean isTimedOut() {
		return date + length < System.currentTimeMillis();
	}

	public long getLeft() {
		return Math.max(0, date + length - System.currentTimeMillis());
	}

	public long getLength() {
		return length;
	}

	public void addLength(long length) {
		this.length += length;
	}

	public boolean canStackWith(Booster booster) {
		return extra == booster.extra && Objects.equals(profession, booster.profession);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Booster booster = (Booster) o;
		return Objects.equals(uuid, booster.uuid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}
}
