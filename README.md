# minecraft_banplugin
A Simple Ban Plugin for Minecraft

Bans/Unban an Player with his uuid.
If a Player is banned, he can't join the Server and the Login Screen will show the Reason and the Ban Time.

### SETUP

1. The Plugin will create a Folder /database with db.proerties and a Folder /config with text.properties on startup.
2. Put your url in the db.properties file. The link will look like **jdbc:mysql://hostname:port/database**
3. Put Your MySql Credentials in the db.properties file. 
4. If a Connection to the Database is successfull a Table will be created.
5. To change the Text of the Plugin, open the text.properties and rename the default values.

### Commands
/ban-own-uuid: uuid time_in_seconds[default = permanent] reason...

bans a uuid of the Server and set the ban Time with the ban Reason.
If you want to update the ban, use the command again.

param:

uuid(required): the Players uuid

time_in_second(optional): the ban time, if not a correct time, the player will be banned permanent

reason(optional): enter the Ban Reason, if not present the default Ban Reason will be shown.

/unban-own-uuid: uuid

unbans a uuid of the server, the Player with this uuid can join the Server again.

param:

uuid(required): The Players uuid

### Events

If the Player trys to Login, the PreLoginEvent will be triggerd. There is a validation, if the player is banned or not.

After a Player makes a successfull Login, his userdata will be saved in a mysql database

