package net.Indyuce.mmoitems.stat.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.apache.commons.lang.Validate;

import com.google.gson.JsonArray;

import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.Mergeable;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringListData implements StatData, RandomStatData, Mergeable {
	@NotNull private List<String> list;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StringListData)) { return false; }
		if (((StringListData) obj).getList().size() != getList().size()) { return false; }
		return SilentNumbers.hasAll(((StringListData) obj).getList(), getList());
	}

	public StringListData() {
		this(new ArrayList<>());
	}

	public StringListData(@NotNull String[] array) {
		this(Arrays.asList(array));
	}

	public StringListData(@NotNull JsonArray array) {
		this();

		array.forEach(str -> list.add(str.getAsString()));
	}

	public StringListData(@NotNull List<String> list) {
		this.list = list;
	}

	@NotNull public List<String> getList() {
		return list;
	}

	@Override
	public StatData randomize(MMOItemBuilder builder) {
		return new StringListData(new ArrayList<>(list));
	}

	@Override
	public void merge(StatData data) {
		Validate.isTrue(data instanceof StringListData, "Cannot merge two different stat data types");
		list.addAll(((StringListData) data).list);
	}

	@Override
	@NotNull
	public StatData cloneData() { return new StringListData(new ArrayList<>(getList())); }

	@Override
	public boolean isClear() { return getList().size() == 0; }

	@Override
	public String toString() {

		StringBuilder b = new StringBuilder("\u00a77");
		for (String str : getList()) {
			if (b.length() > 0) { b.append("\u00a78;\u00a77 "); }
			b.append(str);
		}

		return b.toString();
	}

	/**
	 * @param str Entry to remove
	 *
	 * @return If the value was actually removed. If it wasn't there
	 * 		   in the first place, this will return false.
	 */
	public boolean remove(@Nullable String str) {

		if (!list.contains(str)) { return false; }

		if (removeGuarantee) {

			// Remove that sh
			return list.remove(str);
		} else {

			// OK
			try {
				return list.remove(str);

			} catch (UnsupportedOperationException ignored) {

				list = new ArrayList<>(list);
				removeGuarantee = true;
				return list.remove(str);
			}
		}
	}
	boolean removeGuarantee = false;
}
