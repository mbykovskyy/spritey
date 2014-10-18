---
layout: post
status: publish
published: true
title: Sprite Organiser - Strategy Pattern
author:
  display_name: Max
  login: admin
  email: maksym@bykovskyy.com
  url: http://bykovskyy.com
author_login: admin
author_email: maksym@bykovskyy.com
author_url: http://bykovskyy.com
wordpress_id: 360
wordpress_url: http://bykovskyy.com/spritepacker/blog/?p=360
date: '2010-07-22 10:48:22 -0700'
date_gmt: '2010-07-22 10:48:22 -0700'
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
<p>Before I can start on the user story <a href="http://www.pivotaltracker.com/story/show/4144362" target="_blank">#4144362</a> I have to spend a little bit of time looking at the story <a href="http://www.pivotaltracker.com/story/show/4144404" target="_blank">#4144404</a>. In particular this part,</p>
<blockquote><p>Arrangement algorithms should be easily swapped in and out,  but should not necessarily be visible to the user.</p></blockquote>
<p>Strategy pattern is an obvious choice for easy algorithm switching. I'm going to introduce a sprite organiser, which is effectively a sprite sheet packer that organises sprites according to the slected algorithm/strategy. The initial or default strategy will simply stack sprites one after another similar to flow layout.</p>
<p>I need to add a new story for default sprite arrangement and investigate how the flow layout works.</p>
