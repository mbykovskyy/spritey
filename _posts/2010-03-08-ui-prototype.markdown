---
layout: post
title: UI Prototype
categories:
- Research and Design
tags:
- ui
- prototype
- mockup
- screen
- wireframe
- sketcher
- sprite
- packer
- wireframesketcher
- spritepacker
- image
- texture
- imagepacker
---

I've put together a quick UI prototype using [WireframeSketcher][wireframe_sketcher] developed by [Peter Severin][peter_severin]. WireframeSketcher is an Eclipse plugin for creating screen mockups. It has a rich library of UI components which lets you create prototypes really fast. It is also very easy to create custom widgets by combining various components together. What I also like about this tool is its clean interface and the fact that it's part of Eclipse which means no switching between applications and mockups are saved within a project. It might not be a big deal but for me it's very important to have everything in one place. I would like to thank Peter for kindly giving me a free open-source developer license and I would like to encourage anyone looking for such tool to check it out. This tool is amazing.

![][main_screen]

So, there going to be two sprite sheet views:

- Canvas (positioned on the left) - will render spites how they will actually appear in the exported sheet.
- Sprite List/Tree (positioned top-right) - will group sprites by their classes.

There will also be three sprite views:

- Graphical - will be rendered to the canvas.
- Sprite List Item - will only render sprite class value/text in the Sprite List.
- Sprite Properties (positioned bottom-right) - will display sprite attributes that can be changed manually.

[wireframe_sketcher]: http://wireframesketcher.com  "Wireframe Sketcher"
[peter_severin]: http://wireframesketcher.com/about.html  "Peter Severin"
[main_screen]: {{site.baseurl}}/assets/images/2010/03/MainScreen.png "Main Screen"
