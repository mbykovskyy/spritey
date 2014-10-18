---
layout: post
title: More User Stories
categories:
- Uncategorized
tags:
- sprite
- user story
- css
- tree
---

0. A user can create a deep hierachy of sprites. For example, a user can create a sprite group called "menu" and add another sub-group called "buttons":

		GROUP : menu
			SPRITE : background.png
			GROUP : buttons
				SPRITE : default.png
				SPRITE : pressed.png

0. A user can move a group of sprites to another group by selecting a group and dragging it over onto a target group in a sprite tree.
0. Sprite sheet can be exported to CSS. A user can use this CSS to display sprites on a web page using background positioning.
