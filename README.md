#SlackMC
Link [Slack](https://slack.com) to Minecraft!  

##Features
* Exchange messages between Minecraft and Slack
* Console log, chat log, join/quit log, and remote console built in
* Extend SlackMC with your own plugins using our API
* Exempt specified commands from being sent to Slack
* Filter players with permissions from Slack

##Download
BukkitDev download  
**Note:** Take this BukkitDev labeling with caution. There is [some](https://www.reddit.com/r/admincraft/comments/2jx5wr/psa_bukkitdev_should_no_longer_be_considered_safe/) [skeptism](https://www.reddit.com/r/admincraft/comments/2loa3n/psa_bukkitdev_is_definitely_not_safe/) [about](https://www.reddit.com/r/admincraft/comments/2kg8jb/goto_w_and_you_why_bukkitdev_has_halted_approvals/) the [effectiveness](https://www.reddit.com/r/admincraft/comments/2mbtow/malicious_code_in_your_plugins_welcome_to/) of it. You can take my word that SlackMC can be trusted, but I would encourage you to check the source yourself, or if you can't read code, check to see if a developer trusts this code. You might also want to compile it yourself or get someone you trust to compile it for you.

Stable download

Latest download

##Installation
1. Put the plugin .jar file to your plugins folder.
2. Go to https://my.slack.com/services/new/incoming-webhook.
3. Select the channel or user you want SlackMC messages to go by default.
4. Click the green Add Incoming WebHooks Integration button below. A green "New Integration added!" message should appear.
6. Navigate down to "Your Unique Webhook URL". Copy this URL. It should look like https://company.slack.com/services/hooks/incoming-webhook?token=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.
7. You can customize the default name and icon. This will be rarely needed because SlackMC will usually set its own name and icon.
8. If you decide to change these settings, scroll to the bottom of the page and click the blue Save Integration button.
9. Paste the webhook URL into the webhook for the default channel. You'll learn more about making more than one channel in a further tutorial, but setting up the default channel should be enough for most users.
10. Setup the config and channel options as you like, you can refer to the configuration guide if you need more guidance about this.
11. If you modified the config while the server was on, do /slack reload. If the server was off, you can start it and your new config will take effect. It does not matter either way.

_developing..._
