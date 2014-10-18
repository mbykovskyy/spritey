---
layout: post
status: publish
published: true
title: Tools and Technologies
author:
  display_name: Max
  login: admin
  email: maksym@bykovskyy.com
  url: http://bykovskyy.com
author_login: admin
author_email: maksym@bykovskyy.com
author_url: http://bykovskyy.com
wordpress_id: 73
wordpress_url: http://bykovskyy.com/spritepacker/blog/?p=73
date: '2010-03-09 14:07:36 -0800'
date_gmt: '2010-03-09 14:07:36 -0800'
categories:
- Research and Design
tags:
- eclipse
- rcp
- gef
- jai
- xml
- vtd
- java
---
<p>I just realized that I forgot to mention the programming language and technologies I'm going to use in this project so here's a quick update just about that.</p>
<p>This tool will be written in Java on top of <a href="http://wiki.eclipse.org/index.php/Rich_Client_Platform" target="_blank">Eclipse Rich Client Platform (RCP)</a>. Eclipse RCP is an excellent framework for fast application development. I'm quite familiar with it since I'm actually working on Eclipse-based tools at work.</p>
<p>For sprite sheet editing I'm going to use <a href="http://www.eclipse.org/gef" target="_blank">Eclipse Graphical Editing Framework (GEF)</a> which provides many features for creating  editors for visual editing of arbitrary models. This framework will only be used for rendering and editing sprite sheets. The loading of various image formats will be implemented with Java Advanced Imaging API, but of course at the beginning, to keep things simple,  I will use Java's native ImageReader. Might even stick with it if I get lazy! ;)</p>
<p>For XML parsing I'm going to use <a href="http://vtd-xml.sourceforge.net" target="_blank">VTD-XML</a>. I was introduced to this parser at work. I've never used it but it is claimed to be the best XML parser there is! Check out its benchmark pages, stats are pretty impressive.</p>
