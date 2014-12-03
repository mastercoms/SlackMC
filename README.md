Slack
===========

Spigot/CraftBukkit/Bukkit/BungeeCord plugin for [Slack](https://slack.com)

##Features
* Send chat messages and commands to Slack
* Send login and quit messages to Slack
* Uses minecraft username and avatar as bot information
* Blacklist players or commands from being sent to Slack
* Use permissions to block messages to Slack
* API to send custom events (coming soon)
* BungeeCord support (coming soon)
* See the console (coming soon)
* Send commands from Slack (coming soon)

##Download
[Stable builds](http://dev.bukkit.org/bukkit-plugins/slack/files/)  

[Dev builds](https://github.com/CircuitSoftGroup/SlackBukkit/releases)

##Installation
1. Drop the plugin in your server folder.
2. Create a new incoming webhook and set it up however you would like.
3. Start and stop the server.
4. Copy the webhook URL and set webhook: in the config.yml to that.
5. Start the server.

Verified compatible with CraftBukkit, Spigot, Spigot/CraftBukkit 1.8, and Glowstone. Probably works with any Bukkit API server.

##Commands
**/slack** - reloads the config
_permission: slack.reload_

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

**slack.reload** - _allows you to reload the plugin's config using the command **/slack**_  
default: op  

**slack.hide.login** - /_does not post to Slack when you login._  
default: no one  

**slack.hide.logout** - _does not post to Slack when you login._  
default: no one  

**slack.hide.chat** - _does not post your chats to Slack._  
default: no one  

##API
**(not implemented yet)**
Just 
```java
import static us.circuitsoft.slack.send
```
into your plugin and add the plugin as a dependency. 

The method to send a message to Slack is send(). Javadocs are included in the plugin about parameters. 

The method returns true if the message was successfully sent to Slack, so you might want to do something if it returns false.

##Support
For support questions on how to use the plugin and troubleshooting, post a comment so if I am not available, other people can help you. Explain your problem and use the latest version before asking for help.

For bug reports, please post an issue on Github. Just make sure to explain the problem, how to reproduce it, and make sure you are using the latest version.

If you get an error, please post it to https://gist.github.com/ and then post the URL here.

If you have a feature request, PM me, or code it yourself and pull request it on Github.

##Give Me Money
[Give me money with PayPal](https://www.paypal.com/cgi-bin/webscr?return=https%3A%2F%2Fgithub.com%2FCircuitSoftGroup%2FSlackMC%2F&cn=Add+special+instructions+to+the+addon+author%28s%29&business=circuitsoft%40outlook.com&bn=PP-DonationsBF%3Abtn_donateCC_LG.gif%3ANonHosted&cancel_return=https%3A%2F%2Fgithub.com%2FCircuitSoftGroup%2FSlackMC%2F&lc=US&item_name=Slack+%28from+GitHub.com%29&cmd=_donations&rm=1&no_shipping=1&currency_code=USD)

##Bukkit Dev
http://dev.bukkit.org/bukkit-plugins/slack/
