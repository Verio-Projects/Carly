package rip.skyland.carly.profile.punishments;

import com.google.gson.JsonObject;

import java.util.UUID;

public interface IPunishment {

    /**
     * @return the reason of the punishment
     */
    String getReason();

    /**
     * @return the reason why the punishment was undone
     */
    String getUnpunishReason();

    /**
     * @return the name of the punisher
     */

    String getPunisher();

    /**
     * @return the uuid of the punishment
     */
    UUID getUuid();

    /**
     * @return the uuid of the punished
     */
    UUID getTargetUuid();

    /**
     * @return the type of the punishment
     */
    PunishmentType getPunishmentType();

    /**
     * @return the state of the punishment
     */
    boolean isActive();

    /**
     *
     * sets the activity of the punishment
     *
     * @param val the activity
     */
    void setActive(boolean val);

    /**
     *
     * sets the reason why the player was unpunished
     *
     * @param reason the reason
     */
    void setUnpunishReason(String reason);

    /**
     * @return the date on which the punishment was issued
     */
    long getPunishDate();

    /**
     * @return the json object of a punishment object
     */
    JsonObject toJson();

}
