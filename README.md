# 🥧 PieMotd - Minecraft server MOTD plugin



A powerful dynamic MOTD, server icon and welcome title plugin designed for Paper 1.20.4+ servers



## ✨ Functional features



### 🌈 dynamic RGB MOTD

- Supports MiniMessage format (RGB color, gradient, rainbow color)

- Dynamic variable replacement

- Multi-MOTD rotation display



### 🖼️ smart server avatar

- Automatic avatar rotation

- Multi-format support (PNG, JPG)

- 64x64 pixel verification

![Display variables, minimessage support and dynamic server icon function](https://cdn.modrinth.com/data/cached_images/db5860eccd27f967c9b51a3525f788780d817a79.png)
_Display variables, minimessage support and dynamic server icon function_


### 🎉 welcome to log in

- Customize titles and subtitles

- Welcome sound effects and messages

![Welcome titles that support minimassage colors and variables. This means you don't need to use cumbersome command blocks!](https://cdn.modrinth.com/data/cached_images/df54a98c758c840de46ec939563444b643c26b10.png)
_Welcome titles that support minimassage colors and variables. This means you don't need to use cumbersome command blocks!_

## 📥 install



Place 'PieMotd.jar' in the 'plugins' folder of the server

2. Restart the server

3. Modify ` plugins/PieMotd/config. Yml ` configuration file

4. Use '/piemotd reload' to overload the configuration



## 🎨 MiniMessage format



Support all MiniMessage tags:



- ** Color ** : '<red>', '<#FF0000>

- ** gradient ** : '<gradient:red:blue> text </gradient

- ** rainbow ** : '<rainbow> text </rainbow>

- ** Format ** : '<bold>', '<italic>', '<underlined>



## 🔧 available variables



Server variables

- '%online%' - Number of online players

- '%max_players%' - Maximum number of players

- '%tps%' - Server TPS

- '%time%' - current time

- '%date%' - Current date

- '%memory_used%' - Used memory

- '%memory_max%' - Maximum memory



Player Variables (Titles only)

- '%player%' - Player name

- '%player_count%' - Number of online players



## 💻 command list



- '/piemotd reload' - Reload plugin configuration

- '/piemotd preview' - Preview the current MOTD

- '/piemotd help' - Show help



## 🛠️ permission node



- 'piemotd.reload' - Overload configuration permissions

- 'piemotd.admin' - Administrator privileges (including all permissions)



## ❓ frequently asked questions



Q: The avatar is not displayed? **

A: Ensure that the image is in 64x64 pixel PNG or JPG format, 128x128 is not supported. Make sure to correctly position the image file (including the file suffix) in the cfg.



Q: The color doesn't work? **

A: Check if the MiniMessage format is correct and make sure to use the '<#FFFFFF>' format
