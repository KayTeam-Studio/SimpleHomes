/*
 * Copyright (C) 2021  SirOswaldo
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.kayteam.simplehomes.home;

public class Home {

    // Basic information
    private final String owner;
    private final String name;
    private boolean isPublic = false;
    private double cost = 0.0;

    // Preview information
    private String displayName;

    // Home information
    private String world;
    private double x, y, z;
    private float yaw, pitch;


    public Home(String owner, String name) {
        this.owner = owner;
        this.name = name;
        displayName = "&6" + name;
    }

    public String getOwner() {
        return owner;
    }
    public String getName() {
        return name;
    }
    public boolean isPublic() {
        return isPublic;
    }
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getWorld() {
        return world;
    }
    public void setWorld(String world) {
        this.world = world;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }
    public float getYaw() {
        return yaw;
    }
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
    public float getPitch() {
        return pitch;
    }
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}