package de.darthweiter.banplugin.thread;

import de.darthweiter.banplugin.database.Database;

public class UpdateBanWithUUIDTask implements Runnable {

    private final String uuid;

    public UpdateBanWithUUIDTask(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public void run() {
        Database.updateBanInfosWithUUID(uuid, null, null, false, false, null);
    }
}
