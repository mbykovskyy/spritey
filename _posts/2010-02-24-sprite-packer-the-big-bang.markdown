---
layout: post
title: Sprite Packer - The Big Bang
categories:
- Research and Design
tags:
- sprite
- packer
- spritepacker
- spritesheet
- image
- sheet
- texture
- imagepacker
---

I'm so excited to announce that today I'm officially starting my first (and hopefully not last ;)) ever blog for my new project.

As I was researching for my next simple 2D game I compiled a short list of tools that I thought I will need to make a complete game in short time. Sprite packer was one of them.

There are already a number of sprite packing software lying around the Internet that I could use. Namely, the two packers that I just have to mention for their efficient packing capabilities are the [Image Packer][blitz_packer] from Blitz and the [Sprite Sheet Packer][sprite_packer] developed by Nick Gravelyn, who was also looking for a tool for making sprite sheets and at the end decided to write his own.

While two of them are really good, especially the Sprite Sheet Packer, I felt it would be a great exercise for me to create my own packer that should give the user more control over sprite properties. Such as, defining sprite classes and subclasses (or types/names whatever you wanna call it), manual arrangement of sprites as well as auto-packing capabilities.

Another reason for doing this project is to use "best coding practices" such as coding to interfaces and [TDD][tdd] (just to name the few), and see exactly how much overhead is assosiated with applying them? I'm sure that the benefit of using such practices will, at the end, outweigh the overhead but still I would like to see how much time I will spend on applying each practice.

[blitz_packer]: http://www.blitzbasic.com/Community/posts.php?topic=30518  "Blitz Image Packer"
[sprite_packer]: http://spritesheetpacker.codeplex.com  "Sprite Sheet Packer"
[tdd]: http://en.wikipedia.org/wiki/Test-driven_development  "TDD"
