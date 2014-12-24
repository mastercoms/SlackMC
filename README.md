Slack
===========

Spigot/CraftBukkit/Bukkit/BungeeCord/Glowstone plugin for [Slack](https://slack.com)

##Features
* Send chat messages and commands to Slack
* Send login and quit messages to Slack
* Uses minecraft username and avatar as bot information
* Blacklist players or commands from being sent to Slack
* Use permissions to block messages to Slack
* API to send custom messages
* BungeeCord support
* See the console (coming soon)
* Formatting (coming soon)
* Send commands from Slack (coming soon)
* Sponge (coming soon)

##Download
[Stable builds](http://dev.bukkit.org/bukkit-plugins/slack/files/)

[Dev builds](https://github.com/CircuitSoftGroup/SlackBukkit/releases)

##Installation
1. Drop the plugin in your server folder.
2. Create a new incoming webhook and set it up however you would like.
3. Start and stop the server.
4. Copy the webhook URL and set webhook: in the config.yml to that.
5. Start the server.


Verified compatible with CraftBukkit, Spigot, Spigot/CraftBukkit 1.8, and Glowstone. Definitely works with any Bukkit API server, probably.

###Slack setup
1. Log into your Slack account and go to https://my.slack.com/services/new/incoming-webhook
2. Click on the Add New Integration link on the left-hand side.
3. Scroll down and select Incoming Webhooks.
4. Select a channel/direct message recipient and click the green Add Incoming WebHook button.
5. A green "New Integration added!" confirmation message will appear. Scroll down the page and copy the URL under "Your Unique Webhook URL." It will look something like this: https://yourcompany.slack.com/services/hooks/incoming-webhook?token=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
6. Under the Integration Settings section, you have the option to label your integration, change the name of your Slack bot and its icon. By default, the bot name is incoming-webhook and the default icon is a Sprintly icon. These are optional settings.
7. Scroll down to the bottom of the page and click the blue Save Integration button.

##Commands
**/slack** - The main slack command and command for all subcommands. Return help
_permission: slack.command_

###Subcommands
**/slack send** - Sends a command to slack. Read the custom messages section for details.

**/slack reload**- Reloads Slack's config. Does not reload the plugin.

##Configuration
**version** - the plugin's current version. Do not touch this.
**debug** - whether to post HTTP response codes to console
**webhook** - the incoming webhook URL for slack.
**use-perms** - whether to use permissions or not (for sending to Slack)
**use-blacklist** - whether to use the command blacklist or not
**blacklist** - list of commands you don't want to be sent to slack.

##Permissions
**slack.hide.command** - _does not post commands you do to Slack._
default: no one

**slack.command** - _allows you to do **/slack** and all sub commands_
default: op

**slack.hide.logout** - _does not post to Slack when you login._
default: no one

**slack.hide.chat** - _does not post your chats to Slack._
default: no one

##Custom Messages
On the server, you may use
**/slack send <username> <image URL> <message>**
to send a custom message to Slack.
You can use **null** as image URL to use the username's minecraft head skin.

Programmatically, you can add the plugin as a dependency, and then import the API for the platform you're using (either Bukkit or BungeeCord)
```java
import us.circuitsoft.slack.api.BukkitPoster
```
or
```java
import us.circuitsoft.slack.api.BungeePoster
```

If you're using Bukkit,
```java
new BukkitPoster(message, name, iconUrl).runTaskAsynchronously(this);
```
where message is the message you want to send to Slack, name is the username, and iconUrl is the image URL. You can set iconUrl to null if name is a Minecraft player username, it will then use the player's skin head.

If you're using BungeeCord, 
```java
getProxy().getScheduler().runAsync(this, new BungeePoster(essage, name, iconUrl));
```
Same parameters as in the Bukkit version.

##Support
For support questions on how to use the plugin and troubleshooting, post a comment so if I am not available, other people can help you. Explain your problem and use the latest version before asking for help.

For bug reports, please post an issue on Github. Just make sure to explain the problem, how to reproduce it, and make sure you are using the latest version.

If you get an error, please post it to https://gist.github.com/ and then post the URL here.

If you have a feature request, PM me, or code it yourself and pull request it on Github.

##Donate to me
[Give me money with PayPal](https://www.paypal.com/cgi-bin/webscr?return=https%3A%2F%2Fgithub.com%2FCircuitSoftGroup%2FSlackMC%2F&cn=Add+special+instructions+to+the+addon+author%28s%29&business=circuitsoft%40outlook.com&bn=PP-DonationsBF%3Abtn_donateCC_LG.gif%3ANonHosted&cancel_return=https%3A%2F%2Fgithub.com%2FCircuitSoftGroup%2FSlackMC%2F&lc=US&item_name=Slack+%28from+GitHub.com%29&cmd=_donations&rm=1&no_shipping=1&currency_code=USD)

##Bukkit Dev
http://dev.bukkit.org/bukkit-plugins/slack/
