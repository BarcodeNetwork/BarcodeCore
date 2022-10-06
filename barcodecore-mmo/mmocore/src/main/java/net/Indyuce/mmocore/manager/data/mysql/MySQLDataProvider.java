package net.Indyuce.mmocore.manager.data.mysql;

import io.lumine.mythic.lib.sql.MMODataSource;
import net.Indyuce.mmocore.manager.data.DataProvider;
import net.Indyuce.mmocore.manager.data.PlayerDataManager;
import org.bukkit.configuration.file.FileConfiguration;

public class MySQLDataProvider extends MMODataSource implements DataProvider {
	private final MySQLPlayerDataManager playerManager = new MySQLPlayerDataManager(this);
	
	public MySQLDataProvider(FileConfiguration config) {
		this.setup(config);
	}

	@Override
	public void load() {
		executeUpdateAsync(
			"CREATE TABLE IF NOT EXISTS mmocore_playerdata(uuid VARCHAR(36),class_points "
			+ "INT(11) DEFAULT 0,skill_points INT(11) DEFAULT 0,attribute_points INT(11) "
			+ "DEFAULT 0,attribute_realloc_points INT(11) DEFAULT 0,level INT(11) DEFAULT 1,"
			+ "experience INT(11) DEFAULT 0,class VARCHAR(20),guild VARCHAR(20),last_login LONG,"
			+ "attributes LONGTEXT,professions LONGTEXT,quests LONGTEXT,waypoints LONGTEXT,"
			+ "friends LONGTEXT,skills LONGTEXT,bound_skills LONGTEXT,class_info LONGTEXT,PRIMARY KEY (uuid));");
	}

	@Override
	public PlayerDataManager getDataManager() {
		return playerManager;
	}
}
