/*
 * Copyright 2015 Pravian Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.pravian.aero.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerList implements Iterable<Player> {

    private final List<UUID> uuids = new ArrayList<UUID>();

    public PlayerList() {
    }

    @Override
    public Iterator<Player> iterator() {
        removeGone();
        return new Iterator<Player>() {
            final Iterator<UUID> currentUuids = uuids.iterator();

            @Override
            public boolean hasNext() {
                return currentUuids.hasNext();
            }

            @Override
            public Player next() {
                return getPlayer(currentUuids.next());
            }

            @Override
            public void remove() {
                currentUuids.remove();
            }
        };
    }
    public void add(Player member) {
        uuids.add(member.getUniqueId());
    }

    public void remove(Player member) {
        uuids.remove(member.getUniqueId());
    }

    public List<UUID> getMembers() {
        removeGone();
        return Collections.unmodifiableList(uuids);
    }

    public List<Player> getPlayerMembers() {
        removeGone();
        List<Player> playerMembers = new ArrayList<Player>();
        for (UUID member : uuids) {
            playerMembers.add(getPlayer(member));
        }
        return playerMembers;
    }

    public int size() {
        int size = 0;
        for (UUID uuid : uuids) {
            size += getPlayer(uuid) == null ? 0 : 1;
        }
        return size;
    }

    public void clear() {
        uuids.clear();
    }

    private Player getPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    private void removeGone() {
        Iterator<UUID> it = uuids.iterator();
        while (it.hasNext()) {
            if (getPlayer(it.next()) == null) {
                it.remove();
            }
        }
    }


}