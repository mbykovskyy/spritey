---
layout: post
title: Sprite Organiser - Strategy Pattern
categories:
- Research and Design
tags:
- sprite
- packer
- sheet
- strategy
- pattern
- organiser
---

Before I can start on the user story <a href="http://www.pivotaltracker.com/story/show/4144362" target="_blank">#4144362</a> I have to spend a little bit of time looking at the story <a href="http://www.pivotaltracker.com/story/show/4144404" target="_blank">#4144404</a>. In particular this part,
<blockquote>Arrangement algorithms should be easily swapped in and out,  but should not necessarily be visible to the user.</blockquote>

Strategy pattern is an obvious choice for easy algorithm switching. I'm going to introduce a sprite organiser, which is effectively a sprite sheet packer that organises sprites according to the slected algorithm/strategy. The initial or default strategy will simply stack sprites one after another similar to flow layout.

I need to add a new story for default sprite arrangement and investigate how the flow layout works.
